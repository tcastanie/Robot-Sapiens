package robosapiensBrainServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import robosapiensNeuralNetwork.NeuralNetGlobals;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;


public class RobotBrainCommRelay extends AbstractAgent{
	
	ArrayList<Double> outVal;
	int inSize,outSize;
	DataInputStream in;
	PrintStream out;
	neuralNetMessage msgInit = null;
	
	public String doStep() {
		System.out.println("doing comms");
		if(msgInit != null)
		{
			System.out.println(msgInit.name);
        	System.out.println(sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole, msgInit));			
        	msgInit = null;
		}
		else
		{
		// ///////////////IN<<<<<<<<<<<<<<<<<<<
		try {
			String line;
			ArrayList<Double> msgList = new ArrayList<Double>();
			
			while (!(line = in.readLine()).split(" ")[0].equals("SENSORS"))
				;
			int num = Integer.parseInt(line.split(" ")[1]);
			for (int i = 0; i < num; i++)
			{
				line = in.readLine();
				double valtemp = Double.parseDouble(line.split(" ")[1]);
				//System.out.println("sensor "+ i + " = " + valtemp);
				msgList.add(1.0 - valtemp);
			}
			sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole, new neuralNetMessage(msgList, NeuralNetGlobals.messInput + " " +NeuralNetGlobals.sensors));						
	        } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ////////////////OUT>>>>>>>>>>>>>>>>>
		handleMessages();
		String msg = "EFFECTORS ";
		msg += outVal.size() + "\n";

		for (int i = 0; i < outVal.size(); i++) {
			msg += i + " " + outVal.get(i) + "\n";
			// System.out.println(i + " " +
			// effectorNeurons.get(i).getLastValue());
		}
		//System.out.println(outVal);
		out.print(msg);
		}
		return "doStep";
	}
	
	private void handleMessages() {
		Message m;

		while((m = nextMessage())!=null)
		{
			if(m.getClass() == neuralNetMessage.class)
			{
				neuralNetMessage nnM = (neuralNetMessage)m;
				if(nnM.name.contains(NeuralNetGlobals.messOutput))
				{
						for(int i = 0 ; i < nnM.val.size();i++)
							outVal.set(i, (2.0*(nnM.val.get(i))-1.0));
				}
			}
		}
	}
	
	public void activate() {
		//setName("CommsRelay");
		System.out.println("comm agent active");
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole);
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.CommRole);
			 try {
			        /* string ret = "REGISTERING " + name + "\n";
			        ret += "SENSORS " + sensors.Length + "\n";
			        for (int i = 0; i < sensors.Length; i++)
			            ret += i + " FLOAT\n";
			        ret += "EFFECTORS " + Wheels.Length + "\n";
			        for (int i = 0; i < Wheels.Length; i++)
			            ret += i + " FLOAT\n";
			        */

			       
			        String line;
			   
			        line = in.readLine();
			        System.out.println("reading registering");
			        if(line.split(" ")[0].equals("REGISTERING"))
			        {
				        this.setName(line.split(" ")[1]);
				        line = in.readLine();
				        inSize = Integer.parseInt(line.split(" ")[1]);
			        	for(int i = 0 ; i < inSize ; i++)
			        	{
			        		line = in.readLine();
			        	}
				   
				        line = in.readLine();
				        outSize = Integer.parseInt(line.split(" ")[1]);
			        	outVal = new ArrayList<Double>();
			        	for(int i = 0 ; i < outSize ; i++)
			        	{
			        		line = in.readLine();
			        		outVal.add(0.5);
			        	}
			        	System.out.println(inSize + " Sensor found");
			        	System.out.println(outVal.size() + " Effector found");
			        	out.println("REGISTERED : "+this.getName());
			        	ArrayList<Double> msg = new ArrayList<Double>(2);
			        	msg.add(0,(double)inSize);msg.add(1,(double)outSize);		
			        	msgInit = new neuralNetMessage(msg, NeuralNetGlobals.messInit + " " +NeuralNetGlobals.sensors);
			        }
			        
		}catch(IOException e){
			System.out.println(e.getMessage());
		}	
	}

	public void setInOut(DataInputStream dataInputStream,
			PrintStream printStream) {
		in = dataInputStream;
		out = printStream;
	}
	
}
