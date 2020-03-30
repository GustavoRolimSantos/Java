package chromedino.display.objects.enemies.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import chromedino.ChromeDino;
import chromedino.constants.Constants;
import chromedino.display.objects.AirPlane;
import chromedino.display.objects.enemies.model.Enemy;
import chromedino.utils.Utils;

public class Spike extends Enemy {

	public static final int Y_LAND = 672;

	private int startX, posX, width, height;

	private BufferedImage image;

	private Rectangle rectBound;

	private int appers = 0;

	private boolean reseting = true;

	public Spike(int posX) {
		this.startX = posX;
		this.posX = posX;
		this.image = Utils.getResouceImage("data/enemies/spike/spike.png");

		this.width = image.getWidth() - 10;
		this.height = image.getHeight() - 10;

		rectBound = new Rectangle();
	}

	@Override
	public void update() {
		if (rectBound.getX() == 0 && reseting)
			return;

		posX -= Constants.SPEED;
		
		removeNearObjects();
	}

	@Override
	public void draw(Graphics g) {
		if (rectBound.getX() == 0 && reseting)
			return;

		g.drawImage(image, posX, Y_LAND - image.getHeight(), null);
	}

	@Override
	public Rectangle getBound() {
		rectBound = new Rectangle();
		rectBound.x = posX + (image.getWidth() - width) / 2;
		rectBound.y = Y_LAND - image.getHeight() + (image.getHeight() - height) / 2;
		rectBound.width = width;
		rectBound.height = height;
		reseting = false;
		return rectBound;
	}

	@Override
	public boolean isOutOfScreen() {
		if (posX < -image.getWidth()) {
			return true;
		}
		return false;
	}

	@Override
	public int score() {
		return 20;
	}

	@Override
	public boolean appearRule() {
		if (ChromeDino.getGameScreen() == null)
			return false;

		int width = ChromeDino.getGameScreen().getLand().getScreenWidth();

		int dif = width / AirPlane.CHARGING_RATE;

		if (width > 3000 && dif > appers) {
			appers = dif;
			return true;
		}

		return false;
	}

	@Override
	public void reset() {
		posX = startX;
		rectBound = new Rectangle();
		reseting = true;
	}

	private void removeNearObjects() {
		boolean hasSpike = false, outOfScreen = isOutOfScreen();

		for (Enemy e : ChromeDino.getGameScreen().getEnimiesManager().getEnemies()) {
			if (e instanceof Spike) {
				hasSpike = true;
			}
			
			if (outOfScreen && e instanceof Cactus && !((Cactus)e).isShowing()) {
				((Cactus)e).setShowing(true);
			}
		}
		
		if (outOfScreen)
			return;

		if (hasSpike) {
			ArrayList<Enemy> remove = new ArrayList<>();
			ChromeDino.getGameScreen().getEnimiesManager().getEnemies().forEach(e -> {
				if (e instanceof Cactus) {
					double x = getBound().getX() - 50;

					if (e.getBound().getX() + e.getBound().getWidth() >= x && e.getBound().getX() <= x + getBound().getWidth() + 100) {
						e.reset();
						((Cactus) e).setShowing(false);
						remove.add(e);
					}
				} else if (e instanceof Bird) {
					double x = getBound().getX() - 50;

					if (e.getBound().getX() + e.getBound().getWidth() >= x && e.getBound().getX() <= x + getBound().getWidth() + 100) {
						e.reset();
						remove.add(e);
					}
				}
			});

			ChromeDino.getGameScreen().getEnimiesManager().getEnemies().removeAll(remove);
		}
	}
}
