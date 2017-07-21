package game;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import animation.Animation;
import animation.Star;

/*
 * Background.java
 * 
 * Class for drawing the background and UI elements of the game area
 */
public class Background {	
	// size of the star array
	private final static int MAXNUMSTARS = 400;
	
	private final static int GAP = 20;

	// Game Area using the background
	private GameArea parent;
	// image in the back, currently just a black image
	private BufferedImage bg;
	
	// array of star animations
	private Animation[] stars;
	
	public Background(GameArea parent) {
		this.parent = parent;
		
		// create a black image the size of the game area
		bg = new BufferedImage(Settings.getDimx(), Settings.getDimy(), BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = bg.createGraphics();
		graphics.setPaint(Color.BLACK);
		graphics.fillRect(0, 0, Settings.getDimx(), Settings.getDimy());
		
		stars = new Animation[MAXNUMSTARS];
		addStars();
	}


	// draws background
	public void draw(Graphics2D g2){
		// fill black
		g2.drawImage(bg, 0, 0, Settings.getDimx(), Settings.getDimy(), parent);
		// draw a big PAUSED label when paused
		
		// draw star animations
		for(Animation s: stars){
			if(s == null){
				break;
			}
			s.draw(g2);
		}
	}
	
	// draw game UI for a player
	private void drawPlayerUI(Graphics2D g2, Player p){
		int ph = p.getHealth(); // players health

		// Player Label *************************************************
		g2.setPaint(p.color);
		g2.setFont(new Font("SansSerif", Font.BOLD, 24));
		FontMetrics fm = g2.getFontMetrics();
		int h =  fm.getAscent() - fm.getHeight()/2;
		if(p.id == 1){
			g2.drawString("Player 1", GAP, GAP + 2*h);
		}
		if(p.id == 2){
			g2.drawString("Player 2", Settings.getDimx() - GAP - fm.stringWidth("Player 2"), GAP + 2*h);
		}
		
		
		// Health bar box *********************************************
		g2.setPaint(Color.GRAY);
		g2.setStroke(new BasicStroke(20));
		if(p.id == 1){
			g2.drawLine(GAP, GAP*3, GAP + (10*20), GAP*3); // 10 * 20 because 10 max health each unti 20 wide
		}
		else if(p.id == 2){
			g2.drawLine(Settings.getDimx() - GAP, GAP*3, Settings.getDimx() - GAP - (10*20), GAP*3); // 10 * 20 because 10 max health each unti 20 wide
		}
		
		
		// Health bar ************************************************
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
				g2.drawLine(GAP, GAP*3, 20 + (ph*20), GAP*3); // 10 * 20 because 10 max health each unti 20 wide
			}
			else if(p.id == 2){
				g2.drawLine(Settings.getDimx() - GAP, GAP*3, Settings.getDimx() - GAP -(ph*20), GAP*3); // 10 * 20 because 10 max health each unti 20 wide
			}
		}
		
		// Boost meter *****************************************************
		g2.setPaint(Color.CYAN);
		g2.setStroke(new BasicStroke(5));
		if(p.id == 1){
			g2.drawLine(GAP, GAP*4, GAP + (p.boostFuel/2), GAP*4);
		}
		else if(p.id == 2){
			g2.drawLine(Settings.getDimx() - GAP, GAP*4, Settings.getDimx() - GAP - (p.boostFuel/2), GAP*4);
		}
		
		// Ability cooldowns *************************************************
		int [] pCooldowns = p.getCooldowns();
		int x;
		int y;
		if(p.id == 1){
			x = 4*GAP;
		}
		else if(p.id == 2){
			x = Settings.getDimx() - GAP*10;
		}
		else{
			x = 0;
		}
		y = GAP*5 + 10;
		
		String[] abilityNames = p.getAbilityNames();
		
		g2.setFont(new Font("SansSerif", Font.BOLD, 36));
		
