/*
* GOutConnect.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit.Graphics;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;

/**************************************************************************

				CLASSE GConnect

**************************************************************************/

/** classe abstraite correspondant à la représentation graphique
 des connecteurs des composants (et des modules)
 @see SConnect
 @see GInConnect
 @see GOutConnect
*/

public class GOutConnect extends GConnect {


    //	public GOutConnect(){}

    //	public GOutConnect(SConnect o){super(o);}

    public void paint(Graphics g) 
    {
	super.paint(g);
	Polygon p=new Polygon();
	switch( dir ){
       	case TOP :
	    { 
                p.addPoint(x+width,y+height);
		p.addPoint(x,y+height);
		p.addPoint(x,y+height/2);
		p.addPoint(x+width/2,y);
		p.addPoint(x+width,y+height/2);
            }
            break;
	    
	case RIGHT :
            {
		p.addPoint(x,y);
		p.addPoint(x+width/2,y);
		p.addPoint(x+width,y+height/2);
		p.addPoint(x+width/2,y+height);
                p.addPoint(x,y+height);
            }
            break;

	case BOTTOM :
            {
		p.addPoint(x,y);
                p.addPoint(x,y+height/2);
		p.addPoint(x+width/2,y+height);
		p.addPoint(x+width,y+height/2);
		p.addPoint(x+width,y);
            }
            break;
	    
	case LEFT :
            {  
		p.addPoint(x+width,y);
		p.addPoint(x+width/2,y);
		p.addPoint(x,y+height/2);
		p.addPoint(x+width/2,y+height);
                p.addPoint(x+width,y+height);
	    }
            break;
        }
        Color c = g.getColor();
        g.setColor(fillColor);
        g.fillPolygon(p);
        g.setColor(c);
        g.drawPolygon(p);
    }
}






