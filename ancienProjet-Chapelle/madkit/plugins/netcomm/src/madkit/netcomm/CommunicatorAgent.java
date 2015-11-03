/*
 * CommunicatorAgent.java - Created on Oct 18, 2003
 * 
 * Copyright (C) 2003 Sebastian Rodriguez -- 
 * Based on the code of Copyright (C) 1998-2002 Olivier Gutknecht, Pierre Bommel, Fabien Michel, Thomas Cahuzac
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
 * Last Update: $Date: 2003/12/19 10:14:50 $
 */

package madkit.netcomm;

import java.io.IOException;
import java.net.Socket;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;
import madkit.kernel.Message;
import madkit.kernel.NetworkRequest;

/**The Communicator Agent is used to communicate with old versions of
 * MadKit.
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.2 $
 */
class CommunicatorAgent extends P2PAgent {
	protected Socket socket; //my socket,
	private boolean alive=true; //is the agent alive?
	protected InputProcessor input; //a processor for the input.
	protected SocketKernel myInfo;//contains the information of the local TCPAgent
	protected HashSet dKernels=null;
	

	public CommunicatorAgent(KernelAddress ka, final Socket sock,final SocketKernel info){
		socket=sock;
		myInfo=info;
		if(socket.isClosed()){
			throw new IllegalArgumentException("Can not work with a closed socket");
		}
		distantKernel=ka;
		
		
	}

