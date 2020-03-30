package chromedino.neuralnetwork;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import chromedino.constants.Constants;

public class SaveLoad {

	private final NeuralNetwork neuralNetwork;

	private String file_name = "data/synapses/";
	private File file = new File(file_name);

	private boolean first_line = true;

	public SaveLoad(NeuralNetwork nn) {

		this.neuralNetwork = nn;
	}

	protected boolean fileExists() {
		updateFile();

		return file.exists() && !file.isDirectory();
	}

	public void saveToFile(int score) {	
		if (Constants.BATTLE_MODE)
			return;
		
		int biggerScore = updateFile();
		
		if (file != null && score >= biggerScore) {
			save(score, file);
		}
	}

	private int updateFile() {
		int bigger = 0;
		File current = null;
		for (File file : new File(file_name).listFiles()) {
			try {
				Integer lastScore = Integer.valueOf(file.getName().split("_")[0]);

				if (lastScore >= bigger) {
					current = file;
					bigger = lastScore;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		if (current != null)
			file = current;
		
		return bigger;
	}

	private void save(int score, File lastFile) {
		this.file = new File(file_name + score + "_synapses.txt");
		try {
			try (PrintWriter writer = new PrintWriter(new FileOutputStream(file, true))) {
				if (!first_line) {
					writer.print("\n");
				} else {
					first_line = false;
				}

				// Append the current generation at the end of the file
				for (int l = 0; l < neuralNetwork.genomes_per_generation; l++) {
					for (int i = 0; i < neuralNetwork.layers_amount - 1; i++) {
						for (int j = 0; j < neuralNetwork.neurons_amount[i]; j++) {
							int m;
							if (i + 1 != neuralNetwork.layers_amount - 1) {
								m = neuralNetwork.neurons_amount[i + 1] - 1;
							} else {
								m = neuralNetwork.neurons_amount[i + 1];
							}
							for (int k = 0; k < m; k++) {
								writer.print(neuralNetwork.synapses[l][i][j][k] + " ");
							}
						}
					}
				}
			}
			lastFile.delete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void loadFromFile() throws FileNotFoundException, IOException {
		String current_line, last_generation = null;

		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(file));

		// Get the last line and store it into last_generation
		while ((current_line = reader.readLine()) != null) {
			last_generation = current_line;
		}

		// Split the string and store the numbers (as text)
		List<String> values = Arrays.asList(last_generation.trim().split(" "));

		// Init the synapsis
		int n = 0;
		for (int l = 0; l < neuralNetwork.genomes_per_generation; l++) {
			for (int i = 0; i < neuralNetwork.layers_amount - 1; i++) {
				for (int j = 0; j < neuralNetwork.neurons_amount[i]; j++) {
					int m;
					if (i + 1 != neuralNetwork.layers_amount - 1) {
						m = neuralNetwork.neurons_amount[i + 1] - 1;
					} else {
						m = neuralNetwork.neurons_amount[i + 1];
					}
					for (int k = 0; k < m; k++) {
						neuralNetwork.synapses[l][i][j][k] = Double.parseDouble(values.get(n));
						n++;
					}
				}
			}
		}
	}
}
