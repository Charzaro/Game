package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Player {
	
	// Constants
	private static final String p1ImageFilename = "pixel ship2.png";
	
	private static final int MAX_VELOCITY = 3;
	private static final double TURN_SPEED = Math.PI/96;
	private static final int RATE_OF_FIRE = 4;
	private static final int OVERHEAT_MAX = 100;
	private static final int OVERHEAT_AFTER = 5;
	public static final int MAX_HEALTH = 10;
	public static final int BOOST_MAX = 30;
	private static final int FUEL_MAX = 500;
	
	private static final int BULLET_SPEED = 5;
	
	private static final boolean SHOW_BOXES = false;
	
	// dimensions of the box containing the player
	private static int dimx = 800;
	private static int dimy = 800;
	
	// ratio used for converting constants when using different framerates
	private static int update_factor = 1;
	
	public int id; // number player
	// for creating placeholder triangles
	private int testw = 40;
	private int testh = 50;
	
	// colors
	public Color color;
	private Color bulletColor;

	// position
	private float xpos;
	private float ypos;
	// angle of rotation, 0 being up (radiians)
	public float angle;
	
	// velocity total and velocity in x and y planes
	public float velocity;
	private float xvol;
	private float yvol;
	
	// keypress handler
	private KeyPressHandler keys;
	public KeyStatus Keys;
	
	// counts for firing shots
	private int cooldown;
	private int overheat;
	private int overheat_wait;
	
	public int boostFuel;
	private int collisionCooldown;
	
	private Ability ability1;
	private Ability ability2;
	
	private Animation[] animations;
	
	// health count
	private int health;
	
	// statuses
	private volatile boolean steering_enabled;
	private volatile boolean invulnerable;
	private volatile boolean accel_enabled;
	
	// all purpose counter
	protected int duration;
	
	// used for storing collisions
	private Collision earliestCollision;
	private Collision tempCollision;
	
	// Image for player
	private BufferedImage image;
	private BufferedImage invulnImage;
	
	// bullets shot by this player
	private volatile IProjectile[] bullets;
	
	// for developer cheats
	private boolean cheat = false;

	
	// Constructor
	public Player(int startx, int starty, int id) {
		this.id = id;
		
		// set colors based on player number
		if(id == 1){
			color = Color.ORANGE;
			bulletColor = Color.YELLOW;
		}
		else{
			color = Color.MAGENTA;
			bulletColor = Color.RED;
		}
		
		xpos = startx;
		ypos = starty;
		angle = 0;
		velocity = 0;
		xvol = 0;
		yvol = 0;
		
		cooldown = 0;
		overheat = 0;
		overheat_wait = 0;
		collisionCooldown = 0;
		boostFuel = FUEL_MAX;
		
		duration = 0;
		
		ability1 = new RocketBurst(this);
		ability2 = new DashAttack(this);
		
		animations = new Animation[200];
		
		health = MAX_HEALTH;
		
		invulnerable = false;
		steering_enabled = true;
		accel_enabled = true;
		
		bullets = new IProjectile[300];
		
		earliestCollision = new Collision();
		tempCollision = new Collision();
		
		// Load image (or draw placeholder triangle currently
		image = new BufferedImage(testw, testh, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(color);
		int[] xs = {0, testw/2, testw};
		int[] ys = {testh, 0, testh};
		graphics.fillPolygon(xs, ys, 3);
		
		invulnImage = new BufferedImage(testw, testh+20, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2 = invulnImage.createGraphics();
		
		int[] xs2 = {0, testw/2 , testw};
		int[] ys2 = {testh+20, 0+20, testh+20};
		graphics2.setPaint(Color.GRAY);
		graphics2.fillPolygon(xs, ys, 3);
		graphics2.setPaint(color);
		graphics2.fillPolygon(xs2, ys2, 3);
		
		
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
	
	// set global dimensions
	public static void setDim(int x, int y){
		dimx = x;
		dimy = y;
	}
	
	// set frame rate factor
	public static void setUR(int ur){
		update_factor = 120/ur;
	}
	
	// GETTERS
	
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
	
	public IProjectile[] getBullets(){
		return bullets;
	}
	
	public int[] getCooldowns(){
		return new int[] {ability1.cooldown, ability2.cooldown};
	}
	
	// SETTERS
	
	public void setKeys(KeyPressHandler keys){
		this.keys = keys;
	}
	
	//public void setkeys(KeyStatus keys){
		//this.Keys = keys;
	//}
	
	public synchronized void setInvuln(boolean b){
		invulnerable = b;
	}
	public synchronized void setSteering(boolean b){
		steering_enabled = b;
	}
	public synchronized void setAccel(boolean b){
		accel_enabled = b;
	}
	
	// draw player
	public void draw(Graphics2D g2){
		
		// draw animations
		for(Animation a: animations){
        	if(a != null){
				a.draw(g2);
			}
        }
		
		AffineTransform at = new AffineTransform();

		// Applies transforms in reverse order
        // 3. move to x,y coords
        at.translate(xpos, ypos);

        // 2. rotate
        at.rotate(angle);

        // 1. move rotation point to the middle of the image
        at.translate(-image.getWidth()/2, -image.getHeight()/2);

        // draw the image
        if(invulnerable){
        	g2.drawImage(invulnImage, at, null);
		}
        else{
        	g2.drawImage(image, at, null);
        }
        
        //g2.drawLine((int)(xpos), (int)(ypos), (int)(xpos + (100*Math.sin(angle))), (int)(ypos - (100*Math.cos(angle))));
        
        // shows hitboxes for dev purposes
        if(SHOW_BOXES){
        	g2.setPaint(Color.BLUE);
        	g2.drawPolygon(getBounds());
        }
	}
	
	// draws bullets
	public void drawBullets(Graphics2D g2){
		for(IProjectile b: bullets){
			if(b != null){
				b.draw(g2);
			}	
		}
	}
	
	// resets values to initial
	public void reset(int startx, int starty){
		this.health = MAX_HEALTH;
		
		xpos = startx;
		ypos = starty;
		angle = 0;
		velocity = 0;
		xvol = 0;
		yvol = 0;
		
		cheat = false;
		
		cooldown = 0;
		overheat = 0;
		overheat_wait = 0;
		collisionCooldown = 0;
		boostFuel = FUEL_MAX;
		
		duration = 0;
		
		invulnerable = false;
		steering_enabled = true;
		accel_enabled = true;
		
		bullets = new IProjectile[300];
		
		ability1.reset();
		ability2.reset();
		
		earliestCollision = new Collision();
		tempCollision = new Collision();
	}
	
	public Rectangle getEdges(){
		float cos = Math.abs((float)Math.cos(angle));
		float sin = Math.abs((float)Math.sin(angle));
		float xdist = (image.getWidth() * cos *cos) + (image.getHeight() * sin*sin);
		float ydist = (image.getWidth() * sin*sin) + (image.getHeight() * cos*cos);

		return new Rectangle((int)(xpos - xdist/2), (int)(ypos-ydist/2), (int)(xdist), (int)(ydist));
	}
	
	
	// gets the hitbox of the player
	public Polygon getBounds(){
		
		/* hitbox is a rectangle that is
		 * 	image width * cos^2 plus image height * sin^2
		 *  by
		 *  image width * sin^2 plus image height * cos^2
		 */
		
		float cos = (float)Math.cos(angle);
		float sin = (float)Math.sin(angle);
		float comcos = (float)Math.cos(Math.PI/2 - angle);
		float comsin = (float)Math.sin(Math.PI/2 - angle);
		float[] dist1 = new float[] {(image.getHeight()/2)*sin, (image.getHeight()/2)*cos};
		float[] dist2 = new float[] {(image.getWidth()/2 * comsin), image.getWidth()/2 * comcos};

		
		int[] xs = {(int)(xpos - dist1[0] - dist2[0]), (int)(xpos + dist1[0]),(int)(xpos - dist1[0] + dist2[0])};
		int[] ys = {(int)(ypos + dist1[1] - dist2[1]), (int)(ypos - dist1[1]), (int)(ypos + dist1[1] + dist2[1])};
		
		return new Polygon(xs, ys, 3);
		
		//return new Rectangle((int)(xpos - xdist/2), (int)(ypos-ydist/2), (int)(xdist), (int)(ydist));
		//return new Rectangle((int)xpos-image.getWidth()/2, (int)ypos-image.getHeight()/2, image.getWidth(), image.getHeight());
	}
	
	// reset stored collisions
	public void reset(){
		earliestCollision.reset();
		tempCollision.reset();
	}
	
	// Check for collision with the boundary box in this frame
	public float checkBoundaryCollisions(float time){
		Physics.checkBoxCollision(xpos, ypos, xvol, yvol, getEdges().width/2, getEdges().height/2, 0, 0, dimx, dimy, time, tempCollision);
		if(tempCollision.t < earliestCollision.t){
			earliestCollision.copy(tempCollision);
			return tempCollision.t;
		}
		return time;
	}
	
	// check if player collides with a bullet
	public void intersects(IProjectile b, float timelimit){
		// check if boundary boxes intersect
		if(getBounds().intersects(b.getBounds())){
			// deactivate bullet
			b.deactivate();
			
			if(invulnerable){
				return;
			}
			
			// animation
			Animation newa = new Animation("hit", xpos, ypos, 2, Color.WHITE);
			for(int i=0; i<animations.length; i++){
				if(animations[i] == null){
					animations[i] = newa;
					break;
				}
				else if(!animations[i].active){
					animations[i] = newa;
					break;
				}
			}
			
			// injure player
			if(health > 0){
				health--;
			}
			if(health < 0){
				health = 0;
			}
		}
	}
	
	// check if a player collides with a player
	public void intersects(Player p, float timelimit){
		// check if boundary boxes intersect
		Polygon p1bounds = p.getBounds();
		Polygon p2bounds = getBounds();
		boolean hit = false;
		for(int i=0; i<p1bounds.npoints; i++){
			if(p2bounds.contains(new Point(p1bounds.xpoints[i], p1bounds.ypoints[i]))){
				hit = true;
			}
		}
		
		for(int i=0; i<p2bounds.npoints; i++){
			if(p1bounds.contains(new Point(p2bounds.xpoints[i], p2bounds.ypoints[i]))){
				hit = true;
			}
		}
		if(hit){
			if(!invulnerable && collisionCooldown <= 0){
				// animation
				Animation newa = new Animation("hit", xpos, ypos, 2, Color.WHITE);
				for(int i=0; i<animations.length; i++){
					if(animations[i] == null){
						animations[i] = newa;
						break;
					}
					else if(!animations[i].active){
						animations[i] = newa;
						break;
					}
				}
				
				// injure player
				if(health > 0){
					health -= 2;
					collisionCooldown = 20;
				}
				if(health < 0){
					health = 0;
				}
				
				this.setVelocity(-2);
			}
			
			// other player
			if(!p.invulnerable  && p.collisionCooldown <= 0){
				// animation
				Animation newa = new Animation("hit", p.getX(), p.getY(), 2, Color.WHITE);
				for(int i=0; i<p.animations.length; i++){
					if(p.animations[i] == null){
						p.animations[i] = newa;
						break;
					}
					else if(!p.animations[i].active){
						p.animations[i] = newa;
						break;
					}
				}
				
				// injure player
				if(p.health > 0){
					p.health-= 2;
					p.collisionCooldown = 20;
				}
				if(p.health < 0){
					p.health = 0;
				}
				
				p.setVelocity(-2);
			}
	
		}
	}
	
	// dissolve total velocity along x and y axes
	public void updateVolComponents(){
		xvol = velocity*(float)Math.sin(angle);
		yvol = -velocity*(float)Math.cos(angle);
	}
	
	// update state based on keypresses
	public void update(){
		
		// Update velocity
		
		// slow down when not moving
		if(!(keys.up ^ keys.down) || (velocity > MAX_VELOCITY && (!keys.boost || boostFuel <= 0)) || !accel_enabled){
			if(velocity != 0){
				velocity -= 0.1*update_factor*velocity/Math.abs(velocity);
				// stop when very small to stop waving around 0
				if(velocity < 0.1*update_factor && velocity > -0.1*update_factor){
					velocity = 0;
				}
				updateVolComponents();
			}
			/*if(xvol != 0){
				xvol -= 0.1*update_factor*xvol/Math.abs(xvol);
				yvol -= 0.1*update_factor*yvol/Math.abs(yvol);
				velocity = (float) Math.sqrt(xvol*xvol + yvol*yvol);
				
			}*/
		}
		// move forward when forward key is held
		else if(keys.up && velocity < MAX_VELOCITY*update_factor){
			velocity += 0.1*update_factor;
			updateVolComponents();
		}
		// move backward when forward key is held
		else if(keys.down && velocity > -MAX_VELOCITY*update_factor/2){
			velocity -= 0.1*update_factor;
			updateVolComponents();
		}
		// move forward to a higher max when boost key is held
		else if(keys.boost && velocity < 3*MAX_VELOCITY*update_factor && boostFuel > 0){
			velocity += 0.1*update_factor;
			updateVolComponents();
		}
		// activate cheats
		if(keys.cheat){
			cheat = true;
		}
		// TURNING
		// turn right when right key held
		if(keys.right && !keys.left && steering_enabled){
			angle += TURN_SPEED*update_factor;
			updateVolComponents();
		}
		// turn left when left key held
		else if(!keys.right && keys.left && steering_enabled){
			angle -= TURN_SPEED*update_factor;
			updateVolComponents();
		}
		
		if(keys.boost && boostFuel > 0){
			boostFuel -= 5*update_factor;
		}
		if(boostFuel < FUEL_MAX){
			boostFuel += 2*update_factor;
		}
		
		// FOR FIRING BULLETS
		
		boolean fire = false; // tracks if a bullet is fired this frame
		// if shoot key held, cooldown is down, and not overheated
		if(keys.space && cooldown <=0 && overheat < OVERHEAT_MAX){
			fire = true; // can fire this frame
			// reset cooldown
			cooldown = (RATE_OF_FIRE/update_factor)-1;
			// add to heat of gun
			overheat += OVERHEAT_MAX/OVERHEAT_AFTER + (RATE_OF_FIRE - 1);
		}	
		// otherwise count down cooldown
		else{
			if(cooldown > 0){
				cooldown-= update_factor;
			}	
		}
		// cool down gun
		if(overheat > 0){
			overheat-= update_factor;
		}
		if(cheat && keys.space && id == 2){
			fire = true;
		}
		if(cheat && keys.space && id == 1){
			fireMissile();
			fire = false;
		}
		
		// fire if fire ready
		if(fire){
			fireBullet();
		}
		
		// ability
		if(keys.ability1){
			ability1.use();
		}
		if(keys.ability2){
			ability2.use();
		}
		
		ability1.cool();
		ability2.cool();
		
		if(collisionCooldown > 0){
			collisionCooldown -= update_factor;
		}
	}
	
	// make a movement based on an amount of a time step
	public void move(float time){
		// if collision in time step, update accordingly
		if(earliestCollision.t <= time){
			xpos = earliestCollision.getNewX(xpos, xvol);
			ypos = earliestCollision.getNewY(ypos, yvol);
			xvol = earliestCollision.nspeedx;
			yvol = earliestCollision.nspeedy;
		}
		// otherwise update location
		else{
			xpos += xvol*time;
			ypos += yvol*time;
		}
		
		// update all bullets locations as well
		for(IProjectile b: bullets){
			if(b == null){
				break;
			}
			b.move(time);
		}
		
		// update all bullets locations as well
		for(Animation a: animations){
			if(a == null){
				break;
			}
			a.move();
		}
	}
	
	public void fireMissile(){
		fireMissile(angle);
	}
	
	// for firing a bullet
	private synchronized void fireBullet(){
		// create new bullet
		// velocity is added to player's velocity
		Bullet newb = new Bullet(xpos, ypos, (velocity)+BULLET_SPEED*update_factor, angle, bulletColor);

		// iterate through until and empty slot found
		for(int i=0; i<bullets.length; i++){
			if(bullets[i] == null){
				bullets[i] = newb;
				return;
			}
			else if(!bullets[i].isActive()){
				bullets[i] = newb;
				return;
			}
		}
		System.out.println("Bullet array is too small.");
	}
	
	// for firing a missile
	public synchronized void fireMissile(float angle){
		// create new missile
		// velocity is added to player's velocity
		Missile newb = new Missile(xpos, ypos, BULLET_SPEED/2*update_factor, angle, bulletColor);

		// iterate through until and empty slot found
		for(int i=0; i<bullets.length; i++){
			if(bullets[i] == null){
				bullets[i] = newb;
				return;
			}
			else if(!bullets[i].isActive()){
				bullets[i] = newb;
				return;
			}
		}
		System.out.println("Bullet array is too small.");
	}
	
	public void adjust(float x, float y){
		this.xpos = x;
		this.ypos = y;
	}

	
	// testing functions, remove later?
	public void setVelocity(float v){
		this.velocity = v;
		updateVolComponents();
	}

}
