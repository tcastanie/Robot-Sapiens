/*
* Group.java - Kernel: the kernel of MadKit
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
import java.util.Vector;

/** A MadKit group definition. A group in Aalaadin/MadKit is defined as a set
  of Role objects. Any member can hold many roles within the group.
  This class is an internal MadKit structure.
   
   new Group class for MadKit 3.0
    @author Fabien Michel
    @version 3.0 01/10/01
*/
final class Group extends HashMap
{
	private boolean distributed=false;
	private String description=null;
	private GroupIdentifier groupGate=null;

/**this constructor assumes the creation of an inexistant group which is overlooked 
 We can say a virtual group, i.e. with no member.
 (by default, in the system the resulting group will not be distributed and description is null.
 but this will be changed during the real creation)*/
Group()
{
}

/**this constructor is invoked when one agent success in creating a group*/
Group(AgentAddress creator, boolean distributed,String description, GroupIdentifier theIdentifier)
{
	realCreation(creator,  distributed, description,  theIdentifier);
}

void realCreation(AgentAddress creator, boolean distributed, String description, GroupIdentifier theIdentifier)
{
	if(containsKey("group manager")) //in case this role is overlooked
	{
		AgentAddress ancienCreator = getRolePlayer("group manager");
		if(ancienCreator != null)
			((Role) get("group manager")).removeMember(ancienCreator);
		((Role) get("group manager")).addMember(creator);
	}
	else
	{
		Role groupManager = new Role(1);	// à optimiser
		groupManager.addMember(creator);
		put("group manager",groupManager);
	}
	if(containsKey("member")) //in case this role is overlooked
		((Role) get("member")).addMember(creator);
	else
	{
		Role members = new Role();
		members.addMember(creator);
		put("member",members);
	}
	this.distributed = distributed;
	this.description = description;
	groupGate = theIdentifier;
}

	
///////////////////////////////////// 	FUNDAMENTAL OPERATIONS 	ON GROUP : REQUESTRole, LEAVEROLE & LEAVEGROUP
private int roleAssignment(AgentAddress requester,String roleName)
{
	Role theRole = (Role) get(roleName);
	if(theRole != null)
	{
		if( theRole.addMember(requester) )
		{
			((Role) get("member")).addMember(requester);
			return 1;	//operation success
		}
		else
			return -2;	//role already handled
	}
	else
	{
		theRole = new Role();
		theRole.addMember(requester);
		put(roleName,theRole);
		((Role) get("member")).addMember(requester);
		return 1;
	}
}

int requestRole(AgentAddress requester, String roleName,Object memberCard)
{
	if(groupGate != null)
		if(groupGate.allowAgentToTakeRole(requester, roleName, memberCard))
			return roleAssignment(requester, roleName);
		else
			return -1;	// access denied
	else
		return roleAssignment(requester, roleName);
}
	

/**the agent leave the group
@return true if the group is no longer useful i.e. no member, no overlooker*/
boolean leave(AgentAddress requester)
{
	for (Iterator i=values().iterator(); i.hasNext(); )
		if( ((Role) i.next()).removeMember(requester))
			i.remove();
	return super.isEmpty();
}


//////////// il faut savoir si le fait que le group manager sorte annule l'existance du group ou translation vers un nouvel agent...
boolean leaveRole(AgentAddress requester, String roleName)
{
	if(containsKey(roleName))
		if( ((Role) get(roleName)).removeMember(requester))
		{
			remove(roleName);
			return super.isEmpty();
		}
	return false;	
}

//////////////////////////////////////////////////	OVERLOOKER OPERATIONS

boolean addOverlooker(AgentAddress requester, Overlooker o, Object accessCard)
{
	if(groupGate != null && ! groupGate.allowOverlooking(requester, accessCard))
		return false;
	if(containsKey(o.role))
		return ((Role) get(o.role)).addOverlooker(o);
	else
	{
		Role newRole = new Role();
		put(o.role, newRole);
		return newRole.addOverlooker(o);
	}
}

boolean removeOverlooker(Overlooker o)
{
	if(containsKey(o.role))
		if (((Role) get(o.role)).removeOverlooker(o))
		{
			remove(o.role);
			return super.isEmpty();
		}
	return false;
}

//////////////////////////////////////////////////////////////////////////////////////////////////


////////////////////////////////////////////	INFO OPERATIONS
boolean isDistributed()
{
	return distributed;
}

/**test if the group has some member
@return true if there is no member. i.e. this group is a virtual one.*/
public boolean isEmpty()
{
	if(containsKey("member"))
		return ((Collection) get("member")).isEmpty();
	return true;
}

boolean isPlayingRole(AgentAddress agent,String theRole)
{
	if(containsKey(theRole))
		return ((Collection) get(theRole)).contains(agent);
	return false;
}

AgentAddress[] getRolePlayers(String roleName)
{
	if (containsKey(roleName))
		return (AgentAddress[]) ((Collection) get(roleName)).toArray(new AgentAddress[0]);
	return new AgentAddress[0];
}

AgentAddress getRolePlayer(String roleName)
{
	AgentAddress[] list = getRolePlayers(roleName);
	if (list.length > 0)
		return list[(int) (Math.random()*list.length)];
	return null;
}

String[] getRolesOf(AgentAddress agent)
{
	Collection c = new HashSet();
	for (Iterator i=entrySet().iterator(); i.hasNext(); )
	{
		Map.Entry e = (Map.Entry) i.next();
		if( ((Collection) e.getValue()).contains(agent))
			c.add(e.getKey());
	}
	return (String[]) c.toArray(new String[0]);
}	

String[] availableRoles()
{
	Collection c = new HashSet();
	for (Iterator i=entrySet().iterator(); i.hasNext(); )
	{
		Map.Entry e = (Map.Entry) i.next();
		if(! ((Collection) e.getValue()).isEmpty())
			c.add(e.getKey());
	}
	return (String[]) c.toArray(new String[0]);
}

////////////////////: for Backward Compatibility
Map mapForm()
{
	Map rolesName = new Hashtable();
	for(Iterator i = entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		if(! ((Collection)e.getValue()).isEmpty())
			rolesName.put(e.getKey(),new Vector((Collection)e.getValue()));
	}	
	return rolesName;
}

///////////////////////////////////////		IMPORT & ACCESSORs

void merge(Group g, boolean priority)
{
	if (priority)
	{
		if(g.getRolePlayer("group manager") != null)
			realCreation(g.getRolePlayer("group manager"), true, g.getDescription(),g.getGroupIdentifier());
	}		
	else
		if(getRolePlayer("group manager") == null || (isEmpty() && g.getRolePlayer("member") != null))
			realCreation(g.getRolePlayer("member"), true, getDescription(),getGroupIdentifier());
	if(isEmpty())
	{
		Role members = new Role();
		put("member",members);
	}

	for(Iterator i = g.keySet().iterator();i.hasNext();)
	{
		String roleName = (String) i.next();
		if(! roleName.equals("group manager"))
		{
			AgentAddress[] agents = g.getRolePlayers(roleName);
			for(int j = 0;j<agents.length;j++)
				roleAssignment(agents[j],roleName);
		}
	}
}

GroupIdentifier getGroupIdentifier(){return groupGate;}
String getDescription(){return description;}			
		
}
