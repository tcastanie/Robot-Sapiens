/*
* Booter.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.event.WindowListener;

import madkit.kernel.AbstractAgent;
import madkit.kernel.AbstractMadkitBooter;
import madkit.kernel.Kernel;

/** The SEdit booter for MadKit.

    It starts the kernel, and then launch agents defined in a config
    file, with or without GUI.
    It is also possible to launch agents from SEdit editors...

  @version 1.0
  @author Jacques Ferber and Olivier Gutknecht */

public class Booter extends AbstractMadkitBooter
{

  static AbstractAgentNode agNode=null;

  public static void setAgentNode(AbstractAgentNode a){
	  agNode = a;
  }

    protected WindowListener createWindowHandler(Kernel k,AbstractAgent a){
        return new SEditAgentWindowHandler(k,this,a);
    }

  private AbstractAgent currentAgent=null;
  AgentPropertyFrame propertyEditor=null;

 public void inspectAgent(AbstractAgent elt){
   if (isGraphics){
        currentAgent=elt;
        if ((propertyEditor == null) && (elt == null)) return;
		if (propertyEditor != null)
		  propertyEditor.editObject(elt);
		else
		  propertyEditor = new AgentPropertyFrame(elt,1,false);
    }
 }

 public void showPropertyEditor(boolean b){
      if (isGraphics){
		if (propertyEditor == null){
			if (b)
			  propertyEditor = new AgentPropertyFrame(null,0,true);
		} else
		  if (b){
			  propertyEditor.show();
		  } else
			  propertyEditor.hide();
      }
	}


  protected Booter(boolean isg, boolean ipnumeric, String initFile, String ipaddress, boolean network)
  {
      super(isg,ipnumeric,initFile,ipaddress,network);
      //System.out.println("classpath: " + System.getProperty("java.class.path"));
   }


    /**
     * Implements the GraphicShell interface
     */
        /// pour le SEdit booter
    public void setupGUI(AbstractAgent a){
	  if (agNode == null){
        String lab = a.getName();
        super.setupGUI(a);
	  } else {
		  agNode.setupGUI(a);
		  agNode = null;
	  }
    }



 /** Booting from command line */
  static public void main(String argv[])
    {
        bootProcess(argv);
        if (initFile != null) {
              Booter boot = new Booter(graphics, ipnumeric, initFile, ipaddress,network);
        } else {
            System.out.println("Sorry config file not found... bye");
        }
    }
}


