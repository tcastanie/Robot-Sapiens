package smaapp;

import madkit.kernel.*;
import madkit.message.StringMessage;
import madkit.simulation.activator.*;

//import java.awt.*;
import java.io.*;
import java.util.*;

import robosapiensBrainServer.RobotBrainGlobals;
//import smaapp.*;
import kernelsim.*;

// ***********************************
// *** NeuronScheduler.java        ***
// *** Creation 23 / 07 / 2003     ***
// *** Jérôme Chapelle@lirmm.fr    ***
// ***********************************

/**
 * A NeuronScheduler is used to schedule the NeuralAgents.
 * 
 * @author Jérôme Chapelle 2004
 */
public class NeuronScheduler extends Scheduler {
	public static final String role = "scheduler";
	static final long serialVersionUID = RobAppSma.serialVersionUID; // 42L;
	String simgroup, neurongroup;
	// RobotAppEnv world;
	TurboMethodActivator neurlive, sensorObserver;

	public int NeuronAgentNb;
	// StringMessage lastmessage=null;
	// public boolean messageread=false;

	Vector<NeuronAgent> Sensors = new Vector<NeuronAgent>();
	Vector<NeuronAgent> Actors = new Vector<NeuronAgent>();
	Vector<NeuronAgent> Drives = new Vector<NeuronAgent>();
	Vector<NeuronAgent> DriveManagers = new Vector<NeuronAgent>();

	Vector<NeuronAgent> Neurons = new Vector<NeuronAgent>();

	Babbler Babble = null; // suppr

	final static int TYPE_SENSOR = 1;
	final static int TYPE_ACTOR = 2;
	final static int TYPE_DRIVE = 4;
	final static int TYPE_DRIVE_MANAGER = 8;

	final static int TYPE_NEURON = 16;

	protected int Phase = 0; // suppr3
	final static int PHASE_BABBLING = 1;
	final static int PHASE_SATISFYING = 2;

	final static int PLAY = 1;
	final static int PAUSE = 0;
	protected int PlayState = PAUSE;

	/**
	 * Crée un nouveau NeuronScheduler dans la simulation simgroup pour le
	 * groupe de neurones neurongroup
	 **/
	public NeuronScheduler(String simgroup, String neurongroup) // ,RobotAppEnv
																// w)
	{
		// world=w;
		this.simgroup = simgroup;
		this.neurongroup = neurongroup;
		// setDebug(true);
		Phase = PHASE_BABBLING; // suppr
	}

	public void activate() {
		System.out.println("Activation neuroscheduler dans groupe " + simgroup
				+ " & " + neurongroup);
		/*
		neurlive = new TurboMethodActivator("runMe", Global.Community,
				neurongroup, "neuron");
		addActivator(neurlive);

		sensorObserver = new TurboMethodActivator("observe", Global.Community,
				neurongroup, "sensor observer");
		addActivator(sensorObserver);
*/
		// println("Neuron scheduler activated");
		createGroupIfAbsent(Global.Community, simgroup, false, null);
		createGroupIfAbsent(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup,  false, null);
		/*
		 * if (!isGroup(simgroup)) { createGroup(true, simgroup, null, null); }
		 * else { requestRole(simgroup, "member", null); }
		 */
		requestRole(RobotBrainGlobals.community, RobotBrainGlobals.BrainGroup, RobotBrainGlobals.nschRole, null);
		requestRole(Global.Community, simgroup, role, null);
		createGroupIfAbsent(Global.Community, neurongroup, false, null);
		requestRole(Global.Community, neurongroup, "member", null);

		/*
		 * if (!isGroup(neurongroup)) { createGroup(true, neurongroup, null,
		 * null); } else { requestRole(neurongroup, "member", null); }
		 */
	}

