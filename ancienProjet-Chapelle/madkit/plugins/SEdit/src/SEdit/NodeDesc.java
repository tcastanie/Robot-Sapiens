/*
* NodeDesc.java - SEdit, a tool to design and animate graphs in MadKit
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


public class NodeDesc extends ElementDesc
{
    public String iconName; // name of icon to load
    /** FIXME: delete this line public String[] iconSet; // set of icons (if it is a GMultiIcon) */
    public String getIconName(){return iconName;}
    public void setIconName(String i){iconName=i;}
    /** FIXME: delete this line if it compiles    public String[] getIconSet() {return iconSet;} */

    public NodeDesc(String name, String desc, String classname)
    {
	super(name, desc, classname, null);
	setGraphicClass("SEdit.Graphics.GOval");

	if ((classname == null) || classname.equals(""))
		setElementClass("SEdit.SimpleNode");
    }
    public String toString()
    {
	return super.toString()+"|,"+iconName;
    }

	public boolean isStandardDefaultValue(String name, String value,int type){
	  if ((type == GRAPHICPROPERTIES) &&
		  ( (name.equalsIgnoreCase("resizable") &&
		   (value.equalsIgnoreCase("false"))) )
		  )
		return true;
	  else return super.isStandardDefaultValue(name, value, type);
    }

}





