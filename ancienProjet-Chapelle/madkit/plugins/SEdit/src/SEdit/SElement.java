/*
* SElement.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.util.Hashtable;

import SEdit.Graphics.GObject;


/** SElement is the abstract class for structure elements.

    The ID is the essential identification field in structure
    elements. ID *must* be unique in a given structure, and it is
    strongly advised that they are *globally* unique too. The current
    ID scheme keep a local naming principle, but this is likely to
    change in the future (switch to AgentAddress)

    @version 2.0
*/

public abstract class SElement extends Object
{
    protected Structure structure;
    public void setStructure(Structure _structure){structure = _structure;}
    public Structure getStructure()
    {
	return structure;
    }

    public abstract void initGraphics(Hashtable properties);

    public void delete()
    { GObject go=getGObject();
	  if (go != null)
		go.delete();
    }

    protected String id;

    /** Set the element ID. Warning: the element ID must be treated as immutable anyway. */
    public void setID(String value){id = value;}
    public String getID(){return(id);}

    /** The element label (mutable) */
    protected String label = null;
    public void setLabel(String value){label = value;}
    public String getLabel(){return(label);}

    /** The element comment (mutable) */
    protected String comment = "";
    public void setComment(String value){comment = value;}
    public String getComment(){return(comment);}

    protected ElementDesc descriptor;
    public void setDescriptor(ElementDesc d){descriptor = d;}
    /** Access to an element descriptor */
    public ElementDesc getDescriptor(){return(descriptor);}

    protected GObject element;
    public void setGObject(GObject value){element = value; value.setSElement(this);}
    public GObject getGObject(){return(element);}

	public boolean persistent=true;

	/** Initialization method called at the end of the creation
	of an element. By default do nothing */
	public void init(){;}


    public void dump()
    {
	System.err.println("Dump Information "+toString());
    }

    public void inspect()
    {
        StructureBean bean=(StructureBean) getStructure().getAgent().getGUIObject();
	    if (bean!=null){
           bean.inspectElement(this);
        }
    }


    public SElement() { }
}




