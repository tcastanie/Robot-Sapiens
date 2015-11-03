/*
* AgentPropertyFrame.java - SEdit, a tool to design and animate graphs in MadKit
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
import SEdit.Graphics.GObject;

public class AgentPropertyFrame extends PropertyFrame {
    AbstractAgent element;

    public AgentPropertyFrame(int n){
        super(n);
    }

    public AgentPropertyFrame(Object elt, int n, boolean b){
        super(elt,n,b);
    }


    public void editObject(Object elt){

	  GObject go = null;
	  JComponent gc = null;
	  AbstractAgent ag = null;
	  element = (AbstractAgent) elt;

	  Vector v = new Vector();
	  v.addElement("id");
	  properties[0] = new PropertyComponent(element,v);

      if ((properties != null) && (properties[0] != null)){
		propertiesTabbedPane.removeAll();
	  }
	  propertiesTabbedPane.add("Agent",properties[0]);

	  repaint();
  }
}
