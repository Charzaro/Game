package game;

import java.awt.Graphics2D;

public class RocketBurst extends Ability {
	
	private static final int COOLDOWN_SEC = 8; // cooldown in seconds

	public RocketBurst(Player p, short num){
		super(p, num);
	}

	public void use() {
		if(cooldown <= 0){
			cooldown = update_rate * COOLDOWN_SEC; // 10 second cool down ( ex. goes down 120 every second bc 120 fps)
			float pi = (float)Math.PI;
			player.fireMissile(0);
			player.fireMissile(pi/4);
			player.fireMissile(pi/2);
			player.fireMissile(pi);
			player.fireMissile(5*pi/4);
			player.fireMissile(3*pi/2);
			player.fireMissile(7*pi/4);
		}
	}
	
	public void draw(Graphics2D g2){
		
	}

}
