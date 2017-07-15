package JSwing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class CustomButton extends JComponent implements MouseListener{
	
	private static final long serialVersionUID = 1L;
	public static final int FONT_SIZE = 80;
	private static final int BUTTON_WIDTH = 1000;
	private static final int BUTTON_HEIGHT = 100;
	
	protected String label;
	private int width;
	private int height;
	
	protected Font font;

	protected boolean mouseOver;
	private volatile int animationCount;
	public boolean selected = false;

	public CustomButton(String label, int width, int height){
		animationCount = 0;
		
		mouseOver = false;
		font = new Font("SansSerif", Font.BOLD, FONT_SIZE);
		this.label = label;
	
		this.width = width;
		this.height = height;
		
		setSize(width, height);

		addMouseListener(this);
		
		
	}
	
	public CustomButton(String label, int width, int height, int fontSize){
		animationCount = 0;
		
		mouseOver = false;
		font = new Font("SansSerif", Font.BOLD, fontSize);
		this.label = label;
	
		this.width = width;
		this.height = height;
		
		setSize(width, height);

		addMouseListener(this);
		
		
	}
	
	public CustomButton(String label){
		animationCount = 0;
		
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
		
		if(mouseOver){
			g2.setColor(Color.DARK_GRAY);
		}
		else{
			g2.setColor(Color.BLACK);
		}
		
		g2.fillRect(0, 0, width, height);
		g2.setPaint(Color.WHITE);
		if(mouseOver){
			g2.setStroke(new BasicStroke(60));
			g2.drawLine(animationCount-100, height, animationCount, 0);
		}
		
		g2.setFont(font);
		FontMetrics fm = g2.getFontMetrics();
		g2.drawString(label, width/2 - fm.stringWidth(label)/2, (height - fm.getHeight())/2 + fm.getAscent());
	}
	
	public void changeFontSize(int size){
		font = new Font(font.getFamily(), font.getStyle(), size);
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseOver = true;
		Thread t = new Thread(){
			public void run(){
				animationCount = 1;
				while(animationCount > 0){
					if(animationCount-100 > width || !mouseOver){
						animationCount = width*2;
						return;
					}
					try{
						sleep(1000/30L);
					}
					catch(InterruptedException e){
						e.printStackTrace();
					}
					animationCount+= 100;
					repaint();
				}
			}
			
		};
		t.start();
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseOver = false;
		animationCount = 0;
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