package chromedino.display.objects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

import chromedino.ChromeDino;
import chromedino.constants.Constants;
import chromedino.display.GameScreen;
import chromedino.display.objects.enemies.model.Enemy;
import chromedino.neuralnetwork.NeuralNetwork;
import chromedino.utils.Animation;
import chromedino.utils.Utils;

public class Dinosaur {

	/* Dinosaur constants */

	public static final int LAND_POSY = 627;
	public static float GRAVITY = 0.35f;

	private static final int NORMAL_RUN = 0;
	private static final int JUMPING = 1;
	private static final int DOWN_RUN = 2;
	private static final int DEATH = 3;
	private static final int AIRPLANE = 4;

	/* Neural Network Config */

	private final int MIN_WEIGHT = -1;
	private final int MAX_WEIGHT = 1;

	private final int GENOMES_PER_GENERATION = 3;
	private final int[] NEURONS_AMOUNT = { 6, 6, 3 };

	private final double MUTATION_PROBABILITY = 0.7;

	/* Variables */

	private float posY;
	private float posX;

	private float speedY;
	private Rectangle rectBound;

	public int id, score = 0;

	private int state = NORMAL_RUN;

	private Animation normalRunAnim;
	private BufferedImage jumping;
	private Animation downRunAnim;
	private BufferedImage deathImage;

	private AirPlane airPlane;

	private Color randomColor;
	private boolean alive = true;

	private NeuralNetwork neuralNetwork;

	private boolean playAsHuman = false;

	public int getScore() {
		return score;
	}

	public Dinosaur(GameScreen gameScreen, boolean playAsHuman) {
		id = gameScreen.getDinosaurs().size();

		this.neuralNetwork = new NeuralNetwork(id, NEURONS_AMOUNT, GENOMES_PER_GENERATION, MUTATION_PROBABILITY, MIN_WEIGHT, MAX_WEIGHT);

		this.playAsHuman = playAsHuman;
		
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
			float red = rand.nextFloat(), green = rand.nextFloat(), blue = rand.nextFloat();

			randomColor = new Color(red, green, blue);
		}

	
		posY = LAND_POSY;
		rectBound = new Rectangle();
		normalRunAnim = new Animation(90);
		normalRunAnim.addFrame(Utils.changeColor(new File("data/dinosaur/dino0.bmp"), randomColor));
		normalRunAnim.addFrame(Utils.changeColor(new File("data/dinosaur/dino1.bmp"), randomColor));
		jumping = Utils.changeColor(new File("data/dinosaur/dino4.bmp"), randomColor);
		downRunAnim = new Animation(90);
		downRunAnim.addFrame(Utils.changeColor(new File("data/dinosaur/dino2.bmp"), randomColor));
		downRunAnim.addFrame(Utils.changeColor(new File("data/dinosaur/dino3.bmp"), randomColor));
		deathImage = Utils.changeColor(new File("data/dinosaur/dino7.bmp"), randomColor);

