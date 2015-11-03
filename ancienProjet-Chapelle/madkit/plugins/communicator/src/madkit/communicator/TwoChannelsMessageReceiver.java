/*
* TwoChannelsMessageReceiver.java - Communicator: the connection module of MadKit
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
import java.util.*;

import madkit.kernel.Message;

public class TwoChannelsMessageReceiver extends Thread
{
   public ServerSocket serverSocket;
   public DatagramSocket serverDSocket;
   boolean alive = true;

    TransportLayer pere;

    Hashtable clientTable;

    public TwoChannelsMessageReceiver(TransportLayer agent,DatagramSocket serverDSocket,
				      ServerSocket servSock,Hashtable clientTable)
    {
	pere = agent;
	serverSocket = servSock;
	this.serverDSocket=serverDSocket;
	this.clientTable=clientTable;
    }

    /** Telecharge les objets mobiles arrivant sur le port. Le protocole est:
	<UL> 1- reception d'un entier: la taille de la chaine d'octet definissant l'objet
	<LI> 2- reception d' une chaine d'octet de cette taile: l'objet
	<LI> 3- emission de nom de classes (eventuellemnt)
	<LI> 4- reception des fichiers .class demandes
	</UL> */

    public void run()
    {
	int len = 0;
	Object a = null;

	while (true)
	    {
		len = 0;
		a = null;

		if(!alive)
			break;
		try
		    {
		    	if(serverSocket != null)
		    	{
				Socket socket = serverSocket.accept();
				pere.debug("ports: serveur:"+serverSocket.getLocalPort()+" listener:"+socket.getLocalPort());
				new TwoChannelsSocketExtremity(serverDSocket,socket,pere,clientTable);
			}
		    }
		catch (IOException e)
		    {
			pere.debug( "Exception while listening port " +e);

			break;
		    }
	    }
	    pere.debug("receiver closed");
    }

    public synchronized void close()
    {
        try
	    {
		pere.debug("Receiver: CLOSE!");
		alive=false;
	        serverSocket.close();
	        serverSocket=null;
	    }
	catch (Exception e)
	    {
		pere.debug("Can't close MessageServer from DynamicTwoChannelsCommunicator in an orthodox way");
	    }
    }
}
