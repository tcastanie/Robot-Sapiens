/*
* MobileEntity.java - A simple reactive agent library
* Copyright (C) 1998-2002 Jacques Ferber
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
package SEdit.Formalisms.World;

import java.io.*;
import java.awt.*;
import madkit.kernel.*;
import java.util.*;

import SEdit.*;
import SEdit.Formalisms.*;
import SEdit.Graphics.*;


public class MobileEntity extends WorldEntity
{

  public static final double convDegGrad = 0.0174533;
  public static final double convGradDeg = 57.29577951;

  /** 	Movement type which supposes that the world is bounded
  */
  public  static int BOUND = 0;

  /** 	Movement type which supposes that the world is toric
  */
  public  static int WRAP = 1;

  /** 	Current movement type. WRAP is the default.
  *		Note: this should be set in the environment and not here...
  */
  protected  int movementType = WRAP;


  private int maxSpeed = 6;
  /** Set the maximum speed at which a body can move */
  public void setMaxSpeed(int r){maxSpeed = r;}

  /** Returns the maximum speed of a body */
  public int getMaxSpeed(){return maxSpeed;}

  private int speed = maxSpeed;

  /** Set the current speed of a body. Its speed cannot be higher than speedMax
  */
  public void setSpeed(int r){
  	 if (r <= maxSpeed)
  	 	speed = maxSpeed;
  	 else
  		speed = r;
  }

  /** get the current speed of a body.
  */
  public int getSpeed(){return speed;}

  int direction=0;
  /** 	Set the current direction of a body. In degree.
  *		0 degree heads towards the right.
  */
  public void setDirection(int r){direction = r;}
  public int getDirection(){return direction;}

  int detectingRange=40;
  boolean detecting=false;

  /** Set if the body can detect entities or not
  */
  public void setDetecting(boolean b){detecting=b;}

  /** indicates whether the body detects an entity or not
  */
  public boolean isDetecting() {return detecting;}

  /** Set the detecting rang.
  */
  public void setDetectingRange(int r){detectingRange = r;}

  /** Get the current detecting range
  */
  public int getDetectingRange(){return detectingRange;}

  /* Initialization of a mobile entity.
  *	 Takes its brain class and tries to instantiate an
  *	 agent with this brain class if it is not null.
  *	 Sets its speed = speedMax.
  */
  public void init(){
  		setSpeed(getMaxSpeed());
  		String s = getBrainClass();
  		if (s != null){
  			makeBrain(s, null, false,getBehaviorFileName());
  		}
  }


  // les actions possibles
  void moveDir(int dx, int dy){
  		Rectangle d=getEnvironment().getWorldDimension();
  		if (movementType == WRAP)
  			getGObject().translateWrap(dx,dy,d);
  		else if (movementType == BOUND)
  			getGObject().translateBound(dx,dy,d);
  		else getGObject().translate(dx,dy);
  }

  /** Moves the body at its speed
  */
  public void move(){
      int dx = (int)(Math.round(speed*Math.cos(convDegGrad*direction)));
      int dy = (int)(Math.round(speed*Math.sin(convDegGrad*direction)));

      moveDir(dx, dy);
  }

  /** Heads the body into the direction of an entity e
  */
  public void directTowards(WorldEntity e){
      if (e != null){
  		int d = getDirectionTo(e);
  		direction = d;
  	 }
  }

  /** 	Computes and returns the direction which heads to the position of an entity e.
  */
  public int getDirectionTo(WorldEntity e){
    if (e != null){
  	  Point p = e.getGObject().getCenter();
  	  Point c = getGObject().getCenter();
  	  // System.out.print(":: old: " + direction);
  	  int dx = p.x - c.x;
  	  int dy = p.y - c.y;
  	  // System.out.print(", dx:"+dx+", dy: " + dy);
  	  double dir = Math.atan(((double) dy)/(double) dx);
  	  int direct = (int)Math.round((dir)*convGradDeg);
  	  if ((direct < 0)||(dy < 0)) {
  	  //		System.out.print(", ang: " + direction);
  	  	 if (dx > 0)
  	  	 	direct = 360+direct;
  	  	 else
  	  		direct = 180+direct;
  	  }
  	  return direct;
  	 }
  	 return (int)(Math.round(Math.random() * 360));
  }

  /** 	Computes and returns the direction which heads to a position given by a Point p.
  */
  public int getDirectionTo(Point p){
  	  Point c = getGObject().getCenter();
  	  // System.out.print(":: old: " + direction);
  	  int dx = p.x - c.x;
  	  int dy = p.y - c.y;
  	  // System.out.print(", dx:"+dx+", dy: " + dy);
  	  double dir = Math.atan(((double) dy)/(double) dx);
  	  int direct = (int)Math.round((dir)*convGradDeg);
  	  if ((direct < 0)||(dy < 0)) {
  	  //		System.out.print(", ang: " + direction);
  	  	 if (dx > 0)
  	  	 	direct = 360+direct;
  	  	 else
  	  		direct = 180+direct;
  	  }
  	  return direct;
  }

  /** 	Computes and returns the distance between the mobile entity
  *		and an entity e.
  */
    public int getDistanceTo(WorldEntity e){
  	 if (e != null){
	  	  Point p = e.getGObject().getCenter();
	  	  Point c = getGObject().getCenter();
	  	  // System.out.print(":: old: " + direction);
	  	  int dx = p.x - c.x;
	  	  int dy = p.y - c.y;
	  	  // System.out.print(", dx:"+dx+", dy: " + dy);
	  	  double dist = Math.sqrt(dx*dx+dy*dy);
	  	  return ((int)Math.round(dist));
  	 } return(-1);
  }

   /** 	Move in the direction of an entity e.
   *	This is a simple combination of directTowards and move.
   */
  public void moveTo(WorldEntity e){
  	  directTowards(e);
  	  move();
  }

   /** 	Returns a vector of all entities detected within the current detecting range.
   */
  public Vector detect(){
  	Point c = getGObject().getCenter();
  	return getEnvironment().detectEntities(this,c,detectingRange);
  }

  /** 	Avoid obstacles by bouncing on them. This is a very bad method and its
   *	behavior will soon be modified...
   */
  public void avoidObstacles(Vector ents){
  	 if (ents != null){
  	  	for(int i=0;i<ents.size();i++){
  	  		WorldEntity e = (WorldEntity) ents.elementAt(i);
  	  		if ((e instanceof Obstacle) || (e instanceof MobileEntity)){
  	  		   if (isTouching(e)) {
  	  			// a simple bounce with a bit of random
  	  			direction = ((direction + 180) + (int) Math.round(10)) % 360;
  	  			return;
  	  		   }
  	  		}
  	  	}
  	  }
  }


  /**	Indicates if this mobile entity touches a world entity
  */
  public boolean isTouching(WorldEntity e){
  	 return getGObject().getBounds().intersects(e.getGObject().getBounds());
  }

  /** 	Gives a random direction to the body. This method does not use
  *		the randomCptMax parameter
  */
  public void randomDir(){
      direction = (int)(Math.round(Math.random() * 360));

  }

    private int randomCptMax=10;
	private int cpt=randomCptMax;

	public void setRandomCptMax(int n){randomCptMax = n;}

  /** moves a body into a random direction.
      The body goes into the same direction randomCptMax time unit.
      The parameter randomCptMax may be modified using the setRandomCptMax method */
	public void randomMove(){
        cpt--;
  	    if (cpt==0){
		  cpt=randomCptMax;
  	      randomDir();
  	      move();
		}
  	    else move();
    }

}
