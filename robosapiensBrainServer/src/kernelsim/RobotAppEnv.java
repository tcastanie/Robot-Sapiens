package kernelsim;

import java.util.*;
import java.awt.*;
import java.io.*;
//import java.lang.Object;
import madkit.kernel.*;
//import java.net.*;

/** The RobotAppEnv class, used to simulate the environment evolution
  @author Olivier Simonin, Jérôme Chapelle
  @version 2.0 */
  
public class RobotAppEnv extends AbstractAgent implements ReferenceableAgent 
{
	static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
    private String fileEnv;
    public  static int NONE  = 0;
    public  static int NORTH = 1;
    public  static int SOUTH = 2;
    public  static int EAST  = 3;
    public  static int WEST  = 4;

    int TYPE_OBSTACLE = 1;
    int TYPE_PLAQUE = 2;
    int TYPE_ROBOT = 4;
    int DONT_CARE_PLAQUE = 8;
    int DONT_CARE_OBSTACLE = 16;
    int DONT_CARE_FRIENDS = 32;
    int NB_TYPES =  4;
    int objectType=TYPE_OBSTACLE;

    // Si coef>1 un choc augmente la force
    // Si coef<1 un choc diminue la force
    float coef_rebond_plq=1;
    float coef_rebond_rob=1;

    static int x=0;
    static int y=21;
    int R=17;

    public /*static*/ int Lbox = 9;

    static int cpt=0;
    public int enr=0;
    public int Nrob=30;
    public MatEnv mat = null;

    public Shaft TabS[]=null;
    public Shaft Base;
    public Rect Zone_Depot;
    public int Taille_Zone_Depot;

    public Rect TabR[]=null;
    public Plaques LesPlaques=null; //new Plaques();

    // public Vector theRobots= new Vector();

    public int Rlight=190;

    ////////////////////

    public int nbS=0,nbR=0;
    public int bord = 10, h=40, l=10;
    public static int agentsNb=0;

    public byte N_puitsFirst=0;
    public byte N_puitsLast=-2;
    public byte N_base=0;
    public PrintWriter pw;

    Color wall; //= Color.blue;
    //Color color_wall = Color.blue;
    Color thing= Color.green;
    Color box;
    Color color_wall = Color.gray;
    Color color_line = Color.green;
    Color base=Color.white;
    String group,file_ref;

    int dimx,dimy;
    /** Etat des 8 capteurs IR (mis à jour par radarV) */
	CapIR capteurs=new CapIR();
    /** Creates a new environment using the file <i>file<i> for the simulation that belongs to the group <i>group</i>.
    */
    public RobotAppEnv(String group,String file)
    {
	this.group = group;
	wall = Color.lightGray;
	box  = Color.red;

	file_ref=file;
	String fileEnv = file+".env";
	
	mat = new MatEnv(fileEnv);
	
	//System.out.println("mat.dimx="+mat.dimx+" dimy="+mat.dimy);
	//System.out.println("la mat:"+mat.toString());
	String fileS = file+".sha";
	
	loadfond(fileEnv);

	LoadShaft(fileS);

	String N= String.valueOf(Nrob);

	if (!RmcLauncherApp.is_applet)
	try {
         FileOutputStream fos = new FileOutputStream(file+N+".res");
         pw = new PrintWriter((OutputStream)fos);
	 }
	catch (IOException e)
	{	RmcLauncherApp.is_applet=true;
		System.out.println(e);
	}
	dimx=TabR[0].x2;
	dimy=TabR[0].y2;
        LesPlaques.SetWorldDim(dimx,dimy);
	System.out.println("25Env52 dimx = "+TabR[0].x2);
    }

    /** Computes x at the power of 2 */
    public int carre(int x) {return x*x;}

    /** Tells if the RobotAppPhy <i>rob</i> intersects with another RobotAppPhy in the same environment */
    public int InterRob(RobotAppPhy rob)
    {
	int nI=0;
	RobotAppPhy r;

        for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
         {
          r=(RobotAppPhy)e.nextElement();
	  if (rob!=r)
	    if (r.C.isInter(rob.C)==true) nI++;
	 }
        return nI;
    }
   /** Tells if the RobotAppPhy <i>rob</i> intersects with the obstacles that belong to the environment */
   public int InterObs(RobotAppPhy rob)
    {
	int nI=0;

        for(int i=1;i<nbR;i++)
         {
	    if (rob.C.isInter(TabR[i])==true) nI++;
	 }
        return nI;
    }
   /** Tells if the RobotAppPhy <i>rob</i> intersects with the Plates (fr:Plaques) that belong to the environment */
   public int InterPlq(RobotAppPhy rob)
    {
	int nI=0;

        for(int i=0;i<LesPlaques.GetSize();i++)
         {
	    if (rob.C.isInter((LesPlaques.GetRect(i))) ==true) nI++;
	 }
        return nI;
    }

    public boolean test_moveD(RobotAppPhy rob)
    {
	boolean go=true;
	RobotAppPhy r;

	// test de non collision avec un autre rob
	for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
         {
          r=(RobotAppPhy)e.nextElement();
	  if (rob!=r)
	    if (r.C.isInter(rob.C)==true) go=false;
	 }

	// test de non collision avec un mur

	int i=1;
        while((i<nbR) && (go==true))
        {
	   if (rob.C.isInter(TabR[i])==true) go=false;
	   i++;
        }

      	// test de non collision avec une plaque
        /*
	i=0;
        while((i<TabPlaques.GetSize()) && (go==true))
        {
	   if (rob.C.isInter(TabPlaques.GetRect(i))==true) go=false;
	   i++;
        }*/

	return go;

    }

    public boolean test_moveP(RobotAppPhy rob)
    {
	boolean go=true;

      	// test de non collision avec une plaque

	int i=0;

        while((i<LesPlaques.GetSize()) && (go==true))
        {
	   if (rob.C.isInter(LesPlaques.GetRect(i))==true) go=false;
           //if (isInterR(rob,LesPlaques.GetPlaque(i))==true) go=false;
	   i++;
        }

	return go;
    }

    public Vecteur Dlight(RobotAppPhy rob)
    {
     Vecteur r=new Vecteur(0,0);
     int x0=rob.C.xc;
     int y0=rob.C.yc;

     CirAgent cBig = new CirAgent(x0,y0,Rlight,null);

     for(int i=0;i<nbS;i++)
        {
	 cBig.R=TabS[i].vol;
	 if (cBig.isInside(TabS[i].x+4,TabS[i].y+4)==true)
	 {
	    if ((rob.N_agent<3) || (TabS[i].vol<=400))  r.fix(TabS[i].x+4-x0,-(TabS[i].y+4-y0));
	 }
	}
     return r;
    }


 public float MesureEnv(RobotAppPhy rob,float rm)
    {
     double alpha=0;

     float vx,vy,d;
     boolean sense;
     double val=0;

     Vecteur v= new Vecteur(0,0);
     int x0=rob.C.xc;
     int y0=rob.C.yc;
     int xt,yt;

     CirAgent cBig = new CirAgent(x0,y0,(int)rm,null);
     RobotAppPhy r;

     v.s=1;
     d=rm;

     for(alpha=0;alpha<(2*Math.PI);alpha+=Math.PI/4)
     {

     vx=(float)Math.cos(alpha);
     vy=(float)Math.sin(alpha);
     xt=x0+Math.round(vx*d);
     yt=y0-Math.round(vy*d);

     sense=false;
     // test murs exterieurs

     if ((xt<0) || (yt<0) || (xt>dimx) || (yt>dimy))
	     {
		 val=val-0.12;
		 sense=true;
      	     }
     else
     {
     // test murs interieurs:

       for(int i=1;i<nbR;i++)
        {
	 if (cBig.isInter(TabR[i])==true)
	 {

	    if (TabR[i].isInside(xt,yt)==true)
	     {
		 val=val-0.12;
		 sense=true;
      	     }
	 }
	}
     }
     // test autres robots :

    if (sense==false)
     for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
        {
         r=(RobotAppPhy)e.nextElement();
	 if ((rob!=r) && (r.C.isInter(cBig)==true))
	 {

	    if (r.C.isInside(x0+Math.round(vx*d),y0-Math.round(vy*d))==true)
	     {
		 val=val-0.04;
		 sense=true;
      	     }

	 }
	}

     if (sense==false) val=val+0.02;

     } // iteration du for

     return (float)val;
    }

public Vecteur DDepotZ(RobotAppPhy rob)
    {
     Vecteur r=new Vecteur(0,0);
     int x0=rob.C.xc;
     int y0=rob.C.yc;
     r.fix(Base.x+4-x0,(Base.y+4-y0));
     return r;
    }

