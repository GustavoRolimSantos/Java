package chromedino.display.objects;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import chromedino.ChromeDino;
import chromedino.utils.Utils;

public class AirPlane {
	
	private boolean isUsing;

	private BufferedImage airplane1, airplane2;

	private Dinosaur dinosaur;

	private float posX;
	private int posY;

	private int TRIGGER_PIXELS, START_PIXEL;

	public static int PIXELS_RATE = 4029;
	
	public static int CHARGING_RATE = 6000;
	
	private final int OBSTACLE_WIDTH = 970;
	
	private Random random = new Random();

	public AirPlane(int width, Dinosaur dinosaur) {
		this.dinosaur = dinosaur;
		airplane1 = Utils.getResouceImage("data/airplane/airplane0.bmp");
		airplane2 = Utils.getResouceImage("data/airplane/airplane1.bmp");

		posX = dinosaur.getPosX() - 15;
		posY = 517;
	}

	public void use() {
		if (!isUsing) {
			int width = ChromeDino.getGameScreen().getLand().getScreenWidth();

			if (width != 0 && (TRIGGER_PIXELS == 0 || width > TRIGGER_PIXELS)) {
				isUsing = true;
				dinosaur.setPosY(posY - 15);

				TRIGGER_PIXELS = width + PIXELS_RATE;
				START_PIXEL = width;
			}
		}
	}

	public void reset() {
		isUsing = false;
		TRIGGER_PIXELS = 0;
		START_PIXEL = 0;
	}

	public void stopUsing() {
		isUsing = false;
		TRIGGER_PIXELS = ChromeDino.getGameScreen().getLand().getScreenWidth() + PIXELS_RATE;
		START_PIXEL = 0;
	}
	
	public void draw(Graphics g) {
		if (!dinosaur.isAlive() || TRIGGER_PIXELS == 0 || START_PIXEL == 0)
			return;

		int width = ChromeDino.getGameScreen().getLand().getScreenWidth();

		if (width > START_PIXEL + OBSTACLE_WIDTH)
			stopUsing();

		if (isUsing) {
			g.drawImage(random.nextBoolean() ? airplane1 : airplane2, (int) posX, posY, null);
		}

	}

	public boolean isUsing() {
		return isUsing;
	}

}
