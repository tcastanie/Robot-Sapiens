/*
* JessBrain.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

/**
 *
 */

import java.io.PrintWriter;

import jess.JessException;
import jess.Rete;
import madkit.jess.JessController;
import madkit.kernel.OPanel;


public class JessBrain extends Brain {

  PrintWriter out;
  PrintWriter err;

  PrintWriter getOut(){return out;}
  PrintWriter getErr(){return err;}

  Rete rete;

   public JessBrain(){
  	super();
   }

   public void init(){
	String s = ((BasicBody)getBody()).getBehaviorFileName();
	if (s != null)
       setController(new JessController(this,s));
	else
       setController(new JessController(this));
	rete = ((JessController) getController()).getRete();
  }

  public void initGUI()
  {
    OPanel o = new OPanel();
    this.setGUIObject(o);

    out = new PrintWriter(o.getOut());
    err = new PrintWriter(o.getOut());

    rete.addOutputRouter("t", out);
    rete.addOutputRouter("WSTDOUT", out);
    rete.addOutputRouter("WSTDERR", err);
  }

  public void activate(){
	//	 rete.addUserfunction(new Warbot.Move(this));
	//	 rete.addUserfunction(new Warbot.SetDir(this));
  }

  public void println(String s){
  	if (out != null)
          out.println(s);
        else
          super.println(s);
  }


  public void getPerception(){
	   Percept[] percepts=getPercepts();
	   for(int i=0;i<percepts.length;i++){
	       Percept e=percepts[i];
		   //rete.store("PERCEPT", e);
		   //println(":: perceiving: " + e);
		   try {
			   rete.executeCommand("(assert (percept (type " + e.getPerceptType()+")"
											 + " (x " + e.getX() + ")"
											 + " (y " + e.getY() + ")"
											 + " (energy " + e.getEnergy() + ")"
											 + " (radius " + e.getRadius() + ")"
											 + " (team " + e.getTeam() + ")"
										//	 + " (percept  (fetch PERCEPT))"
											 + " ))");
			   rete.executeCommand("(facts)");
		   } catch (JessException re) {
				println("ERROR ON PERCEPTION: " + re.toString());
			}
	   }
  }

    /// IMPORTANT ACTIONS
  public void doIt(){
		 this.getController().doIt();
  }



}
