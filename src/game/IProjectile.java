package game;

import java.awt.Graphics2D;
import java.awt.Rectangle;

public interface IProjectile {
	public void move(float time);
	public float checkBoundaryCollisions(float time);
	public Rectangle getBounds();
	public void draw(Graphics2D g2);
	public boolean isActive();
	public void reset();
	public void deactivate();
}
