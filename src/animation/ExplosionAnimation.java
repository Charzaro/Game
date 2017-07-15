package animation;

import java.awt.Color;
import java.awt.Graphics2D;

public class ExplosionAnimation extends Animation {
	
	private float time;
	private float size;

	public ExplosionAnimation(float xpos, float ypos, float size, float time, Color c) {
		super(xpos, ypos, c);
		this.size = size;
		this.time = time;
	}
	
	@Override
	public void draw(Graphics2D g2){
		if(active){
			float radius;
			if(frame > 30 * time){
				radius = size;
			}
			else{
				radius = size * (float)frame/(30*time);
			}
			g2.setPaint(color);
			g2.fillOval((int)(xpos - radius), (int)(ypos - radius), (int)(2*radius), (int)(2*radius));
		}
		if(frame > (120*time)){
			active = false;
		}
	}

}
