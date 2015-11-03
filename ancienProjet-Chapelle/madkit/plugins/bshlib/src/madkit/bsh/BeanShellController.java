/*
* BeanShellController.java - BeanShell agents: linking MadKit with
* BeanShell to describe agents behaviors in BeanShell
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
package madkit.bsh;

/**
 * The controller which makes agents controllable by Python scripts
 * @author J. Ferber
 * @date 3/01/2002
 */

import java.io.FileReader;
import java.io.IOException;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.Message;
import madkit.messages.ControlMessage;
import madkit.utils.agents.LanguageController;
import bsh.EvalError;
import bsh.Interpreter;

public class BeanShellController extends LanguageController {

    public static final String DATE = "16/04/2002";
    public static final String VERSION = "1.0";


    boolean okForLive=false;
    public void setOkForLive(boolean b){okForLive=b;}
    public boolean getOkForLive(){return okForLive;}


	boolean okForDoIt=false;
    public void setOkForDoIt(boolean b){okForDoIt=b;}
    public boolean getOkForDoIt(){return okForDoIt;}

    Interpreter interp;

    public Interpreter getInterpret(){return interp;}


    public BeanShellController(AbstractAgent _ag) {
        super(_ag);
		interp = new Interpreter();
    }

    public BeanShellController(AbstractAgent _ag, String f){
        super(_ag,f);
		interp = new Interpreter();
    }


    boolean methodExist(String meth){
        String[] meths = interp.getNameSpace().getMethodNames();
        for(int i=0;i<meths.length;i++){
            // System.out.println("meths["+i+"]="+meths[i]);
            if (meth.equals(meths[i]))
                return true;
        }
        return false;
    }


  public void activate() {

	int r = thisAgent.createGroup(false,"bsh",null,null);

	//println("Abstract activate: OK2");
  	//3.0 int brr = requestRole("Python","member",null);
	thisAgent.requestRole("bsh","member",null);
  	try {
	    interp.eval("import madkit.kernel.*;");
	    interp.eval("import madkit.messages.*;");
	    interp.eval("import madkit.bsh.*");
	    interp.eval("import madkit.utils.graphics.*;");
	    interp.eval("import madkit.utils.common.*;");
	    interp.eval("import madkit.utils.agents.*;");
	    
            interp.set("self",thisAgent);
            interp.set("myController",this);
            interp.set("me",thisAgent.getAddress());
            interp.eval("void print(s){self.print(s);}");
            interp.eval("void println(s){self.println(s);}");
	} catch(EvalError e){
		println("BeanShell error while activating the agent");
	}

    println("BeanShellLib, integration of BeanShell (the light Java interpreter) into Madkit");
    println("Version " + VERSION + " - " + DATE + " - Copyright (C) 2002 MadKit team");
    thisAgent.activate();
    loadFile(getLibFile());
    loadFile(getBehaviorFile());
    //println("behaviorFile: " + getBehaviorFile());
    //println("self: " + interp.get("self"));
    //println("activate: " + interp.get("activate"));

        try {
        if (methodExist("activate"))
            interp.eval("activate();");
            }
        catch (EvalError re) {
                println("BeanShell error doing " + "activate()");
                re.printStackTrace();
        }
  }


  protected void handleControlMessage(ControlMessage m){
  	String act = m.getAction();
  	println("<< ControlMessage: " + act);
  	try {
  		if (act.equals("dir"))
  			interp.eval("dir()");
        else if (act.equals("eval")){
  			interp.eval(m.getContent());
			println("OK");
            okForLive=true;
    	} else {
  			super.handleControlMessage(m);
  		}

  	} catch (EvalError re) {
        println("BeanShell error doing " + act);
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
            try {
                if (methodExist("live") && (okForLive==true)){
					exitImmediatlyOnKill(); //to be sure the agent thread can be really stopped
                    interp.eval("live()");
                    break;
                } else {
                    Message m = ((Agent)thisAgent).waitNextMessage();
                    if (m instanceof ControlMessage){
                        handleControlMessage((ControlMessage) m);
			        }
		        }
            } catch (EvalError re) {
                println("BeanShell error doing " + "live()");
                re.printStackTrace(System.err);
                setOkForLive(false);
            }
        }
  }

  public void end(){
        try {
            if (methodExist("end"))
                interp.eval("end()");
            }
        catch (EvalError re) {
                println("BeanShell error doing " + "end()");
                re.printStackTrace(System.err);
        }
    thisAgent.end();
 }

  public void doIt(){
        try{
             if (!okForDoIt && methodExist("doIt"))
                setOkForDoIt(true);
             if (okForDoIt)
			    interp.eval("doIt()");
        } catch (EvalError re) {
                println("BeanShell error doing " + "doIt()");
                re.printStackTrace(System.err);
		 }
  }

  public void loadFile(String f) {
     FileReader st=null;
  	 if (f != null){
		 try {
			 st = new FileReader(f);
		 }
		 catch(IOException e){
            println("BeanShell file not found " + f);
			return;
		 }
         try {
            interp.eval(st,interp.getNameSpace(),f);
        } catch(EvalError e){
            println("BeanShell error while importing " + f);
			e.printStackTrace(System.err);
        }
     }
  }

}
