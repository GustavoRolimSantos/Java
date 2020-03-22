package br.com.maxtercreations.responsiveswing.theme;

import java.awt.Color;
import java.util.ArrayList;

public class ThemesManager {

	private ThemeModel currentModel;

	private ArrayList<ThemeModel> themes = new ArrayList<>();
	
	public ThemesManager() {
		themes.add(new ThemeModel("Light", new Color(0, 110, 204), new Color(128, 134, 139), new Color(255, 0, 0), new Color(128, 134, 139), new Color(246, 248, 250)));
		themes.add(new ThemeModel("Dracula", new Color(148, 82, 126), new Color(93, 109, 154), new Color(148, 82, 82), new Color(255, 255, 255), new Color(40, 42, 54)));
		themes.add(new ThemeModel("Dark", new Color(59, 112, 222), new Color(128, 128, 128), new Color(148, 82, 82), new Color(255, 255, 255), new Color(23, 22, 22)));
	
		currentModel = themes.get(0);
	}

	public void addTheme(ThemeModel model) {
		this.themes.add(model);
	}

	public void removeTheme(ThemeModel model) {
		this.themes.remove(model);
	}

	public void setCurrentTheme(ThemeModel model) {
		this.currentModel = model;
	}

	public void setCurrentTheme(String themeName) {
		themes.forEach(theme -> {
			if (theme.getName().equalsIgnoreCase(themeName)) {
				currentModel = theme;
			}
		});
	}

	public ThemeModel getCurrentModel() {
		return currentModel;
	}
	
	public ArrayList<ThemeModel> getThemes() {
		return themes;
	}

}
