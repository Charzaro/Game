package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class GameArea extends JPanel {
	
	private static final int UPDATE_RATE = 30; // Frames per second 
	private static final float EPSILON_TIME = 1e-3f; // 0.01
	
	private Background bg;
	
	int width;
	int height;
	
	private Player p1;
	private volatile Bullet[] projectiles;
	
	private boolean isPaused;
	
	private KeyPressHandler keys;
	
	private InputMap inputMap;
	private ActionMap actionMap;
	
	int cooldown;

	public GameArea(int width, int height) {
		this.width = width;
		this.height = height;
		
		p1 = new Player(400, 400);
		projectiles = new Bullet[100];
		
		// set up box with solid white background
		bg = new Background(this, width, height);
		
		setPreferredSize(new Dimension(width, height));
		repaint();
		
		
		keys = new KeyPressHandler();
		this.addKeyListener(keys);
		
		inputMap = getInputMap(JPanel.WHEN_FOCUSED);
		actionMap = getActionMap();
		setKeyBindings();
		
		cooldown = 0;
		
		isPaused = true;
		play();
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// draw BG
		bg.draw(g2);
		
		for(Bullet b: projectiles){
			if(b != null){
				b.draw(g2);
			}	
		}
		
		p1.draw(g2, this);
	}
	
	// start time
		public void play(){
			if(isPaused){ // make sure not already playing
				isPaused = false;
				// start game thread
				Thread time = new Thread(){
					synchronized public void run(){
						while(true){
							
							long startTime, timeTaken, timeLeft;
							startTime = System.currentTimeMillis(); // get start time of tick
							
							// update positions
							update(); 
							repaint();
							
							timeTaken = System.currentTimeMillis() - startTime; // get time taken to update
							// time left after updating
							timeLeft = 1000L / UPDATE_RATE - timeTaken;
							
							try{ // wait for amount of time left in the tick
								sleep(timeLeft);
							}
							catch(Exception e){
								e.printStackTrace();
							}
							if(isPaused){ // go turned to false, stop
								break;
							}
							
						}
					}
				};
				// start simulation thread
				time.start();
			}
		}
		
		// pauses time
		public void pause(){
			isPaused = true; // switching this boolean lets the current time thread terminate
		}
		
		public void update(){
			float timeleft = 1.00f; // 100%
			p1.setVelocity(keys.getXResult(), keys.getYResult());
			if(keys.space && cooldown <= 0){
				fireBullet();
				cooldown = 5;
			}
			if(cooldown > 0){
				cooldown--;
			}
			bg.update();
			do{
				// Check if each particle hits the box boundaries (must be done first as it resets collision)
				float firstCollisionTime = timeleft;
				p1.reset();

				float p1time;
				if((p1time = p1.checkBoundaryCollisions(timeleft)) < firstCollisionTime){
					firstCollisionTime = p1time;
				}
				
				for(Bullet b: projectiles){
					if(b == null){
						break;
					}
					b.reset();
					if(b.checkBoundaryCollisions(timeleft) < 0.1){
						b.active = false;
					}
				}
				
				p1.move(firstCollisionTime);
				for(Bullet b: projectiles){
					if(b == null){
						break;
					}
					b.move(firstCollisionTime);
				}
				
				// update remaining portion of time step to check
				timeleft -= firstCollisionTime;
				//System.out.println(timeleft);
				
			}while(timeleft > EPSILON_TIME); // until entire time step checked
			
		}
		
		private synchronized void fireBullet(){
			Bullet newb = new Bullet(p1.getX(), p1.getY(), 5, 0, -20, Color.RED);
			for(int i=0; i<projectiles.length; i++){
				if(projectiles[i] == null){
					projectiles[i] = newb;
					return;
				}
				else if(!projectiles[i].active){
					projectiles[i] = newb;
					return;
				}
			}
			System.out.println("Bullet array is too small.");
		}
		
		private void setKeyBindings(){
			Action spaceAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					if(isPaused){
						play();
					}
					else{
						pause();
					}
				}
			};
			
			inputMap.put(KeyStroke.getKeyStroke("ESCAPE"), "spaceAction");

			actionMap.put("spaceAction", spaceAction);

		}

}