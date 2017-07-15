package game;

import java.awt.Color;
/*
 * Bullet.java
 * 
 * A basic bullet projectile
 */

public class Bullet extends Projectile {

	public Bullet(float startx, float starty, float velocity, float angle, Color color) {
		super(startx, starty, velocity, angle, color);
		// bullets are 5 by 20
		bulletWidth = 5;
		bulletHeight = 20;
	}
	
	// move according to time slot, a collision deactives the bullet
	public void move(float time){
		if(active){
			if(earliestCollision.t <= time){
				xpos = earliestCollision.getNewX(xpos, xvol);
				ypos = earliestCollision.getNewY(ypos, yvol);
				active = false;
			}
			else{
				xpos += xvol*time;
				ypos += yvol*time;
			}
		}
		
	}
	
	// gives a reflected bullet
	public Projectile reflectedCopy(Color c){
		return new Bullet(xpos, ypos, velocity, (float)(angle+Math.PI), c);
	}

}
