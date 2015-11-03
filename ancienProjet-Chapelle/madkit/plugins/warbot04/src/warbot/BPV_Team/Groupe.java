package warbot.BPV_team;

import java.util.Vector;

public class Groupe {
	private Vector robots = new Vector();
	
	public Groupe() {}
	
	// liste de 'Mobile' : Amis vus
	// Mise à jour de la liste
	public void amisVus(Vector liste) {
		Vector vus = new Vector();
		int i = 0;
		int nb_robots = this.robots.size();
		while (i < nb_robots) {
			Mobile m = (Mobile) this.robots.elementAt(i);
			int index = liste.indexOf(m);
			if (index != -1) {
				Mobile anc = (Mobile) liste.elementAt(index);
				m.setVal(anc.getX(), anc.getY());
				++i;
				liste.remove(index);
			}
			else {
				this.robots.remove(i);
				nb_robots--;
			}
		}
		this.robots.addAll(liste);
	}
	
	public Point vecteurMoyen() {
		Point vect = new Point();
		int nb_mvt = 0;
		int nb_robots = this.robots.size();
		for (int i = 0; i < nb_robots; ++i) {
			Mobile robot = (Mobile) this.robots.elementAt(i);
			double x = robot.getNextX() - robot.getX();
			double y = robot.getNextY() - robot.getY();
			if (x != 0 ||y != 0) {
				vect.setCoord(vect.getX() + x, vect.getY() + y);
				nb_mvt ++;
			}
		}
		if (nb_mvt > 1) {
			vect.setCoord(vect.getX() / nb_mvt, vect.getY() / nb_mvt);
		}
		return vect;
	}
	
	public int nbVus () { return this.robots.size(); }
	public Point barycentre() {
		Point b = new Point(0,0);
		int nb_robots = this.robots.size();
		if (nb_robots > 0) {
			for (int i = 0; i < nb_robots; ++i) {
				Mobile r = (Mobile) this.robots.elementAt(i);
				b.setCoord(b.getX() + r.getX(), b.getY() + r.getY());
			}
			b.setCoord(b.getX() / nb_robots, b.getY() / nb_robots);
		}
		return b;
	}
	
}
