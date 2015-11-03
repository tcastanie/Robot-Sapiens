/*
* TermiteLauncher.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.termites; 

import turtlekit.kernel.Launcher;
/** Termite simulation launcher 
  @author Fabien MICHEL
  @version 1.1 6/12/1999 */

public class TermiteLauncher extends Launcher
{

 int nbOfTermites=400;
 float densityRate= (float) 0.5;



/**redefine this method is not compulsory, but it is where you have to initialize
	the simulation variables and specialy the patch variables
	and their properties(evaporation, diffusion...)
	The constructor is where you have to change the default values
	of the simulation parameters using the corresponding accessors
	Default Values are:

	setSimulationName("? NAME ?); //The simulation name corresponds to the Madkit

						 group that will be created for the simulation.

	setWidth(100);
	setHeight(100);
	setCellSize(4);		//on screen size for patches and trutles
	setWrapModeOn(false);
	setCyclePause(10); //cycle pause represents the pause time between
	   two simulation steps. This default quick  pause 
	   is supposed to avoid that the simulation takes all ressources. 
	*/

public TermiteLauncher()
{
	setSimulationName("TERMITES");
	setWidth(150);
	setHeight(110);
}



/**With these accessors this variable will be directly accessible during the simulation
  in the Launcher's properties Box*/

 public void setNbOfTermites (int add){nbOfTermites = add;}
 public int getNbOfTermites(){return nbOfTermites;}

/**With these accessors this variable will be directly accessible during the simulation
  in the Launcher's properties Box, So you can change this value when doing a reset
  to obtain another simulation based on different parameters*/

 public void setDensityRate (float add){densityRate = add;}
 public float getDensityRate(){return densityRate;}



 /** This method is compulsory (abstract in super class).
	It is in this method that the optional agents
	of the simulation (turtles, viewers and observers) have to be added.
	For the termite simulation we add the termites, a default viewer
	and a observer (to initialize the simulation's patches)*/

 public void addSimulationAgents()
 { 
 addViewer(3); // we choose a default viewer with a cell size of 3
 for (int i = 0; i < nbOfTermites; i++) //add the termites with the method addTurtle

	{
      Termite t = new Termite();
	addTurtle(t);
	}
// this method add the PatchInitializer (extends Observer) with no GUI (false) 
 addObserver(new PatchInitializer(densityRate),false);
 }

}









