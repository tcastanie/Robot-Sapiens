/*
* TurtleProbe.java -TurtleKit - A 'star logo' in MadKit
* Copyright (C) 2000-2002 Fabien Michel
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
package turtlekit.kernel;

import madkit.kernel.Probe;
/** The TurtleProbe class 
  @author Fabien MICHEL
  @version 3.0 09/10/2001 */

public class TurtleProbe extends Probe
{
	/**build a turtleProbe on the specified group/role couple    */
    public TurtleProbe(String group,String role)
    {
	super(group, role);
    }

/** @return an array of the turtles that handle the group/role couple*/
    synchronized public Turtle[] getTurtles()
    {
    	return (Turtle[]) getCurrentAgentsList().toArray(new Turtle[0]);
    }
    
/** @return the number of turles that handle the group/role couple*/
    synchronized public int nbOfTurtles()
    {
    	return numberOfAgents();
    }
}
