package br.com.maxtercreations.responsiveswing.theme;

import java.awt.Color;

public class ThemeModel {

	private String name;

	private Color primaryColor, secondaryColor, errorColor, textColor, backgroundColor;

	public ThemeModel(String name, Color primaryColor, Color secondaryColor, Color errorColor, Color textColor, Color backgroundColor) {
		super();
		this.name = name;
		this.primaryColor = primaryColor;
		this.secondaryColor = secondaryColor;
		this.errorColor = errorColor;
		this.textColor = textColor;
		this.backgroundColor = backgroundColor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Color getPrimaryColor() {
		return primaryColor;
	}

	public void setPrimaryColor(Color primaryColor) {
		this.primaryColor = primaryColor;
	}

	public Color getSecondaryColor() {
		return secondaryColor;
	}

	public void setSecondaryColor(Color secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public Color getErrorColor() {
		return errorColor;
	}

	public void setErrorColor(Color errorColor) {
		this.errorColor = errorColor;
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}
