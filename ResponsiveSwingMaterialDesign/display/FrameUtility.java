package br.com.maxtercreations.responsiveswing.display;

import javax.swing.JFrame;

import br.com.maxtercreations.responsiveswing.utils.Utils;

public class FrameUtility {

	public static JFrame build(String name, int x, int y, int width, int height, boolean moveable) {
		Utils.registerFont("GOTHIC.TTF", 1);

		JFrame frame = new JFrame(name);

		frame.setBounds(x, y, width, height);
		frame.setResizable(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		return frame;
	}

}