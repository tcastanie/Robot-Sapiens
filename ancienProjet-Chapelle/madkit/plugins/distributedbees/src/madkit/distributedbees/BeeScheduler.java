/*
* BeeScheduler.java - DistributedBees demo program
* Copyright (C) 1998-2004 P. Bommel, F. Michel
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

package madkit.distributedbees;

import madkit.kernel.*;

import madkit.simulation.activators.*;
import madkit.messages.*;
import java.util.Date;

/** 
=========================================================
version 2
	creation d'un groupe avec le nom du site pour éviter multiples schedulers

        requestRole(BeeLauncher.BEE_COMMUNITY,  "master","scheduler");
        or
        requestRole(BeeLauncher.BEE_COMMUNITY,  "duplica","scheduler");

        requestRole(BeeLauncher.BEE_COMMUNITY,  monGroup,"scheduler");
=========================================================
 */

public class BeeScheduler extends Scheduler implements ReferenceableAgent
{
    /////// ATTRIBUTS ///////////////
    public String monGroup;
    public boolean run = true; 
    //protected boolean living = true;
    boolean observerActivator = true;
    int pause = 50;
    long defaultPause = 0;
    long iteration=0;
    int iterationsBetweenMessage = 3;  
    //  les activators  
    TurboMethodActivator watcherActivator ;
    TurboMethodActivator epiphytActivator ;
    TurboMethodActivator controlerActivator; 
    ///////////  Constructor  /////////////
    BeeScheduler(String group)
    {
	monGroup = group;
    }
    
    ////// ACCESSORS  //////////////////
    public void setRun(boolean b) {run=b;}
    public boolean getRun() {return run; }  
    public void setObserverActivator(boolean b) {observerActivator=b;}
    public boolean getObserverActivator() {return observerActivator; } 
    public void setPause(int b)
    {
        if (b < 1) pause=1;
        else pause=b;
    }
    public int getPause() {return pause; }
    public void setIterationsBetweenMessage(int b)
    {if (b < 1) iterationsBetweenMessage=1; 
    else iterationsBetweenMessage=b;}
    public int getIterationsBetweenMessage() {return iterationsBetweenMessage; }

    ////////// METHODS /////////// 
    /** Places BeeActivator on the bees and TurboMethodActivators on the "watcher" and the "controler" in the group 'group'*/
    public synchronized void setActivators() 
    {    
        run = false;     
        controlerActivator = new TurboMethodActivator("commande",BeeLauncher.BEE_COMMUNITY,"simulation","controler");
        addActivator(controlerActivator);
        
        watcherActivator = new TurboMethodActivator("observe",BeeLauncher.BEE_COMMUNITY,"bees","BeeWatcher");
        addActivator(watcherActivator); 

        epiphytActivator = new TurboMethodActivator("observe",BeeLauncher.BEE_COMMUNITY,"master", "BeeEpiphyt");
        addActivator(epiphytActivator); 
        run = true; 
    }
    

    ////////// madkit methods //////////////
    public void activate()
    {
    	System.err.println("\n"+this+" activated\n");

	createGroup(false, BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(), null, null);
	requestRole(BeeLauncher.BEE_COMMUNITY,  getAddress().getKernel().toString(), "scheduler",null);


	createGroup(true, BeeLauncher.BEE_COMMUNITY, "simulation", null, null);
	requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation", "scheduler",null);

        setActivators(); 
    }
   
public void live()
{
	long timeToexecute=0;
	System.err.println("BeeScheduler > LIVE");
	pause(400);
	iteration=0;
	long lastBeep = (new Date()).getTime();
	long newBeep = lastBeep;
	while(true)
	{
		exitImmediatlyOnKill();
		if(run)
		{
			pause( (int) (defaultPause+pause));
			iteration++;
	        	if (controlerActivator != null)
	        		controlerActivator.execute();
			if (watcherActivator != null)
	                	watcherActivator.execute();
			if(iteration % 20 == 0)
			{
			    Message m=null;
				//System.err.println("lastBeep = "+lastBeep );
			    newBeep = (new Date()).getTime();
			    timeToexecute = newBeep - lastBeep;
			    lastBeep = newBeep;
				//System.err.println("newBeep = "+newBeep );
			    //System.err.println("timeToexecute = "+timeToexecute);
			    //System.err.println("pause+ = "+defaultPause);
			    if(monGroup.equals("master"))
				broadcastMessage(BeeLauncher.BEE_COMMUNITY,  "simulation", "scheduler", new ObjectMessage(new Long(timeToexecute)));
			    else
				m = nextMessage();//wait
			    if(m != null && monGroup.equals("duplica"))
				{
				    long other = ((Long) ((ObjectMessage)m).getContent()).longValue();
				    if( (other - timeToexecute) > pause)
					defaultPause++;
				    else if( (timeToexecute - other) > pause)
					defaultPause--;
				}
		    	}
			if (iteration%iterationsBetweenMessage == 0) 
			    epiphytActivator.execute();
		}
	}		
}
    
    public void end()
    {
        System.err.println("BeeScheduler > END");
        run = false;
        removeAllActivators();
        println ("BeeScheduler agent Ended");
    }
    
        public void die()
    {
    	killAgent(this);
}


}






  






