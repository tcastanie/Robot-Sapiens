/*
* AbstractMadkitBooter.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
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
package madkit.kernel;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.WindowConstants;


import madkit.boot.Madkit;
import madkit.boot.MadkitClassLoader;

import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

/** The standard madkit booter.

    It starts the kernel, and then launch agents defined in a config
    file, with or without GUI.

  @version 1.1
  @author Jacques Ferber, Olivier Gutknecht and Fabien Michel */

public class AbstractMadkitBooter implements GraphicShell
{

// static elements
    protected static int port;
    protected static boolean graphics = false;
    protected static boolean ipnumeric = false;
    protected static boolean nativelf = true;
    protected static String initFile = null;
    protected static String ipaddress = null;
    protected static boolean network = false;
    
  
  
  public static String CONFIG_FILE="cfg";
  public static String SCHEME_FILE="scm";
  public static String PYTHON_FILE="py";
  public static String BEANSHELL_FILE="bsh";
  public static String JESS_FILE="clp";
  public static String SEDIT_FILE="sed";
  public static String FORMALISM_FILE="fml";

  public static String ALL_FILES="cfg,scm,py,bsh,clp,sed,fml";


  static AbstractMadkitBooter booterInstance=null;

  public static void setBooter(AbstractMadkitBooter boot){
	  booterInstance = boot;
  }

  public static AbstractMadkitBooter getBooter(){
	  return booterInstance;
  }

  //
  protected Kernel theKernel;
  protected boolean isGraphics = false;
  protected Hashtable guis;


  protected String initialAgentClass=null;
  protected String initialAgentName=null;


  private SplashScreen starter;
  protected void setSplashScreen(SplashScreen splash){
    starter = splash;
  }
  
  protected boolean allowForPluginAgentification=true; //Default is that plugins are represented as agents
  

  protected SplashScreen getSplashScreen(){return starter;}
  public void setProgress(String s){
	if (starter != null)
	    starter.setText(s);
 }

    public static Hashtable labeltable=new Hashtable();

  /**
   * Gets a new agent label from the agentLabel table...
   */
  public static String getAgentLabel(String lab){
    if (!labeltable.containsKey(lab)){
        labeltable.put(lab, new Integer(2));
        return(lab);
    } else {
        int count = ((Integer)(labeltable.get(lab))).intValue();
        String label = lab+"-"+count++;
        labeltable.put(lab, new Integer(count));
        return(label);
    }
  }

  /**
   * Gets a new agent label from its class, using the
   * function getAgentLabel. Useful to create agents
   * with new names.
   */
  public static String getAgentLabelFromClassName(String s){
        int k = s.lastIndexOf('.');
        if (k != -1){
            return getAgentLabel(s.substring(k+1));
        }
        else return getAgentLabel(s);
  }



  /**
   * Create a ScriptAgent (i.e. Jess, Scheme, Python, etc..) from its className..
   * following the MadkitClassLoader... (so you don't have to take care of what
   * classes are in the initial CLASSPATH
   * Uses the Class.forName(..) instantiation if there is no ClassLoader associated
   * to the booter...
   * Exemple: makeScriptAgent(myLauncherAgent,"madkit.models.python.PythonAgent","java.lang.String",filePath,true)
   */
  public AbstractAgent makeScriptAgent(AbstractAgent ag, String className, String typeArg, Object arg, boolean gui){
        Object a=null;
        try {
            Class cl = null;
            cl = Utils.loadClass(className);
            Class[] cp0 = new Class[1];
            Object[] co0 = new Object[1];

            //cp0[0]=Class.forName(typeArg);
            cp0[0] = Utils.loadClass(typeArg);

            co0[0]=arg;

            Constructor s = cl.getConstructor(cp0);
            a= s.newInstance(co0);
            //System.out.println(":: a class: " + a.getClass());

        } catch (ClassNotFoundException ex){
              System.err.println("Agent class does not exist"+ ex.getMessage());
        } catch (Exception ccex){
              System.err.println("Agent launch exception:"+ccex);
              // ccex.printStackTrace();
        }
         if ((a != null) && (a instanceof AbstractAgent)){
              String label = getAgentLabelFromClassName(a.getClass().getName());
              ag.launchAgent((AbstractAgent) a,label,gui);
              return (AbstractAgent) a;
         }
         return null;
  }

