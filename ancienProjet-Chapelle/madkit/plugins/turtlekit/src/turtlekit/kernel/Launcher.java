/*
* Launcher.java -TurtleKit - A 'star logo' in MadKit
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

import java.util.Enumeration;
import java.util.Vector;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;

/** This agent sets up, launches and manages Turtle based simulations.
    You have to defined a subclass of this one to custom and launch a simulation.
    Yo have at least to override the addSimulationAgents method to create the agents
    you want to launch in your simulation: turtles, viewers and observers.
    To declare patch variables (PatchVariable objects) you have to override the
    initializePatchVariables method.
    
    @author Fabien MICHEL
  @version 4.0 25/04/2002 */

public abstract class Launcher extends Agent
{
    LauncherGui onScreen;
    Vector flavors=null;
    Vector launchedAgents=new Vector();
    TurtleEnvironment playGround=null;
    TurtleScheduler sch=null;
    boolean run = false,start=false,wrap=true,diffusion=false,pythonOn=false;
    String simulationName=("? NAME ?");
    int cellSize = 4,cyclePause = 10,cycleDisplayEvery=1000,envWidth=100,envHeight=100;
    
    /**The constructor is where you have to change the default values
       of the simulation parameters using the corresponding accessors.
       Default Values are:
       setSimulationName("? NAME ?"); //The simulation name corresponds to the Madkit
       group that will be created for the simulation.
       setWidth(100);
       setHeight(100);
       setCellSize(4);		//onscreen size for patches and trutles
       setWrapModeOn(false);
       setCyclePause(10); //cycle pause represents the pause time between
       two simulation steps. This default quick  pause 
       is supposed to avoid that the simulation takes all ressources. 
    */
    public Launcher()
    {
    	/*try{
	    	Class python = Class.forName("org.python.util.PythonInterpreter");
		interp = (org.python.util.PythonInterpreter) python.newInstance();
	    	pythonOn = true;
	}
	catch(Exception e)	{		pythonOn = false;}*/
	
   }
    
    
    final public void setWidth (int add){envWidth = add;}
    final public int getWidth(){return envWidth;}
    final public void setCellSize (int add){cellSize = add;}
    final public int getCellSize(){return cellSize;}
    final public void setHeight (int add){envHeight = add;}
    final public int getHeight(){return envHeight;}
    final public String getSimulationName(){return simulationName;}
    final public void setSimulationName(String name){simulationName = name;}   
    
    
    final public void setCyclePause (int add){
	cyclePause  = add;
	if (sch != null) sch.delay = cyclePause ;}
    
    final void setReset ()
    {
	start=false;
	println("Reseting: Please wait ...");
	for(Enumeration e = launchedAgents.elements();e.hasMoreElements();)
	    killAgent((AbstractAgent) e.nextElement());
   	if (run)
	    {
		run = false;
		//sendMessage(sch.getAddress(), new StopMessage());
		//waitNextMessage();
	    }
	killAgent(sch);
	sch=null;
	playGround.finalReset();
	killAgent(playGround);
	flavors=null;
	launchedAgents.removeAllElements();
	//System.gc();
	//System.runFinalization();
	//waitNextMessage();
	launchSimulation();
	start = true;
	run = true;	    
    }
    final public void setWrapModeOn (boolean b)
    {
	if (playGround != null && start && run)
	    {
		start = false;
		run = false;
		sendMessage(sch.getAddress(), new TopMessage());
		waitNextMessage();
		playGround.wrap=b;
		wrap=b;
		if (diffusion) playGround.initNeighborhood();
		sendMessage(sch.getAddress(), new TopMessage());
		run = true;
		start =true;
		return;
	    }
	if (playGround != null && start)
	    {
		start = false;
		playGround.wrap=b;
		wrap=b;
		if (diffusion) playGround.initNeighborhood();
		start =true;
		return;
	    }
	wrap = b;
    }
    void setStop()
    {
	if (run)
	    {
		run = false;
		sendMessage(sch.getAddress(), new TopMessage());
		waitNextMessage();
		println("Simulation paused");
	    }
	else 
	    {
		sendMessage(sch.getAddress(), new TopMessage());
		println("Simulation running");
		run = true;
	    }
    }

