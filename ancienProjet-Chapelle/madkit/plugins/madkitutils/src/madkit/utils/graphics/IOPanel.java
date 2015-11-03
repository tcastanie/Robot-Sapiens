/*
* IOPanel.java - Graphics utilities for MadKit agents
* Copyright (C) 1998-2002  Olivier Gutknecht
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.utils.graphics;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.Reader;
import java.io.Writer;

import javax.swing.JPanel;
import javax.swing.JTextField;

import madkit.kernel.OPanel;

/** A generic GUI with text input and output abilities. */
public class IOPanel extends JPanel
{
  public JTextField inField;
  public OPanel outField;
  private Reader in;
  
  public IOPanel()
  {
    outField = new OPanel();
    inField = new JTextField();
    setSize(250,150);
    
    setLayout(new BorderLayout());
    add("Center",outField);
    add("South",inField);

    in = new GUIReader(inField);
  }

    /** Define a new Input/Output panel with input strings duplicated in the
	output area */
  public IOPanel(boolean b)
  {
    outField = new OPanel();
    inField = new JTextField();
    setSize(250,150);
    
    setLayout(new BorderLayout());
    add("Center",outField);
    add("South",inField);

    in = new GUIReader(inField, getOut());
  }

  public Reader getIn()
    {
      return in;
    }
  public Writer getOut()
    {
      return outField.getOut();
    }
  public Writer getErr()
    {
      return outField.getOut();
    }

  public synchronized void doLayout() 
  {
    super.doLayout();
  }

  
   public Dimension getPreferredSize() 
  {
    return getSize();
  }
  
}
