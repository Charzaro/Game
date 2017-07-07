package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Animation {
	
	private static int update_factor = 1;
	
	private static int dimx = 800;
	private static int dimy = 800;
	
	private int frame;
	private String type;
	private float xpos;
	private float ypos;
	private float size;
	
	private float angle = 0;
	
	private Color color;
	
	public boolean active;

	public Animation(String type, float xpos, float ypos, float size, Color c) {
		this.type = type;
		this.xpos = xpos;
		this.ypos = ypos;
		this.size = size;
		
		this.color = c;
		
		active = true;
		frame = 0;
	}
	
	public Animation(String type, float xpos, float ypos, float size, Color c, float angle){
		this.type = type;
		this.xpos = xpos;
		this.ypos = ypos;
		this.size = size;
		this.angle = angle;
		
		this.color = c;
		
		active = true;
		frame = 0;
	}
	
	public static void setDim(int x, int y){
		dimx = x;
		dimy = y;
	}
	
	// set frame rate factor
	public static void setUR(int ur){
		update_factor = 120/ur;
		System.out.println(update_factor);
	}
	
	public void draw(Graphics2D g2){
		if(active){
			switch (type){
			case "star":
				g2.setPaint(color);
				float y = frame*size;
				if(y > dimy){
					active = false;
				}
				else{
					g2.fillOval((int)(xpos-size), (int)(y-size), (int)(2*size), (int)(2*size));
				}
				break;
			case "hit":
				if(frame < 20){
					g2.setPaint(color);
					g2.setStroke(new BasicStroke(2));
					g2.drawLine((int)xpos, (int)ypos, (int)(xpos-size*frame), (int)(ypos-size*frame));
					g2.drawLine((int)xpos, (int)ypos, (int)(xpos+size*frame), (int)(ypos-size*frame));
					g2.drawLine((int)xpos, (int)ypos, (int)(xpos-size*frame), (int)(ypos+size*frame));
					g2.drawLine((int)xpos, (int)ypos, (int)(xpos+size*frame), (int)(ypos+size*frame));
				}
				else{
					active = false;
				}
				break;
			case "bullet":
				if(active){
					float x = (float) (xpos + frame*update_factor*120*Math.sin(angle));
					y = (float) (ypos - frame*update_factor*120*Math.cos(angle));
					if(x < 0 || x > dimx || y < 0 || y > dimy){
						active = false;
					}
					g2.setPaint(color);
					g2.setStroke(new BasicStroke(size));
					g2.drawLine((int)xpos, (int)ypos, (int)x, (int)y);
				}
			}
		}
	}
	
	public void move(){
		frame+= update_factor;
	}

}
