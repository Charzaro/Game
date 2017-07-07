package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public abstract class Projectile {
	
	
	protected static final boolean SHOW_BOXES = false;
	
	protected int bulletWidth;
	protected int bulletHeight;
	
	protected static int dimx = 800;
	protected static int dimy = 800;
	
	protected float xpos;
	protected float ypos;
	protected float xdist;
	protected float ydist;
	
	protected float angle;
	protected float velocity;
	protected float xvol;
	protected float yvol;
	protected Color color;
	
	public Collision earliestCollision;
	protected Collision tempCollision;
	
	public boolean active;

	public Projectile(float startx, float starty, float velocity, float angle, Color color) {
		this.xpos = startx;
		this.ypos = starty;
		this.velocity = velocity;
		this.xvol = velocity*(float)(Math.sin(angle));
		this.yvol = -velocity*(float)(Math.cos(angle));

		this.angle = angle;
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
		return bulletWidth;
	}
	
	public boolean isActive(){
		return active;
	}
	
	public void deactivate(){
		active = false;
	}
	
	protected void dissolveVol(){
		this.xvol = velocity*(float)(Math.sin(angle));
		this.yvol = -velocity*(float)(Math.cos(angle));
	}
	
	public static void setDim(int x, int y){
		dimx = x;
		dimy = y;
	}
	
	public void draw(Graphics2D g2){
		if(active){
			xdist = bulletHeight/2 * (float)-Math.sin(angle);
			ydist = bulletHeight/2 * (float)Math.cos(angle);
			g2.setPaint(color);
			g2.setStroke(new BasicStroke(bulletWidth));
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
		float xd = (bulletWidth * cos) + (bulletHeight * sin);
		float yd = (bulletWidth * sin) + (bulletHeight * cos);
		return new Rectangle((int)(xpos - xd/2), (int)(ypos-yd/2), (int)(xd), (int)(yd));
		//return new Rectangle((int)(xpos-xradius/2), (int)(ypos-yradius/2), (int)(xradius), (int)(yradius));
	}
	
	public void reset(){
		earliestCollision.reset();
		tempCollision.reset();
	}
	
	public float checkBoundaryCollisions(float time){
		Physics.checkBoxCollision(xpos, ypos, xvol, yvol, bulletWidth, bulletHeight, 0, 0, dimx, dimy, time, tempCollision);
		if(tempCollision.t < earliestCollision.t){
			earliestCollision.copy(tempCollision);
			return tempCollision.t;
		}
		return time;
	}
	
	public boolean checkHit(Player p){
		return p.getBounds().intersects(getBounds());
	}
	
	public int getDamage(){
		return 1;
	}
	
	public abstract Projectile reflectedCopy(Color c);
	
	public abstract void move(float time);
	
}