/*
* Bag.java -Warbot: robots battles in MadKit
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

import java.util.ArrayList;
import java.util.List;

final class Bag implements java.io.Serializable
{
	int capacity;
	private List objects;
	
Bag(int maxCapacity)
{
	capacity=maxCapacity;
	objects=new ArrayList(maxCapacity);
}

boolean put(Entity thing)
{
	if(objects.size() < capacity && ! objects.contains(thing))
	{
		objects.add(thing);
		return true;
	}
	return false;
}

Entity remove(int index)
{
	if(objects.size() > index)
		return (Entity) (objects.remove(index));
	return null;
}

Entity get(int index)
{
	if(objects.size() > index)
		return (Entity) (objects.get(index));
	return null;
}	

boolean contains(Entity e){return objects.contains(e);}

boolean isEmpty(){return objects.isEmpty();}
boolean isFull(){return (objects.size()==capacity);}

int size(){return objects.size();}

Entity[] returnContent()
{
	return  (Entity[]) objects.toArray(new Entity[0]);
}	

}
