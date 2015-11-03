package warbot.BPV_team;

import java.util.Vector;

import madkit.kernel.AgentAddress;
import warbot.kernel.Percept;
import warbot.kernel.WarbotMessage;

public class BPVHomeKiller extends Robot {

	public static final int AIDE_EXPLORER = 1;               // un explorateur voit un explorateur ennemi
	public static final int AIDE_ROCKET = 2;                    // un robot voit un tir ennemi et essaie de trouver d'ou vient le tir
	public static final int AIDE_BASE_ENNEMIE = 3;      // la base ennemie est decouverte
	public static final int AIDE_LAUNCHER = 4;              // un explorateur voit un launcher ennemi
	public static final int AIDE_ATTAQUE = 5;                  // un launcher voit un launcher ennemi
	public static final int AIDE_BASE_AMIE = 6;              // la base voit un launcher ennemi

	private int prioriteType(String type_unite){
		if (type_unite.equals("Home"))
			return 1;
		else if (type_unite.equals("Explorer"))
			return 0;
		else if (type_unite.equals("RocketLauncher"))
			return 2;
		else
			return -1;
	}


	public void activate() {
		super.activate();
		this.randomHeading();
		synchro = new Compteur(2);
		definirRoles();
	}
	
	private void definirRoles() {
		this.createGroup(false,groupName,null,null);
		this.requestRole(groupName,"launcher",null);
		this.requestRole(groupName,"info",null);
		this.requestRole(groupName, "mobile", null);
	}

	public void end() {
		this.broadcast(groupName, "launcher", "FIN");
	}
	
	public void monEtat() {
		if ((ennemi_courant.existe()) && ennemi_courant.getType().equals("RocketLauncher")) {
			String[] args = {String.valueOf(ennemi_courant.getX() - coordonnees.getX()),
									String.valueOf(ennemi_courant.getY() - coordonnees.getY()),
									String.valueOf(ennemi_courant.getEnergy()) };
			this.broadcast(groupName, "launcher", "HELP2", args);
		}
	}

	private double ecartMin(Percept p) {
		if (p.getPerceptType().equals("Obstacle"))
			return 30;
		else if (p.getPerceptType().equals("Rocket"))
			return 30;
		else if (!p.getTeam().equals(this.getTeam()))
			return 70;
		else if (p.getPerceptType().equals("Explorer"))
			return 30;
		else if (aide.besoin() != 0)
			return 100;
		else
			return 70;
	}

	private boolean gene(Point cible, Percept p) {
		int taille = 12;
		if (p.getPerceptType().equals("Home"))
			taille = 20;
		// Distance par rapport au tir
		double angle = this.towards(cible.getX(), cible.getY());
		angle = Math.PI * angle / 180;
		double t = Math.tan(angle);
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		
		double dist_x = (  p.getX() + t* p.getY()) / (c + s * t);
		double dist_y = -p.getY()/c + t * dist_x;
		
		return (Math.abs(dist_y) < taille) && (dist_x > 0) && (dist_x< cible.distanceTo(new Point()));
	}

	private boolean tirer(Point cible_abs) {
		if (! attenteRoquettes.pret())
			return false;
		Point cible = new Point (cible_abs.getX() - coordonnees.getX(), cible_abs.getY() - coordonnees.getY());
		Percept[] percepts = this.getPercepts();
		boolean qqn_devant = false;
		int i = 0;
		int nb_percepts = percepts.length;
		while (!qqn_devant && (i < nb_percepts)) {
			Percept p = percepts[i];
			if (p.getTeam().equals(this.getTeam()))
				qqn_devant = gene(cible, p);
			i ++;
		}
		
		if (! qqn_devant) {
			this.launchRocket(this.towards(cible.getX(), cible.getY()));
			attenteRoquettes.decrementer();
			//setUserMessage(String.valueOf(Math.round(cible.getX())) +" -- " + String.valueOf(Math.round(cible.getY()))); 
			return true;
		}
		else {
			return false;
		}
	}

