package chromedino.display;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import chromedino.constants.Constants;
import chromedino.display.metrics.DataMetrics;
import chromedino.display.metrics.GraphicData;
import chromedino.display.objects.Dinosaur;
import chromedino.display.objects.enemies.EnemiesManager;
import chromedino.display.objects.scenario.Clouds;
import chromedino.display.objects.scenario.Land;
import chromedino.display.objects.scenario.Montain;
import chromedino.neuralnetwork.LiveView;
import chromedino.utils.Utils;

@SuppressWarnings("serial")
public class GameScreen extends JPanel implements Runnable, KeyListener {

	private Land land;
	private Montain montain;
	private ArrayList<Dinosaur> dinosaurs = new ArrayList<>(), died = new ArrayList<>();
	private EnemiesManager enemiesManager;
	private Clouds clouds;
	private Thread thread;
	public Dinosaur bestDinosaur;

	private BufferedImage replayButtonImage, gameOverButtonImage;

	private boolean isKeyPressed;

	private int gameState = Constants.START_GAME_STATE;

	private long stoppedTime, startTime;

	public int bestScore, bestDistance;

	private DataMetrics dataMetrics;
	private GraphicData graphicData;

	public GameScreen() {
		if (Constants.BATTLE_MODE) {
			Constants.START_POPULATION = 2;
			dinosaurs.add(new Dinosaur(this, false));
			dinosaurs.add(new Dinosaur(this, true));
		} else {
			for (int i = 0; i < Constants.START_POPULATION; i++) {
				dinosaurs.add(new Dinosaur(this, false));
			}
		}

		enemiesManager = new EnemiesManager();
		land = new Land(Constants.SCREEN_WIDTH);
		montain = new Montain(Constants.SCREEN_WIDTH);

		replayButtonImage = Utils.getResouceImage("data/several/replay_button.png");
		gameOverButtonImage = Utils.getResouceImage("data/several/gameover_text.png");

		clouds = new Clouds(Constants.SCREEN_WIDTH);
		startTime = System.currentTimeMillis();

		gameState = Constants.GAME_PLAYING_STATE;

		dataMetrics = new DataMetrics();
		graphicData = new GraphicData(this);

	}

	public void gameUpdate() {
		long current = System.currentTimeMillis();
		if (gameState == Constants.GAME_OVER_STATE && stoppedTime != 0 && current - stoppedTime >= 500) {
			stoppedTime = 0;
			gameState = Constants.GAME_PLAYING_STATE;
			resetGame();
		}

		if (gameState == Constants.GAME_PLAYING_STATE) {
			Constants.SPEED += 0.0005;

			clouds.update();
			land.update();
			montain.update();

			for (Dinosaur dinosaur : dinosaurs) {
				dinosaur.update();

				if (enemiesManager.isCollision(dinosaur)) {
					if (!died.contains(dinosaur)) {
						
						if (!Constants.BATTLE_MODE) {
							dinosaur.getNeuralNetwork().newGenome(dinosaur.score);
						}
						
						dinosaur.setAlive(false);

						died.add(dinosaur);
					}
				}
			}
			enemiesManager.update();

			if (died.size() > 0 && died.size() == dinosaurs.size()) {
				land.reset();
				stoppedTime = System.currentTimeMillis();
				gameState = Constants.GAME_OVER_STATE;
			}
		}
	}

