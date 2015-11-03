/*
* SpecialViewer.java -TurtleKit - A 'star logo' in MadKit
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





import java.awt.Graphics;

import turtlekit.kernel.Patch;
import turtlekit.kernel.Turtle;
import turtlekit.kernel.Viewer;








/**A viewer that override the paintTurtle method to obtain a different visual effect,


	


   @author Fabien MICHEL


  @version 1.2 4/1/2000 */





public class SpecialViewer extends Viewer 


{


    public void paintTurtle(Graphics g,Turtle t,int x,int y,int cellSize)


    {


		g.setColor(t.getColor());


		g.fillOval(x,y,cellSize*3,cellSize*3);


	}





    public void paintPatch(Graphics g,Patch p,int x,int y,int cellSize)


    {


		g.setColor(p.getColor());


		g.fillRect(x,y,cellSize*3,cellSize*3);


	}





}









































