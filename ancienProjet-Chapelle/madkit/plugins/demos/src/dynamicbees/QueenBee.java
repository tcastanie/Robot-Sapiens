/*
* QueenBee.java - DynamicBees, a demo for the probe and watcher mechanisms
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
  @version 2.0
  @author Fabien MICHEL 01/02/2001*/
public class QueenBee extends AbstractBee
{
  static int border = 20;
 
  public QueenBee()
  {
  	super();
  }

 public void buzz()
    {
    	int acc=beeWorld.queenMaxAcc;
    	int vel=beeWorld.queenMaxVel;
       oldp.x = p.x;
      oldp.y = p.y;
 
      dX += randomFromRange(acc);
      dY += randomFromRange(acc);
      // keep speed limited to maximums
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

      // update the queen's position
      p.x += dX;
      p.y += dY;
		
      // check to see if the queen hit the edge
      if (p.x < border || p.x > (beeWorld.screenWidth - border))
	{
	  dX = -dX;
	  p.x += (dX );
	}
      if (p.y < border || p.y > (beeWorld.screenHeight - border))
	{
	  dY = -dY;
	  p.y += (dY );
	}
    }

    public void activate()
    {
	foundGroup("bees");
	requestRole("bees","queen bee");
    }
    
   
}
