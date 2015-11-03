/*
* HomeKiller.java -Warbot: robots battles in MadKit
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
import warbot.kernel.WarbotMessage;

public class HomeKiller extends Brain
{
	String groupName="warbot-";
	String roleName="launcher";

public HomeKiller(){}

public void activate()
{
	groupName=groupName+getTeam();
	randomHeading();
    //println("I am a home killer robot");
    createGroup(false,groupName,null,null);
	requestRole(groupName,roleName,null);
	requestRole(groupName,"mobile",null);
    showUserMessage(true);
}

int waitingForRocket = 0;
int waitingMax=3;

void decrWaitingForRocket(){
	waitingForRocket--;
	if (waitingForRocket<0)
		waitingForRocket=0;
}


public void doIt()
{
    setUserMessage(null);
	decrWaitingForRocket();
	//if (this.getMessageBoxSize()>0)
	//   println(":: received "+this.getMessageBoxSize()+" messages");
	if(!isMoving())
		randomHeading();
	Percept[] detectedEntities=getPercepts();
	if (detectedEntities.length>0){
		for(int i=0;i<detectedEntities.length;i++)
		{   Percept e=detectedEntities[i];
			if (e.getPerceptType().equals("Home")){
				if(!e.getTeam().equals(getTeam())){
                    setUserMessage("destroying, Urg, Urg..");
					launchRocket(towards(e.getX(),e.getY()));
					setHeading(towards(e.getX(),e.getY()));
					waitingForRocket=waitingMax;
					return;
				}
			}
			// tuer les autres... waouh!!!
			if ((e.getPerceptType().equals("RocketLauncher") ||
			    e.getPerceptType().equals("Explorer")) &&
				(waitingForRocket<=0) &&
				(!e.getTeam().equals(getTeam()))){
                    setUserMessage("shooting, Urg, Urg..");
					launchRocket(towards( e.getX(),e.getY()  ));
					waitingForRocket=waitingMax;
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
	// bouffer, miam, miam
	if(detectedEntities.length > 0 && detectedEntities[min].getPerceptType().equals("Food")){
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
	//println("reading my messages");
	WarbotMessage m=null;
	WarbotMessage lastMessage=null;
	while((m = readMessage())!= null){
			 lastMessage=m;
	}
	if (lastMessage != null){
	   String act = lastMessage.getAct();
	   if (act.equals("homeposition")){
		  double xpos = lastMessage.getFromX();
		  double ypos = lastMessage.getFromY();
		  //println("go to the enemy home: "+xpos+", "+ypos);
		  setHeading(towards(xpos,ypos));
          setUserMessage("mission killing base");
	   } else
	   if (act.equals("help")){
		  double xpos = lastMessage.getFromX();
		  double ypos = lastMessage.getFromY();
		  //println("help an explorer "+xpos+", "+ypos);
          setUserMessage("helping friend");
		  setHeading(towards(xpos,ypos));
	   }
	}
	move();
}

}
