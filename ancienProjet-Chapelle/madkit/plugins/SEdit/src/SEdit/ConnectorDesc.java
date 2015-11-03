/*
* ConnectorDesc.java - SEdit, a tool to design and animate graphs in MadKit
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


public class ConnectorDesc extends NodeDesc // test
{
    int mode = ERR;
    
    public static final int ERR = 0;
    public static final int IN  = 1;
    public static final int OUT = 2;
    
    public void setMode(int m)
    {
	mode = m;
    }
    
    public int getMode()
    {
	return mode;	
    }
    
    public ConnectorDesc(String name, String desc, String classname, String type)
    {
	super(name, desc, classname);
	if (type.toLowerCase().equals("in"))
	    {
		mode = IN;
		setGraphicClass("SEdit.Graphics.GInConnect");//InConnect");	
		if ((classname == null) || classname.equals(""))
		    setElementClass("SEdit.SInConnect");
	    }
       	if (type.toLowerCase().equals("out"))
	    {
		mode = OUT;
		setGraphicClass("SEdit.Graphics.GOutConnect");//utConnect");	
		if ((classname == null) || classname.equals(""))
		    setElementClass("SEdit.SOutConnect");
	    }
    }    		
    public String toString() 
    {   	
	return super.toString()+"|,"+mode;
    }
}
