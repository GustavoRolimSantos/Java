package chromedino.display.objects.scenario;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import chromedino.constants.Constants;
import chromedino.utils.Utils;

public class Montain {

	public static final int LAND_POSY = 475;

	private ArrayList<BufferedImage> images = new ArrayList<>();
	private ArrayList<ImageMontain> listMontain = new ArrayList<>();

	private int width;

	public Montain(int width) {
		this.width = width;

		for (int i = 0; i < 6; i++) {
			images.add(Utils.getResouceImage("data/scenario/montain/montain" + i + ".bmp"));
		}

		build(0);
		build(2);
		build(4);
	}

	private void build(int montainId) {
		int currentWidth = 0, counter = 0;

		while (currentWidth < width) {
			ImageMontain imageMontain = new ImageMontain();
			imageMontain.image = images.get(montainId + counter);

			imageMontain.posX = currentWidth;
			imageMontain.type = montainId;

			listMontain.add(imageMontain);

			counter++;
			currentWidth += imageMontain.image.getWidth();

			if (counter == 2)
				counter = 0;
		}
	}

	public void update() {
		listMontain.forEach(imageMontain -> {

			if (imageMontain.posX <= -imageMontain.image.getWidth()) {
				imageMontain.posX = imageMontain.image.getWidth();
			}

			for (int i = 0; i < Constants.SPEED / 100; i++) {
				if (imageMontain.type == 0)
					imageMontain.posX -= 0.5;

				if (imageMontain.type == 2)
					imageMontain.posX -= 1;

				if (imageMontain.type == 4)
					imageMontain.posX -= 1.5;
			}

		});
	}

	public void draw(Graphics g) {
		try {
			for (ImageMontain imageMontain : listMontain) {
				g.drawImage(imageMontain.image, (int) imageMontain.posX, LAND_POSY, null);
			}
		} catch (Exception e) {

		}
	}

	private class ImageMontain {
		int type;
		float posX;
		BufferedImage image;
	}

}
