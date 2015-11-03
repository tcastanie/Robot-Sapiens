/*
* SchemeAgentNode.java - the MadKit Desktop application
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



public class SchemeAgentNode extends AgentNode {
    //public ImageIcon getLeafIcon(){ return agentNodeDescriptor.getIcon();}
	
	static AgentNodeDescriptor schemeAgentNodeDescriptor =
			   new AgentNodeDescriptor("/images/agents/schemeAgent.gif",
				   new String[][]{ // commands
						   {"launch","execute"},
						   {"edit with AgentEditor","edit"},
						   {"edit with JSynEdit","JSynEdit"},
						   {"rename file", "rename"},
						   {"delete file", "delete"},
						   {"properties", "info"}
					   });


		//public ImageIcon getLeafIcon(){ return agentNodeDescriptor.getIcon();}

	public SchemeAgentNode(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
		super(ag, file, desc, iconSize, iconPanel);
		this.setDescriptor(schemeAgentNodeDescriptor);
	}

    public void execute(){
        buildAgent("SchemeAgent","madkit.scheme.SchemeAgent","java.io.File",file);
      }
}
