package maxtercreations;

import javax.swing.JFrame;

import constants.Constants;
import game.Game;
import game.controller.GameController;

@SuppressWarnings("serial")
public class FlappyBird extends JFrame {

	private static Game game;

	public FlappyBird() {
		super("FlappyBird AI");
		
		setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);

		game = new Game();
		
		addKeyListener(new GameController(game));
		add(game.getGameScreen());
		setVisible(true);
		
		game.getGameScreen().startGame();
	}
	
	public static Game getGame() {
		return game;
	}

	public static void main(String args[]) {
		new FlappyBird();
	}
	
}
