package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Background {
	
	private final static int MAXNUMSTARS = 800;
	
	private static float update_factor = 1;
	
	private GameArea parent;
	
	private BufferedImage bg;
	
	private int width;
	private int height;
	
	private Animation[] stars;
	
	public Background(GameArea parent, int width, int height) {
		this.parent = parent;
		
		this.width = width;
		this.height = height;
		
		bg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = bg.createGraphics();
		graphics.setPaint(Color.BLACK);
		graphics.fillRect(0, 0, width, height);
		
		stars = new Animation[MAXNUMSTARS];
	}
	
	public static void setUR(int ur){
		update_factor = 120/ur;
	}
	
	public void draw(Graphics2D g2){
		g2.drawImage(bg, 0, 0, width, height, parent);
		
		if(parent.isPaused){
			g2.setPaint(Color.GREEN);
			g2.setFont(new Font("SansSerif", Font.BOLD, 96));
			g2.drawString("PAUSED", width/2 - 200, height/2 - 100);
		}
		
		for(Animation s: stars){
			if(s == null){
				break;
			}
			s.draw(g2);
		}
	}
	
	private void drawPlayerUI(Graphics2D g2, Player p){
		int ph = p.getHealth();

		// Player Label
		g2.setPaint(p.color);
		g2.setFont(new Font("SansSerif", Font.BOLD, 24));
		if(p.id == 1){
			g2.drawString("Player 1", 20, 40);
		}
		if(p.id == 2){
			g2.drawString("Player 2", width - 120, 40);
		}
		
		
		// Health bar box
		g2.setPaint(Color.GRAY);
		g2.setStroke(new BasicStroke(20));
		if(p.id == 1){
			g2.drawLine(20, 60, 20 + (10*20), 60); // 10 * 20 because 10 max health each unti 20 wide
		}
		else if(p.id == 2){
			g2.drawLine(width - 20, 60, width - 20 - (10*20), 60); // 10 * 20 because 10 max health each unti 20 wide
		}
		
		
		// Health bar
		g2.setStroke(new BasicStroke(10));
		// get correct color
		if(ph <= 2){
			g2.setPaint(Color.RED);
		}
		else if(ph <= 5){
			g2.setPaint(Color.YELLOW);
		}
		else{
			g2.setPaint(Color.GREEN);
		}
		
		if(ph > 0){
			if(p.id == 1){
				g2.drawLine(20, 60, 20 + (ph*20), 60); // 10 * 20 because 10 max health each unti 20 wide
			}
			else if(p.id == 2){
				g2.drawLine(width - 20, 60, width - 20 -(ph*20), 60); // 10 * 20 because 10 max health each unti 20 wide
			}
		}
		else{
			// announce that player 2 wins if player 1 has no health
			if(p.id == 1){
				g2.setPaint(Color.MAGENTA);
				g2.setFont(new Font("SansSerif", Font.BOLD, 96));
				g2.drawString("Player 2 WINS", width/2 - 300, height/2 - 20);
			}
			else if(p.id == 2){
				g2.setPaint(Color.ORANGE);
				g2.setFont(new Font("SansSerif", Font.BOLD, 96));
				g2.drawString("Player 1 WINS", width/2 - 300, height/2 - 20);
			}
		}
		
		// Boost meter
		g2.setPaint(Color.CYAN);
		g2.setStroke(new BasicStroke(5));
		if(p.id == 1){
			g2.drawLine(20, 80, 20 + (p.boostFuel/2), 80);
		}
		else if(p.id == 2){
			g2.drawLine(width - 20, 80, width - 20 - (p.boostFuel/2), 80);
		}
		
		// Ability cooldowns
		int [] pCooldowns = p.getCooldowns();
		int x;
		if(p.id == 1){
			x = 20;
		}
		else if(p.id == 2){
			x = width - 200;
		}
		else{
			x = 0;
		}
		g2.setFont(new Font("SansSerif", Font.BOLD, 36));
		// Ability 1
		if(pCooldowns[0] == 0){
			g2.setPaint(Color.GREEN);
			g2.drawString(Integer.toString(0), x, 120);
		}
		else{
			//g2.setPaint(Color.GRAY);
			//g2.fillRect(x, 120, 50, 50);
			g2.setPaint(Color.WHITE);
			g2.drawString(Integer.toString(1+(pCooldowns[0]/(120/(int)update_factor))), x, 120);
		}
		
		// Ability 2
		if(pCooldowns[1] == 0){
			g2.setPaint(Color.GREEN);
			g2.drawString(Integer.toString(0), x + 60, 120);
		}
		else{
			g2.setPaint(Color.WHITE);
			g2.drawString(Integer.toString(1+(pCooldowns[1]/(120/(int)update_factor))), x + 60, 120);
		}
	}
	
	public void drawGameUI(Graphics2D g2){
		drawPlayerUI(g2, parent.p1);
		drawPlayerUI(g2, parent.p2);
	}
	
	private void addStars(){
		int frequency = 3;
		int max_size = 4;
		int min_size = 1;
		int numNewStars = (int)(Math.random()*frequency);
		for(int i=0; i<numNewStars; i++){
			int x = (int)(Math.random()*width);
			double randNum = Math.random();
			float r = (float)(randNum*randNum*(max_size - min_size)+min_size);
			
			Animation s = new Animation("star", x, 0, r, Color.WHITE);
			for(int j=0; j<stars.length; j++){
				if(stars[j] == null){
					stars[j] = s;
					return;
				}
				else if(!stars[j].active){
					stars[j] = s;
					return;
				}
			}
			System.out.println("Not enough room for all stars");
		}
	}

	public void update(){
		addStars();

		for(Animation s: stars){
			if(s == null){
				break;
			}
			//System.out.println("BulletY: " + b.getY());
			s.move();
		}
	}

}
