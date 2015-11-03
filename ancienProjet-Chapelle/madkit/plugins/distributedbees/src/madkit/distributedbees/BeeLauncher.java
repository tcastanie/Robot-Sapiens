/*
* BeeLauncher.java - DistributedBees demo program
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
import java.awt.Color;
import java.util.*;

/** This class allows to create the SIMULATION group.
Then, it is able to launch a various number of Bees, according to the instructions of a "Master User": X red bees, Y blue bees and Z green bees. These bees can be launched or killed at any time of the simulation.
It also launches an EpiphyteAgent, a BeeScheduler and some BeeControllers. For instance, if the "Master User" creates 50 red bees, the BeeLauncher will create a red BeeControler and will ask the BeeEpiphyt and the BeeScheduler to update their probes and activators.

-----------------------------------------------------------
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation","launcher");
        requestRole(BeeLauncher.BEE_COMMUNITY,  group,"launcher");
-----------------------------------------------------------
  @author Pierre BOMMEL, Fabien MICHEL
  @version 1.1 07/07/2000 */

public class BeeLauncher extends Scheduler
{
    /////// ATTRIBUTS ///////////////
    public int version = 2;
    public static final int WORLD_SIZE = 500;
    public static final String BEE_COMMUNITY = "buzz";
    protected String defaultPurpose = "buzz";
    protected List theLaunchers = new ArrayList();
    protected TurboMethodActivator killRedBees,killBlueBees,killGreenBees;
    protected Activator redControler,blueControler,greenControler,schedul;
    /**
     * @label myGUI
     */
    BeeLauncherGUI myGUI;

    BeeEpiphyt epiphyt;

    /**
     * @label watcher
     */
    BeeWorldViewer watcher;

    int numberOfGreenBees = 0;
    int numberOfRedBees = 0;
    int numberOfBlueBees = 0;

    boolean launchTheGreen = false;
    boolean launchTheRed = false;
    boolean launchTheBlue = false;
    boolean killTheGreens = false;
    boolean killTheBlues = false;
    boolean killTheReds = false;
    boolean killThemAll = false;
    boolean redOn=false;
    boolean blueOn=false;
    boolean greenOn=false;
    int pause = 1000;
    String group;

    //---------CONSTRUCTORS-----------

    //---------ACCESSORS-----------
    public int getNumberOfRedBees(){return numberOfRedBees;}
    public void setNumberOfRedBees(int v){numberOfRedBees = v;}
    public boolean getLaunchTheRed(){return launchTheRed;}
    public void setNumberOfBlueBees(int v){numberOfBlueBees = v;}
    public int getNumberOfBlueBees(){return numberOfBlueBees;}
    public boolean getLaunchTheBlue(){return launchTheBlue;}
    public int getNumberOfGreenBees(){return numberOfGreenBees;}
    public void setNumberOfGreenBees(int v){numberOfGreenBees = v;}
    public boolean getLaunchTheGreen(){return launchTheGreen;}
    public void setKillThemAll (boolean b) {killThemAll = b;}
    public boolean getKillThemAll(){return killThemAll;}
    public void setLaunchTheRed (boolean b) {launchTheRed = b;}
    public void setLaunchTheBlue (boolean b) {launchTheBlue = b;}
    public void setLaunchTheGreen (boolean b) {launchTheGreen = b;}


    public void setPause(int b)
    {if (b < 1) pause=1;
    else pause=b;}
    public int getPause()
    {return pause; }
    //---------METHODS-----------

    protected void launchScheduler()
    {
    	if(getAgentWithRole(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(), "scheduler") == null)
    	{
            	//System.err.println("is local = "+tmp.isLocal());
    		println("launching local BeeScheduler...");
       		launchAgent(new BeeScheduler(group),"BeeScheduler-"+version, false);
       	}
    }

