/*
* KernelAddress.java - Kernel: the kernel of MadKit
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

import java.util.*;
import java.net.InetAddress;

/** This class identifies a Kernel Address. It is the naming of the
    "home agent platform" in a transport address or communication
    scheme (as proposed in FIPA-1999-17). The syntax is now "host:id"
    but will probably change to "mka://host/id" to map better to the
    FIPA specification

  @author Olivier Gutknecht
  @version d6 - 09/01/97 */

public class KernelAddress extends Object implements java.io.Serializable
{
  private String host;
  private String ID = "0";
  
    /** Define a new unique KernelAddress on the local host 
	@param ipnumber define is the host must be in numeric form or resolved though the DNS
     */
  public KernelAddress(boolean ipnumber)
    {
	ID = "K"+ (new Date()).getTime();
	try
	    {
		if (ipnumber)
		    host = InetAddress.getLocalHost().getHostAddress();
		else
		    host = InetAddress.getLocalHost().getHostName();
	    } 
	catch(Exception e) 
	    { 
		host = "localKernel";
	    }
    }
  
public KernelAddress(boolean ipnumber, String ipaddress)
{
	ID = "K"+ (new Date()).getTime();
	try
	{
		String hostname = InetAddress.getLocalHost().getHostName();
		InetAddress addresses[] = InetAddress.getAllByName(hostname);
		for(int i=0; i<addresses.length; i++) 
		{
			if (ipaddress.compareTo(addresses[i].getHostAddress()) == 0)
			{
				host = ipaddress;
				return;
			}
		}
		if (ipnumber)
			host = InetAddress.getLocalHost().getHostAddress();
		else
			host = hostname;
	} 
	catch(Exception e) 
	{ 
		host = "localKernel";
	}
}
  
  /** Tries to parse an KernelAddress from a string representation. 
    The official format is "a:b" (with a an internet host and an b unique ID )

    @param def string representation of Kernel address
    @throws InvalidAddressException if the string cannot be parsed in
    the KernelAddress String format */

  public KernelAddress(String def) throws InvalidAddressException 
    {
      StringTokenizer tk=new StringTokenizer(def,":",false);
      try 
	{
	  if (tk.countTokens() == 2)
	    {
	      host=tk.nextToken();
	      ID = tk.nextToken();
	    }
	  else 
	    throw 
	      new InvalidAddressException("Invalid count on KernelAddress:"+
					  def+tk.countTokens());
        }
      catch(NoSuchElementException e)
        {
	  throw 
	    new InvalidAddressException("Invalid element on KernelAddress !"+ 
					def+tk);
        }
    }

  /** Check is this address is a local address */
  public boolean isLocal()
    {
      return (this.equals(Kernel.getAddress()));
    }

  /** Compare two KernelAdresses 
   @param o other address (can be void) */
  public boolean equals(Object o)
    {
      if (o instanceof KernelAddress)
	{
	  KernelAddress a = (KernelAddress)o;
	  if (a != null) 
	   return (toString().equals(a.toString()));
	  else
	    return false;
	}
      else
	return false;
    }
    /** Return the host name encoded in this KernelAddress */
  public String getHost()
    { return host; }
    /** Return the anonymous ID associated */
  public String getID() 
    { return ID; }

    /** Returns the canonical string representation for this platform address */
  public String toString()
  {
	 return (host+":"+ID);
  }
  public String shortString()
  {
  	return host+":"+ID.substring(10);
  }
}










