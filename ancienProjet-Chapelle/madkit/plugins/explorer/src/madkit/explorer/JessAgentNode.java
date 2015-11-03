/*
* JessAgentNode.java - the MadKit Desktop application
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
import java.io.*;
import madkit.TreeTools.*;
//import SEdit.*;




public class JessAgentNode extends AgentNode {
	
	static AgentNodeDescriptor jessAgentNodeDescriptor =
		   new AgentNodeDescriptor("/images/agents/jessAgent.gif",
			   new String[][]{ // commands
					   {"launch","execute"},
					   {"edit with AgentEditor","edit"},
					   {"edit with JSynEdit","JSynEdit"},
					   {"rename file", "rename"},
					   {"delete file", "delete"},
					   {"properties", "info"}
				   });


    //public ImageIcon getLeafIcon(){ return agentNodeDescriptor.getIcon();}
	public JessAgentNode(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
		super(ag, file, desc, iconSize, iconPanel);
		this.setDescriptor(jessAgentNodeDescriptor);
	}

    public void execute(){
        try {
            Class cl = Utils.loadClass("jess.Rete");
            buildAgent("JessAgent","madkit.jess.JessAgent", "java.lang.String",file.getAbsolutePath());
        } catch (ClassNotFoundException ex){
              System.err.println("Jess has not been installed. Please download Jess 6.0");
              System.err.println("from http://herzberg.ca.sandia.gov/jess/");
              System.err.println("replace the old jess.jar in the libs/support/ directory");
              System.err.println("by the new jar that you have downloaded, and call it 'jess.jar'");
              System.err.println("Restart MadKit and that's it");
              System.err.println("See the user manual for more details");
              return;
        } catch (Exception ccex){
              System.err.println("Error in instanciating a JessAgent"+ccex);
              // ccex.printStackTrace();
        }
    }

}
