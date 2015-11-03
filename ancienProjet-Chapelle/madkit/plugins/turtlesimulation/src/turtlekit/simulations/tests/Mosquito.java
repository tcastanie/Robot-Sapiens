/*
 * Mosquito.java -TurtleKit - A 'star logo' in MadKit
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
import java.awt.Color;

import turtlekit.kernel.Turtle;

/** a turtle for a turtle command test 
    @author Fabien MICHEL
    @version 1.1 4/1/2000 */

public class Mosquito extends Turtle 
{
    int sunX,sunY,cpt=50;
    public Mosquito(int a,int b)
    {
	super("move");
	sunX=a;
	sunY=b;
    }

    public String move()
    {
	if (Math.random() > 0.5) turnRight(15);
	else turnLeft(15);
	fd(1);
	cpt--;
	if (cpt < 0)
	    {
		setHeading(towards(sunX,sunY));
		return ("fall");
	    }
	else return ("move");
    }

    public String fall()
    {
	fd(1);
	if (distance(sunX,sunY) < 1)
	    {
		cpt = (int) (Math.random()*100);
		return "move";
	    }
	else return "fall";
    }

    public void setup()
    {
	setXY(sunX,sunY);
	setPatchColor(Color.yellow);
	moveTo(sunX+(int) (Math.random()*10),sunY+(int) (Math.random()*10));
	setHeading(Math.random()*360);
	setColor(new Color((int) (Math.random()*256),(int) (Math.random()*256),(int) (Math.random()*256)));
    }
}
