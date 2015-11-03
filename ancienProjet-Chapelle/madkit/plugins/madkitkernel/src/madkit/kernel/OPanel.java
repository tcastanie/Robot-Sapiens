/*
* OPanel.java - Graphics utilities for MadKit agents
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
package madkit.kernel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Writer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/** A generic GUI with text output abilities (which can be directly
    mapped with the setOutputWriter(..) call in the AbstractAgent class. 
*/

public class OPanel extends JPanel
{
  public JTextArea outField;
  public JScrollPane jscrollpane;
    
  private Writer out;

  public OPanel()
  {
    outField = new JTextArea(5,32);
    outField.setEditable(false);
    setSize(250,100);

    setLayout(new BorderLayout());

    jscrollpane = new JScrollPane(outField);
    add("Center",jscrollpane);
    
    out = new JTextAreaWriter(outField);
    
    JButton b = new JButton("clear");
    b.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
	        clearOutput();
    	}
});

	add("South",b);

  }

  public OPanel(String s)
  {
    this();      
    add("North",new JLabel(s));
  }

  public Writer getOut()
    {
      return out;
    }

  public synchronized void doLayout() 
    {
      super.doLayout();
    }
  
  public Dimension getPreferredSize() 
    {
      return getSize();
    }
    
  public void clearOutput()
  {
  	outField.setText("");
  }
}