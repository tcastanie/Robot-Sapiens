/*
* DesktopAgent.java - the MadKit Desktop application
* Copyright (C) 2000-2002  David Pujol, Jacques Ferber
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

package madkit.desktop2;

import java.awt.*;

import madkit.kernel.*;
import java.lang.reflect.*;


/**
 * DesktopAgent: an agent which is able to explore a
 * directory and launch agents of different styles..
 * Replace the old GBox and the previous DesktopAgent version 1.xx
 *
 * Needs SEdit for its compilation, but does not need SEdit at runtime to work properly
 * @author D. Pujol, J. Ferber
 * @version 1.2
 */

public class DesktopAgent extends Agent{
	protected DesktopAgentGUI desktop;

	public static String VERSION="2.1";
	
	public void initGUI(){
		desktop = new DesktopAgentGUI(this);
		setGUIObject(desktop);
		//DesktopBooter.setDesktopAgent(this);
	}
	
	public void activate(){
		DesktopBooter.setDesktopAgent(this);
		desktop.init();

		createGroup(false,"system",null,null);
		requestRole("system","desktop",null);

	}
	
	public void live(){
		while(true){
			Message m = waitNextMessage();
			handleMessage(m);
		}
	}
	
	
	protected void handleMessage(Message ms){
		 if (ms.getClass().getName().equals("SEdit.SEditMessage")){
		 	try {
		 		Class c = Utils.loadClass("SEdit.SEditTools");
		 		Class[] args=new Class[2];
		 		args[0] = Utils.loadClass("madkit.kernel.AbstractAgent");
		 		args[1] = Utils.loadClass("madkit.kernel.Message");
		 		Method meth=c.getMethod("createStructure",args);
		 		meth.invoke(c,new Object[]{this,ms});
		 	} catch(ClassNotFoundException ex){
		 		System.err.println("Error: SEdit not installed");
		 	} catch(NoSuchMethodException ex){
				System.err.println("Internal error with SEdit "+ex);
		 	} catch(InvocationTargetException ex){
			 System.err.println("Internal error with SEdit "+ex);
		 	}catch(IllegalAccessException ex){
			System.err.println("Internal error with SEdit "+ex);
		   }
		 }
		 else if (ms instanceof StringMessage){
		   handleMessage((StringMessage) ms);
		 }
	   }
	
	protected void handleMessage(StringMessage str){
	}
	
	
	
//	Caution: needs SEdit to be compiled !!
//	 protected void handleMessage(SEditMessage fm){
//	 	   System.out.println("receiving SEdit Message:"+fm.getRequest());
//		   StructureAgent sa;
//		   String fileName;
//		   if (fm.getRequest() == "reply"){
//			   Formalism f = (Formalism) fm.getParameter();
//			   fileName = fm.getFileName();
//			   if (f != null){
//				   if (fileName != null){
//					   sa = new StructureAgent(f,fileName);
//					   launchAgent(sa,f.getName()+" - "+fileName,true);
//					   fileName = null;
//				   } else {
//					   sa = new StructureAgent(f);
//					   launchAgent(sa,f.getName()+" - Untitled",true);
//				 }
//			   }
//			  else
//					System.err.println("ERROR: no formalism found");
//		   }
//	 }
	
//	public void launchAgent(AbstractAgent a, String label, boolean t){
//		super.launchAgent(a,label,t);
//	}
	
	protected void addAgent(AbstractAgent ag, Component c, Point p, Dimension d){
		desktop.addAgent(ag, c, p, d);
	}
	
/*	protected void addDefaultGUI(AbstractAgent ag){
		//créer un component standart;
		//addAgent(ag, c);
	}*/
	
	protected void removeGUI(Component c){
		desktop.removeGUI(c);
	}
	
	public void windowClosing(AWTEvent we){
		if (desktop != null)
			desktop.quitMadkit();
		else
			System.exit(0);
	}
}
