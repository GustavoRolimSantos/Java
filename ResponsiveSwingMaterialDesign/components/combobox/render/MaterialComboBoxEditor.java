package br.com.maxtercreations.responsiveswing.components.combobox.render;

import java.awt.Component;

import javax.swing.plaf.basic.BasicComboBoxEditor;

import br.com.maxtercreations.responsiveswing.ResponsiveSwingMaterialDesign;

public class MaterialComboBoxEditor extends BasicComboBoxEditor {

    @Override
    public Component getEditorComponent() {
        Component component = super.getEditorComponent();
        component.setForeground(ResponsiveSwingMaterialDesign.TEXT_COLOR);
        return component;
    }
}
