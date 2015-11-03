/*
* SocketKernel.java - Communicator: the connection module of MadKit
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

import java.util.*;
import java.io.*;


public class SocketKernel implements Serializable
{
  String host;
  int port;
  
  public SocketKernel(String s, int p)
  {
    host=s;
    port=p;
  }

  public SocketKernel(int p)
  {
    host=madkit.kernel.Kernel.getAddress().getHost();
    port=p;
  }

  public SocketKernel(String def) throws Exception
  {
    StringTokenizer tk=new StringTokenizer(def,":",false);
    if (tk.countTokens() == 2)
      {
	host=tk.nextToken();
	port=Integer.parseInt(tk.nextToken());
      }
    else 
      throw new NoSuchElementException("Invalid count on SocketKernel");
  }
  
  
  public String getHost()
  {
    return host;
  }

  public int getPort()
  {
    return port;
  }

  public void setPort(int p)
  {
    port = p;
  }
  
  public boolean equals(SocketKernel sk)
  {
    return host.equals(sk.host) && (sk.port == port);
    
  }
  
  public String toString()
  {
    return new String(host+":"+port);
  }
  
}













