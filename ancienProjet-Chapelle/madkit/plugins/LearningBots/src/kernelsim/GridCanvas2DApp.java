package kernelsim;

//import madkit.kernel.Agent;
import java.awt.*;
import javax.swing.*;
//import java.io.*;
//import java.util.*;

import java.awt.event.*;

public class GridCanvas2DApp extends JPanel implements ActionListener
{
  int MAXX;
  int MAXY;



       RobotAppEnv world;
       static final long serialVersionUID = RobotAppEnv.serialVersionUID; //42L;
    int i, j;

    RobotWorldViewer2DApp lwv;
	
	/** numero du robot que l'on déplace avec la souris */
	int nrobmove=-1;
	/** numero de la plaque que l'on déplace avec la souris */
	int nplaquemove=-1;
	

    public void flash()
    {
	paintImmediately(getVisibleRect());
    }

  public GridCanvas2DApp(int width,int height,RobotWorldViewer2DApp l)
    {
	setSize(new Dimension(width,height));
	lwv=l;
	this.enableEvents(AWTEvent.MOUSE_EVENT_MASK+AWTEvent.MOUSE_MOTION_EVENT_MASK);
	MAXX=getSize().width;
	MAXY=getSize().height;

    }

    public Dimension getPreferredSize() {return getSize();}
    
    /** Routine de bas niveau affichant l'état du SMA réactif, avec
	double-buffering si disponible */

    public void paintComponent(Graphics g)
    {
	super.paintComponent(g);

	g.setColor(Color.black);
	g.fillRect(0,0,MAXX,MAXY);
	lwv.paintInfo(g);
    }
    

public void actionPerformed(ActionEvent e) {
	       //if (e.getActionCommand().equals("yes")) { k='s'; sortie=true;}
	       //if (e.getActionCommand().equals("no")) { sortie=true;}
	       System.out.println("Lancer cette commande(GC):");
	       System.out.println(e.getActionCommand());
	       //svw.sendEvent(e);
	       
	       //processKeyEvent(e);
	      }

 public void processMouseEvent(MouseEvent e)
    {
    	
    
     if (e.getID() == MouseEvent.MOUSE_PRESSED)
       {
	 //Graphics g=this.getGraphics();
	boolean tbool=false;
	 int flags = e.getModifiers();
         if ((flags & InputEvent.META_MASK) != 0)
	     { // bouton droit souris
	     	//System.out.println("This is a mouse event Right"+e.getX()+","+e.getY());
	     	
	     	nrobmove= lwv.getRobotId(e.getX(),e.getY());
	     	if (nrobmove!=-1)
	     	{ lwv.debrayeRobot(nrobmove,true); }
	     		     	
	     }
         else // bouton gauche souris
	     { //fx=e.getX(); fy=e.getY();
	     	//System.out.println("This is a mouse event Left"+e.getX()+","+e.getY());
	     	nrobmove= lwv.getRobotId(e.getX(),e.getY());
	     	
	     	if (nrobmove!=-1)
	     	{	tbool=lwv.debrayeRobot(nrobmove,true);
	     		System.out.println("Pressed on (rob):"+nrobmove+"("+tbool+")"); }
	     	else
	     	{	nplaquemove=lwv.getPlaqueId(e.getX(),e.getY());
	     		System.out.println("Pressed on (plq):"+nplaquemove+"("+tbool+")"); }
	     }
	}
	else if (e.getID() == MouseEvent.MOUSE_RELEASED)
	{
		int flags = e.getModifiers();
		boolean tbool=false;
		
         	if ((flags & InputEvent.META_MASK) != 0)
	     	{ // bouton droit souris
	     		//tbool=lwv.debrayeRobot(nrobmove,false);
	     		System.out.println("Released on:"+nrobmove+"("+tbool+")");
		}
		else // bouton gauche souris
	     	{
	     		//System.out.println("Released on:"+nrobmove);
			if (nrobmove!=-1)
			{	tbool=lwv.debrayeRobot(nrobmove,false);
				System.out.println("Released on (rob):"+nrobmove+"("+tbool+")");
			}
			else
			{	if (nplaquemove!=-1)
				{ System.out.println("Released on (plq):"+nplaquemove+"("+tbool+")"); }
			}
			repaint();
			nrobmove=-1;
			nplaquemove=-1;
		} // else // bouton gauche souris
	} // else if (e.getID() == MouseEvent.MOUSE_RELEASED)
	
	
   }

 public void processMouseMotionEvent(MouseEvent e)
    {
     if (e.getID() == MouseEvent.MOUSE_DRAGGED)
       {
	   //System.out.println("Mouse motion event of"+nrobmove+" to "+e.getX()+","+e.getY());
	   if (nrobmove!=-1)
	   {
	   	lwv.moveRobotTo(nrobmove,e.getX(),e.getY());
	   	repaint();
	   }
	   else
	   if (nplaquemove!=-1)
	   {
	   	lwv.movePlaqueTo(nplaquemove,e.getX(),e.getY());
	   	repaint();
	   }
	
	   
       }
     else
     	super.processMouseMotionEvent(e);
     
    } //public void processMouseMotionEvent(MouseEvent e)


}









