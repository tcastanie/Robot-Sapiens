/*
* Palmtree.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

public class Palmtree extends Entity
{

public Palmtree(WarbotEnvironment theWorld,int radius)
{
	super(theWorld,"palmtree","",radius,500,0);
}

public Percept makePercept(double dx, double dy, double d){
	   Percept p = super.makePercept(dx,dy,d);
	   p.setPerceptType("Obstacle");
	   return p;
}
void doAction(){}

}