	public void live() // throws ThreadDeath
	{
		Message m = null;
		StringMessage lastmessage = null;
		// NeuronAgent natmp=null; //suppr

		while (true) {
			m = nextMessage();

			while (m != null) {
				System.out.println("Nsched:J'ai reçu un message de type: "
						+ m.getClass().toString());
				if (m.getClass() == CreateAgentMessage.class) {
					System.out.println("C'était bien une demande de création ("
							+ m + ")");
					CreateAgentMessage mc = (CreateAgentMessage) m;
					NeuronAgent na = createNeuronAgent(mc.getneuronGroup());

					registerNeuron(na);

					na.setLabel(mc.getlabel());

					na.setActors(mc.getMotorGroup()); // REVIENDRE
					// natmp=na; SendMessage to DriveMan
					System.out.println("Nsch:neurone " + mc.getlabel()
							+ " activé");
					sendMessage(m.getSender(), new AckCreatedMessage(na));
					// Babble.Babbler_State = Babble.BABBLER_RENEW;
				} else {
					lastmessage = (StringMessage) m;
					boolean messageread = false;
					if ((!messageread)
							&& (lastmessage.getContent().compareTo("Pause") == 0)) {
						System.out.println("NSched: Going to pause ...");
						PlayState = PAUSE;

					}
					if ((!messageread)
							&& (lastmessage.getContent().compareTo("Play") == 0)) {
						System.out.println("NSched: Going to play ...");
						PlayState = PLAY;
					}
					if (!messageread) {
						System.out.println("NSched:received:"
								+ lastmessage.getContent());
					}

				}
				if (PlayState == PAUSE) {
					// pause(10);
					m = waitNextMessage();
				} else {
					m = nextMessage();
				}
			}

			if (PlayState == PLAY) {
				pause(10); // 050602
				//System.out.println("nsch - play");
				for(int i = 0 ; i < DriveManagers.size(); i ++)
					DriveManagers.get(i).runMe();
				for(int i = 0 ; i < Drives.size(); i ++)
					Drives.get(i).runMe();
				for(int i = 0 ; i < Sensors.size(); i ++)
					Sensors.get(i).runMe();
				for(int i = 0 ; i < Actors.size(); i ++)
					Actors.get(i).runMe();
				
				//neurlive.execute(); // runMe des neurones
				//sensorObserver.execute(); // observe des viewers

			} // if (PlayState==PLAY)
			else { // On ne passera ici qu'au début de la simulation, avant que
					// l'on
					// ai pressé la première fois sur play/pause ... Cette pause
					// permet
					// d'éviter que le simulateur utilise 100% du CPU à boucler
					// dans le
					// while ... Avec la pause l'affichage de l'environement est
					// instantané, sans la pause: compter entre 5 et 20 sec ...
				pause(10);
			}
		} // while (true)
	} // public void live()

	// -Sensors-------------------- Les capteurs --------------------------
	/* Enregistre l'agent na comme étant un Sensor (capteur) */
	public int registerSensor(NeuronAgent na) {
		System.out.println("(NSched):" + na + " registered as Sensor");
		na.requestRole(neurongroup, "sensor", null);
		na.setType(TYPE_SENSOR);
		Sensors.add(na);
		return Sensors.indexOf(na);

	}

	/**
	 * Renvoie le nombre de Sensor enregistrés (identique à getSensorLength())
	 */
	public int getSensorSize() {
		return Sensors.size();
	}

	/** Récupère le i ieme Sensor (capteur) */
	public NeuronAgent getSensor(int i) {
		return ((NeuronAgent) (getSensorActor(Sensors, i, TYPE_SENSOR)));
	}

	/** Récupère le Sensor (capteur) qui porte le nom name */
	public NeuronAgent getSensorByName(String name) {
		return ((NeuronAgent) (getSensorActorByName(Sensors, name, TYPE_SENSOR)));
	}

	/**
	 * Renvoie le nombre de Sensor enregistrés (identique à getSensorSize())
	 */
	public int getSensorLength() {
		return Sensors.size();
	}

	public int registerNeuron(NeuronAgent neuronagent) {
		System.out
				.println((new StringBuilder()).append("(NSched):")
						.append(neuronagent).append(" registered as Neuron")
						.toString());
		neuronagent.requestRole(neurongroup, "Neuron", null);
		neuronagent.setType(TYPE_NEURON);
		Neurons.add(neuronagent);
		return Neurons.indexOf(neuronagent);
	}

	public int getNeuronSize() {
		return Neurons.size();
	}

	public NeuronAgent getNeuron(int i) {
		return getSensorActor(Neurons, i, 16);
	}

