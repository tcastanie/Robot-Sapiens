/*
* AgentInformation.java - Kernel: the kernel of MadKit
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

import java.util.Date;

/** The "identity card" of an agent. Stores name, identifiers & creation date

  @author Olivier Gutknecht
  @version d5 09/01/97 */

final public class AgentInformation extends Object implements java.io.Serializable
{
	private AgentAddress owner;
	private AgentAddress address;
	private Date creationDate;

AgentInformation(String theName, Object theOwner)
{
	address  = new AgentAddress(theName,Kernel.getAddress());
	if(theOwner instanceof AbstractAgent)
		owner = ((AbstractAgent) theOwner).getAddress();
	else
		owner = address;
	creationDate = new Date();
}

/** Get the patronymic agent name stored in the AgentAddress for this agent (does not used for uniqueness) */
public String getName() {return address.getName();}

/** Change the patronymic agent name */
void setName(String theName) {address.setName(theName);}

/** Return the owner (i.e. the agent which launched this agent), can be itself if launched directly by the kernel*/
public AgentAddress getOwner() {return owner;}

/** Return the time when the agent has been registred in the kernel tables */
public Date getCreationDate() {return creationDate;}

/** Return the agent own AgentAddress */       

public AgentAddress getAddress() {return address;}

public String toString()
{
return "Agent:" + address.getName() + " (" + address.toString() + ")\n"+
	" owner: " + owner + "\n"+
	" created:" + creationDate.toString() + "\n";
}

}