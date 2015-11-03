/*
* PropertyFrame.java - the MadKit Desktop application
* Copyright (C) 2000-2002  Jacques Ferber, Olivier Gutknecht
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
package madkit.system.property;

//import SEdit.Graphics.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.border.*;
import madkit.kernel.AbstractAgent;
//import madkit.platform.desktop.property.*;

public abstract class PropertyFrame extends JFrame  {
  JTabbedPane propertiesTabbedPane = new JTabbedPane();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel titleLabel = new JLabel();

  PropertyComponent[] properties;

  public PropertyFrame(int n) {
	  super("Properties editor");
    try {
      properties=new PropertyComponent[n];
      init();
	  show();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public PropertyFrame(Object elt, int n, boolean b) {
	  super("Properties editor");
    try {
      properties=new PropertyComponent[n];
      init();
	  editObject(elt);
	  pack();
	  if (b) show();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void init() throws Exception {
    this.getContentPane().setLayout(borderLayout1);
    titleLabel.setFont(new java.awt.Font("Serif", 1, 14));
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
    titleLabel.setText("Properties");
    this.getContentPane().add(propertiesTabbedPane, BorderLayout.CENTER);
    //this.getContentPane().add(titleLabel, BorderLayout.NORTH);

	this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
  }

  abstract void editObject(Object elt);
}
