/*
* SNode.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.beans.IntrospectionException;
import java.util.Hashtable;
import java.util.Vector;

import SEdit.Graphics.GObject;


/** This class is the model for a node in a structure. It is decoupled
    from the graphical representation

    @version 2.0
    @see SElement
    @see SArrow */

public abstract class SNode extends SElement
{
    // FIXME: should it be changed for in/out ports mechanism ?
    protected abstract  void addOutArrow(SArrow a);
    protected abstract  void addInArrow(SArrow a);

    public abstract Vector getInArrows();
    public abstract Vector getOutArrows();

    public void deleteInArrow(SArrow a) {}
    public void deleteOutArrow(SArrow a) {}


    protected abstract boolean isConnectable(SArrow s, boolean asTarget);
    public void activate(){}


    public void initGraphics(Hashtable properties)
    {
	NodeDesc d = (NodeDesc) getDescriptor();
	try
	    {
		GObject o;

		/*if (d.getIconName() != null)
		 {
		     o = new GIcon();//d.getIconName());
		     o.setSElement(this);
		 }
		else
		    { */
			Utils.debug(d+" "+d.getGraphicClass());
            //Class c = Class.forName(d.getGraphicClass());
			Class c = madkit.kernel.Utils.loadClass(d.getGraphicClass());
			Utils.debug(d+" "+d.getGraphicClass());

			o = (GObject) c.newInstance();
			o.setSElement(this);
			o.setEditor(getStructure().getEditor());
		//    }

		Hashtable descproperties = d.getGraphicProperties();
		if (descproperties!=null)
		    ReflectorUtil.setProperties(o,descproperties);

		if (properties!=null)
		    ReflectorUtil.setProperties(o,properties);
		setGObject(o);
	    }
	catch(ClassNotFoundException e1){
	    System.out.println("Initialisation du graphique - classe invalide :" + d.getGraphicClass());
	    return;
	}
	catch(InstantiationException e2){
	    System.out.println("il a pas aime l'instantiation dis donc! " + d.getGraphicClass());
	    return;
	}
	catch(IntrospectionException e3){
	    System.out.println("il a pas aime l'introspection dis donc! " + d.getGraphicClass());
	    return;
	}
	catch(IllegalAccessException e4){
	    System.out.println("il a pas aime l'acces dis donc! " + d.getGraphicClass());
	    return;
	}
    }

    public void delete()
    {
	super.delete();
	if (structure != null)
	    {
		structure.removeNode(this);
		structure = null;
	    }
    }

	public void reInstall(Structure st){
		   setStructure(st);
		   st.addNode(this);
		   GObject gob = this.getGObject();
		   if ((gob != null) && (st.getEditor()!= null)){
			  gob.reInstall(st.getEditor());
		   }
	}

}



















