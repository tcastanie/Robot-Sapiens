/*
* ReflexiveProbe.java - Simulation: the general classes for handling simulation in MadKit
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
package madkit.simulation.probes;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Probe;

/** This class implements a dynamic probe on a given property.
  @version 3.0
  @author Olivier Gutknecht
  @author Fabien Michel*/

public class ReflexiveProbe extends Probe
{     
	protected Map fields;
	private String property;
    

public ReflexiveProbe(String group, String role,String property)
{
	super(group, role);
	this.property = property;
	fields = new HashMap();
}
    
public ReflexiveProbe(String community, String group, String role, String property)
{
	super(community, group, role);
	this.property = property;
	fields = new HashMap();
}
    
public String getProperty() {return property;}

public void findFields()
{
	for(Iterator i = getAgentsIterator();i.hasNext();)
    	{
    		Object theAgent = i.next() ;
    		try
    		{
    			fields.put(theAgent, theAgent.getClass().getField(property));
    		}
    		catch(Exception e)
    		{
			System.err.println("Can't find property: "+property+" on "+ ((AbstractAgent)theAgent).toString() +e);
		}
	}
}

public void initialize()
{
	findFields();
}

public void update(AbstractAgent theAgent, boolean added)
{
	if(added)
		updateFieldFor(theAgent);
	else
		fields.remove(theAgent);
}
   
public void updateFieldFor(Object theAgent)
{
	try
	{
		fields.put(theAgent, theAgent.getClass().getField(property));
	}
 	catch(Exception e)
   	{
		System.err.println("Can't find property: "+property+" on "+ theAgent.toString() +e);
	}
}

    synchronized public double getDouble(Object theAgent)
    {
    	try
    	{
    		return ((Field)fields.get(theAgent)).getDouble(theAgent);
    	}
    	catch(IllegalAccessException e)
    	{
    		System.err.println("Unable to getDouble on "+((AbstractAgent)theAgent).toString()+"  "+e);
    		return Double.NaN;
    	}
    }
    
    synchronized public long getLong(Object theAgent) 
    {
    	try
    	{
		return ((Field)fields.get(theAgent)).getLong(theAgent);
    	}
    	catch(IllegalAccessException e)
    	{
    		System.err.println("Unable to getLong on "+((AbstractAgent)theAgent).toString()+"  "+e);
    		return (long) Double.NaN;
    	}
    }
    synchronized public boolean getBoolean(Object theAgent) 
    {
    	try
    	{
		return ((Field)fields.get(theAgent)).getBoolean(theAgent);
    	}
    	catch(IllegalAccessException e)
    	{
    		System.err.println("Unable to getBoolean on "+((AbstractAgent)theAgent).toString()+"  "+e);
    		return false;
    	}
    }

    synchronized public char getChar(Object theAgent) 
    {
    	try
    	{
		return ((Field)fields.get(theAgent)).getChar(theAgent);
    	}
    	catch(IllegalAccessException e)
    	{
    		System.err.println("Unable to getBoolean on "+((AbstractAgent)theAgent).toString()+"  "+e);
    		return Character.MAX_VALUE;
    	}
    }

    synchronized public Object getObject(Object theAgent) 
    {
    	try
    	{
		return ((Field)fields.get(theAgent)).get(theAgent);
    	}
    	catch(IllegalAccessException e)
    	{
    		System.err.println("Unable to getObject on "+((AbstractAgent)theAgent).toString()+"  "+e);
    		return null;
    	}
    }
    public int getPropertyCount()
    {
	return fields.size();
    }
}
