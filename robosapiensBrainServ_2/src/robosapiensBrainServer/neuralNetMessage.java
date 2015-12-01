package robosapiensBrainServer;

import java.util.ArrayList;

import madkit.kernel.Message;

public class neuralNetMessage extends Message{

	public ArrayList<Double> val;
	public String name;
	
	public neuralNetMessage(ArrayList<Double> valin, String namein)
	{
		val = valin;
		name = namein;
	}
	
}
