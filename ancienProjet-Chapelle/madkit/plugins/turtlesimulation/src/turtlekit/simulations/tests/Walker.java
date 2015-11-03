/*
* Walker.java -TurtleKit - A 'star logo' in MadKit
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





/** the only thing is to walk and change color 


 


  @author Fabien MICHEL


  @version 1.2 6/12/1999 */





 public class Walker extends Turtle 


 {


 int count=10;





 public Walker(String s)


  {super(s);}





 public void setup()


  {


  randomHeading();


  playRole("walker");


  }





 public String walk()


  {


	fd(1);


   if (count < 0)


      {


      count = (int) (Math.random()*90);


      return("change");


      }


   else


   {


  	  count--;


 	  return ("walk");


   }


}  





 public String change()


  {


  randomHeading();


  if (getHeading() > South) setColor(Color.red);


  else if (getHeading() > West) setColor(Color.blue);


    else if (getHeading() > North) setColor(Color.green);


      else setColor(Color.yellow);


  return("walk");


  }





}


