/*
* BeeColorProbe.java - DynamicBees, a demo for the probe and watcher mechanisms
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

import java.util.Iterator;

import madkit.kernel.AbstractAgent;
import madkit.simulation.probes.ReflexiveProbe;
/** This probe inspects the beeColor property of an AbstractBee.
  @version 2.0
  @author Fabien MICHEL 01/02/2001*/
public class BeeColorProbe extends ReflexiveProbe
{ 
    BeeColor[] table;

    public BeeColorProbe(String group, String role, String property)
    {
	super(group, role, property);
    }
    
public void initialize()
{
	findFields();
	table = new BeeColor[fields.size()];
	int j=0;
	for(Iterator i = getAgentsIterator();i.hasNext();j++)
		table[j] = (BeeColor) getObject(i.next());
}

public void update(AbstractAgent theAgent, boolean added)
{
	super.update(theAgent,added);
	table = new BeeColor[fields.size()];
	int j=0;
	for(Iterator i = getAgentsIterator();i.hasNext();j++)
		table[j] = (BeeColor) getObject(i.next());
}

public synchronized BeeColor[] getColors()
{
	return table;
}

}
