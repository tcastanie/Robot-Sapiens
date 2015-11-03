/*
* MovableEntity.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

public abstract class MovableEntity extends Entity
{

/////////////////////////////////////////////////////////////////////////// VARIABLES

//action mechanism encoding
final protected static int MOVE=1;  //ACTION DESCRIPTION

private double coveredDistance=0.0;

public double getCoveredDistance(){return coveredDistance;}

//movement variables
double angle=0,angleCos=1,angleSin=0;
boolean moving=false;
//CONSTRUCTOR
MovableEntity(WarbotEnvironment theWorld,String name,String team,int radius,int energy,int detectingRange)
{
	super(theWorld,name,team,radius,energy,detectingRange);
}

MovableEntity(){ }

/////////////////////////////////////////////////////	INTERNAL PACKAGE CLASSES' ACCESS METHODS
void setMoving(boolean value)
{
	moving=value;
}

double speedFactor = 2.0;

final void doPhysicalMove()
{
	setXY(x+speedFactor*angleCos, y+speedFactor*angleSin);
	//setXY(x+angleCos, y+angleSin);
    coveredDistance=speedFactor;
}

protected void tryMove()
{
	if (energy>0)
	{
		if (myWorld.authorizeMove(this,newX(),newY()))
  		{
  			doPhysicalMove();
  			moving=true;
  		}
   	}
}


////////////////////////////////////////////  MOVEMENT METHODS

final double newX(){return x+angleCos;}
final double newY(){return y+angleSin;}

final double getCosAlpha(){return angleCos;}
final double getSinAlpha(){return angleSin;}

/////////////////////////////////////////////////////////////////// ACTION MECHANISM METHODS
void update()
{
  	setMoving(false);
	getShot=false;
    coveredDistance=0.0;
}


//////////////////////////////////////////////////////////////////// PARTIAL BODY INTERFACE	3 functions

final public void setHeading(double direction)
{
	angle = direction%360;
	if (angle < 0) angle+=360;
	angleSin=Math.sin( (Math.PI*angle)/180);
	angleCos=Math.cos( (Math.PI*angle)/180);
}
final public double getHeading(){return angle;}

final public boolean moving(){	return moving;}

}
