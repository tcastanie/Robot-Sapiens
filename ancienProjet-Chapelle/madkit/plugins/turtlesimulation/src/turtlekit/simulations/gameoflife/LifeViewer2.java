/*
* LifeViewer2.java -TurtleKit - A 'star logo' in MadKit
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

import java.awt.Color;
import java.awt.Graphics;

import turtlekit.kernel.Patch;
import turtlekit.kernel.Viewer;


/**LifeViewer overrides the paintPatch method in order to adjust the onscreen color of a patch
   
   @author Fabien MICHEL
    @version 1.1 23/02/2001 */

public class LifeViewer2 extends Viewer 
{
    public void paintPatch(Graphics g, Patch p,int x,int y,int CellSize)
    {
	if( p.getVariableValue("lifeValue2") == 0)
		g.setColor(Color.black);
	else
		g.setColor(Color.yellow);
	g.fillRect(x,y,CellSize,CellSize);
    }
}
