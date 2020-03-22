package br.com.maxtercreations.responsiveswing.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.text.Normalizer;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import br.com.maxtercreations.responsiveswing.constants.Constants;

public class Utils {

	public static Font getMainFont(int size) {
		return new Font("Century Gothic", 0, size);
	}

	public static Font getBoldFont(int size) {
		return new Font("Century Gothic Bold", 0, size);
	}

	public static void registerFont(String fontName, float size) {
		try {
			Font customFont = Font.createFont(Font.TRUETYPE_FONT, Utils.class.getResourceAsStream(Constants.RESOURCES_DIRECTORY + "/" + fontName));
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(customFont);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public static String removeAccents(String text) {
		return Normalizer.normalize(text.trim(), Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").trim();
	}

	public static boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (Exception e) {

		}
		return false;

	}

	public static ImageIcon convertImageColors(String directory, Color color) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(Utils.class.getResource(directory));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int width = image.getWidth(), height = image.getHeight();
		WritableRaster raster = image.getRaster();

		for (int xx = 0; xx < width; xx++) {
			for (int yy = 0; yy < height; yy++) {
				int[] pixels = raster.getPixel(xx, yy, (int[]) null);
				pixels[0] = color.getRed();
				pixels[1] = color.getGreen();
				pixels[2] = color.getBlue();
				raster.setPixel(xx, yy, pixels);
			}
		}

		return new ImageIcon(image);
	}

	public static BufferedImage makeRoundedCorner(BufferedImage image, int cornerRadius) {
		int w = image.getWidth(), h = image.getHeight();
		BufferedImage output = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2 = output.createGraphics();

		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		g2.fill(new RoundRectangle2D.Float(0, 0, w, h, cornerRadius, cornerRadius));

		g2.setComposite(AlphaComposite.SrcAtop);
		g2.drawImage(image, 0, 0, null);

		g2.dispose();

		return output;
	}

	public static BufferedImage resize(BufferedImage img, int height, int width) {
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}

}
