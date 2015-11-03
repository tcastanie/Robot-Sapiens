/*
 * AbstractHandler.javaCreated on Dec 11, 2003
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

import java.net.InetAddress;

import madkit.kernel.KernelAddress;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
abstract class  AbstractHandler implements ConnectionHandler {
	protected final InetAddress _address;
	protected final int _port;
	protected final KernelAddress _localKernel;
	protected final KernelAddress _distantKernel;
	protected final boolean _active;
	/*------------------------------------------------------------------------------------*/
	 /**
	 * @param addr
	 * @param port
	 */
	public AbstractHandler(KernelAddress localKernel, KernelAddress distatntKernel,InetAddress addr,int port,boolean activeMode) {
		_address=addr;
		_port=port;
		_localKernel=localKernel;
		_distantKernel=distatntKernel;
		_active=activeMode;
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.handlers.ConnectionHandler#getPort()
	 */
	public int getPort() {
		return _port;
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.handlers.ConnectionHandler#getInetAddress()
	 */
	public InetAddress getInetAddress() {
		return _address;
	}
	
	public String getLocalKernelID(){
		return _localKernel.getID();
	}
	
	public KernelAddress getDistantKernel(){
		return _distantKernel;
	}
	/* (non-Javadoc)
	 * @see madkit.netcomm.handlers.ConnectionHandler#isActiveMode()
	 */
	public boolean isActiveMode() {
		return _active;
	}

}
