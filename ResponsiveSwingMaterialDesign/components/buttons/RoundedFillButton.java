package br.com.maxtercreations.responsiveswing.components.buttons;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import br.com.maxtercreations.responsiveswing.callback.CallbackT;
import br.com.maxtercreations.responsiveswing.components.utils.RoundedBorder;

public class RoundedFillButton {

	private RoundedButton backgroundButton;
	private JLabel textLabel;
	private JPanel panel;
	
	public RoundedFillButton(String text, Font font, Color foreground, Color background, Rectangle rectangle, int radius, int stroke, JPanel panel, Boolean build, CallbackT callbackT) {
		this.panel = panel;
		this.backgroundButton = new RoundedButton(text, font, background, rectangle, radius, stroke, callbackT);
		this.backgroundButton.setBorder(new RoundedBorder(radius, background, true));
		this.textLabel = new JLabel(text);
		
		if (rectangle != null)
			this.textLabel.setBounds(rectangle);
		
		this.textLabel.setForeground(foreground);
		this.textLabel.setFont(font);
		this.textLabel.setHorizontalAlignment(SwingConstants.CENTER);
		this.textLabel.setVerticalAlignment(SwingConstants.CENTER);
		this.textLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (callbackT != null)
					callbackT.done(textLabel.getText());
			}
		});

		if (build)
			build();
	}
	
	public void dispose() {
		panel.remove(backgroundButton);
		panel.remove(textLabel);
	}
	
	public void build() {
		panel.add(textLabel);
		panel.add(backgroundButton);
	}
	
	public void ajustText(int y) {
		textLabel.setBounds(textLabel.getX(), textLabel.getY() + y, textLabel.getWidth(), textLabel.getHeight());
	}
	
	public void ajustText(int x, int y) {
		textLabel.setBounds(textLabel.getX() + x, textLabel.getY() + y, textLabel.getWidth(), textLabel.getHeight());
	}
	
	public JLabel getTextLabel() {
		return textLabel;
	}
	
	public RoundedButton getBackgroundButton() {
		return backgroundButton;
	}
	
	public void setVisible(boolean visible) {
		textLabel.setVisible(visible);
		backgroundButton.setVisible(visible);
	}
	
	public boolean isVisible() {
		return textLabel.isVisible();
	}

	public void setVerticalAlignment(int alignment) {
		textLabel.setVerticalAlignment(alignment);
		backgroundButton.setVerticalAlignment(alignment);
	}

	public void setHorizontalAlignment(int alignment) {
		textLabel.setHorizontalAlignment(alignment);
		backgroundButton.setHorizontalAlignment(alignment);
	}
	
}
