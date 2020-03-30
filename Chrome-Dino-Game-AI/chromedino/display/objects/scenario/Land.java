package chromedino.display.objects.scenario;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import chromedino.constants.Constants;
import chromedino.utils.Utils;

public class Land {

	public static final int LAND_POSY = 650;

	private ArrayList<BufferedImage> images = new ArrayList<>();
	private ArrayList<ImageLand> listLand = new ArrayList<>();

	private int screenWidth;

	private int width;

	public Land(int width) {
		this.width = width;

		for (int i = 0; i < 6; i++) {
			images.add(Utils.getResouceImage("data/scenario/land/land" + i + ".bmp"));
		}

		int currentWidth = 0, counter = 0;

		while (currentWidth < width) {
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
		
		for (int i = 0; i < Constants.SPEED; i++) {
			listLand.forEach(imageLand -> {
				if (imageLand.posX - 1 <= 0) {
					imageLand.posX = width;
				} else {
					imageLand.posX -= 1;
				}
			});
		}
		
		screenWidth += Constants.SPEED;
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
