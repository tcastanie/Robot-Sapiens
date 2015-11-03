/*
* NewNodeCommand.java - SEdit, a tool to design and animate graphs in MadKit
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

public class NewNodeCommand extends Command
{
    String nd;
    Point p;

    public NewNodeCommand(NodeDesc nodeDesc, Point position)
    {
	this(nodeDesc.getName(), position);
    }
    
    public NewNodeCommand(String nodeDesc, Point position)
    {
	super("newnode", null, null);
	nd = nodeDesc;
	p = position;
    }
    
    public String getNodeDesc() 
    {
	return nd;
    }

    public Point getPosition() 
    {
	return p;
    }
    
    public String toString()
    {
	return super.toString()+" "+nd+" "+p;
    }
}
