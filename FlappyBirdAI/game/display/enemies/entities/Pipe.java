package game.display.enemies.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import constants.Constants;
import game.display.enemies.model.Enemy;
import maxtercreations.FlappyBird;
import utils.Utils;

public class Pipe extends Enemy {

	private static final int TOP_Y = 50, BOTTOM_Y = 280;
	
	public static final int MAX_TOP_PIPE = 150;

	public int startX, posX, posY, width, height, range;

	private ArrayList<BufferedImage> images = new ArrayList<>();

	private BufferedImage currentImage;

	private Rectangle rectBound;

	private boolean image1, reseting;
	
	
	public Pipe(int posX, boolean image1, int range) {
		this.range = range;
		this.startX = posX;
		this.posX = posX;

		this.image1 = image1;

		images.add(Utils.getImageResource("data/sprites/scenario/enemies/pipe1.png"));
		images.add(Utils.getImageResource("data/sprites/scenario/enemies/pipe2.png"));

		rectBound = new Rectangle();

		setup();
	}

	@Override
	public void update() {
		if (rectBound.getX() == 0 && reseting) {
			reseting = false;
			return;
		}

		posX -= FlappyBird.getGame().getVelocity();
	}

	@Override
	public void draw(Graphics g) {
		if (rectBound.getX() == 0 && reseting) {
			reseting = false;
			return;
		}

		g.drawImage(currentImage, posX, posY, null);
		
		if (Constants.SHOW_COLLISION_AREA) {
			g.fillRect(posX, posY, width, height);
		}
	}

	@Override
	public Rectangle getBound() {
		rectBound = new Rectangle();
		rectBound.x = posX;
		rectBound.y = posY;
		rectBound.width = width;
		rectBound.height = height;
		return rectBound;
	}

	@Override
	public boolean isOutOfScreen() {
		if (posX <= -currentImage.getWidth()) {
			return true;
		}
		return false;
	}

	@Override
	public int score() {
		return 10;
	}

	@Override
	public void reset() {
		posX = Constants.SCREEN_WIDTH + 100;
		rectBound = new Rectangle();
		reseting = true;
	}

	private void setup() {
		currentImage = images.get(image1 ? 0 : 1);

		this.width = currentImage.getWidth();
		this.height = currentImage.getHeight();
		
		posY = (image1 ? TOP_Y + range : BOTTOM_Y + range) - currentImage.getHeight();
	}
	
	public int getRange() {
		return range;
	}

}
