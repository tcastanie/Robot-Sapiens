/*
 * SocketHandler.javaCreated on Dec 11, 2003
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
package madkit.netcomm.handlers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import madkit.kernel.KernelAddress;
import madkit.netcomm.Codec;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
public class SocketHandler extends AbstractHandler  {

	/*------------------------------------------------------------------------------------*/
	/**
	* 
	*/
	public SocketHandler(KernelAddress localKernel,KernelAddress distantKernel, InetAddress addr,int port, boolean actionMode) {
		super(localKernel,distantKernel, addr,port,actionMode);
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.handlers.ConnectionHandler#getName()
	 */
	public String getName() {
		return "sockethandler";
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.handlers.ConnectionHandler#sendObject(java.lang.Object)
	 */
	public boolean sendObject(Object obj) throws IOException {
		Socket socket=new Socket(_address,_port);
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		writeObject(getLocalKernel(),out);
		writeObject(obj, out);
		socket.shutdownOutput();
		socket.close();
		return true;
	}

	/*----------------------------------------------------------------------------------------*/
	/**
	 * @return
	 */
	private Object getLocalKernel() {
		return _localKernel;
	}

	private void writeObject(Object obj, DataOutputStream out) throws IOException {
		byte[] data=Codec.encode(obj);		
		out.writeInt(data.length);//first send the size
		out.flush();
		out.write(data); //now the object
		out.flush();
	}

	/*----------------------------------------------------------------------------------------*/
	/**
	 * @return
	 */
	public Socket createSocket() throws IOException {
		
		return new Socket(_address,_port);
	}

}
