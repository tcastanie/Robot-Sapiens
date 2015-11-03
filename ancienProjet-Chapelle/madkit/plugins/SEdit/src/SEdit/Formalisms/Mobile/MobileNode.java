/*
* MobileNode.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit.Formalisms.Mobile;

import SEdit.*;
import SEdit.Graphics.*;
import SEdit.Formalisms.*;


/***********************************************************

				CLASSE MobileNode

************************************************************/

public class MobileNode extends SimpleNode
{   
	boolean fixed=false;
	double dx=0.0;
	double dy=0.0;
	
	public void setDx(double _dx){dx = _dx;}
	public void setDy(double _dy){dy = _dy;}
	public void addDx(double _dx){dx += _dx;}
	public void addDy(double _dy){dy += _dy;}
	
	public double getDx(){return(dx);}
	public double getDy(){return(dy);}
	
	
	public boolean isFixed(){return(fixed);}
	public void setFixed(boolean f){fixed = f;}
	
}
