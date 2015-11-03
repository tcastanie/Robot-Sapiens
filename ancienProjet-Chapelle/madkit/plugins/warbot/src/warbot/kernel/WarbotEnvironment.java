/*
* WarbotEnvironment.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

import java.awt.Point;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import madkit.kernel.Message;
import madkit.kernel.StringMessage;
import madkit.messages.ObjectMessage;


class PerceptComparator implements Comparator{
	  public int compare(Object o1, Object o2){
			 double d1 = ((Percept) o1).getDistance();
			 double d2 = ((Percept) o2).getDistance();
			 if (d1<d2) return -1;
			 else if (d1==d2) return 0;
			 else return 1;
	  }
}

final public class WarbotEnvironment extends EnvironmentAgent
{
	public static final int HOME=1,WALL=2,PALMTREE=3,HAMBURGER=4,ROCKETLAUNCHER=5,EXPLORER=6;
    public static final String[] TYPES={"None","Home","Wall","Palmtree","Hamburger","RocketLauncher","Explorer"};

	public Environment2D world;
	Vector removableEntity;
	Vector evolutiveEntities;
	Map homes;
	Collection walls = new HashSet();

	PerceptComparator perceptComparator= new PerceptComparator();

    WarbotStructure theStructure;

WarbotEnvironment(WarbotStructure st, int width,int height,String group)
{
	super(group);
    theStructure = st;
	world=new Environment2D(width,height);
	removableEntity=new Vector();
	evolutiveEntities=new Vector();
	homes=new HashMap();
	//makeWorld();
}

public void activate(){
    super.activate();
	makeWalls();
}

final synchronized void removeEntity(Entity theEntity)
{
	world.removeEntity(theEntity);
	if(! (theEntity instanceof BasicBody))
	{
		evolutiveEntities.remove(theEntity);
	}
}

final synchronized boolean contains(Entity theEntity){
    return world.contains(theEntity);
}

final synchronized  boolean addEntity(Entity theEntity)
{
	if ((! (theEntity instanceof BasicBody)) && theEntity.getSpeed()>0)
		evolutiveEntities.addElement(theEntity);
	world.addEntity(theEntity);
    theEntity.setEnvironmentAgent(this);
	if(theEntity instanceof Home)
	{
		if(homes.containsKey(theEntity.getTeam()))
			((Collection) homes.get(theEntity.getTeam())).add(theEntity);
		else
		{
			Collection c = new HashSet();
			c.add(theEntity);
			homes.put(theEntity.getTeam(),c);
		}

	}
	return true;
}

boolean dropEntity(Entity deposor,Entity theEntity,double dir)
{
	int minDist=deposor.getRadius()+theEntity.getRadius()+1;
	double angle=dir%360;
	if(angle<0) angle+=360;
	for(int i=0;i<180;i++)
	{
		double angleSin=Math.sin( (Math.PI*angle)/180);
		double angleCos=Math.cos( (Math.PI*angle)/180);
		theEntity.setXY(minDist*angleCos+deposor.getX(),minDist*angleSin+deposor.getY());
		if(authorizeMove(theEntity,theEntity.getX(),theEntity.getY()))
		{
			world.addEntity(theEntity);
			theEntity.reInstall(theStructure);
			return true;
		}
		angle+=2;
		angle%=360;
	}
	return false;
}

void makeWorld()
{
	for(int i = 500;i<600;i+=5)
		for(int j = 40;j<100;j+=5)
			makeHamburger(i,j);
	for(int i = 200;i<300;i+=5)
		for(int j = 760;j>700;j-=5)
			makeHamburger(i,j);
	for(int i = 200;i<275;i++)
		makeHamburger((int) (Math.random()*(world.getWidth()-50)+25), (int) (Math.random()*(world.getHeight()-50)+25));
}

void makeWalls()
{
	for (Iterator i = walls.iterator();i.hasNext();)
		world.removeEntity((Entity) i.next());
	walls.clear();
	int height = 3;
	for(int i = 20;i<world.getWidth();i+=50)
		makeWall(i,height);
	height = world.getHeight()-3;
	for(int i = 20;i<world.getWidth();i+=50)
		makeWall(i,height);
	height = 3;
	for(int i = 20;i<world.getHeight();i+=50)
		makeWall(height,i);
	height = world.getWidth()-3;
	for(int i = 20;i<world.getHeight();i+=50)
		makeWall(height,i);
}



void makeWall(int x,int y)
{
//	Entity e = new Wall(this,"wall",20,Integer.MAX_VALUE);
//	setEntity(e,x,y);
//	walls.add(e);
    theStructure.getAgent().doCommand(new SEdit.NewNodeCommand("Wall",new Point(x,y)));
	Entity w = theStructure.getLastEntity();
	w.persistent=false;
    walls.add((Wall) w);
}

void makeHamburger(int x, int y)
{
	//Entity e = new Hamburger(this,"hamburger",7,500,0);
	//setEntity(e,x,y);
    theStructure.getAgent().doCommand(new SEdit.NewNodeCommand("Hamburger",new Point(x,y)));
}
void setEntity(Entity e, int x, int y)
{
	e.setXY(x,y);
	world.addEntity(e);
}

synchronized void moveWorld()
{
	for(Enumeration e=evolutiveEntities.elements();e.hasMoreElements();)
	{
		Entity ent = (Entity) e.nextElement();
		if(ent.willAct())
			ent.doIt();
	}
}

synchronized void deleteEntities()
{
	System.out.println("deleting the entities");
	for(Enumeration e=removableEntity.elements();e.hasMoreElements();)
		((Entity) e.nextElement()).delete();
	removableEntity.removeAllElements();

	Entity[] entities = world.getAllEntities();
	for(int i=0;i<entities.length;i++)
		entities[i].delete();
	checkMail();
}

void checkVictory()
{
	if(! homes.isEmpty())
	{
		boolean stop = true;
		for(Iterator i = homes.values().iterator();i.hasNext();)
		{
			Collection c = (Collection) i.next();
			for(Iterator j = c.iterator();j.hasNext();)
			{
				Entity e = (Entity) j.next();
				if( e.getEnergy()<0)
				{
					j.remove();
					stop = false;
				}
			}
		}
		if(stop)
			return;

		Collection defeated = new HashSet();
		String aliveTeam = null;

		for(Iterator i = homes.entrySet().iterator();i.hasNext();)
		{
			Map.Entry e = (Map.Entry)  i.next();
			if( ((Collection) e.getValue()).isEmpty())
			{
				//System.err.println(""+(String)e.getKey());
				Entity[] entities = world.getAllEntities();
				for(int t = 0;t<entities.length;t++)
					if(entities[t].getTeam().equals(e.getKey()))
						entities[t].setSpeed(0);
				defeated.add(e.getKey());
				i.remove();
			}
			else
				aliveTeam = (String) e.getKey();
		}
		if(homes.size() == 0)
		{
			Object[] defeat = {"drawGame",defeated};
			sendMessage(getSimulationGroup(),"warbot GUI",new ObjectMessage(defeat));
		}

		else if(homes.size() == 1)
		{
			Object[] victory = {"victory",aliveTeam};
			sendMessage(getSimulationGroup(),"warbot GUI",new ObjectMessage(victory));
			homes.clear();
		}
	}
}

boolean authorizeMove(Entity theEntity,double x, double y)
{
	Entity[] allEntities = world.getAllEntities();

	//still inside the world
	/*if (x+theEntity.getRadius()>world.getWidth() || y+theEntity.getRadius()>world.getHeight() || x-theEntity.getRadius()<0 || y-theEntity.getRadius()<0 )
		return false;*/
	//test if impact
	double radius=theEntity.getRadius();
	for (int i=0;i<allEntities.length;i++)
	{
		double eradius = allEntities[i].getRadius();
		double ex = allEntities[i].getX();
		double ey = allEntities[i].getY();
		if(allEntities[i] != theEntity && (! (Math.abs(ex-x) > radius+eradius || Math.abs(ey-y) > radius+eradius || Math.sqrt( Math.pow(ex-x,2)+ Math.pow(ey-y,2) ) > radius+eradius)) )
			return false;
	/*for (int i=0;i<allEntities.length;i++)
		if(allEntities[i] != theEntity && (! (Math.abs(allEntities[i].getX()-x) > x+theEntity.getRadius()+allEntities[i].getRadius() || Math.abs(allEntities[i].getY()-y) > y+theEntity.getRadius()+allEntities[i].getRadius() || Math.sqrt( Math.pow(allEntities[i].getX()-x,2)+ Math.pow(allEntities[i].getY()-y,2) ) > theEntity.getRadius()+allEntities[i].getRadius())) )
			return false;*/
	}
	return true;
}

