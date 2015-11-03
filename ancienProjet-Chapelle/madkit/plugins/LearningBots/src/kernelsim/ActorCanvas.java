package kernelsim;

//import madkit.kernel.Agent;
import madkit.kernel.*;
//import madkit.kernel.AbstractAgent;
import java.awt.*;
import javax.swing.*;
//import java.io.*;
//import java.util.*;
import java.awt.event.*;

/** Class on which actor values are drawn and that handles mouse events<br>
(fr: classe qui affiche les valeur des effecteurs (actor) et qui gère les évennements de la souris).
*/
public class ActorCanvas extends JPanel implements ActionListener
{
  int MAXX;
  int MAXY;
  final static long serialVersionUID=0; // Warning du compilo

       ActorViewer avw;
       
       int actnum=-1; // L'Actionneur que l'on manipule avec la souris

    int i, j;

    public void flash()
    {
	paintImmediately(getVisibleRect());
    }

  public ActorCanvas(int width,int height,ActorViewer av)
    {
	setSize(new Dimension(width,height));
	MAXX=getSize().width;
	MAXY=getSize().height;
	avw=av;
	this.enableEvents(AWTEvent.MOUSE_EVENT_MASK+AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

public void actionPerformed(ActionEvent e) {
	       //if (e.getActionCommand().equals("yes")) { k='s'; sortie=true;}
	       //if (e.getActionCommand().equals("no")) { sortie=true;}
	       System.out.println("Lancer cette commande(SC):");
	       System.out.println(e.getActionCommand());
	       avw.sendEvent(e);
	       
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
	     	/*
	     	actnum=avw.getActorNum(e.getX(),e.getY());
	     	if (actnum!=-1)
	     	{
	     		//System.out.println("actnum:nbActors="+actnum+":"+avw.theActors.length);
	     		double newvalue=avw.estimateValue(e.getY());
	     		//int valuetemp=avw.theActors[actnum].getLastValue();
	     		System.out.println("actnum:newvalue="+actnum+":"+newvalue);
	     		AgentAddress agtAddrTemp = (avw.theActors[actnum-1]).getAddress();
	     		
	     		avw.pleaseSendMessage(agtAddrTemp,new NeuralMessage(newvalue));
	     		
	     	} // if (actnum!=-1)
	     	else
	     	{*/
		     	if (avw.getBar()) {avw.setGraph(true);}
		     	else {avw.setBar(true);}
		//}
	     }
         else // bouton gauche souris
	     { 
	     	actnum=avw.getActorNum(e.getX(),e.getY());
	     	if (actnum!=-1)
	     	{
	     		//System.out.println("actnum:nbActors="+actnum+":"+avw.theActors.length);
	     		double newvalue=avw.estimateValue(e.getY());
	     		//int valuetemp=avw.theActors[actnum].getLastValue();
	     		System.out.println("actnum:newvalue="+actnum+":"+newvalue);
	     		AgentAddress agtAddrTemp = (avw.theActors[actnum-1]).getAddress();
	     		avw.pleaseSendMessage(agtAddrTemp,new NeuralMessage(newvalue));
	     		
	     	} // if (actnum!=-1)
	     	else
	     	{
	     		if (avw.getRainbow())
		       	{ avw.setGreenMajenta(true);}
		       	else if (avw.getGreenMajenta())
		       		{ avw.setGray(true); }
		       		else if (avw.getGray())
		       			{ avw.setRainbow(true);}
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
	     	{ actnum=-1;
		} // else // bouton gauche souris
	} // else if (e.getID() == MouseEvent.MOUSE_RELEASED)
	
   }

 public void processMouseMotionEvent(MouseEvent e)
    {
     if (e.getID() == MouseEvent.MOUSE_DRAGGED)
       {
	   //System.out.println("Mouse motion event of"+nrobmove+" to "+e.getX()+","+e.getY());
	   if (actnum!=-1)
	   {
	   	//lwv.moveRobotTo(nrobmove,e.getX(),e.getY());
	   	//repaint();
	   	AgentAddress agtAddrTemp = (avw.theActors[actnum-1]).getAddress();
	     	double newvalue=avw.estimateValue(e.getY());
	     	avw.pleaseSendMessage(agtAddrTemp,new NeuralMessage(newvalue));
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
	avw.paintInfo(g);
	
	//repaint();
	//040713 faire le repaint en cas de chgt de valeurs
	
	//System.out.println("coucou !!!");
    }



}









