/*
 * StatsAgent.java - Created on Nov 17, 2003
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

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import madkit.kernel.Message;


/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class StatsAgent extends MadkitNetworkAgent {
	public static final long DEFAULT_SAMPLE_PERIOD=400;
	
	private boolean alive =true;
	
	private long samplePeriod=DEFAULT_SAMPLE_PERIOD;
	private boolean sample=NetConfigMessage.isEnableStat();
	
	
	private StatsAgentGUI gui=null;
	
	public StatsAgent(boolean shouldSample){
		sample=shouldSample;
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
		setDebug(NetConfigMessage.isDebug());
		sample=NetConfigMessage.isEnableStat();
		samplePeriod=NetConfigMessage.getSamplePeriod();
	}
	
	///////////////////////////////////////
	//// Madkit agent methods
	/////////////////////////////////

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		super.activate();
		requestRole(community,group,"stadistics");
		
		if(!hasGUI() && sample){
			redisplayMyGUI();
		}
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		while(alive){
			Message m=waitNextMessage(samplePeriod);
			exitImmediatlyOnKill();
			if(sample){
				if(hasGUI()){
					gui.setReceived(getReceivedBytesCount());
					gui.setSent(getSentBytesCount());
					reset();
				}
			}
			if(m instanceof NetConfigMessage){
				handleConfigMessage((NetConfigMessage) m);
			}
		}
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		leaveRole(community,group,"stadistics");
	}
	
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#initGUI()
	 */
	public void initGUI() {
		if(gui==null){
			gui=new StatsAgentGUI();
			setGUIObject(gui);
		}
	}
	
	///////////////////////////////////////
	//// Stats control methods
	/////////////////////////////////

	/**
	 * @return Returns the samplePeriod.
	 */
	long getSamplePeriod() {
		return samplePeriod;
	}

	/**
	 * @param samplePeriod The samplePeriod to set.
	 */
	void setSamplePeriod(long samplePeriod) {
		this.samplePeriod = samplePeriod;
	}

	
	void setSample(boolean s){
		sample=s;
	}
	
	

	
	

}

///////////////////////////////////////////////////////////////////////////////
////		Agent's GUI
///////////////////////////////////////////////////////////////////////////////

class StatsAgentGUI extends JPanel{
	private Monitor received;
	private Monitor sent;
	
	private final long maxOut=10000;
	private final long maxIn=10000;
	
	public StatsAgentGUI(){
		received=new Monitor();
		received.setBottomString("   Time (ms)");
		received.setTopString("   Received Bytes");
		sent=new Monitor();
		sent.setBottomString("   Time (ms)");
		sent.setTopString("   Sent Bytes");
		
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		
		add(received);
		add(sent);
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(140,200));
	}
	
	public void setReceived(long bytes){
		received.setValues(maxIn,bytes);
	}
	

	public void setSent(long bytes){
		sent.setValues(maxOut,bytes);
	}
	
}
