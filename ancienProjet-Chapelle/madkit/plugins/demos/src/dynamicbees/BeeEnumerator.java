/*
* BeeEnumerator.java - DynamicBees, a demo for the probe and watcher mechanisms
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

import madkit.kernel.AbstractAgent;
import madkit.kernel.Probe;
/** This probe is used by the environment to know the bees
  @version 2.0
  @author Fabien MICHEL 01/02/2001*/
public class BeeEnumerator extends Probe
{ 
	BeeEnvironment beeWorld;
	Bee[] bees=new Bee[0];

    public BeeEnumerator(String group, String role,BeeEnvironment bw)
    {
	super(group,role);
	beeWorld=bw;
    }
    
    public void update(AbstractAgent theAgent, boolean added)
    {
    	initialize();
    }
    
    public void initialize()
    {
    	bees = (Bee[]) getCurrentAgentsList().toArray(new Bee[0]);
    	beeWorld.updateBees(bees);
    }
    
    public synchronized Bee[] getBees()
    {
	return bees;
    }
 }