  public AbstractAgent makeJavaAgent(AbstractAgent ag, String className, boolean gui){
        AbstractAgent a=null;
        try {
            MadkitClassLoader ucl = Madkit.getClassLoader();
            Class cl = null;
            cl = Utils.loadClass(className);
            a= (AbstractAgent) cl.newInstance();

            if ((a != null) && (a instanceof AbstractAgent)){
              String label = getAgentLabelFromClassName(a.getClass().getName());
              ag.launchAgent((AbstractAgent) a,label,gui);
            }
        } catch (ClassNotFoundException ex){
              System.err.println("Agent class does not exist"+ ex.getMessage());
        } catch (Exception ccex){
              System.err.println("Agent launch exception:"+ccex);
              // ccex.printStackTrace();
        }
        return a;
    }

  protected AbstractMadkitBooter(boolean isg, boolean ipnumeric, String initFile, String ipaddress, boolean network)
  {
      //System.out.println("booting with graphics="+isg+", ipnumeric="+ipnumeric+
      //                  ", initFile="+initFile+", ipaddress="+ipaddress+", network="+network);
        this.init(isg,ipnumeric,initFile,ipaddress,network);
  }

  /**
   * Create the plugins agents which represent the different plugins
   *
   */
  void createPluginAgents(){
	  	Class c=null;
	  	try {
	  		c = Madkit.getClassLoader().loadClass("madkit.system.PluginAgent");
	  	}
		catch (ClassNotFoundException e){
			System.err.println("Warning: no agent plugins");
			return;
		}  
  		File pluginsDir = new File(System.getProperty("madkit.dir")+File.separator+"plugins");
		if ((pluginsDir != null) && (pluginsDir.isDirectory())){
			File[] plugins = pluginsDir.listFiles();
			//System.out.println("Class LocalTree, Method buildDirectoryTree : "+file.getName());
			for (int i=0;i<plugins.length;i++) {
				if (plugins[i].isDirectory()){
					try {
						Agent a = (Agent) c.newInstance();
						Class[] par = new Class[1];
						par[0]=Class.forName("java.io.File");
						Method m = a.getClass().getMethod("init",par);
						Method pluginName = a.getClass().getMethod("getPluginName",new Class[0]);
						if (m != null){
							m.invoke(a,new Object[]{plugins[i]});
							this.launchAgent(a,"plugin-"+pluginName.invoke(a,new Object[0]),false,null,null);
						}
					}
					catch(Exception e){}
				}
			}
		}
  }
/**
 * Initialization method of the boot process. Normally called as 'super' from
 * subclasses that overrid this method.
 * @param isg
 * @param ipnumeric
 * @param initFile
 * @param ipaddress
 * @param network
 */
 protected void init(boolean isg, boolean ipnumeric, String initFile, 
 					String ipaddress, boolean network){
        setBooter(this);

	  setProgress("Starting micro-kernel...");
      isGraphics = isg;
      if (ipaddress == null)
	  theKernel = new Kernel("Kernel",ipnumeric);
      else
	  theKernel = new Kernel("Kernel",true, ipaddress);
      // new graphic shell architecture
      if (isGraphics)
	  {
	      guis = new Hashtable();
	      theKernel.registerGUI(this);
	  }
      
   	  setProgress("Initializing main class");
      if (initialAgentClass != null){
        launchAgent(initialAgentClass,initialAgentName);
      }
      try
      {
      		Thread.currentThread().sleep(200);
      }
      catch(Exception e)
      {
      	System.err.println("can't make a pause "+e);
	}
      if (allowForPluginAgentification) 
      		createPluginAgents();
      if(initFile != null){
 	     // readScriptFile(new File(initFile));
 	     loadConfigFile(new File(initFile));
      }
      // initialize Python
      try {
      	Class cPython = Utils.loadClass("madkit.python.PythonController");
      	if (cPython != null){
      		cPython = Utils.loadClass("org.python.core.PySystemState");
      		if (cPython != null){
      			setProgress("initializing Python.. It can take a minute the first time");
      			Method meth = cPython.getMethod("initialize",new Class[0]);
      			meth.invoke(cPython,new Object[0]);
      		}
      	}
      	
      } catch(ClassNotFoundException e){}
      catch(Exception e){
      	System.out.println("Error in initializing Python: "+ e.getMessage());
      //	e.printStackTrace();
      }

      if(network){
	        setProgress("initializing network...");
            Agent comm = null;
            try {
                Class c = Madkit.getClassLoader().loadClass("madkit.communicator.DynamicTwoChannelsCommunicator");
                comm = (Agent) c.newInstance();
            }
            catch (Exception e){
                System.err.println("ERROR: Communicator agent not found");
                return;
            }
            // comm = new agents.network.communicator.DynamicTwoChannelsCommunicator();
      		theKernel.launchAgent(comm, "Communicator "+Kernel.getAddress(),this,graphics);
      }
   }



