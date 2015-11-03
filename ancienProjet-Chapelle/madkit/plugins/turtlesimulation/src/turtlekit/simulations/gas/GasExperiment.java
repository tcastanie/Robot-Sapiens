/*
* GasExperiment.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.gas;

import turtlekit.kernel.Launcher;

/**  @author Fabien MICHEL

     @version 1.1 6/12/1999 */

public class GasExperiment extends Launcher
{

    int nbOfTurtles=400, wallX=20,holeSize=2;

    public void setNbOfTurtles (int add){nbOfTurtles = add;}

    public int getNbOfTurtles(){return nbOfTurtles;}

    public void setWallX (int add){wallX = add;}

    public int getWallX(){return wallX;}

    public void setHoleSize(int add){holeSize = add;}

    public int getHoleSize(){return holeSize;}

    public GasExperiment()
    {
	setWidth(80);
	setHeight(30);
	setSimulationName("I NEED SPACE");
	setWrapModeOn(false);
    }

    public void addSimulationAgents()
    { 
	for (int i = 0; i <= nbOfTurtles; i++)
	    addTurtle(new Gas(wallX));
	addObserver(new GasObserver(wallX,nbOfTurtles,holeSize),true);  
	addViewer(3);
 }   
}



