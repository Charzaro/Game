package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class Background {
	
	private final static int MAXNUMSTARS = 200;
	
	private JPanel panel;
	
	private BufferedImage bg;
	
	private int width;
	private int height;
	
	private Bullet[] stars;
	
	private int numActiveStars;
	
	public Background(JPanel panel, int width, int height) {
		this.panel = panel;
		
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
		g2.drawImage(bg, 0, 0, width, height, panel);
		
		for(Bullet b: stars){
			if(b == null){
				break;
			}
			b.draw(g2);
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