 public Vecteur radarV(RobotAppPhy rob,float dir,float ouv,float r0,float rm,float density,byte except,byte agt,int toDetect)
   {
	// rm = rayon de vision
	// init avant la mise a jour
	capteurs.init();
	
     double alpha=0;
     double dalpha=0;
     //@SuppressWarnings("unused")
     float d=r0,dmin=rm; //051018 ,l
     //float rr=rm/3;
     float vx,vy;
     int k=0;
     //byte sign=(byte)1;
     //byte val;
     Vecteur v= new Vecteur(0,0);
     int x0=rob.C.xc;
     int y0=rob.C.yc;
     int xt,yt;

     CirAgent cBig = new CirAgent(x0,y0,(int)rm,null);
     RobotAppPhy r;

     v.s=1;

     if (((toDetect & TYPE_OBSTACLE)!=0)
        &&((toDetect & DONT_CARE_OBSTACLE)==0))
     // test murs exterieurs
     {    d=r0;
          while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    xt=x0+Math.round(vx*d);
	    yt=y0-Math.round(vy*d);
	    if ((xt<0) || (yt<0) || (xt>dimx) || (yt>dimy))
	     {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
	       capteurs=majCaps(new VecteurR((float)vx*(rm-d),(float)vy*(rm-d) ),capteurs, d, dir);
      	     }
	   }
	  d=d+density;
	  }

     // test murs interieurs:
	 if ((toDetect & DONT_CARE_OBSTACLE)==0)
       for(int i=1;i<nbR;i++)
        {
	 if (cBig.isInter(TabR[i])==true)
	 {
	  d=r0;
          while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    xt=x0+Math.round(vx*d);
	    yt=y0-Math.round(vy*d);
	    if (TabR[i].isInside(xt,yt)==true)
	     {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
	       capteurs=majCaps(new VecteurR((float)vx*(rm-d),(float)vy*(rm-d) ),capteurs, d, dir);
      	     }
	   }
	  d=d+density;
	  }
	 }
	}

} // Fin de tous les murs

    //if ((toDetect & TYPE_PLAQUE)!=0)
     // test plaques:
     int signe_temp=((toDetect & TYPE_PLAQUE)!=0)?1:-1;
     if ((toDetect & DONT_CARE_PLAQUE)!=0) signe_temp=1;
     // signe: +1 = Eviter / -1 = Approcher / 0 = pas faire attention
     for(int i=0;i<LesPlaques.GetSize();i++)
        {
          if (!(((toDetect & DONT_CARE_PLAQUE)!=0)&& (LesPlaques.GetPlaque(i).HasRobot(rob.N_agent))))
          {
             if (cBig.isInter(LesPlaques.GetRect(i)) ==true)
             {
              d=r0;
              while(d<=rm)
              {
              dalpha=2*ouv/d;
              for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
               {
                vx=(float)Math.cos(alpha);
                vy=(float)Math.sin(alpha);
                xt=(x0+Math.round(vx*d));
                yt=(y0-Math.round(vy*d));
                if ((LesPlaques.GetRect(i)).isInside(xt,yt)==true)
                 {
                   if (d<dmin) dmin=d;
                   v.addV(signe_temp*Math.round(vx*(rm-d)),signe_temp*Math.round(vy*(rm-d))); k++;
                   capteurs=majCaps(new VecteurR((float)vx*(rm-d),(float)vy*(rm-d) ),capteurs, d,dir);
                 }
               }
              d=d+density;
              }
             }
            }
	}

     // test autres robots :
	 if ((toDetect & TYPE_ROBOT) !=0)
       if (agt==1)
     {
      Plaque maPlq=null;
      if (rob.maPlaque!=-1) maPlq=LesPlaques.GetPlaque(LesPlaques.IndicePlaque(rob.maPlaque));
     for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
        {
         r=(RobotAppPhy)e.nextElement();
	 if ((rob!=r) && (r.C.isInter(cBig)==true) )
          if (!(
                ((toDetect & DONT_CARE_FRIENDS)!=0)
             && (rob.maPlaque!=-1)
             && (  (maPlq.HasRobot(r.N_agent)))
             ))
             {
	  d=r0;
	  while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    if (r.C.isInside(x0+Math.round(vx*d),y0-Math.round(vy*d))==true)
	     {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
	       capteurs=majCaps(new VecteurR((float)vx*(rm-d),(float)vy*(rm-d) ),capteurs, d,dir);
	       if ((alpha>=dir-dalpha) && (alpha<=dir+dalpha)) v.s=-1;
      	     }
	   }
	  d=d+density;
	  }
	 }
	}
     }

     v.fixN((float)dmin);
     //v.s=-1;
     return v;
   }
 /** Pour le calcul intermédiaire de capteursIR */
 public CapIR majCaps(VecteurR vobs,CapIR capteursT, float rm, double dir)
 {		// dir = direction dans laquelle va/regarde le robot
 		// rm = rayon de vision
 		// capteurT => Etat des 8 capteur IR
 		// vtemp
 		
		VecteurR vtemp=vobs; vtemp.var(dir);
		CapIR captmp=new CapIR(capteursT);
       
	       /*
	       // Mis en commentaire car .angleDegres semble buggé !!!
	       int capindice=0; //=(Math.round(vtemp.angleDegres())%(45));
	       double ecartangle=360/8;
	       double vatmp= (vtemp.angleDegres()/45);
 
	       if ((vatmp>=(-0.5*ecartangle)) && (vatmp<(0.5*ecartangle))) {capindice=0;}
	       if ((vatmp>=(0.5*ecartangle)) && (vatmp<(1.5*ecartangle))) {capindice=1;}
	       if ((vatmp>=(1.5*ecartangle)) && (vatmp<(2*ecartangle))) {capindice=2;}
	       if ((vatmp>=(2.5*ecartangle)) && (vatmp<(3.5*ecartangle))) {capindice=3;}
	       if ((vatmp>=(3.5*ecartangle)) && (vatmp<(4.5*ecartangle))) {capindice=4;}
	       if ((vatmp>=(4.5*ecartangle)) && (vatmp<(5.5*ecartangle))) {capindice=5;}
	       if ((vatmp>=(5.5*ecartangle)) && (vatmp<(6.5*ecartangle))) {capindice=6;}
	       if ((vatmp>=(6.5*ecartangle)) && (vatmp<(7.5*ecartangle))) {capindice=7;}
	       if ((vatmp>=(7.5*ecartangle)) && (vatmp<(8.5*ecartangle))) {capindice=0;}
	       */
	       
	       int capindiceb=0;
	       double vagtmp=vtemp.angle();
	       double ecartanglea=2*Math.PI/8;
	       
	       if ((vagtmp>=(-0.5*ecartanglea)) && (vagtmp<(0.5*ecartanglea))) {capindiceb=0;}
	       if ((vagtmp>=(0.5*ecartanglea)) && (vagtmp<(1.5*ecartanglea))) {capindiceb=1;}
	       if ((vagtmp>=(1.5*ecartanglea)) && (vagtmp<(2*ecartanglea))) {capindiceb=2;}
	       if ((vagtmp>=(2.5*ecartanglea)) && (vagtmp<(3.5*ecartanglea))) {capindiceb=3;}
	       if ((vagtmp>=(3.5*ecartanglea)) && (vagtmp<(4.5*ecartanglea))) {capindiceb=4;}
	       if ((vagtmp>=(4.5*ecartanglea)) && (vagtmp<(5.5*ecartanglea))) {capindiceb=5;}
	       if ((vagtmp>=(5.5*ecartanglea)) && (vagtmp<(6.5*ecartanglea))) {capindiceb=6;}
	       if ((vagtmp>=(6.5*ecartanglea)) && (vagtmp<(7.5*ecartanglea))) {capindiceb=7;}
	       if ((vagtmp>=(7.5*ecartanglea)) && (vagtmp<(8.5*ecartanglea))) {capindiceb=0;}       
	       if ((vagtmp<0)||(vagtmp>=8)) {System.out.println("Erreur !!!"+vagtmp);}

	       double distobjet=( ( Math.sqrt(Math.pow(vtemp.vx,2)+Math.pow(vtemp.vy,2)) )/rm);
	       
	       //capindice=captmp.Cgoal(vtemp.angle());
	       
	       captmp.C[capindiceb]=((float)(Math.max(capteursT.C[capindiceb],distobjet)));
	       
	       /*System.out.println("capteursIR = ("+vatmp+","+(float)Math.PI+") "+captmp.C[0]+","+captmp.C[1]+","+captmp.C[2]+","+captmp.C[3]
         				+","+captmp.C[4]+","+captmp.C[5]+","+captmp.C[6]+","+captmp.C[7]+",");*/
	       
	       return captmp;
	       
}
   
   /**
  * Renvoi les valeurs des 8 capteurs IR (distance aux objets) du dernier RobAppPhy sur lequel a été appelé radarV
  * <p>Chaque valeur comprise entre 0 (aucun objet) et 1 (touché)</p>
  * <p>Mis à jour lors de l'appel à radarV !</p>
  */   
 public CapIR getCapteursIR()
   { return capteurs;   	}
 /**
  * Détermine les valeurs des 8 capteurs IR (distance aux objets)
  * <p>Chaque valeur comprise entre 0 (aucun objet) et 1 (touché)</p>
  */   
 public CapIR capteursIR(RobotAppPhy rob,float dir,float ouv,float r0,float rm,float density,byte except,byte agt,int toDetect)
   { // REVIENDRE marche pas car fait des modifs en trop par rapport à radarV
	// rm = rayon de vision
     double alpha=0;
     double dalpha=0;
     float d=r0,dmin=rm; //,l
     //float rr=rm/3;
     float vx,vy;
     int k=0;
     //byte sign=(byte)1;
     //byte val;
     Vecteur v= new Vecteur(0,0);
     
     //VecteurR vtemp = new VecteurR(0,0); // Vecteur temporaire pour calcul
     //int capindice; // indice du capteur IR concerné, var temp
     //float distobjet;  // distance à l'objet, var temp
     
     int x0=rob.C.xc;
     int y0=rob.C.yc;
     int xt,yt;
	
     CirAgent cBig = new CirAgent(x0,y0,(int)rm,null);
     RobotAppPhy r;

     v.s=1;

     if (((toDetect & TYPE_OBSTACLE)!=0)
        &&((toDetect & DONT_CARE_OBSTACLE)==0))
     // test murs exterieurs
     {    d=r0;
          while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    xt=x0+Math.round(vx*d);
	    yt=y0-Math.round(vy*d);
	    if ((xt<0) || (yt<0) || (xt>dimx) || (yt>dimy))
	     {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
	       //majCaps(new VecteurR(Math.round(vx*(rm-d)),Math.round(vy*(rm-d)) ),capteurs, d,dir);
	       capteurs=majCaps(new VecteurR(vx*(rm-d),vy*(rm-d) ),capteurs, d,dir);
	       
	       
      	     }
	   }
	  d=d+density;
	  }

     // test murs interieurs:
	 if ((toDetect & DONT_CARE_OBSTACLE)==0)
       for(int i=1;i<nbR;i++)
        {
	 if (cBig.isInter(TabR[i])==true)
	 {
	  d=r0;
          while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    xt=x0+Math.round(vx*d);
	    yt=y0-Math.round(vy*d);
	    if (TabR[i].isInside(xt,yt)==true)
	     {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
	       capteurs=majCaps(new VecteurR(vx*(rm-d),vy*(rm-d) ),capteurs, d, dir);
      	     }
	   }
	  d=d+density;
	  }
	 }
	}

} // Fin de tous les murs

    //if ((toDetect & TYPE_PLAQUE)!=0)
     // test plaques:
     int signe_temp=((toDetect & TYPE_PLAQUE)!=0)?1:-1;
     if ((toDetect & DONT_CARE_PLAQUE)!=0) signe_temp=1;
     // signe: +1 = Eviter / -1 = Approcher / 0 = pas faire attention
     for(int i=0;i<LesPlaques.GetSize();i++)
        {
          if (!(((toDetect & DONT_CARE_PLAQUE)!=0)&& (LesPlaques.GetPlaque(i).HasRobot(rob.N_agent))))
          {
             if (cBig.isInter(LesPlaques.GetRect(i)) ==true)
             {
              d=r0;
              while(d<=rm)
              {
              dalpha=2*ouv/d;
              for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
               {
                vx=(float)Math.cos(alpha);
                vy=(float)Math.sin(alpha);
                xt=(x0+Math.round(vx*d));
                yt=(y0-Math.round(vy*d));
                if ((LesPlaques.GetRect(i)).isInside(xt,yt)==true)
                 {
                   if (d<dmin) dmin=d;
                   v.addV(signe_temp*Math.round(vx*(rm-d)),signe_temp*Math.round(vy*(rm-d))); k++;
                   capteurs=majCaps(new VecteurR(vx*(rm-d),vy*(rm-d) ),capteurs, d, dir);
                 }
               }
              d=d+density;
              }
             }
            }
	}

     // test autres robots :
	 if ((toDetect & TYPE_ROBOT) !=0)
       if (agt==1)
     {
      Plaque maPlq=null;
      if (rob.maPlaque!=-1) maPlq=LesPlaques.GetPlaque(LesPlaques.IndicePlaque(rob.maPlaque));
     for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
        {
         r=(RobotAppPhy)e.nextElement();
	 if ((rob!=r) && (r.C.isInter(cBig)==true) )
          if (!(
                ((toDetect & DONT_CARE_FRIENDS)!=0)
             && (rob.maPlaque!=-1)
             && (  (maPlq.HasRobot(r.N_agent)))
             ))
             {
	  d=r0;
	  while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    if (r.C.isInside(x0+Math.round(vx*d),y0-Math.round(vy*d))==true)
	     {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
	       if ((alpha>=dir-dalpha) && (alpha<=dir+dalpha)) v.s=-1;
	       capteurs=majCaps(new VecteurR(vx*(rm-d),vy*(rm-d) ),capteurs, d, dir);
      	     }
	   }
	  d=d+density;
	  }
	 }
	}
     }

     v.fixN((float)dmin);
     //v.s=-1;
     //return v;
     return capteurs;
   }

 public boolean PercoitPlaque(RobotAppPhy rob,float dir,float ouv,float r0,float rm,float density)
   {
     double alpha=0;
     double dalpha=0;
     float d=r0; //,l; //,dmin=rm;
     //float rr=rm/3;
     float vx,vy;
     //int k=0;
     //byte sign=(byte)1;
     //byte val;
     int x0=rob.C.xc;
     int y0=rob.C.yc;
     int xt,yt;

     CirAgent cBig = new CirAgent(x0,y0,(int)rm,null);
     //RobotAppPhy r;

     // test plaques:
     // signe: +1 = Eviter / -1 = Approcher / 0 = pas faire attention
     for(int i=0;i<LesPlaques.GetSize();i++)
        { // Reviendre 010619 (ok?)
             if (cBig.isInter(LesPlaques.GetRect(i)) ==true)
             {
              d=r0;
              while(d<=rm)
              {
              dalpha=2*ouv/d;
              for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
               {
                vx=(float)Math.cos(alpha);
                vy=(float)Math.sin(alpha);
                xt=(x0+Math.round(vx*d));
                yt=(y0-Math.round(vy*d));
                if ((LesPlaques.GetRect(i)).isInside(xt,yt)==true)
                 {
                   return true;
                 }
               }
              d=d+density;
              }
             }
	}
     return false;
   }

