package animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import game.Settings;

public class Star extends Animation {
	
	private static final int SIZE_CHANGE = 2;
	private static final float BLINK_TIME = 0.5f;

	private float size;
	private float currentSize;
	private int blinkGap;
	
	public Star(float xpos, float ypos, float size, Color c) {
		super(xpos, ypos, c);
		this.size = size;
		blinkGap = 10;
		frame = (int) (Math.random()*120*blinkGap);
		currentSize = size;
		
	}
	
	@Override
	public void draw(Graphics2D g2){
		if(active){
			
			// move across screen as frame count increases, top to bottom
			if(frame >= 120*blinkGap){
				g2.setPaint(new Color(color.getRed(), color.getGreen(), color.getBlue(), 127));
				g2.fillOval((int)(xpos-SIZE_CHANGE*currentSize), (int)(ypos-SIZE_CHANGE*currentSize), (int)(2*SIZE_CHANGE*currentSize), (int)(2*SIZE_CHANGE*currentSize));
				g2.setPaint(color);
				g2.setStroke(new BasicStroke(2));
				g2.drawLine((int)xpos, (int)ypos, (int)(xpos+size+1), (int)ypos);
				g2.drawLine((int)xpos, (int)ypos, (int)(xpos-size-1), (int)ypos);
				g2.drawLine((int)xpos, (int)ypos, (int)xpos, (int)(ypos+size+1));
				g2.drawLine((int)xpos, (int)ypos, (int)xpos, (int)(ypos-size-1));
				//currentSize += (float)SIZE_CHANGE*size/120*(float)BLINK_TIME;	
			}
			g2.setPaint(color);
			g2.fillOval((int)(xpos-currentSize), (int)(ypos-currentSize), (int)(2*currentSize), (int)(2*currentSize));
			if(frame >= 120*(blinkGap+BLINK_TIME)){
				frame = (int) (Math.random()*120*blinkGap);
				currentSize = size;
			}
		}
	}
	
	public void moveStar(Graphics2D g2){
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
