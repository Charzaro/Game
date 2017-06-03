package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Player {
	
	private static final String p1ImageFilename = "pixel ship2.png";
	
	private float xpos;
	private float ypos;
	private int radius;
	
	private float xvol;
	private float yvol;
	
	private Collision earliestCollision;
	private Collision tempCollision;
	
	private BufferedImage image;

	public Player(int startx, int starty) {
		
		xpos = startx;
		ypos = starty;
		radius = 100;
		xvol = 0;
		yvol = 0;
		
		earliestCollision = new Collision();
		tempCollision = new Collision();
		
		try{
			image = ImageIO.read(new File(p1ImageFilename));
			radius = image.getWidth()/2;
		}
		catch(IOException e){
			System.out.println("Could not open ship image: " + e);
		}
	}
	
	public float getX(){
		return xpos;
	}
	
	public float getY(){
		return ypos;
	}
	
	public void draw(Graphics2D g2, JPanel p){
		g2.drawImage(image, (int)(xpos-radius), (int) (ypos-radius), p);
		//g2.setPaint(Color.BLUE);
		//g2.fillOval((int)(xpos-radius), (int) (ypos-radius), 2*radius, 2*radius);
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
			xvol = earliestCollision.nspeedx;
			yvol = earliestCollision.nspeedy;
		}
		else{
			xpos += xvol*time;
			ypos += yvol*time;
		}
	}
	
	
	// testing functions, remove later?
	public void setVelocity(float x, float y){
		this.xvol = x;
		this.yvol = y;
	}

}
