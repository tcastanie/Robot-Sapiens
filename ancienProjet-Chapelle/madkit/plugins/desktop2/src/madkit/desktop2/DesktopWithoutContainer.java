package madkit.desktop2;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

//import madkit.desktopicon.*;

public class DesktopWithoutContainer implements Desktop, WindowListener{
	protected HashMap frames;
	protected AgentInfo ai;
	protected StartMenu startMenu;
	protected JFrame frame;
	protected DesktopAgentGUI desktopAgentGUI;
	protected boolean bool;
	protected WindowManager wm;
	
	public DesktopWithoutContainer(){
		super();
		frames = new HashMap();
		wm = new CascadeWindow(this);
		bool = true;
	}
	
	public void init(AgentInfo ai, LinkedList menuInfos, DesktopAgentGUI desktopAgentGUI){
		this.ai = ai;
		this.desktopAgentGUI = desktopAgentGUI;
		startMenu = new StartMenu();
		createStartMenu(menuInfos);
		
		frame = new JFrame("MadKit");
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(startMenu, BorderLayout.CENTER);
		frame.pack();
		frame.addWindowListener(this);
		
		if (ai.getLocation().getX() == -1 && ai.getLocation().getY() == -1){
			frame.setLocation(0, 0);
			startMenu.setOrientation(StartMenu.SOUTH);
		}
		else{
			if(startMenu.getOrientation().equals(StartMenu.NORTH))
				frame.setLocation(ai.getLocation());
			else if(startMenu.getOrientation().equals(StartMenu.WEST))
				frame.setLocation(ai.getLocation());
			else if(startMenu.getOrientation().equals(StartMenu.EAST))
				frame.setLocation((int)(ai.getLocation().getX() + ai.getSize().getWidth() - frame.getSize().getWidth()),
									(int)(ai.getLocation().getY()));
			else if(startMenu.getOrientation().equals(StartMenu.SOUTH))
				frame.setLocation((int)(ai.getLocation().getX()),
									(int)(ai.getLocation().getY() + ai.getSize().getHeight() - frame.getSize().getHeight()));
		}
	}
	public int getHeight(){
		return (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
	}
	public int getWidth(){
		return (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	}
	
	protected void createStartMenu(LinkedList menuInfos){
		Iterator it = menuInfos.iterator();
		while (it.hasNext()){
			MenuInfo menuInfo = (MenuInfo) it.next();
			JMenu menu = new JMenu(menuInfo.getName());
			String str = menuInfo.getIcon();
			if(str != null)
				try{
					menu.setIcon(new ImageIcon(this.getClass().getResource(str)));
				}catch(Exception eeee){ System.out.println ("Icon: " + str + " not found"); }
			addToStartMenu(menu, menuInfo);
			startMenu.addMenuItem(menu);
		}
	}
	
	protected void addToStartMenu(JMenu menu, MenuInfo menuInfo){
		Iterator it = menuInfo.getMenuInfo().iterator();
		while (it.hasNext()){
			MenuInfo mi = (MenuInfo) it.next();
			JMenu m = new JMenu(mi.getName());
			String str = mi.getIcon();
			if(str != null)
				try{
					m.setIcon(new ImageIcon(this.getClass().getResource(str)));
				}catch(Exception eeee){ System.out.println ("Icon: " + str + " not found"); }
			
			addToStartMenu(m, mi);
			startMenu.addMenuItem(menu);
			menu.add(m);
		}
		it = menuInfo.getMenuItemInfo().iterator();
		while (it.hasNext()){
			MenuItemInfo mii = (MenuItemInfo) it.next();
			JMenuItem m = new JMenuItem(mii.getName());
			String str = mii.getIcon();
			if(str != null)
				try{
					m.setIcon(new ImageIcon(this.getClass().getResource(str)));
				}catch(Exception eeee){ System.out.println ("Icon: " + str + " not found"); }
			
			if (desktopAgentGUI.addListenerTo(mii, m))
				menu.add(m);
		}
	}
	
	public void addMenuSystem(JMenuItem menuSystem){
		if (bool){
			startMenu.addSeparator();
			bool = false;
		}
		startMenu.addMenuItem(menuSystem);
	}
	
	public void addIcon(IconInfo iconInfo){}
	public void removeIcon(IconInfo icon){}
	public void setProperty(PropertyDesktop propertyDesktop){}
	public void toFront(Component c){
		if (c instanceof JFrame){
			((JFrame)c).toFront();
		}
	}
	public void toBack(Component c){
		if (c instanceof JFrame){
			((JFrame)c).toBack();
		}
	}
	
	public void addComponent(AgentInfo _ai, Component c){
		AgentJFrame f = new AgentJFrame(_ai, _ai.getAgent().getName(), c);
		wm.addComponent(f);
		if (_ai.getLocation().getX() == -1 && _ai.getLocation().getY() == -1)
			wm.orderComponent(f);
		frames.put(c, f);
		f.show();
	}
	public void removeComponent(Component c){
		Object o = frames.get(c);
		if (o != null){
			wm.removeComponent((Component)o);
			frames.remove(c);
			((JFrame)o).dispose();
		}
	}
	public void dispose(){
		Iterator it = frames.keySet().iterator();
		while (it.hasNext()){
			Component c = (Component)it.next();
			Object o = frames.get(c);
			if (o != null){
				((AgentJFrame)o).updateAgentInfo();
				((AgentJFrame)o).setAgentInfo(null);
				((AgentJFrame)o).dispose();
			}
			it.remove();
		}
		frame.getContentPane().remove(startMenu);
		frame.dispose();
	}
	public Component getTopComponent(){ return frame; }
	public void show(){
		frame.setVisible(true);
	}
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowClosing(WindowEvent e){ desktopAgentGUI.quitMadkit(); }
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
}

class AgentJFrame extends JFrame implements WindowListener {
	AgentInfo ai;
	AgentJFrame(AgentInfo _ai, String s, Component c){
		super(s);
		ai = _ai;
		getContentPane().add(c);
		setLocation(ai.getLocation());
		if (ai.getSize().getWidth() != -1 || ai.getSize().getHeight() != -1){
			setSize(ai.getSize());
		}
		else
			pack();

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(this);
	}
	public void setAgentInfo(AgentInfo a){ ai = a; }
	public void updateAgentInfo(){
		ai.setLocation(getLocation());
		ai.setSize(getSize());
	}
	
	public void windowOpened(WindowEvent e){}
	public void windowClosing(WindowEvent e) {
		if (ai != null && ai.getAgent() != null)
			ai.getAgent().windowClosing(e);
		else
			e.getWindow().dispose();
	}
	public void windowClosed(WindowEvent e){}
	public void windowActivated(WindowEvent e){}
	public void windowDeactivated(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
}
