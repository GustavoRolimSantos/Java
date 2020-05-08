package game.display.scenario;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import utils.Utils;

public class Landscape {

	public static final int LAND_POSY = -130;

	private ArrayList<BufferedImage> images = new ArrayList<>();
	private ArrayList<ImageLand> listLand = new ArrayList<>();

	//private int width;

	public Landscape(int width) {
		//this.width = width;

		for (int i = 0; i < 9; i++) {
			images.add(Utils.getImageResource("data/sprites/scenario/landscape.png"));
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
		/*for (int i = 0; i < Constants.VELOCITY; i++) {
			listLand.forEach(imageLand -> {
				if (imageLand.posX < -imageLand.image.getWidth()) {
					imageLand.posX = width;
				} else {
					imageLand.posX -= 1;
				}
			});
		}*/
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
