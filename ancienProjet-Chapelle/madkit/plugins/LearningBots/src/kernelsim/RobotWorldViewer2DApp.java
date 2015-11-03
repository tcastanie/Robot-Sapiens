// Copyright (C) 1997 by MadKit Team
package kernelsim;

import madkit.kernel.*;
//import java.io.*;
//import java.util.*;
import java.awt.*;
//import madkit.simulation.activators.*;


public class RobotWorldViewer2DApp extends Watcher  implements ReferenceableAgent
{
	static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
    public RobotProbeApp detector;
    protected Dimension gridSize;
    protected Dimension visibleSize;
    public static double PI=3.141593;

    RobotSchedulerApp sch; RmcLauncherApp rmc;

    public static float satpV[]=new float[50];
    public static float satiV[]=new float[50];

    public static int n_rob;
    public static boolean alt[]=new boolean[50];

    int dimx,dimy;

    String group;
    int cellSize = 6;
    int Rc;

    RobotAppEnv world;
    GridCanvas2DApp onScreen;
    Shaft[] tabS;             // tableau des puits (sources)
    Rect[] tabR;              // tableau des obstacles rect. fixes
    Plaques tabPlaques;       // tableau des objets (mobiles, découpables)
    RobotAppPhy[] theRobots;    // tableau des robots
    Shaft Base;
    Rect zone_Depot;
    int taille_Zone_Depot;

    Color colbase=Color.red;

    //** var pour affichages dans simu **/

    boolean flash = false;
    boolean show = true;
    boolean VectB = true;
    boolean RadarB = false;
    boolean ComB = false;
    boolean Field = false;
    int     NbA = 3;

    public void setFlash (boolean add){flash = add;}
    public boolean getFlash(){return flash;}
    public void setShow (boolean add){show = add;}
    public boolean getShow(){return show;}
    public boolean getVectAff(){return VectB;}
    public void setVectAff(boolean b) {VectB=b;}

    public boolean getRadarAff(){return RadarB;}
    public void setRadarAff(boolean b) {RadarB=b;}

    public boolean getCommunicationAff(){return ComB;}
    public void setCommunicationAff(boolean b) {ComB=b;}

    public int getNbA(){return NbA;}
    public void setNbA(int n) {NbA=n;}

    public boolean getField(){return Field;}
    public void setField(boolean b) {Field=b;}

    public int bord,h,l;

    public RobotWorldViewer2DApp(String g,RobotAppEnv re,RobotSchedulerApp s)
    {
	world = re;
	sch=s;
	tabS = world.TabS;
	tabR = world.TabR;
        tabPlaques=world.LesPlaques;
        // Reviendre (inutile ?)
        bord=world.bord;
        h=world.h;
        l=world.l;

	Base = world.Base;
        zone_Depot=world.Zone_Depot;
        taille_Zone_Depot=world.Taille_Zone_Depot;
	dimx=tabR[0].x2;
	dimy=tabR[0].y2;
	group = g;

	visibleSize = new Dimension(dimx,dimy);
    }

    public void initGUI()
    {
	//setBean(onScreen = new GridCanvas2DApp(dimx,dimy,this)); < deprecated
	onScreen = new GridCanvas2DApp(dimx,dimy,this);
	System.out.println("Dimension :"+dimx+" "+dimy);
	setGUIObject(onScreen);
	//onScreen.setSize(new Dimension(dimx,dimy));
    }

    public Dimension getVisibleSize()
    {
	return visibleSize;
    }


    public void activate()
    {
	detector = new RobotProbeApp(group,"robot");
	addProbe(detector);
	//update();
	theRobots = detector.getRobots();
	if (!isGroup(group)) { createGroup(true, group, null, null); }
        else { requestRole(group, "member", null); }
	//joinGroup(group);
	requestRole(group,"robot observer",null);
    }

	public void setRmc(RmcLauncherApp r)
	{
		rmc=r;
	}
	
	// Devenu inutile, le 050609
	/* 
	public int addSensorViewerToRobot(int rid)
	{
		if ((theRobots == null)||(rid<0)||(rid>n_rob)) return -1;
		rmc.addSensorViewer(rid);
		return rid;
	}*/

