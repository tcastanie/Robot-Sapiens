package smaapp;

import kernelsim.*;

//import madkit.kernel.*;
//import java.awt.*;
//import java.lang.reflect.Method;
import java.util.*;
//import java.io.*;

// ***********************************
// *** NeuronAgent.java            ***
// *** Creation 23 / 07 / 2003     ***
// *** Jérôme Chapelle@lirmm.fr    ***
// ***********************************

/** Babbler.java 
* @author Jérôme Chapelle 2004
*/
public class Babbler 
{
  NeuronScheduler nsch;

  // Vector TestedMotors=new Vector();
  public NeuronAgent currentMotor=null;
  public NeuronAgent OtherMotor=null;
  public int OtherMotorNum=0;
  Vector<NeuronAgent> OtherMotors=new Vector<NeuronAgent>();
  Vector<NeuronAgent> MotorGroup=new Vector<NeuronAgent>();

  int inhibit; // variable qui permet d'éviter de changer de tache en permanence
  int inhibitmax=20; // Nb de boucles avant de changer de tache  
  double generalSat=0;
  
  int nbintervals=10; // Nb de valeurs différentes pour une phase de test d'un moteur
  Vector ValuesToTry=new Vector();
  public double ValueToTry=0;
  //Vector ValuesToTry2=new Vector();

  public int babblephasenum=0;
  public final int BABBLER_BLANK=0;
  public final int BABBLER_WORKING=1;
  public final int BABBLER_END=2;
  //public final int BABBLER_ZOMBIE=3;
  public final int BABBLER_RENEW=3;
    
  public int Babbler_State=BABBLER_BLANK;
  
  Vector<NeuronAgent> Sensors=new Vector<NeuronAgent>();
  Vector SensorGroup=null;
  double[] SensorsValues=null;
  int SensorsValuesNb=0;
  double[] LastSensorsValues=null;
  int LastSensorsValuesNb=0;
  double[] SensorsImpact=null;
  int SensorsImpactNb;
  double[] SensorsImpactV1=null;
  int SensorsImpactV1Nb;
  double[] SensorsImpactV2=null;
  int SensorsImpactV2Nb;

  
  /** Constructs a new Babbler */
  public Babbler(NeuronScheduler nsched)
    {
	this.nsch=nsched;
	checkAll();
	
    }
  /** Initialize the babbler: Motors and Sensors are listed, Sensor values are captured */
  public void Initialize()
  {
	  babblephasenum++;
	  int ilim=nsch.getActorSize();
	  NeuronAgent atemp=null;
	  OtherMotors.clear();
	  MotorGroup.clear();
	  System.out.println("Babble.checkAll:Actor size="+ilim);
	  for (int i=0;i<ilim;i++)
	  {
	  	atemp=nsch.getActor(i);
	  	OtherMotors.addElement(atemp);
	  }
	  OtherMotorNum=-1;
	  atemp=/*(NeuronAgent)*/(OtherMotors.firstElement());
  	  OtherMotors.removeElement(atemp);
  	  currentMotor=atemp;
  	  MotorGroup.addElement(currentMotor);
  	  
  	  ValuesToTry=generateValuesToTry(nbintervals);
	  
	  ilim=nsch.getSensorSize();
	  SensorsValuesNb=ilim;
	  SensorsValues=new double[SensorsValuesNb];
	  SensorsImpact=new double[SensorsValuesNb];
	  SensorsImpactNb=0;
	  SensorsImpactV1=new double[SensorsValuesNb];
	  SensorsImpactV1Nb=0;
	  SensorsImpactV2=new double[SensorsValuesNb];
	  SensorsImpactV2Nb=0;
	  
	  for (int i=0;i<ilim;i++)
	  {
	  	atemp=nsch.getSensor(i);
  		Sensors.addElement(atemp);
  		SensorsValues[i]=(/*(NeuronAgent)*/atemp).getLastValue();
	  }
	  inhibit=inhibitmax;
	  this.generalSat=nsch.getDriveManager(0).getLastValue();
	  System.out.println("Babbler: initialisé ("+babblephasenum+")");
	  Babbler_State=BABBLER_WORKING;
  }
  
