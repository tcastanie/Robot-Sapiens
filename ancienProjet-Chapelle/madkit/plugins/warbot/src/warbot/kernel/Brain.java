/*
* Brain.java -Warbot: robots battles in MadKit
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

import java.awt.Color;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Message;
import madkit.kernel.ReferenceableAgent;

/** The main super class of robot developpers.

This class gives all the avaible primitives that brains can use to control their body.
these methods must be invoked inside the <code> doIt </code> method.
Some of them represent actions that cost time and energy.
The <code> doIt </code> method describes what the body has to do in one turn.
Implementation of a robot behavior, by subclassing this <code>Brain</code> class or by
designing Python or Jess scripts must take care of the following features.
<p>Many actions may be done only once per turn. This is true for instance of the methods: <method>move</method>,
<code>eat</code>, <code>take</code>, <code>drop</code>, <code>launchRocket</code>, and
<code>buildRocket</code>.</p>
<p>Only one of these actions may be actually performed per turn, thus calling these methods several times per turn is useless!!
<p>For example :
<pre>
    public void doIt(){
        move();
        move();
        launchRocket(120);
    }
</pre>
<p>will only consists in an attempt to launch a rocket in the direction 120. The two <code>move</code>invocations will be overwritten
by the invocation of the <code>launchRocket</code> action.
<p>
Some methods are only available for a specific type of robot's body.
Only RocketLaunchers may actually
shoot and build rockets. Calling such an action to an Explorer will lead to nothing.

Each type of robot also differs in their speed, initial energy, detecting range.
Information about these attributes may be required with the getter <code>getXXX()</code>
where <code>XXX</code> is the attribute to be known.


To perceive the world a robot has to ask for the <code>getPercepts</code> method that returns
an array of Percepts, i.e. an array of objects which are all instance of the
<code>Percept</code> class. To actually know the type of percept, you must use the
<code>getPerceptType</code> method that returns a String corresponding to the type of entity.
<p> Here are the percept types and their related entity class:
<pre>
    Obstacle        Wall (or PalmTree)
    Food            Hamburger
    Rocket          Rocket
    Explorer        Explorer
    RocketLauncher  RocketLauncher
    Home            Home
</pre>
<p>See the <class>Percept</class> for more information about the use of percepts.

*/

public abstract class Brain extends AbstractAgent implements ReferenceableAgent, java.io.Serializable
{
	private Body myBody=null;
	private String myIcon=null;

public Brain(){}

final void setBody(Body b){myBody=b;}
final Body getBody(){return myBody;}

final void delete(){ killAgent(this); }

/**
 * Method invoked just after creation of the Brain and before the activate() method.
 * Make all initializations which are not based on the agent here...
 */
public void init(){}

/** returns the direction heading to the relative point a,b*/
final public double towards(double a,double b)
{
	if (a == 0 && b == 0) return 0.0;
	if (b < 0)
	    return 180*Math.asin(a / Math.sqrt(Math.pow(a,2)+Math.pow(b,2)))/Math.PI+270;
	else
	    return 180*Math.acos(a / Math.sqrt(Math.pow(a,2)+Math.pow(b,2)))/Math.PI;
}

/** @return the x position of the robot
 *
 */
/*final public int getX(){
	   return (int) Math.round(((BasicBody)getBody()).getX());
}*/

/** @return the y position of the robot
 *
 */
/*final public int getY(){
	   return (int) Math.round(((BasicBody)getBody()).getY());
}*/

/** returns the direction heading to the absolute point a,b*/
/*final public double towardsAbsolute(double a,double b)
{
	double x = ((BasicBody)getBody()).getX();
	double y = ((BasicBody)getBody()).getY();
	return towards(a-x,b-y);
} */

/** returns the distance relative to the absolute point a,b */
/*public double distanceToAbsolute(double a, double b)
{
	double x = ((BasicBody)getBody()).getX();
	double y = ((BasicBody)getBody()).getY();
  	return Math.sqrt( Math.pow(a-x,2) + Math.pow(b-y,2) ) - myBody.getRadius();
} */

/** returns the distance relative to the perceived entity */
public double distanceTo(Percept p)
{
  	//return Math.sqrt( Math.pow(p.getX(),2) + Math.pow(p.getY(),2) ) - myBody.getRadius()-p.getRadius();
  	return p.getDistance();
}

/** returns the distance covered
 *  for the last unit of time. Usually 2 */
public int getCoveredDistance()
{
  	return (int) Math.round(myBody.getCoveredDistance());
}


/////////////////////////////////////////////::
/** This method controls the behavior of a robot. It is called once per turn.
This method must be implemented in subclasses of this class.
*/
public abstract void doIt();

/** Try to move the robot in the world
This action may only be performed once per turn (when the robot is actived),
Thus, it is useless to call it several time in the doIt (it will have no effect)*/
public void move(){	myBody.move();	}

/**Try to eat some Food (Food is a percept like everything in warbot world),
This action may only be performed once per turn (when the robot is actived),
Thus, it is useless to call it several time in the doIt (it will have no effect)
 * */
public void eat(Food fd){	myBody.eat(fd);	}

/**try to put something in the robot's bag.
This action may only be performed once per turn (when the robot is actived),
Thus, it is useless to call it several time in the doIt (it will have no effect)
*/
public void take(Food f){	myBody.take(f);	}

/**
 * @return the team of the robot
 */
public String getTeam()
{
	return myBody.getTeam();
}

/**
 * @eturn the energy level of the robot
 */
  public int getEnergyLevel()
  {
  	return  myBody.getEnergy();
  }

