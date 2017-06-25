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
	
	public Player p1;
	public Player p2;
	
	private boolean isPaused;
	
	private KeyPressHandler p1keys;
	private KeyPressHandler p2keys;
	
	private InputMap inputMap;
	private ActionMap actionMap;
	
	public GameArea(int width, int height) {
		this.width = width;
		this.height = height;
		
		Player.setDim(width, height);
		Bullet.setDim(width, height);
		
		p1 = new Player(400, 400, 1);
		p2 = new Player(200, 200, 2);
		
		// set up box with solid white background
		bg = new Background(this, width, height);
		
		setPreferredSize(new Dimension(width, height));
		repaint();
		
		
		p1keys = new KeyPressHandler(1);
		p1.setKeys(p1keys);
		this.addKeyListener(p1keys);
		
		p2keys = new KeyPressHandler(2);
		p2.setKeys(p2keys);
		this.addKeyListener(p2keys);
		
		inputMap = getInputMap(JPanel.WHEN_FOCUSED);
		actionMap = getActionMap();
		setKeyBindings();
		
		isPaused = true;
		play();
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// draw BG
		bg.draw(g2);
		
		p1.drawBullets(g2);
		p2.drawBullets(g2);
		
		p1.draw(g2);
		p2.draw(g2);
		
		bg.drawHealthBars(g2);
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
								if(timeLeft > 0){
									sleep(timeLeft);
								}
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
			
			// update according to keypresses for this frame
			p1.update();
			p2.update();
			
			// update bg for this frame
			bg.update();
			// update positions for this frame
			do{
				// Check if each particle hits the box boundaries (must be done first as it resets collision)
				float firstCollisionTime = timeleft;
				p1.reset();
				p2.reset();

				float p1time;
				if((p1time = p1.checkBoundaryCollisions(timeleft)) < firstCollisionTime){
					firstCollisionTime = p1time;
				}
				
				float temptime;
				if((temptime = p2.checkBoundaryCollisions(timeleft)) < firstCollisionTime){
					firstCollisionTime = temptime;
				}
				
				for(Bullet b: p1.getBullets()){
					if(b == null){
						break;
					}
					if(b.active){
						b.reset();
						if((temptime = b.checkBoundaryCollisions(timeleft)) < firstCollisionTime){
							firstCollisionTime = temptime;
						}
						p2.intersects(b, timeleft);
					}
				}
				for(Bullet b: p2.getBullets()){
					if(b == null){
						break;
					}
					if(b.active){
						b.reset();
						if((temptime = b.checkBoundaryCollisions(timeleft)) < firstCollisionTime){
							firstCollisionTime = temptime;
						}
						p1.intersects(b, timeleft);
					}
				}
				
				p1.move(firstCollisionTime);
				p2.move(firstCollisionTime);
				
				// update remaining portion of time step to check
				timeleft -= firstCollisionTime;
				//System.out.println(timeleft);
				
			}while(timeleft > EPSILON_TIME); // until entire time step checked
			
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
