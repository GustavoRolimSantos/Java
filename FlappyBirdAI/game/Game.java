package game;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import constants.Constants;
import constants.Constants.GameState;
import game.display.Player;
import maxtercreations.FlappyBird;

public class Game {

	private ArrayList<Player> players = new ArrayList<>();

	private GameState gameState = GameState.START;

	private Player bestPlayer;
	private String lastBestPlayer;
	private int deaths, bestScore, bestDistance;

	private double velocity = Constants.START_SPEED;

	private GameScreen gameScreen;

	public Game() {
		gameScreen = new GameScreen(this);
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void addPlayer(Player player) {
		this.players.add(player);
	}

	public List<Player> getDied() {
		return players.stream().filter(dino -> !dino.isAlive()).collect(Collectors.toList());
	}

	public GameState getGameState() {
		return gameState;
	}

	public void setGameState(GameState gameState) {
		this.gameState = gameState;
	}

	public Player getBestPlayer() {
		return bestPlayer;
	}

	public int getBestScore() {
		return bestScore;
	}

	public int getBestDistance() {
		return bestDistance;
	}

	public void updateBestPlayer(Player player) {
		if (player.getScore() > bestScore) {
			bestScore = player.getScore();
			bestDistance = FlappyBird.getGame().getGameScreen().getGround().getScreenWidth();
		}
		
		bestPlayer = player;
		lastBestPlayer = toString();
	}

	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public void setBestPlayer(Player player) {
		bestPlayer = player;
	}

	public String getFormattedScore() {
		return bestPlayer != null ? String.valueOf(bestPlayer.getScore()) : "0";
	}
	
	public double getVelocity() {
		return velocity;
	}
	
	public void increaseVelocity() {
		velocity += Constants.ACCELERATION;
	}
	
	public void resetVelocity() {
		velocity = Constants.START_SPEED;
		deaths = 0;
	}
	
	public void increaseDeaths() {
		deaths++;
	}
	
	public int getDeaths() {
		return deaths;
	}

	@Override
	public String toString() {
		return bestPlayer == null ? (lastBestPlayer == null ? "Unknown" : lastBestPlayer)
				: "" + (bestPlayer.getNeuralNetwork().current_generation + 1) + " ["
						+ (bestPlayer.getNeuralNetwork().current_genome + 1) + "/"
						+ bestPlayer.getNeuralNetwork().genomes_per_generation + " genome]";
	}

}
