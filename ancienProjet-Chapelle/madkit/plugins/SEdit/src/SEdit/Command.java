/*
* Command.java - SEdit, a tool to design and animate graphs in MadKit
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

/** The description of an action that might be requested on a whole
    structure or on a single element (node or arrow). Usually defined
    in the formalism file */

public class Command implements Executable
{
    protected String action;
    protected String desc;
    protected String icon;
        
    public Command() {}

    public Command(String value)
    {
	this(value, null, null);
    }
    
    public Command(String value, String description)
    {
	this(value, description, null);
    }   

    public Command(String value, String description, String iconlocation)
    {
	action = value;
	desc = description;
	icon = iconlocation;
    }    

    public String getAction() 
    {
	return action;
    }
    public String getDescription()
    {
	return desc;
    }
    public String getIcon() 
    {
	return icon;
    }
    
    public boolean hasDescription() 
    {
	return (desc!=null);
    }
    
    public boolean hasIcon() 
    {
	return (icon!=null);
    }

    public String toString()
    {
	return action+" ("+desc+") <"+icon+">";
    }

    public void execute () 
    {
    }    
}

