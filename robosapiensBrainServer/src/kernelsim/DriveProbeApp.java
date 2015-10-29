package kernelsim;

//import java.io.*;
//import java.lang.reflect.*;
//import java.util.Vector;
//import java.awt.Graphics;
//import java.util.List;

import madkit.kernel.*;
import smaapp.*;

/** The DriveProbeApp class, used to probe the values of the drives<br>
* (fr: classe utilisée pour récupérer les valeurs des motivations)
* @author Jérôme Chapelle
* @version 1.0 */

public class DriveProbeApp extends Probe
{

    public DriveProbeApp(String group,String role)
    {
	super(group, role);
	System.out.println("driveProbe group:role="+group+":"+role);
    }

    /*public void changeGroupRole(String group,String role)
    {
	super(group, role);
	System.out.println("actorProbe group:role="+group+":"+role);
    }*/

    @SuppressWarnings("unchecked")
    NeuronAgent[] getDrives()
    {
    	
		return (NeuronAgent[]) getCurrentAgentsList().toArray(new NeuronAgent[0]);
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











