/*
* EditBeanShellAgent.java - BeanShellEditor agents, an small editor to evaluate beanshell expressions
* Copyright (C) 1998-2000 Jacques Ferber
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
package madkit.bsh;

import madkit.utils.agents.AbstractEditorAgent;

public class BeanShellEditorAgent extends AbstractEditorAgent {

  // JessEditorPanel display;

   public BeanShellEditorAgent(){
  	super();
    setController(new BeanShellController(this));
  }

  public BeanShellEditorAgent(String f){
  	super();
    setController(new BeanShellController(this,f));
  }


  public void initGUI()
  { BeanShellController c = (BeanShellController) getController();
	//System.out.println("GUI OK1");
    display = new BeanShellEditorPanel(this,c.getInterpret());
	//System.out.println("GUI OK2");
	setGUIObject(display);
	//System.out.println("GUI OK3");
  }


  public void activate(){
    super.activate();
    println("BeanShell output appears in this window");
    println("Type and evaluate your input in the editor above");
  }


}
