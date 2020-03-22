package br.com.maxtercreations.responsiveswing;

import java.awt.Color;

import br.com.maxtercreations.responsiveswing.layouts.ResponsiveLayout;
import br.com.maxtercreations.responsiveswing.theme.ThemesManager;

public class ResponsiveSwingMaterialDesign {

	private static final ThemesManager themesManager = new ThemesManager();

	public static Color PRIMARY_COLOR = new Color(0, 110, 204);
	public static Color SECONDARY_COLOR = new Color(128, 134, 139);
	public static Color ERROR_COLOR = new Color(148, 82, 82);
	public static Color TEXT_COLOR = new Color(128, 134, 139);
	public static Color BACKGROUND_COLOR = new Color(246, 248, 250);

	public static ThemesManager getThemesManager() {
		return themesManager;
	}

	public static void setCurrentTheme(String themeName, ResponsiveLayout... layouts) {
		themesManager.setCurrentTheme(themeName);

		PRIMARY_COLOR = themesManager.getCurrentModel().getPrimaryColor();
		SECONDARY_COLOR = themesManager.getCurrentModel().getSecondaryColor();
		ERROR_COLOR = themesManager.getCurrentModel().getErrorColor();
		TEXT_COLOR = themesManager.getCurrentModel().getTextColor();

		BACKGROUND_COLOR = themesManager.getCurrentModel().getBackgroundColor();

		for (ResponsiveLayout layout : layouts) {
			layout.update();
		}
	}

}