    public Object getDefaultGUIObject(AbstractAgent a){
		OPanel o = new OPanel();
		a.setGUIObject(o);
		a.setOutputWriter(o.getOut());
		return o;
    }

    // implements the GraphicShell interface
    public void setupGUI(AbstractAgent a){

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    	setupGUI(a,new Point(-1,-1),new Dimension(-1,-1));
    }
    
    
	public void setupGUI(AbstractAgent a, Point p, Dimension d){
		JFrame f = new JFrame(a.getName());
		a.initGUI();

		if (a.getGUIObject() != null)
			f.getContentPane().add("Center",(Component)a.getGUIObject());
		f.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		f.addWindowListener(createWindowHandler(theKernel,a));
		if ((d.width < 0) || (d.height < 0))
			f.pack();
		else
			f.setSize(d);
		if ((p.x < 0) || (p.y < 0)){
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			f.setLocation((int)(screenSize.width/2 - f.getSize().getWidth()/2 + (int) (Math.random()*500)-250),
						(int)(screenSize.height/2 - f.getSize().getHeight()/2) + (int) (Math.random()*500)-250);
		} else {
			f.setLocation(p.x,p.y);
		}
		f.show();
		guis.put(a, f);
	}

    protected WindowListener createWindowHandler(Kernel k,AbstractAgent a){
        return new AgentWindowHandler(k,this,a);
    }


    public void disposeGUI(AbstractAgent a)
    {
	Object f = guis.get(a);

	if ((f!=null) && (f instanceof Frame)){
		((Frame)f).dispose();
        guis.remove(a);
	    }
    }

    public void disposeGUIImmediatly(AbstractAgent a){
        disposeGUI(a); // default...
    }

    
	void launchAgent(AbstractAgent a, String name){
		theKernel.launchAgent(a,name,this,graphics);
	}

    void launchAgent(AbstractAgent a, String name, boolean gui, Point p, Dimension d){
    	if (p == null) p = new Point(-1,-1);
		if (d == null) d = new Dimension(-1,-1);
        theKernel.launchAgent(a,name,this,gui,p,d);
    }

    void launchAgent(String ag, String name){
        try {
            //Class agentclass = Class.forName(ag);
            Class agentclass = Madkit.getClassLoader().loadClass(ag);
            AbstractAgent a = null;
            a = (AbstractAgent)(agentclass.newInstance());
            launchAgent(a,name);
        }
        catch (ClassNotFoundException ex){
            System.err.println("Agent class does not exist");
        }
        catch (Exception ccex) {
            System.err.println("Agent launch exception:"+ccex);
            ccex.printStackTrace();
        }
    }
    
