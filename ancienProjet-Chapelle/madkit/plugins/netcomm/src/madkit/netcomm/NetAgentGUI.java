/*
 * NetAgentGUI.java - Created on Nov 2, 2003
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
 * Last Update: $Date: 2004/03/29 09:27:58 $
 */

package madkit.netcomm;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.TitledBorder;


/**This is the Gui of the NetAgent.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.2 $
 */
final class NetAgentGUI extends JTabbedPane {
	private NetAgent _agent;
	private JLabel _state;
	private RouterAgent _router;
	private RouterGUI _routergui;
	private JPanel mainPanel;
	public NetAgentGUI(NetAgent ag, RouterAgent router){
		_agent=ag;
		_router=router;
		//Graphic Componets
		mainPanel = new JPanel(new BorderLayout());
		_state=new JLabel("State: ");
		JPanel connectPanel=new JPanel();
		final JTextField host=new JTextField(20);
		final JSpinner port=new JSpinner();
		port.setValue(new Integer(NetAgent.DEFAULT_PORT));
		
		JButton bAdd=new JButton("Add");
		bAdd.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				String h=host.getText();
				int p=((Integer)port.getValue()).intValue();
				_agent.connectKernel(h,p);
			}
		});
		
		connectPanel.add(host);
		connectPanel.add(port);
		connectPanel.add(bAdd);
		
		JPanel pTitle=new JPanel();
		pTitle.setLayout(new GridLayout(2,1));
		pTitle.add(_state);
		pTitle.add(connectPanel);
						
		mainPanel.setPreferredSize(new Dimension(300,250));
		mainPanel.add(pTitle,BorderLayout.NORTH);

		_routergui=new RouterGUI(_router);
		mainPanel.add(_routergui,BorderLayout.CENTER);
		JPanel controlPanel=new JPanel();
		controlPanel.setBorder(new TitledBorder("Control"));
		controlPanel.setLayout(new BoxLayout(controlPanel,BoxLayout.Y_AXIS));
		JButton bRefresh=new JButton("Refresh");
		bRefresh.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				_routergui.updateRoutes();				
			}
		});
		controlPanel.add(bRefresh);
		
//		JButton bDisconnect=new JButton("Disconnet");
//		bDisconnect.addActionListener(new ActionListener(){
//
//			public void actionPerformed(ActionEvent e) {
////				KernelInfo info=(KernelInfo) _kernels.getSelectedValue();
////				if(info==null){
////					return;
////				}
////				_agent.disconnectKernel(info.getAgentAddress());
////				
//			}
//		});		
//		controlPanel.add(bDisconnect);
//		
		JSeparator sep=new JSeparator();
		controlPanel.add(sep);
		
		JButton bBroad=new JButton("BroadCast");
		bBroad.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				_agent.madkitBroadcast();				
			}
		});
		
		controlPanel.add(bBroad);
		mainPanel.add(controlPanel,BorderLayout.EAST);
		
		this.add("Connections",mainPanel);
		
		// launching StatsAgent
		StatsAgent stats=new StatsAgent(NetConfigMessage.isEnableStat());
		_agent.setStats(stats);
		stats.initGUI();
		_agent.launchAgent(stats,"StatsAgent",false);
		if (stats.getGUIObject() != null){
			this.add("Monitor",(JPanel) stats.getGUIObject());
		}
		
	}


	/**
	 * @param message
	 */
	public void kernelConnected(NetworkMessage message) {
		_routergui.updateRoutes();
	}


	/**
	 * @param message
	 */
	public void kernelDisconnected(NetworkMessage message) {
		_routergui.updateRoutes();
	}

	public void updateKernel(NetworkMessage message){
		_routergui.updateRoutes();
	}
	/**
	 * @param string
	 */
	public void setStatus(String string) {
		_state.setText("State: "+string);
		
	}

}

