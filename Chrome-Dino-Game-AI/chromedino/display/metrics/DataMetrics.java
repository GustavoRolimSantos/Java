package chromedino.display.metrics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class DataMetrics {

	public ArrayList<Integer> bestOfGeneration = new ArrayList<>(), average = new ArrayList<>(), learningCurve = new ArrayList<>();

	private final int PAD = 20;

	public DataMetrics() {
		bestOfGeneration.add(0);
		average.add(0);
		learningCurve.add(0);
	}

	public Graphics2D getGraphics(Graphics g, int w, int h) {
		
		if (bestOfGeneration.size() > 50 || average.size() > 50 || learningCurve.size() > 50) {
			bestOfGeneration.clear();
			average.clear();
			learningCurve.clear();
			
			bestOfGeneration.add(0);
			average.add(0);
			learningCurve.add(0);
		}
		
		Graphics2D g2 = (Graphics2D) g;

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.fillRect(0, 0, w, h);

		g2.setColor(Color.decode("#D6D6D6"));

		int y_1 = 0, x_1 = 0;

		for (int x = 0; x < (h / PAD) - 1; x++) {
			y_1 += PAD;
			g2.draw(new Line2D.Double(PAD, y_1, w - PAD, y_1));
		}

		for (int x = 0; x < (w / PAD) - 1; x++) {
			x_1 += PAD;
			g2.draw(new Line2D.Double(x_1, PAD, x_1, h - PAD));
		}

		// Draw ordinate.
		g2.draw(new Line2D.Double(PAD, PAD, PAD, h - PAD));
		// Draw abcissa.
		g2.draw(new Line2D.Double(PAD, h - PAD, w - PAD, h - PAD));
		// Draw labels.
		Font font = g2.getFont();
		FontRenderContext frc = g2.getFontRenderContext();
		LineMetrics lm = font.getLineMetrics("0", frc);
		float sh = lm.getAscent() + lm.getDescent();
		// Ordinate label.
		String s = "Score";
		float sy = PAD + ((h - 2 * PAD) - s.length() * sh) / 2 + lm.getAscent();
		g2.setColor(Color.BLACK);
		for (int i = 0; i < s.length(); i++) {
			String letter = String.valueOf(s.charAt(i));
			float sw = (float) font.getStringBounds(letter, frc).getWidth();
			float sx = (PAD - sw) / 2;
			g2.drawString(letter, sx, sy);
			sy += sh;
		}

		drawSubtitle(g2, lm, sh, font, h, w, PAD * 2, Color.BLUE, "Generation's Best");
		drawSubtitle(g2, lm, sh, font, h, w, 220, Color.ORANGE, "Average");
		drawSubtitle(g2, lm, sh, font, h, w, 340, Color.MAGENTA, "Learning Curve");
		// Draw lines.

		g2.setColor(Color.decode("#D6D6D6"));

		drawData(bestOfGeneration, g2, w, h, Color.BLUE, Color.BLUE.darker());
		drawData(average, g2, w, h, Color.ORANGE, Color.ORANGE.darker());
		drawData(learningCurve, g2, w, h, Color.MAGENTA, Color.MAGENTA.darker());
		
		return g2;
	}

	private void drawSubtitle(Graphics2D g2, LineMetrics lm, float sh, Font font, int h, int w, int addX, Color color, String text) {
		g2.setColor(Color.BLACK);
		float sy = h - PAD + (PAD - sh) / 2 + lm.getAscent();
		float sx = PAD + addX;
		g2.drawString(text, sx, sy);

		g2.setColor(color);
		g2.fill(new Ellipse2D.Double(sx - 20, sy - 8, 10, 10));
	}

	private void drawData(ArrayList<Integer> dataList, Graphics2D g2, int w, int h, Color lineColor, Color pointColor) {
		double xInc = (double) (w - 2 * PAD) / (dataList.size() - 1);
		double scale = (double) (h - 2 * PAD) / getMax(dataList);
		g2.setPaint(lineColor);
		for (int i = 0; i < dataList.size() - 1; i++) {
			double x1 = PAD + i * xInc;
			double y1 = h - PAD - scale * dataList.get(i);
			double x2 = PAD + (i + 1) * xInc;
			double y2 = h - PAD - scale * dataList.get(i + 1);
			g2.draw(new Line2D.Double(x1, y1, x2, y2));
		}
		// Mark data points.
		g2.setPaint(pointColor);
		for (int i = 0; i < dataList.size(); i++) {
			double x = PAD + i * xInc;
			double y = h - PAD - scale * dataList.get(i);
			g2.fill(new Ellipse2D.Double(x - 2, y - 2, 4, 4));
		}
	}

	private int getMax(ArrayList<Integer> dataList) {
		int max = -Integer.MAX_VALUE;
		for (int i = 0; i < dataList.size(); i++) {
			if (dataList.get(i) > max)
				max = dataList.get(i);
		}
		return max;
	}

}