	private void lireMessages() {
		// Lecture des messages
		while (! this.isMessageBoxEmpty()) {
			WarbotMessage message = this.readMessage();
			if (!message.getSender().equals(this.getAddress())){
				if (message.getAct().equals("INFO-QG")) {
					double x_absolu = message.getFromX() + enDouble(message.getArgN(1)) + coordonnees.getX();
					double y_absolu = message.getFromY() + enDouble(message.getArgN(2)) + coordonnees.getY();
					Point b = new Point(x_absolu, y_absolu);
					if (! baseEnnemieConnue(b))
						bases_ennemies.add(b);
				}
				else if (message.getAct().equals("FIN")) {
					if (aide.getAdresseDemande().equals(message.getSender().getName())) {
						aide.finAssistance();
						this.broadcast(groupName, "launcher", "ETAT");
						this.broadcast(groupName, "explorer", "ETAT");
					}
					if ((offre != null) && offre.equals(message.getSender()))
						offre = null;
				}
				else if (message.getAct().equals("ROCKET-FIN") && offre.equals(message.getSender()))
					offre = null;
				else if (message.getAct().equals("ETAT"))
					monEtat();
				else if (message.getAct().equals("FIN-QG")) {
					double x = message.getFromX() + enDouble(message.getArg1()) + coordonnees.getX();
					double y = message.getFromY() + enDouble(message.getArg2()) + coordonnees.getY();
					Point b = new Point(x,y);
					baseEnnemieSuppr(b);
					aide.finAssistance();
				}
				else if (message.getAct().equals("ROCKET-OFFRE")) {
					if (offre == null)
						offre = message.getSender();
					else if (! offre.equals(message.getSender()))
						this.send(message.getSender(), "ROCKET-NON");
				}
				else {
					String act = message.getAct();
					if (act.equals("HELP0") || act.equals("HELP1") || 
						act.equals("HELP2") || act.equals("HELP3") || act.equals("ROCKET")) {
						double x = coordonnees.getX() + message.getFromX()+ enDouble(message.getArg1());
						double y = coordonnees.getY() + message.getFromY()+ enDouble(message.getArg2());
						int energy = Integer.valueOf(message.getArgN(3)).intValue();
						if (message.getAct().equals("ROCKET"))
							aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_ROCKET,3, tour);
						else if (message.getAct().equals("HELP0")){
							if (bases_ennemies.isEmpty())
								aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_EXPLORER,3, tour);
						}
						else if (message.getAct().equals("HELP1")) {
							aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_LAUNCHER,3, tour);
						}
						else if (message.getAct().equals("HELP2"))
							aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_ATTAQUE,3, tour);
						else if (message.getAct().equals("HELP3"))
							aide.setAssistance( coordonnees, message.getSender().getName(), x, y, energy, AIDE_BASE_AMIE,3, tour);
					}
				}
			}
		}
	}			


	public void  doIt(){	
		boolean attendre = false;

		miseAJourMouvement();
	
		// Time to live decremente
		aide.decrementer();
	
		if ((aide.besoin() != 0) && (coordonnees.distanceTo(aide.getCoord()) <= 90))
			aide.finAssistance();
	
		//Rechargement des roquettes?
		if (! attenteRoquettes.pret())
			attenteRoquettes.decrementer();
	
		lireMessages();
	
		if (synchro.pret())
			monEtat();
		synchro.decrementer();
	
		Point base_cible = new Point();
		if (! bases_ennemies.isEmpty())
			for(int i = 0; i < bases_ennemies.size(); ++i){
				Point b = (Point) bases_ennemies.elementAt(i);
				if (aide.setAssistance(coordonnees, this.getName(), b.getX(), b.getY(), 10000, AIDE_BASE_ENNEMIE,3, tour))
					base_cible = b;
			}
			
		// Deplacement normal

		if (aide.besoin() != 0) {
			double direct = this.towards(aide.getVisee().getX() - coordonnees.getX(), aide.getVisee().getY() -coordonnees.getY());
			if ((coordonnees.distanceTo(aide.getCoord()) > 150) || (aide.besoin() > AIDE_LAUNCHER) || (aide.besoin()  == AIDE_BASE_ENNEMIE)) 
				this.setHeading(direct);
			else
				this.setHeading(180 + direct);
		}

		// BaryCentre des unites ennemies proches
		Point centre = new Point(); 
		Percept[] percepts = this.getPercepts();
		int nb_unites = 0;
	
		boolean ennemi_perdu = ennemi_courant.existe();
		boolean qg_ennemi_visible = ! (base_cible.estConnu() && (base_cible.distanceTo(coordonnees) <= 100));
		for(int cpt = 0; cpt < percepts.length; ++cpt){
			Percept p = percepts[cpt];
			String team = p.getTeam();
			String type_percept = p.getPerceptType();
			if ((type_percept.equals("Home") || type_percept.equals("RocketLauncher") || type_percept.equals("Explorer")) 
			&& !team.equals(this.getTeam())) {
				if (p.getPerceptType().equals("Home")) {
					Point base_abs = new Point(p.getX() +coordonnees.getX(), p.getY() +coordonnees.getY());
					if (! baseEnnemieConnue(base_abs)) {
						bases_ennemies.add(base_abs);
						this.broadcast(groupName, "info", "INFO-QG", String.valueOf(p.getX()), String.valueOf(p.getY()));
					}
					qg_ennemi_visible = qg_ennemi_visible || (base_abs.distanceTo(base_cible) <= 10);
				}
				Point coord = new Point(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY());
				int energie = p.getEnergy();
				if (! ennemi_courant.existe()) {
					ennemi_courant.setCible(type_percept, coord, energie, tour);
					if (type_percept.equals("RocketLauncher")) {
						String[] args = {String.valueOf(p.getX()), String.valueOf(p.getY()), String.valueOf(p.getEnergy())};
						this.broadcast(groupName, "launcher", "HELP2", args);
					}
				}
				else if (ennemi_courant.egal(type_percept, coord)) {
					ennemi_courant.setCible(type_percept, coord, energie, tour);
					ennemi_perdu = false;
				}
				else if ((ennemi_courant.getEnergy() > p.getEnergy()) && (prioriteType(ennemi_courant.getType()) <= prioriteType(p.getPerceptType())))
					ennemi_courant.setCible(type_percept, coord, energie, tour);
					ennemi_perdu = false;
			}
		}	
		if (base_cible.estConnu() &&  !qg_ennemi_visible) {
			baseEnnemieSuppr(base_cible);
			this.broadcast(groupName, "mobile", "FIN-QG", String.valueOf(base_cible.getX() - coordonnees.getX()), String.valueOf(base_cible.getY() - coordonnees.getY()));
		}

		if (ennemi_perdu) {
				this.broadcast(groupName, "launcher", "FIN");
				this.broadcast(groupName, "launcher", "ETAT");
				this.broadcast(groupName, "explorer", "ETAT");
				this.setHeading(this.towards(ennemi_courant.getX() - coordonnees.getX(), ennemi_courant.getY() - coordonnees.getY()));
				ennemi_courant.delCible();
		}
		else if (ennemi_courant.existe()) {
				Point coord = ennemi_courant.viser(coordonnees);
				if (tirer(coord))
					return;
		}

		// Manoeuvres d"evitement
		Point gentils = new Point();
		Point mechants = new Point();
		int nb_mechants = 0;
		int nb_gentils = 0;	
		Point evite = new Point();
		Vector amis = new Vector();
		
		Vector liste_amis = new Vector();
		Vector rockets = new Vector();
		double dist_aide = 1000;
		if (aide.besoin() != 0)
			dist_aide = aide.getVisee().distanceTo(coordonnees);
		for (int i = 0; i < percepts.length; ++i) {
			Percept p = percepts[i];
			double dist = this.distanceTo(p);
			if (p.getPerceptType().equals("RocketLauncher") && p.getTeam().equals(this.getTeam())) {
					liste_amis.add(p);
					Mobile r = new Mobile(coordonnees.getX() + p.getX(), coordonnees.getY() + p.getY());
					amis.add(r);
					if (aide.besoin() == AIDE_LAUNCHER) {
						Point p_ami = new Point(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY());
						double dist_ami = p_ami.distanceTo(aide.getVisee());
						if ((dist_aide > 200) && (dist_ami > dist_aide+20))
							attendre = true;
					}
			}
			else if (p.getPerceptType().equals("Rocket")) {
					rockets.add(p);
			}
			else if (p.getPerceptType().equals("Home") && p.getTeam().equals(this.getTeam())) {
				liste_amis.add(p);
			}
			double ecart_min = ecartMin(p);
			if ((dist > 0) && (dist < ecart_min)) {
				double mult = (ecart_min - dist)/ecart_min;
				if (p.getPerceptType().equals("Obstacle") || p.getTeam().equals(this.getTeam())) {
					gentils.setCoord(gentils.getX() - p.getX()/dist *mult, gentils.getY() - p.getY()/dist*mult);
					nb_gentils ++;
				}
				else {
					nb_mechants ++;
					mechants.setCoord(mechants.getX() - p.getX()/dist*mult, mechants.getY() - p.getY()/dist*mult);
				}
			}
		}	
		
		mon_groupe.amisVus(amis);

		if (attendre)
			this.setHeading(this.getHeading() + 180);


		Rocket rocket_dangereuse = eviteRockets(rockets);
	
		boolean tirer_loin = (aide.besoin() != 0) && (aide.getVisee().distanceTo(coordonnees) < 200);
		// Si on voit une rocket sans voir d"ennemi
		if ((rocket_dangereuse != null) && !tirer_loin) {
			Point viser = rocket_dangereuse.getVisee(coordonnees);
			double d = coordonnees.distanceTo(viser);
			// Estimation de la pos de l"ennemi : a 150 de distance
			double x = (viser.getX() - coordonnees.getX()) / d * 150;
			double y = (viser.getY() - coordonnees.getY())/ d * 150;
			String[] args = {String.valueOf(x), String.valueOf(y), String.valueOf(10000)};
			this.broadcast(groupName, "launcher", "ROCKET", args);
			// Demande a un explorateur de venir voir s"il y a quelqu"un
			if (offre == null)
				this.broadcast(groupName, "explorer", "ROCKET-REQ",String.valueOf(x),String.valueOf(y));
			else
				this.send(offre, "ROCKET-OK", String.valueOf(x), String.valueOf(y));
		}
		if ((rocket_dangereuse != null) && (attenteRoquettes.pret()))
			if (tirer(rocket_dangereuse.getVisee(coordonnees)))
				return;
	
		if (tirer_loin) {
			Point p = new Point(aide.getVisee().getX(), aide.getVisee().getY());
			if (tirer(p))
				return;
		}
		
		// Besoin de construire des rockets?
		int seuil_rocket = 50;
		if (tirer_loin)
			seuil_rocket = 10;
	
		if (this.getRocketNumber() < seuil_rocket) {
			this.buildRocket();
			return;
		}
		if (mon_groupe.nbVus() != 0) {
			Point bary = mon_groupe.barycentre();
			if ( aide.besoin() == 0) {
				if (coordonnees.distanceTo(centre) >60) {
					double direct = this.getHeading()*Math.PI/ 180;
					Point but_virtuel = new Point(30*Math.cos(direct),30*Math.sin(direct));
					this.setHeading(this.towards(but_virtuel.getX() + bary.getX()-coordonnees.getX(),but_virtuel.getY() + bary.getY() - coordonnees.getY() ));
				}
				Point formation = mon_groupe.vecteurMoyen();
				double direct = this.getHeading()*Math.PI/ 180;
				Point but_virtuel = new Point(Math.cos(direct),Math.sin(direct));
				this.setHeading(this.towards(but_virtuel.getX() + formation.getX() ,but_virtuel.getY() + formation.getY()));
			}
		}

		eviteObstacles(liste_amis);

		if (nb_gentils > 0) {
			double direct = this.getHeading()*Math.PI/ 180;
			Point but_virtuel = new Point();
			if (aide.besoin() != 0)
				but_virtuel.setCoord(Math.cos(direct),Math.sin(direct));
			else
				but_virtuel.setCoord(5*Math.cos(direct),5*Math.sin(direct));
			this.setHeading(this.towards(but_virtuel.getX() + gentils.getX(),but_virtuel.getY() + gentils.getY() ));
		}
		if (nb_mechants > 0) {
			double direct = this.getHeading()*Math.PI /180;
			Point but_virtuel = new Point(2*Math.cos(direct),2*Math.sin(direct));
			this.setHeading(this.towards(but_virtuel.getX() + mechants.getX(),but_virtuel.getY() + mechants.getY() ));
		}
	
		this.move();
		

	}

	private Cible ennemi_courant = new Cible();
	private Assistance aide = new Assistance();
	private Compteur attenteRoquettes = new Compteur(4) ;
	private AgentAddress offre = null;
	private Groupe mon_groupe = new Groupe(); 

}
