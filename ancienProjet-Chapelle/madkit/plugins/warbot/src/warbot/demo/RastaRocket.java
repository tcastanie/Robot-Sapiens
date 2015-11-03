/*
* RastaRocket.java -Warbot: robots battles in MadKit
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
package warbot.demo;

import warbot.kernel.Brain;
import warbot.kernel.Food;
import warbot.kernel.Percept;

public class RastaRocket extends Brain
{

public RastaRocket(){}

public void activate()
{
	randomHeading();
    println("I am a rasta rocket robot");
    println("I am heading " + getHeading());
}


public void doIt()
{
	if(! isMoving())
		randomHeading();
	Percept[] detectedEntities=getPercepts();
	if (detectedEntities.length>0)
		for(int i=0;i<detectedEntities.length;i++)
		{   Percept e=detectedEntities[i];
			if (e.getPerceptType().equals("Home"))
			{
				if(!e.getTeam().equals(getTeam()))
				{
					launchRocket(towards( e.getX(),e.getY()  ));
					return;
				}
			}
			if (e.getPerceptType().equals("RocketLauncher") || e.getPerceptType().equals("Explorer"))
			{
				if(!e.getTeam().equals(getTeam()))
				{
					launchRocket(towards( e.getX(),e.getY()  ));
					return;
				}
			}
		}
	int min = 0;
	for(int i=0;i< detectedEntities.length;i++)
	{   Percept e=detectedEntities[i];
		if (distanceTo(e) < distanceTo(detectedEntities[min]) && e.getPerceptType().equals("Food"))
			min=i;
	}
	if(detectedEntities.length > 0){
	    Percept p = detectedEntities[0];
	    if (p.getPerceptType().equals("Food")){
			if(distanceTo(detectedEntities[min]) < 2)
			{
				eat((Food)detectedEntities[min]);
				return;
			}
			else
			{
				setHeading(towards(detectedEntities[min].getX(),detectedEntities[min].getY()));
				move();
				return;
			}
	    }
	}
	move();
}

/* double randomDirection()
{
	return Math.random()*360;
} */

}
