/*
* WorldStructure.java - A simple reactive agent library
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
import madkit.kernel.*;
import java.util.*;
import java.awt.*;
import javax.swing.*;

import SEdit.*;
import SEdit.Formalisms.*;
import SEdit.Graphics.*;


/** A Structure which acts both as an environment
	and as a scheduler specialized in managing worlds..

  @author Jacques Ferber
  @version 0.1

*/

public class WorldStructure extends Structure implements Runnable
{
    String groupName;
    WorldControlDialog ed;

    static String groupTypeName="DemoWorld";
	static int groupCounter=1;

	public Vector getEntities(){
		return getNodes();
	}

    public static void setGroupTypeName(String s){
    	groupTypeName=s;
    }

	String getGroupName(){
		if (groupName != null)
			return groupName;
		else {
			groupName=groupTypeName+"-"+groupCounter++;
			return groupName;
		}
	}

    int iteration = 0;
    int delay = 60;

  	Thread process=null;
	protected boolean running;

    boolean step=false;


    public void setDelay(int n){delay=n;}
    public int getDelay(){return delay;}

    public boolean execute=false;
    public void setExecute(boolean b){execute = b;}
    public boolean getExecute(){return execute;}


    public boolean showIteration=false;
    public void setShowIteration(boolean b){showIteration = b;}
    public boolean getShowIteration(){return showIteration;}

    protected Rectangle worldDimension=new Rectangle(5,5,600,400);
    public Rectangle getWorldDimension(){return worldDimension;}
    public void setWorldDimension(Rectangle d){worldDimension = d;}
    public void setWorldDimension(int x, int y, int w, int h){
    	worldDimension.x = x;
    	worldDimension.y = y;
    	worldDimension.width = w;
    	worldDimension.height = h;
    }

    public void setWorldWidth(int v){
    	worldDimension.width = v;
    	System.out.println(":: setWorldWidth: " + v);
    }
    public int getWorldWidth(){return worldDimension.width;}

    public void setWorldHeight(int v){worldDimension.height = v;}
    public int getWorldHeight(){return worldDimension.height;}


    public void println(String s){
    	System.out.println(s);
    }

    public WorldStructure()
    {
    	setWorldDimension(5,5,1000,700);
    	setGridSize(16);
    	setSnapToGrid(true);
    	setDisplayGrid(true);
    }

	public void drawBackground(Graphics g){
		Rectangle r = getWorldDimension();
		g.setColor(Color.blue);
		g.drawRect(4,4,r.x+r.width+2,r.y+r.height+2);
		g.drawRect(3,3,r.x+r.width+4,r.y+r.height+4);
		g.drawRect(2,2,r.x+r.width+6,r.y+r.height+6);
	}

	public synchronized Vector detectEntities(WorldEntity w, Point p, int r){
		Rectangle b;
		WorldEntity e;
		Vector result=new Vector();
		boolean found =false;
		Vector entities = getEntities(); // pour l'instant tous les nodes sont des entities...
		for (Iterator ent = entities.iterator() ; ent.hasNext();) {
    		e= (WorldEntity)ent.next();
			if ((e != null)&&(e != w)) {
				Point c = e.getGObject().getCenter();
//				b = e.getGObject().getBounds();
				int dist = (int) Math.sqrt((p.x-c.x)*(p.x-c.x) + (p.y-c.y)*(p.y-c.y));
//				if (r.intersects(b))
				if (dist <= r){
					found = true;
					result.addElement(e);
				}
			}
		}
		if (found)
			return result;
		else
			return null;
	}

  /** Close the structure by deleting all entities before closing. Is used
  	  to delete all agents that are related to entities */
  	public void end(){
  		WorldEntity[] entities=new WorldEntity[getEntities().size()];
  		getEntities().copyInto(entities);
  		// Vector entities = getEntities();
  		// System.out.println("delete entities: " + entities.length);
  		for (int i=0;i<entities.length;i++){
    		WorldEntity e= entities[i];
    		// System.out.println("deleting: " + e);
			if (e != null)
				e.delete();
		}

  	}

	public boolean isRunning(){return running;}

    public void run()
    {
      while (running) {
		if (showIteration)
			System.out.println("iteration: " + iteration);
		runEnvironment();
		runEntities();
      	getEditor().repaint();

		iteration++;
		step = false;

	  	try {process.sleep(delay);}
		catch (InterruptedException e) 	{
			running = false; break;
		}
	  }
    }

	public synchronized void runEnvironment(){;}

	public synchronized void  runEntities(){
		Vector entities = getEntities(); // A priori tous les nodes sont des entities...

		for (Enumeration ent = entities.elements() ; ent.hasMoreElements();) {
    		WorldEntity e= (WorldEntity)ent.nextElement();
		    // System.out.println("running: " + e);
			if (e != null)
				e.doIt();
		}
    }

    public void start(){
	  if (!running){
	  	running = true;
      	process = new Thread(this);
      	process.setPriority(Thread.MAX_PRIORITY);
      	process.start();
      }
    }

    public void stop(){
		running = false;
	}


    public void step(){
    	if (!running) {
			runEnvironment();
			runEntities();
      		getEditor().repaint();
		}
    }

    public void modifyDelay(){
       		if (ed == null)
       			ed = new WorldControlDialog(this);
       		else
       			ed.show();
    }

   /* public void end()
    {
    	for (int i=0; i < entities.size(); i++)
			killAgent((AbstractAgent) entities.elementAt(i));
    	for (int i=0; i < viewers.size(); i++)
			killAgent((AbstractAgent) viewers.elementAt(i));
		killAgent(getEnvironment());
		leaveGroup(getGroupName());
    } */

}


