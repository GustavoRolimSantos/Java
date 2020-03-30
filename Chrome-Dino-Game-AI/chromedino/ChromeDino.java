package chromedino;

import javax.swing.JFrame;

import chromedino.constants.Constants;
import chromedino.display.GameScreen;

@SuppressWarnings("serial")
public class ChromeDino extends JFrame {

	private static GameScreen gameScreen;

	public ChromeDino() {
		super("ChromeDino");
		setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		gameScreen = new GameScreen();
		
		addKeyListener(gameScreen);
		add(gameScreen);
	}

	public void startGame() {
		setVisible(true);
		gameScreen.startGame();
	}
	
	public static GameScreen getGameScreen() {
		return gameScreen;
	}

	public static void main(String args[]) {
		new ChromeDino().startGame();
	}
	
}
