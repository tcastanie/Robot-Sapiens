package robosapiensBrainServer;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

import smaapp.RobotBrain;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;
import madkit.kernel.Scheduler;
import madkit.simulation.activator.GenericBehaviorActivator;

public class RobotBrainScheduler extends Scheduler{
	
	private GenericBehaviorActivator<AbstractAgent> brainActivator;
	private GenericBehaviorActivator<AbstractAgent> commActivator;
	
	    @Override
	   protected void activate() {
	
			RobotBrainLocals.socket = new ThreadLocal<Socket>();
			RobotBrainLocals.socket.set(RobotBrainGlobals.sockets.get(RobotBrainGlobals.sockets.size()-1));
			System.out.println(RobotBrainLocals.socket.get().getPort());
			RobotBrainLocals.in = new ThreadLocal<DataInputStream>();
			RobotBrainLocals.out = new ThreadLocal<PrintStream>();
			try {
				RobotBrainLocals.in.set(new DataInputStream (RobotBrainLocals.socket.get().getInputStream()));
				RobotBrainLocals.out.set(new PrintStream(RobotBrainLocals.socket.get().getOutputStream()));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	        // 1 : create the simulation group
	    	createGroup(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup);
	    	createGroup(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup);
	    	
	        // 2 : launch some simulated agents
	    	RobotBrain r = new RobotBrain();
	    	launchAgent(r);
	        r.initNsch();
	        
	
	        // 3 : initialize the activator on the correct (1) CGR location and (2) behavior
	        commActivator = new GenericBehaviorActivator<AbstractAgent>(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole, "doComm");
	        addActivator(commActivator);
	        brainActivator = new GenericBehaviorActivator<AbstractAgent>(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.BrainRole, "doStep");
	        addActivator(brainActivator);
	
	        // 4 : we are done, because Scheduler already defines a live method
	        // calling the execution of the activator. We will override it later.
	        // here we just slow down the simulation to not flood the console
	        //setDelay(300);
	        
	        setSimulationState(SimulationState.RUNNING);
	        sendMessage(r.getAgentAddressIn(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.BrainRole),new madkit.message.StringMessage("PlayBrain"));
	    }

}
