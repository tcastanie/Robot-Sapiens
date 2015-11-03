/*
* SElementPropertyFrame.java - SEdit, a tool to design and animate graphs in MadKit
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

import javax.swing.JComponent;

import madkit.kernel.AbstractAgent;
import SEdit.Graphics.GJavaComponent;
import SEdit.Graphics.GObject;
import SEdit.property.PropertyEvent;
import SEdit.property.PropertyEventListener;


public class SElementPropertyFrame extends PropertyFrame implements PropertyEventListener {

    SElement element;
    StructureBean sBean;

    SElementPropertyFrame(StructureBean sb, int n){
        super(n);
        sBean=sb;
    }
    SElementPropertyFrame(StructureBean sb, Object elt, int n, boolean b){
        super(elt,n,b);
        sBean=sb;
    }

    public void updateProperty(PropertyEvent e){
        sBean.getEditor().repaint();
    }

    void editObject(Object elt){

	  GObject go = null;
	  JComponent gc = null;
	  AbstractAgent ag = null;

	  element = (SElement) elt;

	  int j = 0;
	  if ((properties != null) && (properties[0] != null)){
		j = propertiesTabbedPane.getSelectedIndex();
		propertiesTabbedPane.removeAll();
	  }
	  if (element != null)
		go = element.getGObject();
	  if (go != null){
		if (go instanceof GJavaComponent)
			gc = ((GJavaComponent) go).getComponent();
	  }
	  if (element instanceof AbstractAgentNode){
		  ag = ((AbstractAgentNode) element).getAgent();
	  }

	  Vector v = new Vector();
	  v.addElement("id");
	  properties[0] = new PropertyComponent(element,v);
	  properties[1] = new PropertyComponent(go,new Vector());
	  properties[2] = new PropertyComponent(gc,new Vector());
	  properties[3] = new PropertyComponent(ag,new Vector());

	  properties[0].addPropertyEventListener(this);
	  properties[1].addPropertyEventListener(this);
	  properties[2].addPropertyEventListener(this);
	  properties[3].addPropertyEventListener(this);

	  propertiesTabbedPane.add("Element",properties[0]);
	  propertiesTabbedPane.add("Graphics",properties[1]);
	  propertiesTabbedPane.add("Bean",properties[2]);
	  propertiesTabbedPane.add("Agent",properties[3]);
	  propertiesTabbedPane.setSelectedIndex(j);

	  repaint();
  }
}
