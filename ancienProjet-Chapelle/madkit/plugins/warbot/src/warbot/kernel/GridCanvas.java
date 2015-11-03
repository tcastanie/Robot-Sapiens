/*
* GridCanvas.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package warbot.kernel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

final class GridCanvas extends JPanel
{
    AbstractViewer bwv;

    public void flash()
    {
	paintImmediately(getVisibleRect());
    }
    
    public GridCanvas(int width,int height,AbstractViewer l)
    {
	setOpaque(true);
	setBackground(Color.white);
	setForeground(Color.white);
	setSize(new Dimension(width,height));
	bwv=l;
    }
    public Dimension getPreferredSize() {return getSize();}
    
    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);
	bwv.paintEntities(g);
    }
}










