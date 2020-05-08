package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import constants.Constants;
import constants.Constants.GameState;
import game.display.Player;
import game.display.enemies.EnemiesManager;
import game.display.scenario.Ground;
import game.display.scenario.Landscape;
import utils.Utils;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements Runnable {

	private Ground ground;
	private Landscape landscape;
	private EnemiesManager enemiesManager;
	private Thread thread;

	private BufferedImage replayButtonImage, gameOverButtonImage, logoImage;

	private Game game;

	private long startTime;

	private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

	public GameScreen(Game game) {
		this.game = game;

		setupGameMode();
		initGraphics();
	}

	public void gameUpdate() {

		if (game.getGameState().equals(GameState.GAME_OVER)) {
			try {
				Thread.sleep(500);
				game.setGameState(GameState.PLAYING);
				resetGame();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (game.getGameState().equals(GameState.PLAYING)) {
			game.increaseVelocity();

			landscape.update();
			enemiesManager.update();
			ground.update();

			for (Player player : game.getPlayers()) {
				player.update();

				if (!enemiesManager.isCollision(player) || !player.isAlive())
					continue;

				player.setAlive(false);

				if (game.getDeaths() == Constants.START_POPULATION) {
					ground.reset();
					game.setGameState(GameState.GAME_OVER);
					player.getNeuralNetwork().getSaveLoad().saveToFile(player.getScore());
					break;
				}
			}
		}
	}

	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g.setColor(Color.decode("#f7f7f7"));
		g.fillRect(0, 0, getWidth(), getHeight());

		landscape.draw(g);
		enemiesManager.draw(g);
		ground.draw(g);

		for (Player player : game.getPlayers()) {
			if (game.getGameState().equals(GameState.PLAYING) && player.getScore() >= game.getBestScore()) {
				game.updateBestPlayer(player);
			}
			player.draw(g);
		}

		if (game.getGameState().equals(GameState.GAME_OVER)) {
			g.drawImage(gameOverButtonImage, 60, 450, null);
			g.drawImage(replayButtonImage, 100, 500, null);
		}

		int baseY = 30;

		g2d.setColor(Color.WHITE);
		g2d.fillRoundRect(Constants.SCREEN_WIDTH - 250, baseY, 340, 250, 30, 30);

		int logoX = ((Constants.SCREEN_WIDTH - 250) + 125 - (logoImage.getWidth() / 2));

		g2d.drawImage(logoImage, logoX, baseY + 30, null);

		baseY += 75;

		g2d.setColor(Color.BLACK);
		g2d.setFont(new Font("Arial", 0, 16));
		g2d.drawString("Score: " + game.getFormattedScore(), 1050, baseY += 25);
		g2d.drawString("Record:", 1050, baseY += 25);
		g2d.setColor(Color.BLUE);
		g2d.drawString(" " + game.getBestScore() + " points [" + game.getBestDistance() + "m]", 1110, baseY);
		g2d.setColor(Color.BLACK);
		g2d.drawString("Distance: " + ground.getScreenWidth() + " [" + decimalFormat.format(game.getVelocity()) + " m/s]", 1050, baseY += 25);
		g2d.drawString("Population: " + (Constants.START_POPULATION - game.getDied().size()) + "/" + Constants.START_POPULATION, 1050, baseY += 25);
		g2d.drawString("Generation:", 1050, baseY += 25);
		g2d.drawString(game.toString(), 1140, baseY);
		g2d.drawString("Time: ", 1050, baseY += 25);
		g2d.setColor(Color.RED);
		g2d.drawString(" " + Utils.compareTime(startTime, System.currentTimeMillis()), 1105, baseY);
	}

	@Override
	public void run() {

		int fps = 60;
		long msPerFrame = 1000 * 1000000 / fps;
		long lastTime = 0;
		long elapsed;

		int msSleep;
		// int nanoSleep;

		while (true) {
			gameUpdate();
			repaint();
			System.nanoTime();
			elapsed = (lastTime + msPerFrame - System.nanoTime());
			msSleep = (int) (elapsed / 1000000);
			// nanoSleep = (int) (elapsed % 1000000);
			if (msSleep <= 0) {
				lastTime = System.nanoTime();
				continue;
			}
			try {
				// Thread.sleep(msSleep, nanoSleep);
				Thread.sleep(16);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastTime = System.nanoTime();
		}
	}

	private void resetGame() {
		game.resetVelocity();

		enemiesManager.reset();

		for (Player player : game.getPlayers()) {
			player.reset();
		}

		game.setBestPlayer(null);
	}

	private void setupGameMode() {
		if (Constants.BATTLE_MODE) {
			Constants.START_POPULATION = 2;
			game.addPlayer(new Player(game, false));
			game.addPlayer(new Player(game, true));
		} else {
			for (int i = 0; i < Constants.START_POPULATION; i++) {
				game.addPlayer(new Player(game, false));
			}
		}
	}

	private void initGraphics() {
		ground = new Ground(Constants.SCREEN_WIDTH);
		enemiesManager = new EnemiesManager();
		landscape = new Landscape(Constants.SCREEN_WIDTH);

		replayButtonImage = Utils.getImageResource("data/sprites/play.png");
		gameOverButtonImage = Utils.getImageResource("data/sprites/game_over.png");
		logoImage = Utils.getImageResource("data/sprites/flappy_bird.png");

		game.setGameState(GameState.PLAYING);

		startTime = System.currentTimeMillis();
	}

	/* Getters */

	public Ground getGround() {
		return ground;
	}

	public EnemiesManager getEnimiesManager() {
		return enemiesManager;
	}

	public void startGame() {
		thread = new Thread(this);
		thread.start();
	}

}
