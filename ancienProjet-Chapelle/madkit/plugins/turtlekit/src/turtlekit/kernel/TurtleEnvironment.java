/*
* TurtleEnvironment.java -TurtleKit - A 'star logo' in MadKit
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import madkit.kernel.AbstractAgent;
import madkit.kernel.ReferenceableAgent;

/**This Agent is the one who creates turtles,patches and who cares about managing them all (creation, death...)

  @author Fabien MICHEL
  @version 3.0 09/10/2001  */

public class TurtleEnvironment extends AbstractAgent implements ReferenceableAgent
{
    Map variables=null;
    Map diffuseCoef=null;
    Map evapCoef=null;
    public Patch grid[][] = null;
    int x, y;
    int turtlesCount=-1;
    String simulationGroup;
    boolean wrap=true;
    Vector theTurtles = new Vector();

    public TurtleEnvironment (int width, int height, String group)
    {
	x = width;
	y = height;
 	grid = new Patch[x][y];
	initGrid();
 	this.simulationGroup = group;
    }

    public void displayOff(){
	for (int i=0; i < x; i++)
	    for (int j=0; j < y; j++)
		if (grid[i][j].change) grid[i][j].change=false;}

    public void displayOn(){
	for (int i=0; i < x; i++)
	    for (int j=0; j < y; j++)
		grid[i][j].change=true;}

    final void addVariables(String[] var,double[] values)
    {
	variables = new HashMap(var.length);
	for(int i=0;i<var.length;i++) variables.put(var[i],new Integer(i));
	for (int i=0; i < x; i++)
	    for (int j=0; j < y; j++)
		{
		    double[] val = new double[values.length];
		    for(int k=0;k<var.length;k++) val[k]=values[k];
		    grid[i][j].variableValue = val;
		}
    }

    final void diffuseVariables(String[] var,double[] dc)
    {
	diffuseCoef = new HashMap(var.length);
	for(int i=0;i<var.length;i++)
	    diffuseCoef.put(var[i],new Double(dc[i]));
    }

    final void evapVariables(String[] var,double[] dc)
    {
	evapCoef = new HashMap(var.length);
	for(int i=0;i<var.length;i++)
	    evapCoef.put(var[i],new Double(dc[i]));
    }

    final void initNeighborhood()
    {
	Patch acc[];
	if (wrap)
	    for (int i=0; i < x; i++)
		for (int j=0; j < y; j++)
		    {
			acc = new Patch[8];
			acc[0]=grid[(i+1) % x][j];
			acc[7]=grid[(i+1) % x][(j-1+y) % y];
			acc[6]=grid[i][(j-1+y) % y];
			acc[5]=grid[(i-1+x) % x][(j-1+y) % y];
			acc[4]=grid[(i-1+x) % x][j];
			acc[3]=grid[(i-1+x) % x][(j+1) % y];
			acc[2]=grid[i][(j+1) % y];
			acc[1]=grid[(i+1) % x][(j+1) % y];
			grid[i][j].setNeighborhood(acc);
		    }
	else
	    {
		for (int i=0; i < x; i++)
		    for (int j=0; j < y; j++)
			{
			    if (i==0)
				if (j> 0 && j< y-1)
				    {
					acc = new Patch[5];
					acc[0]=grid[i+1][j];
					acc[1]=grid[i+1][j+1];
					acc[2]=grid[i][j+1];
					acc[3]=grid[i][j-1];
					acc[4]=grid[i+1][j-1];
				    }
				else
				    {
					if (j==0)
					    {
						acc= new Patch[3];
						acc[0]=grid[i+1][j];
						acc[1]=grid[i+1][j+1];
						acc[2]=grid[i][j+1];
					    }
					else
					    {
						acc= new Patch[3];
						acc[0]=grid[i+1][j];
						acc[1]=grid[i][j-1];
						acc[2]=grid[i+1][j-1];
					    }
				    }
			    else 
				if (i==(x-1))
				    if (j> 0 && j< y-1)
					{
					    acc = new Patch[5];
					    acc[0]=grid[i][j+1];
					    acc[1]=grid[i-1][j+1];
					    acc[2]=grid[i-1][j];
					    acc[3]=grid[i-1][j-1];
					    acc[4]=grid[i][j-1];
					}
				    else
					{
					    if (j==0)
						{
						    acc= new Patch[3];
						    acc[0]=grid[i][j+1];
						    acc[1]=grid[i-1][j+1];
						    acc[2]=grid[i-1][j];
						}
					    else
						{
						    acc= new Patch[3];
						    acc[0]=grid[i-1][j];
						    acc[1]=grid[i-1][j-1];
						    acc[2]=grid[i][j-1];
						}
					}
				else
				    if (j==0)
					{
					    acc = new Patch[5];
					    acc[0]=grid[i+1][j];
					    acc[1]=grid[i+1][j+1];
					    acc[2]=grid[i][j+1];
					    acc[3]=grid[i-1][j+1];
					    acc[4]=grid[i-1][j];
					}
				    else
					if (j==y-1)
					    {
						acc = new Patch[5];
						acc[0]=grid[i+1][j];
						acc[1]=grid[i-1][j];
						acc[2]=grid[i-1][j-1];
						acc[3]=grid[i][j-1];
						acc[4]=grid[i+1][j-1];
					    }
					else
					    {
						acc = new Patch[8];
						acc[0]=grid[i+1][j];
						acc[7]=grid[i+1][j-1];
						acc[6]=grid[i][j-1];
						acc[5]=grid[i-1][j-1];
						acc[4]=grid[i-1][j];
						acc[3]=grid[i-1][j+1];
						acc[2]=grid[i][j+1];
						acc[1]=grid[i+1][j+1];
					    }
			    grid[i][j].setNeighborhood(acc);
			}
	    }
    }
    
