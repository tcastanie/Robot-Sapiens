/*
* LanguageController.java - Controllers : behavior plugging mechanism into MadKit agents
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
package madkit.utils.agents;

/**
 * Titre :        Madkit 3.0 dev projet
 * Description :  Madkit project
 * (C) 1998-2001 Madkit Team
 * Copyright :    Copyright (c) 2001
 * Société :
 * @author O. Gutknecht, M. Fabien, J. Ferber
 * @version 1.0
 */
import java.io.File;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Controller;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;
import madkit.messages.ControlMessage;

public abstract class LanguageController implements Controller {

    protected AbstractAgent thisAgent;
    public AbstractAgent thisAgent(){return thisAgent;}

    public void end(){ thisAgent.end();}

    public LanguageController(AbstractAgent _ag) {
        thisAgent = _ag;
    }


    public LanguageController(AbstractAgent _ag, String f){
        this(_ag);
        String path = new File(f).getAbsolutePath();
        setBehaviorFile(path);
    }

  String libFile = null;
  String behaviorFile = null;

  public String getLibFile(){ return libFile;}
  public void setLibFile(String s){ libFile = s;}

  public String getBehaviorFile(){ return behaviorFile;}
  public void setBehaviorFile(String s){ behaviorFile = s;}


  private boolean watchOutMessages=true;
  private boolean watchInMessages=true;
  private boolean watchRestartEngine=false;
  private boolean watchControlMessages=false;

  public void setWatchOutMessages(boolean v){watchOutMessages=v;}
  public void setWatchInMessages(boolean v){watchInMessages=v;}
  public void setWatchRestartEngine(boolean v){watchRestartEngine=v;}
  public void setWatchControlMessages(boolean v){watchControlMessages=v;}

  public boolean getWatchOutMessages(){return watchOutMessages;}
  public boolean getWatchInMessages(){return watchInMessages;}
  public boolean getWatchRestartEngine(){return watchRestartEngine;}
  public boolean getWatchControlMessages(){return watchControlMessages;}

  public void toggleWatchOutMessages(){watchOutMessages=!watchOutMessages;}
  public void toggleWatchInMessages(){watchInMessages=!watchInMessages;}
  public void toggleWatchRestartEngine(){watchRestartEngine=!watchRestartEngine;}
  public void toggleWatchControlMessages(){watchControlMessages=!watchControlMessages;}

  public void activate(){
    println("Language controller");
  }


  public void println(String s){thisAgent.println(s);}


  protected void handleControlMessage(ControlMessage m){
  	String act = m.getAction();
  	// to be defined
  }

	//Make the thread crash himself
  final protected void exitImmediatlyOnKill(){
	thisAgent.getMyGroups();
	}
	
  public void live(){
  }

  /**
   * The doIt() activation method for synchronous agents. To be defined
   */
  public void doIt(){
  }


  protected void readMessages(){
  	 while (!thisAgent.isMessageBoxEmpty())
  	 	handleMessage(thisAgent.nextMessage());
  }

  protected void handleMessage(Message m){
  	if (m instanceof ControlMessage){
  		handleControlMessage((ControlMessage) m);
  	}
  }

  public void doPause(int n){
    if (thisAgent instanceof Agent)
  	    ((Agent)thisAgent).pause(n);
  }

  // JF: interface functions between the language and Madkit...
  // has to be finished with all the functions
  // some have been simplified, I am not sure it is better...
 /* public void doRequestRole(String group, String role){thisAgent.requestRole(group,role,null);}
  public void doLeaveRole(String group, String role){thisAgent.leaveRole(group,role);}
  public void doLeaveGroup(String group){thisAgent.leaveGroup(group);}

  public AgentAddress[] doGetAgentsWithRole(String group, String role){
  		AgentAddress[] aglst = thisAgent.getAgentsWithRole(group,role);
  		return aglst;
  }

  public AgentAddress doGetAgentWithRole(String group, String role){
  		return(thisAgent.getAgentWithRole(group,role));
  }
  public String doGetName(){return(thisAgent.getName());}
  public String[] doGetRoles(String group){return(thisAgent.getRoles(group));}

  public Vector doGetGroups(){return(thisAgent.getGroups());}
  public boolean doIsGroup(String group){return(thisAgent.isGroup(group));}
  public int doCreateGroup(boolean dist, String group){return thisAgent.createGroup(dist,group,null,null);}
 */
  public void doLaunchAgent(AbstractAgent agent, java.lang.String name, boolean gui){
  	thisAgent.launchAgent(agent, name, gui);
  }

  ///////////////////////////////////
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

}
