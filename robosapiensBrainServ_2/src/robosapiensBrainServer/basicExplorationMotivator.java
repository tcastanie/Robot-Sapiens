package robosapiensBrainServer;

import java.util.ArrayList;

import robosapiensNeuralNetwork.NeuralNetGlobals;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;

public class basicExplorationMotivator extends AbstractAgent {
	private double step = 0.05;
	private double threshold = 0.05;
	private double val = 0.0;
	private double tempval = 0.0;
	private ArrayList<Double> outvals;
	private ArrayList<Double> outvalsCumulated = new ArrayList<Double>(2);
	neuralNetMessage msgInit = null;
	public int id;
	boolean skip;
	int timer = 0;
	int timeLimit = 400; 
	double leftRight = 0.0;

	public basicExplorationMotivator()
	{
		outvalsCumulated.add(0.0);
		outvalsCumulated.add(0.0);
	}
	
	public String doStep() {
		skip = false;
		if (msgInit != null) {
			sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole, msgInit);
			msgInit = null;
		} else {
			// System.out.println("doing Motivator");
			Message m;
			while ((m = nextMessage()) != null) 
			{
				if (m.getClass() == neuralNetMessage.class) 
				{
					neuralNetMessage nnm = (neuralNetMessage) m;
					if (nnm.name.contains(NeuralNetGlobals.messOutput)&& !skip) 
					{
						skip = true;
						outvals = nnm.val;
						if(timer == timeLimit)
						{
							leftRight = Math.abs(leftRight/(double)timeLimit);
							System.out.println(leftRight);
							
							outvalsCumulated.set(0, outvalsCumulated.get(0)/(double)timeLimit);
							outvalsCumulated.set(1, outvalsCumulated.get(1)/(double)timeLimit);
							if (Math.abs(outvalsCumulated.get(0) - outvalsCumulated.get(1)) > threshold || leftRight > 0.3)
							{
								//System.out.println("negative exploring");
								val += step * Math.abs(outvalsCumulated.get(0) - outvalsCumulated.get(1))*(4.0)+(step*4.0*leftRight);							
							}
							else
							{
								//System.out.println("positive exploring");
								val -= (step) * (1.0 - Math.abs(outvalsCumulated.get(0) - outvalsCumulated.get(1)))+(step*(1.0-leftRight));
							}
							val = Math.min(val, 1.0);
							val = Math.max(val, 0.0);
							timer = 0;
							System.out.println("basic explore val : " + val);
							System.out.println(outvalsCumulated);
							outvalsCumulated.set(0, 0.0);
							outvalsCumulated.set(1, 0.0);
							leftRight = 0.0;
						}
						else
						{
							for(int i = 0 ; i < outvals.size(); i++)
								outvalsCumulated.set(i, outvalsCumulated.get(i)+outvals.get(i));
							if(outvals.get(0)>outvals.get(1)+0.01)
								leftRight+=1.0;
							else if (outvals.get(1)>outvals.get(0)+0.01)
								leftRight-=1.0;
							
							timer++;
						}
						


						sendMsg();
						
					} else if (nnm.name.contains(NeuralNetGlobals.messReInit)) {
						skip = true;
						System.out.println("reinit basic explorer : " + val);
						val = 0.0;
						//System.out.println("reinit basic explorer : " + val);
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

	public void activate() {
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole);
		msgInit = new neuralNetMessage(null, NeuralNetGlobals.messInit);
	}
}
