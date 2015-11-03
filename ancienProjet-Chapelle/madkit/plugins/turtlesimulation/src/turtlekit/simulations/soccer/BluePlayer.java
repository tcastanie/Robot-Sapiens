/*
* BluePlayer.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.soccer;



import java.awt.Color;



/** blue player

 

  @author Fabien MICHEL

  @version 1.2 6/12/1999 */



 public class BluePlayer extends Player 

 {



  public BluePlayer(int a,int b,int sp,Ball ba)

  {

	  super(a,b,sp,ba);

	  xGoal=25;

  }



  boolean takeBall()

  {

	  if (distFromBall() < 1.2)

	  {

		  if (ball.holden && Math.random()<.25)	return false;

		  ball.takenBy(this);

		  ballHolder = true;

		  setColor(Color.cyan);

		  return true;

	  }

	  return false;

  }



  void computeTeamateAndNearest()

  {

	  double d = 1000;

	  int[] t = new int[3];

	  t[0]=-1;t[1]=-1;t[2]=-1;

	  for(int cpt=0;cpt<3;cpt++)

	  {

		  for (int i = 11;i<22;i++)

		  {

			  if (i != mySelf() && i!=t[0] && i!=t[1])

			  {

			  Player p  = (Player) getTurtleWithID(i);

			  if (p !=null && distance(p.xcor(),p.ycor()) < d)

				{

					d = distance(p.xcor(),p.ycor()) ;

					teamNearest[cpt]=p;

					t[cpt]=i;

				}

			  }

		  }

		  d=1000;

	  }

	  d=1000;

	  for (int i = 0;i<11;i++)

	  {

		  Player p  = (Player) getTurtleWithID(i);

		  if (p !=null && distance(p.xcor(),p.ycor()) < d)

			{

				d = distance(p.xcor(),p.ycor()) ;

				otherNearest = p;

			}		  

	  }				

  }



  public String play()

  {

	  computeNearestFromBall();

	  computeDistFromBall();

	  computeTeamateAndNearest();



	  if (whoNearest==this || (whoNearest.mySelf() < 11 && nearerBall && distFB < 15)  )

	  {

		  goForBall();

		  move();

		  if (takeBall()) return "dribble";

		  return "play";

	  }



	  if (whoNearest.mySelf() < 11 || distance(xInit,yInit) > 50)

	  {

		  if (Math.random() < .7) repositioner();

		  move();

		  return "play";

	  }



	  if (Math.random() > .2)  goForGoal();

	  else randomHeading();

	  move();

	  return "play";

  }



 public void setup()

  {

	 moveTo(xInit,yInit);

	 setColor(Color.blue);

	 playRole("bluePlayer");

  }

}