    protected void launchWatcher()
    {
        if(watcher == null)
            {
                println("launching BeeWorldViewer...");
                watcher = new BeeWorldViewer(BeeLauncher.WORLD_SIZE + 80, BeeLauncher.WORLD_SIZE);
                launchAgent(watcher,"BeeWorldViewer", true);
                watcher.addListener();
                System.err.println("BeeWorldViewer ok !");
            }
        watcher.show = true;
    }

    protected void launchEpiphyt()
    {
        if (epiphyt == null){
            println("launching BeeEpiphyt...");
            epiphyt = new BeeEpiphyt();
            launchAgent(epiphyt,"BeeEpiphyt", false);
        }
    }
    protected void launchControler(Color couleur, String purpose)
    {
       	if(getAgentWithRole(BeeLauncher.BEE_COMMUNITY,  getAddress().getKernel().toString(),couleur.toString()+"Controler") == null)
		launchAgent(new BeeControler(couleur, purpose, group),couleur.toString()+"Controler", false);
    }

    public synchronized void launchBees(String purpose, Color couleur)
    {
        String stringColor = ""; int numberOfBees=0;
        if (couleur.equals(Color.red))
                { stringColor = "red"; numberOfBees = numberOfRedBees;launchTheRed = false;}
        if (couleur.equals(Color.blue))
                {stringColor = "blue";numberOfBees = numberOfBlueBees; launchTheBlue = false;}
        if (couleur.equals(Color.green))
                {stringColor = "green"; numberOfBees = numberOfGreenBees; launchTheGreen = false;}
        //Lance la reine
        println("Launching "+stringColor+" Bees...");
        QueenBee qb = new QueenBee(WORLD_SIZE + 80,WORLD_SIZE, couleur);
        launchAgent(qb, stringColor+" queen bee",false);
        qb.initSwarm(numberOfBees);
        //Lance les abeilles
        for (int i = 0; i < numberOfBees; i++)
            {
                Bee bee = new Bee(qb, couleur);
                qb.addBee(bee);
            }
        System.err.println(""+numberOfBees+" "+stringColor+" bees have been launched");
        //Lance le controleur
        sendMessage(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(), couleur.toString()+"Controler", new StringMessage("purpose"));
    }

    public void killRedBees() {killColoredBees(Color.red);}
    public void killBlueBees() {killColoredBees(Color.blue);}
    public void killGreenBees() {killColoredBees(Color.green);}

    protected synchronized void killColoredBees(Color couleur)
    {
        String stringColor="";
        if (couleur.equals(Color.red))
            {
		for(Iterator i = killRedBees.getAgentsIterator();i.hasNext();)
		    {
			((QueenBee)i.next()).die();
		    }
                stringColor = "red";
		killTheReds = false;
	    }
    
        if (couleur.equals(Color.blue))
            {
		for(Iterator i = killBlueBees.getAgentsIterator();i.hasNext();)
		    {
			((QueenBee)i.next()).die();
		    }
                stringColor = "blue";
		killTheBlues = false;
	    }
        if (couleur.equals(Color.green))
            {
		for(Iterator i = killGreenBees.getAgentsIterator();i.hasNext();)
		    {
			((QueenBee)i.next()).die();
		    }
                stringColor = "green";
		killTheGreens = false;
	    }
	println("kill "+stringColor+" Bees");
    }

    public void reset()
    {
	    try
		{
		    if (epiphyt != null) {killAgent(epiphyt); epiphyt = null;}
		}
	    catch (Exception e) {System.err.println("Erreur de reset()...killAgent(epiphyt) "+e);}

	if(getAgentsWithRole( BeeLauncher.BEE_COMMUNITY,getAddress().getKernel().toString(),"launcher").length==1)
	{
	    System.err.println("killing local agents !!");
	    ((BeeScheduler) schedul.getAgentNb(0)).die();
	    killRedBees();
	    killBlueBees();
	    killGreenBees();
	    ((BeeControler) redControler.getAgentNb(0)).die();
	    ((BeeControler) blueControler.getAgentNb(0)).die();
	    ((BeeControler) greenControler.getAgentNb(0)).die();
	}
	try
	    {
		if (watcher != null) {killAgent(watcher); watcher = null;}
	    }
	catch (Exception e) {System.err.println("Erreur de reset()...killAgent(watcher) "+e);}
	
	System.gc();
	//System.runFinalization();
	println("Reset done: no more agent");
    }

