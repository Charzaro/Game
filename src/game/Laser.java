package game;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.Line2D;

public class Laser extends Projectile {

	public Line2D hitLine;
	
	public Laser(float startx, float starty, float velocity, float angle, Color color) {
		super(startx, starty, velocity, angle, color);
		hitLine = new Line2D.Float(startx, starty, startx + (float)(Player.dimx*Math.sin(angle)), starty + (float)(Player.dimx*-Math.cos(angle)));
	}
	
	public boolean checkHitscan(Polygon p){
		boolean hit = false;
		for(int i=0; i<p.npoints; i++){
			if(i == p.npoints - 1){
				if(hitLine.intersectsLine(p.xpoints[i], p.ypoints[i], p.xpoints[0], p.ypoints[0])){
					hit = true;
				}
			}
			else{
				if(hitLine.intersectsLine(p.xpoints[i], p.ypoints[i], p.xpoints[i+1], p.ypoints[i+1])){
					hit = true;
				}
			}
		}
		return hit;
	}

	@Override
	public void move(float time) {
		if(active){
			if(earliestCollision.t <= time){
				xpos = earliestCollision.getNewX(xpos, xvol);
				ypos = earliestCollision.getNewY(ypos, yvol);
				active = false;
			}
			else{
				xpos += xvol*time;
				ypos += yvol*time;
			}
		}
	}
	
	@Override
	public boolean checkHit(Player player){
		Polygon p = player.getBounds();
		boolean hit = false;
		for(int i=0; i<p.npoints; i++){
			if(i == p.npoints - 1){
				if(hitLine.intersectsLine(p.xpoints[i], p.ypoints[i], p.xpoints[0], p.ypoints[0])){
					hit = true;
				}
			}
			else{
				if(hitLine.intersectsLine(p.xpoints[i], p.ypoints[i], p.xpoints[i+1], p.ypoints[i+1])){
					hit = true;
				}
			}
		}
		return hit;
	}
	
	@Override
	public int getDamage(){
		return 3;
	}
	
	public Projectile reflectedCopy(Color c){
		return new Laser(xpos, ypos, velocity, (float)(angle+Math.PI), c);
	}

}
