package chromedino.neuralnetwork;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.DecimalFormat;

public class LiveView {

	public static void paintNeuralNetwork(Graphics2D g, NeuralNetwork neuralNetwork) {
		nn = neuralNetwork;

		boolean jump = nn.neurons[2][0] > 0.5;
		boolean squat = nn.neurons[2][1] > 0.5;
		boolean useAirplane = nn.neurons[2][2] > 0.5;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		for (int i = 0; i < nn.layers_amount; i++) {
			int currentNeuronHeight = (nn.neurons_amount[i] - 1) * VERTICAL_SPACE_BETWEEN_NEURONS + nn.neurons_amount[i] * NEURONS_DIAMETER + PANEL_PADDING * 2;
			int topMargin = (height - currentNeuronHeight) / 2;

			// Draw neurons[i]
			for (int l = 0; l < nn.neurons_amount[i]; l++) {
				// Draw synapses[current_genome][i][l]
				g.setColor(Color.LIGHT_GRAY);

				if (i > 0) {
					if (l + 1 < nn.neurons_amount[i] && nn.neurons[i][l] > 0.5 && nn.neurons[i][l + 1] > 0.5) {
						g.setColor(Color.decode("#FFC3C3"));
					}
				} else {
					if (i + 1 < nn.layers_amount && nn.neurons[i + 1][l] > 0.5) {
						g.setColor(Color.decode("#FFC3C3"));
					}
				}

				g.setFont(new Font("Arial", Font.PLAIN, 12));

				if (i != nn.layers_amount - 1) {
					int nextNeuronHeight = (nn.neurons_amount[i + 1] - 1) * VERTICAL_SPACE_BETWEEN_NEURONS + nn.neurons_amount[i + 1] * NEURONS_DIAMETER + PANEL_PADDING * 2; // Height of the next neurons (including paddings)
					int nextTopMargin = (height - nextNeuronHeight) / 2, m;
					
					if (i + 1 != nn.layers_amount - 1) {
						m = nn.neurons_amount[i + 1] - 1;
					} else {
						m = nn.neurons_amount[i + 1];
					}
					
					for (int n = 0; n < m; n++) {
						g.drawLine(baseX + NEURONS_DIAMETER / 2 + PANEL_PADDING + NEURONS_DIAMETER * i + HORIZONTAL_SPACE_BETWEEN_NEURONS * i, baseY + NEURONS_DIAMETER / 2 + PANEL_PADDING + topMargin + NEURONS_DIAMETER * l + VERTICAL_SPACE_BETWEEN_NEURONS * l, baseX + NEURONS_DIAMETER / 2 + PANEL_PADDING + NEURONS_DIAMETER * (i + 1) + HORIZONTAL_SPACE_BETWEEN_NEURONS * (i + 1), baseY + nextTopMargin - topMargin + NEURONS_DIAMETER / 2 + PANEL_PADDING + topMargin + NEURONS_DIAMETER * n + VERTICAL_SPACE_BETWEEN_NEURONS * n);
					}
				}

				g.setColor(Color.LIGHT_GRAY);

				if (i > 0) {
					if (nn.neurons[i][l] > 0.5)
						g.setColor(activeColor);
				} else {
					
					if (nn.neurons[i + 1][l] > 0.5)
						g.setColor(activeColor);

					String value = getTitle(l);

					g.setColor(Color.BLACK);
					g.setFont(new Font("Arial", Font.PLAIN, 12));
					
					value += df.format(nn.neurons[i][l]);
					
					g.drawString(value, baseX - 200, baseY + NEURONS_DIAMETER / 2 + 7 + PANEL_PADDING + topMargin + NEURONS_DIAMETER * l + VERTICAL_SPACE_BETWEEN_NEURONS * l);

					g.setColor(Color.LIGHT_GRAY);
				}

				if (i == nn.layers_amount - 1) {

					if (l == 0 && jump) {
						g.setColor(activeColor);
					} else if (l == 1 && squat) {
						g.setColor(activeColor);
					} else if (l == 2 && useAirplane) {
						g.setColor(activeColor);
					}

				}

				g.fillOval(baseX + PANEL_PADDING + NEURONS_DIAMETER * i + HORIZONTAL_SPACE_BETWEEN_NEURONS * i, baseY + PANEL_PADDING + topMargin + NEURONS_DIAMETER * l + VERTICAL_SPACE_BETWEEN_NEURONS * l, NEURONS_DIAMETER, NEURONS_DIAMETER);
			}
		}

		g.setColor(Color.LIGHT_GRAY);

		g.setFont(new Font("Arial", Font.PLAIN, 15));
		if (jump)
			g.setColor(activeColor);
		g.drawString("Jump", baseX + width + PANEL_PADDING + 100, baseY - 20);

		g.setColor(Color.LIGHT_GRAY);

		if (squat)
			g.setColor(activeColor);
		g.drawString("Down", baseX + width + PANEL_PADDING + 100, baseY + (height / 2) + 5);

		g.setColor(Color.LIGHT_GRAY);

		if (useAirplane)
			g.setColor(activeColor);

		g.drawString("Airplane", baseX + width + PANEL_PADDING + 100, baseY + (height / 2) + 30);

		g.setColor(Color.LIGHT_GRAY);
	}
	
	private static String getTitle(int l) {
		String value = "";

		if (l == 0)
			value = "Distance: ";
		if (l == 1)
			value = "Width [Obst]: ";
		if (l == 2)
			value = "Height [Obst]: ";
		if (l == 3)
			value = "Vertical Position [Obst]: ";
		if (l == 4)
			value = "Speed: ";
		if (l == 5)
			value = "Vertical Position: ";
		
		return value;
	}

	private static NeuralNetwork nn;
	
	private static DecimalFormat df = new DecimalFormat("#0.00");
	private static Color activeColor = Color.decode("#FF6C6C");

	private static final int NEURONS_DIAMETER = 15, HORIZONTAL_SPACE_BETWEEN_NEURONS = 15, VERTICAL_SPACE_BETWEEN_NEURONS = 10, PANEL_PADDING = 5;
	private static int width, height, baseX = 800, baseY = 130;
	

}