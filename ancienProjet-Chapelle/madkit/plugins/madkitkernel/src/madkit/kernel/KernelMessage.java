/*
* KernelMessage.java - Kernel: the kernel of MadKit
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

/** This message class is used for communication between the
    KernelAgent and system agents to get information about the current
    state of the platform, subscribe to common hooks, ...

    Any platform service beside group/role management and local
    messaging and lifecycle is handled by agents, thus an extension
    mechanism is needed. That is what the KernelAgent (role "kernel"
    in group "system") and the hooks mechanism is for.

    @author Olivier Gutknecht
    @author Fabien Michel revized for MadKit III 05/09/01 (communication)
 */
final public class KernelMessage extends Message  implements PrivateMessage
{
    public final static int NO_REQUEST              = 1;
    public final static int INVALID_HOOK_REQUEST    = 2;
    public final static int INVALID_INVOCATION      = 3;

    public final static int REPLY                   = 13;
    public final static int INVOKE                  = 14;
    public final static int MONITOR_HOOK            = 15;

    public final static int REQUEST_MONITOR_HOOK    = 22;
    public final static int REMOVE_MONITOR_HOOK     = 23;

    private int type;
    private int operation;
    private Object arg;
    private Object subject=null;


    public KernelMessage(int t, int o, Object a, Object s){
        super();
        type=t;
        operation=o;
        arg=a;
        subject=s;
    }

    public KernelMessage(int t, int o, Object a)
    {
	super();
	type=t;
	operation=o;
	arg=a;
    }

    public KernelMessage(int t, int o)
    {
	this(t,o,null);
    }

    public int getType()    {	return type;    }
    public int getOperation()    {	return operation;    }
    public Object getArgument()    {	return arg;    }
    public Object getSubject()    {	return subject;    }

  public String toString()
  {
    return super.toString() + " - Type: " + type + " - Operation: " + operation + " - Args: " + arg;
  }
}


