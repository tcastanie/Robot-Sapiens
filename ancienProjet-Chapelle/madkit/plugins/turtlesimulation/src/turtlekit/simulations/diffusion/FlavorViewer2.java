/*
* FlavorViewer2.java -TurtleKit - A 'star logo' in MadKit
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



import java.awt.Color;
import java.awt.Graphics;

import turtlekit.kernel.Patch;
import turtlekit.kernel.Viewer;





/**FlavorViewer2 overrides the paintPatch method in order to adjust the onscreen color

 of a patch to the flavor2's value and then make only the diffusion of flavor2 visible  

	

  @author Fabien MICHEL

  @version 1.2 4/1/2000 */



public class FlavorViewer2 extends Viewer 

{

    public void paintPatch(Graphics g, Patch p,int x,int y,int CellSize)

    {

		/*the choosen color have no special meaning, just make the diffusion effect

		 look likes a shining star*/

		int a = ((int) p.getVariableValue("flavor2"))%255;

		if (a > 0)

			g.setColor(new Color(255,(a+20)%255,0));

		else 

			g.setColor(Color.black);

		g.fillRect(x,y,CellSize,CellSize);

	}

}

