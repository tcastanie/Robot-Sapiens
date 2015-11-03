/*
* Observer.java -TurtleKit - A 'star logo' in MadKit
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

import madkit.kernel.AbstractAgent;
import madkit.kernel.ReferenceableAgent;
import madkit.kernel.Watcher;
import madkit.simulation.probes.ReflexiveProbe;

/** Observer is an abstract class that have to be extended in order to initialize patches 
	or make some observations.
	To observe turtle, create TurtleProbe on a special role and then add it using addProbe();
			walkers = new TurtleProbe(getSimulationGroup(),"walker");
			addProbe(walkers);

	You can specially create Turtle[] variables using TurtleProbe's getTurtles() method.
	This method permits to obtain an array of turtle regarding the role they play.
	MoreOver this agent has access to the patchGrid variable in order to observe
	or initialize the patches.
  @author Fabien MICHEL
  @see TurtleProbe
  @version 3.0 20/02/2002 */

public abstract class Observer extends Watcher implements ReferenceableAgent
{
      	String simulationGroup = null;
	public Patch[][] patchGrid=new Patch[0][0];
	private EnvProbe patchProbe; 
	public int envWidth,envHeight;

/** MadKit kernel usage*/  
    final public void activate()  
    {
		System.err.println(getName()+" activated");
		joinGroup(simulationGroup);
		requestRole(simulationGroup,"observer");
		addProbe(new EnvProbe("grid",simulationGroup,"world",this));
		setup();
    }
    
    final public String getSimulationGroup(){return simulationGroup;}

	/**override this method to observe the state of the world using turtle tables
	  or/and the patchGrid variable*/
	public void watch(){};

  	/**override this method to make other initializations,not in constructor*/
	public void setup(){};

final synchronized void updateWorldData(Patch[][] grid)
{
	patchGrid=grid;
}

}

class EnvProbe extends ReflexiveProbe
{
Observer myViewer;

EnvProbe(String property, String group, String role,Observer viewer)
{
	super(group,role,property);
	myViewer=viewer;
}

public void update(AbstractAgent theAgent, boolean added)
{
	super.update(theAgent,added);
	if(added)
		myViewer.updateWorldData((Patch[][])getObject(theAgent));
	System.err.println(this);
}

public void initialize()
{
	super.initialize();
	if(numberOfAgents()>0)
		myViewer.updateWorldData((Patch[][])getObject(getAgentNb(0)));
	System.err.println(this);
}

}
