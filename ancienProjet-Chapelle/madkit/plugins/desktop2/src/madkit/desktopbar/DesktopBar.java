package madkit.desktopbar;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

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
	JComponent start;
	
	public DesktopBar(DesktopWithBar desktop, JComponent start){
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
	protected void activeButton(ButtonBar bb){
		if (currentButton != null)
			currentButton.setActive(false);
		currentButton = bb;
		currentButton.setActive(true);
	}
	protected void removeButton(ButtonBar bb){
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
