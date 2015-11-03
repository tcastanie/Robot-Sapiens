/*
 * SimpleSSLAgent.javaCreated on Dec 5, 2003
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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import madkit.kernel.KernelAddress;

/**Secured communications agent. Based on the Communicator agent
 * but the communication are through a SSLSocket :)
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
class SimpleSSLAgent extends CommunicatorAgent {
	private SSLSocket sslSocket;
	
	static{
		SSLConfigurator.configure();
	}
	
	/**Creates a SimpleSSLAgent in passive mode. 
	 * @param ka
	 * @param sock
	 * @param info
	 */
	public SimpleSSLAgent(KernelAddress ka, Socket sock, SocketKernel info) {
		super(ka,sock,info);
	}

	/**Creates a SimpleSSLAgent in passive mode. 
	 * @param ka
	 * @param sock
	 * @param info
	 * @param dKernels
	 */
	public SimpleSSLAgent( KernelAddress ka, Socket sock,
						   					SocketKernel info,	HashSet kernels) {
		super(ka, sock, info,(HashSet) kernels.clone());
	}


	/* (non-Javadoc)
	 * @see madkit.netcomm.BasicCommAgent#openInputOutputStreams(java.net.Socket)
	 */
	protected void openInputOutputStreams(Socket socket) {
		
		SSLSocketFactory sf=((SSLSocketFactory)SSLSocketFactory.getDefault());
		try {
			final SSLSocket ssl=(SSLSocket) sf.createSocket(socket,socket.getInetAddress().getHostName(),socket.getPort(),true);
			if(dKernels==null){//if this kernel didn't start the comm
				ssl.setUseClientMode(false);			
			}else{
				ssl.setUseClientMode(true);
			}
			ssl.startHandshake();
			
			doInputOutputOpen(ssl);	
		} catch (IOException e1) {
			debug("IOException caught "+e1.getMessage());
			closeSocket();
		} 
			
	}

	/**
	 * Open the Input/Ouput Streams
	 */
	private void doInputOutputOpen(SSLSocket ssl) {
		try {
			out = new DataOutputStream(ssl.getOutputStream());
			in = new DataInputStream(new BufferedInputStream(ssl.getInputStream()));
		} catch (IOException e) {
			debug("IOException caught "+e);
		}
		debug("Communication Secured :-)");
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#getProtocol()
	 */
	protected String getProtocol(){
			return KnownProtocols.SIMPLE_SSL_PROTO_V1;
		}
}
