/*
* FormalismAgent.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import madkit.kernel.Agent;
import madkit.kernel.Message;

//import gnu.mapping.Environment;

class FileExtension implements FilenameFilter {
    private String extension;

    public FileExtension(String ext) {
	this.extension = ext;
    }

    public boolean accept(File dir, String name) {
	return name.endsWith(extension);
    }
}

public class FormalismAgent extends Agent
{
    public final static String FORMALISM_FOLDER = "lib/formalisms";

    XMLFormalism  xf = new XMLFormalism();
  public Vector formalisms;

  public FormalismAgent()
  {
  	super();
    formalisms = new Vector();


    File cwd = new File(System.getProperty("madkit.dir"));
    File formdir = new File(cwd, FORMALISM_FOLDER);
    if (formdir!=null)
	getFromDirectory(formdir);
    //   updateFormalisms(new StateTransitionFormalism("StateTransition"));
    //     updateFormalisms(new DataFlowFormalism("Data Flow"));
    // updateFormalisms(new PetriFormalism("Petri"));
    //updateFormalisms(new PetriWithPortFormalism("Petri with ports"));
    //updateFormalisms(new SimpleClassFormalism("Object"));
    //updateFormalisms(new MobileFormalism("Mobile"));
    // updateFormalisms(new  StdBricFormalism("Bric"));
    // updateFormalisms(new  AalaadinFormalism(""));
    //   supprime car il est trop mauvais
    //    updateFormalisms(new AutomatonFormalism("Automaton"));
  }

    public void getFromDirectory(File directory)
    {
	Vector result = new Vector();
	String names[];
	names = directory.list(new FileExtension(".fml"));
	if (names != null)
	    for (int i=0; i<names.length; i++)
		{
		    //		    System.err.println("Loading:"+directory.getPath() + File.separatorChar +
				       //					      names[i]);

		    Formalism f = xf.parse(directory.getPath() + File.separatorChar +
					      names[i]);

	f.setBase(directory.getPath() + File.separatorChar);
	updateFormalisms(f);

		}
    }


  public void activate()
    {
      createGroup(false,"sedit",null,null);
      requestRole("sedit","formalism-server",null);
      println("Yip! the formalism-server is up & running");
    }

    protected void updateFormalisms(Formalism f)
    {
		formalisms.addElement(f);
		// SEditMessage fm = new SEditMessage("new",f.getName());
    }


  protected void handleMessage(SEditMessage m)
  {
    String req = m.getRequest();
    println("received message:"+req);
    if (req.equals("load"))
	{
		loadFormalism((String) m.getParameter());
	}
    if (req.equals("www-get"))
	{
		downloadFormalism((String) m.getParameter());
	}
    if (req.equals("get"))
	{
	    Formalism f = getFormalism((String)m.getParameter());
	    println("Looking for "+(String)m.getParameter());
	    SEditMessage fm = new SEditMessage("reply",f,m.getFileName());
	    sendMessage(m.getSender(), fm);
	}
    if (req.equals("list"))
	{
	    Vector v = new Vector();
	    for(int i=0;i<formalisms.size();i++){
		Formalism f = (Formalism) formalisms.elementAt(i);
		String[] param = new String[2];
		param[0]=f.getName();
		param[1]=f.getDescription();
		v.addElement(param);//f.getName());
	    }
	    SEditMessage fm = new SEditMessage("reply",v);
	    sendMessage(m.getSender(), fm);
	}

  }

    public  Formalism getFormalism(String _name){
    	for(int i=0;i<formalisms.size();i++){
	    	Formalism f = (Formalism) formalisms.elementAt(i);
	    	// System.err.println("Debug"+f.getName()+"/"+_name+"/"+f.getName().equals(_name)+"/"+f);

	    	if (f.getName().equals(_name))
			return(f);
		}
    	return(null);
    }


 void loadFormalism(String fileName){
 	 Formalism f;
 	 println(":: loading formalism : " + fileName);
	 f = xf.parse(fileName);
	 if (f!=null)
	     {
		 f.setBase(fileName);
		 updateFormalisms(f);
		 println(":: formalism : " + fileName + " loaded");
		 String[] param = new String[2];
		 param[0]=f.getName();
		 param[1]=f.getDescription();

		 broadcastMessage("sedit","control-center", new SEditMessage("update",param));//f.getName()));
 	 } else {
 	 	println(":: ERROR loading formalism : " + fileName);
 	 }
 }
 void downloadFormalism(String fileName){

	 try {
	     println(":: loading formalism : " + fileName);
	     URL u = new URL(fileName);
	     Formalism f;
	     f = xf.parse(fileName);
	     if (f!=null)
		 {
		     f.setBase(u);//fileName);
		     updateFormalisms(f);
		     println(":: formalism : " + fileName + " loaded");
		     String[] param = new String[2];
		     param[0]=f.getName();
		     param[1]=f.getDescription();

		 broadcastMessage("sedit","control-center", new SEditMessage("update",param));//f.getName()));
		 //		 broadcastMessage("sedit","control-center", new SEditMessage("update",f.getName()));
		 } else {
		     println(":: ERROR loading formalism : " + fileName);
		 }
	 }
	 catch (MalformedURLException e){
System.err.println(e);	 }

 }


  public void live()
    {
      while (true) {
	    println("Waiting for messages");
	    Message m = waitNextMessage();
	    if (m instanceof SEditMessage)
			handleMessage((SEditMessage)m);
	  }
    }
}











