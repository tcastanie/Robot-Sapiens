/*
* JessController.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
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
package madkit.jess;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import jess.Funcall;
import jess.Jesp;
import jess.JessException;
import jess.Rete;
import jess.Userpackage;
import jess.Value;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Controller;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;
import madkit.kernel.Utils;
import madkit.messages.ControlMessage;
import madkit.utils.agents.LanguageController;

/**
 * JessController: the controller of agents whose behavior is defined in Jess
 * (c) 2001 - Madkit Team
 * @author J. Ferber
 * @version 1.1
 */

public class JessController extends LanguageController implements Controller{


    Jesp jesp;
    Rete rete;

    static final String JESS_LIBFILE="plugins/jesslib/scripts/madkitlib.clp";
    public JessController(AbstractAgent _ag) {
        this(_ag,null);
    }


    public JessController(AbstractAgent _ag, String f){
        super(_ag);

        rete = new Rete();
        setLibFile(System.getProperty("madkit.dir")+File.separator+JESS_LIBFILE);
        if (f != null){
          String path = new File(f).getAbsolutePath();
          setBehaviorFile(path);
        }
    }

    public static final String DATE = "31/03/2002";
    public static final String VERSION = "1.1";


    public Rete getRete(){return rete;}


  /** 	Variable qui contient les types de messages pour
  		faire le lien entre Madkit et Jess
  */
  Hashtable messageTypes = new Hashtable();

  public String getMessageType(String x){
  		return (String) messageTypes.get(x);
  }

  public void setMessageType(String x, String y){
  		messageTypes.put(x,y);
  }


  boolean activation;

  public void setActivate(boolean b){
  	if (b == false) {
        try {
            println("halting");
            rete.halt();
        } catch(JessException e){
            System.err.println("Error in halting Jess with agent: " + this.thisAgent);
        }
  	}
  }

  public boolean getActivate() {return true;}


  public void activate() {
  	// setDebug(true);

    int b = thisAgent.createGroup(true,"Jess",null,null);
  	thisAgent.requestRole("Jess","member",null);
  	println("Jess, the Java Expert System Shell");
    println("Copyright (C) 1998 E.J. Friedman-Hill and the Sandia Corporation");

    println("JessAgentLib, integration of Jess into Madkit");
    println("Version " + VERSION + " - " + DATE + " - Copyright (C) 2000-2002 MadKit team");

  	execute();
    thisAgent.activate();
  }


  public void println(String s){thisAgent.println(s);}

  private void execute()
  {
    // rete = new Rete();

    // Load in optional packages, but don't fail if any are missing.
    String [] packages = { "jess.StringFunctions",
                           "jess.PredFunctions",
                           "jess.MultiFunctions",
                           "jess.MiscFunctions",
                           "jess.MathFunctions",
                           "jess.BagFunctions",
                           "jess.reflect.ReflectFunctions",
                           "jess.view.ViewFunctions" };

    for (int i=0; i< packages.length; i++)
      {
        try
          {
            rete.addUserpackage((Userpackage)Utils.loadClass(packages[i]).newInstance());
                                //  Class.forName(packages[i]).newInstance());
          }
        catch (Throwable t) { /* Optional package not present */ }
      }

    try
     {

	    rete.executeCommand("(printout t (jess-version-string) crlf)");

	    // Madkit AbstractAgent functions
		rete.addUserfunction(new madkit.jess.CreateGroup(this));
		rete.addUserfunction(new madkit.jess.RequestRole(this));
		rete.addUserfunction(new madkit.jess.LeaveRole(this));
		rete.addUserfunction(new madkit.jess.LeaveGroup(this));
		rete.addUserfunction(new madkit.jess.GetAgentsWithRole(this));
		rete.addUserfunction(new madkit.jess.GetAgentWithRole(this));
		//rete.addUserfunction(new madkit.models.jess.GetName(this));
		rete.addUserfunction(new madkit.jess.GetRoles(this));
		rete.addUserfunction(new madkit.jess.GetMyGroups(this));
		rete.addUserfunction(new madkit.jess.GetExistingGroups(this));
		rete.addUserfunction(new madkit.jess.GetAvailableCommunities(this));
		rete.addUserfunction(new madkit.jess.IsGroup(this));
		rete.addUserfunction(new madkit.jess.IsRole(this));
		rete.addUserfunction(new madkit.jess.SendMessage(this));
		rete.addUserfunction(new madkit.jess.BroadcastMessage(this));
		rete.addUserfunction(new madkit.jess.LaunchAgent(this));
		//rete.addUserfunction(new madkit.models.jess.GetAddress(this));

		// added functions
		rete.addUserfunction(new madkit.jess.Me(this));
		rete.addUserfunction(new madkit.jess.MyController(this));
		rete.addUserfunction(new madkit.jess.ReadMessages(this));
		rete.addUserfunction(new madkit.jess.Pause(this));


        // rete.executeCommand("(batch " + getLoadFile() + ")");
        loadFile(getLibFile());
        loadFile(getBehaviorFile());

      }
    catch (JessException re)
      {
        println("ERROR DURING SETUP: " + re.toString());
      }

  }

