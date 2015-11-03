/*
* SchemeAgent.java - SchemeAgents : using Scheme Kawa in MadKit
* Copyright (C) 1998-2002  Olivier Gutknecht
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.scheme;


import gnu.expr.Interpreter;
import gnu.lists.LList;
import gnu.mapping.Environment;
import gnu.mapping.InPort;
import gnu.mapping.OutPort;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JToolBar;

import kawa.standard.Scheme;
import madkit.boot.Madkit;
import madkit.boot.MadkitClassLoader;
import madkit.kernel.AbstractAgent;
import madkit.kernel.AbstractMadkitBooter;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.OPanel;
import madkit.kernel.Utils;
import madkit.messages.ControlMessage;
import madkit.system.EditorAgent;
import madkit.utils.graphics.DefaultControlPanel;

class SchemeAgentGUI extends DefaultControlPanel implements ActionListener{

    OPanel outPanel;


    SchemeAgentGUI(SchemeAgent _ag){
        super(_ag);
        setLayout(new BorderLayout());
        JToolBar mytoolBar = new JToolBar();
        mytoolBar.addSeparator();
    //			/addTool(toolBar, "print", "demo/agents/system/print.gif");
        //addButton(mytoolBar, "run", "Run","/agents/jess/run.gif");
        //addButton(mytoolBar, "halt", "Halt", "/agents/jess/halt.gif");
        //addButton(mytoolBar, "reinit", "Re-init","/agents/jess/reinit.gif");
        addButton(mytoolBar, "notePad", "Edit script with NotePadAgent","/images/agents/agentEditor24.gif");
        addButton(mytoolBar, "jEdit", "Edit script with jEdit","/images/toolbars/jedit24.gif");

        add(mytoolBar,BorderLayout.NORTH);

        outPanel = new OPanel();
        add(outPanel,BorderLayout.CENTER);
    }

    OPanel getOutPanel(){
        return outPanel;
    }

    protected void jedit(){
        String path = ((SchemeAgent)ag).schemeSource;
        if (path == null){
            File f = ((SchemeAgent)ag).schemeFile;
            if (f == null)
                println("sorry no source file to edit");
            else {
                path = f.getPath();
                println("editing : " + path);
                AgentAddress agJedit = ag.getAgentWithRole("system","jedit");
                if (agJedit == null){
                	try {
		            	// $$ JF : to be fixed
		            	Class cag = Class.forName("JEditAgent");
		            	Agent a = (Agent) cag.newInstance();
		                // JEditAgent a = new JEditAgent(new File(path));
		                ag.launchAgent(a,"jEdit",false);
		                agJedit=ag.getAddress();
		                System.out.println("jEdit address: " + agJedit);
		            } catch(Exception e){
		            	System.err.println("Unable to launch JEditAgent: package jEdit may have not been correctly installed");
		            }
                //    JEditAgent a = new JEditAgent(f);
                //    ag.launchAgent(a,"jEdit",false);
                //    agJedit=ag.getAddress();
                //    System.out.println("jEdit address: " + agJedit);
                } else
                    ag.sendMessage(agJedit,new ControlMessage("edit",f.getAbsolutePath()));
                System.out.println("jEdit launcher : " + agJedit);
            }
        }
    }

    public void command(String c){

 		if (c.equals("evalBuffer")) evalBuffer();
 		else if (c.equals("evalSelection")) evalSelection();
   		else if(c.equals("run")){
   		} else if(c.equals("halt")){
            // halt();
   		} else if(c.equals("reinit")){
   		} else if(c.equals("jEdit")){
            jedit();
        } else if (c.equals("notePad")){
            String s = ((SchemeAgent)ag).schemeSource;
            if (s == null){
                File f = ((SchemeAgent)ag).schemeFile;
                if (f == null){
                    println("sorry no source file to edit");
                } else {
                    s = f.getPath();
                    println("editing : " + s);
                    EditorAgent ed = new EditorAgent(s);
                    ag.launchAgent(ed,"Edit : " + s,true);
                }
            } else {
                println("editing : " + s);
                EditorAgent ed = new EditorAgent(s);
                ag.launchAgent(ed,"Edit : " + s,true);
            }
        }
       else super.command(c);
 	}

    void evalBuffer() {
   }

   void evalSelection() {
   }

   public void println(String s){
        ag.println(s);
   }


}

/** A wrapper class for agents coded in Scheme with Kawa */
public class SchemeAgent extends Agent
{
    String schemeSource = null;
    File schemeFile = null;

