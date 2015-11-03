/*
* Virus2.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.virus;

import java.awt.Color;

import turtlekit.kernel.Turtle;

/** An infected turtle transmits virus, but not by sending a message,
	just by changing the color of the turtles who cross its way
 
  @author Fabien MICHEL
  @version 1.1 6/12/1999 */


 public class Virus2 extends Turtle 
 {
	boolean infected;

 public Virus2(String inf)
 {
	super(inf);
	if (inf.equals("red")) infected = true;
	else infected = false;
 }

 public void setup()
 {
	if (infected)
	{
		playRole("infected");
		setColor(Color.red);
	}
	else
		 setColor(Color.green);
 }

void wiggle()
{
	fd(1);
	turnRight(Math.random()*60);
	turnLeft(Math.random()*60);
}     
 
 public String green()
  {
  if (getColor() == Color.red)
  {
	  playRole("infected");
	  return ("red");
  }
  else
  {
	  wiggle();
	  return ("green");
  }
 }

 public String red()
  {
	 wiggle();
	 Turtle[] ts = turtlesHere();
	 if (ts != null)
		 for (int i=0; i < ts.length;i++)
			 if (ts[i].getColor() == Color.green)
				 ts[i].setColor(Color.red);
	 return("red");
 }


}
