/*
* EditorAgent.java -a NotePad agent, to edit text and send string messages to other agents
* Copyright (C) 1998-2002 Jacques Ferber
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
package jsynedit;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import madkit.kernel.*;
import madkit.utils.agents.AbstractEditorAgent;
import madkit.messages.ControlMessage;

import java.io.File;


/**
  *	An agent that implements a simple editor and which is able to
  * send the content of its buffer to other agents using 'StringMessages'
  * messages.
  *

  @author Jacques FERBER
  @date 26/03/2000
  @version 1.0

*/

public class JSynEditAgent extends AbstractEditorAgent 
{

  String targetGroup;
  String targetRole;
    Hashtable orgdump;
 String fileToEdit=null;
 EditorPanel display;

 boolean showInMessages=true;
 void showInMessages(boolean b){ showInMessages=b;}

 boolean showOutMessages=true;
 void showOutMessages(boolean b){ showOutMessages=b;}

  AgentAddress mykernel;

  public JSynEditAgent(){ super();}
  public JSynEditAgent(String s){ super(); fileToEdit=s;}

  public void initGUI()
  {
    display = new EditorPanel(this);
	setGUIObject(display);
    if (fileToEdit != null){
 //       display.setCurrentFile(fileToEdit);
        display.readFile(fileToEdit);
    }
  }


  public void enterGroupRole(String comm, String group,String role, String passwd){
  		((EditorPanel)display).print(":: Request role " + role + " in " + group);
  		int r = requestRole(comm,group,role,passwd);
                if (r == 1)
  		    ((EditorPanel)display).println(" with success");
                else
  		    ((EditorPanel)display).println(" and failed");

  }

  public void setTargetGroup(String group){
  		targetGroup = group;
  		((EditorPanel)display).showCurrentGroup(group);
  }

    public void setTargetRole(String role){
  		targetRole = role;
  		((EditorPanel)display).showCurrentRole(role);
  }


  public void setTarget(String group, String role){
  	if (group == null) return;
  	targetGroup = group;
  	targetRole = role;
  	((EditorPanel)display).showCurrentGroup(group);
  	((EditorPanel)display).showCurrentRole(role);
  }


  public void leaveGR(String group, String role){
  	if (group == null) return;
  	leaveRole(group,role);
	  }


  public void activate() {
  	createGroup(false,"system",null,null);
  	requestRole("system","editor",null);
    mykernel = getAgentWithRole("system","kernel");
    if (mykernel == null)
    	System.err.println("ERROR : invalid kernel agent: " + mykernel);
    sendMessage(mykernel,new KernelMessage(KernelMessage.INVOKE,Kernel.DUMP_ORGANIZATION));

  }

  public void handleMessage(Message m){
  	String s=null;
  	if (m instanceof StringMessage){
  		s=((StringMessage)m).getString();
                if (showInMessages){
  		  ((EditorPanel)display).println("<< receiving message from "+m.getSender()+">> \n" + s);
                } else
                  ((EditorPanel)display).print(s);
  	}
  	else if (m instanceof KernelMessage){
  		Hashtable org = null;
  		KernelMessage me = (KernelMessage) m;
  		// reply from DUMP_ORG
		if (me.getType() == KernelMessage.REPLY) {
    		org = (Hashtable) me.getArgument();
		orgdump = org;//joinGroupInteractively(org);

		}
  	} else if (m instanceof ControlMessage){
  		ControlMessage msg=(ControlMessage) m;
  		if ("edit".equalsIgnoreCase(msg.getAction()))
  			display.readFile(msg.getContent());
  	}
  }

  protected void requestJoinGroup(){
	sendMessage(mykernel,new KernelMessage(KernelMessage.INVOKE,Kernel.DUMP_ORGANIZATION));
  }

    protected Vector getAllGroups()
    {
	Vector groups=new Vector();
	for (Enumeration e=orgdump.keys(); e.hasMoreElements();)
	    groups.addElement((String)e.nextElement());
	groups.removeElement("system");
	 return groups;
    }

  /*  // is not used anymore..
    protected Vector getAllRoles(String g)
    {
	Hashtable group= (Hashtable)orgdump.get(g);
	Vector roles=new Vector();
	if (group!=null)
	    for (Enumeration e=group.keys(); e.hasMoreElements();)
		roles.addElement((String)e.nextElement());
	return roles;
    } */

  protected void joinGroupInteractively(Hashtable org){
  	Vector groups=new Vector();
	for (Enumeration e=org.keys(); e.hasMoreElements();)
	    groups.addElement((String)e.nextElement());

   	((EditorPanel)display).joinGroup(groups);
  }

  public String[] askGetGroups(){
  	return(getExistingGroups());
  }

  public String[] askGetRoles(String theGroup){
  	return(getExistingRoles(theGroup));
  }

  public String[] askGetOwnRoles(String theGroup){
  	return(getExistingRoles(theGroup));
  }

  protected void clearRole(){
    if ((targetRole != null) && (targetGroup != null))
    	leaveRole(targetGroup,targetRole);
  	targetRole = null;
  	((EditorPanel)display).showCurrentRole("none");
  }


  public void live(){
	while(true) {
		Message m = waitNextMessage();
	    handleMessage(m);
	}
  }

  public void sendString(String s){
        if (showOutMessages)
  		  ((EditorPanel)display).println(">> " + s);
  	if (getRecipient() != null)
  		sendMessage(getRecipient(), new StringMessage(s));
  	if ((targetGroup != null) && (targetRole != null)){
  		AgentAddress[] agList = getAgentsWithRole(targetGroup,targetRole);
  		for(int i=0; i< agList.length;i++){
  			if (!agList[i].equals(getAddress()))
  				sendMessage(agList[i], new StringMessage(s));
  		}
  	}

  }
  
  public void createAgentFromScript(String path){
	   Agent a=null;
	   try {
		   if (path.endsWith(".scm")){
			AbstractMadkitBooter.getBooter().makeScriptAgent(this,"madkit.scheme.SchemeAgent","java.io.File",new File(path), true);
		   }else if (path.endsWith(".clp"))
			   AbstractMadkitBooter.getBooter().makeScriptAgent(this,"madkit.jess.JessAgent","java.lang.String",path, true);
		   else if (path.endsWith(".py"))
			   AbstractMadkitBooter.getBooter().makeScriptAgent(this,"madkit.python.PythonAgent","java.lang.String",path, true);
		   else if (path.endsWith(".bsh"))
			   AbstractMadkitBooter.getBooter().makeScriptAgent(this,"madkit.bsh.BeanShellAgent","java.lang.String",path, true);
	   } catch(Exception e){
		   System.err.println("Error: not a kwown Script file : " + path);
	   }
   }


}