	/**
	 * Launch an agent from a XML element
	 */
	public void launchAgent(Element elt){
		AgentLauncher al=null;
		boolean gui=true;
		int x=-1,y=-1,h=-1,w=-1;
		String guiAttr = elt.getAttribute("gui");
		if (guiAttr != null){
			if (guiAttr.equalsIgnoreCase("true"))
				gui=true;
			else if (guiAttr.equalsIgnoreCase("false"))
				gui=false;
		}
		String descX = elt.getAttribute("X");
		String descY = elt.getAttribute("Y");
		String descH = elt.getAttribute("height");
		String descW = elt.getAttribute("width");
		try { x = Integer.parseInt(descX);} catch(NumberFormatException ex){}
		try { y = Integer.parseInt(descY);} catch(NumberFormatException ex){}
		try { h = Integer.parseInt(descH);} catch(NumberFormatException ex){}
		try { w = Integer.parseInt(descW);} catch(NumberFormatException ex){}
		Point p = new Point(x,y);
		Dimension d = new Dimension(w,h);
	
		//System.out.println("process agent "+elt.getAttribute("name")+" "+elt.getAttribute("gui"));
		if (elt.getAttribute("type").equalsIgnoreCase("Java")){
			al = AgentLauncher.makeAgentLauncher("madkit.kernel.JavaAgentLauncher",
			this,null,elt.getAttribute("code"),elt.getAttribute("name"),null,null,gui,p,d);
		} else if (elt.getAttribute("type").equalsIgnoreCase("Scheme")){
			al = AgentLauncher.makeAgentLauncher("madkit.kernel.ScriptAgentLauncher",
			this,"SchemeAgent","madkit.scheme.SchemeAgent",elt.getAttribute("name"),
			"java.io.File",new File(elt.getAttribute("code")),gui,p,d);
		} else if (elt.getAttribute("type").equalsIgnoreCase("Jess")){
			al = AgentLauncher.makeAgentLauncher("madkit.kernel.ScriptAgentLauncher",
			this,"JessAgent","madkit.jess.JessAgent",elt.getAttribute("name"),
			"java.lang.String",elt.getAttribute("code"),gui,p,d);
		} else if (elt.getAttribute("type").equalsIgnoreCase("Python")){
			al = AgentLauncher.makeAgentLauncher("madkit.kernel.ScriptAgentLauncher",
			this,"PythonAgent","madkit.python.PythonAgent",elt.getAttribute("name"),
			"java.lang.String",elt.getAttribute("code"),gui,p,d);
		} else if (elt.getAttribute("type").equalsIgnoreCase("BeanShell")){
			al = AgentLauncher.makeAgentLauncher("madkit.kernel.ScriptAgentLauncher",
			this,"BeanShellAgent","madkit.bsh.BeanShellAgent",elt.getAttribute("name"),
			"java.lang.String",elt.getAttribute("code"),gui,p,d);
		} else if (elt.getAttribute("type").equalsIgnoreCase("SEdit")){
			al = AgentLauncher.makeAgentLauncher("SEdit.SEditFileAgentLauncher",
			this,"BeanShellAgent","madkit.bsh.BeanShellAgent",elt.getAttribute("name"),
			"java.lang.String",elt.getAttribute("code"),gui,p,d);
		}
		al.launch();
	}


    public void loadConfigFile(File f){
        try {

			FileInputStream from = new FileInputStream(f);
			Document doc =  DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(from);
			Element racine;
			NodeList agentlist;
			//on récupère le noeud racine
			racine=doc.getDocumentElement();  //le noeud racine (Desktop)
			//toolbars
			agentlist=racine.getElementsByTagName("launch-agent");
			for(int i=0; i<agentlist.getLength(); i++){
				Element te = (Element) agentlist.item(i);
				launchAgent(te);
			}
        }
        catch(IOException e){
            System.err.println("File read error with !\n"+f.getName());
        }
        catch(SAXException e){
            System.err.println("Parsing error of the file !\n"+f.getName());
            //e.printStackTrace();
        }
        catch(Exception e){
            System.err.println("XML problem !\n"+f.getName());
            //e.printStackTrace();
        }
    }
    
