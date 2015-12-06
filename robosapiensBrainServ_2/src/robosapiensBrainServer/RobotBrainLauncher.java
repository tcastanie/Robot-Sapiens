package robosapiensBrainServer;

import java.net.Socket;

import madkit.kernel.Madkit;

public class RobotBrainLauncher implements Runnable{
	Socket socket;
	
	public RobotBrainLauncher(Socket clientSocket) {
		RobotBrainGlobals.sockets.add(clientSocket);
	}

	@Override
	public void run() {

		// r.initNsch();
		String[] args2 = { "--launchAgents", "robosapiensBrainServer.RobotBrainScheduler,false" }; // This
																			// agent
																			// with
																			// GUI
		Madkit m = new Madkit(args2);
		
		// sendMessage(sch.getAddress(), new StringMessage("Play"));
	}

}