		// Ability 1 *************************************************
		if(pCooldowns[0] == 0){
			g2.setPaint(Color.GREEN);
			drawWord(g2, Integer.toString(0), x, y);
		}
		else{
			g2.setPaint(Color.WHITE);
			drawWord(g2, Integer.toString(1+(pCooldowns[0]/(120/(int)Settings.update_factor))), x, y);
		}
		g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
		drawWord(g2, abilityNames[0], x, y + GAP + 10);
		
		// Ability 2 *************************************************
		
		g2.setFont(new Font("SansSerif", Font.BOLD, 36));
		if(pCooldowns[1] == 0){
			g2.setPaint(Color.GREEN);
			drawWord(g2, Integer.toString(0), x + GAP*6, y);
		}
		else{
			g2.setPaint(Color.WHITE);
			drawWord(g2, Integer.toString(1+(pCooldowns[1]/(120/(int)Settings.update_factor))), x + GAP*6, y);
		}
		g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
		drawWord(g2, abilityNames[1], x + GAP*6, y + GAP + 10);
	}
	
	// draws UI for the game
	public void drawGameUI(Graphics2D g2){
		drawPlayerUI(g2, parent.p1);
		drawPlayerUI(g2, parent.p2);

		// announce that player 2 wins if player 1 has no health
		int p1h = parent.p1.getHealth();
		int p2h = parent.p2.getHealth();
		if(p1h <= 0 && p2h > 0){
			g2.setPaint(Color.MAGENTA);
			g2.setFont(new Font("SansSerif", Font.BOLD, 96));
			drawWord(g2, "Player 2 WINS", Settings.getDimx()/2, Settings.getDimy()/2);
		}
		else if(p2h <= 0 && p1h > 0){
			g2.setPaint(Color.ORANGE);
			g2.setFont(new Font("SansSerif", Font.BOLD, 96));
			drawWord(g2, "Player 1 WINS", Settings.getDimx()/2, Settings.getDimy()/2);
		}
		else if(p1h <= 0 && p2h <= 0){
			g2.setPaint(Color.RED);
			g2.setFont(new Font("SansSerif", Font.BOLD, 96));
			drawWord(g2, "TIE", Settings.getDimx()/2, Settings.getDimy()/2);
		}
		
		if(parent.isPaused){
			g2.setPaint(Color.GREEN);
			g2.setFont(new Font("SansSerif", Font.BOLD, 96));
			// draw the word Paused above the center of the screen
			drawWord(g2, "PAUSED", Settings.getDimx()/2, Settings.getDimy()/3);
		}

	}
	
	// adds a star animation to the background at the top of the screen
	private void addStars(){
		
		for(int i=0; i<MAXNUMSTARS; i++){
			int x = (int)(Math.random()*Settings.getDimx()); // random location across the top of the screen
			int y = (int)(Math.random()*Settings.getDimy()); // random location across the top of the screen
			double randNum = Math.random()*10; // random number 0 to 1, when squared still gives 0 to 1 but smaller numbers more likely
			float r;
			if(randNum < 4){
				r = 1;
			}
			else if(randNum < 7){
				r = 2;
			}
			else if(randNum < 9){
				r = 3;
			}
			else{
				r = 4;
			}
			
			// adds a star animation to the first empty spot in the array, or the first spot with a star animation that has finished
			Animation s = new Star(x, y, r, Color.WHITE);
			for(int j=0; j<stars.length; j++){
				if(stars[j] == null){
					stars[j] = s;
					break;
				}
				else if(!stars[j].active){
					stars[j] = s;
					break;
				}
			}
			// if all spots are filled with stars in use, print an error
			//System.err.println("Not enough room for all stars");
		}
	}
	
	public void drawWord(Graphics2D g2, String s,  int x, int y){
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(s, x - fm.stringWidth(s)/2, (y*2 - fm.getHeight())/2 + fm.getAscent());
	}

	// updates each frame, adding starts and moving stars
	public void update(){
		//addStars(0);
	}
	
	public void move(){
		for(Animation s: stars){
			if(s == null){
				break;
			}
			s.move();
		}
	}

}
