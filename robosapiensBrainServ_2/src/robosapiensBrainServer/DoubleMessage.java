package robosapiensBrainServer;

import madkit.kernel.Message;

public class DoubleMessage extends Message{

	double val;
	String name;
	
	public DoubleMessage(double valin, String namein)
	{
		val = valin;
		name = namein;
	}
	
}
