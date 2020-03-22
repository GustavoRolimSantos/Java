package br.com.maxtercreations.responsiveswing.components.utils.filters;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

public class BlackFilter {

	private JFrame frame;
	private JPanel blackPanel;
	private BufferedImage blackBuffer, currentGraphics;
	private boolean visible = false;

	public void build(JFrame frame) {
		this.frame = frame;

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				blackPanel = new BlackPanel();

				frame.setGlassPane(blackPanel);
				frame.setVisible(true);

				JRootPane root = SwingUtilities.getRootPane(frame);

				blackBuffer = GaussianBlur.createCompatibleImage(frame.getWidth(), frame.getHeight());

				Graphics2D graphics2D = blackBuffer.createGraphics();

				root.paint(graphics2D);
				graphics2D.dispose();

				blackBuffer = GaussianBlur.createThumbnailFast(blackBuffer, frame.getWidth() / 2);

				frame.getGlassPane().setVisible(true);
				currentGraphics = new BufferedImage(root.getWidth(), root.getHeight(), BufferedImage.TYPE_3BYTE_BGR);

				frame.getGlassPane().setVisible(false);

				blackPanel = new BlackPanel();
			}
		});
	}

	public void setVisible() {
		setVisible(false);
	}
	
	public void setVisible(boolean load) {
		if (load)
			frame.setGlassPane(blackPanel);
		
		frame.getGlassPane().setVisible(true);
		visible = true;
	}

	public void dispose() {
		frame.getGlassPane().setVisible(false);
		frame.getContentPane().setEnabled(true);
		frame.setVisible(true);
		visible = false;
	}
	
	public boolean isVisible() {
		return visible;
	}

	/* Support Classes */

	@SuppressWarnings("serial")
	private class BlackPanel extends JPanel {
		public BlackPanel() {
			setOpaque(false);
		}

		@Override
		protected void paintComponent(Graphics g) {

			int width = frame.getContentPane().getWidth(), height = frame.getContentPane().getHeight() - 1;

			if (currentGraphics == null)
				return;
			
			Graphics2D graphics2D = currentGraphics.createGraphics();
			graphics2D.drawImage(currentGraphics, 0, 0, null);
			graphics2D = (Graphics2D) g.create();
			graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			graphics2D.setColor(new Color(0, 0, 0, 150));
			graphics2D.fillRect(0, 0, width, height);

			graphics2D.setColor(Color.WHITE);
			graphics2D.drawRect(0, 0, width, height);

			graphics2D.dispose();
		}
	}

}