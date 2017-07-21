package JSwing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import game.GameArea;

public class MenuPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static final int BUTTON_GAP = 40;
	private static final int NUM_ABILITIES = 6;
	
	private static final String P1COLOR = "#e8a961";
	private static final String P2COLOR = "#ad84f9";
	
	private static final short BURST = 0;
	private static final short HOME = 1;
	private static final short SNIPE = 2;
	private static final short DASH = 3;
	private static final short REFLECT = 4;
	private static final short PUSH = 5;

	private int width, height;
	
	private MainFrame parent;
	
	private BufferedImage image;
	
	private InputMap inMap;
	private ActionMap acMap;
	
	private JPanel currentPanel;
	private JPanel mainMenu;
	private JPanel customizeMenu;
	private JPanel controlsMenu;
	
	private AbilityButton[] p1Ability1Array;
	private AbilityButton[] p1Ability2Array;
	private AbilityButton[] p2Ability1Array;
	private AbilityButton[] p2Ability2Array;
	
	public short p1Abil1Chosen;
	public short p1Abil2Chosen;
	public short p2Abil1Chosen;
	public short p2Abil2Chosen;
	
	private HashMap<String, Integer> keys1;
	private HashMap<String, Integer> keys2;

	public MenuPanel(MainFrame parent, int width, int height) {
		this.width = width;
		this.height = height;
		
		this.parent = parent;
		
		inMap = this.getInputMap(JPanel.WHEN_FOCUSED);
		acMap = this.getActionMap();
		setKeyBindings();
		
		addMouseListener(new ClickListener());
		
		// set background
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = image.createGraphics();
		graphics.setPaint(Color.GRAY);
		graphics.fillRect(0, 0, width, height);
		
		buildPanels();
		
		loadKeys();
		
		p1Abil1Chosen = 0;
		p1Abil2Chosen = 1;
		p2Abil1Chosen = 0;
		p2Abil2Chosen = 1;
		
		p1Ability1Array[p1Abil1Chosen].selected = true;
		p1Ability2Array[p1Abil2Chosen].selected = true;
		p2Ability1Array[p2Abil1Chosen].selected = true;
		p2Ability2Array[p1Abil2Chosen].selected = true;
	

		// set panel size
		setPreferredSize(new Dimension(width, height));
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, 0, 0, width, height, parent);
		super.paintChildren(g);
	}
	
	public void swapTo(JPanel newpanel){
		remove(currentPanel);
		currentPanel = newpanel;
		add(currentPanel);
		currentPanel.revalidate();
		repaint();
	}
	
	private void loadKeys(){
		keys1 = new HashMap<String, Integer>();
		keys1.putAll(parent.getP1Keybinds());
		keys2 = new HashMap<String, Integer>();
		keys2.putAll(parent.getP2Keybinds());
		
		showKeyBinds();
	}
	
	private void sendKeys(){
		parent.setKeys((short)1, keys1);
		parent.setKeys((short)2, keys2);
	}
	
	void showKeyBinds(){
		accelButton.label = KeyEvent.getKeyText(keys1.get("up"));
		reverseButton.label = KeyEvent.getKeyText(keys1.get("down"));
		rightButton.label = KeyEvent.getKeyText(keys1.get("right"));
		leftButton.label = KeyEvent.getKeyText(keys1.get("left"));
		shootButton.label = KeyEvent.getKeyText(keys1.get("shoot"));
		boostButton.label = KeyEvent.getKeyText(keys1.get("boost"));
		ability1Button.label = KeyEvent.getKeyText(keys1.get("ability1"));
		ability2Button.label = KeyEvent.getKeyText(keys1.get("ability2"));
		
		accelButton2.label = KeyEvent.getKeyText(keys2.get("up"));
		reverseButton2.label = KeyEvent.getKeyText(keys2.get("down"));
		rightButton2.label = KeyEvent.getKeyText(keys2.get("right"));
		leftButton2.label = KeyEvent.getKeyText(keys2.get("left"));
		shootButton2.label = KeyEvent.getKeyText(keys2.get("shoot"));
		boostButton2.label = KeyEvent.getKeyText(keys2.get("boost"));
		ability1Button2.label = KeyEvent.getKeyText(keys2.get("ability1"));
		ability2Button2.label = KeyEvent.getKeyText(keys2.get("ability2"));
		
		repaint();
	}
	
	private void abilityClick(boolean p1, boolean abil1, short buttonPressed){
		AbilityButton[] array;
		if(p1){
			if(abil1){
				array = p1Ability1Array;
			}
			else{
				array = p1Ability2Array;
			}
		}
		else{
			if(abil1){
				array = p2Ability1Array;
			}
			else{
				array = p2Ability2Array;
			}
		}
		for(AbilityButton b: array){
			b.selected = false;
			array[buttonPressed].selected = true;
		}
		repaint();
	}
	
	
	// makes mouse clicks pause/play game
	private class ClickListener extends MouseAdapter{
		@Override
		public void mouseReleased(MouseEvent e){
		}
	}
	
	private void swapKey(boolean p1, String key, int code){
		if(p1){
			if(keys1.containsKey(key)){
				keys1.remove(key);
				keys1.put(key, code);
			}
		}
		else{
			if(keys2.containsKey(key)){
				keys2.remove(key);
				keys2.put(key, code);
			}
		}
		showKeyBinds();
	}

	private class KeyListener extends KeyAdapter{
		@Override
		public void keyReleased(KeyEvent e){
			if(accelButton.selected){
				swapKey(true, "up", e.getKeyCode());
				accelButton.selected = false;
			}
			if(reverseButton.selected){
				swapKey(true, "down", e.getKeyCode());
				reverseButton.selected = false;
			}
			if(rightButton.selected){
				swapKey(true, "right", e.getKeyCode());
				rightButton.selected = false;
			}
			if(leftButton.selected){
				swapKey(true, "left", e.getKeyCode());
				leftButton.selected = false;
			}
			if(shootButton.selected){
				swapKey(true, "shoot", e.getKeyCode());
				shootButton.selected = false;
			}
			if(boostButton.selected){
				swapKey(true, "boost", e.getKeyCode());
				boostButton.selected = false;
			}
			if(ability1Button.selected){
				swapKey(true, "ability1", e.getKeyCode());
				ability1Button.selected = false;
			}
			if(ability2Button.selected){
				swapKey(true, "ability2", e.getKeyCode());
				ability2Button.selected = false;
			}
			
			if(accelButton2.selected){
				swapKey(false, "up", e.getKeyCode());
				accelButton2.selected = false;
			}
			if(reverseButton2.selected){
				swapKey(false, "down", e.getKeyCode());
				reverseButton2.selected = false;
			}
			if(rightButton2.selected){
				swapKey(false, "right", e.getKeyCode());
				rightButton2.selected = false;
			}
			if(leftButton2.selected){
				swapKey(false, "left", e.getKeyCode());
				leftButton2.selected = false;
			}
			if(shootButton2.selected){
				swapKey(false, "shoot", e.getKeyCode());
				shootButton2.selected = false;
			}
			if(boostButton2.selected){
				swapKey(false, "boost", e.getKeyCode());
				boostButton2.selected = false;
			}
			if(ability1Button2.selected){
				swapKey(false, "ability1", e.getKeyCode());
				ability1Button2.selected = false;
			}
			if(ability2Button2.selected){
				swapKey(false, "ability2", e.getKeyCode());
				ability2Button2.selected = false;
			}
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
	
	private void buildPanels(){
		
// MAIN MENU
		
		CustomButton playButton = new CustomButton("Play"){
			@Override
			public void mouseReleased(MouseEvent e){
				parent.openGame();
			}
		};
		
		CustomButton customizeButton = new CustomButton("Customize Ships"){
			@Override
			public void mouseReleased(MouseEvent e){
				swapTo(customizeMenu);
				mouseOver = false;
			}
		};
		
		CustomButton controlsButton = new CustomButton("Controls"){
			@Override
			public void mouseReleased(MouseEvent e){
				loadKeys();
				swapTo(controlsMenu);
				controlsMenu.requestFocusInWindow();
				mouseOver = false;
			}
		};
		
		CustomButton optionsButton = new CustomButton("Options"){
			@Override
			public void mouseReleased(MouseEvent e){
				swapTo(controlsMenu);
				mouseOver = false;
			}
		};
		
		int bHeights = 3 * (controlsButton.getHeight() + BUTTON_GAP) - BUTTON_GAP;
		mainMenu = new JPanel();
		mainMenu.setLayout(new BoxLayout(mainMenu, BoxLayout.PAGE_AXIS));
		mainMenu.add(Box.createRigidArea(new Dimension(0, (height - bHeights)/2)));
		mainMenu.add(playButton);
		mainMenu.add(Box.createRigidArea(new Dimension(0,40)));
		mainMenu.add(customizeButton);
		mainMenu.add(Box.createRigidArea(new Dimension(0,40)));
		mainMenu.add(controlsButton);
		mainMenu.setOpaque(false);
		//add(mainMenu);
		
		
// CUSTOMIZE MENU
		
		// Buttons
		
		// back button
		CustomButton mainMenuButton = new CustomButton("Back"){
			@Override
			public void mouseReleased(MouseEvent e){
				parent.setAbilities(p1Abil1Chosen, p1Abil2Chosen, p2Abil1Chosen, p2Abil2Chosen);
				swapTo(mainMenu);
				mouseOver = false;
			}
		};
		
		
		// P1 ability 1
		
		AbilityButton burstButton1 = new AbilityButton("Burst Attack"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil1Chosen = BURST;
				abilityClick(true, true, BURST);
			}
		};
		
		AbilityButton homingButton1 = new AbilityButton("Homing Rockets"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil1Chosen = HOME;
				abilityClick(true, true, HOME);
			}
		};
		
		AbilityButton sniperButton1 = new AbilityButton("Sniper Laser"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil1Chosen = SNIPE;
				abilityClick(true, true, SNIPE);
			}
		};
		
		AbilityButton dashButton1 = new AbilityButton("Dash Attack"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil1Chosen = DASH;
				abilityClick(true, true, DASH);
			}
		};
		
		AbilityButton reflectButton1 = new AbilityButton("Reflect"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil1Chosen = REFLECT;
				abilityClick(true, true, REFLECT);
			}
		};
		
		AbilityButton pushButton1 = new AbilityButton("Push"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil1Chosen = PUSH;
				abilityClick(true, true, PUSH);
			}
		};
		
		p1Ability1Array = new AbilityButton[NUM_ABILITIES];
		p1Ability1Array[BURST] = burstButton1;
		p1Ability1Array[HOME] = homingButton1;
		p1Ability1Array[SNIPE] = sniperButton1;
		p1Ability1Array[DASH] = dashButton1;
		p1Ability1Array[REFLECT] = reflectButton1;
		p1Ability1Array[PUSH] = pushButton1;
		
		
		// P1 ability 2
		
		AbilityButton burstButton2 = new AbilityButton("Burst Attack"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil2Chosen = BURST;
				abilityClick(true, false, BURST);
			}
		};
		
		AbilityButton homingButton2 = new AbilityButton("Homing Rockets"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil2Chosen = HOME;
				abilityClick(true, false, HOME);
			}
		};
		
		AbilityButton sniperButton2 = new AbilityButton("Sniper Laser"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil2Chosen = SNIPE;
				abilityClick(true, false, SNIPE);
			}
		};
		
		AbilityButton dashButton2 = new AbilityButton("Dash Attack"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil2Chosen = DASH;
				abilityClick(true, false, DASH);
			}
		};
		
		AbilityButton reflectButton2 = new AbilityButton("Reflect"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil2Chosen = REFLECT;
				abilityClick(true, false, REFLECT);
			}
		};
		
		AbilityButton pushButton2 = new AbilityButton("Push"){
			@Override
			public void mouseReleased(MouseEvent e){
				p1Abil2Chosen = PUSH;
				abilityClick(true, false, PUSH);
			}
		};
		
		p1Ability2Array = new AbilityButton[NUM_ABILITIES];
		p1Ability2Array[BURST] = burstButton2;
		p1Ability2Array[HOME] = homingButton2;
		p1Ability2Array[SNIPE] = sniperButton2;
		p1Ability2Array[DASH] = dashButton2;
		p1Ability2Array[REFLECT] = reflectButton2;
		p1Ability2Array[PUSH] = pushButton2;
		
		// P2 ability 1
		AbilityButton burstButton3 = new AbilityButton("Burst Attack"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil1Chosen = BURST;
				abilityClick(false, true, BURST);			}
		};

		AbilityButton homingButton3 = new AbilityButton("Homing Rockets"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil1Chosen = HOME;
				abilityClick(false, true, HOME);
			}
		};

		AbilityButton sniperButton3 = new AbilityButton("Sniper Laser"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil1Chosen = SNIPE;
				abilityClick(false, true, SNIPE);
			}
		};

		AbilityButton dashButton3 = new AbilityButton("Dash Attack"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil1Chosen = DASH;
				abilityClick(false, true, DASH);
			}
		};

		AbilityButton reflectButton3 = new AbilityButton("Reflect"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil1Chosen = REFLECT;
				abilityClick(false, true, REFLECT);
			}
		};
		
		AbilityButton pushButton3 = new AbilityButton("Push"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil1Chosen = PUSH;
				abilityClick(false, true, PUSH);
			}
		};

		p2Ability1Array = new AbilityButton[NUM_ABILITIES];
		p2Ability1Array[BURST] = burstButton3;
		p2Ability1Array[HOME] = homingButton3;
		p2Ability1Array[SNIPE] = sniperButton3;
		p2Ability1Array[DASH] = dashButton3;
		p2Ability1Array[REFLECT] = reflectButton3;
		p2Ability1Array[PUSH] = pushButton3;
		
		// P2 ability 2

		AbilityButton burstButton4 = new AbilityButton("Burst Attack"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil2Chosen = BURST;
				abilityClick(false, false, BURST);
			}
		};

		AbilityButton homingButton4 = new AbilityButton("Homing Rockets"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil2Chosen = HOME;
				abilityClick(false, false, HOME);
			}
		};

		AbilityButton sniperButton4 = new AbilityButton("Sniper Laser"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil2Chosen = SNIPE;
				abilityClick(false, false, SNIPE);
			}
		};

		AbilityButton dashButton4 = new AbilityButton("Dash Attack"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil2Chosen = DASH;
				abilityClick(false, false, DASH);
			}
		};

		AbilityButton reflectButton4 = new AbilityButton("Reflect"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil2Chosen = REFLECT;
				abilityClick(false, false, REFLECT);
			}
		};
		
		AbilityButton pushButton4 = new AbilityButton("Push"){
			@Override
			public void mouseReleased(MouseEvent e){
				p2Abil2Chosen = PUSH;
				abilityClick(false, false, PUSH);
			}
		};
		
		p2Ability2Array = new AbilityButton[NUM_ABILITIES];
		p2Ability2Array[BURST] = burstButton4;
		p2Ability2Array[HOME] = homingButton4;
		p2Ability2Array[SNIPE] = sniperButton4;
		p2Ability2Array[DASH] = dashButton4;
		p2Ability2Array[REFLECT] = reflectButton4;
		p2Ability2Array[PUSH] = pushButton4;
		
		// Labels
		JLabel p1Label = new JLabel("Player 1");
		p1Label.setForeground(Color.WHITE);
		p1Label.setFont(new Font("SansSerif", Font.BOLD, 80));
		JLabel p2Label = new JLabel("Player 2");
		p2Label.setForeground(Color.WHITE);
		p2Label.setFont(new Font("SansSerif", Font.BOLD, 80));
		
		JLabel p1Abil1Label = new JLabel("Ability 1");
		p1Abil1Label.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel p1Abil2Label = new JLabel("Ability 2");
		p1Abil2Label.setFont(new Font("SansSerif", Font.BOLD, 48));
		
		JLabel p2Abil1Label = new JLabel("Ability 1");
		p2Abil1Label.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel p2Abil2Label = new JLabel("Ability 2");
		p2Abil2Label.setFont(new Font("SansSerif", Font.BOLD, 48));
		
		// set up a BoxLayout along vertical middle with a nested panel in the middle and the back button at the bottom
		customizeMenu = new JPanel();
		customizeMenu.setPreferredSize(new Dimension(width, height));
		customizeMenu.setLayout(new BoxLayout(customizeMenu, BoxLayout.PAGE_AXIS));
		customizeMenu.add(Box.createVerticalGlue());
		
		// nested panel holding the labels and ability buttons
		JPanel selectionPanel = new JPanel();
		//selectionPanel.setPreferredSize(new Dimension(width, height/2));
		selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.LINE_AXIS));
		
		// Player 1
		selectionPanel.add(p1Label);
		selectionPanel.setBackground(Color.decode("#002868"));
		
		JPanel p1Abil1 = new JPanel();
		p1Abil1.setBackground(Color.decode(P1COLOR));
		p1Abil1.setLayout(new GridLayout(NUM_ABILITIES+1, 0));
		//p1Abil1.setPreferredSize(new Dimension(width/6, height/2));
		p1Abil1.add(p1Abil1Label);
		p1Abil1.add(burstButton1);
		p1Abil1.add(homingButton1);
		p1Abil1.add(sniperButton1);
		p1Abil1.add(dashButton1);
		p1Abil1.add(reflectButton1);
		p1Abil1.add(pushButton1);
		selectionPanel.add(p1Abil1);
		
		JPanel p1Abil2 = new JPanel();
		p1Abil2.setBackground(Color.decode(P1COLOR));
		p1Abil2.setLayout(new GridLayout(NUM_ABILITIES+1, 0));
		//p1Abil1.setPreferredSize(new Dimension(width/4, height/2));
		p1Abil2.add(p1Abil2Label);
		p1Abil2.add(burstButton2);
		p1Abil2.add(homingButton2);
		p1Abil2.add(sniperButton2);
		p1Abil2.add(dashButton2);
		p1Abil2.add(reflectButton2);
		p1Abil2.add(pushButton2);
		selectionPanel.add(p1Abil2);
		
		// Player 2
		selectionPanel.add(p2Label);
		
		JPanel p2Abil1 = new JPanel();
		p2Abil1.setBackground(Color.decode(P2COLOR));
		p2Abil1.setLayout(new GridLayout(NUM_ABILITIES+1, 0));
		//p2Abil1.setPreferredSize(new Dimension(width/4, height/2));
		p2Abil1.add(p2Abil1Label);
		p2Abil1.add(burstButton3);
		p2Abil1.add(homingButton3);
		p2Abil1.add(sniperButton3);
		p2Abil1.add(dashButton3);
		p2Abil1.add(reflectButton3);
		p2Abil1.add(pushButton3);
		selectionPanel.add(p2Abil1);
		
		JPanel p2Abil2 = new JPanel();
		p2Abil2.setBackground(Color.decode(P2COLOR));
		p2Abil2.setLayout(new GridLayout(NUM_ABILITIES+1, 0));
		//p1Abil1.setPreferredSize(new Dimension(width/4, height/2));
		p2Abil2.add(p2Abil2Label);
		p2Abil2.add(burstButton4);
		p2Abil2.add(homingButton4);
		p2Abil2.add(sniperButton4);
		p2Abil2.add(dashButton4);
		p2Abil2.add(reflectButton4);
		p2Abil2.add(pushButton4);
		selectionPanel.add(p2Abil2);
		
		//selectionPanel.setOpaque(false);
		customizeMenu.add(selectionPanel);
		
		customizeMenu.add(Box.createRigidArea(new Dimension(0, BUTTON_GAP)));
		customizeMenu.add(mainMenuButton);
		customizeMenu.add(Box.createRigidArea(new Dimension(0, BUTTON_GAP)));
		customizeMenu.setOpaque(false);
		
