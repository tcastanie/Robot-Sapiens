/*
* Ellipse.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.Rectangle;


public class Ellipse// implements Shape
{
    public int x;
    public int y;
    public int width;
    public int height;

    public Ellipse()
{    x = 0;
    y = 0;
    width = 0;
    height = 0;
    }

    public Ellipse(int x, int y, int w, int h) {
	x = x;
	y = y;
	width = w;
	height = h;
    }

	public int getX() {
	    return  x;
	}

	public int getY() {
	    return  y;
	}
	public int getWidth() {
	    return  width;
	}
	public int getHeight() {
	    return  height;
	}

public boolean contains(int x, int y) {
    
    if (width <= 0.0) {
	return false;
    }
    double nx = (x - getX()) / width - 0.5;
    double height = getHeight();
    if (height <= 0.0) {
	return false;
    }
    double ny = (y - getY()) / height - 0.5;
    return (nx * nx + ny * ny) < 0.25;
}


public Rectangle getBounds()
{
    return new Rectangle(x,y,width,height);
}

}








