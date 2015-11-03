/*
* AgentPanel.java - Graphics utilities for MadKit agents
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
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;

/** A generic GUI that embeds a Java AWT Component */    
public class AgentPanel extends JPanel
{
  public Component inside;

  public AgentPanel(Component i)
  {
    setSize(200,100);
   
    setLayout(new BorderLayout());
    add("Center",i);
  }

  public AgentPanel(Component i, String s)
  {
    setSize(200,100);
    
    setLayout(new BorderLayout());
    add("North",new JLabel(s));
    add("Center",i);
  }

  public synchronized void doLayout() 
  {
    super.doLayout();
  }
  
  
}





