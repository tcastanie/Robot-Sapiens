/*
* SEditFileNode.java - the MadKit Desktop application
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
import SEdit.*;
import madkit.TreeTools.*;


public class SEditFileNode extends EditableFileNode {

    static AgentNodeDescriptor formalismNodeDescriptor =
            new AgentNodeDescriptor("/sedit/SEditFileIconColor16.gif",
               new String[][]{ // commands
                    {"launch","execute"},
                    {"edit with NotePadAgent","edit"},
                    {"edit with JSynEdit","JSynEdit"},
                    {"rename file", "rename"},
                    {"delete file", "delete"},
                    {"properties","info"}
                });

   public ImageIcon getLeafIcon(){ return formalismNodeDescriptor.getImage();}
   
   public SEditFileNode(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
	   super(ag, file, desc, iconSize, iconPanel);
		this.setDescriptor(formalismNodeDescriptor);
   }

   public void execute(){
		   try {
		   Class cl = madkit.kernel.Utils.loadClass("SEdit.SEditMessage");
		   String form = XMLStructureLoader.parseFormalismName(file.getPath());

		   AgentAddress fs = ag.getAgentWithRole("public","sedit","formalism-server");
		   if (fs == null){
			   Agent formServer = new FormalismAgent();
			   ag.launchAgent(formServer,"Formalizator",false);
			   fs = formServer.getAddress();
		   }
		   Thread.sleep(500); // To wait that the agent is coming, beurkkk, should define 'future' instead..
		   ag.sendMessage(fs, new SEditMessage("get", form, file.getPath()));
		   }
		   catch (ClassNotFoundException ex) {
			   System.err.println(
			   "SEdit is not properly installed, please install the SEdit plugin");
		   }
		   catch (Exception ex) {
			   System.err.println(
				   ":: ERROR invalid SEditFile (check its path and its content) : " +
				   file);
		   }
	   }

//	public void execute(){ 
//		try {
//			Class cl = Class.forName("SEdit.SEditMessage");
//            String form = XMLStructureLoader.parseFormalismName(file.getPath());
//            ((SEditAgent) ag).startEditor(form,file.getPath());
//		} catch (Exception ex) {
//			System.err.println(
//			"SEdit is not properly installed, please install the SEdit plugin");
//		}
//	}
}
