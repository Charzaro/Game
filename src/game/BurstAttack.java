package game;

import java.awt.Graphics2D;

public class BurstAttack extends Ability {
	
	private static final int COOLDOWN_SEC = 8; // cooldown in seconds
	private static final short NUM_BULLETS = 14;
	private static final int BULLET_VELOCITY = 3;

	public BurstAttack(Player p, short num){
		super(p, num);
	}

	public void use() {
		if(cooldown <= 0){
			cooldown = Settings.update_rate * COOLDOWN_SEC; // 10 second cool down ( ex. goes down 120 every second bc 120 fps)
			float pi = (float)Math.PI;
			float portion = 2*(float)Math.PI / NUM_BULLETS;
			for(int i=0; i<NUM_BULLETS; i++){
				player.fireBullet(BULLET_VELOCITY, i*portion);
			}
		}
	}
	
	public void draw(Graphics2D g2){
		
	}
	
	public String getName(){
		return "Burst Attack";
	}

}
