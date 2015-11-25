package smaapp;

import kernelsim.*;
import madkit.kernel.*;
import madkit.message.StringMessage;


//import java.awt.*;
//import java.lang.reflect.Method;
//import java.util.*;
import java.io.*;
import java.util.Vector;

import robosapiensBrainServer.RobotBrainGlobals;

/*import java.util.*;

 import javax.comm.*;
 import com.sun.comm.Win32Driver;*/

// ***********************************
// *** NeuronAgent.java            ***
// *** Creation 23 / 07 / 2003     ***
// *** Jérôme Chapelle@lirmm.fr    ***
// ***********************************

/**
 * NeuronAgent.java
 * 
 * @author Jérôme Chapelle 2004
 */
public class NeuronAgent extends NeuralAgent {

	static final long serialVersionUID = RobAppSma.serialVersionUID; // 42L;
	public LearnModule Brain = null;
	public Vector<NeuronAgent> Actors, Sensors;
	public Vector<FuzzyFilter> ActorFilters, SensorFilters;
	// public Vector<FuzzyPair> intervales;

	protected int last_action = -1;
	protected int persist = 0;
	protected static final int persistance = 100;
	protected float sat_init;
	protected float sat_moy;
	protected int sat_moy_nb = 0;

	// Spécifique au GE / Drive manager
	private Babbler Babble = null;
	protected int Phase = 0;
	final static int PHASE_BABBLING = 1;
	final static int PHASE_SATISFYING = 2;
	private NeuronAgent natmp = null;

	/** Constructs a new NeuronAgent */
	public NeuronAgent() {
		Phase = PHASE_BABBLING;
	}

	public String itos(int n) {
		return ((new Integer(n)).toString());
	}

	public String ftos(float n) {
		return ((new Float(n)).toString());
	}

	public String dtos(double n) {
		return ((new Float(n)).toString());
	}

	/**
	 * Initialise the NeuronAgent with parameters:
	 * <ul>
	 * <li>String g: the group the NeuronAgent belongs to
	 * <li>String n: the name which is given to the NeuronAgent
	 * <li>NeuronScheduler nsch: the agent that schedules this NeuronAgent
	 * </ul>
	 */
	public void initialisation(String g, String n, NeuronScheduler nsch) {
		simulationGroup = g;
		neuronName = n;
		this.nsch = nsch;
		System.out.println("Agent neurone initialisé ... (" + simulationGroup
				+ " " + neuronName + " " + this.nsch + ")");
	}

	public void setActors(Vector<NeuronAgent> MotorGroup) {
		Actors = MotorGroup;
		// REVIENDRE
		// pour chaque motor, un fuzzyfilter
		ActorFilters = new Vector<FuzzyFilter>();
		for (int i = 0; i < Actors.size(); i++) {
			FuzzyFilter ff = new FuzzyFilter(FuzzyFunc.LINE,
					FuzzyFilter.nb_intervales_def);
			ActorFilters.add(ff);
			System.out.println(ff.toString());
			/*
			 * for (int j=0; j<=5 ; j++) { float value=((float)j)/5; int
			 * interv=ff.appartenance(value); float
			 * ap=ff.appartenance(value,interv); System.out.println(value+
			 * " appartient à l'intervale "+interv+" µ(value)="+ap); }
			 */
		}
		// Puis créer matrice apprentissage
		Brain = new LearnModule(1, (int) Math.pow(
				FuzzyFilter.nb_intervales_def, Actors.size()),/* 1, */2);
		;
		Brain.InitMAct();
		// save / load brain
		if (!RmcLauncherApp.is_applet) {
			String rsrc = (nsch.simgroup + "." + nsch.neurongroup + "."
					+ this.getLabel() + ".mact.txt");
			if (1 == 1) {

				this.loadBrain("s" + rsrc);
				Brain.AfficheMAct();
			} else {
				this.saveBrain(rsrc);
			}

		}
		// c
		/*
		 * // initialisation du driver Win32Driver w32Driver= new Win32Driver();
		 * w32Driver.initialize(); // récupération de l'énumération Enumeration
		 * portList=CommPortIdentifier.getPortIdentifiers(); // affichage des
		 * noms des ports CommPortIdentifier portId; while
		 * (portList.hasMoreElements()){
		 * portId=(CommPortIdentifier)portList.nextElement();
		 * System.out.println(portId.getName()); }
		 */
	}

