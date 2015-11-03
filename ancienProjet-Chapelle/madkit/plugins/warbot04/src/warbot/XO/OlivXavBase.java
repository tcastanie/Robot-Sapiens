package warbot.XO;

import java.lang.*;
import java.util.*;
import java.io.*;
import warbot.kernel.*;
import madkit.kernel.*;

public class OlivXavBase extends Brain{

	String groupName="warbot-";
	String roleName="base";
	double[][] majBases = new double[20][2];
	int waitingMax = 50;
	int waitingForMessage = 0;
	int groupe = 0;
	AgentAddress[] effectifGuerriers;
	Memoire mem;
	boolean attaqueEnCours = false;
	
	int searchRadius,searchAngle,searchDir;
	

	public OlivXavBase(){}

	public void activate(){
		this.showUserMessage(true);
		groupName = groupName+getTeam();
    		createGroup(false,groupName,null,null);
		requestRole(groupName,roleName,null);
		for(int i=0; i<majBases.length; i++){
			for(int j=0; j<2; j++){
				majBases[i][j] = 0;
			}
		}
		mem = new Memoire(0, 0, 0, 0, majBases);
		searchRadius = 600;
		searchAngle = 0;
		searchDir = 1;
	}

	public void doIt(){
		int capaciteCourrier = 100;
		String act = null;
		double x = 0;
		double y = 0;
		double xPosBaseEnnemie = 0;
		double yPosBaseEnnemie = 0;
		String xposbaseEnnemie = null;
		String yposbaseEnnemie = null;
		
		WarbotMessage m = null;
		WarbotMessage lastMessage = null;
		
		Percept[] percepts = getPercepts();
		
		//formation des groupes de guerriers : 2 en défense, les autres en attaque.
		effectifGuerriers = getAgentsWithRole(groupName, "guerrier");
		if(effectifGuerriers.length > 8){
			groupe = (effectifGuerriers.length)-2;
		}
		else{
			groupe = effectifGuerriers.length;
		}
		
		decrWaitingTime();
		
		searchAngle = searchAngle + searchDir;
		if (searchRadius < 1500) {
			if (searchAngle > 360) {
				searchRadius = searchRadius + 400;
				searchAngle = 0;
			}
		} else if (searchAngle > 360) searchAngle = 0;

		broadcast(groupName,"eclaireur","base:stand far !",""+searchRadius,""+searchAngle);
		
		WarbotMessage[] stock = new WarbotMessage[capaciteCourrier];
		for(int i=0; i<capaciteCourrier; i++){stock[i] = null;}
		int k=0;
              	while( ((m = readMessage())!= null) && (k < capaciteCourrier) ){
              		if(m.getSender() != getAddress()){
              			stock[k] = m; 
              			k++;
              		}
              	}
              	
              	//gestion des ennemis perçus dans le périmètre d'une base
              	boolean present = false;
              	if( (mem.getHelpX() != 0) && (mem.getHelpY() != 0) ){
              		if(percepts.length > 0){
	  			for(int i=0;i<percepts.length;i++){
					Percept p = percepts[i];
					String pType= p.getPerceptType();
					if( pType.equals("RocketLauncher") && (!p.getTeam().equals(getTeam())) ){
						present = true;
						double d = Math.sqrt((p.getX()-mem.getHelpX())*(p.getX()-mem.getHelpX())+(p.getY()-mem.getHelpY())*(p.getY()-mem.getHelpY()));
						if( d < 5 ){
							String[] tab = new String[2];
							mem.setHelpX(p.getX());
							mem.setHelpY(p.getY());
							tab[0] = ""+mem.getHelpX();
							tab[1] = ""+mem.getHelpY();
							broadcast(groupName, "guerrier", "base:hostile", tab);
						}
            				}
        			}
        			if(!present){
        				broadcast(groupName, "guerrier", "ok");
        				mem.setHelpX(0);
        				mem.setHelpY(0);
        			}
        		}
        		else{
        			broadcast(groupName, "guerrier", "ok");
        			mem.setHelpX(0);
        			mem.setHelpY(0);
        		}
        	}
				
		//initialisation du tableau de mise à jour de la position des bases ennemies
		for(int i=0; i<majBases.length; i++){
			for(int j=0; j<2; j++){
				majBases[i][j] = 0;
			}
		}
		
		//chargement de l'historique
		for(int i=0; i<majBases.length; i++){
			for(int j=0; j<2; j++){
				majBases[i][j] = mem.stockBases[i][j];
			}
		}
		
		//prise en compte de la destruction d'une base ennemie
		k = 0;
		while(k < capaciteCourrier){
			if(stock[k] != null){
				lastMessage = stock[k];
				act = lastMessage.getAct();
				if(act.equals("guerrier:baseDetruite")){
					double[] tabo = new double[2];
					x = Double.valueOf((lastMessage.getContent())[0]).doubleValue();
					y = Double.valueOf((lastMessage.getContent())[1]).doubleValue();
					xPosBaseEnnemie = lastMessage.getFromX()+x;
	   				yPosBaseEnnemie = lastMessage.getFromY()+y;
					tabo[0] = xPosBaseEnnemie;
					tabo[1] = yPosBaseEnnemie;
					mem.setXBaseEnnemie(0);
					mem.setYBaseEnnemie(0);
					attaqueEnCours = false;
					removeBase(majBases, tabo);
				}
			}
			k++;
		}
		
		//ajout d'une base ennemie dans la mémoire si elle n'y est pas stockée		
		k = 0;
		while(k < capaciteCourrier){
			if(stock[k] != null){
				lastMessage = stock[k];
				act = lastMessage.getAct();
				if(act.equals("eclaireur:baseEnnemie")){
					double[] result = new double[2];
					x = Double.valueOf((lastMessage.getContent())[0]).doubleValue();
		   			y = Double.valueOf((lastMessage.getContent())[1]).doubleValue();
	   				xPosBaseEnnemie = lastMessage.getFromX()+x;
	   				yPosBaseEnnemie = lastMessage.getFromY()+y;
	   				result[0] = xPosBaseEnnemie;
	   				result[1] = yPosBaseEnnemie;
	   				if(!exist(majBases, result)){
	   					int lib = caseLibre(majBases);
	   					majBases[lib][0] = xPosBaseEnnemie;
	   					majBases[lib][1] = yPosBaseEnnemie;
	   				}
				}	
			}
			k++;
		}
		
		//envoie des ordres d'attaque sur une base ennemie
		if(!attaqueEnCours){
			if(!vide(majBases)){
				double[] choix = takeLast(majBases);
				mem.setXBaseEnnemie(choix[0]);
				mem.setYBaseEnnemie(choix[1]);
				String[] mes = new String[2];
				mes[0] = ""+choix[0];
				mes[1] = ""+choix[1];
				attaqueEnCours = true;
				if(groupe > 3){
					int j = 0;
					while(j<groupe){
						AgentAddress ag = effectifGuerriers[j];
						send(ag, "base:baseEnnemie", mes);
						j++;
					}
				}
				else{
					broadcast(groupName, "guerrier", "base:baseEnnemie", mes);
				}
			}
		}
		
		//envoie d'une mise à jour tout les 50 tours
		if(waitingForMessage == 0){
			String[] msg = new String[2];
			msg[0] = ""+mem.getXBaseEnnemie();
			msg[1] = ""+mem.getYBaseEnnemie();
              		broadcast(groupName,"guerrier","base:maj1", msg);
              		waitingForMessage = waitingMax;
              	}
		
		//sauvegarde après la mise à jour de la position des bases ennemies
		mem.saveBases(majBases);
		
		if(percepts.length > 0){
	  		for(int i=0;i<percepts.length;i++){
				Percept p = percepts[i];
				String pType= p.getPerceptType();
				if( pType.equals("RocketLauncher") && (!p.getTeam().equals(getTeam())) ){
					if( (mem.getHelpX() == 0) && (mem.getHelpY() == 0) ){
						String[] tab = new String[2];
						tab[0] = ""+p.getX();
						tab[1] = ""+p.getY();
						mem.setHelpX(p.getX());
						mem.setHelpY(p.getY());
						broadcast(groupName, "guerrier", "base:hostile", tab);
					}
            			}
        		}
        	}
	}
	
