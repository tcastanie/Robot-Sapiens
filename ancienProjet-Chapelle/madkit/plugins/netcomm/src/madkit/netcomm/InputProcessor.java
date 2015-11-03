/*
 * InputProcessor.java - Created on Nov 2, 2003
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

/**Utility Class. It handles the incomming data as defined in <code>receiveIncomming()</code> method of the P2PAgent.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr *
 * @version $Revision: 1.1 $
 */

class InputProcessor implements Runnable{
	private P2PAgent agent;
	private Thread myThread=null;
	
	public InputProcessor(P2PAgent ag){
		agent=ag;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Thread currentThread = Thread.currentThread();
		while(myThread == currentThread){
			agent.receiveIncomming();
		}
	}
	
	/**
	 * Starts the Processor
	 */
	public void start(){
		if (myThread == null) {
			myThread = new Thread(this,"IncommingProcessor");            
			myThread.start();
            
		}
	}  

	/**
	 * Stops the Processor
	 */
	public void stop(){
		myThread=null;
	}

	/**Gets the Processor state.
	 * @return true if the processor is not running.
	 */
	public boolean isStopped() {
		return myThread==null;
	}
}