	public void delibere() {
		if (persist <= 0) {
			persist = persistance;
			if (last_action != -1) {
				// Tire conclusion expérience passée
				// Calcul de la variation de satisfaction (delta)
				float delta = sat_moy - sat_init;
				// Calcul de la récompense qui tient compte du delta et de la
				// valeur initiale de sat
				float reward = (Math.abs(delta) * delta + ((1 - Math.abs(delta)) * sat_moy));// /2;
				// On fixe beta, le coéfficient d'apprentissage, à 0.9
				float beta = (float) 0.9;
				// Calcul classique de la nouvelle valeur en fnct de l'ancienne
				// et de reward:
				// float oldvalue = Brain.GetValue(0,last_action,0);
				// float newv = (beta * oldvalue)+(1-beta)*reward;
				// La même chose en une seule ligne:
				float newv = (beta * Brain.GetValue(0, last_action, 0))
						+ (1 - beta) * reward;
				Brain.SetValue(0, last_action, 0, newv);
				// On augmente le nb de fois où cette action a été choisie:
				// float oldnb=Brain.GetValue(0,last_action,1);
				// float newnb=oldnb+1;
				// Brain.SetValue(0,last_action,1,newnb);
				// La même chose en une seule ligne:
				Brain.SetValue(0, last_action, 1,
						Brain.GetValue(0, last_action, 1) + 1);
				this.saveBrain("s" + nsch.simgroup + "." + nsch.neurongroup
						+ "." + this.getLabel() + ".mact.txt");
			}
			// Choisir nouvelle action
			float poids_max = 0;
			int action_max = 0;
			float poids = 0;
			for (int i = 0; i < Brain.m_action(); i++) {
				poids = (Brain.GetValue(0, i, 0) + 1)
						/ (Brain.GetValue(0, i, 1) + 1);
				if (poids > poids_max) {
					poids_max = poids;
					action_max = i;
				}
			}
			last_action = action_max;
			sat_init = (float) nsch.getDriveByName("SatP").getLastValue();
			sat_moy = sat_init;
			sat_moy_nb = 1;
		} else {
			persist--;
			sat_moy = (sat_moy * ((float) sat_moy_nb) + ((float) nsch
					.getDriveByName("SatP").getLastValue())) / (sat_moy_nb + 1);
			sat_moy_nb++;
		}

		int MotLAct, MotRAct;
		MotRAct = last_action % 3;
		MotLAct = (last_action - MotRAct) / 3;

		FuzzyQuad fq = ActorFilters.elementAt(0).intervale(MotLAct + 1);
		sendMessage(
				Actors.elementAt(0).getAgentAddressIn(Global.Community,
						simulationGroup, role), new NeuralMessage(
						(double) (fq.b.noyau + fq.b_pre_noyau()) / 2));
		fq = ActorFilters.elementAt(1).intervale(MotRAct + 1);
		sendMessage(
				Actors.elementAt(1).getAgentAddressIn(Global.Community,
						simulationGroup, role), new NeuralMessage(
						(double) (fq.b.noyau + fq.b_pre_noyau()) / 2));

		// last_action=action_max;
	}

	public NeuronAgent getActor(int i) {
		if ((i < 0) || (i >= Actors.size())) {
			System.out
					.println("NeuronAgent.getActors(i): indice i hors limites");
			return null;
		}
		return Actors.elementAt(i);
	}

	public void setSensors(Vector<NeuronAgent> SensorGroup) {
		Sensors = SensorGroup;
	}

	/*
	 * final void die() {
	 * 
	 * }
	 */

	/**
	 * Asks the agent to present itself through the debug window
	 * (System.out.println)
	 */
	public void sayHello() {
		System.out.println("Bonjour je suis " + neuronName + " de "
				+ simulationGroup);
	}

	public void end() {
		leaveRole(Global.Community, simulationGroup, role);
		leaveGroup(Global.Community, simulationGroup);
	}

