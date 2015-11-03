package madkit.desktop2;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

import madkit.kernel.*;
import madkit.system.property.*;
import madkit.utils.graphics.*;
//import madkit.system.*;
//import madkit.desktopicon.*;
//import madkit.desktopbar.*;

public abstract class DesktopMDI implements Desktop, DesktopProperty, DesktopIconAdder, WindowListener, DesktopWithBar, ActionListener, ComponentListener, MouseListener{
	protected HashMap internalFrames, icons;
	protected AgentInfo ai;
	protected MonJDesktopPane desktop;
	protected StartMenu startMenu;
	protected DesktopBar dBar;
	protected JPanel bar;
	protected PropertyDesktop propertyDesktop;
	protected PropertyFrame propertyFrame;
	protected JPopupMenu popupMenu;
	protected JMenuItem property, output, addIcon, reorder;
	protected DesktopAgentGUI desktopAgentGUI;
	//protected MadkitOutput madkitOutput;
	protected JInternalFrame madkitOutputFrame;
	protected boolean bool;
	protected WindowManager wm;
	
	public DesktopMDI(){
		super();
		internalFrames = new HashMap();
		icons = new HashMap();
		desktop = new MonJDesktopPane();
		desktop.addComponentListener(this);
		desktop.addMouseListener(this);
		wm = new CascadeWindow(this);
		bool = true;
		
		//madkitOutput = new MadkitOutput();
		
		popupMenu = new JPopupMenu();
		
		property = popupMenu.add("Desktop property");
		property.addActionListener(this);
		
		output = popupMenu.add("Desktop output");
		output.addActionListener(this);
		
		addIcon = popupMenu.add("Add icon");
		addIcon.addActionListener(this);
		
		popupMenu.addSeparator();
		
		reorder = popupMenu.add("Re order windows");
		reorder.addActionListener(this);
	}
	
