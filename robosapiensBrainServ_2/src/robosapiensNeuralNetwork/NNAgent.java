package robosapiensNeuralNetwork;

import robosapiensBrainServer.RobotBrainGlobals;
import madkit.kernel.AbstractAgent;

public class NNAgent extends AbstractAgent{

	NeuralNetwork neuralNet;
	
	public NNAgent()
	{
		
	}
	
	public void doStep()
	{
		
	}
	
	public void Activate()
	{
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole);
	}
}
