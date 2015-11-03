/*
* AgentAddress.java - Kernel: the kernel of MadKit
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



/** This class describe the unique identifiers used in the MadKit
  kernel.  An identifier created with this class is guaranteed to be
  unique.

  This class has been designed to be extended and subclassed in the
  future to handle various address type (FIPA, CORBA, MAF, JNA,
  ...). The syntax in this actual class is MadKit-specific.

  @author Olivier Gutknecht
  @version 1.2 - 01/12/98 */

import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class AgentAddress extends Object implements java.io.Serializable
{

	final private static String MADKIT_PROTOCOL = "mka";
	private static int agentCounter = 0;
	private String protocol;
	private String name;
	private String localID;
	private KernelAddress kernel;

  /** Define a new AgentAddress
    @param n agent name
    @param k local kernel address
    */

public AgentAddress(String n, KernelAddress k)
{
	agentCounter++;
	protocol = MADKIT_PROTOCOL;
	name     = n;
	localID = Integer.toString(agentCounter);
	kernel   = k;
}



/** Attempts to define an AgentAddress from a string representation (MadKit syntax).
    The official format is "mka:a,b@k" ("a" an agent name, "b" agent ID, "k" a kernel address)
    @param def string representation or agent name
    @throws InvalidAddressException if the string cannot be parsed */

public AgentAddress(String def) throws InvalidAddressException
{

	StringTokenizer tk=new StringTokenizer(def,":,@",true);
	try
	{
		protocol =  tk.nextToken();
		if ( (!(protocol.equals(MADKIT_PROTOCOL))) && (!(protocol.equals("request"))))
			throw new NoSuchElementException("invalid protocol:"+protocol);
		tk.nextToken();
		name=tk.nextToken();
		tk.nextToken();
		localID=tk.nextToken();
		tk.nextToken();
		StringBuffer sb = new StringBuffer();
		while(tk.hasMoreTokens())
			sb.append(tk.nextToken());
		kernel=new KernelAddress(sb.toString());
	}
	catch(NoSuchElementException e)
	{
		throw new InvalidAddressException("Invalid AgentAddress !"+def);
	}
}


/** Returns a string representation of the AgentAddress (with MadKit syntax) */
public String toString()
{
	return (name + "," + localID + "@" + kernel.shortString());
}


/** Check is this address is a local address */
public boolean isLocal()
{
	return kernel.isLocal();
}



/** Compare two instances of AgentAddress
@param o other agent address (can be void) */
public boolean equals(Object o)
{
	if (o instanceof AgentAddress)
	{
		AgentAddress a = (AgentAddress)o;
		if (a == null)
			return false;
		else
			return (kernel.equals(a.getKernel())) && localID.equals(a.localID) && protocol.equals(a.protocol);
	}
	else
		return false;
}


/** Return this address protocol */
public String getProtocol()    { return protocol; }

/** Return current agent name */
public String getName()    { return name; }

/** Set current agent name. This does not affect uniqueness of the AgentAddress */
void setName(String n) { name = n; }

/** The discriminating part of an AgentAddress */
public String getLocalID()  { return localID; }

/**return the address of the kernel on which the agent is currently running*/
public KernelAddress getKernel() { return kernel; }


public int hashCode()
{
	return (localID+kernel.toString()).hashCode();
}


void update(KernelAddress ka)
{
    agentCounter++;
    localID = Integer.toString(agentCounter);
    kernel   = ka;
}
}