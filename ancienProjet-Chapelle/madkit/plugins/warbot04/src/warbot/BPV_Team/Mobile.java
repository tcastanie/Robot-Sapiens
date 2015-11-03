package warbot.BPV_team;

public class Mobile {
	
	private Point pos = null;
	private Point depl = new Point(0,0);

	private Point visee = null; // position estimee pour viser avec un missile
	private boolean visee_deja_calcule = false;

	private Point next_pos = null; // position estimee a la prochaine mise a jour

	private int tour = 0;
	private int diff_tour = 1; // nb de tours entre derniere et avant-derniere mise a jour
	private boolean libre = true; // libre == true -> objet peut changer de direction
	
	// x et y -> position de l'objet
	// tour -> numero du tour de l'agent
	// libre -> indique si l'objet peut changer de direction
	public Mobile(double x, double y, int tour, boolean libre) {
		this.pos = new Point(x,y);
		this.tour = tour;
		this.visee = new Point(x,y);
		this.visee_deja_calcule = true;
		this.next_pos = new Point(x,y);
		this.libre = libre;
	}
	
	public Mobile(double x, double y, int tour) {
		this(x,y,tour, true);
	}
	
	public Mobile(double x, double y) {
		this(x,y, 0, true);
	}
	
	public Mobile(double x, double y, boolean libre) {
		this(x,y,0, libre);
	}
	
	
	public void setVal(double x, double y) {
		this.setVal(x, y, this.tour + 1);
	}
	
	public void setVal(double x, double y, int tour) {
		this.diff_tour = tour - this.tour;
		this.tour = tour;
		if (this.diff_tour == 0)
			this.diff_tour = 1;
		this.depl.setCoord((x - this.pos.getX())/ (10*this.diff_tour), (y - this.pos.getY())/ (10*this.diff_tour));
		this.next_pos.setCoord(2*x -this.pos.getX(),2*y -this.pos.getY());
		this.pos.setCoord(x,y);
		this.visee_deja_calcule = false;
	}
	
	public double getX() { return this.pos.getX(); }
	public double getY() { return this.pos.getY(); }
	public Point getCoord() { return this.pos; }
	public double getNextX() { return this.next_pos.getX(); }
	public double getNextY() { return this.next_pos.getY(); }
	public Point getNextCoord() { return this.next_pos; }
	
	public Point getVisee(Point ag_coord) {
		if (this.visee_deja_calcule)
			return this.visee;
		
		int a = 0;
		int b = 200;
		while (b-a > 1) {
			int m = (a+b) / 2;
			this.visee.setCoord(this.pos.getX() + m*this.depl.getX(), this.pos.getY() + (m*this.depl.getY()));
			if (this.visee.distanceTo(ag_coord) > m + 14)
				a = m;
			else
				b = m;
		}
		//this.visee.setCoord(this.pos.getX() + b*depl.getX(),this.pos.getY() + b*this.depl.getY());
		this.visee_deja_calcule = true;
		return this.visee;
	}
	
	// pour pouvoir rechercher dans un contenant du type Vector
	public boolean equals(Object o) {
		Point p = null;
		if (o instanceof Point) {
			p = (Point) o;
		}
		else if (o instanceof Mobile) {
			Mobile m = (Mobile) o;
			p = m.getCoord();
		}
		if (this.libre)
			return (p.distanceTo(this.getCoord()) < 6*this.diff_tour + 0.001);
		else
			return (p.distanceTo(this.getNextCoord()) < 0.001) || (p.distanceTo(this.getCoord()) < 0.001);
	}
	
}
