/*
* Explorer.java -Warbot: robots battles in MadKit
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

public class Explorer extends BasicBody
{


public Explorer(WarbotEnvironment env,Brain b,String team)
{
	super(env,b,"explorer",team,13,5000,60);
	setSpeed(2);
    //if (myBag == null)
	//    myBag=new Bag(1000);
}

public Explorer()
{
    super();
	setDetectingRange(60);
	setSpeed(2);
	maximumEnergy = 5000;
}

public Percept makePercept(double dx, double dy, double d){
	   Percept p = super.makePercept(dx,dy,d);
	   p.setPerceptType("Explorer");
	   return p;
}

void doAction()
{
    //if (getBrain() != null) getBrain().println("trying: " + ACTIONS[action]);
    super.doAction();
}



/*
//////////////////////////////////////// internal methods
void tryPutInMyBag()
{
	//System.err.println("dist reel"+distanceFrom(takeWhat));
	if(takeWhat!=null && myWorld.contains(takeWhat) && takeWhat instanceof Hamburger && distanceFrom(takeWhat) < 2 && myBag.put(takeWhat))
		myWorld.removeEntity(takeWhat);
}

private void putOutOfMyBag(int index)
{
	if(myBag.size()>=index)
	{
		Entity e=myBag.get(index);
		if(e != null && dropEntity(e,0))
			myBag.remove(index);
	}
}

////////////////////////////////////     PARTIAL BODY INTERFACE 6 functions

public Percept[] inMyBag()
{
	Entity[] ents= myBag.returnContent();
	Percept[] percepts=new Percept[ents.length];
	for(int i=0;i<ents.length;i++)
		if (ents[i] instanceof Hamburger)
			percepts[i]=new Food(0,0,0,null,ents[i].getEnergy());
		else
			percepts[i]=null;
	return percepts;
}

public boolean isMyBagEmpty(){return myBag.isEmpty();}
public boolean isMyBagFull(){return myBag.isFull();}
public int getBagCapacity(){return myBag.capacity;}
public int nbOfObjectsInMyBag(){return myBag.size();}

public void drop(int index)
{
	action = DROP;
	dropIndex = index;
} */

}
