/*
* WorldEntity.java - A simple reactive agent library
* Copyright (C) 1998-2002 Jacques Ferber
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
package SEdit.Formalisms.World;

import java.io.*;
import java.awt.*;
import madkit.kernel.*;
import java.util.*;

import SEdit.*;
import SEdit.Formalisms.*;
import SEdit.Graphics.*;


public class WorldEntity extends SNode
{
	// pas de liens entre entites...
    public  Vector getInArrows() {return null;}
    public  Vector getOutArrows() {return null;}
    protected void addOutArrow(SArrow a) {}
    protected void addInArrow(SArrow a){}
    protected boolean isConnectable(SArrow s, boolean isTarget){return(false);}

  	boolean deleted=false; // a flag to avoid circularity

    WorldStructure getEnvironment(){
    	return (WorldStructure) getStructure();
    }

  public Point getLocation(){
  	return getGObject().getCenter();
  }

  // the brain
  Brain myBrain;

  /** Set the brain of an entity. The brain
      generally derives from AbstractAgent. */
  public void setBrain(Brain b){myBrain = b;}

  /** Get the brain of an entity which
      generally derives from AbstractAgent. */
  public Brain getBrain(){return myBrain;}

  protected String brainClass;

  /** set the brain class name of an entity. The brain
      generally derives from AbstractAgent. */
  public void setBrainClass(String s){brainClass = s;}

  /** Get the brain class name of an entity which
      generally derives from AbstractAgent. */
  public String getBrainClass(){return brainClass;}

  protected String behaviorFileName=null;

  /** Set the behavior file name attached to the brain class
   *  used for scripting the behavior.
   */
  public void setBehaviorFileName(String s){
	  behaviorFileName=s;
  }

  /** Return the behavior file name attached to the brain class
   *  used for scripting.
   */
  public String getBehaviorFileName(){return behaviorFileName;}

  /** Returns the position of an entity, given as a Point.
  */
  public Point getPosition(){
  	  return getGObject().getCenter();
  }

  /** Computes and returns the distance between two entities
   */
  public int getDistance(WorldEntity e){
	Point wp = e.getPosition();
	Point mep = getPosition();
	int dx=wp.x-mep.x;
	int dy=wp.y-mep.y;
	return ((int) Math.round(Math.sqrt(dx*dx+dy*dy)));
  }


  /** 	Creates the brain and launches if it is an agent.
  *		The brain class is given as a String. The name argument is used to instantiate
  *		the name of the corresponding agent. If the gui flag is true, a bean is created
  *		and associated to this agent.
  */
  public void makeBrain(String className, String name, boolean gui,
                        String behaviorFileName){
  	try {
  		Class c;
          //c = Class.forName(className);
  		c = madkit.kernel.Utils.loadClass(className);
	    myBrain = (Brain) c.newInstance();
	    myBrain.setBody(this);
	    if (myBrain instanceof AbstractAgent){
	    	String n=name;
	    	if (n == null){
	    		n = getLabel();
	    	}
	    	if (n == null){
	    		n = getID();
	    	}
			if (behaviorFileName != null)
			  setBehaviorFileName(behaviorFileName);
	    	getStructure().getAgent().doLaunchAgent((AbstractAgent) myBrain,n,gui);
	    }

	}
	catch(ClassNotFoundException ev){
	    System.err.println("Class not found :" + className + " "+ev);
	    ev.printStackTrace();
	}
	catch(InstantiationException e){
	    System.err.println("Can't instanciate ! " + className +" "+e);
	    e.printStackTrace();
	}
	catch(IllegalAccessException e){
	    System.err.println("illegal access! " + className+" "+e);
	    e.printStackTrace();
	}
 }

  public void showLine(WorldEntity e, Color c){
  	 Graphics g = getGObject().getEditor().getGraphics();
  	 Point cme = getGObject().getCenter();
  	 Point ce = e.getGObject().getCenter();
	 g.setColor(c);
	 g.drawLine(cme.x,cme.y,ce.x,ce.y);
  }

 boolean askForDeletion=false;
 /** Delete an entity, and its brain if there is one. Do not ask directly
  *  this method.
 */
 public void delete(){
   // System.out.println("Actually deleting " + this);
 	if (deleted) return;
 	deleted = true;
 	if (myBrain != null){
 		if (myBrain instanceof AbstractAgent)
 			getStructure().getAgent().doKillAgent((AbstractAgent) myBrain);
 		else
 			myBrain.delete();
 	}
 	super.delete();
 }

 public void selfDelete(){
	if (((WorldStructure) getStructure()).isRunning()){
	  askForDeletion=true;
	  // System.out.println("asking for deletion: "+this);
	}
	else
	  delete();
 }

  /** 	The generic activators of entities.
  *		If there is brain, this brain is activated via the doIt method.
  */
  public void doIt(){
	beforeDoIt();
  	if (myBrain != null){
  		myBrain.doIt();
  	}
  	else
  		bodyDoIt();
	afterDoIt();
  }

  /** The activator of entities which do not have brain
  */
  public void bodyDoIt(){;}

  /** Method asked before the activation of entities
   */
  public void beforeDoIt(){ }

  /** Method asked after the activation of entities
   */
  public void afterDoIt(){
	if (askForDeletion)
	  delete();
  }

  public String toString(){
	  return("a "+this.getClass().getName());
  }


}
