/*
* Probe.java - Kernel: the kernel of MadKit
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

/** This class defines a watcher probe. 
    A probe is configured according to a community, a group and a role.

    @author Fabien Michel (MadKit 3.0 05/09/01) and 2.0 (Overlooker).
    @author Olivier Gutknecht version 1.0
    @since MadKit 2.0
    @version 3.0*/

public class Probe extends Overlooker
{     
	public Probe(String communityName, String groupName, String roleName)
	{
		super(communityName, groupName, roleName);
	}

	public Probe(String groupName, String roleName)
	{
		super(groupName, roleName);
	}
	
	/** this method is automatically invoked the first time the agents variable is updated so it's a good place to
	initialize activator's parameters like in the SingleMethodActivator for exemple*/
	public void initialize(){}
	
	/**this method is automatically invoked when changes occur on the considered group/role couple
	@param theAgent is the agent which have been added or removed from the agents collection (a List)
	@param added is true is theAgent has been added to the agents, false if removed from it*/
	public void update(AbstractAgent theAgent, boolean added){}
}
