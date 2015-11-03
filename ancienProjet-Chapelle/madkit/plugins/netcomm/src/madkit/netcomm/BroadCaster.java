/*
 * BroadCaster.java - Created on Nov 4, 2003
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

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

/**Simple class to broadcast the presence of a new kernel in the network.
 * After the broadcast, the interested kernels will contact the local NetAgent(actually
 * the TCPServerAgent) to initiate a connection.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
final class BroadCaster {
	
	public static void broadcast(String kernelID,String host,int port) throws IOException{
		
		MulticastSocket socket;
		DatagramPacket packet;
		InetAddress addressGroup;

		addressGroup = InetAddress.getByName(UDPServerAgent.MADKIT_BROADCAST_ADDR_GROUP);
		
		socket = new MulticastSocket();
		// join a Multicast group and send the group salutations
		
		socket.joinGroup(addressGroup);
		byte[] data = null;
	
			
		String str =UDPServerAgent.MADKIT_PROTO+kernelID+":"+host+":"+port+":";
		data = str.getBytes();
		if(data.length<1024){
			
		}
		
		
		for(int i=UDPServerAgent.DEFAULT__BROADCAST_PORT;i<UDPServerAgent.DEFAULT__BROADCAST_PORT+5;i++){
				packet = new DatagramPacket(data,str.length(),addressGroup,i);	
				//Sends the packet
				 socket.send(packet);
		}
		
		
		
	}  
}
