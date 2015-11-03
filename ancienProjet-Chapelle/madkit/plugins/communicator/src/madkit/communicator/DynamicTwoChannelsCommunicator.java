/*
* DynamicTwoChannelsCommunicator.java - Communicator: the connection module of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Pierre Bommel, Fabien Michel,
*               Thomas Cahuzac, Nicolas Bernard
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/

package madkit.communicator;

import madkit.kernel.*;
import java.util.*;
import java.net.*;

final public class DynamicTwoChannelsCommunicator extends Agent
{
	static int DEFAULT_PORT = 4444;

	TransportLayer comm;
	DynamicCommBean agentGUI;

	AgentAddress mykernel;
	// do not change this line: the socketInfo has to be instanciated at class definition time
	SocketKernel socketInfo = new SocketKernel(DEFAULT_PORT);

	Map kernels = new HashMap();
	Hashtable clientTable;

	boolean alive = true;

	// Variables relatives à la gestion des segments de messages.
	short messagesSegmentsTableSize = 400;
	int garbagePeriod = 10000;
	int timeoutBetweenSegments = 50000;


	/* ------------ */
	/* Constructors */
	/* ------------ */


	public DynamicTwoChannelsCommunicator()
	{
		clientTable=new Hashtable();
	}
	
	public DynamicTwoChannelsCommunicator(int port)
	{	
		socketInfo.setPort(port);
		clientTable=new Hashtable();
	}


	/* ---------------------- */
	/* Accesseurs - Modifiers */
	/* ---------------------- */


	String getId()
	{
		return getAddress().getKernel().getID();
	}

	int  getPort() { return socketInfo.getPort(); }
	void setPort(int p) { socketInfo.setPort(p); }

	short getMessagesSegmentsTableSize() {return messagesSegmentsTableSize;}
	int getGarbagePeriod() {return garbagePeriod;}
	int getTimeoutBetweenSegments() {return timeoutBetweenSegments;}
	boolean isAlive() {return alive;}


	/* ---------------------- */
	/* Relatif au thread	  */
	/* ---------------------- */


	// Initialisation de l'interface graphique.
	public void initGUI()
	{
		setGUIObject (agentGUI = new DynamicCommBean(this));
	}


	// Lancement du communicator (interface + classe utilitaire).
	public void activate()
	{
		println("Communicator Activated");

		comm = new TransportLayer(this, clientTable);
		if(hasGUI()) agentGUI.setActive(comm.getServerActive());
		
		requestRole("public","system","communicator",null);

		mykernel = getAgentWithRole("communications","site");
		sendMessage(mykernel, new NetworkRequest(NetworkRequest.BE_COMMUNICATOR));
	}


	// Comportement : attente et traitement des messages.
	public void live()
	{
		while(alive)
		{
			Message e = waitNextMessage();
		    exitImmediatlyOnKill();
			if (! alive) return;
			if (e instanceof KernelMessage)
		    	handleMessage((KernelMessage)e);
			else if (e instanceof StringMessage)
				handlePersonalMessage((StringMessage) e);
		}
	}


    // Fermeture "propre" des composants du communicator.
    /** Dernière méthode appelé par le Kernel lorsqu'on tue un agent (killAgent()),
        ou qu'un agent threadé (Agent, pas les AbstractAgents !) sort de son thread
    (live()). Pour mourir dans une agonie... explicite ! */
  // Fermeture "propre" des composants du communicator.
  	public void end()
  	{
		sendMessage(mykernel, new NetworkRequest(NetworkRequest.STOP_COMMUNICATOR));
  		alive = false;

  		for(Iterator i = clientTable.values().iterator();i.hasNext();)
  		{
  			comm.disconnect((TwoChannelsSocketExtremity) i.next());
		}

  		comm.close();
  		println("Agent SocketCommunicator killed. server is closed");
    	System.gc();
  	}

