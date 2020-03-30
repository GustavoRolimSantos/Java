package chromedino.display.objects.enemies;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import chromedino.ChromeDino;
import chromedino.display.objects.Dinosaur;
import chromedino.display.objects.enemies.entities.Bird;
import chromedino.display.objects.enemies.entities.Cactus;
import chromedino.display.objects.enemies.entities.Spike;
import chromedino.display.objects.enemies.model.Enemy;

public class EnemiesManager {

	private List<Enemy> enemies = new ArrayList<Enemy>();

	private Enemy nextEnemy = null;
	private Enemy bird, cactus, /*cactus2, cactus3,*/ spike;

	private Random random = new Random();

	public EnemiesManager() {
		reset();
	}

	public void update() {
		bird.update();
		cactus.update();
		//cactus2.update();
		//cactus3.update();
		spike.update();

		Enemy enemy = enemies.get(0);
		
		if (nextEnemy == null) {
			nextEnemy = createEnemy(enemy);
			
			if (nextEnemy instanceof Spike) {
				//Enemy lastEnemy = enemies.set(1, nextEnemy);
				enemies.add(nextEnemy);
				nextEnemy = null;
			}
		}

		if (enemy.isOutOfScreen()) {
			for (Dinosaur dinosaur : ChromeDino.getGameScreen().getDinosaurs()) {
				dinosaur.upScore(enemy.score());
			}

			enemy.reset();
			enemies.remove(enemy);

			if (nextEnemy != null) {
				enemies.add(nextEnemy);
				//enemies.add(cactus2);
				//enemies.add(cactus3);
			}
			nextEnemy = null;
		}
	}

	public void draw(Graphics g) {
		bird.draw(g);
		cactus.draw(g);
		//cactus2.draw(g);
		//cactus3.draw(g);
		spike.draw(g);
	}

	private Enemy createEnemy(Enemy enemy) {
		if (spike.appearRule())
			return spike;

		if (enemy == null || enemy.isOutOfScreen()) {

			if (bird.appearRule())
				return bird;

			if (cactus.appearRule())
				return cactus;

		}

		return cactus;
	}

	public boolean isCollision(Dinosaur dinosaur) {
		for (Enemy e : enemies) {
			if (dinosaur.getBound().intersects(e.getBound())) {
				return true;
			}
		}
		return false;
	}

	public void reset() {
		nextEnemy = null;

		enemies.clear();
		
		if (spike != null) {
			spike.reset();
		}

		bird = new Bird(1280);
		cactus = new Cactus(1280 + random.nextInt(270));
		//cactus2 = new Cactus(1707 + random.nextInt(270));
		//cactus3 = new Cactus(1977 + random.nextInt(200));
		spike = new Spike(1380);

		enemies = new ArrayList<Enemy>();

		nextEnemy = createEnemy(null);

		enemies.add(nextEnemy);
		//enemies.add(cactus2);
		//enemies.add(cactus3);
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

}
