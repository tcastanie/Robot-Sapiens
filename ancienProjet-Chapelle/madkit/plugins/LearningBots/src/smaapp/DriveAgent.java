package smaapp;

import kernelsim.*;
import madkit.kernel.*;
//import java.awt.*;
//import java.lang.reflect.Method;
//import java.util.*;

// ***********************************
// *** DriveAgent.java            ***
// *** Creation 23 / 07 / 2003     ***
// *** Jérôme Chapelle@lirmm.fr    ***
// ***********************************

 /** Agent Drive (Motivation) */
public class DriveAgent extends NeuralAgent
{
	static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
	
  public DriveAgent()
    {

    }

public void initialisation(String g,String n, NeuronScheduler nsch)
	{
		simulationGroup=g;
		neuronName=n;
		this.nsch=nsch;
		System.out.println("Agent neurone émotionnel initialisé ... ("+simulationGroup+" "+neuronName+" "+this.nsch+")");
	}


 /*final void die()
   {

   }*/

 public void sayHello()
 {
 	System.out.println("Bonjour je suis "+neuronName+" de "+simulationGroup);
 }
 public void end()
  {
	 leaveRole(simulationGroup,"neuron");
	 leaveGroup(simulationGroup);
  }
     /*
 
  public float getLastValue()
  {	// protected float last_value;
	 return last_value;
  }
 
  public String getLastDebugString()
  {	// protected String last_ds;
	 return last_ds;
  }
  */
  
 public String runMe()
 { 
 	NeuralMessage msg= (NeuralMessage)(nextMessage());	
 	//Message msg = nextMessage();
 	
	while (msg != null)
	{	/*if (((this.getLabel()).compareTo("IPhy"))==0)
		{
			System.out.println("J'ai recu ("+neuronName+")="+msg.toString());
		}
		
		if (((this.getLabel()).compareTo("SatP"))==0)
		{
			System.out.println("J'ai recu ("+neuronName+")="+msg.toString());
		}
		if (((this.getLabel()).compareTo("SatI"))==0)
		{
			System.out.println("J'ai recu ("+neuronName+")="+msg.toString());
		}
		*/
		if (msg.getValue()!=-1)
		{	
			last_value=msg.getValue();
			last_ds=msg.getDebugString();
			//System.out.println("J'ai un message ! ("+neuronName+")="+msg.toString());
		}
		/*
		else if (msg.getDebugString()!=null)
			{ label=msg.getDebugString(); }*/
		 msg= (NeuralMessage)(nextMessage());
	}

	if (type==(NeuronScheduler.TYPE_DRIVE+NeuronScheduler.TYPE_DRIVE_MANAGER))
	{
		int dlim=nsch.getDriveSize();
		int ndrive=0;
		double mdrive=0; // moyenne
		double mindrive=1;
		//int nummindrive=0; // Num du drive le plus faible
		for (int i=0;i<dlim;i++)
		{	NeuronAgent na=nsch.getDrive(i);
			if (na.getType()==NeuronScheduler.TYPE_DRIVE)
			{ ndrive++;
			  mdrive+=na.getLastValue();
			  if (na.getLastValue()<mindrive)
			  { mindrive=na.getLastValue();
			    //nummindrive=i;
			  } // if (nsch.getDrive(i).getLastValue()<mindrive)
			} // if (nsch.getDrive(i).type==nsch.TYPE_DRIVE)
		} // for (int i=0;i<dlim;i++)
		
		
		
		last_value=(mdrive/ndrive);
		
		
		if (inhibit<0) {inhibit=0;}
		
		if ((mindrive<0.5)&&(inhibit==0))
		{	inhibit=200;
			dlim=nsch.getActorLength();
			
			double rnd= Math.random();
			
			if ((rnd>=0)&&(rnd<=0.5))
			{
				// Ici on envoie une val différente à chaque capteur
				for (int i=0;i<dlim;i++)
				{				
					AgentAddress agtAddrTemp=nsch.getActor(i).getAddress();
					sendMessage(agtAddrTemp,new NeuralMessage(Math.random()));
					
				} // for (int i=0;i<dlim;i++)
			}
			else
			{	// Ici on envoie la meme valeur à chaque capteur
				for (int i=0;i<dlim;i++)
				{				
					AgentAddress agtAddrTemp=nsch.getActor(i).getAddress();
					sendMessage(agtAddrTemp,new NeuralMessage(rnd));
					
				} // for (int i=0;i<dlim;i++)
			}
			
			
		} // if (mindrive==0)
		else {inhibit--;}
		
		
		
		
	}
	
	return ("runMe"); // fin de runMe()
 }

}
