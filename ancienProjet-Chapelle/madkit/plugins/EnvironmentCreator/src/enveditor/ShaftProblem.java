package enveditor;

import madkit.kernel.Agent;
import javax.swing.*;
import java.util.*;

import madkit.kernel.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class ShaftProblem extends JPanel{

 int TYPE_OBSTACLE = 1 ;
 int TYPE_PLAQUE = 2;
 int NB_TYPES =  2;
 int objectType=TYPE_OBSTACLE;
 Color wall = Color.blue;
 Color thing= Color.green;

 private int lastx,lasty,fx,fy;
 static int etat=0,yT=0;

 int DIMX; // dimension de l'environnement
 int DIMY;
 int bord = 10, h=40, l=10;
 int Lbox = 9;
 int Ra = 7;
 char k;
 byte flagBox=0;

 Color box  = Color.red;
 Color base= Color.white;
 Color lineColor = Color.green;

 String ch;
 String filename=null;

 Rect TabR[] = new Rect[100];
 Rect TabThing[] = new Rect[100];
 Shaft TabB[] = new Shaft[100];
 Shaft Base ;  // la base est un puits un peu particulier !
    //CirAgent TabA[] = new CirAgent[100];
 int nbR,nbB,nbase = 0;
 int nbThing=0;

 Car    Tc[];
 int cpt = 0;
 boolean sortie=false,first=true,saved=false;

  public static void main(String[] args)
    {
      String env = args[0];
      String filebox=null;
      try { filebox  = args[1];}
      catch (Exception e) {}
      new ShaftProblem(env,filebox);
    }

 public ShaftProblem(String env,String fb)
    { //super("Create Winding Shaft Problem");
      loadfond(env);
      if (fb!=null) { filename=fb; load(); } else filename=".sha";
      DIMX=TabR[0].x2;
      DIMY=TabR[0].y2;
      this.init();
      this.setSize(DIMX+2*bord+2*l,DIMY+2*bord+h+l);
      //this.pack();
      //this.show();
      setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
      fond();
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
    }

 public void processKeyEvent(KeyEvent e)
    {
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
	if (sortie==true) System.exit(0);
       }
    }

 public void paint(Graphics g)
    {
     // pour reafficher...
     fond();
     g.drawString("s : save  q : quit  l : load",20,34);
     g.setColor(wall);
     for(int i=1;i<nbR;i++)
	 {
	  TabR[i].drawRect(l+bord,h+bord,g);
	 }

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

     for(int i=0;i<nbB;i++)
	 {
	 TabB[i].drawShaft(l+bord,h+bord,g);
	 String txtv=String.valueOf(TabB[i].vol);
	 String txtd=String.valueOf(TabB[i].deb);
	 Font font=new Font("sanserif",Font.PLAIN,8);
	 g.setFont(font);
         g.drawString(txtv+","+txtd,l+bord+TabB[i].x-Lbox,h+bord+TabB[i].y+2*Lbox);
	 }
     if (nbase==1) Base.drawShaft(l+bord,h+bord,g);
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
    {/*
     if (e.getID() == MouseEvent.MOUSE_PRESSED)
       {
	 Graphics g=this.getGraphics();

	 int flags = e.getModifiers();
         if ((flags & Event.META_MASK) != 0)
	     { // bouton droit souris **************************************************
	       fx=e.getX(); fy=e.getY();
	       // ========== test effacer un puits ==================
 	       if (flagBox==0)
	       {
		boolean find=false;
		int i=0;
		while((i<nbB) && (find==false))
		 {
		  if (TabB[i].isInside(fx-l-bord,fy-h-bord)==true) find=true;
		  else i++;
		 }
		if (find==true)
		 {
		  if (i < nbB-1) TabB[i]=TabB[nbB-1];
		  nbB--;
		  repaint();
		 }
		else
		 // ============== test effacer base ================
		 {
		  if ((nbase==1) && (Base.isInside(fx-l-bord,fy-h-bord)==true))
		  {
		  nbase=0;
		  repaint();
		  }
		 else
		 // ========= sinon poser une base ====================
		 {
	          if (nbase==0)
		  {
		  //verif zone depos possible
		      // fx=fx-Ra; fy=fy-Ra;
		  boolean libre=false;
		     //  if ((fx>=l+bord) && (fx<=DIMX+l+bord-Lbox-Ra) && (fy>=h+bord) && (fy<=DIMY+h+bord-Lbox-Ra)) libre=true;
                  if ((fx>=l+bord-1) && (fx<=DIMX+l+bord-Lbox+1) && (fy>=h+bord-1) && (fy<=DIMY+h+bord-Lbox+1)) libre=true;
	          Rect newbox= new Rect(fx-bord-l,fy-bord-h,fx-bord-l+Lbox,fy-bord-h+Lbox,null);
      		  i=1;
		  while((i<nbR) && (libre==true))
		   {
		    if ( newbox.isInter(TabR[i])==true) libre=false;
		    i++;
		   }
                  // Reviendre (bizarre)
                  i=0;
                  //System.out.println("nbThing="+nbThing);
      		  while((i<nbThing) && (libre==true))
		   {
		    if ( newbox.isInter(TabThing[i])==true) libre=false;
		    i++;
		   }
                 // Reviendre: verifier aussi avec les thing
		 i=0;
		  while((i<nbB) && (libre==true))
	           {
		    Rect other= new Rect(TabB[i].x,TabB[i].y,TabB[i].x+Lbox,TabB[i].y+Lbox,null);
		    if (newbox.isInter(other)==true) libre=false;
		    i++;
		   }

		  if (libre==true)
		    {
		     Base=new Shaft(fx-bord-l,fy-bord-h,Lbox,Lbox,0,0,base);
		     // dessiner la base
		     Base.drawShaft(l+bord,h+bord,g);
		     nbase=1;
		    }
		  }
		 }
	       }
	       }

	     }
         else // bouton gauche souris *****************************************************
	   {
	    fx=e.getX(); fy=e.getY();
	     if ((fx>=l+bord-1) && (fx<=DIMX+l+bord-Lbox+1) && (fy>=h+bord-1) && (fy<=DIMY+h+bord-Lbox+1))
	      {
	       // verif espace libre
	       Rect newbox= new Rect(fx-bord-l,fy-bord-h,fx-bord-l+Lbox,fy-bord-h+Lbox,null);
	       int i=1;
	       boolean libre=true;
	       while((i<nbR) && (libre==true))
	        {
	          if ( (TabR[i].isInter(newbox)==true) || (newbox.isInter(TabR[i]))) libre=false;
	          i++;
	        }

               i=0;
               while((i<nbThing) && (libre==true))
	        {
	          if ( (TabThing[i].isInter(newbox)==true) || (newbox.isInter(TabThing[i]))) libre=false;
                  i++;
                }

	       i=0;
	       while((i<nbB) && (libre==true))
	       {
	       Rect other= new Rect(TabB[i].x,TabB[i].y,TabB[i].x+Lbox,TabB[i].y+Lbox,null);
	       if ( (other.isInter(newbox)==true) || (newbox.isInter(other))) libre=false;
	       i++;
	       }

               if (nbase==1)
		   {
		    Rect other= new Rect(Base.x,Base.y,Base.x+Lbox,Base.y+Lbox,null);
		    if ((Base.isInter(newbox)==true) || (newbox.isInter(other))) libre=false;
		   }
	       if (libre==true)
	       {
		// saisir volume du puits
                TextIn Fvol= new TextIn(this,"Volume of Shaft","");
		Fvol.show();
		int v=0;
		try { v= Integer.parseInt(Fvol.getS());
		    } catch (NumberFormatException en) {System.out.println(en);}
	        Fvol.dispose();
		// saisir debit  du puits
                TextIn Fdeb= new TextIn(this,"Rate of Shaft","");
		Fdeb.show();
		int d=0;
		try { d= Integer.parseInt(Fdeb.getS());
		    } catch (NumberFormatException en) {System.out.println(en);}
	        Fdeb.dispose();
                //
	       TabB[nbB]=new Shaft(fx-bord-l,fy-bord-h,Lbox,Lbox,d,v,box);
	       TabB[nbB].drawShaft(l+bord,h+bord,g);
	       nbB++;
	       setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		 paint(g);
	       }
	      }
	   }

      }*/
   }

 public void processMouseMotionEvent(MouseEvent e)
    {
     if (e.getID() == MouseEvent.MOUSE_DRAGGED)
       {
	   /*   int x=e.getX(), y=e.getY();
	   Graphics g=this.getGraphics();
	   g.drawRect(lastx,lasty,x-lastx,y-lasty);
	   */
       }
     else super.processMouseMotionEvent(e);
    }

 public void save()
    {
     try {
        FileOutputStream fos = new FileOutputStream(filename);
	PrintWriter pw = new PrintWriter((OutputStream)fos);
        for (int i =0; i<nbB;i++)
		pw.println(TabB[i].x+" "+TabB[i].y+" "+TabB[i].vol+" "+TabB[i].deb);

	if (nbase==1)
	 { pw.println('B');
	   pw.println(Base.x+" "+Base.y);
	 }
	pw.flush();
	pw.close();
      }
     catch (IOException e) {System.out.println(e);}

    }

 public void loadfond(String fileenv)
    {
     File f;
     FileReader in = null;
     int size=0;
     char[] data = null;

     try {
        f=new File(fileenv);
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
     TabR[nbR] = new Rect();
     nbThing=0;
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

		   if (n==6)
		       { n=1;
                         if (RectTemp.color==wall)
                          {
                            TabR[nbR].x1=RectTemp.x1;
                            TabR[nbR].x2=RectTemp.x2;
                            TabR[nbR].y1=RectTemp.y1;
                            TabR[nbR].y2=RectTemp.y2;
                            TabR[nbR].color=RectTemp.color;
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
                            }
		       }
		   finNb=false; l=0;
		   }
	       }
	  i++;
	 }
    }


