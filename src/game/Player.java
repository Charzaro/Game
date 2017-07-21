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
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import abilities.Ability;
import abilities.BurstAttack;
import abilities.DashAttack;
import abilities.HomingRockets;
import abilities.Push;
import abilities.RailGun;
import abilities.Reflect;
import animation.Animation;
import animation.ExplosionAnimation;
import animation.Hitmark;
import animation.HitscanAnimation;

public class Player {

	// Constants

	// image file name for player
	private static final String p1ImageFilename = "ShipDesign1.jpg";


	public static final int MAX_VELOCITY = 3; // max speed
	public static final float MAX_TURN_SPEED = (float)Math.PI/96; // max turn speed
	private static final int RATE_OF_FIRE = 4; // rate of fire
	private static final int OVERHEAT_MAX = 100; // overheat bar
	private static final int OVERHEAT_AFTER = 5; // how many shots can be fired before overheat
	public static final int MAX_HEALTH = 10; // player health
	public static final int BOOST_MAX = 30; // boost bar
	private static final int FUEL_MAX = 500; // fuel bar
	private static final int KNOCKBACK = 6; // knockback on collision

	private static final int BULLET_SPEED = 5; // basic bullet speed

	private static final boolean SHOW_BOXES = false; // debug: show hitboxes

	public int id; // number player
	// for creating placeholder triangles
	private int testw = 40;
	private int testh = 50;

	// Jpanel containing player
	private GameArea parent;

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
	private float steerSpeed;
	private float xvol;
	private float yvol;

	private float radius;

	private float knockbackX;
	private float knockbackY;

	// keypress handler
	private KeyPressHandler keys;
	public KeyStatus Keys;

	// counts for firing shots
	private int cooldown;
	private int overheat;
	private int overheat_wait;

	public int boostFuel; // how much boost fuel is left
	private int collisionCooldown; // gives collision immunity briefly after a collision, this time that immunitity

	// abilities chosen for the player
	private Ability ability1;
	private Ability ability2;

	// an array of animations for the player
	private Animation[] animations;

	// health count
	private int health;

	// statuses
	private volatile boolean steering_enabled;
	private volatile boolean invulnerable;
	private volatile boolean accel_enabled;
	private boolean reflecting;
	private boolean pushing;

	// all purpose counter
	protected int duration;

	// used for storing collisions
	private Collision earliestCollision;
	private Collision tempCollision;

	// Image for player
	private BufferedImage image;
	private BufferedImage invulnImage;

	// bullets shot by this player
	private volatile Projectile[] bullets;

	// for developer cheats
	private boolean cheat = false;


