/*
* Flavor.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.kernel;

/**Use this class to create a numeric patch variable "flavor" (double). Having created one,
	you can set its evaporation and diffusion coefficients to a specific percentage
	(0 <= coef <= 1). You can also give a default value for this flavor (when the simualtion
	starts each patch has its own flavor with the given default value).
	Evaporation simply consists in deacrease the value.
	Diffusion makes each patch share a percentage of a value with
	its eight neighboring patches.
	@author Fabien MICHEL
	@version 1.2 4/1/2001
*/
final public class Flavor
{
	double evaporation=0;
	double diffCoef=0;
	String n;
	double defaultV=0;

	public Flavor(String name)
	{
		n=name;
	}
	final public void setEvaporation(double evapCoef)
	{
		evaporation=evapCoef;
	}
	final public void setDiffuseCoef(double diffuseCoef)
	{
		diffCoef=diffuseCoef;
	}
	final public void setDefaultValue(double defaultValue)
	{
		defaultV= defaultValue;
	}
}