/*  protected synchronized void endMessages()
  {
  	for(Iterator i = clientTable.values().iterator();i.hasNext();)
  	{
		sendMessage(mykernel, new NetworkRequest(NetworkRequest.STOP_COMMUNICATOR));
  		alive = false;

  		for(Iterator i = clientTable.values().iterator();i.hasNext();)
  		{
  			comm.disconnect((TwoChannelsSocketExtremity) i.next());
		}

  		comm.close();
  		println("Agent SocketCommunicator killed. server is closed");
    	System.gc();
  	}
  	*/


  	/* ---------------------- */
	/* Gestion des pannes	  */
	/* ---------------------- */


	// Après déconnection accidentelle, cette fonction est appelée par
	// le SocketExtremity pour mettre les tables du communicator à jour.
	synchronized void disconnectFrom(String kernelId)
  	{
  		//System.out.println("Deconnection de "+kernelId);
  		for(Iterator i = kernels.keySet().iterator();i.hasNext();)
  		{
  			String kernelAdd = (String) i.next();
  			//System.out.println("kernelAdd:"+kernelAdd);
  			KernelAddress ka = null;
  			try
  			{
  				ka = new KernelAddress(kernelAdd);
  				//System.out.println("ka = "+ka.getID());
  			}
  			catch(Exception e)
  			{
  				println("Bad  KernelAddress in Communicator :"+e);
  			}
  			if(ka.getID().equals(kernelId))
  			{
  				SocketKernel distantSocket = (SocketKernel) kernels.get(ka.toString());
  				i.remove();
  				sendMessage(mykernel, new NetworkRequest(NetworkRequest.DECONNECTED_FROM, kernelId));
  				if(hasGUI()) {
  					// System.out.println("Removing remote kernel infos");
  					agentGUI.removeText(distantSocket.getHost()+":"+distantSocket.getPort());
				}
  				break;
  			}
  		}
  	}



	/* -------------------------- */
	/* nouveaux kernels distants  */
	/* -------------------------- */
  	/** addHost add a host to the list of hosts.. The host may either be given as a a name address (e.g. bibo.foo.fr) 
  	 * or as an IP number (e.g. 127.210.19.123)
  	 * @param hostname
  	 * @param port
  	 */

	// Ajoute à la table un kernel distant et s'y connecte.
	// Utilisé manuellement à partir de l'interface graphique.
	// ou par programme à partir d'un StringMessage de type add-host
	
 	synchronized void addHost(String hostname, int port)
  	{
    	println("Trying to contact "+hostname+":"+port);
    	try{
      		for (Iterator i = kernels.keySet().iterator(); i.hasNext();){
        		String kernelAdd = (String) i.next();
        		//System.out.println("trying to connect to:"+kernelAdd);
				KernelAddress ka = new KernelAddress(kernelAdd);
				//System.out.println("Kernel address ID: "+ka.getID());
				//System.out.println("Kernel address: "+ka.toString());
				
				SocketKernel distantSI = (SocketKernel) kernels.get(kernelAdd);
				if ((ka.getHost().equals(hostname)|| hostname.equals("")) && distantSI.getPort()==port)
					return;
				if (mykernel.getKernel().equals(ka))
					return;
			}
      	}
    	catch (Exception e){
    		println("Error: "+e);
    		return;
    	}

    	InetAddress ia=null;
		try {
			ia=InetAddress.getByName(hostname);
	    }
		catch (UnknownHostException e){
			println("Host "+hostname+" unknown.");
			return;
		}

		if(hasGUI()) agentGUI.setStatus("Req.");
		comm.connect(new SocketKernel(ia.getHostAddress(), port), buildSynch('c'));
 	}


	// Ajoute un kernel distant à la table des kernels.
	private synchronized boolean addKernel(KernelAddress ka, SocketKernel ska)
	{
		if (socketInfo.equals(ska)) return false;
		if(! kernels.containsKey(ka.toString()) && ska != null)
		{
			kernels.put(ka.toString(),ska);
			debug("@@@@@@@@@@@ add a new kernel @@@@@@@@ = "+ska+"KernelAddress= "+ka);
			if(hasGUI()) agentGUI.setText(ska.getHost()+":"+ska.getPort());
			println("connected with "+ka);
			return true;
		}
		return false;
	}


	/* ---------------------- */
	/* Gestion de messagerie  */
	/* ---------------------- */


	// Envoi un message reçu au kernel local.
	synchronized void injectMessage(Message m)
    {
		sendMessage(mykernel,new NetworkRequest(NetworkRequest.INJECT_MESSAGE, m));
    }


    // Traite un message provenant du kernel local.
    synchronized private void handleMessage(Message m)
    {
        Message orig=null;
      
    	if (m instanceof KernelMessage){
          KernelMessage temp = (KernelMessage) m;
          orig = (Message) (temp.getArgument());
	    } else  
        	orig = m;
        comm.sendto(orig, orig.getReceiver().getKernel());
        debug("sending message "+ m);
    }

    private void handlePersonalMessage(StringMessage m){
    	debug("Communicator: personal message: "+m);
    	StringTokenizer st = new StringTokenizer(m.getString()," :");
    	String performative = "nop";
    	String host=null;
    	int port=DEFAULT_PORT;
    	String portname=null;
    	if (st.hasMoreTokens()){
    		performative = st.nextToken();
    	}

    	if (performative.equals("add-host")){
    		if (st.hasMoreTokens()){
    			host = st.nextToken();
    			if (st.hasMoreTokens()){
    				portname = st.nextToken();
					try {
						port = Integer.parseInt(portname);
    				}catch (NumberFormatException ex){
    					System.err.println("Communicator error: not a valid port number: "+port);
    				}
    			}
    			this.addHost(host,port);
    		}
    		else {
    			System.err.println("Communicator error: not a valid host to add: "+host);
    		}
    	} else if (performative.equals("get-port")){
    		sendMessage(m.getSender(),new StringMessage("reply port="+this.getPort()));
    	}
    }

    /* ------------------------------ */
	/* Gestion de la synchronisation  */
	/* ------------------------------ */


	// Traitement des messages de synchronisation reçus.
	synchronized void transmitInfo(Vector args)
    {
		Enumeration a = args.elements();
		String trafficOperation = (String) a.nextElement();

		if (trafficOperation.equals("ACK_SOCKET_INFO"))
		{
			debug("  ---------SocketCom--------- ACK_SOCKET_INFO ---- ");
			KernelAddress distantK = (KernelAddress)a.nextElement();
			SocketKernel distantSI = (SocketKernel)a.nextElement();
			AgentAddress distantKernel = (AgentAddress)a.nextElement();
			Collection otherKernels = (Collection)a.nextElement();
			if (addKernel(distantK,distantSI))
			{
				if(hasGUI()) agentGUI.setStatus("ok");
				for(Iterator i = otherKernels.iterator();i.hasNext();)
				{
					SocketKernel distantSocket = (SocketKernel) i.next();
					if(! ((socketInfo.equals(distantSocket) || kernels.containsValue(distantSocket))))
						comm.connect(distantSocket, buildSynch('c'));
				}
			}
		}

		if (trafficOperation.equals("TRANSMIT_SOCKET_INFO"))
	    {
			debug("  ---------SocketCom------- TRANSMIT_SOCKET_INFO ---- ");
			KernelAddress distantK = (KernelAddress)a.nextElement();
			SocketKernel distantSI = (SocketKernel)a.nextElement();
			AgentAddress distantKernel = (AgentAddress)a.nextElement();
			Collection otherKernels = (Collection)a.nextElement();
			if (addKernel(distantK,distantSI))
            {
             	comm.replyConnect(distantK, buildSynch('r'));

                sendMessage(mykernel, new NetworkRequest(NetworkRequest.CONNECTION_REQUEST, distantKernel));
                if(hasGUI()) agentGUI.setStatus("ok");
                debug("  ---------SocketCom--------- Etape 0 ");
                if(otherKernels != null)
                	for(Iterator i = otherKernels.iterator();i.hasNext();)
	                {
        	        	SocketKernel distantSocket = (SocketKernel) i.next();
                		if(! ((socketInfo.equals(distantSocket) || kernels.containsValue(distantSocket))))
                			comm.connect(distantSocket, buildSynch('c'));
					}
            }
       	}
	}


  	/* ---------------------- */
	/* Fonctions utilitaires  */
	/* ---------------------- */


  	// Construction d'un vecteur de synchronisation.
  	// c signifie connect et r signifie reply.
  	private Vector buildSynch(char s)
  	{
  		Vector v = new Vector();

		if (s == 'c') v.addElement("TRANSMIT_SOCKET_INFO");
		else if (s == 'r') v.addElement("ACK_SOCKET_INFO");

		v.addElement(getAddress().getKernel());
		v.addElement(socketInfo);
		v.addElement(mykernel);
		v.addElement(new HashSet(kernels.values()));

		return v;
	}

  	// Ne pas détruire l'agent à la fermeture de la fenêtre.
    //public void windowClosing(AWTEvent we){}

}