/*
* Prey.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.hunt;

import java.awt.Color;

import turtlekit.kernel.Turtle;

/** A Prey

  @author Fabien MICHEL
  @version 1.1 17/10/2000 */

 public class Prey extends Turtle 
 {

 public Prey()
 {
 	//first behavior to do (here it is the only behavior of this turtle)
	super("live");
 }

public void setup()
{
	playRole("prey");
	randomHeading();
	setColor(Color.white);
	if (countTurtlesHere()>0)
		fd(1);
}

//a behavior
public String live()
{
	if (catched())
		return null;
	turnRight(Math.random()*60);
	turnLeft(Math.random()*60);
	move();
	return "live";
}

void move()
  {
  for (int i = 0;i<4;i++)
	  if (countTurtlesAt(dx(),dy())>0)
	  {
		  if (Math.random() > .5) turnRight(Math.random()*170);
		  else turnLeft(Math.random()*170);
	  }
	  
	// avoid being two on the same patch  
  if (countTurtlesAt(dx(),dy())==0)
	  fd(1);
  }


// test if I'm dead
boolean catched()
{
	int cpt=0;
	for(int i=-1;i<=1;i++)
		for(int j=-1;j<=1;j++)
			if (! (i==0 && j==0) )
			{
				Turtle[] tur = turtlesAt(i,j);
				if (tur!= null && tur.length>0 && tur[0].isPlayingRole("predator")) // instead of "instanceof". So predator can be another java class
					cpt++;
			}
	if (cpt>3) return true;
	return false;
}

}




