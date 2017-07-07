package game;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import JSwing.MainFrame;


public class GameArea extends JPanel {

	// Constants
	private static final long serialVersionUID = 1L;
	
	private static final int UPDATE_RATE = 120; // Frames per second 
	private static final float EPSILON_TIME = 1e-3f; // 0.01
	
	private MainFrame parent;
	
	private Background bg; // class handling background and UI
	
	// dimensions of game area
	int width;
	int height;
	
	// Players
	public Player p1;
	public Player p2;
	
	// true when game is running
	boolean isPaused;
	
	// handle key inputs for players
	private KeyPressHandler p1keys;
	private KeyPressHandler p2keys;
	// input maps for creating key handlers (for some reason making them makes the handler work)
	private InputMap inputMap;
	private ActionMap actionMap;
	
	
	// Constructor
	public GameArea(MainFrame parent, int width, int height) {
		this.parent = parent;
		
		changeFrameRate(); // set frame rate conversions if not 120 fps

		// listener for clicks
		addMouseListener(new ClickListener());
		
		this.width = width;
		this.height = height;
		
		// tell the players and bullets what dimensions to use
		Player.setDim(width, height);
		Bullet.setDim(width, height);
		Missile.setDim(width, height);
		Animation.setDim(width, height);
		
		// iniate players at opposite ends of the screen
		p1 = new Player(40, height/2, 1);
		p2 = new Player(width-40, height/2, 2);
		
		// set up background and UI elements
		bg = new Background(this, width, height);
		
		// set panel size
		setPreferredSize(new Dimension(width, height));
		repaint();
		
		// Add key press handlers to each player
		
		p1keys = new KeyPressHandler(1);
		p1.setKeys(p1keys);
		this.addKeyListener(p1keys);
		
		p2keys = new KeyPressHandler(2);
		p2.setKeys(p2keys);
		this.addKeyListener(p2keys);
		
		
		/*
		p1.setKeys(new KeyStatus());
		p2.setKeys(new KeyStatus());
		*/
		
		// generate input maps so keypress handlers work
		inputMap = getInputMap(JPanel.WHEN_FOCUSED);
		actionMap = getActionMap();
		setKeyBindings();
		
		// start with game unpaused
		isPaused = true;
		play();
	}
	
	// set conversion factor for compensation for frame rate changes (scales up periodic things when less frames per sec)
	public void changeFrameRate(){
		Player.setUR(UPDATE_RATE);
		Background.setUR(UPDATE_RATE);
		Animation.setUR(UPDATE_RATE);
		Ability.setUR(UPDATE_RATE);
	}
	
	// draws game
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// draw BG
		bg.draw(g2);
		
		// draw bullets
		p1.drawBullets(g2);
		p2.drawBullets(g2);
		
		// draw players
		p1.draw(g2);
		p2.draw(g2);
		
