/*
 * Created on 27 nov. 2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.explorer;

import java.io.File;

import madkit.TreeTools.GenericIconDescriptor;
import madkit.kernel.AbstractAgent;

/**
 * @author ferber
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
import madkit.kernel.*;
import javax.swing.*;
//import java.io.*;
//import madkit.TreeTools.*;
import madkit.system.*;


public class HTMLFileNode extends EditableFileNode {

	/**
	 * @param ag
	 * @param file
	 * @param desc
	 * @param iconSize
	 * @param iconPanel
	 */
	public HTMLFileNode(
		AbstractAgent ag,
		File file,
		GenericIconDescriptor desc,
		int iconSize,
		IconPanel iconPanel) {
		super(ag, file, desc, iconSize, iconPanel);
		this.setDescriptor(HTMLFileNodeDescriptor);
		// TODO Auto-generated constructor stub
	}
	static AgentNodeDescriptor HTMLFileNodeDescriptor =
		new AgentNodeDescriptor("/images/html.png",
			new String[][]{ // commands
					{"browse","execute"},
					{"edit with AgentEditor","edit"},
					{"edit with JSynEdit","JSynEdit"},
					{"rename file", "rename"},
					{"delete file", "delete"},
					{"properties", "info"}
				});


    
//	public HTMLFileNode(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
//		super(ag, file, desc, iconSize, iconPanel);
//		this.setDescriptor(agentNodeDescriptor);
//	}
    
	public ImageIcon getLeafIcon(){ return HTMLFileNodeDescriptor.getImage();}
	
	public void execute(){
		String s=file.getPath();
		System.out.println("Browsing : " + s);
		Agent ed = new WebBrowserAgent("file:"+s);
		ag.launchAgent(ed,"WebBrowserAgent",true);
	}

}
