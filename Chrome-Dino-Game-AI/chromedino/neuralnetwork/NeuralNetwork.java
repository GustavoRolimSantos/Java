package chromedino.neuralnetwork;

import static java.lang.Math.exp;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import chromedino.constants.Constants;

public class NeuralNetwork {

	protected int layers_amount;
	private double fits[]; // [genome_index]
	protected int neurons_amount[]; // [layer_index]. Must be >= 2 (inputs and outputs). This is also used to
									// calculate the synapses amount
	private double inputs[];
	public double neurons[][]; // [layer_index][neuron_index]
	protected double synapses[][][][]; // [genome_index][layer_index][neuron_index][synapses_index]
	public int genomes_per_generation;
	public int current_genome = 0;
	public int current_generation = 0;
	private double random_mutation_probability;
	private double min_weight, max_weight;

	private SaveLoad save_load;

	public NeuralNetwork(int dinosaurId, int neurons_amount[], int genomes_per_generation, double random_mutation_probability, double min_weight, double max_weight) {
		// Copy costructor parameters

		this.neurons_amount = neurons_amount;
		this.genomes_per_generation = genomes_per_generation;
		this.random_mutation_probability = random_mutation_probability;
		this.min_weight = min_weight;
		this.max_weight = max_weight;

		layers_amount = neurons_amount.length;

		// Create fits array
		fits = new double[genomes_per_generation];

		// Generate neurons
		neurons = new double[layers_amount][];
		for (int i = 0; i < layers_amount; i++) {
			if (i != layers_amount - 1) {
				neurons_amount[i]++; // The last neuron is the bias.
			}
			neurons[i] = new double[neurons_amount[i]];
		}

		// Set biases to 1
		for (int i = 0; i < layers_amount - 1; i++) {
			neurons[i][neurons_amount[i] - 1] = 1;
		}

		// Generate synapses
		synapses = new double[genomes_per_generation][][][];
		for (int k = 0; k < genomes_per_generation; k++) {
			synapses[k] = new double[layers_amount - 1][][];
			for (int i = 0; i < layers_amount - 1; i++) {
				synapses[k][i] = new double[neurons_amount[i]][];
				for (int j = 0; j < neurons_amount[i]; j++) {
					if (i + 1 != layers_amount - 1) {
						synapses[k][i][j] = new double[neurons_amount[i + 1] - 1];
					} else {
						synapses[k][i][j] = new double[neurons_amount[i + 1]];
					}
				}
			}
		}

		save_load = new SaveLoad(this);

		// If the file exists, load it. Else, init randomly
		if (save_load.fileExists()) {
			try {
				save_load.loadFromFile();
				
				if (!Constants.BATTLE_MODE) {
					crossover();
				}
			} catch (IOException ex) {
				Logger.getLogger(NeuralNetwork.class.getName()).log(Level.SEVERE, null, ex);
			}
		} else {
			initSynapsesRandomly();
		}
	}

	private void initSynapsesRandomly() {
		for (int l = 0; l < genomes_per_generation; l++) {
			for (int i = 0; i < layers_amount - 1; i++) {
				for (int j = 0; j < neurons_amount[i]; j++) {
					int m;
					if (i + 1 != layers_amount - 1) {
						m = neurons_amount[i + 1] - 1;
					} else {
						m = neurons_amount[i + 1];
					}
					for (int k = 0; k < m; k++) {
						synapses[l][i][j][k] = randDouble(min_weight, max_weight);
					}
				}
			}
		}
	}

	public SaveLoad getSaveLoad() {
		return save_load;
	}

	public double[] getOutputs(double inputs[]) {
		// Copy function parameters
		this.inputs = inputs;

		setNeuronsValues();

		// Return outputs
		return neurons[layers_amount - 1];
	}

