package abilities;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;

import game.Player;
import game.Settings;

public class RailGun extends Ability {

private static final int COOLDOWN_SEC = 4; //3; // cooldown in seconds
	
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
			g2.setStroke(new BasicStroke(Settings.update_rate/duration/2));
			Point2D point = Settings.map.findClosestObstaclePoint(player.getX(), player.getY(), player.getAngle());
			g2.drawLine((int)(player.getX()), (int)(player.getY()), (int)(point.getX()), (int)(point.getY()));
			//g2.drawLine((int)(player.getX()), (int)(player.getY()), (int)(player.getX() + 2*Settings.getDimx()*Math.sin(player.getAngle())), (int)(player.getY()-2*Settings.getDimx()*Math.cos(player.getAngle())));
			
			
		}
	}
	
	@Override
	public void cool(){
		if(active){
			if(duration < 2*Settings.update_rate){
				player.setSteerSpeed(player.MAX_TURN_SPEED/(1+duration/7));
				boolean key;
				if(num == 1){
					key = player.getKeyPresses().ability1;
				}
				else{
					key = player.getKeyPresses().ability2;
				}
				if(!key){
					active = false;
					player.fireHitScan();
					player.setAccel(true);
					cooldown = Settings.update_rate*COOLDOWN_SEC;
					duration = 1;
					player.setSteerSpeed(player.MAX_TURN_SPEED);
				}
				duration += 1;
			}
			else{
				active = false;
				player.fireHitScan();
				player.setAccel(true);
				cooldown = Settings.update_rate*COOLDOWN_SEC;
				duration = 1;
				player.setSteerSpeed(player.MAX_TURN_SPEED);
			}
		}
		else if(cooldown > 0){
			cooldown --;
		}
		
	}

	@Override
	public void use() {
		if(cooldown <= 0){
			active = true;
			player.setAccel(false);
		}
	}
	
	public String getName(){
		return "Sniper Shot";
	}

}
