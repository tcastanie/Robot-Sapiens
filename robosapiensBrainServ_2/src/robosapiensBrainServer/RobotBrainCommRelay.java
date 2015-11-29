package robosapiensBrainServer;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import madkit.kernel.AbstractAgent;


public class RobotBrainCommRelay extends AbstractAgent{
	
	ArrayList<Double> outVal;
	int inSize,outSize;
	DataInputStream in;
	PrintStream out;
	
	public String doStep() {
		System.out.println("doing comms");
		// ///////////////IN<<<<<<<<<<<<<<<<<<<
		try {
			String line;

			while (!(line = in.readLine()).split(" ")[0].equals("SENSORS"))
				;
			int num = Integer.parseInt(line.split(" ")[1]);
			for (int i = 0; i < num; i++)
			{
				line = in.readLine();
				double valtemp = Double.parseDouble(line.split(" ")[1]);
				System.out.println("sensor "+ i + " = " + valtemp);
				sendMessage(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nnRole, new DoubleMessage(valtemp, "Input " + i));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// ////////////////OUT>>>>>>>>>>>>>>>>>
		String msg = "EFFECTORS ";
		msg += outVal.size() + "\n";

		for (int i = 0; i < outVal.size(); i++) {
			msg += i + " " + outVal.get(i) + "\n";
			// System.out.println(i + " " +
			// effectorNeurons.get(i).getLastValue());
		}
		out.print(msg);
		
		return "doStep";
	}

	public void activate() {
		setName("CommsRelay");
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.ManagementGroup, RobotBrainGlobals.CommRole);
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
