/*
* TurboMethodActivator.java - Simulation: the general classes for handling simulation in MadKit
* Copyright (C) 1998-2002 Fabien Michel
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
package madkit.simulation.activators;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Activator;

/** Optimized version of the SingleMethodActivator can only invoke a simple method with no parameters
@version 2.0
@author Fabien Michel*/

public class TurboMethodActivator extends Activator
{     
    private String method;
    /** methods maps an agent to its corresponding Method object for runtime invocation*/
    protected Map methods;

    public TurboMethodActivator(String methodName, String groupName, String roleName)
    {
	super(groupName, roleName);
	method = methodName;
	methods = new HashMap();
    }
   
    public TurboMethodActivator(String methodName, String communityName, String groupName, String roleName)
    {
	super(communityName, groupName, roleName);
	method = methodName;
	methods = new HashMap();
    }
   
    public String getMethodName()
    {
	return method;
    }

    synchronized public void initialize()
    {
    	for (Iterator i = getAgentsIterator();i.hasNext();)
		setMethodFor(i.next());
    }
    
    synchronized public void update(AbstractAgent theAgent, boolean added)
    {
    	if(added)
    		setMethodFor(theAgent);
    	else
    		methods.remove(theAgent);
    }

    synchronized public void execute()
    {
	for(Iterator i = methods.entrySet().iterator();i.hasNext();)
	{
		Map.Entry entry = (Map.Entry) i.next();
		try
		{
			((Method)entry.getValue()).invoke(entry.getKey(),null);
		}
		catch (Exception e)
		{
			//System.err.println("Can't invoke:"+((Method)entry.getValue()).toString()+" on "+((AbstractAgent)entry.getKey()).toString()+"  "+e.toString()); 
		}
	}
    }
    
final void setMethodFor(Object theAgent)
{
	Method m = null;
	try
	{
		m = theAgent.getClass().getMethod(method,null);
	}
	catch (Exception e)
	{
		System.err.println("Can't find method: "+method+" on "+((AbstractAgent)theAgent).toString()+"    "+e);
		return;
	}
	if(m != null)
		methods.put(theAgent,m);
}
	
    	
    
}









