package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;

public class Bullet implements IProjectile {
	
	private static final boolean SHOW_BOXES = false;
	
	private static final int BULLET_WIDTH = 5;
	private static final int BULLET_HEIGHT = 20;
	
	private static int dimx = 800;
	private static int dimy = 800;
	
	private float xpos;
	private float ypos;
	private float xdist;
	private float ydist;
	
	private float angle;
	private float velocity;
	private float xvol;
	private float yvol;
	private Color color;
	
	public Collision earliestCollision;
	private Collision tempCollision;
	
	public boolean active;

	public Bullet(float startx, float starty, float velocity, float angle, Color color) {
		this.xpos = startx;
		this.ypos = starty;
		this.velocity = velocity;
		this.xvol = velocity*(float)(Math.sin(angle));
		this.yvol = -velocity*(float)(Math.cos(angle));

		this.angle = angle;
		xdist = BULLET_HEIGHT/2 * (float)-Math.sin(angle);
		ydist = BULLET_HEIGHT/2 * (float)Math.cos(angle);
		this.color = color;
		
		earliestCollision = new Collision();
		tempCollision = new Collision();
		
		active = true;
	}
	
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
		return BULLET_WIDTH;
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void deactivate(){
		active = false;
	}
	
	private void dissolveVol(){
		this.xvol = velocity*(float)(Math.sin(angle));
		this.yvol = -velocity*(float)(Math.cos(angle));
	}
	
	public static void setDim(int x, int y){
		dimx = x;
		dimy = y;
	}
	
	public void draw(Graphics2D g2){
		if(active){
			g2.setPaint(color);
			g2.setStroke(new BasicStroke(BULLET_WIDTH));
			g2.drawLine((int)(xpos - xdist), (int)(ypos - ydist), (int)(xpos + xdist), (int)(ypos + ydist));

			if(SHOW_BOXES){
	        	g2.setPaint(Color.BLUE);
	        	g2.draw(getBounds());
	        }
			//g2.fillRect((int)(xpos - radius), (int)(ypos - radius), (int)width, (int)height);
		}
	}
	
	public Rectangle getBounds(){
		float cos = Math.abs((float)Math.cos(angle));
		float sin = Math.abs((float)Math.sin(angle));
		float xd = (BULLET_WIDTH * cos) + (BULLET_HEIGHT * sin);
		float yd = (BULLET_WIDTH * sin) + (BULLET_HEIGHT * cos);
		return new Rectangle((int)(xpos - xd/2), (int)(ypos-yd/2), (int)(xd), (int)(yd));
		//return new Rectangle((int)(xpos-xradius/2), (int)(ypos-yradius/2), (int)(xradius), (int)(yradius));
	}
	
	public void reset(){
		earliestCollision.reset();
		tempCollision.reset();
	}
	
	public float checkBoundaryCollisions(float time){
		Physics.checkBoxCollision(xpos, ypos, xvol, yvol, BULLET_WIDTH, BULLET_HEIGHT, 0, 0, dimx, dimy, time, tempCollision);
		if(tempCollision.t < earliestCollision.t){
			earliestCollision.copy(tempCollision);
			return tempCollision.t;
		}
		return time;
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

}
