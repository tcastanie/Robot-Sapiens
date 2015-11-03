/*
* Structure.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.Graphics;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;


/******************************************************************
			CLASSE Structure
******************************************************************/


/** A structure is the basic model data type. It contains nodes and
    arrows, and is responsible for creation, I/O, and identificaiton.

    The structure data type is completely decoupled from actual
    graphic representation (which is managed by the StructureEditor,
    and GObject plus its subclasses)

    @author Jacques Ferber and Olivier Gutknecht
    @version 2.0
    @see StructureEditor */

public class Structure extends Object
{
    /** The model formalism */
    protected Formalism formalism;
    protected String description = "";



    public void setFormalism(Formalism f){ formalism = f; }
    public Formalism getFormalism(){ return(formalism); }

    protected Vector nodeList = new Vector();
    protected Vector arrowList = new Vector();

    public String getDescription(){return(description);}
    public void setDescription(String s){description=s;}

    public Vector getNodes(){return(nodeList);}
    public Vector getArrows(){return(arrowList);}

    // compteur des noms des nodes et des arrows
    private int numArrow=0;
    private int numNode=0;

    /** Ol: FIXME nobody sets this flag
    	JF: right, but somebody should!!
	Ol: Not sure. Who sould know if a structure is modified or
	not ? The structure itself or the editor ?
    */
    protected boolean modified = false;	// dit si on a bouge quelque chose

    // Current graph editor
    protected StructureEditor editor;
    public StructureEditor getEditor(){ return(editor); }
    public void setEditor(StructureEditor s){ editor = s; }
    // Current structure agent
    protected StructureAgent agent;
    public StructureAgent getAgent(){ return(agent); }
    public void setAgent(StructureAgent s){ agent = s; }


	// quelques preferences
	protected boolean snapToGrid=false;
	public void setSnapToGrid(boolean b){
		boolean doUpdate=false;
		if (b == snapToGrid) doUpdate=true;
		snapToGrid = b;
		if (doUpdate) getEditor().setSnapToGrid(true);
	}
	public boolean getSnapToGrid(){return snapToGrid;}

	public void setSnapToGrid1(boolean b){
		snapToGrid = b;
	}

	/* public void toggleSnapToGrid(){
 		snapToGrid = !snapToGrid;
 	} */
	// grid management
    protected boolean displayGrid=false;
 	public void setDisplayGrid(boolean b){displayGrid = b;}
 	public boolean getDisplayGrid(){return displayGrid;}
 	public void toggleShowGrid(){
 		displayGrid = !displayGrid;
 	}

    protected int gridSize=10;
 	public void setGridSize(int n){gridSize = n;}
 	public int getGridSize(){return gridSize;}


	/** Initialize a structure when everything is OK.
		Does nothing by default. May be redefined by subclasses.
		*/
    public void initStructure() {
    }

    /** Called by the StructureAgent when the agent is killed
    	Does nothing by default. May be used by subclasses.
    */
    public void end(){
    }

    public void activate()
    {
	for (Enumeration e=getNodes().elements(); e.hasMoreElements();)
	    ((SNode)e.nextElement()).activate();
    }
    public void preactivate(){}

    public void clearAll()
	{
		nodeList.removeAllElements();
		arrowList.removeAllElements();
    }

    /** Returns a new node ID not existing yet in the structure */
    public String newNodeID()
    {
   	String name;
	do {
	    name = "N"+numNode++;
	} while (existID(name));
	return(name);
    }

    /** Returns a new arrow ID not existing yet in the structure */
    public String newArrowID()
    {
   	String name;
	do {
         	name = "A"+numArrow++;
	} while (existID(name));
		return(name);
    }

    /** Find if a given ID exists already in the structure */
    public boolean existID(String id) {
   	return ((getNode(id) != null) || (getArrow(id) != null));
    }

    public void addNode(SNode o)
    {
	o.setID(newNodeID());
	nodeList.addElement(o);
	o.setStructure(this);
    }
    public void addNode(SNode o, String s)
    {
	o.setID(s);
	nodeList.addElement(o);
	o.setStructure(this);
	// System.err.println("Added node"+s+" "+o.getID());

    }

