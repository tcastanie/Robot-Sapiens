/*
* Turtle.java -TurtleKit - A 'star logo' in MadKit
* Copyright (C) 2000-2002 Fabien Michel
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
package turtlekit.kernel;

import java.awt.Color;
import java.lang.reflect.Method;
import java.util.Vector;

import madkit.kernel.AbstractAgent;
import madkit.kernel.ReferenceableAgent;


/** The Turtle class implements the Turtle commands which are used to move set heading... 
 
    @author Fabien MICHEL
    @version 1.2 4/1/2000 */

public class Turtle extends AbstractAgent implements ReferenceableAgent
{
    /**default direction values: setHeading(East) ~ setHeading(0)*/
    static public final int East=0,NorthEast=45,North=90,NorthWest=135,West=180,SouthWest=225,South=270,SouthEast=315;
    private double angle,x,y,angleCos=1,angleSin=0;
    private int who;
    boolean hidden=false;
    private TurtleEnvironment world;
    private String initMethod;
    private Vector rolePlayed;
        
    Method nextAction = null;
    Color color=Color.red;
    Patch position;
    
    /** the initMethod is the first action (after setup) that the turtle will do*/
    public Turtle()
    {
    	initMethod="defaultAction";
    	randomHeading();
    }
    
    
    //For the Python Mode
    public Turtle(String initMethod)
    {
	this.initMethod=initMethod;
    }
    
    final void setNextAction(Method nextMethod)
    {
	if (nextMethod == null)
		die();
	else
		nextAction= nextMethod;
    }
    
final public String defaultAction(){return "defaultAction";}
    
    final void initialisation(int a,int b,TurtleEnvironment w,int t,Patch pos)
    {
	world =w;
	try
	    {
	Method first = getClass().getMethod(initMethod,null);
	setNextAction(first);
	    }
	catch (Exception e) {System.err.println("Can't find method:"+e+initMethod);}
	who = t;
	position = pos;
	x=a;
	y=b;
    }
    
    /**Madkit kernel usage */
    final public void activate()
    {
	joinGroup(getSimulationGroup());
	requestRole(getSimulationGroup(),"turtle");
	setup();
    }
    /**Madkit kernel usage */
    final public void end()
    {
	leaveGroup(getSimulationGroup());
    }
    
    public void setup(){};
    
    final public void die()	{ world.removeTurtle(this); }
    
    final double normeX(double a){
	if (world.wrap)	
	    if (a>world.x)
		return a%world.x;
	    else
		if (a < 0) return a+world.x;
		else return a;
	else
	    if (a>=(world.x-0.5))
		return world.x-1;
	    else
		if (a<0) return 0;
		else return a;
    }
    
    final double normeY(double a){
	if (world.wrap)	
	    if (a>world.y)
		return a%world.y;
	  else
	      if (a < 0) return a+world.y;
	      else return a;
	else
	  if (a>=(world.y-0.5))
	      return world.y-1;
	  else
	      if (a<0) return 0;
	      else return a;
    }

    final int normeX(int a){
	if (world.wrap)
	    {	
		a %=world.x;
		if (a < 0) return a+world.x;
		else return a;
	    }
	else
	    if (a>=world.x)
		return world.x-1;
	    else
		if (a<0) return 0;
		else return a;
}
    
    final int normeY(int a){
	if (world.wrap)
	    {	
		a %=world.y;
		if (a < 0) return a+world.y;
		else return a;
	  }
	else
	    if (a>=world.y)
		return world.y-1;
	    else
		if (a<0) return 0;
		else return a;
    }
    
    
    public String toString(){return "turtle "+who+" at "+xcor()+" "+ycor()+" : heading="+angle+",color="+color;}
    
    ///////////////////// the turtle command  /////////////////////////////////
    
    /**get the MadKit group of the simulation*/
    public String getSimulationGroup(){return world.simulationGroup;}
    
    /**one way to identify a kind of turtle: give them a Role in the simulation.*/
    public final void playRole(String role)
    {
	if (rolePlayed == null) rolePlayed = new Vector();
	rolePlayed.addElement(role);
	requestRole(getSimulationGroup(),role);
    }
    public final boolean isPlayingRole(String role)
    {
	return (rolePlayed != null && rolePlayed.contains(role));
    }
/**the turtle will no longer play the specified role*/
    public final void giveUpRole(String role)
    {
	leaveRole(getSimulationGroup(),role);
	if (rolePlayed != null)
	    rolePlayed.removeElement(role);
    }
    /**return the current heading of the turtle*/
    public final double getHeading(){return angle;}
     /**set the turtle heading to the value of direction*/
    public final void setHeading(double direction)
    {
	angle = direction%360;
	if (angle < 0) angle+=360;
	angleSin=Math.sin( (Math.PI*angle)/180);
	angleCos=Math.cos( (Math.PI*angle)/180);
    }
    
