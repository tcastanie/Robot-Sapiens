/*
* TurtleActivator.java -TurtleKit - A 'star logo' in MadKit
* Copyright (C) 2000-2002 Fabien Michel
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package turtlekit.kernel;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Activator;

/** The TurtleActivator invoke and set the turtles nextAction variable 

  @author Fabien MICHEL
  @version 3.0 09/10/2001 */

final class TurtleActivator extends Activator
{     
	Map methodTable;

    public TurtleActivator(String group)
    {
	super(group, "turtle");
	methodTable=new HashMap();
    }

public void initialize()
{
	for(Iterator i = getAgentsIterator();i.hasNext();)
	{
		Object o = i.next();
		if (! methodTable.containsKey(o.getClass()))
			methodTable.put(o.getClass(),new HashMap());
	}
}    

public void update(AbstractAgent theAgent, boolean added)
{
	if (added && ! methodTable.containsKey(theAgent.getClass()))
		methodTable.put(theAgent.getClass(),new HashMap());
}    

synchronized public void execute()
{
	for (Iterator i = getAgentsIterator();i.hasNext();)
	/*for(Enumeration enum = agents.elements();enum.hasMoreElements();)*/
	{
		Turtle turtle = (Turtle) i.next();
		//Turtle turtle = (Turtle) enum.nextElement();
		String nextMethod=null; 
		try
		{	
		   nextMethod = (String) (turtle.nextAction).invoke(turtle,null);
		   if (nextMethod != null)
		   {
			if (!((Map) methodTable.get(turtle.getClass())).containsKey(nextMethod))
			{
			try
			    {
				((Map) methodTable.get(turtle.getClass())).put(nextMethod, (turtle.getClass()).getMethod(nextMethod,null));
			    }
			catch (Exception e) {System.err.println("Can't find method:"+e+nextMethod);}
			}
		    turtle.setNextAction( (Method) ((Map) methodTable.get(turtle.getClass())).get(nextMethod) );
		    }
		   else
		   	turtle.setNextAction(null);
		}
		catch (Exception e) {System.err.println("Can't invoke:"+e+" "+(turtle.nextAction).toString()+"\n");e.printStackTrace();}
	}
}

final synchronized Turtle[] getTurtles()
{
	return (Turtle[]) getCurrentAgentsList().toArray(new Turtle[0]);
}
	

}

