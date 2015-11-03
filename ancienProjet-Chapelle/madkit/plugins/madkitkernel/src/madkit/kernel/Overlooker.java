/*
* Overlooker.java - Kernel: the kernel of MadKit
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

import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

/** This kernel internal tool defines the superclass class for probe and activator.
    An overlooker is configured according to a community, a group and a role.
    The agents variable is automatically updated and the update()
    operation is invoked when changes occur on the given group and role.

    @author Fabien Michel 
    @since MadKit 2.1
    @version 3.0*/

abstract class Overlooker
{ 
	private Role overlookedRole;
	String community;
	String group;
	String role;
    
Overlooker(String communityName, String groupName, String roleName)
{
	community=communityName;
	group=groupName;
	role=roleName;
}
    
Overlooker(String group, String role)
{
	this(Kernel.DEFAULT_COMMUNITY, group, role);
}
    
synchronized final void setOverlookedRole(Role theRole)
{
	overlookedRole = theRole;
	initialize();
}

abstract public void initialize();
abstract public void update(AbstractAgent theAgent,boolean added);

/** return a ListIterator orver the agents that handle the group/role couple
@since MadKit 3.0*/
final public ListIterator getAgentsIterator()
{
	return getCurrentAgentsList().listIterator();
}

/** return a ListIterator that has been previously shuffled
@since MadKit 3.0*/
final public ListIterator getShuffledAgentsIterator()
{
	List l = getCurrentAgentsList();
	Collections.shuffle(l);
	return l.listIterator();
}

/** return a list view (a snapshot at moment t) of the agents that handle the group/role couple (in proper sequence)
@since MadKit 3.0
*/
final synchronized public List getCurrentAgentsList()
{
	return overlookedRole.getAgentsList();
}

/** return the number of the agents that handle the group/role couple*/
final public int numberOfAgents()
{
	return getCurrentAgentsList().size();
}

final synchronized public AbstractAgent getAgentNb(int nb)
{
	return overlookedRole.getAgentNb(nb);
}

final public String getCommunity()  {	return community;   }
final public String getGroup()  {	return group;   }
final public String getRole()   {	return role;    }

public String toString()
{
	return this.getClass().getName()+" on <"+community+";"+group+";"+role+">, agents "+"are "+getCurrentAgentsList().size();
}    
}
