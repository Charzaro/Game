package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyPressHandler extends KeyAdapter {
	
	public boolean right;
	public boolean left;
	public boolean up;
	public boolean down;
	public boolean space;
	public boolean boost;
	public boolean ability1;
	public boolean ability2;
	public boolean cheat;
	
	private int rightkey;
	private int leftkey;
	private int upkey;
	private int downkey;
	private int spacekey;
	private int boostkey;
	private int ability1key;
	private int ability2key;
	private boolean c1, c2, c3;
	
	public KeyPressHandler(int player){
		right = false;
		left = false;
		up = false;
		down = false;
		space = false;
		boost = false;
		ability1= false;
		ability2 = false;
		
		c1 = false;
		c2 = false;
		c3 = false;
		
		if(player == 1){
			leftkey = 37;
			upkey = 38;
			rightkey = 39;
			downkey = 40;
			spacekey = 96; // enter
			boostkey = 110; // period/delete
			ability1key = 99; //num 3
			ability2key = 10; // enter
		}
		
		else if(player == 2){
			leftkey = 65;
			upkey = 87;
			rightkey = 68;
			downkey = 83;
			spacekey = 86; // space
			boostkey = 67; // shift
			ability1key = 66; // b
			ability2key = 78; // n
		}
		else{
			System.out.println("Error: player keybinds not yet set.");
		}
		
		
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == leftkey){
			left = true;
		}
		if(e.getKeyCode() == upkey){
			up = true;
		}
		if(e.getKeyCode() == rightkey){
			right = true;
		}
		if(e.getKeyCode() == downkey){
			down = true;
		}
		if(e.getKeyCode() == spacekey){ 
			space = true;
		}
		if(e.getKeyCode() == boostkey){ 
			boost = true;
		}
		if(e.getKeyCode() == ability1key){ 
			ability1 = true;
		}
		if(e.getKeyCode() == ability2key){ 
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
		//System.out.println("Test");
		//System.out.println("Key Code: " + e.getKeyCode() + " Key char: " + e.getKeyChar());
	}
	
	// redundant key pressing for certainty
	public void keyHeld(KeyEvent e){
		if(e.getKeyCode() == leftkey){
			left = true;
		}
		if(e.getKeyCode() == upkey){
			up = true;
		}
		if(e.getKeyCode() == rightkey){
			right = true;
		}
		if(e.getKeyCode() == downkey){
			down = true;
		}
		if(e.getKeyCode() == spacekey){ 
			space = true;
		}
		if(e.getKeyCode() == boostkey){ 
			boost = true;
		}
		if(e.getKeyCode() == ability1key){ 
			ability1 = true;
		}
		if(e.getKeyCode() == ability2key){ 
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
		//System.out.println("Test");
		//System.out.println("Key Code: " + e.getKeyCode() + " Key char: " + e.getKeyChar());
	}
	
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == leftkey){
			left = false;
		}
		if(e.getKeyCode() == upkey){
			up = false;
		}
		if(e.getKeyCode() == rightkey){
			right = false;
		}
		if(e.getKeyCode() == downkey){
			down = false;
		}
		if(e.getKeyCode() == spacekey){
			space = false;
		}
		if(e.getKeyCode() == boostkey){ 
			boost = false;
		}
		if(e.getKeyCode() == ability1key){ 
			ability1 = false;
		}
		if(e.getKeyCode() == ability2key){ 
			ability2 = false;
		}
		//System.out.println("Test");
		//System.out.println("Key Code: " + e.getKeyCode() + " Key char: " + e.getKeyChar());
	}

}
