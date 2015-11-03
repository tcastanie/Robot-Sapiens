/*
* Soccer.java -TurtleKit - A 'star logo' in MadKit
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



import turtlekit.kernel.Launcher;



/** only a test simulation with an basic IA for players.

  @author Fabien MICHEL

  @version 1.1 6/12/1999 */



public class Soccer extends Launcher

{

	int rsp=9,bsp=9;

	boolean animation=false;

	Ball b=null;

	SoccerObserver obs;



	public Soccer ()

	{
		setCyclePause(100);
		
		setSimulationName("SOCCER");

		setWidth(200);

		setHeight(150);

		setCellSize(4);

		setWrapModeOn(false);

	}



 public void setRedShootPower (int add){rsp = add;}

 public int getRedShootPower(){return rsp;}

 public void setBlueShootPower (int add){bsp = add;}

 public int getBlueShootPower(){return bsp;}

 public void setAnimation(boolean add)

 {

	 animation = add;

	 if (obs!=null)

		 obs.animation = add;

 }

 public boolean getAnimation(){return animation;}



 public void addSimulationAgents()

{

	 obs = new SoccerObserver(animation);

	 addObserver(obs,true);

	 addViewer(new SoccerViewer());



	b = new Ball();

	addTurtle(new RedPlayer(40,45,rsp,b)); 

	addTurtle(new RedPlayer(40,65,rsp,b)); 

	addTurtle(new RedPlayer(40,85,rsp,b)); 

	addTurtle(new RedPlayer(40,105,rsp,b)); 

	addTurtle(new RedPlayer(60,50,rsp,b)); 

	addTurtle(new RedPlayer(60,75,rsp,b)); 

	addTurtle(new RedPlayer(60,90,rsp,b)); 

	addTurtle(new RedPlayer(90,50,rsp,b)); 

	addTurtle(new RedPlayer(90,75,rsp,b)); 

	addTurtle(new RedPlayer(90,90,rsp,b)); 

	addTurtle(new RedPlayer(25,75,rsp,b)); 



	addTurtle(new BluePlayer(160,45,bsp,b)); 

	addTurtle(new BluePlayer(160,65,bsp,b)); 

	addTurtle(new BluePlayer(160,85,bsp,b)); 

	addTurtle(new BluePlayer(160,105,bsp,b)); 

	addTurtle(new BluePlayer(140,50,bsp,b)); 

	addTurtle(new BluePlayer(140,75,bsp,b)); 

	addTurtle(new BluePlayer(140,90,bsp,b)); 

	addTurtle(new BluePlayer(110,50,bsp,b)); 

	addTurtle(new BluePlayer(110,75,bsp,b)); 

	addTurtle(new BluePlayer(110,90,bsp,b)); 

	addTurtle(new BluePlayer(175,75,bsp,b)); 

	addTurtle(b);

}



}

