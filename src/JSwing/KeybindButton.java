package JSwing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class KeybindButton extends JComponent implements MouseListener {
	
	private static final long serialVersionUID = 1L;
	public static final int FONT_SIZE = 20;
	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HEIGHT = 100;
	
	private MenuPanel parent;
	
	protected String label;
	private int width;
	private int height;
	
	protected Font font;

	protected boolean mouseOver;
	private volatile int animationCount;
	public boolean selected = false;

	public KeybindButton(String label, int width, int height, MenuPanel parent){
		this.parent = parent;
		
		mouseOver = false;
		font = new Font("SansSerif", Font.BOLD, FONT_SIZE);
		this.label = label;
	
		this.width = width;
		this.height = height;
		
		setSize(width, height);

		addMouseListener(this);
		
		
	}
	
	public KeybindButton(String label, MenuPanel parent){
		this.parent = parent;
		
		mouseOver = false;
		font = new Font("SansSerif", Font.BOLD, FONT_SIZE);
		this.label = label;
		
		width = BUTTON_WIDTH;
		height = BUTTON_HEIGHT;
		
		setSize(width, height);

		addMouseListener(this);
		
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		Graphics2D g2 = (Graphics2D) g;
		
		if(selected){
			g2.setColor(Color.GRAY);
		}
		if(mouseOver){
			g2.setColor(Color.DARK_GRAY);
		}
		else{
			g2.setColor(Color.BLACK);
		}
		
		g2.fillRect(0, 0, width, height);
		
		g2.setPaint(Color.WHITE);
		g2.setFont(font);
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(label, width/2 - fm.stringWidth(label)/2, (height - fm.getHeight())/2 + fm.getAscent());
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(!selected){
			label = "Click here";
			selected = true; 
		}
		else{
			selected = false; 
			parent.showKeyBinds();
		}

	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseOver = true;
		repaint();

	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseOver = false;
		repaint();
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(width, height);
	}
	
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}

}
