/*
* SoccerViewer.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.soccer;



import java.awt.Color;
import java.awt.Graphics;

import turtlekit.kernel.Turtle;
import turtlekit.kernel.Viewer;





/**SoccerViewer changes the turtle display to obtain a tiny player insteed of a rect,

 

  @author Fabien MICHEL

  @version 1.2 4/1/2000 */



public class SoccerViewer extends Viewer 

{

    public void paintTurtle(Graphics g,Turtle t,int x,int y,int cellSize)

    {

		if (t.mySelf() != 22)

		{

			g.setColor(Color.lightGray);

			g.drawLine(x+1,y,x+2,y);

			g.drawLine(x,y+1,x,y+1);

			g.drawLine(x+3,y+1,x+3,y+1);

			g.setColor(t.getColor());

			g.drawLine(x+1,y+1,x+2,y+1);

			g.drawLine(x+1,y+2,x+2,y+2);

			g.setColor(Color.black);

			g.drawLine(x,y+3,x,y+3);

			g.drawLine(x+3,y+3,x+3,y+3);

		}

		else

		{

			g.setColor(Color.white);

			g.fillOval(x,y,cellSize,cellSize);

		}

	}

}

