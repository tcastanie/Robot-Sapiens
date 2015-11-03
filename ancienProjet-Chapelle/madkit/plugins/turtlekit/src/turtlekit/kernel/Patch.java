/*
* Patch.java -TurtleKit - A 'star logo' in MadKit
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

import java.awt.Color;
import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;


/** The Patch class
  @author Fabien MICHEL
    @version 1.2 20/3/2000 
 */

final public class Patch
{
	TurtleEnvironment world;
	double diffusion;
	double[] variableValue;
	Hashtable marks=null;
	transient Patch neighbors[];
	Collection turtlesHere = new HashSet();
	Color color;
	boolean change = true;

    Patch(TurtleEnvironment w)
    {
	world=w;
	color = Color.black;
    }
    final void setNeighborhood(Patch[] acc){neighbors = acc;}
    
    final void update(int i)
  {
      for (int a=0;a<neighbors.length;a++)
		  variableValue[i]+=neighbors[a].diffusion;
  }

  final void removeAgent(Turtle a)
  {
      change=true;
      turtlesHere.remove(a);
  }

  final void addAgent (Turtle a)
  {
      turtlesHere.add(a);
      a.position=this;
  }

  final int getVariableIndex(String fl){return ((Integer)world.variables.get(fl)).intValue();}

/**set the value of the corresponding patch variable to n,
  use it with observers in the setup method for example*/
  final public void setPatchVariable(String VariableName,double n){change=true;variableValue[getVariableIndex(VariableName)]=n;}
/**add n to the value of the corresponding patch variable*/
  final public void incrementPatchVariable(String VariableName,double n){change=true;variableValue[getVariableIndex(VariableName)]+=n;}
    /**return the value of the corresponding variable*/
  final public double getVariableValue(String VariableName){return variableValue[getVariableIndex(VariableName)];}
  
final public Color getColor(){return color;}
    
final public void setColor(Color c)
    {
	color = c;
	change = true;
    }

final synchronized Turtle[] getOtherTurtles(Turtle t)
{
	Collection c = new HashSet(turtlesHere);
	c.remove(t);
	return (Turtle[]) c.toArray(new Turtle[0]);
}

final synchronized int size()
{
	return turtlesHere.size();
}

  /** Drop a mark on the patch

  @param markName: mark name
  @param value: mark itself, can be any java object*/
  final public void dropMark(String markName, Object value)
  {
	  change = true;
	  if (marks == null)
		  marks = new Hashtable(1);
	  marks.put(markName,value);
  }

  /** get a mark deposed on the patch
	 @return the correponding java object, null if not present*/
  final public Object getMark(String markName){
	  if (marks == null) return null;
	  Object theMark = marks.get(markName);
	  if (theMark != null)
	    {
		change = true;
		marks.remove(markName);
		return theMark;
	    }
	  else
	    return null;
    }
  /** tests if the corresponding mark is present on the patch (true or false)*/
  final public boolean isMarkPresent(String markName )
    {  
      if (marks != null)
		return marks.containsKey(markName);
      else
		return false;
    }

  /** returns the turtles who are on the patch*/
final public Turtle[] getTurtles()
{
	return (Turtle[]) turtlesHere.toArray(new Turtle[0]);
}
    
final public Patch[] getNeighbors(){return neighbors;}
    
final public String toString()
{
	String s="";
	 for(Iterator i = world.variables.keySet().iterator();i.hasNext();)
	 {
		 String var = (String) i.next();
		 double d = getVariableValue(var);
		 s+=(""+var+"="+d+",");
	 }
	return (s+"; ");
}

}