  /** Check if all is ok. Initiates the babbler if needed.  */
  public void checkAll()
  {	// Cas où le babbler commence ...
  	if (/*(TestedMotors.size()==0)&&*/(OtherMotors.size()==0)&&(currentMotor==null))
  	{ // Si aucun moteur testé, et qu'on en teste pas un:
  	  	Initialize();
  	}
  	
  	// currentMotor==null => on passe au moteur suivant
  	if (currentMotor==null)
  	{	NeuronAgent atemp=null;
  		atemp=/*(NeuronAgent)*/(OtherMotors.firstElement());
  		OtherMotors.removeElement(atemp);
  		OtherMotorNum=0;
  		currentMotor=atemp;
  		ValuesToTry=generateValuesToTry(nbintervals);
  		/*System.out.println("Babbler.checkAll:VectorSize="+ValuesToTry.size());
  		for (int i=0;i<ValuesToTry.size();i++)
  		{	System.out.println("Babbler.checkAll:VectorValue(i="+i+")="+((Double)ValuesToTry.elementAt(i)).doubleValue());
  			}*/
  	}
  }
  /** Generates <i>n</i> values and returns a vector of these values */
  @SuppressWarnings("unchecked")
public Vector generateValuesToTry(int n)
  {	Vector tmp = new Vector();
  	double interval= (((double)1)/((double)n));
  	//System.out.println("Babbler.generateValuesToTry:interval="+interval);
  	double vtemp;
  	for (int i=0;i<n;i++)
  	{	double d = /*(double)*/i;
  		vtemp=(d*interval);
  		//System.out.println("Babbler.generateValuesToTry:i,vtemp="+i+","+vtemp);
  		tmp.addElement(new Double(vtemp));
	}
	tmp.addElement(new Double(1));

  	return tmp;
  }
  /** Back up of previous sensor values, and capture the new ones
  */
  public void updateSensorsValues()
  {
  	LastSensorsValues=SensorsValues;
  	//LastSensorsValuesNb=SensorsValuesNb;
  	
	  SensorsValues=new double[SensorsValuesNb];
	  
	  //NeuronAgent atemp=null;
	  double vtemp; //,vtemp2;
	  double oldGeneralSat=generalSat;
	  generalSat=nsch.getDriveManager(0).getLastValue();
	  if (oldGeneralSat==0)
	  {	oldGeneralSat=generalSat; }

	  for (int i=0;i<SensorsValuesNb;i++)
	  {
	  	vtemp=(/*(NeuronAgent)*/(Sensors.elementAt(i))).getLastValue(); //;
  		//vtemp=atemp.getLastValue();
  		SensorsValues[i]=vtemp;
  		
  		//oldGeneralSat=generalSat;
  		
  		// on cumule l'impact proportionnellement au changement d'état émotionnel de l'agent
  		SensorsImpact[i]+=(Math.abs(vtemp-LastSensorsValues[i])*((generalSat+1)/(oldGeneralSat+1)));
  		SensorsImpactNb++;
  		// Avant (ds le compareAndUpdate):
  		// SensorsImpact[i]+=Math.abs(SensorsValues[i]-LastSensorsValues[i]);
	  }
	  //compareAndUpdate();
  }

  public double truncate(double n,int nbdec)
  {
	  double power=Math.pow(10,nbdec);
	  double result = Math.round(n*power)/power;
	  if (result==1) {result=Math.floor(n*power)/power;}
	  return result;
	  
  }
  public double analyseImpact()
  {
      double maximpactV1 = 0;
      int maximpactV1Nb = 0;
      double maximpactV2 = 0.0D;
      int maximpactV2Nb = 0;
      VecteurN V1=new VecteurN(SensorsImpactV1,SensorsValuesNb);
      VecteurN V2=new VecteurN(SensorsImpactV2,SensorsValuesNb);
      double angle=V1.angle(V2);
      double distance=V1.distance(V2);
      double sigma_angle=VecteurN.angleSimilaire(V1,V2); //1-(angleAbs/(Math.PI));
      double sigma_distance = VecteurN.distanceSimilaire(V1,V2); //(Math.min(V1.norme(),V2.norme())/Math.max(V1.norme(),V2.norme()));
      System.out.println("analyseImpact:angle(VA,VB)="+truncate(angle,3)+" // Distance="+truncate(distance,3));
      //System.out.println("analyseImpact:angle(VA)="+V1.angle()+"/"+V1.angleNorme()+" // angle(VB)="+V2.angle()+"/"+V2.angleNorme());
      System.out.println("analyseImpact:sigma_angle="+truncate(sigma_angle,3)+" // sigma_distance="+truncate(sigma_distance,3)+" // sigma_angle*sigma_distance="+truncate((sigma_angle*sigma_distance),3));
      
      for(int k = 0; k < SensorsValuesNb; k++)
      {
          if(SensorsImpactV1[k] > maximpactV1)
          {
        	  maximpactV1 = SensorsImpactV1[k];
        	  maximpactV1Nb = k;
          }
          if(SensorsImpactV2[k] > maximpactV2)
          {
        	  maximpactV2 = SensorsImpactV2[k];
        	  maximpactV2Nb = k;
          }
      }

      if(maximpactV1Nb == maximpactV2Nb)
      {
          System.out.println("Motors :"+(/*(NeuronAgent)*/MotorGroup.get(0)).getLabel()+"(V1="+(floor(maximpactV1, 3))+"/"+SensorsImpactV1Nb+") and "+(currentMotor.getLabel())+"(V2="+(floor(maximpactV2, 3))+"/"+SensorsImpactV1Nb+")");
          //return Math.min(maximpactV1, maximpactV2) / Math.max(maximpactV1, maximpactV2);
          return (sigma_angle*sigma_distance);
          
      }
      
      //else
      return 0;
      
  }
  
