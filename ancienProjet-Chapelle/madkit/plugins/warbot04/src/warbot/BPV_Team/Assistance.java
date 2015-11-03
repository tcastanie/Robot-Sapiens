package warbot.BPV_team;

public class Assistance {
	private String adr_demande = "";
	
	private Point ag_coord = null;
	
	private Mobile mobile = null;		
	
	private int 
		energie = 0,
		prio = 0,
		ttl = 0;
		
	public Assistance() {}
	
	public boolean setAssistance(Point ag_coord, String adresse_demande, double dest_x, double dest_y, int energie, int priorite, int ttl, int tour) {
		boolean plus_proche = (this.prio == 0);
		Point pt_dest = new Point(dest_x, dest_y);
		boolean meme_cible = (this.mobile != null) && pt_dest.distanceTo(this.mobile.getCoord()) <= 15;
		if (!plus_proche && !meme_cible) {
			double nouv_dist = pt_dest.distanceTo(ag_coord);
			double actuel_dist = this.mobile.getCoord().distanceTo(ag_coord);
			if (priorite == this.prio)
				plus_proche = nouv_dist < actuel_dist;
			else
				plus_proche = priorite > this.prio;
		}
		if ((meme_cible && adresse_demande.equals(this.adr_demande)) || plus_proche) {
			if (adresse_demande.equals(this.adr_demande) && meme_cible)  {
				this.mobile.setVal(dest_x, dest_y, tour);
			}
			else
				this.mobile = new Mobile(dest_x , dest_y, tour);
			this.ag_coord = ag_coord;
			this.prio = priorite;
			this.adr_demande = adresse_demande;
			this.ttl = ttl;
			this.energie = energie;
			return true;
		}
		else
			return false;
	}
	
	public void decrementer() {
		if (this.ttl > 0)
			this.ttl --;
		if (this.ttl  == 0)
			this.prio = 0;
	}
	
	public void finAssistance() {
		this.prio = 0;
	}
	
	public double getX() {
		return this.mobile.getX();	
	}
	public  double getY() {
		return this.mobile.getY();	
	}
	public Point getCoord() {
		return this.mobile.getCoord();	
	}
	public Point getVisee() {
		return this.mobile.getVisee(this.ag_coord);
	}
	public int besoin() {
		return this.prio;
	}
	public String getAdresseDemande() {
		return this.adr_demande;	
	}
	
}
