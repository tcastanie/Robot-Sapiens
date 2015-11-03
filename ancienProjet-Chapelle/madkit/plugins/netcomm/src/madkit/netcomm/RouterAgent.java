/*
 * RouterAgent.java - Created on Oct 18, 2003
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
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;
import madkit.kernel.KernelMessage;
import madkit.kernel.Message;

/**The Router Agent takes care of all outgoing Messages.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 * @version $Revision: 1.1 $
 */
class RouterAgent extends MadkitNetworkAgent {
	public static final String CONFIG="configuring";
	Hashtable routeTable=null;
	private SocketKernel myInfo=null;
	private boolean alive=true;
	private RouterGUI gui;
	
	public RouterAgent(SocketKernel info){
		routeTable=new Hashtable();
		myInfo=info;
	}
	
	////////////////////////////////////////////////////////////////////////
	//             Madkit Agent's methods
	////////////////////////////////////////////////////////////////////////
	
	
	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		while(alive){
			Message m=waitNextMessage();
			exitImmediatlyOnKill();
			if(!alive) break;
			if(m instanceof NetworkMessage){
				handleNetworkMessage((NetworkMessage) m);
			}else if(m instanceof NetConfigMessage){
				handleConfigMessage((NetConfigMessage)m);
			}else{
				debug(m.toString());
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();
		if(!isRole(community,group,"router"))
			requestRole(community,group,"router",memberCard);
		debug("Activated");
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		debug("Router killed");
		super.end();
	}
	
	////////////////////////////////////////////////////////////////////////
	//            Router Agent's method
	////////////////////////////////////////////////////////////////////////
	/**Sends a message to a Distant Kernel.
	 * @param message message to send.
	 */
	private void sendDistantMessage(KernelMessage message) {
		Message orig;
		if (message instanceof KernelMessage){
		  KernelMessage temp = (KernelMessage) message;
		  orig = (Message) (temp.getArgument());
		}else  orig = message;
		
		if(shouldUseUDP(orig)){
			println ("WARN:  No udp message routing yet!!!");
		}else{
			DistantKernelInformation info = getP2PAddress(orig);
			if(info!=null){
				AgentAddress p2p=info.getP2PAgent();
				sendMessage(p2p,new NetworkMessage(NetworkMessage.DISTANT_MESSAGE,orig));
			}else{
				println("WARN : unknown destination kernel"+orig.getReceiver().getKernel());
			}	
		}
		
	}
	
	private DistantKernelInformation getP2PAddress(Message orig) {
		DistantKernelInformation info=(DistantKernelInformation)routeTable.get(orig.getReceiver().getKernel().getID());
		return info;
	}
	
	private AgentAddress getP2PAddress(KernelAddress kernelAddress) {
		DistantKernelInformation info=(DistantKernelInformation)routeTable.get(kernelAddress.getID());
		return info.getP2PAgent();
	}

	/**checks if the message should be sent using the UDP protocol.
	 * @param msg Message to check
	 * @return true iff the message should be sent using UDP (implements UDPMessage)
	 */
	private boolean shouldUseUDP(Message msg) {
		return (msg instanceof UDPMessage);
	}

	/**Adds a route to the routing table.
	 * Before adding a new kernel, it checks if the kernel's ID already existes in the routing table. if it does, no action 
	 * is taken and returns false.
	 * @param ka KernelAddress of the distant kernel to add.
	 * @param addr AgentAddress of the P2PAgent responsable for the connection with the kernel
	 * @param dkinfo SocketKernel of the distant Kernel
	 * @return true iff the distant kernel was add. false if the kernel already has an entry in the route table
	 */
	
	private boolean  addRoute(KernelAddress ka, AgentAddress addr, SocketKernel dkinfo, String protocol){
		
		DistantKernelInformation tmp=(DistantKernelInformation) routeTable.get(ka.getID());
		
		if(tmp!=null){
			if(tmp.getProtocol().equals(CONFIG)){
				if(protocol.equals(CONFIG)){
					return false;
				}else{
					routeTable.remove(ka.getID());
				}
			}else{
				debug("Kernel "+ka+" already added");
				return false;
			}
			
		}
		
		debug("adding new kernel "+ka.getID());
		
		routeTable.put(ka.getID(),new DistantKernelInformation(ka,addr,dkinfo,protocol));
		if(!protocol.equals(CONFIG))
			sendConnectedKernelInformation(dkinfo,ka,addr,protocol);
		return true;
	}
	
	
	/**Sends a message to <code>agent</code> containing a hashSet with the kernels known by the localKernel
	 * @param agent P2PAgent that requested a Sync
	 */
	private void replyConnect(AgentAddress agent) {
		sendMessage(agent,new NetworkMessage(NetworkMessage.SYNCH_REQUEST_REPLY,new HashSet(routeTable.values())));
		
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleNetworkMessage(madkit.netcomm.NetworkMessage)
	 */
	protected void handleNetworkMessage(NetworkMessage message) {
		switch (message.getType()) {
			case NetworkMessage.SYNCH_REQUEST :
				debug("handling NetworkMessage.SYNCH_REQUEST");
				Vector vec=(Vector) message.getArgument();
				handleSynchRequest(message.getSender(),vec);
				break;
			case NetworkMessage.ROUTE_MESSAGE:
				debug("Routing message");
				sendDistantMessage((KernelMessage) message.getArgument());
				break;
				
			case NetworkMessage.CONNECT_KERNEL:
				debug("CONNECT_KERNEL received");
				Vector v1=(Vector) message.getArgument();
				Socket brSocket=(Socket)v1.get(0);
				KernelAddress id= (KernelAddress) v1.get(1);//check if the kernel is known
				SocketKernel distantSK=(SocketKernel) v1.get(2);
				
				if(id!=null && routeTable.containsKey(id)){
					debug("broadcast from known kernel");
					try {	brSocket.close();} catch (IOException e) {}
					break;
				}
				launchNetConfigConnection(distantSK,brSocket,id);
				break;
			case NetworkMessage.KERNEL_DISCONNECTED:
				routeTable.remove(((KernelAddress)message.getArgument()).getID());
				break;
			case NetworkMessage.UPDATE_ROUTE:
				debug("UPDATE_ROUTE");
				Vector v=(Vector) message.getArgument();
				KernelAddress addr=(KernelAddress) v.get(0);
				AgentAddress p2p=(AgentAddress) v.get(1);
				DistantKernelInformation info=(DistantKernelInformation) routeTable.remove(addr.getID());
				info.setP2PAgent(p2p);
				info.setProtocol((String) v.get(2));
				routeTable.put(addr.getID(),info);
				sendMessage(getAgentWithRole(community,group,"netagent"),new NetworkMessage(NetworkMessage.UPDATE_ROUTE,v));
				sendMessage(message.getSender(),new NetworkMessage(NetworkMessage.UPDATE_ROUTE_DONE,addr));
				break;
			default :
				debug("unknown NetworkMessage Received");
				debug(message.toString());
				break;
		}
		
	}


	/**Handles a request of a P2PAgent.<br>
	 * The vector must contain:<br>
	 * the KernelAddress<br>
	 * the Distant SocketKernel<br>
	 * the AgentAdress of the distant Kernel<br>
	 * the Connection protocol<br>
	 * the Kernels known by the distant kernel.This can be either the DistantKernelInFormation (used by netcomm)
	 * or just the socketKernel. (for compatibility with 3.1)<br>
	 * @param address AgentAddress of the P2PAgent requesting the Sync
	 * @param vec Information to sync
	 */
	private void handleSynchRequest(AgentAddress sender, Vector vec) {
		Enumeration a = vec.elements();
		KernelAddress distantK = (KernelAddress)a.nextElement();
		SocketKernel distantSI = (SocketKernel) a.nextElement();
		AgentAddress distantKernel = (AgentAddress)a.nextElement();
		String protocol=(String)a.nextElement();
		Collection otherKernels = (Collection)a.nextElement();
		if (addRoute(distantK,sender,distantSI,protocol))//el de ack
		{
			replyConnect(sender);
			for(Iterator i = otherKernels.iterator();i.hasNext();)
			{
				Object o=i.next();
				if( o instanceof DistantKernelInformation){
					DistantKernelInformation distantInfo=(DistantKernelInformation) o;
					if(! ( myKernel.getKernel().getID().equals(distantInfo.getDistantKernelAddress().getID())
						|| isConfiguring(distantInfo.getDistantKernelAddress())
						|| isKnownKernelSocket(distantInfo.getSocketKernel())) ){
							launchNetConfigConnection(distantInfo.getSocketKernel(),null,distantInfo.getDistantKernelAddress());
					}	
				}else if (o instanceof SocketKernel){
					SocketKernel distantSocket = (SocketKernel) o;
					if(!(myInfo.equals(distantSocket) || isKnownKernelSocket(distantSocket))){
						launchNetConfigConnection(distantSocket,null,null);
					}
				}
				
				
			}
		}else{
			debug("Adding Route failed");
			if(!sender.equals(getP2PAddress(distantK)))//kill the agent.. the route is handled by someone else
				sendMessage(sender,new NetworkMessage(NetworkMessage.DIE,null));
		}
		
		
		
	}


	/**
	 * @param address
	 * @return
	 */
	private boolean isConfiguring(KernelAddress address) {
		return routeTable.containsKey(address.getID());
	}

	/**Launches a new NetConfigAgent to configure and establish a connection with a distant Kernel.
	 * @param distantSocket SocketKernel of the distant kernel.
	 */
	private void launchNetConfigConnection(SocketKernel distantSocket, Socket socket, KernelAddress dka) {
		try {
			if(socket==null){
				socket=new Socket(distantSocket.getHost(),distantSocket.getPort());
			}
			NetConfigAgent configa=new NetConfigAgent(socket,myInfo,distantSocket,new HashSet(routeTable.values()));
			String name="netconfig";
			if(distantSocket!=null){
				name+=distantSocket.getHost()+distantSocket.getPort();
			}
			launchAgent(configa,name,false);
			if(dka!=null){
				addRoute(dka,configa.getAddress(),distantSocket,CONFIG);
			}
			
		} catch (UnknownHostException e) {
			//debug("UnknownHostException caught ",e);
		} catch (IOException e) {
			//debug("IOException caught ",e);
		}
		
		
	}

	/**Checks if the the kenel listening on the SocketKernel has already been added to the routing Table.
	 * @param distantKernel SocketKernel of the distant Kernel
	 * @return true iff the kenel listening on the SocketKernel <code>distantKernel</code> is known
	 */
	private boolean isKnownKernelSocket(SocketKernel distantKernel) {
		Iterator it=routeTable.values().iterator();
		while (it.hasNext()) {
			DistantKernelInformation element = (DistantKernelInformation) it.next();
			if(element.getSocketKernel().equals(distantKernel)) return true;			
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleConfigMessage(madkit.netcomm.NetConfigMessage)
	 */
	protected void handleConfigMessage(NetConfigMessage message) {
		if(message.getSender()==getAgentWithRole(community,group,"netagent"))
			setDebug(NetConfigMessage.isDebug());	
	}
	
	/**Informs the NetAgent that a new kernel has been connected. 
	 * @param distantSocket SocketKernel of the distant Kernel
	 * @param distantKernel KernelAddress of the distant Kernel
	 * @param p2p P2PAgent responsable for the connection.
	 */
	protected void sendConnectedKernelInformation(SocketKernel distantSocket,KernelAddress distantKernel,AgentAddress p2p, String protocol){
		AgentAddress addr=getAgentWithRole(community,group,"netagent");
		Vector v=new Vector();
		v.add(distantSocket.getHost()+":"+distantSocket.getPort());
		v.add(distantKernel);
		v.add(p2p);
		v.add(protocol);
		sendMessage(addr,new NetworkMessage(NetworkMessage.KERNEL_CONNECTED,v));
	}
	
}
