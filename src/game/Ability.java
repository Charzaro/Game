package game;

import java.awt.Graphics2D;

public abstract class Ability {
	
	protected static int update_rate = 120;

	protected int cooldown;
	protected Player player;
	protected short num;
	
	public Ability(Player p, short num){
		this.player = p;
		this.num = num;
		cooldown = 0;
	}
	
	public static void setUR(int ur){
		update_rate = ur;
	}
	
	public void cool(){
		if(cooldown > 0){
			cooldown--;
		}
	}
	
	public void reset(){
		cooldown = 0;
	}
	
	public abstract void use();
	public abstract void draw(Graphics2D g2);
}
