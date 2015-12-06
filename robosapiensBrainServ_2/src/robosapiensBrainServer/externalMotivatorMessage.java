package robosapiensBrainServer;

import java.util.ArrayList;

import madkit.kernel.Message;

public class externalMotivatorMessage extends Message{

	public ArrayList<String> val;
	public String name;
	
	public externalMotivatorMessage(ArrayList<String> valin, String namein)
	{
		val = valin;
		name = namein;
	}
	

}
