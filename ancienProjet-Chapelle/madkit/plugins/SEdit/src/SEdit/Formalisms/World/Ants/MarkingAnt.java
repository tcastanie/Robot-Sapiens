/*
* MarkingAnt.java - A simple reactive agent library
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
package SEdit.Formalisms.World.Ants;

import java.util.*;
import java.awt.*;

import SEdit.Formalisms.World.*;


public class MarkingAnt extends AbstractAnt
{
  int value=0;
  int cptMarkMax=10;
  int cptMark=cptMarkMax;

  void initCpt(){
  	super.initCpt();
  	value=0;
  }
  
  public MarkingAnt(){
  		super();
  }
  
  /** l'activateur des entites */
  public void bodyDoIt(){
  	  WorldEntity e;
  	  Vector ents = detect();
  	  // avoidObstacles(ents);
  	  if (ents != null)
  	  	for(int i=0;i<ents.size();i++){
  	  		e = (WorldEntity) ents.elementAt(i);
  	  		if ((e instanceof Food) && !carrying){
  	  		  if (isTouching(e)){
  	  		  	eat(e);
  	  		  	carrying = true;
  	  		  	initCpt();
  	  		  	return;
  	  		  }
  	  		  else{
  	  			moveTo(e);
  	  		  	initCpt();
  	  			return;
  	  		  }
  	  		}
  	  		else if (e instanceof Home) {
  	  		  if (getHome() == null) setHome(e);
  	  		  if (carrying){
	  	  		   if (isTouching(e)){
	  	  		  	carrying = false;
	  	  		  	((Home) e).addFood();
	  	  		  	initCpt();
	  	  		  	return;
	  	  		  }
	  	  		  else{
  	  		 		dropMark();
	  	  			moveTo(e);
	  	  			return;
	  	  		  }
			  }
  	  		}
  	  	}
	if (carrying){
  	  	WorldEntity h = getHome();
  	  	if (h != null) {
  	  		 dropMark();
  	  		 moveTo(h);
  	  	}
  	  	else randomMove();
  	  }
  	  else {
  	  	if ((ents != null) && (containMarks(ents)))
  	  		followMarks(ents);
  	  	else randomMove();
  	  }
  }
  
 void dropMark(){
  	cptMark--;
  	if (cptMark <= 0){
 		Point p = getLocation();
 		Mark m = (Mark) getStructure().createNode("Mark",p.x,p.y);
 		m.setValue(value++);
 		cptMark=cptMarkMax;
    }
 }

 boolean containMarks(Vector ents){
 	for(int i=0;i<ents.size();i++){
  	  WorldEntity e = (WorldEntity) ents.elementAt(i);
  	  if ((e != null) && (e instanceof Mark))
  	  	return true;
  	} return false;
 }
 
  void followMarks(Vector ents){
  	Mark m=null;
 	for(int i=0;i<ents.size();i++){
  	  WorldEntity e = (WorldEntity) ents.elementAt(i);
  	  if (e instanceof Mark)
  	  	if (m == null)
  	  		m = (Mark) e;
  	  	else if (((Mark) e).getValue() < m.getValue())
  	  			m = (Mark) e;
  	 }
  	 moveTo(m);
 }

}
