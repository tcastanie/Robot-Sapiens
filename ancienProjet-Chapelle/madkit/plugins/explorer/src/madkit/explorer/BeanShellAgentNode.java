/*
* BeanShellAgentNode.java - the MadKit Desktop application
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

import java.io.*;
import madkit.kernel.*;
import madkit.TreeTools.*;

public class BeanShellAgentNode extends AgentNode {
	
	static AgentNodeDescriptor beanShellAgentNodeDescriptor =
			   new AgentNodeDescriptor("/images/agents/agentBeanShell.gif",
				   new String[][]{ // commands
						   {"launch","execute"},
						   {"edit with AgentEditor","edit"},
						   {"edit with JSynEdit","JSynEdit"},
						   {"rename file", "rename"},
						   {"delete file", "delete"},
						   {"properties", "info"}
					   });


	public BeanShellAgentNode(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
		super(ag, file, desc, iconSize, iconPanel);
		this.setDescriptor(beanShellAgentNodeDescriptor);
	}

    public void execute(){
        buildAgent("BeanShellAgent","madkit.bsh.BeanShellAgent",
                    "java.lang.String",file.getAbsolutePath());
    }
}
