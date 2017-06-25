package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Player {
	
	private static final String p1ImageFilename = "pixel ship2.png";
	
	private static final int MAX_VELOCITY = 10;
	private static final double TURN_SPEED = Math.PI/24;
	private static final int RATE_OF_FIRE = 0;
	private static final int OVERHEAT_AFTER = 5;
	
	private static final int BULLET_SPEED = 20;
	
	private static final boolean SHOW_BOXES = false;
	
	private static int dimx = 800;
	private static int dimy = 800;
	
	public int id;
	
	private int testw = 40;
	private int testh = 50;
	
	private float xpos;
	private float ypos;
	
	public float angle;
	public float velocity;
	private float xvol;
	private float yvol;
	
	private KeyPressHandler keys;
	
	public int cooldown;
	public int overheat;
	
	private int health;
	
	private Collision earliestCollision;
	private Collision tempCollision;
	
	private BufferedImage image;
	
	private volatile Bullet[] bullets;

	public Player(int startx, int starty, int id) {
		this.id = id;
		
		xpos = startx;
		ypos = starty;
		angle = 0;
		velocity = 0;
		xvol = 0;
		yvol = 0;
		
		cooldown = 0;
		overheat = 0;
		
		health = 10;
		
		bullets = new Bullet[100];
		
		earliestCollision = new Collision();
		tempCollision = new Collision();
		
		image = new BufferedImage(testw, testh, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(Color.RED);
		int[] xs = {0, testw/2, testw};
		int[] ys = {testh, 0, testh};
		graphics.fillPolygon(xs, ys, 3);
		/*
		try{
			image = ImageIO.read(new File(p1ImageFilename));
			radius = image.getWidth()/2;
		}
		catch(IOException e){
			System.out.println("Could not open ship image: " + e);
		}
		*/
	}
	
	public static void setDim(int x, int y){
		dimx = x;
		dimy = y;
	}
	
	public float getX(){
		return xpos;
	}
	
	public float getY(){
		return ypos;
	}
	
	public float getXvol(){
		return xvol;
	}
	
	public float getYvol(){
		return yvol;
	}
	
	public float getAngle(){
		return angle;
	}
	
	public float getVelocity(){
		return velocity;
	}
	
	public int getWidth(){
		return image.getWidth();
	}
	
	public int getHeight(){
		return image.getHeight();
	}
	
	public int getHealth(){
		return health;
	}
	
	public Bullet[] getBullets(){
		return bullets;
	}
	
	public void setKeys(KeyPressHandler keys){
		this.keys = keys;
	}
	
	public void draw(Graphics2D g2){
		AffineTransform at = new AffineTransform();

		// Applies transforms in reverse order
        // 3. move to x,y coords
        at.translate(xpos, ypos);

        // 2. rotate
        at.rotate(angle);

        // 1. move rotation point to the middle of the image
        at.translate(-image.getWidth()/2, -image.getHeight()/2);

        // draw the image
        g2.drawImage(image, at, null);
        
        if(SHOW_BOXES){
        	g2.setPaint(Color.BLUE);
        	g2.draw(getBounds());
        }
		//g2.drawImage(image, (int)(xpos-image.getWidth()/2), (int) (ypos-image.getHeight()/2), p);
		//g2.setPaint(Color.BLUE);
		//g2.fillOval((int)(xpos-radius), (int) (ypos-radius), 2*radius, 2*radius);
	}
	
	public void drawBullets(Graphics2D g2){
		for(Bullet b: bullets){
			if(b != null){
				b.draw(g2);
			}	
		}
	}
	
	public Rectangle getBounds(){
		float cos = Math.abs((float)Math.cos(angle));
		float sin = Math.abs((float)Math.sin(angle));
		float xdist = (image.getWidth() * cos *cos) + (image.getHeight() * sin*sin);
		float ydist = (image.getWidth() * sin*sin) + (image.getHeight() * cos*cos);
		return new Rectangle((int)(xpos - xdist/2), (int)(ypos-ydist/2), (int)(xdist), (int)(ydist));
		//return new Rectangle((int)xpos-image.getWidth()/2, (int)ypos-image.getHeight()/2, image.getWidth(), image.getHeight());
	}
	
	public void reset(){
		earliestCollision.reset();
		tempCollision.reset();
	}
	
	public float checkBoundaryCollisions(float time){
		Physics.checkBoxCollision(xpos, ypos, xvol, yvol, image.getWidth()/2, image.getHeight()/2, 0, 0, dimx, dimy, time, tempCollision);
		if(tempCollision.t < earliestCollision.t){
			earliestCollision.copy(tempCollision);
			return tempCollision.t;
		}
		return time;
	}
	
	public void intersects(Bullet b, float timelimit){
		if(getBounds().intersects(b.getBounds())){
			b.active = false;
			if(health > 0){
				health--;
			}
			if(health < 0){
				health = 0;
			}
			System.out.println(health);
		}
	}
	
	public void updateVolComponents(){
		xvol = -velocity*(float)Math.sin(angle);
		yvol = velocity*(float)Math.cos(angle);
	}
	
	public void update(){
		if(!(keys.up ^ keys.down)){
			if(velocity != 0){
				velocity -= velocity/Math.abs(velocity);
				updateVolComponents();
			}	
		}
		else if(keys.up && velocity > -MAX_VELOCITY){
			velocity--;
			updateVolComponents();
		}
		else if(keys.down && velocity < MAX_VELOCITY/2){
			velocity++;
			updateVolComponents();
		}
		if(keys.right && !keys.left){
			angle += TURN_SPEED;
			updateVolComponents();
		}
		else if(!keys.right && keys.left){
			angle -= TURN_SPEED;
			updateVolComponents();
		}
		
		boolean fire = false;
		if(keys.space && cooldown <= 0 && overheat < OVERHEAT_AFTER*10){
			fire = true;
			cooldown = RATE_OF_FIRE;
			overheat += 11;
		}
		if(cooldown > 0){
			cooldown--;
		}
		if(overheat > 0){
			overheat--;
		}
		if(fire){
			fireBullet();
		}
	}
	
	public void move(float time){
		if(earliestCollision.t <= time){
			xpos = earliestCollision.getNewX(xpos, xvol);
			ypos = earliestCollision.getNewY(ypos, yvol);
			xvol = earliestCollision.nspeedx;
			yvol = earliestCollision.nspeedy;
		}
		else{
			xpos += xvol*time;
			ypos += yvol*time;
		}
		
		for(Bullet b: bullets){
			if(b == null){
				break;
			}
			b.move(time);
		}
	}
	
	
	private synchronized void fireBullet(){
		Bullet newb = new Bullet(xpos, ypos, 5, 10, (velocity/2)-BULLET_SPEED, angle, Color.RED);

		for(int i=0; i<bullets.length; i++){
			if(bullets[i] == null){
				bullets[i] = newb;
				return;
			}
			else if(!bullets[i].active){
				bullets[i] = newb;
				return;
			}
		}
		System.out.println("Bullet array is too small.");
	}
	
	
	// testing functions, remove later?
	public void setVelocity(float x, float y){
		this.xvol = x;
		this.yvol = y;
	}

}
