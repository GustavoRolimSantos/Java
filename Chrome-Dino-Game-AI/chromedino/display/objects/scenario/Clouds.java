package chromedino.display.objects.scenario;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import chromedino.constants.Constants;
import chromedino.utils.Utils;

public class Clouds {
	
	private static final int LAND_POSY = 300;
	private static final int CLOUDS_QUANTITY = 8;

	private BufferedImage image;
	private ArrayList<ImageCoud> listClouds = new ArrayList<>();

	private int width;
	
	private Random random;

	public Clouds(int width) {
		this.width = width;

		random = new Random();
		
		image = Utils.getResouceImage("data/scenario/cloud/cloud.png");

		int currentWidth = 0;
		
		int currency = width / CLOUDS_QUANTITY;

		for (int i = 0; i < CLOUDS_QUANTITY; i++) {
			ImageCoud imageLand = new ImageCoud();
			imageLand.image = image;
			imageLand.posX = currentWidth;
			imageLand.posY = LAND_POSY + random.nextInt(300);

			listClouds.add(imageLand);

			currentWidth += currency;
		}

	}

	public void update() {
		listClouds.forEach(imageCloud -> {

			imageCloud.posX -= (Constants.SPEED / 20);

			if (imageCloud.posX <= 0) {
				imageCloud.posX = width;
			}
		});
	}

	public void draw(Graphics g) {
		try {
			for (ImageCoud imgCloud : listClouds) {
				g.drawImage(imgCloud.image, (int) imgCloud.posX, imgCloud.posY, null);
			}
		} catch (Exception e) {

		}
	}

	private class ImageCoud {
		float posX;
		int posY;
		BufferedImage image;
	}

}
