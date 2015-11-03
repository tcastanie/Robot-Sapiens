package kernelsim;

//import madkit.kernel.Agent;
import javax.swing.*;
//import java.util.*;

//import madkit.kernel.*;
import java.awt.*;
import java.awt.event.*;
//import java.util.Date;
//import java.io.*;

//import java.lang.Integer;

/** Classe inutilisée - unused class **/
public class GraphicMenu extends JPanel implements ActionListener{
	static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
 int DIMX; // dimension de l'environnement
 int DIMY;
 int bord = 10, h=40, l=10;
 int fx, fy; // pour les evts pointer de la souris (coord x y)
 char k;

 String ch;

 boolean sortie=false,first=true,saved=false,grid=true;

 public GraphicMenu(int sx, int sy)
    { //super("Create Environment");
      DIMX=sx;
      DIMY=sy;
      this.init();
      this.setSize(DIMX+2*bord+2*l,DIMY+2*bord+h+l);
    }

 public void init()
    {
     this.enableEvents(AWTEvent.MOUSE_EVENT_MASK |
		       AWTEvent.KEY_EVENT_MASK |
		       AWTEvent.MOUSE_MOTION_EVENT_MASK);
     //this.requestFocus();

    }

 public void setSize(int x,int y)
    {
        super.setSize(x,y);
    }

 public void fond()
    {
     Graphics g=this.getGraphics();
     g.setColor(Color.gray);
     g.fillRect(l,h,DIMX+2*bord,bord-1);
     g.fillRect(l,h+bord-1,bord-1,DIMY+2);
     g.fillRect(l+bord+DIMX+1,h+bord-1,bord-1,DIMY+2);
     g.fillRect(l,h+bord+DIMY+1,DIMX+2*bord,bord-1);
     //System.out.println(g);
    }

public void actionPerformed(ActionEvent e) {
	       //if (e.getActionCommand().equals("yes")) { k='s'; sortie=true;}
	       //if (e.getActionCommand().equals("no")) { sortie=true;}
	       System.out.println("Lancer cette commande(CE):");
	       System.out.println(e.getActionCommand());
	       //processKeyEvent(e);
	      }

 public void processKeyEvent(KeyEvent e)
    {	System.out.println("KeyEvent:"+e);
     if (e.getID() == KeyEvent.KEY_RELEASED)
       {
	   //char k;
	k = e.getKeyChar();
        if (k=='q')
	   {
	    /*YesNoDialog quit = new YesNoDialog(this,"Quit ?","Save before quitting ?","Yes, save and quit","No, just quit","Cancel");
	    quit.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	       if (e.getActionCommand().equals("yes")) { k='s'; sortie=true;}
	       if (e.getActionCommand().equals("no")) { sortie=true;}
	      }
	     });
	    quit.show();
		*/
	   }
        if (k=='s')
	   {
	    /*TextIn name= new TextIn(this,"File name",filename);
	    name.show();
	    filename=name.getS();
	    name.dispose();
	    save();
	    saved=true;*/
	   }
        if (k=='l')
	   {/*
	    TextIn name= new TextIn(this,"File name",filename);
	    name.show();
	    filename=name.getS();
	    name.dispose();
	    load();
	    repaint();*/
	   }
	if (k=='g')
           {/*
	    grid=!grid;
	    repaint();*/
	   }
        if (k=='o')
           {
	    //objectType=(objectType+1)%NB_TYPES;
            /*if (objectType==TYPE_OBSTACLE) objectType=TYPE_PLAQUE;
            else if (objectType==TYPE_PLAQUE) objectType=TYPE_OBSTACLE;
            repaint();*/
	   }
	if (sortie==true) System.exit(0); // Close - show<-false - 
       }
    }

