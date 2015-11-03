/*
* BwvInputListener.java - DistributedBees demo program
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

import java.awt.Cursor;
import java.awt.event.*;
import java.awt.Color;

/** this class defines an InputListener that listen the inputs of a user : mouse events and keys events.
It send then to the Controler a PointMessage with the QueenBee new position.
=========================================================
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation","inputListener");
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation","java.awt.Color[r=...,g=...,b=...]InputListener");
=========================================================
* @see Controler
  @author Pierre BOMMEL, Fabien MICHEL
* @version 1.1 11/05/2000 
*/

public class BwvInputListener extends InputListener 
{
    ///////////  ATTRIBUTS  ///////////// 


    /**
     * @label myTV 
     */
    protected GridCanvas2 myTV; // pour mouseEntered et mouseExited


    /**
     * @label myBwViewer 
     */
    protected BeeWorldViewer myBwViewer;
    
    ///////////  Constructor  ///////////// 
    public BwvInputListener(GridCanvas2 panel, BeeWorldViewer viewer)
    {
        super();
        myTV = panel;   
        myBwViewer = viewer;
    }
    
    //////////////////////////////////////////////////////////////////////  
    public void changeColoredView(Color c, boolean b)  
        {
            if (b)
                myBwViewer.addColoredRole(c);
            else
                myBwViewer.removeColoredRole(c);
        }

    public void mouseEntered(MouseEvent e)
    {
                if (mouseAction)        
                    myTV.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }
    
    public void mouseExited(MouseEvent e) 
    {
        myTV.setCursor(Cursor.getDefaultCursor());
    }
}



