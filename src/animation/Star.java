package animation;

import java.awt.Color;
import java.awt.Graphics2D;

import game.Settings;

public class Star extends Animation {

	private float size;
	
	public Star(float xpos, float ypos, float size, Color c) {
		super(xpos, ypos, c);
		this.size = size;
	}
	
	@Override
	public void draw(Graphics2D g2){
		if(active){
			g2.setPaint(color);
			// move across screen as frame count increases, top to bottom
			float y = frame*size + ypos;
			// deactive once gone past the bottom
			if(y > Settings.getDimy()){
				active = false;
			}
			else{
				// draw a white circle
				g2.fillOval((int)(xpos-size), (int)(y-size), (int)(2*size), (int)(2*size));
			}
		}
	}

}