	// Constructor
	public Player(int startx, int starty, float angle, int id, GameArea parent) {

		this.parent = parent;
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
		this.angle = angle;
		velocity = 0;
		steerSpeed = MAX_TURN_SPEED;
		xvol = 0;
		yvol = 0;

		knockbackX = 0;
		knockbackY = 0;

		cooldown = 0;
		overheat = 0;
		overheat_wait = 0;
		collisionCooldown = 0;
		boostFuel = FUEL_MAX;

		duration = 0;

		ability1 = new Push(this, (short)1);
		ability2 = new RailGun(this, (short)2);

		animations = new Animation[200];

		health = MAX_HEALTH;

		invulnerable = false;
		reflecting = false;
		pushing = false;
		steering_enabled = true;
		accel_enabled = true;

		bullets = new Projectile[300];

		earliestCollision = new Collision();
		tempCollision = new Collision();

		// Load image (or draw placeholder triangle currently
		image = new BufferedImage(testw, testh, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(color);
		int[] xs = {0, testw/2, testw};
		int[] ys = {testh, 0, testh};
		graphics.fillPolygon(xs, ys, 3);

		// create an image for the dash attack
		invulnImage = new BufferedImage(testw, testh+20, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graphics2 = invulnImage.createGraphics();

		int[] xs2 = {0, testw/2 , testw};
		int[] ys2 = {testh+20, 0+20, testh+20};
		graphics2.setPaint(Color.GRAY);
		graphics2.fillPolygon(xs, ys, 3);
		graphics2.setPaint(color);
		graphics2.fillPolygon(xs2, ys2, 3);

		radius = testh/2;
		/* FOR WHEN AN IMAGE IS DRAWN
		try{
			image = ImageIO.read(new File(p1ImageFilename));
			//radius = image.getWidth()/2;
		}
		catch(IOException e){
			System.out.println("Could not open ship image: " + e);
		}
		 */
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

	public Projectile[] getBullets(){
		return bullets;
	}

	public String[] getAbilityNames(){
		String[] result = {ability1.getName(), ability2.getName()};
		return result;
	}

	public int[] getCooldowns(){
		return new int[] {ability1.getCooldown(), ability2.getCooldown()};
	}

	public HashMap<String, Integer> getKeys(){
		return keys.getKeys();
	}

	public KeyPressHandler getKeyPresses(){
		return keys;
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
	public void setReflect(boolean b){
		reflecting = b;
	}
	public void setPush(boolean b){
		pushing = b;
	}
	public synchronized void setSteering(boolean b){
		steering_enabled = b;
	}
	public synchronized void setSteerSpeed(float as){
		steerSpeed = as;
	}
	public synchronized void setAccel(boolean b){
		accel_enabled = b;
	}

	public void setAbilities(short abil1, short abil2){
		switch(abil1){
		case 0:
			ability1 = new BurstAttack(this, (short)1);
			break;
		case 1:
			ability1 = new HomingRockets(this, (short)1);
			break;
		case 2:
			ability1 = new RailGun(this, (short)1);
			break;
		case 3:
			ability1 = new DashAttack(this, (short)1);
			break;
		case 4:
			ability1 = new Reflect(this, (short)1);
			break;
		}

		switch(abil2){
		case 0:
			ability2 = new BurstAttack(this, (short)2);
			break;
		case 1:
			ability2 = new HomingRockets(this, (short)2);
			break;
		case 2:
			ability2 = new RailGun(this, (short)2);
			break;
		case 3:
			ability2 = new DashAttack(this, (short)2);
			break;
		case 4:
			ability2 = new Reflect(this, (short)2);
			break;
		}


	}

	// draw player
	public void draw(Graphics2D g2){

		// draw animations
		for(Animation a: animations){
			if(a != null){
				a.draw(g2);
			}
		}

		// draw abilities
		ability1.draw(g2);
		ability2.draw(g2);

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
		for(Projectile b: bullets){
			if(b != null){
				b.draw(g2);
			}	
		}
	}

	// resets values to initial
	public void reset(int startx, int starty, float angle){
		this.health = MAX_HEALTH;

		xpos = startx;
		ypos = starty;
		this.angle = angle;
		velocity = 0;
		steerSpeed = MAX_TURN_SPEED;
		xvol = 0;
		yvol = 0;

		knockbackX = 0;
		knockbackY = 0;

		cheat = false;

		cooldown = 0;
		overheat = 0;
		overheat_wait = 0;
		collisionCooldown = 0;
		boostFuel = FUEL_MAX;

		duration = 0;

		invulnerable = false;
		reflecting = false;
		pushing = false;
		steering_enabled = true;
		accel_enabled = true;

		animations = new Animation[200];
		bullets = new Projectile[300];

		ability1.reset();
		ability2.reset();

		earliestCollision = new Collision();
		tempCollision = new Collision();
	}

	// currently unused, returns a rectangle around the ship
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
	}

	// reset stored collisions
	public void reset(){
		earliestCollision.reset();
		tempCollision.reset();
	}

	// Check for collision with the boundary box in this frame
	public float checkBoundaryCollisions(float time){
		Physics.checkBoxCollision(xpos, ypos, xvol+knockbackX, yvol+knockbackY, getEdges().width/2, getEdges().height/2, 0, 0, Settings.getDimx(), Settings.getDimy(), time, tempCollision);
		if(tempCollision.t < earliestCollision.t){
			earliestCollision.copy(tempCollision);
			return tempCollision.t;
		}
		return time;
	}

	public float checkObstacleCollision(float time, Obstacle o){
		float earliestTime = time;
		float newx;
		float newy;
		boolean insideY;
		boolean insideX;
		
		if(o.health <=0 ){
			return time;
		}
		
		Physics.checkVerticalLine(xpos, xvol+knockbackX, yvol+knockbackY, radius, o.xpoints[0], time, tempCollision);
		newy = (yvol+knockbackY)*tempCollision.t + ypos;
		insideY =  newy < o.ypoints[1] + radius && newy > o.ypoints[0] - radius;
		if(tempCollision.t < earliestCollision.t && insideY){
			earliestCollision.copy(tempCollision);
			earliestTime = tempCollision.t;
		}
		else{
			tempCollision.reset();
		}
		Physics.checkVerticalLine(xpos, xvol+knockbackX, yvol+knockbackY, radius, o.xpoints[1], time, tempCollision);
		newy = (yvol+knockbackY)*tempCollision.t + ypos;
		insideY =  newy < o.ypoints[1] + radius && newy > o.ypoints[0] - radius;
		if(tempCollision.t < earliestCollision.t && insideY){
			earliestCollision.copy(tempCollision);
			earliestTime = tempCollision.t;
		}
		else{
			tempCollision.reset();
		}

		Physics.checkHorizontalLine(ypos, xvol+knockbackX, yvol+knockbackY, radius, o.ypoints[0], time, tempCollision);
		newx = (xvol+knockbackX)*tempCollision.t + xpos;
		insideX =  newx < o.xpoints[1] + radius && newx > o.xpoints[0] - radius;
		if(tempCollision.t < earliestCollision.t && insideX){
			earliestCollision.copy(tempCollision);
			earliestTime = tempCollision.t;
		}
		else{
			tempCollision.reset();
		}
		Physics.checkHorizontalLine(ypos, xvol+knockbackX, yvol+knockbackY, radius, o.ypoints[1], time, tempCollision);
		newx = (xvol+knockbackX)*tempCollision.t + xpos;
		insideX =  newx < o.xpoints[1] + radius && newx > o.xpoints[0] - radius;
		if(tempCollision.t < earliestCollision.t && insideX){
			earliestCollision.copy(tempCollision);
			earliestTime = tempCollision.t;
		}
		else{
			tempCollision.reset();
		}


		return earliestTime;
	}

	// check if player collides with a bullet
	public void intersects(Projectile b, float timelimit){
		// if bullet isnt active, skip
		if(!b.isActive()){
			return;
		}
		// reflect bullet if currently reflecting and bullet is near player
		if(reflecting){
			float distance = Physics.getDistance(xpos, ypos, b.xpos, b.ypos);
			if(distance < 60){
				addProjectile(b.reflectedCopy(bulletColor));
				b.deactivate();
				return;
			}
		}
		// check if the bullet has hit the player
		if(b.checkHit(this)){
			// reflect if hit (mainly for hitscans that wont be "near" the player but still hit)
			if(reflecting){
				addProjectile(b.reflectedCopy(bulletColor));
				b.deactivate();
				return;
			}
			// disable bullet
			b.deactivate();
			// end here if player is invulnerable
			if(invulnerable){
				return;
			}

			// hit animation
			addAnimation(new Hitmark(xpos, ypos, Color.WHITE));

			// injure player
			if(health > 0){
				health-= b.getDamage();
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

		// check if any point of p1's hitbox is inside of p2's hitbox
		for(int i=0; i<p1bounds.npoints; i++){
			if(p2bounds.contains(new Point(p1bounds.xpoints[i], p1bounds.ypoints[i]))){
				hit = true;
			}
		}
		// check if any point of p2's hitbox is inside of p1's hitbox
		for(int i=0; i<p2bounds.npoints; i++){
			if(p1bounds.contains(new Point(p2bounds.xpoints[i], p2bounds.ypoints[i]))){
				hit = true;
			}
		}

		// if the two players have collided
		if(hit){
			float angle = Physics.findAngle(xpos, ypos, p.getX(), p.getY());

			// P1
			// if not invincible and have not just been dealth damage from a collision
			if(!invulnerable && collisionCooldown <= 0){
				// animation
				addAnimation(new Hitmark(xpos, ypos, Color.WHITE));

				// injure player
				if(health > 0){
					health -= 2;
					collisionCooldown = 20;
				}
				if(health < 0){
					health = 0;
				}

				// bounce player back
				knockback(KNOCKBACK, angle);
				setVelocity(0);
				//addVelocity(-KNOCKBACK*(float)Math.sin(angle), KNOCKBACK*(float)Math.cos(angle));
				//this.setVelocity(-KNOCKBACK);
			}

			// other player (same as p1)
			if(!p.invulnerable  && p.collisionCooldown <= 0){
				// animation
				addAnimation(new Hitmark(p.getX(), p.getY(), Color.WHITE));

				// injure player
				if(p.health > 0){
					p.health-= 2;
					p.collisionCooldown = 20;
				}
				if(p.health < 0){
					p.health = 0;
				}

				p.knockback(-KNOCKBACK, angle);
				p.setVelocity(0);
				//p.addVelocity(KNOCKBACK*(float)Math.sin(angle), -KNOCKBACK*(float)Math.cos(angle));
				//p.setVelocity(-KNOCKBACK);
			}

		}
		float dist;
		if(pushing && (dist = Physics.getDistance(xpos, ypos, p.xpos, p.ypos)) < Push.PUSH_RANGE){
			p.knockback(200/dist, Physics.findAngle(p.xpos, p.ypos, xpos, ypos));
		}
		if(p.pushing && (dist = Physics.getDistance(xpos, ypos, p.xpos, p.ypos)) < Push.PUSH_RANGE){
			knockback(200/dist, Physics.findAngle(xpos, ypos, p.xpos, p.ypos));
		}
	}

	public void knockback(float knockback, float angle){
		knockbackX += -knockback*(float)Math.sin(angle);
		knockbackY += knockback*(float)Math.cos(angle);
	}

	// dissolve total velocity along x and y axes
	public void updateVolComponents(){
		xvol = Physics.xComponent(velocity, angle);
		yvol = Physics.yComponent(velocity, angle);
	}

	// update state based on keypresses
	public void update(){

		// Update velocity

		if(knockbackX != 0){
			knockbackX -= 0.1*Settings.update_factor*knockbackX/Math.abs(knockbackX);
			if(knockbackX < 0.1*Settings.update_factor && knockbackX > -0.1*Settings.update_factor){
				knockbackX = 0;
			}
		}
		if(knockbackY != 0){
			knockbackY -= 0.1*Settings.update_factor*knockbackY/Math.abs(knockbackY);
			if(knockbackY < 0.1*Settings.update_factor && knockbackY > -0.1*Settings.update_factor){
				knockbackY = 0;
			}
		}

		// slow down when not moving
		if((!keys.up && velocity > 0) || (velocity > MAX_VELOCITY && (!keys.boost || boostFuel <= 0)) || velocity > 3*MAX_VELOCITY){
			velocity -= 0.1*Settings.update_factor;
			// stop when very small to stop waving around 0
			if(velocity < 0.1*Settings.update_factor){
				velocity = 0;
			}
			updateVolComponents();
		}
		if(!keys.down && velocity < 0){
			velocity += 0.1*Settings.update_factor;
			// stop when very small to stop waving around 0
			if(velocity > -0.1*Settings.update_factor){
				velocity = 0;
			}
			updateVolComponents();
		}
		// move forward when forward key is held
		if(keys.up){
			// move forward to a higher max when boost key is held
			if(keys.boost && velocity < 3*MAX_VELOCITY*Settings.update_factor && boostFuel > 0){
				velocity += 0.1*Settings.update_factor;
				updateVolComponents();
			}
			else if(velocity < MAX_VELOCITY*Settings.update_factor){
				velocity += 0.1*Settings.update_factor;
				updateVolComponents();
			}
			
		}
		// move backward when forward key is held
		if(keys.down && velocity > -MAX_VELOCITY*Settings.update_factor/1.5){
			velocity -= 0.1*Settings.update_factor;
			updateVolComponents();
		}
		// activate cheats
		if(keys.c3){
			cheat = true;
		}
		// TURNING
		// turn right when right key held
		if(keys.right && !keys.left && steering_enabled){
			angle += steerSpeed*Settings.update_factor;
			updateVolComponents();
		}
		// turn left when left key held
		else if(!keys.right && keys.left && steering_enabled){
			angle -= steerSpeed*Settings.update_factor;
			updateVolComponents();
		}

		// use up boost fuel
		if(keys.boost && keys.up && boostFuel > 0){
			boostFuel -= 3*Settings.update_factor;
		}
		// recharge boost fuel
		if(boostFuel < FUEL_MAX && !keys.boost){
			boostFuel += 2*Settings.update_factor;
		}

		// FOR FIRING BULLETS

		boolean fire = false; // tracks if a bullet is fired this frame
		// if shoot key held, cooldown is down, and not overheated
		if(keys.shoot && cooldown <=0 && overheat < OVERHEAT_MAX){
			fire = true; // can fire this frame
			// reset cooldown
			cooldown = (RATE_OF_FIRE/Settings.update_factor)-1;
			// add to heat of gun
			overheat += OVERHEAT_MAX/OVERHEAT_AFTER + (RATE_OF_FIRE - 1);
		}	
		// otherwise count down cooldown
		else{
			if(cooldown > 0){
				cooldown-= Settings.update_factor;
			}	
		}
		// cool down gun
		if(overheat > 0){
			overheat-= Settings.update_factor;
		}
		if(cheat && keys.shoot && id == 2){
			fire = true;
		}
		if(cheat && keys.shoot && id == 1){
			fireMissile(angle);
			fire = false;
		}

		// fire if fire ready
		if(fire){
			fireBullet();
		}

		// use ability
		if(keys.ability1){
			ability1.use();
		}
		if(keys.ability2){
			ability2.use();
		}

		if(keys.cheat){
			System.out.println("Hi");
			angle = 5*(float)Math.PI/4;
		}

		// cooldown abilities
		ability1.cool();
		ability2.cool();

		// cooldown timer for collisions
		if(collisionCooldown > 0){
			collisionCooldown -= Settings.update_factor;
		}
	}

	// make a movement based on an amount of a time step
	public void move(float time){
		// if collision in time step, update accordingly
		if(earliestCollision.t <= time){
			xpos = earliestCollision.getNewX(xpos, xvol+knockbackX);
			ypos = earliestCollision.getNewY(ypos, yvol+knockbackY);
			if(earliestCollision.xcollide){
				xvol = 0;
				knockbackX = 0;
			}
			if(earliestCollision.ycollide){
				yvol = 0;
				knockbackY = 0;
			}

		}
		// otherwise update location
		else{
			xpos += (xvol + knockbackX)*time;
			ypos += (yvol + knockbackY)*time;
		}

		// update all bullets locations as well
		for(Projectile b: bullets){
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

	public void fireHitScan(){
		// create new bullet
		// velocity is added to player's velocity
		Hitscan newb = new Hitscan(xpos, ypos, 120, angle, bulletColor);

		addAnimation(new HitscanAnimation(xpos, ypos, bulletColor, angle));

		// iterate through until and empty slot found
		addProjectile(newb);
	}

	// fires missile at player's angle and at default speed
	public void fireMissile(){
		fireMissile(angle);
	}

	// for firing a missile
	public void fireMissile(float angle){
		// create new missile
		// velocity is added to player's velocity
		Missile newb = new Missile(xpos, ypos, angle, bulletColor, this);

		// add to projectile array
		addProjectile(newb);

	}

	// fires a bullet at custom velocity and angle
	public void fireBullet(float velocity, float angle){
		Bullet newb = new Bullet(xpos, ypos, velocity*Settings.update_factor, angle, bulletColor);

		addProjectile(newb);
	}

	// fires a bullet at custom angle
	public void fireBullet(float angle){
		Bullet newb = new Bullet(xpos, ypos, (this.velocity)+BULLET_SPEED*Settings.update_factor, angle, bulletColor);

		addProjectile(newb);
	}

	// for firing a bullet at player's angle and default velocity
	public void fireBullet(){
		Bullet newb = new Bullet(xpos, ypos, (this.velocity)+BULLET_SPEED*Settings.update_factor, angle, bulletColor);

		addProjectile(newb);

	}

	// for firing a bullet at player's angle and default velocity
	public void fireExplosion(int x, int y){
		Explosion newb = new Explosion(x, y, 60, 0.5f, bulletColor);
		addAnimation(new ExplosionAnimation(x, y, 60,0.5f, bulletColor));
		addProjectile(newb);
	}

	// adds an animation to the animation array
	void addAnimation(Animation newa){
		// iterates through until a deactivated animation or empty slot is found
		for(int i=0; i<animations.length; i++){
			if(animations[i] == null){
				animations[i] = newa;
				return;
			}
			else if(!animations[i].active){
				animations[i] = newa;
				return;
			}
		}
		System.err.println("Not enough room in animations array.");
	}

	// adds a projectile to the projectile array
	private void addProjectile(Projectile newb){
		// iterates through until a deactivated projectile or empty slot is found
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
		System.err.println("Projectile array is too small.");
	}

	// changes position
	public void adjust(float x, float y){
		this.xpos = x;
		this.ypos = y;
	}


	// testing functions, remove later?
	public void setVelocity(float v){
		this.velocity = v;
		updateVolComponents();
	}

	// testing functions, remove later?
	public void addVelocity(float xvol, float yvol){
		this.xvol += xvol;
		this.yvol += yvol;

		this.velocity = (float)Math.sqrt(xvol*xvol + yvol*yvol);
		this.angle = Physics.findAngle(0, 0, xvol, yvol);
	}

}