 public void paint(Graphics g)
    {
     // pour reafficher...
     fond();
     
     /*
     String SobjectType=null;
     if (objectType==TYPE_OBSTACLE)
      SobjectType=new String("Obstacle.");
     else
      if (objectType==TYPE_PLAQUE)
        SobjectType=new String("Plaque");
      else
        SobjectType=new String("Pb");
     g.drawString("s : save  q : quit  l : load  g : grid="+grid+" o : object ("+SobjectType+")",20,34);
     g.setColor(wall);
     for(int i=1;i<nbR;i++)
	 {
	  TabR[i].drawRect(l+bord,h+bord,g);
	 }
     // Reviendre (Affichage)
     g.setColor(thing);
     for(int i=0;i<nbThing;i++)
	 {
	  TabThing[i].drawRect(l+bord,h+bord,g);
	 }
     g.setColor(Color.black);
     for(int i=0;i<nbThing;i++)
	 {
          g.drawString("W :"+(TabThing[i].x2-TabThing[i].x1)*(TabThing[i].y2-TabThing[i].y1),TabThing[i].x1+bord+l,TabThing[i].y1+bord+h);
	 }

     if (grid==true)
     {
      for(int i=0;i<DIMX/cg;i++)
       for(int j=0;j<DIMY/cg;j++)
        g.drawLine(l+bord+i*cg,h+bord+j*cg,l+bord+i*cg,h+bord+j*cg);
     }
     */
    }

 public void aff(String txt,int x,int y)
    {
     ch=txt;
     //@SuppressWarnings("unused")
     //051018 Graphics g=this.getGraphics();
    }

 public int calGrid(int v)
    {
	return v=(int)java.lang.Math.round((double)v/5)*5;
    }

 public void processMouseEvent(MouseEvent e)
    {
    	
    //System.out.println("This is a mouse event");
     if (e.getID() == MouseEvent.MOUSE_PRESSED)
       {
    	 //@SuppressWarnings("unused")
	 //051018 Graphics g=this.getGraphics();

	 int flags = e.getModifiers();
         if ((flags & MouseEvent.META_MASK) != 0) // Précédemment "Event" tout court
	     { // bouton droit souris
	     
	     /*
	       if (first == true)
	       {
	       int x=e.getX()-l-bord, y=e.getY()-h-bord;
	       int i=1;
	       while(i<nbR)
		 {
		  if (TabR[i].isInside(x,y)==true)
		      {
		       if (i<(nbR-1)) TabR[i]=TabR[nbR-1];
		       //System.out.println(i);
		       nbR--;
		      }
		  else i++;
		 }
                i=0;
                while(i<nbThing)
		 {
		  if (TabThing[i].isInside(x,y)==true)
		      {
                       System.out.println("IsInside !!! (i):"+i);
		       if (i<(nbThing-1)) TabThing[i]=TabThing[nbThing-1];
		       //System.out.println(i);
		       nbThing--;
		      }
		  else i++;
		 }

	       repaint();
		}
	*/
	     }
         else // bouton gauche souris
	 if (first == true)
	     { fx=e.getX(); fy=e.getY();
	 	/*
	       if (grid==true)
		   { fx=calGrid(fx-l-bord)+l+bord;
		     fy=calGrid(fy-h-bord)+h+bord;
		   }
	     if ((fx>=bord+l) && (fx<DIMX+l+bord) && (fy>=h+bord) && (fy<DIMY+h+bord))
	      {
   	       // dessiner la petite croix
	       g.drawLine(fx-1,fy,fx+1,fy);
	       g.drawLine(fx,fy-1,fx,fy+1);
	       first = false;
	      }*/
	     }
	 else // du : if (first==true)
	     {//@SuppressWarnings("unused")
	       //051018 int x=e.getX(), y=e.getY();
	       
	       /*
	       // dessiner le mur
	       if (grid==true)
		   { x=calGrid(x-l-bord)+l+bord;
		     y=calGrid(y-h-bord)+h+bord;
		   }
	       if (fx>x) { int t=fx; fx=x; x=t; }
	       if (fy>y) { int t=fy; fy=y; y=t; }
	       if (x<(bord+l)) x=bord+l;
	       if (x>=(DIMX+bord+l)) x=DIMX+bord+l-1;
	       if (y<(h+bord-1)) y=h+bord-1;
	       if (fx<(l+bord)) fx=l+bord;
	       if (fy<(h+bord)) fy=h+bord;
	       if (y>=(DIMY+bord+h)) y=DIMY+bord+h-1;

	       if (objectType==TYPE_OBSTACLE)
                { TabR[nbR] = new Rect(fx-bord-l,fy-bord-h,x-bord-l,y-bord-h,wall);
	          nbR++;
                }
               else
                if (objectType==TYPE_PLAQUE)
                { TabThing[nbThing] = new Rect(fx-bord-l,fy-bord-h,x-bord-l,y-bord-h,thing);
	          nbThing++;
                }
                */
	       repaint();
	       first = true;
	     }

       }
    }


}
