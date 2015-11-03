package enveditor;

import madkit.kernel.Agent;
import javax.swing.*;
import java.util.*;

import madkit.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.io.*;

import java.lang.Integer;

public class CreateEnv extends JPanel implements ActionListener{
 
 public int TYPE_OBSTACLE = 1;
 public int TYPE_PLAQUE = 2;
 int NB_TYPES =  2;
 public int objectType=TYPE_OBSTACLE;
 Color wall = Color.blue;
 Color thing= Color.green;

 private int lastx,lasty,fx,fy;
 static int cg=5;
 static int etat=0,yT=0;

 int DIMX; // dimension de l'environnement
 int DIMY;
 int bord = 10, h=40, l=10;
 char k;



 String ch;
 String filename=".env";

 Rect TabR[] = new Rect[100];
 int nbR = 1;
 Rect TabThing[] = new Rect[100];
 int nbThing = 0;


 Car    Tc[];
 int cpt = 0;
 boolean sortie=false,first=true,saved=false,grid=true;

  /*public static void main(String[] args)
    {
      int x = 100;
      int y = 100;
      try { x = Integer.parseInt(args[0]); }
      catch (Exception e) {}
      try { y = Integer.parseInt(args[1]); }
      catch (Exception e) {}
      new CreateEnv(x,y);
    }*/

 public CreateEnv(int sx, int sy)
    { //super("Create Environment");
      DIMX=sx;
      DIMY=sy;
      this.init();
      TabR[0] = new Rect(0,0,sx,sy,null);
      this.setSize(DIMX+2*bord+2*l,DIMY+2*bord+h+l);
      //this.pack();
      TabThing[0]=new Rect(0,0,sx,sy,null);
      
      //this.show();
      //setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      //fond();
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
     g.setColor(wall);
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
	    YesNoDialog quit = new YesNoDialog(this,"Quit ?","Save before quitting ?","Yes, save and quit","No, just quit","Cancel");
	    quit.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	       if (e.getActionCommand().equals("yes")) { k='s'; sortie=true;}
	       if (e.getActionCommand().equals("no")) { sortie=true;}
	      }
	     });
	    quit.show();

	   }
        if (k=='s')
	   {
	    TextIn name= new TextIn(this,"File name",filename);
	    name.show();
	    filename=name.getS();
	    name.dispose();
	    save();
	    saved=true;
	   }
        if (k=='l')
	   {
	    TextIn name= new TextIn(this,"File name",filename);
	    name.show();
	    filename=name.getS();
	    name.dispose();
	    load();
	    repaint();
	   }
	if (k=='g')
           {
	    grid=!grid;
	    repaint();
	   }
        if (k=='o')
           {
	    //objectType=(objectType+1)%NB_TYPES;
            if (objectType==TYPE_OBSTACLE) objectType=TYPE_PLAQUE;
            else if (objectType==TYPE_PLAQUE) objectType=TYPE_OBSTACLE;
            repaint();
	   }
	if (sortie==true) System.exit(0); // Close - show<-false - 
       }
    }

 public void paint(Graphics g)
    {
     // pour reafficher...
     fond();
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

 public int calGrid(int v)
    {
	return v=(int)java.lang.Math.round((double)v/5)*5;
    }

 public void processMouseEvent(MouseEvent e)
    {
    	
    //System.out.println("This is a mouse event");
     if (e.getID() == MouseEvent.MOUSE_PRESSED)
       {
	 Graphics g=this.getGraphics();

	 int flags = e.getModifiers();
         if ((flags & Event.META_MASK) != 0)
	     { // bouton droit souris
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
	     }
         else // bouton gauche souris
	 if (first == true)
	     { fx=e.getX(); fy=e.getY();
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
	      }
	     }
	     	 else // du : if (first==true)
	     {
	       int x=e.getX(), y=e.getY();
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
	       repaint();
	       first = true;
	     }

       }
    }

