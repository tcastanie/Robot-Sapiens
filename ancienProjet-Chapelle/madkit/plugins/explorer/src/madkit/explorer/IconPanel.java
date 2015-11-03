package madkit.explorer;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import madkit.TreeTools.*;
import madkit.kernel.*;

public class IconPanel extends JPanel {
	
	AbstractAgent ag;

	private Color BackGround = Color.white;
	private Color TextColor = Color.black;
	int Nb_Y, Nb_X, iconSize, Nb_Icons;

	public IconPanel(AbstractAgent ag, int iconSize) {
		super(new FlowLayout());
		this.ag = ag;
		this.setBackground(BackGround);
		this.iconSize = iconSize;
		this.Nb_Icons = 0;
	}

	public void clear() {
		this.removeAll();
		this.Nb_Icons = 0;
		this.repaint();
	}

	public void addItem(GenericIconDescriptor desc, String item) {
		Icon icon = new Icon(ag, desc, item, iconSize, this);
		desc.setArg(icon);
		this.add(icon);
		this.Nb_Icons++;
		this.setPreferredSize(new Dimension(iconSize, Nb_Icons * iconSize));
		this.repaint();
	}

	 public void renameItem(GenericIconDescriptor desc, String oldName, String newName) {

		 Component tab[] = this.getComponents();
		 int index = indexOf(oldName, tab);
		 if (index != -1)
		 {
			 this.remove(index);
			 Icon icon = (Icon) tab[index];
			 if(newName != null)
			 {
				 icon.name = newName;
				 this.add(icon,index);
				 icon.setToolTipText(icon.name);
			 	 icon.getDescriptor().setArg(icon);
			 }			
			 this.setPreferredSize(new Dimension(iconSize, Nb_Icons * iconSize));
			 this.repaint();
		 }
	 }

	public void CopyItem(String newName) {

		Component tab[] = this.getComponents();
		int index = indexOf(newName, tab);
		if (index != -1)
		{
			this.remove(index);
			Icon icon = (Icon) tab[index];
			if(newName != null)
			{
				icon.name = newName;
				this.add(icon,index);
				icon.setToolTipText(icon.name);
				icon.getDescriptor().setArg(icon);
			}			
			this.setPreferredSize(new Dimension(iconSize, Nb_Icons * iconSize));
			this.repaint();
		}
	}



	public int indexOf(String oldName, Component tab[])
	{
		if (countItems() == 0)
			return 0;
		else
		{
			int i;
			int index = -1;	
			for (i=0; i<=countItems()-1;i++) 
			{
				Icon icon = (Icon)tab[i];
				if (icon.name.equals(oldName)) 
				{
					index=i ;
					tab[i] = icon;
				}
			}
			return index;
		}
	}
//fin Brd

	public int countItems() {
		return this.getComponentCount();
	}
	
//	rajout brd

	public void simpleClick(AWTEvent e)
	{
		
	}
//fin	rajout brd


	public void leftClick(AWTEvent e) {

	}
	public void doubleClick(AWTEvent e) {
	}
	public void rightClick(AWTEvent e) {
		Icon icon = (Icon) (e.getSource());
		JPopupMenu popup = icon.getDescriptor().getPopup();
		if (popup == null)
			return;
		MouseEvent me = (MouseEvent) e;
		icon.getDescriptor().setArg(icon);
		popup.show(me.getComponent(), me.getX(), me.getY());
		
      
	}


	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Dimension tmp = getSize();
		//System.out.println("DIMENSION : "+tmp);
		Color bg = getBackground();
		Color fg = getForeground();
		Nb_X = tmp.width / iconSize;
		Nb_Y = Nb_Icons / Nb_X;
		if (Nb_Icons % Nb_X != 0)
			Nb_Y++;
		//System.out.println("Nombre d'icones en largeur : "+Nb_X);
		//System.out.println("Nombre d'icones en hauteur : "+Nb_Y);
		g.setColor(TextColor);
		Component Tab[] = this.getComponents();
		for (int i = 0; i < countItems(); i++) {
			//System.out.println(Tab[i]);
			int x = i % Nb_X;
			int y = i / Nb_X;
			Tab[i].setSize(iconSize, iconSize);
			Tab[i].setLocation(x * iconSize, y * iconSize);
			Tab[i].setVisible(true);
		}
		g.setColor(bg);
		setPreferredSize(new Dimension(iconSize, Nb_Y * iconSize));

	}
}
