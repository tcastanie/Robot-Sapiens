/*
* Role.java - Kernel: the kernel of MadKit
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/** Role class for MadKit 3.0
    @author Fabien Michel
    @version 2.0
    @since MadKit 3.0
*/

final class Role extends HashSet
{
	private transient Collection overlookers;
	private transient Collection referenceableAgents;
	
Role()
{
	referenceableAgents = new ArrayList();
	overlookers = new HashSet();
}

Role(int size)
{
	super(size);
	referenceableAgents = new ArrayList(size);
	overlookers = new HashSet();
}

///////////////////////////////////////////////////////		ADD & REMOVE (also update overlookers)

/**return true if the agent has been added*/
synchronized boolean addMember(AgentAddress agent)
{
	if(add(agent))
	{
		AbstractAgent theReference = Kernel.getReference(agent);
		if(theReference instanceof ReferenceableAgent )
		{
			referenceableAgents.add(theReference);
			for (Iterator i=overlookers.iterator(); i.hasNext(); )
				((Overlooker) i.next()).update(theReference,true);
		}
		return true;
	}
	return false;
}

/**return true if the role is no more usefull no member no overlooker*/
synchronized boolean removeMember(AgentAddress agent)
{
	if(remove(agent))
	{
		AbstractAgent theReference = Kernel.getReference(agent);
		if(referenceableAgents.remove(theReference))
			for (Iterator i=overlookers.iterator(); i.hasNext(); )
				((Overlooker) i.next()).update(theReference,false);
	}
	else
		return false;
	return (isEmpty() && overlookers.isEmpty());
}

/////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////	OVERLOOKER PART

/**return true if the overlooker has been sucessfully added*/
synchronized boolean addOverlooker(Overlooker o)
{
	if(overlookers.add(o))
	{
		o.setOverlookedRole( this );
		return true;
	}
	return false;
}

/**return true if the overlooker has been sucessfully removed
false means that the overlooker was not set on this role*/
synchronized boolean removeOverlooker(Overlooker o)
{
	if(overlookers.remove(o))
		return (isEmpty() && overlookers.isEmpty());
	return false;
}

synchronized List getAgentsList()
{
    if(Kernel.fastSynchronous)
	return (List) referenceableAgents;
    return new ArrayList(referenceableAgents);
}

synchronized AbstractAgent getAgentNb(int number)
{
	if(number < referenceableAgents.size())
		return (AbstractAgent) ((List)referenceableAgents).get(number);
	return null;
}

///////////////////////////////////////////////:	UTILITY WHEN IMPORTING
void update()
{
	referenceableAgents = new HashSet();
	overlookers = new HashSet(7);
}

}
