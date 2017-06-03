package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyPressHandler extends KeyAdapter {
	
	public boolean right;
	public boolean left;
	public boolean up;
	public boolean down;
	public boolean space;
	
	public void KeyPressHandler(){
		right = false;
		left = false;
		up = false;
		down = false;
		space = false;
	}
	
	public void keyPressed(KeyEvent e){
		if(e.getKeyCode() == 37){
			left = true;
		}
		if(e.getKeyCode() == 38){
			up = true;
		}
		if(e.getKeyCode() == 39){
			right = true;
		}
		if(e.getKeyCode() == 40){
			down = true;
		}
		if(e.getKeyCode() == 32){ // space
			space = true;
		}
		//System.out.println("Test");
		System.out.println("Key Code: " + e.getKeyCode() + " Key char: " + e.getKeyChar());
	}
	
	public void keyReleased(KeyEvent e){
		if(e.getKeyCode() == 37){
			left = false;
		}
		if(e.getKeyCode() == 38){
			up = false;
		}
		if(e.getKeyCode() == 39){
			right = false;
		}
		if(e.getKeyCode() == 40){
			down = false;
		}
		if(e.getKeyCode() == 32){ // space
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
