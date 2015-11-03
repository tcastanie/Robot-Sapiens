/*
* Player.java -TurtleKit - A 'star logo' in MadKit
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



/** the abstract class that defines the basic behaviors of all turtles who are soccer players

 

  @author Fabien MICHEL

  @version 1.2 6/12/1999 */



 public abstract class Player extends Turtle 

 {

	 boolean ballHolder = false;

	 int xInit,yInit,shootPower,xGoal,yGoal;

	 Ball ball;

	 boolean nearerBall = false;

	 double distFB=60;

	 Player otherNearest,whoNearest;

	 Player[] teamNearest;



	 public void init(){}





  public Player(int a,int b,int sp,Ball ba)

  {

	  super("play");

	  xInit=a;

	  yInit=b;

	  shootPower=sp;

	  ballHolder=false; 

	  ball = ba;

	  teamNearest=new Player[3];

	  yGoal=75;

  }



  void move()

  {

	  for (int i = 0;i<4;i++)

		  if (countTurtlesAt(dx(),dy())>0 || getPatchColorAt(dx(),dy()) == Color.black)

		  {

			  if (Math.random() > .5) turnRight(Math.random()*90);

			  else turnLeft(Math.random()*90);

		  }

	  if (countTurtlesAt(dx(),dy())==0 && getPatchColorAt(dx(),dy()) != Color.black)

		  fd(1);

  }



  double distFromBall() {return distance(ball.xcor(),ball.ycor());}



  void computeDistFromBall()

  {

	  double d = distFromBall();

	  if (d < distFB) nearerBall = true;

	  else nearerBall = false;

	  distFB=d;

  }



  void goForGoal()

  {

	  setHeading(towards(xGoal,ycor()));

	  if (Math.random() <.5) turnLeft(Math.random()*30);

	  else turnRight(Math.random()*30);

  }



  void goForBall()

  {

	  double ballHeading = ball.getHeading();

	  if (ball.speed < 1 || distFB < 5)

	  {

		  setHeading(towards(ball.xcor(),ball.ycor()));

	  }

	  else 

	  {

		  double anticip=distFB;

      if ( (ballHeading> 337.5 && ballHeading< 360) || (ballHeading> 0 && ballHeading< 22.5) )

		  setHeading(towards(ball.xcor()+anticip,ball.ycor()));

	  else if (ballHeading> 22.5 && ballHeading< 67.5) setHeading(towards(ball.xcor()+anticip, ball.ycor() + anticip));

	  else if (ballHeading> 67.5 && ballHeading< 112.5) setHeading(towards(ball.xcor() ,ball.ycor()+ anticip ));

	  else if (ballHeading> 112.5 && ballHeading< 157.5) setHeading(towards(ball.xcor() - anticip,ball.ycor() + anticip));

	  else if (ballHeading> 157.5 && ballHeading< 202.5) setHeading(towards(ball.xcor() - anticip,ball.ycor()));

	  else if (ballHeading> 202.5 && ballHeading< 247.5) setHeading(towards(ball.xcor() - anticip,ball.ycor() - anticip));

	  else if (ballHeading> 247.5 && ballHeading< 292.5) setHeading(towards(ball.xcor(),ball.ycor() - anticip));

	  else if (ballHeading> 292.5 && ballHeading< 337.5) setHeading(towards(ball.xcor() + anticip,ball.ycor() - anticip));

	  }

  }



/*  void goForGoal()

  {

	  if (Math.random() <.5) turnLeft(Math.random()*20);

	  else turnRight(Math.random()*20);

  }

  */

  void repositioner(){setHeading(towards(xInit+Math.random()*30-15,yInit+Math.random()*30-15));}


  void computeNearestFromBall()
  {
	  double d = 1000;
	  for(int i=0;i<22;i++)

	  {

		  Player p  = (Player) getTurtleWithID(i);

		  if (p.distFromBall() < d)

		  {

			  whoNearest=p;

			  d=p.distFromBall();

		  }

	  }

  }





/*  void computeTeamateAndNearest()

  {

	  double d = 60;

	  int cpt=0;

	  for (int i = 0;i<11;i++)

	  {

		  if (i != mySelf())

		  {

		  Player p  = (Player) getTurtleWithID(i);

		  if (p !=null && distance(p.xcor(),p.ycor()) < d)

			{

				d = distance(p.xcor(),p.ycor()) ;

				teamNearest[cpt]=p;

				cpt++;

				cpt%=3;

				whoNearest = p;

			}

		  }

	  }

	  for (int i = 11;i<22;i++)

	  {

		  Player p  = (Player) getTurtleWithID(i);

		  if (p !=null && distance(p.xcor(),p.ycor()) < d)

			{

				d = distance(p.xcor(),p.ycor()) ;

				whoNearest = p;

			}		  

	  }				

  }					*/



   abstract void computeTeamateAndNearest();



  void shoot(int x,int y,int power)

  {

	  if (mySelf() < 11) setColor(Color.red);

	  else setColor(Color.blue);

	  ballHolder=false;

	  ball.holden=false;

	  ball.holder=null;

	  ball.speed = (int) (Math.random()*power);

	  if (ball.speed < 5) ball.speed=5;

	  if (ball.speed > 8) ball.speed=8;

	  if (Math.random() <.5) ball.setHeading(towards(x,y)+Math.random()*15);

	  else ball.setHeading(towards(x,y)-Math.random()*15);

  }





/*  public String play()

  {

	  computeDistFromBall();

	  computeTeamateAndNearest();

	  System.out.println(this.toString()+" :"+teamNearest.toString()+" dist = "+distFB);

	  if (ballHolder)

	  {

		  int cpt=2;

		  //goForGoal();

		  for(int i=0;i<2;i++)

		  {

			  if (towards(teamNearest[i].xcor(),teamNearest[i].ycor()) < 90 || towards(teamNearest[i].xcor(),teamNearest[i].ycor()) > 270)

				  cpt=i;

		  }

		  double d = distance(teamNearest[cpt].xcor(),teamNearest[cpt].ycor());

		  if (d<shootPower) shoot(teamNearest[cpt].xcor(),teamNearest[cpt].ycor(),(int)d);

		  else shoot(teamNearest[cpt].xcor(),teamNearest[cpt].ycor(),shootPower);

		  return "play";

	  }

	  else

	  {

		  if (distFB > 30 || distFB >= teamNearest[0].distFromBall() || distFB >= teamNearest[1].distFromBall())

			  repositioner();

		  else

		  {

			  goForBall();

			  TakeBall();

		  }

	  }

	  move();

	  return "play";

  }



 public void setup()

  {

	 moveTo(xInit,yInit);

  playRole("redPlayer");

  }		  */



public String dribble()
{
	if (ballHolder)
	{
		computeDistFromBall();
		computeTeamateAndNearest();
		if (distance(xGoal,yGoal) < 25)
		{
			shoot(xGoal,yGoal,shootPower);
			return "play";
		}
		double o1 = towards(otherNearest.xcor(),otherNearest.ycor());
		if (  getHeading() - o1 > 90 || getHeading() - o1 < -90  || distance(otherNearest.xcor(),otherNearest.ycor()) > 6 )
		{
			goForGoal();
			setHeading(getHeading()+Math.random()*20-10);
			move();
			return "dribble";
		}
		else
  			  if (Math.random() > 0.7)
			  {
				  goForGoal();
				  move();
				  return "dribble";
			  }
		int cpt=0;
		double t1;
		for(int i=2;i>=0;i--)
		{
			t1 = towards(teamNearest[i].xcor(),teamNearest[i].ycor()) ;
			if (t1 > 90 && t1 < 270 &&  ( (t1- o1) > 20 || (t1- o1) < -20 ) )
			{
				cpt=i;
				break;

			}

		}

		double d = distance(teamNearest[cpt].xcor(),teamNearest[cpt].ycor());

		if (d<shootPower) shoot(teamNearest[cpt].xcor(),teamNearest[cpt].ycor(),(int)d);

		else shoot(teamNearest[cpt].xcor(),teamNearest[cpt].ycor(),shootPower);

	}

	return "play";

}



}















