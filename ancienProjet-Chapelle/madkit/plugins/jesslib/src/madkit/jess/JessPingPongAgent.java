/*
* JessPingPongAgent.java - JessEditor agents, a simple editor to evaluate Jess rules
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



public class JessPingPongAgent extends JessAgent {


  public JessPingPongAgent(){
  	super();
  }

  public JessPingPongAgent(String f){
  	super(f);
  }

  public void activate(){
  	super.activate();
  	String f="";

  	String s = System.getProperty("madkit.dir",null);
  	// println("user dir = " + s);
    f = "plugins/jesslib/scripts/jessfiles/madkit standard/PingPong.clp";
    //f = f.replace('/',File.separatorChar);
  	((JessController) getController()).loadFile(f);
  }

}
