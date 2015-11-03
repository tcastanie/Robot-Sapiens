/*
 * SocketDynamicConnection.javaCreated on Dec 11, 2003
 *
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *Last Update $Date: 2003/12/17 16:33:14 $

 */
package madkit.netcomm;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Vector;

import madkit.kernel.AgentAddress;
import madkit.kernel.Kernel;
import madkit.kernel.KernelAddress;
import madkit.kernel.KernelMessage;
import madkit.kernel.Message;
import madkit.kernel.NetworkRequest;
import madkit.netcomm.handlers.ConnectionHandler;
import madkit.netcomm.handlers.SocketHandler;
import madkit.netcomm.handlers.UnreachableKernel;

/**Dynamic Connection Agent that uses standard sockets.<br>
 * This agent will handle the communition with kernels with no shared communities.
 * When a community is shared with on of the handled kernels, the communication will
 * be handled by a normal permanent socket communication Agent. (in this cases the BasicCommAgent) 
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
class SocketDynamicConnection extends AbstractDynamicConnectionAgent {
	
	private static int SERVER_PORT=22222;
	
	private int _port=SERVER_PORT;
	private static final int refeshingTime=100000;
	
	//internal communication between Dynamic connection agents
	private static final String REFRESH="refreshing";
	private final static String DISCONECTION="DISCONNECTION";
	private final static String START_NORMAL_COMM="startnormalcomm";
	
	//server side
	/**Output stream*/
	protected DataOutputStream serverout = null;
	/**Input stream*/
	protected DataInputStream serverin = null;
	
	static final String handledProtocol="multipleSocketv1";
	static final String ROLE=handledProtocol;
	
	private InputProcessor input=null;
	private ServerSocket server=null;
	private SocketKernel localSocketKernel;
	private boolean alive=true;
	
	//contains the set of distant kernel waiting for a sync ack.
	private Vector _pendingSync=new Vector();

	public SocketDynamicConnection(SocketKernel sk){		
		localSocketKernel=sk;
		
	}
	
	/**Starts a server socket*/
	private void startServer() {
		boolean done=false;
		while(!done){
			try {
				server=new ServerSocket(_port);
				done=true;
				SERVER_PORT=_port+1;// i update so there is no conflict with
				debug("finally listening on "+_port);
			} catch (IOException e) {
				_port++;
			}
		
		}
		

		input=new InputProcessor(this);
		input.start();
	}


	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.AbstractDynamicConnectionAgent#canHandleNormalConnection()
	 */
	protected boolean canHandleNormalConnection() {
		return true;
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.AbstractDynamicConnectionAgent#requestNormalConnection(madkit.kernel.AgentAddress)
	 */
	protected void requestNormalConnection(AgentAddress aa) {
		ConnectionHandler handler=getConnectionHandler(aa.getKernel().getID());
		try {
			Socket s=((SocketHandler)handler).createSocket();
			openInputOutputStreams(s);
			sendObject(getAddress().getKernel());
			sendObject(START_NORMAL_COMM);
			KernelAddress dka=handler.getDistantKernel();
			launchNormalCommAgent(s, dka);
		} catch (IOException e) {
			debug("IOException caught : "+ e.getMessage());
		}
	}

	/**Launches a BasicCommAgent.
	 * @param s
	 * @param dka distant kernel kernelAddress
	 */
	private void launchNormalCommAgent(Socket s, KernelAddress dka) {
		BasicCommAgent bca=new BasicCommAgent(s,dka,_port);
		launchAgent(bca,"basiccommAgent",false);
		Vector v=new Vector();
		v.add(dka);
		v.add(bca.getAddress());
		v.add(bca.getProtocol());
		sendMessage(getAgentWithRole(community,group,"router"),new NetworkMessage(NetworkMessage.UPDATE_ROUTE,v));
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#closeSocket()
	 */
	protected void closeSocket() {debug("calling close socket.. ERROR");} //nothing to do .. there is no premanent socket

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#getProtocol()
	 */
	protected String getProtocol() {
		return handledProtocol; 
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleNetworkMessage(madkit.netcomm.NetworkMessage)
	 */
	protected void handleNetworkMessage(NetworkMessage message) {
		switch (message.getType()) {
			case NetworkMessage.DIE :
				informDisconection();
				alive=false;
				break;
			case NetworkMessage.DISTANT_MESSAGE:
				sendDistantMessage((Message) message.getArgument());
				break;
			case NetworkMessage.HANDLE_KERNEL_COMM:
				debug("handling demand!!");
				Vector v= (Vector) message.getArgument();
				Socket sock=(Socket) v.get(0);
				Object o=v.get(1);
				if( (o==null ) || ( o instanceof HashSet) ){
					HashSet dkernels=(HashSet) o;
					if(myKernel==null){
						myKernel=getAgentWithRole(Kernel.DEFAULT_COMMUNITY,"communications","site");
					}
					new DySocketConfigurator(this,_port,myKernel,localSocketKernel,sock,dkernels).start();
					
				}else 
//					if(o instanceof KernelAddress){
//					KernelAddress distantkernel=(KernelAddress) o;
//					int port=((Integer)v.get(2)).intValue();
//					//SocketHandler sh=new SocketHandler(getAddress().getKernel(),distantkernel,sock.getInetAddress(),port);
//				}else
					{
					debug("Demand malformed");
				}
				
				break;
			case NetworkMessage.SYNCH_REQUEST_REPLY :
				debug("SYNCH_REQUEST_REPLY received");
				handlePendingSync(message);
				break;
			case NetworkMessage.UPDATE_ROUTE_DONE:
				removeConnectionHandler(((KernelAddress)message.getArgument()).getID());
				break;
			default :
				break;
		}

	}


	/*----------------------------------------------------------------------------------------*/
	/**
	 * Informs all the handled kernels of the disconnection of the local kernel
	 */
	private void informDisconection() {
		Collection ta=getAllHandlers();
		for (Iterator iter = ta.iterator(); iter.hasNext();) {
			ConnectionHandler element = (ConnectionHandler) iter.next();
			sendObject(DISCONECTION,element);
		}
	}
	/*----------------------------------------------------------------------------------------*/
	/**Sends the sync to all pending Kernels
	 * @param message the networkmessage received from the router agent (type: NetworkMessage.SYNCH_REQUEST_REPLY)
	 */
	private void handlePendingSync(NetworkMessage message) {
		Vector sync=buildSynch('r',(HashSet) message.getArgument());
		String id;
		for(int i=0;i<_pendingSync.size();i++){
			id=(String) _pendingSync.get(i);
			try {
				getConnectionHandler(id).sendObject(sync);
			} catch (Exception e) {				
				debug("Exception caught : "+ e.getMessage());
			}
			_pendingSync.remove(i);
			
		}
		
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleConfigMessage(madkit.netcomm.NetConfigMessage)
	 */
	protected void handleConfigMessage(NetConfigMessage message) {
		// TODO Auto-generated method stub

	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();
		if(getAgentWithRole(community,group,ROLE)!=null){
			alive=false;
		}else{
			requestRole(community,group,handledProtocol,memberCard);
			startServer();
			AgentAddress kernel=getAgentWithRole("system","kernel");
			sendMessage(kernel,new KernelMessage(KernelMessage.REQUEST_MONITOR_HOOK,Kernel.NEW_COMMUNITY));
		}
			
		
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		while(alive){
			Message msg=waitNextMessage(refeshingTime);
			exitImmediatlyOnKill();
			if(!alive) break;
			if(msg ==null){
				refreshConnections();
				continue;
			}
			if(msg instanceof NetworkMessage){
				handleNetworkMessage((NetworkMessage) msg);
			}else if(msg instanceof NetConfigMessage){
				handleConfigMessage((NetConfigMessage) msg);
			}else if (msg instanceof KernelMessage){
				handleHook((KernelMessage)msg);
				
			}
		}
	}



	/*----------------------------------------------------------------------------------------*/
	
	/**Checks that all handled kernels are still available.<br>
	 * As the connections are dynamic, this means that a connection is opened only when a 
	 * message needs to be sent, this method checks that all kernels are available, if not the handler is removed.<br> 
	 * 
	 */
	private void refreshConnections() {
		Collection ta=getAllHandlers();
		for (Iterator iter = ta.iterator(); iter.hasNext();) {
			ConnectionHandler element = (ConnectionHandler) iter.next();
			sendObject(REFRESH,element);
		}
		
	}

	/*----------------------------------------------------------------------------------------*/
	/**Send a message to a distant kernel. It automatically retreives the right handler using the message's receiver.
	 * @param msg 
	 */
	private void sendDistantMessage(Message msg) {
		ConnectionHandler handler=super.getConnectionHandler(msg.getReceiver().getKernel().getID());
		if(handler==null){
			sendDisconnectedKernelInformation(msg.getReceiver().getKernel());
		}else{
		sendObject(msg, handler);
		}
	}

	/**Sends an object using the especified handler
	 * @param obj Object to send
	 * @param handler ConnectionHandler to use
	 */
	private void sendObject(Object obj, ConnectionHandler handler) {
		try {
			handler.sendObject(obj);
		} catch (IOException e) {
			debug("Impossible to send Distant Message : "+e.getClass().getName()+" : "+e.getMessage()+" removing handler");
			removeConnectionHandler(handler.getDistantKernel().getID());
			sendDisconnectedKernelInformation(handler.getDistantKernel());
			System.gc();
		} catch (UnreachableKernel e) {
			debug("Impossible to send Distant Message : "+e.getClass().getName()+" : "+e.getMessage()+" removing handler");
			removeConnectionHandler(handler.getDistantKernel().getID());
			sendDisconnectedKernelInformation(handler.getDistantKernel());
			System.gc();
		}
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.netcomm.P2PAgent#receiveIncomming()
	 */
	void receiveIncomming() {
		try {
			Socket sock=server.accept();
			openServerInputOutputStreams(sock);
			DynamicObject o=receiveDynamicObject();
			if(o==null) return;
			if(o.getObject() instanceof Message){
				injectMessage((Message) o.getObject());
			}else if (o.getObject() instanceof Vector){
					debug("Vector Received! - sync");
					requestSynch(o);
			}else if(o.getObject() instanceof String){
				if(handleDistantInformation(o)){
					launchNormalCommAgent(sock,o.getKernelAddress());
					return;//must return to avoid closing the socket of the normal comm.
				}
			}else{
				debug("Unknwon object received");
			}
			sock.close();
		} catch (ClassNotFoundException e) {
			//logger.debug("ClassNotFoundException caught ",e);
			debug("ClassNotFoundException caught : "+ e.getMessage());
		} catch (IOException e) {
			//logger.debug("IOException caught ",e);
			debug("IOException caught : "+ e.getMessage());
		}
	}
	

	/*----------------------------------------------------------------------------------------*/
	/**
	 * @param sock
	 */
	private void openServerInputOutputStreams(Socket sock) {
		try {
			serverout = new DataOutputStream(sock.getOutputStream());
			serverin = new DataInputStream(new BufferedInputStream(sock.getInputStream()));
		} catch (IOException e) {
			debug("IOException caught : "+e.getMessage());
		}
	}

	/*----------------------------------------------------------------------------------------*/
	/**Handles an internal message sent by a peer DynamicConnectionAgent.
	 * @param string
	 */
	private boolean handleDistantInformation(DynamicObject o) {
		String info=(String) o.getObject();
		if(info.equals(DISCONECTION)){
			removeConnectionHandler(o.getDistantKernelID());
			sendDisconnectedKernelInformation(o.getKernelAddress());
		}else if(info.equals(REFRESH)){
			debug("refreshing");
		}else if(info.equals(START_NORMAL_COMM)){
			return true;
		}
		return false;
	}

	/*----------------------------------------------------------------------------------------*/
	/**receives a Dynamic object.
	 * @return the received dynamic object
	 */
	private DynamicObject receiveDynamicObject() throws IOException, ClassNotFoundException {
		Object o=serverReceiveObject();
		KernelAddress id;
		
		if(o instanceof KernelAddress){
			id=(KernelAddress) o;
		}else{
			debug("Dynamic Object error... ERROR");
			return null;
		}
		o=serverReceiveObject();
		DynamicObject dyo=new DynamicObject(id,o);
		return dyo;
	}

	/*----------------------------------------------------------------------------------------*/
	/**
	 * @return
	 */
	private Object serverReceiveObject() throws IOException, ClassNotFoundException {
		Object a;
		debug("Waiting incomming data");
		int len = serverin.readInt(); //reception de la taille de l'objet serialise...
		debug("R:done "+len+" preparing array");
		if(len<0){
			return null;
		}
		byte[] byteobj = new byte[len];
		serverin.readFully(byteobj); // lecture de l'obj serialise...
		
		a=Codec.decode(byteobj);
		bytesReceived(len);
		return a;
		
		
	}

	//TO REFACTOR!!
	/**Requests the Sync of Kernels to the Router Agent.
	 * @param args The Vector received from the other side.
	 */
	private void requestSynch(DynamicObject dyo){
		
		Vector args=(Vector) dyo.getObject();
		Collection otherKernels=null;//este vector va a contener los kernels distantes
		Vector v=new Vector();
		Enumeration a = args.elements();
		String trafficOperation = (String) a.nextElement();
		AgentAddress kaddr=(AgentAddress) args.get(3);//AgentAddres del kernel distante
		
		

		//create a compatible SocketKernel 
		v.add(args.get(1));//kernelAddres
		v.add(args.get(2));//socketKernle
		v.add(args.get(3));//kernel's AgentAddress
		v.add(getProtocol());
		v.add(args.get(4));//otherKernels
		

		
		if (trafficOperation.equals("TRANSMIT_SOCKET_INFO")){
			debug("  ---------SocketCom------- TRANSMIT_SOCKET_INFO ---- ");
			debug("Adding pending sync");
			_pendingSync.add(dyo.getDistantKernelID());
			//envio los kernels del kernel distante a al router para q genere nuevas conexiones.
			sendMessage(community,group,"router",new NetworkMessage(NetworkMessage.SYNCH_REQUEST,v));
			//informa al kernel
			fowardToKernel( new NetworkRequest(NetworkRequest.CONNECTION_REQUEST, kaddr));
		}
		
		if (trafficOperation.equals("ACK_SOCKET_INFO"))
		{
			debug("  ---------SocketCom--------- ACK_SOCKET_INFO ---- ");
			sendMessage(community,group,"router",new NetworkMessage(NetworkMessage.SYNCH_REQUEST,v));
			fowardToKernel( new NetworkRequest(NetworkRequest.CONNECTION_REQUEST, kaddr));
		}
		
	
	}
	
	//	Construction d'un vecteur de synchronisation.
	// c signifie connect et r signifie reply.
	/* En el protocol el orden en el vector es
	 * 1. El kernelAddress del kernel local
	 * 2. el SocketKernel local
	 * 3. el AgentAddress del Kernel
	 * 4. un hashset con los kernels distantes.
	 * */
	private Vector buildSynch(char s,HashSet dKernels)
	{
		Vector v = new Vector();

		if (s == 'c') v.addElement("TRANSMIT_SOCKET_INFO");
		else if (s == 'r') v.addElement("ACK_SOCKET_INFO");

		v.addElement(getAddress().getKernel());
		
		
		v.addElement(localSocketKernel);//need to change the socketKernel object to keep backwards comp
		v.addElement(myKernel);
//		Collection scks=new Vector();
//		for (Iterator iter = dKernels.iterator(); iter.hasNext();) {
//			DistantKernelInformation info=(DistantKernelInformation) iter.next();
//			SocketKernel sk=info.getSocketKernel();
//			scks.add(sk);
//		}
//		v.addElement(new HashSet(scks));
		v.add(dKernels);
		return v;
	}
	
	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		informDisconection();
	}

}

/**
 * A dynamic object contains an object and the kernel address of the kernel that originated this object.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class DynamicObject{
	final KernelAddress _id;
	final Object _obj;
	
	public DynamicObject(KernelAddress kid,Object obj){
		_id=kid;
		_obj=obj;
	}
	
	/*----------------------------------------------------------------------------------------*/
	/**Gets the Distant kernel address that originated this message
	 * @return the distant kernel address
	 */
	public KernelAddress getKernelAddress() {
		return _id;
	}

	/**Gets the Distant kernel address ID that originated this message
	 * @return the distant kernel address id
	 */
	public String getDistantKernelID(){
		return _id.getID();
	}
	
	/**gets the object sent by the distant kernel
	 * @return the object.
	 */
	public Object getObject(){
		return _obj;
	}
}
