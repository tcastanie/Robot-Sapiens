/*
* Kernel.java - Kernel: the kernel of MadKit
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

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;
import java.awt.Point;
import java.awt.Dimension;

/** This class is the heart of the MadKit micro-kernel.  Most of these
  methods will only be useful to "system" agents developpers
  @author Ol Gutknecht
  @author Fabien Michel since MadKit 3.0
  	- org operations are now implemented in the Organization class
  	- there are also changes in messaging routines
  	- hooks are now called only when the considered operation is effectively done
  	- new features have been added:
  	  - migration of agents (for the moment for security reason, this feature has been
            more or less disabled).
  	  - communities
  @version 3.1
 */

final public class Kernel
{
	public final static int SEND_MESSAGE            = 1;
	public final static int SEND_BROADCAST_MESSAGE  = 2;
	public final static int KILL_AGENT              = 3;
	public final static int CREATE_GROUP            = 4;
	public final static int LEAVE_GROUP             = 5;
	public final static int ADD_MEMBER_ROLE         = 6;
	public final static int REMOVE_MEMBER_ROLE      = 7;
	public final static int RESTORE_AGENT	    	= 8;
	public final static int LAUNCH_AGENT            = 0;
	public final static int CONNECTED_TO            = 9;
	public final static int DISCONNECTED_FROM       = 10;
	public final static int NEW_COMMUNITY	    	= 11;
	public final static int DELETE_COMMUNITY        = 12;

	final static int HOOKS_NUMBER = 13;

	public final static int GET_GROUPS    = 20;
	public final static int GET_AGENTS    = 21;
	public final static int DUMP_ORGANIZATION = 22;
	public final static int GET_AGENTINFO = 23;
	/*public final static int BE_COMMUNICATOR = 15;
	public final static int STOP_COMMUNICATOR = 16;*/
	public final static int MIGRATION = 17;
	public final static int CONNECTION = 24;
	public final static int DECONNECTION = 25;
	public final static int DUMP_COMMUNITIES = 27;
	//public final static int GET_AVAILABLE_DESTINATIONS = 26;

	final public static String DEFAULT_COMMUNITY = "public";

	final public static String VERSION = "4.0.8 - Gassho Rei";

	final public static String BUGREPORT = "Please file bug reports on http://sourceforge.net/projects/madkit";

	static KernelAddress kernelAddress;
	String kernelName;		//remove it ??

	private boolean debug = false;
	private Writer ostream = new OutputStreamWriter (System.err);

	private SiteAgent siteAgent;
	private KernelAgent kernelAgent;
	GraphicShell gui = null;
	private static Map localAgents = null;
	private Map organizations;
	private static ThreadGroup agentsThread=new ThreadGroup("agents");

	static boolean fastSynchronous=false;
	static boolean interGroupMessage=true;

    static public ThreadGroup getAgentThreadGroup(){ return agentsThread;}

    static int agentsNb=0;

    public static int getAgentsNb()  { return agentsNb;  }  
    
    public static KernelAddress getAddress()  {    return kernelAddress;  }
    private static void setAddress(KernelAddress k)  {    kernelAddress=k;  }

public void registerGUI(GraphicShell g)
{
	gui=g;
	//siteAgent.initGUI();
	//disposeGUIOf(siteAgent);
	//redisplayGUIOf(siteAgent);
}
public String getName()    {	return kernelName;    }

public Kernel(String theName, boolean ipnumeric)
{
	kernelAddress = new KernelAddress(ipnumeric);
	kernelName=theName;
	initialization();
}

public Kernel(String theName, boolean ipnumeric, String ipaddress)
{
	kernelAddress = new KernelAddress(ipnumeric,ipaddress);
	kernelName=theName;
	initialization();
}

public Kernel(String theName)
{
	this(theName, false);
}

void initialization()
{
	Organization communities = new Organization();
	organizations = new Hashtable();
	organizations.put("communities",communities);

  	localAgents = new HashMap();

	kernelAgent = new KernelAgent();
	siteAgent = new SiteAgent(organizations, kernelAgent);
	launchAgent(siteAgent,"SITEAGENT"/*+kernelAddress*/,this,false);
	System.err.println("\n\t-----------------------------------------------------");
	System.err.println("\n\t\t\t    MadKit/Aalaadin \n\n\tby MadKit Team (c) 1997-2004\n");
	System.err.println("\t\t\tversion: "+VERSION+"\n");
	System.err.println("\t-----------------------------------------------------\n");
	System.err.println(BUGREPORT+"\n\n");
	displayln("MadKit Agent microKernel "+getAddress() + " is up and running");
}

///////////////////////////////////// 	FUNDAMENTAL OPERATIONS : CREATE, REQUESTROLE, LEAVEROLE & LEAVEGROUP

final int createGroup(AgentAddress creator, boolean distributed, String communityName, String groupName, String description, GroupIdentifier theIdentifier)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		organization = siteAgent.createCommunity(communityName);
	int result = organization.createGroup(creator, distributed, groupName, description, theIdentifier);
	if(result == 1)
	{
		kernelAgent.callHooks(CREATE_GROUP, creator, communityName, groupName, null);
		if(distributed)
			siteAgent.updateDistantOrgs(creator,communityName,groupName);
	}
	return result;
}