    public final void setColor(Color c){color=c;}
    public final Color getColor(){return color;}
    /**if true, the turtle hides itself (no draw)*/
    public final void setHidden(boolean b){hidden = b;}
    public final boolean getHidden(){return hidden;}
    public final void setPatchColor(Color c){position.setColor(c);}
    public final Color getPatchColor(){return position.color;}
    /**get the color of the patch situated at (a,b) units away*/
    public final Color getPatchColorAt(int a,int b){return world.getPatchColor(normeX(a+xcor()),normeY(b+ycor()));}
    /**set the color of the patch situated at (a,b) units away*/
    public final void setPatchColorAt(Color c,int a,int b){world.setPatchColor(c,normeX(a+xcor()),normeY(b+ycor()));}
    
    /**turtle move forward*/
    public final void fd(int nb)
    {
    	moveTo(x+angleCos*nb,y+angleSin*nb);
    	
    	/*
	for(int i = 0;i < nb;i++)
	    {
		x = normeX(x+angleCos);
		y = normeY(y+angleSin);
		world.moveTurtle(x,y,this);
	    }*/
    }
    /** turtle move backward*/
    public final void bk(int nb)
    {
    	moveTo(x-angleCos*nb,y-angleSin*nb);
/*		x = normeX(x-angleCos*nb);
		y = normeY(y-angleSin*nb);
		world.moveTurtle(x,y,this);
/*	for(int i = 0;i < nb;i++)
	    {
		x = normeX(x-angleCos);
		y = normeY(y-angleSin);
		world.moveTurtle(x,y,this);
	    }*/
    }
    /** teleport the turtle to patch (a,b).
	Can be used as a jump primitive: MoveTo(xcor()+10,ycor())*/
    /*public final void moveTo(int a,int b)
    {
	x = normeX(a);
	y = normeY(b);
	world.moveTurtle(x,y,this);
    }*/
    
    /** teleport the turtle to patch (a,b).
	Can be used as a jump primitive: MoveTo(xcor()+10,ycor())*/
    public final void moveTo(double a,double b)
    {
	x = normeX(a);
	y = normeY(b);
	world.moveTurtle(x,y,this);
    }
    
    public final void moveTo(int a, int b)
    {
	x = normeX(a);
	y = normeY(b);
	world.moveTurtle(x,y,this);
    }
    
    /**teleport the turtle to the center patch*/ 
    public final void home(){x=world.x/2;y=world.y/2;world.moveTurtle(x,y,this);}
    
    public final void setX(double a)
    {
	x = normeX(a);
	world.moveTurtle(x,y,this);
    }
    public final void setY(double b)
    {
	y = normeY(b);
	world.moveTurtle(x,y,this);
    }
    public final void setXY(double a,double b)
    {
	x = normeX(a);
	y = normeY(b);
	world.moveTurtle(x,y,this);
    }
    /**return the "onscreen distance" between turtle the patch (a,b)*/
    final public double distanceNowrap(double a,double b)
    {
	a = normeX(a);
	b = normeY(b);
	a-=x;
	b-=y;
	return Math.sqrt( a*a + b*b );
    }
    /**returns the distance from the patch (a,b). 
       The "wrapped distance", when wrap mode is on, (around the edges of the screen)
       if that distance is shorter than the "onscreen distance."*/
    public final double distance(double a,double b)
    {
	if (! world.wrap) return distanceNowrap(a,b);
	a = normeX(a);
	b = normeY(b);
	if (Math.abs(a-x) > world.x/2)
	    if (a < x) a+=world.x;
	    else a-=world.x;
	if (Math.abs(b-y) > world.y/2)
	    if (b < y) b+=world.y;
	    else b=b-world.y;
	a-=x;
	b-=y;
	return Math.sqrt( a*a + b*b );
    }
    public final double towardsNowrap(double a,double b)
    {
	a = normeX(a);
	b = normeY(b);
	a-=x;
	b-=y;
	if (a == 0 && b == 0) return 0.0;
	if (b < 0)
	    return 180*Math.asin(a / Math.sqrt(a*a+b*b))/Math.PI+270;
	else
	    return 180*Math.acos(a / Math.sqrt(a*a+b*b))/Math.PI;
    }
    /**returns direction to the patch (a,b).
       If the "wrapped distance", when wrap mode is on, (around the edges of the screen)
       is shorter than the "onscreen distance," towards will report
       the direction of the wrapped path,
       otherwise it while will report the direction of the onscreen path*/
    public final double towards(double a,double b)
    {
	if (! world.wrap) return towardsNowrap(a,b);
	if (distance(a,b) > distanceNowrap(a,b))
	    return towardsNowrap(a,b);
	else 
	    {
		a = normeX(a);
		b = normeY(b);
		if (Math.abs(a-x) > world.x/2)
		    if (a < x) a=a+world.x;
		    else a-=world.x;
		if (Math.abs(b-y) > world.y/2)
		    if (b < y) b=b+world.y;
		    else b=b-world.y;
		a-=x;
		b-=y;
		if (a == 0 && b == 0) return 0.0;
		if (b < 0)
		    return 180*Math.asin(a / Math.sqrt(a*a+b*b))/Math.PI+270;
		else
		    return 180*Math.acos(a / Math.sqrt(a*a+b*b))/Math.PI;
	    }
    }
    