		// draw UI
		bg.drawGameUI(g2);
	}
	
	// restarts game from starting state
	public void resetGame(){
		p1.reset(40, height/2);
		p2.reset(width-40, height/2);
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
			repaint();
		}
		
		// creates each new frame
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
				float firstCollisionTime = timeleft; // looks for first collision
				// reset collisions for each player
				p1.reset();
				p2.reset();

				// check for boundary collisions for players and bullets
				float temptime;
				if((temptime = p1.checkBoundaryCollisions(timeleft)) < firstCollisionTime){
					firstCollisionTime = temptime;
				}

				if((temptime = p2.checkBoundaryCollisions(timeleft)) < firstCollisionTime){
					firstCollisionTime = temptime;
				}
				
				for(Projectile b: p1.getBullets()){
					if(b == null){ // until first empty cell
						break;
					}
					if(b.isActive()){
						b.reset(); // reset collisions
						if((temptime = b.checkBoundaryCollisions(timeleft)) < firstCollisionTime){
							firstCollisionTime = temptime;
						}
						// check collisions with enemy
						p2.intersects(b, timeleft);
						if(b instanceof Missile){
							Missile m = (Missile) b;
							m.homex = p2.getX();
							m.homey = p2.getY();
						}
					}
				}
				// same as earlier loop
				for(Projectile b: p2.getBullets()){
					if(b == null){
						break;
					}
					if(b.isActive()){
						b.reset();
						if((temptime = b.checkBoundaryCollisions(timeleft)) < firstCollisionTime){
							firstCollisionTime = temptime;
						}
						p1.intersects(b, timeleft);
						if(b instanceof Missile){
							Missile m = (Missile) b;
							m.homex = p1.getX();
							m.homey = p1.getY();
						}
					}
				}
				
				p1.intersects(p2, timeleft);
				
				// move players to position at earliest collision
				p1.move(firstCollisionTime);
				p2.move(firstCollisionTime);
				
				// update remaining portion of time step to check
				timeleft -= firstCollisionTime;
				//System.out.println(timeleft);
				
			}while(timeleft > EPSILON_TIME); // until entire time step checked
			// check for the end of game and pause if a player has died
			positionCheck();
			if(p1.getHealth() == 0){
				pause();
			}
			if(p2.getHealth() == 0){
				pause();
			}
			
		}
		
		private void positionCheck(){
			// receneter player 1 if outside of bounds
			if(p1.getX() + p1.getEdges().getWidth()/2 > width){
				p1.adjust(p1.getX()-1, p1.getY());
				//System.out.println("USED");
			}
			else if(p1.getX() - p1.getEdges().getWidth()/2 < 0){
				p1.adjust(p1.getX()+1, p1.getY());
				//System.out.println("USED");
			}
			if(p1.getY() + p1.getEdges().getHeight()/2 > height){
				p1.adjust(p1.getX(), p1.getY()-1);
				//System.out.println("USED");
			}
			else if(p1.getY() - p1.getEdges().getHeight()/2 < 0){
				p1.adjust(p1.getX(), p1.getY()+1);
				//System.out.println("USED");
			}
			
			// recenter player 2 if out of bounds
			if(p2.getX() + p2.getEdges().getWidth()/2 > width){
				p2.adjust(p2.getX()-1, p2.getY());
				//System.out.println("USED");
			}
			else if(p2.getX() - p2.getEdges().getWidth()/2 < 0){
				p2.adjust(p2.getX()+1, p2.getY());
				//System.out.println("USED");
			}
			if(p2.getY() + p2.getEdges().getHeight()/2 > height){
				p2.adjust(p2.getX(), p2.getY()-1);
				//System.out.println("USED");
			}
			else if(p2.getY() - p2.getEdges().getHeight()/2 < 0){
				p2.adjust(p2.getX(), p2.getY()+1);
				//System.out.println("USED");
			}
		}
		



		
		
		// key bindings to make keypresshandler work, binds esc to pause/play
		private void setKeyBindings(){
			Action escAction = new AbstractAction(){
				public void actionPerformed(ActionEvent e){
					if(!isPaused){
						pause();
					}
					parent.openMenu();
				}
			};
			
			//Action upAction = new AbstractAction(){
			//	public void actionPerformed(ActionEvent e){
			//		if(!p1.Keys.up){
			//			p1.Keys.up = true;
			//		}
			//	}
			//};
			
			inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "escAction");
			
			actionMap.put("escAction", escAction);
			
			//KeyMapSetter km = new KeyMapSetter();
			//km.set(inputMap, actionMap, p1, p2);

		}
		
		// makes mouse clicks pause/play game
		private class ClickListener extends MouseAdapter{
			@Override
			public void mouseReleased(MouseEvent e){
				if(isPaused){
					if(p1.getHealth() == 0 || p2.getHealth() == 0){
						resetGame();
					}
					play();
				}
				else{
					pause();
				}
			}
		}

}
