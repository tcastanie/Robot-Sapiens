package warbot.BPV_team;


public class CibleObs extends Cible {

	private int vu_par_autre = 0;
 
	public CibleObs() {}

	public void setVuParAutre(int val) {
			vu_par_autre = val;
	}
	public int getVuParAutre() {
			return vu_par_autre;
	}

}
