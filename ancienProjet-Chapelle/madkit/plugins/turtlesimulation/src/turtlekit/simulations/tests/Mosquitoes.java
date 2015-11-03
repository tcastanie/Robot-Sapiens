/*
* Mosquitoes.java -TurtleKit - A 'star logo' in MadKit
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
/** only a test simulation 
   @author Fabien MICHEL
  @version 1.1 6/12/1999 */
public class Mosquitoes extends Launcher


{


 int nbOfMosquitoes=200;


 public void setNbOfMosquitoes(int add){nbOfMosquitoes= add;}


 public int getNbOfMosquitoes(){return nbOfMosquitoes;}

	public Mosquitoes ()


	{


		setSimulationName("Mosquitoes like ligth");


	}





public void addSimulationAgents()


{


	int i = 0;


	for (; i < nbOfMosquitoes/2; i++) addTurtle(new Mosquito(30,30)); 


	for (;i < nbOfMosquitoes; i++) addTurtle(new Mosquito(70,70)); 





	addViewer(4);


}





}


