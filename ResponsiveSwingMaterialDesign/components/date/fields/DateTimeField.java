package br.com.maxtercreations.responsiveswing.components.date.fields;

import br.com.maxtercreations.responsiveswing.layouts.ResponsiveLayout;

public class DateTimeField extends DateField {

	public DateTimeField(String caption, ResponsiveLayout responsiveLayout) {
		super(caption, responsiveLayout);
		setDateFormat("dd/MM/yyyy HH:mm:ss");
	}


}
