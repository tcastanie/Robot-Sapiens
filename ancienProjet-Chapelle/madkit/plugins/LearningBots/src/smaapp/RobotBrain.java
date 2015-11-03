//
// RobotBrain.java
//
// controlé par neurones  - v 1.0 - Jérôme Chapelle
//

package smaapp;

import java.awt.*;
import java.util.*;
import java.io.*;
import madkit.kernel.*;
//import madkit.simulation.activators.TurboMethodActivator;
import kernelsim.*;
/** The brain part of a robot: defines the behavior of it. 
* @author Jérôme Chapelle 2004
*/
 public class RobotBrain extends RobotAppPhy
 {
	 static final long serialVersionUID = RobotAppPhy.serialVersionUID; //42L;
     // Utiles ...
    int TYPE_OBSTACLE = 1;
    int TYPE_PLAQUE = 2;
    int TYPE_ROBOT = 4;
    int DONT_CARE_PLAQUE = 8;
    int DONT_CARE_OBSTACLE = 16;
    int DONT_CARE_FRIENDS = 32;
    int NB_TYPES =  4;

    int ToDetect=0;

    int TYPE_POUSSEUR=1;
    int TYPE_DECOUPEUR=2;

    public int VERTICAL=1;
    public int HORIZONTAL=2;
    public int HAUT=1;
    public int BAS=2;
    public int DROITE=3;
    public int GAUCHE=2;


     // Les neurones
     NeuronScheduler nsch= null;
     public String RobotBrainType="RC52";
     public String RobotBrainName="c3p0";
     //public NeuronAgent na;
     public int NeuronAgentNb=0;
     int NeuronNbMax=18;
     String NeuronGroup=null;
     StringMessage lastmessage=null;
     public boolean messageread=false;

     /** ETATS de l'agent **/
     //float alph=0;
     boolean cont=false;    // vrai pour persister dans l'emission de SatI
     boolean change=false;  // vrai quand changement de tache
     boolean fond=false;    // vrai quand touche fond du corridor (lumiere) pour changer de tache
     int N_voisin=0;        // nb. de voisins percus
     int N_voisinOld=0;     // a t-1
     Color color;

     int tmp_01,tmp_02;

     /** DEPLACEMENT ROBOT **/

     boolean obs=false;    // obstacle detecte a t -1
     boolean plaq=false;   // plaque détectée à t-1
     int s;                // var sens contournement des obstacles
     float c;              // coef amplif aleatoire dans direction quand satp diminue
     //Vecteur vl=new Vecteur(0,0); // pour vecteur agent-source_lumiere
     Vecteur vz=new Vecteur(0,0); // pour vecteur agent-Zone de dépot
     //double vl_old;        // distance a la lumiere a t-1
     double vz_old;        // distance a la base a t-1

     double Da=0;


     /** TACHES ROBOT **/

     int Ntaches=6;        // nb. de taches connus du robot
     tache t[]=new tache[Ntaches];   // liste des taches activables
     boolean inh[]=new boolean[Ntaches]; // liste des taches non-activables (inhibées)
     int tmax=0;
     float imax=0;

     boolean stat;         // type de tache : statique ou deplacement
     public boolean veg;   // true pour fn vegetative active
     byte test_agt=0;      // 1 les agents sont detecte comme obstacles par le radar.
     //boolean chercher=true;     // true= on cherche les plaques / false on evite
     boolean maintenir=false;
     //int maPlaque=-1;


     /** COMMUNICATION **/

     int Nacc=0;     // nb. d'accointances (com) de l'agent a t (VERIF si utilise a l'exterieur ???)
     int ns=-1;      // num robot avec le + fort sig. sat int.
     int oldns;      // au t-1
     float oldsign;  // signe du signal a t-1
     boolean egal=false;  // vrai quand signal exterieur aussi fort que le sien

     /** Variables Internes **/

     int prog=0;
     int x,y; // copie des coordonnees


     /** variables pour simulateur **/

     boolean coul=true; // pour dir. initiale alea.
     boolean som=true;  // vrai pour additionner tous les vecteurs repulsif

//**********
     int pers=0; // sert a RIEN ?
     int EMPATHIE=100; // RIEN ?
//**********

public String itos(int n)
{ return ((new Integer(n)).toString()); }
public String ftos(float n)
{ return ((new Float(n)).toString()); }
public String dtos(double n)
{ return ((new Float(n)).toString()); }

/** Constructs a RobotBrain. Initialize the array <i>taches t[Ntaches]</i> */
    public RobotBrain()
    {
	  super();
	  color=Color.yellow; // ?
          Ra=17; // ???

	  // = COMPORTEMENT ROBOT =
	  alpha=(float)0.5; // tres altruiste !
          gama=(float)0.8;  // coef 1/gama ampl. de Satp pour le test chgt de tache

	  for(int i=0;i<Ntaches;i++) t[i]=new tache();
	  if (coul)  Vtask.a=randomDirectionR(180); // init dir alea des robots
          pers=EMPATHIE+1;

          for (int i=0;i<Ntaches;i++)
             t[i].a=false;
          t[0].a=true;
  	  t[0].init(0.01); // Chercher
	  t[1].init(1);    // Pousser/Tirer
          t[2].init(1);    // Decouper
	  t[3].init(1);    // Approcher/Accrocher
          t[4].init(1);    // Maintenir
    }

    public void reInit()
     {
	 Tcurrent=0;

	 if (coul==true) Vtask.a=randomDirectionR(180);
     }

 public Vecteur DetectLight()
     {
      Vecteur v=new Vecteur(0,0);
      v=Dlight();
      return v;
     }
 /** Calls DDepotZ() */
 public Vecteur DetectDepotZ()
     {
      Vecteur v=new Vecteur(0,0);
      v=DDepotZ();
      return v;
     }
 /** Computes x to the power of two. */
 public double carre(double x)
 {
   return x*x;
 }
/** Simulates emission and reception of signals <br>(fr: Simulation des emissions et receptions des signaux)*/
 public void ComSat() // SIMULATION DES EMISSIONS ET RECEPTIONS DES SIGNAUX
     {
      // trouver les Sati percus dans Rc du robot:

      int i=0;
      Nacc=0;
      //boolean ext;

      for(Enumeration e=otherRobots.elements();e.hasMoreElements();)
       {
	RobotBrain other= (RobotBrain)e.nextElement();

	if ((other.x!=-1) || (other.y!=-1))
	{
 	 //ext=false;
	 if (other!=this)
	 {
	  // calcul distance
	  float dx=this.xr-other.xr;
	  float dy=this.yr-other.yr;

	  float dist2rob=(float)Math.round(Math.sqrt(dx*dx+dy*dy));
	  float OldDist=com[i].dist;
	  com[i].dist=dist2rob;

	  com[i].seg.fix(-dx,dy);

	  if ((dist2rob < other.Rc) && (other.emit==true) && (initCom==false)) // distance com. possible ?
	   {
	     Nacc++;
	     if (com[i].c==true)
	      {
		  com[i].Dsat=(float)other.Sati-com[i].sat;
		  com[i].known=true;
	      }
	       else com[i].known=false;

             if (other.si>=0) com[i].signSat=1; else com[i].signSat=-1;
	     com[i].sat=other.Sati;
	     com[i].si=other.si;
             com[i].c=true;

	     com[i].Ddist=dist2rob-OldDist;

	     com[i].alt.fix(dx,-dy);

             com[i].angle=normalise((float)(com[i].alt.a+Math.PI));
	   } else
	   {
	     com[i].c=false;
	     com[i].known=false;
	     com[i].signSat=1;
	     com[i].Dsat=0;
	   }
	}
	else {
	     com[i].c=false;
	     com[i].known=false;
 	     }
	i++;
	}
       }

     }

  public void SaveBrain(String name)
  {
  	//System.out.println("Agent"+N_agent+" save Brain ("+name+")...");
  	System.out.println("RobotBrain("+N_agent+": Going to save brain ("+name+")...");
  	
  	if (!RmcLauncherApp.is_applet)
      try{
      	
          FileWriter dip;
            dip=new FileWriter(name+".nsch.txt");
            /*
          	String rsrc=name+".nsch.txt";
          	OutputStream defs=null;
           
	       defs = this.getClass().getResourceAsStream(rsrc);
		System.out.println(defs+"-"+rsrc);
	      BufferedReader dip = new BufferedReader (new InputStreamReader(defs));
	    */
	      //String s=null;
          
	   dip.write(nsch.simgroup);dip.write("\n");
	   dip.write(nsch.neurongroup);dip.write("\n");
	   dip.write((new Integer(nsch.Phase)).toString());dip.write("\n");
		nsch.SaveBrain(dip);
          dip.close();
         }
      catch (Exception e)
      {	RmcLauncherApp.is_applet=true;
      	e.printStackTrace();
      }
  	
  }

  public void LoadBrain(String name)
  {
  	//System.out.println("Agent"+N_agent+" load Brain ("+name+")...");
  	System.out.println("RobotBrain("+N_agent+": Going to load brain ("+name+")...");
  	
  if (!RmcLauncherApp.is_applet)
  {
      
      try
	  {
	  	String rsrc=name+".nsch.txt";
          	InputStream defs=null;
           
	       defs = this.getClass().getResourceAsStream(rsrc);
		System.out.println(defs+"-"+rsrc);
		
	      BufferedReader dip = new BufferedReader (new FileReader(rsrc)); //new InputStreamReader(defs));
	      String s=null;
		nsch.killAgents();
		killAgent(nsch);
		initNsch(); // fait le new 
		
		
		s=dip.readLine();System.out.println(s);
		nsch.simgroup=s;
		s=dip.readLine();System.out.println(s);
		nsch.neurongroup=s;
		s=dip.readLine();System.out.println(s);
		nsch.Phase=Integer.valueOf(s).intValue();
	
		nsch.LoadBrain(dip);
	  }
      catch (Exception eofe)
	  {
	      System.err.println("Load:"+eofe.getMessage());
	      eofe.printStackTrace();
	  }
	  /*
	try{
          FileReader fr;
            fr=new FileReader(name+".nsch.txt");
	   fr.read(nsch.simgroup);
	   fr.read(nsch.neurongroup);
	   fr.read(nsch.Phase);
	nsch.LoadBrain(name);
          fr.close();
         }
      catch (Exception e)
      {	RmcLauncherApp.is_applet=true;
      	e.printStackTrace();
      }*/
      } // !(is applet)
  }
    


  public void initNsch()
  {
  	// Appellé par RobAppSma lors de la création de la simulation
  	int a=0;
  	System.out.print(++a);
  	NeuronGroup= "NeuronGroup"+N_agent;
  	System.out.print(++a);
  	
	System.out.println("new NeuronScheduler");
	nsch = new NeuronScheduler(simulationGroup,NeuronGroup);
	System.out.print(++a);
	
	System.out.println("launchAgent NeuronScheduler...");
	launchAgent(nsch,"NeuronScheduler",false);
	System.out.print(++a);
	int vtmp=0;
	for (int i=0;i<NeuronNbMax;i++)
	{	// Créaction et recensement:
		NeuronAgent na=createNeuronAgent();;
			
		//AgentAddress agtAddrTemp;
	    	if ((i>=0)&&(i<=9))
	    	{
	    		vtmp=nsch.registerSensor(na); // request role neurongroup,sensor
	    		System.out.println("(sensor) vtmp="+vtmp+" - i="+i);
		}
		
		if ((i>=10)&&(i<=11))
		{
			vtmp=nsch.registerActor(na); // request role neurongroup,sensor
			System.out.println("(actor) vtmp="+vtmp+" - i="+i);
			if (i==10)
			{
				na.setLabel("MotL");
				MotLAgt=na;
			}
			else
			{
				na.setLabel("MotR");
				MotRAgt=na;
			}
		}
		
		if ((i>=12)&&(i<=15))
		{
			vtmp=nsch.registerDrive(na);
			//nsch.registerDrive(na);
			if (i==12)
			{
				 na.setLabel("SatP");
				 SatPAgt=na;
			}
			if (i==13)
			{
				 na.setLabel("SatE");
				 SatEAgt=na;
			}
			if (i==14)
			{
				na.setLabel("IPhy");
				IPhyAgt=na;
			}
			if (i==15)
			{
				na.setLabel("Faim");
				FaimAgt=na;
			}
			System.out.println("(drive) vtmp="+vtmp+" - i="+i);
		}
		if (i==16)
		{
			vtmp=nsch.registerActor(na); // request role neurongroup,sensor
			System.out.println("(actor) vtmp="+vtmp+" - i="+i);
			na.setLabel("MotFact1");
			MotFact1Agt=na;
		}
		if (i==17)
		{
			vtmp=nsch.registerDriveManager(na);
			na.setLabel("DrvMan");
			DrvManAgt=na;
			System.out.println("(drivemanager) vtmp="+vtmp+" - i="+i);
		}
		if (i>NeuronNbMax) System.out.println("RobotBrain: Trop de neurones générés !!! ");
		
		// Mettre un label sur le neurone
		if ((i>=0)&&(i<8))
			{
				if (CapIRAgt==null)
					{CapIRAgt=new NeuronAgent[8];}
					na.setLabel("CapIR"+i);
					CapIRAgt[i]=na;
			}
			else if (i==8) { na.setLabel("BaseDir"+i); BaseDirAgt=na;}
				else if (i==9) { na.setLabel("BaseDist"+i); BaseDistAgt=na;}

	}
	
	System.out.print(++a);
			
	            
	if (!isGroup(NeuronGroup)) { createGroup(true, NeuronGroup, null, null); }
	else { requestRole(NeuronGroup, "member", null); }
	
	//sendMessage(nsch.getAddress(), new StringMessage("clockinitnsch"));
	
	//AgentAddress agtAddrTemp;
	
	sendMessage(MotLAgt.getAddress(),new NeuralMessage(0.5,"MotL"));
	sendMessage(MotRAgt.getAddress(),new NeuralMessage(0.5,"MotR"));
	sendMessage(MotFact1Agt.getAddress(),new NeuralMessage(0.5,"MotFact1"));
		
	sendMessage(SatPAgt.getAddress(),new NeuralMessage(0.5,"SatP"));
	sendMessage(SatEAgt.getAddress(),new NeuralMessage(0.5,"SatE"));
	sendMessage(IPhyAgt.getAddress(),new NeuralMessage(1,"IPhy"));
	sendMessage(FaimAgt.getAddress(),new NeuralMessage(1,"Faim"));
	sendMessage(DrvManAgt.getAddress(),new NeuralMessage(0.75,"DrvMan"));
	
	/*
	 	sendMessage((nsch.getDriveByName("SatP").getAddress()),new NeuralMessage(0.5,"SatP"));
	sendMessage((nsch.getDriveByName("SatE").getAddress()),new NeuralMessage(0.5,"SatE"));
	sendMessage((nsch.getDriveByName("IPhy").getAddress()),new NeuralMessage(1,"IPhy"));
	sendMessage((nsch.getDriveByName("Faim").getAddress()),new NeuralMessage(1,"Faim"));
	 */
	
	/*double vtemp=(nsch.getDriveByName("Faim").getLastValue());
	while (vtemp!=1)
	{
		
		//System.out.println("Ya un gros soucis Faim: "+nsch.getDriveByName("Faim").getLabel());
		agtAddrTemp = (nsch.getActorByName("MotL").getAddress());
		sendMessage(agtAddrTemp,new NeuralMessage(0.5));
		agtAddrTemp = (nsch.getActorByName("MotR").getAddress());
		sendMessage(agtAddrTemp,new NeuralMessage(0.5));
			
		sendMessage((nsch.getDriveByName("SatP").getAddress()),new NeuralMessage(0.5,"SatP"));
		sendMessage((nsch.getDriveByName("SatE").getAddress()),new NeuralMessage(0.5,"SatE"));
		sendMessage((nsch.getDriveByName("IPhy").getAddress()),new NeuralMessage(1,"IPhy"));
		sendMessage((nsch.getDriveByName("Faim").getAddress()),new NeuralMessage(1,"Faim"));
		
		//System.out.println("Ya un gros soucis Faim");
		vtemp=(nsch.getDriveByName("Faim").getLastValue());
	}*/
	
  }

	public void ChoixActionNonAltruisme()
	{
		    pers=EMPATHIE+1;
		
		    switch(Tcurrent)
		    {
		
		     case 0: { // calcul dir alea
		                  Vtask.fixN(Ra);
			          if (Math.random()>0.8)
		                  {
		                    Vtask.var(randomDirectionR(20));
		                    //double alp=5*Math.PI /4 ;//Math.atan((double)(-vz.vy)/(double)vz.vy);
		                    //alp=alp+Math.PI;
		                    //alp=alp-Vtask.a;
		                    //Vtask.var((float)(alp*0.05));
		                  }
			       txt="al";
			       test_agt=1;
			       stat=false;  n_Satp=3;
		               if (frustration_cpt<1) ToDetect = TYPE_OBSTACLE+TYPE_ROBOT;
		               else ToDetect=TYPE_OBSTACLE+TYPE_ROBOT+TYPE_PLAQUE;
		               } break;
		     case 1: { // pousser/tirer
			       stat=false;
			       //Vtask.fix((float)vl.vx,(float)vl.vy);;
			       //Vtask.fixN(Ra);
		               //Vtask.fixA();
		               Vtask.fix((float)xbase-x,y-(float)ybase);
		               Vtask.fixN(Ra);
		               ToDetect=TYPE_OBSTACLE+TYPE_ROBOT+DONT_CARE_PLAQUE+DONT_CARE_FRIENDS;
		               test_agt=0;
			       n_Satp=1;
		             } break;
		
		     case 2: { // Decouper ...
			       stat=true;
			       //Vtask.fix((float)-vl.vx,(float)-vl.vy);
		               //Vtask.fix((float)xbase-x,(float)ybase-y);
			       //Vtask.fixN(20);
		               //System.out.println("maPlaque="+maPlaque);
		               if ((AppliquerDecoupe(maPlaque))==false)
		               System.out.println("Moi="+N_agent+" Oups impuissant sur maPlaque="+maPlaque);
		
			       test_agt=0;
			       n_Satp=2;
		             } break;
		
		     case 3: { // Approcher
			       stat=false;
		                vz_old=vz.norme();
		                if (maPlaque==-1)
		                {
		                  //System.out.println("Jessaie d'accrocher ...");
		
		                  maPlaque=AccrocherPlaque(new Point(
		                   (int)Math.round(x+(C.R*LgBras)*Math.cos(Vtask.a)),
		                   (int)Math.round(y-(C.R*LgBras)*Math.sin(Vtask.a))
		                   ));
		                  if (maPlaque!=-1)
		                  {
		                    distAccroche=GetDistanceTo(maPlaque,C.xc,C.yc);
		                    System.out.println("Moi="+N_agent+ " accroche + "+maPlaque+" distance="+distAccroche);
		                  }
		                }
		               if (frustration_cpt<1) ToDetect = TYPE_OBSTACLE+TYPE_ROBOT;
		               else ToDetect=TYPE_OBSTACLE+TYPE_ROBOT+TYPE_PLAQUE;
			       test_agt=0;
			       n_Satp=3;
		             } break;
		     case 4: { // Maintenir
			       stat=true;
		               test_agt=0;
		               ToDetect = TYPE_OBSTACLE+TYPE_ROBOT;
			       n_Satp=2;
		             } break;
		
		     } // switch(Tcurrent)
	} // ChoixActionNonAltruisme()
	
  public String runMe()
    {
    	Message m = nextMessage();
    	StringMessage lastmessage=null;
	if (m!=null)
	{
		lastmessage=(StringMessage)m;
		messageread=false;
									
		if ((!messageread) &&(lastmessage.getString().compareTo("LoadBrain")==0))
		{	//System.out.println("RobotBrain: Going to load brain ...");
			LoadBrain(RobotBrainType+"."+RobotBrainName);
			messageread=true;
		}
		if ((!messageread) &&(lastmessage.getString().compareTo("SaveBrain")==0))
		{	//System.out.println("RobotBrain: Going to save brain ...");
			SaveBrain(RobotBrainType+"."+RobotBrainName);
			messageread=true;
		}
		if ((!messageread) &&(lastmessage.getString().compareTo("PauseBrain")==0))
		{	//System.out.println("RobotBrain: Going to save brain ...");
			sendMessage(nsch.getAddress(),new StringMessage("Pause"));
			messageread=true;
		}
		if ((!messageread) &&(lastmessage.getString().compareTo("PlayBrain")==0))
		{	//System.out.println("RobotBrain: Going to save brain ...");
			sendMessage(nsch.getAddress(),new StringMessage("Play"));
			messageread=true;
		}
		if ((!messageread) &&(lastmessage.getString().compareTo("UpdateDrives")==0))
		{	System.out.println("RobotBrain: Going to retrieve drive values ...");
			//sendMessage(nsch.getAddress(),new StringMessage("Play"));
			messageread=true;
		}
		
		
		if (!messageread)
		{
			System.out.println("RobotBrain: I don't understand message:"+ lastmessage.getString());
		}
		
	}
	
  	if (nsch!=null)
  	{
	  	// Ici je récupère la valeur de chaque satisfaction au cas ou l'utilisateur
	  	// les aurait modifié via l'interface
  		
  		if (FaimAgt.getLastDebugString()=="ManMod")
  		{	sfaim = (int)(((FaimAgt.getLastValue())*2-1)*32767); }
  		if (IPhyAgt.getLastDebugString()=="ManMod")
  		{  	siphy = (int)(((IPhyAgt.getLastValue())*2-1)*127);	}
  		if (SatPAgt.getLastDebugString()=="ManMod")
		{	sp = (int)(((SatPAgt.getLastValue())*2-1)*127); }
  		if (SatEAgt.getLastDebugString()=="ManMod")
  		{	si = (int)(((SatEAgt.getLastValue())*2-1)*127); }
  	}
  	double SavedAngle=Vtask.a;
	x=C.xc;
	y=C.yc;
        //AfficheMAct();
        for (int i=0;i<Ntaches;i++)
        { inh[i]=false; }

        /*
        if (RobotType==TYPE_DECOUPEUR)
        { inh[Ntaches-1]=true; }*/

        if (RobotType==TYPE_POUSSEUR)
        { inh[2]=true; }

	if ((x!=-1) || (y!=-1))
	{
		//
		// C'est ici qu'on trouve le cerveau et les reflexes de l'agent !
		//
		
		// Maj calcul fluidite
		if (N_agent==1) {mvt=0; fluid=0;}
		
		/** appel lecture des COM ! **/
		
		if  ((RmcLauncherApp.MS==true)) ComSat();
		
		if (initCom==true) { reInit(); initCom=false;  }
		
		
		/** Evaluation Satp **/
		
		prog=0;
		
		// Debuggage qui permet d'afficher un messg en cas de
		// Changement de Tache ou de modele de gratification
		/*if ((tmp_01!=n_Satp) || (tmp_02!=Tcurrent))
		{ tmp_01=n_Satp; tmp_02=Tcurrent;
		  if (N_agent==2)
		    System.out.println("Moi="+N_agent"+"n_Satp= "+n_Satp+" / Tcurrent="+Tcurrent);
		}*/
		
		vz_old=vz.norme();
		vz=DetectDepotZ(); // test detection Depot
		
		maPlaque=UpdateNumPlaque(maPlaque);
		if (maPlaque==-1)
		{	//t[1].a=false;t[2].a=false;
		        inh[1]=true;inh[2]=true;inh[4]=true;
		} // if (maPlaque==-1)
		
		// Inhibition du maintient:
		//if ((maPlaque==-1)||(Tcurrent==2)) inh[4]=true;
		
		pouss_ok=((maPlaque!=-1)&&(vz.norme()<vz_old));
		
		switch(n_Satp) {
			case 1 :  /*if (vz.norme()<(vz_old)) prog=1;
			         else  if (N_voisin==0) prog=-1; else prog=-2;
			        vz_old=vz.norme();*/
			        // Pousser/Tirer
			        // gratifie le déplacement vers la base
			        if (pouss_ok)
			          if (dep_ok) prog=2;
			          else prog=1;
			        else prog=-2;
			        sp+=prog;
				break;
			case 2 :  //prog=NbPix-1;
			        if (NbPix==0) sp-=4;
			        else sp+=NbPix-1;
			        /*if (vl.norme()>(vl_old)) prog=1;
			         else if (N_voisin==0) prog=-1; else prog=-2;
			        vl_old=vl.norme();
			        */
				break;
			
			case 3 : // Gratifie le déplacement
			       /*if (dep_ok==true) prog=1;
			        else prog=-1;
			        */
			       if (!dep_ok) sp-=3;
			        else sp+=(int)(0.5+Math.cos(alph))*2;
			       break;
		} // switch(n_Satp)
		
		// Si (signal ext est aussi fort que notre signal)
		// Et (si on a pas de chance)
		// Alors, On "rouspete" moins fort
		if ((egal==true) && (Math.random()>0.5)) { prog=-3; }
		
		if (prog==-3) if (sp<127)  {sp++;} else {sp--;}
		if ((Tcurrent==0)&&(sp>126)) {sp=126;}
		
		if (sp>127) sp=127;
		if (sp<-127) sp=-127;
		
		Satp=(float)((float)sp/127); // valeur reelle dans [-1,1] de satP
		
		if (  (sp<=-120) &&
		              ( ((Tcurrent==1)&&(RobotType==TYPE_POUSSEUR))
		              ||((Tcurrent==2)&&(RobotType==TYPE_DECOUPEUR))
		              ||((Tcurrent==3)&&(RobotType==TYPE_DECOUPEUR))
		              ||(Tcurrent==4))
		 )
		{
		  if (frustration_cpt>=Seuil_RasLeBol)
		    {
		      if ((Tcurrent==1)||(Tcurrent==2)||(Tcurrent==4))
		      {
		          DecrocherPlaque();
		          System.out.println("Moi="+N_agent+" decroche plaque="+maPlaque);
		          JeMaintien=false;JeMaintienPlus=false;
		          maPlaque=-1;
		          distAccroche=-1;
		          inh[1]=true;inh[2]=true;inh[3]=true;inh[4]=true;
		      } // if ((Tcurrent==1)||(Tcurrent==2))
		      else
		      if (Tcurrent==3)
		      {
		          inh[1]=true;inh[2]=true;inh[3]=true;inh[4]=true;
		      } // else if (Tcurrent==3)
		
		    } // if (frustration_cpt>=Seuil_RasLeBol)
		  else {frustration_cpt++; //System.out.println("frustration_cpt++");
		        }
		} // Si on doit etre frustré  if (  (sp<=-120) &&
				
		if (debrayage==false)
		{ 
			stat=false; // type nouvelle action...
		}
		else {stat=true;}
		veg=false;
		alt=false;  // si agent cht tache par altruisme
		
		
		//***************************************
		// Si on est pas en mode SURVIE,
		if (veg == false)
		{
		    // etablir une liste des taches possibles avec intensite:
		
		    if ( // Accol01
		          (     ((Tcurrent!=1)&&(RobotType==TYPE_POUSSEUR))
		             || ((Tcurrent==1)&&(RobotType==TYPE_POUSSEUR)&&(pouss_ok))
		             || ((Tcurrent!=2)&&(Tcurrent!=3)&&(RobotType==TYPE_DECOUPEUR))
		             || ((Tcurrent==0)||(Tcurrent==5))
		             )
		          &&(frustration_cpt>0))
		    { frustration_cpt-=Oubli_RasLeBol;//System.out.println("frustration_cpt--");
		      if (frustration_cpt<0)
		      {
		      	frustration_cpt=0;//System.out.println("frustration_cpt'=0");
		      } // if (frustration_cpt<0)
		    } //if ( // Accol01
		
		
		   //** choix tache intensite max:
		   boolean change=false;
		
		  char Sit3;
		   if (maPlaque!=-1) Sit3=2;
		   else
		    if (PercoitPlaque((float)Vtask.a,ouv,(float)Ra,(float)Rv,2)) Sit3=1;
		    else Sit3=0;
		  //if ((Sit3==1)&&(N_agent==1)) System.out.println("Je percoit plaque!");
		  if (exp)
		    {
		      if ((Tcurrent==0)&&(Sit3!=0)) exp_fin=true;
		      if ((Tcurrent==3)&&(Sit3!=1)) exp_fin=true;
		      if (((Tcurrent==1)||(Tcurrent==2))&&(Sit3!=2)) exp_fin=true;
		      if ((Tcurrent==4)&&(JeMaintienPlus)) exp_fin=true;
		
		    } // if (exp)
		    // 010619 fin
		    // calculer le signal le + fort
		   I1=0;I2=0;
		   float vsat=0;
		    if (ns!=-1) vsat=com[ns].sat; else vsat=0;
		
		    egal=false;
		
		    if  ((RmcLauncherApp.MS==true)) // Si on est ds le modele satisfaction/alt
		    {
		      int news=-1;
		      int inf=0,nc=0;
		      RobotAppPhy r;
		
		      for(int i=0;i<otherRobots.size();i++)
		       if ((com[i].c==true) && (com[i].dist>(3*Ra)) )
		       {
		         r=(RobotAppPhy)otherRobots.elementAt(i);
		            nc++;
		          if (com[i].si>si) inf++;
		          if (com[i].sat<0)
		              { if (com[i].sat<=vsat)
		                        { news=i; vsat=com[i].sat;
		                          if (r.RobotType==TYPE_POUSSEUR) I2=2;
		                           else I1=2;
		                        }
		              }
		          else
		              if ((vsat>=0) && (com[i].sat>=vsat))
		                  { news=i; vsat=com[i].sat;
		                    if ((r.RobotType==TYPE_POUSSEUR) && (I2!=2)) I2=1;
		                    if ((r.RobotType==TYPE_DECOUPEUR) && (I1!=2)) I1=1;
		                  }
		       } // if ((com[i].c==true) && (com[i].dist>(3*Ra)) )
		
		        if (news!=-1)
		          {
		           if (ns==-1) ns=news;
		           else
		            if (com[news].sat!=com[ns].sat)  ns=news;
		          }
		          else ns=-1;
		
		
		   // Choix dans la matrice
		   Situation=(char)(Sit3+3*(I2+3*I1));
		
		    if (news!=-1)
		      {
		       if (ns==-1) ns=news;
		       else
			if (com[news].sat!=com[ns].sat)  ns=news;
		      }
		      else ns=-1;
		
		} // if  ((RmcLauncherApp.MS==true)) // Si on est ds le modele satisfaction/alt
		
		    // * test chgt de tache ! *
		    // * pour ALTRUISME *
		     satext=0;
		     
		     //051018 int in=0;
		    if (ns==-1) {oldalt=false;oldns=-1;alt=false;egal=false;}
		    //System.out.print(">> tmax="+tmax+" ns="+ns+" ");
		    if ( (ns!=-1) && (RmcLauncherApp.MS==true)) // **************************
		      {
		    	//051018 in=sp;
			  if (com[ns].si==si) egal=true; else egal=false;
			  if (oldalt==true)
			     {
				 if ((com[oldns].signSat!=oldsign) || (ns!=oldns))
		                 { oldalt=false;
		                 // Fin d'une expérience:
		                    if (exp) exp_fin=true;
		                 }
				 if (! oldalt) cont=false;
			     }
		          if (! oldalt)
			    {
				pers=0;
			        //051018 if (change) in=(int)(t[Tcurrent].i*127);
			    }
			 satext=(int)(com[ns].si);
		       } // if ( (ns!=-1) && (RmcLauncherApp.MS==true))
		         //System.out.println("LE FAMEUX TEST");
		         // LE FAMEUX TEST POUR BASCULER OU CONTINUER en ALTRUISME
		
		////////////////////
		    alt=false;
		    boolean choix=false;
		    // Apprentissage
		    if ((!exp)&&((I1==2)||(I2==2)))
		      InitExp(satext);
		
		    if (!exp) // Si on est pas dans une expérience (?)
		    do
		    {
		    ChoixMatrice(true);
		
		    if (t[Tcurrent].a==true) // si Tcurrent tjs activable apres altruisme par ex.
		     {
		      if (gama*t[tmax].i>(Satp*t[Tcurrent].i))
			{
			    if (tmax!=Tcurrent) { change=true; Tcurrent=tmax; }
			}
		     } else { Tcurrent=tmax; change=true;}
		
		
		     /////////////////
		     if ((ns!=-1) && (t[Ntaches-1].a))
		         {
			  if ((satext<0)
		               && (((satext<si) && (!oldalt))
				  || ((satext<sp) && oldalt))
			      ) alt=true;
		            else
		            if /*( (((alpha*satext)>((1-alpha)*in)) || oldalt)
		                && (satext>Math.abs(si))) alt=true;*/
		                (satext==127) alt=true;
		         }
		        else choix=true;
		
		      if (alt) choix=true;
		      else  {inh[Ntaches-1]=true;}
		    }
		    while(choix!=true);
		
		    if ( (!exp) && ((change)||(alt && !oldalt)) &&(satext!=0) )
		    {
		      InitExp(satext);
		    }
		    // Ici on va examiner l'expérience que l'on tente:
		    // (on calcule la moyenne des signaux recus)
		    if (exp) // else
		    {
		      exp_tps++;
		      if ((exp_tps>exp_max)||((I1==0)&&(I2==0)))
		      { exp_fin=true; }
		      else
		      {
		        exp_moy=(exp_moy*(exp_tps-1)/exp_tps)+(satext/exp_tps);
		      }
		    }
		
		    if ((exp_fin)&&(exp_tps>exp_min))
		    {
		      // Une expérience s'est terminée, on va en tirer des
		      // conclusions :
		        int n;
		        float v,r,Delta,f;
		        n=(int)MAct[Tcurrent][exp_sit][1];
		        v=MAct[Tcurrent][exp_sit][0];
		        Delta=exp_moy-exp_deb;
		        f=Math.abs(Delta)/256;
		        r=((f*Delta/2 + (1-f)*exp_moy)+127)/256; // Reward
		        MAct[Tcurrent][exp_sit][0]=Beta*v+(1-Beta)*r;
		        MAct[Tcurrent][exp_sit][1]=n+1;
		        System.out.print("Moi="+N_agent+" a termine (tps="+exp_tps+") Tcurrent="+Tcurrent+" Moyenne:"+(exp_moy-exp_deb));
		        System.out.println(" Sit="+(int)exp_sit+" Mat="+MAct[Tcurrent][exp_sit][0]+"/"+MAct[Tcurrent][exp_sit][1]);
		      exp=false;
		      exp_fin=false;
		    } // if ((exp_fin)&&(exp_tps>exp_min))
		    else
		      if (exp_fin)
		      {
		        System.out.println("Moi="+N_agent+" a termine car Temps="+exp_tps+" trop court. (Tcurrent="+Tcurrent+", Sit="+(int)Situation+")");
		        exp=false;
		        exp_fin=false;
		      }
		
		
			
		
		    if (alt)
		         {
			      //  System.out.println("Al "+N_agent);
			      alt=true;
			      oldns=ns;
			      oldsign=com[ns].signSat;
			      com[ns].Alt((float)Ra);
			      Da=com[ns].alt.a-Vtask.a;
			      if (Da>Math.PI) Da-=2*Math.PI;
			      else  if (Da<-Math.PI) Da+=2*Math.PI;
			      Vtask.var((float)(Math.abs(Da)*Da/Math.PI));
			      // Vtask.fix(com[ns].alt.vx,com[ns].alt.vy);
			      Vtask.var((float)(Math.random()*Math.PI/8-Math.PI/16));
			      n_Satp=3;
			      oldalt=true;
			      test_agt=0;
			      txt="Alt";
		              Tcurrent=Ntaches-1;
		       }
		       else
		       { oldalt=false; alt=false; ns=-1; }
		
		
		   // ** execution tache sauf altruime **
		
		// debrayage ici !
		if (debrayage==false)
		{  //System.out.println("Embrayage!");
		   //if (Tcurrent==-1) {Tcurrent=Tpast;System.out.println("Embrayage!");}
		   
		   if (alt==false)
		   {	ChoixActionNonAltruisme();
		   	//Tcurrent=-1;
		   	
		   } // if (alt==false)
		}
		if (debrayage==true)
			{	//if (Tcurrent!=-1) {Tpast=Tcurrent;System.out.println("Debrayage!");}
				//Tcurrent=-1;
			}
		
		 } // if (veg == false)
		
		//= Somme des vecteurs *
		
		//if (som) Vtask.addV(Vsli); // eviter agents
		
		// debrayage ici !
		Vtask.fixN(25);
		
		//System.out.println("VtaskI:"+Vtask.vx+","+Vtask.vy);
		/*if (t[1].a) // Pousser
		  System.out.println("Vtask:"+Vtask.vx+","+Vtask.vy+"(Moi="+this.N_agent+")");
		*/
		//== Si deplacement integrer les signaux de repulsion =
		//== et les repulsions aux obstacles
		
		 byte except=(byte)-2;
		 Vecteur Vrad=null;
		 Vrad = new Vecteur(radarV( ((float)Vtask.a),ouv,(float)Ra,(float)Rv,2,except,(byte)1,TYPE_OBSTACLE+TYPE_PLAQUE+TYPE_ROBOT));
	 
		// On envoie les valeurs aux neurones capteurs
		// Tout d'abord les capteurs IR de proximité:
		CapIR valcapteursIR = new CapIR(getCapteursIR());
		//051018 String NeuronGroup= "NeuronGroup"+N_agent;
		int nagenttrouve=0;
		
		//AgentAddress agtAddrTemp;

		for (int i=0; (i<8); i++)
		{	nagenttrouve++;
			//agtAddrTemp = (nsch.getSensor(i).getAddress());
			if (i<8) {
				//System.out.println("Envoi msg à g="+NeuronGroup+" v="+valcapteursIR.NC(i));
				//sendMessage(NeuronGroup,"neuron",new NeuralMessage(valcapteursIR.NC(i),"CapIR"+i));
				double valtemp=3*valcapteursIR.NC(i);
				if (valtemp>=1) {valtemp=1;}
				
				//if (valtemp!=0) {
				sendMessage(CapIRAgt[i].getAddress(),new NeuralMessage(valtemp,"CapIR"+i));
				
				if (valtemp>=0.8)
				{
					siphy-=4;
				}
				
				}
		}
		

		
		siphy+=2;
		
		// Ici la position de la base, 
		//if (v.length>=9)
		
		
		{	// direction de la base => angle ramené entre 0 et 1
			// Angle relatif à la direction du robot (Vtask.a)
			
			VecteurR vtemp=new VecteurR(vz.vx,vz.vy);
			double angleTemp = ((vtemp.normalise(vtemp.a-Vtask.a))/(2*Math.PI));
			//agtAddrTemp = (nsch.getSensor(8).getAddress());
			if ((MotLAgt.getLastValue()==MotRAgt.getLastValue())&&(MotRAgt.getLastValue()==0.5))
			{
				angleTemp=BaseDirAgt.getLastValue();
			}
			sendMessage(BaseDirAgt.getAddress(),new NeuralMessage(angleTemp,"BaseDir"+8+"("+(angleTemp*360)+")"));
			//sendMessage(agtAddrTemp,new NeuralMessage(0,"BaseDir"));
			// distance à la base exprimée sous 1 / d
			// (on ajoute 1 à d pour ramener le resultat entre 0 et 1)
			double disttemp=(64/(vtemp.norme()+1));
			if(MotLAgt.getLastValue() == MotRAgt.getLastValue() && MotRAgt.getLastValue() == MotRAgt.getFirstValue() && MotFact1Agt.getLastValue() != 0.5 && BaseDistAgt.getLastValue() != disttemp)
			{
				disttemp = BaseDistAgt.getLastValue();
			}
			
			if (disttemp>=1) {disttemp=1;}
			if (disttemp==0) {disttemp=0.0001;}
			
			//agtAddrTemp = (nsch.getSensor(9).getAddress());
			sendMessage(BaseDistAgt.getAddress(),new NeuralMessage(disttemp,"BaseDist"+9));	
			int tailledepot=world.Taille_Zone_Depot;
			VecteurR vtemp2=new VecteurR(vz.vx,vz.vy);
			if (vtemp2.norme()<=(tailledepot/2)) {sfaim = sfaim + 100;}
			//sendMessage(agtAddrTemp,new NeuralMessage(0,"BaseDist"));
		} // if (v.length>=9)
		
		
		//agtAddrTemp = (nsch.getDrive(0).getAddress());
		//agtAddrTemp = (nsch.getDriveByName("SatP").getAddress());
		double satptmp=(Satp+1)/2;
		/*
		if (this.Vtask.norme()>=0.25)
		{	// Si on avance on augmente la satisfaction
			satptmp=((Satp*256)+4*(Vtask.norme()-0.25))/256;
		}
		else
		{	// Si on avance pas on diminue de 2/256 (+-equ 1%) 
			satptmp=((Satp*256)-2)/256;
		}
		//System.out.println("Satp:satptmp="+Satp+":"+satptmp);
		sendMessage(SatPAgt.getAddress(),new NeuralMessage(satptmp,"SatP"));
		*/
		if (satext>127) {satext=127;}
		if (satext<-127) {satext=-127;}
		SatExte=(float)((float)satext/127); // valeur reelle dans [-1,1] de satext
		satptmp=(((double)SatExte)+1)/2; //(Sati+1)/2;
		//agtAddrTemp = (nsch.getDriveByName("SatE").getAddress());
		//System.out.println("SatExte:satptmp="+SatExte+":"+satptmp);
		sendMessage(SatEAgt.getAddress(),new NeuralMessage(satptmp,"SatE"));
		
		if (siphy>127) {siphy=127;}
		if (siphy<-127) {siphy=-127;}
		SatIPhy=(float)((float)siphy/127); // valeur reelle dans [-1,1] de satiphy
		satptmp=(((double)SatIPhy)+1)/2; // valeur reelle dans [0,1] de satiphy
		//System.out.println("siphy(-127;+127)="+siphy+" - SatIPhy(-1:1)="+SatIPhy+" - satptmp(0;1)="+satptmp);
		//agtAddrTemp = (IPhyAgt.getAddress());
		//double valtmp= 1; valtmp=valtmp*2/3;
		sendMessage(IPhyAgt.getAddress(),new NeuralMessage(satptmp,"IPhy"));
		
		
		int tmpvalueforfaim = (int)(
			(10*Math.abs(MotLAgt.getLastValue()-0.5))
			+(10*Math.abs(MotRAgt.getLastValue()-0.5))
			);
		
		sfaim=sfaim-10-tmpvalueforfaim;
		if (sfaim>32767) {sfaim=32767;}
		if (sfaim<-32767) {sfaim=-32767;}
		SatFaim=(float)((float)sfaim/32767); // valeur reelle dans [-1,1] de satiphy
		satptmp=(((double)SatFaim)+1)/2; // valeur reelle dans [0,1] de satiphy
		//System.out.println("siphy(-127;+127)="+siphy+" - SatIPhy(-1:1)="+SatIPhy+" - satptmp(0;1)="+satptmp);
		//double valtmp= 1; valtmp=valtmp*2/3;
		sendMessage(FaimAgt.getAddress(),new NeuralMessage(satptmp,"Faim"));
		
		
		
		
		Vrad=null;
		Vrad = new Vecteur(radarV( ((float)Vtask.a),ouv,(float)Ra,(float)Rv,2,except,(byte)1,ToDetect));
		
		if (stat==false) // Si on est en déplacement
		{
		     Vsli.fix(Vtask.vx/3,Vtask.vy/3);
		     Vi.fix(Vtask.vx,Vtask.vy);
		
		     if  ((RmcLauncherApp.MS==true))
		     {
		     //int nr=0;
		     //VecteurR Vslit = new VecteurR(0,0);
		     for(int i=0;i<otherRobots.size();i++)
		         if ((com[i].known==true) && (com[i].Dsat<0) && (com[i].dist>=0)) // NOTION INFORMATION
		           { com[i].Alt((float)1);
		             Da=com[i].alt.a-Vsli.a;
		             if (Da>Math.PI) Da-=2*Math.PI;
		             else  if (Da<-Math.PI) Da+=2*Math.PI;
		             Vsli.var((float)(Math.abs(Da)*Da/Math.PI));
		           }
		     } // if  ((RmcLauncherApp.MS==true))
		
		//sendMessage(nsch.getAddress(), new StringMessage("clockrunme"));

		 VecteurR Vobs= new VecteurR(Vrad);
		
		
		 // Les murs / robots / plaques
		 float da;
		 if (Vobs.norme()>0)
		   {
		     obs=true;
		     s=Vtask.signe(Vobs);
		
		     if ((test_agt==0) && (Vrad.s==-1)) { da=(float)((Math.PI/16)*carre(2*Ra/Vobs.norme()));}
		     else
		     if (Vobs.norme()<1.2*Ra) da=(float)((Math.PI/16)*0.85*carre(carre(2*Ra/Vobs.norme())));
		      else da=(float)((Math.PI/16)*carre(2*Ra/Vobs.norme()));
		
		     da=(float)(da*(0.9+Math.random()*0.2)); // un peu d'aleatoire
		     Vtask.var((float)(s*da));
		   }
		 else obs=false;
		 //int s2=0;
		
		/* ababi */
		
		 if (sfaim<=-32767)
		 { // A pu de batterie: robot mourru
		 	Vtask=getVectorFromMotors2(0.5,0.5,C.R,SavedAngle);
		 }
		 else
		 {
		 	Vtask=getVectorFromMotors2(MotLAgt.getLastValue(),MotRAgt.getLastValue(),C.R,SavedAngle);
		 }
		 //REVIENDRE A nettoyer : (sp déjà définit, mais mal, d'ou la bidouille ci dessous)
		 sp = (int)(((SatPAgt.getLastValue())*2-1)*127);
		 Satp=(float)((float)sp/127);
		 satptmp=(Satp+1)/2;
		 // fin bidouille
		 if (this.Vtask.norme()>=0.5)
			{	// Si on avance on augmente la satisfaction
				satptmp=((satptmp*128)+2*(Vtask.norme()-0.25))/128;
			}
			else
			{	// Si on avance pas on diminue de 1/128 (de l'orde du %) 
				satptmp=((satptmp*128)-1*(1-Vtask.norme()))/128;
			}
		 if (satptmp>1) satptmp=1;
		 if (satptmp<0) satptmp=0;
		 
		sendMessage(SatPAgt.getAddress(),new NeuralMessage(satptmp,"SatP"));
		 
		//agtAddrTemp=nsch.getActorByName("MotL");
		//sendMessage(agtAddrTemp,new NeuralMessage(0.5));
		Vtaskold.fix(Vtask.vx,Vtask.vy);
		
		ExecutionTacheDeplacement();
		
		} // if (stat==false) // Si on est en déplacement

	} //if ((x!=-1) || (y!=-1))

      return ("runMe"); // fin de runMe()
    }

public void ExecutionTacheDeplacement()
{
		/** Execution tache deplacement **/
		
		boolean dep=true;
		// si le vecteur de deplacement est nul, pas de deplacement !
		if ((Vtask.vx==Vtask.vy) && (Vtask.vx==0)){ dep = false; return;}
		mvt++;
		float xrp,yrp;
		// 010616-2
		dep_ok=true;
		
		xrp=xr+(float)Math.cos(Vtask.a);
		yrp=yr-(float)Math.sin(Vtask.a);
		
		int xp=Math.round(xrp);
		int yp=Math.round(yrp);
		float xPl=xrp-xr;
		float yPl=yrp-yr;
		
		      // ext. env ?
		      if ((xp<Ra) || (yp<Ra) || (xp>(dimx()-Ra)) || (yp>(dimy()-Ra)) )
			  {  dep=false; // 010616-2 dep_ok=false;
		          }
		      else
		      {
		       //byte v=getValue(xp,yp);
		
		       // Si on (Pousse/Tire)
		       if (Tcurrent==1)
		       {
		        if (maPlaque!=-1)
		          // On a une plaque !!!!!
		          {
		            //this.DeplacerPlaque(maPlaque,new Point(xPl,yPl));
		            if (RobotType==TYPE_POUSSEUR)
		            {
		              //System.out.print("Je("+N_agent+") pousse ...");
		              if ((AppliquerForce(Math.round(xPl*PuissanceEnPoussee),Math.round(yPl*PuissanceEnPoussee),maPlaque))==false)
		              System.out.println("Moi="+N_agent+" Oups impuissant sur maPlaque="+maPlaque);
		              //System.out.print("ok. ");
		            }
		            else
		              if (RobotType==TYPE_DECOUPEUR)
		              {
		                //System.out.print("Je("+N_agent+") coupe ...");
		                if ((AppliquerDecoupe(maPlaque))==false)
		                System.out.println("Moi="+N_agent+" Oups impuissant sur maPlaque="+maPlaque);
		                //System.out.print("ok. ");
		              }
		          } // if (maPlaque!=-1)
		        // Ne devrait (jamais) arriver:
		        else {System.out.println("Bug RobotBrain.runMe");}
		       } // if (Tcurrent==1)
		       else
		       {
		             C.xc=xp; // on suppose le deplacement un instant :
		             C.yc=yp;
		             // Si (pas de colision monde)
		             this.C.R=18;
		             if (test_moveD()==true)
		                { // 010608
		
		                  if (test_moveP()) // Si (pas de colision plaque)
		                  {
		                    x=xp; y=yp;
		                    xr=xrp; yr=yrp;
		
		                    fluid++;
		                    im=0;
		                    // New 28-05-2001
		                    alph=((float)(Vtask.DanglePP(Vi)));
		                    if (Vtask.DanglePP(Vi)<=(Math.PI/4)) dep_ok=true;
		                    if (Tcurrent==1) System.out.println("Alert !!!!");
		                  }
		                  else { dep_ok=false;
		                        }
		                } // if (test_moveD()==true)
		                else { dep_ok=false;
		                    }
		             this.C.R=17;
		        } //else // if (Tcurrent==1)
		      } // else // if ((xp<Ra) || (yp<Ra) || (xp>(dimx()-Ra)) || (yp>(dimy()-Ra)) )
		
		      if (dep==false) // == Si dep impossible
			{
			    C.xc=x; // on remet l'agent à sa position :
			    C.yc=y;
		            if (Tcurrent==1) // pousser
		              System.out.println("Moi="+this.N_agent+" pousse mais bloque");
			    // compteur de fluidite...
			    im++;
		
			 dep_ok=false;
			} // if (dep==false)
}

/** Nouvelle version en cours de développement (modèle géométrique, calcul en deux temps) */
public VecteurR getVectorFromMotors(double Lmotor,double Rmotor, double Rayon, double Angle)
{
	//double coefmoteur=4;
	// Permet de calculer la distance parcourue en fn de la valeur de Lmotor et Rmotor
	
	// x et y coordonnées du robot pour le calcul
	//double x,y;
	//x=this.xr; y=this.yr;
	
	// Calculer l'effet de la roue droite:
	
	
	VecteurR Vresult= null;
	
	Torseur MotorLeft,MotorRight;
	VecteurR vl=new VecteurR(Lmotor-0.5,0);
	VecteurR vr=new VecteurR(Rmotor-0.5,0);
	System.out.println(vl.toString()+" # "+vr.toString());
	vl.fixA(Angle);
	vr.fixA(Angle);
	System.out.println(vl.toString()+" # "+vr.toString());
	
	MotorLeft = new Torseur(vl.vx,vl.vy,0,0,0,0);
	MotorRight = new Torseur(vr.vx,vr.vy,0,0,0,0);
	MotorLeft.reduction(-1*Rayon,0,0);
	MotorRight.reduction(1*Rayon,0,0);
	
	System.out.println(MotorLeft.toString()+" # "+MotorRight.toString());
	Torseur Res=Torseur.add(MotorLeft,MotorRight);
	
	Vresult=new VecteurR(Res.rx,Res.ry);
	System.out.println(Res.toString());
	Vresult.fixA(Res.moz);	
	return Vresult;
}

/** Version avec torseurs (mauvaise) */
public VecteurR getVectorFromMotors3(double Lmotor,double Rmotor, double Rayon, double Angle)
{
	VecteurR Vresult= null;
	
	Torseur MotorLeft,MotorRight;
	VecteurR vl=new VecteurR(Lmotor-0.5,0);
	VecteurR vr=new VecteurR(Rmotor-0.5,0);
	System.out.println(vl.toString()+" # "+vr.toString());
	vl.fixA(Angle);
	vr.fixA(Angle);
	System.out.println(vl.toString()+" # "+vr.toString());
	
	MotorLeft = new Torseur(vl.vx,vl.vy,0,0,0,0);
	MotorRight = new Torseur(vr.vx,vr.vy,0,0,0,0);
	MotorLeft.reduction(-1*Rayon,0,0);
	MotorRight.reduction(1*Rayon,0,0);
	
	System.out.println(MotorLeft.toString()+" # "+MotorRight.toString());
	Torseur Res=Torseur.add(MotorLeft,MotorRight);
	
	Vresult=new VecteurR(Res.rx,Res.ry);
	System.out.println(Res.toString());
	Vresult.fixA(Res.moz);	
	return Vresult;
}

/** Version approximée (plutot bonne) */
public VecteurR getVectorFromMotors2(double Lmotor,double Rmotor, double Rayon, double Angle)
{
	//double RminusL = 0;
	double rm=Rmotor-0.5;
	double lm=Lmotor-0.5;
	double deltarmlm=rm-lm;
	VecteurR Vresult= null; //new VecteurR(0,0);
	//if (Rmotor -0.5) - (Lmotor-0.5);
	if (rm==lm)
	{
		if (rm!=0)
		{
			Vresult=new VecteurR(rm+lm,0);
			Vresult.fixA(Angle);
		}
		else // si rm==0 et lm==0, alors robot immobile
		{
			Vresult=new VecteurR(0,0);
			Vresult.fixA(Angle);
		}
	}
	
	else
	if ((rm>=0)&&(lm>=0))
	{
		Vresult=new VecteurR(2*Math.abs(deltarmlm),0);
		Vresult.fixA(Angle+((deltarmlm)*(Math.PI/4)));
	}
	else if ((rm<=0)&&(lm<=0))
	     {
	     	Vresult=new VecteurR(-2*Math.abs(deltarmlm),0);
		Vresult.fixA(Angle+((deltarmlm)*(Math.PI/4)));
	     }
	     else
	     	{
	     		deltarmlm=Math.abs(rm)-Math.abs(lm);
	     		if (rm>lm)
	     		{
	     			Vresult=new VecteurR(2*Math.abs(deltarmlm),0);
	     			Vresult.fixA(Angle+((deltarmlm)*(Math.PI/4)));
	     		}
	     		else if (lm>rm)
	     		{
	     			Vresult=new VecteurR(2*Math.abs(deltarmlm),0);
	     			Vresult.fixA(Angle-((deltarmlm)*(Math.PI/4)));
	     		}
	     		
		}
	return Vresult;
}

public String signals()
     {
      N_voisin=0;

      if (((x!=-1) || (y!=-1))  && (RmcLauncherApp.MS==true))
      {
       emit=false;
       //== Calcul et emission Sati =
       //051018 boolean insat=false;

	if (stat==false)
	 {
	  // agent(s) devant la marche de dep ?

	    //float A=ouv;
	    //int k=0;
	    //VecteurR Va = new VecteurR(0,0);
	    Vsa.fix(0,0);
	    proche=false;

            if ((RobotType==TYPE_POUSSEUR) && (maPlaque!=-1)
                && (GetForceRequise(maPlaque)>PuissanceEnPoussee)
                &&(!pouss_ok) && (ns==-1)) //&&(sp<-50))
            { // 010619 Bug
              si=127;
              emit=true;
              Rc=100;
              //051018 insat=true;
              //System.out.println("Emission !!!");
              //com[N_agent]
            }
            else
            {
                si=0;
                Rc=70;
            }
        }
      if ((si!=120) && (sp!=0) && (sp<127))
             {
              emit=true;
              si=sp;
             }

       if (emit==false)//if (insat==false)
               {
                   si=0; // remettre a zero quand non emit
               }

       Sati=(float)((float)si/127);

     }
     return ("signals");

    }

    public String majDepRob()
    {
      return ("majDepRob");
    }

    public int UpdateNumPlaque(int numP)
    {
      if ((numP!=-1) && (!Exist(numP)))
              {
                System.out.println("Moi="+N_agent+" Decrochage force'de "+maPlaque);
                maPlaque=-1; distAccroche=-1;
                return -1;
              }
      int d=GetDistanceTo(numP,C.xc,C.yc);
      if ((numP!=-1) &&
          ( (d>distAccroche+varDecroche)) //||(d<distAccroche-(2*varDecroche)) )
          )
      {
          System.out.println("Moi="+N_agent+" s'est fait largue' par "+maPlaque+" "+distAccroche+"/"+d);
          DecrocherPlaque();
          maPlaque=-1; distAccroche=-1;
          return -1;
      }
      /*if ((numP!=-1) && (Tcurrent!=2) && (Tcurrent!=1) && (Tcurrent!=4))
      {
        DecrocherPlaque();
          maPlaque=-1; distAccroche=-1;
          return -1;
      }*/
      return numP;

    }

    public void ChoixMatrice(boolean allow_altruism)
    {
      tmax=0;imax=0;
            for (int i=0;i<Ntaches;i++)
            { t[i].a=false; }
	    for(int i=0;i<Ntaches;i++)
              if ((inh[i]!=true)&&(MAct[i][Situation][0]>imax))
              {tmax=i;imax=MAct[i][Situation][0];}
            t[tmax].a =true;
    }

    public void InitExp(int satext)
    {
      if (Math.random()<coef_exp) // coef d'exploration
                { //Tentons une expérience
                  //System.out.println("Moi="+N_agent+" va tenter");
                  float Chance[][]=new float[Ntaches][2];
                  int j=0;
                  float somme=0;
                  for (int i=0;i<Ntaches;i++)
                    {
                      if (!inh[i])
                        {
                          Chance[j][1]=i;
                          Chance[j][0]=(MAct[i][Situation][0]+1)
                                      /(MAct[i][Situation][1]+1);
                          somme+=Chance[j][0];
                          j++;
                        }
                    }
                  //System.out.println("-1");
                  float limite=(float)Math.random()*somme;
                  somme=0;
                  int i=0;
                  do
                  {
                    somme+=Chance[i][0];
                    i++;
                  }
                  while ((somme<=limite)&&(i<j));
                  //System.out.println("-2");
                  Tcurrent=(int)Chance[i-1][1];
                  exp=true;
                  exp_deb=satext;
                  exp_tps=0;
                  exp_sit=Situation;
                  System.out.println("Moi="+N_agent+" essaye Tcurrent="+Tcurrent);
                }
    }
 
    protected NeuronAgent createNeuronAgent()
    {
            NeuronAgent na;
        
            ///* 040429
            NeuronAgentNb++;
            
            NeuronGroup= "NeuronGroup"+N_agent;
            
            String NeuronName = "Neurone" + NeuronAgentNb;
            na = new NeuronAgent();
            
            //foundGroup(NeuronGroup);
            na.initialisation(NeuronGroup, NeuronName, nsch); //, this);
            
    	    //na.launchAgent(na,"Robot",false);
    	    
    	    launchAgent(na,NeuronName,false);
	    return na;
    }
    
    protected NeuronAgent createDriveAgent()
    {
    	    NeuronAgent na;
            NeuronAgentNb++;
            
            NeuronGroup= "NeuronGroup"+N_agent;
            
            String NeuronName = "Neurone" + NeuronAgentNb;
            na = new NeuronAgent();
            
            //foundGroup(NeuronGroup);
            na.initialisation(NeuronGroup, NeuronName, nsch); //, this);
            
    	    //na.launchAgent(na,"Robot",false);
    	    
    	    launchAgent(na,NeuronName,false);
	    return na;
    }
    public void end()
    {
	nsch.end();
	killAgent(nsch);
	leaveRole(NeuronGroup, "member");
	leaveGroup(NeuronGroup);
	super.end();
    	System.out.println("RobotBrain:end()");
    	
    }

    
}