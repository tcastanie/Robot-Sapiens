package madkit.desktop2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

//import madkit.iconadder.*;
//import madkit.desktopicon.*;
//import madkit.backgroundProperty.*;

interface DesktopProperty{
	public Color getBackgroundColor();
	public String getWallpaperPath();
	public String getWallpaperPos();
	public void setBackgroundColor(Color color);
	public void setWallpaperPath(String path);
	public void setWallpaperPos(String pos);
	public int getScreenWidth();
	public int getScreenHeight();
	public void removePropertyFrame();
	public boolean getSkinUse();
	public void setSkinUse(boolean b);
	public String getSkinTheme();
	public void setSkinTheme(String b);
}

interface DesktopIconAdder{
	public void removeIconAdderFrame(JInternalFrame frame);
	public void addNewIcon(IconInfo iconInfo);
	public void IconChanged(IconInfo iconInfo);
}

public class MonJDesktopPane extends JDesktopPane{
	ImageIcon wallpaper;
	String pos;
	
	public MonJDesktopPane(){
		super();
	}
	public void setWallpaper(String path){
		if (path == null) wallpaper = null;
			wallpaper = new ImageIcon(path);
		repaint();
	}
	public void setWallpaperPos(String pos){
		if (pos == null) this.pos = "";
		else this.pos = pos;
		repaint();
	}
	public boolean isOpaque(){ return false; }
	
	public void paint(Graphics g){
		Graphics2D g2D = (Graphics2D)g;
		
		Rectangle r = g2D.getClipBounds();
		Color c = getBackground();
		if(c == null)
			c = Color.lightGray;
		g2D.setColor(c);
		if (r != null) {
			g2D.fillRect(r.x, r.y, r.width, r.height);
		}
		else {
			g2D.fillRect(0, 0, getWidth(), getHeight());
		}
		
		if (wallpaper != null){
			int X, Y, W, H;
			if (pos.equalsIgnoreCase(PropertyDesktop.CENTER)){
				X = (int)((getWidth() - wallpaper.getIconWidth()) /2);
				Y = (int)((getHeight() - wallpaper.getIconHeight()) /2);
				W = (int)(wallpaper.getIconWidth());
				H = (int)(wallpaper.getIconHeight());
			}
			else{
				X = 0;
				Y = 0;
				W = getWidth();
				H = getHeight();
			}
			Rectangle rect = g2D.getClipBounds();
			g2D.clipRect(0, 0, getWidth(), getHeight());
			
			g2D.drawImage(wallpaper.getImage(), X, Y, W, H, null);
			
			if (rect != null) g2D.setClip(rect);
			else g2D.setClip(null);
		}
		super.paint(g);
	}
}

class IconAdderFrame extends JInternalFrame implements IconAdder, ComponentListener{
	protected DesktopIconAdder desktopIconAdder;
	AddDesktopIcon addDesktopIcon;
	IconInfo iconInfo;
	
	public IconAdderFrame(DesktopIconAdder desktopIconAdder, IconInfo iconInfo){
		super("Icon Maker", true, true, true, false);

		this.addComponentListener(this);
		this.iconInfo = iconInfo;
		this.desktopIconAdder = desktopIconAdder;
		this.setLayer(JLayeredPane.PALETTE_LAYER);
		addDesktopIcon = new AddDesktopIcon(this);
		this.getContentPane().add(addDesktopIcon);
		addDesktopIcon.setIconInfo(iconInfo);
	}
	
	public void closeWindow(){
		desktopIconAdder.removeIconAdderFrame(this);
		this.dispose();
	}
	public void addNewIcon(IconInfo iconInfo){
		desktopIconAdder.addNewIcon(iconInfo);
	}
	public void IconChanged(IconInfo iconInfo){
		desktopIconAdder.IconChanged(iconInfo);
	}
	
	public void pack(){
		super.pack();
		if (iconInfo != null)
			addDesktopIcon.setIconInfo(iconInfo);
	}
	public void componentHidden(ComponentEvent e){}
	public void componentMoved(ComponentEvent e){}
	public void componentResized(ComponentEvent e){}
	public void componentShown(ComponentEvent e){
		addDesktopIcon.scrollToCurrentMiniature();
	}
}

