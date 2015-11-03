/*
* NetworkRequest.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.kernel;



/** This message class is to be used to communicate with the siteAgent
  	of the corresponding running kernel.

    @author Fabien Michel 16/01/02
    @version 1.0
*/

final public class NetworkRequest extends Message implements PrivateMessage

{

	public final static int BE_COMMUNICATOR			= 1;

	public final static int STOP_COMMUNICATOR		= 2;

	public final static int CONNECTION_REQUEST		= 3;

	public final static int DECONNECTED_FROM		= 4;

	public final static int GET_AVAILABLE_DESTINATIONS	= 5;

	public final static int REQUEST_MIGRATION		= 6;

	public final static int INJECT_MESSAGE			= 7;

	public final static int JOIN_COMMUNITY			= 8;

	public final static int LEAVE_COMMUNITY			= 9;

	

	private int code;

	private Object arg=null;

	

	public NetworkRequest(int requestCode)

	{

		code = requestCode;

	}

	

	public NetworkRequest(int requestCode, Object argument)

	{

		code = requestCode;

		arg = argument;

	}

	

	public int getRequestCode()    {	return code;    }

	public Object getArgument()    {	return arg;    }

}
