/*
* PatchInitializer.java -TurtleKit - A 'star logo' in MadKit
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

import java.awt.Color;

import turtlekit.kernel.Observer;
/** The only goal of this Observer is to setup the patches for 
	the termite simulation, having its variables: patchGrid, EnvWidth, EnvHeight
 
  @author Fabien MICHEL
  @version 1.0 20/3/2000 */

public class PatchInitializer extends Observer
{
float densityRate;

public PatchInitializer(float density)
{
	densityRate = density;	
}

public void setup()
{
	 for(int i=0;i<envWidth;i++)
		 for(int j=0;j<envHeight;j++)
			if (Math.random() < densityRate)
				patchGrid[i][j].setColor(Color.yellow);
			else
				patchGrid[i][j].setColor(Color.black);
}

}
