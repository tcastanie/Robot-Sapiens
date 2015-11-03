/*
* QueenBeeProbe.java - DistributedBees demo program
* Copyright (C) 1998-2004 P. Bommel, F. Michel
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

/** This probe inspects object properties on Referenceable agents.
  @version 0.2
  @author Pierre Bommel, F. Michel */

public class QueenBeeProbe extends Probe
{
    /**
     * @link aggregation
     * @label tableau
     */
    //QueenBee[] tableDeQueen;

    public QueenBeeProbe(String group, String role)
    {
	super(BeeLauncher.BEE_COMMUNITY, group, role);
    }

    /*public void update()
    {
	tableDeQueen = new QueenBee[agents.length];
	for(int i=0;i<agents.length;i++)
	    tableDeQueen[i]= (QueenBee) agents[i];
    }*/

    public QueenBee[] getQueenBees()
    {
	return (QueenBee[]) getCurrentAgentsList().toArray(new QueenBee[0]);
    }
 }
