/*
* GOval.java - SEdit, a tool to design and animate graphs in MadKit
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
import java.awt.Point;

/**************************************************************************

				CLASS GOval

**************************************************************************/


public class GOval extends GNode {
  protected static int rDefault=40;   //Diameter

  public GOval()
  {
      setDimension(rDefault,rDefault);
  }

  public void paint(Graphics g)
	{
	    Color c=g.getColor();
	    g.setColor(Color.white);
	    g.fillOval(x,y,width,height);
	    g.setColor(c);
	    g.drawOval(x,y,width,height);
	}


  public boolean contains(Point p)
  {
    double nx = (p.x - x) / (double)width - 0.5;
    double ny = (p.y - y) / (double)height - 0.5;
    return (nx * nx + ny * ny) < 0.25;
  }


  public Point intersection(Point p)
  {
      float k1 = ((float) width/2) /
	  (float) Math.sqrt((x+ width/2.0-p.x) * (x+ width/2.0-p.x) +
			    (y+height/2.0-p.y) * (y+height/2.0-p.y));

      float k2 = ((float) height/2) /
	  (float) Math.sqrt((x+ width/2.0-p.x) * (x+ width/2.0-p.x) +
			    (y+height/2.0-p.y) * (y+height/2.0-p.y));

      if ((k2 == Float.POSITIVE_INFINITY) || (k1 == Float.POSITIVE_INFINITY))
	  return getCenter();

      Point   i =new Point( (int)Math.round(x+ width/2.0 + k1*(p.x-(x+ width/2.0))),
			    (int)Math.round(y+height/2.0 + k2*(p.y-(y+height/2.0))));
      return i;
  }
}













