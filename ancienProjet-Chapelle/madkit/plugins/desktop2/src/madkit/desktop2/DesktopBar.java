
package madkit.desktop2;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

interface DesktopWithBar{
	public boolean setBarOrientation(String orientation);
}

public class DesktopBar extends JPanel{
	public final static String NORTH = BorderLayout.NORTH;
	public final static String SOUTH = BorderLayout.SOUTH;
	public final static String EAST = BorderLayout.EAST;
	public final static String WEST = BorderLayout.WEST;
	
	LinkedList bBar;
	ButtonBar currentButton = null;
	String orientation;
	JPanel bar;
	DesktopWithBar desktop;
	JPanel startMenu;
	PosDesktopBar pos;
	StartMenu start;
	
	public DesktopBar(DesktopWithBar desktop, StartMenu start){
		super();
		orientation = SOUTH;
		bBar = new LinkedList();
		this.desktop = desktop;
		
		setLayout(new BorderLayout());
		bar = new JPanel(new GridLayout(1, 0));
		add(bar, BorderLayout.CENTER);
		
		startMenu = new JPanel(new BorderLayout());
		this.start = start; //new StartMenu(this);
		startMenu.add(start, BorderLayout.CENTER);
		
		pos = new PosDesktopBar(this, orientation);
		startMenu.add(pos, BorderLayout.EAST);
		
		add(startMenu, BorderLayout.WEST);
	}
	
	public String getOrientation(){ return orientation; }
	
	public void setOrientation(String orientation){
		if (desktop.setBarOrientation(orientation)){
			this.orientation = orientation;
			startMenu.remove(pos);
			remove(startMenu);
	    
		    if ((orientation.equals(NORTH))||(orientation.equals(SOUTH))){
		    	startMenu.add(pos, EAST);
	    		add(startMenu, WEST);
	    		bar.setLayout(new GridLayout(1, 0));
	    	}
	    	else{
		    	startMenu.add(pos, SOUTH);
	    		add(startMenu, NORTH);
	    		bar.setLayout(new GridLayout(0, 1));
	    	}
			updateUI();
		}
	}
	
	public ButtonBar addButtonBar(JInternalFrame iaf){
		ButtonBar bb = new ButtonBar(this, iaf, iaf.getTitle());
		bar.add(bb);
		bBar.add(bb);
		activeButton(bb);
		return bb;	
	}
	public String toString(){
		return "[DesktopBar] Orientation: " + orientation;
	}
	public void activeButton(ButtonBar bb){
		if (currentButton != null)
			currentButton.setActive(false);
		currentButton = bb;
		currentButton.setActive(true);
	}
	public void removeButton(ButtonBar bb){
		bBar.remove(bb);
		bar.remove(bb);
		bar.repaint();
	}
	//public void addMenuItem(JMenuItem mi){
	//	start.addMenuItem(mi);
	//}
	//public void addSeparator(){
	//	start.addSeparator();
	//}
	//public void removeMenuItem(JMenuItem mi){
	//	start.remove(mi);
	//}
	//public boolean isFullScreen(){
	//	return desktop.isFullScreen();
	//}
	//public void setFullScreen(boolean bool){
	//	desktop.setFullScreen(bool);
	//}
}
class buttonOrientation extends JButton{
	String orientation;
	public buttonOrientation(String orientation){
		super();
		this.orientation = orientation;
	}
	public void paint(Graphics g){
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		int x = this.getWidth();
		int y = this.getHeight();
		int z = 4;
		
		if (orientation.equals(DesktopBar.WEST)){
			int[] X = {x-z  , x-z  , 2*x/5, x/2, z  , x/2, 2*x/5};
			int[] Y = {2*y/5, 3*y/5, 3*y/5, y-z, y/2, z  , 2*y/5};
			g2D.draw(new Polygon(X, Y, 7));
		}
		if (orientation.equals(DesktopBar.EAST)){
			int[] X = {z    , z    , 2*x/3, x/2, x-z, x/2, 2*x/3};
			int[] Y = {2*y/5, 3*y/5, 3*y/5, y-z, y/2, z  , 2*y/5};
			g2D.draw(new Polygon(X, Y, 7));
		}
		if (orientation.equals(DesktopBar.NORTH)){
			int[] X = {2*x/5, 3*x/5, 3*x/5, x-z, x/2, z  , 2*x/5};
			int[] Y = {y-z  , y-z  , 2*y/5, y/2, z  , y/2, 2*y/5};
			g2D.draw(new Polygon(X, Y, 7));
		}
		if (orientation.equals(DesktopBar.SOUTH)){
			int[] X = {2*x/5, 3*x/5, 3*x/5, x-z, x/2, z  , 2*x/5};
			int[] Y = {z    , z    , 2*y/3, y/2, y-z, y/2, 2*y/3};
			g2D.draw(new Polygon(X, Y, 7));
		}
	}
}

