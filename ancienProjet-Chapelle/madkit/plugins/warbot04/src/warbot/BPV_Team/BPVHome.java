
package warbot.BPV_team;

import warbot.kernel.*;


public class BPVHome extends Brain {

	private String groupName = null;
	private Compteur synchro = new Compteur(2);
	
	
	public void activate() {
		groupName = "warbot-"+this.getTeam();
		this.createGroup(false, groupName, null, null);
		this.requestRole(groupName, "info", null);
		
	}

	public void end() {
		this.broadcast(groupName, "launcher", "FIN");
	}	

	public void doIt() {
		if (synchro.pret()) {
			Percept[] percepts = this.getPercepts();
			for(int i = 0; i < percepts.length; ++i) {
				Percept p = percepts[i];
				if (p.getPerceptType()=="RocketLauncher" && !p.getTeam().equals(this.getTeam())) {
					String[] args = {String.valueOf(p.getX()), String.valueOf(p.getY()), String.valueOf(p.getEnergy())};
					this.broadcast(groupName, "launcher", "HELP3", args);
				}
					
					
			}
		}
		else
			synchro.decrementer();
		while (! this.isMessageBoxEmpty()){
			WarbotMessage message = this.readMessage();
		}
	}	
		
}




/**
		
	while not self.isMessageBoxEmpty():
		message = self.readMessage()
		
		**/