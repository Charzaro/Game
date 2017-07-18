package game;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;

import JSwing.ReadWriter;
import animation.HitscanAnimation;

public class Map {

	private Obstacle[] obstacles;

	public Map(String mapName) {
		loadMap(mapName);
	}

	public Obstacle[] getObstacles(){
		return obstacles;
	}


	public void loadMap(String mapName){
		if(mapName.equals("Empty")){
			obstacles = new Obstacle[0];
			Settings.setMap(this);
			return;
		}
		try{
			obstacles = ReadWriter.readMap(mapName);
		}
		catch(IOException e){
			obstacles = new Obstacle[0];
			System.err.println("Map read fail");
			e.printStackTrace();
		}
		finally{
			Settings.setMap(this);
		}
	}

	public Point2D findClosestObstaclePoint(float xpos, float ypos, float angle){
		// slope of the line
		float slope;
		if(Physics.xComponent(1, angle) == 0){
			slope = 0;
		}
		else{
			slope = Physics.yComponent(1, angle)/Physics.xComponent(1, angle);
		}

		// closest point to the player that is on on obstacle or game border, starts beyond game border
		Point2D closestPoint = new Point2D.Float((float)(xpos + 2*Settings.getDimx()*Math.sin(angle)), (float)(ypos + 2*Settings.getDimx()*-Math.cos(angle)));
		// distance from current closest point to the player
		float closestDistance = Physics.getDistance(xpos, ypos, (float)closestPoint.getX(), (float)closestPoint.getY());
		// line crossing the screen to use to check intersections with obstacles
		Line2D line = new Line2D.Float(new Point2D.Float(xpos, ypos), closestPoint);
		for(Obstacle o: obstacles){
			if(o == null){
				break;
			}

			float tempVal;
			float tempDist;
			Line2D templine = o.topLine();
			if(line.intersectsLine(templine)){
				tempVal = (float)templine.getY1() - ypos;
				float dx;
				if(slope == 0){
					dx = 0;
				}
				else
					dx = tempVal/slope;
				if((tempDist = Physics.getDistance(xpos, ypos, xpos + dx, ypos+tempVal)) < closestDistance){
					closestPoint.setLocation(xpos + tempVal/slope, ypos+tempVal);
					closestDistance = tempDist;
				}
			}
			templine = o.bottomLine();
			if(line.intersectsLine(templine)){
				tempVal = (float)templine.getY1() - ypos;
				float dx;
				if(slope == 0){
					dx = 0;
				}
				else
					dx = tempVal/slope;
				if((tempDist = Physics.getDistance(xpos, ypos, xpos + dx, ypos+tempVal)) < closestDistance){
					closestPoint.setLocation(xpos + tempVal/slope, ypos+tempVal);
					closestDistance = tempDist;
				}
			}

			templine = o.rightLine();
			if(line.intersectsLine(templine)){
				tempVal = (float)templine.getX1() - xpos;
				if((tempDist = Physics.getDistance(xpos, ypos, xpos + tempVal, ypos+tempVal*slope)) < closestDistance){
					closestPoint.setLocation(xpos + tempVal, ypos+tempVal*slope);
					closestDistance = tempDist;
				}
			}
			templine = o.leftLine();
			if(line.intersectsLine(templine)){
				tempVal = (float)templine.getX1() - xpos;
				if((tempDist = Physics.getDistance(xpos, ypos, xpos + tempVal, ypos+tempVal*slope)) < closestDistance){
					closestPoint.setLocation(xpos + tempVal, ypos+tempVal*slope);
					closestDistance = tempDist;
				}
			}
		}
		return closestPoint;
	}
	
	public boolean lineOfSightCheck(float x1, float y1, float x2, float y2){
		boolean hit = false;
		Line2D losCheck = new Line2D.Float(x1, y1, x2, y2);
		for(Obstacle o: obstacles){
			if(losCheck.intersects(o.getRect())){
				hit = true;
			}
		}
		return hit;
	}

}
