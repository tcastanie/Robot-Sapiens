package robosapiensBrainServer;

import java.net.Socket;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;

import robosapiensNeuralNetwork.NNAgent;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;
import madkit.kernel.Scheduler;
import madkit.simulation.activator.GenericBehaviorActivator;

public class RobotBrainScheduler extends Scheduler{

	private GenericBehaviorActivator<AbstractAgent> neuralNetActivator;
	private GenericBehaviorActivator<AbstractAgent> commActivator;
	private GenericBehaviorActivator<AbstractAgent> motivatorActivator;
	
	RobotBrainCommRelay commsAgent;
	NNAgent	NNetAgent;
	externalExplorationMotivator explorer;
	/*
	MotivationalAgent motivator;
	basicExplorationMotivator explMotivator;
	*/
	    @Override
	   protected void activate() {
	
			RobotBrainLocals.socket = new ThreadLocal<Socket>();
			RobotBrainLocals.socket.set(RobotBrainGlobals.sockets.get(RobotBrainGlobals.sockets.size()-1));
			//System.out.println(RobotBrainLocals.socket.get().getPort());
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
	    	commsAgent = new RobotBrainCommRelay();
	    	commsAgent.setInOut(RobotBrainLocals.in.get(),RobotBrainLocals.out.get());
	    	NNetAgent = new NNAgent();
	    	explorer = new externalExplorationMotivator();
	    	/*
	    	motivator = new MotivationalAgent();
	    	motivator.id = 0;
	    	explMotivator = new basicExplorationMotivator();
	    	explMotivator.id = 1;
	    	*/
	    	System.out.println("launching brain agents");
	    	
	    	launchAgent(NNetAgent);
	    	launchAgent(commsAgent);
	    	launchAgent(explorer);
	    	
/*	    	launchAgent(motivator);
	    	launchAgent(explMotivator);*/
	    	
	    	System.out.println("starting activators");
	        // 3 : initialize the activator on the correct (1) CGR location and (2) behavior
	        commActivator = new GenericBehaviorActivator<AbstractAgent>(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole, "doStep");
	        addActivator(commActivator);
	        neuralNetActivator = new GenericBehaviorActivator<AbstractAgent>(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole, "doStep");
	        addActivator(neuralNetActivator);
	        motivatorActivator = new GenericBehaviorActivator<AbstractAgent>(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole, "doStep");
	        addActivator(motivatorActivator);
	        
/*	        neuronActivator = new GenericBehaviorActivator<AbstractAgent>(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.NeuronRole, "runMe");
	        addActivator(neuronActivator);
*/

	        // 4 : we are done, because Scheduler already defines a live method
	        // calling the execution of the activator. We will override it later.
	        // here we just slow down the simulation to not flood the console
	        //setDelay(300);
	        
	        System.out.println("start sim");
	        setSimulationState(SimulationState.RUNNING);
	       
	    }

}