	public void newGenome(double current_genome_fit) {
		// Set current genome fit
		fits[current_genome] = current_genome_fit;

		// If all genomes have been executed, create a new generation
		if (current_genome + 1 == genomes_per_generation) {
			current_genome = 0;
			newGeneration();
		} else {
			current_genome++;
		}
	}

	private void newGeneration() {
		boolean no_progress = true;

		// Check if the NN made any progress
		for (int i = 0; i < genomes_per_generation; i++) {
			if (fits[i] != 0) {
				no_progress = false;
				break;
			}
		}

		if (no_progress) {
			initSynapsesRandomly();
		} else {
			crossover();
		}

		current_generation++;
	}

	private void crossover() {
		// Sort
		int j_max;
		double fit_temp, synapses_temp[][][];
		for (int i = 0; i < genomes_per_generation - 1; i++) {
			j_max = i;
			for (int j = i + 1; j < genomes_per_generation; j++) {
				if (fits[j] > fits[j_max]) {
					j_max = j;
				}
			}
			if (j_max != i) {
				fit_temp = fits[i];
				synapses_temp = synapses[i];
				fits[i] = fits[j_max];
				synapses[i] = synapses[j_max];
				fits[j_max] = fit_temp;
				synapses[j_max] = synapses_temp;
			}
		}

		// The best genome is now the first. We mix it with all the other genomes
		double prob_rand;
		for (int l = 1; l < genomes_per_generation; l++) {
			for (int i = 0; i < layers_amount - 1; i++) {
				for (int j = 0; j < neurons_amount[i]; j++) {
					int m;
					if (i + 1 != layers_amount - 1) {
						m = neurons_amount[i + 1] - 1;
					} else {
						m = neurons_amount[i + 1];
					}
					for (int k = 0; k < m; k++) {
						// If this genome made any progress, mix it with the first genome or generate a
						// new number randomly or keep the current value
						if (fits[l] != 0) {
							prob_rand = randDouble(0, 1);
							if (prob_rand < random_mutation_probability) {
								synapses[l][i][j][k] = randDouble(min_weight, max_weight);
							} else {
								prob_rand = randDouble(0, 1);
								if (prob_rand < 0.5) {
									synapses[l][i][j][k] = synapses[0][i][j][k];
								}
								// Else keep the current value (implicit)
							}
						}
						// Else mix it with the first genome or generate a new number randomly
						else {
							prob_rand = randDouble(0, 1);
							if (prob_rand < random_mutation_probability) {
								synapses[l][i][j][k] = randDouble(min_weight, max_weight);
							} else {
								synapses[l][i][j][k] = synapses[0][i][j][k];
							}
						}
					}
				}
			}
		}
	}

	private void setNeuronsValues() {
		// Copy inputs
		for (int i = 0; i < neurons_amount[0] - 1; i++) {
			neurons[0][i] = inputs[i];
		}

		// Init the other neurons to 0
		for (int i = 1; i < layers_amount; i++) {
			int m;
			if (i + 1 != layers_amount) {
				m = neurons_amount[i] - 1;
			} else {
				m = neurons_amount[i];
			}
			for (int j = 0; j < m; j++) {
				neurons[i][j] = 0;
			}
		}

		// Multiply neurons and synapses and sum the results (calculate the next neurons
		// values)
		for (int i = 1; i < layers_amount; i++) {
			int m;
			if (i != layers_amount - 1) {
				m = neurons_amount[i] - 1;
			} else {
				m = neurons_amount[i];
			}
			for (int j = 0; j < m; j++) {
				for (int k = 0; k < neurons_amount[i - 1]; k++) {
					neurons[i][j] += neurons[i - 1][k] * synapses[current_genome][i - 1][k][j];
				}

				// Activation function
				neurons[i][j] = sigmoid(neurons[i][j]);
			}
		}
	}

	private double sigmoid(double x) {
		return 1 / (1 + exp(-x));
	}

	private double randDouble(double min, double max) {
		Random r = new Random();
		return min + (max - min) * r.nextDouble();
	}
}