    public void loadFile(AbstractAgent ag, File f){
        String path=f.getPath();
        System.out.println("loading file "+f);
        AgentLauncher al;
        if (path.endsWith("."+CONFIG_FILE)){
            loadConfigFile(f);
        } else if (path.endsWith("."+PYTHON_FILE)){
			al = AgentLauncher.makeAgentLauncher("madkit.kernel.ScriptAgentLauncher",
						this,"PythonAgent","madkit.python.PythonAgent",null,
						"java.lang.String",path,true,null,null);
            //makeScriptAgent(ag,"madkit.python.PythonAgent","java.lang.String",path,true);
            al.launch();
        } else if (path.endsWith("."+BEANSHELL_FILE)){
			al = AgentLauncher.makeAgentLauncher("madkit.kernel.ScriptAgentLauncher",
			this,"BeanShellAgent","madkit.bsh.BeanShellAgent",null,
			"java.lang.String",path,true,null,null);
            // makeScriptAgent(ag,"madkit.bsh.BeanShellAgent","java.lang.String",path,true);
            al.launch();
        } else if (path.endsWith("."+SCHEME_FILE)){
        	System.out.println("OK1");
			al = AgentLauncher.makeAgentLauncher("madkit.kernel.ScriptAgentLauncher",
			this,"SchemeAgent","madkit.scheme.SchemeAgent",null,
			"java.io.File",f,true,null,null);
            //makeScriptAgent(ag,"madkit.scheme.SchemeAgent","java.io.File",f,true);
			al.launch();
        } else if (path.endsWith("."+JESS_FILE)){
			al = AgentLauncher.makeAgentLauncher("madkit.kernel.ScriptAgentLauncher",
			this,"JessAgent","madkit.jess.JessAgent",null,
			"java.lang.String",path,true,null,null);
			// makeScriptAgent(ag,"madkit.jess.JessAgent","java.lang.String",path,true);
			al.launch();
        } else if (path.endsWith("."+FORMALISM_FILE)){
			al = AgentLauncher.makeAgentLauncher("SEdit.FormalismFileLauncher",
			this,null,null,null,"java.io.File",f,true,null,null);
			al.setAgent(ag);
			al.launch();
	/*		File cwd = new File(System.getProperty("madkit.dir"));
			File formdir = new File(cwd, FormalismAgent.FORMALISM_FOLDER);
                Formalism form;
                XMLFormalism  xf = new XMLFormalism();
                System.out.println(">> loading formalism : " + path);
                form = xf.parse(path);
                form.setBase(formdir.getPath()+File.separator);
                // System.out.println("setbase: " + f.getBase());
                if (f!=null) {
                    StructureAgent sa=new StructureAgent(form);
                    ag.launchAgent(sa,form.getName()+" - Untitled",true);
                } else {
                    System.err.println(":: ERROR loading formalism : " + path);
                } */
        } else if (path.endsWith("."+SEDIT_FILE)){
			al = AgentLauncher.makeAgentLauncher("SEdit.SEditFileLauncher",
			this,null,null,null,"java.io.File",f,true,null,null);
			al.setAgent(ag);
			al.launch();
       /* 	AgentAddress fs = ag.getAgentWithRole("public","sedit","formalism-server");
        	if (fs == null){
        		Agent formServer = new FormalismAgent();
        		ag.launchAgent(formServer,"Formalizator",false);
        		fs = formServer.getAddress();
        	}
            String form = XMLStructureLoader.parseFormalismName(path);
            ag.sendMessage(fs, new SEditMessage("get",form,path)); */
        }
    }

    


  static void handleArgs(String[] argv){
      if (argv.length > 0) {
          for ( int t=0;t<argv.length ;t++ ){
                if ( argv[t].equals("--version")) {
                        System.out.println("MadKit/Aalaadin - O. Gutknecht, J. Ferber, F. Michel");
                        System.out.println("  microKernel version: "+Kernel.VERSION);
                        System.exit(0);
                    }
                if (argv[t].equals("--graphics"))
                graphics = true;

                if (argv[t].equals("--ip-numeric"))
                ipnumeric = true;

                // new config files
                if (argv[t].equals("--config"))
                    initFile=argv[++t];

                if ( argv[t].equals("--with-native-lf"))
                    nativelf=true;
                if ( argv[t].equals("--communicator-on"))
                    network=true;
                    ipaddress = null;
                if ( argv[t].equals("--with-java-lf"))
                    nativelf=false;
                if ( argv[t].equals("--no-interGroup-Messages"))
                    Kernel.interGroupMessage=false;
                 if ( argv[t].equals("--fastSynchonous"))
                    Kernel.fastSynchronous=true;
               if (argv[t].equals("--ip-numeric")){
                    ipnumeric = true;
                    StringTokenizer tk=new StringTokenizer(argv[++t],".",false);
                    try {
                        if (tk.countTokens() == 4){
                            int val;
                            int j;
                            for(j=0; j<4; j++) {
                                val = Integer.decode(tk.nextToken()).intValue();
                                if ((val < 0) || (val > 256))
                                    break;
                            }
                            if (j==4)
                                ipaddress = argv[t];
                        }
                        if(ipaddress == null)
                            System.err.println("Invalid IP address sent as parameter");
                    }
                    catch (Exception e)
                    {
                        System.err.println("Invalid IP address sent as parameter");
                    }
	   	        }

        }
    }
  }

 /** Booting from command line */
  static public void bootProcess(String argv[])
    {

      //installClassPath();
      handleArgs(argv);

      try {
        // Through java.lang.reflect, so we don't _need_ Swing at class loading
		if (graphics) {
            Class c = Utils.loadClass("javax.swing.UIManager");
            Class[] cp0 = new Class[0];
            Class[] cp1 = new Class[1];
            Object[] co0 = new Object[0];
            Object[] co1 = new Object[1];

            cp1[0]=Class.forName("java.lang.String");

            Method g;
            Method s = c.getMethod("setLookAndFeel",cp1);

            if (nativelf)
                g = c.getMethod("getSystemLookAndFeelClassName",cp0);
            else
              g = c.getMethod("getCrossPlatformLookAndFeelClassName",cp0);

            co1[0]= g.invoke(null,co0);
            s.invoke(null,co1);
        }
      }
      catch(Exception e) {
            System.err.println("Swing Look&feel exception:"+e);
      }
 	}
}


