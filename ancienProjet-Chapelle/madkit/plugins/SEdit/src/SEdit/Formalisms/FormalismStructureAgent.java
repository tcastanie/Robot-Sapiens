/*
* FormalismStructureAgent.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit.Formalisms;

import java.io.*;
import SEdit.*;



public class FormalismStructureAgent extends StructureAgent
{

    XMLFormalism  xf;
    String fileFormalismName="agents.xml";
    boolean showElements=false;



    public FormalismStructureAgent(String s){
        super();
        fileFormalismName=s;
    }

    public FormalismStructureAgent(String s, boolean b){
        super();
        fileFormalismName=s;
        setShowElementPanel(b);
    }

    public FormalismStructureAgent(){
        super();
    }

	public void initGUI()
    {
    	File cwd = new File(System.getProperty("madkit.dir"));
    	File directory = new File(cwd, FormalismAgent.FORMALISM_FOLDER);

		// System.out.println("OK1");
		xf = new XMLFormalism();
		// System.out.println("OK2");
	 	formalism = xf.parse(directory.getPath() + File.separatorChar + fileFormalismName);
	 	formalism.setBase(directory.getPath() + File.separatorChar);
 	 	println(":: initializing formalism : " + fileFormalismName);
 	 	setShowElementPanel(false);
		initStructure();
		gui = new DefaultStructureBean(this);
        //gui = new MinimalStructureBean(this);
		setGUIObject(gui);
    }

}
