/*
 * NetworkIdentifier.java - Created on Oct 18, 2003
 * 
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Last Update: $Date: 2003/12/17 16:33:14 $
 */

package madkit.netcomm;

import madkit.kernel.AgentAddress;
import madkit.kernel.GroupIdentifier;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class NetworkIdentifier implements GroupIdentifier {

	/**
	 * 
	 */
	public NetworkIdentifier() {
		super();
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.GroupIdentifier#allowAgentToTakeRole(madkit.kernel.AgentAddress, java.lang.String, java.lang.Object)
	 */
	public boolean allowAgentToTakeRole(
		AgentAddress requester,
		String roleName,
		Object memberCard) {
		// TODO Auto-generated method stub
		return true;
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.GroupIdentifier#allowOverlooking(madkit.kernel.AgentAddress, java.lang.Object)
	 */
	public boolean allowOverlooking(
		AgentAddress requester,
		Object accessCard) {
		// TODO Auto-generated method stub
		return false;
	}

}
