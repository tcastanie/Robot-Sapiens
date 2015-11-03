package madkit.desktopbar;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class ButtonBar extends JToggleButton implements ActionListener, InternalFrameListener, MouseListener{
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
	protected void setActive(boolean bool){
		if (bool){
			this.setBackground(Color.ORANGE);
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
		if (e.getModifiers() == e.BUTTON3_MASK){
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
