/*
* TransportLayer.java - Communicator: the lower-level connection module.
* Copyright (C) 1998-2002 Olivier Gutknecht, Pierre Bommel, Fabien Michel,
*               Thomas Cahuzac, Nicolas Bernard
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

import madkit.kernel.*;
import java.util.*;
import java.net.*;
import java.awt.AWTEvent;

public class TransportLayer
{
	TwoChannelsMessageServer server;
	DynamicTwoChannelsCommunicator agent;
        Hashtable clients;


	/* ------------ */
	/* Constructeur */
	/* ------------ */

	TransportLayer(DynamicTwoChannelsCommunicator dtcc, Hashtable clientTable)
	{
		agent = dtcc;
                clients = clientTable;
        try
        {
        	server = new TwoChannelsMessageServer(this, agent.getPort(), clients);
        }
        catch (Exception e)
        {
            println("Can't initialize the Message Server.");
        }

        setServerOn();
	}


	/* ---------------------- */
	/* Contrôle serveur		  */
	/* ---------------------- */


	// Retourne vrai si le serveur est actif.
	boolean getServerActive()
	{
		return (server != null);
	}


	// Active le serveur.
	private synchronized void setServerOn()
	{
		if (! getServerActive())
		try
			{
				server = new TwoChannelsMessageServer(this, agent.getPort(), clients);
			}
		catch(Exception e)
			{
				println("Server Error"+e);
			}
	}


	// Désactive le serveur.
	private synchronized void setServerOff()
	{
		if (getServerActive()) server.close();
		server = null;
	}


	/* ---------------------- */
	/* Gestion Connections	  */
	/* ---------------------- */


	// Connection à un noyau distant.
	synchronized void connect(SocketKernel target, Vector v)
	{
		Socket socket;
		try
	    {
			socket = new Socket(target.getHost(),target.getPort());
	    }
		catch (Exception e)
	    {
			println("Client: Error on Connection: "+e);
			return;
	    }

		TwoChannelsSocketExtremity client = new TwoChannelsSocketExtremity(server.getDatagramSocket(), socket, this, clients);
		client.sendWithTCP(v);
	}


	// Réponse au noyau distant qui attend la connection.
	synchronized void replyConnect(KernelAddress target, Vector v)
	{
		sendTCPWithKernelAddress(v,target);
	}


	// Déconnection d'avec un noyau distant par fermeture du socket.
	synchronized void disconnect(TwoChannelsSocketExtremity client)
	{
		client.closeSocket();
                clients.remove(client);
	}


	// Envoyer un message à un kernel distant.
        synchronized void sendto(Message m, KernelAddress dest)
    {
    		if (shouldUseUDP(m)) sendUDPWithKernelAddress(m, dest);
		else sendTCPWithKernelAddress(m, dest);
    }


	/* ------------------------------------- */
	/* Communication avec l'agent : Entrées  */
	/* ------------------------------------- */


	// Les quatre fonctions suivantes retourne un champ du communicator.
	boolean communicatorAlive()
	{
		return agent.isAlive();
	}

	int communicatorGarbage()
	{
		return agent.getGarbagePeriod();
	}

	short communicatorSize()
	{
		return agent.getMessagesSegmentsTableSize();
	}

	int communicatorTime()
	{
		return agent.getTimeoutBetweenSegments();
	}

	String communicatorId()
	{
		return agent.getId();
	}


	/* ------------------------------------- */
	/* Communication avec l'agent : Sorties  */
	/* ------------------------------------- */


	// Permet de régler le numéro du port du communicator.
	void setPort(int P)
	{
		agent.setPort(P);
	}

	// Appelle la fonction transmitInfo du communicator.
	void communicatorTransmit(Vector args)
	{
		agent.transmitInfo(args);
	}

	// Appelle la fonction injectMessage du communicator.
	void communicatorInject(Message m)
	{
		agent.injectMessage(m);
	}

	// Appelle la fonction disconnectFrom du communicator.
	void communicatorDisconnect(String kernelId)
	{
		agent.disconnectFrom(kernelId);
	}

	// Affichage des messages d'information et erreurs.
	void println(String s)
	{
		agent.println(s);
	}

	void debug(String s)
	{
		agent.debug(s);
	}


	/* ---------------------- */
	/* Fonctions utilitaires  */
	/* ---------------------- */


	// Envoyer un message en TCP à partir d'un KernelAddress.
	synchronized private void sendTCPWithKernelAddress(Object o,KernelAddress ka)
    {
		String add=ka.getID();
		TwoChannelsSocketExtremity client=(TwoChannelsSocketExtremity) clients.get(add);
		if(client != null) client.sendWithTCP(o);
    }


    // Envoyer un message en UDP à partir d'un KernelAddress.
    synchronized private void sendUDPWithKernelAddress(Object o,KernelAddress ka)
    {
		String add=ka.getID();
		TwoChannelsSocketExtremity client=(TwoChannelsSocketExtremity) clients.get(add);
		if(client != null) client.sendWithUDP(o);
    }


	// Retourne vrai si le message est UDP.
    private boolean shouldUseUDP(Message m)
    {
		return (m instanceof UDPMessage);
    }


    // A effectuer à la destruction de la présente classe.
    void close()
	{
		setServerOff();
	}

}