	/*
	 * 
	 * public float getLastValue() { // protected float last_value; return
	 * last_value; }
	 * 
	 * public String getLastDebugString() { // protected String last_ds; return
	 * last_ds; }
	 */
	/** This function is called by the NeuronScheduler */
	public String runMe() // throws ThreadDeath
	{
		// while (true)
		// {
		// Message msg = waitNextMessage();

		// 060208 NeuralMessage msg= (NeuralMessage)(nextMessage());
		Message m = nextMessage();
		//System.out.println("runMe neuron <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		while (m != null) // while (msg != null)
		{
			if (m.getClass() == NeuralMessage.class) {
				NeuralMessage msg = (NeuralMessage) (m);
				if (msg.getValue() != -1) {
					last_value = msg.getValue();
					last_ds = msg.getDebugString();
					if (first_value == -1) {
						first_value = last_value;
					}
				}
				/*
				 * else if (msg.getDebugString()!=null) {
				 * label=msg.getDebugString(); }
				 */
			} // if (m.getClass()==NeuralMessage.class)
			else {
				if (m.getClass() == AckCreatedMessage.class) {
					Phase = nsch.PHASE_SATISFYING;
					natmp = ((AckCreatedMessage) m).getneuronAgent();
				} else // if (m.getClass()==AckCreatedMessage.class)
				{
					if (m.getClass() == StringMessage.class) {
						System.out.println("NeuronAgent (" + this.label
								+ ") a reçu un string message");
					} else // if (m.getClass()==StringMessage.class)
					{
						System.out.println("NeuronAgent (" + this.label
								+ ") a reçu un message de type inconnu ("
								+ m.getClass() + ")");
					} // else // if (m.getClass()==StringMessage.class)
				} // else // if (m.getClass()==AckCreatedMessage.class)
			}
			m = nextMessage(); // msg= (NeuralMessage)(nextMessage());
		} // while (msg != null)

		if (type == (NeuronScheduler.TYPE_DRIVE + NeuronScheduler.TYPE_DRIVE_MANAGER)) {
			runMeDriveMan();
		} else if (type == NeuronScheduler.TYPE_DRIVE) {
			runMeDrive();
		}

		return ("runMe"); // fin de runMe()
	}

	/** Function called by the RunMe() if the neuron it applied to is a Drive */
	public String runMeDrive() {
		if (Brain == null) { // 5 => Pas, Peu, Moyen, Beaucoup, Tout
			Brain = new LearnModule(nsch.getSensorSize() * 5,
					nsch.getActorSize() * 5,/* 1, */2);
			Brain.InitMAct();
			Brain.setLabel(this.getLabel());
			// Brain.AfficheMAct();
		}
		if (this.getLabel() == "Faim") { // REVIENDRE
			if (this.getLastValue() == 0.5039370078593493) {
				System.out.println("Value Faim=" + this.getLastValue());
			}
		}
		// System.out.println("Hello je suis le Drive: "+this.neuronName+" "+this.label);
		return ("runMeDrive");

	}

	/**
	 * Function called by the RunMe() if the neuron it applied to is a
	 * DriveManager
	 */
	public String runMeDriveMan() {
		//System.out.println("rumMeDriveMAn");
		if (drivenumber == 0) {
			drivenumber = nsch.getDriveSize();
		}
		if (Brain == null) {
			Brain = new LearnModule(drivenumber, drivenumber,/* 1, */2);
		}

		int ndrive = 0;
		double mdrive = 0; // moyenne
		double mindrive = 1;
		int nummindrive = 0; // Num du drive le plus faible

		// Récupération du drive le + faible et de la moyenne
		NeuronAgent nagtmp;
		for (int i = 0; i < drivenumber; i++) {
			nagtmp = nsch.getDrive(i);
			if (nagtmp.type == NeuronScheduler.TYPE_DRIVE) {
				ndrive++;
				mdrive += nagtmp.getLastValue();
				if (nagtmp.getLastValue() < mindrive) {
					mindrive = nagtmp.getLastValue();
					nummindrive = i;
				} // if (nsch.getDrive(i).getLastValue()<mindrive)
			} // if (nsch.getDrive(i).type==nsch.TYPE_DRIVE)
		} // for (int i=0;i<dlim;i++)
		last_value = (mdrive / ndrive);

		// Intégration début
		// ----------------------------------------------------------------
		if (Phase == PHASE_BABBLING) {
			if (Babble == null) {
				if (nsch.getActorSize() > 0) {
					Babble = new Babbler(nsch);
				}
			} else // if (Babble==null)
			{
				Babble.execute2();
				if (Babble.Babbler_State == Babble.BABBLER_WORKING) {
					NeuronAgent atemp = Babble.currentMotor;
					double vtemp = Babble.ValueToTry; // (double)0.5; //

					sendMessage(atemp.getAgentAddressIn(Global.Community,
							simulationGroup, role), new NeuralMessage(vtemp));

				} else // if(Babble.Babbler_State==Babble.BABBLER_WORKING)
				{
					if (Babble.Babbler_State == Babble.BABBLER_END) {
						if (natmp == null) {
							System.out.println("Nsch:babbling terminé");
							String s = "";
							for (int i = 0; i < Babble.MotorGroup.size(); i++)
								s += (((NeuronAgent) Babble.MotorGroup.get(i))
										.getLabel());
							AgentAddress a = nsch.getAgentAddressIn(
									RobotBrainGlobals.community, 
									RobotBrainGlobals.BrainGroup,
									RobotBrainGlobals.nschRole);
							System.out.println(a);
							sendMessage(a, new CreateAgentMessage(
									nsch.neurongroup, s, Babble.MotorGroup));
							// Phase=PHASE_SATISFYING;
							// Babble.Babbler_State=Babble.BABBLER_ZOMBIE;
						}
					} // if (Babble.Babbler_State==Babble.BABBLER_END)
				} // else //if(Babble.Babbler_State==Babble.BABBLER_WORKING)

			} // else // if (Babble==null)

			// println("Neuron Scheduler agent living");
			// 040708 update();
		} // if (Phase==PHASE_BABBLING)
		if (Phase == PHASE_SATISFYING) {
			natmp.delibere();
		}

		// Intégration fin
		// ------------------------------------------------------------------
		// System.out.println("Hello je suis le DriveManager: "+this.neuronName+" "+this.label);
		// System.out.println("Le Drive le plus faible est: "+nsch.getDrive(nummindrive).neuronName+" "+nsch.getDrive(nummindrive).label);

		return ("runMeDrvMan");

	}

