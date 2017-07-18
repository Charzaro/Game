package abilities;

import java.awt.Graphics2D;

import game.Player;
import game.Settings;

/*
 * DashAttack.java
 * 
 * An abiltiy that launchs the player forward with immunity and damages enemy by 2 on collision
 */

public class DashAttack extends Ability {
	
	private static final int COOLDOWN_SEC = 4; // cooldown in seconds
	
	private volatile boolean active; // true if ability is in use

	public DashAttack(Player p, short num) {
		super(p, num);
		active = false;
	}

	// uses the ability
	@Override
	public void use() {
		if(cooldown <= 0){ // when cooldown is off
			// dashing thread
			Thread t = new Thread(){
				synchronized public void run(){
					// activate invuln and disable steering, immediately set velocity to 40
					active = true;
					player.setInvuln(true);
					player.setSteering(false);
					player.setVelocity(40);
					//wait 100 milliseconds
					try {
						sleep(100L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					// re-enable vulnerability, steering, and set velocity to 0
					player.setInvuln(false);
					player.setSteering(true);
					player.setVelocity(0);
					active = false;
				}
			};
			
			if(!active){ // if not already in use, use
				cooldown = Settings.update_rate*COOLDOWN_SEC;
				t.start();
			}
		}
		
	}
	
	
	public void draw(Graphics2D g2){
		
	}
	
	public String getName(){
		return "Dash Attack";
	}

}
