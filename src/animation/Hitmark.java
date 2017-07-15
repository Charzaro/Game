package animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Hitmark extends Animation {
	
	private static final int HITSIZE = 2;

	public Hitmark(float xpos, float ypos, Color c) {
		super(xpos, ypos, c);
	}
	
	@Override
	public void draw(Graphics2D g2){
		if(active){ //runs until 20th frame count
			g2.setPaint(color);
			// draws an X that increases in size with the frame count
			g2.setStroke(new BasicStroke(2));
			g2.drawLine((int)xpos, (int)ypos, (int)(xpos-2*frame), (int)(ypos-HITSIZE*frame));
			g2.drawLine((int)xpos, (int)ypos, (int)(xpos+2*frame), (int)(ypos-HITSIZE*frame));
			g2.drawLine((int)xpos, (int)ypos, (int)(xpos-2*frame), (int)(ypos+HITSIZE*frame));
			g2.drawLine((int)xpos, (int)ypos, (int)(xpos+2*frame), (int)(ypos+HITSIZE*frame));
			if( frame > 20){
				active = false;
			}
		}
	}
}
