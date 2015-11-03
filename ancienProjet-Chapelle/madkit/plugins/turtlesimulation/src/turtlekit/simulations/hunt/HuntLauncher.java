/*
* HuntLauncher.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.hunt; 

import turtlekit.kernel.Launcher;

/** Hunt simulation launcher 
    
  @author Fabien MICHEL
  @version 1.1 6/12/2000 */

public class HuntLauncher extends Launcher
{

    int nbOfprey=50;
    int nbOfpredator=30;
    int predatorVision=6;
    
    
public HuntLauncher()
{
    setCyclePause(10);
    setSimulationName("HUNT");
    setWidth(200);
    setHeight(200);
}

    public void setNbOfprey (int add){nbOfprey = add;}
    public int getNbOfprey(){return nbOfprey;}
    public void setPredatorVision (int add){predatorVision = add;}
    public int getPredatorVision(){return predatorVision;}
    public void setNbOfpredator (int add){nbOfpredator = add;}
    public int getNbOfpredator(){return nbOfpredator;}
    
    public void addSimulationAgents()
    { 
	setCyclePause(10);
	
	for (int i = 0; i < nbOfprey; i++) //add the prey with the method addTurtle
	    {
		Prey t = new Prey();
		addTurtle(t);
	    }
	for (int i = 0; i < nbOfpredator; i++) //add the predator with the method addTurtle
	    {
	    Predator t = new Predator (predatorVision);
	    addTurtle(t);
	}
	addViewer(3); // we choose a default viewer with a cell size of 3
	
    }
}


