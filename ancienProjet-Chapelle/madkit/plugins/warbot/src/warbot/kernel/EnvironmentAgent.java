/*
* EnvironmentAgent.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

import madkit.kernel.AbstractAgent;
import madkit.kernel.ReferenceableAgent;

abstract class EnvironmentAgent extends AbstractAgent implements ReferenceableAgent
{
String simulationGroup;

EnvironmentAgent(String theGroup){simulationGroup=theGroup;}

void setSimulationGroup(String theGroup){simulationGroup=theGroup;}
String getSimulationGroup(){return simulationGroup;}

public void activate()
{
	requestRole(simulationGroup,"environment",WarbotIdentifier.password);
}

abstract  void removeEntity(Entity theEntity);
abstract  boolean addEntity(Entity theEntity);

abstract boolean contains(Entity theEntity);


/*Object requestEnv(String theMethod,Object[] parameters)
{
	Method m=null;
	Class[] c=null;
	if (parameters!=null)
	{
		c=new Class[parameters.length];
		try{
			for(int i=0;i<parameters.length;i++)
			{
				/*if(parameters[i] instanceof Entity2D)
					c[i]=Class.forName("madkit.models.simukit.Entity2D");
				else
					c[i]=parameters[i].getClass();
			}

		}
		catch(Exception e)
		{
			System.err.println("bug ds request env "+e+" "+e.getMessage());
			return null;
		}
	}
	try{
		m = (this.getClass()).getMethod(theMethod,c);
	}
	catch(Exception e)
	{
		System.err.println("my Environment don't know how "+theMethod+"\n"+e+" "+e.getMessage());
		return null;
	}
	try{
		return m.invoke(this,parameters);
	}
	catch (Exception e)
	{
		System.err.println("Can't invoke:"+theMethod+" "+e.getMessage());
	}
	return null;
}
*/
}
