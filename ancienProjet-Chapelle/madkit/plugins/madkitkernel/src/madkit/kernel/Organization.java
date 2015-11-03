/*
* Organization.java - Kernel: the kernel of MadKit
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

/** This class is the heart of AGR mechanisms of MadKit 3.0. It implements the Aalaadin mechanisms
with new security possibilities using groups and role
    @author Fabien Michel
    @version 1.0 MadKit 3.0
    @since MadKit 3.0
*/
final class Organization extends HashMap
{

///////////////////////////////////// 	FUNDAMENTAL OPERATIONS : CREATE, REQUESTRole, LEAVEROLE & LEAVEGROUP

synchronized int createGroup(AgentAddress creator,boolean distributed,String groupName, String description, GroupIdentifier theIdentifier)
{
	// the group does not exist
	if(! containsKey(groupName))
	{
		put(groupName,new Group(creator, distributed, description, theIdentifier));
		return 1;
	}
	else
		if ( getGroup(groupName).isEmpty())	// the group is overlooked but is not registered as a real one
		{
			getGroup(groupName).realCreation(creator, distributed, description, theIdentifier);
			return 1;
		}
		else
			return -1;	// the group already exists
}

synchronized int requestRole(AgentAddress requester, String groupName,String roleName,Object memberCard)
{
	Group theGroup = getGroup(groupName);
	if(theGroup != null && (! theGroup.isEmpty()) && (! roleName.equals("group manager")) )
		return theGroup.requestRole(requester,roleName,memberCard);
	return -3;	// the Group does not exist
}

synchronized boolean leaveGroup(AgentAddress requester, String groupName)
{
	if(getGroup(groupName).leave(requester) )
		remove(groupName);
	return isEmpty();
}

synchronized boolean leaveRole(AgentAddress requester, String groupName,String roleName)
{
	if( getGroup(groupName).leaveRole(requester, roleName) )
		remove(groupName);
	return isEmpty();
}

///////////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////// OVERLOOKER OPEARTIONS
synchronized boolean addOverlooker(AgentAddress requester, Overlooker o, Object accessCard)
{
	Group theGroup = getGroup(o.group);
	if(theGroup != null)
		return theGroup.addOverlooker(requester, o, accessCard);
	else
	{
		theGroup = new Group();
		put(o.group,theGroup);
		return theGroup.addOverlooker(requester, o, accessCard);
	}
}

synchronized boolean removeOverlooker(Overlooker o)
{
	Group theGroup = getGroup(o.group);
	if(theGroup != null)
	{
		if(theGroup.removeOverlooker(o))
		{
			remove(o.group);
			return isEmpty();
		}
	}
	return false;
}

///////////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////// ORGANIZATION INFO OPERATIONS
boolean isPlayingRole(AgentAddress theAgent, String groupName, String roleName)
{
	return (containsKey(groupName) &&  getGroup(groupName).isPlayingRole(theAgent, roleName));
}

boolean isGroup(String groupName)
{
	return (containsKey(groupName) &&  (! getGroup(groupName).isEmpty()));
}

AgentAddress[] getRolePlayers(String groupName, String roleName)
{
	if(isGroup(groupName))
		return getGroup(groupName).getRolePlayers(roleName);
	return new AgentAddress[0];
}

AgentAddress getRolePlayer(String groupName, String roleName)
{
	if(isGroup(groupName))
		return getGroup(groupName).getRolePlayer(roleName);
	return null;
}

String[] getGroupRolesOf(AgentAddress agent, String groupName)
{
	if(isGroup(groupName))
		return getGroup(groupName).getRolesOf(agent);
	return new String[0];
}

Group getGroup(String groupName)
{
	return (Group) get(groupName);
}

boolean isDistributed(String groupName)
{
	Group g = getGroup(groupName);
        if (g != null)
          return g.isDistributed();
	else
          return false;
}

String[] getRolesIn(String groupName)
{
	Group g = getGroup(groupName);
        if (g != null)
          return g.availableRoles();
        else
          return new String[0];
}

synchronized String[] getGroups()
{
	Collection c = new HashSet(size());
	for (Iterator i=entrySet().iterator(); i.hasNext(); )
	{
		Map.Entry e = (Map.Entry) i.next();
		if( ! ((Group) e.getValue()).isEmpty())
			c.add(e.getKey());
	}
	return (String[]) c.toArray(new String[0]);
}

synchronized String[] getCurrentGroupsOf(AgentAddress agent)
{
	Collection c = new HashSet(size());
	for (Iterator i=entrySet().iterator(); i.hasNext(); )
	{
		Map.Entry e = (Map.Entry) i.next();
		if( ((Group) e.getValue()).isPlayingRole(agent, "member") )
			c.add(e.getKey());
	}
	return (String[]) c.toArray(new String[0]);
	/*Collection c = new HashSet(size());
	Map m = Collections.synchronizedMap(this);
	synchronized(m)
	{
		for (Iterator i=m.entrySet().iterator(); i.hasNext(); )
		{
			Map.Entry e = (Map.Entry) i.next();
			if( ((Group) e.getValue()).isPlayingRole(agent, "member") )
				c.add(e.getKey());
		}
	}
	return (String[]) c.toArray(new String[0]);*/
}

AgentAddress[] getGroupMembers(String groupName)
{
	if(isGroup(groupName))
		return getGroup(groupName).getRolePlayers("member");
	return new AgentAddress[0];
}

synchronized Map getLocalOrganization()
{
	Map groupsName = new Hashtable();
	for(Iterator i = entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		if (! ((Group) e.getValue()).isEmpty())
			groupsName.put(e.getKey(),((Group) e.getValue()).mapForm());
	}
	return groupsName;
}


///////////////////////////////////////////////		IMPORT EXPORT

synchronized Organization exportOrg()
{
	Organization org = new Organization();
	for(Iterator i = entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		Group g = (Group) e.getValue();
		if(g.isDistributed() && ! g.isEmpty())
			org.put(e.getKey(),e.getValue());
	}
	return org;
}

synchronized void importOrg(Organization org, boolean priority)
{
	for(Iterator i = org.entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		if (! containsKey(e.getKey()))
		{
			Group newGroup = new Group();
			put(e.getKey(),newGroup);
			newGroup.merge((Group) e.getValue(),priority);
		}
		else
			((Group) get(e.getKey())).merge((Group) e.getValue(),priority);
	}
}

synchronized boolean removeAgentsFromKernel(KernelAddress distantAddress)
{
	for(Iterator i = values().iterator();i.hasNext();)
	{
		Group g = (Group) i.next();
		if(g.isDistributed())
		{
			AgentAddress[] agents = g.getRolePlayers("member");
			for(int j=0;j<agents.length;j++)
				if(agents[j].getKernel().equals(distantAddress))
					if(g.leave(agents[j]))
						i.remove();
		}
	}
	return isEmpty();
}

synchronized boolean removeDistantAgents()
{
	for(Iterator i = values().iterator();i.hasNext();)
	{
		Group g = (Group) i.next();
		if(g.isDistributed())
		{
			AgentAddress[] agents = g.getRolePlayers("member");
			for(int j=0;j<agents.length;j++)
				if(! agents[j].isLocal())
					if(g.leave(agents[j]))
						i.remove();
		}
	}
	return isEmpty();
}

}
