/*
* LogicalSwitch.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit.Formalisms.Logical;



import java.util.*;

import java.lang.Math;

import java.io.*;



import SEdit.Graphics.*;

import SEdit.*;





/** A simple input component for binary values

 */



public class LogicalSwitch extends SimpleNode

{

    public boolean value=true;

    

    public void activate()

    {

	if (getOutArrows() != null) 

	    for (Enumeration e = getOutArrows().elements(); e.hasMoreElements();)

		((LogicalLink)e.nextElement()).send(value);

    }

	

    public void putZero()

    {

	value=false;

	activate();

    }

    

    public void putOne()

    {

	value = true;

	activate();

    }

    public boolean getValue()

    {

	return value;

    }

    public void setValue(boolean b)

    {

	value=b;

    }   

}

















