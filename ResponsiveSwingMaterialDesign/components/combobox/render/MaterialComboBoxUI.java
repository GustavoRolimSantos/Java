package br.com.maxtercreations.responsiveswing.components.combobox.render;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicComboBoxUI;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;
import br.com.maxtercreations.responsiveswing.components.combobox.AutoCompletion;
import br.com.maxtercreations.responsiveswing.components.utils.RoundedBorder;

public class MaterialComboBoxUI extends BasicComboBoxUI {

	@Override
	public void installUI(JComponent c) {
		super.installUI(c);

		comboBox.setOpaque(false);
		comboBox.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
		comboBox.setBorder(new RoundedBorder(20, ResponsiveSwingMaterialDesign.SECONDARY_COLOR));
		comboBox.setLightWeightPopupEnabled(true);
		comboBox.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		AutoCompletion.enable(comboBox);
	}

	@SuppressWarnings("serial")
	@Override
	protected JButton createArrowButton() {
		return new JButton() {
			@Override
			public int getWidth() {
				return 0;
			}
		};
	}

	@Override
	public void update(Graphics g, JComponent c) {
		g = MaterialDrawingUtils.getAliasedGraphics(g);
		Graphics2D g2d = (Graphics2D)g;
		
		g2d.setColor(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);
		g2d.setStroke(new BasicStroke(4));
		g2d.fillRoundRect(0, 0, comboBox.getWidth(), comboBox.getHeight(), 20, 20);
		
		paint(g2d, c);
	}
	
	@Override
	public void paint(Graphics arg0, JComponent arg1) {
		super.paint(arg0, arg1);
		

		comboBox.setBorder(new RoundedBorder(20, ResponsiveSwingMaterialDesign.SECONDARY_COLOR));
	}

}
