package robosapiensBrainServer;

import java.util.ArrayList;

import robosapiensNeuralNetwork.NeuralNetGlobals;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;

public class MotivationalAgent extends AbstractAgent{
	private double step = 0.0001;
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
			Message m;
			while((m = nextMessage()) != null)
			{
		if(m.getClass() == neuralNetMessage.class)
		{
			neuralNetMessage nnm = (neuralNetMessage)m;
			if(nnm.name.contains(NeuralNetGlobals.messOutput))
			{
				outvals = nnm.val;
				if((outvals.get(0) <= 0.55 && outvals.get(0) >= 0.45)|| (outvals.get(1) <= 0.55 && outvals.get(1) >= 0.45))
					val += step;
				else
					val -= step;

				val = Math.min(val, 1.0);
				val = Math.max(val, 0.0);
				sendMsg();		
			}else if(nnm.name.contains(NeuralNetGlobals.messReInit))
			{
				System.out.println("reinit motivator : " + val);
				val = 0.0;
				sendMsg();
			}			
		}
		}
		}
		return "doStep";
	}
	
	private void sendMsg() {
		ArrayList<Double> outMsg = new ArrayList<>();
		outMsg.add(val);
		outMsg.add((double) id);
		// System.out.println("expl mot : " + val + "\tfor " +
		// Math.abs(outvals.get(0) - outvals.get(1) ));
		sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole,
				new neuralNetMessage(outMsg, NeuralNetGlobals.messInput));
		
	}
	public void activate()
	{
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole);
		msgInit = new neuralNetMessage(null, NeuralNetGlobals.messInit);
	}
}
