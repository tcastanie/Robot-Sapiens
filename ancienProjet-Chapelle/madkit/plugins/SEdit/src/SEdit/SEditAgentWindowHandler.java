/*
* SEditAgentWindowHandler.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.event.WindowEvent;

import madkit.kernel.AbstractAgent;
import madkit.kernel.AbstractMadkitBooter;
import madkit.kernel.AgentWindowHandler;
import madkit.kernel.Kernel;

/**
  @author Olivier Gutknecht, J. Ferber
  @version 1.0
  */

public class SEditAgentWindowHandler extends AgentWindowHandler
{

  public SEditAgentWindowHandler(Kernel k, AbstractMadkitBooter b, AbstractAgent ag)
  {
     super(k,b,ag);
  }

  public void windowClosing(WindowEvent we)
  {
        super.windowClosing(we);
    	((SEdit.Booter)theBooter).inspectAgent(null);
   }


    public void windowActivated(WindowEvent e) {
    	super.windowActivated(e);
        ((SEdit.Booter)theBooter).inspectAgent(theAgent);
    }

}






