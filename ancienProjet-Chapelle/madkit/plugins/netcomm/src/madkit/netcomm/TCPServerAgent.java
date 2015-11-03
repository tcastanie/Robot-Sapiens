/*
 * NetSocketAgent.java - Created on Oct 18, 2003
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
import java.net.ServerSocket;
import java.net.Socket;


/** The TCPServerAgent waits for new connections.
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class TCPServerAgent extends MadkitNetworkAgent {
	
	private final SocketKernel myInfo;
	private ServerSocket server;
	private boolean alive=true;
	
	public TCPServerAgent(int port){
		myInfo=createServer(port);

	}

	private SocketKernel createServer(int port) {
		
		boolean done=false;
		while(!done){
			try {
				server=new ServerSocket(port);
				done=true;
				break;
			} catch (IOException e) {
				port++;
			}
		}
	
		return new SocketKernel(port);

	}

	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		while(alive){
			try {
				Socket socket = server.accept();
				exitImmediatlyOnKill();
				if(!alive) break;
				launchAgent(new NetConfigAgent(socket,myInfo,null),"configAgent",false);
			} catch (IOException e) {}			
		}
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		requestRole(community,group,"tcpsserveragent",memberCard);
		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
			debug("IOException caught : "+e.getMessage());
		}
	}

	/**
	 * @return
	 */
	public SocketKernel getSocketKernel() {
		return myInfo;
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
		// TODO Auto-generated method stub
		
	}

}
