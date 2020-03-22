package br.com.maxtercreations.responsiveswing.components.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.border.Border;

public class ImageRoundedBorder implements Border {
	
	/* This class has the function to round the borders. */

	private int radius, index = -1, stroke = 1;
	private Color color;
	private boolean fill = false;

	public ImageRoundedBorder(int radius) {
		this.radius = radius;
	}
	
	public ImageRoundedBorder(int radius, int index) {
		this.radius = radius;
		this.index = index;
	}
	
	public ImageRoundedBorder(int radius, Color color) {
		this.radius = radius;
		this.color = color;
	}
	
	public ImageRoundedBorder(int radius, Color color, boolean fill) {
		this.radius = radius;
		this.color = color;
		this.fill = fill;
	}
	
	private Insets defaultInsets;

	public Insets getBorderInsets(Component c) {
		int index = this.index != -1 ? this.index : this.radius / 10;
		
		if (defaultInsets == null)
			defaultInsets = new Insets(0, 0, 0, 0);
		
		Insets in = new Insets(index + 2 + defaultInsets.top, index + 1 + defaultInsets.left, index + 2 + defaultInsets.bottom, index + defaultInsets.right);
		
		return in;
	}
	
	public void setDefaultInsets(Insets defaultInsets) {
		this.defaultInsets = defaultInsets;
	}

	public boolean isBorderOpaque() {
		return true;
	}
	
	public void setStroke(int stroke) {
		this.stroke = stroke;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		Graphics2D g2d = (Graphics2D)g;
		
		Color oldColor = g2d.getColor();
		
		if (color != null)
			g2d.setColor(color);
		
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		g2d.setStroke(new BasicStroke(stroke));
		
		if (fill)
			g2d.fillRoundRect(x + (stroke * 2), y + (stroke * 2) - 2, width - (stroke * 4), height - (stroke * 4), radius, radius);
		else 
			g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, radius, radius);
		
		g2d.setColor(oldColor);
	}
	
	public Color getColor() {
		return color;
	}
}