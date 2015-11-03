/*
* AbstractAnt.java - A simple reactive agent library
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

import SEdit.Formalisms.World.*;


public abstract class AbstractAnt extends MobileEntity
{
  protected boolean carrying=false;
  int cptMax=10;
  int cpt=cptMax;
  WorldEntity home;

  public WorldEntity getHome(){
  	return home;
  }

  public void setHome(WorldEntity p){
  	home = p;
  }

  public AbstractAnt(){
  		randomDir();
  		setDetecting(true);
  		setDetectingRange(80);
  }

  void initCpt(){
  	cpt=cptMax;
  }

  public boolean isCarrying(){ return carrying;}


 public void randomMove(){
	cpt--;
  	if (cpt==0){
  	  initCpt();
  	  randomDir();
  	  move();
  	}
  	else move();
  }

    public void eat(WorldEntity e){
  		if ((e != null) && (e instanceof Food)){
  			int v = ((Food) e).getEnergy();
  			e.delete();
  			// increaseEnergyLevel(v);
  		}
  }

}
