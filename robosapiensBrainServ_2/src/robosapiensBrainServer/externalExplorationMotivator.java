package robosapiensBrainServer;

import java.util.ArrayList;

import madkit.kernel.Message;
import robosapiensNeuralNetwork.NeuralNetGlobals;

public class externalExplorationMotivator extends abstractExternalMotivator{
	double rewardBig = 3.0;
	double rewardSmall = NeuralNetGlobals.fitnessIncreseStep;
	int failureTime = 5000;
	int counter = 0;
	
	public String doStep() {
			String outMsg = "RUN";

			Message m;
			while ((m = nextMessage()) != null) 
			{
				if (m.getClass() == externalMotivatorMessage.class) 
				{
					externalMotivatorMessage nnm = (externalMotivatorMessage) m;
					if (nnm.name.contains(NeuralNetGlobals.messInput) && nnm.name.contains(NeuralNetGlobals.motivator)) 
					{
						for(String line : nnm.val)
						{
							if(Integer.parseInt(line.split(" ")[0]) == id)
							{
								switch(line.split(" ")[1].split("_")[1])
								{
									case "1":
									{
										sendRewardMsg(rewardSmall);
										break;
									}
									case "2":
									{
										sendRewardMsg(rewardBig);
										counter = 0;
										break;
									}
									default:
									{
										break;
									}
								}
							}
						}
					}
				}
				else if(m.getClass() == neuralNetMessage.class)
				{
					neuralNetMessage nnm = (neuralNetMessage) m;
					if (nnm.name.contains(NeuralNetGlobals.messReInit)) 
					{
						counter = 0;
						outMsg = "REINIT";
					}
				}
			}
			if (counter > failureTime)
			{
				System.out.println("EXPLORER FAILURE");
				sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole,
						new neuralNetMessage(null, NeuralNetGlobals.messFailure));
				counter = 0;
			}
		counter++;
		sendCtrlMsg(outMsg);
		return "doStep";
	}
	private void sendRewardMsg(double val) {
		ArrayList<Double> out = new ArrayList<Double>();
		out.add(val);
		sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole,
				new neuralNetMessage(out, NeuralNetGlobals.messReward));		
	}

	
	private void sendCtrlMsg(String msg) {
		ArrayList<String> out = new ArrayList<String>();
		out.add(msg);
		sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.CommRole,
				new externalMotivatorMessage(out, NeuralNetGlobals.messInput));		
	}

	public void activate() {
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole);
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.externalMotivatorRole);
	}
	
}
