/*
* FlavorViewer.java -TurtleKit - A 'star logo' in MadKit
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





/**FlavorViewer overrides the paintPatch method in order to adjust the onscreen color of a patch to the flavor's value and then make the diffusion visible 

   

   @author Fabien MICHEL

   @version 1.2 4/1/2000 */



public class FlavorViewer extends Viewer 

{

    public void paintPatch(Graphics g, Patch p,int x,int y,int CellSize)

    {

	int a = ((int) p.getVariableValue("flavor"))%256;

	g.setColor(new Color(a,a,a));

	g.fillRect(x,y,CellSize,CellSize);

    }

}

