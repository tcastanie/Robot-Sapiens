/*
* ArrowDesc.java - SEdit, a tool to design and animate graphs in MadKit
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


public class ArrowDesc extends ElementDesc
{
    /** la classe d'origine de la fleche. Par defaut, c'est la classe
	SNode qui represente n'importe quel noeud */
    String originClass="SEdit.SNode";
    /** la classe de destination de la fleche. Par dŽfaut, c'est la classe
	SNode qui represente n'importe quel noeud */
    String targetClass="SEdit.SNode";

    NodeDesc originDesc, targetDesc;


    public ArrowDesc(String name, String desc, String classname, String image)
    {
	this(name, desc, classname, image, null, null);
    }

    public ArrowDesc(String name, String desc, String classname, String image, String from, String to)
    {
	super(name, desc, classname, image);
	setGraphicClass("SEdit.Graphics.GArrow");
	if (classname == null)
	    setElementClass("SEdit.SArrow");
	else
	    if (classname.equals(""))
		setElementClass("SEdit.SArrow");
	if (from!=null)
	    if (from.length()>0)
		originClass = from;
	if (to!=null)
	    if (to.length()>0)
		targetClass = to;
    }

    public ArrowDesc(String name, String desc, String classname, String image, String from, String to,
		     NodeDesc fd, NodeDesc ft) {
		this(name,desc,classname,image, from, to);
		originDesc=fd;
		targetDesc=ft;
    }

    public boolean isConnectable(ElementDesc from, ElementDesc to){
	    if (originDesc==null){
			if (targetDesc==null)
			  return true;
			else
			  return (to == targetDesc);
		} else {
			if (targetDesc==null)
			  return (from == originDesc);
			else
			  return ((from == originDesc) && (to == targetDesc));
		}
    }

	public boolean isStandardDefaultValue(String name, String value,int type){
	  if ((type == GRAPHICPROPERTIES) &&
		  ( (name.equalsIgnoreCase("lineStyle") &&
		   (value.equalsIgnoreCase("2"))) ||
		   (name.equalsIgnoreCase("startingForm") &&
		   (value.equalsIgnoreCase("0"))) ||
		   (name.equalsIgnoreCase("endingForm") &&
		   (value.equalsIgnoreCase("1")))  ||
		   (name.equalsIgnoreCase("width") &&
		   (value.equalsIgnoreCase("0"))) ||
		   (name.equalsIgnoreCase("height") &&
		   (value.equalsIgnoreCase("0"))) )
		  )
		return true;
	  else return super.isStandardDefaultValue(name, value, type);
    }

    public String toString() {
    	return (super.toString()+","+originClass+"<-arrowdesc->"+targetClass);
    }
}


