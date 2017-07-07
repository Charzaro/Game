package game;

import java.awt.Graphics2D;

public class DashAttack extends Ability {
	
	private static final int COOLDOWN_SEC = 3; // cooldown in seconds
	
	private volatile boolean active;

	public DashAttack(Player p, short num) {
		super(p, num);
		active = false;
	}

	@Override
	public void use() {
		if(cooldown <= 0){
			Thread t = new Thread(){
				synchronized public void run(){
					active = true;
					player.setInvuln(true);
					player.setSteering(false);
					player.setVelocity(40);
					try {
						sleep(100L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					player.setInvuln(false);
					player.setSteering(true);
					player.setVelocity(0);
					active = false;
				}
			};
			
			if(!active){
				cooldown = update_rate*COOLDOWN_SEC;
				t.start();
			}
		}
		
	}
	
	
	public void draw(Graphics2D g2){
		
	}

}