final int requestRole(AgentAddress requester, String communityName, String groupName, String roleName, Object memberCard)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null)
	{
		int result = organization.requestRole(requester, groupName, roleName, memberCard);
		if(result == 1)
		{
			if(organization.isDistributed(groupName))
				siteAgent.updateDistantOrgs(ADD_MEMBER_ROLE, requester, communityName, groupName, roleName, memberCard);
			kernelAgent.callHooks(ADD_MEMBER_ROLE, requester, communityName, groupName, roleName);
		}
		return result;
	}
	else
		return -4;
}

final boolean leaveGroup(AgentAddress requester, String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null && organization.isPlayingRole(requester,groupName,"member"))
	{
		if(organization.isDistributed(groupName))
			siteAgent.updateDistantOrgs(LEAVE_GROUP, requester,communityName, groupName, null, null);
		kernelAgent.callHooks(LEAVE_GROUP, requester, communityName, groupName, null);
		if(organization.leaveGroup(requester,groupName))
			siteAgent.removeCommunity(communityName);
		return true;
	}
	return false;
}

final boolean leaveRole(AgentAddress requester, String communityName, String groupName, String roleName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null && organization.isPlayingRole(requester,groupName,roleName))
	{
		if(organization.isDistributed(groupName))
			siteAgent.updateDistantOrgs(REMOVE_MEMBER_ROLE, requester,communityName, groupName, roleName, null);
		if(organization.leaveRole(requester,groupName,roleName))
			siteAgent.removeCommunity(communityName);
		kernelAgent.callHooks(REMOVE_MEMBER_ROLE, requester, communityName, groupName, roleName);
		return true;
	}
	return false;
}

final boolean isBelongingToGroup(AgentAddress who, String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null && organization.isPlayingRole(who,groupName,"member"))
	    return true;
	return false;
}

//////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////// OVERLOOKING

/////////////////////////////////////////////////////////////////////////////////////////
final boolean addOverlooker(AgentAddress requester, Overlooker o, Object accessCard)
{
	Organization organization = getOrganizationFor(o.community);
	if(organization == null)
		return false;
	return organization.addOverlooker(requester, o, accessCard);
}

final void removeOverlooker(Overlooker o)
{
	Organization organization = getOrganizationFor(o.community);
	if(organization != null && organization.removeOverlooker(o))
		siteAgent.removeCommunity(o.community);
}
///////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////   AGENT LAUNCHING & KILLING

////////////////////////////////////////////////////////////////////////////////////

final public synchronized void launchAgent(AbstractAgent agent, String name, Object creator, boolean startGUI){
	launchAgent(agent,name,creator,startGUI,new Point(-1,-1),new Dimension(-1,-1));
}
final public synchronized void launchAgent(AbstractAgent agent, String name, 
											Object creator, boolean startGUI, Point position, Dimension dim)
{
	if (debug)
		displayln("Agent launch: "+ name +", created by " + creator.toString());
	if (agent.getAgentInformation() == null)
	{
		agent.setAgentInformation(new AgentInformation(name, creator));
		agent.setCurrentKernel(this);
		localAgents.put(agent.getAddress(),agent);
		///*organization.*/requestRole(agent.getAddress(), "public", "local","member",null);	//temporaire
		if (startGUI && (gui != null))
			gui.setupGUI(agent,position,dim);
		Thread thread = null;
		//System.err.println("launch "+ Thread.currentThread() +"  "+Thread.currentThread().getPriority());
		if (agent instanceof Agent)
		{
			thread = new Thread(agentsThread, (Agent) agent, agent.getName() + "_thread");
			//thread.setPriority(5);//Thread.MIN_PRIORITY);
			//System.err.println("launch "+ thread +"  "+thread .getPriority());
		}

		if (thread != null)
			thread.start();
		else
		{
			Controller c = agent.getController();
			if (c != null)
				c.activate();
			else
				agent.activate();
        	}
		agentsNb++;
		kernelAgent.callHooks(LAUNCH_AGENT, agent.getAgentInformation());
	}
	else
		if (debug)
			displayln("ASSERT: agent already registred");
}

