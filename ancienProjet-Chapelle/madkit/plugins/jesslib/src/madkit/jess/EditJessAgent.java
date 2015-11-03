/*
* EditJessAgent.java - JessEditor agents, a simple editor to evaluate Jess rules
* Copyright (C) 2000-2002 Jacques Ferber
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
package madkit.jess;

import madkit.utils.agents.AbstractEditorAgent;


public class EditJessAgent extends AbstractEditorAgent {

  // JessEditorPanel display; // already defined in AbstractEditorAgent

  static boolean jess_checked=false;

  public EditJessAgent(){
  	this(null);
  }

  public EditJessAgent(String f){
  	super();
        if (!jess_checked){
            try {
                Class cl = madkit.kernel.Utils.loadClass("jess.Rete");
                jess_checked=true;
            } catch (ClassNotFoundException ex){
                  System.err.println("CAUTION: if you want to use Jess with MadKit, you need to install Jess properly.");
                  System.err.println("Please download Jess 6.0, from http://herzberg.ca.sandia.gov/jess/");
                  System.err.println("replace the old jess.jar in the libs/support/ directory");
                  System.err.println("by the new jar that you have downloaded, and call it 'jess.jar'");
                  System.err.println("Restart MadKit and that's it");
                  System.err.println("See the user manual for more details");
                  return;
            } catch (Exception ccex){
                  System.err.println("Error in instanciating a JessAgent"+ccex);
                  // ccex.printStackTrace();
            }
        }
        setController(new JessController(this,f));
  }


  public void initGUI()
  {
      display = new JessEditorPanel(this,((JessController)getController()).getRete());
      setGUIObject(display);
  }


  public void println(String s){
  	display.stdout().println(s);
  }

  public void activate(){
  	super.activate();

    println("Jess output appears in this window");
    println("Type and evaluate your input in the editor above");
  }
}
