package game;

public abstract class Ability {
	
	protected static int update_rate = 120;

	protected int cooldown;
	protected Player player;
	
	public Ability(Player p){
		this.player = p;
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
}