Entity authorizeMove(Rocket theRocket,double newX,double newY)
{
	Entity[] allEntities = world.getAllEntities();

	//test if impact
	double radius=theRocket.getRadius();
	for (int i=0;i<allEntities.length;i++)
	{
		double eradius = allEntities[i].getRadius();
		double ex = allEntities[i].getX();
		double ey = allEntities[i].getY();
		if(allEntities[i] != theRocket && (! (Math.abs(ex-newX) > radius+eradius || Math.abs(ey-newY) > radius+eradius || Math.sqrt( Math.pow(ex-newX,2)+ Math.pow(newY-ey,2) ) > radius+eradius)) )
		{
			//System.err.println("unauthorize move of rocket "+theRocket+ " with "+allEntities[i]);
			return allEntities[i];
		}
	}
	return null;
}

/*Entity[] getPerception(Entity robot)
{
	Collection detected = new HashSet();
	Entity[] allEntities = world.getAllEntities();
	int minDist=robot.getDetectingRange()+robot.getRadius();
	int fromX = robot.xcor();
	int fromY = robot.ycor();

	for (int i=0;i<allEntities.length;i++)
		if(allEntities[i] != robot &&
		   Math.abs(allEntities[i].xcor()-fromX) < minDist+allEntities[i].getRadius() &&
		   Math.abs(allEntities[i].ycor()-fromY) < minDist+allEntities[i].getRadius() &&
		   Math.sqrt( Math.pow(allEntities[i].xcor()-fromX,2)+Math.pow(allEntities[i].ycor()-fromY,2)) < minDist+allEntities[i].getRadius() )
			detected.add(allEntities[i]);
	if (detected.size()>0)
		return (Entity[]) detected.toArray(new Entity[0]);
	return null;
}*/

