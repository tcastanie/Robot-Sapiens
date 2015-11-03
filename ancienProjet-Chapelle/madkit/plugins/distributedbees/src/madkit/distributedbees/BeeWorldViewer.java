/*
* BeeWorldViewer.java - DistributedBees demo program
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

import madkit.kernel.*;
import java.awt.*;
import java.util.Vector;

/**
    ========================================
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees","beeWatcher");
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees",c.toString()+"BeeWatcher");
    ========================================*/
public class BeeWorldViewer extends Watcher implements ReferenceableAgent
{   
 ///////////  ATTRIBUTS  /////////////
    private String monGroup = "bees";
    private Vector myColors = new Vector();

    /**
     * @label onScreen 
     */
    GridCanvas2 onScreen;

    /**
     * @label listener 
     */
    BwvInputListener listener;
        
    /**
     * @label bees 
     * @link aggregation
     */
    Bee[] bees;   

    /**
     * @label queenBees 
     * @link aggregation
     */
    QueenBee[] queenBees;

    /**
     * @label greenBeeProbe 
     */
    BeeProbe greenBeeProbe;

    /**
     * @label redBeeProbe 
     */
    BeeProbe redBeeProbe;

    /**
     * @label blueBeeProbe 
     */
    BeeProbe blueBeeProbe;

    /**
     * @label greenQueenProbe 
     */
    BeeProbe greenQueenProbe;

    /**
     * @label redQuennProbe 
     */
    BeeProbe redQueenProbe;

    /**
     * @label blueQueenProbe
     */
    BeeProbe blueQueenProbe;
  
    int width;
    int height;
    
    boolean flash = false;
    public boolean show = false;
    
    public void setFlash (boolean add){flash = add;}
    public boolean getFlash(){return flash;}

    public GridCanvas2 getCanvas(){return onScreen;}
    //////// CONSTRUCTORS ////////////////////////   
    public BeeWorldViewer()
    {
        this(BeeLauncher.WORLD_SIZE, BeeLauncher.WORLD_SIZE);
    }
    
    public BeeWorldViewer(int w,int h)
    {
        System.err.println("Starting BeeWorldViewer");
        width = w;
        height = h;
        System.err.println("BeeWorldViewer: fin constructeur");
    }
    //////// METHODS ////////////////////

    private void paintBeesWithProbe(Graphics g, BeeProbe aProbe)
    {
        try{
            bees = aProbe.getBees();            
            for(int j=0;j<bees.length;j++)
                {
                                // for the body
                    g.setColor(bees[j].beeColor);
                    g.drawLine(((bees[j]).oldp).x,((bees[j]).oldp).y,
                               (bees[j]).p.x,(bees[j]).p.y);
                                // for the head 
                    g.setColor(Color.white);
                    g.fillOval((bees[j]).p.x,(bees[j]).p.y, 4, 4);
                }
        }
        catch(Exception e) 
            {
                System.err.println("Erreur dans paintBeesWithProbe "+e);
            }
    }

    private void paintQueenWithProbe(Graphics g, BeeProbe aProbe)
    {
        try{
            bees = aProbe.getBees();            
            for(int j=0;j<bees.length;j++)
                {
                                // for the body
                    g.setColor(bees[j].beeColor);
                    g.drawLine(((bees[j]).oldp).x,((bees[j]).oldp).y,
                               (bees[j]).p.x,(bees[j]).p.y);
                                // for the head 
                    g.fillOval((bees[j]).p.x - 5,(bees[j]).p.y - 5, 10, 10);
                    //les objets bees
                    paintObjectsBees(g, ((QueenBee) (bees[j])).monEssaim);
                }
        }
        catch(Exception e) 
            {
                System.err.println("Erreur dans paintQueenWithProbe "+e);
            }
    }

    private void paintObjectsBees(Graphics g, Bee[] swarm)
    {           
            for(int j=0;j<swarm.length;j++)
                {
                                // for the body
                    g.setColor(swarm[j].beeColor);
                    g.drawLine(((swarm[j]).oldp).x,((swarm[j]).oldp).y,
                               (swarm[j]).p.x,(swarm[j]).p.y);
                                // for the head 
                    g.setColor(Color.white);
                    g.fillOval((swarm[j]).p.x,(swarm[j]).p.y, 4, 4);
                }
    }
    