/** Kill a given agent (from another agent). */
final void killAgent(AbstractAgent target, AbstractAgent killer)
{
	// Basically a wrapper verifying rights for the caller to terminate its mate */
	if (target!=null && target.getAgentInformation()!=null && (killer.getAddress().equals(target.getAgentInformation().getOwner()) || target == killer))
		killAgent(target);
}

/** Kill a given agent (also manage groups update) */
final synchronized public void killAgent(AbstractAgent a)
{
	if(a.getCurrentKernel() == null)
		return;
	if (a instanceof Agent && Thread.currentThread() != ((Agent)a).getAgentThread())
		((Agent)a).getAgentThread().stop();
	Controller c = a.getController();
	if (c != null)
		c.end();
	else
		a.end();
	removeAgentFromOrganizations(a.getAddress());
	localAgents.remove(a.getAddress());
	if (gui != null)
		gui.disposeGUI(a);
	a.setCurrentKernel(null);
	kernelAgent.callHooks(KILL_AGENT, a.getAgentInformation());
	agentsNb--;
}

//experimental
final synchronized void receiveAgent(AbstractAgent agent)
{
 	agent.getAddress().update(kernelAddress);
 	agent.setCurrentKernel(this);
	localAgents.put(agent.getAddress(),agent);
	if (gui != null)
		gui.setupGUI(agent);

	/*organization.*/
	//requestRole(agent.getAddress(),"public", "local","member",null);	//temporaire

	//kernelAgent.callHooks(RESTORE_AGENT, agent.getAgentInformation());
	Thread thread = null;
	if (agent instanceof Agent)
		thread = new Thread(/*tg,*/(Agent) agent, agent.getName() + "_thread");
	if (thread != null)
		thread.start();
	else
		agent.activate();
}

final synchronized void restoreAgent(AbstractAgent agent, String name, Object creator, boolean startGUI)
{
	if (debug)
		displayln("Agent restoration: "+ name +", restored by " + creator.toString());
	if (agent.getAgentInformation() != null)
	{
		agent.setAgentInformation(new AgentInformation(name, creator));
		agent.setCurrentKernel(this);
		localAgents.put(agent.getAddress(),agent);

		/*organization.*/
		//requestRole(agent.getAddress(),"public", "local","member",null);		//temporaire

		if (startGUI && (gui != null))
			gui.setupGUI(agent);
		Thread thread = null;
		if (agent instanceof Agent)
			thread = new Thread((Agent) agent, agent.getName() + "_thread");
		if (thread != null)
			thread.start();
		else
			agent.activate();
		kernelAgent.callHooks(RESTORE_AGENT, agent.getAgentInformation());
	}
	else
		if (debug)
	    		displayln("ASSERT: restoration impossible: agent has not been previously launched");
}

final synchronized private void killAgents()	// BUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
{
	AgentAddress myAgent = kernelAgent.getAddress();
	for(Iterator i=localAgents.keySet().iterator(); i.hasNext(); )
	{
		AgentAddress next = (AgentAddress) i.next();
		if(! next.equals(myAgent))
			removeAgentFromOrganizations(next);
		i.remove();
	}
}

final synchronized void removeReferenceOf(AgentAddress agent)
{
	if (gui != null)
		try
		{
			gui.disposeGUI((AbstractAgent)localAgents.get(agent));
		}
		catch(Exception e){}
	localAgents.remove(agent);
}



///////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////	MESSAGING

/** Send a message. If the the siteAgent has a communicator it will delegate the message to it
   @param sender Agent requesting the send
   @param receiver Destination agent
   @param m the Message itself
   @see Message*/
