/*
* GRoundedRectangle.java - SEdit, a tool to design and animate graphs in MadKit
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

/**************************************************************************

				CLASSE GRectangle

**************************************************************************/


public class GRoundedRectangle extends GRectangle {

    int arcWidth = 40;
    int arcHeight = 60;


    /**
       * Get the value of arcHeight.
       * @return Value of arcHeight.
       */
    public int getArcHeight() {return arcHeight;}

    /**
       * Set the value of arcHeight.
       * @param v  Value to assign to arcHeight.
       */
    public void setArcHeight(int  v) {this.arcHeight = v;}


    /**
       * Get the value of arcWidth.
       * @return Value of arcWidth.
       */
    public int getArcWidth() {return arcWidth;}

    /**
       * Set the value of arcWidth.
       * @param v  Value to assign to arcWidth.
       */
    public void setArcWidth(int  v) {this.arcWidth = v;}

  public void paint(Graphics g)
    {
	g.drawRoundRect(x,y,width,height, arcWidth, arcHeight);
    }

}













