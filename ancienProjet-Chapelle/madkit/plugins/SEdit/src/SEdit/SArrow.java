/*
* SArrow.java - SEdit, a tool to design and animate graphs in MadKit
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

import SEdit.Graphics.GObject;

/*****************************************************************

            CLASSE SArrow

/*****************************************************************/

/** The arrow element definition. It is decoupled from the actual graphical representation
    @version 2.0
*/
public class SArrow extends SElement
{
    protected SNode origin, target;

    protected SArrow()
    {
	super();
    }

    public SNode getOrigin() {return origin;}
    public SNode getTarget() {return target;}
    public void setOrigin(SNode o) {origin = o;}
    public void setTarget(SNode o) {target = o;}


    /*    public void initGraphics()
	  {
	  GArrow g = new GArrow();
	  g.setEndingForm(GArrow.SHARPEND);
	  setGObject(g);
	}*/
    public void delete(){
       if (target != null) target.deleteInArrow(this);
       if (origin != null) origin.deleteOutArrow(this);
       Structure struct = this.getStructure();
       struct.removeArrow(this);
       super.delete();
    }

    public void initGraphics(Hashtable properties)
    {
	//	initGraphics();
	//	GArrow g = new GArrow();
	//setGObject(g);

	ArrowDesc d = (ArrowDesc) getDescriptor();
	try
	    {
		GObject o;

		if (true)
		    {
			Utils.debug(d+"--GraphicObject: "+d.getGraphicClass());
            //Class c = Class.forName(d.getGraphicClass());
			Class c = madkit.kernel.Utils.loadClass(d.getGraphicClass());
			Utils.debug(d+" "+d.getGraphicClass());

			o = (GObject) c.newInstance();
			o.setSElement(this);
			o.setEditor(getStructure().getEditor());
		    }

		Hashtable descproperties = d.getGraphicProperties();
		if (descproperties!=null)
		    ReflectorUtil.setProperties(o,descproperties);

		if (properties!=null)
		    ReflectorUtil.setProperties(o,properties);
		setGObject(o);
	    }
	catch(ClassNotFoundException e1){
	    System.out.println("Initialisation du graphique - classe invalide :");
	    return;
	}
	catch(InstantiationException e2){
	    System.out.println("il a pas aime l'instantiation dis donc! " + d.getGraphicClass());
	    return;
	}
	catch(IntrospectionException e1){
	    System.out.println("Initialisation du graphique - classe invalide :");
	    return;
	}
	catch(IllegalAccessException e4){
	    System.out.println("il a pas aime l'acces dis donc! " + d.getGraphicClass());
	    return;
	}
    }

    /** Check if the arrow itself is o.k. for connection */
    public boolean isConnectable(SNode from, SNode to)
    {
        if (from == null){
            System.err.println("ERROR: isConnectable: origin SNode is null");
            return false;
        }
        if (to == null){
            System.err.println("ERROR: isConnectable: target SNode is null");
            return false;
        }
	return ((ArrowDesc)getDescriptor()).isConnectable(from.getDescriptor(), to.getDescriptor());
    }

    public String toString(){
      String s=this.getClass().getName()+":"+this.getID();
      if (target != null) s=s+",target="+target.getID();
      if (origin != null) s=s+",origin="+origin.getID();
      return s;
    }
}












