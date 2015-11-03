/*
* PythonAgent.java - Python agents: linking MadKit with Jython to describe agents behaviors in Python
* Copyright (C) 2000-2002  Jacques Ferber
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
package madkit.python;


/*************************************************************************
 *
 * An agent to be controlled by Python scripts.
 * @author J. Ferber
 * @date last revision: 17/04/2002
 *
 ********************************************************************** */

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.PrintWriter;

import javax.swing.JToolBar;

import madkit.kernel.Agent;
import madkit.kernel.OPanel;
import madkit.system.EditorAgent;
import madkit.utils.graphics.DefaultControlPanel;

class PythonAgentGUI extends DefaultControlPanel implements ActionListener{

    OPanel outPanel;


    PythonAgentGUI(PythonAgent _ag){
        super(_ag);
        setLayout(new BorderLayout());
        JToolBar mytoolBar = new JToolBar();
        mytoolBar.addSeparator();
    //			/addTool(toolBar, "print", "demo/agents/system/print.gif");
        //addButton(mytoolBar, "dir", "Run","agents/system/print.gif");
        //addButton(mytoolBar, "halt", "Halt", "/agents/jess/halt.gif");
        //addButton(mytoolBar, "reinit", "Re-init","/agents/jess/reinit.gif");
        addButton(mytoolBar, "notePad", "Edit script in NotePadAgent","/images/agents/agentEditor24.gif");
        addButton(mytoolBar, "jEdit", "Edit script with jEdit","/images/toolbars/jedit24.gif");

        add(mytoolBar,BorderLayout.NORTH);

        outPanel = new OPanel();
        add(outPanel,BorderLayout.CENTER);
    }

    OPanel getOutPanel(){
        return outPanel;
    }



    public void command(String c){

        PythonController co = (PythonController) ag.getController();
 		if (c.equals("evalBuffer")) evalBuffer();
 		else if (c.equals("evalSelection")) evalSelection();
   		//else if(c.equals("run"))
  	 	//	co.doSendControlMessage("run");
   		//else if(c.equals("reset"))
  	 	//	co.doSendControlMessage("reset");
   		//else if(c.equals("halt")){
        //    co.halt();
   		//}
   		//else if(c.equals("reinit"))
  	 	//	co.doSendControlMessage("reinit");
        else if(c.equals("jEdit"))
  	 		jedit();
        else if (c.equals("notePad")){
            String s = co.getBehaviorFile();
            if (s == null)
                co.println("sorry no behavior file to edit");
            else {
                co.println("editing : " + s);
                EditorAgent ed = new EditorAgent(s);
                ag.launchAgent(ed,"Edit : " + s,true);
            }
        }
       else super.command(c);
 	}

    void evalBuffer() {
   }

   void evalSelection() {
   }

   void println(String s){
        ag.println(s);
   }


}


public class PythonAgent extends Agent {

  PrintWriter out;
  PrintWriter err;


   public PythonAgent(){
  	super();
    setController(new PythonController(this));
    ((PythonController)getController()).setOkForLive(true);
  }

  public PythonAgent(String f){
  	super();
    setController(new PythonController(this,f));
    ((PythonController)getController()).setOkForLive(true);
  }


  public void initGUI()
  {

    PythonAgentGUI o = new PythonAgentGUI(this);
    setGUIObject(o);

    out = new PrintWriter(o.getOutPanel().getOut());
    err = new PrintWriter(o.getOutPanel().getOut());
  }

  public void println(String s){
    if (out != null)
  	out.println(s);
     else
        super.println(s);
  }



}
