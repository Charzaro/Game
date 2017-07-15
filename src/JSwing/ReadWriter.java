package JSwing;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

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
			System.err.println("Error: File not found. " + e.getLocalizedMessage());
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

}
