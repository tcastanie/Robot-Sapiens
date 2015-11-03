/*
* AbstractAgentNode.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.util.Vector;

import madkit.kernel.AbstractAgent;
import SEdit.Graphics.GAgent;

 /**************************************************************************
 *
 *			CLASS AbstractAgentNode
 *
 *	 A type of node which encapsulates an agent and displays it into
 *	 an editor.
 *	 AbstractAgentNode is a subclass of SNode for the moment. Could be a subclass
 *	 of SComponent in the future
 *   Copyright :    Copyright (c) 2001
 *   @author J. Ferber
**************************************************************************/

abstract public class AbstractAgentNode extends SNode {
	AbstractAgent theAgent=null;
    String agentClass=null;

    public AbstractAgentNode() {}

	public AbstractAgent getAgent(){
		return theAgent;
	}

    public void setLabel(String s){
        super.setLabel(s);
        theAgent.setName(s);
        GAgent gui=(GAgent) this.getGObject();
        if (gui != null)
            gui.setName(s);
    }

    public String getName(){
        String lab=getLabel();
        if (lab != null)
            return lab;
        else {
            if (theAgent != null){
                lab=theAgent.getClass().getName();
                int j=lab.lastIndexOf('.');
                if (j != -1)
                    lab=lab.substring(j+1);
            } else return getID();
        }
        return Booter.getAgentLabel(lab);
    }

    public void setupGUI(AbstractAgent ag){
		  if (ag != theAgent){
			System.out.println("Error in setupGUI of an agent");
			return;
		  }
		  if (!(getGObject() instanceof GAgent)){
			System.err.println("Error: not an instance of GAgent :"+ getGObject());
			return;
		  }
		  theAgent.initGUI();
		  if (theAgent.getGUIObject() != null)
		    ((GAgent) getGObject()).initGUI(theAgent.getGUIObject());
    }

    /** set the agentClass name which
	 *  has to be instantiated
	  */
    public void setAgentClass(String s){agentClass = s;}

    /** Get the agentClass name which
	 *  has to be instantiated
	 *  */
    public String getAgentClass(){return agentClass;}

    public void delete(){
        if (theAgent != null){
            getStructure().getAgent().doKillAgent(theAgent);
        }
        super.delete();
    }
	/**
	 * Creates the Agent from the agentClass
     */
	 public void init(){
	}


  //-----------------------------------------------------------------
  protected void addInArrow(SArrow a) {
    /**@todo: implémenter cette méthode SEdit.SNode abstract*/
  }
  public Vector getInArrows() {
    /**@todo: implémenter cette méthode SEdit.SNode abstract*/
	return null;
  }
  protected void addOutArrow(SArrow a) {
    /**@todo: implémenter cette méthode SEdit.SNode abstract*/
  }
  protected boolean isConnectable(SArrow s, boolean asTarget) {
    /**@todo: implémenter cette méthode SEdit.SNode abstract*/
	return true;
  }
  public Vector getOutArrows() {
    /**@todo: implémenter cette méthode SEdit.SNode abstract*/
	  return null;
  }


}
