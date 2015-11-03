/*
* SEditAgent.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit;

import java.util.*;
import java.io.*;
import madkit.kernel.*;
import java.awt.AWTEvent;


public class SEditAgent extends Agent
{
    SEditGUI gui;

    public static String VERSION = "3.1";
    public static String DATE = "11/2002";
    static String AUTOLOAD_FOLDER = "autoload";

    Booter myBooter=null;

   // protected Hashtable formalisms = new Hashtable();

    String fileDir;
    String fileName;

    boolean insertMode=false; // indique si on doit lancer un structure agent
    						  // en lui passant les fileDir + fileName

    // protected SchemeModule scheme; // l'environnement specifique a l'agent lanceur...

  /* la derniere StructureFrame qui vient d'etre ouverte */
    public SEditAgent()
    {
		super();
		//setDebug(true);
		//scheme = new SchemeModule();
		//scheme.setCurrentAgent(this);
        myBooter=(Booter) Booter.getBooter();
    }

    public void activate()
    {
        //myBooter=Booter.getBooter();
		createGroup(false,"sedit",null,null);
		requestRole("sedit", "control-center");
		println("SEdit, Version "+VERSION+" launched");

		debug("Launching FormalismServer");
		launchAgent(new FormalismAgent(),"Formalizator",false);
		//pause(500);
		//launchAgent(new FormalismAgent(),"Formalizator",false);
		//pause(2000);
    }

    public void loadFormalism(String s)
    {
	sendMessage("sedit","formalism-server", new SEditMessage("load",s));
    }

    public void urlFormalism(String s)
    {
	sendMessage("sedit","formalism-server", new SEditMessage("www-get",s));
    }

    public Booter getBooter(){return myBooter;}

    public void initGUI()
    {
	gui = new SEditGUI(this);
	setGUIObject(gui);
    }

	public void doLaunchAgent(AbstractAgent ag, String label, boolean bean){
		launchAgent(ag,label,bean);
	}

    public void live()
    {
        while (this.getAgentsWithRole("sedit","formalism-server").length==0){
            pause(200);
        }
        sendMessage("sedit","formalism-server", new SEditMessage("list"));
        SEditMessage fm = (SEditMessage)waitNextMessage();

		Vector v = (Vector) fm.getParameter();
		// System.out.println("formalismes: " + v);
		for(int i=0;i<v.size();i++)  {
		    String[] param = (String[])(v.elementAt(i));
		    if (param[1].equals(""))
			  gui.addPreloaded(param[0],param[0]);
		    else
			  gui.addPreloaded(param[0],param[1]);
			gui.repaint();
	    }

		while(true){
			Message m =  waitNextMessage();
	      	if (m instanceof SEditMessage){
	      		handleMessage((SEditMessage) m);
	      	}
		}
	}



	protected void handleMessage(SEditMessage fm){
		StructureAgent sa;
		if (fm.getRequest() == "reply"){
		    Formalism f = (Formalism) fm.getParameter();
		    if (f != null){
			if (fileName != null){
			    sa = new StructureAgent(f,fileName);
			    launchAgent(sa,f.getName()+" - "+fileName,true);
			    fileName = null;
			} else {
			    sa = new StructureAgent(f);
			    launchAgent(sa,f.getName()+" - Untitled",true);
		      }
		    }
		   else
		   	 System.err.println("ERROR: no formalism found");
		} else if (fm.getRequest() == "update"){
		    String[] param = (String[])fm.getParameter();
		    if (param[1].equals(""))
			gui.updateFormalismList(param[0],param[0]);
		    else
			gui.updateFormalismList(param[0],param[1]);
		    gui.repaint();


		}
	}

  public void startEditor(String s)
  {
      sendMessage("sedit","formalism-server", new SEditMessage("get",s));
  }


   void openFile(String fn){
	  Object result;
	  if (fn != null){
		  try {
		    String f = XMLStructureLoader.parseFormalismName(fn);
		    if (f!=null){
			    fileName = fn;
			    sendMessage("sedit","formalism-server", new SEditMessage("get",f));
			}
		  }
		  catch(Exception e){
		    System.out.println("INTERNAL ERROR: "+e);
		  }
	    }
    }

    // create a model with a formalism name
   public void createModel(String s) {
    	insertMode = true;
      	sendMessage("sedit","formalism-server", new SEditMessage("get",s));
    }

    public void end(){
        // Maybe a bit hard isn't it?
        System.exit(0);
    }

    public void windowClosing(AWTEvent we){
        gui.quit();
    }

}














