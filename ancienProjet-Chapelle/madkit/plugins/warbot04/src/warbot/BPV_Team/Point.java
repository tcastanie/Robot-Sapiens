package warbot.BPV_team;

public class Point {
	private double _x = 0.0;
	private double _y = 0.0;
	private boolean _connu = false;
	
	public Point() {}
	
	public Point(double _x, double _y) {
		this._x = _x;
		this._y = _y;
		this._connu = true;
	}
	
	public double getX() { return _x; }
	public double getY() { return _y; }
	
	public void setCoord(double _x, double _y) {
		this._x = _x;
		this._y = _y;
		this._connu = true;
	}
	
	public void setX(double _x) {
		this._x = _x;
	}
	public void setY(double _y) {
		this._y = _y;
	}
	public void setConnu(boolean _connu) {
		this._connu = _connu;
	}
	public boolean estConnu() { return this._connu; }
	
	public double distanceTo(Point p) {
		double d_x = this._x - p.getX();
		double d_y = this._y - p.getY();
		return Math.sqrt(d_x*d_x+ d_y*d_y);
	}
	
	public String toString() {
		long x = Math.round(this._x * 1000);
		long y = Math.round(this._y * 1000);
		return "( " + ( x/1000) + ", " + (y /1000) + ")";  	
	}
	
}
