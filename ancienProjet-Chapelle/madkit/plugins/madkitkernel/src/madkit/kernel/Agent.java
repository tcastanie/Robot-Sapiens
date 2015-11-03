/*
* Agent.java - Kernel: the kernel of MadKit
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
/** The main MadKit AbstractAgent class. Provides support for non threaded agent
 * lifecycle, messaging, graphical interface, group and roles
 * management, agent information, ...
 *
 * The agent behavior intentionnaly is *not* defined. It is up to the
 * agent developer to choose an agent model or to develop his specific
 * agent library on top of the facilities provided by MadKit. However,
 * all agent share the same organizational view, and the basic
 * messaging code, so integration of agent coming from different
 * developer and having heterogeneous models is quite easy.
 *
 * Agent-related methods (almost everything here) can invoked only
 * after registration in the kernel (i.e. after the activate() method has been called
 * on this agent). That means that you should not use any of the agent methods in
 * constructor
 *
  @author Olivier Gutknecht
  @author Fabien Michel : MadKit 3.0 revizion
 */

public abstract class Agent extends AbstractAgent implements Runnable, java.io.Serializable
{
	transient Thread agentThread;

  //////////////////////////////////////////////
  // ------ Thread managment
  /** Suspend the agent for a while. The main agent thread is put to
      sleep for the specified duration. If the agent developer creates
      additional threads, they will not be suspended
      @param t pause duration in milliseconds */

final public void pause(int t)
{
	try {Thread.sleep(t);}
	catch (InterruptedException e) {}
}

/** Returns the agent thread  */
final Thread getAgentThread()
{
	return agentThread;
}

  /////////////////////////////////////////////
  // ------ Messaging interface

  /** This method is the blocking version of nextMessage().  It
    suspends the main agent thread (but not the whole threadgroup)
    until a message has been received */
final public Message waitNextMessage()
{
	try
	{
		synchronized(messageBox)
		{
			while(messageBox.isEmpty())
			{
				messageBox.wait();
			}
		}
		return nextMessage();
	}
	catch (InterruptedException e)
	{
		return  null;
	}
}

/** This method is the blocking version of nextMessage(), with
    time-out.  It suspends the main agent thread until a message has
    been received or the time out delay elapsed (and returns null in
    that case)
    @param timeout the maximum time to wait, in milliseconds */
final public Message waitNextMessage(long timeout)
{
	try
	{
		synchronized(messageBox)
		{
			if(messageBox.isEmpty())
				messageBox.wait(timeout);
		}
		return nextMessage();
	}
	catch (InterruptedException e)
	{
		return  null;
	}
}

/** this method is dramatically private. Nyark ;-) */
public void receiveMessage(Message m)
{
	super.receiveMessage(m);
	synchronized (messageBox)
	{
		messageBox.notifyAll();
	}
}

/** This method defines the main behavior for threaded agents. */
public void live() {}

/** Only useful to the agent micro-kernel */
final public void run()
{
	try
	{
		agentThread = Thread.currentThread();
	        Controller c = getController();
	        // if there is a controller, the controller takes over
	        if (c != null)
	        {
	        	c.activate();
			c.live();
	        }
		else // else standard behavior..
		{
	            activate();
		    live();
	        }
	}
	catch (RuntimeException r)
	{
		if(currentKernel == null)
			return;
		currentKernel.display ("Runtime Agent Exception : " + r.toString());
		r.getMessage();
		r.printStackTrace();
		currentKernel.display(this+" Agent killed.");
	}
	catch (Exception e)
	{
		System.err.println("exception");
		if(currentKernel == null)	return;
		currentKernel.display("### UNDEFINED EXCEPTION ! : " + e.toString());
		e.printStackTrace();
		currentKernel.display(this+"Agent killed. (MadKit internal error)");
		currentKernel.display("Please send a bug report !");
	}
	if(currentKernel != null)
		currentKernel.killAgent(this);
}

/**Due to the "Thread problem" (see http://java.sun.com/j2se/1.4/docs/api/index.html for more details)
when an agent is killed, by another agent or interface (unvolontary end: whithout exiting of the <code>live</code> method)
the Kernel will try a Thread.stop on it. And the Thread problem is that this method is currently unsafe and does not work
every time. The result is that the agent has been killed, that is to say removed from organizations and invisible by others (no more messages),
but its thread is still alive with unpredictable behaviors, wasting cpu time.
The sun API says :
"Most uses of stop should be replaced by code that simply modifies some variable to indicate that the target thread should stop running. The target thread
should check this variable regularly, and return from its run method in an orderly fashion if the variable indicates that it is to stop running. (This is the approach
that JavaSoft's Tutorial has always recommended.)  "

In MadKit, agents are autonomous :)

So it is the developper responsability to be sure that an agent cleanly exit its thread when dying.
The <code>exitImmediatlyOnKill</code> method is made to easy this procedure.
When a dead agent, that has been killed, calls this method, its thread immediatly exits if it is still running, avoiding to become a "zombie agent"
For example this kind of agent may not be able to be cleanly killed by another due to its simplicity (no wait, no pause).

public void live()
{
	int i=0;
	while(true)	//UNSAFE
		i++;
}

public void live()
{
	int i=0;
	while(true)
	{
		exitImmediatlyOnKill();		//SURE CODE
		i++;
	}
}
*/

//Make the thread crash himself
final protected void exitImmediatlyOnKill()
{
	getMyGroups();
}


}

