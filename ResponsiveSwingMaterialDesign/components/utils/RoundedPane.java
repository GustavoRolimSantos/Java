package br.com.maxtercreations.responsiveswing.components.utils;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import br.com.maxtercreations.responsiveswing.components.utils.filters.GaussianBlur;

@SuppressWarnings("serial")
public class RoundedPane extends JPanel {

	//private int shadowSize = 8;
	private Dimension dimension;
	private RoundRectangle2D roundRect;

	public RoundedPane(Dimension dimension, RoundRectangle2D roundRect) {
		this.dimension = dimension;
		this.roundRect = roundRect;
		setLayout(null);
		setOpaque(false);
	}

	@Override
	public Insets getInsets() {
		return new Insets(0, 0, 10, 10);
	}

	@Override
	public Dimension getPreferredSize() {
		return dimension;
	}

	@Override
	protected void paintComponent(Graphics g) {
		int width = getWidth() - 1, height = getHeight() - 1;

		Graphics2D graphics2d = (Graphics2D) g.create();

		GaussianBlur.applyQualityProperties(graphics2d);

		Insets insets = getInsets();
		Rectangle bounds = getBounds();

		bounds.x = insets.left;
		bounds.y = insets.top;
		bounds.width = width - (insets.left + insets.right);
		bounds.height = height - (insets.top + insets.bottom);

		RoundRectangle2D shape = roundRect;
		BufferedImage bufferedImage = GaussianBlur.createTranslucityCompatibleImage(bounds.width, bounds.height);
		Graphics2D g2d = bufferedImage.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(Color.BLACK);
		g2d.translate(-bounds.x, -bounds.y);
		g2d.fill(shape);
		g2d.dispose();

		/* Lag BufferedImage shadow = GaussianBlur.generateShadow(bufferedImage, shadowSize, Color.BLACK, 0.3f);
		graphics2d.drawImage(shadow, shadowSize, shadowSize, this);*/
		
		graphics2d.setColor(getBackground());
		graphics2d.fill(shape);

		getUI().paint(graphics2d, this);

		graphics2d.setColor(Color.WHITE);
		graphics2d.draw(shape);
		graphics2d.dispose();
	}

}