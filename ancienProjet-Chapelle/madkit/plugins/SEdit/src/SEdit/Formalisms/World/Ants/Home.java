/*
* Home.java - A simple reactive agent library
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


public class Home extends FixedEntity 
{
  int foodQuant=0;
  /** l'activateur des entites */
  public void bodyDoIt(){;}
  
  public Home(){
  	resetFood();
  }
  
  public void addFood(){
  	foodQuant++;
  	setLabel(""+foodQuant);
  }
  
  // public int getFoodQuant(){return foodQuant();}
  
  public void resetFood(){
  	foodQuant=0;
  	setLabel(""+foodQuant);
  }

}
