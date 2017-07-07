package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Missile extends Projectile {
	
	private static final float TURN_SPEED = (float) (Math.PI/240); // /96 before
	private static final int SPEED = 3;
	
	public float homex;
	public float homey;
	private int duration;

	public Missile(float startx, float starty, float velocity, float angle, Color color) {
		super(startx, starty, SPEED, angle, color);
		bulletWidth = 10;
		bulletHeight = 20;
			
		duration = 400;
	}

	public void move(float time) {
		if(active){
			if(duration <= 0){
				active = false;
			}
			else{
				if(earliestCollision.t <= time){
					xpos = earliestCollision.getNewX(xpos, xvol);
					ypos = earliestCollision.getNewY(ypos, yvol);
					active = false;
				}
				else{
					xpos += xvol*time;
					ypos += yvol*time;
				}
				
				float dx = -1*(homex - xpos);
				float dy = homey - ypos;
				float gangle = (float)Math.atan(dx/dy);
				//double test = Math.toDegrees(gangle);
				//double test2;
				if(dy < 0){
					gangle += Math.PI;
				}
				
				//test = Math.toDegrees(gangle);
				//test2 = Math.toDegrees(angle);
				//System.out.println(Math.toDegrees(gangle));
				
				gangle -= angle;
				if(gangle < 0){
					gangle += 2*Math.PI;
				}

				if(gangle == 0){
					
				}
				else if(gangle >= Math.PI){
					angle += TURN_SPEED;
					dissolveVol();
					
				}
				else if(gangle < Math.PI){
					angle -= TURN_SPEED;
					dissolveVol();
				}
				
				duration--;
			
			}
		}
	}
	
	public Projectile reflectedCopy(Color c){
		return new Missile(xpos, ypos, velocity, (float)(angle+Math.PI), c);
	}
}

