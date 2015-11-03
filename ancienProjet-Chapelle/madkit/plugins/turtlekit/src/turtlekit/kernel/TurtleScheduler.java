/*
* TurtleScheduler.java -TurtleKit - A 'star logo' in MadKit
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

import madkit.kernel.Message;
import madkit.simulation.activators.TurboMethodActivator;

/** The TurtleKit scheduler

  @author Fabien MICHEL
  @version 2.1 10/04/2002 

*/

public class TurtleScheduler extends madkit.kernel.Scheduler

{
    String group;
    TurtleActivator turtleDoIt;
    TurboMethodActivator oberserversDoIt,viewersDoIt,displayAllWorld,updateDisplay,evaporation,diffusion;

    int iteration = 0;
    int delay = 100;

    public TurtleScheduler(String group)
    {
	this.group=group;
	//setDebug(true);
    }

    final public void activate()

    {
	joinGroup(group);
	requestRole(group, "scheduler");
	sendMessage(group,"launcher",new TopMessage());
	waitNextMessage();

	turtleDoIt = new TurtleActivator(group);
	addActivator(turtleDoIt);
	oberserversDoIt = new TurboMethodActivator("watch",group,"observer");
	addActivator(oberserversDoIt);	
	viewersDoIt = new TurboMethodActivator("display",group,"viewer");
	addActivator(viewersDoIt );	
	diffusion= new TurboMethodActivator("diffusion",group,"world");
	addActivator(diffusion);	
	evaporation = new TurboMethodActivator("evaporation",group,"world");
	addActivator(evaporation);	
	displayAllWorld = new TurboMethodActivator("displayOn",group,"world");
	addActivator(displayAllWorld);	
	updateDisplay = new TurboMethodActivator("displayOff",group,"world");
	addActivator(updateDisplay);	

	oberserversDoIt.execute();
    }

final public void live()
{
	while(true)
	{
		exitImmediatlyOnKill();
		if (delay == 0)
			Thread.yield();
		else
			pause(delay);
		checkMail();
		scheduleWorld();
	}	
} 

public void end()
{
	removeAllActivators();
	sendMessage(group,"launcher",new TopMessage());
	leaveGroup(group);
}

final private void checkMail()
{
	Message m = nextMessage();
	if ( m != null)
	{
		sendMessage(m.getSender(),new TopMessage());
		displayAllWorld.execute();   
		while(true)
		{
			exitImmediatlyOnKill();
			m = nextMessage();
			if(m != null)
			if(m instanceof TopMessage)
			{
				displayAllWorld.execute();   
				viewersDoIt.execute();   
				return;
			}
		pause(300);
		displayAllWorld.execute();   
		viewersDoIt.execute();   
		}
	}
}

final protected void executeTurtles()
{
	turtleDoIt.execute();
}

final protected void executeDiffusion()
{
	diffusion.execute();
}

final protected void executeEvaporation()
{
	evaporation.execute();
}

final protected void executeObservers()
{
	oberserversDoIt.execute();
}

final protected void executeDisplay()
{
	viewersDoIt.execute();
	updateDisplay.execute();
}

final protected void incrementeIteration()
{
	iteration++;
}

/** This method can be overriden to define a special kind of schedule
Default schedule is :
<p>
<code>public void scheduleWorld()
{
	executeTurtles();
	executeDiffusion();
	executeEvaporation();
	executeObservers();
	executeDisplay();
	incrementeIteration();
}</code>
*/
public void scheduleWorld()
{
	executeTurtles();
	executeDiffusion();
	executeEvaporation();
	executeObservers();
	executeDisplay();
	incrementeIteration();
}

	
/*
final Turtle[] getTurtles()
{
	return turtleDoIt.getTurtles();
}*/

}

class StopMessage extends Message
{}
