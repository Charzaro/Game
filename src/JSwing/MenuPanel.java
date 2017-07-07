package JSwing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
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

import game.GameArea;

public class MenuPanel extends JPanel {
	
	private int width, height;
	
	private MainFrame parent;
	
	private BufferedImage image;
	
	private InputMap inMap;
	private ActionMap acMap;

	public MenuPanel(MainFrame parent, int width, int height) {
		this.width = width;
		this.height =height;
		
		this.parent = parent;
		
		inMap = this.getInputMap(JPanel.WHEN_FOCUSED);
		acMap = this.getActionMap();
		setKeyBindings();
		
		addMouseListener(new ClickListener());
		
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(Color.GRAY);
		graphics.fillRect(0, 0, width, height);
	}
	
	public void paintGraphics(Graphics g){
		g.drawImage(image, 0, 0, width, height, parent);
	}
	
	
	// makes mouse clicks pause/play game
	private class ClickListener extends MouseAdapter{
		@Override
		public void mouseReleased(MouseEvent e){
		}
	}
	
	private void setKeyBindings(){
		Action escAction = new AbstractAction(){
			public void actionPerformed(ActionEvent e){
				parent.openGame();
			}
		};
		
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, true), "escAction");
		
		acMap.put("escAction", escAction);

	}


}