    /** Renvoie le numero du robot qui est sur le point (mx,my) 1<=num<=n **/
    public int getRobotId(int mx,int my)
    {	int x,y,R;
    	if(theRobots == null) return -1;
    	n_rob=theRobots.length;
    	byte bret=-1;
	for (int i=0;i<theRobots.length;i++)
	    {
	    	y=theRobots[i].C.yc;
	        x=theRobots[i].C.xc;
	        R=theRobots[i].C.R;
	        double doc;
	        doc= Math.sqrt( Math.pow( ((double)(mx-x)),2)+Math.pow( ((double)(my-y)),2 ) ); // Distance du clic au centre du robot
	        if (doc<=R)
	        {	
	        	bret=theRobots[i].N_agent;
	        }
	        //System.out.println("Agent "+theRobots[i].N_agent+" ("+x+","+y+","+R+") "+" ("+i+")("+mx+","+my+","+doc+") ");
	    }
    	return ((int)bret);
    }

public boolean debrayeRobot(int rid,boolean val)
{
	if ((theRobots == null)||(rid<1)||(rid>n_rob)) return false;
	System.out.println("Robot "+rid+" embrayage: "+val);
	theRobots[rid-1].debrayage=val;
	return val;
}

public boolean isDebrayeRobot(int rid)
{
	if ((theRobots == null)||(rid<1)||(rid>n_rob)) return false;
	boolean val=theRobots[rid-1].debrayage;
	System.out.println("Robot "+rid+" debrayage val= "+val);
	return val;
}

public int moveRobotTo(int rid,int mx,int my)
    {	//int x,y,R;
    
    	n_rob=theRobots.length;
    	if ((theRobots == null)||(rid<1)||(rid>n_rob)) return -1;
    	theRobots[rid-1].C.xc=mx;
    	theRobots[rid-1].C.yc=my;
    	theRobots[rid-1].xr=mx;
    	theRobots[rid-1].yr=my;
    	/*theRobots[rid-1].sxr=mx;
    	theRobots[rid-1].syr=my;*/
    	theRobots[rid-1].sxc=mx;
    	theRobots[rid-1].syc=my;
    	/*
    	     public float xr,yr;
      // Pour Undo:
      private float sxr,syr; // Sauvegarde des dernières val. de xr et yr
      private int sxc,syc; // Sauvegarde des dernières val. de C.xc et C.yc*/
    	return rid;
    }
    
public int getPlaqueId(int mx,int my)
    {	
    	int bret=-1;
    	
    	Plaque tmpPlq;
        for(int i=0;i<tabPlaques.GetSize();i++)
	 {
          //g.drawRect();
          tmpPlq=tabPlaques.GetPlaque(i);
          if ((mx>=tmpPlq.x1)&&(mx<=tmpPlq.x2)&&(my>=tmpPlq.y1)&&(my<=tmpPlq.y2))
          {
          	bret=i;
          	break;
          }
        
          //(tabPlaques.GetRect(i)).drawRect(0,0,g);
	 }
    	
    	return bret;
    }

public int movePlaqueTo(int rid,int mx,int my)
    {	
    	if ((rid<0)||(rid>=tabPlaques.GetSize())) return -1;
    	tabPlaques.movePlaqueC(rid,mx,my);
    	return rid;
    }    
 /** Dans cette classe, on décide de choisir son apparence:
    affichage classique du disque ROBOT */
    public void paintRobots(Graphics g)
    {

	int x,y,R,Rv; //,x2,y2,lc,Lc;
	if(theRobots == null && detector != null )	theRobots = detector.getRobots();

	if(theRobots != null)

	{


	n_rob=theRobots.length;
	for (int i=0;i<theRobots.length;i++)
	    {
		y=theRobots[i].C.yc;
	        x=theRobots[i].C.xc;
	        R=theRobots[i].C.R;
		Rv=theRobots[i].Rv;
		Rc=theRobots[i].Rc;
		alt[i]=theRobots[i].alt;

		float satp=theRobots[i].Satp;
		satpV[i]=satp;
		float sati=theRobots[i].Sati;
		satiV[i]=sati;
		float satiphy=theRobots[i].SatIPhy;
		
		// old: Color Cagtp = new Color(240,(int)(100+55*(satp+1)),140);
		Color Cagtp = new Color(240,(int)(100+55*(satiphy+1)),140);
		
	      if (theRobots[i].RobotType==theRobots[i].TYPE_POUSSEUR)
	      { // old: Cagtp = new Color(240,(int)(100+55*(satp+1)),140);
	      	Cagtp = new Color(240,(int)(100+55*(satiphy+1)),140);
	      }
	      else
              { // old: Cagtp = new Color(20,(int)(100+55*(satp+1)),140);
              	Cagtp = new Color(20,(int)(100+55*(satiphy+1)),140);
              }
		//if (alt[i]==true) Cagtp=Color.blue;
		/* Avant: on tenait compte de l'état sat/alt du robot
			pour choisir sa couleur ...
		if (sati!=0)
		
              if (theRobots[i].RobotType==theRobots[i].TYPE_POUSSEUR)
		    Cagtp = new Color(240,(int)(100+55*(sati+1)),140);
                  else Cagtp = new Color(20,(int)(100+55*(sati+1)),140);
                  */
		g.setColor(Cagtp);
		g.fillOval(x-R,y-R,R*2,R*2); // dessine l'agent total en Sat I
		//		g.setColor(Cagtp);
		//		g.fillOval(x-R/2,y-R/2,R,R); // dessine l'agent central en Sat P
		/*
		g.setColor(Color.yellow); // dessine les 8 capteurs
		for(int j=0;j<8;j++)
		    {
		     lc=(int)(theRobots[i].Ra*Math.cos(theRobots[i].Vtask.a+j*PI/4));
		     Lc=(int)(theRobots[i].Ra*Math.sin(theRobots[i].Vtask.a+j*PI/4));
		     g.drawLine(x+lc,y-Lc,x+lc,y-Lc);
		    }
		*/

		if (RadarB) { g.setColor(Color.blue); g.drawOval(x-10,y-10,20,20);}
		if (ComB) { g.setColor(Color.cyan); g.drawOval(x-Rc,y-Rc,2*Rc,2*Rc);}


		if (VectB==true)
		    {
			float ouv=theRobots[i].ouv; ////////////////// ouv
			int xv,yv;

			g.setColor(Color.red);
			/*
			xv=(int)theRobots[i].Vrep.vx; /////////////// rep
			yv=(int)-theRobots[i].Vrep.vy;

		        g.drawLine(x,y,x+xv,y+yv);

			g.setColor(Color.blue);
			xv=(int)theRobots[i].Vi.vx;
			yv=(int)-theRobots[i].Vi.vy;

		        g.drawLine(x,y,x+xv,y+yv);

			if (alt[i]==false) g.setColor(Color.green);
			else if (theRobots[i].rep==false) g.setColor(Color.blue);
			     else g.setColor(Color.yellow);
			*/

			VecteurR Vdir=new VecteurR(theRobots[i].Vtask.vx,theRobots[i].Vtask.vy);
			Vdir.fixN(20);
			xv=(int)Vdir.vx; ////////////// Vtask
			yv=(int)-Vdir.vy;

			g.drawLine(x,y,x+xv,y+yv);

			g.setColor(Color.red);

			g.drawArc(x-Rv,y-Rv,2*Rv,2*Rv,(int)((theRobots[i].Vtaskold.a-ouv)*180/PI),(int)(2*ouv*180/PI));

			xv=(int)(Rv*Math.cos(theRobots[i].Vtaskold.a-ouv));
			yv=(int)(-Rv*Math.sin(theRobots[i].Vtaskold.a-ouv));
			g.drawLine(x,y,x+xv,y+yv);
			xv=(int)(Rv*Math.cos(theRobots[i].Vtaskold.a+ouv));
			yv=(int)(-Rv*Math.sin(theRobots[i].Vtaskold.a+ouv));
			g.drawLine(x,y,x+xv,y+yv);
			/*
			g.setColor(Color.yellow); // vect signaux rep -> Vsli
			xv=(int)theRobots[i].Vsli.vx;
			yv=(int)-theRobots[i].Vsli.vy;

			g.drawLine(x,y,x+xv,y+yv);
			*/
		    }

         Font font=new Font("sanserif",Font.PLAIN,10);
         g.setFont(font);

	 g.setColor(Color.black);
	 String s= String.valueOf(i+1);
	 if (theRobots[i].exp)
          g.drawString(s+"  "+theRobots[i].Tcurrent+"*",x-9,y-2);
         else
           g.drawString(s+"  "+theRobots[i].Tcurrent,x-9,y-2);

	 // Avant: s= String.valueOf((int)(127*satp));
	 s= String.valueOf((int)(127*satiphy));
	 g.drawString(s,x-10,y+11);

	 if (sati!=0)
	    {
	     s= String.valueOf((int)(127*sati));
	     g.drawString(s,x-10,y+26);
	    }

	 if (theRobots[i].alt==true) s="A";
	 else s=""; // String.valueOf(theRobots[i].Tcurrent);
	 g.drawString(s,x+4,y-2);
	 /*
	 if (theRobots[i].proche==true)
	  {
	    s="P";
	    g.setColor(Color.black);
	   g.drawString(s,x-12,y+4);
	  }
	 */
	}
	}
   }