    /** send to all Launchers of the "simulation" group:
        - a StringMessage, when bees are killed, or
        - a TableMessage, indicated the numberOfRedBees, numberOfRedQueen, numberOfBlueBees, numberOfBlueQueen, numberOfGreenBees, numberOfGreenQueen of the world */
    public void updateTheLaunchers()
    {
        println("Send Message to all Launchers of 'simulation' group");
        AgentAddress [] lesLaunchers = getAgentsWithRole(BeeLauncher.BEE_COMMUNITY, "simulation","launcher");
        println("nb de Launchers... = "+lesLaunchers.length);
        if(lesLaunchers.length == 0) return;

        if(killTheReds)
            {
                StringMessage msg = new StringMessage("killTheReds");
                broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation","launcher", msg);
            }
        else
            if(killTheGreens)
                {
                    StringMessage msg = new StringMessage("killTheGreens");
                    broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation","launcher", msg);
                }
            else
                if(killTheBlues)
                    {
                        StringMessage msg = new StringMessage("killTheBlues");
                        broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation","launcher", msg);
                    }
                else
                    {
                        TableMessage message = new TableMessage(this.conversion());
                        broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation","launcher", message);
                    }
    }

    public void handleSimpleMessage(Message m)
    {
        AgentAddress aa = m.getSender();
        theLaunchers.add(aa);
        TableMessage message = new TableMessage(this.conversion(), theLaunchers);
        sendMessage(aa, message);
        pause(300);
        epiphyt.observe();
    }

    public void handleStringMessage(StringMessage msg)
    {
        if (msg.getString().equals("killTheReds")) killRedBees();
        if (msg.getString().equals("killTheGreens")) killGreenBees();
        if (msg.getString().equals("killTheBlues")) killBlueBees();
        if (msg.getString().equals("killThemAll"))
	    {
		theLaunchers.remove(msg.getSender());
		masterBalancing();
	    }
    }

