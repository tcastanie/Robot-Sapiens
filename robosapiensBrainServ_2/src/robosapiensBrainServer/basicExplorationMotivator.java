package robosapiensBrainServer;

import java.util.ArrayList;

import robosapiensNeuralNetwork.NeuralNetGlobals;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;

public class basicExplorationMotivator extends AbstractAgent{
	private double step = 0.005;
	private double threshold = 0.1;
	private double val = 0.0;
	private ArrayList<Double> outvals;
	neuralNetMessage msgInit = null;
	public int id;
	
	public String doStep()
	{
		if(msgInit != null)
		{
			sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole, msgInit);						
			msgInit = null;
		}
		else
		{
		//System.out.println("doing Motivator");
		Message m = nextMessage();
		if(m != null)
		{
		if(m.getClass() == neuralNetMessage.class)
		{
			neuralNetMessage nnm = (neuralNetMessage)m;
			if(nnm.name.contains(NeuralNetGlobals.messOutput))
			{
				outvals = nnm.val;
				if(Math.abs(outvals.get(0) - outvals.get(1) )> threshold)
					val += step;
				else
					val -= (3*step);

				val = Math.min(val, 1.0);
				val = Math.max(val, 0.0);
				ArrayList<Double> outMsg = new ArrayList<>();
				outMsg.add(val);		
				outMsg.add((double)id);		
				//System.out.println("expl mot : " + val);
				sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole, new neuralNetMessage(outMsg, NeuralNetGlobals.messInput));						
			}	
			else if(nnm.name.contains(NeuralNetGlobals.messReInit))
			{
				val = 0;
			}
		}
		}
		}
		return "doStep";
	}

	public void activate()
	{
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole);
		msgInit = new neuralNetMessage(null, NeuralNetGlobals.messInit);
	}
}