public Vecteur radarAgt(RobotAppPhy rob,float dir,float ouv,float r0,float rm,float density,byte except,byte agt)
   {
     double alpha=0;
     double dalpha=0;
     float d=r0,/*l,*/dmin=rm;
     //float rr=rm/3;
     float vx,vy;
     int k=0;
     //byte sign=(byte)1;
     //byte val;
     Vecteur v= new Vecteur(0,0);
     int x0=rob.C.xc;
     int y0=rob.C.yc;
     //int xt,yt;

     CirAgent cBig = new CirAgent(x0,y0,(int)rm,null);
     RobotAppPhy r;

     v.s=1;

     // test autres robots :


     {
     for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
        {
         r=(RobotAppPhy)e.nextElement();
	 if ((rob!=r) && (r.C.isInter(cBig)==true))
	 {
	  d=r0;
	  while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    if (r.C.isInside(x0+Math.round(vx*d),y0-Math.round(vy*d))==true)
	     {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
	       if ((alpha>=dir-dalpha) && (alpha<=dir+dalpha)) v.s=-1;
      	     }
	   }
	  d=d+density;
	  }
	 }
	}
     }

     v.fixN((float)dmin);
     //v.s=-1;
     return v;
   }

 public Vecteur radarP(RobotAppPhy rob,float dir,float ouv,float r0,float rm,float density,byte except,byte agt)
   {
     double alpha=0;
     double dalpha=0;
     float d=r0,/*l,*/dmin=rm;
     //float rr=rm/3;
     float vx,vy;
     int k=0;
     //byte sign=(byte)1;
     //byte val;
     Vecteur v= new Vecteur(0,0);
     int x0=rob.C.xc;
     int y0=rob.C.yc;
     int xt,yt;

     CirAgent cBig = new CirAgent(x0,y0,(int)rm,null);
     //RobotAppPhy r;

     v.s=1;

     // test plaques:

       for(int i=0;i<LesPlaques.GetSize();i++)
        {
	 if (cBig.isInter(LesPlaques.GetRect(i)) ==true)
	 {
	  d=r0;
          while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    xt=x0+Math.round(vx*d);
	    yt=y0-Math.round(vy*d);
	    if ((LesPlaques.GetRect(i)).isInside(xt,yt)==true)
	     {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
      	     }
	   }
	  d=d+density;
	  }
	 }
	}

     v.fixN((float)dmin);
     //v.s=-1;
     return v;
   }

 public Vecteur radarA(RobotAppPhy rob,float dir,float ouv,float r0,float rm,float density,byte except)
   {
     double alpha=0;
     double dalpha=0;
     float d=r0,/*l,*/dmin=rm;
     //float rr=rm/3;
     float vx,vy;
     int k=0;
     //byte sign=(byte)1;
     //byte val;
     Vecteur v= new Vecteur(0,0);
     int x0=rob.C.xc;
     int y0=rob.C.yc;
     //int xt,yt;

     CirAgent cBig = new CirAgent(x0,y0,(int)rm,null);
     RobotAppPhy r;

     v.s=1;


     // test autres robots :

     for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
        {
         r=(RobotAppPhy)e.nextElement();
	 if ((rob!=r) && (r.C.isInter(cBig)==true))
	 {
	  d=r0;
	  while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    if (r.C.isInside(x0+Math.round(vx*d),y0-Math.round(vy*d))==true)
	     {
	       if (d<dmin) dmin=d;
	       v.addV(Math.round(vx*(rm-d)),Math.round(vy*(rm-d))); k++;
	       if ((alpha>=dir-ouv/2) && (alpha<=dir+ouv/2)) v.s=-1;
      	     }
	   }
	  d=d+density;
	  }
	 }
	}


     v.fixN((float)dmin);
     //v.s=-1;
     return v;
   }

 // ************ version 8 capteurs ************************** //

 public Cap radarCap(RobotAppPhy rob,float dir,float ouv,float r0,float rm,float density,byte except,byte agt)
   {
     double alpha=0;
     double dalpha=0;
     float d=r0; //,l,dmin=rm;
     //float rr=rm/3;
     float vx,vy;
     //int k=0;
     //byte sign=(byte)1;
     //byte val;
     //Vecteur v= new Vecteur(0,0);
     int x0=rob.C.xc;
     int y0=rob.C.yc;
     int xt,yt;
     Cap c=new Cap();
     c.init();

     CirAgent cBig = new CirAgent(x0,y0,(int)rm,null);
     RobotAppPhy r;

     // test murs exterieurs
          d=r0;
          while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    xt=x0+Math.round(vx*d);
	    yt=y0-Math.round(vy*d);
	    if ((xt<0) || (yt<0) || (xt>dimx) || (yt>dimy))
	     {
		 c.valid(alpha-dir);
      	     }
	   }
           d=d+density;
	  }

     // test murs interieurs:

       for(int i=1;i<nbR;i++)
        {
	 if (cBig.isInter(TabR[i])==true)
	 {
	  d=r0;
          while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    xt=x0+Math.round(vx*d);
	    yt=y0-Math.round(vy*d);
	    if (TabR[i].isInside(xt,yt)==true)
	     {
	      c.valid(alpha-dir);
      	     }
	   }
	  d=d+density;
	  }
	 }
	}

     // test autres robots :

       if (agt==1)
     {
     for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
        {
         r=(RobotAppPhy)e.nextElement();
	 if ((rob!=r) && (r.C.isInter(cBig)==true))
	 {
	  d=r0;
	  while(d<=rm)
	  {
	  dalpha=2*ouv/d;
	  for(alpha=dir-ouv;alpha<(dir+ouv);alpha+=dalpha)
	   {
	    vx=(float)Math.cos(alpha);
	    vy=(float)Math.sin(alpha);
	    if (r.C.isInside(x0+Math.round(vx*d),y0-Math.round(vy*d))==true)
	     {
	       c.valid(alpha-dir);
      	     }
	   }
	  d=d+density;
	  }
	 }
	}
     }

     return c;
   }


