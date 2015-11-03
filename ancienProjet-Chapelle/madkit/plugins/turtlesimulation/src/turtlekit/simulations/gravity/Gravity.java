/*
* Gravity.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.gravity;



import turtlekit.kernel.Launcher;
import turtlekit.kernel.Turtle;



/** the gravity simulation is far from based on physic consideration. Just

	some turtles who turn around another special one 

 

  @author Fabien MICHEL

  @version 1.1 6/12/1999 */



public class Gravity extends Launcher

{



 int nbOfPlanet=200;

 int nbOfSun=3;

 int rayonMax=13;

 public void setNbOfPlanet (int add){nbOfPlanet= add;}

 public int getNbOfPlanet(){return nbOfPlanet;}

 public void setNbOfSun (int add){nbOfSun= add;}

 public int getNbOfSun(){return nbOfSun;}

 public void setRayonMax(int add){rayonMax= add;}

 public int getRayonMax(){return rayonMax;}



	public Gravity ()

	{

		setSimulationName("Gravity ");

		setWidth(200);

		setHeight(150);

	}



 public void addSimulationAgents()

{

	Turtle[] ts = new Turtle[nbOfSun];

	for (int i =0;i < nbOfSun;i++)

	{

		Turtle t1 = new BlackHole();

		ts[i] = t1;

		addTurtle(t1);

	}

	for (int i = 0; i < nbOfPlanet; i++)

		addTurtle(new Star(ts,rayonMax));

	addViewer(2);

}



}

