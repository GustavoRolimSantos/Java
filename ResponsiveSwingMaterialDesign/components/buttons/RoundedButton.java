package br.com.maxtercreations.responsiveswing.components.buttons;

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;

import br.com.maxtercreations.responsiveswing.callback.CallbackT;
import br.com.maxtercreations.responsiveswing.components.utils.RoundedBorder;

@SuppressWarnings("serial")
public class RoundedButton extends JButton {

	public RoundedButton(String text, Font font, Color color, Rectangle rectangle, CallbackT callbackT) {
		draw(text, font, color, null, rectangle, 50, 1, callbackT);
	}
	
	public RoundedButton(String text, Font font, Color color, Rectangle rectangle, int radius, CallbackT callback) {
		draw(text, font, color, null, rectangle, radius, 1, callback);
	}
	
	public RoundedButton(String text, Font font, Color color, Rectangle rectangle, int radius, int stroke, CallbackT callback) {
		draw(text, font, color, null, rectangle, radius, stroke, callback);
	}
	
	private void draw(String text, Font font, Color foreground, Color background, Rectangle rectangle, int radius, int stroke, CallbackT callback) {
		setOpaque(false);
		RoundedBorder border = new RoundedBorder(radius, background);
		border.setStroke(stroke);
		setBorder(border);
		
		if (rectangle != null) {
			setBounds(rectangle);
		}
		
		setForeground(foreground);
		setFont(font);
		setText(text);
		setContentAreaFilled(false);
		setFocusPainted(false);

		if (callback != null) {
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					callback.done(getText());
				}
			});
		}
	}
	
	public void recolor(Color color, int radius, boolean fill) {
		RoundedBorder border = new RoundedBorder(radius, color, fill);
		border.setStroke(2);
		setBorder(border);
	}
	
}
