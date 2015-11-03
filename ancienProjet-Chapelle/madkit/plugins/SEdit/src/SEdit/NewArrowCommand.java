/*
* NewArrowCommand.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit;

import java.awt.Point;

public class NewArrowCommand extends Command
{
    String  ad;
    String originID;
    String targetID;
    Point p;
    
    
    public NewArrowCommand(ElementDesc arrowDesc, Point position, SElement from, SElement to)
    {
	this(arrowDesc.getName(), position, from.getID(), to.getID());
    }

    public NewArrowCommand(String arrowDesc, Point position, String from, String to)
    {
	super("newarrow", null, null);
	ad = arrowDesc;
	p = position;
	originID = from;
	targetID = to;
    }
    
    public String getArrowDesc() 
    {
	return ad;
    }

    public Point getPosition() 
    {
	return p;
    }

    public String getOriginID() 
    {
	return originID;
    }
    public String getTargetID() 
    {
	return targetID;
    }
    
    public String toString()
    {
	return super.toString()+" "+ad+" "+originID+"<->"+targetID+" "+p;
    }
}



