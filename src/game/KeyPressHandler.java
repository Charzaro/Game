package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.HashMap;

import JSwing.ReadWriter;
/*
 * KeyPressHandler.java
 * 
 * Handles key presses on the keyboard for a given player
 */
public class KeyPressHandler extends KeyAdapter {
	
	private ReadWriter ioStream = new ReadWriter();
	
	private int player;
	private String filename;
	
	// flags for if a key is being held
	public boolean right;
	public boolean left;
	public boolean up;
	public boolean down;
	public boolean shoot;
	public boolean boost;
	public boolean ability1;
	public boolean ability2;
	public boolean cheat;
	
	// key codes corresponding to a players key
	private HashMap<String, Integer> keyMap; // = new HashMap<String, Integer>();
	

	private boolean c1, c2, c3;
	
	public KeyPressHandler(int player){
		
		this.player = player;
		
		// start all flags as false
		right = false;
		left = false;
		up = false;
		down = false;
		shoot = false;
		boost = false;
		ability1= false;
		ability2 = false;
		
		// dev secrets
		c1 = false;
		c2 = false;
		c3 = false;
		
		if(player == 2){
			filename = "p2binds.ini";
		}
		else{
			filename = "p1binds.ini";
		}
		
		try{
			keyMap = ioStream.readBinds(filename);
		}
		catch(Exception e){
			System.err.println(e.getMessage());
			System.err.println("Default keybinds used.");
			setDefaults();
		}
	}

	public void setDefaults(){
		// key bindings for player 1
		
		keyMap = new HashMap<String, Integer>();
		if(player == 2){
			keyMap.put("left", 37);
			keyMap.put("up", 38);
			keyMap.put("right", 39);
			keyMap.put("down", 40);
			keyMap.put("shoot", 98);
			keyMap.put("boost", 99);
			keyMap.put("ability1", 10);
			keyMap.put("ability2", 110);
		}

		// key bindings for player 2
		else if(player == 1){
			keyMap.put("left", 65);
			keyMap.put("up", 87);
			keyMap.put("right", 68);
			keyMap.put("down", 83);
			keyMap.put("shoot", 86);
			keyMap.put("boost", 67);
			keyMap.put("ability1", 71);
			keyMap.put("ability2", 70);

		}
		else{
			System.out.println("Error: player keybinds not yet set.");
		}
	}
	
	public void setKeys(HashMap<String, Integer> newKeys){
		for(String s: newKeys.keySet()){
			if(keyMap.containsKey(s)){
				keyMap.remove(s);
				keyMap.put(s, newKeys.get(s));
			}
			else{
				System.err.println("Trying to bind a key to an improper action.");
			}
		}
		
		try{
			ioStream.writeBinds(keyMap, filename);
		}
		catch(IOException e){
			System.err.println("Could not save keybinds.");
			System.err.println(e.getMessage());
		}
		
	}
	
	public HashMap<String, Integer> getKeys(){
		return keyMap;
	}
	
	
	// detects a key press
	// if a key press corresponds to the players key code, sets flag as true.
	// only checks a key code if the key is not true already, ie. not already pressed
	public void keyPressed(KeyEvent e){
		if(!left && e.getKeyCode() == keyMap.get("left")){
			left = true;
		}
		if(!up && e.getKeyCode() == keyMap.get("up")){
			up = true;
		}
		if(!right && e.getKeyCode() == keyMap.get("right")){
			right = true;
		}
		if(!down && e.getKeyCode() == keyMap.get("down")){
			down = true;
		}
		if(!shoot && e.getKeyCode() == keyMap.get("shoot")){ 
			shoot = true;
		}
		if(!boost && e.getKeyCode() == keyMap.get("boost")){ 
			boost = true;
		}
		if(!ability1 && e.getKeyCode() == keyMap.get("ability1")){ 
			ability1 = true;
		}
		if(!ability2 && e.getKeyCode() == keyMap.get("ability2")){ 
			ability2 = true;
		}
		if(e.getKeyCode() == 97){ 
			c1 = true;
		}
		if(e.getKeyCode() == 101){
			if(c1){
				c2 = true;
			}
			else{
				c1 = false;
			}
			
		}
		if(e.getKeyCode() == 105){
			if(c1 && c2){
				cheat = true;
			}
			else{
				c1 = false;
				c2 = false;
			}
			
		}
		// DEV for finding key codes easily
		//System.out.println("Test");
		//System.out.println("Key Code: " + e.getKeyCode() + " Key char: " + e.getKeyChar());
	}
	
	// on a key release, checks if any of the key codes match the released key and sets that flag to false
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == keyMap.get("left")){
			left = false;
		}
		if(e.getKeyCode() == keyMap.get("up")){
			up = false;
		}
		if(e.getKeyCode() == keyMap.get("right")){
			right = false;
		}
		if(e.getKeyCode() == keyMap.get("down")){
			down = false;
		}
		if(e.getKeyCode() == keyMap.get("shoot")){
			shoot = false;
		}
		if(e.getKeyCode() == keyMap.get("boost")){ 
			boost = false;
		}
		if(e.getKeyCode() == keyMap.get("ability1")){ 
			ability1 = false;
		}
		if(e.getKeyCode() == keyMap.get("ability2")){ 
			ability2 = false;
		}
		//System.out.println("Test");
		//System.out.println("Key Code: " + e.getKeyCode() + " Key char: " + e.getKeyChar());
	}

}
