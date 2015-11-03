/*
* CreationObserver.java -TurtleKit - A 'star logo' in MadKit
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

import madkit.guis.SimplePlotPanel;
import madkit.kernel.AgentAddress;
import madkit.linechart.LineChartAgent;
import madkit.linechart.LineChartMessage;
import turtlekit.kernel.Observer;
import turtlekit.kernel.TurtleProbe;

/** This agent watch the number of turtles in the cration simulation. A kind of bench
	to view how many simulated turtles MadKit supports 
  @author Fabien MICHEL
  @version 1.1 6/12/1999 */

public class CreationObserver extends Observer
{
    SimplePlotPanel plot;
	TurtleProbe creators,walkers,ovnis,planets,suns;
	int wall,nb;
	int time = 0;
	AgentAddress linechartAddress;
	LineChartAgent myLinechart;
	
public void initGUI()
{
	setGUIObject(plot = new SimplePlotPanel("total number of turtles",300,5000));
}

public void setup()
{
	plot.initialisation();
	creators = new TurtleProbe(getSimulationGroup(),"creator");
	walkers = new TurtleProbe(getSimulationGroup(),"walker");
	ovnis = new TurtleProbe(getSimulationGroup(),"ovni");
	planets = new TurtleProbe(getSimulationGroup(),"black hole");
	suns = new TurtleProbe(getSimulationGroup(),"star");
	addProbe(creators);
	addProbe(walkers );
	addProbe(ovnis);
	addProbe(planets);
	addProbe(suns);
	myLinechart = new LineChartAgent();
	launchAgent(myLinechart, "turtle counter",true);
	linechartAddress = myLinechart .getAddress();
	sendMessage(linechartAddress, new LineChartMessage("turtle count over time"));
}

public void watch()
{
	/*time++;
	sendMessage(linechartAddress, new LineChartMessage("turtle count over time",time,planets.nbOfTurtles()+suns.nbOfTurtles()+creators.nbOfTurtles()+walkers.nbOfTurtles()+ovnis.nbOfTurtles()));*/
	plot.addPoint(planets.nbOfTurtles()+suns.nbOfTurtles()+creators.nbOfTurtles()+walkers.nbOfTurtles()+ovnis.nbOfTurtles());
}

public void end()
{
	//killAgent(myLinechart);
}


}