  public synchronized void halt(){
    try {
  			rete.halt();
  			println(":: Jess engine halted");
  		}
     catch (JessException re) {
        println("Jess error while halting ");
	 }
  }

  protected void handleControlMessage(ControlMessage m){
  	String act = m.getAction();
  	Value v=Funcall.TRUE;
  	if (getWatchControlMessages())
        println("<< ControlMessage: " + act);
  	try {
  		if (act.equals("halt")){
  			rete.halt();
  			println(":: Jess engine halted");
  		//	display.runEnable(true);
  		}
  		else if (act.equals("run")){
  			// display.runEnable(false);
  			v= rete.executeCommand("(run)");
  			// display.runEnable(true);
  		} else if (act.equals("reset"))
  			v= rete.executeCommand("(reset)");
  		else if (act.equals("facts"))
  			v= rete.executeCommand("(facts)");
  		else if (act.equals("rules"))
  			v= rete.executeCommand("(rules)");
  		else if (act.equals("watch")){
  			v= rete.executeCommand("(watch "+m.getContent()+")");
  		} else if (act.equals("unwatch")){
  			v= rete.executeCommand("(unwatch "+m.getContent()+")");
  		} else if (act.equals("eval"))
  			v = rete.executeCommand(m.getContent());
        else if (act.equals("watchOutMessages"))
  			toggleWatchOutMessages();
        else if (act.equals("watchInMessages"))
  			toggleWatchInMessages();
        else if (act.equals("watchRestartEngine"))
  			toggleWatchRestartEngine();
        else if (act.equals("watchControlMessages"))
  			toggleWatchControlMessages();
  		else if (act.equals("clear"))
  			v= rete.executeCommand("(clear)");
  		else if (act.equals("reinit")){
  			v= rete.executeCommand("(clear)");
        	loadFile(getLibFile());
        	loadFile(getBehaviorFile());
  		}
  		else if (act.equals("load")){
  			loadFile(m.getContent());
    	} else {
  			println("** Error: command unknown " + act);
  			return;
  		}
  	    println("> " + v.toString());
  	} catch (JessException re) {
        println("Jess error doing " + act);
        re.printStackTrace(System.err);
	 }
  }

  protected boolean handleMyMessages(Message m){
  	String s=null;
  	if (m instanceof ControlMessage){
  		handleControlMessage((ControlMessage) m);
  		return(false);
  	}
  	else {
  		try {
  			boolean restart = false;
  			if (!(rete.listActivations().hasNext()))
  				restart = true;
  			rete.store("MESSAGE", m);
  			if (getWatchInMessages())
                println("<< receiving message of type : " + m.getClass().getName());
  			rete.executeCommand("(definstance " + getMessageType(m.getClass().getName()) + "(fetch MESSAGE) static)");
  			// println(":: message well received: " + restart);
  			return(true); // the engine message should be restarted.. Handled in the live function
  		} catch (JessException re) {
        	println("ERROR ON MESSAGE RECEPTION: " + re.toString());
        	return(false);
    	}
      }
  }

  public void live(){
    Agent ag = (Agent) thisAgent;
	while(true) {
		Message m = ag.waitNextMessage();
	    if (handleMyMessages(m)){
	    	try {
                if (getWatchRestartEngine())
  			  	    println(":: restart engine");
  				// display.runEnable(false);
  				rete.run();
  				// display.runEnable(true);
  			}
  			catch (JessException re) {
        		println("Run error???");
        		re.printStackTrace(System.err);
    		}
    	}
    }
  }

  /**
   * The doIt() activation method for synchronous agents
   */
  public void doIt(){
        boolean cont = false;
		Message m = null;
		while ((m = thisAgent.nextMessage())!= null)
		  cont = handleMyMessages(m);
		if (cont){
			try {
				println(":: restart engine");
				// display.runEnable(false);
				rete.run();
				// display.runEnable(true);

			}
			catch (JessException re) {
				println("Run error??? see the output to get the stack trace");
				re.printStackTrace(System.err);
			}
		}
  }


  public void readMessages(){
  	 while (!thisAgent.isMessageBoxEmpty())
  	 	handleMyMessages(thisAgent.nextMessage());
  }

  public void doPause(int n){
    if (thisAgent instanceof Agent)
  	    ((Agent)thisAgent).pause(n);
  }

