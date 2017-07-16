package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import animation.ExplosionAnimation;

public class Missile extends Projectile {
	
	// turn speed of homing missiles
	private static final float TURN_SPEED = (float) (Math.PI/240); // /96 before
	// speed of homing missiles
	private static final int SPEED = 4;
	
	private Player p;
	
	// position to home towards
	public float homex;
	public float homey;
	// how long the missile travels
	private int duration;

	public Missile(float startx, float starty, float angle, Color color, Player p) {
		super(startx, starty, SPEED, angle, color);
		
		this.p = p;
		bulletWidth = 10;
		bulletHeight = 20;
			
		duration = 400;
	}

	// move the missile
	public void move(float time) {
		if(active){
			// shut off when duration used up
			if(duration <= 0){
				active = false;
				p.fireExplosion((int)xpos, (int)ypos);
				
			}
			else{
				// move forward
				if(earliestCollision.t <= time){
					xpos = earliestCollision.getNewX(xpos, xvol);
					ypos = earliestCollision.getNewY(ypos, yvol);
					active = false;
				}
				else{
					xpos += xvol*time;
					ypos += yvol*time;
				}
				
				// change angle towards the homing position
				
				// goal angle to turn to
				float gangle = Physics.findAngle(xpos, ypos, homex, homey);
				
				
				if(angle < 0){
					angle += 2*Math.PI;
				}
				if(gangle < 0){
					gangle += 2*Math.PI;
				}
				
				double testAng = Math.toDegrees(angle);
				double testGang = Math.toDegrees(gangle);
				// find difference between angles
				gangle = angle - gangle;
				if(gangle < 0){
					gangle += 2*Math.PI;
				}
				// adjust to center around 180
				double testGangAft = Math.toDegrees(gangle);
				// dont turn if goal angle is the same as the current angle, dont rotate
				if(gangle == 0){
					
				}
				// increase angle if goal angle is greater than the angle
				else if(gangle >= Math.PI){
					angle += TURN_SPEED;
					dissolveVol();
					
				}
				// decrease angle if goal angle is less than the angle
				else if(gangle < Math.PI){
					angle -= TURN_SPEED;
					dissolveVol();
				}
				
				duration--;
			
			}
		}
	}
	
	// return a reflected copy
	public Projectile reflectedCopy(Color c){
		return new Missile(xpos, ypos, (float)(angle+Math.PI), c, p);
	}

	// checks if projectile has hit the player
	public boolean checkHit(Player p){
		if(p.getBounds().intersects(getBounds())){
			this.p.addAnimation(new ExplosionAnimation(xpos, ypos, 60, 0.5f, color));
			p.knockback(-4, Physics.findAngle(xpos, ypos, p.getX(), p.getY()));
			return true;
		}
		return false;
	}
	
	@Override
	public int getDamage(){
		return 2;
	}
}