	public void init(AgentInfo ai, LinkedList menuInfos, DesktopAgentGUI desktopAgentGUI){
		this.ai = ai;
		this.desktopAgentGUI = desktopAgentGUI;
		startMenu = new StartMenu();
		createStartMenu(menuInfos);
		
		
		bar = new JPanel(new BorderLayout());
		bar.add(startMenu, BorderLayout.WEST);
		
		dBar = new DesktopBar(this, startMenu);
		bar.add(dBar, BorderLayout.CENTER);
	}
	public int getHeight(){
		return desktop.getHeight();
	}
	public int getWidth(){
		return desktop.getWidth();
	}
	public void toFront(Component c){
		if (c instanceof JInternalFrame){
			((JInternalFrame)c).toFront();
		}
	}
	public void toBack(Component c){
		if (c instanceof JInternalFrame){
			((JInternalFrame)c).toBack();
		}
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
	public Color getBackgroundColor(){
		if (propertyDesktop.getDesktopColor() == null)
			return desktop.getBackground();
		return propertyDesktop.getDesktopColor();
	}
	public String getWallpaperPath(){
		return propertyDesktop.getWallpaperPath();
	}
	public String getWallpaperPos(){
		return propertyDesktop.getWallpaperPos();
	}
	

	public boolean getSkinUse(){return propertyDesktop.getSkinUse();}
	public void setSkinUse(boolean b){
		propertyDesktop.setSkinUse(b);
	}

	public String getSkinTheme(){return propertyDesktop.getSkinTheme();}
	public void setSkinTheme(String b){
		propertyDesktop.setSkinTheme(b);
	}
	
	public void setBackgroundColor(Color color){
		desktop.setBackground(color);
		propertyDesktop.setDesktopColor(color);
	}
	public void setWallpaperPath(String path){
		propertyDesktop.setWallpaperPath(path);
		desktop.setWallpaper(propertyDesktop.getWallpaperPath());
	}
	public void setWallpaperPos(String pos){
		propertyDesktop.setWallpaperPos(pos);
		desktop.setWallpaperPos(propertyDesktop.getWallpaperPos());
	}
	public int getScreenWidth(){
		return desktop.getWidth();
	}
	public int getScreenHeight(){
		return desktop.getHeight();
	}
	public void addIcon(IconInfo iconInfo){
		DesktopIcon icon = new DesktopIcon();
		
		MonMenuItem modifIcon = new MonMenuItem("Icon property", iconInfo);
		modifIcon.setActionCommand("IconProperty");
		modifIcon.addActionListener(this);
		icon.addMenuItem(modifIcon);
		
		modifIcon = new MonMenuItem("Delete icon", iconInfo);
		modifIcon.setActionCommand("DelIcon");
		modifIcon.addActionListener(this);
		icon.addMenuItem(modifIcon);
		
		if(iconInfo.getIcon() != null)
			try{
				icon.setIcon(new ImageIcon(this.getClass().getResource(iconInfo.getIcon())));
			}catch(Exception eeee){ System.out.println ("Icon: " + iconInfo.getIcon() + " not found"); }
		
		if(iconInfo.getLabel() != null)
			icon.setLabel(iconInfo.getLabel());
		
		if(iconInfo.getDescription() != null)
			icon.setToolTipText(iconInfo.getDescription());
			
		icon.setLocation(iconInfo.getX(), iconInfo.getY());
		
		if(iconInfo.getType() != null && iconInfo.getCode() != null){
			if (iconInfo.getType().equalsIgnoreCase("Java")){
				icon.addActionListener(new JavaAgentButton(ai.getAgent(), iconInfo.getCode(), iconInfo.getGUI()));
			}
			else if (iconInfo.getType().equalsIgnoreCase("Scheme")){
				icon.addActionListener(new SchemeAgentButton(ai.getAgent(), iconInfo.getCode(), iconInfo.getGUI()));
			}
			else if (iconInfo.getType().equalsIgnoreCase("Jess")){
				icon.addActionListener(new JessAgentButton(ai.getAgent(), iconInfo.getCode(), iconInfo.getGUI()));
			}
			else if (iconInfo.getType().equalsIgnoreCase("Python")){
				icon.addActionListener(new PythonAgentButton(ai.getAgent(), iconInfo.getCode(), iconInfo.getGUI()));
			}
			else if (iconInfo.getType().equalsIgnoreCase("BeanShell")){
				icon.addActionListener(new BeanShellAgentButton(ai.getAgent(), iconInfo.getCode(), iconInfo.getGUI()));
			}
			else if (iconInfo.getType().equalsIgnoreCase("SEditFormalism")){
				icon.addActionListener(new SEditFormalismButton(ai.getAgent(), iconInfo.getCode(), iconInfo.getGUI()));
			}
			else if (iconInfo.getType().equalsIgnoreCase("SEditFile")){
				icon.addActionListener(new SEditFileButton(ai.getAgent(), iconInfo.getCode(), iconInfo.getGUI()));
			}
		}
		
		icons.put(iconInfo, icon);
		desktop.add(icon);
		icon.show();
	}
	public void removeIcon(IconInfo icon){
		DesktopIcon ic = (DesktopIcon)icons.get(icon);
		if (icon != null){
			desktop.remove(ic);
			icons.remove(ic);
			desktop.repaint();
		}
	}
	
	public void setProperty(PropertyDesktop propertyDesktop){
		this.propertyDesktop = propertyDesktop;
		if (propertyDesktop.getDesktopColor() != null){
			desktop.setBackground(propertyDesktop.getDesktopColor());
		}
		if (propertyDesktop.getWallpaperPath() != null && !propertyDesktop.getWallpaperPath().equals("")){
			desktop.setWallpaper(propertyDesktop.getWallpaperPath());
		}
		if (propertyDesktop.getWallpaperPos() != null){
			desktop.setWallpaperPos(propertyDesktop.getWallpaperPos());
		}
	}
	public void addComponent(AgentInfo _ai, Component c){
		InternalAgentFrame iaf = new InternalAgentFrame(_ai, _ai.getAgent().getName(), c);
		wm.addComponent(iaf);
		if (_ai.getLocation().getX() == -1 && _ai.getLocation().getY() == -1)
			wm.orderComponent(iaf);
		internalFrames.put(c, iaf);
		desktop.add(iaf);
		
		MonMenuItem menu = new MonMenuItem("Agent Property", _ai.getAgent());
		menu.setActionCommand("property");
		menu.addActionListener(this);
		dBar.addButtonBar(iaf).addMenuItem(menu);
		//dBar.addMenuItem(menu);
		
		iaf.show();
	}
	public void removeComponent(Component c){
		Object o = internalFrames.get(c);
		if (o != null){
			wm.removeComponent((Component)o);
			internalFrames.remove(c);
			((JInternalFrame)o).dispose();
		}
	}
	public void dispose(){
		Iterator it = internalFrames.keySet().iterator();
		while (it.hasNext()){
			Component c = (Component)it.next();
			Object o = internalFrames.get(c);
			if (o != null){
				((InternalAgentFrame)o).updateAgentInfo();
				((InternalAgentFrame)o).setAgentInfo(null);
				((InternalAgentFrame)o).dispose();
			}
			it.remove();
		}
		it = icons.keySet().iterator();
	    while (it.hasNext()){
	    	IconInfo iconInfo = (IconInfo) it.next();
	    	DesktopIcon desktopIcon = (DesktopIcon)icons.get(iconInfo);
	    	saveIconState(iconInfo, desktopIcon);
	    }
	}
	protected void saveIconState(IconInfo iconInfo, DesktopIcon desktopIcon){
	    iconInfo.setX((int)desktopIcon.getLocation().getX());
		iconInfo.setY((int)desktopIcon.getLocation().getY());
	}
	public void removePropertyFrame(){
		propertyFrame = null;
	}
	public void removeIconAdderFrame(JInternalFrame f){
		desktop.remove(f);
		desktop.repaint();
	}
	public void actionPerformed(ActionEvent e){
		Object o = e.getSource();
		String command = e.getActionCommand();
		
		if (o == property){
			if (propertyFrame == null){
				propertyFrame = new PropertyFrame(this);
				desktop.add(propertyFrame);
				wm.addComponent(propertyFrame);
				wm.orderComponent(propertyFrame);
				propertyFrame.show();
			}
			else{
				try{ propertyFrame.setSelected(true); }
				catch(Exception ee){ ee.printStackTrace(System.err); }
			}
		}
		else if (o == output){
			AgentLauncher al = AgentLauncher.makeAgentLauncher("madkit.kernel.JavaAgentLauncher",
						AbstractMadkitBooter.getBooter(),null,"madkit.system.MadkitOutput","MadkitOutput",
						null,null,true,new Point(300,400),new Dimension(600,200));
			al.launch();
		/*	if (madkitOutputFrame == null){
				madkitOutputFrame = new JInternalFrame("Madkit Output", true, true, true, false);
				madkitOutputFrame.getContentPane().add(madkitOutput);
				madkitOutputFrame.setLayer(JLayeredPane.PALETTE_LAYER);
				madkitOutputFrame.pack();
				desktop.add(madkitOutputFrame);
				wm.addComponent(madkitOutputFrame);
				wm.orderComponent(madkitOutputFrame);
				madkitOutputFrame.show();
			}
			else if (madkitOutputFrame.isClosed()){
				desktop.add(madkitOutputFrame);
				madkitOutputFrame.show();
			}
			else{
				try{ madkitOutputFrame.setSelected(true); }
				catch(Exception ee){ ee.printStackTrace(System.err); }
			}	*/
		} 
		else if (o == addIcon){
			IconAdderFrame iconAdderFrame = new IconAdderFrame(this, null);
			iconAdderFrame.pack();
			iconAdderFrame.setMinimumSize(iconAdderFrame.getSize());
			iconAdderFrame.show();
			desktop.add(iconAdderFrame);
		}
		else if (o == reorder){
			wm.reOrderAllComponent();
		}
		else if (o instanceof MonMenuItem){
			Object oo = ((MonMenuItem) o).getObject();
			if (oo instanceof IconInfo){
				if (((MonMenuItem) o).getActionCommand().equals("IconProperty")){
					IconAdderFrame iconAdderFrame = new IconAdderFrame(this, (IconInfo)oo);
					iconAdderFrame.pack();
					iconAdderFrame.setMinimumSize(iconAdderFrame.getSize());
					iconAdderFrame.show();
					desktop.add(iconAdderFrame);
				}
				else if (((MonMenuItem) o).getActionCommand().equals("DelIcon")){
					desktopAgentGUI.removeIcon((IconInfo)oo);
				}
			}
			else if ((command.equals("property")) && (oo instanceof AbstractAgent)){
				PropertyAgent propertyAgent = new PropertyAgent((AbstractAgent)oo);
				String label = DesktopBooter.getAgentLabelFromClassName(propertyAgent.getClass().getName());
				ai.getAgent().launchAgent(propertyAgent,label,true);		
			}
		}
	}
	public void componentHidden(ComponentEvent e){}
	public void componentMoved(ComponentEvent e){}
	public void componentShown(ComponentEvent e){}
	public void componentResized(ComponentEvent e){
		Object o = e.getSource();
		if (o == desktop && propertyFrame != null){
			propertyFrame.setScreenSize(desktop.getWidth(), desktop.getHeight());
		}
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){
            if (GraphicUtils.isPopupTrigger(e)){
			popupMenu.show(desktop, e.getX(), e.getY());
            }
        }
	public void mouseReleased(MouseEvent e){
//		if (e.getModifiers() == e.BUTTON3_MASK){
//			popupMenu.show(desktop, e.getX(), e.getY());
//		}
	}
	
	public void windowActivated(WindowEvent e){}
	public void windowClosed(WindowEvent e){}
	public void windowClosing(WindowEvent e){ desktopAgentGUI.quitMadkit(); }
	public void windowDeactivated(WindowEvent e){}
	public void windowDeiconified(WindowEvent e){}
	public void windowIconified(WindowEvent e){}
	public void windowOpened(WindowEvent e){}
	
	public void addNewIcon(IconInfo iconInfo){
		desktopAgentGUI.addIcon(iconInfo);
	}
	public void IconChanged(IconInfo iconInfo){
		DesktopIcon desktopIcon = (DesktopIcon) icons.get(iconInfo);
		if (desktopIcon != null){
			saveIconState(iconInfo, desktopIcon);
			desktop.remove(desktopIcon);
			this.addIcon(iconInfo);
			desktop.repaint();
		}
	}
	
	abstract public Component getTopComponent();
	abstract public boolean setBarOrientation(String orientation);
	abstract public void show();
}