  double floor(double d, int i)
  {
      double d1 = Math.pow(10D, i);
      return Math.ceil(d1 * d) / d1;
  }
  
  /*
  public void compareAndUpdate()
  {
  	for (int i=0;i<SensorsValuesNb;i++)
  	{
  		SensorsImpact[i]+=Math.abs(SensorsValues[i]-LastSensorsValues[i]);
  	}
  	
  }*/

  public void showVector(double SensorsImpactVector[], int SensorsImpactVectorNb, int SensorsValuesNbLocal, String FirstMotorLabel, String VectorName)
  {
      System.out.println(VectorName+"= impact de("+FirstMotorLabel+"): ");
      for(int k = 0; k < SensorsValuesNbLocal; k++)
          System.out.print(+(floor(SensorsImpactVector[k], 2))+" ");

      System.out.println();
      double d = /*(double)*/SensorsImpactVectorNb;
      for(int l = 0; l < SensorsValuesNbLocal; l++)
    	  System.out.print((floor(SensorsImpactVector[l] / d, 2))+" ");

      System.out.println();
  }

  public void execute2()
  {  
	  if (Babbler_State<BABBLER_RENEW)
	  {
	  if (OtherMotors==null)
	  	{
	  		Initialize();
	  	}

	  	// inhibit = 0 => nouvelle expérience
	  	if (inhibit<=0)
	  	{	//System.out.println("Babbler.execute: inhibit=0");
	  		inhibit=inhibitmax;
	  		updateSensorsValues();
	  		//compareAndUpdate();
	  		if ((ValuesToTry.size()==0))
	  		{ // Toutes les valeurs ont été essayées pour le moteur
	  			//System.out.println("Impact de("+currentMotor.getLabel()+"): ");
	  			
	  			nsch.sendMessage(currentMotor.getAgentAddressIn(Global.Community, nsch.simgroup,  "scheduler"),new NeuralMessage(currentMotor.getFirstValue()));
	  			
	  			//currentMotor.setLastValueAsFirstValue();
	  			
	  			if (OtherMotorNum == -1)
	  			{
		  			// OtherMotorNum = -1, ça veut dire qu'on vient de finir de tester
		  			// le premier moteur
		  			OtherMotorNum=0;
		  			
		  			SensorsImpactV1=SensorsImpact;
		  			SensorsImpactV1Nb=SensorsImpactNb;
		  			SensorsImpact=new double[SensorsValuesNb];
		  			SensorsImpactNb=0;
		  			
		  			System.out.println("Babbler: Fin de test premier moteur ("+(/*(NeuronAgent)*/MotorGroup.firstElement()).getLabel()+")");
                    showVector(SensorsImpactV1, SensorsImpactV1Nb, SensorsValuesNb, (/*(NeuronAgent)*/MotorGroup.firstElement()).getLabel(), "SensorsImpactV1");
		  			
		  			currentMotor=/*(NeuronAgent)*/OtherMotors.elementAt(OtherMotorNum);
		  			ValuesToTry=generateValuesToTry(nbintervals);
			  		Double vtt=(Double)(ValuesToTry.elementAt(0));
		  			ValueToTry=vtt.doubleValue();
		  			ValuesToTry.remove(0);
		  			//System.out.println("Babbler.execute:newValue"+ValueToTry);  				
		  			
		  			
	  			}
	  			else // OtherMotorNum!=-1 => on a fini de tester un des OtherMotors
	  			{
	  				SensorsImpactV2=SensorsImpact;
	  				SensorsImpactV2Nb=SensorsImpactNb;
		  			SensorsImpact=new double[SensorsValuesNb];
		  			SensorsImpactNb=0;
		  			
		  			System.out.println("Babbler: Fin de test d'un des OtherMotors ("+currentMotor.getLabel()+")");
                    showVector(SensorsImpactV2, SensorsImpactV2Nb, SensorsValuesNb, currentMotor.getLabel(), "SensorsImpactV2");
                    
		  			double sigma=analyseImpact();
		  			if (sigma>=0.5)
		  			{
		  				MotorGroup.addElement(currentMotor);
		  				System.out.println("Moteur "+currentMotor.getLabel()+" sera ajouté au groupe (sigma="+truncate(sigma,3)+")");
                    } else
                    {
                        System.out.println("Moteur "+currentMotor.getLabel()+" ne sera PAS ajouté au groupe (sigma="+truncate(sigma,3)+")");
                    }
		  			
	  				// OtherMotorNum !=-1 => on vient de finir de tester un autre moteur
	  				OtherMotorNum++;
	  				
	  				if (OtherMotorNum>=OtherMotors.size())
	  				{	// On vient de tester le dernier moteur :o/
	  					// Le babbling est sensé être terminé ...
	  					System.out.println("Babbler: Babbling terminé, MotorGroup contient:");
                        for(int i = 0; i < MotorGroup.size(); i++)
                            System.out.print("-"+(/*(NeuronAgent)*/MotorGroup.get(i)).getLabel());

                        System.out.println("-");
	  					Babbler_State=BABBLER_END;
	  				}
	  				else
	  				{	// On passe au moteur suivant
	  					currentMotor=/*(NeuronAgent)*/OtherMotors.elementAt(OtherMotorNum);
	  					// Generation des valeurs ...
	  					ValuesToTry=generateValuesToTry(nbintervals);
				  		Double vtt=(Double)(ValuesToTry.elementAt(0));
			  			ValueToTry=vtt.doubleValue();
			  			ValuesToTry.remove(0);
			  			//System.out.println("Babbler.execute:newValue"+ValueToTry);  
	  				}
	  				
	  				
	  			}
	  			
	  		} // if ((ValuesToTry.size()==0))
	  		else // Il reste donc des valeurs à essayer ...
	  		{	
	  			/*System.out.println("Babbler.execute:VectorSize="+ValuesToTry.size());
		  		for (int i=0;i<ValuesToTry.size();i++)
		  		{	System.out.println("Babbler.execute:VectorValue(i="+i+")="+((Double)ValuesToTry.elementAt(i)).doubleValue());
		  		}*/
	  			updateSensorsValues(); // Reviendre: pas déjà appelé ???
		  		Double vtt=(Double)(ValuesToTry.elementAt(0));
	  			ValueToTry=vtt.doubleValue();
	  			ValuesToTry.remove(0);
	  			//System.out.println("Babbler.execute:newValue"+ValueToTry);
	  		} //else // if ((ValuesToTry.size()==0))
	
	  		
	  	} // if (inhibit<=0)
	  	else
	  	{
	 			updateSensorsValues();
	  			//compareAndUpdate();
	  			inhibit--;
	  		
	  	}
	} // if (Babbler_State<BABBLER_ZOMBIE) equiv à >=BABBLER_END
	  else
	  {
		  if (this.Babbler_State==this.BABBLER_RENEW)
		  {
			  Initialize();
		  }
		  // else // state = ZOMBIE
		  // { // do nothing }
	  }
  	
  }

