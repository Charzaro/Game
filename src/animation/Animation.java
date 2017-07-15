package animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import game.Settings;
/*
 * Animation.java
 * 
 * Creates visuals with no other effect on the game
 */
public abstract class Animation {
	
	// frame number the animation is on
	protected int frame;
	// position
	protected float xpos;
	protected float ypos;
	
	protected Color color;
	
	public boolean active;

	public Animation(float xpos, float ypos, Color c) {
		this.xpos = xpos;
		this.ypos = ypos;
		
		this.color = c;
		
		active = true;
		frame = 0;
	}
	
	// draws the animation
	public abstract void draw(Graphics2D g2);
	
	// increases the frame count
	public void move(){
		frame+= Settings.update_factor;
	}

}
