/*
* BeeProbe.java - DistributedBees demo program
* Copyright (C) 1998-2004 O. Gutknecht, P. Bommel, F. Michel
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

package madkit.distributedbees;
import madkit.kernel.Probe;
//import madkit.lib.simulation.ObjectProbe;
/** This probe inspects object properties on Referenceable agents.
  @version 0.2
  @author Olivier Gutknecht */

public class BeeProbe extends Probe
{ 
    /**
     * @link aggregation
     * @label tableau 
     */
    Bee[] table;

    public BeeProbe(String group, String role)
    {
	super(BeeLauncher.BEE_COMMUNITY, group, role);
    }
    /** 
	public void update()
	{
	findFields();
	Object[] bees =  getObjects();
	table = new Bee[bees.length];
	for(int i=0;i<bees.length;i++)
	table[i]= (Bee) bees[i];
	}*/
    
   /* public void update()
    {
	table = new Bee[agents.length];
	for(int i=0;i<agents.length;i++)
	    table[i]= (Bee) agents[i];
    }*/

    public Bee[] getBees()
    {
	return (Bee[]) getCurrentAgentsList().toArray(new Bee[0]);
    }
 }