  public void execute()
  {
  	checkAll();
  	
  	// inhibit = 0 => nouvelle expérience
  	if (inhibit<=0)
  	{	System.out.println("Babbler.execute: inhibit=0");
  		inhibit=inhibitmax;
  		updateSensorsValues();
  		//compareAndUpdate();
  		if (ValuesToTry.size()==0)
  		{ // Toutes les valeurs ont été essayées
  		  // Reviendre: tirer conclusion expérience +vidage Impact
  			System.out.println("Impact de("+currentMotor.getLabel()+"): ");
  			for (int i=0;i<SensorsValuesNb;i++)
  			{ 
  				//System.out.print(SensorsImpactV1[i]/SensorsImpactV1Nb+" ");
  				System.out.print(SensorsImpactV1[i]+" ");
  			}
  			System.out.println("");
  			
  			// Si on est sur le dernier OtherMotor
  			// On met currentMotor à null, le checkAll qui suit fera le necessaire
  			// cad: currentMotor va passer au moteur restant dans la liste
  			if (OtherMotorNum==(OtherMotors.size()-1))
  			{
  				currentMotor=null;
  			}
  			// Si on est pas au dernier OtherMotor, on passe au suivant
  			else
  			{
  				OtherMotorNum++;
  			}
  			
  			
  			
  			SensorsImpactV1=new double[SensorsValuesNb];
	  		SensorsImpactV1Nb=0;
  			checkAll(); // passer au moteur suivant, autre tirage de valeurs...
  		}
  		else
  		{
  			/*System.out.println("Babbler.execute:VectorSize="+ValuesToTry.size());
	  		for (int i=0;i<ValuesToTry.size();i++)
	  		{	System.out.println("Babbler.execute:VectorValue(i="+i+")="+((Double)ValuesToTry.elementAt(i)).doubleValue());
	  	}*/
	  		Double vtt=(Double)(ValuesToTry.elementAt(0));
  			ValueToTry=vtt.doubleValue();
  			ValuesToTry.remove(0);
  			System.out.println("Babbler.execute:newValue"+ValueToTry);
  		}

  		
  	}
  	else
  	{
 			updateSensorsValues();
  			//compareAndUpdate();
  			inhibit--;
  		
  	}
  	
  }

}

  