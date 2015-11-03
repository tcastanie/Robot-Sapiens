/*
* JavaAgentNode.java - the MadKit Desktop application
* Copyright (C) 2000-2002  Jacques Ferber, Olivier Gutknecht
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package madkit.explorer;

import madkit.kernel.*;
import javax.swing.*;
import java.io.*;
//import SEdit.*;


public class JavaAgentNode extends FileIcon {
    String className="madkit.kernel.Agent";
    String smallClassName="Agent";
    
	static AgentNodeDescriptor agentNodeDescriptor =
		new AgentNodeDescriptor("/images/agents/AgentIconColor16.gif",
			new String[][]{ // commands
					{"launch","execute"},
				});

    public ImageIcon getLeafIcon(){ return AgentNode.agentNodeDescriptor.getImage();}

	public JavaAgentNode(AbstractAgent ag, int iconSize, IconPanel iconPanel,String _smallName, String _longName){
			super(ag, new File(_smallName), agentNodeDescriptor, iconSize, iconPanel);
			className = _longName;
			smallClassName = _smallName;
		}

    public void execute(){
        Object a=null;
        try {

            //MadkitClassLoader ucl = AbstractMadkitBooter.getBooter().getClassLoader();
            // Class cl = Class.forName(className);
            Class cl = Utils.loadClass(className);
            a= cl.newInstance();

            if ((a != null) && (a instanceof AbstractAgent)){
              String label = AbstractMadkitBooter.getAgentLabelFromClassName(a.getClass().getName());
              ag.launchAgent((AbstractAgent) a,label,true);
            }
        } catch (ClassNotFoundException ex){
              System.err.println("Agent class does not exist"+ ex.getMessage());
        } catch (Exception ccex){
              System.err.println("Agent launch exception:"+ccex);
              System.err.println(ccex.getMessage());
              ccex.printStackTrace();
        }

    }
}
