/*
 * DetailsAgent.java - Created on Jan 31, 2004
 * 
 * Copyright (C) 2003-2004 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
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
 * Last Update: $Date: 2004/04/14 11:55:46 $
 */

package madkit.pluginmanager;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;

import madkit.kernel.Agent;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.3 $
 */
class DetailsAgent extends Agent implements UpdateRoles{
	boolean _alive=true;
	private final PluginInformation _info;
	/**
	 * 
	 */
	public DetailsAgent(PluginInformation info) {
		super();
		_info=info;

	}

	public PluginInformation getPluginInformation(){
		return _info;
	}
	/**
	 * Sends a message to the update agent to inform that the
	 * user selected this plugin to install
	 */
	public void informSelection() {
		sendMessage(getAgentWithRole(community,group,PLUGIN_MANAGER),new SelectedPluginMessage(_info));
		_alive=false;
		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() {
		while(_alive){
			
			pause(200);
		}
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		requestRole(community,group, DETAILS,null);
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
		disposeMyGUI();
		
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#initGUI()
	 */
	public void initGUI() {
		setGUIObject(new DetailsGUI(this));
	}

}


class DetailsGUI extends JPanel{
	private final DetailsAgent _agent;
	
	public DetailsGUI(DetailsAgent agent){
		_agent=agent;
		
		//components
		setLayout(new BorderLayout());
		JPanel btnPanel=new JPanel();
		
		JButton bSelect=new JButton("Select");
		bSelect.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				_agent.informSelection();
			}
		});
		
		JButton bClose=new JButton("Close");
		bClose.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				_agent.disposeMyGUI();
				_agent._alive=false;
				
			}
		});
		
		btnPanel.add(bSelect);
		btnPanel.add(bClose);
		
		add(btnPanel,BorderLayout.SOUTH);
		String info=formatDetails();
		JEditorPane lInfo=new JEditorPane();
		lInfo.setEditorKit(new HTMLEditorKit());
		lInfo.setEditable(false);
		lInfo.setText(info);
		JScrollPane scroll=new JScrollPane();
		scroll.setViewportView(lInfo);
		add(scroll,BorderLayout.CENTER);
		setSize(300,300);
	}
	
	/** Formats the plugin information into html.
	 * @return html formatted information
	 */
	private String formatDetails() {
		PluginInformation pluginfo=_agent.getPluginInformation();
		if(pluginfo==null){
			String str="<html><body>";
			str+="<font color=red > No Plugin Information available. <br> May be a parse error. <br> Please report this to the server Maintaner</font>";
			str+="</body></html>";
			
			return str;
		}
		String info="<html><body>";
		info+="<b>Name:</b>"+pluginfo.getName()+"<br>";
		info+="<b>Version:</b> "+pluginfo.getVersion().toString();
		info+="<br>";
		info+="<b>Author:</b><br>"+pluginfo.getAuthorName()+"<br>"+pluginfo.getAuthorEmail()+"<br>"+pluginfo.getAuthorWeb()+"<br>";
		if(pluginfo.getFileName()!=null){
		    info+="<br><b>File:</b> "+pluginfo.getFileName()+"<br>";
			info+="<b>Size</b> "+pluginfo.getSize()+"<br>";    
		}else{
		    info+="<br><b>File:</b> None <br>";
			
		}
		
		info+="<br><b>Depends: </b><br>";
		for (Iterator iter = pluginfo.getDependencies().iterator(); iter.hasNext();) {
			Dependency element = (Dependency) iter.next();
			info+=element.name +" ( "+element.version.toString()+" ) <br>";
		}
		
		info+="<b><br>Description</b>";
		info+="<p>";
		info+=pluginfo.getDescription();
		info+="</p>";
		info+="</body></html>";
		return info;
	}
	
}