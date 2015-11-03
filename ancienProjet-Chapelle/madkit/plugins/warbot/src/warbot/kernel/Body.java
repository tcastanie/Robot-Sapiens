/*
* Body.java -Warbot: robots battles in MadKit
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

/** This interface gives all the avaible primitive possible for brains.
these methods can be directly invoked inside the <code> doIt </code> method of a <class> Brain </class>.
Some of them represent actions that cost time and energy.
As the <method> doIt </method> method represents what you want your body to do when it is time to act,
invoke the <method> move </method> method several time in the doIt is useless : your body can do this only once.
<code> move </code>, <code> eat </code>, <code> take </code>, <code> drop </code> , <code> launchRocket </code>
are these methods. You can do one of them when it is your turn to act.
For example :
<code>
public void doIt()
{
	move();
	move();
	launchRocket(120);
}
</code>
Finally the action of the robot will be a try for a rocket launch. (but the two <code> move </code>invocations will
not cost anything (energy) to the Robot, they are overwritten by the invocation of <code> launchRocket </code>.
<p>
Moreover, some methods are only available for a specific type of Body (a Robot).
In fact you can be a rocket launcher or an explorer (before starting a warbot game, you have to associate brain classes
with the robots of your team).
For example, each type of robot can move. But a rocket launcher can launch rocket and can not take things
(it does not have a bag to do this).
On contrary, an explorer can not launch rocket, but it owns a bag that can hold things (only hamburger for now).
So, all the methods that are related to a bag possesion can not be invoke by a rocket launcher.
So a Robot behavior, implemented as a subclass of
<class>Brain</Brain>, must be designed with this in mind.

Each type of robot also differs in their speed, initial energy, detecting range ...

  @version 2.0
  @author
  Fabien MICHEL, Jacques FERBER 19/06/01 00:42
*/
interface Body			// 20 functions
{


	////////////////////////	BASIC ROBOT	15 functions

/**set the robot heading to the direction of value*/
public void setHeading(double direction);

/**get the robot's current heading*/
public double getHeading();

/**suicide*/
public void die();

/**return the current team as a String Object*/
public String getTeam();

/**return all the percepts avaible within the detecetion range.
you have to use the getPerceptType method of an object Percept
to know the kind (class) of one percept*/
public Percept[] getPercepts();

/**try to do the action of move in the world
you can do this action only one time per turn (when the robot is actived),
so it is useless to call it several time in the doIt*/
public void move();

public void eat(Food f);

public int getMaximumEnergy();

/** returns the distance covered during the last time unit. This method may
 *  be called to sum the path taken by agents.
 */
public double getCoveredDistance();

public void setShowUserMessage(boolean b);
public void setUserMessage(String s);
public boolean isShowUserMessage();

////////////////////////////////////////  BASIC BODY

/**return true if the robot's bag if empty. */
public boolean isMyBagEmpty();

/**return true if the robot's bag if full.*/
public boolean isMyBagFull();

/**return the current nb of objects in the robot's bag.*/
public int nbOfObjectsInMyBag();

/**return the capacity of the robot's bag.*/
public int getBagCapacity();

/**try to put something in the robot's bag.
you can do this action only one time per turn (when the robot is actived),
so it is useless to call it several time in the doIt */
public void take(Food f);

/**try to put something out of the robot's bag.
you can do this action only one time per turn (when the robot is actived),
so it is useless to call it several time in the doIt */

public void drop(int index);

public Percept[] inMyBag();
	/////////////////////////////////////////
	////////////////////////	ENTITY	4 functions


/**return the current energy of the robot*/
public int getEnergy();

public boolean getShot();

public boolean moving();

public int getRadius();

/////////////////////////////////////////





////////////////////////////////////////  ROCKETLAUNCHER	2 function

/**try to do the action of launching a rocket in the direction wanted
to do this kind of action you have to be rocket launcher,
this is done by play the role of rocket launcher.
As the action move, it is useless to call this method several time in the doIt: at most
just one rocket will be launched*/
public void launchRocket(double direction);


/**try to do the action of building a rocket for future launch*/
public void buildRocket();

public int getRocketNb();

/*public int getLaserRange();

/** Use this action supposed you have choosed to play the role of "light robot"
public void setDoubleSpeed();

/** Use this action supposed you have choosed to play the role of "light robot"
public void setNormalSpeed();
*/

public void createDefaultImage();
}
