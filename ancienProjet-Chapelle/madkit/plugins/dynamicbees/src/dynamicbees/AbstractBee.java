/*
* AbstractBee.java - DynamicBees, a demo for the probe and watcher mechanisms
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

import java.awt.Color;
import java.awt.Point;

import madkit.kernel.AbstractAgent;
import madkit.kernel.ReferenceableAgent;
/**
  @version 2.0
  @author Olivier Gutknecht
  @author Fabien MICHEL 01/02/2001*/
public abstract class AbstractBee extends AbstractAgent implements ReferenceableAgent
{
  public Point p;	// current x pos of bee
  public Point oldp;
  int dX;		// current velocity in x dir
  int dY;		// current velocity in y dir
  int oldX;
  int oldY;
  public BeeEnvironment beeWorld;
  public BeeColor beeColor;
  
  public AbstractBee()
  {
      	p = new Point();
    	oldp = new Point();
       beeColor = new BeeColor(Color.getHSBColor((float)( ((int) (Math.random()*360)) / 360.0),(float)1.0,(float)1.0));
  }

  public int RangedRdm(int min, int max)
    {
	int range, t;
	range = max - min +1;
	t = (int)(java.lang.Math.random() * range) % range;
	if (t+min > max) 
	    t = max - min;
	return (t+min);
    }
	
    /** --------------------------------------------------------------------------
     *  randomFromRange - return a value from -val to +val
     *
     */
 final public int randomFromRange(int val)
  {
      return (RangedRdm(-(val)/2, (val)/2));
  }
  
 public void setEnvironment(BeeEnvironment bw)
    {
    	beeWorld = bw;
	p.x = ((int) (Math.random()*(beeWorld.screenWidth-25)))+10;
	p.y = ((int) (Math.random()*(beeWorld.screenHeight-25)))+10;
	oldp.x = p.x; 
	oldp.y = p.y;
	
	dX = randomFromRange(beeWorld.beeMaxAcc);
	dY = randomFromRange(beeWorld.beeMaxAcc);      
     }

}

