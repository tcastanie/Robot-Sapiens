package turtlekit.kernel;

import madkit.kernel.Message;

/** a launcher that just a python command center and a viewer
    
    @author Fabien MICHEL
    @version 1.1 5/5/2002 */

public class PythonLauncher extends Launcher
{
    
    public PythonLauncher()
    {
	setSimulationName("python");
	setWidth(120);
	setHeight(120);
    }
    
    public void activate()
    {
    	sendMessage(getAddress(), new Message()); // self message to automatically launch
    	super.activate();
    	try
    	{
    		launchPython();
    	}
    	catch(Exception e){}
	stopOrResumeSimulation();
    }
    
    
    /**No turtle just a viewer*/
    public void addSimulationAgents()
    {
	addViewer(3);
    }    
}