  // interface functions between Jess and Madkit...
  public int doRequestRole(String group, String role){return thisAgent.requestRole(group,role,null);}
  public int doRequestRole(String comm, String group, String role){return thisAgent.requestRole(comm,group,role,null);}
  public void doLeaveRole(String group, String role){thisAgent.leaveRole(group,role);}
  public void doLeaveRole(String comm,String group, String role){thisAgent.leaveRole(comm,group,role);}
  public void doLeaveGroup(String group){thisAgent.leaveGroup(group);}
  public void doLeaveGroup(String comm,String group){thisAgent.leaveGroup(comm,group);}


  public AgentAddress[] doGetAgentsWithRole(String group, String role){
  		AgentAddress[] aglst = thisAgent.getAgentsWithRole(group,role);
  		return aglst;
  }
  public AgentAddress[] doGetAgentsWithRole(String comm,String group, String role){
  		AgentAddress[] aglst = thisAgent.getAgentsWithRole(comm,group,role);
  		return aglst;
  }

  public AgentAddress doGetAgentWithRole(String group, String role){
  		return(thisAgent.getAgentWithRole(group,role));
  }
  public AgentAddress doGetAgentWithRole(String comm, String group, String role){
  		return(thisAgent.getAgentWithRole(comm,group,role));
  }


  public String doGetName(){return(thisAgent.getName());}
  public String[] doGetRoles(String group){return(thisAgent.getRoles(group));}

  public String[] doGetMyGroups(){return(thisAgent.getMyGroups());}
  public String[] doGetMyGroups(String comm){return(thisAgent.getMyGroups(comm));}
  public String[] doGetExistingGroups(){return(thisAgent.getExistingGroups());}
  public String[] doGetExistingGroups(String comm){return(thisAgent.getExistingGroups(comm));}

  public String[] doGetAvailableCommunities(){return(thisAgent.getAvailableCommunities());}

  public boolean doIsGroup(String group){return(thisAgent.isGroup(group));}
  public boolean doIsGroup(String comm,String group){return(thisAgent.isGroup(comm,group));}
  public boolean doIsRole(String group, String role){return(thisAgent.isRole(group,role));}
  public boolean doIsRole(String comm,String group,String role){return(thisAgent.isRole(comm,group,role));}

  public int doCreateGroup(boolean dist, String group){return thisAgent.createGroup(dist,group,null,null);}
  public int doCreateGroup(boolean dist, String comm, String group){return thisAgent.createGroup(dist,comm,group,null,null);}

  public void doLaunchAgent(AbstractAgent agent, java.lang.String name, boolean gui){
  	thisAgent.launchAgent(agent, name, gui);
  }

  public  void doSendStringMessage(AgentAddress a, String m){
  		if (getWatchOutMessages())
            println(">> sending string message : "+ m + " to " + a);
  		thisAgent.sendMessage(a,new StringMessage(m));
  }

  public  void doSendMessage(AgentAddress a, Message m){
  		if (getWatchOutMessages())
  		    println(">> sending message : " + m + " to " + a);
  		thisAgent.sendMessage(a,m);
  		// println(":: message " + m + " sent to " + a);
  }

  public void doSendControlMessage(String act){
  		thisAgent.sendMessage(thisAgent.getAddress(),new ControlMessage(act));
  }

  public void doSendControlMessage(String act, String cont){
  		thisAgent.sendMessage(thisAgent.getAddress(),new ControlMessage(act,cont));
  }

  public  void doSendMessage(java.lang.String g, java.lang.String r, Message m){
  		if (getWatchOutMessages())
  		    println(">> sending messages : " + m + " to role " + r + " of " + g);
  		thisAgent.sendMessage(g,r,m);
  }

  public void doBroadcastMessage(String g, String r, Message m) {
  		if (getWatchOutMessages())
  		    println(">> broadcast messages : " + m + " to role " + r + " of " + g);
  		thisAgent.broadcastMessage(g,r,m);
  }

  public void doBroadcastMessage(String c, String g, String r, Message m) {
  		if (getWatchOutMessages())
  		    println(">> broadcast messages : " + m + " to role " + r + " of " + g + " from community " + c);
  		thisAgent.broadcastMessage(c,g,r,m);
  }

  public void loadFile(String f) {
  	 if (f != null){
		try {
            //println(">> loading : " + f);
       		FileReader fis = new FileReader(f);
  			// Create a parser for the file, telling it where to take input
  			// from and which engine to send the results to
  			Jesp j = new Jesp(fis, rete);
  				try      {
        			// parse and execute one construct, without printing a prompt
        			j.parse(false);
        		}
        		catch (JessException re)      {
        		// All Jess errors are reported as 'ReteException's.
        			re.printStackTrace(System.err);
        		}
    	   } catch(FileNotFoundException ex){
  				println("Error opening file " + f);
    	   } catch(IOException ex){
  				println("Error reading file " + f);
    	   }
	 }
  }
}
