/*
* VirusObserver.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.virus;

import madkit.guis.SimplePlotPanel;
import turtlekit.kernel.Observer;
import turtlekit.kernel.TurtleProbe;

/** This agent watches the number of infected turtles
    @author Fabien MICHEL
    @version 1.2 31/01/2000 */

public class VirusObserver extends Observer
{
    SimplePlotPanel plot;
    TurtleProbe infectedTurtles;
    int nbMax;

    public VirusObserver(int a){ nbMax= a;}

    public void initGUI()
    {
	setGUIObject(plot = new SimplePlotPanel("infected turtles",250,nbMax));
    }

public void setup()
{
    plot.initialisation();	//GUI initialization
    infectedTurtles = new TurtleProbe(getSimulationGroup(),"infected");
    addProbe(infectedTurtles);

}	  

/**this method overrides watch in the class Observer. 
   So it will be invoked for each simulation step*/
public void watch()
{
    plot.addPoint(infectedTurtles.nbOfTurtles());
}

}











