/* public void processMouseMotionEvent(MouseEvent e)
    {
     if (e.getID() == MouseEvent.MOUSE_DRAGGED)
       {
	   /*   int x=e.getX(), y=e.getY();
	   Graphics g=this.getGraphics();
	   g.drawRect(lastx,lasty,x-lastx,y-lasty);
	   */
 /*      }
     else super.processMouseMotionEvent(e);
    }*/

 public void save()
    {
     try {
        FileOutputStream fos = new FileOutputStream(filename);
	PrintWriter pw = new PrintWriter((OutputStream)fos);
        for (int i =0; i<nbR;i++)
	    {   // Reviendre : sauver le type (Ok ?)
		pw.println(TabR[i].x1+" "+TabR[i].y1+" "+TabR[i].x2+" "+TabR[i].y2+" "+TYPE_OBSTACLE);
	    }
        // Reviendre (Save)
        System.out.println("Nb de choses (nbThing)="+nbThing);
        for (int i =0; i<nbThing;i++)
	    {   System.out.println("Thing n i="+i+" ("+TabThing[i].y1+" "+TabThing[i].x2+" "+TabThing[i].y2+" "+TYPE_PLAQUE+")");
		pw.println(TabThing[i].x1+" "+TabThing[i].y1+" "+TabThing[i].x2+" "+TabThing[i].y2+" "+TYPE_PLAQUE);
	    }

	pw.flush();
	pw.close();
      }
     catch (IOException e) {System.out.println(e);}
    }

 public void load()
    {
     File f;
     FileReader in = null;
     int size=0;
     char[] data = null;
     Dimension tmpdim= new Dimension(0,0);
     try {
        f=new File(filename);
	in = new FileReader(f);
        size = (int) f.length();
	data = new char[size];
	int chars_read=0;
	while(chars_read<size)
	    chars_read += in.read(data, chars_read, size-chars_read);
     }
     catch (IOException e) {System.out.println(e);}
     finally { try { if (in != null) in.close();} catch (IOException e) {} }
     // traitement du texte charge :
     int i=0,n=1,l=0,v;
     char c;
     char[] nbT= new char[12];
     boolean finNb=false;
     nbR=0; // <= on efface les anciennes donnees ...
     nbThing=0;
     TabR[nbR] = new Rect();
     TabThing[nbThing] = new Rect();
     Rect RectTemp=new Rect();
     while(i<size)
	 {
	  c=data[i];
	  if ((c>='0') && (c<='9'))
	      {
	       nbT[l]=c;
	       l++;
	       finNb=true;
	      }
	   else
	       {   if (finNb==true)
		   {
		    String s=String.valueOf(nbT,0,l);
		    v=Integer.parseInt(s);
		    switch (n) {
		    case 1: RectTemp.x1=v;break;
		    case 2: RectTemp.y1=v;break;
		    case 3: RectTemp.x2=v;break;
		    case 4: RectTemp.y2=v;break;
                    case 5: {
                              if (v==TYPE_OBSTACLE) RectTemp.color=wall;
                              if (v==TYPE_PLAQUE) RectTemp.color=thing;
                              break;
                            }
		   }
		   n++;
                   // reviendre (nvar) : n++
		   if (n==6)
		       { n=1;
                         if (RectTemp.color==wall)
                          {
                            TabR[nbR].x1=RectTemp.x1;
                            TabR[nbR].x2=RectTemp.x2;
                            TabR[nbR].y1=RectTemp.y1;
                            TabR[nbR].y2=RectTemp.y2;
                            TabR[nbR].color=RectTemp.color;
                            tmpdim = new Dimension ((int)Math.max((Math.max(RectTemp.x1,RectTemp.x2)),(tmpdim.getWidth()))
                            			,(int)Math.max((Math.max(RectTemp.y1,RectTemp.y2)),(tmpdim.getHeight())));
                            nbR++;
		            TabR[nbR]= new Rect();
                          }
                         else if (RectTemp.color==thing)
                            {
                              TabThing[nbThing].x1=RectTemp.x1;
                              TabThing[nbThing].x2=RectTemp.x2;
                              TabThing[nbThing].y1=RectTemp.y1;
                              TabThing[nbThing].y2=RectTemp.y2;
                              TabThing[nbThing].color=RectTemp.color;
                              nbThing++;
		              TabThing[nbThing]= new Rect();
		              tmpdim = new Dimension ((int)Math.max((Math.max(RectTemp.x1,RectTemp.x2)),(tmpdim.getWidth()))
                            			,(int)Math.max((Math.max(RectTemp.y1,RectTemp.y2)),(tmpdim.getHeight())));
                            }
		       }
		   finNb=false; l=0;
		   }
	       }
	  i++;
	 }
	 
	 
	 DIMX=(int)(tmpdim.getWidth());
	 DIMY=(int)(tmpdim.getHeight());
	 System.out.println("dimension: "+tmpdim);
	 //return tmpdim;
    }

}
