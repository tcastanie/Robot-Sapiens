/*
 * NetworkMessage.java - Created on Oct 19, 2003
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

import madkit.kernel.Message;

/**Internal communication messages.
 * Used for request within the netcomm MAS.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
public final class NetworkMessage extends Message {
	/**This type of messages request to the Router agent to build the HashSet
	 * containing the known kernel, their SocketKernels and the agent to route it.
	 * Althought the last information is not used at this moment, it will be used in future version to provide
	 * decentralized route when possible.
	 * The argument must containt a vector containing only the SocketKernels known by
	 * the distant Kernel.
	 * @see NetworkMessage#SYNCH_REQUEST_REPLY
	 */
	public static final int SYNCH_REQUEST=0;
	
	/**This type of message is the reponse of the RouterAgent to the sender 
	 * of a <i>SYNCH_REQUEST</i>
	 * The sent argument is a HashSet containg DistantKernelInformation objects.
	 * @see NetworkMessage#SYNCH_REQUEST
	 * 
	 */
	public static final int  SYNCH_REQUEST_REPLY=1;
	/**This type of message is used to inform of a new Kernel
	 * connection.
	 * The argument contains a String with the host and port
	 */
	public static final int  KERNEL_CONNECTED=2;
	
	/**This type of message is used to inform of a new Kernel
	 * disconnection.
	 * The argument is null.
	 */
	public static final int  KERNEL_DISCONNECTED=3;
	/**Used to demand a P2PAgent to close the connection with its 
	 * distant kernel 
	 * 
	 */
	public static final int  DISCONNECT=4;
	/**Used to request the Netagent to forward a message to the
	 * local kernel. This is need to ensure that the communications
	 * are handled by the Communicator.(This is tested by the site 
	 * agent. eg. for new connections)
	 * the Argument is the message to foward.
	 */
	public static final int  FOWARD_TO_KERNEL=5;

	
	/**Used to indicate a P2PAgent that the argument is a distant
	 * message that should be sent to the kernel on the other end.
	 */
	public static final int DISTANT_MESSAGE=6;
	
	/**Used to ask the Router Agent to Route a message;
	 * */
	public static final int ROUTE_MESSAGE=7;
	
	/**Used to request the RouterAgent to create a connection
	 * with a distant client
	 * The argument is a vector containing: 
	 * 0. the socket to use in the connection.<br>
	 * 1. the KernelAddress ID if it's known (Broadcast) or null<br>
	 * 2. the SocketKernel if known.
	 * 
	 */
	public static final int CONNECT_KERNEL=8;
	
	
	/**Used to kill the p2pAgents*/
	public static final int DIE=9;
	
	/**Used to request a multipleConnectionHandler to handle the communication 
	 * with a distant kernel. The argument contains a vector with:<br>
	 *  0. the socket <br>
	 *  1. a hashset with the distant kernles<br>
	 */
	public static final int HANDLE_KERNEL_COMM=10;
	
	/**Used to request the router to update the agent who connects the kernel with a distant kernel.<br>
	 * The Argument contains a Vector with:<br>
	 * 0. The KernelAddress of the concerned kernel.<br>
	 * 1. The AgentAddress of the P2PAgent responsible for the connection.<br>
	 * 2. The new protocol.
	 *  */
	public static final int UPDATE_ROUTE=11;
	
	/**Used to ack a request to update a route. <br>
	 * The Argument containts:
	 * the kernelAddress of the updated route.*/
	public static final int UPDATE_ROUTE_DONE=12;
	
	
	//////////////////////////////END OF TYPES
	
	/**Message type*/
	final int type;
	/**Message type dependant argument*/
	private final Object argument;

	/**Creates a NetworkMessage type.
	 * @param type Type of the message
	 * @param arg message type dependant Argument.
	 */
	public NetworkMessage(int type,Object arg) {
		super();
		this.type=type;
		this.argument=arg;
	}
	

	/**Gets the message type
	 * @return The type of message
	 */
	public int getType(){
		return type;
	}
	

	/**Gets the argument of the message.
	 * Please refer to the different message types to check its return.
	 * @return the message type
	 */
	public Object getArgument(){
		return argument;
	}
}