    SchemeAgentGUI gui;

    protected Environment env;
    public static Scheme interp = null;
    public static int envcount;

    protected OutPort out_p, err_p;

 /** The constructor instantiates a new Scheme interpreter with a
      local environment and load the SchemeAgentLib glue */
  public SchemeAgent()
  {
      if (Interpreter.defaultInterpreter == null)
	  {
	      Interpreter.defaultInterpreter = new Scheme();
	      Environment.setCurrent(Interpreter.defaultInterpreter.getEnvironment());
	  }
      if (interp==null)
	  interp = new Scheme();

      envcount++;
      env = interp.getNewEnvironment();
      //System.err.println("Instanciating SchemeAgent " + envcount + this + env + interp);

      Object x = interp.eval("(define %scheme-agent-count% "+envcount+")",env);
      env.define("%current-agent%", this);
      env.define("self", this);


      StringBuffer libsource = new StringBuffer();
      AbstractMadkitBooter booter = AbstractMadkitBooter.getBooter();
      MadkitClassLoader ucl = null;
      if (booter != null)
        ucl = Madkit.getClassLoader();
      try
	  {
	      String rsrc="/madkit/scheme/SchemeAgentLib.scm";
          InputStream defs=null;

          /* if (ucl != null)
            defs = ucl.loadClass("madkit.scheme.SchemeAgent").getResourceAsStream(rsrc);
          else
	        defs = Class.forName("madkit.scheme.SchemeAgent").getResourceAsStream(rsrc); */
	        
	       defs = getClass().getResourceAsStream(rsrc);

	      BufferedReader dip = new BufferedReader (new InputStreamReader(defs));
	      String s=null;

	      while((s=dip.readLine()) != null)
		  {
		      libsource.append('\n');
		      libsource.append(s);
		  }
	  }
      catch (Exception eofe)
	  {
	      System.err.println("Load:"+eofe.getMessage());
	      eofe.printStackTrace();
	  }

      interp.eval(libsource.toString(),env);
  }

  /** The constructor instantiates a new Scheme interpreter with a
      local environment and load the SchemeAgentLib glue */
  public SchemeAgent(String s)
  {
    this();
    schemeSource = s;
  }

  public SchemeAgent(File s)
  {
    this();
    schemeFile = s;
  }

  public void initGUI() {
    if (env.lookup("initGUI") != null){
        try {
	        interp.eval("(initGUI)",env);
        }
        catch(Exception e){
            System.err.println("Error in the (initGUI) function : "+ e);
            e.printStackTrace();
        }
        catch (Throwable ex){
            System.err.println("Error in evaluating (initGUI): "+ ex);
        }
    }
    else {
        gui = new SchemeAgentGUI(this);
        setGUIObject(gui);

        setOutputWriter(gui.getOutPanel().getOut());
        out_p = new OutPort(gui.getOutPanel().getOut(),true,"<msg_stdout>");
        // err_p = new OutPort(gui.getOutPanel().getErr(),true,"<msg_stderr>");
    }
  }


