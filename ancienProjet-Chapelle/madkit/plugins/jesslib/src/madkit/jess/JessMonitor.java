/*
* JessMonitor.java - JessEditor agents, a simple editor to evaluate Jess rules
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

import java.util.Vector;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Utils;
import madkit.messages.ControlMessage;


public class JessMonitor extends Agent {

  public static final String VERSION="0.8";
  public static final String DATE="15/05/2001";

  AgentAddress recipient=null;

  String targetGroup;
  String targetRole;

  public void setRecipient(AgentAddress rec){
  		recipient = rec;
  		display.setRecipientLabel(rec.toString());
  }

  public AgentAddress getRecipient(){return recipient;}

  JessMonitorPanel display;


  public void initGUI()
  {
    display = new JessMonitorPanel(this);
	setGUIObject(display);
  }

  public void println(String s){
  	display.println(s);
  }

  public void activate(){
    int r = createGroup(false,"Jess",null,null);
  	requestRole("Jess","monitor",null);

    println("JessMonitor - version " + VERSION + " - " + DATE);
    println("Author: J. Ferber - (C) MadKit Team 2000");
  }

  public void live(){
  	while (true) {
  		waitNextMessage();
  	}
  }


  public void sendControlMessage(String act, String content){
  	 	if (recipient != null){
  	 		println(">> " + act + " : " + content);
  	 		sendMessage(recipient,new ControlMessage(act, content));
  	 	}
  }

  public void sendControlMessage(String act){
  	 	if (recipient != null){
  	 		println(">> " + act);
  	 		sendMessage(recipient,new ControlMessage(act));
  	 	}
  }

  void launch(String fName){
  	Agent jag = new JessAgent(fName);
  	launchAgent(jag,Utils.getFileNameFromPath(fName),true);
  	setRecipient(jag.getAddress());
  }

    public void setTargetGroup(String group){
  		targetGroup = group;
  		// ((EditorPanel)display).showCurrentGroup(group);
  }

    public void setTargetRole(String role){
  		targetRole = role;
  		// ((EditorPanel)display).showCurrentRole(role);
  }


  public void setTarget(String group, String role){
  	if (group == null) return;
  	targetGroup = group;
  	targetRole = role;
  	// ((EditorPanel)display).showCurrentGroup(group);
  	// ((EditorPanel)display).showCurrentRole(role);
  }

  public Vector askGetGroups(){
  	return(getGroups());
  }

  public String[] askGetRoles(String theGroup){
  	return(getRoles(theGroup));
  }

}
