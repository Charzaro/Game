package game;

public class DashAttack extends Ability {
	
	private static final int COOLDOWN_SEC = 2; // cooldown in seconds
	
	private volatile boolean active;

	public DashAttack(Player p) {
		super(p);
		active = false;
	}

	@Override
	public void use() {
		if(cooldown <= 0){
			Thread t = new Thread(){
				synchronized public void run(){
					active = false;
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
					cooldown = update_rate*COOLDOWN_SEC;
				}
			};
			
			if(!active){
				t.start();
			}
		}
		
	}

}
