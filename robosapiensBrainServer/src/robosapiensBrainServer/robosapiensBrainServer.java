package robosapiensBrainServer;

import madkit.kernel.Madkit;
import smaapp.RobotBrain;

public class robosapiensBrainServer {

	public static void main(String[] args) {
		System.out.println("hello world!");
		
		RobotBrain r=null;
		r = new RobotBrain("newSim");
		
	    //r.initNsch();
	    String[] args2 = { "--launchAgents","smaapp.RobotBrain,true" }; // This agent with GUI
	    Madkit.main(args2);
	}

}
