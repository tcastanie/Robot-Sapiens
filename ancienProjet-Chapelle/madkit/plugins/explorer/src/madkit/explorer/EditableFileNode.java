/*
* EditableFileNode.java - A Generic file which may be edited
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
import madkit.system.EditorAgent;
import madkit.messages.ControlMessage;
//import javax.swing.*;
//import madkit.system.JEditAgent;

public class EditableFileNode extends FileIcon {
//    AbstractAgent ag;
//    public EditableFileNode(File f, AbstractAgent _ag){
//        super(f);
//        ag = _ag;
//    }
    
	public EditableFileNode(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
		super(ag, file, new GenericIconDescriptor(),
				iconSize, iconPanel);
		this.setDescriptor(editableFileNodeDescriptor);
	}

	static GenericIconDescriptor editableFileNodeDescriptor = 
		new FileIconDescriptor(
			new String[][]{ // commands
				{"txt","/images/text.png"},
				{"xml","/images/html.png"},
				{"java","/images/source_java.png"},
			},
			new String[][]{ // commands
						{"edit with AgentEditor","edit"},
						{"edit with JSynEdit","JSynEdit"},
				//		{"rename file", "rename"},
				//		{"delete file", "delete"},
						{"properties", "info"}
			});
	
    public void edit(){
        String s=this.getFile().getAbsolutePath();
        System.out.println("Editing : " + s);
        EditorAgent ed = new EditorAgent(s);
        ag.launchAgent(ed,"Edit : " + s,true);
    }
    
    public void execute(){
    	edit();
    }

    public void JSynEdit(){
		String s=this.getFile().getAbsolutePath();
		System.out.println("Editing : " + s);
		Agent ed;
		AgentAddress edagent=ag.getAgentWithRole("system","editor");
		System.out.println("Editor: "+edagent);
		if (edagent != null){
			System.out.println("Sending message to "+edagent);
			ag.sendMessage(edagent,new ControlMessage("edit",file.getAbsolutePath()));
		} else 
			try {
				ed = new jsynedit.JSynEditAgent(s);
				ag.launchAgent(ed,"Edit : " + s,true);
			}catch(Exception e){
				System.err.println("Warning: JSynEdit has not been implemented");
			}
    }
    
    public void jedit(){
//        String path = file.getPath();
//        AgentAddress agJedit = ag.getAgentWithRole("system","jedit");
//        if (agJedit == null){
//            JEditAgent a = new JEditAgent(file);
//            ag.launchAgent(a,"jEdit",false);
//        } else
//            ag.sendMessage(agJedit,new ControlMessage(JEditAgent.EDIT,file.getAbsolutePath()));
//        System.out.println("jEdit launcher : " + agJedit);
    	System.out.println("Warning: for the moment jEdit has not been re-implemented into MadKit");
    }

}