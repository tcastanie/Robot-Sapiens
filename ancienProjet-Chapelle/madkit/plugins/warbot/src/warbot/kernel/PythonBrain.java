/*
* PythonBrain.java -Warbot: robots battles in MadKit
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
 * The brain which makes robot agents controllable by Python script files
 */

import java.io.PrintWriter;

import madkit.kernel.OPanel;
import madkit.python.PythonController;

public class PythonBrain extends Brain {


  PrintWriter out;
  PrintWriter err;

  PrintWriter getOut(){return out;}
  PrintWriter getErr(){return err;}

   public PythonBrain(){
  	super();
   }

   public void init(){
	String s = ((BasicBody)getBody()).getBehaviorFileName();
	if (s != null)
       setController(new PythonController(this,s));
	else
       setController(new PythonController(this));
  }

  public void initGUI()
  {
    OPanel o = new OPanel();
    setGUIObject(o);

    out = new PrintWriter(o.getOut());
    err = new PrintWriter(o.getOut());

  }

  public void activate(){
        PythonController c = (PythonController) getController();
	if (out != null) {
          c.getInterpret().setOut(out);
	  c.getInterpret().setOut(err);
        }
  }

  public void println(String s){
  	if (out != null)
          out.println(s);
        else
          super.println(s);
  }

  /// IMPORTANT ACTIONS
  public void doIt(){
		 this.getController().doIt();
  }






}
