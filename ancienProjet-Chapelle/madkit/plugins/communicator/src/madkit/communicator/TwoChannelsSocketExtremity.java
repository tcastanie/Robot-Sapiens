/*
* TwoChannelsSocketExtremity.java - Communicator: the connection module of MadKit
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

import madkit.kernel.*;
/**Thomas Cahuzac.
Une instance de cette classe par Kernel distant*/

public class TwoChannelsSocketExtremity extends Thread
{


    protected short messageNumber = 0;
    protected short maxDataSize = 1400;

    public Socket socket;
    public DataInputStream in;
    public DataOutputStream out;
    DatagramSocket dSocket;

    TransportLayer pere;

    protected Hashtable tableClient;

    int datagramSocketPort;

    String kernel=null;


    public TwoChannelsSocketExtremity(DatagramSocket dSocket,Socket socket,TransportLayer pere, Hashtable tableClient)
    {
	this.pere=pere;
	this.socket=socket;
	this.dSocket=dSocket;
	this.tableClient=tableClient;
	try
	    {
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	    }
	catch (IOException e)
	    {
		pere.println("Error when opening DataInputStream: "+e);
	    }
	try
	    {
		String kernel=pere.communicatorId();
		out.writeInt(kernel.length());
		out.writeChars(kernel);
		out.writeInt(dSocket.getLocalPort());
		out.flush();
	    }
	catch (IOException e)
	    {
		pere.println("Unable to write kernel address: "+e);
	    }
	try
	    {
		int l=0;
		kernel="";
		l=in.readInt();
		for (int i=0;i<l;i++)
		    kernel=kernel+in.readChar();
		datagramSocketPort=in.readInt();
	    }
	catch (IOException e)
	    {
		pere.println("Unable to read kernel address: "+e);
	    }

	String add=kernel; //+":"+socket.getPort();
	if (kernel.equals(pere.communicatorId()))
	    {
		pere.debug("Can not add my kernel.");
		closeSocket();
		return;
	    }
	if (tableClient.containsKey(add))
	    {
		pere.debug("Kernel "+add+" already add.");
		closeSocket();
		return;
	    }
	pere.debug("Adding Kernel "+add+" on table.");
	tableClient.put(add,this);

	start();

    }


    protected void closeSocket()
    {
	try
	    {
		pere.println("Closing socket to "+kernel+" ...");
		socket.close();
		socket=null;

	    }
	catch(Exception e)
	    {
	    }
    }

    /** Telecharge les objets mobiles arrivant sur le port. Le protocole est:
	<UL> 1- reception d'un entier: la taille de la chaine d'octet definissant l'objet
	<LI> 2- reception d' une chaine d'octet de cette taile: l'objet
	<LI> 3- emission de nom de classes (eventuellemnt)
	<LI> 4- reception des fichiers .class demandes
	</UL> */

    public Object decode(byte[] rawobj) throws IOException
    {
	ByteArrayInputStream bis=new ByteArrayInputStream(rawobj);

     	Object a = null;
     	try
	    {
     		ObjectInputStream objin = new ObjectInputStream(bis);

     		try
		    {
           		a = objin.readObject();
		    }
     		catch (Exception e)
		    {
           		pere.debug("MessageDatagramReceiver readObject:"+e.getMessage());
		    }
	    }
     	catch (IOException ioe)
	    {
		pere.debug("Exception while converting Byte raw object into stream");
	    }

	return a;
    }

    public void run()
    {
	try
	    {
		while (true)
		    {
			    pere.debug("R:reading size");
			int len = in.readInt(); //reception de la taille de l'objet serialise...
 			    pere.debug("R:done "+len+" preparing array");
 			byte[] byteobj = new byte[len];
 			in.readFully(byteobj); // lecture de l'obj serialise...
			Object a=null;
			a=decode(byteobj);

			if (a instanceof Message)
			    {       // l'objet recu est un agent
				Message aMessage = (Message)a;
				pere.communicatorInject(aMessage);
			    }
			else
			    if (a instanceof Vector)
				{
				    pere.communicatorTransmit((Vector)a);
				}
		    }
	    }
	catch(IOException e3)
	    {
		//closeSocket();
		pere.println("Connexion lost with "+kernel+".");
		//tableClient.remove(kernel);
		//e3.printStackTrace();
		pere.communicatorDisconnect(kernel);
		closeSocket();
	    }

	    pere.debug("socketExtremity closed");
    }

