/*
* GasObserver.java -TurtleKit - A 'star logo' in MadKit
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

import java.awt.Color;

import madkit.guis.SimplePlotPanel;
import turtlekit.kernel.Observer;
import turtlekit.kernel.Turtle;
import turtlekit.kernel.TurtleProbe;

/** This agent watch the gas simulation (nb of turtles on right or left) 
  @author Fabien MICHEL
  @version 1.1 6/12/1999 */

public class GasObserver extends Observer
{
    SimplePlotPanel plot;
    TurtleProbe allTurtles;
    int wall,nb,holeSize;

public GasObserver (int xWall,int nbOfTurtle, int holesize)
{
	nb=nbOfTurtle;
	wall=xWall;
	holeSize = holesize;
}

public void initGUI()
{
	setGUIObject(plot = new SimplePlotPanel("gas on the right side of the wall",300,nb));
}

public void setup()
{
	//initialize the GUI
	plot.initialisation();

	//paint the box
	for(int i=0;i<patchGrid[0].length;i++)
		patchGrid[wall][i].setColor(Color.white);
	for (int i=0;i < holeSize;i++)
		patchGrid[wall][envHeight/2-holeSize/2+i].setColor(Color.black);

	//all the turtle play the "turtle" role
	allTurtles = new TurtleProbe(getSimulationGroup(),"turtle");
	addProbe(allTurtles);
}

synchronized public void watch()
{
	int cpt = 0;
	Turtle[] theTurtles = allTurtles.getTurtles();
	for(int i = 0;i < theTurtles.length;i++)
		if (theTurtles[i].xcor()>=wall)
			cpt++;
	plot.addPoint(cpt);
}
}