    public void paintEnv(Graphics g)
    {
	g.setColor(Color.white);
	g.fillRect(0,0,dimx,dimy);

	//g.setColor(Color.gray);
	for(int i=1;i<world.nbR;i++)
	    {
		tabR[i].drawRect(0,0,g);
	    }
        g.setColor(Color.black);
        for(int i=1;i<world.nbR;i++)
	 {
          //  Affichage num de mur
	  g.drawString("n "+i,tabR[i].x1+4,tabR[i].y1+12);
          //  Affichage num de mur et coordonnées
       	  /*g.drawString("n "+i+"("+tabR[i].x1+","+tabR[i].y1+")-("+tabR[i].x2+","+tabR[i].y2+")" ,
                       tabR[i].x1+4,tabR[i].y1+12);*/
         }


        //zone_Depot.drawRect(0,0,g);
        zone_Depot.drawC(0,0,taille_Zone_Depot/2,g);
        //zone_Depot.drawRect(0,0,g);

        g.setColor(world.thing);
        for(int i=0;i<tabPlaques.GetSize();i++)
	 {
          (tabPlaques.GetRect(i)).drawRect(0,0,g);
	 }

        Plaque tmpPlq;
        g.setColor(Color.darkGray);
        for(int i=0;i<tabPlaques.GetSize();i++)
	 {
          //g.drawRect();
          tmpPlq=tabPlaques.GetPlaque(i);
          g.drawRect(tmpPlq.x1,tmpPlq.y1,tmpPlq.x2-tmpPlq.x1,tmpPlq.y2-tmpPlq.y1);
          //(tabPlaques.GetRect(i)).drawRect(0,0,g);
	 }

        g.setColor(Color.black);

        for(int i=0;i<tabPlaques.GetSize();i++)
	 {
          tmpPlq=tabPlaques.GetPlaque(i);
          drawPlaque(tmpPlq,i,g);
         }
        /*
	for(int i=0;i<world.nbS;i++)
	    {
	      tabS[i].drawShaftE(0,0,g);
	      tabS[i].drawC(0,0,g); // dessine les disques de la lumiere
	    }*/

	g.setColor(colbase);
	Base.drawShaft(0,0,g);

	 Font font=new Font("sanserif",Font.PLAIN,10);
         g.setFont(font);

	 g.setColor(Color.blue);
	 String s= String.valueOf(sch.iteration);
	 g.drawString("it="+s+" nsim="+RobotSchedulerApp.nfic,10,10);

    }