final void sendMessage(Message m)
{
	try
	{
		if (m.getReceiver().isLocal())
		{
			sendLocalMessage((Message)m.clone());
			if (!(m instanceof  PrivateMessage))
				kernelAgent.callHooks(SEND_MESSAGE, m);
		}
		else
			if(siteAgent.sendDistantMessage(m) && !(m instanceof PrivateMessage))
				kernelAgent.callHooks(SEND_MESSAGE, m);
	}
	catch(MessageException mexc)
	{
		if (debug)
			siteAgent.debug("Unable to send message "+m+" : "+mexc);
	}
}

/** Sends a local message
   @param m the message itself
   @see Message*/
final void sendLocalMessage(Message m) throws MessageException
{
	AbstractAgent a = (AbstractAgent) localAgents.get(m.getReceiver());
	if (a != null)
		a.receiveMessage(m);
	else
		throw new MessageException("Unknown agent");
}

/** Sends a broadcast message. If a specialized system agent can handle distributed message, the kernel will delegate the message to it
   @param groupName Group
   @param roleName Role
   @param m the Message itself
   @see Message*/
final void sendBroadcastMessage(String communityName, String groupName, String roleName, Message m)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null)
	{
		AgentAddress[] receivers = organization.getRolePlayers(groupName, roleName);
		if (!(m instanceof PrivateMessage))
		{
			Vector h = new Vector();
			h.addElement(groupName);
			h.addElement(roleName);
			h.addElement(m);
			kernelAgent.callHooks(SEND_BROADCAST_MESSAGE, h);
		}
		for(int i = 0; i< receivers.length;i++)
		{
			Message m2 = (Message) m.clone();
			m2.setReceiver(receivers[i]);
			try
			{
				if (receivers[i].isLocal())
					sendLocalMessage(m2);
				else
					siteAgent.sendDistantMessage(m2);
			}
			catch(MessageException mexc)
			{
				if (debug)
					siteAgent.debug("Unknown agent");
			}
		}
	}
}

////////////////////////////////////////////////// internal methods

final static synchronized AbstractAgent getReference(Object agent)
{
	return (AbstractAgent) localAgents.get(agent);
}

final static synchronized AgentAddress[] getLocalAgents()
{
	return (AgentAddress[]) localAgents.keySet().toArray(new AgentAddress[0]);
}



///////////////////////////////////////////////// ORGANIZATIONS MANAGEMENT
final synchronized Organization getOrganizationFor(String communityName)
{
	return (Organization) organizations.get(communityName);
}

final synchronized boolean isGroup(String community, String groupName)
{
	Organization organization = getOrganizationFor(community);
	if(organization != null)
		return organization.isGroup(groupName);
	return false;
}

final synchronized public String[] getCurrentGroupsOf(AgentAddress theAgent, String communityName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new String[0];
	return organization.getCurrentGroupsOf(theAgent);
}

final synchronized public String[] getExistingGroups(String communityName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new String[0];
	return organization.getGroups();
}

final synchronized public AgentAddress[] getRolePlayers(String communityName, String groupName, String roleName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new AgentAddress[0];
	return organization.getRolePlayers(groupName, roleName);
}

final synchronized AgentAddress getRolePlayer(String communityName, String groupName, String roleName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return null;
	return organization.getRolePlayer(groupName, roleName);
}

final public synchronized String[] getGroupRolesOf(AgentAddress agent, String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new String[0];
	return organization.getGroupRolesOf(agent, groupName);
}

final synchronized public String[] getExistingRoles(String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new String[0];
	return organization.getRolesIn(groupName);
}

final synchronized boolean isCommunity(String communityName)
{
	Organization organization = getOrganizationFor("communities");
	return  (organization.isPlayingRole(siteAgent.getAddress(),communityName,"member") || organization.getRolePlayer(communityName,"site") != null);
}

final synchronized String[] getCommunities()
{
	return siteAgent.getCommunities();
}

final synchronized boolean connectedWithCommunity(String communityName)
{
	return getOrganizationFor("communities").isPlayingRole(siteAgent.getAddress(), communityName, "site");
}

final synchronized void removeAgentFromOrganizations(AgentAddress theAgent)	// must be optimized
{
	Map groupNames = new HashMap();
	for(Iterator i = organizations.entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		Organization org = (Organization) e.getValue();
		groupNames.put(e.getKey(),org.getCurrentGroupsOf(theAgent));
	}
	for(Iterator i = groupNames.entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		String[] groups = (String[]) e.getValue();
		for(int j=0;j<groups.length;j++)
			leaveGroup(theAgent,(String) e.getKey(),groups[j]);
	}
}