    final void initGrid()
    {
	for (int i=0; i < x; i++)
	    for (int j=0; j < y; j++)
		    grid[i][j] = new Patch(this);
    }
    
    
final public void diffusion()
{
	if (diffuseCoef != null)
	{
		for(Iterator z = diffuseCoef.entrySet().iterator();z.hasNext();)
		{
			Map.Entry e = (Map.Entry) z.next();
			double coef = ((Double) e.getValue()).doubleValue(); 
			int index = ((Integer) variables.get(e.getKey())).intValue();
			if (wrap)
			{
				double give = coef/8;
				for (int i=0; i < x; i++)
					for (int j=0; j < y; j++)
					{
						grid[i][j].diffusion=grid[i][j].variableValue[index]*give;
						grid[i][j].variableValue[index]-=grid[i][j].variableValue[index]*coef;
					}
			}
			else
				for (int i=0; i < x; i++)
					for (int j=0; j < y; j++)
					{
						grid[i][j].diffusion=grid[i][j].variableValue[index]*(coef/grid[i][j].neighbors.length);
						grid[i][j].variableValue[index]-=grid[i][j].variableValue[index]*coef;
					}
			for(int i=0; i < x; i++)
				for (int j=0; j < y; j++)
					grid[i][j].update(index);
		}
		for(int i=0; i < x; i++)
			for (int j=0; j < y; j++)
				grid[i][j].change=true;
	}
}
		
		
final public void evaporation()
{
	if (evapCoef != null)
	{
		for(Iterator z = evapCoef.entrySet().iterator();z.hasNext();)
		{
			Map.Entry e = (Map.Entry) z.next();
			double coef = ((Double) e.getValue()).doubleValue(); 
			int index = ((Integer) variables.get(e.getKey())).intValue();
			for (int i=0; i < x; i++)
				for (int j=0; j < y; j++)
					grid[i][j].variableValue[index]-=grid[i][j].variableValue[index]*coef;
		}
		for(int i=0; i < x; i++)
			for (int j=0; j < y; j++)
				grid[i][j].change=true;
	}
}

    final synchronized void finalReset()
    {
	for(Enumeration e = theTurtles.elements();e.hasMoreElements();)
	    {
		Turtle t = (Turtle) e.nextElement();
		if (t != null) t.die();
	    }
    }

    final int createAgent(Turtle agt, int a, int b)
    {
	turtlesCount++;
	agt.initialisation(a,b,this,turtlesCount,grid[a][b]); 
	grid[a][b].addAgent(agt);
	theTurtles.addElement(agt);
	launchAgent(agt,"turtle"+turtlesCount,false);
	return turtlesCount;
    }
    final int addAgent(Turtle agt){return createAgent(agt, (int) (Math.random() * x), (int) (Math.random() * y) );}
    final int addAgent(Turtle agt, int u, int t) {	return createAgent(agt, u%x, t%y);  }

    final void removeTurtle(Turtle t)
    {
	(t.position).removeAgent(t);
	theTurtles.setElementAt(null,t.mySelf());
	killAgent(t);
    }

final Turtle[] turtlesAt(int u,int z)
{
	return grid[u][z].getTurtles();
}

final int turtlesCountAt(int u, int v){return grid[u][v].size();}

    final Color getPatchColor(int u,int v){    return (grid[u][v]).color;}
    final void setPatchColor(Color c,int u,int v){(grid[u][v]).setColor(c);}

    final void moveTurtle(double a,double b,Turtle t)
    {
	int u = (int) Math.round(a)%x;
	int v = (int) Math.round(b)%y;
	if (grid[u][v] != t.position)
	    {
		(t.position).removeAgent(t);
		grid[u][v].addAgent(t);
	    }
    }

    final public void activate()
    {
	joinGroup(simulationGroup);
	requestRole(simulationGroup,"world");
    }
    final public void end()
    {
	leaveGroup(simulationGroup);
    }
}
