/*
 * AbstractDynamicConnectionAgent.javaCreated on Dec 11, 2003
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

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import madkit.kernel.AgentAddress;
import madkit.kernel.Kernel;
import madkit.kernel.KernelMessage;
import madkit.netcomm.handlers.ConnectionHandler;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *	@version $Revision: 1.1 $
 */
abstract class AbstractDynamicConnectionAgent extends P2PAgent {
	
	/**Contains the Distant kernels that this agent is in charge of*/
	private Hashtable _handlers=new Hashtable();
	
	/**Adds <code>handler</code> as the handler for <code>kernelID</code>.
	 * The handler is only added if no handler exists for <code>kernelID</code>.
	 * @param kernelID 
	 * @param handler 
	 * @return true if the handler was added. false otherwise.
	 */
	public synchronized boolean addConnectionHandler(String kernelID, ConnectionHandler handler){
		boolean b;
		synchronized(_handlers){
			if(_handlers.keySet().contains(kernelID)){
				debug("not adding handler..  kenel id already here ");
				b= false;
			}else{
				debug("new handler added");
				_handlers.put(kernelID,handler);
				b= true;
			}
		}
		
		return b;
	}
	
	/**gets the handler for the kernel's id <code>kernelID</code>
	 * @param kernelID
	 * @return the connection handler, null it no handler exists.
	 */
	public ConnectionHandler getConnectionHandler(String kernelID){
		ConnectionHandler h;
		synchronized(_handlers){
			h=(ConnectionHandler) _handlers.get(kernelID);
		}
		return h;
	}
	
	protected void handleHook(KernelMessage kmsg){
		if(kmsg.getType()==KernelMessage.MONITOR_HOOK){
			switch (kmsg.getOperation()) {
				case Kernel.NEW_COMMUNITY :
					String comm=(String)kmsg.getArgument();
					startNomalCommIfNeeded(comm,false);
					break;
				
				default :
					debug("unkwon kernel message received");
					break;
			}
		}
		if(kmsg.getType()==KernelMessage.REPLY){
			switch (kmsg.getOperation()) {
				case Kernel.DUMP_COMMUNITIES:
					Map org = null;
					// reply from DUMP_ORG
					if (kmsg.getType() == KernelMessage.REPLY) {
						org =  (Map) kmsg.getArgument();
						 Map e= (Map) org.get("communities");
						Collection co=e.keySet();
						for (Iterator iter = co.iterator(); iter.hasNext();) {
							String comm=(String) iter.next();
							if(!comm.equals("public"))
								startNomalCommIfNeeded(comm,true);
							
						}
					}					
				break;

				default :
					break;
			}
		}
			
	}
	
	
	
	/*----------------------------------------------------------------------------------------*/
	/**Checks if the community <code>comm</code> is shared by the local kernel and
	 * any other kernel. If it is, the DynamicConnection Agent will request a normal communication
	 * if:<br>
	 * <ul>
	 * <il><code>onlyIfActive</code> is false. or</il><br>
	 * <il><code>onlyIfActive</code> is true and this agent is on active mode with the distant kernel in question.<br></il>
	 * </ul>
	 * @param comm Community to check.
	 * @param onlyIfActive request normal communication only if in active mode
	 */
	private void startNomalCommIfNeeded(String comm, boolean onlyIfActive) {
		Collection oks=sharesCommunity(comm);
		if(oks!=null){//a distant kernel shares a community .. should pass to normal comm
			if(canHandleNormalConnection()){
				for (Iterator iter = oks.iterator(); iter.hasNext();) {
				AgentAddress aa = (AgentAddress) iter.next();
					if(!onlyIfActive)
						requestNormalConnection(aa);
					else{
						ConnectionHandler ch=getConnectionHandler(aa.getKernel().getID());
						if(ch.isActiveMode()){
							requestNormalConnection(aa);
						}
					}
				}
			}
		}
	}

	/**Checks if the localkernel shares the community <code>createdCommunity</code> with other kernels.
	 * If the commutity is shared, returns a Collection containing the agentAddress of the distant siteAgents.
	 * returns null if the community is not shared.
	 * @param createdCommunity
	 * @return returns a Collection containing the agentAddress of the distant siteAgents, If the commutity is shared.
	 */
	protected Collection sharesCommunity(String createdCommunity){
		AgentAddress aas[]=getAgentsWithRole("communities",createdCommunity,"site");
		String kid=myKernel.getKernel().getID();
		boolean belong=false;
		for(int i=0; (i < aas.length) && !belong ;i++){
			belong=kid.equals(aas[i].getKernel().getID());
		}
		
		if(!belong) return null; //if i don't belong to the community.. then.. i dont share the community
		Vector otherKernel=new Vector();//holds the list of kernels.. handled by this agent. that shares the communitiy
		synchronized(_handlers){
			for(int i=0; i < aas.length;i++){//if i do belong.. with whom do i share it??	
				if(_handlers.containsKey(aas[i].getKernel().getID())){//one of my handled kernels belongs... do i belong??	
					if(! aas[i].getKernel().getID().equals(myKernel.getKernel().getID())) //should pass this test always 
						otherKernel.add(aas[i]);
					else debug("My Kernel is part of the routes... ??");
				}
		}
		}
		return otherKernel;
		//return null;
	}
	
	/**Removes the connection handler associated with <code>id</code>.
	 * @param id
	 */
	protected void removeConnectionHandler(String id){
		synchronized(_handlers){
			_handlers.remove(id);
		}
	}
	/*----------------------------------------------------------------------------------------*/
	/**Returns a collection containing all availlable handlers.
	 * @return
	 */
	
	protected Collection getAllHandlers(){
		Vector v=new Vector();
		synchronized(_handlers){
			v.addAll(_handlers.values());
			
		}
		
		return  v;
	}
	
	/*----------------------------------------------------------------------------------------*/
	/**
	 * @return
	 */
	
	protected boolean isHandlersEmpty(){
		boolean b;
		synchronized(_handlers){
			b=_handlers.isEmpty();
		}
		return b; 
	}
	
	/*----------------------------------------------------------------------------------------*/
	/**Checks if the implementation agent can move to a normal permanent connection.
	 * @return
	 */
	protected abstract boolean canHandleNormalConnection() ;
	
	/*----------------------------------------------------------------------------------------*/
	/**Requests the distant kernel to move to a p2p constant connection.
	 * @param aa
	 */
	protected abstract void requestNormalConnection(AgentAddress aa) ;
	
}
