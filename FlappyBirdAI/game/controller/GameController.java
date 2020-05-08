package game.controller;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import constants.Constants;
import constants.Constants.GameState;
import game.Game;
import game.display.Player;

public class GameController extends KeyAdapter {

	private Game game;

	public GameController(Game gameScreen) {
		this.game = gameScreen;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!game.getGameState().equals(GameState.PLAYING))
			return;

		if (Constants.BATTLE_MODE) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				for (Player player : game.getPlayers()) {
					if (!player.isAlive()) {
						player.setAlive(false);
					}
				}
			}
		}

		for (Player player : game.getPlayers()) {
			if (player.isPlayingAsHuman()) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE)
					player.jump();
			}
		}

	}

}
