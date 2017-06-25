package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Background {
	
	private final static int MAXNUMSTARS = 200;
	
	private GameArea parent;
	
	private BufferedImage bg;
	
	private int width;
	private int height;
	
	private Bullet[] stars;
	
	private int numActiveStars;
	
	public Background(GameArea parent, int width, int height) {
		this.parent = parent;
		
		this.width = width;
		this.height = height;
		
		numActiveStars = 0;
		
		bg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = bg.createGraphics();
		graphics.setPaint(Color.BLACK);
		graphics.fillRect(0, 0, width, height);
		
		stars = new Bullet[MAXNUMSTARS];
	}
	
	public void draw(Graphics2D g2){
		g2.drawImage(bg, 0, 0, width, height, parent);
		
		for(Bullet b: stars){
			if(b == null){
				break;
			}
			b.draw(g2);
		}
	}
	
	public void drawHealthBars(Graphics2D g2){
		int p1h = parent.p1.getHealth();
		int p2h = parent.p2.getHealth();
		
		g2.setStroke(new BasicStroke(10));
		
		if(p1h <= 2){
			g2.setPaint(Color.RED);
		}
		else if(p1h <= 5){
			g2.setPaint(Color.YELLOW);
		}
		else{
			g2.setPaint(Color.GREEN);
		}
		
		if(p1h > 0){
			g2.drawLine(20, 20, 20 + (p1h*20), 20);
		}
		
		if(p2h <= 2){
			g2.setPaint(Color.RED);
		}
		else if(p2h <= 5){
			g2.setPaint(Color.YELLOW);
		}
		else{
			g2.setPaint(Color.GREEN);
		}
		
		if(p2h > 0){
			g2.drawLine(width - 20, 20, width - (20 + (p2h*20)), 20);
		}
	}
	
	private void addStars(){
		int frequency = 3;
		int max_size = 4;
		int min_size = 1;
		int size2speed_factor = 3;
		int numNewStars = (int)(Math.random()*frequency);
		for(int i=0; i<numNewStars; i++){
			int x = (int)(Math.random()*width);
			double randNum = Math.random();
			float r = (float)(randNum*randNum*(max_size - min_size)+min_size);
			Bullet b = new Bullet(x, 0, r, r, size2speed_factor*r, (float)(0), Color.WHITE);
			for(int j=0; j<stars.length; j++){
				if(stars[j] == null){
					stars[j] = b;
					return;
				}
				else if(!stars[j].active){
					stars[j] = b;
					return;
				}
			}
			System.out.println("Not enough room for all stars");
		}
	}

	public void update(){
		addStars();

		for(Bullet b: stars){
			if(b == null){
				break;
			}
			//System.out.println("BulletY: " + b.getY());
			if(b.active && (b.getY() >= height || b.getY() < 0)){
				b.active = false;
			}
			b.move(1);
		}
	}

}
