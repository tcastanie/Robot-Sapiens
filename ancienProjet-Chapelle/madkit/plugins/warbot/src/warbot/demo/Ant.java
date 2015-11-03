/*
* Ant.java -Warbot: robots battles in MadKit
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
package warbot.demo;

import warbot.kernel.Brain;
import warbot.kernel.Food;
import warbot.kernel.Percept;

public class Ant extends Brain
{
	boolean goalReached=false;

public Ant(){	}

public void activate()
{
	randomHeading();
   // println("I am an ant like robot");
   // println("I am heading " + getHeading());
   this.showUserMessage(true);
}

// simple procs that may be used
void dropAll(){
	 for (int i=0;i<bagSize();i++)
		 drop(i);
}

void takeFood(Food p){
	 if(distanceTo(p) < 2){
	    if(Math.random()<.1) eat((Food)p);
		else  take((Food) p);
	 } else {
	    setHeading(towards(p.getX(),p.getY()));
		move();
	 }
}

public void doIt()
{
	if(! isMoving())
	{
		randomHeading();
		move();
		return;		//exit doIt to avoid doing something else
	}
	if(isBagFull())
		goalReached=true;
	if(isBagEmpty())
		goalReached=false;
	if(goalReached)
	{
		drop(0);
		return;
	}
	Percept[] percepts = getPercepts();
	Percept[] myBag=null;

	if (isBagEmpty())
        setUserMessage(null);
    else
        setUserMessage("bag: "+ bagSize());

    Percept perceptFood=null;
    Percept perceptHome=null;

	if(percepts.length > 0){
      // compute indexes from percepts
	  for(int i=0;i<percepts.length;i++){
		Percept p = percepts[i];
		String pType= p.getPerceptType();
		if (pType.equals("Home") && (perceptHome == null))
           perceptHome = p;
        else if (pType.equals("Food") && (perceptFood == null))
		   perceptFood = p;
	    }
        // use indexes to control behavior
        if ((perceptHome !=null) && (perceptHome.getTeam().equals(getTeam())) && isBagEmpty()){
            setHeading(towards(-perceptHome.getX(),-perceptHome.getY()));
            setUserMessage("leaving base");
            move();
            return;
        }
        if ((perceptFood !=null) && (perceptHome == null)){
            setUserMessage("miam");
            takeFood((Food) perceptFood);
            return;
        }
        if ((perceptHome !=null) && (perceptHome.getTeam().equals(getTeam())) && !isBagEmpty()){
            if(distanceTo(perceptHome) < 2){
                    setUserMessage("drop all");
                    dropAll();
                    return;
            } else {
                    setHeading(towards(perceptHome.getX(),perceptHome.getY()));
                    setUserMessage("going to base");
                    move();
                    return;
            }
        }
    }
    move();
  }


}
