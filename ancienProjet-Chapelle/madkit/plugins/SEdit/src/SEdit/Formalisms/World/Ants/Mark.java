/*
* Mark.java - A simple reactive agent library
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


public class Mark extends FixedEntity 
{
  int value=0;
  public int getValue(){return value;}
  public void setValue(int v){value = v;}
  
  int time=120;
  public int getTime(){return time;}
  public void setTime(int v){time = v;}
  
  /** l'activateur des entites */
  public void bodyDoIt(){
  	time--;
  	if (time <= 0){
  		// System.out.println("Mark destroying..");
  		delete();
  	}
  }
  
  public Mark(){
  }
  

}
