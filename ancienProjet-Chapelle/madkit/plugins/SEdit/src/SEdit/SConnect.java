/*
* SConnect.java - SEdit, a tool to design and animate graphs in MadKit
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

/**************************************************************************

				CLASSE SConnect

**************************************************************************/

/** la classe abstraite qui correspond à la notion de connecteur
    dans un composant.

    Tous les connecteurs doivent nécessairement dériver de la classe SConnect

    **/

public abstract class SConnect extends SNode {

    protected Vector arrows=new Vector(); // contient les liens avec d'autres elts.
    protected SComponent component;	// contient le lien avec le component
    protected ConnectorPlacement connectorPlacement;	// contient le lien avec le connectorPlacement

    protected String name_on_module; // EXPERIMENTAL


    protected void setComponent(SComponent _comp)
    {  component = _comp; }
    public SComponent getComponent() {return(component);}

    void setConnectorPlacement(ConnectorPlacement _comp)
    {  connectorPlacement = _comp; }
    public ConnectorPlacement getConnectorPlacement() {return(connectorPlacement);}


    public void deleteInArrow(SArrow a) {
		    arrows.removeElement(a);
	}
    public void deleteOutArrow(SArrow a) {
		    arrows.removeElement(a);
    }

    public void delete() {

	super.delete();
	component.removeConnector(this);
	if (arrows != null) {
	    for(int i=0;i<arrows.size();i++)
		((SArrow)arrows.elementAt(i)).delete();
	}
	if (((ComponentDesc)(component.getDescriptor())).getModule()==ComponentDesc.MODULE_FIXED)
	    if (!component.beingDeleted)
		component.delete();
    }

   public void initGraphics(Hashtable properties)
    {
	ConnectorDesc d = (ConnectorDesc) getDescriptor();
	try
	    {
		GObject o;

		//		Utils.debug("IG:"+d.getGraphicClass()+"/"+d);
        // Class c = Class.forName(d.getGraphicClass());
		Class c = madkit.kernel.Utils.loadClass(d.getGraphicClass());
		//	Utils.debug(d+" "+d.getGraphicClass());

		o = (GObject) c.newInstance();

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


    // deux methodes a redefinir dans SOutConnect et SInConnect
    protected void addToComponent(){}
    protected void removeFromComponent()
    {
    }


}

