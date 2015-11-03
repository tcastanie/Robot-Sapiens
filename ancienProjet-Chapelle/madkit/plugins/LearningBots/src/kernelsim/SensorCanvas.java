package kernelsim;

//import madkit.kernel.Agent;
import java.awt.*;
import javax.swing.*;
//import java.io.*;
//import java.util.*;
import java.awt.event.*;

public class SensorCanvas extends JPanel implements ActionListener
{
  int MAXX;
  int MAXY;
  final static long serialVersionUID=0; // Warning du compilo

       SensorViewer svw;

    int i, j;

    public void flash()
    {
	paintImmediately(getVisibleRect());
    }

  public SensorCanvas(int width,int height,SensorViewer sv)
    {
	setSize(new Dimension(width,height));
	MAXX=getSize().width;
	MAXY=getSize().height;
	svw=sv;
	this.enableEvents(AWTEvent.MOUSE_EVENT_MASK );
     	//this.requestFocus();
	

}

public void actionPerformed(ActionEvent e) {
	       //if (e.getActionCommand().equals("yes")) { k='s'; sortie=true;}
	       //if (e.getActionCommand().equals("no")) { sortie=true;}
	       System.out.println("Lancer cette commande(SC):");
	       System.out.println(e.getActionCommand());
	       svw.sendEvent(e);
	       
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
	     	if (svw.getBar()) {svw.setGraph(true);}
	     	else {svw.setBar(true);}
	     }
         else // bouton gauche souris
	     { 
     	       	if (svw.getRainbow())
	       	{ svw.setGreenMajenta(true);}
	       	else if (svw.getGreenMajenta())
	       		{ svw.setGray(true); }
	       		else if (svw.getGray())
	       			{ svw.setRainbow(true);}
	     	
	     	//fx=e.getX(); fy=e.getY();
	}
	
	
	}
	
   }

 public void processMouseMotionEvent(MouseEvent e)
    {
    	System.out.println("This is a mouse motion event");
    	
    	/*
     if (e.getID() == MouseEvent.MOUSE_DRAGGED)
       {
	      int x=e.getX(), y=e.getY();
	   Graphics g=this.getGraphics();
	   g.drawRect(lastx,lasty,x-lastx,y-lasty);
	   
       }
     else super.processMouseMotionEvent(e);
     */
    }	      
    public Dimension getPreferredSize() {return getSize();}

    /** Routine de bas niveau affichant l'état du SMA réactif, avec
	double-buffering si disponible */

    public void paintComponent(Graphics g)
    {
	
	//super.paintComponent(g);
	g.setColor(Color.black);
	g.fillRect(0,0,MAXX,MAXY);
	svw.paintInfo(g);
	
	//repaint();
	//040713 faire le repaint en cas de chgt de valeurs
	
	//System.out.println("coucou !!!");
    }



}