class PosDesktopBar extends JPanel implements ActionListener{
	DesktopBar dBar;
	JButton N;
	JButton S;
	JButton W;
	JButton E;
	Dimension size = new Dimension(80, 20);
	
	public PosDesktopBar(DesktopBar dBar, String dir){
		super();
		this.dBar = dBar;
		
		/*if (dir.equals(DesktopBar.NORTH) || dir.equals(DesktopBar.SOUTH))
			setLayout(new GridLayout(2,2));
		else*/
			setLayout(new GridLayout(1,4));
		
		N = new buttonOrientation(DesktopBar.NORTH);
		N.addActionListener(this);
		S = new buttonOrientation(DesktopBar.SOUTH);
		S.addActionListener(this);
		W = new buttonOrientation(DesktopBar.WEST);
		W.addActionListener(this);
		E = new buttonOrientation(DesktopBar.EAST);
		E.addActionListener(this);
		
		add(N);
		add(S);
		add(W);
		add(E);
	}
	public Dimension getPreferredSize(){
		return size;
	}
	public void actionPerformed(ActionEvent e){
		Object src = e.getSource();
		if (src == N){
			/*setLayout(new GridLayout(2,2));
			size.width = 80;
			size.height = 20;*/
			dBar.setOrientation(DesktopBar.NORTH);
		}
		if (src == S){/*
			setLayout(new GridLayout(2,2));
			size.width = 80;
			size.height = 20;*/
			dBar.setOrientation(DesktopBar.SOUTH);
		}
		if (src == E){/*
			setLayout(new GridLayout(1,4));
			size.width = 80;
			size.height = 20;*/
			dBar.setOrientation(DesktopBar.EAST);
		}
		if (src == W){/*
			setLayout(new GridLayout(1,4));
			size.width = 80;
			size.height = 20;*/
			dBar.setOrientation(DesktopBar.WEST);
		}
	}
}

class ButtonBar extends JToggleButton implements ActionListener, InternalFrameListener, MouseListener{
	Component component;
	boolean visible;
	Color defaultColor;
	DesktopBar dBar;
	JPopupMenu popupMenu;
	JMenuItem reduce, enlarge, close, restore;
	
