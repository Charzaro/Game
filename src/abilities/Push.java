package abilities;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import game.Player;
import game.Settings;

public class Push extends Ability {
	
	private static final int COOLDOWN_SEC = 6;
	public static final int PUSH_RANGE = 150;
	
	private int duration;
	private boolean active;

	public Push(Player p, short num) {
		super(p, num);
		duration = 0;
	}

	@Override
	public void use() {
		if(cooldown <= 0 && !active){
			player.setPush(true);
			active = true;
		}
		
	}
	
	@Override
	public void cool(){
		if(cooldown > 0){
			cooldown--;
		}
		if(active){
			duration++;
		}
		if(duration > 60*Settings.update_factor){
			player.setPush(false);
			cooldown = COOLDOWN_SEC*Settings.update_rate;
			duration = 0;
			active = false;
		}
	}

	@Override
	public void draw(Graphics2D g2) {
		if(active){
			int radius = duration*PUSH_RANGE/60;
			g2.setPaint(Color.BLUE);
			g2.setStroke(new BasicStroke(5));
			g2.drawOval((int)player.getX()-PUSH_RANGE/60, (int)player.getY()-PUSH_RANGE/60, 2*PUSH_RANGE/60, 2*PUSH_RANGE/60);
			g2.drawOval((int)player.getX()-radius, (int)player.getY()-radius, 2*radius, 2*radius);
			radius = (duration-20)*PUSH_RANGE/60;
			g2.drawOval((int)player.getX()-radius, (int)player.getY()-radius, 2*radius, 2*radius);
			radius = (duration-40)*PUSH_RANGE/60;
			g2.drawOval((int)player.getX()-radius, (int)player.getY()-radius, 2*radius, 2*radius);
		}

	}

	@Override
	public String getName() {
		return "Push";
	}

}
