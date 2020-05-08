package game.display.enemies.model;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class Enemy {
	
	public abstract void update();

	public abstract void draw(Graphics g);

	public abstract Rectangle getBound();

	public abstract boolean isOutOfScreen();
	
	public abstract int score();
	
	public abstract void reset();
	
}
