/*
* Environment2D.java -Warbot: robots battles in MadKit
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


class Environment2D extends AbstractEnvironment 
{
	private int width;
	private int height;
	private boolean wrapModeOn;


public Environment2D(int theWidth,int theHeight)
{
	width = theWidth;
	height = theHeight;
	wrapModeOn = false;
}
public Environment2D(int theWidth,int theHeight,boolean isWrapModeOn)
{
	width = theWidth;
	height = theHeight;
	wrapModeOn = isWrapModeOn;
}
public Environment2D()
{
	width = 300;
	height = 300;
	wrapModeOn = false;
}

public void setWidth(int  theWidth){width = theWidth;}
public int getWidth(){return width;}
public void setHeight(int  theHeight){height = theHeight;}
public int getHeight(){return height;}
public void setWrapModeOn (boolean isWrapModeOn){wrapModeOn = isWrapModeOn;}
public boolean getWrapModeOn (){return wrapModeOn ;}

public int normalizeX(int x){return normalize(x,width);}
public int normalizeY(int y){return normalize(y,height);}
public int normalize(int x,int size)
{
if (wrapModeOn)
{	
	x %=size;
	if (x < 0) return x+size;
	else return x;
}
else
	if (x>width)
		return size;
	else
		if (x<0) return 0;
		else return x;
}

}
