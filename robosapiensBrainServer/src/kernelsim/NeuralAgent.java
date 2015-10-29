package kernelsim;

import madkit.kernel.*;
import smaapp.*;
//import java.awt.*;
//import java.lang.reflect.Method;
//import java.util.*;
//import madkit.simulation.activators.*;


// ***********************************
// *** NeuralAgent.java            ***
// *** Creation 23 / 11 / 2003     ***
// *** Jérôme Chapelle@lirmm.fr    ***
// ***********************************

 /** Agent neurone */
 public class NeuralAgent extends AbstractAgent //implements ReferenceableAgent
 {
	 public static final String role = "neuron";
	 static final long serialVersionUID = RmcLauncherApp.serialVersionUID; //42L;
    // Basée sur 'RobotAppPhy.java'
     /** Used to know to which simulation this neuron belongs to */
     public String simulationGroup;
     /** Defines the name of the neuron */
     public String neuronName;
     
     /** première valeur reçue */
     protected double first_value=-1;
     
     /** dernière valeur reçue */
     protected double last_value;
     /** dernier message de debug (string)*/
     protected String last_ds=null;
     /** Defines what should be displayed in a NeuronViewer */
     protected String label=null;
     /** Tells which NeuronScheduler schedules this neuron */
     protected NeuronScheduler nsch;
     protected int drivenumber=0;
     /** Different types of neuron are defined in NeuronScheduler class.
     * <ul>
     * <li>final static int TYPE_SENSOR=1;
     * <li>final static int TYPE_ACTOR=2;
     * <li>final static int TYPE_DRIVE=4;
     * <li>final static int TYPE_DRIVE_MANAGER=8;
     * <li>final static int TYPE_NEURON=16;
 	</p>
 	*/
     protected int type = 0;
     
     /** The drive manager uses this variable in order to wait a defined time before trying another action */
     public int inhibit=0;

 public NeuralAgent()
    {

    }

 final void die()
   {

   }

 public void end()
  {
	 leaveRole(Global.Community,simulationGroup,"neuron");
	 leaveGroup(Global.Community,simulationGroup);
  }
  /** Renvoie le label de l'agent */
  public String getLabel()
  {	 return label;  }
  /** Attribue un label à l'agent */
  public void setLabel(String l)
  {	// protected String last_ds;
	 label = l;
  }
  /** Return the type of the neuron */
  public int getType()
  { return type; }
  /** Sets the type of the neuron */
  public void setType(int t)
  { type=t; }

  public double getFirstValue()
  {
      return first_value;
  }
  
 /** Renvoie la dernière valeur reçue */
  public double getLastValue()
  {	// protected double last_value;
	 return last_value;
  }
  /** Modifie la dernière valeur reçue */
  public void setLastValueAsFirstValue()
  {	// protected double last_value;
	 if (first_value==-1) {System.out.println("ERREUR dans NeuralAgent.setLastValueAsFirstValue()");}
	 last_value=first_value;
  }
  /** Renvoie le dernier message de debug (string)*/
  public String getLastDebugString()
  {	// protected String last_ds;
	 return last_ds;
  }
  
 /** Active le neurone */
  public void activate()
  {	System.out.println("Activation NeuralAgent en cours...");
          System.out.print(simulationGroup +"...");
          /*
          if (!isGroup(simulationGroup)) { createGroup(true, simulationGroup, null, null); }
          else { requestRole(simulationGroup, "member", null); }
          */
      	createGroupIfAbsent(Global.Community, simulationGroup, false, null);
        requestRole(Global.Community,simulationGroup, "member", null);
	  //joinGroup(simulationGroup);

          System.out.println("ok ...");
	  requestRole(Global.Community,simulationGroup,role,null);
	  System.out.println("group "+simulationGroup);
	  System.out.println("Neuron activated: "+neuronName);
  }
  
//////////////////////////////////////////////
}
