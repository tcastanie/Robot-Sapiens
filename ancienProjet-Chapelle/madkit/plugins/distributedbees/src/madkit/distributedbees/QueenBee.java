/*
* QueenBee.java - DistributedBees demo program
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

import madkit.kernel.*;
import java.awt.*;

/**------------------------------------------
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees","queen");
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees",beeColor.toString()+"Queen");
        ------------------------------------------*/
public class QueenBee extends Bee implements ReferenceableAgent
{
    int screenWidth;
    int screenHeight;
    int border = 20;
    int haut, bas, gauche, droite; 
      ////////TEST/////////////


    /**
     * @label monEssaim
     * @link aggregation 
     */
    Bee[] monEssaim;
    int index = 0;
    
    ///// CONSTRUCTEUR //////////////
    public QueenBee(int l, int h, Color c)
    {
        screenWidth=l;
        screenHeight=h;
        beeColor = c;
        MaxAcc = 5;
        MaxVel = 12;        
        p.x = RangedRdm(border, screenWidth  - border);
        p.y = RangedRdm(border, screenHeight - border);
        oldp.x = p.x; 
        oldp.y = p.y;     
        dX = randomFromRange(MaxAcc);
        dY = randomFromRange(MaxAcc); 
        haut = border;
        bas = screenHeight - border;
        gauche = border;
        droite = screenWidth - border;     
    }

    //////////////Methods /////////////

      ////////TEST/////////////
    public void initSwarm(int taille)
    {
        monEssaim = new Bee[taille];
    }
    
    public void addBee(Bee bee) 
    {
        monEssaim[index]=bee;
        if (index < monEssaim.length) index++;
    }

  public void buzz()
    {
    	//System.err.println("buzzing");
      oldp.x = p.x;
      oldp.y = p.y;
      dX += randomFromRange(MaxAcc);
      dY += randomFromRange(MaxAcc);
      // keep speed limited to maximums
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

      // update the queen's position
      p.x += dX;
      p.y += dY;

                
      // check to see if the queen hit the edge
      if (p.x < border || p.x > (screenWidth - border))
        {
          dX = -dX;
          p.x += (dX );
        }
      if (p.y < border || p.y > (screenHeight - border))
        {
          dY = -dY;
          p.y += (dY );
        }
      ////////TEST/////////////
      for (int i = 0; i < monEssaim.length; i++)
          (monEssaim[i]).buzz();
      
    }
  public void moveTo(Point newPoint)
    {
        oldp.x = p.x;
        oldp.y = p.y;
        if (newPoint != null)
            {           
                // update the queen's position
                p.x = newPoint.x;
                p.y = newPoint.y;
            }
      ////////TEST/////////////
      for (int i = 0; i < monEssaim.length; i++)
          (monEssaim[i]).buzz();
    }
 
   public void moveToImmediatly(Point newPoint)
    {
                p.x = newPoint.x;
                p.y = newPoint.y;
    }
 
    /** La reine se déplace sur un rectangle de la taille de la fenetre - border*/
  public void carre()
    {
        int max = MaxVel / 2;
        int diffX = max;
        int diffY = max;
                
        oldp.x = p.x;
        oldp.y = p.y;
        
        // check to see if the queen hit the corner of the square
        while(diffX*diffY > 0)
            {   
                if (p.x >= droite)
                    {
                        p.x = droite;
                        p.y += diffY;
                        if (p.y >= bas)
                            {
                                diffY = p.y - bas;
                                p.y = bas;
                                p.x -= diffY;
                            }
                        diffY = 0;
                    }
                else
                if (p.x <= gauche)
                    {
                        p.x = gauche;
                        p.y -= diffY;
                        if (p.y <= haut)
                            {
                                diffY = haut - p.y;
                                p.y = haut;
                                p.x += diffY;
                            }
                        diffY = 0;
                    }
                else
                if (p.y <= haut)
                    {
                        p.y = haut;
                        p.x += diffX;
                        if (p.x >= droite)
                            {
                                diffX = p.x - droite;
                                p.x = droite;
                                p.y -= diffX;
                            }
                        diffX = 0;
                    }
                else
                if (p.y >= bas)
                    {
                        p.y = bas;
                        p.x -= diffX;
                        if (p.x <= gauche)
                            {
                                diffX = gauche - p.x;
                                p.x = gauche;
                                p.y += diffX;
                            }
                        diffX = 0;
                    }
                else
                    {
                        p.x += diffX;
                        p.y += diffY;
                        diffX = 0;
                    }
            }
      ////////TEST/////////////
      for (int i = 0; i < monEssaim.length; i++)
          (monEssaim[i]).buzz();
    }
    
    
    public void activatedByControler()
    {
        System.err.println("Queen BEE = activatedByControler()");
        leaveRole("bees","bee");
    }

    public void activatedByScheduler()
    {
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees","bee",null);
    }
    
    public void activate()
    {
        //joinGroup(BeeLauncher.BEE_COMMUNITY,"bees");
        //requestRole(BeeLauncher.BEE_COMMUNITY,  "bees","bee");
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees","queen",null);
        //requestRole(BeeLauncher.BEE_COMMUNITY,  "bees","beeView");
        //requestRole(BeeLauncher.BEE_COMMUNITY,  "bees",beeColor.toString());
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees",beeColor.toString()+"Queen",null);
    }

    public void die()
    {
	killAgent(this);
    }
    
    public void end()
    {   
        for (int i = 0; i < monEssaim.length; i++)
            monEssaim[i] = null;
    }

    public int getSwarmSize()
    {
	return monEssaim.length+1;
    }
    
}











