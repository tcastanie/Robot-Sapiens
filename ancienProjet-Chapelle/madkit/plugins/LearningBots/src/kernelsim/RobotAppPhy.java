package kernelsim;

import madkit.kernel.*;
import java.awt.*;
//import java.lang.reflect.Method;
import java.util.*;
import smaapp.*;

 /** Defines the physical part of a robot. */
 public class RobotAppPhy extends AbstractAgent implements ReferenceableAgent
 {	public static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
     //public NeuronAgent na;
     //public int NeuronAgentNb=0;
     /** The simulation group this robot belongs to */
     protected String simulationGroup;
    // De RobotEnvironnement.java
    public  static int NONE  = 0;
    public  static int NORTH = 1;
    public  static int SOUTH = 2;
    public  static int EAST  = 3;
    public  static int WEST  = 4;
     public CirAgent C;
     public static Vector otherRobots;
     public static int  Ra=17; // Rayon du disque d'un robot par def. <==========
     public int Rv=30; // rayon vision obstacles
     public int Rc=70; // dist. max pour comm. centre a centre = Rv + Ra
     public int R0 = 10; // 040427

     public int Tcurrent=0; // tache courante du robot
    // Si debrayage == true, le cerveau ne peut plus manipuler le robot
     public boolean debrayage=false;
     public int Tpast=0; // Tache précedente (pour le debrayage)
     
     //public float ouv=(float)Math.PI/2; // angle d'ouverture de vision du robot
     public float ouv=(float)Math.PI; // angle d'ouverture de vision du robot 040428

     public byte N_agent=-1;
     public /*static*/ long mvt=0,fluid=0;

     public boolean touche=false;
     public boolean proche=false;

     //public boolean pouss_ok=false;

     public int maPlaque=-1;
     public int distAccroche=-1;
     public boolean JeMaintien=false;
     public boolean JeMaintienPlus=false;
     public int NbPix;
     // Distance à laquelle se trouvait la plaque
     // au moment de l'approche.
     public int varDecroche=16;
     // variation de distance qui provoque la décroche
     // exprimée en nb de pixels

     public float LgBras=(float)1.3;
     public int PuissanceEnPoussee=1024*2;


    
     public float MAct[][][]=new float[6][27][2];

     // Pour l'apprentissage
     public float Beta=(float)0.9;
     public boolean exp=false; // On tente une expérience ?
     public int exp_tps=0;     // Tps d'execution de l'expérience
     public int exp_min=15;    // Tps min pour expérimenter
     public int exp_max=50;   // Tps max pour expérimenter
     public boolean exp_fin=false; // Experience terminee
     public int exp_deb=0;
     public float exp_moy=0; // Moyenne d'une expérience
     public int exp_sit;    // Situation au debut de l'expérience

     public float coef_exp=(float)0.5; // coef d'exploration

     public boolean dep_ok=false; // etat dernier deplacement reussi
     public boolean pouss_ok=false; // Etat du dernier poussage reussi
     public int frustration_cpt=0;
     public int Seuil_RasLeBol=400;
     public int Oubli_RasLeBol=4;
     /** les niveaux de satisfaction */
     public float alph=0;
     public float Sat=0;

     public float Satp=0;
     public float Sati=0;
     public float SatIPhy=0;
     public float SatExte=0;
     public float SatFaim=0;
     
     public float Sate=0;
     public int sp=0, si=0, siphy=127, satext; // SatP et SatI SatIPhy SatExte et SatFaim dans [-127,+127]
     public int sfaim=32767; // SatFaim dans [-32767,+32767]
     // Les agents qui composent la couche émotionnelle du robot
     public NeuronAgent FaimAgt=null,IPhyAgt=null,SatPAgt=null,SatEAgt=null,DrvManAgt=null;
     // Les agents qui composent la couche physique du robot
     public NeuronAgent MotLAgt=null,MotRAgt=null,MotFact1Agt=null;
     public NeuronAgent CapIRAgt[]=null; //new NeuronAgent[8];
     public NeuronAgent BaseDirAgt=null,BaseDistAgt=null;
     
     public char Situation;
     public char I1,I2;

     public double INSAT = -0.1;
     public double ALPHA =  0.5;
     public float alpha; // altruisme entre 0 et 1 ?

     public float gama; // coef amplif Satp pour test chgt de tache
     public byte N_base;
     public byte N_puitsFirst;
     public byte N_puitsLast;

     public int xbase; // coordonnees de la base des robots
     public int ybase;

    public int TYPE_POUSSEUR=1;
    public int TYPE_DECOUPEUR=2;
    public int RobotType;

     public String txt;
     public boolean emit=false;
     public boolean alt=false,oldalt=false,initCom=false; // altruisme au pas precedent
     public int n_Satp=1;
     public int im=0;

     protected RobotAppEnv world;

     public Com[] com=null;  // tab des com avec agents

     /** coordonnees et vecteurs */
     public float xr,yr;
      // Pour Undo:
      public float sxr,syr; // Sauvegarde des dernières val. de xr et yr
      public int sxc,syc; // Sauvegarde des dernières val. de C.xc et C.yc
      public boolean UndoAllowed=false;
     public VecteurR Vtask= new VecteurR(0,0); // vect tache de dep courante
     public VecteurR Vtaskold= new VecteurR(0,0);
     //public VecteurR Vrep= new VecteurR(0,0);
     public VecteurR Vi= new VecteurR(0,0);
     public VecteurR Vsli= new VecteurR(0,0); // vecteur somme des sati de repulsion
     public VecteurR Vsa = new VecteurR(0,0);

     public boolean charge=false;
     public boolean rep;

     /** capteurs - emetteurs */
     public int A[]={-45,0,45,90,135,180,225,270,315,360};
     public int A12[]={0,45,90,135,180,225,270,315,360,405,450,495};

     public char C8;
     public char obs;
     public char Altruism=0;
     public Event[] Tevent;
     public char MaxSignal;
     public char NMaxEv;
     public char CAlt;
     public char TASK=1; // 1 pour aller vers E, -1 s'en eloigner

     int DRoueG, DRoueD, UNIT_ACC=10;

  public RobotAppPhy()
    {

    }

 public  void init_coord(int x, int y)
   {
       C.xc=x;
       C.yc=y;
       xr=(float)(x+0.5);
       yr=(float)(y+0.5);
   }

 void initialisation(int x,int y,String g,RobotAppEnv re,int nr,byte N_agent,int TypeRob)
	{
		//this.x=x; this.y=y;
	    C=new CirAgent(x,y,Ra,Color.blue);
	    this.N_agent=N_agent;
	    init_coord(x,y);
	    world = re;

	    simulationGroup = g;

	    N_base=world.N_base;
	    N_puitsFirst=world.N_puitsFirst;
	    N_puitsLast=world.N_puitsLast;
	    xbase=world.Base.x+(int)(world.Lbox/2);
	    ybase=world.Base.y+(int)(world.Lbox/2);

	    this.RobotType=TypeRob;

	    Tevent=new Event[nr];
	    com=new Com[nr];
	    for(int i=0;i<nr;i++)
	    {
	     com[i]=new Com();

	    }
	    //    System.out.println("nr="+nr+" com="+com.toString());
            System.out.print("Agent n"+this.N_agent+" / Type=");
            if (this.RobotType==TYPE_DECOUPEUR)
              System.out.print("TYPE_DECOUPEUR");
            else
              System.out.print("TYPE_POUSSEUR");
            System.out.println();

            for (int i=0;i<6;i++)
              for (int j=0;j<27;j++)
                {
                  if (  ((i==0) )
                      ||((i==3) && ((j-1)%3==0))
                      ||((i==5) && (j>2))
                      ||((i==1) && (RobotType==TYPE_POUSSEUR) && (((j-2)%3)==0) )
                      ||((i==2) && (RobotType==TYPE_DECOUPEUR) && (((j-2)%3)==0) )
                      ||((i==4) && (((j-2)%3)==0) )
                      )
                    {
                      if (i==5) MAct[i][j][0]=(float)0.6;
                        else if (i==0) MAct[i][j][0]=(float)0.1;
                            else MAct[i][j][0]=(float)0.5;
                      MAct[i][j][1]=1;
                    }
                  else
                  {
                    MAct[i][j][0]=0;
                    MAct[i][j][1]=0;
                  }

                }


	}

 final void die()
   {

   }

 public void end()
  {
	 leaveRole(simulationGroup,"Robot");
	 leaveGroup(simulationGroup);
	 System.out.println("RobAppPhy End !");
  }


///////////////////// Les primitives d'un robot   /////////////////////////////////

 public int  dimx()
     {
	 return world.dimx;
     }

 public int dimy()
     {
	 return world.dimy;
     }

 public boolean test_moveD()
     {
	 return world.test_moveD(this);
     }

public boolean test_moveP()
     {
	 return world.test_moveP(this);
     }


// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

public char prec(char i)
{
  return (char)(i-1);
}

public char suiv(char i)
{
  return (char)(i+1);
}

public char Mod8(char i)
{
  i=(char)(i % 8);
  if (i<0) i=(char)(8+i);
  return i;
}

public int Ang360(int a)
{
  if (a<0) a=a+360;
  if (a>360) a=a-360;
  return a;
}

public int Angle10(char i)
{
  return A[i+1];
}

public char App(int v,int vinf,int vsup)
{
    char r=0;
    if ((v>=vinf) && (v<=vsup)) r=1;
    return r;
}

public char CapteurA(int a)  // pour a entre 0 et 360
{
  char n= (char)Math.round(a/45);
  if (n==8) n=0;
  return n;
}

public void decode_trame(Event ev, int nEv) //////////////////////////////////////
{
  int i;
  char m1=3;  // = 00000011
  int v=0;

  for(i=0;i<8;i++)
    {

      if ((ev.tr[i][0].b8toc() & 15) == m1)  // selectionne les trames
	                                       // debutant par 1100 => val 00000011
	{
	  v=ev.tr[i][0].bitp(5)+2*ev.tr[i][0].bitp(6)+
	    4*ev.tr[i][0].bitp(7)+8*ev.tr[i][0].bitp(8);
	  v=v+16*ev.tr[i][1].bitp(1)+32*ev.tr[i][1].bitp(2)+
	    64*ev.tr[i][1].bitp(3)+128*ev.tr[i][1].bitp(4);
	  // test du bit de parite //
	  if ((v % 2) == ev.tr[i][1].bitp(5))
	   {
	     ev.val[i]=(char)(v-127);
	     if (ev.val[i]>MaxSignal) { MaxSignal=(char)(v-127); NMaxEv=(char)nEv; }
	   }
	   else
	    ev.val[i]=0;
	}
    }

  // if (App(ev.pos,talt-epsilon,talt+epsilon)==1) { EmitRep=1; Nalt=nEv;}  a MODIF %%%%%%%%%%%%%%%%%%%%

} ////////////////////////////////////////////////////////////////////////////////////


 // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
 // %% simuler fonctions appel cartes robot
 // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

public char adr(int n) ///  simulation de la lecture du char en provenance des capteurs IR
{
  return 0;
}


public void emit(char v) // emission de la valeur v sur les emetteurs IR
{
  ///// a completer !
}

public int RoueG_cpt()
{
  return 0;
}

public int RoueD_cpt()
{
  return 0;
}

public void RoueG_cpt0()
{
}

public void RoueD_cpt0()
{
}

public char Light(char n) ///// revoie l'intensite lumineuse percue sur le capteur numero n
{
  return 0;
}

public void SetVit() // mettre les valeurs VitG et VitD sur les roues
{

}

// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

public VecteurR Mesure_L()
{
  // evaluer la plus petite distance lumiere robot et recup Angle
  //int i,imax=-1; //,angle;
  // char Lmax=0,l[8];

  VecteurR v = new VecteurR(0,0);

  return v;
}

public void RecupDep()
{
  DRoueG=RoueG_cpt();
  DRoueD=RoueD_cpt();

  RoueG_cpt0();
  RoueD_cpt0();
}

public int Mesure_R()
{
  int r=1;
  if ((DRoueG==0) && (DRoueD==0)) r= -1;
  return r;
}

public char Urgence(int v, int a)
{
    return 0;
}

public char DangerV(int v, int a)
{
    return 0;
}

public float PropA(int a)
{
  if (a>180) a=a-360;
  if (a<-180) a=a+360;
  if (a>90) a=90;
  if (a<-90) a=-90;
  return (float)(UNIT_ACC*Math.abs(a)/90);
}


 // %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

public Vecteur radarV(float dir,float ouv,float r0,float rm,float density,byte except,byte agt,int toDetect)
{
     return world.radarV(this,dir,ouv,r0,rm,density,except,agt,toDetect);
}

   /**
  * Appelle RobAppEnv.getCapteursIR() =
  * Renvoi les valeurs des 8 capteurs IR (distance aux objets) du dernier RobAppPhy sur lequel a été appelé radarV
  * <p>Chaque valeur comprise entre 0 (aucun objet) et 1 (touché)</p>
  * <p>Mis à jour lors de l'appel à radarV !</p>
  */   
public CapIR getCapteursIR()
   { return world.getCapteursIR();   	}
 /**
  * Appelle RobotAppEnv.capteursIR =
  * Détermine les valeurs des 8 capteurs IR (distance aux objets)
  * <p>Chaque valeur comprise entre 0 (aucun objet) et 1 (touché)</p>
  */   
public CapIR capteursIR(float dir,float ouv,float r0,float rm,float density,byte except,byte agt,int toDetect)
{
     return world.capteursIR(this,dir,ouv,r0,rm,density,except,agt,toDetect);
}

 public float MesureEnv(float rm)
 {
     return world.MesureEnv(this,rm);
 }

// public Vecteur radarV(float dir,float ouv,float r0,float rm,float density,byte except,byte agt)
// {
//     return world.radarV(this,dir,ouv,r0,rm,density,except,agt);
// }
 public boolean PercoitPlaque(float dir,float ouv,float r0,float rm,float density)
 {
     return world.PercoitPlaque(this,dir,ouv,r0,rm,density);
 }
 public Vecteur radarP(float dir,float ouv,float r0,float rm,float density,byte except,byte agt)
 {
     return world.radarP(this,dir,ouv,r0,rm,density,except,agt);
 }
 public Vecteur radarAgt(float dir,float ouv,float r0,float rm,float density,byte except,byte agt)
 {
     return world.radarAgt(this,dir,ouv,r0,rm,density,except,agt);
 }

 public Vecteur radarA(float dir,float ouv,float r0,float rm,float density,byte except)
 {
     return world.radarA(this,dir,ouv,r0,rm,density,except);
 }

 public Cap radarCap(float dir,float ouv,float r0,float rm,float density,byte except,byte agt)
 {
     return world.radarCap(this,dir,ouv,r0,rm,density,except,agt);
 }

 public Vecteur Dlight()
 {
     return world.Dlight(this);
 }
 /** Returns world.DDepotZ(this); */
 public Vecteur DDepotZ()
 {
     return world.DDepotZ(this);
 }
 /*
 public void moveTo(float angle)
    {
	
 }*/

 /** Renvoie une direction aléatoire */
  public int randomDirection()
    {
      int dir = (int)(Math.random() * 4);

      switch(dir)
	{
	case 0:
	  return RobotAppEnv.NORTH;
	case 1:
	  return RobotAppEnv.EAST;
	case 2:
	  return RobotAppEnv.SOUTH;
	case 3:
	  return RobotAppEnv.WEST;
	default:
	  return RobotAppEnv.NONE;
	}
    }

     ////////// meth sur l'acces au puits

     public int ShaftDebR(int np)
     {
	 int d= world.TabS[np].debR;
	 // if (d>deb) d=deb;
	 return d;
     }

     public int ShaftDeb(int np)
     {
	 return world.TabS[np].deb;
     }

     public int ShaftVol(int np)
     {
	 return world.TabS[np].vol;
     }

     public void ShaftVoldec(int np,int v)
     {
	 world.TabS[np].vol-=v;
     }

     public void Shaftcpt(int np)
     {
	world.TabS[np].nbr++;
     }

     public void VolBaseAdd(int d)
     {
	 world.Base.vol+=d;
     }

     ////////// meth sur calcul alea de la  direction


 /** Renvoie une direction aléatoire entre -a et a en degres*/
  public float randomDirectionR(float a)
    {
     return((float)(((Math.random()*2*a)-a)*Math.PI/180));
    }

  public float randomDirectionR(float a1,float a2)
    {
     if (Math.random()<0.5)
    	 {return((float)((a1+Math.random()*(a2-a1))*Math.PI/180));}
      //else
     return((float)((-a1-Math.random()*(a2-a1))*Math.PI/180));
    }

  public int Dir4(float angle)
    {
     if ((angle<Math.PI/4) || (angle>1.75*Math.PI)) return(3);
     if (angle<0.75*Math.PI) return(1);
     if (angle<1.25*Math.PI) return(4);
     return(2);
     }

   public float normalise(float angle)
    {
     int cpt=0;
     while((angle<0) || (angle>2*Math.PI))
     {
      if (angle<0) angle+=2*Math.PI;
      if (angle>2*Math.PI) angle-=2*Math.PI;
      cpt++;
      if (cpt>2) System.out.println ("norm"+" "+angle);
     }
     return angle;
    }

/////////////////////////////////////////////////////////
  public void activate()
  {	  System.out.println(this.simulationGroup);
	  if (!isGroup(simulationGroup)) { createGroup(true, simulationGroup, null, null); }
          else { requestRole(simulationGroup, "member", null); }
	//joinGroup(simulationGroup);
	  requestRole(simulationGroup,"robot",null);
	  //System.out.println("group "+simulationGroup);
	  //System.out.println("robot activated");
            //040429
            
        
	//joinGroup(NeuronGroup);
            //createNeuronAgent();
            
            /*
            //NeuronAgent na;
            na = new NeuronAgent();
            na.initialisation("NeuronGroup",this);
            //na.activate();
            //na.launchAgent(na,"Robot",false);
            launchAgent(na,"NeuroneA",false);
             //NeuronAgent na;
            na = new NeuronAgent();
            na.initialisation("NeuronGroup",this);
            //na.activate();
            //na.launchAgent(na,"Robot",false);
            launchAgent(na,"NeuroneB",false); // 030903
            */
  }
   public int AccrocherPlaque(Point p)
     {
	 return world.AccrocherPlaque(this,p);
     }

   public int MaintenirPlaque(int PlaqueId)
     {
	 return world.MaintenirPlaque(this,PlaqueId);
     }

   public boolean Exist(int PlaqueId)
     {
	 return world.Exist(PlaqueId);
     }

   public int GetDistanceTo(int PlaqueId,int xfrom,int yfrom)
    {
      return world.GetDistanceTo(PlaqueId,xfrom,yfrom);
    }

   public int GetForceRequise(int PlaqueId)
    {
      return world.GetForceRequise(PlaqueId);
    }

   public boolean HasMoved(int PlaqueId)
     {
	 return world.HasMoved(PlaqueId);
     }
   public int DecrocherPlaque()
     {
	 return world.DecrocherPlaque(this);
     }
   public int DeplacerPlaque(int numPlaque,Point p)
     {
	 return world.DeplacerPlaque(numPlaque,p);
     }

   public boolean AppliquerForce(int fx, int fy,int numPlaque)
     {
	 if (world.AppliquerForce(this.N_agent,fx,fy,numPlaque))
         {return true;}
         /*else
         {*/
          System.out.println("Erreur d'appel AppliquerForce("+this.N_agent+","+fx+","+fy+","+numPlaque+")");
          return false;
         //}

     }
   /*
   public boolean SpecifieDecoupe(int numPlaque,int direction,int morceau,int epaisseur)
   {
     return world.SpecifieDecoupe(this.N_agent,numPlaque,direction,morceau,epaisseur);
   }*/

   public boolean AppliquerDecoupe(int numPlaque)
     {
	 if (world.AppliquerDecoupe(this.N_agent,numPlaque))
         {return true;}
         /*else
         {*/
          System.out.println("Erreur d'appel AppliquerDecoupe("+this.N_agent+","+numPlaque+")");
          return false;
         //}
     }

   public void SimulateMove(float dx, float dy)
   {
      //System.out.println(">"+N_agent+">");
      // Sauvegarde des dernières valeurs
      sxr=xr;      syr=yr;
      sxc=C.xc;    syc=C.yc;
      // Affectation de nouvelles valeurs
      xr=xr+dx;    yr=yr+dy;

      fluid++;
      im=0;
      // New 28-05-2001
      alph=(float)(Vtask.DanglePP(Vi));
      //if (Vtask.DanglePP(Vi)<=(Math.PI/4))
      //if (this.N_agent==1) System.out.println("dx,dy="+dx+","+dy);
      if ((dx==0)&&(dy==0))
      { dep_ok=false;}
      else {dep_ok=true; }

      C.xc=Math.round(xr);
      C.yc=Math.round(yr);
      UndoAllowed=true;
      //System.out.println("< SimMove on "+N_agent+"<");
   }

   public void UndoMove()
   {
      if (UndoAllowed)
      { // Récupération des anciennes valeurs
        xr=sxr;      yr=syr;
        C.xc=sxc;    C.yc=syc;
        im++;
        alph=(float)(Vtask.DanglePP(Vi));
        UndoAllowed=false;
        dep_ok=false;
      }
      else
      {
        System.out.println("Undo not allowed N_agent="+N_agent);
      }
   }

   /*public void ConfirmMove()
   {  // Bla bla ...
      sxr=xr;      syr=yr;
      sxc=C.xc;    syc=C.yc;
      UndoAllowed=false;
   }*/

  public RobotAppPhy GetRobotById(int id)
  {
    int i=0; boolean go=true;
    RobotAppPhy tmprob=null;
    while ((i<otherRobots.size()) && go)
    {
      tmprob=((RobotAppPhy)otherRobots.elementAt(i));
      if (tmprob.N_agent==id) go=false;
    }
    if (!go) return tmprob;
    /*else {*/  System.out.println("Bug RobotAppPhy.GetRobotById : mauvais id");
            return null;
         //}
  }

  public void AfficheMAct()
  {
        for (int k=0; k<3; k++)
        {
             for (int i=0;i<6;i++)
              for (int j=0;j<9;j++)
                {
                  if (j==0) System.out.print(i+" ");
                  System.out.print( ((double)((int)(MAct[i][j+(k*9)][0]*1000))) /1000);
                  System.out.print("/"+(int)MAct[i][j+(k*9)][1]+" ");
                  if (j==8) System.out.println();
                }
              System.out.println();
        }
  }

//  protected void addNeuron(NeuronAgent n)
//	{
	//playGround.addAgent(t,getNumberRob(),getxp(),getyp());
//          addNeuronAgent(n);
//	}
//    void addNeuronAgent(NeuronAgent n)
//    {      createNeuronAgent(n);	}

    
//////////////////////////////////////////////
}