    public byte[] encode(Object o,ByteArrayOutputStream byteout) throws IOException
    {
	ObjectOutputStream objout = new ObjectOutputStream(byteout);
	objout.writeObject(o); //agent ecrit dans un bytearray...

	return byteout.toByteArray();
    }

    synchronized public void sendWithTCP(Object object)
    {
	if (socket != null)
	    {// Object sending procedure using TCP
		sendObjectWithTCP(object);
	    }
    }

    private void sendObjectWithTCP(Object object)
    {

	try
	    {
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		byte[] data=encode(object,byteout);
		//		ObjectOutputStream objout = new ObjectOutputStream(byteout);

// 		// pere.debug("C:serializing "+object);
// 		objout.writeObject(object); //agent ecrit dans un bytearray...

		// pere.debug("C:sending size");

		//pere.println("Extremity write "+kernel+" l="+byteout.size()+" "+this+" "+object.getClass().getName());
		out.writeInt(byteout.size());
		out.flush();
		// pere.debug("C:sending array");
		out.write(data); //...puis l'objet lui meme
		out.flush();
		// pere.debug("C:done");
		/*try
		  {
		  clientSocket.close();
		  }
		  catch (IOException e3)
		  {
		  pere.println("Error while closing Client Socket");
		  }*/
	    }
	catch(IOException e)
	    {
		pere.println("Unable to send TCP message to "+kernel+" = "+e);
		closeSocket();
	    }
    }

    synchronized public void sendWithUDP(Object o)
    {
	if (dSocket!=null)
	    {
		sendObjectWithUDP(o);
	    }
    }

    private void sendObjectWithUDP(Object object)
    {
	boolean lastPacket = false;
	boolean firstPacket = true;

	int offset = 0;
	byte segmentNumber = 0;

	try
	    {
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		DataOutputStream outD = new DataOutputStream(byteout);
		byte[] rawobj;

		rawobj = encode(object,byteout);



		while (!lastPacket)
		    {
			byteout.reset();

			if ((rawobj.length - offset) > maxDataSize) // Teste la longueur du message
			    {
				byteout.write(rawobj,offset,maxDataSize); // Si > limite traite la "tranche" suivante
					offset += maxDataSize;
			    }
			else
			    {
					byteout.write(rawobj,offset,(rawobj.length - offset)); // Sinon traite la totalité du message
					lastPacket = true;
			    }

			outD.writeShort(messageNumber); // Ecrit le numéro de la série de paquets
			byte debutfin = 0;
			if (firstPacket) debutfin++;              // Crée le flag indiquant si le paquet est premier et/ou dernier
			if (lastPacket | (segmentNumber == -1)) {debutfin++;debutfin++;} // Tronque le message s'il comporte plus de 255 segments
			outD.writeByte(debutfin); // Ecrit si le paquet est le premier et/ou le dernier de la serie
			outD.writeByte(segmentNumber); // Ecrit le n° de "tranche"

			DatagramPacket outDP = new DatagramPacket(byteout.toByteArray(),byteout.size(),
						 socket.getInetAddress(),datagramSocketPort); //Création du paquet

			//pere.println("sending to "+socket.getInetAddress().getHostName()+":"+datagramSocketPort);
			dSocket.send(outDP); // Envoi du paquet

			if (segmentNumber == -1) lastPacket = true; // Tronque le message s'il comporte plus de 255 segments
			firstPacket = false;
			segmentNumber++;

		    }
		messageNumber++;
	    }

	catch(IOException e2)
	    {
		pere.println("Unable to send UDP message to "+kernel+" .");
		closeSocket();
	    }
	//finally {dSocket.close();};
    }

}
