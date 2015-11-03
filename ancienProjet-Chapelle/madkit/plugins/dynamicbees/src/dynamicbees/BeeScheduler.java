/*
* BeeScheduler.java - DynamicBees, a demo for the probe and watcher mechanisms
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel
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
package dynamicbees;

import madkit.simulation.activators.TurboMethodActivator;
/**
  @version 2.0
  @author Fabien MICHEL 01/02/2001*/
public class BeeScheduler extends madkit.kernel.Scheduler
{

    TurboMethodActivator queenBees,viewers,bees;
    int delay=30;
    //int iteration = 0;

 public BeeScheduler() {}
 public BeeScheduler(int d)  {  delay=d;  }
 public int getDelay() {return delay;}
 public void setDelay(int  v) {this.delay = v;}

 public void activate()
  {
      println("activation");
      foundGroup("bees");
      requestRole("bees","scheduler");
      queenBees = new TurboMethodActivator("buzz","bees","queen bee");
      addActivator(queenBees);
      bees = new TurboMethodActivator("buzz","bees","bee");
      addActivator(bees);
      viewers = new TurboMethodActivator("observe","bees","bee observer");
      addActivator(viewers);
      System.err.println(bees);
  }

  public void live()
    {
	while(true)
	    {
	    	exitImmediatlyOnKill();
		if (delay==0)
		    Thread.yield(); // So we avoid locking other threads on cooperative JVM
		else
		    pause(delay);
		queenBees.execute();
		bees.execute();
		viewers.execute();
		//iteration++;
	    }
    } 
    
    public void end()
    {
    	println("stopping simulation ...");
    	disposeMyGUI();
    	super.end();
    }
    	
}
