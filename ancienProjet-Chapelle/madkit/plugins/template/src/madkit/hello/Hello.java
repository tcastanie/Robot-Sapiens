package madkit.hello;

import madkit.kernel.*;

public class Hello extends Agent {
	String myCommunity="myCommunity";
	String myGroup="myGroup";
	String myRole="myRole";
	
	boolean alive = true;
	
	public void activate(){
		println("Hello I'm an agent !");
		// create a distributed group
		int r = createGroup(true, myCommunity, myGroup, null, null);
		if (r != 1)
			alive =false;
		else 
			requestRole(myCommunity, myGroup, myRole);
	}
	
	public void live()
	{
		println("Hello world...");
		while(alive){
			Message m = waitNextMessage();
			handleMessage(m);
		}
	}
	
	void handleMessage(Message m){
		// You should describe here the agent's behavior
		// upon reception of a message
	}
	
	public void end()
	{
		println("\t That's it !!! Bye ");
		pause(2000); // just to be abloe to see the last message..
	}
}