  /** Loads the Scheme agent code, then evaluates the activate function, if bounded */
  public void activate() {
        try {
            if (schemeSource!=null)
                interp.eval("(load \""+schemeSource+"\")",env);
            else
                if (schemeFile!=null){
                    kawa.standard.load.loadSource(new InPort(new FileReader(schemeFile)),env);
                }
        }
        catch (Exception e)
        {
            System.err.println("Error: Can't load Scheme File "+e);
            e.printStackTrace();
        }
        catch (Throwable ex){
            System.err.println("Well, I cannot definitively load Scheme file:"+ ex);
        }

        if (env.lookup("activate") != null)
            try {
                interp.eval("(activate)",env);
            }
            catch(Exception e){
                System.err.println("Error in the (activate) function : "+ e);
                e.printStackTrace();
            }
            catch (Throwable ex){
                System.err.println("Error in evaluating (activate): "+ ex);
            }
    }

  /** Evaluates the live function, if bounded */
  public void live() {
	if (env.lookup("live") != null)
        try {
	        interp.eval("(live)",env);
        }
        catch(Exception e){
            System.err.println("Error in the (live) function : "+ e);
            e.printStackTrace();
        }
        catch (Throwable ex){
            System.err.println("Error in evaluating (live): "+ ex);
        }
    }

  /** Evaluates the end function, if bounded */
  public void end()
  {
      if (env.lookup("end") != null)
        try {
	        interp.eval("(end)",env);
        }
        catch(Exception e){
            System.err.println("Error in the (end) function : "+ e);
            e.printStackTrace();
        }
        catch (Throwable ex){
            System.err.println("Error in evaluating (end): "+ ex);
        }
  }


  public void println(String s){
    super.println(s);
  }

   public void println(Object o){
        interp.print(o,out_p,false);
   }

   public void print(Object o){
        interp.print(o,out_p);
   }

  /** Java-Scheme glue code */
  public void makeLaunchAgent(String cl, String n)
  {
    try
    {
        Class c = Utils.loadClass(cl);
        Agent a = (Agent) c.newInstance();
        launchAgent(a,n, true);
      }
    catch (Exception e)
      {
	println("launch-agent exception:"+e);
   }
  }

  // FIX IT: old glue code.. Is it still necessary?
  public void doLaunchAgent(AbstractAgent a, String n, boolean gui)
  {
    launchAgent(a,n, gui);
  }


  /** Java-Scheme glue code : getAgentsWithRole */
  public LList schemeGetAgentsWithRole(String g, String r){return LList.makeList(getAgentsWithRole(g,r),0);}
  public LList schemeGetAgentsWithRole(String c, String g, String r){return LList.makeList(getAgentsWithRole(c,g,r),0);}

  /** Java-Scheme glue code : getMyGroups*/
  public LList schemeGetMyGroups() {return LList.makeList(getMyGroups(),0);}
  public LList schemeGetMyGroups(String c){return LList.makeList(getMyGroups(c),0);}

  /** Java-Scheme glue code : getExistingGroups*/
  public LList schemeGetExistingGroups() {return LList.makeList(getExistingGroups(),0);}
  public LList schemeGetExistingGroups(String c){return LList.makeList(getExistingGroups(c),0);}

    /** Java-Scheme glue code : getRoles*/
  public LList schemeGetRoles(String g) {return LList.makeList(getExistingRoles(g),0);}
  public LList schemeGetRoles(String c, String g){return LList.makeList(getExistingRoles(c,g),0);}
    /** Java-Scheme glue code : getRoles*/
  public LList schemeGetExistingRoles(String g) {return LList.makeList(getExistingRoles(g),0);}
  public LList schemeGetExistingRoles(String c, String g){return LList.makeList(getExistingRoles(c,g),0);}
    /** Java-Scheme glue code : getRoles*/
  public LList schemeGetMyRoles(String g) {return LList.makeList(getMyRoles(g),0);}
  public LList schemeGetMyRoles(String c, String g){return LList.makeList(getMyRoles(c,g),0);}

  /** Java-Scheme glue code : getAvailableCommunities*/
  public LList schemeGetAvailableCommunities() {return LList.makeList(getAvailableCommunities(),0);}



  public void InternalDebug(String g)
  {
      debug(getAddress()+" "+g);
  }
}















