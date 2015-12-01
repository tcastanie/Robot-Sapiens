package robosapiensBrainServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import robosapiensNeuralNetwork.GenomeManager;
import madkit.kernel.Madkit;

public class robosapiensBrainServer {
	static boolean stop = false;

	public static void main(String[] args) {
		System.out.println("hello world!");
		System.out.println("listening on : " + RobotServerGlobals.port);
		
		new Thread(new GenomeManager()).start();
		
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(RobotServerGlobals.port);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port", e);
		}

		while (!stop) {
			Socket clientSocket = null;
			try {
				clientSocket = serverSocket.accept();
				System.out.println("new client");
			} catch (IOException e) {
				if (stop) {
					System.out.println("Server Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection",
						e);
			}
			new Thread(new RobotBrainLauncher(clientSocket))
					.start();
		}
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
