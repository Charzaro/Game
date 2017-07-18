package animation;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import game.Map;
import game.Settings;

public class HitscanAnimation extends Animation {
	
	private Line2D line;

	public HitscanAnimation(float xpos, float ypos, Color c, float angle) {
		super(xpos, ypos, c);
		
		Point2D p = Settings.map.findClosestObstaclePoint(xpos, ypos, angle);
		line = new Line2D.Float(xpos, ypos, (float)p.getX(), (float)p.getY());
		
	}
	
	@Override
	public void draw(Graphics2D g2){
		if(active){
			g2.setPaint(color);
			g2.setStroke(new BasicStroke(8*(120 - frame)/120));
			g2.drawLine((int)line.getX1(), (int)line.getY1(), (int)line.getX2(), (int)line.getY2());
		}
		if(frame > 60){
			active = false;
		}
	}

}