    public void addArrow(SArrow o, SNode from, SNode to)
    {
	o.setID(newArrowID());
	arrowList.addElement(o);
	o.setStructure(this);
	from.addOutArrow(o);
	to.addInArrow(o);
	o.setOrigin(from);
	o.setTarget(to);
    }

    public void addArrow(SArrow o, String s, SNode from, SNode to)
    {
	o.setID(s);
	arrowList.addElement(o);
	o.setStructure(this);
	from.addOutArrow(o);
	to.addInArrow(o);
	o.setOrigin(from);
	o.setTarget(to);
    }


    public boolean isConnectable(SArrow edge, SNode from, SNode to)
    {
	// Ol FIXME : the setIn/OutArrows with side effect is _very dangerous_ as it may leave the in/out
	// status of a node in incoherent state
	return (edge.isConnectable(from, to) && from.isConnectable(edge, false) && to.isConnectable(edge, true));// &&
	    //	    from.setInArrows(edge) && to.setOutArrows(edge)) FIXME !
	    //	{
	    // addArrow(edge);
	    //return true;
	    //}
	    //	else
	    //return false;
    }


    public void removeNode(SNode o){	nodeList.removeElement(o); }
    public void removeArrow(SArrow o){	arrowList.removeElement(o); }

    public void deleteAll(){
      Vector v1 = (Vector) nodeList.clone();
      for (Iterator e=v1.iterator();e.hasNext();){
          ((SNode)e.next()).delete();
      }
      v1 = (Vector) arrowList.clone();
      for (Iterator e=v1.iterator();e.hasNext();){
          ((SArrow)e.next()).delete();
      }
      nodeList.removeAllElements();
      arrowList.removeAllElements();
    }

    /** Find a node by its ID */
    public SNode getNode(String id) {
   	SNode o;
	    // System.err.println("Looking for"+id+nodeList);
   	for (int i=0; i<nodeList.size();i++){
	    o = (SNode)nodeList.elementAt(i);
	    // System.err.println("Looking for"+id+" "+o.getID()+o+"/"+i);

    if (o.getID().equals(id))
           	return (o);
	}
	return(null);
    }

    /** Find an arrow by its ID */
    public SArrow getArrow(String id) {
   	SArrow o;
   	for (int i=0; i<arrowList.size();i++){
	    o = (SArrow)arrowList.elementAt(i);
	    if (o.getID().equals(id))
           	return (o);
	}
	return(null);
    }


     /** Return the *first* node with a given label in the structure */
   public SNode getNodeFromLabel(String name) {
   	SNode o;
   	for (int i=0; i<nodeList.size();i++){
	    o = (SNode)nodeList.elementAt(i);
	    if (o.getLabel()!=null)
		if (o.getLabel().equals(name))
		    return (o);
	}
	return(null);
    }

    /** Return the *first* node with a given label in the structure */
    public SArrow getArrowFromLabel(String id) {
   	SArrow o;
   	for (int i=0; i<arrowList.size();i++){
	    o = (SArrow)arrowList.elementAt(i);
	    if (o.getLabel()!=null)
		if (o.getLabel().equals(id))
		    return (o);
	}
	return(null);
    }

    /** draw the background. Does nothing in Structure.
    May be overridden by subclasses. */
    public void drawBackground(Graphics g){
    }

    /** create a node and install it into the editor.
    	@argument nameDescr is the name of its node-desc
    	as it is given in formalisms files.
    */
    public SNode createNode(String nameDescr, int x, int y){
    	NodeDesc typeElement = getFormalism().getNodeDesc(nameDescr);
    	return (SNode) getAgent().doCommand(new NewNodeCommand((NodeDesc)typeElement, new Point(x,y)));
    }

    public void dump() {
	System.out.println("nodes:");
	for (int i=0; i<nodeList.size();i++){
	    System.out.println(nodeList.elementAt(i));
       	}
	System.out.println("arrows:");
	for (int i=0; i<arrowList.size();i++){
	    System.out.println(arrowList.elementAt(i));
	}
    }

    public String toString()
    {
	String name = "anonymous";
	return("<Model \"" + name
	       + "\" : \""  + getFormalism() + "\">");
    }

}











