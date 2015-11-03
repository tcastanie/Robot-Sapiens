/*
* AbstractEnvironment.java -Warbot: robots battles in MadKit
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

abstract class AbstractEnvironment
{
	private java.util.Collection entities;
 
public AbstractEnvironment()
{
	entities = new java.util.HashSet();
}

synchronized public void addEntity(Entity e)
{
	entities.add(e);
}
synchronized public void removeEntity(Entity e)
{
	entities.remove(e);
}

synchronized public Entity[] getAllEntities()
{
	return (Entity[]) entities.toArray(new Entity[0]);
}

synchronized boolean contains(Entity e)
{
	return entities.contains(e);
}


}
