/*
* GridPlayer.java -TurtleKit - A 'star logo' in MadKit
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
package turtlekit.simulations.gameoflife;

import turtlekit.kernel.Observer;
import turtlekit.kernel.Patch;


/** This agent just play the game of life
    
    @author Fabien MICHEL
    @version 1.1 23/02/2001 */

public class GridPlayer extends Observer
{
    boolean doubleGame;
    double percentage;
    byte[][] gridBuffer;
    Patch[] neighbors;
    
    public GridPlayer(double percentageValue,boolean twoVariables)
    {
    	doubleGame = twoVariables;
	percentage = percentageValue;
    }
    
    public void setup()
    {
    gridBuffer=new byte[envWidth][envHeight];
    	for(int i=0;i<envWidth;i++)
	    	for(int j=0;j<envHeight;j++)
			patchGrid[i][j].setPatchVariable("lifeValue",0);
    	
    	for(int i=0;i<envWidth;i++)
	    	for(int j=0;j<envHeight;j++)
	    		if(Math.random()<percentage)
				patchGrid[i][j].setPatchVariable("lifeValue",1);
			else
				patchGrid[i][j].setPatchVariable("lifeValue",0);

	if(doubleGame)
    	for(int i=0;i<envWidth;i++)
	    	for(int j=0;j<envHeight;j++)
	    		if(Math.random()<percentage)
				patchGrid[i][j].setPatchVariable("lifeValue2",1);
			else
				patchGrid[i][j].setPatchVariable("lifeValue2",0);
	
	/*patchGrid[30][50].setPatchVariable("lifeValue",1);
	patchGrid[31][51].setPatchVariable("lifeValue",1);
	patchGrid[30][52].setPatchVariable("lifeValue",1);
	
	
	patchGrid[50][52].setPatchVariable("lifeValue",1);
	patchGrid[51][52].setPatchVariable("lifeValue",1);
	patchGrid[52][52].setPatchVariable("lifeValue",1);*/
	
    }
    
    public void watch()
    {
    	//int[][] gridBuffer=new int[envWidth][envHeight];
   	for(int i=0;i<envWidth;i++)
	    	for(int j=0;j<envHeight;j++)
	    	{
	    		/*Patch[]*/ neighbors = patchGrid[i][j].getNeighbors();
	    		byte alive=0;
	    		for(int k=0;k<neighbors.length;k++)
	    		{
	    			alive+=(byte)neighbors[k].getVariableValue("lifeValue");
	    		}
			gridBuffer[i][j]=(byte)patchGrid[i][j].getVariableValue("lifeValue");
	    		if ( gridBuffer[i][j]==1 && (alive < 2 || alive >3))
	    		{
				gridBuffer[i][j]=0;
			}
	    		else if (alive == 3)
	    			gridBuffer[i][j]=1;
	    	}
    	for(int i=0;i<envWidth;i++)
	    	for(int j=0;j<envHeight;j++)
	    	{
	    		patchGrid[i][j].setPatchVariable("lifeValue",gridBuffer[i][j]);
	    	}

	if(doubleGame)
	{
   	for(int i=0;i<envWidth;i++)
	    	for(int j=0;j<envHeight;j++)
	    	{
	    		/*Patch[]*/ neighbors = patchGrid[i][j].getNeighbors();
	    		byte alive=0;
	    		for(int k=0;k<neighbors.length;k++)
	    		{
	    			alive+=(byte)neighbors[k].getVariableValue("lifeValue2");
	    		}
			gridBuffer[i][j]=(byte)patchGrid[i][j].getVariableValue("lifeValue2");
	    		if ( gridBuffer[i][j]==1 && (alive < 2 || alive >3))
	    		{
				gridBuffer[i][j]=0;
			}
	    		else if (alive == 3)
	    			gridBuffer[i][j]=1;
	    	}
    	for(int i=0;i<envWidth;i++)
	    	for(int j=0;j<envHeight;j++)
	    	{
	    		patchGrid[i][j].setPatchVariable("lifeValue2",gridBuffer[i][j]);
	    	}
	}
	
	    	
   }
 		  
    
}



