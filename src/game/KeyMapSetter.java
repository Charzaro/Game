package game;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;

public class KeyMapSetter {
	
	//public static HashMap map = new HashMap<String, Boolean>();
	
	public KeyMapSetter(){
		
	}

	private class upAction extends AbstractAction implements ActionListener{
		private Player p;
		private boolean pressed;

		public upAction(Player p, boolean pressed){
			this. p = p;
			this.pressed = pressed;
		}
		public void actionPerformed(ActionEvent e){
			if(pressed != p.Keys.up){
				p.Keys.up = pressed;
			}
		}

	}
	
	private class downAction extends AbstractAction implements ActionListener{
		private Player p;
		private boolean pressed;

		public downAction(Player p, boolean pressed){
			this. p = p;
			this.pressed = pressed;
		}
		public void actionPerformed(ActionEvent e){
			if(pressed != p.Keys.down){
				p.Keys.down = pressed;
			}
		}

	}
	
	private class rightAction extends AbstractAction implements ActionListener{
		private Player p;
		private boolean pressed;

		public rightAction(Player p, boolean pressed){
			this. p = p;
			this.pressed = pressed;
		}
		public void actionPerformed(ActionEvent e){
			if(pressed != p.Keys.right){
				p.Keys.right = pressed;
			}
		}

	}
	
	private class leftAction extends AbstractAction implements ActionListener{
		private Player p;
		private boolean pressed;

		public leftAction(Player p, boolean pressed){
			this. p = p;
			this.pressed = pressed;
		}
		public void actionPerformed(ActionEvent e){
			if(pressed != p.Keys.left){
				p.Keys.left = pressed;
				if(p.id == 2){
					System.out.println("WORKING");
				}
			}
		}

	}
	
	private class shootAction extends AbstractAction implements ActionListener{
		private Player p;
		private boolean pressed;

		public shootAction(Player p, boolean pressed){
			this. p = p;
			this.pressed = pressed;
		}
		public void actionPerformed(ActionEvent e){
			if(pressed != p.Keys.space){
				p.Keys.space = pressed;
			}
		}

	}
	
	private class boostAction extends AbstractAction implements ActionListener{
		private Player p;
		private boolean pressed;

		public boostAction(Player p, boolean pressed){
			this. p = p;
			this.pressed = pressed;
		}
		public void actionPerformed(ActionEvent e){
			if(pressed != p.Keys.boost){
				p.Keys.boost = pressed;
			}
		}

	}
	
	private class ability1Action extends AbstractAction implements ActionListener{
		private Player p;
		private boolean pressed;

		public ability1Action(Player p, boolean pressed){
			this. p = p;
			this.pressed = pressed;
		}
		public void actionPerformed(ActionEvent e){
			if(pressed != p.Keys.ability1){
				p.Keys.ability1 = pressed;
			}
		}

	}
	
	private class ability2Action extends AbstractAction implements ActionListener{
		private Player p;
		private boolean pressed;

		public ability2Action(Player p, boolean pressed){
			this. p = p;
			this.pressed = pressed;
		}
		public void actionPerformed(ActionEvent e){
			if(pressed != p.Keys.ability2){
				p.Keys.ability2 = pressed;
			}
		}

	}
	

	public void set(InputMap inMap, ActionMap acMap, Player p1, Player p2){
		
		// Player 1
		
		// up
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "p1upPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "p1upRel");
		// down
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "p1downPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "p1downRel");
		// right
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "p1rightPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "p1rightRel");
		// left
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "p1leftPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "p1leftRel");
		// shoot
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0, false), "p1shootPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_O, 0, true), "p1shootRel");
		// boost
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL, 0, false), "p1boostPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DECIMAL, 0, true), "p1boostRel");
		// ability1
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0, false), "p1ability1Press");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD3, 0, true), "p1ability1Rel");
		// ability2
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), "p1ability2Press");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), "p1ability2Rel");
		
		// Player 2
		
		// up
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "p2upPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "p2upRel");
		// down
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "p2downPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "p2downRel");
		// right
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "p2rightPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "p2rightRel");
		// left
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "p2leftPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "p2leftRel");
		// shoot
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, false), "p2shootPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0, true), "p2shootRel");
		// boost
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, false), "p2boostPress");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0, true), "p2boostRel");
		// ability1
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, false), "p2ability1Press");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_B, 0, true), "p2ability1Rel");
		// ability2
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, false), "p2ability2Press");
		inMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0, true), "p2ability2Rel");


		// Player 1
		
		//up
		acMap.put("p1upPress", new upAction(p1, true));
		acMap.put("p1upRel", new upAction(p1, false));
		//down
		acMap.put("p1downPress", new downAction(p1, true));
		acMap.put("p1downRel", new downAction(p1, false));
		//right
		acMap.put("p1rightPress", new rightAction(p1, true));
		acMap.put("p1rightRel", new rightAction(p1, false));
		//left
		acMap.put("p1leftPress", new leftAction(p1, true));
		acMap.put("p1leftRel", new leftAction(p1, false));
		//shoot
		acMap.put("p1shootPress", new shootAction(p1, true));
		acMap.put("p1shootRel", new shootAction(p1, false));
		//boost
		acMap.put("p1boostPress", new boostAction(p1, true));
		acMap.put("p1boostRel", new boostAction(p1, false));
		//ability1
		acMap.put("p1ability1Press", new ability1Action(p1, true));
		acMap.put("p1ability1Rel", new ability1Action(p1, false));
		//ability2
		acMap.put("p1ability2Press", new ability2Action(p1, true));
		acMap.put("p1ability2Rel", new ability2Action(p1, false));
		
		// Player 2
		
		//up
		acMap.put("p2upPress", new upAction(p2, true));
		acMap.put("p2upRel", new upAction(p2, false));
		//down
		acMap.put("p2downPress", new downAction(p2, true));
		acMap.put("p2downRel", new downAction(p2, false));
		//right
		acMap.put("p2rightPress", new rightAction(p2, true));
		acMap.put("p2rightRel", new rightAction(p2, false));
		//left
		acMap.put("p2leftPress", new leftAction(p2, true));
		acMap.put("p2leftRel", new leftAction(p2, false));
		//shoot
		acMap.put("p2shootPress", new shootAction(p2, true));
		acMap.put("p2shootRel", new shootAction(p2, false));
		//boost
		acMap.put("p2boostPress", new boostAction(p2, true));
		acMap.put("p2boostRel", new boostAction(p2, false));
		//ability1
		acMap.put("p2ability1Press", new ability1Action(p2, true));
		acMap.put("p2ability1Rel", new ability1Action(p2, false));
		//ability2
		acMap.put("p2ability2Press", new ability2Action(p2, true));
		acMap.put("p2ability2Rel", new ability2Action(p2, false));
	}

}
