/*
* ReflexiveActivator.java - Simulation: the general classes for handling simulation in MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel
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

import madkit.kernel.*;
import java.lang.reflect.*;
import java.util.*;


/** This class implements a one arg method activator.
  @version 1.0 Fabien Michel*/

public class OneValueArgsMethodActivator extends Activator
{
	private String methodName;
	private Map methods;
	private Class[] argsClass;
    
    public OneValueArgsMethodActivator(String group, String role, String methodName, Class[] argsClass)
    {
	this(Kernel.DEFAULT_COMMUNITY, group, role, methodName, argsClass);
    }
        
    public OneValueArgsMethodActivator(String communityName, String group, String role, String methodName, Class[] argsClass)
    {
    	super(communityName, group, role);
    	methods = new HashMap();
    	this.methodName = methodName;
    	this.argsClass = argsClass;
    }
    
    synchronized public void initialize()
    {
    	findMethods(methodName, argsClass);
    }

    	    
    protected void findMethods(String methodName, Class[] parameterTypes)
    {
	for(Iterator i = getAgentsIterator();i.hasNext();)
    	{
    		Object theAgent = i.next() ;
    		try
    		{
    			methods.put(theAgent, theAgent.getClass().getMethod(methodName, argsClass));
    		}
    		catch(Exception e)
    		{
			System.err.println("Can't find method: "+methodName+" on "+ ((AbstractAgent)theAgent).toString()+" "+e);
		}
	}
    }

synchronized public void update(AbstractAgent theAgent, boolean added)
{
	if(added)
		updateFieldFor(theAgent);
	else
		methods.remove(theAgent);
}
   
synchronized public void updateFieldFor(Object theAgent)
{
	try
	{
		methods.put(theAgent, theAgent.getClass().getMethod(methodName, argsClass));
	}
	catch(Exception e)
	{
		System.err.println("Can't find method: "+methodName+" on "+ ((AbstractAgent)theAgent).toString()+" "+e);
	}
}


synchronized public Object[] execute(Object[] args)
    {	
	Object values[] = new Object[methods.size()];
	int j=0;
	for (Iterator i = methods.entrySet().iterator();i.hasNext();j++)
	{
		Map.Entry e = (Map.Entry) i.next();
		try
		    {	
			values[j] = ((Method) e.getValue()).invoke(e.getKey(), args);
		    }
		catch (Exception ex) {System.err.println("Can't invoke:"+e.getValue().toString()+" on "+e.getKey().toString()+" "+ex.toString());}
	}
	return values;
    }
}
