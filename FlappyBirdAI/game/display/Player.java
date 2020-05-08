package game.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.Random;

import constants.Constants;
import game.Game;
import game.display.enemies.EnemiesManager;
import game.display.enemies.entities.Pipe;
import game.display.enemies.model.Enemy;
import maxtercreations.FlappyBird;
import neuralnetwork.NeuralNetwork;
import utils.Animation;
import utils.Utils;

public class Player {

	/* Player constants */

	private static final int LAND_POSY = 60;
	private static float GRAVITY = 0.55f;

	/* Variables */

	private boolean alive = true, playAsHuman, hasJumped;

	private float posY, posX, speedY;

	private int id, score;

	private Rectangle rectBound = new Rectangle();
	private Animation animation = new Animation(90);
	private Color randomColor;
	private NeuralNetwork neuralNetwork;
	private EnemiesManager enemiesManager;

	public Player(Game game, boolean playAsHuman) {
		id = game.getPlayers().size();

		this.neuralNetwork = new NeuralNetwork(new int[] { 4, 4, 1 }, 3, 0.7, -1, 1);
		this.playAsHuman = playAsHuman;

		buildColorAndPosition();

		animation.addFrame(Utils.changeColor(new File("data/sprites/player/bird1.bmp"), randomColor));
		animation.addFrame(Utils.changeColor(new File("data/sprites/player/bird2.bmp"), randomColor));
		animation.addFrame(Utils.changeColor(new File("data/sprites/player/bird3.bmp"), randomColor));
	}

	public void draw(Graphics g) {
		if (!alive)
			return;

		g.drawImage(animation.getFrame(), (int) posX, (int) posY, null);

		if (!Constants.SHOW_COLLISION_AREA)
			return;

		Color color = g.getColor();

		g.setColor(Color.RED);

		for (Enemy e : enemiesManager.getEnemies()) {
			int range = ((Pipe) e).getRange();

			int x = (int) (e.getBound().getX() + (e.getBound().getWidth() / 2) - 5);
			int y = (int) Math.abs(e.getBound().getY() + e.getBound().getHeight() + (30 - range) + range);
			g.fillRect(x, y, 10, 10);
		}

		g.drawLine(0, 630, 1280, 630);
		g.setColor(color);

	}

	public void update() {
		if (!playAsHuman) {

			if (enemiesManager == null)
				enemiesManager = FlappyBird.getGame().getGameScreen().getEnimiesManager();

			Object[] nextEnemy = enemiesManager.getNextEnemy(new Point((int) posX, (int) posY));

			double distance = (double) nextEnemy[1];

			Enemy enemy = (Enemy) nextEnemy[0];

			if (distance == 9999)
				return;

			neuralNetwork.getOutputs(new double[] { distance, posY - enemy.getBound().getY(),
					posX + animation.getFrame().getWidth(), posY + animation.getFrame().getHeight() });

			boolean jump = neuralNetwork.neurons[2][0] > 0.5;

			if (jump)
				jump();
		}

		animation.updateFrame();

		if (!hasJumped)
			return;

		if (posY < Constants.SCREEN_HEIGHT - 90 - animation.getFrame().getHeight()) {
			speedY += GRAVITY;
			posY += speedY;
		} else {
			posY = Constants.SCREEN_HEIGHT - 90;
		}
	}

	public Player playAsHuman() {
		playAsHuman = !playAsHuman;
		return this;
	}

	public boolean isPlayingAsHuman() {
		return playAsHuman;
	}

	public void jump() {
		if (posY < Constants.SCREEN_HEIGHT - animation.getFrame().getHeight() && posY >= (LAND_POSY + 30)) {
			speedY = -5f;
			posY += speedY;
		}
		hasJumped = true;
	}

	public Rectangle getBound() {
		rectBound = new Rectangle();
		rectBound.x = (int) posX + 5;
		rectBound.y = (int) posY;
		rectBound.width = animation.getFrame().getWidth() - 10;
		rectBound.height = animation.getFrame().getHeight();
		return rectBound;
	}

	public void reset() {
		posY = LAND_POSY;
		score = 0;
		alive = true;
		hasJumped = false;
		GRAVITY = 0.45f;
		speedY = GRAVITY;
	}

	public void upScore(int up) {
		if (alive) {
			score += up;

			if (FlappyBird.getGame().getBestPlayer() == null || score > FlappyBird.getGame().getBestPlayer().getScore())
				FlappyBird.getGame().updateBestPlayer(this);
		}
	}

	private void buildColorAndPosition() {
		if (Constants.BATTLE_MODE) {
			if (playAsHuman) {
				randomColor = new Color(255, 57, 57);
				posX = (int) (50 + id * 0.087);
			} else {
				randomColor = new Color(239, 239, 239);
				posX = (int) (200 + id * 0.087);
			}
		} else {
			posX = (int) (13 + id * 0.087);

			Random rand = new Random();

			randomColor = new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat());
		}

		posY = 60 + new Random().nextInt(200);
	}

	public int getScore() {
		return score;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;

		if (!Constants.BATTLE_MODE)
			this.neuralNetwork.newGenome(score);

		FlappyBird.getGame().increaseDeaths();
	}

	public boolean isAlive() {
		return alive;
	}

	public NeuralNetwork getNeuralNetwork() {
		return neuralNetwork;
	}

}
