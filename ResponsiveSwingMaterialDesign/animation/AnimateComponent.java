package br.com.maxtercreations.responsiveswing.animation;

import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.Timer;

import br.com.maxtercreations.responsiveswing.utils.Utils;

public class AnimateComponent {

	private int fontIndex = 1;

	public void execute(int time, ArrayList<Animation> animations) {
		fontIndex = 0;

		ArrayList<Font> fonts = new ArrayList<>();

		Animation an = animations.get(0);
		double t = 0;

		while (t < an.getFont().getSize()) {
			int dif = (int) (an.getPoint1().getY() - an.getPoint2().getY());

			t = (an.getFont().getSize() / dif * fontIndex);

			fonts.add(Utils.getMainFont((int) (t)));

			fontIndex++;
		}

		Timer timer = new Timer(time, null);

		timer.addActionListener(e -> {

			for (int i = 0; i < animations.size(); i++) {
				Animation animation = animations.get(i);
				JComponent c = animation.getComponent();
			
				boolean stop = false;

				if (!stop && (animation.getYSkipper() == Animation.STEP && c.getY() <=( (int)animation.getPoint2().getY())) || (animation.getYSkipper() == -Animation.STEP && c.getY() > ((int)animation.getPoint2().getY()))) {
					new Thread(() -> {
						c.setBounds(c.getX(), c.getY() + animation.getYSkipper(), (int) c.getWidth(), (int) c.getHeight());
					}).start();

					new Thread(() -> {
						c.setFont(fonts.get(fontIndex - 1));
					}).start();
				} else {
					stop = true;
				}

				if (stop) {
					System.out.println("Animation finished.");
					
					if (animation.getCallback() != null) {
						animation.getCallback().done();
					}
					
					timer.stop();
				}
			}
		});

		timer.start();

	}

}
