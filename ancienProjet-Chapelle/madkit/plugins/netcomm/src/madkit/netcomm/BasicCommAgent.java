/*
 * BasicCommAgent.javaCreated on Dec 13, 2003
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
 *Last Update $Date: 2003/12/17 16:33:14 $

 */
package madkit.netcomm;


import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

import madkit.kernel.AgentAddress;
import madkit.kernel.Kernel;
import madkit.kernel.KernelAddress;
import madkit.kernel.KernelMessage;
import madkit.kernel.Message;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
class BasicCommAgent extends P2PAgent {
	final static String handledProtocol="basiccommprotocol";
	private InputProcessor _input;
	private Socket _socket;
	private boolean alive=true;
	private KernelAddress _distantKernel;
	private int _dynamicPort;
	
	/*------------------------------------------------------------------------------------*/
	 /**
	 * @param s
	 */
	public BasicCommAgent(Socket s, KernelAddress distantKernel,int port) {
		_socket=s;
		_distantKernel=distantKernel;
		_dynamicPort=port;
		openInputOutputStreams(_socket);
		_input=new InputProcessor(this);
		_input.start();
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#closeSocket()
	 */
	protected void closeSocket() {
		try {
			alive=false;
			if(!_socket.isClosed()) _socket.close();
			if(!_input.isStopped()) _input.stop();
			sendDisconnectedKernelInformation(_distantKernel);
		} catch (IOException e) {
			debug("IOException caught : "+ e.getMessage());
		}

	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#getProtocol()
	 */
	protected String getProtocol() {
		return handledProtocol.toString();
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleNetworkMessage(madkit.netcomm.NetworkMessage)
	 */
	protected void handleNetworkMessage(NetworkMessage message) {
		switch (message.getType()) {
			case NetworkMessage.DIE :
				closeSocket();
				break;
				
			case NetworkMessage.DISTANT_MESSAGE:
				sendDistantMessage((Message)message.getArgument());
				break;
			default :
				debug("unknown networkmessage type "+message.getType());
				break;
		}

	}

	/*----------------------------------------------------------------------------------------*/
	/**
	 * @param message
	 */
	private void sendDistantMessage(Message message) {
		sendObject(message);
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleConfigMessage(madkit.netcomm.NetConfigMessage)
	 */
	protected void handleConfigMessage(NetConfigMessage message) {
		// TODO Auto-generated method stub

	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();
		sendMessage(getAgentWithRole("system","kernel"),new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK,Kernel.DELETE_COMMUNITY));
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		while(alive){
			Message msg=waitNextMessage();
			exitImmediatlyOnKill();
			if(!alive) return;
			if(msg instanceof NetworkMessage){
				handleNetworkMessage((NetworkMessage) msg);
			}else if (msg instanceof NetConfigMessage){
				handleConfigMessage((NetConfigMessage) msg);
			}else if(msg instanceof KernelMessage){
				handleKernelMessage((KernelMessage)msg);
			}
		}
	}

	/**
	 * @param message
	 */
	private void handleKernelMessage(KernelMessage message) {
		switch (message.getType()) {
			case Kernel.DELETE_COMMUNITY :
				debug("Shares no community");
				if(!sharesCommunity((String) message.getArgument())){
					Vector v=new Vector();
					v.add(_socket);
					v.add(_distantKernel);
					v.add(new Integer(_dynamicPort));
					//sendMessage(getAgentWithRole(community,group,SocketDynamicConnection.ROLE),new NetworkMessage(NetworkMessage.HANDLE_KERNEL_COMM,v));
				}
				break;

			default :
				break;
		}
		
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#receiveIncomming()
	 */
	void receiveIncomming() {
		try {
			Object o=super.receiveObject();
			if(o instanceof Message){
				injectMessage((Message) o);
			}else {
				debug("Unknown object received");
			}
		} catch (IOException e) {
			closeSocket();
			debug("IOException caught : "+ e.getMessage());
		} catch (ClassNotFoundException e) {
			closeSocket();
			debug("ClassNotFoundException caught : "+ e.getMessage());
		}
	}

	protected boolean sharesCommunity(String createdCommunity){
		AgentAddress aas[]=getAgentsWithRole("communities",createdCommunity,"site");
		
		for(int i=0; i < aas.length;i++){
			if(_distantKernel.getID().equals(aas[i])) return true;
		}
		return false;
	}
	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		closeSocket();
	}

}
