/*
* GroupIdentifier.java - Kernel: the kernel of MadKit
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



/** this interface is implemented by objects which are used to verify if an agent,
  the *requester*, can safely enter a group with the role *roleName* and
  the access object *memberCard*

    @author Fabien Michel
    @version 1.0

*/

public interface GroupIdentifier extends java.io.Serializable
{
	public boolean allowAgentToTakeRole(AgentAddress requester, String roleName, Object memberCard);
	public boolean allowOverlooking(AgentAddress requester, Object accessCard);
}

