package warbot.BPV_team;

public class Cible {
	
	private Mobile mobile = null;
	private String type_cible = "";
	private int energie = 0;
	
	public Cible() {
		// Rien a definir
	}
	
	public Cible(String type_percept, Point coord, int energie, int tour) {
		setCible(type_percept, coord, energie, tour);
	}
	
	public void setCible(String type_percept, Point coord, int energie, int tour) {
		if (this.mobile == null)
			this.mobile = new Mobile(coord.getX(), coord.getY(), tour);
		else if (this.egal(type_percept, coord))
			this.mobile.setVal(coord.getX(), coord.getY(), tour);
		this.type_cible = type_percept;
		this.energie = energie;
	}
	
	public boolean egal(String type_percept, Point coord) {
		boolean result = Math.abs(coord.getX() - this.mobile.getX()) <= 15;
		result = result && Math.abs(coord.getY() - this.mobile.getY()) <= 15;
		result = result && this.type_cible == type_percept;
		return result;
	}
	
	public void delCible() {
		this.mobile = null;
	}
	public boolean existe() {
		return this.mobile != null;
	}
	
	public double getX() { return this.mobile.getX(); }
	public double getY() { return this.mobile.getY(); }
	public String getType() { 
		return this.type_cible;
	}
	public int getEnergy() { return this.energie; }
	
	// En coordonnees absolues -> Point p
	public double distanceTo(Point p) {
		if (this.mobile != null)
			return this.mobile.getCoord().distanceTo(p);
		else
			return -1;
	}
	
	public Point viser(Point ag_coord) {
		return this.mobile.getVisee(ag_coord);
	}
	
	
}
