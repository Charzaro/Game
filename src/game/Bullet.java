package game;

import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet implements IProjectile {
	private float xpos;
	private float ypos;
	private float radius;
	private float xvol;
	private float yvol;
	private Color color;
	
	private Collision earliestCollision;
	private Collision tempCollision;
	
	public boolean active;

	public Bullet(float startx, float starty, float radius, float xvol, float yvol, Color color) {
		this.xpos = startx;
		this.ypos = starty;
		this.xvol = xvol;
		this.yvol = yvol;
		this.radius = radius;
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
	
	public void draw(Graphics2D g2){
		if(active){
			g2.setPaint(color);
			g2.fillOval((int)(xpos - radius), (int)(ypos - radius), (int)(2*radius), (int)(2*radius));
			//g2.fillRect((int)(xpos - radius), (int)(ypos - radius), (int)radius, (int)radius);
		}
	}
	
	public void reset(){
		earliestCollision.reset();
		tempCollision.reset();
	}
	
	public float checkBoundaryCollisions(float time){
		Physics.checkBoxCollision(xpos, ypos, xvol, yvol, radius, 0, 0, 800, 800, time, tempCollision);
		if(tempCollision.t < earliestCollision.t){
			earliestCollision.copy(tempCollision);
			return tempCollision.t;
		}
		return time;
	}
	
	public void move(float time){
		if(earliestCollision.t <= time){
			xpos = earliestCollision.getNewX(xpos, xvol);
			ypos = earliestCollision.getNewY(ypos, yvol);
		}
		else{
			xpos += xvol*time;
			ypos += yvol*time;
		}
	}

}
