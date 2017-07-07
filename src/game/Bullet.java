package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

public class Bullet extends Projectile {

	public Bullet(float startx, float starty, float velocity, float angle, Color color) {
		super(startx, starty, velocity, angle, color);
		bulletWidth = 5;
		bulletHeight = 20;
	}
	
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
	
	public Projectile reflectedCopy(Color c){
		return new Bullet(xpos, ypos, velocity, (float)(angle+Math.PI), c);
	}

}
