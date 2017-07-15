package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Reflect extends Ability {
	
	private static final int COOLDOWN_SEC = 7;
	
	private int duration;

	public Reflect(Player p, short num) {
		super(p, num);
		duration = 0;
	}

	@Override
	public void use() {
		if(cooldown <= 0){
			player.setReflect(true);
			cooldown = Settings.update_rate*COOLDOWN_SEC;
			duration++;
		}
		

	}

	@Override
	public void draw(Graphics2D g2) {
		if(duration > 0){
			duration++;
			int offset = duration - 10;
			g2.setPaint(Color.BLUE);
			g2.setStroke(new BasicStroke(5));
			g2.drawOval((int)(player.getX()-60), (int)(player.getY()-Math.abs(60 - 2*duration)), 120, Math.abs(120 - 4*duration));
			g2.drawOval((int)(player.getX()-60), (int)(player.getY()-Math.abs(60 - 2*offset)), 120, Math.abs(120 - 4*offset));
			offset -= 10;
			g2.drawOval((int)(player.getX()-60), (int)(player.getY()-Math.abs(60 - 2*offset)), 120, Math.abs(120 - 4*offset));
			//g2.drawOval((int)(player.getX()-duration), (int)(player.getY()-duration), 2*duration, 2*duration);
			//g2.drawOval((int)(player.getX()-duration/2), (int)(player.getY()-duration/2), duration, duration);
			if(duration > 60){
				player.setReflect(false);
				duration = 0;
				
			}
		}
		

	}
	
	public String getName(){
		return "Reflect";
	}

}
