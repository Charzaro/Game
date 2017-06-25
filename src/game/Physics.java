package game;

public class Physics {

	static Collision tempC = new Collision();
	final static float T_EPSILON = 0.005f;

	
	// check if the particle collides with the bounding box
	public static void checkBoxCollision(float xpos, float ypos, float xvol, float yvol, float widthr, float heightr, float xmin, float ymin,
			float xmax, float ymax, float t, Collision C){
		
		// right
		checkVerticalLine(xpos, xvol, yvol, widthr, xmax, t);
		if(tempC.t < C.t){
			C.copy(tempC);
		}
		
		// left
		checkVerticalLine(xpos, xvol, yvol, widthr, xmin, t);
		if(tempC.t < C.t){
			C.copy(tempC);
		}
		
		// up
		checkHorizontalLine(ypos, xvol, yvol, heightr, ymin, t);
		if(tempC.t < C.t){
			C.copy(tempC);
		}

		// down
		checkHorizontalLine(ypos, xvol, yvol, heightr, ymax, t);
		if(tempC.t < C.t){
			C.copy(tempC);
		}

	}
	
	// check if a particle hits a vertical line
	public static void checkVerticalLine(float xpos, float xvol, float yvol, float radius, float linex,
			float tmax){

		tempC.reset(); // reset collision holder

		if(xpos == 0){ // no collision if nothing moves
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
		if(timetocollision>0 && timetocollision<=tmax){
			tempC.t = timetocollision;
			tempC.nspeedx = 0; // reflect x
			tempC.nspeedy = yvol; // leave y
		}
		
	}

	// check if a particle hits a horizontal line
	public static void checkHorizontalLine(float ypos, float xvol, float yvol, float radius, float liney,
			float tmax){
		
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
		if(timetocollision>0 && timetocollision<=tmax){
			tempC.t = timetocollision;
			tempC.nspeedx = xvol; // leave x
			tempC.nspeedy = 0; // reflect y
		}
	}
	
	// check if a point hits another point
		public static void bulletIntersectsPlayer(Bullet b, Player p, Collision p1Collision, Collision p2Collision, float timeLimit){
			// difference in x and y positions
			float Cx = p.getX() - b.getX();
			float Cy = p.getY() - b.getY();
			// difference in x and y velocities
			float Vx = p.getXvol() - b.getXvol();
			float Vy = p.getYvol() - b.getYvol();
			// distance between particles' centers
			float r = b.getRadius() + p.getHeight();
			
			// IMPRECISE TO USE RADIUS
			
			if(Vx == 0 && Vy == 0){
				return; // will never collide
			}
			// create a quadritc function (some reductions already done
			float part1 = -1*(Cx*Vx + Cy*Vy); // b (about)
			float part2 = r*r*(Vx*Vx + Vy*Vy) + 2*Cx*Cy*Vx*Vy - Vx*Vx*Cy*Cy - Vy*Vy*Cx*Cx; // b2 - 4ac (about)
			float part3 = Vx*Vx + Vy*Vy; // a (about)
			
			// solve for both solutions (t is time of collision)
			float t1 = (part1 + (float)Math.sqrt(part2))/part3;
			float t2 = (part1 - (float)Math.sqrt(part2))/part3;
			
			// take the earliest positive collision if it is within the time step
			if(t1 > 0 && t1 <= timeLimit && (t1 <= t2 || t2 < 0)){
				// store time in the collisions and calculate response
				p1Collision.t = t1;
				p1Collision.nspeedx = 0;
				p1Collision.nspeedy = 0;
				p2Collision.t = t1;
				p2Collision.nspeedx = 0;
				p2Collision.nspeedy = 0;
			}
			else if(t2>0 && t2 <= timeLimit){
				// store time in the collisions and calculate response
				p1Collision.t = t2;
				p1Collision.nspeedx = 0;
				p1Collision.nspeedy = 0;
				p2Collision.t = t2;
				p2Collision.nspeedx = 0;
				p2Collision.nspeedy = 0;
			}

		}
}
