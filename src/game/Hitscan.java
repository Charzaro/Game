package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;

public class Hitscan extends Projectile {

	public Line2D hitLine; // line across which a hit is detected
	
	private int duration;
	
	public Hitscan(float startx, float starty, float velocity, float angle, Color color) {
		super(startx, starty, velocity, angle, color);
		hitLine = new Line2D.Float(startx, starty, startx + (float)(Settings.getDimx()*Math.sin(angle)), starty + (float)(Settings.getDimx()*-Math.cos(angle)));
		duration = 0;
	}
	
	public void draw(Graphics2D g2){
	}
	

	// uses same movement as non-hitscan, but only to keep the time that the hitscan line is active
	// might need to be changed??
	@Override
	public void move(float time) {
		if(active){
			duration++;
			if(duration > 60 / Settings.update_factor){
				active = false;
			}
		}
	}
	
	// checks if shot hits the enemy player
	@Override
	public boolean checkHit(Player player){
		// goes through each pair of points in the hitbox and sees if the hitscan line crosses any line of the hitbox
		Polygon p = player.getBounds();
		boolean hit = false;
		for(int i=0; i<p.npoints; i++){
			if(i == p.npoints - 1){
				if(hitLine.intersectsLine(p.xpoints[i], p.ypoints[i], p.xpoints[0], p.ypoints[0])){
					if(!Settings.map.lineOfSightCheck(xpos, ypos, player.getX(), player.getY())){
						hit = true;
					}
				}
			}
			else{
				if(hitLine.intersectsLine(p.xpoints[i], p.ypoints[i], p.xpoints[i+1], p.ypoints[i+1])){
					if(!Settings.map.lineOfSightCheck(xpos, ypos, player.getX(), player.getY())){
						hit = true;
					}
				}
			}
		}
		return hit;
	}
	
	// a hit does 3 damage
	@Override
	public int getDamage(){
		return 3;
	}
	
	// reflects bullet
	public Projectile reflectedCopy(Color c){
		return new Hitscan(xpos, ypos, velocity, (float)(angle+Math.PI), c);
	}

}
