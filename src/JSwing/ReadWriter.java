package JSwing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import game.Obstacle;

public class ReadWriter {
	
	private static final String FILENAME = "binds.ini";
	
	public ReadWriter() {
	}
	
	public void writeBinds(HashMap<String, Integer> keys, String filename) throws IOException{
		try{
			ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(filename));
			objOut.writeObject((Object)keys);
			objOut.close();
		}
		catch(IOException e){
			System.err.println("Error: " + e.getLocalizedMessage());
			throw e;
		}
	}
	
	public HashMap<String, Integer> readBinds(String filename) throws Exception{
		
		try{
			HashMap<String, Integer> result;
			ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(filename));
			result = (HashMap<String,Integer>) objIn.readObject();
			objIn.close();
			return result;
		}
		catch(IOException e){
			System.err.println("Error: File not found. " + e.getLocalizedMessage());
			throw e;
		}
		catch(ClassNotFoundException e){
			System.err.println("Class not found.");
			throw e;
		}
		
	}
	
	public static void writeMap(String mapName, Obstacle[] map) throws IOException{
		try{
			DataOutputStream objOut = new DataOutputStream(new FileOutputStream(mapName + ".map"));
			objOut.writeInt(map.length);
			for(Obstacle o: map){
				objOut.writeFloat(o.xpoints[0]);
				objOut.writeFloat(o.ypoints[0]);
				objOut.writeFloat(o.width);
				objOut.writeFloat(o.height);
			}
			objOut.close();
		}
		catch(IOException e){
			System.err.println("Error: " + e.getLocalizedMessage());
			throw e;
		}
	}
	
	public static Obstacle[] readMap(String mapName) throws IOException{
		try{
			DataInputStream in = new DataInputStream(new FileInputStream(mapName + ".map"));
			int size = in.readInt();
			Obstacle[] result = new Obstacle[size];
			for(int i=0; i<size; i++){
				Obstacle o = new Obstacle(in.readFloat(), in.readFloat(), in.readFloat(), in.readFloat());
				result[i] = o;
			}
			return result;
		}
		catch(IOException e){
			throw e;
		}
	}

}
