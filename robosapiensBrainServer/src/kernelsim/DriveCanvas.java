package kernelsim;

//import madkit.kernel.Agent;
import madkit.kernel.*;
//import madkit.kernel.AbstractAgent;
import java.awt.*;
import javax.swing.*;
//import java.io.*;
//import java.util.*;
import java.awt.event.*;
/** Class on which drive values are drawn and that handles mouse events<br>
(fr: classe qui affiche les valeur des motivations (drives) et qui gère les évennements de la souris).
*/
public class DriveCanvas extends JPanel implements ActionListener
{
  int MAXX;
  int MAXY;

  final static long serialVersionUID=0; // Warning du compilo

       DriveViewer dvw;
       
       int drvnum=-1; // L'Actionneur que l'on manipule avec la souris

    int i, j;

    public void flash()
    {
	paintImmediately(getVisibleRect());
    }

  public DriveCanvas(int width,int height,DriveViewer dv)
    {
	setSize(new Dimension(width,height));
	MAXX=getSize().width;
	MAXY=getSize().height;
	dvw=dv;
	this.enableEvents(AWTEvent.MOUSE_EVENT_MASK+AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

public void actionPerformed(ActionEvent e) {
	       //if (e.getActionCommand().equals("yes")) { k='s'; sortie=true;}
	       //if (e.getActionCommand().equals("no")) { sortie=true;}
	       System.out.println("Lancer cette commande(SC):");
	       System.out.println(e.getActionCommand());
	       dvw.sendEvent(e);
	       
	       //processKeyEvent(e);
	      }
	      
 public void processMouseEvent(MouseEvent e)
    {
    	
    //System.out.println("This is a mouse event");
    
     if (e.getID() == MouseEvent.MOUSE_PRESSED)
       {
	 //Graphics g=this.getGraphics();
	 int flags = e.getModifiers();
         if ((flags & InputEvent.META_MASK) != 0)
	     { // bouton droit souris
	     	drvnum=dvw.getDriveNum(e.getX(),e.getY());
	     	if (drvnum!=-1)
	     	{	/*
	     		double newvalue=dvw.estimateValue(e.getY());
	     		System.out.println("drvnum:newvalue="+drvnum+":"+newvalue);
	     		AgentAddress agtAddrTemp = (dvw.theDrives[drvnum-1]).getAddress();
	     		dvw.pleaseSendMessage(agtAddrTemp,new NeuralMessage(newvalue));
	     		*/
	     		dvw.setSmiley(!dvw.getSmiley());
	     		
	     	} // if (drvnum!=-1)
	     	else
		{
			if (dvw.getBar()) {dvw.setGraph(true); System.out.println("setGraph(true");}
		     	else {dvw.setBar(true);System.out.println("setBar(true");}
		}
	     } // bouton droit souris
         else // bouton gauche souris
	     { 
	     	drvnum=dvw.getDriveNum(e.getX(),e.getY());
	     	if (drvnum!=-1)
	     	{
	     		//System.out.println("drvnum:nbActors="+drvnum+":"+dvw.theActors.length);
	     		double newvalue=dvw.estimateValue(e.getY());
	     		//int valuetemp=dvw.theActors[drvnum].getLastValue();
	     		System.out.println("drvnum:newvalue="+drvnum+":"+newvalue);
	     		AgentAddress agtAddrTemp = (dvw.theDrives[drvnum-1]).getAddress();
	     		//dvw.pleaseSendMessage(dvw.sch.getAddress(), new StringMessage("Pause"));
	     		dvw.pleaseSendMessage(agtAddrTemp,new NeuralMessage(newvalue,"ManMod"));
	     		//dvw.pleaseSendMessage(dvw.sch.getAddress(), new StringMessage("Play"));
	     		
	     		
	     	} // if (drvnum!=-1)
	     	else
	     	{
	     		if (dvw.getRainbow())
		       	{ dvw.setGreenMajenta(true);}
		       	else if (dvw.getGreenMajenta())
		       		{ dvw.setGray(true); }
		       		else if (dvw.getGray())
		       			{ dvw.setRainbow(true);}
	     	}
	     	
	     	
	     } //else // bouton gauche souris
	
	
	} // if (e.getID() == MouseEvent.MOUSE_PRESSED)
	else if (e.getID() == MouseEvent.MOUSE_RELEASED)
	{
		int flags = e.getModifiers();
		//boolean tbool=false;
		
         	if ((flags & InputEvent.META_MASK) != 0)
	     	{ // bouton droit souris
	     		//tbool=lwv.debrayeRobot(nrobmove,false);
	     		//System.out.println("Released on:"+nrobmove+"("+tbool+")");
		}
		else // bouton gauche souris
	     	{ drvnum=-1;
		} // else // bouton gauche souris
	} // else if (e.getID() == MouseEvent.MOUSE_RELEASED)
	
   }

 public void processMouseMotionEvent(MouseEvent e)
    {
     if (e.getID() == MouseEvent.MOUSE_DRAGGED)
       {
	   //System.out.println("Mouse motion event of"+nrobmove+" to "+e.getX()+","+e.getY());
	   if (drvnum!=-1)
	   {
	   	//lwv.moveRobotTo(nrobmove,e.getX(),e.getY());
	   	//repaint();
	   	AgentAddress agtAddrTemp = (dvw.theDrives[drvnum-1]).getAddress();
	     	double newvalue=dvw.estimateValue(e.getY());
	     	dvw.pleaseSendMessage(agtAddrTemp,new NeuralMessage(newvalue,"ManMod"));
	   }
       }
     else
     	super.processMouseMotionEvent(e);
     
    } //public void processMouseMotionEvent(MouseEvent e)      
    

    public Dimension getPreferredSize() {return getSize();}

    /** Routine de bas niveau affichant l'état du SMA réactif, avec
	double-buffering si disponible */

    public void paintComponent(Graphics g)
    {
	
	//super.paintComponent(g);
	g.setColor(Color.black);
	g.fillRect(0,0,MAXX,MAXY);
	dvw.paintInfo(g);
	
	//repaint();
	//040713 faire le repaint en cas de chgt de valeurs
	
	//System.out.println("coucou !!!");
    }



}









