package br.com.maxtercreations.responsiveswing.components.image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import br.com.maxtercreations.responsiveswing.components.utils.ImageRoundedBorder;
import br.com.maxtercreations.responsiveswing.layouts.ResponsiveLayout;
import br.com.maxtercreations.responsiveswing.utils.Utils;

@SuppressWarnings("serial")
public class ImageLabel extends JComponent {

	private BufferedImage bufferedImage;

	private JLabel label;
	
	private int imageWidth, imageHeight;
	
	private final int margin = 10;

	public ImageLabel(String src, ResponsiveLayout rl, int radius, int width, int height) {
		try {
			BufferedImage from = Utils.resize(ImageIO.read(new File(src)), height - margin, width - margin);

			bufferedImage = Utils.makeRoundedCorner(from, radius);
			
			imageWidth = width;
			imageHeight = height;

			label = new JLabel();
			label.setIcon(new ImageIcon(bufferedImage));

			label.setHorizontalAlignment(SwingConstants.CENTER);

			rl.add(label);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setBorder(ImageRoundedBorder border) {
		label.setBorder(border);
	}

	public JComponent getComponent() {
		return this;
	}

	@Override
	public void repaint() {
		super.repaint();

		if (label != null && getX() != 0 && label.getBounds() != getBounds()) {
			
			int x = getX() + (getWidth() / 2) - (imageWidth / 2);
			
			label.setBounds(x, getY() + (getHeight() / 2) - (imageHeight / 2), imageWidth, imageHeight);
		}
	}

}
