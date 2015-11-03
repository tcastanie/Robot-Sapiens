/*
* Warbot.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

import java.io.File;

import SEdit.DefaultStructureBean;
import SEdit.FormalismAgent;
import SEdit.StructureAgent;
import SEdit.XMLFormalism;


//import gnu.kawa.util.*;

/** WARBOT
  @version 2.1
  @author
  Fabien MICHEL et Jacques FERBER
  Warbot is the main class which is called directly by the launcher
  in order to make Warbot an independant application...

 */

public class Warbot extends StructureAgent {

    XMLFormalism  xf;

	public void initGUI()
    {
    	File cwd = new File(System.getProperty("madkit.dir"));
    	File directory = new File(cwd, FormalismAgent.FORMALISM_FOLDER);

		xf = new XMLFormalism();
		String fileFormalismName = "warbot3.fml";
	 	formalism = xf.parse(directory.getPath() + File.separatorChar + fileFormalismName);
	 	formalism.setBase(directory.getPath() + File.separatorChar);
 	 	println(":: initializing formalism : " + fileFormalismName);
 	 	setShowElementPanel(false);
		initStructure();
		gui = new DefaultStructureBean(this);
		setGUIObject(gui);
    }
}