    /**The Launcher handles a TableMessage received from another Launcher. TableMessage contains an array of 3 Vectors (greens, reds et blues). So, the Launcher will creates the colored swarms with the right purpose*/
    public void handleTableMessage(TableMessage msg)
    {
    	if(! msg.getSender().isLocal())
    	{
	        Vector[] tableau = msg.getTable();
	        Vector[] tableauOrigin = this.conversion();
	        for (int i = 0; i< 3; i++)
	            {
	                Vector beesOrigin = tableauOrigin[i];
	                Vector bees = tableau[i];
	                if( bees.size() > beesOrigin.size())
	                    for (int j = beesOrigin.size(); j<bees.size(); j++)
	                        {
	                            if (i == 0)
	                                {
		                       	    ((BeeControler)greenControler.getAgentNb(0)).setPurposeName((String) bees.get(0));
	                                    numberOfGreenBees=((Integer)bees.elementAt(j)).intValue() - 1;
	                                    launchBees((String)bees.elementAt(0), Color.green);
	                                }
	                            if (i == 1)
	                                {
		                       	    ((BeeControler)redControler.getAgentNb(0)).setPurposeName((String) bees.get(0));
	                                    numberOfRedBees =((Integer)bees.elementAt(j)).intValue() - 1;
	                                    launchBees((String)bees.elementAt(0), Color.red);
	                                }
	                            if (i == 2)
	                                {
		                       	    ((BeeControler)blueControler.getAgentNb(0)).setPurposeName((String) bees.get(0));
	                                    numberOfBlueBees =((Integer)bees.elementAt(j)).intValue() - 1;
	                                    launchBees((String)bees.elementAt(0), Color.blue);
	                                }
	                        }
	            }
	  }
	  
          //Ajout pour le balancing automatique
          if (msg.getLaunchers().size() > 0)
            theLaunchers = msg.getLaunchers();
	System.err.println(""+this.getAddress()+" "+theLaunchers);            
    }

/**Methode appelée lors de la reception d'un KernelMessage provenant de son KernelAgent.
 * c'est KernelMessage classique du genre "leaveGroup". Si l'agent qui quitte le groupe est un
 * Launcher, alors il est enlevé de la liste theLaunchers. Si je deviens le 1er element de cette liste
 * alors je deviens "master"
 */
  protected void handleKernelMessage(KernelMessage m)
  {
      if(m.getOperation() == Kernel.DISCONNECTED_FROM)
	  {
	      for(Iterator i = theLaunchers.iterator();i.hasNext();)
		  {
		      AgentAddress tmp = (AgentAddress) i.next();
		      if(tmp.getKernel().equals((KernelAddress) m.getArgument()))
			  i.remove();
		  }
	      masterBalancing();
	      return;
	  }
      /*
      String groupName = ((AGRTrio)m.getArgument()).getGroup();
      System.err.println("handle Kernelmessage:"+m.getOperation()+" GROUP = "+groupName);
      if (groupName.equals("master") || groupName.equals("duplica"))
	  {
	      AgentAddress theDistantLauncher = ((AGRTrio)m.getArgument()).getAgent();
	      System.err.println("the distantLauncher :"+theDistantLauncher.toString()+" leaves group "+groupName);
	      //AgentAddress anAgentToRemove = null;
	      System.err.println("launchers ="+theLaunchers);
	      theLaunchers.remove(theDistantLauncher);
	      masterBalancing();
	      }*/
  }


    public void handleMessage(Message m)
    {
        if (m.getSender() != this.getAddress())
            {
                //for bees creation
                if (m instanceof TableMessage) handleTableMessage((TableMessage)m);
                else
                    //for the murder of bees ...
                    if (m instanceof StringMessage) handleStringMessage((StringMessage)m);
                    //for balancing
                    else
                      if (m instanceof KernelMessage) handleKernelMessage((KernelMessage) m);
                      //It answers to the sender of m, the number of red, blue and green bees running in the "bees" local group */
                      else handleSimpleMessage(m);
            }

    }

        //--------- methods --------------
    /**this method is call when the DuplicaLauncher balances from group "duplica" to group "master" */
    public synchronized void masterBalancing()
    {
	System.err.println("Master Balancing check");
	System.err.println(""+getAddress()+" "+theLaunchers);
	System.err.println("agent with role master "+getAgentWithRole(BeeLauncher.BEE_COMMUNITY, "master","launcher") );
	if(getAgentWithRole(BeeLauncher.BEE_COMMUNITY, "master","launcher") == null && ((AgentAddress)theLaunchers.get(0)).equals(getAddress()))
	    {
		group = "master";
		createGroup(true, BeeLauncher.BEE_COMMUNITY, "master", null, null);
		requestRole(BeeLauncher.BEE_COMMUNITY, "master","launcher",null);
		//leaveGroup(BeeLauncher.BEE_COMMUNITY, "duplica");
		// Change the Controlers
		
		launchEpiphyt();
		
		setName("Master");
		myGUI.updateName();
		System.err.println("\n"+"Duplica to Master OK."+"\n");
	    }
	pause(50);
	((BeeScheduler) schedul.getAgentNb(0)).monGroup=group;
	((BeeScheduler) schedul.getAgentNb(0)).defaultPause=0;
    }




