/*
* GSegmentedArrow.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import SEdit.SArrow;

/*****************************************************************

            CLASSE GSegmentedArrow

/*****************************************************************/
public class GSegmentedArrow extends GArrow
{
    public final static int HORIZONTAL_HORIZONTAL = 1;
    public final static int HORIZONTAL_VERTICAL = 2;
    public final static int VERTICAL_HORIZONTAL = 3;
    public final static int VERTICAL_VERTICAL = 4;
    public final static int AUTOMATIC = 5;

    // locations of added points
    int x1,y1;
    int x2,y2;

    public GSegmentedArrow(){
      	super();
      	setEndingForm(GArrow.SHARPEND);
      	setLineStyle(HORIZONTAL_HORIZONTAL);
    }

    public GSegmentedArrow(int _x, int _y){
	  this();
	  setLocation(_x,_y);
    }

    public void paint(Graphics g) {
		prepareColor(g);

		GObject origin = ((SArrow)getSElement()).getOrigin().getGObject();
		GObject target = ((SArrow)getSElement()).getTarget().getGObject();

		Point p_orig=origin.getCenter(), p_target=target.getCenter();
		Dimension d_orig = origin.getDimension(), d_target = target.getDimension();


		if (lineStyle == HORIZONTAL_HORIZONTAL) {
			x1 = Math.abs(p_orig.x-p_target.x)/2+Math.min(p_orig.x,p_target.x);
			x = x1;
			y1 = p_target.y;
			y = p_orig.y;
		}

		//Calcule des points d'intersection du lien avec les noeuds ...
		Point p2=new Point(x1,y1);
		q=origin.intersection(getLocation());
		p=target.intersection(p2);

		g.drawLine(q.x,q.y,x,y);
		g.drawLine(x,y,x1,y1);
		g.drawLine(x1,y1,p.x,p.y);

		if ((startingForm == -1) || (endingForm == -1)) {
			startingForm = startingForm;
			endingForm = endingForm;
		}
		drawEnding(g,startingForm,getLocation(),q); //starting form
		drawEnding(g,endingForm,p2,p);   //ending form
	}

}
