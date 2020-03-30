package chromedino.utils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Animation {

	private List<BufferedImage> list;
	private long deltaTime, previousTime;
	private int currentFrame = 0;

	public Animation(int deltaTime) {
		this.deltaTime = deltaTime;
		this.list = new ArrayList<BufferedImage>();
		this.previousTime = 0;
	}

	public void updateFrame() {
		if (System.currentTimeMillis() - previousTime >= deltaTime) {
			currentFrame++;
			
			if (currentFrame >= list.size())
				currentFrame = 0;
			
			previousTime = System.currentTimeMillis();
		}
	}

	public void addFrame(BufferedImage image) {
		list.add(image);
	}

	public BufferedImage getFrame() {
		if (currentFrame == list.size())
			return list.get(0);
		
		return list.get(currentFrame);
	}

}