	void decrWaitingTime(){
		waitingForMessage--;
		if (waitingForMessage<0)
			waitingForMessage=0;
	}
	
	//fonctions annexes pour la mise à jour du tableau des bases ennemies
	
	public void removeBase(double[][] bases, double[] tabo){
		for(int i=0; i<bases.length; i++){
			double d = Math.sqrt((bases[i][0]-tabo[0])*(bases[i][0]-tabo[0])+(bases[i][1]-tabo[1])*(bases[i][1]-tabo[1]));
			if( d < 5 ){
				for(int j=0; j<2; j++){
					bases[i][j] = 0;
				}
			}
		}
	}
	
	public boolean exist(double[][] bases, double[] tabo){
		boolean ok = false;
		for(int i=0; i<bases.length; i++){
			double d = Math.sqrt((bases[i][0]-tabo[0])*(bases[i][0]-tabo[0])+(bases[i][1]-tabo[1])*(bases[i][1]-tabo[1]));
			if( d < 5 ){
				ok = true;
				return ok;
			}
		}
		return ok;
	}
	
	public int caseLibre(double[][] bases){
		for(int i=0; i<bases.length; i++){
			if( (bases[i][0]==0) && (bases[i][1]==0) ){
				return i;
			}
		}
		return 0;
	}
	
	public double[] takeLast(double[][] bases){
		double[] result = new double[2];
		for(int i=0; i<result.length; i++){result[i]=0;}
		for(int i=0; i<bases.length; i++){
			if( (bases[i][0]!=0) && (bases[i][1]!=0) ){
				result = bases[i];
			}
		}
		return result;
	}
	
	public boolean vide(double[][] bases){
		boolean res = true;
		for(int i=0; i<bases.length; i++){
			if( (bases[i][0]!=0) && (bases[i][1]!=0) ){
				res = false;
				return res;
			}
		}
		return res;
	}
}