	public CommunicatorAgent(KernelAddress ka,final Socket sock,final SocketKernel info, HashSet dKernels){
		this(ka,sock,info);
		this.dKernels=dKernels;
	}
	////////////////////////////////////////////////////////////////////////
	//             Madkit Agent's methods
	////////////////////////////////////////////////////////////////////////
	
	
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();
		openInputOutputStreams(socket);
		myKernel=getAgentWithRole("communications","site");
		debug(" Activated - connecting to "+distantKernel);
		input=new InputProcessor(this);
		input.start();
	}


	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		if(dKernels!=null){
			sendObject(buildSynch('c',dKernels));
		}
		
		while (alive){
				exitImmediatlyOnKill();
				
				Message m=waitNextMessage();
				if(!alive) continue;
				if(m instanceof NetworkMessage){
					handleNetworkMessage((NetworkMessage)m);
				}else if(m instanceof NetConfigMessage){
					handleConfigMessage((NetConfigMessage)m);
				}else sendObject(m);
				
			}
			println("Connexion lost with "+distantKernel+".");
	}


	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#closeSocket()
	 */
	protected void closeSocket() {
		debug("Closing Socket");
		sendDisconnectedKernelInformation(distantKernel);
		try {
			alive=false;
			if(!input.isStopped())
			input.stop();
			pause(10);
			if(!socket.isClosed())
				socket.close();
			
		} catch (IOException e) {}
	}


	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		debug("Communicator agent finished");
		closeSocket();
	}
	////////////////////////////////////////////////////////////////////////
	//             Communicator Agent's methods
	////////////////////////////////////////////////////////////////////////
	


	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleConfigMessage(madkit.netcomm.NetConfigMessage)
	 */
	protected void handleConfigMessage(NetConfigMessage message) {
		setDebug(NetConfigMessage.isDebug());
		
	}


	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleNetworkMessage(madkit.netcomm.NetworkMessage)
	 */
	protected void handleNetworkMessage(NetworkMessage message) {
		switch (message.getType()) {
			case NetworkMessage.SYNCH_REQUEST_REPLY :
				debug("SYNCH_REQUEST_REPLY received");
				sendObject(buildSynch('r',(HashSet) message.getArgument()));
				break;
			
			case NetworkMessage.DISCONNECT:
				debug("Request to Disconnect received");
				closeSocket();
				break;
				
			case NetworkMessage.DISTANT_MESSAGE:
				sendObject(message.getArgument());
				break;
				
			case NetworkMessage.DIE:
				debug("DIE received");
				closeSocket();
				break;
			default :
				debug("UnKnown Message Type");
				debug(message.toString());
				break;
		}
		
	}


	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#receiveIncomming()
	 */
	void receiveIncomming() {
		try{
			Object a=null;
			a = receiveObject();
			if (a instanceof Message){
				Message aMessage = (Message)a;
				injectMessage(aMessage);
			}else if (a instanceof Vector){
				requestSynch((Vector) a);
			}
				
			
		}catch(IOException e3){
			closeSocket();
		}catch (ClassNotFoundException e) {
			debug("ClassNotFoundException : "+e.getMessage());
		}
	}


	/**Requests the Sync of Kernels to the Router Agent.
	 * @param args The Vector received from the other side.
	 */
	private void requestSynch(Vector args){
		Collection otherKernels=null;//este vector va a contener los kernels distantes
		Vector v=new Vector();
		Enumeration a = args.elements();
		 String trafficOperation = (String) a.nextElement();
		 AgentAddress kaddr=(AgentAddress) args.get(3);//AgentAddres del kernel distante
		 
		 if (trafficOperation.equals("ACK_SOCKET_INFO"))
		 {
			 debug("  ---------SocketCom--------- ACK_SOCKET_INFO ---- ");
			 
		 }

		 if (trafficOperation.equals("TRANSMIT_SOCKET_INFO"))
		 {
			 debug("  ---------SocketCom------- TRANSMIT_SOCKET_INFO ---- ");
			
		 }
		//create a compatible SocketKernel 
		v.add(args.get(1));//kernelAddres
		madkit.communicator.SocketKernel sk=(madkit.communicator.SocketKernel) args.get(2);
		v.add(new SocketKernel(sk.getHost(),sk.getPort()));//socketKernle
		v.add(args.get(3));//kernel's AgentAddress
		v.add(getProtocol());
		v.add(formatOtherKernels((Collection) args.get(4)));//otherKernels
		 //envio los kernels del kernel distante a al router para q genere nuevas conexiones.
		sendMessage(community,group,"router",new NetworkMessage(NetworkMessage.SYNCH_REQUEST,v));
		if (trafficOperation.equals("TRANSMIT_SOCKET_INFO")){
			fowardToKernel( new NetworkRequest(NetworkRequest.CONNECTION_REQUEST, kaddr));
		}
		
	}
	

	
	/**
	 * @param object
	 * @return
	 */
	private Collection formatOtherKernels(Collection ok) {
		Vector v=new Vector();
		for (Iterator iter = ok.iterator(); iter.hasNext();) {
			madkit.communicator.SocketKernel element = (madkit.communicator.SocketKernel) iter.next();
			v.add(new SocketKernel(element.getHost(),element.getPort()));
		}
		return v;
	}

	//	Construction d'un vecteur de synchronisation.
	 // c signifie connect et r signifie reply.
	/* En el protocol el orden en el vector es
	 * 1. El kernelAddress del kernel local
	 * 2. el SocketKernel local
	 * 3. el AgentAddress del Kernel
	 * 4. un hashset con los kernels distantes.
	 * */
	/**Create a Vector Compatible with Madkit 3.1*/
	 private Vector buildSynch(char s,HashSet dKernels)
	 {
		 Vector v = new Vector();

		 if (s == 'c') v.addElement("TRANSMIT_SOCKET_INFO");
		 else if (s == 'r') v.addElement("ACK_SOCKET_INFO");

		 v.addElement(getAddress().getKernel());
		madkit.communicator.SocketKernel sktemp=new madkit.communicator.SocketKernel(myInfo.getHost(),myInfo.getPort());
		 v.addElement(sktemp);//need to change the socketKernel object to keep backwards comp
		 v.addElement(myKernel);
		 Collection scks=new Vector();
		 for (Iterator iter = dKernels.iterator(); iter.hasNext();) {
			DistantKernelInformation info=(DistantKernelInformation) iter.next();
			madkit.communicator.SocketKernel sk=new madkit.communicator.SocketKernel(info.getSocketKernel().getHost(),info.getSocketKernel().getPort());//need to change the socketKernel object to keep backwards comp
			 scks.add(sk);
		 }
		 v.addElement(new HashSet(scks));
		 return v;
	 }
	
	////////////////////////////////////////////////////////////////////////
	//             P2PAgent methos
	////////////////////////////////////////////////////////////////////////

	
	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#getProtocol()
	 */
	protected String getProtocol() {
		return KnownProtocols.COMM_PROTO;
	}

	
}

