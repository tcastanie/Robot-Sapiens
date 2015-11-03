package madkit.explorer;

import java.awt.*;
import javax.swing.*;


import java.awt.event.*;

import madkit.TreeTools.*;
import madkit.kernel.AbstractAgent;
import madkit.utils.graphics.*;

public class Icon extends JPanel implements  MouseListener
											{

	GenericIconDescriptor desc;
	IconPanel iconPanel;
	String name;
	private int x_pos, y_pos, iconSize;
	private boolean selected;
	static Icon LastSelected = null;

	AbstractAgent ag;

	public Icon(
		AbstractAgent ag,
		GenericIconDescriptor desc,
		String name,
		int iconSize,
		IconPanel iconPanel) {
		this.desc = desc;
		this.name = name;
		this.iconSize = iconSize;
		this.selected = false;
		this.x_pos = 0;
		this.y_pos = 0;
		this.setBackground(Color.white);
		this.iconPanel = iconPanel;
		this.ag = ag;
		this.addMouseListener(this);
//		this.addMouseMotionListener(this);
		this.setToolTipText(name);
		
	}
	
	protected void setDescriptor(GenericIconDescriptor d){
		desc = d;
		desc.setArg(this);
	}

	public void drawName(String name, Graphics g) {
		int size = g.getFontMetrics().stringWidth(name);
		if (size > iconSize) {
			if (this.selected) {
				g.fillRect((iconSize - size - 2) / 2, (iconSize/2)+2, size + 2, 14);
				g.setColor(Color.white);
			} else
//				g.setColor(Color.red);
				g.setColor(Color.black);
			g.drawString((name.substring(0, (iconSize / 6 - 1)) + ".."), 3, (iconSize/2)+12);
		} else {
			if (this.selected) {g.fillRect((iconSize - size - 2) / 2, (iconSize/2)+2, size + 2, 14);
				g.setColor(Color.white);
			} else
				//g.setColor(Color.red);
				g.setColor(Color.black);
			g.drawString(name, (iconSize - size) / 2, (iconSize/2)+12);
		}
	}
	
	protected Image getImage(){
		return desc.getImage().getImage();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		int third = iconSize / 3;
		Image image = getImage();
		g.drawImage(image,(iconSize - third) / 2, third / 2, third, third, this);
		drawName(name, g);
	}

	public String getName() {
		return name;
	}
	public GenericIconDescriptor getDescriptor() {
		return desc;
	}
	
	public void command(String s){
		System.err.println("Invalid action "+s);
	
	}


	//MouseListener
	public void mousePressed(MouseEvent e) {
		
		if (this.selected == false) {
			if (LastSelected != null) {
				LastSelected.selected = false;
				LastSelected.repaint();
			}
			LastSelected = this;
			LastSelected.selected = true;
			LastSelected.repaint();

		} else {
			this.selected = false;
			this.repaint();
			LastSelected = null;
		}
		int clicks = e.getClickCount();
		int bouton = e.getButton();
		if (clicks == 2 && bouton == 1) {
			iconPanel.doubleClick(e);
			return;
		}
		if (GraphicUtils.isPopupTrigger(e)) {
			iconPanel.rightClick(e);
			return;
		} else
			iconPanel.leftClick(e);
			iconPanel.simpleClick(e);
		
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		
	}


}
