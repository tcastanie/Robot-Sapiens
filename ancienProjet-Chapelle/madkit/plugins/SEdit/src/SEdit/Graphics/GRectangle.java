/*
* GRectangle.java - SEdit, a tool to design and animate graphs in MadKit
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
import java.awt.Rectangle;

/**************************************************************************

				CLASSE GRectangle

**************************************************************************/


public class GRectangle extends GNode {
    protected static int rDefaultWidth = 40;
    protected static int rDefaultHeight = 60;

  public GRectangle()
  {
      setDimension(rDefaultWidth,rDefaultHeight);
  }

  public void paint(Graphics g)
    {
	    Color c=g.getColor();
	    g.setColor(Color.white);
	    g.fillRect(x,y,width,height);
	    g.setColor(c);
	    g.drawRect(x,y,width,height);
    }

    public boolean isContainedIn(Rectangle r)
    {
	return r.contains(x+width/2,y+height/2);
    }

  public boolean contains(Point p)
  {
      return (new Rectangle(x,y,width,height)).contains(p);
  }


  public Point intersection(Point p)
  {
      float k,k1,k2;
      k1= Math.abs( (float) (width/2) / (float)(p.x-(x+width/2)) );
      k2= Math.abs( (float) (height/2) / (float)(p.y-(y+height/2)) );
      k=Math.min(k1,k2);

      Point i=new Point( Math.round((x+width/2)+k*(p.x-(x+width/2))) ,
			 Math.round((y+height/2)+k*(p.y-(y+height/2))) );
      return i;
  }
}













