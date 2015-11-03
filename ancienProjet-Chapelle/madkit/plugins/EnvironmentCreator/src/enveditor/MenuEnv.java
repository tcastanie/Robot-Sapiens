package enveditor;

import madkit.kernel.Agent;
import javax.swing.*;
import java.util.*;

import madkit.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.io.*;

public class MenuEnv extends JPanel implements ActionListener{
 
 private int lastx,lasty,fx,fy;

 int DIMX; // dimension de l'environnement
 int DIMY;
 int bord = 10, h=40, l=10;
 Color wall = Color.blue;
 Color thing= Color.green;
 String ch;
 String filename=".env";

 EnvEditor ptr;

 public MenuEnv(int sx, int sy,EnvEditor p)
    { //super("Create Environment");
    ptr=p;
      DIMX=sx;
      DIMY=sy;
      this.init();
      //TabR[0] = new Rect(0,0,sx,sy,null);
      //this.setSize(DIMX+2*bord+2*l,DIMY+2*bord+h+l);
      //this.pack();
      //TabThing[0]=new Rect(0,0,sx,sy,null);
      
      //this.show();
      //setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      //fond();
    }

 public void init()
    {
     this.enableEvents(AWTEvent.MOUSE_EVENT_MASK );
     this.requestFocus();

    }

 public void setSize(int x,int y)
    {
    	System.out.println("MenuEnv:setSize("+x+","+y+")");
        super.setSize(x,y);
    }

public void actionPerformed(ActionEvent e) {
	       //if (e.getActionCommand().equals("yes")) { k='s'; sortie=true;}
	       //if (e.getActionCommand().equals("no")) { sortie=true;}
	       System.out.println("Lancer cette commande(ME):");
	       System.out.println(e.getActionCommand());
	       ptr.sendEvent(e);
	       //processKeyEvent(e);
	      }

public void paint(Graphics g)
    {
     // pour reafficher...
     //fond();
     //g.drawString("s : save  q : quit  l : load  g : grid="+grid+" o : object ("+SobjectType+")",20,34);
     //g.setColor(wall);
     
    }

 public void aff(String txt,int x,int y)
    {
     ch=txt;
     Graphics g=this.getGraphics();

     //g.drawChars(ctxt,0,txt.length(),x,y);
     //if (yT==0) g.clearRect(0,0,DIMX,DIMY);
     //g.drawString(txt,x,y);
     //System.out.println(txt);

    }

 public void processMouseEvent(MouseEvent e)
    {
    	/*
    System.out.println("This is a mouse event");
     if (e.getID() == MouseEvent.MOUSE_PRESSED)
       {
	 Graphics g=this.getGraphics();

	 int flags = e.getModifiers();
         if ((flags & Event.META_MASK) != 0)
	     { // bouton droit souris
	     }
         else // bouton gauche souris
	     { fx=e.getX(); fy=e.getY();
	     }
	}
	*/
   }

 public void processMouseMotionEvent(MouseEvent e)
    {/*
     if (e.getID() == MouseEvent.MOUSE_DRAGGED)
       {
	      int x=e.getX(), y=e.getY();
	   Graphics g=this.getGraphics();
	   g.drawRect(lastx,lasty,x-lastx,y-lasty);
	   
       }
     else super.processMouseMotionEvent(e);*/
     
    }

 public void save()
    {
    
    }

 public void load()
    {
     
    }

}
