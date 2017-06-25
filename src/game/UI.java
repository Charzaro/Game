package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class UI {
	
	private int width;
	private int height;

	public UI(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void drawHealthBars(Graphics2D g2, int p1H, int p2H){
		g2.setStroke(new BasicStroke(10));
		if(p1H <= 2){
			g2.setPaint(Color.RED);
		}
		else if(p1H <= 5){
			g2.setPaint(Color.YELLOW);
		}
		else{
			g2.setPaint(Color.GREEN);
		}
		if(p1H > 0){
			g2.drawLine(20, 20, 20+(p1H*20), 20);
		}
		
		if(p2H <= 2){
			g2.setPaint(Color.RED);
		}
		else if(p2H <= 5){
			g2.setPaint(Color.YELLOW);
		}
		else{
			g2.setPaint(Color.GREEN);
		}
		if(p2H > 0){
			g2.drawLine(width-20, 20, width - 20 - (p2H*20), 20);
		}
		
	}
	
	public void draw(Graphics2D g2, int p1H, int p2H){
		drawHealthBars(g2, p1H, p2H);
	}

}
