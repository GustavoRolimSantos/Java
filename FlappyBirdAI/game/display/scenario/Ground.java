package game.display.scenario;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import maxtercreations.FlappyBird;
import utils.Utils;

public class Ground {

	public static final int LAND_POSY = 250;

	private ArrayList<BufferedImage> images = new ArrayList<>();
	private ArrayList<ImageLand> listLand = new ArrayList<>();

	private int screenWidth;

	private int width;

	public Ground(int width) {
		this.width = width;

		for (int i = 0; i < 7; i++) {
			images.add(Utils.getImageResource("data/sprites/scenario/ground.png"));
		}

		int currentWidth = 0, counter = 0;

		while (currentWidth < 1400) {
			ImageLand imageLand = new ImageLand();
			imageLand.image = images.get(counter);
			imageLand.posX = currentWidth;

			listLand.add(imageLand);

			counter++;
			currentWidth += imageLand.image.getWidth();

			if (counter == images.size())
				counter = 0;
		}

	}
	
	public void update() {
		
		double velocity = FlappyBird.getGame().getVelocity();
		
		for (int i = 0; i < velocity; i++) {
			listLand.forEach(imageLand -> {
				if (imageLand.posX < -imageLand.image.getWidth()) {
					imageLand.posX = width;
				} else {
					imageLand.posX -= 1;
				}
			});
		}
		
		screenWidth += velocity;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void reset() {
		screenWidth = 0;
	}

	public void draw(Graphics g) {
		try {
			for (ImageLand imgLand : listLand) {
				g.drawImage(imgLand.image, (int) imgLand.posX, LAND_POSY, null);
			}
		} catch (Exception e) {

		}
	}

	private class ImageLand {
		float posX;
		BufferedImage image;
	}

}
