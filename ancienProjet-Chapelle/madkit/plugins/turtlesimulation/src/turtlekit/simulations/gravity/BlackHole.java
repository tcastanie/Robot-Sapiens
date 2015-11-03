/*
* BlackHole.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.gravity;



import java.awt.Color;

import turtlekit.kernel.Turtle;



/** the "galaxy" center

 

  @author Fabien MICHEL

  @version 1.1 4/1/2000 */



 public class BlackHole extends Turtle 

 {

 int cpt;



 public BlackHole()

  {

	 super("move");

	 cpt=3;

 }



 public void setup()

  {

  playRole("black hole");

  setHeading(towards(getWorldWidth()/2,getWorldHeight()/2));

  setColor(Color.cyan);

  }



/**the only one behavior of what we have call a BlackHole*/

 public String move()

 {

	cpt--;

	if (cpt<0) {	

		cpt=2;

		fd(1);

			}

	return "move";

}



}















