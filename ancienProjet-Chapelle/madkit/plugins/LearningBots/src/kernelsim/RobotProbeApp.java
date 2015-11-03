package kernelsim;

//import java.io.*;
//import java.lang.reflect.*;
//import java.util.Vector;
//import java.awt.Graphics;
import madkit.kernel.*;
import smaapp.RobotBrain;

/** The RobotProbe class

  @author Olivier Simonin
  @version 1.0 */

public class RobotProbeApp extends Probe
{

    public RobotProbeApp(String group,String role)
    {
	super(group, role);
    }
    @SuppressWarnings("unchecked")
    RobotBrain[] getRobots()
    {
    	    	return (RobotBrain[]) getCurrentAgentsList().toArray(new RobotBrain[0]);

		/*int n = agents.length;
		RobotBrain[] t = new RobotBrain[n];
		for(int i =0;i<n;i++)
		{
			t[i] = (RobotBrain) agents[i];
		}
		return t;*/
    }


}











