package smaapp;

// ***********************************
// *** RobAppSma.java              ***
// *** Creation: 2003              ***
// *** Jérôme Chapelle@lirmm.fr    ***
// ***********************************

//import madkit.kernel.*;
import kernelsim.*;
//import java.awt.*;
//import java.io.*;
import java.util.*;
/** The main class used to choose, tune, and launch a simulation 
* @author Jérôme Chapelle 2004
*/
public class RobAppSma extends RmcLauncherApp
{
	static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;

    public RobAppSma()
	{
		//super("./rmc/simulations/robot/");
                //super("./rmc/apprentis/robot/");
                super("./smaapp/");
		setSimulationName("blank");
		setSimulationFile(getSimulationName());
	}

    /** Initializes : creates the robots and add them in the Vector <i>otherRobots</i>. */
    protected void init()
    {
	Vector<RobotBrain> V= new Vector<RobotBrain>();
	
	RobotBrain r=null;

	System.out.println("salut !!!!");
	//System.exit(0);
	for(int i=0;i<getNumberRob();i++)
	{
		r = new RobotBrain();
	        addRobot(r);
	        r.initNsch();
		V.addElement(r);
	        System.out.print("robot (N_agent="+r.N_agent
                                  +") world dans activate");
	}
	RobotBrain.otherRobots=V;
        //foundGroup(simulationName);
	
	
        // Reviendre
	//RobotWatcher52 rw = new SatWatcher(simulationName,getNumberRob());
	//launchAgent(rw,"robot watcher",true);
	/*
        RobotWatcher rw2 = new RobotWorldViewer(simulationName);
	launchAgent(rw2,"robot watcher",true);

	ShaftWatcher sw = new ShaftWatcher(simulationName);
	launchAgent(sw,"shaft watcher",true);
	*/
    }



}









