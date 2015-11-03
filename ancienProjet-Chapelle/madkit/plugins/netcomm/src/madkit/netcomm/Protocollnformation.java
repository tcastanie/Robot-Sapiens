/*
 * Protocollnformation.java - Created on Nov 4, 2003
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

import java.util.Collection;

/**Provides information of the soported protocols.
 * In the new Communication system an object implementing this interface
 * is sent to inform of the possible protocols that can be used.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
public interface Protocollnformation {
	/**Searches for the best protocol to use from the receivers point of
	 * view. It must take the first protocol of the local protocols as search it 
	 * among the protocols in <code>info</code>. If found this is the protocol
	 * that will be used.
	 * @param info distant kernel known protocols
	 * @return the best protocol to use or null if no match is found
	 */
	public String getBestProtocol(Protocollnformation info);
	/**Gets the known protocols.
	 * @return a collection of strings with the protocols name. 
	 */
	public Collection getProtocols();
	
	/**Is the Connection refused??
	 * @return
	 */
	public boolean refused();
	
	/**Reason of the distant Kernel to refuse the connection.
	 * @return
	 */
	public String refuseReason();
	
	/**Returns the version of the the Protocol information
	 * @return
	 */
	public String getVersion();
	
	/**Returns a Collection of parameters to further configure a the protocols
	 * @return
	 */
	public Collection getExtraParameters();
}
