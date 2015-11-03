package warbot.BPV_team;

import java.util.Vector;

import warbot.kernel.Brain;
import warbot.kernel.Percept;

public class Robot extends Brain {

	protected int tour = 0;

	protected Point coordonnees = new Point(0,0);
	protected String groupName = null;

	protected Compteur synchro = null; // defini dans les sous classes

	protected Vector bases_ennemies = new Vector();
	protected Vector liste_rockets = new Vector();

	public void activate() {
		groupName = "warbot-" + this.getTeam();
	}
	
	protected void monEtat() {}
	
	public boolean baseEnnemieConnue(Point b) {
		boolean trouve = false;
		int i =0;
		while (( !trouve) && (i < bases_ennemies.size())){
			trouve = ((Point) bases_ennemies.elementAt(i)).distanceTo(b) < 10;
			++i;
		}
		return trouve;
	}

	public void baseEnnemieSuppr(Point b) {
			Vector nouv_liste = new Vector();
			for (int i = 0; i < bases_ennemies.size(); ++i) {
				Point p = ((Point) bases_ennemies.elementAt(i));
				if (p.distanceTo(b) >= 10) {
					nouv_liste.add(p);
				}
			}
			bases_ennemies = nouv_liste;
	}

	public void eviteObstacles(Vector percepts) {
		
		double dist1 = 500; // obstacle le + pres en ligne droite devant le cote gauche du robot
		double dist2 = 500; //                                                                      droit
		int taille_robot = 20; // superieur a la taille reelle

		// on dessine 2 droite paralleles a la direction
		// qui partent des bords du robot -> d1 : y = 20 et d2 : y = -20
		// Dans nouveau repere : origine = self
		//			                      rotation du repere de l'angle de direction courant
		double direction = this.getHeading();
		double angle = Math.PI * direction / 180;
		double t = Math.tan(angle);
		double s = Math.sin(angle);
		double c = Math.cos(angle);
		if (c == 0) {
			c = 0.000001;
		}
		
		for (int i = 0; i < percepts.size(); ++i) {
			Percept p = (Percept) percepts.elementAt(i);
			// centre_x, centre_y : centre de l'obstacle dans le repere
			double centre_x = (  p.getX() + t* p.getY()) / (c + s * t);
			double centre_y = -p.getY()/c + t * centre_x;
			
			if (centre_x > 0){
				if ((centre_y >= 0) && (centre_y <= 2*taille_robot)) {
					double y = centre_y - taille_robot;
					dist1 = Math.min(dist1,-Math.sqrt(taille_robot*taille_robot - y*y) + centre_x);
				}
				else if ((centre_y < 0) && (centre_y >= -(2*taille_robot))) {
					double y = centre_y + taille_robot;
					dist2 = Math.min(dist2,-Math.sqrt(taille_robot*taille_robot - y*y) + centre_x);
				}
			}
		}
		if ((Math.min(dist1, dist2) <= 100) && (Math.abs(dist1 - dist2) > 2)) {
				if (dist1 < dist2)
					direction += 100/dist1;
				else
					direction -= 100/dist2;
	
				this.setHeading(direction);				
		}				
	}
	
	public void miseAJourMouvement() {
		// Mouvement normal et mise a jour des coordonnees
		double alpha = this.getHeading() * Math.PI / 180;
		double	depl_x = 2*Math.cos(alpha);
		double	depl_y = 2*Math.sin(alpha);
		if (! this.isMoving()) {
			this.randomHeading();
		}
		else {
			coordonnees.setCoord(coordonnees.getX() + depl_x, coordonnees.getY() + depl_y);
		}
		tour ++;
	
	}
	
	public double enDouble(String str) {
		Double d = Double.valueOf(str);
		
		return d.doubleValue();
		
	}
	
	public Rocket eviteRockets(Vector rockets) {
		Point evite_rockets = new Point(0,0);
		int nb_rockets = 0;
		Vector nouvelles_rockets = new Vector();
		
		for (int i = 0; i < rockets.size(); ++i) {
			Percept p = (Percept) rockets.elementAt(i);
			Point pos = new Point(p.getX() + coordonnees.getX(), p.getY() + coordonnees.getY());
			Rocket r = new Rocket(pos);
			nouvelles_rockets.add(r);
		}
		
		// Evitement des rockets en prenant en compte leur deplacement
		int cpt = 0;
		while (cpt < liste_rockets.size()) {
			Rocket r = (Rocket) liste_rockets.elementAt(cpt);
			int index = nouvelles_rockets.indexOf(r);
			if ( index != -1) {
				Rocket r2 = (Rocket) nouvelles_rockets.elementAt(index);
				r.setCoord(r2.getX(), r2.getY());
				nouvelles_rockets.remove(index);
				++cpt;
			}
			else {
				liste_rockets.remove(cpt);
			}
		}
		// Ici dans liste_rockets on a toutes les rockets vues au moins 2 fois
		// dans nouvelles_rockets on a celles qui viennent d'apparaitre 
	
		double plus_proche = 100;
		Rocket	roc = null;
	
		for (int i =0; i < liste_rockets.size(); ++i) {
			Rocket r = (Rocket) liste_rockets.elementAt(i);
			double s = (r.getNextCoord().getY() - r.getY())/10;
			double	c = (r.getNextCoord().getX() - r.getX())/10;
			if (c == 0)
				c = 0.0000001;
			double t = s/c;
			double	x = r.getX() - coordonnees.getX();
			double	y = r.getY() - coordonnees.getY();
		
			double	centre_x = -( x + t* y) / (c + s * t);
			double centre_y = -y/c - t * centre_x;
			Point centre = new Point(x,y);
			double distance = centre.distanceTo(new Point(0,0));
		
			if ((! r.getShootee()) && (plus_proche > centre_x)) {
								roc = r;
								plus_proche = centre_x;
			}
		
			// Si je suis sur la trajectoire
			if ((centre_x > 0) && (Math.abs(centre_y) < 16)) {
				nb_rockets += 1;
				double mult = (100 - distance)/100;
				if (centre_y >= 0)
					evite_rockets.setCoord(evite_rockets.getX() + mult*y, evite_rockets.getY() - mult*x);
				else
					evite_rockets.setCoord(evite_rockets.getX() - mult*y, evite_rockets.getY() + mult*x);
			}
		}
			
		liste_rockets.addAll(nouvelles_rockets);
			

		if (nb_rockets > 0) {
			double direct = this.getHeading()*Math.PI/ 180;
			Point but_virtuel = new Point(2*Math.cos(direct),2*Math.sin(direct));
			this.setHeading(this.towards(but_virtuel.getX() + evite_rockets.getX(),but_virtuel.getY() + evite_rockets.getY()));
		}
	
				
		if (roc != null) {
			roc.setShootee(true);
		}
		return roc;
				
	}	


	public void doIt() {
		miseAJourMouvement();
		
		Percept[] percepts = this.getPercepts();
				
		Vector v = new Vector();
		for (int i = 0; i < percepts.length; ++i) {
			v.add(percepts[i]);
		}
		eviteRockets(v);
		
		
		move();
	}


}