    public void paintInfo(Graphics g)
    {
	paintEnv(g);
	paintRobots(g);
    }


    public void observe()
    {
	if (show)
	    {
		if (! flash) onScreen.repaint();
		else onScreen.flash();
	    }
    }
    public void drawDecoup(Plaque Plq,Graphics g)
    {
        //  Si decoupe:

          if (Plq.dec_prog>0)
            {
              if (Plq.dec_x1>Plq.dec_x2)
              { Plq.dec_x1=Plq.dec_x2;
                Plq.dec_x2=Plq.dec_x1-Plq.dec_x2;
                Plq.dec_x1=Plq.dec_x1-Plq.dec_x2;}
              if (Plq.dec_y1>Plq.dec_y2)
              { Plq.dec_y1=Plq.dec_y2;
                Plq.dec_y2=Plq.dec_y1-Plq.dec_y2;
                Plq.dec_y1=Plq.dec_x1-Plq.dec_y2;}
              int PtStop=-1;
              if (Plq.dec_x1==Plq.dec_x2)
                { // Trait vertical
                  PtStop=(int)(Plq.dec_y1+(Plq.dec_y2-Plq.dec_y1)*Plq.dec_prog);
                  // Partie deja decoupee
                  g.drawLine(Plq.dec_x1,Plq.dec_y1,Plq.dec_x2,PtStop);
                  // Partie pas encore decoupee: en pointillés
                  for (int yi=PtStop;yi<Plq.dec_y2;yi+=4)
                    {g.drawLine(Plq.dec_x1,yi,Plq.dec_x2,yi+1);}
                }
              else if (Plq.dec_y1==Plq.dec_y2)
                { // Trait horizontal
                  PtStop=(int)(Plq.dec_x1+(Plq.dec_x2-Plq.dec_x1)*Plq.dec_prog);
                  // Partie deja decoupee
                  g.drawLine(Plq.dec_x1,Plq.dec_y1,
                            PtStop,Plq.dec_y2);
                  // Partie pas encore decoupee: en pointillés
                  for (int xi=PtStop;xi<Plq.dec_x2;xi+=4)
                    g.drawLine(xi,Plq.dec_y1,xi+1,Plq.dec_y2);
                }

              // Dessin du point de decoupe
              if ((Plq.Rob_Decoup!=-1)&&(PtStop!=-1))
                { int irob=-1;
                  for (int indi=0;indi<n_rob;indi++)
                    if (theRobots[indi].N_agent==Plq.Rob_Decoup)
                      irob=indi;
                  if ((irob!=-1)&&(irob<n_rob))
                  { if (Plq.dec_x1==Plq.dec_x2)
                    {g.drawLine((int)theRobots[irob].xr,
                              (int)theRobots[irob].yr,
                              Plq.dec_x1,PtStop);
                     g.fillOval(Plq.dec_x1-3,PtStop-3,6,6);
                    }
                    else
                    {g.drawLine((int)theRobots[irob].xr,
                              (int)theRobots[irob].yr,
                              PtStop,Plq.dec_y1);
                     g.fillOval(PtStop-3,Plq.dec_y1-3,6,6);
                    }

                  }
                  else System.out.println("Bug WorldViewer drawDecoup");
                }

            }
	  //g.drawString("n "+i,tabR[i].x1+4,tabR[i].y1+12);
  }

  public void drawPlaque(Plaque tmpPlq,int i,Graphics g)
  {
          int tmp_id=tmpPlq.GetId();
          g.setColor(Color.black);
	  g.drawString("W ("+i+"/"+tmp_id+"): "+(tmpPlq.Surface()),tmpPlq.x1,tmpPlq.y1);
          // Affichage des coordonnées:
          /*g.drawString("("+tmpPlq.GetRect().x1+","+tmpPlq.GetRect().y1+")-("+
                          tmpPlq.GetRect().x2+","+tmpPlq.GetRect().y2+")"
                        ,tabPlaques.GetPlaque(i).x1,tabPlaques.GetPlaque(i).y1+12);*/
          // Dessin de la découpe
          g.setColor(Color.red);
          drawDecoup(tmpPlq,g);
  }

}
