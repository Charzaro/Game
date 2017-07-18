package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

public class Obstacle{
	
	public float[] xpoints;
	public float[] ypoints;
	
	public float width;
	public float height;

	public Obstacle(float x, float y, float width, float height) {
		xpoints = new float[] {x, x+width};
		ypoints = new float[] {y, y+height};
		
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics2D g2){
		g2.setPaint(Color.GRAY);
		g2.fillRect((int)xpoints[0], (int)ypoints[0], (int)width, (int)height);
	}
	
	public Rectangle getRect(){
		return new Rectangle((int)xpoints[0], (int)ypoints[0], (int)width, (int)height);
	}
	
	public Line2D topLine(){
		return new Line2D.Float(xpoints[0], ypoints[0], xpoints[1], ypoints[0]);
	}
	
	public Line2D bottomLine(){
		return new Line2D.Float(xpoints[0], ypoints[1], xpoints[1], ypoints[1]);
	}
	
	public Line2D rightLine(){
		return new Line2D.Float(xpoints[1], ypoints[0], xpoints[1], ypoints[1]);
	}
	
	public Line2D leftLine(){
		return new Line2D.Float(xpoints[0], ypoints[0], xpoints[0], ypoints[1]);
	}


}