    final void stepByStep()
    {
	sendMessage(sch.getAddress(), new TopMessage());
	sendMessage(sch.getAddress(), new TopMessage());
    }
    //////////////////////////////////////////////////////////////////
    final void initializeVariables()
    {
	System.err.println(flavors.toString());
	int dcIndex=0,evapIndex=0,index=0;
	String[] fls = new String[flavors.size()];
	double[] vals = new double[flavors.size()];
	for(Enumeration e = flavors.elements();e.hasMoreElements();)
	    {
		PatchVariable f = (PatchVariable) e.nextElement();
		System.err.println(f.toString());
		if (f.diffCoef != 0) dcIndex++;
		if (f.evaporation != 0)	evapIndex++;
		fls[index]=f.n;
		vals[index]=f.defaultV;
		index++;

		//for the game of life
		diffusion=true;
		playGround.initNeighborhood();


	    }
	if (dcIndex>0)
	    {
		playGround.initNeighborhood();
		diffusion=true;
	    }
	
	/*for(int i=0;i<vals.length;i++)
	  System.err.println(fls[i]+" "+vals[i]);*/
	
	playGround.addVariables(fls,vals);
	if (dcIndex>0)
	    {
		String[] fls2=new String[dcIndex];
		double[] vals2 = new double[dcIndex];
		dcIndex=0;
		for(Enumeration e = flavors.elements();e.hasMoreElements();)
		    {
			PatchVariable f = (PatchVariable) e.nextElement();
			if (f.diffCoef != 0)
			    {
				fls2[dcIndex]=f.n;
				vals2[dcIndex]=f.diffCoef;
				dcIndex++;
			    }
		    }
		playGround.diffuseVariables(fls2,vals2);
	    }
	if (evapIndex>0)
	    {
		String[] fls2=new String[evapIndex];
		double[] vals2 = new double[evapIndex];
		evapIndex=0;
		for(Enumeration e = flavors.elements();e.hasMoreElements();)
		    {
			PatchVariable f = (PatchVariable) e.nextElement();
			if (f.evaporation != 0)
			    {
				fls2[evapIndex]=f.n;
				vals2[evapIndex]=f.evaporation;
				evapIndex++;
			    }
		    }
		playGround.evapVariables(fls2,vals2);
	    }
    }
    final void launchSimulation()
    {
	println("Launching simulation !");
	println("Please wait...");
	if(sch==null)
		sch = new TurtleScheduler(simulationName);
	sch.delay=cyclePause;
	launchAgent(sch,simulationName+" scheduler",false);
	initializePatchVariables();
	createLogoWorld();
	addSimulationAgents();
	waitNextMessage();
	pause(100);
	sendMessage(sch.getAddress(), new TopMessage());
    }
    final void createLogoWorld()
    {
	playGround = new TurtleEnvironment(envWidth,envHeight,simulationName);
	playGround.wrap=wrap;
	launchAgent(playGround,simulationName+" world",false);
	if (flavors!=null)
		initializeVariables();
    }
    final void initViewer(Viewer v,int cellS,String viewerName){
	v.cellSize=cellS;
	addObserver(v,true,viewerName);
    	if (start && run)
	    {
		sendMessage(sch.getAddress(), new TopMessage());
		waitNextMessage();
		sendMessage(sch.getAddress(), new TopMessage());
	    }
}
    /////////////////////////////////////////////////////////////////////////////////
    /**place a turtle at a random patch*/
    final protected void addTurtle(Turtle t){playGround.addAgent(t);}
    /**place a turtle on the patch (u,v). Be sure to use the addTurtle methods
       in the addSimulationAgents method*/
    final protected void addTurtle(Turtle t,int u,int v){playGround.addAgent(t,u,v);}
    
    /**Add a specified Observer to the simulation.
       Be careful, use this method only in the addSimulationAgents method.*/
    final public void addObserver(Observer theObserver,boolean hasGUI,String agentName){
	theObserver.simulationGroup = simulationName;
	theObserver.envWidth=envWidth;
	theObserver.envHeight=envHeight;
	launchAgent(theObserver,agentName,hasGUI);
	launchedAgents.addElement(theObserver);
    }

