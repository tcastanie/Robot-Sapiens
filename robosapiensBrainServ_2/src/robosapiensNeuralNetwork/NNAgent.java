package robosapiensNeuralNetwork;

import java.util.ArrayList;

import robosapiensBrainServer.RobotBrainGlobals;
import robosapiensBrainServer.neuralNetMessage;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;

public class NNAgent extends AbstractAgent{
	NeuralNetwork neuralNet = new NeuralNetwork();
	ArrayList<Double> inputs = new ArrayList<Double>();
	ArrayList<Double> outputs = new ArrayList<Double>();
	int sensorCount = 0;
	boolean init = false;
	boolean hasComm = false;
	boolean hasMotiv = false;
	boolean backingOut = false;
	int backoutTimer = 0;
	Genome currentGenome;
	
	public NNAgent()
	{
		
	}
	
	public void doStep()
	{
		//System.out.println("doing NN");
		handleMessages();
		if(init && hasComm && hasMotiv&& !backingOut)
		{
			System.out.println("doing init NN");
			neuralNet.ReleaseNet();
			neuralNet.CreateNet(NeuralNetGlobals.nHiddenLayer, sensorCount + 1, NeuralNetGlobals.hiddenLayerSize, outputs.size());
			System.out.println("mine : " + neuralNet.ToGenome().weights.size());
			currentGenome = NeuralNetGlobals.genAlg.GetNextGenome();
			System.out.println("his  : " + currentGenome.weights.size());
			

			neuralNet.FromGenome(currentGenome, sensorCount + 1, NeuralNetGlobals.hiddenLayerSize,NeuralNetGlobals.nHiddenLayer, outputs.size());
			System.out.println("his2 : " + neuralNet.ToGenome().weights.size());
			
			System.out.println(neuralNet.GetTotalOutputs());
			
			init = false;
			hasComm = false;
			hasMotiv = false;
		}
		else if (backingOut)
		{
			if(checkInputFailure() || backoutTimer > 0)
				backOut();
			else
				backingOut = false;
		}
		else if(!init)
		{
			currentGenome.fitness += NeuralNetGlobals.fitnessIncreseStep;
			neuralNet.SetInput(inputs);
			if(checkInputFailure())
			{
				System.out.println("failure detected");
				backingOut = true;
				NeuralNetGlobals.genAlg.SetGenomeFitness(currentGenome.fitness, currentGenome.index);
				currentGenome = NeuralNetGlobals.genAlg.GetNextGenome();
				neuralNet.FromGenome(currentGenome, sensorCount + 1, NeuralNetGlobals.hiddenLayerSize,NeuralNetGlobals.nHiddenLayer, outputs.size());
				reverseOutput();
				backoutTimer = 50;
				return;
			}
			neuralNet.Update();
			for(int i = 0 ; i < outputs.size(); i++)
				outputs.set(i, neuralNet.GetOutput(i));
			sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));
			sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));						
		}			
	}
	
	private void reverseOutput() {
		double val = -outputs.get(0);
		for(int i = 0 ; i < outputs.size(); i++)
			outputs.set(i,val);
	}

	private void backOut() {
		if(checkInputFailure()&&backoutTimer == 0)
		{
			reverseOutput();
			backoutTimer = 50;
		}
			
		sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));
		sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));	
		backoutTimer--;
	}

	private boolean checkInputFailure() {
		for(double d : inputs)
			if(d > NeuralNetGlobals.inputFailureThreshold)
				return true;
		return false;
	}

	private void handleMessages() {

		for(Message m : nextMessages(null))
		{
			if(m.getClass() == neuralNetMessage.class)
			{
				neuralNetMessage nnM = (neuralNetMessage)m;
				if(nnM.name.contains(NeuralNetGlobals.messInput))
				{
					if(nnM.name.contains(NeuralNetGlobals.sensors))
					{
						for(int i = 0 ; i < nnM.val.size();i++)
							inputs.set(i, nnM.val.get(i));
					}
					else
					{
						inputs.set(sensorCount, nnM.val.get(0));
					}
					
				}
				else if(nnM.name.contains(NeuralNetGlobals.messInit))
				{
					System.out.println("got init msg : "+nnM.name);
					init = true;
					if(nnM.name.contains(NeuralNetGlobals.sensors))
					{
						sensorCount = nnM.val.get(0).intValue();
						for(int i = 0 ; i < nnM.val.get(0).intValue();i++)
							inputs.add(0,0.0);
						for(int i = 0 ; i < nnM.val.get(1).intValue();i++)
							outputs.add(0,0.0);
						System.out.println(outputs.size());
						hasComm = true;
					}
					else
					{
						hasMotiv = true;
						inputs.add(0.0);
					}
				}
			}
		}
	}

	public void activate()
	{
		System.out.println("NNAgent activated");
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole);
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.nnRole);
		
	}
}
