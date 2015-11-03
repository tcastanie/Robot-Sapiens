/*
* GNode.java - SEdit, a tool to design and animate graphs in MadKit
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

import SEdit.SComponent;
import SEdit.SElement;

/*****************************************************************

            CLASSE GNode

/*****************************************************************/

public class GNode extends GObject {

	final public int HANDLE_SIZE=8;
	protected int selectedHandle=0;
	final int NO_HANDLE=0;
	final int LEFT_TOP_HANDLE=1;
	final int RIGHT_TOP_HANDLE=2;
	final int LEFT_BOTTOM_HANDLE=3;
	final int RIGHT_BOTTOM_HANDLE=4;
	final int LEFT_HANDLE=5;
	final int TOP_HANDLE=6;
	final int RIGHT_HANDLE=7;
	final int BOTTOM_HANDLE=8;
    protected boolean resizable=false;

	public int getSelectedHandle(){
		return selectedHandle;
	}
	/**
    * Get the value of resizable.
    * @return Value of resizable.
    */
    public boolean getResizable() {
		return resizable;
    }

	/**
    * set the value of resizable.
    * @param b Boolean value to assign to resizable.
    */
	public void setResizable(boolean b){
		resizable = b;
	}

    public GNode() {
    }

	public void select(boolean state){
	    super.select(state);
	    selectedHandle = NO_HANDLE;
    }

  	public void doReSelect(int _x, int _y){
		selectedHandle = getHandleNum(_x,_y);
	}

	public void doDrag(int dx, int dy){
			// System.out.println(":: drag, handle: " + selectedHandle);

        	//paint(g);
			switch (selectedHandle){
				case NO_HANDLE : x+=dx; y+=dy; break;
				case LEFT_TOP_HANDLE : x = x+dx; y = y+dy; height = height-dy; width = width-dx; break;
				case RIGHT_TOP_HANDLE : y = y+dy; height = height-dy; width = width+dx; break;
				case LEFT_BOTTOM_HANDLE : x = x+dx; height = height+dy;
									      width = width-dx;break;
				case RIGHT_BOTTOM_HANDLE : height = height+dy; width = width+ dx; break;
				case LEFT_HANDLE : x = x+dx; width = width-dx; break;
				case RIGHT_HANDLE : width = width+dx; break;
				case TOP_HANDLE : y = y+dy; height = height-dy; break;
				case BOTTOM_HANDLE : height = height+dy; break;
				default : break;
			}
			SElement o = getSElement();
			if (o instanceof SComponent){
			    ((SComponent) o).computeConnectorsPositions();
			}
        	//paint(g);
	}

    public boolean isContainedIn(Rectangle r)
    {
	return r.contains(x+width/2,y+height/2);
    }

    public boolean contains(Point p){
      return (new Rectangle(x,y,width,height)).contains(p);
   }


    public Point intersection(Point p){
      float k,k1,k2;
      k1= Math.abs( (float) (width/2) / (float)(p.x-(x+width/2)) );
      k2= Math.abs( (float) (height/2) / (float)(p.y-(y+height/2)) );
      k=Math.min(k1,k2);

      Point i=new Point( Math.round((x+width/2)+k*(p.x-(x+width/2))) ,
			 Math.round((y+height/2)+k*(p.y-(y+height/2))) );
      return i;
    }

	/**
	 * Displays the handles when the graphic object
	 * is selected and is resizable.
	 */
	public void displayHandles(Graphics g){
		int s = HANDLE_SIZE;
		int l = width;
		int h = height;
		if (getResizable()){
			g.setColor(Color.blue);
			g.fillRect(x,y,s,s);		// left top
			g.fillRect(x+l-s,y,s,s);	// right top
			g.fillRect(x,y+h-s,s,s);	// left bottom
			g.fillRect(x+l-s,y+h-s,s,s);// right bottom

			g.fillRect(x,y+(h-s)/2,s,s); // gauche
			g.fillRect(x+(l-s)/2,y,s,s); // haut
			g.fillRect(x+l-s,y+(h-s)/2,s,s); // droite
			g.fillRect(x+(l-s)/2,y+h-s,s,s); // bas
		}
	}

	int getHandleNum(int _x, int _y){
		int s = HANDLE_SIZE;
		int l = width;
		int h = height;
		if ((_x >= x) && (_y >= y) && (_x <= x+s) && (_y <= y+s)) return LEFT_TOP_HANDLE;
		else if ((_x >= x+l-s) && (_y >= y) && (_x <= x+l) && (_y <= y+s)) return RIGHT_TOP_HANDLE;
		else if ((_x >= x) && (_y >= y+h-s) && (_x <= x+s) && (_y <= y+h)) return LEFT_BOTTOM_HANDLE;
		else if ((_x >= x+l-s) && (_y >= y+h-s) && (_x <= x+l) && (_y <= y+h)) return RIGHT_BOTTOM_HANDLE;

		else if ((_x >= x) && (_y >= y+(h-s)/2) && (_x <= x+s) && (_y <= y+s+(h-s)/2)) return LEFT_HANDLE;
		else if ((_x >= x+(l-s)/2) && (_y >= y) && (_x <= x+s+(l-s)/2) && (_y <= y+s)) return TOP_HANDLE;
		else if ((_x >= x+l-s) && (_y >= y+(h-s)/2) && (_x <= x+l) && (_y <= y+s+(h-s)/2)) return RIGHT_HANDLE;
		else if ((_x >= x+(l-s)/2) && (_y >= y+h-s) && (_x <= x+s+(l-s)/2) && (_y <= y+h)) return BOTTOM_HANDLE;
		else return NO_HANDLE;
	}
}
