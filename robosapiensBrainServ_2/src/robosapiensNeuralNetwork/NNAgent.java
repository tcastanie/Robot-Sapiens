package robosapiensNeuralNetwork;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import robosapiensBrainServer.RobotBrainGlobals;
import robosapiensBrainServer.RobotServerGlobals;
import robosapiensBrainServer.neuralNetMessage;
import robosapiensBrainServer.stringMessage;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;

public class NNAgent extends AbstractAgent{
	NeuralNetwork neuralNet = new NeuralNetwork();
	ArrayList<Double> inputs = new ArrayList<Double>();
	ArrayList<Double> outputs = new ArrayList<Double>();
	int sensorCount = 0;
	int motivatorCount = 0;
	boolean init = false;
	boolean hasComm = false;
	boolean hasMotiv = false;
	boolean backingOut = false;
	int backoutTimer = 0;
	String botName = "newbie";
	Genome currentGenome;
	double bestFitness = 0.0;
	
	public NNAgent()
	{
		
	}
	
	public void doStep()
	{
		//System.out.println("doing NN");
		handleMessages();
		if(init && hasComm && !backingOut)
		{
			System.out.println("doing init NN");
			neuralNet.ReleaseNet();
			
			neuralNet.CreateNet(NeuralNetGlobals.nHiddenLayer, sensorCount + motivatorCount, NeuralNetGlobals.hiddenLayerSize, outputs.size());
			System.out.println("mine : " + neuralNet.ToGenome().weights.size());
			currentGenome = initGenome(botName);
			System.out.println("his  : " + currentGenome.weights.size());
			currentGenome.fitness = 0.0;

			neuralNet.FromGenome(currentGenome, sensorCount + motivatorCount, NeuralNetGlobals.hiddenLayerSize,NeuralNetGlobals.nHiddenLayer, outputs.size());
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
			//System.out.println(inputs);
			if(checkInputFailure())
			{
				doNextGenome();
				return;
			}
			neuralNet.Update();
			for(int i = 0 ; i < outputs.size(); i++)
				outputs.set(i, (2.0*neuralNet.GetOutput(i))-1.0);
			sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));
			broadcastMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));						
		}			
	}
	
	private void doNextGenome() {
		System.out.println(inputs);
		backingOut = true;
		System.out.println("failure detected , fitness : " + currentGenome.fitness);
		//NeuralNetGlobals.genAlg.SetGenomeFitness(currentGenome.fitness, currentGenome.index);
		if(currentGenome.fitness>bestFitness)
		{
			bestFitness = currentGenome.fitness;
			currentGenome.name = botName;
			if(!botName.equals("noSave"))
				currentGenome.save(RobotServerGlobals.botSavePath+"/"+botName+"/"+botName+RobotServerGlobals.genomeFileExtension);
		}				
		/// changing genomes
		currentGenome = NeuralNetGlobals.genAlg.GetNextGenome(currentGenome);
		if(currentGenome == null)
		{
			System.out.println("ERROR : GENOME NULL");
			System.exit(0);
		}
		neuralNet.FromGenome(currentGenome, sensorCount + motivatorCount, NeuralNetGlobals.hiddenLayerSize,NeuralNetGlobals.nHiddenLayer, outputs.size());
		System.out.println("output size : "+ outputs.size());
		outputs.set(0, outputs.get(0) > 0.0 ? -1.0 : 1.0);
		outputs.set(1, outputs.get(0));
		backoutTimer = 500;
		broadcastMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole, new neuralNetMessage(null, NeuralNetGlobals.messReInit));							
		return;
	}

	private Genome initGenome(String nem) {

		if(nem.equals("noSave"))
			return NeuralNetGlobals.genAlg.GetNextGenome(null);
		else
		{
			Genome gen = new Genome();
			File f  = new File(RobotServerGlobals.botSavePath+"/"+nem+"/"+nem+RobotServerGlobals.genomeFileExtension);
			if(f.exists() && f.length()> 0&&gen.load(RobotServerGlobals.botSavePath+"/"+nem+"/"+nem+RobotServerGlobals.genomeFileExtension))
				return gen;
			else
			{
				f = new File(RobotServerGlobals.botSavePath+"/"+nem);
				f.mkdirs();
				f = new File(RobotServerGlobals.botSavePath+"/"+nem+"/"+nem+RobotServerGlobals.genomeFileExtension);
				
				try {
					f.createNewFile();
				} catch (IOException e) {e.printStackTrace();}
				return NeuralNetGlobals.genAlg.GetNextGenome(null);
			}
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
			backoutTimer = 330;
			//System.out.println("backout switch");
		}
		if(backoutTimer > 320)	
		{
			ArrayList<Double> outputsHalt = new ArrayList<Double>();
			for(int i = 0 ;i < outputs.size(); i++)
				outputsHalt.add(0.0);
			sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole, new neuralNetMessage(outputsHalt, NeuralNetGlobals.messOutput));
			//broadcastMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));	
		}
		else if(backoutTimer > 300)	
		{
			ArrayList<Double> outputsHalt = new ArrayList<Double>();
			outputsHalt.add(0.0);
			outputsHalt.add(0.7);
			sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole, new neuralNetMessage(outputsHalt, NeuralNetGlobals.messOutput));
			//broadcastMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));	
		}
		else
		{
			sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));
			//broadcastMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.motivatorRole, new neuralNetMessage(outputs, NeuralNetGlobals.messOutput));				
		}
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
						//System.out.println("ROUNDING " + nnM.val.get(1) + " to " + (int)Math.round(nnM.val.get(1)));
						inputs.set(sensorCount + (int)Math.round(nnM.val.get(1)), nnM.val.get(0));
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
						motivatorCount++;
						inputs.add(0.0);
					}
				}
				else if(nnM.name.contains(NeuralNetGlobals.messReward))
				{
					if(currentGenome!=null)
						currentGenome.fitness += nnM.val.get(0);
					else
						System.out.println("reward ignored : "+nnM.val.get(0));
				}else if(nnM.name.contains(NeuralNetGlobals.messFailure))
				{
					doNextGenome();
				}
			}
			else if(m.getClass() == stringMessage.class)
			{
				stringMessage nnM = (stringMessage)m;
				if(nnM.name.contains(NeuralNetGlobals.messInit))
				{
					botName = nnM.val;
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
