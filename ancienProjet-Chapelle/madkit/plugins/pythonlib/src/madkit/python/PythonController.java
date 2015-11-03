/*
* PythonController.java - Python agents: linking MadKit with Jython to describe agents behaviors in Python
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

/**
 * The controller which makes agents controllable by Python scripts
 * @author J. Ferber
 * @date 3/01/2002
 */

import java.io.FileInputStream;
import java.io.IOException;


import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;
import madkit.messages.ControlMessage;
import madkit.utils.agents.LanguageController;

import org.python.core.PyException;
import org.python.util.PythonInterpreter;

public class PythonController extends LanguageController {

    public static final String DATE = "3/01/2002";
    public static final String VERSION = "1.0";


    boolean okForLive=false;
    public void setOkForLive(boolean b){okForLive=b;}
    public boolean getOkForLive(){return okForLive;}


	boolean okForDoIt=false;
    public void setOkForDoIt(boolean b){okForDoIt=b;}
    public boolean getOkForDoIt(){return okForDoIt;}

    PythonInterpreter interp;

    public PythonInterpreter getInterpret(){return interp;}


    public PythonController(AbstractAgent _ag) {
        super(_ag);
		interp = new PythonInterpreter();
    }

    public PythonController(AbstractAgent _ag, String f){
        super(_ag,f);
		interp = new PythonInterpreter();
    }


  public void activate() {

  	// setDebug(true);

    //3.0int bc = createGroup(true,"Python",null,null);
	//System.out.println("Abstract activate: OK1");
	//println("Abstract activate: OK1");
	//System.out.println("Abstract activate: OK1 bis");
	int r = thisAgent.createGroup(false,"python",null,null);

	//println("Abstract activate: OK2");
  	//3.0 int brr = requestRole("Python","member",null);
	thisAgent.requestRole("python","member",null);
  	try {
	    interp.exec("import sys");
		interp.set("self",thisAgent);
                interp.set("myController",this);
		interp.set("me",thisAgent.getAddress());
		interp.exec("def launch(x,s):myController.doLaunchAgent(x,s,1)");
                //interp.exec("def requestRole(g,r,a):myController.doRequestRole(g,r)");
                interp.exec("sys.path.append('scripts/pythonfiles')");
	} catch(PyException e){
		println("Python error while activating");
	}

    println("PythonLib, integration of Python (jython) into Madkit");
    println("Version " + VERSION + " - " + DATE + " - Copyright (C) 2002 MadKit team");
    thisAgent.activate();
    loadFile(getLibFile());
    loadFile(getBehaviorFile());
    //println("behaviorFile: " + getBehaviorFile());
    //println("self: " + interp.get("self"));
    //println("activate: " + interp.get("activate"));
    if (interp.get("activate") != null){
        try {
            interp.exec("activate()");
            }
        catch (PyException re) {
                println("Python error doing " + "activate()");
                re.printStackTrace();
        }
    }
  }


  protected void handleControlMessage(ControlMessage m){
  	String act = m.getAction();
  	println("<< ControlMessage: " + act);
  	try {
  		if (act.equals("dir"))
  			interp.exec("print dir()");
        else if (act.equals("eval")){
  			interp.exec(m.getContent());
			println("OK");
            okForLive=true;
    	} else {
  			super.handleControlMessage(m);
  		}

  	} catch (PyException re) {
        println("Python error doing " + act);
        re.printStackTrace(System.err);
	 }
  }

  protected void handleMessage(Message m){
  	if (m instanceof ControlMessage){
  		handleControlMessage((ControlMessage) m);
  	}
  }

  public void live(){
    //println("live: " + interp.get("live"));
    //println("okForLive: " + okForLive);
    while(true) {
        if ((interp.get("live") != null) && (okForLive==true)){
            try {
                interp.exec("live()");
                break;
            } catch (PyException re) {
                println("Python error doing " + "live()");
                re.printStackTrace(System.err);
                setOkForLive(false);
            }
        } else {
                Message m = ((Agent)thisAgent).waitNextMessage();
                if (m instanceof ControlMessage){
                        handleControlMessage((ControlMessage) m);
                } if (m instanceof StringMessage){
                		interp.exec(((StringMessage) m).getString());
                        thisAgent.sendMessage(m.getSender(),new StringMessage("OK"));
                }
        }
    }
  }

  public void end(){
      if (interp.get("end") != null){
        try {
            interp.exec("end()");
            }
        catch (PyException re) {
                println("Python error doing " + "end()");
                re.printStackTrace(System.err);
        }
    }
    thisAgent.end();
 }

  public void doIt(){
		 if (!okForDoIt && (interp.get("doIt") != null))
			setOkForDoIt(true);
		 if (okForDoIt){
			try{
			    interp.exec("doIt()");
			}
			catch (PyException re) {
                println("Python error doing " + "doIt()");
                re.printStackTrace(System.err);
            }
		 }
  }

  public void loadFile(String f) {
     FileInputStream st=null;
  	 if (f != null){
		 try {
			 st = new FileInputStream(f);
		 }
		 catch(IOException e){
            println("Python file not found " + f);
			return;
		 }
         try {
            interp.execfile(st);
        } catch(PyException e){
            println("Python error while importing " + f);
			e.printStackTrace(System.err);
        }
     }
  }

}
