package game;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import animation.HitscanAnimation;

public class Map {

	private String mapName;
	private Obstacle[] obstacles;
	
	public class Spawn{
		public int x;
		public int y;
		public float angle;
	}
	
	public Spawn spawn1 = new Spawn();
	public Spawn spawn2 = new Spawn();

	public Map(String mapName) {
		this.mapName = mapName;
		
		loadMap(mapName);

	}

	public Obstacle[] getObstacles(){
		return obstacles;
	}
	


	public void loadMap(String mapName){
		try{
			if(mapName.equals("Empty")){
				obstacles = new Obstacle[0];
				setDefaultSpawns();
			}
			else{
				readMap(mapName);
			}
		}
		catch(IOException e){
			System.err.println("Map read fail");
			obstacles = new Obstacle[0];
			setDefaultSpawns();
			e.printStackTrace();
		}
		finally{
			Settings.setMap(this);
		}
	}
	
	public void saveMap(){
		try{
			writeMap(mapName);
		}
		catch(IOException e){
			System.err.println("Map write fail");
			e.printStackTrace();
		}
	}
	
	public void setDefaultSpawns(){
		spawn1.x = 40;
		spawn2.x = Settings.getDimx()-40;
		spawn1.y = spawn2.y = Settings.getDimy()/2;
		spawn1.angle = spawn2.angle = 0;
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
	
	public void convertMap(String mapName){
		if(mapName.equals("Empty")){
			obstacles = new Obstacle[0];
			return;
		}
		try{
			oldRead(mapName);
			
			writeMap(mapName);
			
			System.out.println("Conversion successful");
		}
		catch(IOException e){
			obstacles = new Obstacle[0];
			System.err.println("Conversion failed");
			e.printStackTrace();
		}
		finally{
			Settings.setMap(this);
		}
	}
	
	public void writeMap(String mapName) throws IOException{
		try{
			DataOutputStream objOut = new DataOutputStream(new FileOutputStream(mapName + ".map"));
			
			// write first spawn
			objOut.writeInt(spawn1.x);
			objOut.writeInt(spawn1.y);
			objOut.writeFloat(spawn1.angle);
			// write second spawn
			objOut.writeInt(spawn2.x);
			objOut.writeInt(spawn2.y);
			objOut.writeFloat(spawn2.angle);
			// write obstacles
			objOut.writeInt(obstacles.length);
			for(Obstacle o: obstacles){
				objOut.writeFloat(o.xpoints[0]);
				objOut.writeFloat(o.ypoints[0]);
				objOut.writeFloat(o.width);
				objOut.writeFloat(o.height);
				objOut.writeBoolean(o.breakable);
			}
			objOut.close();
		}
		catch(IOException e){
			System.err.println("Error: " + e.getLocalizedMessage());
		}
	}
	
	public void readMap(String mapName) throws IOException{
		try{
			DataInputStream in = new DataInputStream(new FileInputStream(mapName + ".map"));
			
			spawn1.x = in.readInt();
			spawn1.y = in.readInt();
			spawn1.angle = in.readFloat();
			
			spawn2.x = in.readInt();
			spawn2.y = in.readInt();
			spawn2.angle = in.readFloat();
			
			int size = in.readInt();
			obstacles = new Obstacle[size];
			for(int i=0; i<size; i++){
				Obstacle o = new Obstacle(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat(), in.readBoolean());
				obstacles[i] = o;
			}
		}
		catch(IOException e){
			throw e;
		}
	}
	
	public void oldRead(String mapName) throws IOException{
		try{
			DataInputStream in = new DataInputStream(new FileInputStream(mapName + ".map"));
			
			spawn1.x = in.readInt();
			spawn1.y = in.readInt();
			spawn1.angle = in.readFloat();
			
			spawn2.x = in.readInt();
			spawn2.y = in.readInt();
			spawn2.angle = in.readFloat();
			
			int size = in.readInt();
			obstacles = new Obstacle[size];
			for(int i=0; i<size; i++){
				Obstacle o = new Obstacle(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat(), false);
				obstacles[i] = o;
			}
		}
		catch(IOException e){
			throw e;
		}
	}

}
