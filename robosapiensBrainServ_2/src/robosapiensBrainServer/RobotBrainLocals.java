package robosapiensBrainServer;

import java.io.DataInputStream;
import java.io.PrintStream;
import java.net.Socket;

public class RobotBrainLocals {
	public static ThreadLocal<Socket> socket;
	public static ThreadLocal<DataInputStream> in;
	public static ThreadLocal<PrintStream> out;

}
