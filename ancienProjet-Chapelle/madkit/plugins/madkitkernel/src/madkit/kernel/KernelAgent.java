/*
* KernelAgent.java - Kernel: the kernel of MadKit
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

/** The KernelAgent represents the Kernel ...
  @author Ol Gutknecht
 * @author Fabien Michel : MadKit 3.0 modifications
  @version 3.0
 */

final class KernelAgent extends Agent
{
	private Collection distantKernels;
	private Collection[] monitor_hooks;
	private AgentAddress myCommunicator=null;
	//private Map communities;
	Organization localOrg;
	SiteAgent siteAgent;


KernelAgent()
{
	monitor_hooks = new Collection[Kernel.HOOKS_NUMBER];
	for (int i=0; i < Kernel.HOOKS_NUMBER; i++)
		monitor_hooks[i]=new HashSet(7);
}

////////////////////////////////////////////////////	LIFE CYCLE

final public  void activate()
{
	//setDebug(true);
	//createGroup(false,"system",null,null);
    	//createGroup(false,"local",null,null);
	requestRole("system","kernel",null);
	pause(100);
	//launchAgent(new turtlekit.simulations.tests.Creation(),"termites",true);
	//launchAgent(new warbot.kernel.Warbot(),"warbot",true);
	//launchAgent(new demo.agents.system.OrganizationTracer(),"org tracer",true);
	//launchAgent(new demo.agents.system.MessageTracer(),"message tracer",true);
	//launchAgent(new demo.agents.SuperPingPong(),"player",true);
}

final public void live()
{
	while(true)
	{
		Message e = waitNextMessage();
		handleMessage(e);
	}
}

public void end()
{
	System.err.println("KERNEL AGENT KILLED !!!!!!!!!!!!!");
	System.err.println("UNSTABLE SYSTEM ....");
	//System.exit(0);
}

final synchronized void handleMessage(Message e)
{
	//System.err.println("receive message"+e);
	if (e instanceof KernelMessage)
	{
		KernelMessage m = (KernelMessage)e;
		switch (m.getType())
		{
			case KernelMessage.INVOKE:
			      invokeKernelOperation(m);
			      break;
			default: // It's a hooker :-)
			      manageHooks(m);
			      break;
		}
	}
	/*else
		if(e instanceof PrivateMessage)
		{
			PrivateMessage pm = (PrivateMessage) e;
			switch(pm.code)
			{
				case 5:
					if(getDebug())
						System.err.println("receiving a migration "+pm.ref.toString());
					kernel.receiveAgent(pm.ref);
					System.gc();
					System.runFinalization();
					break;
				case 6:
					kernel.removeAgentFromOrg(pm.initiator);
					break;
			}
		}*/
}

////////////////////////////////////////////////////////////////////////////////////////////

final protected void invokeKernelOperation(KernelMessage m)
{
	switch (m.getOperation())
	{
		case Kernel.GET_AGENTINFO:
			AgentAddress aad = (AgentAddress)m.getArgument();
			AbstractAgent a = Kernel.getReference(aad);
			AgentInformation ai = null;
			if (a != null)
				ai = a.getAgentInformation();
			sendMessage(m.getSender(), new KernelMessage(KernelMessage.REPLY,m.getOperation(),ai));
			break;
		case Kernel.GET_GROUPS:
			sendMessage(m.getSender(), new KernelMessage(KernelMessage.REPLY,m.getOperation(),localOrg.getGroups()));
			break;
		case Kernel.DUMP_ORGANIZATION:
			sendMessage(m.getSender(), new KernelMessage(KernelMessage.REPLY,m.getOperation(),localOrg.getLocalOrganization()));
			break;
        	case Kernel.DUMP_COMMUNITIES:
			sendMessage(m.getSender(), new KernelMessage(KernelMessage.REPLY,m.getOperation(),getCurrentKernel().getDumpCommunities()));
			break;
		/*case Kernel.SEND_MESSAGE:
			try
			{
				Message m2 = (Message) m.getArgument();
				kernel.sendLocalMessage(m2);
				if(! (m2 instanceof KernelMessage || m2 instanceof PrivateMessage))
					callHooks(Kernel.SEND_MESSAGE, m2.clone());
			}
			catch (MessageException me) {if (getDebug()) System.err.println(me);}
			break;*/
		case Kernel.GET_AGENTS:
			sendMessage(m.getSender(), new KernelMessage(KernelMessage.REPLY, m.getOperation(), getCurrentKernel().getLocalAgents()));
			break;
		/*case Kernel.BE_COMMUNICATOR:	// possibly verify this agent
			myCommunicator = m.getSender();
			siteAgent.setCommunicator(myCommunicator);
			break;
		case Kernel.STOP_COMMUNICATOR:	// possibly verify this agent
			if(myCommunicator!= null && myCommunicator.equals(m.getSender()))
			{
				myCommunicator = null;
				siteAgent.setCommunicator(null);
				for(Iterator i = distantKernels.iterator();i.hasNext();)
				{
					kernel.disconnectFromKernel( ((AgentAddress) i.next()).getKernel());
					i.remove();
				}
				System.gc();
				System.runFinalization();
			}
			break;
		case Kernel.GET_AVAILABLE_DESTINATIONS:	// possibly verify this agent
			KernelAddress[] destinations = new KernelAddress[distantKernels.size()];
			int j=0;
			for (Iterator i = distantKernels.iterator();i.hasNext();j++)
				destinations[j] = ((AgentAddress) i.next()).getKernel();
			sendMessage(m.getSender(), new KernelMessage(KernelMessage.REPLY, m.getOperation(), destinations));
			break;
		case Kernel.MIGRATION:	// possibly verify this agent
			if(getDebug())
				println("receiving a migration request of "+m.getSender());
			tryMigration((KernelAddress) m.getArgument(),m.getSender());
			break;
		case Kernel.CONNECTION:
	  		siteAgent.establishConnectionWith((AgentAddress) m.getArgument(),true);
	  		distantKernels.add(m.getArgument());
		  	break;
		case Kernel.DECONNECTION:
	  		distantKernels.remove(m.getArgument());
		  	siteAgent.deconnection((String) m.getArgument());
		  	break;*/
	}
}

final protected void manageHooks(KernelMessage m)
{
	if (m.getOperation() < Kernel.HOOKS_NUMBER)
		switch (m.getType())
		{
			case KernelMessage.REQUEST_MONITOR_HOOK:
				if (getDebug())
					println("Adding hook:"+m.getOperation()+" for "+m.getSender());
				monitor_hooks[m.getOperation()].add(m.getSender());
				break;
			case KernelMessage.REMOVE_MONITOR_HOOK:
				if (getDebug())
					println("Removing hook:"+m.getOperation()+" for "+m.getSender());
				monitor_hooks[m.getOperation()].remove(m.getSender());
				break;
		}
}

final synchronized void callHooks(int theHook, Object argument)
{
	debug("hook called : hook's code is : "+theHook+" "+argument);
	if (monitor_hooks[theHook].size()>0)
		for (Iterator i = monitor_hooks[theHook].iterator();i.hasNext();)
			sendMessage((AgentAddress)i.next(), new KernelMessage(KernelMessage.MONITOR_HOOK, theHook,argument));
}

final synchronized void callHooks(int theHook, AgentAddress theAgent, String communityName, String groupName,String RoleName)
{
	debug("hook called : hook's code is : "+theHook+" "+theAgent+" "+communityName+" "+groupName+" "+RoleName);
	if (monitor_hooks[theHook].size()>0)
		for (Iterator i = monitor_hooks[theHook].iterator();i.hasNext();)
			sendMessage((AgentAddress)i.next(), new KernelMessage(KernelMessage.MONITOR_HOOK, theHook,new AGRTrio(theAgent,communityName,groupName,RoleName)));
}

}
