/*
* TwoChannelsMessageServer.java - Communicator: the connection module of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Pierre Bommel, Fabien Michel, Thomas Cahuzac
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
package madkit.communicator;

import java.io.*;
import java.net.*;
import madkit.kernel.Message;
import java.util.*;

public class TwoChannelsMessageServer extends Thread
{
    TransportLayer agent;
    ServerSocket serverSocket;
    DatagramSocket serverDSocket;
    Hashtable tableClient;
    TwoChannelsDatagramListener dm;
    TwoChannelsMessageReceiver m;

    public int getServerPort()
    {
	if (serverSocket!=null)
	    return serverSocket.getLocalPort();
	return -1;
    }

    public  DatagramSocket getDatagramSocket()
    {
	return serverDSocket;
    }


    public TwoChannelsMessageServer(TransportLayer a, int port,Hashtable tableClient)
	throws IOException
    {
	agent=a;
	this.tableClient=tableClient;


	int i=0;
	while (i < 5)
	    {
		try
		    {
			serverDSocket = new DatagramSocket(port+i);
			a.setPort(port+i);
			agent.println("UDPSocket Listening on port "+serverDSocket.getLocalPort());
			i=5;
		    }
		catch (SocketException e)
		    {
			i++;
			agent.debug("Could not listen on port: " + port + ", " + e);
		    }
	    }

	i=0;
	while (i < 5)
	    {
		try
		    {
			serverSocket = new ServerSocket(port+i);
			a.setPort(port+i);
			start();
			agent.println("TCPSocket Listening on port "+(port+i));
			i=5;
		    }
		catch (IOException e)
		    {
			i++;
			agent.debug("Could not listen on port: " + port + ", " + e);
		    }
	    }

    }

    public void close()
    {
    	dm.stop();
    	m.stop();
    	dm.close();
    	m.close();
    	stop();
	try {sleep(10);}
	catch (InterruptedException ie) {};
    }


    public void run()
    {
	dm = new TwoChannelsDatagramListener(agent,serverDSocket);
	m = new TwoChannelsMessageReceiver(agent,serverDSocket,serverSocket,tableClient);
	dm.start();
	m.start();
    }
}
