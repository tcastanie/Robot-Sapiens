/*
* AbstractPredator.java - A simple reactive agent library
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
package SEdit.Formalisms.World.Preys;

import SEdit.Formalisms.World.*;
import java.io.*;
import madkit.kernel.AbstractAgent;
import java.util.*;

import SEdit.*;

/**
 * Titre :        Preys
 * Description :
 * Copyright :    Copyright (c) 2000
 * Société :
 * @author J. Ferber
 * @version 1.0
 */

 /* The abstract class from which all predators are built
 *
 */
public abstract class AbstractPredator extends AbstractAgent implements Brain {
  public PredatorBody body;

  public void setBody(WorldEntity e){body = (PredatorBody) e;}
  public WorldEntity getBody(){return body;}

  int coeffPredator=500;
  int coeffPrey=10000;

  public int getCoeffPredator(){return coeffPredator;}
  public int getCoeffPrey() {return coeffPrey;}
  public void setCoeffPredator(int a){coeffPredator = a;}
  public void setCoeffPrey(int a){coeffPrey = a;}

  public void delete(){}
  public void setBehaviorFileName(String s){}

  public void activate(){
  		joinGroup("Preys");
  		requestRole("Preys","predator");
		body.setSpeed(10);
  		println("Hello, I'm a Predator!!");
		body.setDetecting(true);
  }

  public void end(){
  	println("Gosh, I'm dead..");
  	body.delete();
  }

	PreyBody getPrey(Vector ents){
		 for(int i=0;i<ents.size();i++){
  	  		WorldEntity e = (WorldEntity) ents.elementAt(i);
  	  		if (e instanceof PreyBody) return((PreyBody) e);
		 }
		 return(null);
	}

    void avoidPredators(Vector ents){
	   Vector2D direction=null;
	   int nPredators=0;
	   PreyBody prey=null;

	   // loop to compute and avoid predators
	   for(int i=0;i<ents.size();i++){
  	  		WorldEntity e = (WorldEntity) ents.elementAt(i);
  	  		if (e instanceof PredatorBody){
				 if (nPredators==0){
					direction=new Vector2D(0,0);
				 }
				 int d = body.getDistanceTo(e);
				 int t = body.getDirectionTo(e);
				 if (d > 0){
				    direction.addPolar(coeffPrey/d,t);
                                    println("I see a predator:" + d + ", " + t+" : "+direction);
				 }
				 nPredators++;
  	  		  }
	   }
	   if (nPredators == 0){
			 body.avoidObstacles(ents);
			 body.randomMove();
       } else {
	      direction.opposite();
		  direction.normalize();
		  // println("dir norm: " + direction);
		  int t = direction.theta();
		  // println("moving dir: " + t);
		  body.setDirection(t);
		  body.move();
	   }
  }

}
