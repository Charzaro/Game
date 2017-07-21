package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
/*
 * Projectile.java
 * 
 * Abstract class for creating new projectile types
 */

public abstract class Projectile {
	
	// For debug: show hitboxes
	protected static final boolean SHOW_BOXES = false;
	
	// length and width of projectile
	protected int bulletWidth;
	protected int bulletHeight;
	
	// position
	protected float xpos;
	protected float ypos;
	
	// various stats of the projectile
	protected float angle;
	protected float velocity;
	protected float xvol;
	protected float yvol;
	protected Color color;
	
	// holds collisions
	public Collision earliestCollision;
	protected Collision tempCollision;
	
	// true when can damage and be seen, false when done
	public boolean active;

	public Projectile(float startx, float starty, float velocity, float angle, Color color) {
		// set starting values
		this.xpos = startx;
		this.ypos = starty;
		if(velocity <= 2){
			this.velocity = 2;
		}
		else{
			this.velocity = velocity;
		}
		
		// dissolve velocities along axis
		this.xvol = this.velocity*(float)(Math.sin(angle));
		this.yvol = -this.velocity*(float)(Math.cos(angle));

		this.angle = angle;
		this.color = color;
		
		earliestCollision = new Collision();
		tempCollision = new Collision();
		
		active = true;
	}

	// GETTERS
	public float getX(){
		return xpos;
	}
	
	public float getY(){
		return ypos;
	}
	
	public float getXvol(){
		return xvol;
	}
	
	public float getYvol(){
		return yvol;
	}
	
	public float getRadius(){
		return bulletWidth;
	}
	
	public boolean isActive(){
		return active;
	}
	
	// SETTER
	public void deactivate(){
		active = false;
	}
	
	// dissolves current velocity along x and y axis
	protected void dissolveVol(){
		this.xvol = velocity*(float)(Math.sin(angle));
		this.yvol = -velocity*(float)(Math.cos(angle));
	}
	
	
	// draws the projectile
	public void draw(Graphics2D g2){
		if(active){
			// trig x and y distances from center of bullet, creates two ends of a line to draw
			float xdist = bulletHeight/2 * (float)-Math.sin(angle);
			float ydist = bulletHeight/2 * (float)Math.cos(angle);
			g2.setPaint(color);
			g2.setStroke(new BasicStroke(bulletWidth));
			// draw a line for the bullet
			g2.drawLine((int)(xpos - xdist), (int)(ypos - ydist), (int)(xpos + xdist), (int)(ypos + ydist));

			if(SHOW_BOXES){
	        	g2.setPaint(Color.BLUE);
	        	g2.draw(getBounds());
	        }
		}
	}
	
	// gets hitbox for bullet
	public Rectangle getBounds(){
		// draws a rectangle around the bullet, should be changed eventually to draw a rotated rectangle
		float cos = Math.abs((float)Math.cos(angle));
		float sin = Math.abs((float)Math.sin(angle));
		float xd = (bulletWidth * cos) + (bulletHeight * sin);
		float yd = (bulletWidth * sin) + (bulletHeight * cos);
		return new Rectangle((int)(xpos - xd/2), (int)(ypos-yd/2), (int)(xd), (int)(yd));
	}
	
	// resets collisions
	public void reset(){
		earliestCollision.reset();
		tempCollision.reset();
	}
	
	
	// checks if bullet has hit the borders of the game area
	public float checkBoundaryCollisions(float time){
		Physics.checkBoxCollision(xpos, ypos, xvol, yvol, bulletWidth, bulletHeight, 0, 0, Settings.getDimx(), Settings.getDimy(), time, tempCollision);
		if(tempCollision.t < earliestCollision.t){
			earliestCollision.copy(tempCollision);
			return tempCollision.t;
		}
		return time;
	}
	
	public float checkObstacleCollision(float time, Obstacle o){
		float earliestTime = time;
		if(o.health <= 0){
			return time;
		}
		if(o.breakable && this.getBounds().intersects(o.getRect())){
			o.health -= getDamage();
		}
		boolean insideY =  ypos < o.ypoints[1] + 1 && ypos > o.ypoints[0] - 1;
		boolean insideX =  xpos < o.xpoints[1] + 1 && xpos > o.xpoints[0] - 1;
		if(insideY){
			Physics.checkVerticalLine(xpos, xvol, yvol, 1, o.xpoints[0], time, tempCollision);
			if(tempCollision.t < earliestCollision.t){
				earliestCollision.copy(tempCollision);
				earliestTime = tempCollision.t;
			}
			Physics.checkVerticalLine(xpos, xvol, yvol, 1, o.xpoints[1], time, tempCollision);
			if(tempCollision.t < earliestCollision.t){
				earliestCollision.copy(tempCollision);
				earliestTime = tempCollision.t;
			}
		}
		if(insideX){
			Physics.checkHorizontalLine(ypos, xvol, yvol, 1, o.ypoints[0], time, tempCollision);
			if(tempCollision.t < earliestCollision.t){
				earliestCollision.copy(tempCollision);
				earliestTime = tempCollision.t;
			}
			Physics.checkHorizontalLine(ypos, xvol, yvol, 1, o.ypoints[1], time, tempCollision);
			if(tempCollision.t < earliestCollision.t){
				earliestCollision.copy(tempCollision);
				earliestTime = tempCollision.t;
			}
		}

		return earliestTime;
	}
	
	// checks if projectile has hit the player
	public boolean checkHit(Player p){
		return p.getBounds().intersects(getBounds());
	}
	
	// return the amount of damage done by this bullet
	public int getDamage(){
		return 1;
	}
	
	// returns a copy of this projectile but in the opposite direction and as the new color
	// different projectiles must be reflected differently
	public abstract Projectile reflectedCopy(Color c);
	
	// moves the projectile forward one time step
	public abstract void move(float time);
	
}