
package warbot.BPV_team;

import java.util.Vector;

import madkit.kernel.AgentAddress;
import warbot.kernel.Percept;
import warbot.kernel.WarbotMessage;

public class BPVExplorer extends Robot {

	public void activate() {
		super.activate();
		synchro = new Compteur(5);
		
		this.randomHeading();
		num = (this.getAgentsWithRole(groupName, "explorer")).length;
		this.broadcast(groupName, "explorer", "NUM", String.valueOf(num));
		definirRoles();
	}
	
	public void  definirRoles() {
		this.createGroup(false,groupName,null,null);
		this.requestRole(groupName,"explorer",null);
		this.requestRole(groupName, "info", null);
		this.requestRole(groupName,"mobile",null);
	}
	
	public void end(){
		this.broadcast(groupName, "explorer", "FIN-NUM", String.valueOf(num));
		this.broadcast(groupName, "launcher", "FIN");
	}

	public void monEtat(){
		for(int i = 0; i < cibles_observees.size(); ++i) {
			CibleObs c = (CibleObs)cibles_observees.elementAt(i);
			if (c.getVuParAutre() == 0) {
				String[] args = {String.valueOf(c.getX() - coordonnees.getX()), String.valueOf(c.getY() - coordonnees.getY()), String.valueOf(c.getEnergy()) };
				if (c.getType().equals("RocketLauncher")) {
					this.broadcast(groupName, "mobile", "HELP1", args);
				}
				else if (c.getType().equals("Explorer")) {
					this.broadcast(groupName, "mobile", "HELP0", args);
				}
			}
		}
	}

	private double ecartMin(Percept p) {
		if (p.getPerceptType().equals("Obstacle"))
			return 20;
		else if (p.getPerceptType().equals("Rocket")) 
			return 30;
		else if (! p.getTeam().equals(this.getTeam())) {
			if (p.getPerceptType().equals("RocketLauncher"))
				return 120;
			else
				return 5;
		}
		else if (p.getPerceptType().equals("Explorer"))
			return 154;
		else if (! cibles_observees.isEmpty())
			return 12;
		else
			return 0;
	}
	
