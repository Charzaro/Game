package game;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

public class RailGun extends Ability {

private static final int COOLDOWN_SEC = 2; // cooldown in seconds
	
	private volatile boolean active;
	private int duration;

	public RailGun(Player p, short num) {
		super(p, num);
		active = false;
		duration = 1;
	}
	
	public void draw(Graphics2D g2){
		if(active){
			g2.setPaint(player.color);
			g2.setStroke(new BasicStroke(update_rate/duration/2));
			g2.drawLine((int)(player.getX()), (int)(player.getY()), (int)(player.getX() + player.dimx*Math.sin(player.getAngle())), (int)(player.getY()-player.dimx*Math.cos(player.getAngle())));
			
			if(duration < update_rate){
				player.setSteerSpeed(player.MAX_TURN_SPEED/(1+duration/5));
				boolean key;
				if(num == 1){
					key = player.getKeys().ability1;
				}
				else{
					key = player.getKeys().ability2;
				}
				if(!key){
					active = false;
					player.fireHitScan();
					player.setAccel(true);
					cooldown = update_rate*COOLDOWN_SEC;
					duration = 1;
					player.setSteerSpeed(player.MAX_TURN_SPEED);
				}
				duration += 1;
			}
			else{
				active = false;
				player.fireHitScan();
				player.setAccel(true);
				cooldown = update_rate*COOLDOWN_SEC;
				duration = 1;
				player.setSteerSpeed(player.MAX_TURN_SPEED);
			}
		}
	}

	@Override
	public void use() {
		if(cooldown <= 0){
			active = true;
			player.setAccel(false);
		}
	}

}