		airPlane = new AirPlane(Constants.SCREEN_WIDTH, this);
	}

	public void setAlive(boolean alive) {
		this.state = alive ? NORMAL_RUN : DEATH;
		this.alive = alive;
	}

	public boolean isAlive() {
		return alive;
	}

	public int getId() {
		return id;
	}

	public AirPlane getAirPlane() {
		return airPlane;
	}

	public NeuralNetwork getNeuralNetwork() {
		return neuralNetwork;
	}

	public float getPosX() {
		return posX;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public void setPosX(float f) {
		this.posX = f;
	}

	public void draw(Graphics g) {
		if (!alive)
			return;

		switch (state) {
		case NORMAL_RUN:
			g.drawImage(normalRunAnim.getFrame(), (int) posX, (int) posY, null);
			break;
		case AIRPLANE:
			g.drawImage(normalRunAnim.getFrame(), (int) posX, (int) posY, null);
			break;
		case JUMPING:
			g.drawImage(jumping, (int) posX, (int) posY, null);
			break;
		case DOWN_RUN:
			g.drawImage(downRunAnim.getFrame(), (int) posX, (int) (posY + 20), null);
			break;
		case DEATH:
			g.drawImage(deathImage, (int) posX, (int) posY, null);
			break;
		}
		
		airPlane.draw(g);
	}

	public Object[] getNextEnemy() {
		double smallerDistance = 9999;
		
		if (ChromeDino.getGameScreen().getEnimiesManager().getEnemies().isEmpty()) {
			return new Object[] { null, smallerDistance };
		}

		Enemy enemy = ChromeDino.getGameScreen().getEnimiesManager().getEnemies().get(0);

		for (Enemy e : ChromeDino.getGameScreen().getEnimiesManager().getEnemies()) {
			double currentDistance = new Point((int) posX, (int) posY).distance(new Point((int) e.getBound().getX(), (int) e.getBound().getY()));

			if (currentDistance <= smallerDistance) {
				smallerDistance = currentDistance;
				enemy = e;
			}
		}
		return new Object[] { enemy, smallerDistance };
	}

	public void update() {
		
		if (!playAsHuman) {
			
			
			Object[] nextEnemy = getNextEnemy();

			double distance = (double) nextEnemy[1];

			Enemy enemy = (Enemy) nextEnemy[0];

			if (distance == 9999)
				return;

			neuralNetwork.getOutputs(new double[] { distance, enemy.getBound().getWidth(), enemy.getBound().getHeight(), enemy.getBound().getY(), Constants.SPEED, posY });

			boolean jump = neuralNetwork.neurons[2][0] > 0.5;
			boolean squat = neuralNetwork.neurons[2][1] > 0.5;
			boolean useAirplane = neuralNetwork.neurons[2][2] > 0.5;

			if (useAirplane) {
				down(false);
				airPlane.use();
			}
			
			if (!airPlane.isUsing()) {
				if (jump)
					jump();

				if (squat) 
					down(true);
			} else {
				//if (squat) {
					//airPlane.stopUsing();
				//}
			}
			
			
		}

		normalRunAnim.updateFrame();
		downRunAnim.updateFrame();

		if (posY >= LAND_POSY) {
			posY = LAND_POSY;
			if (!airPlane.isUsing()) {
				if (state != DOWN_RUN) {
					state = NORMAL_RUN;
				}
			} else {
				state = AIRPLANE;
			}
		} else {
			if (!airPlane.isUsing()) {
				speedY += GRAVITY;
				posY += speedY;
			}
		}
	}
	
	public Dinosaur playAsHuman() {
		playAsHuman = !playAsHuman;
		return this;
	}
	
	public boolean isPlayingAsHuman() {
		return playAsHuman;
	}

	public void jump() {
		if (posY >= LAND_POSY) {
			speedY = -8f;
			posY += speedY;
			state = JUMPING;
		}
	}

	public void down(boolean isDown) {
		if (airPlane.isUsing())
			return;
		
		speedY = 4f;
		posY += speedY;
		state = isDown ? DOWN_RUN : NORMAL_RUN;
	}

	public Rectangle getBound() {
		rectBound = new Rectangle();
		if (state == DOWN_RUN) {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY + 20;
			rectBound.width = downRunAnim.getFrame().getWidth() - 10;
			rectBound.height = downRunAnim.getFrame().getHeight();
		} else {
			rectBound.x = (int) posX + 5;
			rectBound.y = (int) posY;
			rectBound.width = normalRunAnim.getFrame().getWidth() - 10;
			rectBound.height = normalRunAnim.getFrame().getHeight();
		}
		return rectBound;
	}

	public void reset() {
		posY = LAND_POSY;
		score = 0;
		airPlane.reset();
		alive = true;
	}

	public void upScore(int up) {
		if (isAlive()) {
			score += up;
			
			if (score > ChromeDino.getGameScreen().getBestScore()) {
				ChromeDino.getGameScreen().bestScore = score;
				ChromeDino.getGameScreen().bestDistance = ChromeDino.getGameScreen().getLand().getScreenWidth();
				ChromeDino.getGameScreen().bestDinosaur = this;
			}
		}
	}

}
