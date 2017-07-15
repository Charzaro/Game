package game;

import java.awt.Graphics2D;

/*
 * Ability.java
 * 
 * Abstract class for making abilities
 */
public abstract class Ability {

	// useful numbers
	protected int cooldown; // frames until can be used again
	protected Player player; // player that has this ability
	protected short num; // which ability (1 or 2) this ability is
	
	public Ability(Player p, short num){
		this.player = p;
		this.num = num;
		cooldown = 0;
	}
	
	//  lowers cooldown by 1
	public void cool(){
		if(cooldown > 0){
			cooldown--;
		}
	}
	
	// resets ability to starting state
	public void reset(){
		cooldown = 0;
	}
	
	// uses ability
	public abstract void use();
	// draws any visuals for ability
	public abstract void draw(Graphics2D g2);
	// for drawing ability name
	public abstract String getName();
	
}
