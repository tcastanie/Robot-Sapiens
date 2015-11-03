/*
* CodingModule.java - Communicator: the connection module of MadKit
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
import madkit.kernel.*;

public class CodingModule
{
    protected final byte CODE_DEFAULT=0;

    protected AbstractAgent myAgent;

    /////////////////////
    // encode

    public CodingModule(AbstractAgent myAgent)
    {
	this.myAgent=myAgent;
    }

    private byte getCode(Object o)
    {
	return CODE_DEFAULT;
    }

    public byte[] encode(Object o,ByteArrayOutputStream byteout) throws IOException
    {
	DataOutputStream dos=new DataOutputStream(byteout);
	
	dos.writeByte(getCode(o));

	return encodeDefault(o,byteout);
    }

    protected byte[] encodeDefault(Object o,ByteArrayOutputStream byteout) throws IOException
    {

	ObjectOutputStream objout = new ObjectOutputStream(byteout);
	objout.writeObject(o); //agent ecrit dans un bytearray...

	return byteout.toByteArray();
    }


    //////////////////
    // decode

    public Object decode(byte[] rawobj) throws IOException
    {
	ByteArrayInputStream bis=new ByteArrayInputStream(rawobj);
	DataInputStream dis=new DataInputStream(bis);
	byte type=dis.readByte();

	Object obj;

	obj=reconstructObject(bis);
	
	if (obj==null)
	    System.out.println("Error: decode null object with code "+type+" .");

	return obj;
    }

    private Object reconstructObject(ByteArrayInputStream bis) throws IOException
    {
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
           		System.err.println("MessageDatagramReceiver readObject:"+e.getMessage());
		    }
	    }
     	catch (IOException ioe)
	    {
		System.err.println("Exception while converting Byte raw object into stream");
	    }

	return a;
    }

    public boolean shouldBeDefaultTreated(Object o)
    {
	return true;
    }


}
