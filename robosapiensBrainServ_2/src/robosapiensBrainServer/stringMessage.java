package robosapiensBrainServer;

import madkit.kernel.Message;

public class stringMessage extends Message{
	public stringMessage(String namein, String valin) {
		name = namein;
		val = valin;
	}
	public String name;
	public String val;	
}
