package chromedino.display.objects.enemies.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import chromedino.ChromeDino;
import chromedino.constants.Constants;
import chromedino.display.objects.enemies.model.Enemy;
import chromedino.utils.Utils;

public class Bird extends Enemy {

	public static int Y_LAND = 545;

	private int startX, posX, width, height;

	private BufferedImage image1, image2;

	private Rectangle rectBound;

	private Random random = new Random();
	
	private boolean reseting = true;
	
	private boolean option1;
	
	public Bird(int posX) {
		this.startX = posX;
		this.posX = posX;
		
		image1 = Utils.getResouceImage("data/bird/bird1.png");
		image2 = Utils.getResouceImage("data/bird/bird2.png");
		
		this.width = image1.getWidth() - 10;
		this.height = image1.getHeight() - 10;
		
		rectBound = new Rectangle();

		randomize();
	}

	@Override
	public void update() {
		if (rectBound.getX() == 0 && reseting)
			return;
		
		double speed = Constants.SPEED * 1.6;
		
		posX -= speed;
	}

	@Override
	public void draw(Graphics g) {
		if (rectBound.getX() == 0 && reseting)
			return;
		
		g.drawImage(posX % 7 == 0 ? image2 : image1, posX, Y_LAND - image1.getHeight(), null);
	}

	@Override
	public Rectangle getBound() {
		rectBound = new Rectangle();
		rectBound.x = posX + (image1.getWidth() - width) / 2;
		rectBound.y = Y_LAND - image1.getHeight() + (image1.getHeight() - height) / 2;
		rectBound.width = width;
		rectBound.height = height;
		reseting = false;
		return rectBound;
	}

	@Override
	public boolean isOutOfScreen() {
		if (posX < -image1.getWidth()) {
			return true;
		}
		return false;
	}

	@Override
	public int score() {
		return option1 ? 80 : 40;
	}

	@Override
	public boolean appearRule() {
		if (ChromeDino.getGameScreen() == null)
			return false;
		
		int width = ChromeDino.getGameScreen().getLand().getScreenWidth();

		return width > 3000 && random.nextInt(100) <= 70;
	}

	@Override
	public void reset() {
		posX = startX;
		rectBound = new Rectangle();
		randomize();
		reseting = true;
	}
	
	private void randomize() {
		if (random.nextInt(100) <= 70)
			Y_LAND = 635;
	}

}
