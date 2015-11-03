/*
* ObjectProbe.java - Simulation: the general classes for handling simulation in MadKit
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

import java.util.Iterator;
/** This probe inspects object properties on Referenceable agents.
  @author Fabien Michel (version 2.0 & 3.0)
  @author Olivier Gutknecht (version 0.2)
  @version 3.0
   */

public class ObjectProbe extends ReflexiveProbe
{ 
    int count = 0;  
    public ObjectProbe(String group, String role, String property)
    {
	super(group, role, property);
    }
    
   public ObjectProbe(String community, String group, String role,String property)
    {
	super(community, group, role, property);
    }
    
    synchronized public Object[] getObjects()
    {
	
	Object[] objs = new Object[numberOfAgents()];
	count = 0;
	
	try
	    {	
		for (Iterator i = getAgentsIterator();i.hasNext();count++)
		{
			objs[count] = getObject(i.next());
		}
	    }
	catch (Exception e) 
	    {
		System.err.println("<Object Probe> Can't access property: "+e);
	    }
	if (count==0)
	    {
		objs = null;
	    }
	
	return objs;
    }

    public int getCount()
    {
	getObjects();
	return count;
    }
}










