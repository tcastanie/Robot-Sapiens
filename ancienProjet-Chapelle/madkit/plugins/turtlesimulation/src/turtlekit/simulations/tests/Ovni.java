/*
* Ovni.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.tests;





import java.awt.Color;

import turtlekit.kernel.Turtle;


/** a turtle for a graphic test 


  @author Fabien MICHEL


  @version 1.2 12/2/2000 */





 public class Ovni extends Turtle 


 {





 public Ovni()


  {


	 super("fly");


  }





 public void setup()


  {


  playRole("ovni");


  home();


  randomHeading();


  setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));


  }





public String fly()


 {


	turnRight(15);


	fd(2);


	return ("fly");


 }





}


