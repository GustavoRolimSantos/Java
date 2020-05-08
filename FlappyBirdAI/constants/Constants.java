package constants;

public class Constants {
	
	public static final boolean BATTLE_MODE = false;
	public static final boolean SHOW_COLLISION_AREA = false;
	
	public static final int SCREEN_WIDTH = 1280, SCREEN_HEIGHT = 335;

	public static final double START_SPEED = 3, ACCELERATION = 0.001;

	public static int START_POPULATION = 2000;
	
	public enum GameState {
		START, PLAYING, GAME_OVER
	}
	
}
