package utils;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTool {

	private static final int THRESHOLD = 35, TRANSPARENT = 0; // 0x00000000;

	private File inputFile, outputFile;
	
	private BufferedImage outputImage;

	public ImageTool(File inputFile) {
		this.inputFile = inputFile;
	}
	
	public void setOutputFile(File outputFile) {
		this.outputFile = outputFile;
	}

	public void removeColor(Color replaceColor) {
		try {
			
			if (outputImage == null)
				outputImage = ImageIO.read(inputFile);

			int width = outputImage.getWidth(null), height = outputImage.getHeight(null);

			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			Graphics g = image.getGraphics();

			g.drawImage(outputImage, 0, 0, null);

			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int pixel = image.getRGB(x, y);
					Color color = new Color(pixel);

					int dr = Math.abs(color.getRed() - replaceColor.getRed()), dg = Math.abs(color.getGreen() - replaceColor.getGreen()), db = Math.abs(color.getBlue() - replaceColor.getBlue());

					if (dr < THRESHOLD && dg < THRESHOLD && db < THRESHOLD) {
						image.setRGB(x, y, TRANSPARENT);
					}
				}
			}

			outputImage = image;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void changeColor(Color oldColor, Color newColor) {
		try {
			if (outputImage == null)
				outputImage = ImageIO.read(inputFile);
			
			int RGB_MASK = 0x00ffffff;

			int oldRGB = oldColor.getRed() << 16 | oldColor.getGreen() << 8 | oldColor.getBlue();
			int toggleRGB = oldRGB ^ (newColor.getRed() << 16 | newColor.getGreen() << 8 | newColor.getBlue());

			int w = outputImage.getWidth();
			int h = outputImage.getHeight();

			int[] rgb = outputImage.getRGB(0, 0, w, h, null, 0, w);
			for (int i = 0; i < rgb.length; i++) {
				if ((rgb[i] & RGB_MASK) == oldRGB) {
					rgb[i] ^= toggleRGB;
				}
			}
			outputImage.setRGB(0, 0, w, h, rgb, 0, w);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public BufferedImage getImage() {
		return outputImage;
	}

	public void save(String fileType) throws Exception {
		if (outputFile == null)
			throw new Exception("The output file is null.");
		
		try {
			ImageIO.write(outputImage, fileType, outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}