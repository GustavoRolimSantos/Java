package chromedino.display.metrics;

import chromedino.display.GameScreen;

public class GraphicData {
	
	private GameScreen gameScreen;
	
	public GraphicData(GameScreen gameScreen) {
		this.gameScreen = gameScreen;
	}

	public void updateLearningCurve() {
		if (gameScreen.getBestDinosaur() != null) {

			gameScreen.getDied().get(gameScreen.getDied().size() - 1).getNeuralNetwork().getSaveLoad().saveToFile(gameScreen.getBestDinosaur().score);

			if (gameScreen.getDataMetrics().learningCurve.isEmpty() || (gameScreen.bestScore > gameScreen.getDataMetrics().learningCurve.get(gameScreen.getDataMetrics().learningCurve.size() - 1))) {
				gameScreen.getDataMetrics().learningCurve.add(gameScreen.bestScore);
			}
		}
	}

	public void updateAverage() {
		gameScreen.getDataMetrics().bestOfGeneration.add(gameScreen.getDied().get(gameScreen.getDied().size() - 1).score);

		int average = 0;

		if (gameScreen.getDied().size() > 10) {
			for (int i = 11; i > 1; i--) {
				average += gameScreen.getDied().get(gameScreen.getDied().size() - i).getScore();
			}

			average /= 10;
		} else {
			for (int i = 0; i < gameScreen.getDied().size(); i++) {
				average += gameScreen.getDied().get(i).getScore();
			}

			average /= gameScreen.getDied().size();
		}

		gameScreen.getDataMetrics().average.add(average);
	}
	
}
