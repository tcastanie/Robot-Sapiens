/*
* Message.java - Kernel: the kernel of MadKit
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

/** The generic MadKit message class. Use a subclass to adapt it to
    your needs. This class is quite lightweight, it just defines sender
    and receivers (expressed with AgentAddres class) and automatically
    stamp the date of send on the message 

  @author Olivier Gutknecht
  @author Fabien Michel revized for MadKit 3.0 05/09/01 
*/

public class Message extends Object implements Cloneable, java.io.Serializable
{
	private java.util.Date creationDate;
	private AgentAddress sender=null;
	private AgentAddress receiver=null;

public Message()
{
	creationDate=new java.util.Date();
}



final void setSender(AgentAddress s) {sender=s;}

final void setReceiver(AgentAddress s) {receiver=s;}



/** Returns the original sender. This information can be trusted */
final public AgentAddress getSender() {return sender;}

/** Returns the intended receiver */
final public AgentAddress getReceiver() {return receiver;}

/** Returns the creation date for the message (instanciation time) */
final public java.util.Date getCreationDate() {return creationDate;}





/** Mandatory for some deep optimizations in the micro kernel message passing code */
public Object clone()
{
	try
	{
		Message that = (Message)super.clone();
		return that;
	}
	catch (CloneNotSupportedException e) { throw new InternalError(); }
}
 

/** Returns a debug string with enveloppe and content for the message */
public String toString()
{
	return "Sender:"+sender+"\n"+" Receiver:"+receiver+"\n"+" Creation Date:"+creationDate.toString()+"\n"+" Class:"+getClass().getName();
}		

}





























