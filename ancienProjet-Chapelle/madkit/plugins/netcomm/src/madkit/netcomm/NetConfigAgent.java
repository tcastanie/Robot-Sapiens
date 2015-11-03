/*
 * NetConfigAgent.java - Created on Oct 18, 2003
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
 * Last Update: $Date: 2003/12/17 16:33:14 $
 */

package madkit.netcomm;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;

import madkit.kernel.AgentAddress;
import madkit.kernel.InvalidAddressException;
import madkit.kernel.KernelAddress;
import madkit.netcomm.rules.ConnectionRules;
import madkit.netcomm.rules.Rule;

/**This Agent configures the communication to be used.
 * It has two different modes:<br>
 * <b>passive</b>: when the connection to configure is an incomming connection
 * received by the TCPServerAgent. In this mode the agent will wait
 * for an incomming int and will use it to known whether the communication
 * is from an old communicator or a NetConfigAgent.<br>
 * <b>active</b>: when the connection is a request of the local kernel. In this
 * case the agent will try to use the new configuration protocol. If refused will try to
 * connect as a communicator agent.<br>
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class NetConfigAgent extends MadkitNetworkAgent {
	/**Used to anounce a broadcast communication*/
	public static final int MADKIT_BROADCAST=-31337;
	/**Used to request the new configuration protocol*/
	public static final int MADKIT_CONFIG=-99;
	
	private static final String REFUSE_STRING="REFUSE:";
	//***The different stages of configuration
	//	tries to find out if the other understand a protocol negociation. (Communicator 3.1 doesn't)
	private static final int CHECK_PROTO=0;
	// The other side is a communicator of madkit 3.1
	private static final int OLD_PROTO=1;
	// negotiation of protocols
	private static final int GET_BEST_PROTO=2;
	// configure the protocol and start the p2pagent associated with this protocol
	private static final int CONFIG_PROTO=3;
	
	private static Hashtable _configuring=new Hashtable();
	
	private int size;
	
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private boolean passive;
	private boolean alive=true;
	/**Rule to apply to this Connection*/
	private Rule rule;
	/**Local SocketKernel information*/
	private SocketKernel myInfo;
	
	/**Contains the known distant kernels*/
	private HashSet dKernels=null;
	
	/**Keeps the selected protocol.*/
	private String protocolToUse=null;
	
	private int stage=CHECK_PROTO;
	/**Creates a new ConfigAgent. This constructor should be used when 
	 * the Agent should configure an incomming connection. Sets the agent to passive mode.
	 * @param socket Socket created by the incomming connection.
	 * @param info Local SocketKernel information
	 */
	public NetConfigAgent(Socket socket, SocketKernel info, SocketKernel distantSK){
		this(socket,info,distantSK,null);
		this.passive=true;
	}
	
	
	/**Creates a new NetconfigAgent and sets it to active mode
	 * @param socket2
	 * @param myInfo2
	 * @param collection
	 */
	public NetConfigAgent(Socket socket2, SocketKernel info,SocketKernel distantSK, HashSet kernels) {
		
		this.socket=socket2;
		this.passive=false;
		myInfo=info;
		if(kernels!=null)
			dKernels=(HashSet) kernels.clone();
//		if(distantSK!=null){
//			alive=checkhost(distantSK.getHost(),distantSK.getPort());
//			if(!alive){closeSocket();}
//		}
		openInputOutputStreams(socket);	
		loadRule();//load the rule for this connecition
	}
	
