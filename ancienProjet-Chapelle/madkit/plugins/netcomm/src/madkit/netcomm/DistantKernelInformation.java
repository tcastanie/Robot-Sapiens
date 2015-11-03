/*
 * DistantKernelInformation.java - Created on Oct 19, 2003
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


import java.io.Serializable;

import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;

/**Contains all information necesary about a distant connection.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class DistantKernelInformation implements Serializable{

	private AgentAddress p2pAgent;
	private final SocketKernel info;
	private int udpPort;
	private String protocol;
	private final KernelAddress distantKernelAddress;
	
	/**Contains all information needed to route messages.
	 * @param agent The agent in charge of that connection
	 * @param info The SoketKernel information of the  Distant Kernel
	 * @param udpPort The udp Port of that Kernel.
	 */
	public DistantKernelInformation(KernelAddress distantKA, AgentAddress agent ,SocketKernel info, String proto) {
		p2pAgent=agent;
		this.info=info;
		distantKernelAddress=distantKA;
		//this.udpPort=udpPort;
		this.protocol=proto.toString();
	}
	
	public SocketKernel getSocketKernel(){
		return info;
	}
	
	public AgentAddress getP2PAgent(){
		return p2pAgent;
	}
	
	public void setP2PAgent(AgentAddress p2p){
		p2pAgent=p2p;
	}

	public String getProtocol(){ return protocol.toString();}
	public void setProtocol(String proto){
		protocol=proto.toString();
	}
	
	public String getHost(){
		return info.getHost();
	}
	/**
	 * @return Returns the distantKernelAddress.
	 */
	public final KernelAddress getDistantKernelAddress() {
		return distantKernelAddress;
	}

}
