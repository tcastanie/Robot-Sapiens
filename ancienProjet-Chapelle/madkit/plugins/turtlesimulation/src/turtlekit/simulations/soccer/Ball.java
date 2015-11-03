/*
* Ball.java -TurtleKit - A 'star logo' in MadKit
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

import turtlekit.kernel.Turtle;



/** the ball

 

  @author Fabien MICHEL

  @version 1.2 6/12/1999 */



 public class Ball  extends Turtle 

 {

     boolean holden = false;

     int xInit,yInit,speed=1,slowDown=10;

     Player holder=null;

     Player lastHolder=null;

     int teamBall=0;

     public int status=-1;



     public Ball()

     {

	 super("move");

     }



  void takenBy(Player p)

  {

	  if (holder != null)

	  {

		  if (holder.mySelf() < 11)  holder.setColor(Color.red);

		  else holder.setColor(Color.blue);

		  holder.ballHolder = false;

	  }

	  holder = p;

	  lastHolder=p;

	  if (holder.mySelf()<11) teamBall=1;

	  else teamBall=2;

	  holden = true;

  }



  void step()

  {

      status=-1;

	  if (getPatchColorAt(dx(),dy()) == Color.black)

	  {

		  if (xcor() > 178)

		  {

			  if (ycor() > 69 && ycor() <80) status = 0;

			  else status = 1;

		  }

		  if (xcor() < 20)

		  {

			  if (ycor() > 69 && ycor() <80) status = 2;

			  else status = 3;

		  }

		  if (ycor() < 24)

		  {

			  status = 3;

		  }

		  if (xcor() > 124)

		  {

			  status = 4;

		  }

	  }

	  

	  if (getPatchColorAt(dx(),dy()) == Color.darkGray)

	      {

			  setHeading(180-getHeading());

	      }



	  if (status == -1) fd(1);

  }



  public String move()

  {

	  if (holden) setXY(holder.xcor(),holder.ycor());

	  else

	  {

	      for(int i=0;i<speed;i++) step();

	  }

	speed = (int) (speed - (speed * (slowDown / 100.0))); 

	return "move";

  }



 /**initialize + paint the center circle*/

public void setup()

  {

  playRole("ball");

  setColor(Color.white);

  moveTo(99,87);

  setHeading(0);

  for(int i=0;i<100;i++)

  {

	  fd(1);turnRight(5);setPatchColor(Color.white);

  }

  moveTo(100,75);

  randomHeading();

  }

}





















