package JSwing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;

public class AbilityButton extends JComponent implements MouseListener{
	
	public static final int BUTTON_WIDTH = 100;
	public static final int BUTTON_HEIGHT = 100;
	public static final int GAP = 20;
	
	private String label;
	private Font font;
	
	private boolean mouseOver;
	public boolean selected;

	public AbilityButton(String label) {
		this.label = label;
		mouseOver = false;
		selected = false;
		font = new Font("SansSerif", Font.PLAIN, 16);
		
		addMouseListener(this);
	}
	
	@Override
	protected void paintComponent(Graphics g){
		if(selected){
			g.setColor(Color.GRAY);
		}
		else if(mouseOver){
			g.setColor(Color.DARK_GRAY);
		}
		else{
			g.setColor(Color.BLACK);
		}
		g.fillRect(GAP, GAP, getWidth()-2*GAP, getHeight()-2*GAP);
		g.setColor(Color.WHITE);
		g.setFont(font);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(label, getWidth()/2 - fm.stringWidth(label)/2, (getHeight() - fm.getHeight())/2 + fm.getAscent());
		
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
		return new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);
	}
	
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
}
