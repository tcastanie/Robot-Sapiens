/*
* Prey.java - A simple reactive agent library
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
 * Titre :  Preys
 * Description :
 * Copyright :    Copyright (c) 2000
 * Société :
 * @author: J. Ferber
 * @version 1.0
 */


/** The prey brain
 */
public class Prey extends AbstractAgent implements Brain
{
  public PreyBody body;

  int nbPredators=3;
  public int getNbPredators(){return nbPredators;}
  public void setNbPredators(int n){nbPredators = n;}

  public void setBody(WorldEntity e){body = (PreyBody) e;}
  public WorldEntity getBody(){return body;}

  public void delete(){}
  public void setBehaviorFileName(String s){}


  public void activate(){
  		joinGroup("Preys");
  		requestRole("Preys","prey");
		body.setSpeed(10);
  		println("Hello, I'm a Prey!!");
  }

  public void end(){
  	println("Gosh, I'm dead..");
  	body.delete();
  }

  /** l'activateur des entites */
  public void doIt(){
  	  WorldEntity e;
  	  Vector ents = body.detect();
	  if (surrounded(ents)){
		 body.delete();
		 return;
	  }
  	  body.avoidObstacles(ents);
  	  body.randomMove();
  }

  boolean surrounded(Vector ents){
		  WorldEntity e;
		  int count=0;
		  if (ents != null){
		    for(int i=0;i<ents.size();i++){
		     println("surrounded by: " + ents);
		     e = (WorldEntity) ents.elementAt(i);
			 if (e instanceof PredatorBody){
				count++;
				if (count >= nbPredators)
				   return(true);
			 }
		    }
		  }
		  return(false);
  }

}