	public NeuronAgent getNeuronByName(String s) {
		return getSensorActorByName(Neurons, s, 16);
	}

	public int getNeuronLength() {
		return Neurons.size();
	}

	// -Actors-------------------- Les moteurs --------------------------
	/** Enregistre l'agent na comme étant un Actor (actionneur) */
	public int registerActor(NeuronAgent na) {
		System.out.println("(NSched):" + na + " registered as Actor");
		na.requestRole(neurongroup, "actor", null);
		na.setType(TYPE_ACTOR);
		Actors.add(na);
		return Actors.indexOf(na);
	}

	/**
	 * Renvoie le nombre d' Actor enregistrés (identique à getActorLength())
	 */
	public int getActorSize() {
		return Actors.size();
	}

	/** Récupère le i ieme Actor (actionneur) */
	public NeuronAgent getActor(int i) {
		return ((NeuronAgent) (getSensorActor(Actors, i, TYPE_ACTOR)));
	}

	/** Récupère l' Actor (actionneur) qui porte le nom name */
	public NeuronAgent getActorByName(String name) {
		return ((NeuronAgent) (getSensorActorByName(Actors, name, TYPE_ACTOR)));
	}

	/**
	 * Renvoie le nombre d' Actor enregistrés (identique à getActorSize())
	 */
	public int getActorLength() {
		return Actors.size();
	}

	// -Drives-------------------- Les motivations --------------------------
	/** Enregistre l'agent na comme étant un Drive (une motivation) */
	public int registerDrive(NeuronAgent na) {
		System.out.println("(NSched):" + na + " registered as Drive");
		na.requestRole(neurongroup, "drive", null);
		na.setType(TYPE_DRIVE);
		Drives.add(na);
		return Drives.indexOf(na);
	}

	/** Renvoie le nombre de Drive enregistrés */
	public int getDriveSize() {
		return Drives.size();
	}

	/** Récupère le i ieme Drive (une motivation) */
	public NeuronAgent getDrive(int i) {
		return ((NeuronAgent) (getSensorActor(Drives, i, TYPE_DRIVE)));
	}

	/** Récupère le Drive (la motivation) qui porte le nom name */
	public NeuronAgent getDriveByName(String name) {
		return ((NeuronAgent) (getSensorActorByName(Drives, name, TYPE_DRIVE)));
	}

	/**
	 * Enregistre l'agent na comme étant un DriveManager (gestionnaire
	 * d'émotions)
	 */
	public int registerDriveManager(NeuronAgent na) {
		System.out.println("(NSched):" + na + " registered as DriveManager");
		na.requestRole(neurongroup, "drive", null);
		na.requestRole(neurongroup, "drvman", null);
		na.setType(TYPE_DRIVE + TYPE_DRIVE_MANAGER);
		Drives.add(na);
		DriveManagers.add(na);
		return DriveManagers.indexOf(na);
	}

	/** Récupère le i ieme DriveManager (gestionnaire d'émotions) */
	public NeuronAgent getDriveManager(int i) {
		return ((NeuronAgent) (getSensorActor(DriveManagers, i, TYPE_DRIVE
				+ TYPE_DRIVE_MANAGER)));
	}

	/**
	 * Récupère le i ieme Agent de type TYPE dans le vecteur v (fonction
	 * utilisée par getSensor(...) getActor(...) getDrive(...)
	 * getDriveManager(...)
	 */
	private NeuronAgent getSensorActor(Vector v, int i, int TYPE) {
		if (i >= v.size()) {
			String dbmsg = "";
			if (TYPE == TYPE_SENSOR) {
				dbmsg = "Sensor";
			}
			if (TYPE == TYPE_ACTOR) {
				dbmsg = "Actor";
			}
			if (TYPE == TYPE_DRIVE) {
				dbmsg = "Drive";
			}
			if (TYPE == TYPE_DRIVE_MANAGER) {
				dbmsg = "DriveManager";
			}
			if (TYPE == (TYPE_DRIVE + TYPE_DRIVE_MANAGER)) {
				dbmsg = "Drive+DriveManager";
			}
			System.out.println("Indice hors limites (NSched): get" + dbmsg
					+ "(" + i + ") -(max=" + (v.size() - 1) + ")");
			return null;
		}

		return ((NeuronAgent) (v.elementAt(i)));
	}

