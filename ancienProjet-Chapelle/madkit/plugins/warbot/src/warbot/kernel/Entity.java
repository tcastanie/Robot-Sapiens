/*
* Entity.java -Warbot: robots battles in MadKit
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

import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.Serializable;
import java.util.Vector;

import javax.swing.ImageIcon;

import SEdit.SArrow;
import SEdit.SNode;

public abstract class Entity extends SNode implements Serializable
{
/////////////////////////////////////////////////////////////////////////// SEDIT connection
// relation to SEdit nodes..
    public  Vector getInArrows() {return null;}
    public  Vector getOutArrows() {return null;}
    protected void addOutArrow(SArrow a) {}
    protected void addInArrow(SArrow a){}
    protected boolean isConnectable(SArrow s, boolean isTarget){return(false);}

/////////////////////////////////////////////////////////////////////////// VARIABLES

// fundamental link : the world is assigned at first by the warbot manager or the environment if from config file (every time for now)
transient WarbotEnvironment myWorld=null;

//action mechanism encoding
final protected static int NULL=0;	//ACTION DESCRIPTION

protected int actionIn=5,speed=0,action=NULL;

//localization variables
protected double x=0,y=0;
protected int xdisplay,ydisplay,displaySize;

//physical variables
protected int detectingRange=0;
protected int energy=0;
protected int initialEnergy=0;
protected int radius=0;

//working var
String name="entity";
String team="";
protected boolean showDetect=false;
protected boolean showEnergyLevel=true;

//"what's up" variables
boolean getShot=false;

//graphic
transient ImageIcon myGif=null;

String simulationGroup="Warbot";

void setSimulationGroup(String s){simulationGroup=s;}


public Percept makePercept(double dx,double dy, double dist){
	   Percept p= new Percept(this,dx,dy, dist);
	   return p;
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////

//Constructors
Entity(WarbotEnvironment theWorld,String name,String team,int radius,int energy,int detectingRange)
{
	myWorld=theWorld;
	this.name=name;
	this.team=team;
	setRadius(radius);
	this.energy=energy;
	this.initialEnergy=energy;
	this.detectingRange=detectingRange;
	createDefaultImage();
}

Entity(){

}

void  createDefaultImage()
{
	final ImageIcon tmp = new ImageIcon(Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("warbot/kernel/images/"+team+name+".gif")).getScaledInstance(radius*2,radius*2,Image.SCALE_SMOOTH));
	myGif = tmp;
	//myGif = new ImageIcon(Toolkit.getDefaultToolkit().createImage(ClassLoader.getSystemResource("warbot/kernel/images/"+team+name+".gif")).getScaledInstance(radius*2,radius*2,Image.SCALE_SMOOTH));
}

ImageIcon getImage()
{
	return myGif;
}

/////////////////////////////////////////////////////	INTERNAL PACKAGE CLASSES' ACCESS METHODS
final void setXY(double x,double y)
{
	//setX(x);
	//setY(y);
	this.x = x;
	this.y = y;
	xdisplay = xcor()- radius;
	ydisplay = ycor() - radius;
    this.getGObject().setCenter((int)Math.round(x),(int)Math.round(y));
    //System.out.println("moving to  x:" + x + ", y:"+y);
}

//final void setX(double value){x=value;}
final public double getX(){return x;}
final public void setX(double v){x = v;}
final int xcor(){return (int)x;}
//final void setY(double value){y=value;}
final public double getY(){return y;}
final public void setY(double v){y = v;}
final int ycor(){return (int)y;}

public void setRadius(int value)
{
	radius = value;
	displaySize = radius*2;
}

final public void setDetectingRange (int value){ detectingRange=value;}
final public int getDetectingRange (){return detectingRange;}
final public void setEnvironmentAgent (WarbotEnvironment theEnv){myWorld=theEnv;}
final public WarbotEnvironment getEnvironmentAgent (){return myWorld;}
final public void setEnergy(int value){energy=value;}
final public String getTeam(){return team;}
final public void setTeam(String theTeam){team=theTeam;}
final public String getName(){return name;}
final public void setName(String theName){name=theName;}

final public void setShowDetect(boolean t){showDetect = t;}
final public boolean getShowDetect(){return showDetect;}
public void toggleShowDetect(){showDetect = !showDetect;}


final public void setShowEnergyLevel(boolean t){showEnergyLevel = t;}
final public boolean getShowEnergyLevel(){return showEnergyLevel;}
public void toggleShowEnergyLevel(){showEnergyLevel = !showEnergyLevel;}



public void init(){
    super.init();
    if (x==0 && y==0){
       updateCoordinatesFromGObject();
    }
}

void updateCoordinatesFromGObject(){
    //System.out.println("try to update from GObject: " + getGObject());
    if (this.getGObject() != null){
            Point p = this.getGObject().getCenter();
            x = p.x;
            y = p.y;
    }
}


/////////////////////////////////////////////////////////////////// DISPLAY METHODS
/** Displays the graphic representation of the body.
 */
 /*
public void draw(Graphics g, JPanel pane){
    getImage().paintIcon(pane,g,xdisplay, ydisplay);

}

void drawEnergyLevel(Graphics g, int n, int max){
        int diam = 2*getRadius();
	 	double prop = (double)diam/(double)max;
	 	int v = (int)Math.round(prop*(double)n);
	 	int r = diam-v;
        int x = xcor()-getRadius();
        int y = ycor()-getRadius()-1;

		g.setColor(Color.green);
	 	g.fillRect(x,y-5,v,3);
	 	g.setColor(Color.red);
	 	g.fillRect(x+v,y-5,r,3);
}
*/