// Controls Menu
		
		// Buttons
		CustomButton backButton = new CustomButton("Back", 500, 100){
			@Override
			public void mouseReleased(MouseEvent e){
				loadKeys();
				swapTo(mainMenu);
				mouseOver = false;
			}
		};
		
		CustomButton applyButton = new CustomButton("Apply", 500, 100){
			@Override
			public void mouseReleased(MouseEvent e){
				sendKeys();
				swapTo(mainMenu);
				mouseOver = false;
			}
		};
		
		accelButton = new KeybindButton("", this);
		reverseButton = new KeybindButton("", this);
		rightButton = new KeybindButton("", this);
		leftButton = new KeybindButton("", this);
		shootButton = new KeybindButton("", this);
		boostButton = new KeybindButton("", this);
		ability1Button = new KeybindButton("", this);
		ability2Button = new KeybindButton("", this);
		
		accelButton2 = new KeybindButton("", this);
		reverseButton2 = new KeybindButton("", this);
		rightButton2 = new KeybindButton("", this);
		leftButton2 = new KeybindButton("", this);
		shootButton2 = new KeybindButton("", this);
		boostButton2 = new KeybindButton("", this);
		ability1Button2 = new KeybindButton("", this);
		ability2Button2 = new KeybindButton("", this);
		
		JLabel p1ControlsLabel = new JLabel("Player 1");
		p1ControlsLabel.setFont(new Font("SansSerif", Font.BOLD, 60));
		JLabel p2ControlsLabel = new JLabel("Player 2");
		p2ControlsLabel.setFont(new Font("SansSerif", Font.BOLD, 60));
		
		
		JLabel accelLabel = new JLabel("Accelerate");
		accelLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel reverseLabel = new JLabel("Reverse");
		reverseLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel rightLabel = new JLabel("Turn Right");
		rightLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel leftLabel = new JLabel("Turn Left");
		leftLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel shootLabel = new JLabel("Shoot");
		shootLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel boostLabel = new JLabel("Boost");
		boostLabel.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel ability1Label = new JLabel("Ability 1");
		ability1Label.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel ability2Label = new JLabel("Ability 2");
		ability2Label.setFont(new Font("SansSerif", Font.BOLD, 48));
		
		JLabel accelLabel2 = new JLabel("Accelerate");
		accelLabel2.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel reverseLabel2 = new JLabel("Reverse");
		reverseLabel2.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel rightLabel2 = new JLabel("Turn Right");
		rightLabel2.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel leftLabel2 = new JLabel("Turn Left");
		leftLabel2.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel shootLabel2 = new JLabel("Shoot");
		shootLabel2.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel boostLabel2 = new JLabel("Boost");
		boostLabel2.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel ability1Label2 = new JLabel("Ability 1");
		ability1Label2.setFont(new Font("SansSerif", Font.BOLD, 48));
		JLabel ability2Label2 = new JLabel("Ability 2");
		ability2Label2.setFont(new Font("SansSerif", Font.BOLD, 48));
		
		
		controlsMenu = new JPanel();
		controlsMenu.setLayout(new BoxLayout(controlsMenu, BoxLayout.PAGE_AXIS));
		
		controlsMenu.setPreferredSize(new Dimension(width, height));
		controlsMenu.add(Box.createVerticalGlue());
		
		JPanel controlsHolder = new JPanel();
		controlsHolder.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		controlsHolder.setBackground(Color.decode("#60ccf7"));
		controlsHolder.setLayout(new FlowLayout());
		//controlsHolder.setOpaque(false);
		
		JPanel p1Controls = new JPanel();
		p1Controls.setBackground(Color.decode(P1COLOR));
		p1Controls.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		p1Controls.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		p1Controls.add(p1ControlsLabel, c);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		p1Controls.add(accelLabel, c);
		c.gridx = 0;
		c.gridy = 2;
		p1Controls.add(reverseLabel, c);
		c.gridx = 0;
		c.gridy = 3;
		p1Controls.add(rightLabel, c);
		c.gridx = 0;
		c.gridy = 4;
		p1Controls.add(leftLabel, c);
		c.gridx = 3;
		c.gridy = 1;
		p1Controls.add(shootLabel, c);
		c.gridx = 3;
		c.gridy = 2;
		p1Controls.add(boostLabel, c);
		c.gridx = 3;
		c.gridy = 3;
		p1Controls.add(ability1Label, c);
		c.gridx = 3;
		c.gridy = 4;
		p1Controls.add(ability2Label, c);
		
		c.gridx = 1;
		c.gridy = 1;
		p1Controls.add(accelButton, c);
		c.gridx = 1;
		c.gridy = 2;
		p1Controls.add(reverseButton, c);
		c.gridx = 1;
		c.gridy = 3;
		p1Controls.add(rightButton, c);
		c.gridx = 1;
		c.gridy = 4;
		p1Controls.add(leftButton, c);
		c.gridx = 4;
		c.gridy = 1;
		p1Controls.add(shootButton, c);
		c.gridx = 4;
		c.gridy = 2;
		p1Controls.add(boostButton, c);
		c.gridx = 4;
		c.gridy = 3;
		p1Controls.add(ability1Button, c);
		c.gridx = 4;
		c.gridy = 4;
		p1Controls.add(ability2Button, c);
		
		JPanel p2Controls = new JPanel();
		p2Controls.setBackground(Color.decode(P2COLOR));
		p2Controls.setLayout(new GridBagLayout());
		p2Controls.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		
		c.gridwidth = 4;
		c.gridx = 0;
		c.gridy = 0;
		p2Controls.add(p2ControlsLabel, c);
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		p2Controls.add(accelLabel2, c);
		c.gridx = 0;
		c.gridy = 2;
		p2Controls.add(reverseLabel2, c);
		c.gridx = 0;
		c.gridy = 3;
		p2Controls.add(rightLabel2, c);
		c.gridx = 0;
		c.gridy = 4;
		p2Controls.add(leftLabel2, c);
		c.gridx = 3;
		c.gridy = 1;
		p2Controls.add(shootLabel2, c);
		c.gridx = 3;
		c.gridy = 2;
		p2Controls.add(boostLabel2, c);
		c.gridx = 3;
		c.gridy = 3;
		p2Controls.add(ability1Label2, c);
		c.gridx = 3;
		c.gridy = 4;
		p2Controls.add(ability2Label2, c);
		
		c.gridx = 1;
		c.gridy = 1;
		p2Controls.add(accelButton2, c);
		c.gridx = 1;
		c.gridy = 2;
		p2Controls.add(reverseButton2, c);
		c.gridx = 1;
		c.gridy = 3;
		p2Controls.add(rightButton2, c);
		c.gridx = 1;
		c.gridy = 4;
		p2Controls.add(leftButton2, c);
		c.gridx = 4;
		c.gridy = 1;
		p2Controls.add(shootButton2, c);
		c.gridx = 4;
		c.gridy = 2;
		p2Controls.add(boostButton2, c);
		c.gridx = 4;
		c.gridy = 3;
		p2Controls.add(ability1Button2, c);
		c.gridx = 4;
		c.gridy = 4;
		p2Controls.add(ability2Button2, c);
		
		controlsHolder.add(p1Controls);
		controlsHolder.add(p2Controls);
		controlsMenu.add(controlsHolder);
		
		JPanel bottomButtons = new JPanel();
		bottomButtons.setLayout(new BoxLayout(bottomButtons, BoxLayout.LINE_AXIS));
		bottomButtons.setOpaque(false);
		bottomButtons.add(backButton);
		bottomButtons.add(Box.createRigidArea(new Dimension(BUTTON_GAP, 0)));
		bottomButtons.add(applyButton);
		
		controlsMenu.add(Box.createVerticalGlue());
		controlsMenu.add(bottomButtons);
		controlsMenu.add(Box.createRigidArea(new Dimension(0, BUTTON_GAP)));
		
		controlsMenu.addKeyListener(new KeyListener());
		controlsMenu.setOpaque(false);
		
		
		currentPanel = mainMenu;
		
		add(currentPanel);
	}
	
	
	private KeybindButton accelButton;
	private KeybindButton reverseButton;
	private KeybindButton rightButton;
	private KeybindButton leftButton;
	private KeybindButton shootButton;
	private KeybindButton boostButton;
	private KeybindButton ability1Button;
	private KeybindButton ability2Button;
	
	private KeybindButton accelButton2;
	private KeybindButton reverseButton2;
	private KeybindButton rightButton2;
	private KeybindButton leftButton2;
	private KeybindButton shootButton2;
	private KeybindButton boostButton2;
	private KeybindButton ability1Button2;
	private KeybindButton ability2Button2;

}
