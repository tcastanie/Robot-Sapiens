/*
* AbstractAgent.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
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
package madkit.kernel;

import java.awt.AWTEvent;
import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

/**
 * The main MadKit AbstractAgent class. It provides support for agent's
 * <ul>
 * <li> Lifecycle
 * <li> Group and roles management
 * <li> Messaging
 * <li> Graphical interface
 * <li> Agent informations
 * </ul>
 *<p>
 * The agent's behavior is <i>intentionnaly not defined</i>. It is up to the
 * agent developer to choose an agent model or to develop his specific
 * agent library on top of the facilities provided by MadKit. However,
 * all agent share the same organizational view, and the basic
 * messaging code, so integration of agent coming from different
 * developer and having heterogeneous models is quite easy.
 *<p>
 * Agent-related methods (almost everything here) can be invoked only
 * after registration in the kernel (i.e. after the <code>activate</code> method has been invoked
 * on this agent). That means that you should not use any of the agent methods in constructor
 *
 * @author Olivier Gutknecht
 * @author Fabien Michel
 * @version 3.4	4/02/2002
 */

public class AbstractAgent extends Object implements Serializable
{
	transient private Object currentBean;
	transient private Writer ostream;

	private boolean bean_mode = false;
	private boolean debugFlag = false;
	private AgentInformation agentInformation = null;

	transient Kernel currentKernel = null;

	/*MessageBox */java.util.Vector messageBox;

