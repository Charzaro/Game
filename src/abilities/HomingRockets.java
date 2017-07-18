package abilities;

import java.awt.Graphics2D;

import game.Player;
import game.Settings;

public class HomingRockets extends Ability {
	
	private static int COOLDOWN_SEC = 6;

	public HomingRockets(Player p, short num) {
		super(p, num);
	}

	@Override
	public void use() {
		if(cooldown <= 0){
			cooldown = Settings.update_rate * COOLDOWN_SEC; // 10 second cool down ( ex. goes down 120 every second bc 120 fps)
			float pi = (float)Math.PI;
			player.fireMissile(player.getAngle());
			player.fireMissile(player.getAngle() + pi/4);
			player.fireMissile(player.getAngle() - pi/4);
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		// TODO Auto-generated method stub

	}
	
	public String getName(){
		return "Homing Rockets";
	}

}