	@Override
	public void paint(Graphics g1) {
		g1.setColor(Color.decode("#f7f7f7"));
		g1.fillRect(0, 0, getWidth(), getHeight());

		Graphics2D g = dataMetrics.getGraphics(g1, 500, 300);

		LiveView.paintNeuralNetwork(g, bestDinosaur != null ? bestDinosaur.getNeuralNetwork() : dinosaurs.get(0).getNeuralNetwork());

		int baseY = 40;

		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", 0, 16));
		g.drawString("Score: " + (bestDinosaur != null ? ("" + bestDinosaur.score) : "0"), 1050, baseY + 25);
		g.drawString("Record Score:", 1050, baseY + 50);
		g.setColor(Color.BLUE);
		g.drawString(" " + bestScore, 1160, baseY + 50);
		g.setColor(Color.BLACK);
		g.drawString("Record Distance: " + bestDistance, 1050, baseY + 75);
		g.drawString("Distance: " + land.getScreenWidth(), 1050, baseY + 100);
		g.drawString("Start Population: " + Constants.START_POPULATION, 1050, baseY + 125);
		g.drawString("Current Population: " + (Constants.START_POPULATION - died.size()), 1050, baseY + 150);
		g.drawString("Generation:", 1050, baseY + 175);
		g.drawString(bestDinosaur == null ? "Unknown" : "" + (bestDinosaur.getNeuralNetwork().current_generation + 1) + " [" + (bestDinosaur.getNeuralNetwork().current_genome + 1) + "/" + bestDinosaur.getNeuralNetwork().genomes_per_generation + " genome]", 1140, baseY + 175);
		g.drawString("Time: ", 1050, baseY + 200);
		g.setColor(Color.RED);
		g.drawString(" " + Utils.compareTime(startTime, System.currentTimeMillis()), 1105, baseY + 200);

		switch (gameState) {
		case Constants.START_GAME_STATE:
			for (Dinosaur dinosaur : dinosaurs) {
				dinosaur.draw(g);
			}
			break;
		case Constants.GAME_PLAYING_STATE:
			Dinosaur best = null;
			for (Dinosaur dinosaur : dinosaurs) {
				if ((dinosaur != null && best == null) || dinosaur.score >= best.score)
					best = dinosaur;

				dinosaur.draw(g);
			}

			if (best != null)
				bestDinosaur = best;
		case Constants.GAME_OVER_STATE:
			clouds.draw(g);
			montain.draw(g);
			land.draw(g);
			enemiesManager.draw(g);
			for (Dinosaur dinosaur : dinosaurs) {
				dinosaur.draw(g);
			}
			if (gameState == Constants.GAME_OVER_STATE) {
				g.drawImage(gameOverButtonImage, 542, 342, null);
				g.drawImage(replayButtonImage, 623, 362, null);
			}
			break;
		}
	}

	@Override
	public void run() {

		int fps = 100;
		long msPerFrame = 1000 * 1000000 / fps;
		long lastTime = 0;
		long elapsed;

		int msSleep;
		int nanoSleep;

		while (true) {
			gameUpdate();
			repaint();
			System.nanoTime();
			elapsed = (lastTime + msPerFrame - System.nanoTime());
			msSleep = (int) (elapsed / 1000000);
			nanoSleep = (int) (elapsed % 1000000);
			if (msSleep <= 0) {
				lastTime = System.nanoTime();
				continue;
			}
			try {
				Thread.sleep(msSleep, nanoSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			lastTime = System.nanoTime();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!isKeyPressed) {
			isKeyPressed = true;

			if (gameState == Constants.GAME_PLAYING_STATE) {

				if (Constants.BATTLE_MODE) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						for (Dinosaur dinosaur : dinosaurs) {
							if (!dinosaur.isAlive()) {
								dinosaur.setAlive(false);
								died.add(dinosaur);
							}
						}
					}
				}

				for (Dinosaur dinosaur : dinosaurs) {
					if (dinosaur.isPlayingAsHuman()) {
						if (e.getKeyCode() == KeyEvent.VK_UP)
							dinosaur.jump();
						if (e.getKeyCode() == KeyEvent.VK_DOWN)
							dinosaur.down(true);
						if (e.getKeyCode() == KeyEvent.VK_A)
							dinosaur.getAirPlane().use();
					}
				}
			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		isKeyPressed = false;
		if (gameState == Constants.GAME_PLAYING_STATE && e.getKeyCode() == KeyEvent.VK_DOWN) {
			for (Dinosaur dinosaur : dinosaurs) {
				dinosaur.down(false);
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	private void resetGame() {
		graphicData.updateLearningCurve();
		graphicData.updateAverage();

		Constants.SPEED = Constants.START_SPEED;

		died.clear();
		enemiesManager.reset();

		bestDinosaur = null;

		for (Dinosaur dinosaur : dinosaurs) {
			dinosaur.reset();
		}
	}

	/* Getters */

	public int getBestScore() {
		return bestScore;
	}

	public Dinosaur getBestDinosaur() {
		return bestDinosaur;
	}

	public Land getLand() {
		return land;
	}

	public EnemiesManager getEnimiesManager() {
		return enemiesManager;
	}

	public ArrayList<Dinosaur> getDinosaurs() {
		return dinosaurs;
	}

	public ArrayList<Dinosaur> getDied() {
		return died;
	}

	public long getStartTime() {
		return startTime;
	}

	public void startGame() {
		thread = new Thread(this);
		thread.start();
	}

	public DataMetrics getDataMetrics() {
		return dataMetrics;
	}

}
