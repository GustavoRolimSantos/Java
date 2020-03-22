package br.com.maxtercreations.responsiveswing.components.combobox.render;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;

@SuppressWarnings("serial")
public class MaterialComboBoxRenderer extends BasicComboBoxRenderer {

	@SuppressWarnings("rawtypes")
	@Override
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		
		if (c instanceof JLabel) {
			JLabel l = (JLabel) c;
			l.setBorder(new EmptyBorder(0, 30, 0, 3));
			l.setBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);
			
			if (isSelected) {
				list.setSelectionForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
				list.setSelectionBackground(ResponsiveSwingMaterialDesign.BACKGROUND_COLOR);
			} else {
				l.setForeground(ResponsiveSwingMaterialDesign.PRIMARY_COLOR);
			}

			return l;
		}
		return c;
	}
}
