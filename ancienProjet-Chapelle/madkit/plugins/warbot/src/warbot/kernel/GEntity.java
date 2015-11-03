/*
* GEntity.java -Warbot: robots battles in MadKit
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

import java.awt.Graphics;

import SEdit.Graphics.GIcon;


public class GEntity extends GIcon {

	 public void paint(Graphics g){
		width=2*((Entity)getSElement()).getRadius();
		height=width;
        //System.out.println("painting.. radius:"+((Entity)getSElement()).getRadius());
	 	super.paint(g);
    }

     public void doDrag(int dx, int dy) {
            super.doDrag(dx,dy);
	        ((Entity)getSElement()).updateCoordinatesFromGObject();
    }

    public void translate(int dx, int dy) {
            super.translate(dx,dy);
	        ((Entity)getSElement()).updateCoordinatesFromGObject();
    }


}