        /** This method is called by the GridCanvas (JComponent). It paints the Bees in white and their trajectory of the color defined in  */
    public void paintBees(Graphics g)
    {
        if (show)
            {
                paintQueenBees(g);
                if (greenBeeProbe != null) paintBeesWithProbe(g, greenBeeProbe);
                if (redBeeProbe != null) paintBeesWithProbe(g, redBeeProbe);
                if (blueBeeProbe != null) paintBeesWithProbe(g, blueBeeProbe);
            }
    }
    
    public void paintQueenBees(Graphics g)
    {
        if (greenQueenProbe != null) paintQueenWithProbe(g, greenQueenProbe);
        if (redQueenProbe != null) paintQueenWithProbe(g, redQueenProbe);
        if (blueQueenProbe != null) paintQueenWithProbe(g, blueQueenProbe);
    }
    
    public void addColoredRole(Color c)
    {
        if (myColors.indexOf(c) == -1)
            {
                myColors.addElement(c);
                requestRole(BeeLauncher.BEE_COMMUNITY,  monGroup,c.toString()+"BeeWatcher",null);
                addColoredProbes(c);
            }
    }   
     
    public void removeColoredRole(Color c)
    {
        try 
            {
                if (myColors.indexOf(c) != -1)
                    {
                        leaveRole(monGroup,c.toString()+"BeeWatcher");
                        myColors.removeElement(c);
                        removeColoredProbes(c);
                    }
            }
        catch(Exception e)    
            {
                System.err.println("Exception dans removeColoredRole(Color c) :"+e);
            }   
    }
        

    public void addColoredProbes(Color c)
    {   
        if (c == Color.green)
            {
                greenQueenProbe = new BeeProbe("bees", c.toString()+"Queen");   
                addProbe(greenQueenProbe);
                greenBeeProbe = new BeeProbe("bees", c.toString());     
                addProbe(greenBeeProbe);
            }
        
        if (c == Color.red)
            {
                redQueenProbe = new BeeProbe("bees", c.toString()+"Queen");     
                addProbe(redQueenProbe);
                redBeeProbe = new BeeProbe("bees", c.toString());       
                addProbe(redBeeProbe);
                //System.err.println(redQueenProbe);
            }
        
        if (c == Color.blue)
            {
                blueQueenProbe = new BeeProbe("bees", c.toString()+"Queen");    
                addProbe(blueQueenProbe);
                blueBeeProbe = new BeeProbe("bees", c.toString());      
                addProbe(blueBeeProbe);
            }
    }
    
    public void removeColoredProbes(Color c)
    {
        if (c == Color.green)
            {
                removeProbe(greenQueenProbe);
                greenQueenProbe = null;
                removeProbe(greenBeeProbe);     
                greenBeeProbe = null;   
            }
        
        if (c == Color.red)
            {
                removeProbe(redQueenProbe);
                redQueenProbe  = null;
                removeProbe(redBeeProbe);       
                redBeeProbe = null;
            }
        
        if (c == Color.blue)
        {
            removeProbe(blueQueenProbe);
            blueQueenProbe  = null;
            removeProbe(blueBeeProbe);  
            blueBeeProbe = null;
        }
    }
    

    public void addListener()
    {
        try
            {
                System.err.println("Starting InputListener");
                listener = new BwvInputListener(onScreen, this) ;
                launchAgent(listener, "User's Control", true);
            }
        catch(Exception e)
            {System.err.println("BeeWorldViewer: Exception dans activate, avant addListener :"+e);}
        try
            {
                onScreen.addListener(listener);
                
                System.err.println("BeeWorldViewer activated");
            }
        catch(Exception e)
            {System.err.println("BeeWorldViewer: Exception dans activate :"+e);} 
    }
    
    ///////////////////////////////////////////////////////////////////////////    
    public void observe()
    {
        if (! flash) 
            onScreen.repaint();
        else 
            onScreen.flash();
    }
 
     //////// Madkit usage  ////////////////////

    public void initGUI()
    {
        setGUIObject(onScreen = new GridCanvas2(width,height,this));
    }

    public void activate()
    {
	createGroup(true, BeeLauncher.BEE_COMMUNITY, "bees", null, null);
        requestRole(BeeLauncher.BEE_COMMUNITY,  "bees", "BeeWatcher", null);
    }
    
    public void end()
    {
        super.end();
        if (listener != null)
        	killAgent(listener);
    }        
}