	public void load(BufferedReader dip) {
		try {
			String s = null;
			String dbug = new String("NA.load:");
			s = dip.readLine();
			System.out.println(dbug + s);
			simulationGroup = s;
			s = dip.readLine();
			System.out.println(dbug + s);
			neuronName = s;
			s = dip.readLine();
			System.out.println(dbug + s);
			last_value = Float.valueOf(s).floatValue();
			s = dip.readLine();
			System.out.println(dbug + s);
			if (s.compareTo("null") == 0) {
				last_ds = null;
			} else {
				last_ds = s;
			}
			s = dip.readLine();
			System.out.println(dbug + s);
			if (s.compareTo("null") == 0) {
				label = null;
			} else {
				label = s;
			}
			s = dip.readLine();
			System.out.println(dbug + s);
			type = (Integer.valueOf(s)).intValue();
			s = dip.readLine();
			System.out.println(dbug + s);
			inhibit = (Integer.valueOf(s)).intValue();
			Brain = new LearnModule();
			Brain.load(dip);
		} catch (Exception eofe) {
			System.err.println("NA.load:" + eofe.getMessage());
			eofe.printStackTrace();
		}
	}

	public void save(FileWriter dip) {
		try {
			dip.write(simulationGroup);
			dip.write("\n");
			dip.write(neuronName);
			dip.write("\n");
			dip.write(dtos(last_value));
			dip.write("\n");
			if (last_ds == null) {
				last_ds = new String("null");
			}
			dip.write(last_ds);
			dip.write("\n");
			if (label == null) {
				label = new String("null");
			}
			dip.write(label);
			dip.write("\n");
			// dip.write(nsch);dip.write("\n");
			dip.write(itos(type));
			dip.write("\n");
			dip.write(itos(inhibit));
			dip.write("\n");
			// dip.write(inhibit);dip.write("\n");
			Brain.save(dip);
		} catch (Exception e) { // RmcLauncherApp.is_applet=true;
			e.printStackTrace();
		}
	}

	/*
	 * public void save(FileWriter dip) { try {
	 * dip.write(simulationGroup);dip.write("\n");
	 * dip.write(neuronName);dip.write("\n"); dip.write((new
	 * Double(last_value)).toString());dip.write("\n"); if (last_ds==null)
	 * {last_ds=new String("null");} dip.write(last_ds);dip.write("\n"); if
	 * (label==null) {label=new String("null");}
	 * dip.write(label);dip.write("\n"); //dip.write(nsch);dip.write("\n");
	 * dip.write((new Integer(type)).toString());dip.write("\n"); dip.write((new
	 * Integer(inhibit)).toString());dip.write("\n");
	 * //dip.write(inhibit);dip.write("\n"); Brain.save(dip); } catch (Exception
	 * e) { //RmcLauncherApp.is_applet=true; e.printStackTrace(); } }
	 */
	public void loadBrain(String filename) {
		try {
			String rsrc = filename; // (nsch.simgroup+"."+nsch.neurongroup+"."+this.getLabel()+".mact.txt");
			/*
			 * InputStream defs=null; defs =
			 * this.getClass().getResourceAsStream(rsrc);
			 */
			BufferedReader dip = new BufferedReader(new FileReader(rsrc));
			Brain.load(dip);
		} catch (Exception eofe) {
			System.err.println("Load:" + eofe.getMessage());
			eofe.printStackTrace();
		}
		Brain.AfficheMAct();
	}

	public void saveBrain(String filename) {
		try {
			FileWriter dip;
			dip = new FileWriter(filename);
			Brain.save(dip);
			dip.close();
			System.out.println("Brain.neuronagent saved!");
		} catch (Exception e) {
			RmcLauncherApp.is_applet = true;
			e.printStackTrace();
		}
	}
	// ////////////////////////////////////////////
}
