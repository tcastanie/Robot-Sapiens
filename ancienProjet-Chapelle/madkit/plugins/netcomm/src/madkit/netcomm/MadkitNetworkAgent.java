/*
 * MadkitNetworkAgent.java - Created on Oct 20, 2003
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

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Kernel;

//import org.apache.log4j.Level;
//import org.apache.log4j.Logger;

/**This the generic Networking Agent.
 * Containts mostly standard information usefull for several agents type 
 * in the networking MAS 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
public abstract class MadkitNetworkAgent extends Agent {
	
//	/**A geneiric log4j Logger.
//	 *
//	 */
//	protected  final Logger logger=Logger.getLogger(this.getClass());
	
	/**This is the member card for the networking agents. only agents extending this type of agent 
	 * will be allowed into the Community. This Is mainly because of security reasons.*/
	protected static Object memberCard;
	/**Utility variable. It holds the Community name for the Networking agent.
	 */
	protected static final String community=Kernel.DEFAULT_COMMUNITY;
	/** The Netwoking group.*/
	protected static final String group="networking";
	
	/**The Local's kernel address.*/
	protected AgentAddress myKernel;
	
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#debug(java.lang.String)
	 */
	public void debug(String s) {
		super.debug(s);
//		logger.debug(s);
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#print(java.lang.String)
	 */
	public void print(String theString) {
		super.print(theString);
//		logger.info(theString);
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#println(java.lang.String)
	 */
	public void println(String theString) {
		super.println(theString);
//		logger.info(theString);
	}
	/**Used to report fatal errors in the communications proccess.
	 * This method should be used only when a unrecoverable error has occured
	 *@param msg
	 *@param exp
	 */
	protected void fatatError(String msg, Throwable exp){
//		logger.fatal(msg,exp);
	}
	
	/**Used to report fatal errors in the communications proccess.
	 * This method should be used only when a unrecoverable error has occured
	 * @param msg
	 */
	protected void fatatError(String msg){
		super.println("FATAL !!- "+msg);
//		logger.fatal(msg);
	}
	
	/**Handles the internal messages.
	 * @param message
	 * @see NetworkMessage
	 */
	protected abstract void handleNetworkMessage(NetworkMessage message);
	/**Handles the Configuration messages. These messages might have influence only in a 
	 * few type of agents.
	 * @param message
	 * @see NetConfigMessage
	 */
	protected abstract void handleConfigMessage(NetConfigMessage message);
	
	/**This methods has been overloaded to initialize some of the variables
	 * provided by this agent. Please remember to all it before your own activate method.
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();
		myKernel=getAgentWithRole(Kernel.DEFAULT_COMMUNITY,"communications","site");
		
	}

//	/* (non-Javadoc)
//	 * @see madkit.kernel.AbstractAgent#setDebug(boolean)
//	 */
//	public void setDebug(boolean b) {
//		super.setDebug(b);
//		if(b){
////			logger.setLevel(Level.DEBUG);
//		}else{
////			logger.setLevel(Level.INFO);
//		}
//	}

	//////////////////////////////////////////////////////////////////////////////
	///  For statistical use only !!!
	//////////////////////////////////////////////////////////////////////////////
	private static long inputBytes=0;//received bytes count
	private static long outputBytes=0;//sent bytes count
	
	protected long getSentBytesCount(){
		return outputBytes;
	}
	
	protected long getReceivedBytesCount(){
		return inputBytes;
	}
	
	protected void bytesReceived(long bytes){
		setBytesReceived(inputBytes+bytes);
	}
	
	protected void bytesSent(long bytes){
		setBytesSent(outputBytes+bytes);
	}
	
	private synchronized void setBytesSent(long bytes){
		outputBytes=bytes;
	}
	
	private synchronized void setBytesReceived(long bytes){
		inputBytes=bytes;
	}
	
	protected void reset(){
		setBytesReceived(0);
		setBytesSent(0);
	}
}
