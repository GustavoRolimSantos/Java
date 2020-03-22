package br.com.maxtercreations.responsiveswing.components.utils.filters;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

public class GaussianBlur {

	private JFrame frame;
	private JPanel glassPanel;
	private BufferedImage blurBuffer, backBuffer, currentGraphics;
	private float alpha = 0.0f;

	public void build(JFrame frame) {
		build(frame, 1, 10);
	}

	public void build() {
		//build(ShoppingManager.getManager().getMainFrame(), 1, 10);
	}

	public void build(JFrame frame, float alpha, int radius) {
		this.frame = frame;
		this.alpha = alpha;
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				glassPanel = new GlassPanel();

				frame.setGlassPane(glassPanel);
				frame.setVisible(true);

				JRootPane root = SwingUtilities.getRootPane(frame);

				blurBuffer = createCompatibleImage(frame.getWidth(), frame.getHeight());

				Graphics2D graphics2D = blurBuffer.createGraphics();

				root.paint(graphics2D);
				graphics2D.dispose();

				backBuffer = blurBuffer;
				blurBuffer = createThumbnailFast(blurBuffer, frame.getWidth() / 2);
				blurBuffer = new GaussianBlurFilter(radius).filter(blurBuffer, null);

				frame.getGlassPane().setVisible(true);
				currentGraphics = new BufferedImage(root.getWidth(), root.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

				frame.getGlassPane().setVisible(false);

				glassPanel = new GlassPanel();
			}
		});
	}
	
	public void setVisible() {
		setVisible(false);
	}

	public void setVisible(boolean load) {
		if (load)
			frame.setGlassPane(glassPanel);
		
		frame.getGlassPane().setVisible(true);
		frame.setEnabled(false);
	}

	public void dispose() {
		frame.getGlassPane().setVisible(false);
		frame.setEnabled(true);
		frame.setVisible(true);
	}

	/* Support Classes */

	@SuppressWarnings("serial")
	private class GlassPanel extends JPanel {
		public GlassPanel() {
			setOpaque(false);
		}

		@Override
		protected void paintComponent(Graphics g) {
			if (isVisible() && blurBuffer != null) {
				Graphics2D graphics2D = (Graphics2D) g.create();

				graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				graphics2D.drawImage(backBuffer, 0, 0, null);
				graphics2D.setComposite(AlphaComposite.SrcOver.derive(alpha));
				graphics2D.drawImage(blurBuffer, 0, 0, frame.getWidth(), frame.getHeight(), null);
				graphics2D.dispose();
			}

			Graphics2D graphics2D = currentGraphics.createGraphics();
			graphics2D.drawImage(currentGraphics, 0, 0, null);
			graphics2D = (Graphics2D) g.create();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			graphics2D.setColor(new Color(0, 0, 0, 50));
			graphics2D.fillRect(getX(), getY(), getWidth(), getHeight());

			graphics2D.dispose();
		}
	}
	
	private static GraphicsConfiguration getGraphicsConfiguration() {
		return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
	}

	private static boolean isHeadless() {
		return GraphicsEnvironment.isHeadless();
	}

	public static BufferedImage createCompatibleImage(BufferedImage bufferedImage, int width, int height) {
		return isHeadless() ? new BufferedImage(width, height, bufferedImage.getType()) : getGraphicsConfiguration().createCompatibleImage(width, height, bufferedImage.getTransparency());
	}

	public static BufferedImage createCompatibleImage(int width, int height) {
		return isHeadless() ? new BufferedImage(width, height, 1) : getGraphicsConfiguration().createCompatibleImage(width, height);
	}

	public static BufferedImage createTranslucityCompatibleImage(int width, int height) {
		return createCompatibleImage(width, height, Transparency.TRANSLUCENT);
	}
	
	public static BufferedImage createCompatibleImage(int width, int height, int transparency) {
        BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
        image.coerceData(true);
        return image;
    }

	public static BufferedImage createThumbnailFast(BufferedImage bufferedImage, int newSize) {
		int width = bufferedImage.getWidth(), height = bufferedImage.getHeight();

		if (width > height) {
			if (newSize >= width)
				throw new IllegalArgumentException("newSize must be lower than the image width");

			if (newSize <= 0)
				throw new IllegalArgumentException("newSize must be greater than 0");

			float ratio = width / height;

			width = newSize;
			height = (int) (newSize / ratio);
		} else {
			if (newSize >= height)
				throw new IllegalArgumentException("newSize must be lower than the image height");

			if (newSize <= 0)
				throw new IllegalArgumentException("newSize must be greater than 0");

			float ratio = height / width;

			height = newSize;
			width = (int) (newSize / ratio);
		}

		BufferedImage temp = createCompatibleImage(bufferedImage, width, height);
		Graphics2D g2 = temp.createGraphics();

		try {
			g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
			g2.drawImage(bufferedImage, 0, 0, temp.getWidth(), temp.getHeight(), null);
		} finally {
			g2.dispose();
		}
		return temp;
	}

	public static int[] getPixels(BufferedImage bufferedImage, int x, int y, int width, int height, int[] pixels) {
		if ((width == 0) || (height == 0))
			return new int[0];

		if (pixels == null) {
			pixels = new int[width * height];
		} else if (pixels.length < width * height) {
			throw new IllegalArgumentException("pixels array must have a length >= w*h");
		}

		int imageType = bufferedImage.getType();

		if ((imageType == 2) || (imageType == 1)) {
			Raster raster = bufferedImage.getRaster();
			return (int[]) raster.getDataElements(x, y, width, height, pixels);
		}

		return bufferedImage.getRGB(x, y, width, height, pixels, 0, width);
	}

	public static void setPixels(BufferedImage bufferedImage, int x, int y, int width, int height, int[] pixels) {
		if ((pixels == null) || (width == 0) || (height == 0))
			return;

		if (pixels.length < width * height)
			throw new IllegalArgumentException("pixels array must have a length >= w*h");

		int imageType = bufferedImage.getType();

		if ((imageType == 2) || (imageType == 1)) {
			WritableRaster raster = bufferedImage.getRaster();
			raster.setDataElements(x, y, width, height, pixels);
		} else {
			bufferedImage.setRGB(x, y, width, height, pixels, 0, width);
		}
	}

	public static class GaussianBlurFilter {

		private final int radius;

		public GaussianBlurFilter(int radius) {
			if (radius < 1)
				radius = 1;

			this.radius = radius;
		}

		public BufferedImage filter(BufferedImage src, BufferedImage dst) {
			int width = src.getWidth(), height = src.getHeight();

			if (dst == null) {
				ColorModel destCM = src.getColorModel();
				dst = new BufferedImage(destCM, destCM.createCompatibleWritableRaster(src.getWidth(), src.getHeight()), destCM.isAlphaPremultiplied(), null);
			}

			int[] srcPixels = new int[width * height], dstPixels = new int[width * height];
			float[] kernel = createGaussianKernel(this.radius);

			getPixels(src, 0, 0, width, height, srcPixels);

			blur(srcPixels, dstPixels, width, height, kernel, this.radius);
			blur(dstPixels, srcPixels, height, width, kernel, this.radius);

			setPixels(dst, 0, 0, width, height, srcPixels);

			return dst;
		}

		protected void blur(int[] srcPixels, int[] dstPixels, int width, int height, float[] kernel, int radius) {
			for (int y = 0; y < height; y++) {
				int index = y, offset = y * width;

				for (int x = 0; x < width; x++) {
					float b, g, r, a = r = g = b = 0.0F;

					for (int i = -radius; i <= radius; i++) {
						int subOffset = x + i;

						if ((subOffset < 0) || (subOffset >= width))
							subOffset = (x + width) % width;

						int pixel = srcPixels[(offset + subOffset)];
						float blurFactor = kernel[(radius + i)];

						a += blurFactor * (pixel >> 24 & 0xFF);
						r += blurFactor * (pixel >> 16 & 0xFF);
						g += blurFactor * (pixel >> 8 & 0xFF);
						b += blurFactor * (pixel & 0xFF);
					}

					int ca = (int) (a + 0.5F), cr = (int) (r + 0.5F), cg = (int) (g + 0.5F), cb = (int) (b + 0.5F);

					dstPixels[index] = ((ca > 255 ? '?' : ca) << 24 | (cr > 255 ? '?' : cr) << 16 | (cg > 255 ? '?' : cg) << 8 | (cb > 255 ? '?' : cb));

					index += height;
				}
			}
		}

		protected float[] createGaussianKernel(int radius) {
			if (radius < 1)
				throw new IllegalArgumentException("Radius must be >= 1");

			float[] data = new float[radius * 2 + 1];
			float sigma = radius / 3.0F, twoSigmaSquare = 2.0F * sigma * sigma, sigmaRoot = (float) Math.sqrt(twoSigmaSquare * 3.141592653589793D), total = 0.0F;

			for (int i = -radius; i <= radius; i++) {
				float distance = i * i;
				int index = i + radius;
				data[index] = ((float) Math.exp(-distance / twoSigmaSquare) / sigmaRoot);
				total += data[index];
			}

			for (int i = 0; i < data.length; i++)
				data[i] /= total;

			return data;
		}
	}

	/* Shadow */

	public static BufferedImage generateBlur(BufferedImage imgSource, int size, Color color, float alpha) {
		GaussianBlurFilter filter = new GaussianBlurFilter(size);

		int imgWidth = imgSource.getWidth(), imgHeight = imgSource.getHeight();

		BufferedImage imgBlur = createTranslucityCompatibleImage(imgWidth, imgHeight);
		Graphics2D g2d = imgBlur.createGraphics();
		applyQualityProperties(g2d);

		g2d.drawImage(imgSource, 0, 0, null);
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
		g2d.setColor(color);

		g2d.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
		g2d.dispose();

		imgBlur = filter.filter(imgBlur, null);

		return imgBlur;
	}

	public static BufferedImage generateShadow(BufferedImage imgSource, int size, Color color, float alpha) {
		int imgWidth = imgSource.getWidth() + (size * 2), imgHeight = imgSource.getHeight() + (size * 2);

		BufferedImage imgMask = createTranslucityCompatibleImage(imgWidth, imgHeight);
		Graphics2D g2 = imgMask.createGraphics();
		applyQualityProperties(g2);

		g2.drawImage(imgSource, 0, 0, null);
		g2.dispose();

		BufferedImage imgShadow = generateBlur(imgMask, size, color, alpha);

		return imgShadow;
	}
	
	public static void applyQualityProperties(Graphics2D g2d) {
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
	}

}