	private Controller controller=null;

/** Default no-args constructor, which just set-up internal structures */
public AbstractAgent()
{
	messageBox = new java.util.Vector();//MessageBox();
}

/** Return the current controller of the agent if there is one */
public Controller getController(){return controller;}
/** Assign a controller which will be in charge of the control of the agent's behavior*/
public void setController(Controller c){controller = c;}



///////////////////////////////////////////////////////////////////////		BASIC COMMANDS
/** This method is initially called when the micro-kernel registers the agent.
Usually a good place to set up some initial groups and roles. */
public void activate() {}

/** This method is called by the agent micro-kernel at the end of the agent lifecycle.
If the the agent is killed from the outside, it is the last opportunity
for the agent to cleanly shutdown its operations.*/
public void end() {}



/////////////////////////////////////////////////////////// GROUP & ROLE METHODS	AGR
/** Creates a new Group.
If operation succeed, the agent will automaticaly handle two roles: <i>member</i> and <i>group manager</i>.

	@param distributed if <code>true</code> the new group will be distributed when multiple MadKit kernels are connected
	@param communityName the community within the group will be created. If this community does not exist it will be created.
	@param groupName the name of the new group
	@param description can be null (thus the description will be the name of the group)
	@param theIdentifier an object that implements the <code>GroupIdentifer Interface</code>. When null, there is no group access control
	@return <code> 1 </code>if operation succeed; <code> -1 </code> if the group already exists.
	@see GroupIdentifier
	@since MadKit 3.0
*/
public int createGroup(boolean distributed, String communityName, String groupName, String description, GroupIdentifier theIdentifier)
{
	if (description == null)
       		return currentKernel.createGroup(getAddress(), distributed, communityName, groupName, groupName, theIdentifier);
       	return currentKernel.createGroup(getAddress(), distributed, communityName, groupName, description, theIdentifier);
}
/** Creates a new Group within the default community <i>public</i>*/
public int createGroup(boolean distributed, String groupName, String description, GroupIdentifier theIdentifier)
{
	return createGroup(distributed, Kernel.DEFAULT_COMMUNITY, groupName, description, theIdentifier);
}

/** Request a role within a group.

	@param communityName the group's community.
	@param groupName the desired group.
	@param roleName the desired role.
	@param memberCard the passKey to enter a group. If needed, it is generally delivered by the group's <i>group manager</i> to nice agents :)
	It can be <code> null </code> when the desired group has no security (i.e. was created using <code> null </code> for <i> theIdentifier </i> parameter).
	@return <code> 1 </code>: operation success; <code> -1 </code>: access denied; <code> -2 </code>: the role is already handled by this agent; <code> -3 </code>: the group does not exist; <code> -4 </code>: the community does not exist.
	@since MadKit 3.0
*/
public int requestRole(String communityName, String groupName, String roleName, Object memberCard)
{
	return currentKernel.requestRole(getAddress(), communityName, groupName, roleName, memberCard);
}
/** Request a role within a group of the default community*/
public int requestRole(String groupName, String roleName, Object memberCard)
{
	return currentKernel.requestRole(getAddress(), Kernel.DEFAULT_COMMUNITY, groupName, roleName, memberCard);
}

/** Abandon an handled role within a group.
	@return <code> true </code> if operation is done; false otherwise.
	@since MadKit 3.0
*/
public boolean leaveRole(String communityName, String groupName, String roleName)
{
	return currentKernel.leaveRole(getAddress(), communityName, groupName, roleName);
}
public boolean leaveRole(String groupName, String roleName)
{
	return currentKernel.leaveRole(getAddress(), Kernel.DEFAULT_COMMUNITY, groupName, roleName);
}

/** Make the agent leave the group.

	@return <code> true </code> if operation is done; false otherwise.
	@since MadKit 3.0
*/
public boolean leaveGroup(String communityName, String groupName)
{
	return currentKernel.leaveGroup(getAddress(), communityName, groupName);
}
public boolean leaveGroup(String groupName)
{
	return currentKernel.leaveGroup(getAddress(), Kernel.DEFAULT_COMMUNITY, groupName);
}



///////////////////////////////////////////////////////////// DEPRECATED METHODS

/** @deprecated As of MadKit 3.0. replaced by {@link #createGroup(boolean,String,String,String,GroupIdentifier)}.
	<p>
	This call is now equivalent to <code> createGroup(true, groupName, null, null)</code>
	if the group does not exist or <code> requestRole(groupName, "member", null)</code> otherwise.
*/
public void joinGroup(String groupName)
{
	if(currentKernel.isGroup(Kernel.DEFAULT_COMMUNITY, groupName))
		requestRole(Kernel.DEFAULT_COMMUNITY, groupName,"member",null);
	else
		createGroup(true, Kernel.DEFAULT_COMMUNITY, groupName, null, null);
}
/** @deprecated As of MadKit 3.0. replaced by {@link #requestRole(String,String,Object)}.
	<p>
	This call is now equivalent to <code> requestRole(groupName, roleName, null)</code>.
*/
public void requestRole(String groupName, String roleName){currentKernel.requestRole(getAddress(),Kernel.DEFAULT_COMMUNITY, groupName, roleName, null);}
/** @deprecated As of MadKit 3.0. replaced by {@link #createGroup(boolean,String,String,String,GroupIdentifier)}.
	<p>
	This call is now equivalent to <code>createGroup(true, groupName, null, null)</code>.
*/
public void foundGroup(String groupName){currentKernel.createGroup(getAddress(), true, Kernel.DEFAULT_COMMUNITY, groupName, groupName, null);}



//////////////////////////////////////////////////////////	ORGANIZATION INFORMATIONS

/** Gets the addresses of all agents (including this agent if present) that handle this role within this group*/
public AgentAddress[] getAgentsWithRole(String communityName, String groupName, String roleName)
{
	return currentKernel.getRolePlayers(communityName, groupName, roleName);
}
public AgentAddress[] getAgentsWithRole(String groupName, String roleName)
{
	return currentKernel.getRolePlayers(Kernel.DEFAULT_COMMUNITY, groupName, roleName);
}

/** Gets an agent that handle a given role within a group, <b><i>chosen randomly</i></b>
      @param groupName group name
      @param roleName role name
      @return the agent address.
*/
public AgentAddress getAgentWithRole(String communityName, String groupName, String roleName)
{
	return currentKernel.getRolePlayer(communityName, groupName, roleName);
}
public AgentAddress getAgentWithRole(String groupName, String roleName)
{
	return currentKernel.getRolePlayer(Kernel.DEFAULT_COMMUNITY, groupName, roleName);
}

/** Gets the name of the groups the agent joined in this community.
	@since MadKit 3.0
	@param communityName  a string holding a community name
*/
public String[] getMyGroups(String communityName)
{
	return currentKernel.getCurrentGroupsOf(getAddress(), communityName);
}

/**
 * @return an array of group names
 */
public String[] getMyGroups()
{
	return currentKernel.getCurrentGroupsOf(getAddress(), Kernel.DEFAULT_COMMUNITY);
}

/** return the names of the groups that exist in this community*/
public String[] getExistingGroups(String communityName)
{
	return currentKernel.getExistingGroups(communityName);
}

/** return the names of the groups that exist within the default community*/
public String[] getExistingGroups()
{
	return currentKernel.getExistingGroups(Kernel.DEFAULT_COMMUNITY);
}

/** Gets roles currently handled within a group of this community*/
public String[] getMyRoles(String communityName, String groupName)
{
	return currentKernel.getGroupRolesOf(getAddress(), communityName, groupName);
}
/** Gets roles currently handled within a group of the default community*/
public String[] getMyRoles(String groupName)
{
	return currentKernel.getGroupRolesOf(getAddress(), Kernel.DEFAULT_COMMUNITY, groupName);
}

/** return the names of the roles that exist in this (community,group) couple*/
public String[] getExistingRoles(String communityName, String groupName)
{
	return currentKernel.getExistingRoles(communityName, groupName);
}

/** return the names of the roles that exist within this group in the default community*/
public String[] getExistingRoles(String groupName)
{
	return currentKernel.getExistingRoles(Kernel.DEFAULT_COMMUNITY, groupName);
}

/** @deprecated As of MadKit 3.0. replaced by {@link #getExistingRoles(String,String)}*/
public String[] getRoles(String groupName)
{
	return getExistingRoles(Kernel.DEFAULT_COMMUNITY, groupName);
}

/** @return <code> true </code>if the role exists (i.e. there is at least one agent with this role); <code> false </code>otherwise.*/
public boolean isRole(String communityName, String groupName, String roleName)
{
	if( getAgentWithRole(communityName, groupName, roleName) == null)
		return false;
	return true;
}
/** @return <code> true </code>if the role exists (i.e. there is at least one agent with this role); <code> false </code>otherwise.*/
public boolean isRole(String groupName, String roleName)
{
	return isRole(Kernel.DEFAULT_COMMUNITY, groupName, roleName);
}

/** @deprecated As of MadKit 3.0. replaced by {@link #getExistingGroups(String)}*/
public java.util.Vector getGroups()
{
	String[] groups = currentKernel.getCurrentGroupsOf(getAddress(),Kernel.DEFAULT_COMMUNITY);
	java.util.Vector v = new java.util.Vector();
	for(int i = 0;i < groups.length;i++)
		v.addElement(groups[i]);
	return v;
}

/** Determines if this group already exists in this community*/
public boolean isGroup(String communityName, String groupName)
{
	return currentKernel.isGroup(communityName, groupName);
}
/** Determines if this group already exists in the default community*/
public boolean isGroup(String groupName)
{
	return currentKernel.isGroup(Kernel.DEFAULT_COMMUNITY, groupName);
}

/** Determines if this group already exists in this community
	@since MadKit 3.0
*/
public boolean isCommunity(String communityName)
{
	return currentKernel.isCommunity(communityName);
}

/** returns the available communities
	@since MadKit 3.0
*/
public String[] getAvailableCommunities()
{
	return currentKernel.getCommunities();
}

/** returns true if the community is shared on the network
	@since MadKit 3.0
*/
public boolean connectedWithCommunity(String communityName)
{
	return currentKernel.connectedWithCommunity(communityName);
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////////////// MESSAGING

/** Is there any message left to be read ? */
public boolean isMessageBoxEmpty(){	return messageBox.isEmpty();}

/** Get the number of messages in the message box */
public int getMessageBoxSize()
{
	return messageBox.size();//getSize();
}

/** Gets the first message in the queue.
	@return the first item in the message queue, or null if there is no message available
*/
public Message nextMessage()
{
	if (! isMessageBoxEmpty())
    		return (Message) messageBox.remove(0);
    	return null;
}

//return messageBox.nextMessage();}

/** kernel use */
public void receiveMessage(Message m){    messageBox.add(m);}//}putMessage(m);}

/** Send a message to another agent.
	@param a AgentAddress of the receiver
	@param m Message to be sent
*/
public void sendMessage(AgentAddress a, Message m)
{
	if(a != null)
	{
		m.setReceiver(a);
		m.setSender(getAddress());
		currentKernel.sendMessage(m);
	}
}

/** Send a message to an agent having a specific role in a group. The corresponding AgentAddress is selected with a getAgentWithRole(..) method call.
	@param communityName community in which the group is defined
	@param groupName group in which the role is defined
	@param roleName Role of the receiver
	@param m Message to be sent
*/
public void sendMessage(String communityName, String groupName, String roleName, Message m)
{
    if(! currentKernel.interGroupMessage)
	{
	    if(! currentKernel.isBelongingToGroup(getAddress(),communityName, groupName))
		{
		    println("I'm not allowed to send inter group messages !");
		    return;
		}
	}
	m.setReceiver(getAgentWithRole(communityName, groupName, roleName));
	m.setSender(getAddress());
	if (m.getReceiver() != null)
		currentKernel.sendMessage(m);
}
public void sendMessage(String groupName, String roleName, Message m)
{
	sendMessage(Kernel.DEFAULT_COMMUNITY, groupName, roleName, m);
}

/** Broadcast a message to every agent having a role in a group. You must assume that the message might not be cloned when sent to local agents*/
public void broadcastMessage(String communityName, String groupName, String roleName, Message m)
{
	m.setSender(getAddress());
	currentKernel.sendBroadcastMessage(communityName, groupName, roleName, m);
}
public void broadcastMessage(String groupName, String roleName, Message m)
{
	broadcastMessage(Kernel.DEFAULT_COMMUNITY, groupName, roleName, m);
}

/**
 * Basic handling of all messages that an agent may receive to ask it
 * to perform various tasks. This method should be invoked after or before
 * handling specific messages.
 * @param the message to be handled
 * @return a boolean value which is true if the message has been handled
 * and false otherwise.
 */
/* public boolean handleMessage(Message m){
    return false;
} */



/////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////// AGENT LAUNCHING & KILLING
/////////////////////////////////////////////////////////////////////////////////////////////

/** This method is called in order to launch an agent from another agent.
     @param agent The new (already instanciated) agent.
     @param name The "usual" name
     @param gui Should we setup the agent GUI if possible ?
   */
public void launchAgent(AbstractAgent agent, String name, boolean gui)
{
	if (name == null || name.equals(""))
		currentKernel.launchAgent(agent,"unamed", this, gui);
	else
		currentKernel.launchAgent(agent, name, this, gui);
}

/**experimental*/
public void restoreAgent(AbstractAgent agent)
{
	currentKernel.restoreAgent(agent,agent.getName(),this,agent.hasGUI());
}

/** Kill another agent. This is only possible if the caller launched the target agent itself and still owns a reference to the potential victim */
public void killAgent(AbstractAgent agent)
{
	currentKernel.killAgent(agent, this);
}
///////////////////////////////////////////////////////////////////////////////////



/////////////////////////////////////////////////////
////// Accessors
/////////////////////////////////////////////////////
/** Gets the agent's own agent address */
public AgentAddress getAddress() {      return agentInformation.getAddress();}
/** Gets the agent's own information */
public AgentInformation getAgentInformation() {      return agentInformation;}
/** Change the current agent patronymic name. This name does not need to be unique, and is provided only as a facility */
public void setName(String theName){    agentInformation.setName(theName);}
/** Gets the current agent patronymic name */
public String getName(){    return agentInformation.getName();}

/////////////////////////////////////////////   INTERNAL PACKAGE METHODS
final void setCurrentKernel(Kernel theKernel){	currentKernel = theKernel;}
/**internal package method for some special agents*/
final Kernel getCurrentKernel(){	return currentKernel;}
/** Internal agentInformation direct access method */
final void setAgentInformation(AgentInformation agentInfo){agentInformation = agentInfo;}



///////////////////////// GRAPHICS Agent Graphical Componential Interface
/** Check if a bean is running in GUI mode. Usually verified by the Kernel or the host application to setup or not a default graphical interface
	@return true if a GUI has been instanciated
*/
public boolean hasGUI(){    return (currentBean!=null);}

/** This method is called by the specific external graphic system (as the G-Box)
to ask the agent to prepare a graphical interface. The agent developper should
use a setGUIObject(...) within this method, as well as other necessary
initializations. If the developper does not overload this method, a vanilla text
output might be used as the default interface. */
public void initGUI()
{
	if (currentKernel != null)
		setGUIObject(currentKernel.gui.getDefaultGUIObject(this));
}

/** @deprecated As of MadKit 2.0. replaced by {@link #setGUIObject(Object)}*/
public void setBean(Object theBean)
{
	currentBean = theBean;
	bean_mode = true;
}

/**@deprecated As of MadKit 2.0. replaced by {@link #getGUIObject()}*/
public Object getBean(){return currentBean;}

/** This method set the bean that will be used to represent the agent in a graphical environment, the agent is also registered as running in GUI mode
	@param theBean an allocated graphic component
	@since MadKit 2.0
*/
public void setGUIObject(Object theBean)
{
    currentBean = theBean;
    bean_mode = true;
}

/** Gets the graphic component representing the agent. Usually called by the host application
	@since MadKit 2.0
*/
public Object getGUIObject()
{
	return currentBean;
}

/** Print out debug information only if the debug flag is on */
public void debug(String s)
{
	if(debugFlag)
		println("*Debug* "+s);
}

/** Set the debug flag */
public void setDebug(boolean b){    debugFlag = b;}
/**@return  true if debug is on*/
public boolean getDebug(){return debugFlag;}

/** Prints text information in a environment-independant way (GUI, console, ...) */
public void println(String theString)
{
    print(theString+'\n');
}

/** Prints text information in a environment-independant way (GUI, console, ...) */
public void print(String theString)
{
	try
	{
		if (ostream!=null)
			ostream.write(theString);
		else
			currentKernel.display("["+getAgentInformation().getName()+"] "+theString);
	}
	catch (IOException e)
	{
		System.err.println("IOException println:"+e.toString());
	}
}


/** @deprecated As of MadKit 2.0. replaced by {@link #setOutputWriter(Writer)}*/
public void setOutput(Writer o){setOutputWriter(o);}
/** Reassigns the "standard" agent text output stream (used by method println).*/
public void setOutputWriter(Writer o){ostream = o;}

/** hide the graphical component that represents the Agent in some higher interface
	@since MadKit 3.0
*/
public void disposeMyGUI()
{
	setOutputWriter(null);
	bean_mode=false;
	currentKernel.disposeGUIOf(this);
}

/** try to restore the graphical component that represents the Agent in some higher interface
	@since MadKit 3.0
*/
public void redisplayMyGUI()
{
	currentKernel.redisplayGUIOf(this);
}

public void windowClosing(AWTEvent we){
    	killAgent(this);
}


}
