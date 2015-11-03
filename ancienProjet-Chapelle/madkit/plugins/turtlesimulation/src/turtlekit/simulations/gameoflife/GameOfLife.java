/*
* GameOfLife.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.gameoflife;

import turtlekit.kernel.Launcher;
import turtlekit.kernel.PatchVariable;

/** the game of life
    
    @author Fabien MICHEL
    @version 1.1 23/02/2001 */

public class GameOfLife extends Launcher
{
    
    double percentage=0.5;
    boolean doubleGame=false;
    public void setAlivePercentage(double add){percentage= add;}
    public double getAlivePercentage(){return percentage;}
    public void setDoubleGame(boolean add){doubleGame= add;}
    public boolean getDoubleGame(){return doubleGame;}
    
    public GameOfLife ()
    {
	setSimulationName("game of life");
	setWidth(100);
	setHeight(100);
	setCellSize(4);
    }
    
    
    /**This method is where patch variables and their properties have to be defined.
       Once having define a Flavor Object(new Falvor(name)) and its properties
       (with setDiffuseCoef, setEvaporation and setDefaultValue) you have to add it to
       the patches with the addPatchFlavor method of the Launcher, Be careful use this method
       only when override the initializePatchVariables method.*/
    protected void initializePatchVariables()
    {
	PatchVariable a = new PatchVariable("lifeValue");
	addPatchVariable(a);
	if(doubleGame)
	{
		PatchVariable b = new PatchVariable("lifeValue2");
		addPatchVariable(b);
	}
	
		
   }
    
    /**No turtles just two viewers to view indenpendantly the diffusions of flavor and flavor2*/
    public void addSimulationAgents()
    {
	addObserver(new GridPlayer(percentage,doubleGame),false);
	addViewer(new LifeViewer());
	if(doubleGame)
	{
		addViewer(new LifeViewer2());
		addViewer(new LifeViewer3());
	}
	
    }    
}
