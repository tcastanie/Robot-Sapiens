/*
* AbstractEditorAgent.java - a NotePad editor in MadKit
* Copyright (C) 1998-2002  Jacques Ferber
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
package madkit.utils.agents;


import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;


/**
  *	An abstract agent that implements a simple editor. It is used as
  * a starting point for building language agents or EditorAgent.
  *

  @author Jacques FERBER
  @date 30/06/2000
  @version 1.0
  @see agents.system.EditorAgent

*/

public abstract class AbstractEditorAgent extends Agent {

  public AbstractEditorPanel display;

  AgentAddress recipient = null;

  public AgentAddress getRecipient() { return recipient; }
  public void setRecipient(AgentAddress ad) { recipient = ad; }

  public void println(String s){
  	display.stdout().println(s);
  }

  AgentAddress mykernel;

  public AbstractEditorAgent(){
    super();
  }

}
