package warbot.BPV_team;

public class Rocket extends Mobile {
	private boolean
		deja_vue = false,
		deja_shoote = false;
		
	public Rocket(Point p) {
		super(p.getX(), p.getY(), false);
	}
	
	public void setCoord(double x, double y) {
		super.setVal(x, y);
		deja_vue = true;
	}
	
	public boolean getShootee() { return this.deja_shoote; }
	public void setShootee(boolean b) { this.deja_shoote = b; }
	public boolean getDejaVu() { return this.deja_vue; }
	
	public boolean equals(Object o) {
		if (this.deja_vue)
			return super.equals(o);
		Point p = null;
		if (o instanceof Point) {
				p = (Point) o;
			}
			else if (o instanceof Mobile) {
				Mobile m = (Mobile) o;
				p = m.getCoord();
			}
		double dist = p.distanceTo(this.getCoord());
		return  Math.abs(dist - 10) < 0.00001;// || Math.abs(dist - 4) < 0.00001;
		
	}
	
}
