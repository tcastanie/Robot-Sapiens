/*
 * P2PAgent.java - Created on Oct 18, 2003
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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import madkit.kernel.AgentAddress;
import madkit.kernel.InvalidAddressException;
import madkit.kernel.Kernel;
import madkit.kernel.KernelAddress;
import madkit.kernel.Message;
import madkit.kernel.NetworkRequest;

/**Generic Agent for the Communications protocols.
 * Implements some utility methods to make the coding easier.<br>
 * You can use an InputProcessor to handler incomming data. For this, you must override the <code>receiveIncomming()</code> method
 * and associate this agent this an InputProcessor.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
abstract class P2PAgent extends MadkitNetworkAgent {
	
	
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();
		requestRole(community,group,"p2pagent",memberCard);
		
	}
	
	/**Injects a message into the local Kernel
	 * @param msg message to a local agent
	 */
	protected final void injectMessage(Message msg){
		sendMessage(myKernel,new NetworkRequest(NetworkRequest.INJECT_MESSAGE, msg));
	}
	
	/**Whether o not the agent can close his connection and 
	 * move to the StandBy Mode.
	 * The StandBy mode is performed if the trafic is to low. Then
	 * the connection is closed until new information needs to be sent.
	 * @return true if the agent's protocol supports the StandBy Mode
	 */
	protected boolean canStandBy(){
		return false;
	}
	
	/**Informs the concerned agents that the communication with the DistantKernel ka 
	 * has been lost
	 * @param ka
	 */
	protected void sendDisconnectedKernelInformation(KernelAddress ka){
		AgentAddress addr=getAgentWithRole(community,group,"netagent");
		sendMessage(addr,new NetworkMessage(NetworkMessage.KERNEL_DISCONNECTED,ka));
		addr=getAgentWithRole(community,group,"router");
		sendMessage(addr,new NetworkMessage(NetworkMessage.KERNEL_DISCONNECTED,ka));
	}

	/**Kernel address of the distant Kernel*/
	protected KernelAddress distantKernel;
	/**Output stream*/
	protected DataOutputStream out = null;
	/**Input stream*/
	protected DataInputStream in = null;

	/**Opens the Input/Ouput Streams. 
	 * @param socket communicatio socket.
	 */
	protected void openInputOutputStreams(Socket socket) {
		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e) {
			debug("IOException caught : "+e.getMessage());
		}
	}

	/**Sends an object to the distant conection. 
	 * @param object Object to send.
	 */
	protected void sendObject(Object object) {
		try{
			
			byte[] data=Codec.encode(object);
			out.writeInt(data.length);//first send the size
			out.flush();
			out.write(data); //now the object
			out.flush();
			bytesSent(data.length);//log the number bytes sent to the other side.
			}
		catch(IOException e){
			println("Unable to send TCP message to "+distantKernel.getID()+" = "+e);
			closeSocket();
		}
	
	}

	/**Exchanges the kernels information. 
	 * @param socket
	 * @return The distant kernelAddress or null if the is the local kernel
	 * @throws IOException
	 * @throws InvalidAddressException
	 */
	protected KernelAddress exchangeKernelInformation(Socket socket) throws IOException, InvalidAddressException {
		if(myKernel==null){
			myKernel=getAgentWithRole(Kernel.DEFAULT_COMMUNITY,"communications","site");
		}
		
		
		String kernel=myKernel.getKernel().getID();
		out.writeInt(kernel.length());
		out.writeChars(kernel);
		out.flush();
		out.writeInt(UDPServerAgent.getPort());
		out.flush();
		
	
		int size2=in.readInt();	
	
		KernelAddress ka;
		kernel="";
		for (int i=0;i<size2;i++){
			kernel=kernel+in.readChar();
		}
	
		int datagramSocketPort=in.readInt();
	
		ka=new KernelAddress(socket.getInetAddress().getHostName()+":"+kernel.toString());
		debug("requesting kernel: "+ka);
		
		if(kernel.equals(myKernel.getLocalID())){
			debug("trying to add my own kernel");
			return null;
		}
		return ka;
	}

	/**Closes the Socket used in this communication. 
	 */
	protected abstract void closeSocket();
	
	/**Gets the protocol associated with this Agent Type
	 * @return the protocol
	 */
	protected abstract String getProtocol();

	/**
	 * Override this method if you want an InputProcessor to handle incomming data. 
	 */
	void receiveIncomming() {}

	protected Object receiveObject() throws IOException, ClassNotFoundException {
		Object a;
		debug("Waiting incomming data");
		int len = in.readInt(); 
		debug("R:done "+len+" preparing array");
		if(len<0){
			return null;
		}
		byte[] byteobj = new byte[len];
		in.readFully(byteobj); 
		
		a=Codec.decode(byteobj);
		bytesReceived(len);
		return a;
	}

	protected final void fowardToKernel(Message m) {
		
		 AgentAddress aa=getAgentWithRole(community,group,"netagent");
		sendMessage(aa,new NetworkMessage(NetworkMessage.FOWARD_TO_KERNEL, m));
	}
}