///
/// creation of agents
/// looks a lot like AbstractFileNode in MadkitTreeDirectory or
/// AgentButton in DesktopAgentGUI
//  these codes should be asbtracted...





/* class ScriptAgentLauncher extends AgentLauncher  {
    String type;
    String typeArg;
    Object arg;
    ScriptAgentLauncher(AbstractMadkitBooter booter, String type, String className, 
    					String label, String typeArg, Object arg, Boolean gui, Point p, Dimension d){
        super(booter,className,label,gui,p,d);
        this.type = type;
        this.typeArg = typeArg;
        this.arg = arg;
    }

    public void execute(){
        //System.out.println(":: type: " + type + ", className: " + className + ", label: "
        //                    +label+", typeArg: "+typeArg+", arg: "+arg);
        Object a=null;
        try {
            Class cl;

            cl = Utils.loadClass(className);
            Class[] cp0 = new Class[1];
            Object[] co0 = new Object[1];

            //cp0[0]=Class.forName(typeArg);
            cp0[0]=Utils.loadClass(typeArg);
            co0[0]=arg;

            Constructor s = cl.getConstructor(cp0);
            a= s.newInstance(co0);
            //System.out.println(":: a class: " + a.getClass());

        } catch (ClassNotFoundException ex){
              System.err.println("Agent class does not exist"+ ex.getMessage());
        } catch (Exception ccex){
              System.err.println("Agent launch exception:"+ccex);
              // ccex.printStackTrace();
        }
         if ((a != null) && (a instanceof AbstractAgent)){
              System.out.println("Launching "+ type + " agent");
              if (label == null)
                label = a.getClass().getName();
              if (gui == null)
                    booter.launchAgent((AbstractAgent) a,label,booter.isGraphics,position,dim);
              else
                    booter.launchAgent((AbstractAgent) a,label,gui.booleanValue(),position,dim);
         }
    }
} */

/* class PythonAgentLauncher extends ScriptAgentLauncher  {
    public PythonAgentLauncher(AbstractMadkitBooter booter, String label, String filePath, Boolean gui, Point p, Dimension d){
        super(booter,"PythonAgent","madkit.models.python.PythonAgent",label,"java.lang.String",filePath,gui,p,d);
    }
}

class BeanShellAgentLauncher extends ScriptAgentLauncher  {
    public BeanShellAgentLauncher(AbstractMadkitBooter booter, String label, String filePath, Boolean gui, Point p, Dimension d){
        super(booter,"BeanShellAgent","madkit.models.bsh.BeanShellAgent",label,"java.lang.String",filePath,gui,p,d);
    }
}

class SchemeAgentLauncher extends ScriptAgentLauncher  {
    SchemeAgentLauncher(AbstractMadkitBooter booter, String label, String filePath, Boolean gui, Point p, Dimension d){
            super(booter,"SchemeAgent","madkit.lib.agents.SchemeAgent",label,"java.io.File",new File(filePath),gui,p,d);
    }
}

class JessAgentLauncher extends ScriptAgentLauncher  {
    public void execute(){
        try {
            MadkitClassLoader ucl = Madkit.getClassLoader();
            Class cl = Utils.loadClass(className);
            super.execute();
        } catch (ClassNotFoundException ex){
              System.err.println("Jess has not been installed. Please download Jess 6.0");
              System.err.println("from http://herzberg.ca.sandia.gov/jess/");
              System.err.println("replace the old jess.jar in the libs/support/ directory");
              System.err.println("by the new jar that you have downloaded, and call it 'jess.jar'");
              System.err.println("Restart MadKit and that's it");
              System.err.println("See the user manual for more details");
              return;
        } catch (Exception ccex){
              System.err.println("JessAgent not available");
              // ccex.printStackTrace();
        }
    }
    JessAgentLauncher(AbstractMadkitBooter booter, String label, String filePath, Boolean gui, Point p, Dimension d){
        super(booter,"JessAgent","madkit.models.jess.JessAgent",label,"java.lang.String",filePath,gui,p,d);
    }
}
*/


