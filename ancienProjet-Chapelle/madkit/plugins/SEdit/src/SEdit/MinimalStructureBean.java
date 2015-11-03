/*
* MinimalStructureBean.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.Dimension;
import java.awt.SystemColor;

import javax.swing.JScrollPane;

/**
 *  A minimal structure bean class which does not
 *  contain any command nor menu items. May be used
 *  to encapsulate agents of any kind...
 *  More specific structure beans may be inherited from this class.
 */
public class MinimalStructureBean extends StructureBean {
   public JScrollPane scrollPane;

    public MinimalStructureBean(StructureAgent a)
    {
        super(a);
	    setBackground(SystemColor.control);

        editor = new StructureEditor(myAgent);

       scrollPane = new JScrollPane(22,32);
       scrollPane.setPreferredSize(new Dimension(600,300));

       scrollPane.getViewport().add(editor);
       getContentPane().add("Center",scrollPane);

       editor.setGraphics(scrollPane.getViewport().getGraphics());
	   editor.setStructureBean(this);

       setLocation(200,100);

       this.setVisible(true);
    }
}
