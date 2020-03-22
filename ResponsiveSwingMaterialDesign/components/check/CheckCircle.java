package br.com.maxtercreations.responsiveswing.components.check;

import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;
import br.com.maxtercreations.responsiveswing.callback.CallbackT;
import br.com.maxtercreations.responsiveswing.components.buttons.RoundedButton;
import br.com.maxtercreations.responsiveswing.components.buttons.RoundedFillButton;
import br.com.maxtercreations.responsiveswing.layouts.ResponsiveLayout;
import br.com.maxtercreations.responsiveswing.utils.Utils;

public class CheckCircle {

	private JPanel card;
	
	private RoundedButton border;
	private RoundedFillButton button;
	private JLabel captionLabel;
	
	private CallbackT callbackT;
	
	@SuppressWarnings("serial")
	public CheckCircle(String caption, CallbackT changeStateEvent, ResponsiveLayout rl) {
		this.callbackT = changeStateEvent;
		
		card = new JPanel() {
			@Override
			public void repaint() {
				super.repaint();
				
				if (captionLabel != null && !captionLabel.getForeground().equals(ResponsiveSwingMaterialDesign.SECONDARY_COLOR)) {
					captionLabel.setForeground(ResponsiveSwingMaterialDesign.SECONDARY_COLOR);
					border.recolor(ResponsiveSwingMaterialDesign.PRIMARY_COLOR, 25, false);
					button.getBackgroundButton().recolor(ResponsiveSwingMaterialDesign.PRIMARY_COLOR, 25, true);
				}
			}
		};
		card.setLayout(null);
		card.setOpaque(false);
		
		border = new RoundedButton(null, null, ResponsiveSwingMaterialDesign.PRIMARY_COLOR, new Rectangle(0, card.getY() + (card.getHeight() / 2) + 21, 25, 25), 25, new CallbackT() {	
			@Override
			public void done(Object s) {
				button.setVisible(!button.isVisible());
				callbackT.done(button.isVisible());
			}
		});
		border.setVerticalAlignment(SwingConstants.CENTER);

		
		button = new RoundedFillButton(null, null, null, ResponsiveSwingMaterialDesign.PRIMARY_COLOR, new Rectangle(4, card.getY() + (card.getHeight() / 2) + 25, 17, 17), 17, 1, card, true, new CallbackT() {		
			@Override
			public void done(Object s) {
				button.setVisible(!button.isVisible());
				callbackT.done(button.isVisible());
			}
		});
		button.setVerticalAlignment(SwingConstants.CENTER);
		
		
		card.add(border);
		
		captionLabel = new JLabel(caption);
		captionLabel.setFont(Utils.getMainFont(15));
		captionLabel.setVerticalAlignment(SwingConstants.CENTER);
		captionLabel.setBounds(40, 5,  captionLabel.getFontMetrics(captionLabel.getFont()).stringWidth(caption) + 20, 50);
		captionLabel.setForeground(ResponsiveSwingMaterialDesign.SECONDARY_COLOR);
		card.add(captionLabel);
	}
	
	public void setCaption(String caption) {
		this.captionLabel.setText(caption);
	}
	
	public boolean isChecked() {
		return button.isVisible();
	}
	
	public CheckCircle setChecked(boolean checked) {
		button.setVisible(checked);
		callbackT.done(checked);
		return this;
	}

	public JPanel getComponent() {
		return card;
	}
	
}
