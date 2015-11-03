/*
* Walkers.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.tests;





import turtlekit.kernel.Launcher;





/** Launcher for a small test simulation 


 


  @author Fabien MICHEL


  @version 1.1 6/12/1999 */





public class Walkers extends Launcher


{





 int nbOfWalkers=100;


 public void setNbOfWalkers (int add){nbOfWalkers = add;}


 public int getNbOfWalkers(){return nbOfWalkers;}





	public Walkers ()


	{


		setSimulationName("MOON WALK");


	}





 public void addSimulationAgents()


{


	for (int i = 0; i < nbOfWalkers; i++)


		addTurtle(new Walker("walk"));


	


	addViewer(3);


}





}


