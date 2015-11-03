/*
* Termite.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.termites; 
import java.awt.Color;

import turtlekit.kernel.Turtle;

/**Termite 
 
  @author Fabien MICHEL
  @version 1.1 6/12/1999 */

 public class Termite extends Turtle
 {

/** the first time behavior of a turtle will be "searchForChip"*/
public Termite()
{
	 super("searchForChip");
}

/**it is compulsory to redefine this method. Even empty.
   */
public void setup()
{
	setColor(Color.red);
	randomHeading();
}

//////////here begin the methods that are used to define the termite's behavior/////////

  public String getAway() 
  {
  if (getPatchColor() == Color.black)
     return("searchForChip");
  else
   {
	randomHeading();
	fd(20);
	return("getAway");
   }
  }

 public String searchForChip()
  {
	 wiggle();
      if (getPatchColor() == Color.yellow)
	   {
	       setPatchColor(Color.black);
	       fd(20);
	       return("findNewPile");
	   }
       else
		return ("searchForChip");
  }

/**another one step behavior*/
 public String findNewPile()
 {
	 if (getPatchColor() == Color.yellow)
		 return("findEmptyPatch");
	 else
	 {
		 wiggle();
		 return("findNewPile");
	 }
 }

/**findEmptyPatch is a one time step behavior corresponding to a list of actions.
	So this method will be entirely executed, sure that no other turtle
	of the simulation is actived. 
    It returns a String as the behavior that the turtle will take for the next time step*/
 public String findEmptyPatch()
  {
  wiggle();
  if (getPatchColor() == Color.black)
    {
    	setPatchColor(Color.yellow);
    	return("getAway");
    }
  else
	return("findEmptyPatch");
  }

/**this method defines a non atomic command that can be used in each behavior.
  Note that this method does not return a String*/
void wiggle()
{
	fd(1);
	turnRight(Math.random()*45);
	turnLeft(Math.random()*45);
}     

}
