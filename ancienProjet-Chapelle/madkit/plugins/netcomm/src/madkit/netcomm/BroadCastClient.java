/*
 * BroadCastClient.java - Created on Nov 4, 2003
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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;
import java.util.Vector;

import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;

/**The BroadCastClient listens for incomming announcements.
 * If Broadcasts are being accepted, a netconfigAgent will be launched
 * to connect with the new kernel.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class BroadCastClient extends MadkitNetworkAgent {

	private MulticastSocket socket;
	private DatagramPacket packet;
	private InetAddress addressGroup;
	private boolean acceptBroadcast=NetConfigMessage.isAcceptBroadCast();
	private boolean alive=true;
	
	public BroadCastClient(){
		
		try {
			addressGroup = InetAddress. getByName(UDPServerAgent.MADKIT_BROADCAST_ADDR_GROUP);
		} catch (UnknownHostException e1) {
			debug("UnknownHostException caught "+e1.toString());
		}
		int i=0;
		while(i<5){
			try{		
				socket = new MulticastSocket(UDPServerAgent.DEFAULT__BROADCAST_PORT+i);
		
				//join a Multicast group and send the group salutations
		
				socket.joinGroup(addressGroup);
				byte[] data = new byte[1024];
				packet = new DatagramPacket(data,data.length);
				i=5;
			}catch (IOException e) {
				i++;
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleNetworkMessage(madkit.netcomm.NetworkMessage)
	 */
	protected void handleNetworkMessage(NetworkMessage message) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleConfigMessage(madkit.netcomm.NetConfigMessage)
	 */
	protected void handleConfigMessage(NetConfigMessage message) {
		acceptBroadcast=NetConfigMessage.isAcceptBroadCast();
		if(!acceptBroadcast)
			println("BroadCastClient not accepting messages");
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();
		requestRole(community,group,"broadcastclient",memberCard);
		debug("BroadcastClient active");
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		while(alive){
			try {
				socket.receive(packet);
				if(acceptBroadcast){
					boolean ok=true;
					debug("receiving broadcast");
					exitImmediatlyOnKill();
					byte[] data=packet.getData();
					String str = new String(data);
					if(str.startsWith(UDPServerAgent.MADKIT_PROTO)){//check if it is a madkit broadcast
						int start=str.lastIndexOf('/')+1;
						String s=str.substring(start);
						StringTokenizer tk=new StringTokenizer(s,":",false);
						String kid=tk.nextToken();
						if(kid.equals(myKernel.getKernel().getID())){//check if the broadcast if from the localKenel
							debug("broadcast from localkernel");
							ok=false;
						}
						if(ok){							
							String host=tk.nextToken();							
							String sport=tk.nextToken();							
							int port =Integer.valueOf(sport).intValue();
							sendConnectionRequest(host,port,new KernelAddress(host+":"+kid));
						}
					}else{
						debug("unknown broadcast received: "+str);
					}
				}	
			} catch (IOException e) {				
				closeSocket();
			}catch (Exception e) {//if the string is malformed .. don't stop the listener
				
			}
					
			
		}
	}

	/**Close the multicast socket.
	 * 
	 */
	private void closeSocket() {
		alive=false;
		if(!socket.isClosed())
			socket.close();
		
	}
	
	/**Requests the router to connect to the distant kernel.
	 * First it tries to create the socket, if its possible, sends the message to 
	 * the router.
	 * @param host distant host
	 * @param port distant port
	 */
	private void sendConnectionRequest(String host,int port,KernelAddress requestingKernelID){
		debug("broadcast request received by "+host+":"+port);
		
		try {
			Socket sock=new Socket(host,port);//try to connect... 
			AgentAddress router=getAgentWithRole(community,group,"router");
			Vector v=new Vector();
			v.add(sock);
			v.add(requestingKernelID);
			v.add(new SocketKernel(host,port));
			
			sendMessage(router,new NetworkMessage(NetworkMessage.CONNECT_KERNEL,v));
		} catch (UnknownHostException e) {
			debug("UnknownHostException caught "+e.getMessage());
		} catch (IOException e) {
			debug("IOException caught "+e.getMessage());
		}
	}
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		closeSocket();
		println("BroadcastClient Closed");
		
	}

}
