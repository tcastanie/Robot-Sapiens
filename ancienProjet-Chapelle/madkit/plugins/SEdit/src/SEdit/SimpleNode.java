/*
* SimpleNode.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.util.Vector;

//import SEdit.*;

/**************************************************************************

				CLASSE SimpleNode

**************************************************************************/
/** La classe abstraite qui contient tous les noeuds simples,
cad des noeuds qui se connectent directement au arcs.

@version 2.0
@see SNode

**/

public class SimpleNode extends SNode {
    protected Vector inArrows;
    protected Vector outArrows;


    public Vector getInArrows() {return(inArrows);}
    public Vector getOutArrows() {return(outArrows);}

    public SimpleNode()
    {
	super();
    }

    protected boolean isConnectable(SArrow s, boolean asTarget)
    {
	return true;
    }

    public void addInArrow(SArrow a)
    {
    	if (inArrows == null)
    		inArrows=new Vector();
    	inArrows.addElement(a);
    }

    public void addOutArrow(SArrow a)
    {
    	if (outArrows == null)
    		outArrows=new Vector();
    	outArrows.addElement(a);
    }



    /* Récupère tous les liens sortant d'un certain type */

	public Vector getOutArrows(Class c) {
		if (outArrows == null) return(null);
		Vector res = new Vector();
		for(int i=0; i<outArrows.size();i++) {
			SArrow a = (SArrow) outArrows.elementAt(i);
			if (c.isInstance(a))
				res.addElement(a);
		}
		return(res);
	}

	public Vector getInArrows(Class c) {
		if (inArrows == null) return(null);
		Vector res = new Vector();
		for(int i=0; i<inArrows.size();i++) {
			SArrow a = (SArrow) inArrows.elementAt(i);
			if (c.isInstance(a))
				res.addElement(a);
		}
		return(res);
	}



    public void delete()
    {
	super.delete();

	if (inArrows != null) {
	    Vector v1 =  (Vector)inArrows.clone();
	    for(int i=0;i<v1.size();i++)
		((SArrow)v1.elementAt(i)).delete();
	}
    	if (outArrows != null) {
	Vector v2 = (Vector)outArrows.clone();
	    for(int i=0;i<v2.size();i++)
		((SArrow)v2.elementAt(i)).delete();
	}
    }

    public void deleteInArrow(SArrow a) {
	if (inArrows != null)
          inArrows.removeElement(a);
    }
    public void deleteOutArrow(SArrow a) {
	if (outArrows != null)
          outArrows.removeElement(a);
    }

    public String toString(){
      String s=this.getClass().getName()+":"+this.getID();
      if (inArrows!= null) {
        s = s+",inArrows[";
        for (int i=0;i < inArrows.size();i++){
            s = s+((SArrow)inArrows.elementAt(i)).getID()+",";
        }
        s= s+"]";
      }
      if (outArrows!= null) {
        s = s+",outArrows[";
        for (int i=0;i < outArrows.size();i++){
          s = s+((SArrow)outArrows.elementAt(i)).getID()+",";
        }
        s= s+"]";
      }
    return s;
    }
}








