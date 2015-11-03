/*
* AgentNode.java - the MadKit Desktop application
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
import madkit.TreeTools.*;
//import madkit.system.*;



public class AgentNode extends EditableFileNode {
    static AgentNodeDescriptor agentNodeDescriptor =
        new AgentNodeDescriptor("/images/agents/AgentIconColor16.gif",
            new String[][]{ // commands
                    {"launch","execute"},
                    {"edit with AgentEditor","edit"},
                    {"edit with JSynEdit","JSynEdit"},
					{"rename file", "rename"},
					{"delete file", "delete"},
                    {"properties", "info"}
                });


    
	public AgentNode(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
		super(ag, file, desc, iconSize, iconPanel);
		this.setDescriptor(agentNodeDescriptor);
	}
    
    public ImageIcon getLeafIcon(){ return agentNodeDescriptor.getImage();}

    public void buildAgent(String type, String className, String typeArg, Object arg){
		AgentLauncher al = AgentLauncher.makeAgentLauncher("madkit.kernel.ScriptAgentLauncher",
					AbstractMadkitBooter.getBooter(),type,className,null,typeArg,arg,true,null,null);
		al.launch();
    }

//    public void edit(){
//        String s=file.getPath();
//        System.out.println("Editing : " + s);
//        EditorAgent ed = new EditorAgent(s);
//        ag.launchAgent(ed,"Edit : " + s,true);
//    }
    


    public void jedit(){
 /*       String path = file.getPath();
        AgentAddress agJedit = ag.getAgentWithRole("system","jedit");
        if (agJedit == null){
            JEditAgent a = new JEditAgent(file);
            ag.launchAgent(a,"jEdit",false);
        } else
            ag.sendMessage(agJedit,new ControlMessage(JEditAgent.EDIT,file.getAbsolutePath()));
        System.out.println("jEdit launcher : " + agJedit);
        */
    }
}
