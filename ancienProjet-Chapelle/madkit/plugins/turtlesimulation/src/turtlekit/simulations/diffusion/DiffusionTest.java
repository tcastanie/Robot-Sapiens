/*
* DiffusionTest.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.diffusion;

import turtlekit.kernel.Launcher;
import turtlekit.kernel.PatchVariable;

/** only a test simulation with two viewers to display flavor and flavor2 diffusion
    on differents panels
    
    @author Fabien MICHEL
    @version 1.1 6/12/1999 */

public class DiffusionTest extends Launcher
{
    
    double value=100000000;
    double value2=5E10;
    double valueDiff=0.33;
    double value2Diff=0.1;
    double valueEvap=0;
    double value2Evap=0.02;
    public void setvalue(double add){value= add;}
    public double getvalue(){return value;}
    public void setvalue2(double add){value2 = add;}
    public double getvalue2(){return value2;}
    public void setDiffusionValue(double add){valueDiff = add;}
    public double getDiffusionValue(){return valueDiff;}
    public void setDiffusionValue2(double add){value2Diff = add;}
    public double getDiffusionValue2(){return value2Diff;}
    public void setEvaporationValue(double add){valueEvap = add;}
    public double getEvaporationValue(){return valueEvap;}
    public void setEvaporationValue2(double add){value2Evap= add;}
    public double getEvaporationValue2(){return value2Evap;}
    
    public DiffusionTest ()
    {
	setSimulationName("diffusion test");
	setWidth(50);
	setHeight(50);
    }
    
    
    /**This method is where patch variables and their properties have to be defined.
       Once having define a Flavor Object(new Falvor(name)) and its properties
       (with setDiffuseCoef, setEvaporation and setDefaultValue) you have to add it to
       the patches with the addPatchFlavor method of the Launcher, Be careful use this method
       only when override the initializePatchVariables method.*/
    protected void initializePatchVariables()
    {
	PatchVariable a = new PatchVariable("flavor");
	a.setDiffuseCoef(valueDiff);
	a.setEvapCoef(valueEvap);
	addPatchVariable(a);
	PatchVariable b = new PatchVariable("flavor2");
	b.setDiffuseCoef(value2Diff);
	b.setEvapCoef(value2Evap);
	addPatchVariable(b);
    }
    
    /**No turtles just two viewers to view indenpendantly the diffusions of flavor and flavor2*/
    public void addSimulationAgents()
    {
	addObserver(new GridInitializer(value,value2),false);
	addViewer(new FlavorViewer());
	addViewer(new FlavorViewer2());
    }    
}