  /* Use this action supposed you have choosed to play the role of "laser robot"
  protected int getLaserRange()
  {
  	return myBody.getLaserRange();
  }
    */

/* @return the energy maximum level of your body.
 When energy is near of it is useless to eat something : your level of energy can not be higher*/
public int getMaximumEnergy()
{
	return myBody.getMaximumEnergy();
}


  public boolean getShot()
  {
  	return myBody.getShot();
  }

/** Put a message into the mailbox and update the relative coordinates of the sender
 *  Kernel action, don't invoke this method yourself...
 *  */
final public void receiveMessage(Message m)
{
    //debug("receiveMessage "+getAddress());
	if (!(m instanceof WarbotMessage))
	   return;
	WarbotMessage msg=(WarbotMessage) m;
        double fromX = msg.getFromX();
	double fromY = msg.getFromY();

	double x = ((BasicBody)getBody()).getX();
	double y = ((BasicBody)getBody()).getY();

	msg.setFromX(fromX - x);
	msg.setFromY(fromY - y);
	super.receiveMessage(msg);

	((BasicBody)getBody()).showLine(x,y,fromX,fromY,Color.blue);

}

  //////////////////////////////action primitives


/**return true if the robot's bag if empty. */
public boolean isBagEmpty()
{
	return 	myBody.isMyBagEmpty();
}

public boolean isMyBagEmpty(){
    return isBagEmpty();
}

/**return true if the robot's bag if full. */
public boolean isBagFull()
{
	return 	myBody.isMyBagFull();
}

public boolean isMyBagFull(){
    return isBagFull();
}

/**return the current nb of objects in the robot's bag. */
public int bagSize()
{
	return 	myBody.nbOfObjectsInMyBag();
}

/**return the capacity of the robot's bag.
Call this supposed that you have a bag :-) and so you are an explorer*/
public int getBagCapacity()
{
	return 	myBody.getBagCapacity();
}

/** returns all the things you have in the robot's bag. Actually your return
 *  an array of percepts, not the real things your robots hold
 */
public Percept[] getBagPercepts()
{
	return myBody.inMyBag();
}

/** Drop and put in the environment the item that the robot got in its bag.
 *  This item is referenced by its index in the bag
 *  Drop the item if it is possible. Do nothing if the index is out of the range
 *  of the bag.
 *  CAUTION: this action does not work yet...
 */
public void drop(int index)
{
	myBody.drop(index);
}

/**try to do the action of launching a rocket in the desired direction
to perform such an action the robot's body must have the ability to launch rockets
(like a RocketLauncher)
As the action move, it is useless to call this method several time in the doIt: at most
just one rocket will be launched. It takes some time to be able to send a new rocket.
For instance it takes 3 turns for a RocketLauncher to be able to send a rocket again
*/
 public void launchRocket(double direction)
{
	myBody.launchRocket(direction);
}

/**try to do the action of building a rocket for future launch. It takes some
 * energy to build a rocket.
This action may only be performed once per turn (when the robot is actived),
Thus, it is useless to call it several time in the doIt (it will have no effect)
 * */
public void buildRocket()
{
	myBody.buildRocket();
}

/** how many rocket do the robot currently hold*/
public int getRocketNumber()
{
	return myBody.getRocketNb();
}

/**set the robot direction, direction*/
public void setHeading(double direction)
{
	myBody.setHeading(direction);
}

/**get the robot's current heading*/
public double getHeading()
{
	return myBody.getHeading();
}

/**
 * Tells if the robot has moved in the preceding turn or not.
 * This method is very handy to know if the robot
 * is blocked in its way or not.
 * @return true if the robot has moved, false elsewhere
 */
public boolean isMoving()
{
	return myBody.moving();
}

/**
 * Gets all the percepts available in the detection range. All percept
 * information are given in relative coordinate to the perceiving robot
@return an array of percepts
@see warbot.kernel.Percept
*/
 public Percept[] getPercepts()
{
	/*Percept[] percepts=myBody.getPercepts();
	for(int i=0;i<percepts.length;i++){
			println(":: ["+i+"] " + percepts[i]);
	}*/
  	return myBody.getPercepts();
}

/** Give a random direction to the robot */
  public void randomHeading()
  {
  	setHeading(Math.random()*360);
  }


