package kernelsim;

//import java.io.*;
//import java.lang.reflect.*;
//import java.util.Vector;
//import java.awt.Graphics;
import madkit.kernel.*;
import smaapp.*;

/** The SensorProbeApp class, used to probe the values of the sensors<br>
* (fr: classe utilisée pour récupérer les valeurs des capteurs)
* @author Jérôme Chapelle
* @version 1.0 */

public class SensorProbeApp extends Probe
{

    public SensorProbeApp(String group,String role)
    {
	super(group, role);
	System.out.println("sensorProbe group:role="+group+":"+role);
    }

    /*public void changeGroupRole(String group,String role)
    {
	super(group, role);
	System.out.println("sensorProbe group:role="+group+":"+role);
    }*/
    @SuppressWarnings("unchecked")
    NeuronAgent[] getSensors()
    {	//ListIterator ilist=getAgentsIterator();
    	System.out.println("SensorProb group:role="+getGroup()+":"+getRole());
    	System.out.println("getSensors() (noa) => nb sensor:"+numberOfAgents());
    	NeuronAgent[] nlist=(NeuronAgent[]) getCurrentAgentsList().toArray(new NeuronAgent[0]);
    	System.out.println("getSensors() (gcal) => nb sensor:"+nlist.length);
    	for (int i=0;i<nlist.length;i++)
    	{
    		System.out.println(nlist[i].getLabel());
    	}
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











