/*
* BeeControler.java - A controler for the queen bee
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
import java.awt.Point;
import java.awt.Color;
import madkit.simulation.activators.*;

/** this class defines a BeeControler that control the queenBee. Using Activators, it decides the new purpose of a queenBee.
It receives 2 kinds of Messages:
 - StringMessage from an InputListener: it changes the purpose of the queenBee (alea, carre, moveTo)
 - PointMessage from an InputListener that listen the mouse events. It contains the Point where the queen should be at its next action (for this, the Queen must in the "moveTo" purpose) 
Because of the Activators, the BeeControler must be implemented on the agents current Kernel of a computer
and must stay on it.
=========================================================
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation","controler");
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation",color.toString()+"Controler"); 
        requestRole(BeeLauncher.BEE_COMMUNITY,  monGroup,color.toString()+"Controler");
        requestRole(BeeLauncher.BEE_COMMUNITY,  monGroup,"controler");
=========================================================

* @see InputListener
* @author Pierre BOMMEL, Fabien MICHEL
* @version 1.1 11/05/2000 
*/
 
public class BeeControler extends Scheduler implements ReferenceableAgent 
{
    ///////////  ATTRIBUTS  /////////////
    
    String monGroup;
    boolean actif = true;
    Color color;
    String purposeName = "buzz";
    double dist = 100;
    double ecart = 0;
    Point pt = null;

    /**
     * @label queenActivator 
     */
    TurboMethodActivator carre, buzz;
    OneValueArgsMethodActivator moveTo;
    
    
    /////////// ACCESSORS  /////////////////


    public void setActif (boolean act)  {actif = act;}
    public boolean getActif(){return actif;}
    public String getPurposeName() {return purposeName;}
    public void setPurposeName(String goal) {purposeName = goal;}
    

    //////////  CONSTRUCTORS ////////////////////////////
    public BeeControler(Color couleur, String group)
    {
        super();
        color = couleur;
        monGroup = group;
        System.err.println("Starting BeeControler "+color.toString());
    }
    public BeeControler(Color couleur, String purpose, String group)
    {
        super();
        color = couleur;
        monGroup = group;
        purposeName = purpose;
        System.err.println("Starting BeeControler "+color.toString());
    }

    //////////////////////METHODS ///////////////////////
   
    public synchronized void correction(ServiceMessage m) 
    {
        Point[] ptTable = (((ServiceMessage)m).getTable());
        QueenBee[] mesQueens = (QueenBee[]) buzz.getCurrentAgentsList().toArray(new QueenBee[0]);
        if(mesQueens.length != ptTable.length)
        	return;
        for (int i=0;i<mesQueens.length;i++)
            {
                QueenBee qb = mesQueens[i];
                Point p = ptTable[i];
                
		/*double distan = Math.sqrt(Math.pow((qb.p.x - p.x), 2) + Math.pow((qb.p.y - p.y), 2));
		//ecart = distan - dist
		if ( distan < 80)//(distan / BeeLauncher.WORLD_SIZE)>0.1)
		{
			System.err.println(""+this.getAddress()+"distance = "+ distan+"=>> correction");
	                qb.moveToImmediatly(p);
		}*/

                qb.moveToImmediatly(p);

		//System.err.println("BeeControler > :correction ");

                //double distance = Point2D.distance(qb.p.x, qb.p.y, p.x, p.y);
                //double distan = qb.p.distance(p.x, p.y);
                //Method distanceSq(int, int) not found in class java.awt.Point.
       /*         try
                    {
                        double distan = Math.sqrt(Math.pow((qb.p.x - p.x), 2) + Math.pow((qb.p.y - p.y), 2));
                        //ecart = distan - dist
                        if ( (distan / BeeLauncher.WORLD_SIZE)>0.1)
                            {
                                System.err.println(""+this.getAddress()+"distance = "+ distan+"=>> correction");
                                qb.moveTo(p);
                            }
                    }*/
                /*catch (Exception e) 
                    {
                        System.err.println("BeeControler > :correction "+e);
                    }*/
            }
    }
        
    public void putActivators()
    {
    	Class[] args = new Class[1];
    	args[0] = (new Point()).getClass();   
    	carre = new TurboMethodActivator("carre",BeeLauncher.BEE_COMMUNITY,"bees",color.toString()+"Queen");
    	buzz = new TurboMethodActivator("buzz",BeeLauncher.BEE_COMMUNITY,"bees",color.toString()+"Queen");
    	moveTo = new OneValueArgsMethodActivator(BeeLauncher.BEE_COMMUNITY,"bees",color.toString()+"Queen","moveTo",args);
        addActivator(buzz);
        addActivator(carre);
        addActivator(moveTo);
    }

