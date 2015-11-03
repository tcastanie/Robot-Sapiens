/*
* TwoChannelsDatagramMessageExtremity.java - Communicator: the connection module of MadKit
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

public class TwoChannelsDatagramMessageExtremity extends Thread
{
    public int maxDataSize=1400;

    public DatagramSocket dSocket;
    static byte[] inBuffer = new byte[1428];
    Hashtable receivedPackets;
    Hashtable messagesSize;
    short overheadSize = 4;
    int messageNumber;

    TransportLayer pere;

    protected StatAgent stat;

    CodingModule code;

    public TwoChannelsDatagramMessageExtremity(TransportLayer agent, DatagramSocket serverSocket,StatAgent stat,CodingModule code)
    {
	pere = agent;
	dSocket = serverSocket;
	this.stat=stat;
	this.code=code;
    }

    /** Telecharge les objets mobiles arrivant sur le port. Le protocole est:
	<UL> 1- reception d'un entier: la taille de la chaine d'octet definissant l'objet
	<LI> 2- reception d' une chaine d'octet de cette taile: l'objet
	<LI> 3- emission de nom de classes (eventuellemnt)
	<LI> 4- reception des fichiers .class demandes
	</UL> */

    public int getDatagramSocketPort()
    {
	if (dSocket!=null)
	{
	    return dSocket.getLocalPort();
	}
	return -1;
    }

    private void reconstructObject(byte[] byteout) throws IOException
    {
	Object o=code.decode(byteout);
	stat.onIn(byteout.length,false,o);
	if (code.shouldBeDefaultTreated(o))
	    {
		if (o instanceof Message)
		    {     // l'objet recu est un agent
			Message aMessage = (Message)o;
			pere.communicatorInject(aMessage);
		    }
		else
		    if (o instanceof Vector)
			{
			    pere.communicatorTransmit((Vector)o);
			}
	    }
    }

    public void run()
    {
        int len;
        MessageSegment segment;
        SegmentsArray receivedPackets = new SegmentsArray(pere);

        receivedPackets.start();


	try
	    {
		while(true)
		    {
			len = 0;

                        DatagramPacket in = new DatagramPacket(inBuffer, inBuffer.length);

			//System.out.println("listening on "+dSocket.getLocalAddress().getHostName()+":"+dSocket.getLocalPort());

                        dSocket.receive(in);


                        try
			    {
                                len = in.getLength(); //reception de la taille de l'objet serialise...

                                byte[] byteobj = new byte[len-overheadSize];
                                System.arraycopy(in.getData(),0,byteobj,0,len-overheadSize); // lecture de l'obj serialise...
                                byte[] byteutil = new byte[overheadSize];
                                System.arraycopy(in.getData(),len-overheadSize,byteutil,0,overheadSize); //lecture des autres données

                                DataInputStream datain = new DataInputStream (new BufferedInputStream(new ByteArrayInputStream(byteutil)));
                                Integer packetNumber = new Integer (datain.readShort());
                                byte debutfin = datain.readByte();
                                byte segmentNumber = datain.readByte();

                                if (debutfin == 3) reconstructObject(byteobj);
				// si l'objet et 1er et dernier de sa série il est tout de suite traité
                                else
				    {
					Vector messageSegments;
					segment = new MessageSegment(byteobj,segmentNumber,debutfin > 1);

					messageSegments = receivedPackets.get(packetNumber); //Recupère les paquets de la même série

					if (messageSegments != null) // Verification de la presence d'autres segments de la série
					    {
                                         	if (!messageSegments.contains(segment)) // Vérifie si le paquet n'a pas été déjà reçu
						    messageSegments.insertElementAt(segment, segmentNumber);

						if (((MessageSegment)messageSegments.lastElement()).lastSegment() &&
						    ((messageSegments.size()-1) == ((MessageSegment)messageSegments.lastElement()).getPartNumber()))
						    {
							// Si les tous les segments sont arrivés, on reconstitue le message
							ByteArrayOutputStream completeMessage = new ByteArrayOutputStream();
							for(int i=0; i<messageSegments.size(); i++)
							    completeMessage.write(((MessageSegment)messageSegments.elementAt(i)).getSegment());
							reconstructObject(completeMessage.toByteArray());

							receivedPackets.remove(packetNumber);
						    }
						else receivedPackets.put(packetNumber, messageSegments);
					    }
					else
					    {
						messageSegments = new Vector();
						messageSegments.addElement(segment);
						receivedPackets.put(packetNumber, messageSegments);
					    }
				    }
			    }
                        catch(IOException e2)
			    {
				System.out.println("DynamicTwoChannelsCommunicator: reciever exception " + e2.toString());
				e2.printStackTrace();
			    }
		    }
	    }
	catch(IOException e)
	    {
		dSocket.close();
		System.err.println("DynamicTwoChannelsCommunicator: cannot receive datagram...");

	    }
    }

    public void close()
    {
        try
            {
                dSocket.close();
            }
        catch (Exception e)
            {
                System.err.println("Can't close TwoChannelsDatagramMessageReceiver from DynamicTwoChannelsCommunicator in an orthodox way");
            }

    }


}
