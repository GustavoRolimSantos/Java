package utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Utils {

	public static BufferedImage changeColor(File file, Color color) {
		ImageTool imageTool = new ImageTool(file);

		imageTool.changeColor(Color.BLACK, color);

		return imageTool.getImage();
	}

	public static String compareTime(long current, long end) {
		long different = end - current;

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;
		long mounthsInMilli = daysInMilli * 30;

		different = different % mounthsInMilli;

		long days = different / daysInMilli;
		different = different % daysInMilli;

		long hours = different / hoursInMilli;
		different = different % hoursInMilli;

		long minutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long seconds = different / secondsInMilli;

		return days + "d " + hours + "h " + minutes + "min " + seconds + " sec";
	}

	public static BufferedImage getImageResource(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return img;
	}

}
