/*
 * UDPServerAgent.java - Created on Oct 19, 2003
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

import java.net.DatagramSocket;
import java.net.SocketException;

import madkit.kernel.Message;


/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class UDPServerAgent extends MadkitNetworkAgent {
	
	public static int DEFAULT_PORT=4444;
	public static int DEFAULT__BROADCAST_PORT=44444;
	public static int port=DEFAULT_PORT;
	public static String MADKIT_BROADCAST_ADDR_GROUP="239.239.239.239";
	public static String MADKIT_PROTO="mka://";
	
	private DatagramSocket serverDSocket;
	
	private boolean alive=true;
	
	public UDPServerAgent(){
		int i=0;
		
		while (i < 5){
			try{
				serverDSocket = new DatagramSocket(port+i);
				port+=i;
				
				i=5;
			}catch (SocketException e){
				i++;
				debug("Could not listen on port: " + port + ", " + e);
			}
		}

	}
	
	/**get the current UDP port
	 * @return the port
	 */
	public static synchronized int getPort() {
		return port;
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

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();
		println("UDPSocket Listening on port "+serverDSocket.getLocalPort());
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		super.live();
		while(alive){
			Message msg=waitNextMessage();
			exitImmediatlyOnKill();
			if(!alive) break;
			
		}
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		
	}

}
