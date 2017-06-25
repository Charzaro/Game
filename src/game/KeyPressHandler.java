package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyPressHandler extends KeyAdapter {
	
	public boolean right;
	public boolean left;
	public boolean up;
	public boolean down;
	public boolean space;
	
	private int rightkey;
	private int leftkey;
	private int upkey;
	private int downkey;
	private int spacekey;
	
	public KeyPressHandler(int player){
		right = false;
		left = false;
		up = false;
		down = false;
		space = false;
		
		if(player == 1){
			leftkey = 37;
			upkey = 38;
			rightkey = 39;
			downkey = 40;
			spacekey = 17; // right control for p1
		}
		
		else if(player == 2){
			leftkey = 65;
			upkey = 87;
			rightkey = 68;
			downkey = 83;
			spacekey = 32; // right control for p1
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
		//System.out.println("Test");
		//System.out.println("Key Code: " + e.getKeyCode() + " Key char: " + e.getKeyChar());
	}
	
	public int getXResult(){
		if(right && left){
			return 0;
		}
		else if(right){
			return 10;
		}
		else if(left){
			return -10;
		}
		else{
			return 0;}
	}
	
	public int getYResult(){
		if(up && down){
			return 0;
		}
		else if(up){
			return -10;
		}
		else if(down){
			return 10;
		}
		else{
			return 0;}
	}

}
