package game.display.enemies;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import constants.Constants;
import game.display.Player;
import game.display.enemies.entities.Pipe;
import game.display.enemies.model.Enemy;
import maxtercreations.FlappyBird;

public class EnemiesManager {

	private List<Enemy> enemies = new ArrayList<Enemy>();

	public EnemiesManager() {
		reset();
	}

	public void update() {
		boolean isOut = false;

		for (Enemy e : enemies) {
			if (e.isOutOfScreen()) {
				e.reset();
				isOut = true;
			}

			e.update();
		}

		if (isOut) {
			FlappyBird.getGame().getPlayers().forEach(bird -> {
				bird.upScore(10);
			});
		}
	}

	public void draw(Graphics g) {
		enemies.forEach(e -> {
			e.draw(g);

			if (Constants.SHOW_COLLISION_AREA) {
				g.setColor(Color.RED);
				g.fillRect((int) e.getBound().getX(), (int) e.getBound().getY(), (int) e.getBound().getWidth(),
						(int) e.getBound().getHeight());
			}
		});
	}

	public boolean isCollision(Player player) {	
		if (player.getBound().getY() == Constants.SCREEN_HEIGHT - 90)
			return true;
		
		for (Enemy e : enemies) {
			if (player.getBound().intersects(e.getBound())) {
				return true;
			}
		}
		return false;
	}

	public void reset() {
		enemies.clear();

		Random random = new Random();

		for (int i = 1; i <= 8; i++) {
			int range = random.nextInt(100);

			enemies.add(new Pipe(700 + (i * 180), true, range));
			enemies.add(new Pipe(700 + (i * 180), false, range));
		}
	}

	public Object[] getNextEnemy(Point point) {
		double smallerDistance = 9999;

		Enemy enemy = enemies.get(0);

		for (Enemy e : enemies) {
			double currentDistance = point.distance(new Point((int) e.getBound().getX(), (int) e.getBound().getY()));

			if (currentDistance <= smallerDistance) {
				smallerDistance = currentDistance;
				enemy = e;
			}
		}
		return new Object[] { enemy, smallerDistance };
	}

	public List<Enemy> getEnemies() {
		return enemies;
	}

}