class PropertyFrame extends JInternalFrame implements PropertyView, InternalFrameListener{
	protected DesktopProperty desktop;
	protected PropertyPane propertyPane;
	
	public PropertyFrame(DesktopProperty desktop){
		super("Display property", false, true, false, false);
		this.desktop = desktop;
		this.addInternalFrameListener(this);
		setLayer(JLayeredPane.PALETTE_LAYER);
		propertyPane = new PropertyPane(this, desktop.getScreenWidth(), desktop.getScreenHeight());
		getContentPane().add(propertyPane);
		pack();
	}
	public void setScreenSize(int width, int height){
		propertyPane.setScreenSize(width, height);
	}
	public Color getBackgroundColor(){
		return desktop.getBackgroundColor();
	}
	public String getWallpaperPath(){
		return desktop.getWallpaperPath();
	}
	public String getWallpaperPos(){
		return desktop.getWallpaperPos();
	}
	
	public boolean getSkinUse(){return desktop.getSkinUse();}
	public void setSkinUse(boolean b){desktop.setSkinUse(b);}

	public String getSkinTheme(){return desktop.getSkinTheme();}
	public void setSkinTheme(String b){desktop.setSkinTheme(b);}
	
	public void setBackgroundColor(Color color){
		desktop.setBackgroundColor(color);
	}
	public void setWallpaperPath(String path){
		desktop.setWallpaperPath(path);
	}
	public void setWallpaperPos(String pos){
		desktop.setWallpaperPos(pos);
	}
	public void closeProperty(){
		desktop.removePropertyFrame();
		this.dispose();
	}
	public void internalFrameOpened(InternalFrameEvent e){}
	public void internalFrameClosing(InternalFrameEvent e){ closeProperty(); }
	public void internalFrameClosed(InternalFrameEvent e){}
	public void internalFrameActivated(InternalFrameEvent e){}
	public void internalFrameDeactivated(InternalFrameEvent e){}
	public void internalFrameIconified(InternalFrameEvent e){}
	public void internalFrameDeiconified(InternalFrameEvent e){}
}

class InternalAgentFrame extends JInternalFrame implements InternalFrameListener{
	AgentInfo ai;
	InternalAgentFrame(AgentInfo _ai, String s, Component c){
		super(s,true,true,true,true);
		ai = _ai;
		getContentPane().add(c);
		setLocation(ai.getLocation());
		if (ai.getSize().getWidth() != -1 || ai.getSize().getHeight() != -1){
			setSize(ai.getSize());
		}
		else
			pack();
		/*
		 * on place les fenetres classique sur la couche PALETTE_LAYER 
		 * pour permettre la création d'objet du type icone de bureau
		 */
		setLayer(JLayeredPane.PALETTE_LAYER);
		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		addInternalFrameListener(this);

	}
	public void setAgentInfo(AgentInfo a){ ai = a; }
	public void updateAgentInfo(){
		ai.setLocation(getLocation());
		ai.setSize(getSize());
	}
	
	public void internalFrameOpened(InternalFrameEvent e){}
	public void internalFrameClosing(InternalFrameEvent e) {
		if (ai != null && ai.getAgent() != null)
			ai.getAgent().windowClosing(e);
		else
			e.getInternalFrame().dispose();
	}
	public void internalFrameClosed(InternalFrameEvent e){}
	public void internalFrameActivated(InternalFrameEvent e){}
	public void internalFrameDeactivated(InternalFrameEvent e){}
	public void internalFrameIconified(InternalFrameEvent e){}
	public void internalFrameDeiconified(InternalFrameEvent e){}
}

class MonMenuItem extends JMenuItem{
	Object object;
	
	public MonMenuItem(String str, Object o){
		super(str);
		object = o;
	}

	public Object getObject(){ return (this.object); }
	public void setObject(Object object){ this.object = object; }
}
