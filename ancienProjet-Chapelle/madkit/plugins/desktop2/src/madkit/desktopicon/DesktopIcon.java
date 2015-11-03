package madkit.desktopicon;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class DesktopIcon extends JInternalFrame implements InternalFrameListener, MouseInputListener{
	
	public final static Color SELECT_COLOR=new Color(0,0,0,50);
	public final static Color UNSELECT_COLOR=new Color(0,0,0,0);
	JPopupMenu popupMenu;
	JLabel imglabel;
	int refX=0, refY=0;
	String command = null;
	String type;
	LinkedList actionListener;
	
	public DesktopIcon(){
		super();
		actionListener = new LinkedList();
		
		setBorder(new EmptyBorder(0,0,0,0));
		ComponentUI northUI = getUI();
		((BasicInternalFrameUI) northUI).setNorthPane(null);
		setSize(64,64);
		setBackground(UNSELECT_COLOR);
		setOpaque(false);
		
		imglabel = new JLabel(" ");
		imglabel.setHorizontalAlignment(JLabel.CENTER);
		imglabel.setVerticalTextPosition(JLabel.BOTTOM);
		imglabel.setHorizontalTextPosition(JLabel.CENTER);
		imglabel.setSize(64, 64);
		getContentPane().add(imglabel);
	
		setLayer(JLayeredPane.DEFAULT_LAYER);
		
		imglabel.addMouseListener(this);
		imglabel.addMouseMotionListener(this);
		this.addInternalFrameListener(this);
	}
	public void setIcon(byte[] tabImg){
		ImageIcon icone=new ImageIcon(tabImg);
		imglabel.setIcon(icone);
	}
	public void setIcon(ImageIcon icone){
		imglabel.setIcon(icone);
	}
	public void setLabel(String label){
		imglabel.setText(label);
	}
	public void setToolTipText(String toolTip){
		imglabel.setToolTipText(toolTip);
	}
	public void setActionCommand(String command){
		this.command = command;
	}
	public String getActionCommand(){
		if (command == null)
			return imglabel.getText();
		else
			return command;
	}	
	public void addActionListener(ActionListener dwi){
		actionListener.add(dwi);
	}
	public boolean removeActionListener(ActionListener dwi){
		return actionListener.remove(dwi);
	}
	public void addMenuItem(JMenuItem mi){
		if (mi != null){	
			if (popupMenu == null){
				popupMenu = new JPopupMenu();
			}
			popupMenu.add(mi);
		}
	}
	public void addSeparator(){
		popupMenu.addSeparator();
	}
	public void removeMenuItem(JMenuItem mi){
		popupMenu.remove(mi);
	}

	public void internalFrameActivated(InternalFrameEvent e){ setBackground(SELECT_COLOR); }
	public void internalFrameDeactivated(InternalFrameEvent e){ setBackground(UNSELECT_COLOR); }
	public void internalFrameClosed(InternalFrameEvent e){}
	public void internalFrameClosing(InternalFrameEvent e){}
	public void internalFrameDeiconified(InternalFrameEvent e){}
	public void internalFrameIconified(InternalFrameEvent e){}
	public void internalFrameOpened(InternalFrameEvent e){}
	
	public void mousePressed(MouseEvent e){
		refX=e.getX();
		refY=e.getY();
	}
	public void mouseDragged(MouseEvent e){
		if (getLayer() == JLayeredPane.DEFAULT_LAYER.intValue())
			setLayer(JLayeredPane.POPUP_LAYER);
		setLocation(getX()+e.getX()-refX,getY()+e.getY()-refY);
	}
	public void mouseClicked(MouseEvent e){
		if ((e.getModifiers() == e.BUTTON1_MASK) && (e.getClickCount() == 2)){
			Iterator it = actionListener.iterator();
			while(it.hasNext()){
				((ActionListener)it.next()).actionPerformed(new ActionEvent(this, 0, this.command, e.getModifiers()));
			}
		}
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseReleased(MouseEvent e){
		setLayer(JLayeredPane.DEFAULT_LAYER);
		if (e.getModifiers() == e.BUTTON3_MASK){
			if (popupMenu != null)
				popupMenu.show(this, e.getX(), e.getY());
		}
	}
	public void mouseMoved(MouseEvent e){}


}