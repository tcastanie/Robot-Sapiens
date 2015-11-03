/*
* GHexa.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.Graphics;
import java.awt.Polygon;

/**************************************************************************

				CLASSE GRectangle

**************************************************************************/

/** classe abstraite correspondant à la représentation graphique des éléments
de la structure.

@see SElement
*/

public class GHexa extends GNode {

    public GHexa() {
    height=28;
    width=40;
  }

    public void paint(Graphics g){
	//    	prepareDisplay();
        Polygon p=new Polygon();
	p.addPoint(x+width/4,y);
        p.addPoint(x+3*width/4,y);
        p.addPoint(x+width,y+height/2);
        p.addPoint(x+3*width/4,y+height);
        p.addPoint(x+width/4,y+height);
        p.addPoint(x,y+height/2);
        g.drawPolygon(p);
    }
}





