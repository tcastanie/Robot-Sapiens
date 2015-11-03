/*
* StructureAgent.java - SEdit, a tool to design and animate graphs in MadKit
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

import gnu.lists.LList;

import java.awt.Point;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.text.Document;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;



public class StructureAgent extends Agent
{

      public static String VERSION = "3.1b1";
      public static String DATE = "28/02/2003";
      static String AUTOLOAD_FOLDER = "autoload";


    public StructureBean gui;

    public Object clipboard;

    protected Formalism formalism;
    protected Structure structure;

    protected String fileName = null;

    public String getFileName(){
    	return(fileName);
    }

    int pastenumber=0;
    final static int decalage=20;

    // JF: flag to indicate that a structurebean should show
    // its panel element or not.. This information is looked up
    // when a StructureBean is instantiated
    boolean showElementPanel = true;
    public boolean getShowElementPanel(){return showElementPanel;}
    public void setShowElementPanel(boolean b){showElementPanel=b;}

    // JF FIXME: A way for a structure to send messages to other agents...
    protected AgentAddress recipient=null;
    public AgentAddress getRecipient() { return recipient;}
    public void setRecipient(AgentAddress ag) { recipient = ag;}

    /**
     * Create a structure and initialize it.
     */
    public void initStructure(){
        Class c;
        try {
           // c = Class.forName(formalism.getStructureClass());
            c = madkit.kernel.Utils.loadClass(formalism.getStructureClass());

            structure = (Structure) c.newInstance();
            structure.setAgent(this);
            structure.setFormalism(formalism);
            try {
                // System.out.println("install structure properties: " + formalism.getProperties());
                ReflectorUtil.setProperties(structure,formalism.getProperties());
            }
            catch(Exception e) {
                Utils.log("setProperties failed:"+e);
            }
            structure.initStructure();

        }
        catch(ClassNotFoundException ev){
            System.out.println("Structure Frame - not found the structure with name :"
                       + getFormalism().getStructureClass());
        }
        catch(InstantiationException e){
            System.out.println("Structure Frame - instanciation failed!");
            System.out.println(e.getMessage());
        }
        catch(IllegalAccessException e){
            System.out.println("Structure Frame - access problems!");
            System.out.println(e.getMessage());
        }
    }

    public void initEditor(){
        StructureEditor ed = getStructure().getEditor();
        if (ed != null){
            try {
                ReflectorUtil.setProperties(ed,formalism.getGraphicProperties());
                //System.out.println("graphic properties set!");
            }
            catch(Exception e){
                Utils.log("setGraphicProperties failed:"+e);
            }
        }
    }

    /**
     * get the related structure
     * @return the structure
     */
    public Structure getStructure()
    {
	    return structure;
    }
    
	/**
	 * get the related formalism
	 * @return the formalism
	 */
    public Formalism getFormalism() {
    	return(formalism);
    }

	/**
	 * get the related formalism if its name equals s
     * @param s the name of the formalism
	 * @return the formalism
	 */
    public Formalism getFormalism(String s) {
	if (s.equals(formalism.getName()))
	    return formalism;
	else
	    return null;
    }

	/**
	 * Indicates if the associated formalism is compatible with the formalism fname. 
	 * By default, this test reduces to a simple equals between names
     * @param fname the name of the formalism whose compatibility is tested
	 * @return true if the formalism is compatible, false otherwise
	 */
    public boolean isFormalismCompatible(String fname){
    	return (formalism.getName().equals(fname));
    }

    /**
       * Create a StructureAgent with a formalism
       * @param f the formalism
       */
    public StructureAgent(Formalism f)
    {
        super();
        formalism=f;
        initStructure();
        gui = new DefaultStructureBean(this);
        initEditor();
    }

    /**
     * Create a StructureAgent which has to load its
     * content from a file. The content is loaded in 'insert' mode.
     * @param fileDir directory of the file
     * @param fileName name of the file
     */
    public StructureAgent(Formalism f, String _fileName)
    {
    	super();
        formalism=f;
        initStructure();
        fileName = _fileName;
        gui = new DefaultStructureBean(this);
        initEditor();
    }

    public StructureAgent(){
    }


    public void setClipBoard(Object o)
    {
	clipboard = o;
	pastenumber=1;
    }

    public Object getClipBoard()
    {
	return clipboard;
    }

    public void insertClipBoard()
    {
	if (clipboard!=null)
	    if (clipboard instanceof Document)
		{
		    XMLStructureLoader xf = new XMLStructureLoader(this);
		    //JF: Has be been commented out for xml.jar reasons.
			//put it back as soon as possible
			//xf.integrate(structure,(Document)clipboard);
		    pastenumber++;
		    structure.activate();
		}
    }



    public void insertXMLFile(String fileName, boolean selected)
    {
	Object result;
	if (fileName != null)
	    {
		try{
		    XMLStructureLoader xf = new XMLStructureLoader(this);
		    xf.setInsertMode(selected);
		    Structure st = xf.parse(fileName,getStructure());
            if (st != null) {
		        gui.getEditor().setCanvasSize();
		        pastenumber++;
            }
		    //structure.activate();
		}
		catch(Exception e){
		System.out.println("INTERNAL ERROR: "+e);
		}
	    }
    }


   public void saveXMLFile(String _fileName) {
	  Object result;

	  System.out.println("saving the XML file: " + _fileName);
	  if (_fileName != null){
		try {
		  XMLStructureSaver xf =  new XMLStructureSaver(this);
		  xf.write(new File(_fileName),getStructure(),getFormalism());
		  //  gui.getEditor().setCanvasSize();
		  fileName = _fileName;
		  gui.setTitle(getFormalism().getName() + " - " + _fileName);
		  System.out.println(":: File " + fileName + " saved");
		  }
	    catch(Exception e){
		  System.out.println("INTERNAL ERROR: "+e);
	    }
	  }
    }

    public void activate()
    {
	createGroup(false,"sedit",null,null);
	requestRole("sedit", "editor");
	getStructure().preactivate();
	if (fileName!=null)
	    {
		insertXMLFile(fileName,false);
		gui.getEditor().setCanvasSize();
	    }
	 getStructure().activate();
    }

    public void initGUI()
    {
	try{
	    if (gui == null)
		gui = new DefaultStructureBean(this);
	    this.setGUIObject(gui);
	}
	catch (Exception e)
	    {
		println("StructureAgent:"+e);
		e.printStackTrace();
	    }
    }

    public void live()
    {
		Message m;
		boolean end=true;

		while(end){
		    m = waitNextMessage();
		    if (m instanceof SEditMessage){
		    	if (((SEditMessage) m).getRequest().equals("quit"))
					end=false;
			    else
			    	handleMessage((SEditMessage) m);
		    } else {
		    	Structure str = getStructure();
		    	if (str instanceof ActiveStructure)
		    		((ActiveStructure) str).handleMessage(m);
		    }
		}
    }

    /** apply the end operation to its structure before dying.. */
    public void end()
    {
    	getStructure().end();
        if (getGUIObject() != null)
            ((StructureBean)getGUIObject()).close();
		//System.err.println("end called");
    }

    public void close()
    {
		sendMessage(getAddress(),new SEditMessage("quit"));
    }

    protected void handleMessage(SEditMessage m){
    }

	/** Creates a component and initialize all fields and properties. Call an init method at the end */
    public SComponent newComponent(boolean selected, ComponentDesc nd, String id,
			     String label, Hashtable properties,
			     Point p, Hashtable graphicProperties,
			     Hashtable connectors,
			     Hashtable placement)
    {
	SComponent o = (SComponent) nd.makeElement(properties);
	if (id==null)
	    {
		getStructure().addNode((SNode)o);
		pastenumber=1;
	    }
	else
	    {
		getStructure().addNode((SNode)o, id);
		p.translate(pastenumber*decalage,pastenumber*decalage);
	    }

	if (label!=null)
	    o.setLabel(label);

	o.initGraphics(graphicProperties);
	o.getGObject().setLocation(p);
	//	gui.getEditor().installNode(o.getGObject(), selected);
	//Utils.debug(nd+" "+id+" "+label+" "+p+" "+properties+" "+graphicProperties);

	if (connectors == null)
	    {
		connectors = ((ComponentDesc)nd).makeConnectors();

		for (Enumeration e = connectors.keys(); e.hasMoreElements();)
		    {
			// nécessaire: name, sconnect
			//
			String name = (String) e.nextElement();
			SConnect s = (SConnect)connectors.get(name);
			s.setComponent(o);

			if (s instanceof SInConnect)
			    o.addInConnect(name, (SInConnect)s);
			if (s instanceof SOutConnect)
			    o.addOutConnect(name, (SOutConnect)s);
			getStructure().addNode(s); // mmmm
			//s.setLabel(s.getID()); // mmmh
			s.initGraphics(graphicProperties); // mmmh
			gui.getEditor().installNode(s.getGObject(), false);//selected); // mmmh
			Utils.debug(nd+" "+id+" "+label+" "+p+" "+properties+" "+graphicProperties);
		    }
		o.computeConnectorsPositions();
	    }
	else
	    {
		for (Enumeration e = connectors.keys(); e.hasMoreElements();)
		    {
			// nécessaire: name, sconnect
			//
			String name = (String) e.nextElement();
			SConnect s = (SConnect)connectors.get(name);
			s.setConnectorPlacement((ConnectorPlacement)placement.get(name));
			s.setComponent(o);
			if (s instanceof SInConnect)
			    o.addInConnect(name, (SInConnect)s);
			if (s instanceof SOutConnect)
			    o.addOutConnect(name, (SOutConnect)s);
		    }
	    }
		gui.getEditor().installNode(o.getGObject(), selected);
		o.init();
		return o;
    }

    /** Creates a node and initialize all fields and properties.
    	Call an init method at the end */
    public SNode newNode(boolean selected, NodeDesc nd, String id,
			String label, Hashtable properties,
			Point p, Hashtable graphicProperties)
    {
	SElement o = nd.makeElement(properties);

	if (id==null)
	    {
		getStructure().addNode((SNode)o);
		pastenumber=1;
	    }
	else
	    {
		getStructure().addNode((SNode)o, id);
		p.translate(pastenumber*decalage,pastenumber*decalage);
	    }

	if (label!=null)
	    o.setLabel(label);

	o.initGraphics(graphicProperties);
	o.getGObject().setLocation(p);
	gui.getEditor().installNode(o.getGObject(), selected);
	o.init();
	o.getGObject().init();
	return (SNode) o;
	// Utils.debug("NEEEEEEEWOOODE"+nd+" "+id+" "+label+" "+p+" "+properties+" "+graphicProperties);
    }

	/** Creates a component and initialize all fields and properties.
	Calls an init method at the end */
    public SArrow newArrow(boolean selected, ArrowDesc nd, String id,
			 String fromID, String toID,
			 String label, Hashtable properties,
			 Point p, Hashtable graphicProperties)
    {
	SArrow o = (SArrow) nd.makeElement(properties);
	SNode from = getStructure().getNode(fromID);
	SNode to = getStructure().getNode(toID);

	if (getStructure().isConnectable(o, from, to))
	    {
		if (id==null)
		    {
			getStructure().addArrow(o, from, to);
			pastenumber=1;
		    }

		else
		    {
			getStructure().addArrow(o, id, from, to);
			p.translate(pastenumber*decalage,pastenumber*decalage);
		    }

		if (label!=null)
		    o.setLabel(label);
		//	else
		//  o.setLabel(o.getID());

		o.initGraphics(graphicProperties);
		o.getGObject().setLocation(p);
		gui.getEditor().installArrow(o.getGObject(), selected);
		o.init();
		return o;

		// Utils.debug(nd+" "+" "+from+" "+to+" "+id+" "+label+" "+p+" "+properties+" "+graphicProperties);
	    }
	else
	    System.err.println("Can't connect "+from+" "+to);
	    return null;

	//getStructure().addArrow((SArrow)o);

    }



    public SElement doCommand(Command c)
    {
	//System.err.println(c);
	if (c instanceof NewNodeCommand)
	    {
		NodeDesc nd = getFormalism().getNodeDesc(((NewNodeCommand)c).getNodeDesc());
		Point p = ((NewNodeCommand)c).getPosition();
		if (nd instanceof ComponentDesc)
		    return newComponent(false, (ComponentDesc)nd, null, null, null, p, null, null, null);
		else
		    return newNode(false, nd, null, null, null, p, null);
	    }
	if (c instanceof NewArrowCommand)
	    {
		ArrowDesc nd = getFormalism().getArrowDesc(((NewArrowCommand)c).getArrowDesc());
		String fromID = ((NewArrowCommand)c).getOriginID();
		String toID = ((NewArrowCommand)c).getTargetID();
		Point p = ((NewArrowCommand)c).getPosition();

		return newArrow(false, nd, null, fromID, toID, null, null, p, null);
	    }
	/*	if (c instanceof DeleteCommand)
	    {
		SElement s = ((DeleteCommand)c).getSElement();
		s.delete();
		}*/
		return null;
    }

    // commands that allows structures to pilot StructureAgents...
	public void sendToRecipient(Message m){
		if (getRecipient() != null)
			sendMessage(getRecipient(),m);
	}
	/** glue code */
  public void doPause(int m)
    {
	//debug("schemePause on:"+this);
	pause(m);
	//debug("schemePause off"+this);
    }

  /** glue code */
  public boolean askMessageBoxEmpty()
  {
      //debug("schemeMessageBoxEmpty ?"+isMessageBoxEmpty());
      return isMessageBoxEmpty();
  }


  /** glue code */
  public void doSendMessage(AgentAddress a, Message m)
  {
    sendMessage(a,m);
  }

  /** glue code */
  public void doBroadcastMessage(String g, String r, Message m)
  {
    sendMessage(g,r,m);
  }



  public void println(String s){
  	System.out.println(s);
  }

  /**  glue code */
  public void doKillAgent(AbstractAgent a)
  {
      killAgent(a);
  }


  /** creates and launches an agent of a given class */
  public void makeLaunchAgent(String cl, String n)
  {
    try
      {
	Agent a = (Agent) (madkit.kernel.Utils.loadClass(cl).newInstance());
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

}














