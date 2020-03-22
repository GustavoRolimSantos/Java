package br.com.maxtercreations.responsiveswing.components.combobox;

import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.UIManager;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;
import br.com.maxtercreations.responsiveswing.callback.CallbackT;
import br.com.maxtercreations.responsiveswing.components.combobox.render.MaterialComboBoxEditor;
import br.com.maxtercreations.responsiveswing.components.combobox.render.MaterialComboBoxRenderer;
import br.com.maxtercreations.responsiveswing.components.combobox.render.MaterialComboBoxUI;
import br.com.maxtercreations.responsiveswing.layouts.ResponsiveLayout;
import br.com.maxtercreations.responsiveswing.utils.Utils;

@SuppressWarnings({ "serial", "rawtypes" })
public class ComboBox extends JComboBox {
	
	@SuppressWarnings("unchecked")
	public ComboBox(ResponsiveLayout responsiveLayout, ArrayList<Object> itens) {

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}
		this.setEditor(new MaterialComboBoxEditor());
		this.setRenderer(new MaterialComboBoxRenderer());
		this.setUI(new MaterialComboBoxUI());

		for (Object obj : itens) {
			this.addItem(obj.toString());
		}

		this.setFont(Utils.getMainFont(22));
		this.setBackground(new Color(0, 0, 0, 0));
	}

	@Override
	public void repaint() {
		super.repaint();
		
		if (getForeground() != null && !getForeground().equals(ResponsiveSwingMaterialDesign.PRIMARY_COLOR)) {
			setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
		}
	}
	
	public ComboBox getComponent() {
		return this;
	}
	
	public void addValueChangeListener(CallbackT valueChangeListener) {
		this.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Object item = e.getItem();
					
					if (valueChangeListener != null) {
						valueChangeListener.done(item);
					}
				}
			}
		});
	}

}