  public void showUserMessage(boolean b){
        myBody.setShowUserMessage(b);
  }
  public void setUserMessage(String s){
        myBody.setUserMessage(s);
  }
  public boolean isShowUserMessage(){
        return myBody.isShowUserMessage();
  }

  /**
   * Reads a message in the message box. Equivalent to
     <pre> (WarbotMessage) nextMessage() </pre>
   * @return a WarbotMessage if any, null if none
   */
  final public WarbotMessage readMessage(){
		Message m = this.nextMessage();
		if (m instanceof WarbotMessage)
		    return (WarbotMessage) m;
		else
			return null;
  }

  private void installMsgCoordinates(WarbotMessage m){

	double x = ((BasicBody)getBody()).getX();
	double y = ((BasicBody)getBody()).getY();
	m.setFromX(x);
	m.setFromY(y);

  }
  /** In warbot you can only send messages that are instance of the class WarbotMessage.
  	This in order to do not have the bots exchange object references (yourself for exemple) IT'S A SIMULATION :-) */
  final public void sendMessage(String groupName, String roleName, madkit.kernel.Message m)
  {
  	if (m instanceof WarbotMessage){
	   installMsgCoordinates((WarbotMessage) m);
  		super.sendMessage(groupName, roleName, m);
  	}
  }

  /** In warbot you can only send messages that are instance of the class WarbotMessage.
  	This in order to do not have the bots exchange object references (yourself for exemple) IT'S A SIMULATION :-) */
  final public void sendMessage(madkit.kernel.AgentAddress target, madkit.kernel.Message m)
  {
  	if (m instanceof WarbotMessage){
	   installMsgCoordinates((WarbotMessage) m);
  		super.sendMessage(target, m);
  	}
  }

  /** In warbot you can only send messages that are instance of the class madkit.messages.StringMessage.
  	This in order to do not have the bots exchange object references (yourself for exemple) IT'S A SIMULATION :-) */
  final public void broadcastMessage(String groupName, String roleName, madkit.kernel.Message m)
  {
  	if (m instanceof WarbotMessage){
	   installMsgCoordinates((WarbotMessage) m);
  		super.broadcastMessage(groupName, roleName, m);
  	}
  }

  /**
   * Sends a WarbotMessage with performative m to the target agent
   */
  final public void send(madkit.kernel.AgentAddress target, String m){
		sendMessage(target,new WarbotMessage(m));
  }
  /**
   * Sends a WarbotMessage with performative m and argument a to the target agent
   */
  final public void send(madkit.kernel.AgentAddress target, String m, String a){
		sendMessage(target,new WarbotMessage(m,a));
  }

  /**
   * Sends a WarbotMessage with performative m and arguments a1 and a2 to the target agent
   */
  final public void send(madkit.kernel.AgentAddress target, String m, String a1, String a2){
		sendMessage(target,new WarbotMessage(m,a1,a2));
  }

  /**
   * Sends a WarbotMessage with performative m and an array of String arguments c to the target agent
   */
  final public void send(madkit.kernel.AgentAddress target, String m, String[] c){
		sendMessage(target,new WarbotMessage(m,c));
  }

  /**
   * Sends a WarbotMessage with performative m to all agents with role roleName in group groupName
   */
  final public void broadcast(String groupName, String roleName, String m){
		broadcastMessage(groupName,roleName,new WarbotMessage(m));
  }

  /**
   * Sends a WarbotMessage with performative m and argument a to all agents with
   * role roleName in group groupName
   */
  final public void broadcast(String groupName, String roleName, String m, String a){
		broadcastMessage(groupName,roleName,new WarbotMessage(m,a));
  }

  /**
   * Sends a WarbotMessage with performative m and arguments a1 and a2 to all agents with
   * role roleName in group groupName
   */
  final public void broadcast(String groupName, String roleName, String m, String a1, String a2){
		broadcastMessage(groupName,roleName,new WarbotMessage(m,a1,a2));
  }

    /**
   * Sends a WarbotMessage with performative m and an array of String arguments c to all agents with
   * role roleName in group groupName
   */
  final public void broadcast(String groupName, String roleName, String m, String[] c){
		broadcastMessage(groupName,roleName,new WarbotMessage(m,c));
  }


}