    /** The "do it" method called by the PointMethodActivator from Scheduler.
     Receive information from its InputListener: new position of QueenBee. 
     The new position will be given to the QueenActivator */
    public synchronized void commande()
    {
                if(purposeName.equals("carre"))
                	carre.execute();
                else if(purposeName.equals("moveTo"))
                {
                	Point[] pts = new Point[1];
                	pts[0] = pt;
                	moveTo.execute(pts);
                }
                else
                {
                	buzz.execute();
                }
    	
    	
/*        Message m = null;
        Point pt = null;
        
        if(purposeName.equals("moveTo"))
        {
                while(! isMessageBoxEmpty() )
                {
                	m = nextMessage();
	                if (m instanceof PointMessage)
                       		pt = (((PointMessage)m).getPoint());
                	else if(m instanceof StringMessage)
                	{
	                        purposeName = ((StringMessage)m).getString();
		                for(int i = 0;i < 5;i++)
		                	nextMessage();
		                return;
	                }
		}
        	Point[] pts = new Point[1];
        	pts[0] = pt;
        	moveTo.execute(pts);
	}
	else if(purposeName.equals("carre"))
	{
               	ServiceMessage service = null;
                while(! isMessageBoxEmpty() )
                {
                	m = nextMessage();
	                if (m instanceof ServiceMessage && (! m.getSender().isLocal()) )
	                {
				service = (ServiceMessage) m;
			}
                	else if(m instanceof StringMessage)
                	{
	                        purposeName = ((StringMessage)m).getString();
		                for(int i = 0;i < 5;i++)
		                	nextMessage();
		                return;
	                }
		}
		if(service != null)
			correction(service);
        	carre.execute();
	}
	else if(purposeName.equals("buzz"))
	{
                while(! isMessageBoxEmpty() )
                {
                	m = nextMessage();
			if(m instanceof StringMessage)
                	{
	                        purposeName = ((StringMessage)m).getString();
		                for(int i = 0;i < 5;i++)
		                	nextMessage();
		                return;
	                }
		}
		buzz.execute();
	}	
        	
            /*    while(! isMessageBoxEmpty() )
                {
                	m = nextMessage();
                	if(m instanceof StringMessage)
                	{
                		//System.err.println("receiving String message "+ ((StringMessage)m).getString());
	                        purposeName = ((StringMessage)m).getString();
	                }
                if (m instanceof ServiceMessage && purposeName.equals("carre") && (! m.getSender().isLocal()))
                        correction((ServiceMessage) m);
                else
                //Système de correction : ServiceMessage contient un tableau de Points des positions des reines. La correction est prise en compte et activée immédiatement, sauf si le purposeName = 'buzz' ou 'moveTo'.
	                if (m instanceof PointMessage)
                    	{
                       		pt = (((PointMessage)m).getPoint());
	                }  
                 }

		if(m instanceof StringMessage)
	        	purposeName = ((StringMessage)m).getString();
                if (m instanceof ServiceMessage && purposeName.equals("carre") && (! m.getSender().isLocal()))
                        correction((ServiceMessage) m);
                else
                //Système de correction : ServiceMessage contient un tableau de Points des positions des reines. La correction est prise en compte et activée immédiatement, sauf si le purposeName = 'buzz' ou 'moveTo'.
	                if (m instanceof PointMessage)
                    	{
                       		pt = (((PointMessage)m).getPoint());
	                }  
		*/
       		//System.err.println("current method "+ purposeName);
               /* if(purposeName.equals("carre"))
                	carre.execute();
                else if(purposeName.equals("moveTo"))
                {
                	Point[] pts = new Point[1];
                	pts[0] = pt;
                	moveTo.execute(pts);
                }
                else if(purposeName.equals("buzz"))
                {
                	buzz.execute();
                }*/
    }
 
     ////////////////////////////////////////////////////////////    
    public void activate()
    {
        requestRole(BeeLauncher.BEE_COMMUNITY,  getAddress().getKernel().toString(),color.toString()+"Controler",null);
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation","controler",null);
        requestRole(BeeLauncher.BEE_COMMUNITY,  "simulation",color.toString()+"Controler",null);
        //requestRole(BeeLauncher.BEE_COMMUNITY,  monGroup,"controler",null);
        putActivators();
    }
    
    public void die()
    {
    	killAgent(this);
}

public void live()
{
	while(true)
	{
		exitImmediatlyOnKill();
		Message m = waitNextMessage();
		purgeMailbox(m);
	}
}

public synchronized void  purgeMailbox(Message m)
{
	boolean goForIt = false;
	boolean correction = false;
	ServiceMessage service = null;
		if(m instanceof StringMessage)
		{
			purposeName = ((StringMessage)m).getString();
		}
		else if(m instanceof PointMessage)
		{
			pt = (((PointMessage)m).getPoint());
			goForIt = true;
		}
		else if(m instanceof ServiceMessage && (! m.getSender().isLocal()) )
		{
			service = (ServiceMessage)m;
			correction = true;
		}
		while(! isMessageBoxEmpty())
		{
			//System.err.println("purging mailbox");
			m = nextMessage();
			if(m instanceof StringMessage)
			{
				purposeName = ((StringMessage)m).getString();
			}
			else if(m instanceof PointMessage)
			{
				pt = (((PointMessage)m).getPoint());
				goForIt = true;
			}
			else if(m instanceof ServiceMessage && (! m.getSender().isLocal()) )
			{
				service = (ServiceMessage)m;
				correction = true;
			}
		}
	if(goForIt)
		commande();
	if(correction)
		correction(service);
}


}// end class






