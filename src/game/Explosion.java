package game;

import java.awt.Color;
import java.awt.Graphics2D;

public class Explosion extends Projectile {
	
	private float explosionTime; // in seconds
	
	private int duration;
	private float maxRadius;
	private float radius;

	public Explosion(float startx, float starty, float radius, float time, Color color) {
		super(startx, starty, 0, 0, color);
		
		duration = 0;
		this.radius = 0;
		this.explosionTime = time;
		maxRadius = radius;
	}

	@Override
	public Projectile reflectedCopy(Color c) {
		return this;
	}

	@Override
	public void move(float time) {
		if(active){
			duration += Settings.update_factor*time;
			if(duration > 30 * explosionTime){
				radius = maxRadius;
			}
			else{
				radius = maxRadius * duration/(explosionTime * 120);
			}
			
		}
		if(duration > 120 * explosionTime){
			active = false;
		}

	}
	
	@Override
	public void draw(Graphics2D g2){

	}
	
	@Override
	public boolean checkHit(Player p){
		if(active){
			float distance = Physics.getDistance(p.getX(), p.getY(), xpos, ypos);
			if(distance <= radius){
				active = false;
				return true;
			}
		}
		return false;
	}

}
