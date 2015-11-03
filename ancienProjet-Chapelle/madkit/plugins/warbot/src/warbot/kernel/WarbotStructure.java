/*
* WarbotStructure.java -Warbot: robots battles in MadKit
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

import java.util.Iterator;
import java.util.Vector;

import madkit.kernel.AgentAddress;
import madkit.messages.ACLMessage;
import SEdit.SNode;
import SEdit.Structure;


public class WarbotStructure extends Structure
{
    static int cpt=1;

	AgentAddress schedulerAgent;
    WarbotScheduler scheduler;
    WarbotEnvironment warbotWorld;

    static String groupGenerator="warbot";
    String simulationGroup="WarbotApp";

    Entity lastEntity=null;

    Entity getLastEntity(){return lastEntity;}

    int worldWidth=800;
    int worldHeight=800;

    public void setWorldWidth(int v){
    worldWidth = v;
    }

    public int getWorldWidth(){return worldWidth;}

    public void setWorldHeight(int v){worldHeight = v;}
    public int getWorldHeight(){return worldHeight;}

    public WarbotStructure()
    {
    	/*
    	setGridSize(16);
    	setSnapToGrid(true);
    	setDisplayGrid(true); */
	}

	public void preactivate(){
		System.out.println("\nLAUNCHING WARBOT......      ");
                System.out.println("Warbot "+ WarbotScheduler.VERSION+"  "+WarbotScheduler.DATE);
		System.out.println("by "+WarbotScheduler.AUTHOR);
		System.out.println("(C) Madkit Team - 2002");

        if (agent.isGroup(simulationGroup)){
            cpt++;
            simulationGroup=groupGenerator+"-"+cpt;
        }
        agent.createGroup(false,simulationGroup,null,new WarbotIdentifier());
        agent.requestRole(simulationGroup,"mainpanel",WarbotIdentifier.password);
        if (scheduler == null){
            scheduler = new WarbotScheduler(this,simulationGroup); // creating the scheduler
            warbotWorld = new WarbotEnvironment(this,getWorldWidth(),getWorldHeight(),simulationGroup); // launching the environment
            scheduler.setEnvironment(warbotWorld);

            agent.launchAgent(scheduler,"warbot scheduler",false); // launching the scheduler
            agent.pause(500);
            agent.launchAgent(warbotWorld,"warbotWorld",false); // launching the environment
            schedulerAgent=scheduler.getAddress();
        }

    }

	public void activate(){        // insert elements which were not already in the environment
        for (Iterator i=this.getNodes().iterator();i.hasNext();){
            SNode node = (SNode) i.next();
            if (node instanceof Entity){
                Entity ent = (Entity) node;
                if (!warbotWorld.contains(ent))
                    warbotWorld.addEntity(ent);
            }
        }
	}


    public void addNode(SNode o)
    {
        super.addNode(o);
        //agent.sendMessage(simulationGroup,"environment", new InstallMessage((Entity)o));
        lastEntity=(Entity) o;
        lastEntity.setSimulationGroup(simulationGroup);
        if (warbotWorld != null)
            warbotWorld.addEntity(lastEntity);
    }

    public void addNode(SNode o, String s)
    {
        super.addNode(o,s);
        //agent.sendMessage(simulationGroup,"environment", new InstallMessage((Entity)o));
        lastEntity=(Entity) o;
        lastEntity.setSimulationGroup(simulationGroup);
        if (warbotWorld != null)
            warbotWorld.addEntity(lastEntity);
    }



  /** Close the structure by deleting all entities before closing. Is used
  	  to delete all agents that are related to entities */
  	public void end(){
		   warbotWorld.deleteEntities();
		   agent.killAgent(scheduler);
		   System.out.println("deleting the world");
		   agent.killAgent(warbotWorld);
  	}

   	public void mapViewer() {
            int w = getWorldWidth()+30;
            int h = getWorldHeight()+30;
			int ratio=1;
			for(;ratio<100;ratio++)
				if( (w/ratio) < 500 && (h/ratio) < 500)
					break;
            MapViewer mv = new MapViewer(w/ratio,h/ratio,ratio,simulationGroup);
            agent.launchAgent(mv,"map viewer",true);
            mv.observe();
    }

	boolean isDetecting=false;
	public void showDetect(){
		   isDetecting=!isDetecting;
		   Vector v = this.getNodes();
		   for (Iterator i=v.iterator();i.hasNext();){
			   SNode node=(SNode) i.next();
			   if (node instanceof BasicBody)
				  ((BasicBody)node).setShowDetect(isDetecting);
		   }
	}

    boolean isShowUserMessage=true;
    public void showUserMessage(){
         isShowUserMessage=!isShowUserMessage;
         Vector v = this.getNodes();
         for (Iterator i=v.iterator();i.hasNext();){
                 SNode node=(SNode) i.next();
                 if (node instanceof BasicBody)
                        ((BasicBody)node).setShowUserMessage(isShowUserMessage);
         }
    }

    boolean isShowMessages=true;
    public void showMessages(){
         isShowMessages=!isShowMessages;
         Vector v = this.getNodes();
         for (Iterator i=v.iterator();i.hasNext();){
                 SNode node=(SNode) i.next();
                 if (node instanceof BasicBody)
                        ((BasicBody)node).setShowMessages(isShowMessages);
         }
    }


    public void start(){
        System.out.println("starting..");
        agent.sendMessage(schedulerAgent,new ACLMessage("START"));
    }

    public void stop(){
        agent.sendMessage(schedulerAgent,new ACLMessage("STOP"));
	}

    public void step(){
        agent.sendMessage(schedulerAgent,new ACLMessage("STEP"));
	}


/*    public void modifyDelay(){
       		if (ed == null)
       			ed = new WorldControlDialog(this);
       		else
       			ed.show();
    }*/

/*  public void end()
  {
    	for (int i=0; i < entities.size(); i++)
			killAgent((AbstractAgent) entities.elementAt(i));
    	for (int i=0; i < viewers.size(); i++)
			killAgent((AbstractAgent) viewers.elementAt(i));
		killAgent(getEnvironment());
		leaveGroup(getGroupName());
    } */
}
