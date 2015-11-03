/*
* BeeEpiphyt.java - DistributedBees demo program
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

/** this class defines Epiphyte objects that observe the agents world with Probes
and send the informations to a Viewer (one or several EpiphyteProjectionists) where the display is finally made.
Because of the Probes, the EpiphyteAgent must be implemented on the agents current Kernel of a computer
and must stay on it.
=========================================================
        requestRole(BeeLauncher.BEE_COMMUNITY,  "master","BeeEpiphyt");
=========================================================
* @see EpiphyteProjectionist
* @author Pierre BOMMEL, Fabien MICHEL
* @version 1.1 18/08/2000 
*/

public class BeeEpiphyt extends Watcher  implements ReferenceableAgent
{
    ///////////  ATTRIBUTS  /////////////
    protected ServiceMessage msg = new ServiceMessage();
    protected Point[] redPoints = new Point[0];
    protected Point[] bluePoints = new Point[0];
    protected Point[] greenPoints = new Point[0];
    protected Probe redControler,blueControler,greenControler;
    /**
     * @label blueQueenProbe
     */
    BeeProbe blueQueenProbe;
    //ObjectProbe blueQueenProbe;  
    /**
     * @label redQueenProbe
     */
    BeeProbe redQueenProbe;
    /**
     * @label greenQueenProbe
     */
    BeeProbe greenQueenProbe;
    boolean active = true;
    /////////// ACCESSORS  /////////////////
    public void setActive (boolean add){active = add;}
    public boolean getActive(){return active;}

    //////////  CONSTRUCTORS ////////////////////////////    
    public BeeEpiphyt()
    {
        System.err.println("Starting EpiphyteAgent");
    }
    
    //////////////////////METHODS ///////////////////////
    
    public void watchBlueQueensPoints()
    {
        try
        {
            Bee[] bees = blueQueenProbe.getBees();     
                        if (bees != null)
                {
                                bluePoints = new Point[bees.length];
                                if (bees.length > 0)
                                for (int i=0;i<bees.length;i++)
                                                bluePoints[i] = (Point)((bees[i]).p);
                }
        }
        catch(Exception e) 
            {
                System.err.println("Erreur dans watchBlueQueensPoints "+e);
            }
    }
    
    public void watchGreenQueensPoints()
    {     
        try
        {
            Bee[] bees = greenQueenProbe.getBees();
                        if (bees != null)
                {
                                greenPoints = new Point[bees.length];
                                if (bees.length > 0)
                                for (int i=0;i<bees.length;i++)
                                                greenPoints[i] = (Point)((bees[i]).p);
                }
        }
        catch(Exception e) 
            {
                System.err.println("Erreur dans watchGreenQueensPoints "+e);
            }
    }   
    
    public void watchRedQueensPoints()
    {     
        try
        {
            Bee[] bees = redQueenProbe.getBees();
                        if (bees != null)
                {
                                redPoints = new Point[bees.length];
                                if (bees.length > 0)
                                for (int i=0;i<bees.length;i++)
                                                redPoints[i] = (Point)((bees[i]).p);
                }
        }

        catch(Exception e) 
            {
                System.err.println("Erreur dans watchRedQueensPoints "+e);
            }
    }

    /** The "do it" method called by the SingleMethodActivator.
     Receive information from its probes (getObjectsFromAllProbes()) 
     and send them to bee projectionists (EpiphyteProjectionist) */
    public void observe()
    {
    	if( ((BeeControler)redControler.getAgentNb(0)).getPurposeName().equals("carre"))
    	{
		watchRedQueensPoints();
		if (redPoints.length > 0)
		{
			msg.setTable(redPoints);
			broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation", Color.red.toString()+"Controler", msg);
		}
	}
    		
    	if( ((BeeControler)blueControler.getAgentNb(0)).getPurposeName().equals("carre"))
    	{
		watchBlueQueensPoints();
		if (bluePoints.length > 0)
		{
			msg.setTable(bluePoints);
			broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation", Color.blue.toString()+"Controler", msg);
		}
	}
    		
    	if( ((BeeControler)greenControler.getAgentNb(0)).getPurposeName().equals("carre"))
    	{
		watchGreenQueensPoints();
		if (greenPoints.length > 0)
		{
			msg.setTable(greenPoints);
			broadcastMessage(BeeLauncher.BEE_COMMUNITY, "simulation", Color.green.toString()+"Controler", msg);
		}
	}
    }

    public void activate()
    {
        createGroup(true,BeeLauncher.BEE_COMMUNITY,"master",null,null);
        requestRole(BeeLauncher.BEE_COMMUNITY,  "master","BeeEpiphyt",null);
        
	redControler = new Probe(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(),Color.red.toString()+"Controler");
	blueControler = new Probe(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(),Color.blue.toString()+"Controler");
	greenControler = new Probe(BeeLauncher.BEE_COMMUNITY, getAddress().getKernel().toString(),Color.green.toString()+"Controler");
	addProbe(blueControler);addProbe(redControler);addProbe(greenControler);
    
    blueQueenProbe = new BeeProbe("bees", Color.blue.toString()+"Queen");
    addProbe(blueQueenProbe);
    greenQueenProbe = new BeeProbe("bees", Color.green.toString()+"Queen");
    addProbe(greenQueenProbe);
    redQueenProbe = new BeeProbe("bees", Color.red.toString()+"Queen");
    addProbe(redQueenProbe);
        System.err.println(""+this.getAddress()+" Probes OK.");
    }    

    public void end()
    {
    	super.end();
        System.err.println("BeeEpiphyt > END");
    }
}