public void load() // charger les box(es) et les agents !
    {
     File f;
     FileReader in = null;
     int size=0;
     char[] data = null;

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

     // ===================  traitement du texte charge :
     int i=0,n=1,l=0,v;
     char c;
     char[] nbT= new char[12];
     boolean finNb=false;
     nbB=0; // <= on efface les anciennes donnees ...
     int j=0, agt=0;
     TabB[nbB] = new Shaft(0,0,Lbox,Lbox,0,0,box);
     Base = new Shaft(0,0,Lbox,Lbox,0,0,base);
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
	       {
		 switch (agt) {
		   case 0 :
		   {
		    if (finNb==true)
		    {
		     String s=String.valueOf(nbT,0,l);
		     v=Integer.parseInt(s);
		     switch (n) {
		      case 1: { TabB[nbB].x=v; break;}
		      case 2: { TabB[nbB].y=v; break;}
                      case 3: { TabB[nbB].vol=v; break;}
		      case 4: { TabB[nbB].deb=v; j++;  break;}
		     }
		     n++;
		     finNb=false; l=0;
		    }
	            if ((c=='\n') && (j>0))
		      {
			  //System.out.println("saut !");

			  nbB++; j=0; n=1;
			  TabB[nbB]=new Shaft(0,0,Lbox,Lbox,0,0,box);
		      }
		    if (c=='B')
			{
			 agt=1;
			 nbase=1;
			}
		    break;
		   }
	       case 1 :
		   {
		    if (finNb==true)
		    {
		     String s=String.valueOf(nbT,0,l);
		     v=Integer.parseInt(s);
		     switch (n) {
		      case 1: { Base.x=v; break;}
		      case 2: { Base.y=v; break;}
		      }
		     n++;

		    finNb=false; l=0;
		    }
		   }
		 }
	       }
	  i++;
	 }

    }

}