protected void createAgent(RobotAppPhy agt,int nr,int xp, int yp,int NbDecoup)
    {

	boolean go;
	//int TypeRob;
	agentsNb++;
	// calculer x,y

	if (agentsNb==1) // le premier agent cree
	    {
		x=xp;
		y=yp;
	    }

	if (agentsNb>Nrob) { x=-1;y=-1;}

	do
	{
	        x=x+R+1; // JC
	       if (x>(dimx-(2*R))) {x=R+xp; y=y+2*R+1;} //JC

	    	 //     y=y+2*R+1; // JC
	    	 //   if (y>(dimy-R)) {y=R+yp; x=x+2*R+2;}  // JC

	//    x=x+2*R+2;
	//    if (x>(dimx-2*R)) {x=x-4*R-4; y=y+2*R+2; }

	 // test de non collision avec un mur

	 CirAgent Cr=new CirAgent(x,y,R,Color.blue);

	 int i=1;
	 go=true;
	 while((i<nbR) && (go==true))
	     {
		 if (Cr.isInter(TabR[i])==true) go=false;
		 i++;
	     }

         // Ne sert à rien: p-e car Les Plaques pas encore initialisé ...
         i=0;
         while((i<LesPlaques.TabPlaques.size()) && (go==true))
	     {
		 if (Cr.isInter(LesPlaques.GetPlaque(i).GetRect())==true) go=false;
		 i++;
	     }

	}
	while (go==false);

         if (agentsNb<=NbDecoup)
            agt.initialisation(x,y,group,this,nr,(byte)agentsNb,agt.TYPE_DECOUPEUR);
         else
            agt.initialisation(x,y,group,this,nr,(byte)agentsNb,agt.TYPE_POUSSEUR);
	 R=agt.C.R;

	 x=x+R+1;
         // JC Avant la ligne de dessus était mise en commentaires ...
	     //if (x>(dimx-R)) {x=R+1; y=y+2*R+1;} // virer le 3*


	// JC System.out.println("x"+x+" y"+y+" n"+agentsNb);


	launchAgent(agt,"Robot",false);
	//System.out.println("apres launchAgent");
    }

    public boolean endSeq(int it)
    {
	/*boolean fin=true; // JC
	//for(int i=0;i<nbS;i++)
	//    if (TabS[i].vol>0) fin=false;
        int somme=0;
        for(int i=0;i<nbS;i++)
	    somme=somme+TabS[i].oldvol;
	if (Base.vol<somme) fin=false;
	if (it>5000) fin=true;
	return(fin);*/

        if (LesPlaques.TabPlaques.size()==0)
            return true;
        //else 
        	return false;
    }

    public void resetAgent(int it)
    {
	int i; // COMEBACK
	boolean go;
	//Vector R=RobotExt.otherRobots;
	//********** enregistrer resultats **************

        //System.out.println("(RmcLauncherApp.simulationFile="+RmcLauncherApp.simNameCopy+")"); // 030715
        //fileEnv=RmcLauncherApp.simNameCopy;

        fileEnv = file_ref+".env";
        //System.out.println("resetAgent#01(fileEnv="+fileEnv+")");

	if (!RmcLauncherApp.is_applet)
	{
		pw.println(it);
		pw.flush();
	}

	//pw.close();
	/*enr++; // JC
	if (enr>500)
	    {
	     enr=0;
	     pw.close();

	     if (Nrob>3) Nrob-=100;
	     String N= String.valueOf(Nrob);
	     try {
		 FileOutputStream fos = new FileOutputStream(file_ref+N+".res");
		 pw = new PrintWriter((OutputStream)fos);
	     }
	     catch (IOException e) {System.out.println(e);}
	    }*/
	//********** effacer rob de la matrice *********
	RobotAppPhy r;
	//	   for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
        //   {
	//      r=(RobotAppPhy)e.nextElement();
	      //  if ((r.x!=-1) || (r.y!=-1)) mat.put_val(r.x,r.y,(byte)0);
	//    }
	//********* remettre les agents ****************
       int n=0; int xp,yp;
       xp=RmcLauncherApp.x_prem;yp=RmcLauncherApp.y_prem;
       for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
       {
          r=(RobotAppPhy)e.nextElement();

          n++;
          // calculer x,y

           if (n==1) // le premier agent cree
                {
                    x=xp;
                    y=yp;
                }

            if (agentsNb>Nrob) { x=-1;y=-1;}

            do
            {
                   x=x+R+1;
                   if (x>(dimx-(2*R))) {x=R+xp; y=y+2*R+1;}
             // test de non collision avec un mur

             CirAgent Cr=new CirAgent(x,y,R,Color.blue);

             i=1;
             go=true;
             while((i<nbR) && (go==true))
                 {
                     if (Cr.isInter(TabR[i])==true) go=false;
                     i++;
                 }

             // Ne sert à rien: p-e car Les Plaques pas encore initialisé ...
             i=0;
             while((i<LesPlaques.TabPlaques.size()) && (go==true))
                 {
                     if (Cr.isInter(LesPlaques.GetPlaque(i).GetRect())==true) go=false;
                     i++;
                 }

            }
            while (go==false);

             /*if (agentsNb<=RmcLauncherApp.NDecCopy)
                r.initialisation(x,y,group,this,RmcLauncherApp.NRobCopy,(byte)agentsNb,r.TYPE_DECOUPEUR);
             else
                r.initialisation(x,y,group,this,RmcLauncherApp.NRobCopy,(byte)agentsNb,r.TYPE_POUSSEUR);
              */
             R=r.C.R;
             r.init_coord(x,y);
             x=x+R+1;
             r.Sati=0; r.sp=0; r.si=0 ; r.alt=false; r.oldalt=false;
             r.n_Satp=1; r.initCom=true; r.emit=false; r.im=0;

       } // for enumeration % robots


       /*
       for(Enumeration e=RobotAppPhy.otherRobots.elements();e.hasMoreElements();)
           {
	      r=(RobotAppPhy)e.nextElement();

	      n++;
	// calculer x,y
	  int x,y;
	  if (n>Nrob) { x=-1;y=-1;}
	  else
	    //
	  {
          x=0;y=0;
          boolean FindPlace=false;
	  //	int l=Base.dimx;
	  R=r.C.R;
	  do {
	    x=R+1;
	    y=R+1;
	   r.init_coord(x,y);
	   //if ((InterRob(r)==0) && (InterObs(r)==0)) FindPlace=true;
	   if ((InterRob(r)==0) && (InterObs(r)==0) && (InterPlq(r)==0)) FindPlace=true;
	    else
	       {
		x=x+R+1;
		if (x>(dimx-R)) {x=R+1; y=y+2*R+1;}
	       }
	  }
	  while (FindPlace==false);

	 r.Sati=0; r.Satp=0; r.alt=false; r.oldalt=false;
	 r.xr=(float)(x+0.5); r.yr=(float)(y+0.5);
	 r.n_Satp=1; r.initCom=true; r.emit=false; r.im=0;
	  }


	}
	*/
	//********** reinit env ************************

	/*for(i=0;i<nbS;i++)
	    TabS[i].vol=TabS[i].oldvol;
	Base.vol=0;*/
        LesPlaques.ResetAll();
        System.out.println("resetAgent#02(fileEnv="+fileEnv+")");
        loadfond(fileEnv);
        //System.out.println(LesPlaques.GetSize());
	//System.out.println("apres launchAgent");
    }

    void addAgent(RobotAppPhy agt,int nr,int xp,int yp,int NbDecoup)
    {      createAgent(agt,nr,xp,yp,NbDecoup);	}

    public void activate()
    {
    	if (!isGroup(group)) { createGroup(true, group, null, null); }
        else { requestRole(group, "member", null); }
	//joinGroup(group);
	requestRole(group, "robot world",null);
	println("robot world agent activated");
    }

    public void  MajEnv()
    {
	// Traitement ENVIRONNEMENT : puits...
	// calcul deb reel des puits
	// maj cpt nb rob/puits a 0
	//System.out.println("in maj");
	for(int i= 0;i<nbS;i++)
	    {
	     if (TabS[i].vol<=0) TabS[i].debR=0;
	     else
	      {
	       if (TabS[i].nbr>0) TabS[i].debR=TabS[i].deb/TabS[i].nbr;
	       else TabS[i].debR=TabS[i].deb;
	      }
	     TabS[i].nbr=0;
	    }
	//System.out.println("out maj");
    }

   public void loadfond(String fileenv) throws ArrayIndexOutOfBoundsException 
    {
     //File f;
     //FileReader in = null;
     int size=0;
     char[] data = null;

	

//-----------------------------------------------------------------------------------------------------------
	String fichierenv=null;
	System.out.println("Fileenv="+fileenv);
      try
	  {
	  	String rsrc="/worlds/"+fileenv; //+".env";
          	InputStream defs=null;
           
	       defs = this.getClass().getResourceAsStream(rsrc);

	      BufferedReader dip = new BufferedReader (new InputStreamReader(defs));
	      String s=null;
	      
	      while((s=dip.readLine()) != null)
		  {
		  	if (fichierenv==null) {fichierenv=s+'\n';}
		  	else {fichierenv+=s+'\n';}
		      //System.out.println(s);
		      //libsource.append('\n');
		      //libsource.append(s);
		  }
	  }
      catch (Exception eofe)
	  {
	      System.err.println("Load:"+eofe.getMessage());
	      eofe.printStackTrace();
	  }
	
     if (fichierenv!=null)
	{
		data = fichierenv.toCharArray();
		size = fichierenv.length();
	}
     System.out.println("Lecture par nv fnct("+size+"):");
     for (int i=0;i<size;i++)
     { System.out.print(data[i]);}  
			

     /*
     data=null;size=0;
     // Ancien code (bien pour fichiers en local)
     try {
     	fileenv="impasselight.env";
        System.out.println("loadfond#1024(fileenv="+fileenv+")");
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

      System.out.println("Lecture par old fnct("+size+"):");
     for (int i=0;i<size;i++)
     { System.out.print(data[i]);} 
	*/

     // traitement du texte charge :
     int i=0,n=1,l=0,v;
     char c;
     char[] nbT= new char[12];
     boolean finNb=false;
     Rect RectTemp=new Rect();
     nbR=0; // <= on efface les anciennes donnees ...
     TabR = new Rect[100]; // a ameliorer plus tard en lisant d'abord la longueur du tab, et non 100 REVIENDRE!!!
     TabR[nbR] = new Rect();
     if (LesPlaques==null) LesPlaques = new Plaques();

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
                            if (nbR>=100) {
                            	System.out.println("##################################################");
                            	System.out.println("Dépassement dans RobotAppEnv.loadfond (nbelem>100)");
                            	throw new ArrayIndexOutOfBoundsException(); // REVIENDRE!!!
                            }
		            TabR[nbR]= new Rect();
                          }
                         else if (RectTemp.color==thing)
                            {
                              LesPlaques.AddPlaque(RectTemp);
                            }
		       }
		   finNb=false; l=0;
		   }
	       }
	  i++;
	 }
    }



    public void LoadShaft(String filename) // charger les puits et la base
    {
     //File f;
     //FileReader in = null;
     int size=0;
     char[] data = null;
     
	/*
	// Ancienne méthode, fichiers locaux
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
     */
//-----------------------------------------------------------------------------------------------------------
	String fichierenv=null;
	System.out.println("Filesha="+filename);
      try
	  {
	  	String rsrc="/worlds/"+filename; //+".env";
          	InputStream defs=null;
           
	       defs = this.getClass().getResourceAsStream(rsrc);

	      BufferedReader dip = new BufferedReader (new InputStreamReader(defs));
	      String s=null;
	      
	      while((s=dip.readLine()) != null)
		  {
		  	if (fichierenv==null) {fichierenv=s+'\n';}
		  	else {fichierenv+=s+'\n';}
		      //System.out.println(s);
		      //libsource.append('\n');
		      //libsource.append(s);
		  }
	  }
      catch (Exception eofe)
	  {
	      System.err.println("Load:"+eofe.getMessage());
	      eofe.printStackTrace();
	  }
	
     if (fichierenv!=null)
	{
		data = fichierenv.toCharArray();
		size = fichierenv.length();
	}
     System.out.println("Lecture par nv fnct("+size+"):");
     for (int i=0;i<size;i++)
     { System.out.print(data[i]);}  
          
     // traitement du texte charge :
     int i=0,n=1,l=0,v;
     char c='\0';
     char[] nbT= new char[12];
     boolean finNb=false;
     nbS=0;
     int j=0;
   // lecture longueur tableau ===========
     while((i<size) && (c!='A'))
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
		    if (finNb==true)
	  	    {
		     switch (n) {
       		       case 4: { j++; }
		      }
		     n++;
		     if (n==5) n=3;
		     finNb=false; l=0;
		    }
	            if ((c=='\n') && (j>0))
		      {
			  //SSystem.out.println("saut !");
			  nbS++; j=0; n=1;
		      }
		   }

	  i++;
	 }
     // init tab:
 	TabS = new Shaft[nbS];
	int nboucle=nbS;
        System.out.println("nbS="+nbS);
     // lecture effective des valeurs ===========
     int agt=0;
     i=0;n=1;l=0;
     nbT= new char[12];
     finNb=false;
     nbS=0;j=0;
     c='\0';
     TabS[nbS] = new Shaft(0,0,Lbox,Lbox,0,0,box);
     Base = new Shaft(0,0,Lbox,Lbox,0,0,base);

     while((i<size))
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
		      case 1: { TabS[nbS].x=v; break;}
		      case 2: { TabS[nbS].y=v; break;}
                      case 3: { TabS[nbS].vol=v; TabS[nbS].oldvol=v; break;}
		      case 4: { TabS[nbS].deb=v; j++;  break;}
		     }
		     n++;
		     finNb=false; l=0;
		    }
	            if ((c=='\n') && (j>0))
		      {
			  System.out.println("nbS="+nbS);

			  j=0; n=1;
			  //	  mat.addRect(TabS[nbS].x,TabS[nbS].y,TabS[nbS].x+Lbox-1,TabS[nbS].y+Lbox-1,(byte)-(nbS+2));
			  nbS++;
			 if (nbS<nboucle)  TabS[nbS]=new Shaft(0,0,Lbox,Lbox,0,0,box);

		      }
		    if (c=='B')
			{
			 agt=1;
			 //	 nbase=1;
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
		      case 2: { Base.y=v;
		      N_puitsFirst=(byte)-(nbS+1);
		      // mat.addRect(Base.x,Base.y,Base.x+Lbox-1,Base.y+Lbox-1,(byte)-(nbS+2));
                      Taille_Zone_Depot=RmcLauncherApp.depotSize;
                      //Taille_Zone_Depot=RmcLauncherApp.getDepotSize();
                      Taille_Zone_Depot=RmcLauncherApp.depotSize;
                      Zone_Depot=new Rect(Base.x-(Taille_Zone_Depot/2), //+(Lbox/2),
                                        Base.y-(Taille_Zone_Depot/2), //+(Lbox/2),
                                        Base.x+(Taille_Zone_Depot/2), //+(Lbox/2),
                                        Base.y+(Taille_Zone_Depot/2), //+(Lbox/2),
                                        Color.red);

		      N_base=(byte)-(nbS+2);
		       break;}
		      case 3: // '\003'
                  RmcLauncherApp.depotSize = v;
                  Taille_Zone_Depot = v;
                  Zone_Depot = new Rect(Base.x - Taille_Zone_Depot / 2, Base.y - Taille_Zone_Depot / 2, Base.x + Taille_Zone_Depot / 2, Base.y + Taille_Zone_Depot / 2, Color.red);
                  break;
              
		      }
		     n++;

		    finNb=false; l=0;
		    }
		   }
		  }
		}
	  i++;
	 }
         // Ancienne fnct de lecture
         //try{in.close();}
         //catch (IOException e) {System.out.println(e);}
    }

    /* ********************************
       *** Gestion des Plaques      ***
       ******************************** */

    public int AccrocherPlaque(RobotAppPhy rob,Point p)
    {
	boolean go=true;
        int nPlaque=-1;
	int i=0;
        boolean ret_val=false;
        rob.JeMaintien=false;
        while((i<LesPlaques.GetSize()) && (go==true))
        {
	   if (LesPlaques.GetRect(i).isInside(p.x,p.y)==true)
            {
              nPlaque=LesPlaques.GetPlaque(i).GetId();
              if (rob.RobotType==rob.TYPE_POUSSEUR)
                ret_val=LesPlaques.GetPlaque(i).AddPousseur(rob.N_agent);
              else
                if (rob.RobotType==rob.TYPE_DECOUPEUR)
                  {
                    System.out.print("Essai d'accroche de "+rob.N_agent
                            +" sur plaque:"+i+"/"+LesPlaques.GetPlaque(i).GetId());
                    if (LesPlaques.GetPlaque(i).ForceRequise()<rob.PuissanceEnPoussee/2)
                    { ret_val=false; }
                    else
                    {
                      ret_val=LesPlaques.GetPlaque(i).AddDecoupeur(rob.N_agent,rob.C.xc,rob.C.yc);
                    }
                    if (ret_val) System.out.println(" >>> Succes !!! <<<");
                    else System.out.println(" Echec");
                  }

              go=false;
            }
	   i++;
        }

          // On renvoie -1 si pas de plaque ou si on a pas pu l'accrocher
          return ret_val?nPlaque:-1;
    }

    public int MaintenirPlaque(RobotAppPhy rob,int PlaqueId)
    {
        if (rob.JeMaintien==false)
        {
        rob.NbPix=-1;
        int iPlq=LesPlaques.IndicePlaque(PlaqueId);
        Plaque Plq=LesPlaques.GetPlaque(iPlq);
        if (rob.RobotType==rob.TYPE_DECOUPEUR)
            Plq.RemDecoupeur(rob.N_agent);
        if (rob.RobotType==rob.TYPE_POUSSEUR)
            Plq.RemPousseur(rob.N_agent);
        rob.JeMaintien=true;
        if (Plq.AddMainteneur(rob.N_agent)) return PlaqueId;
        //else {
        	rob.JeMaintien=false;return -1;
        	//}
        }
        //else
          return PlaqueId;
   }

    public boolean Exist(int PlaqueId)
    {
	return (LesPlaques.Exist(PlaqueId));
    }

    public int GetDistanceTo(int PlaqueId,int xfrom,int yfrom)
    {
      if (!LesPlaques.Exist(PlaqueId)) return -1;
      int ind=LesPlaques.IndicePlaque(PlaqueId);
      if (ind==-1) return -1;
      return LesPlaques.GetPlaque(ind).getDistanceTo(xfrom,yfrom);
    }

    public int GetForceRequise(int PlaqueId)
    { if (!LesPlaques.Exist(PlaqueId)) return -1;
      int ind=LesPlaques.IndicePlaque(PlaqueId);
      if (ind==-1) return -1;
      return LesPlaques.GetPlaque(ind).ForceRequise();
    }

    public boolean HasMoved(int PlaqueId)
    {
      return LesPlaques.GetPlaque(LesPlaques.IndicePlaque(PlaqueId)).HasMoved();
    }

    public int DecrocherPlaque(RobotAppPhy rob)
    {
	boolean go=true;
        //int nPlaque=-1;
	int i=0;
        while((i<LesPlaques.GetSize()) && (go==true))
        {
          if (LesPlaques.GetPlaque(i).HasRobot(rob.N_agent))
          {
            if (  (LesPlaques.GetPlaque(i).RemPousseur(rob.N_agent))
                  ||(LesPlaques.GetPlaque(i).RemMainteneur(rob.N_agent))
                  ||(LesPlaques.GetPlaque(i).RemDecoupeur(rob.N_agent))
             )
                go=true;
          }
          i++;
        }
        if (go==false) return i;
        /*else {*/ System.out.println("Bug decrochage (robot="+rob.N_agent+")");
              return -1;
              //}
    }


    public int DeplacerPlaque(int numPlaque,Point p)
    {
	//boolean go=true;
        LesPlaques.GetPlaque(LesPlaques.IndicePlaque(numPlaque)).Move(p.x,p.y);
        return 0;
    }

    public boolean AppliquerForce(int N_agent,int fx,int fy,int numPlaque)
    {
      Plaque Plq=LesPlaques.GetPlaque(LesPlaques.IndicePlaque(numPlaque));
      return Plq.AppliquerForce(N_agent,fx,fy);
    }

    /*
    public boolean SpecifieDecoupe(int numRob,int numPlaque,int direction,int morceau,int epaisseur)
    {
      Plaque Plq=LesPlaques.GetPlaque(LesPlaques.IndicePlaque(numPlaque));
      return Plq.SpecifieDecoupe(numRob,direction,morceau,epaisseur);
    }*/

    public boolean AppliquerDecoupe(int N_agent,int numPlaque)
    {
      Plaque Plq=LesPlaques.GetPlaque(LesPlaques.IndicePlaque(numPlaque));
      return Plq.AppliquerDecoupe(N_agent);
    }

    public void  verifPlaques()
    {
	// Traitement Plaques
        Plaque Plq;
        /*
        if (LesPlaques.TabPlaques.size()==0)
        {
          // Les robots ont terminé de bosser

          // Afficher message:
          System.out.println("Y a plus de plaques !!!!!!");
          stop=true;
        }*/

        // Deplacement
        for (int i=0;i<LesPlaques.TabPlaques.size();i++)
        { // Pour chaque plaque:
          Plq=((Plaque)LesPlaques.TabPlaques.elementAt(i));
          verifPlaque(Plq);
        }

        // Decoupe
        for (int i=0;i<LesPlaques.TabPlaques.size();i++)
        { // Pour chaque plaque:
          Plq=((Plaque)LesPlaques.TabPlaques.elementAt(i));
          if (Plq.Rob_Decoup!=-1)
          {
            //System.out.println("Plq.Rob_decoup"+(Plq.Rob_Decoup));
            int temp=Plq.ExecuterDecoupe();
            ((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(Plq.Rob_Decoup-1)).NbPix=temp;
            for (int j=0;j<RobotAppPhy.otherRobots.size();j++)
            {
              if (Plq.HasMainteneur(((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).N_agent))
                  ((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(j)).NbPix=temp;
            }
          }

          if (LesPlaques.Separer(Plq.GetId())!=-1)
            System.out.println("2DEnv Plaque séparée");
        }

        // Enlever les plaques qui sont dans la zone de Dépot
        LesPlaques.DegagementZoneDepot(Zone_Depot,Taille_Zone_Depot);
	//System.out.println("out Env.verifPlaques");
    }

    public void verifPlaque(Plaque Plq)
    {
      int iPlq; // Indice de la plaque
      double dx,dy; // Deplacement relatif x,y

      dx=dy=0;
      int iPb;
      boolean bonne_soluce,go;
      Point pTemp;

      double mvt[][]=new double[4][2]; // Tab des 4 mouvents possibles (glisser)

      int imvt;
      CheckAgisseurs(Plq);

      if (Plq.Rob_Pouss.size()>Plq.Rob_Maint.size())
          if (Plq.ForcesSuffisantes())
            {
              pTemp=Plq.SommeForces();
              VecteurR Vf=new VecteurR(pTemp.x,pTemp.y);
              Vf.fixN(1);
              dx=Vf.vx;
              dy=Vf.vy;
              if (dx>1) dx=1;
              if (dy>1) dy=1;
              if (dx<-1) dx=-1;
              if (dy<-1) dy=-1;
              // Pour une direction, 4 deplacements possibles:
              mvt[0][0]=dx;                   mvt[0][1]=dy; // On garde dx et dy
              if (dx>dy)
              {
                mvt[1][0]=dx*coef_rebond_plq; mvt[1][1]=0;  // On ne garde que dx
                mvt[2][0]=0;  mvt[2][1]=dy*coef_rebond_plq; // On ne garde que dy
              }
              else // (dx<=dy)
              {
                mvt[1][0]=0;  mvt[1][1]=dy*coef_rebond_plq; // On ne garde que dy
                mvt[2][0]=dx*coef_rebond_plq; mvt[2][1]=0;  // On ne garde que dx
              }
              mvt[3][0]=0;                    mvt[3][1]=0;  // On ne garde ni dx ni dy

              iPlq=LesPlaques.IndicePlaque(Plq.GetId());
              imvt=0;
              go=true;

              while ((imvt<4)&&(go))
              {
                  bonne_soluce=true;
                  //System.out.println("Try move "+mvt[imvt][0]+"/"+mvt[imvt][1]);
                  Plq.Move((float)(mvt[imvt][0]),(float)(mvt[imvt][1]));
                  // Test si plaque deplacée ok
                  // Intersection avec Les Plaques
                  if (bonne_soluce) // Test inutile, mais c pour la forme ...
                    {
                      iPb=PlqInterPlq(Plq,iPlq);
                      if (iPb!=-1)
                        { //System.out.println("Plq="+iPlq+"/"+Plq.GetId()+" bloque contre plq="+iPb+"/?");
                            if (imvt==3) System.out.println("Bug Robot2DEnv52.verifPlaque InterPlq");
                          bonne_soluce=false; }
                    }

                  // Intersection avec les obstacles
                  if (bonne_soluce)
                    {
                      iPb=PlqInterObs(Plq,iPlq);
                      if (iPb!=-1)
                        { //System.out.println("Plq="+iPlq+"/"+Plq.GetId()+" bloque contre obstacle="+iPb);
                            if (imvt==3) System.out.println("Bug Robot2DEnv52.verifPlaque InterObs");
                          bonne_soluce=false; }
                    }

                  // Intersection avec les murs extérieurs
                  if (bonne_soluce)
                    {
                      if (PlqInterMxt(Plq,iPlq))
                        { //System.out.println("Plq="+iPlq+"/"+Plq.GetId()+" bloque avec murs ext");
                            if (imvt==3) System.out.println("Bug Robot2DEnv52.verifPlaque InterMxt");
                          bonne_soluce=false; }
                    }

                  // Intersection avec les robots qui n'agissent pas sur la plaque
                  if (bonne_soluce)
                    {
                       // true : seulement ceux qui n'agissent pas
                       iPb=PlqInterRob(Plq,iPlq,true);
                       if (iPb!=-1)
                         { //System.out.println("Plq="+iPlq+"/"+Plq.GetId()+" bloque(1) contre robot="+iPb);
                            if (imvt==3) System.out.println("Bug Robot2DEnv52.verifPlaque InterRob");
                           bonne_soluce=false;
                         }
                    }

                  // Simulation du déplacement des robots
                  // qui agissent sur la plaque
                  // BugTag01 010618
                  SimuleRobotPlaque(Plq,(float)(mvt[imvt][0]),(float)(mvt[imvt][1]));
                  // Dernier test d'Intersection avec les robots (tous)
                  if (bonne_soluce)
                    {
                       iPb=PlqInterRob(Plq,iPlq,false); // False: tous les robots
                       if (iPb!=-1)
                         { //System.out.println("Plq="+iPlq+"/"+Plq.GetId()+" bloque(2) contre irobot="+iPb+"/"+((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(iPb)).N_agent);
                            if (imvt==3)
                            { System.out.println("Bug RobotAppEnv.verifPlaque InterRob2");
                            //((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(iPb)).frustration_cpt=
                            //  ((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(iPb)).Seuil_RasLeBol-1;
                              RobotSchedulerApp.stop_and_no_save=true;
                            }
                            //UndoSimuleRobotPlaque(Plq);
                            bonne_soluce=false;
                         }
                    }

                  if (bonne_soluce)
                    { go=false; }
                  else
                    { // Undo
                      //if (imvt==3) System.out.println("Bug Robot2DEnv52.verifPlaque (imvt==3 & no move)");
                       Plq.UndoMove();
                       UndoSimuleRobotPlaque(Plq);
                       //SimuleRobotPlaque(Plq,(float)(mvt[imvt][0]),(float)(mvt[imvt][1]));
                      imvt++; }
                } // while ((imvt<3)&&(go))
            } // if (Plq.ForcesSuffisantes())
    }

    public int PlqInterPlq(Plaque Plq,int iPlq)
    { // Renvoie le num de la premiere plaque en intersection avec Plq
      // -1 Si aucune plaque n'intersecte Plq
      int i; boolean go;
      Plaque PlqTemp;
      int nPlq;

              i=0; go=true; nPlq=-1;
              while ((i<LesPlaques.TabPlaques.size())&&go)
              {
                if (iPlq!=i)
                // On ne calcule pas l'inters. d'une plaque avec elle meme...
                {
                  PlqTemp=(Plaque)LesPlaques.TabPlaques.elementAt(i);
                  if (Plq.GetRect().isInter(PlqTemp.GetRect()))
                  {nPlq=i; i--; go=false;}
                }
                i++;
              }
      return nPlq;
    } // Fin PlqInterPlq

    public int PlqInterObs(Plaque Plq,int iPlq)
    { // Renvoie le num du premier obstacle en intersection avec Plq
      // -1 Si aucun obstacle n'intersecte Plq

              // Intersection avec les obstacles
              int nObs=-1;
              int i=1; boolean go=true;

              while((i<nbR)&&go)
              {   //if (iPlq==2) System.out.print(i+"("+Plq.GetRect().isInter(TabR[i])+")");
                  if (Plq.GetRect().isInter(TabR[i]))
                  { nObs=i; i--; go=false; }
                  i++;
              }
              //if (!go) System.out.println("Le mur "+i+" bloque");
              //else System.out.println();
              return nObs;
    } // Fin PlqInterObs

    public boolean PlqInterMxt(Plaque Plq, int iPlq)
    { // Renvoie vrai si il y a intersection entre la plaque Plq
      // et les Murs eXTérieurs.

      // Intersection avec les murs extérieurs
      if ( (Plq.GetRect().x1<0)||(Plq.GetRect().y1<0)
      ||(Plq.GetRect().x2>this.dimx)||(Plq.GetRect().y2>this.dimy))
        {return true;}
      //else 
    	  return false;
    } // Fin PlqInterMxt

    public int PlqInterRob(Plaque Plq,int iPlq)
    { return PlqInterRob(Plq,iPlq,false); }

    public int PlqInterRob(Plaque Plq,int iPlq,boolean ExceptMyRobots)
    { // Renvoie le num du premier robot en intersection avec Plq
      // -1 Si aucun robot n'intersecte Plq
      int i; boolean go;
      int nRob=-1;
              // Intersection avec les robots
              i=0;go=true;
              while((i<RobotAppPhy.otherRobots.size())&&go)
              {
                { /*
                  System.out.print(((RobotAppPhy)(RobotAppPhy.otherRobots.elementAt(i))).N_agent);
                  if (Plq.HasRobot(((RobotAppPhy)(RobotAppPhy.otherRobots.elementAt(i))).N_agent))
                    System.out.println(" Est sur la plaque ");
                  else
                    System.out.println(" Est pas Sur la plaque ");*/
                  //if (!ExceptMyRobots)

                  //if ( (!(ExceptMyRobots))||
                  //     (ExceptMyRobots && (!(Plq.HasRobot(((RobotAppPhy)(RobotAppPhy.otherRobots.elementAt(i))).N_agent))))
                  //   )
                  if (!( (ExceptMyRobots) && (Plq.HasRobot(((RobotAppPhy)(RobotAppPhy.otherRobots.elementAt(i))).N_agent))
                     ))
                     {  // 010622
                        //if (((RobotAppPhy)(RobotAppPhy.otherRobots.elementAt(i))).C.isInter(Plq.GetRect()))
                        if (isInterR( (RobotAppPhy)RobotAppPhy.otherRobots.elementAt(i),Plq))
                        {
                          /*RobotAppPhy r=(RobotAppPhy)RobotAppPhy.otherRobots.elementAt(i);
                          System.out.print("Intersection avec Rob n"+r.N_agent+" (");
                          System.out.print(r.xr+","+r.yr+":"+r.C.xc+","+r.C.yc+","+r.C.R+")");
                          System.out.println(" "+Plq.x1+","+Plq.x2+","+Plq.y1+","+Plq.y2
                                          +","+Plq.dx+","+Plq.dy);*/
                          nRob=i; /*i--;*/ go=false;}

                     }
                }
                i++;
              }
        return nRob;
    } // Fin PlqInterRob

    public void SimuleRobotPlaque(Plaque Plq,float dx,float dy)
    { // Simule le déplacement des robots qui poussent une plaque

      int i;
      RobotAppPhy rtemp;
              i=0;
              // Pour tous les robots:
              float mvt[][]=new float[4][2];
              mvt[0][0]=dx;              mvt[0][1]=dy; // On garde dx et dy
              if (dx>dy)
              {
                mvt[1][0]=dx*coef_rebond_rob;              mvt[1][1]=0;  // On ne garde que dx
                mvt[2][0]=0;               mvt[2][1]=dy*coef_rebond_rob; // On ne garde que dy
              }
              else // (dx<=dy)
              {
                mvt[1][0]=0;               mvt[1][1]=dy*coef_rebond_rob; // On ne garde que dy
                mvt[2][0]=dx*coef_rebond_rob;              mvt[2][1]=0;  // On ne garde que dx
              }
              mvt[3][0]=0;               mvt[3][1]=0;  // On ne garde ni dx ni dy
              int imvt=0;
              boolean go;
              // Essayer le 1er deplacement:
              for (i=0;i<RobotAppPhy.otherRobots.size();i++)
              {   //System.out.print("Bug ? i="+i+" (dx,dy)=("+dx+","+dy+") Plq.GetId()="+Plq.GetId());
                  rtemp=(RobotAppPhy)(RobotAppPhy.otherRobots.elementAt(i));
                  // Pour tous les pousseurs:
                  if (
                      (Plq.HasPousseur(rtemp.N_agent))||
                      (Plq.HasDecoupeur(rtemp.N_agent))||
                      (Plq.HasMainteneur(rtemp.N_agent))
                      )
                    { // Deplacer le robot:
                      // System.out.println("Bug ? i="+i+" N_agent="+ rtemp.N_agent+" (dx,dy)=("+dx+","+dy+") Plq.GetId()="+Plq.GetId());
                      rtemp.SimulateMove(mvt[imvt][0],mvt[imvt][1]);
                      // 010616-3 rtemp.pouss_ok=true;
                      //System.out.println("<<<- (Pousseur)");
                    }
                  //else System.out.println("Pas pousseur");
              }

              // Verif que les robots qui ont bougé
              // ne gènent pas (intersections)
              for (i=0;i<RobotAppPhy.otherRobots.size();i++)
              {   rtemp=(RobotAppPhy)(RobotAppPhy.otherRobots.elementAt(i));
                  // Pour tous les participants:
                  if (
                      (Plq.HasPousseur(rtemp.N_agent))||
                      (Plq.HasDecoupeur(rtemp.N_agent))||
                      (Plq.HasMainteneur(rtemp.N_agent))
                      )
                    { // Tester:
                      // Pour tous les obstacles
                      imvt=1; // Le cas 0 a déjà été simulé.
                      go = true;
                      while ((imvt<4)&&(go))
                      {
                          if ((rtemp.test_moveD())&&(rtemp.test_moveP())) go=false;

                          if (rtemp.C.isInter(Plq.GetRect())==true) go=false;

                          // On continue TQ on a pas une bonne solution
                          if (go)
                            {
                              rtemp.UndoMove();
                              rtemp.dep_ok=false;
                              imvt++;
                              //System.out.println(">>"+rtemp.N_agent+" "+imvt+">>");
                              if (imvt>=3) {rtemp.SimulateMove(mvt[3][0],mvt[3][1]);
                                            rtemp.dep_ok=false;}
                              else {rtemp.SimulateMove(mvt[imvt][0],mvt[imvt][1]);
                                    }
                              //rtemp.DecrocherPlaque();
                              //rtemp.maPlaque=-1;
                            }
                            else
                            { if (imvt<3) rtemp.dep_ok=true;
                              else rtemp.dep_ok=false;
                            }

                      }
                    } // if (Plq.HasPousseur(rtemp.N_agent))
              } // for (i=0;i<RobotAppPhy.otherRobots.size();i++)
    //System.out.print(")");
    } // Fin SimuleRobotPlaque

    public void UndoSimuleRobotPlaque(Plaque Plq)
    { // Annule le déplacement des robots qui poussent une plaque
      RobotAppPhy rtemp;
              // Pour tous les robots:
              for (int i=0;i<RobotAppPhy.otherRobots.size();i++)
              {   rtemp=(RobotAppPhy)(RobotAppPhy.otherRobots.elementAt(i));
                  // Pour tous les pousseurs:
                  if ((Plq.HasPousseur(rtemp.N_agent))
                    ||(Plq.HasMainteneur(rtemp.N_agent))
                    ||(Plq.HasDecoupeur(rtemp.N_agent)))
                    { // Annuler le déplacement du robot:
                      rtemp.UndoMove();
                      //rtemp.pouss_ok=false;
                      rtemp.dep_ok=false;
                    }
              }
    } // Fin UndoSimuleRobotPlaque
    public boolean CheckAgisseurs(Plaque Plq)
    { // Verifie la cohérence des robots affiliés à une Plaque
      // Effectue une(des) corrections si nécessaire
      // Renvoie true si aucune correction, false sinon
      RobotAppPhy rtemp;
      boolean msgerr=false;
      for (int i=0;i<RobotAppPhy.otherRobots.size();i++)
      {
        rtemp=((RobotAppPhy)RobotAppPhy.otherRobots.elementAt(i));
        if (rtemp.maPlaque==-1)
          { rtemp.distAccroche=-1;
            // Le robot n'a pas de plaque
            // On verifie qu'aucune plaque ne pense le contraire:
            if (Plq.HasPousseur(rtemp.N_agent))
              {Plq.RemPousseur(rtemp.N_agent);msgerr=true;}
            if (Plq.HasMainteneur(rtemp.N_agent))
              {Plq.RemMainteneur(rtemp.N_agent);msgerr=true;}
            if (Plq.HasDecoupeur(rtemp.N_agent))
              {Plq.RemDecoupeur(rtemp.N_agent);msgerr=true;}
          }
        else // if (rtemp.maPlaque==-1)
        { // Ici: rtemp.maPlaque!=-1
          Plaque AutrePlq;
          AutrePlq=(LesPlaques.GetPlaque(LesPlaques.IndicePlaque(rtemp.maPlaque)));
          if (!AutrePlq.HasRobot(rtemp.N_agent))
          { msgerr=true;
            rtemp.maPlaque=-1;
            rtemp.distAccroche=-1;
          }
          else // if (!AutrePlq.HasRobot(rtemp.N_agent))
          { // Ici, on va vérifier que le robot est inscit dans le bon vecteur
            if (rtemp.Tcurrent==1) // Si il pousse
              // et qu'il n'est pas incrit pousseur
              if (!AutrePlq.HasPousseur(rtemp.N_agent))
              { if (AutrePlq.HasDecoupeur(rtemp.N_agent))
                  AutrePlq.RemDecoupeur(rtemp.N_agent);
                if (AutrePlq.HasMainteneur(rtemp.N_agent))
                  AutrePlq.RemMainteneur(rtemp.N_agent);
                AutrePlq.AddPousseur(rtemp.N_agent);
              }
            if (rtemp.Tcurrent==2) // Si il découpe,
              // et qu'il n'est pas incrit découpeur:
              if (!AutrePlq.HasDecoupeur(rtemp.N_agent))
              { if (AutrePlq.HasPousseur(rtemp.N_agent))
                  AutrePlq.RemPousseur(rtemp.N_agent);
                if (AutrePlq.HasMainteneur(rtemp.N_agent))
                  AutrePlq.RemMainteneur(rtemp.N_agent);
                AutrePlq.AddDecoupeur(rtemp.N_agent,rtemp.C.xc,rtemp.C.yc);
              }
            if (rtemp.Tcurrent==4) // Si il maintient,
              // et qu'il n'est pas incrit mainteneur:
              if (!AutrePlq.HasMainteneur(rtemp.N_agent))
              { if (AutrePlq.HasPousseur(rtemp.N_agent))
                  AutrePlq.RemPousseur(rtemp.N_agent);
                if (AutrePlq.HasDecoupeur(rtemp.N_agent))
                  AutrePlq.RemDecoupeur(rtemp.N_agent);
                AutrePlq.AddMainteneur(rtemp.N_agent);
              }
          }
        }
        /*
        if (Plq.Rob_Decoup==-1)
          if (Plq.HasMainteneur(rtemp.N_agent))
            rtemp.JeMaintienPlus=true;*/

        if (msgerr)
        { System.out.println("Robot2DEnv52.CheckAgisseurs : Correction d'incohérence(s)");
          return false;
        }
        //else
          return true;
      }
      return false;
    }

  public boolean isInterR(RobotAppPhy rob,Plaque Plq)
    {
      // r = Plq.GetRect();
      float x1,x2,y1,y2,xc,yc;
      x1=Plq.x1+Plq.dx;      x2=Plq.x2+Plq.dx;
      y1=Plq.y1+Plq.dy;      y2=Plq.y2+Plq.dy;
      xc=rob.xr ; yc=rob.yr;
      int R=rob.C.R+1;

     boolean ans=false;
     //boolean go=true;
     float angle=0;

     if (isInsideR(x1,y1,x2,y2,xc,yc)==true) ans=true;
     float xt,yt;
     while ((!ans) && (angle<Math.PI*2 ))
     {  xt=xc+R*(float)Math.cos(angle);
        yt=yc+R*(float)Math.sin(angle);
        if (isInsideR(x1,y1,x2,y2,xt,yt))
          ans=true;
        angle+=(Math.PI*2)/100;
     }

     /*
     else
      {
       if ((x1<=xc+R) && (x1>=xc-R))
	if ((y1<=yc) && (y2>=yc)) ans=true;
         else if ((isInsideR(rob,x1,y1)==true) || (isInsideR(rob,x1,y2))) ans=true;
       if ((x2<=xc+R) && (x2>=xc-R))
	if ((y1<=yc) && (y2>=yc)) ans=true;
         else if ((isInsideR(rob,x2,y1)==true) || (isInsideR(rob,x2,y2))) ans=true;
       if ((y1<=yc+R) && (y1>=yc-R))
	if ((x1<=xc) && (x2>=xc)) ans=true;
         else if ((isInsideR(rob,x1,y1)==true) || (isInsideR(rob,x2,y1))) ans=true;
       if ((y2<=yc+R) && (y2>=yc-R))
	if ((x1<=xc) && (x2>=xc)) ans=true;
         else if ((isInsideR(rob,x1,y2)==true) || (isInsideR(rob,x2,y2))) ans=true;
      }*/
     return ans;
    }

    public boolean isInsideR(float x1,float y1, float x2, float y2, float x, float y)
    {
     return ((x >= x1) && (x <= x2) && (y >= y1) && (y <= y2));
    }

    public boolean isInsideR(RobotAppPhy rob,float x, float y)
    {
      float xc,yc;
      xc=rob.xr; yc=rob.yr;
      int R=rob.C.R;
     return (Math.sqrt((x-xc)*(x-xc)+(y-yc)*(y-yc)) <= R);
    }
    /*
    public boolean isInsideR(Plaque Plq,float x,float y)
    {
     return ((x >= (Plq.x1+Plq.dx)) && (x <= (Plq.x2+Plq.dx))
             && (y >= (Plq.y1+Plq.dy)) && (y <= (Plq.y2+Plq.dy)));
    }
    */
    public void end()
    {
    	 //leaveRole(simulationGroup,"neuron");
    	 agentsNb=0;
	 leaveGroup(group);
    }

}