//	private static synchronized boolean checkhost(String host, int port){
//		Integer iport=(Integer) _configuring.get(host);
//		if(iport!=null && iport.intValue()==port){
//			return false;
//		}
//		_configuring.put(host,new Integer(port));
//		return true;
//	}
//	
	private void openInputOutputStreams(Socket socket) {
		try {
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		} catch (IOException e) {
			debug("IOException caught "+e.getMessage());
			alive=false;
		}
	}
	private void loadRule(){
		ConnectionRules r=ConnectionRules.getInstance();
		this.rule=r.getRule(socket.getInetAddress().getHostName());
		if(rule.getAction()==Rule.REJECT){//no connection is allowed 
			closeSocket();
		}
	}
	
	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {		
		while(alive){
			try {
				switch (stage) {
					case CHECK_PROTO :
						debug("CHECK_PROTO");
						tryNewProtocols();
						break;
					case OLD_PROTO:
						oldProtocolConfig(size);
						break;
					case GET_BEST_PROTO:
						debug("GET_BEST_PROTO");
						negociateProtocol();
						break;
					case CONFIG_PROTO:
						debug("CONFIG_PROTO");
						configProtocol();
						break;
					default :
						debug("Unknown stage");//if the agent gets here... find the bug.. :-)
						alive=false;
						break;
				}
				
			} catch (IOException e) {
				println("Configuration Error: Exception caught . No P2PAgent launched");
				alive=false;
			} catch (InvalidAddressException e) {
			}
			
		}
		
	}

	/**Configures the selected protocol.
	 * When adding new protocols , add here the configuration.
	 * 
	 */
	private void configProtocol() {
		if(protocolToUse!=null){
			
			if(protocolToUse.equals(KnownProtocols.COMM_PROTO)){
				stage=OLD_PROTO;
				
			}else if(protocolToUse.equals(KnownProtocols.SIMPLE_SSL_PROTO_V1)){
				try {
					startSimpleSSL();
				} catch (IOException e) {
					debug("IOException caught "+e.getMessage());
				} catch (InvalidAddressException e) {
					debug("InvalidAddressException caught "+e.getMessage());
				}
				
			}else if(protocolToUse.equals(KnownProtocols.MULTIPLE_SOCKETS_V1)){
				
				Vector arg=new Vector();
				arg.add(this.socket);
				arg.add(dKernels);
				debug("Finding agent for "+ KnownProtocols.MULTIPLE_SOCKETS_V1);
				AgentAddress aa=getAgentWithRole(community,group,SocketDynamicConnection.ROLE);
				if(aa!=null){//there is already a multiple socket.. assing it to him
					debug("Requesting to add Handler");
					closeInputOuput();
					sendMessage(aa,new NetworkMessage(NetworkMessage.HANDLE_KERNEL_COMM,arg));
				}else{//there is no multiple socket.. Create him
					debug("No agent handle "+SocketDynamicConnection.ROLE);
					SocketDynamicConnection smc=new SocketDynamicConnection(myInfo);
					launchAgent(smc,SocketDynamicConnection.ROLE,false);
					aa=getAgentWithRole(community,group,SocketDynamicConnection.ROLE);
					sendMessage(aa,new NetworkMessage(NetworkMessage.HANDLE_KERNEL_COMM,arg));
				}
				alive=false;
			}
		}else{
			debug("Unable to connect - No protocolToUse");
			closeSocket();
		}
	}

	/*----------------------------------------------------------------------------------------*/
	/**
	 * 
	 */
	private void closeInputOuput() {
		in=null;
		out=null;
	}


	/**
	 * 
	 */
	private void startSimpleSSL() throws IOException, InvalidAddressException {

		KernelAddress ka = exchangeKernelInformation();
		if(ka==null){
			closeSocket();
			return;
		}
			
		if(dKernels==null){
			debug("launching in passive mode!");
			launchAgent(new SimpleSSLAgent(ka,socket,myInfo),"SimpleSSLAgent",false);
		}else{
			launchAgent(new SimpleSSLAgent(ka,socket,myInfo,dKernels),"SimpleSSLAgent",false);
		}
		alive=false;
	}

	private KernelAddress exchangeKernelInformation() throws IOException, InvalidAddressException {
		String kernel=myKernel.getKernel().getID();
		out.writeInt(kernel.length());
		out.writeChars(kernel);
		out.writeInt(UDPServerAgent.getPort());
		out.flush();
		debug("Local information sent. ID: "+kernel);
		
		//receive the distant kernel's info
		
		int size2=in.readInt();	
		
		KernelAddress ka;
		kernel="";
		for (int i=0;i<size2;i++){
			kernel=kernel+in.readChar();
		}
		
		int datagramSocketPort=in.readInt();
		
		ka=new KernelAddress(socket.getInetAddress().getHostName()+":"+kernel.toString());
		
		if(kernel.equals(myKernel.getLocalID())){
			debug("trying to add my own kernel");
			return null;
		}
		return ka;
	}

	/**
	 * Protocol negotiation.
	 */
	private void negociateProtocol() {
		if(dKernels==null){//passive communication
			Protocollnformation info = receiveProtocols();
			if(info.refused())//check if the other side refuses the connection
				informRemoteRefuse(info);
			if(rule.getAction()==Rule.REFUSE){//check if this side refuses the connection
				String refuseString=REFUSE_STRING+((String)rule.getArgument());
				sendObject(refuseString);
				closeSocket();
				return;
			}
			
			if(info==null){closeSocket(); return;}
			//all clear ... lets configure
			protocolToUse=rule.getProtocolInformation().getBestProtocol(info);
			if(protocolToUse==null){//no matching protocol
				println("Negotiation failed!!");
				closeSocket();
			}else{//a protocol was selected
				sendObject(protocolToUse);
			}
		}else{
			sendObject(rule.getProtocolInformation());
			protocolToUse=receiveProtocolToUse();
			if(protocolToUse!=null && protocolToUse.startsWith(REFUSE_STRING)){//the other side refueses the connection
				informRemoteRefuse(protocolToUse.substring(REFUSE_STRING.length()));
			}
			
		}
		stage=CONFIG_PROTO;
		
		
	}
	private void informRemoteRefuse(Protocollnformation info) {
		String reason=info.refuseReason();
			informRemoteRefuse(reason);
			
	}


	private void informRemoteRefuse(String reason) {
		println("Connection with "+socket.getInetAddress().getHostName()+" was refused by remote host");
		println("Reason: "+reason);
		closeSocket();
	}


	/**
	 * @return
	 */
	private String receiveProtocolToUse() {
		try {
			Object a=receiveObject();
			if(a instanceof String ){
				return (String) a;
			}else{
				println("Negotiation failed!!");
				closeSocket();
			}
			
		} catch (IOException e) {
			debug("IOException caught "+e.getMessage());
		} catch (ClassNotFoundException e) {
			debug("ClassNotFoundException caught "+e.getMessage());
		}
		return null;
	}

	/**
	 * @return
	 */
	private Protocollnformation receiveProtocols() {
		try{
			Object a=receiveObject();
			if(a instanceof Protocollnformation){
				return (Protocollnformation) a;
			}else{
				debug("Unknown object type received in negotiation");
				closeSocket();
			}
		}catch(IOException e3){
			debug("IOException -  unable to read input "+e3.getMessage());
			closeSocket();
			
		}catch (ClassNotFoundException e) {
			debug("ClassNotFoundException caught "+e.getMessage());
		}
		return null;
	}

	private Object receiveObject() throws IOException, ClassNotFoundException {
		debug("Reading incomming size");
		int len = in.readInt(); //reception de la taille de l'objet serialise...
		debug("R:done "+len+" preparing array");
		byte[] byteobj = new byte[len];
		in.readFully(byteobj); // lecture de l'obj serialise...
		return Codec.decode(byteobj);

	}

	private void sendObject(Object object) {
		try{
			
			byte[] data=Codec.encode(object);
			out.writeInt(data.length);//first send the size
			out.flush();
			out.write(data); //now the object
			out.flush();
			}
		catch(IOException e){
			println("Error in configuration");
			closeSocket();
		}

		}

	private void closeSocket(){
		debug("Closing Socket");
		alive=false;//kill the agent
		try {
			socket.close();
		} catch (IOException e) {}
	}
	/**
	 * 
	 */
	private  boolean tryNewProtocols() {
		debug("role in connection: "+(passive?"passive":"active"));
		try {
			if(dKernels!=null){// active
				out.writeInt(MADKIT_CONFIG);
				out.flush();
				debug("new proto id sent, waiting response");
			}
			
			size=in.readInt();
			if(size<0){
				if(passive){//if the agent is passive in comm ack the new protocols
					out.writeInt(MADKIT_CONFIG);
					out.flush();
				}
				stage=GET_BEST_PROTO;
				
				return true;
			}else{
				if(!passive){//need to re-open the socket ... is there a better, less rude way to do this??
					socket.close();
					socket=new Socket(socket.getInetAddress(),socket.getPort());
					openInputOutputStreams(socket);
					size=in.readInt();
				}
				stage=OLD_PROTO;
				return false;
			}
			
		} catch (IOException e) {
			closeSocket();
			return false;
		}
		
	}

	/**
	 * @param i
	 */
	private void oldProtocolConfig(int size2) throws IOException, InvalidAddressException {
		//check the rule
		//check that the protocol is containted in the available protocols
		if(!rule.contains(KnownProtocols.COMM_PROTO)){
			closeSocket();
			return;
		}		
		
		//TODO move this last configuration .. no need to be here...
		debug("Old Communicator's protocol initiated");
		
		//send my information
		String kernel=myKernel.getKernel().getID();
		out.writeInt(kernel.length());
		out.writeChars(kernel);
		out.writeInt(UDPServerAgent.getPort());//TODO UDP port
		out.flush();
		debug("Local information sent. ID: "+kernel);
		
		//receive the distant kernel's info
		if(size2<0){
			size2=in.readInt();	
		}
		KernelAddress ka;
		kernel="";
		for (int i=0;i<size2;i++){
			kernel=kernel+in.readChar();
		}
		
		int datagramSocketPort=in.readInt();
		
		ka=new KernelAddress(socket.getInetAddress().getHostName()+":"+kernel.toString());		
		if(kernel.equals(myKernel.getLocalID())){
			debug("trying to add my own kernel");
			return;
		}
		
		
		
		if(passive){
			launchAgent(new CommunicatorAgent(ka,socket,myInfo),"CommunicatorAgent",false);
		}else{
			launchAgent(new CommunicatorAgent(ka,socket,myInfo,dKernels),"CommunicatorAgent",false);
		}
		alive=false;//kill the communicator no more information need for this protocol
		
		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		super.end();		
		System.gc();
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleNetworkMessage(madkit.netcomm.NetworkMessage)
	 */
	protected void handleNetworkMessage(NetworkMessage message) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleConfigMessage(madkit.netcomm.NetConfigMessage)
	 */
	protected void handleConfigMessage(NetConfigMessage message) {
		// TODO Auto-generated method stub
		
	}

}
