/*
* Bee.java - DynamicBees, a demo for the probe and watcher mechanisms
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel
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
package dynamicbees;

/**
  @version 1.0
  @author Olivier Gutknecht
  @author Fabien MICHEL 01/02/2001*/

public class Bee extends AbstractBee
{

    public QueenBee queen;
  
    public Bee()     {  }

    public void setQueenBee(QueenBee qb)
    {
	queen=qb;
	beeColor.setColor(queen.beeColor.currentColor);
    }

    /** The "do it" method called by the activator */
    public void buzz()
    {
	int dtx, dty, dist;		// distances from bee to queen
	
	int acc = beeWorld.beeMaxAcc;
	int vel = beeWorld.beeMaxVel;
	if (queen != null)
	{
		dtx = queen.p.x - p.x;
		dty = queen.p.y - p.y;
	}
	else
	{
		dtx = (int) (Math.random()*5);
		dty = (int) (Math.random()*5);
	}	
	dist = Math.abs(dtx) + Math.abs(dty);
	if (dist == 0) 
	    dist = 1;		// avoid dividing by zero
	oldp.x = p.x;
	oldp.y = p.y;
	// the randomFromRange adds some extra jitter to prevent the bees from flying in formation
	dX += (int)((dtx * acc )/dist) + randomFromRange(2);
	dY += (int)((dty * acc )/dist) + randomFromRange(2);
	
	if (dX > vel)
	    dX = vel;
	else
	    if (dX < -vel)
		dX = -vel;
	
	if (dY > vel)
	    dY = vel;
	else
	    if (dY < -vel)
		dY = -vel;
	
	p.x += dX;
	p.y += dY;
    }  

    public void activate()
    {
	foundGroup("bees");
	requestRole("bees","bee");
    }
}
