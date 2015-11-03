/*
* WarbotScheduler.java -Warbot: robots battles in MadKit
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

import madkit.kernel.Message;
import madkit.messages.ACLMessage;
import madkit.simulation.activators.TurboMethodActivator;

final public class WarbotScheduler extends madkit.kernel.Scheduler
{
	public static String VERSION="2.1b3-d1";
	public static String AUTHOR="Fabien MICHEL, Jacques FERBER";
	public static String DATE="10/02/2002";

	String simulationGroup;
	private TurboMethodActivator observers;
	private BrainActivator theAgents;
	private int delay=16;
	private boolean running=false;
	private float displaySpeed=(float)1;
	private WarbotEnvironment warbotWorld;
    WarbotStructure theStructure;
    private int nsteps=-1;
	private long cycleNumber=0;

	public long getCycleNumber(){return cycleNumber;}

    public void setRunning(boolean t){
        running = t;
    }

    public boolean isRunning(){return running;}

    public void setEnvironment(WarbotEnvironment w){
        warbotWorld = w;
    }

    WarbotScheduler(WarbotStructure st, String groupName )
    {
        simulationGroup=groupName;
        theStructure=st;
    }

    private void setDelay(int  v)
    {
        if(delay<1)
            this.delay = 1;
        else
            this.delay = v;
        System.out.println("delay: " + this.delay);
    }

    public void activate()
    {
        requestRole(simulationGroup,"scheduler",WarbotIdentifier.password);
        theAgents=new BrainActivator(simulationGroup);
        addActivator(theAgents,WarbotIdentifier.password);
        observers = new TurboMethodActivator("observe",simulationGroup,"observer");
        addActivator(observers,WarbotIdentifier.password);
        //warbotWorld = new WarbotEnvironment(theStructure,800,800,simulationGroup);
        //launchAgent(warbotWorld,"warbotWorld",false);
        //launchAgent(new WarbotViewer(1000,1000,simulationGroup),"warbot control",true);
        println("Scheduler is activated");
    }


    public void live()
    {
        observers.execute();

      	Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        while(true)
        {   if (running)
            {
				cycleNumber++;
                Message m = nextMessage();
                if (m != null) checkMail(m);
                pause(delay);
                theAgents.executeBrains();
                warbotWorld.moveWorld();
                if(Math.random() < displaySpeed)
                    observers.execute();
                theStructure.getEditor().repaint();

                //warbotWorld.checkVictory();
                warbotWorld.checkMail();
                if (nsteps > 0){
                    nsteps--;
                    if (nsteps <= 0){
                        setRunning(false);
                        theStructure.getEditor().repaint();
                        nsteps=-1;
                    }
                }
            }
            else
            {
                pause(200);
                observers.execute();
                Message m = waitNextMessage();
                checkMail(m);
                warbotWorld.checkMail();
            }
        }
    }

	public void end(){
		   System.out.println("removing the activators");
		   this.removeActivator(theAgents);
		   this.removeActivator(observers);
		   System.out.println("scheduler is dying");
	}

    private void checkMail(Message m)
    {
        if (m instanceof ACLMessage){
            String c = ((ACLMessage)m).getAct();
            if (c.equalsIgnoreCase("STOP")){
                setRunning(false);
             } else if (c.equalsIgnoreCase("START"))
                setRunning(true);
            else if (c.equalsIgnoreCase("STEP")){
                setRunning(true);
                nsteps=1;
            }
        }
        else if (m instanceof TopMessage)
        {
                TopMessage top = (TopMessage)m;
                if(running)
                {
                    if(top.getContent() == -4)
                    {
                        displaySpeed/=2;
                        return;
                    }
                    else if(top.getContent()== -1)
                    {
                        displaySpeed=(float)1;
                        return;
                    }
                else if(top.getContent() == -3)
                    running=false;
                    else if(top.getContent() >= 0)
                    {
                    setDelay(top.getContent());
                    return;
                }
                }
                else
                {
                    if(top.getContent() >= 0)
                    {
                    setDelay(top.getContent());
                    return;
                }
                    else if(top.getContent() == -4)
                    {
                        running = true;
                        displaySpeed/=2;
                        return;
                    }
                    else if(top.getContent()== -1)
                    {
                        running = true;
                        displaySpeed=(float)1;
                        return;
                    }
            }
        }
    }

}