    /**Conversion of "lesAbeilles" (vector of vectors) in an array of 3 vectors (greens, reds et blues); each one contains, firstly, a string indicating the purpose of the swarm, then a list of integers: the number of bees by swarm (bees + queen).
       world state*/
    public Vector[] conversion()
    {
        Vector[] tab = new Vector[3];
        Vector greens = new Vector();
        Vector reds = new Vector();
        Vector blues = new Vector();
	greens.addElement( ((BeeControler)greenControler.getAgentNb(0)).getPurposeName());
	reds.addElement( ((BeeControler)redControler.getAgentNb(0)).getPurposeName());
	blues.addElement( ((BeeControler)blueControler.getAgentNb(0)).getPurposeName());

	for(Iterator i = killRedBees.getAgentsIterator();i.hasNext();)
	    {
		QueenBee qb = (QueenBee) i.next();
		reds.add(new Integer(qb.getSwarmSize()));
	    }

	for(Iterator i = killBlueBees.getAgentsIterator();i.hasNext();)
	    {
		QueenBee qb = (QueenBee) i.next();
		blues.add(new Integer(qb.getSwarmSize()));
	    }

	for(Iterator i = killGreenBees.getAgentsIterator();i.hasNext();)
	    {
		QueenBee qb = (QueenBee) i.next();
		greens.add(new Integer(qb.getSwarmSize()));
	    }
        tab[0] = greens;
        tab[1] = reds;
        tab[2] = blues;
        return tab;
    }

    public void initGUI()
    {
        setGUIObject(myGUI = new BeeLauncherGUI(this));
    }

    public void activate()
    {
	createGroup(false, BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(),null,null);
	createGroup(true, BeeLauncher.BEE_COMMUNITY, "simulation", null, null);
	pause(200);
	createGroup(true, BeeLauncher.BEE_COMMUNITY, "master", null, null);
	//createGroup(true, BeeLauncher.BEE_COMMUNITY, "duplica", null, null);
	createGroup(false, BeeLauncher.BEE_COMMUNITY, "bees", null, null);
	
	
	requestRole(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(),"launcher",null);
	
	launchControler(Color.red, defaultPurpose);
	launchControler(Color.blue, defaultPurpose);
	launchControler(Color.green, defaultPurpose);
	
	killRedBees = new TurboMethodActivator("die", BeeLauncher.BEE_COMMUNITY, "bees",Color.red.toString()+"Queen");
	killBlueBees = new TurboMethodActivator("die", BeeLauncher.BEE_COMMUNITY, "bees",Color.blue.toString()+"Queen");
	killGreenBees = new TurboMethodActivator("die", BeeLauncher.BEE_COMMUNITY, "bees",Color.green.toString()+"Queen");
	redControler = new Activator(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(),Color.red.toString()+"Controler");
	blueControler = new Activator(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(),Color.blue.toString()+"Controler");
	greenControler = new Activator(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(),Color.green.toString()+"Controler");
	schedul = new Activator(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(),"scheduler");
	addActivator(killRedBees);addActivator(killBlueBees);addActivator(killGreenBees);
	addActivator(blueControler);addActivator(redControler);addActivator(greenControler);addActivator(schedul);
	//System.err.println(getActivators()[0]);
	

	if(true || getAgentWithRole(BeeLauncher.BEE_COMMUNITY, "master","launcher") == null)
	    {
		group = "master";
		requestRole(BeeLauncher.BEE_COMMUNITY, "master","launcher",null);
		pause(50);
		launchEpiphyt();
		theLaunchers.add(getAddress());
	    }
	else
	    {
		group = "duplica";
		//requestRole(BeeLauncher.BEE_COMMUNITY, "duplica","launcher",null);
                println("Send message to masterLauncher");
                sendMessage(BeeLauncher.BEE_COMMUNITY, "master","launcher", new Message());
                println("Waiting for MasterLauncher answer ...");
                pause(50);
		Message msg = waitNextMessage(5000);
		if(msg == null)
		{
			System.err.println(""+this.getAddress()+" Master is Gone :((((((((");
			pause(500);
			killAgent(this);
			exitImmediatlyOnKill();
		}
		handleMessage(msg);
		println("Receive MasterLauncher message !");
	    }
	launchScheduler();
	setName(group);
	launchWatcher();
	myGUI.updateName();    
	requestRole(BeeLauncher.BEE_COMMUNITY, "simulation", "launcher", null);
        AgentAddress mykernel = getAgentWithRole("system","kernel");
        //sendMessage(mykernel, new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.LEAVE_GROUP));
	sendMessage(getAgentWithRole("system","kernel"), new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK, Kernel.DISCONNECTED_FROM));
	
    }