void makePerception(BasicBody robot)
{
	Collection percepts = new Vector();
	Entity[] allEntities = world.getAllEntities();
	int minDist=robot.getDetectingRange()+robot.getRadius();
	int fromX = robot.xcor();
	int fromY = robot.ycor();

	for (int i=0;i<allEntities.length;i++)
		if(allEntities[i] != robot){
             double dist2=0.0;
             if (Math.abs(allEntities[i].xcor()-fromX) < minDist+allEntities[i].getRadius() &&
               Math.abs(allEntities[i].ycor()-fromY) < minDist+allEntities[i].getRadius() &&
               (dist2=Math.sqrt(Math.pow(allEntities[i].xcor()-fromX,2)+Math.pow(allEntities[i].ycor()-fromY,2)))
                            < minDist+allEntities[i].getRadius() ){
                          Entity e = allEntities[i];
                          Percept p = e.makePercept(e.x-robot.x,e.y-robot.y,dist2-robot.getRadius());
                          if (e instanceof BasicBody){
                            if ((e.getTeam()!=null) && (e.getTeam().equals(robot.getTeam()))){
                                Brain br = ((BasicBody)e).getBrain();
                                if (br != null)
                                    p.setAgent(br.getAddress());
                            }
                          }
                          percepts.add(p);
             }
		}
	if (percepts.size()>0){
	    Percept[] plst = (Percept[]) percepts.toArray(new Percept[0]);
		Arrays.sort(plst,perceptComparator);
	    robot.setPercepts(plst);
	}
	else
		robot.setPercepts(new Percept[0]);
}

final void checkMail()
{
	Message m = nextMessage();
	if ( m != null)
        if (m instanceof InstallMessage) {
            addEntity(((InstallMessage) m).getEntity());
        }
        else if (m instanceof StringMessage){
            String s = ((StringMessage) m).getString();
            if (s.equals("cleanWorld"))
                this.deleteEntities();
            else if (s.equals("resetWorld"))
                this.resetWorld();
        }
		else {
			/*if (m instanceof BackupMessage)
			{
				if(((BackupMessage)m).type == 1)
					Backup.backupGame(this, ((BackupMessage)m).file);
				else if(((BackupMessage)m).type == 2)
				{
					Backup.rebuildFromWSG(this, ((BackupMessage)m).file);
					broadcastMessage(getSimulationGroup(),"observer",new Message());
				}
				else if(((BackupMessage)m).type == 3)
					Backup.toXml(this, ((BackupMessage)m).file);
				else if(((BackupMessage)m).type == 4)
				{
					loadConfig(((BackupMessage)m).file);
					//broadcastMessage(getSimulationGroup(),"observer",new Message());
				}
				else if(((BackupMessage)m).type == 5)
				{
					Backup.makeEditor(this, ((BackupMessage)m).file);
				}
			}
			else */
				if (m instanceof StringMessage)
					makeWalls();
				else
				{
					System.err.println("!!!!!    reset world  !!!!!!!");
					resetWorld();
				}
        }
}

final synchronized void loadConfig(URL address)
{
//	Backup.fromXml(this, address);
//	broadcastMessage(getSimulationGroup(),"observer",new Message());
}

final void resetWorld()
{
       Entity[] erased = world.getAllEntities();
        homes.clear();
        evolutiveEntities.removeAllElements();
        for(int i=0;i<erased.length;i++)
        {
        	erased[i].delete();
        }
        makeWalls();
	//broadcastMessage(getSimulationGroup(),"observer",new Message());
}

}
