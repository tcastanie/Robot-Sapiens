/*
* Bee.java: a bee that follow a queen
* Copyright (C) 1998-2004 Olivier Gutknecht
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

import java.awt.*;
import madkit.kernel.*;


/** ---------------------------------------------------------------------------------
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees","bee");
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees",beeColor.toString());
---------------------------------------------------------------------------------


 *  Class: Bee - keeps track of individual bees. 
 * @author Olivier Gutknecht, with portions from Michael Nosal
 * @version 0.2
 */

public class Bee extends AbstractAgent implements ReferenceableAgent
{
    //////////ATTRIBUTS////////////////////////////
    public Point p = new Point();               // current x pos of bee
    public Point oldp = new Point();
    
    int dX;             // current velocity in x dir
    int dY;             // current velocity in y dir
    int oldX;
    int oldY;
    
    public Color beeColor;              // current bee color
    
    int MaxAcc = 3;
    int MaxVel = 9;
    
    //private int hue = 270;


    /**
     * @label myQueen 
     */
    private QueenBee myQueen = null;

  
    //////////// constructor //////////
    public Bee(QueenBee b, Color c) 
    {
        myQueen = b;
        beeColor = c;
        p.x = b.p.x;
        p.y = b.p.y;
        dX = 0; // no initial velocity, so this vector is zero
        dY = 0;
        oldp.x = p.x; 
        oldp.y = p.y;
        
        dX = randomFromRange(MaxAcc);
        dY = randomFromRange(MaxAcc); 
    }
    public Bee() 
    {}
    
    //////////// METHODS////////////////////////////////////////////

    /** The "do it" method called by the activator */
    public void buzz()
    {
        int dtx, dty, dist;             // distances from bee to queen
        
        //hue++;
        //beeColor = Color.getHSBColor((float)(hue/ 360.0),(float)1.0,(float)1.0);
        //hue = hue % 359;      // keep going around the color wheel.
        
        dtx = myQueen.p.x - p.x;
        dty = myQueen.p.y - p.y;
        dist = Math.abs(dtx) + Math.abs(dty);
        if (dist == 0) 
            dist = 1;           // avoid dividing by zero
        oldp.x = p.x;
        oldp.y = p.y;
        // the randomFromRange adds some extra jitter to prevent the bees from flying in formation
        dX += (int)((dtx * MaxAcc )/dist) + randomFromRange(2);
        dY += (int)((dty * MaxAcc )/dist) + randomFromRange(2);
        
        if (dX > MaxVel)
            dX = MaxVel;
        else
            if (dX < -MaxVel)
                dX = -MaxVel;
        
        if (dY > MaxVel)
            dY = MaxVel;
        else
            if (dY < -MaxVel)
                dY = -MaxVel;
        
        p.x += dX;
        p.y += dY;
    }  
    
    /** --------------------------------------------------------------------------
     *  RangedRdm - return a random number in the range min <-> max 
     *
     */
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


    public void activate()
    {
        //joinGroup(BeeLauncher.BEE_COMMUNITY,"bees");
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees","bee",null);
        //requestRole(BeeLauncher.BEE_COMMUNITY,  "bees","beeView");
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees",beeColor.toString(),null);
    }

    public void end()
    {
        //System.err.println("Une abeille en moins... ");
    }
}









