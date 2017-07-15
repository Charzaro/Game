package animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import game.Settings;

public class HitscanAnimation extends Animation {
	
	private float angle;

	public HitscanAnimation(float xpos, float ypos, Color c, float angle) {
		super(xpos, ypos, c);
		this.angle = angle;
		
	}
	
	@Override
	public void draw(Graphics2D g2){
		if(active){
			g2.setPaint(color);
			g2.setStroke(new BasicStroke(8*(120 - frame)/120));
			g2.drawLine((int)xpos, (int)ypos, (int)(xpos + Settings.getDimx()*Math.sin(angle)), (int)(ypos + Settings.getDimx()*-Math.cos(angle)));
		}
		if(frame > 60){
			active = false;
		}
	}

}
