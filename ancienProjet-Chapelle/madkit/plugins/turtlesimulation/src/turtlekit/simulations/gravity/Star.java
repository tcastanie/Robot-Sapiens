/*
* Star.java -TurtleKit - A 'star logo' in MadKit
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



/** this turtle turns around the nearer BlackKole.

 If two black holes are near it just goes forward  

 

  @author Fabien MICHEL

  @version 1.1 4/1/2000 */



 public class Star extends Turtle 

 {

 int sunX,sunY,rayon;

 Turtle[] holeTab;



 public Star(Turtle[] ts, int rayonMax)

  {

	 super("fall");

	 holeTab = ts;

	 rayon = ((int) (Math.random()*rayonMax))+1;

  }



 public String fall()

 {

	double dist1=50,dist2=50;

	for(int i = 0;i<holeTab.length;i++)	 

	if (distance(holeTab[i].xcor(),holeTab[i].ycor()) < dist1)  

	 {

		 sunX=holeTab[i].xcor();sunY=holeTab[i].ycor();

		 dist2 = dist1;

		 dist1 = distance(holeTab[i].xcor(),holeTab[i].ycor()) ;

	 }

	if (dist2-dist1 > 10  && dist1 < 15)  

	{

		if (distance(sunX,sunY) > rayon+2)

			setHeading(towards(sunX,sunY));

		else

			setHeading(towards(sunX,sunY)-90);

		 if (distance(sunX,sunY) > rayon) turnLeft(15);

		 fd(1);

	 }

	 else	//System.err.println("orientation = "+getHeading());

		fd(4);

	return "fall";

 }



 public void setup()

  {

		int i = (int) (Math.random()*holeTab.length);	 

		moveTo(holeTab[i].xcor()+((int) (Math.random()*10)),holeTab[i].ycor()+((int) (Math.random()*10)));

		randomHeading();

		setColor(Color.white);

		playRole("star");

  }

}