    public final void randomHeading() { setHeading(Math.random()*360); }
    
    /**create a turtle at the creator position (xcor,ycor)
       returns the ID of the new turtle*/
    public final int createTurtle(Turtle t){ return world.addAgent(t,xcor(),ycor());}
    
    public final int xcor(){ return (int) Math.round(x); }
    public final int ycor(){ return (int) Math.round(y); }
    
    public final double realX(){ return x; }
    public final double  realY(){ return y; }
    
    /**return the Turtle with the specified ID, null if not alive*/
    public final Turtle getTurtleWithID(int a)
    {
	if (a<0 || a> world.theTurtles.size() ) return null;
	return ((Turtle) world.theTurtles.elementAt(a));
    }
    
    /**return the x-increment if the turtle were to take one
       step forward in its current heading.*/
    public final int dx()
    {
	return (int) (Math.round(x+angleCos)-Math.round(x));
    }
    /**return the y-increment if the turtle were to take one
       step forward in its current heading.*/
    public final int dy()
    {
	return (int) (Math.round(y+angleSin)-Math.round(y));
    }
    
    public final void turnRight(double a){angle-=a;	setHeading(angle);}
    public final void turnLeft(double a){angle+=a;	setHeading(angle);}
    
    /**return other turtles on the current patch*/
    public final Turtle[] turtlesHere() {return position.getOtherTurtles(this);}
    /**return turtles who are on the patch situated at (a,b) units away*/
    public final Turtle[] turtlesAt(int a,int b){return world.turtlesAt(normeX(a+xcor()),normeY(b+ycor()));}
    
    public final int countTurtlesHere(){return position.turtlesHere.size();}
    /**return the number of turtles in the patch situated at (a,b) units away*/
    public final int countTurtlesAt(int a,int b){return world.turtlesCountAt(normeX(a+xcor()),normeY(b+ycor()));}
    
    /**return the turtle ID*/
    public final int mySelf(){return who;}
    
    public final int getWorldWidth() {return world.x;}
    public final int getWorldHeight(){return world.y;}

    /**return the value of the corresponding patch variable*/
    public final double getPatchVariable(String variableName){return position.getVariableValue(variableName);}
    /**return the value of the patch situated at (a,b) units away*/
    public final double getPatchVariableAt(String variableName,int a,int b){return world.grid[normeX(a+xcor())][normeY(b+ycor())].getVariableValue(variableName);}
    /**set the value of the corresponding patch variable*/
    public final void incrementPatchVariable(String variableName,double value){position.incrementPatchVariable(variableName,value);}
    public final void incrementPatchVariableAt(String variableName,double value,int a,int b){world.grid[normeX(a+xcor())][normeY(b+ycor())].incrementPatchVariable(variableName,value);}
    /** get a mark deposed on the patch
	@return the correponding java object, null if not present*/
    public final Object getMark(String variableName){return position.getMark(variableName);}
    public final Object getMarkAt(String variableName,int a,int b){return world.grid[normeX(a+xcor())][normeY(b+ycor())].getMark(variableName);}
    /** Drop a mark on the patch
	@param markName: mark name
	@param theMark: mark itself, can be any java object*/
    public final void dropMark(String markName,Object theMark){position.dropMark(markName,theMark);}
    final public void dropMarkAt(String markName,Object theMark,int a,int b){ world.grid[normeX(a+xcor())][normeY(b+ycor())].dropMark(markName,theMark);}
    /** test if the corresponding mark is present on the patch (true or false)*/
    public final boolean isMarkPresent(String markName){return position.isMarkPresent(markName);}  
    /** test if the corresponding mark is present on the patch situated at (a,b) units away*/
    public final boolean isMarkPresentAt(String markName,int a,int b){return world.grid[normeX(a+xcor())][normeY(b+ycor())].isMarkPresent(markName);}  
    //////////////////////////////////////////////
}
