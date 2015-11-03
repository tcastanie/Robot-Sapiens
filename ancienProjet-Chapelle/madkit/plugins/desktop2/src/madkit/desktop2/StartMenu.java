package madkit.desktop2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StartMenu extends JButton implements ActionListener{
	public final static String NORTH = BorderLayout.NORTH;
	public final static String SOUTH = BorderLayout.SOUTH;
	public final static String EAST = BorderLayout.EAST;
	public final static String WEST = BorderLayout.WEST;
	
	JPopupMenu popupMenu;
	String orientation;
	
	public StartMenu(String orientation){
		super("MadKit");
		this.orientation = orientation;
		this.addActionListener(this);
	}
	public StartMenu(){
		super("MadKit");
		this.orientation = SOUTH;
		this.addActionListener(this);
	}
	public Dimension getPreferredSize(){
		return new Dimension(80, 20);
	}
	public void addMenuItem(JMenuItem mi){
		if (mi != null){	
			if (popupMenu == null)
				popupMenu = new JPopupMenu();
			popupMenu.add(mi);
		}
	}
	public void addSeparator(){
		if (popupMenu == null)
			popupMenu = new JPopupMenu();
		popupMenu.addSeparator();
	}
	public void removeMenuItem(JMenuItem mi){
		popupMenu.remove(mi);
	}
	public void setOrientation(String orientation){ this.orientation = orientation; }
	public String getOrientation(){ return orientation; }
	
	public void actionPerformed(ActionEvent e){
		if (popupMenu != null){
			Object o = e.getSource();
			if (o == this){
				if (orientation.equals(NORTH))			
					popupMenu.show(this, this.getX() , this.getY() + (int)getHeight());
				else if (orientation.equals(EAST))				
					popupMenu.show(this, this.getX() + (int)getWidth() - (int)popupMenu.getPreferredSize().getWidth(), this.getY() + (int)getPreferredSize().getHeight());
				else if (orientation.equals(WEST))				
					popupMenu.show(this, this.getX() , this.getY() + (int)getHeight());
				else //if(orientation.equals(SOUTH))				
					popupMenu.show(this, this.getX() , this.getY() - (int)popupMenu.getPreferredSize().getHeight());
				//else
				//	popupMenu.show(this, e.getX() , e.getY());
			}
		}
	}
}