synchronized Map getDumpCommunities(){
    Map res = new HashMap();
    for(Iterator i = organizations.entrySet().iterator();i.hasNext();){
        Map.Entry e = (Map.Entry) i.next();
        res.put(e.getKey(), ((Organization) e.getValue()).getLocalOrganization());
    }
    return res;
}

/////////   redundant & facilities
/**@return the addresses of all the agents who are members of $groupName$*/
final synchronized public AgentAddress[] getMembersWithin(String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new AgentAddress[0];
	return organization.getRolePlayers(groupName,"member");
}
/////////////////////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////   Others
/** Request a kernel stop. All agents are (hopefully) cleanly killed */
public void stopKernel()
{
      displayln("Disconnecting MadKit Kernel: " + getName());
      displayln("- killing local agents " + getName());
      killAgents();
      displayln("MadKit Kernel closed.");
      System.exit(0);
}

/** A generic display method adapting its output to the kernel environment (console, GUI, applet...)
	@param s string to be displayed, add a newline at the end of the string */
public void displayln(String s)
{
    display(s+'\n');
}

/** A generic display method adapting its output to the kernel environment (console, GUI, applet...)
	@param s string to be displayed */
public void display(String s)
{
//	try
//	{
//		ostream.write("<" + getName() + "> : " + s);
//		ostream.flush();
              System.err.print("<" + getName() + "> : " + s);
//	}
////	catch (IOException e)      {
//          System.err.println(e.toString());
//        }
}

/** Reassigns the "standard" agent text output stream (used by println method).  */
public void setOutputStream(Writer o)  {    ostream = o;  }

final void disposeGUIOf(AbstractAgent theAgent)
{
	if (gui != null)
		gui.disposeGUIImmediatly(theAgent);
}

final void redisplayGUIOf(AbstractAgent theAgent)
{
	if (gui != null)
		gui.setupGUI(theAgent);
}

/////////////////////////////////////////////////////////////////////////////////////////////////:

//////////////////////////////////////////////	DEPRECATED METHODS

/**@deprecated As of MadKit 3.0. replaced by {@link #getCurrentGroupsOf(AgentAddress, String)}*/
public Vector getCurrentGroups(AgentAddress theAgent)
{
	Vector v = new Vector();
	String[] groups = getCurrentGroupsOf(theAgent, Kernel.DEFAULT_COMMUNITY);
	for(int i = 0;i<groups.length;i++)
		v.addElement(groups[i]);
	return v;
}

/**@deprecated As of MadKit 3.0. replaced by {@link #getMembersWithin(String, String)}*/
synchronized public Enumeration getGroupMembers(String theGroup)
{
	Collection c = new HashSet();
	AgentAddress[] addresses = getMembersWithin(theGroup, Kernel.DEFAULT_COMMUNITY);
	for(int i=0;i< addresses.length;i++)
		c.add(addresses[i]);
	return Collections.enumeration(c);
}

/**@deprecated As of MadKit 3.0. replaced by {@link #getGroupRolesOf(AgentAddress, String, String)}please use getGroupRolesOf instead*/
synchronized public Vector getMemberRoles(String theGroup, AgentAddress theAgent)
{
	Vector v = new Vector();
	String[] roles = getGroupRolesOf(theAgent, Kernel.DEFAULT_COMMUNITY, theGroup);
	for(int i = 0;i<roles.length;i++)
		v.addElement(roles[i]);
	return v;
}

void synchronizeKernel(Map orgs, boolean priority)
{
	for(Iterator i = orgs.entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		if(siteAgent.connectedWith((String) e.getKey()))
		{
			if (! organizations.containsKey(e.getKey()))
				organizations.put(e.getKey(),new Organization());
			( (Organization) organizations.get(e.getKey()) ).importOrg((Organization)(e.getValue()),priority);
		}
	}
	siteAgent.refreshCommunities();
}

public static void debugString()
{
		System.err.println("--------------------------------------kernel status");
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		Thread[] temp=new Thread[tg.activeCount()];
		tg.enumerate(temp);
		for(int i = 0; i< temp.length; i++)
		{
			if (temp[i] != null)
				System.err.println(""+i+":  "+temp[i]+" is demon "+temp[i].isDaemon());
		}
		System.err.println("used memory: "+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		System.err.println("locals agents are "+localAgents.size());
}
}
