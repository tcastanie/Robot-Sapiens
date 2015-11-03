/*
* GridCanvas2.java - DistributedBees demo program
* Copyright (C) 1998-2004 P. Bommel, F. Michel
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

package madkit.distributedbees;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;


public class GridCanvas2 extends JPanel//JComponent //JPanel
{
    int MAXX;
    int MAXY;
    
    /**
     * @label bwv 
     */
    BeeWorldViewer bwv;
    
    public GridCanvas2(int width,int height,BeeWorldViewer l)
    {
        setSize(new Dimension(width,height));
        MAXX=getSize().width;
        MAXY=getSize().height;
        setOpaque(true);
        setBackground(Color.black);
        setForeground(Color.white);
        bwv=l;
    } 
    ///////////METHODS///////////////////
    public void addListener(InputListener listener)
    {
        addKeyListener((KeyListener) listener);
        addMouseListener((MouseListener) listener);
        addMouseMotionListener((MouseMotionListener) listener);
    }
    
        
    //Surcharge de la methode "isFocusTraversable" car un Panel ne peut normalement pas obtenir
    // la focalisation de saisie 
    public boolean isFocusTraversable() 
    {
        return true;
    }

    public void flash()
    {
        paintImmediately(getVisibleRect());
    }

    public Dimension getPreferredSize() {return getSize();}
    
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        bwv.paintBees(g);
    }
}