    /**Add a specified Observer to the simulation.
       Be careful, do not use these methods before the addSimulationAgents method has been invoked. (during life cycle it is ok)*/
    final public void addObserver(Observer theObserver,boolean hasGUI){
	addObserver(theObserver,hasGUI,simulationName+" Observer");
    }
    /**Add a default world viewer with the current cell size.
       Be careful, do not use these methods before the addSimulationAgents method has been invoked. (during life cycle it is ok)*/
    final public void addViewer(){addViewer(new Viewer());}
    /**add a default world viewer with the specified cell size: cellS*/
    final public void addViewer(int cellS){addViewer(new Viewer(),cellS);}
    /**add a specific world viewer with the specified cell size: cellS*/
    final public void addViewer(Viewer v,int cellS,String viewerName){initViewer(v,cellS,viewerName);}
    /**add a specific world viewer with the specified cell size: cellS*/
    final public void addViewer(Viewer v,int cellS){initViewer(v,cellS,simulationName+" Observer");}
    /**add a specific world viewer with the current cell size*/
    final public void addViewer(Viewer v,String viewerName){initViewer(v,cellSize,viewerName);}
    /**add a specific world viewer with the current cell size*/
    final public void addViewer(Viewer v){addViewer(v,cellSize);}

    /**add a specific scheduler (that overrides the scheduleWorld method for example)*/
    final protected void addMyScheduler(TurtleScheduler s){sch = s;sch.group=simulationName;}
    
    /**Be careful, use this method only in the initializeSimulation method.
       This method add a patch variable (a PatchVariable Object) defined
       with the PatchVariable constructor and the set methods */
    final protected void addPatchVariable(PatchVariable variable){
	if (flavors==null) flavors = new Vector();
	flavors.addElement(variable);}
    
    /**override this method is not compulsory, but it is where you have to initialize
       the patch variables and their properties(evaporation, diffusion...):
       Once you have created a new PatchVariable object and set its properties with the 
       methods of the class PatchVariable(setEvapCoef, setDiffuseCoef and setDefaultValue)
       You have to add it to the simulation using the addPatchVariable method:
       
       protected void initializePatchVariables()
       {
       PatchVariable p = new PatchVariable("flavor");
       p.setDiffuseCoef(0.3153); //Optional
       p.setEvapCoef(0.025); //Optional
       p.setDefaultValue(32); //Optional
       addPatchVariable(a);
       }
    */
    protected void initializePatchVariables(){}
    /**Override this method is compulsory (abstract).
       It is in this method that the optional agents
       of the simulation (turtles, viewers and observers) have to be added.
       To add these agents you have to use the "add" methods of a Launcher:
       - addTurtle
       - addViewer
       - addObserver 
    */
    abstract public void addSimulationAgents(); 
    
    /////////////////////////////////////////////////////////////////////////////
    /**MadKit kernel usage*/
    final public void initGUI()
    {
	setGUIObject(onScreen = new LauncherGui(this));
    }
    
/**MadKit usage*/
public void activate()
{
	onScreen.initialisation();
	int i=2;
	if (isGroup(simulationName))
	{
		while(isGroup(simulationName+" "+i)) i++;
		simulationName+=" "+i;
	}
	createGroup(false,simulationName,null,null);
	requestRole(simulationName,"launcher");
	onScreen.setName(simulationName);
	println("Launcher activated !!");
	println("Waiting for start instruction...");
	while (true)
	{
		pause(50);
		if (start)
		{
			//start=false;
			//onScreen.b1.doClick();
			break;
		}
		if(nextMessage() != null)
		{
			onScreen.b1.doClick();
		}
	}
	start=true;
	run = true;
	launchSimulation();
}
    /**MadKit kernel usage*/
    public final void live()
    {
	/*if(pythonOn)
	{
		AbstractAgent a = new PythonCommandCenter(simulationName);
	    	launchAgent(a, "Python command center",true);
	    	launchedAgents.add(a);
	}*/
	int i=-1;
	while (true)
	    {
		pause(cycleDisplayEvery);
		if (start && i!= sch.iteration)
		    {
			i=sch.iteration;
			println("step "+i);
		    }
	    }
    }
    /**MadKit kernel usage. No redefinition*/
    public final void end()
    {
	println("Closing simulation");
	println("Please wait...");
	while(! start && sch != null) pause(100);
	if (sch!=null)
	    {
		if (run)
			sendMessage(sch.getAddress(), new StopMessage());
		killAgent(sch);
	    }
	if (playGround!=null) playGround.finalReset();
	if (playGround!=null) killAgent(playGround);
	for(Enumeration e = launchedAgents.elements();e.hasMoreElements();)
	    killAgent((AbstractAgent) e.nextElement());
	leaveGroup(simulationName);
	System.gc();
	System.runFinalization();
    }
    
public void launchPython() throws Exception
{
	AbstractAgent a = new PythonCommandCenter(simulationName);
	launchAgent(a, "Python command center",true);
	launchedAgents.add(a);
}

public void stopOrResumeSimulation()
{
	onScreen.b1.doClick();
}    	
    
    
}