	public ButtonBar(DesktopBar dBar, Component component, String title){
		super(title);
		this.component = component;
		this.addActionListener(this);
		this.dBar = dBar;
		visible = true;
		defaultColor = this.getBackground();
		
		if (component instanceof JInternalFrame){
			this.addMouseListener(this);
			
			popupMenu = new JPopupMenu();
			restore = popupMenu.add("Restore");
			restore.addActionListener(this);
			reduce = popupMenu.add("Reduce");
			reduce.addActionListener(this);
			enlarge = popupMenu.add("Enlarge");
			enlarge.addActionListener(this);
			popupMenu.addSeparator();
			close = popupMenu.add("Close");
			close.addActionListener(this);
			
			((JInternalFrame)component).addInternalFrameListener(this);
		}
	}
	public void setActive(boolean bool){
		if (bool){
			this.setBackground(Color.orange);
			this.setSelected(true);
		}
		else{
			this.setBackground(defaultColor);
			this.setSelected(false);
		}
	}
	public void actionPerformed(ActionEvent e){
		if (component instanceof JInternalFrame){
			JInternalFrame internalFrame = (JInternalFrame) component;
			Object o = e.getSource();
			if (o == close){
				internalFrame.doDefaultCloseAction();
			}
			else if (o == enlarge){
				try{
					internalFrame.setMaximum(true);
				}catch(Exception ex){ ex.printStackTrace(System.err); }
			}
			else if (o == reduce){
				try{
					internalFrame.setIcon(true);
				}catch(Exception ex){ ex.printStackTrace(System.err); }
			}
			else if (o == restore){
				if (internalFrame.isIcon()){
					try{
						internalFrame.setIcon(false);
					}catch(Exception ex){ ex.printStackTrace(System.err); }
				}
				else{
					try{
						internalFrame.setMaximum(false);
					}catch(Exception ex){ ex.printStackTrace(System.err); }
				}
				
			}
			else if (!internalFrame.isVisible()){
				try{
					internalFrame.setIcon(false);
				}catch(Exception ex){ ex.printStackTrace(System.err); }
			}
			else if (internalFrame.isSelected()){
				try{
					internalFrame.setIcon(true);
				}catch(Exception ex){ ex.printStackTrace(System.err); }
			}
			else{
				try{
					internalFrame.setSelected(true);
					dBar.activeButton(this);
				}catch(Exception ex){ ex.printStackTrace(System.err); }
			}
		}
	}
	public Dimension getPreferredSize(){ return new Dimension(80,20); }
	
	public void internalFrameActivated(InternalFrameEvent e){
		dBar.activeButton(this);
	}
	public void internalFrameClosed(InternalFrameEvent e){
		dBar.removeButton(this);
	}
	public void internalFrameClosing(InternalFrameEvent e){
		try{
			((JInternalFrame)component).setIcon(true);
		}catch(Exception ex){ ex.printStackTrace(System.err); }
	}
	public void internalFrameDeiconified(InternalFrameEvent e){
		((JInternalFrame)component).show();
		dBar.activeButton(this);
	}
	public void internalFrameIconified(InternalFrameEvent e){
		((JInternalFrame)component).hide();
		setActive(false);
	}
	public void internalFrameDeactivated(InternalFrameEvent e){
		setActive(false);
	}
	public void internalFrameOpened(InternalFrameEvent e){}
	
	
	
	public void mouseClicked(MouseEvent e){
		//e.getButton()
		if (e.getModifiers() == MouseEvent.BUTTON3_MASK){
			if (component instanceof JInternalFrame){
				JInternalFrame internalFrame = (JInternalFrame) component;
				try{
					internalFrame.setSelected(true);
					dBar.activeButton(this);
				}catch(Exception ex){ ex.printStackTrace(System.err); }
			}
			
			if (((JInternalFrame)component).isMaximum() || ((JInternalFrame)component).isIcon())
				restore.setEnabled(true);
			else
				restore.setEnabled(false);
				
			if (((JInternalFrame)component).isIcon())
				reduce.setEnabled(false);
			else
				reduce.setEnabled(true);
			
			if (((JInternalFrame)component).isMaximum())
				enlarge.setEnabled(false);
			else
				enlarge.setEnabled(true);
				
				
			if (dBar.getOrientation().equals(DesktopBar.SOUTH))
				popupMenu.show(this, e.getX(), e.getY() - (int)popupMenu.getPreferredSize().getHeight());
			else if (dBar.getOrientation().equals(DesktopBar.EAST))
				popupMenu.show(this, e.getX() - (int)popupMenu.getPreferredSize().getWidth(), e.getY());
			else if (dBar.getOrientation().equals(DesktopBar.NORTH))
				popupMenu.show(this, e.getX(), e.getY());
			else if (dBar.getOrientation().equals(DesktopBar.WEST))
				popupMenu.show(this, e.getX(), e.getY());
		}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	
	public void addMenuItem(JMenuItem mi){
		popupMenu.add(mi);
	}
	public void addSeparator(){
		popupMenu.addSeparator();
	}
	public void removeMenuItem(JMenuItem mi){
		popupMenu.remove(mi);
	}
}
