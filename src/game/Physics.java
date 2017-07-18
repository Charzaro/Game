package game;

import java.awt.geom.Point2D;

public class Physics {

	static Collision tempC = new Collision();
	final static float T_EPSILON = 0.005f;
	
	public static float getDistance(float x1, float y1, float x2, float y2){
		float xdist = x1 - x2;
		float ydist = y1 - y2;
		return (float)Math.sqrt(xdist*xdist + ydist*ydist);
	}
	
	public static float getDistance(Point2D p1, Point2D p2){
		float xdist = (float)(p1.getX() - p2.getX());
		float ydist = (float) (p1.getY() - p2.getY());
		return (float)Math.sqrt(xdist*xdist + ydist*ydist);
	}
	
	public static float xComponent(float mag, float angle){
		return mag*(float)Math.sin(angle);
		
	}
	
	public static float yComponent(float mag, float angle){
		return -mag*(float)Math.cos(angle);
	}
	
	// angle formed by two points, where point one is the center (point 1 to point 2)
	public static float findAngle(float x1, float y1, float x2, float y2){
		float dx = -1*(x2 - x1);
		float dy = y2 - y1;
		// goal angle to turn to
		float angle = (float)Math.atan(dx/dy);

		// adjust for fact that arctan can only return a value from -90 to 90
		if(dy > 0){
			angle += Math.PI;
		}
		return angle;
	}

	
	// check if the particle collides with the bounding box
	public static void checkBoxCollision(float xpos, float ypos, float xvol, float yvol, float widthr, float heightr, float xmin, float ymin,
			float xmax, float ymax, float t, Collision C){
		
		// right
		checkVerticalLine(xpos, xvol, yvol, widthr, xmax, t, C);
		
		
		// left
		checkVerticalLine(xpos, xvol, yvol, widthr, xmin, t, C);
		
		// up
		checkHorizontalLine(ypos, xvol, yvol, heightr, ymin, t, C);

		// down
		checkHorizontalLine(ypos, xvol, yvol, heightr, ymax, t, C);

	}
	
	// check if a particle hits a vertical line
	public static void checkVerticalLine(float xpos, float xvol, float yvol, float radius, float linex,
			float tmax, Collision C){

		tempC.reset(); // reset collision holder

		if(xvol == 0){ // no collision if nothing moves
			return;
		}

		float distance; // distance from border (negative is a left move, positive is a right move)
		if(linex < xpos){
			distance = xpos - (linex + radius);
		}
		else{
			distance = xpos - (linex - radius);
		}
		distance = -1*distance;
		float timetocollision = distance/xvol;
		if(timetocollision>=0 && timetocollision<=tmax){
			tempC.t = timetocollision;
			tempC.xcollide = true; // stop x
		}
		
		if(tempC.t < C.t){
			C.copy(tempC);
		}
		
	}

	// check if a particle hits a horizontal line
	public static void checkHorizontalLine(float ypos, float xvol, float yvol, float radius, float liney,
			float tmax, Collision C){
		
		tempC.reset(); // reset collision holder
		
		if(yvol == 0){ // no collision if nothing moves
			return;
		}
		
		float distance; // distance from border (negative is an up move, positive is a down move)
		if(liney < ypos){
			distance = ypos - (liney + radius);
		}
		else{
			distance = ypos - (liney - radius);
		}
		distance = -1*distance;
		float timetocollision = distance/yvol;
		if(timetocollision>=0 && timetocollision<=tmax){
			tempC.t = timetocollision;
			tempC.ycollide = true; // stop y
		}
		
		if(tempC.t < C.t){
			C.copy(tempC);
		}
	}
}
