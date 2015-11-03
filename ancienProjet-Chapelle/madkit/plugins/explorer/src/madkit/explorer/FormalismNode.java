/*
* FormalismNode.java - the MadKit Desktop application
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
import SEdit.*;

public class FormalismNode extends FileIcon {
    static AgentNodeDescriptor formalismNodeDescriptor =
            new AgentNodeDescriptor("/sedit/FormalismIconColor16.gif");

    File cwd = new File(System.getProperty("madkit.dir"));
     // Comment out: these lines are about SEdit
    File formdir = new File(cwd,FormalismAgent.FORMALISM_FOLDER);

    public ImageIcon getLeafIcon(){ return formalismNodeDescriptor.getImage();}
//   public FormalismNode(File f, AbstractAgent _ag){
//        super(f);
//        ag = _ag;
//    }
 
   
	public FormalismNode(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
		super(ag, file, formalismNodeDescriptor, iconSize, iconPanel);
	}

//  void createStructure(String fileName){
// 	 Formalism f;
//         XMLFormalism  xf = new XMLFormalism();
// 	 System.out.println(">> loading formalism : " + fileName);
//	 f = xf.parse(fileName);
//         f.setBase(formdir.getPath()+File.separator);
//	 if (f!=null) {
//            StructureAgent sa=new StructureAgent(f);
//            ag.launchAgent(sa,f.getName()+" - Untitled",true);
// 	 } else {
// 	 	System.err.println(":: ERROR loading formalism : " + fileName);
// 	 }
//    }
  
	void createStructure(String fileName){
			try {
				Class cl = madkit.kernel.Utils.loadClass("SEdit.SEditMessage");

				File cwd = new File(System.getProperty("madkit.dir"));
				File formdir = new File(cwd, FormalismAgent.FORMALISM_FOLDER);
				Formalism f;
				XMLFormalism  xf = new XMLFormalism();
				System.out.println(">> loading formalism : " + fileName);
				f = xf.parse(fileName);
				// System.out.println("setbase: " + f.getBase());
				if (f!=null) {
					f.setBase(formdir.getPath()+File.separator);
					StructureAgent sa=new StructureAgent(f);
					ag.launchAgent(sa,f.getName()+" - Untitled",true);
				} else {
					System.err.println(":: ERROR loading formalism : " + fileName);
				}
			}
			catch (ClassNotFoundException ex) {
				System.err.println(
				"SEdit is not properly installed, please install the SEdit plugin");
			}
		}

	public void execute(){
            createStructure(file.getPath());
	}
}
