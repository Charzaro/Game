package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Missile implements IProjectile {
	
	private static final boolean SHOW_BOXES = false;

	private static final int MISSILE_WIDTH = 10;
	private static final int MISSILE_HEIGHT = 20;
	
	private static int dimx = 800;
	private static int dimy = 800;
	
	private float xpos;
	private float ypos;
	private float xdist;
	private float ydist;
	
	public float homex;
	public float homey;
	private int duration;
	
	private float angle;
	private float velocity;
	private float xvol;
	private float yvol;
	private Color color;
	
	public Collision earliestCollision;
	private Collision tempCollision;
	
	public boolean active;

	public Missile(float startx, float starty, float velocity, float angle, Color color) {
		this.xpos = startx;
		this.ypos = starty;
		this.velocity = velocity;
		this.xvol = velocity*(float)(Math.sin(angle));
		this.yvol = -velocity*(float)(Math.cos(angle));
		
		this.angle = angle;
		xdist = MISSILE_HEIGHT/2 * (float)-Math.sin(angle);
		ydist = MISSILE_HEIGHT/2 * (float)Math.cos(angle);
		this.color = color;
		
		duration = 400;
		
		earliestCollision = new Collision();
		tempCollision = new Collision();
		
		active = true;
	}
	
	public static void setDim(int x, int y){
		dimx = x;
		dimy = y;
	}
	
	public boolean isActive() {
		return active;
	}

	public void deactivate() {
		active = false;
	}
	
	public void reset() {
		earliestCollision.reset();
		tempCollision.reset();
	}
	
	private void dissolveVol(){
		this.xvol = (float)(velocity*(Math.sin(angle)));
		this.yvol = (float)(-velocity*(Math.cos(angle)));
		
		xdist = MISSILE_HEIGHT/2 * (float)-Math.sin(angle);
		ydist = MISSILE_HEIGHT/2 * (float)Math.cos(angle);
	}
	
	public Rectangle getBounds() {
		float cos = Math.abs((float)Math.cos(angle));
		float sin = Math.abs((float)Math.sin(angle));
		float xd = (MISSILE_WIDTH * cos) + (MISSILE_HEIGHT * sin);
		float yd = (MISSILE_WIDTH * sin) + (MISSILE_HEIGHT * cos);
		return new Rectangle((int)(xpos - xd/2), (int)(ypos-yd/2), (int)(xd), (int)(yd));
		//return new Rectangle((int)(xpos-xradius/2),
	}
	
	public void draw(Graphics2D g2) {
		if(active){
			g2.setPaint(color);
			g2.setStroke(new BasicStroke(MISSILE_WIDTH));
			g2.drawLine((int)(xpos - xdist), (int)(ypos - ydist), (int)(xpos + xdist), (int)(ypos + ydist));

			if(SHOW_BOXES){
	        	g2.setPaint(Color.BLUE);
	        	g2.draw(getBounds());
	        }
			//g2.fillRect((int)(xpos - radius), (int)(ypos - radius), (int)width, (int)height);
		}
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
					angle += Math.PI/96;
					dissolveVol();
					
				}
				else if(gangle < Math.PI){
					angle -= Math.PI/96;
					dissolveVol();
				}
				
				duration--;
			
			}
		}
	}

	public float checkBoundaryCollisions(float time){
		Physics.checkBoxCollision(xpos, ypos, xvol, yvol, MISSILE_WIDTH, MISSILE_HEIGHT, 0, 0, dimx, dimy, time, tempCollision);
		if(tempCollision.t < earliestCollision.t){
			earliestCollision.copy(tempCollision);
			return tempCollision.t;
		}
		return time;
	}

}
