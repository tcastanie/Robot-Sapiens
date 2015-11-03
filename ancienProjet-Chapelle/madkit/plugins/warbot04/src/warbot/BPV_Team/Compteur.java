package warbot.BPV_team;


public class Compteur {
	private int
		cpt = 0,
		maxi = 0;
	public Compteur(int nb_max) {
		this.cpt = nb_max;
		this.maxi = nb_max;
	}
	public boolean decrementer() {
		this.cpt--;
		if (this.cpt <= 0) {
			this.cpt = this.maxi;
			return true;
		}
		else
			return false;
	}
	public boolean pret() {
		return this.cpt == this.maxi;	
	}
	
}
