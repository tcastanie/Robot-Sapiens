/*
* StructureDialog.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

/**
 *      An editor to show and modify the properties
 *      of the structure and structure editor
 */

public class StructureDialog extends JDialog {

   JTabbedPane propertiesTabbedPane = new JTabbedPane();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel titleLabel = new JLabel();

  PropertyComponent structComp;
  PropertyComponent structEditComp;

  StructureEditor editor;

  public StructureDialog(Frame owner, StructureEditor ed) {
	  super(owner, "Structure properties");
      editor=ed;
      try {
          jbInit();
          edit();
          pack();
          show();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }


  private void jbInit() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    titleLabel.setFont(new java.awt.Font("Serif", 1, 14));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setText("Properties");
    this.getContentPane().add(propertiesTabbedPane, BorderLayout.CENTER);
    //this.getContentPane().add(titleLabel, BorderLayout.NORTH);

	this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
  }

  void edit(){
      Structure struct = editor.getStructure();

      // structure
	  structComp = new PropertyComponent(struct,new Vector());
	  propertiesTabbedPane.add("Structure",structComp);

      // structureEditor
	  Vector v = new Vector();
	  v.addElement("tooltiptext");
	  v.addElement("visible");
	  v.addElement("uiclassid");
	  v.addElement("doublebuffered");
	  v.addElement("opaque");
	  v.addElement("enabled");
	  v.addElement("autoscrolls");
	  v.addElement("alignmenty");
	  v.addElement("alignmentx");
	  v.addElement("name");
	  v.addElement("requestfocusenabled");
	  v.addElement("debuggraphicsoptions");
	  structEditComp = new PropertyComponent(editor,v);

	  propertiesTabbedPane.add("Graphics",structEditComp);

	  repaint();
  }
}