public void checkExistingSimulation()
{
	if(group.equals("master") && getAgentsWithRole(BeeLauncher.BEE_COMMUNITY,"master","launcher").length>1)
	{
		System.err.println("\ntesting" );
	        AgentAddress [] masters = getAgentsWithRole(BeeLauncher.BEE_COMMUNITY,"master","launcher");
		for(int i = 0;i<masters.length;i++)
			if(! getAddress().equals(masters[i]) && getAddress().toString().compareTo(masters[i].toString())>0)
			{
			    try
				{
				    if (epiphyt != null) {killAgent(epiphyt); epiphyt = null;}
				}
			    catch (Exception e) {System.err.println("while duplica balancing (entering an existing simulation) "+e);}
				if(! leaveGroup(BeeLauncher.BEE_COMMUNITY,"master"))
					System.err.println("\n"+"erreur sur leaveGroup");
	System.err.println("agent with role master"+getAgentWithRole(BeeLauncher.BEE_COMMUNITY, "master","launcher") );
				group="duplica";
				//requestRole(BeeLauncher.BEE_COMMUNITY, "duplica","launcher",null);
		                println("Send message to masterLauncher");
		                sendMessage(BeeLauncher.BEE_COMMUNITY, "master","launcher", new Message());
		                println("Waiting for MasterLauncher answer ...");
		                pause(50);
				Message msg = waitNextMessage(5000);
				if(msg == null)
				{
					System.err.println(""+this.getAddress()+" Master is Gone :((((((((");
					pause(500);
					killAgent(this);
					exitImmediatlyOnKill();
				}
				handleMessage(msg);
				println("Receive MasterLauncher message !");
				setName("Duplica");
				myGUI.updateName();
				System.err.println("\n"+"Master to Duplica OK."+"\n");
				pause(50);
				((BeeScheduler) schedul.getAgentNb(0)).monGroup=group;
				((BeeScheduler) schedul.getAgentNb(0)).defaultPause=0;
				System.err.println("\n"+theLaunchers);
			}
	}
}
				
		

    public void live()
    {
	while(true)
	    {
		exitImmediatlyOnKill();
		pause(50);
	       	exitImmediatlyOnKill();
	       	checkExistingSimulation();
                if (launchTheGreen)
                    {
                        launchBees(defaultPurpose, Color.green);
                        updateTheLaunchers();
                    }
                if (launchTheRed)
                    {
                        launchBees(defaultPurpose, Color.red);
                        updateTheLaunchers();
                    }
                if (launchTheBlue)
                    {
                        launchBees(defaultPurpose, Color.blue);
                        updateTheLaunchers();
                    }
                if (killTheGreens)
		    {
			updateTheLaunchers();
			killGreenBees();
		    }
                if (killTheReds)
		    {
			updateTheLaunchers();
			killRedBees();
		    }
                if (killTheBlues)// && blueControler != null)
		    {
			updateTheLaunchers();
			killBlueBees();
		    }
                Message msg = nextMessage();
                if (msg != null)
                    {
                        System.err.println("You know what ? I receive a message ! "+msg);
                        handleMessage(msg);
                    }
            }
    }

    public void end()
    {
	leaveGroup(BeeLauncher.BEE_COMMUNITY, "master");
        reset();
	pause(200);
	broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation","launcher", new StringMessage("killThemAll"));
        System.err.println(""+this.getAddress()+"BeeLauncher > agent ENDED");
        super.end();
    }
}

