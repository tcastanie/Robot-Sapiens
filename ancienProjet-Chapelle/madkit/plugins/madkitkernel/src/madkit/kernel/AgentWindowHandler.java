/*
* AgentWindowHandler.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
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

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
  @author Olivier Gutknecht, J. Ferber
  @version 1.0
  */

public class AgentWindowHandler extends WindowAdapter
{
  protected Kernel theKernel;
  protected AbstractAgent theAgent;
  protected AbstractMadkitBooter theBooter;

  public AgentWindowHandler(Kernel k, AbstractMadkitBooter b, AbstractAgent ag)
  {
    theKernel = k;
    theAgent = ag;
    theBooter = b;
  }

  /* AGENT WINDOW MANAGEMENT */
  public void windowClosing(WindowEvent we){ 
	    if (theAgent != null)
	        theAgent.windowClosing(we);
	    else
	        we.getWindow().dispose();

   }


    public void windowActivated(WindowEvent e) {
        //theBooter.inspectAgent(theAgent);
    }

    public void windowDeactivated(WindowEvent e) {
    }

}