	/**
	 * Récupère l'agent portant le nom name, de type TYPE,dans le vecteur v
	 * (fonction utilisée par getSensorByName(...) getActorByName(...)
	 * getDriveByName(...) getDriveManagerByName(...)
	 */
	private NeuronAgent getSensorActorByName(Vector v, String name, int TYPE) {
		int vtemp;
		// String stemp;
		int n = -1;
		for (int i = 0; i < v.size(); i++) { /*
											 * stemp =
											 * ((NeuronAgent)(v.elementAt
											 * (i))).getLabel();
											 * vtemp=(stemp.compareTo (name));
											 */
			vtemp = ((((NeuronAgent) (v.elementAt(i))).getLabel())
					.compareTo(name));

			if (vtemp == 0) { // System.out.println("Neurone: (i="+i+")(name="+stemp+") (compareTo="+vtemp+")(name="+name+")");
				n = i;
				break;
			}
		}

		if (n >= 0) { // System.out.println("Agent Existant (NSched): nom ("+name+") non trouvé (n="+n+")");
			return ((NeuronAgent) (getSensorActor(v, n, TYPE)));
		}
		/*
		 * else {
		 */
		System.out.println("Agent Inexistant (NSched): nom (" + name
				+ ") non trouvé (n=" + n + ")");
		return null;
		// }
	}

	public void killAgents() {
		killAgents(Sensors);
		killAgents(Actors);
		killAgents(Drives);
		killAgents(DriveManagers);
		killAgents(Neurons);
	}

	public void killAgents(Vector v) {
		if (v != null) {
			if (v.size() > 0) {
				NeuronAgent na;
				for (int i = 0; i < v.size(); i++) {
					na = ((NeuronAgent) (v.elementAt(i)));
					System.out.println("Killing " + na.getLabel() + "("
							+ na.neuronName + ")");
					na.end();
					killAgent(na);
				} // for (int i=0;i<v.size();i++)
			} // if (v.size()>0)
		} // if (v!=null)
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

	public void LoadBrain(BufferedReader dip) // String name)
	{
		System.out.println("NSch Loading brain ..."); // for "+name);
		try {
			int nbsave = 0;
			String s = null;
			s = dip.readLine(); // System.out.println(s);
			nbsave = Integer.valueOf(s).intValue();
			for (int i = 0; i < nbsave; i++) {
				System.out.println("Loading " + (i + 1) + "/" + nbsave
						+ " neuron...");

				NeuronAgent na = createNeuronAgent(neurongroup);
				na.load(dip);
				na.Brain.afficheMact();
			}

		} catch (Exception eofe) {
			System.err.println("NSch.LoadBrain:" + eofe.getMessage());
			eofe.printStackTrace();
		}
	}

	public void SaveBrain(FileWriter dip) // (String name)
	{
		String dbug = new String("NSch.SaveBrain:");
		System.out.println(dbug + "Saving brain ..."); // for "+name);
		NeuronAgent na = null;

		int nbsave = Neurons.size() + 1;
		try {
			dip.write(itos(nbsave) + "\n");
		} catch (Exception e) {
			System.err.println(dbug + "" + e.getMessage());
		}

		for (int i = 0; i < Neurons.size(); i++) {
			na = ((NeuronAgent) (Neurons.elementAt(i)));
			System.out.println(dbug + "Saving " + na.getLabel() + "("
					+ na.neuronName + ")");
			na.save(dip);
		}

		na = ((NeuronAgent) Drives.elementAt(0));
		System.out.println(dbug + "Saving " + na.getLabel() + "("
				+ na.neuronName + ")");
		na.save(dip);

	}

	protected NeuronAgent createNeuronAgent(String NeuronGroup) {

		NeuronAgent na;
		NeuronAgentNb++;
		// NeuronGroup= "NeuronGroup"+N_agent;
		String NeuronName = "Neurone" + NeuronAgentNb;
		na = new NeuronAgent();

		na.initialisation(NeuronGroup, NeuronName, this);

		launchAgent(na, false);
		return na;
	}

	public void end() {
		killAgents();
		// leaveRole(simulationGroup,"neuron");
		// leaveGroup(simulationGroup);
	}

}