package chromedino.display.objects.enemies.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import chromedino.ChromeDino;
import chromedino.constants.Constants;
import chromedino.display.objects.enemies.model.Enemy;
import chromedino.utils.Utils;

public class Cactus extends Enemy {

	public static final int Y_LAND = 660;

	private int startX, backupX, posX, width, height;

	private ArrayList<BufferedImage> images = new ArrayList<>();

	private BufferedImage currentImage;

	private Rectangle rectBound;

	private Random random = new Random();

	private boolean reseting = true;

	private boolean showing = true;

	public Cactus(int posX) {
		this.startX = posX;
		this.posX = posX;
		this.backupX = posX;

		for (int i = 0; i < 5; i++) {
			images.add(Utils.getResouceImage("data/enemies/cactus/cactus" + i + ".bmp"));
		}

		rectBound = new Rectangle();

		randomize();
	}

	@Override
	public void update() {
		if (!showing && ChromeDino.getGameScreen().getEnimiesManager().getEnemies().get(0) instanceof Cactus) {
			showing = true;
		}
		
		if ((rectBound.getX() == 0 && reseting) || (!showing))
			return;

		posX -= Constants.SPEED;
	}

	@Override
	public void draw(Graphics g) {
		if ((rectBound.getX() == 0 && reseting) || (!showing))
			return;

		g.drawImage(currentImage, posX, Y_LAND - currentImage.getHeight(), null);
	}

	@Override
	public Rectangle getBound() {
		rectBound = new Rectangle();
		rectBound.x = posX + currentImage.getWidth();
		rectBound.y = Y_LAND - currentImage.getHeight();
		rectBound.width = width;
		rectBound.height = height;
		reseting = false;
		return rectBound;
	}

	@Override
	public boolean isOutOfScreen() {
		if (posX < -currentImage.getWidth()) {
			return true;
		}
		return false;
	}

	@Override
	public int score() {
		return 10;
	}

	@Override
	public boolean appearRule() {
		return true;
	}

	@Override
	public void reset() {
		posX = startX;
		rectBound = new Rectangle();
		randomize();
		reseting = true;
		showing = true;
	}

	private void randomize() {
		currentImage = images.get(random.nextInt(images.size() - 1));
		
		startX = (int) (backupX + random.nextInt(270));

		this.width = currentImage.getWidth() - 10;
		this.height = currentImage.getHeight() - 10;
	}

	public void setStartX(int startX) {
		this.startX = startX;
	}

	public void setShowing(boolean showing) {
		this.showing = showing;
	}
	
	public boolean isShowing() {
		return showing;
	}

}