/////////////////////////////////////////////////////////////////// ACTION MECHANISM METHODS

public final void setSpeed(int value)
{
	speed=value;
	if(actionIn>speed)
		actionIn=speed;
}
public final int getSpeed(){return speed;}

final boolean willAct()
{
	if(speed>0)
	{
		actionIn--;
		if (actionIn == 0)
		{
			actionIn=speed;
			return true;
		}
	}
	return false;
}

void doIt()
{
	update();

    if (action != NULL)
        doAction();
    if (energy <= 0)
        delete();
}

void update()
{
	getShot=false;
}

abstract void doAction();

//////////////////////////////////////////////////// PARTIAL BODY INTERFACE	3 functions
final public boolean getShot()
{
	return getShot;
}

public int getRadius(){return radius;}

final public int getEnergy(){return energy;}

//////////////////////////////////////////////////// SYSTEM METHODS
// asking from the system
/*void erase()
{
	//myWorld.removeEntity(this);
    delete(); //SEdit modif..
} */

public void delete(){
	myWorld.removeEntity(this);
 	super.delete();
 }

public String toString()
{
	return (super.toString()+"  x= "+xcor()+", y= "+ycor()+" ,radius= "+radius);
}


//////////////////////////////////////////////// internal methods
final boolean contains(int vx,int vy)
{
	return ( (Math.pow(vx-xcor(),2)+Math.pow(vy-ycor(),2)) <= Math.pow(radius,2) );
}

void getMissileShot(int value)
{
	energy-=value;
	getShot=true;
	//System.err.println("my energy is "+energy);
	if(energy<0)
		delete();
}

protected boolean dropEntity(Entity e,double direction)
{
	 return myWorld.dropEntity(this,e,direction);
}

final protected double distanceFrom(Entity e)
{
	if(e !=null)
		return Math.sqrt( Math.pow(e.getX()-x,2) + Math.pow(e.getY()-y,2) ) - radius-e.getRadius();
	else
		return Double.MAX_VALUE;
}

final protected void decreaseEnergyLevel(int value)
  {
  	energy -= value;
  	if (energy<0)
  		delete();
  }
protected void increaseEnergyLevel(int value){energy += value;}

}