	private boolean estMonSuperieur(AgentAddress adr){
		return liste_autres.contains(adr);
	}		
	
	
	private void gestionMessages() {
		// Reception des messages
		while (! this.isMessageBoxEmpty()) {
			WarbotMessage message = this.readMessage();
			if (message.getAct().equals("INFO-QG")) {
				double x_absolu = message.getFromX() + enDouble(message.getArgN(1)) + coordonnees.getX();
				double y_absolu = message.getFromY() + enDouble(message.getArgN(2)) + coordonnees.getY();
				Point b = new Point(x_absolu, y_absolu);
				if (! baseEnnemieConnue(b))
					bases_ennemies.add(b);
			}
			else if (message.getAct().equals("FIN-NUM")) {
					if (Integer.valueOf(message.getArg1()).intValue() < num)
						num--;
			}
			else if (message.getAct().equals("NUM")) {
					liste_autres.add(message.getSender());
			}
			else if  (message.getAct().equals("FIN-QG")) {
					double x = message.getFromX() + enDouble(message.getArg1()) + coordonnees.getX();
					double y = message.getFromY() + enDouble(message.getArg2()) + coordonnees.getY();
					Point b = new Point(x,y);
					baseEnnemieSuppr(b);
			}
			else if (message.getAct().equals("LAUNCHER_VU")) {
				launcher_vu = true;
			}
			else if (message.getAct().equals("ROCKET-REQ")) {
				if ((adr_objectif == null) && (! cible_launcher)) {
					this.send(message.getSender(), "ROCKET-OFFRE");
					adr_objectif = message.getSender();
					Point dest = new Point(message.getFromX() + coordonnees.getX() + enDouble(message.getArg1()), 
													   message.getFromY() + coordonnees.getY() + enDouble(message.getArg2()));
					objectif.setCoord (dest.getX(), dest.getY());
				}
			}
			else if ((message.getAct().equals("ROCKET-OK")) && ((adr_objectif != null) && adr_objectif.equals(message.getSender()))) {
					Point dest = new Point(message.getFromX() + coordonnees.getX() + enDouble(message.getArg1()), 
														message.getFromY() + coordonnees.getY() + enDouble(message.getArg2()));
					objectif.setCoord (dest.getX(), dest.getY());
			}
			else if (message.getAct().equals("ROCKET-NON")) {
					if (adr_objectif.equals(message.getSender()))
						adr_objectif = null;
			}
			else if (estMonSuperieur(message.getSender()) && (message.getAct().equals("HELP0") || message.getAct().equals("HELP1"))) {
					double x = message.getFromX()+ enDouble(message.getArg1()) + coordonnees.getX();
					double y = message.getFromY()+ enDouble(message.getArg2()) + coordonnees.getY();
					Point pos = new Point(x, y);
					for (int i = 0; i < cibles_observees.size(); ++i) {
						CibleObs c = (CibleObs) cibles_observees.elementAt(i);
						if (c.distanceTo(pos) <= 10) 
							c.setVuParAutre(6); // 6 tours avant fin ttl
					}
			}
		}
	}
	
	
	public void doIt() {

		miseAJourMouvement();
		
		Percept[] percepts = this.getPercepts();
	
		Vector liste_obs = new Vector();
		Vector rockets = new Vector();
		
		// Detection des ennemis
		boolean zero_cible = cibles_observees.isEmpty();
		Vector nouvelles_cibles = new Vector();
		cible_launcher = false;
		
		for (int i = 0; i < percepts.length; ++i) {
			Percept p = percepts[i];
			// On trouve le QG ennemi
			if (p.getPerceptType().equals("Home")) {
				liste_obs.add(p);
				if (! p.getTeam().equals(this.getTeam())) {
					Point b = new Point(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY());
					if (! baseEnnemieConnue(b)) {
						bases_ennemies.add(b);
						this.broadcast(groupName, "info", "INFO-QG", String.valueOf(p.getX()), String.valueOf(p.getY()));
					}
				}
			}
			// On entre dans la zone de detection d"un rocket-launcher
			else if (p.getPerceptType().equals("RocketLauncher") && !p.getTeam().equals(this.getTeam())) {
				if (! launcher_vu) {
					this.broadcast(groupName, "explorer", "LAUNCHER_VU");
				}
				launcher_vu = true;
				cible_launcher = true;
				CibleObs c = new CibleObs();
				Point pt = new Point(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY());
				c.setCible(p.getPerceptType(), pt, p.getEnergy(), tour);
				nouvelles_cibles.add(c);
			}
			else if (p.getPerceptType().equals("Explorer") &&  !p.getTeam().equals(this.getTeam())) {
				CibleObs c = new CibleObs();
				Point pt = new Point(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY());
				c.setCible(p.getPerceptType(), pt, p.getEnergy(), tour);
				nouvelles_cibles.add(c);
				liste_obs.add(p);
			}
			else if (p.getPerceptType().equals("RocketLauncher") && (p.getTeam().equals(this.getTeam())))
				liste_obs.add(p);
			else if (p.getPerceptType().equals("Rocket")) {
				rockets.add(p);
			}
		}
		if (adr_objectif != null) {
			if (coordonnees.distanceTo(objectif) < 110) {
				adr_objectif = null;
				objectif.setConnu(false);
				this.send(adr_objectif, "ROCKET-FIN");
			}
			else
				this.setHeading( this.towards(objectif.getX() - coordonnees.getX(), objectif.getY() - coordonnees.getY()));
		}
		
		//		Mise a jour des cibles vues par plusieurs explorateurs
		int nb_vus_par_autre = 0;
		int longueur = cibles_observees.size();
		for (int i = 0; i < nouvelles_cibles.size(); ++i){
			CibleObs c = (CibleObs) nouvelles_cibles.elementAt(i);
			Point pt = new Point(c.getX(), c.getY());
			boolean trouve = false;
			int j = 0;
			while (! trouve && j < longueur) {
				CibleObs c_anc = (CibleObs) cibles_observees.elementAt(j);
				if ((c_anc.getVuParAutre() != 0) && (c_anc.distanceTo(pt) < 10)) { 
					trouve = true;
					c.setVuParAutre(c_anc.getVuParAutre() - 1);
					nb_vus_par_autre ++;
				}
				j++;
			}
		}
		
		cibles_observees = nouvelles_cibles;
		
		int nb_ennemis = 0;
		Point centre = new Point(0,0);
		int nb_vus_seulement_par_moi = 0;
		for (int i = 0; i < cibles_observees.size(); ++i) {
			CibleObs c = (CibleObs) cibles_observees.elementAt(i);
			if ((nb_vus_seulement_par_moi < 2) && c.getType().equals("RocketLauncher") && (c.getVuParAutre() == 0)) {
				double mult = 130 - c.distanceTo(coordonnees);
				centre.setCoord(centre.getX() + (c.getX() - coordonnees.getX())*mult, centre.getY() +  (c.getY() - coordonnees.getY())*mult);
				nb_ennemis++;
				nb_vus_seulement_par_moi++;
			}
			else if (c.getType().equals("RocketLauncher")) {
				double	mult =  300 - c.distanceTo(coordonnees);
				centre.setCoord(centre.getX() + (c.getX() - coordonnees.getX())*mult, centre.getY() +  (c.getY() - coordonnees.getY())*mult);
				nb_ennemis ++;
			}
	
			
		}
		setUserMessage(String.valueOf(nb_vus_seulement_par_moi));
		if (nb_ennemis > 0) {
			centre.setCoord(centre.getX()/nb_ennemis, centre.getY()/ nb_ennemis);
			double repuls = 180;
			this.setHeading(repuls + this.towards(centre.getX(),centre.getY()));
		}
		
		// Evitement des obstacles divers
		Point evite = new Point();
		int nb_obs = 0;
		int nb_murs = 0;
		for (int i = 0; i < percepts.length; ++i) {
			Percept p = percepts[i];
			double ecart_min = ecartMin(p);
			double d = this.distanceTo(p);
			if ((d < ecart_min) && (d > 0)){
				double mult = (ecart_min - d)/ecart_min;
				evite.setCoord(evite.getX() - mult*p.getX()/d, evite.getY() - mult*p.getY()/d );
				nb_obs++;
				if (p.getPerceptType().equals("Obstacle"))
					nb_murs++;
			}
		}
		
		eviteObstacles(liste_obs);
		eviteRockets(rockets);
		
		if (nb_obs > 0) {
				double direct = this.getHeading()*Math.PI/ 180;
				Point but_virtuel = new Point(Math.cos(direct),Math.sin(direct));		
				this.setHeading(this.towards(but_virtuel.getX() + evite.getX(),but_virtuel.getY() + evite.getY() ));
		}
		
		if (synchro.pret()) {
			monEtat();
		}
		else if (zero_cible && !cibles_observees.isEmpty())
			monEtat();
		synchro.decrementer();

		
		gestionMessages();
		
	
		this.move();	
	}

	
	
	private int num = 0;
	private boolean launcher_vu = false;
	private Vector cibles_observees = new Vector();
	private Vector liste_autres = new Vector();
	private Point objectif = new Point();
	private AgentAddress adr_objectif = null;
	private boolean cible_launcher = false;
	
	
}

