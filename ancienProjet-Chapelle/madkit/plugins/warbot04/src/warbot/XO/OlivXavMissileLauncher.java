package warbot.XO;

import java.lang.*;
import java.util.*;
import java.io.*;
import warbot.kernel.*;
import madkit.kernel.*;

public class OlivXavMissileLauncher extends Brain{

	boolean attaquant = false;
	String groupName = "warbot-";
	String roleName = "guerrier";
	String[] goals = new String[4];
	int waitingForRocket = 0;
	int waitingMax = 3;
	Memoire mem;
	
	//gestion oscillations ennemi
	int engosci = 0;
	
	//moi
	int waitingOscillations = 0;
	double xParcouru = 15;
	double yParcouru = 15;
	double goalx, goaly;
	boolean wasMoving;
	Vector redzones = new Vector();
	//fin moi

	public OlivXavMissileLauncher(){}

	public void activate(){
		groupName=groupName+getTeam();
		randomHeading();
    		createGroup(false,groupName,null,null);
    		requestRole(groupName,roleName,null);
   		this.showUserMessage(true);
   		String name = getName();
   		for(int i=0; i<4; i++){goals[i]=null;}
   		mem = new Memoire(0, 0, 0, 0, 0, 0, 0, 0, goals, null, false);
   		//moi
   		wasMoving = false;
   		move();	
   		//fin moi
	}

	public void doIt(){
		double distMin = 10;
		double distMax = 45;
		double x = 0;
		double y =0;
		double xDest = 0;
		double yDest = 0;
		double direction = getHeading();
		String name = getName();
		int goal = 0;
		int capaciteCourrier = 100;
		int k=0;
		AgentAddress addressBase = null;
		
		// moi
		// Tout fonctionne comme sur des roulettes, sauf qu'il faudrait fixer une coordonnée de convention 
		// qui représenterais le but à atteindre ! Attention : le code ci dessous est conçu pour le retour à la base
		// A la place des coordonnées de la base mettre celles du but à atteindre !

		if (wasMoving) majRedZones(getHeading());
		
		//utilise l'ancienne direction, la liste des redzones, waitingOscillations, waitingOscillationsMax, xParcouru
		//yParcouru et les coordonnées du goal (qui sont ici toujours les coordonnées de la base retourner à la base)
		
		//fin moi
		
		if(engosci > 0) engosci--;
		
		wasMoving = isMoving();
				
		AgentAddress[] effectifBase = getAgentsWithRole(groupName, "base");
		if(effectifBase[0] != null){
			addressBase = effectifBase[0];
		}
		
		for(int i=0; i<4; i++){goals[i]=null;}
		
		WarbotMessage m=null;
		WarbotMessage lastMessage=null;
		String act = null;
		
		Percept[] percepts = getPercepts();
		Percept perceptHome=null;
		Percept perceptEnnemi=null;
		Percept perceptBaseEnnemi=null;
		Percept p=null;
		String pType = null;
		
		decrWaitingForRocket();
		
		WarbotMessage[] stock = new WarbotMessage[capaciteCourrier];
		for(int i=0; i<capaciteCourrier; i++){stock[i] = null;}
		k = 0;
              	while( ((m = readMessage())!= null) && (k < capaciteCourrier) ){
              		if(m.getSender() != getAddress()){
              			stock[k] = m;
              			k++;
              		}
              	}
              	
              	//String[] tab = mem.getGoals();
              	String[] tab = new String[4];
              	for(int i=0; i<4; i++){
              		tab[i]=mem.stockGoals[i];
              	}           	
              	
              	double dirgrad = Math.toRadians(direction);
              	double parcouru = (double)getCoveredDistance();
              	
              	//traitement mémoire : calcul position de la base amie
          	if( (mem.getXposbase() != 0) && (mem.getYposbase() != 0) ){
          		// changement de + vers - effectué car erreur ds formule
              		mem.setXposbase(mem.getXposbase()-parcouru*Math.cos(dirgrad));
              		mem.setYposbase(mem.getYposbase()-parcouru*Math.sin(dirgrad));
              	}
              	
              	//traitement mémoire : calcul position de la base ennemie
              	if(attaquant){
              		if( (mem.getXBaseEnnemie() != 0) && (mem.getYBaseEnnemie() != 0) ){
          			// changement de + vers - effectué car erreur ds formule
              			mem.setXBaseEnnemie(mem.getXBaseEnnemie()-parcouru*Math.cos(dirgrad));
				mem.setYBaseEnnemie(mem.getYBaseEnnemie()-parcouru*Math.sin(dirgrad));
			}
		}
		
		//mise à jour de la position du guerrier à aider
              	if(mem.getHelpGuerrier() != null){
              		goals[2]="Help";
              		mem.setHelpX(mem.getHelpX()-parcouru*Math.cos(dirgrad));
			mem.setHelpY(mem.getHelpY()-parcouru*Math.sin(dirgrad));
              	}
		
		//mise à jour si un guerrier ennemi est détruit
              	boolean present = false;
              	if(tab[3] == "AttackEnnemi"){
              		if(percepts.length > 0){
				for(int j=0;j<percepts.length;j++){
					p = percepts[j];
					pType= p.getPerceptType();
					if( pType.equals("RocketLauncher") && (!p.getTeam().equals(getTeam())) ){
						double d = Math.sqrt((p.getX()-mem.getXGuerrierEnnemi())*(p.getX()-mem.getXGuerrierEnnemi())+(p.getY()-mem.getYGuerrierEnnemi())*(p.getY()-mem.getYGuerrierEnnemi()));
						if( d < 5 ){
							present = true;
							engosci = 10;
							mem.setXGuerrierEnnemi(p.getX());
							mem.setYGuerrierEnnemi(p.getY());
							goals[3]="AttackEnnemi";
						}
					}
              	 		}
              	 		if(!present){
              	 			if(engosci == 0){
              	 				broadcast(groupName, roleName, "ok");
              	 				mem.setXGuerrierEnnemi(0);
              	 				mem.setYGuerrierEnnemi(0);
              	 				mem.setHelpX(0);
						mem.setHelpY(0);
						mem.setHelpGuerrier(null);
					}
					else{
						goals[3]="AttackEnnemi";
					}
              	 		}
              		}
              		else{
              			if(engosci == 0){
              				broadcast(groupName, roleName, "ok");
              				mem.setXGuerrierEnnemi(0);
              	 			mem.setYGuerrierEnnemi(0);
              				mem.setHelpX(0);
					mem.setHelpY(0);
					mem.setHelpGuerrier(null);
				}
				else{
					goals[3]="AttackEnnemi";
				}
              	 	}
              	}
              	
              	//mise à jour si la base est détruite
              	present = false;
              	if(mem.getPerceptBase()){
              		if( (tab[1] == "AttackBase") && (tab[2] == null) && (tab[3] == null) ){
              			String[] tabo = new String[2];
              			if(percepts.length > 0){
					for(int j=0;j<percepts.length;j++){
						p = percepts[j];
						pType= p.getPerceptType();
						if( pType.equals("Home") && (!p.getTeam().equals(getTeam())) ){
							present = true;
							goals[1]="AttackBase";
						}
					}
					if(!present){			
						tabo[0] = ""+mem.getXBaseEnnemie();
						tabo[1] = ""+mem.getYBaseEnnemie();
						mem.setXBaseEnnemie(0);
						mem.setYBaseEnnemie(0);
						mem.setPerceptBase(false);
						mem.setHelpX(0);
						mem.setHelpY(0);
						mem.setHelpGuerrier(null);
						broadcast(groupName, "base", "guerrier:baseDetruite", tabo);
						broadcast(groupName, "guerrier", "guerrier:baseDetruite");
              					attaquant = false;
					}
				}
				else{
					tabo[0] = ""+mem.getXBaseEnnemie();
					tabo[1] = ""+mem.getYBaseEnnemie();
					mem.setXBaseEnnemie(0);
					mem.setYBaseEnnemie(0);
					mem.setPerceptBase(false);
					mem.setHelpX(0);
					mem.setHelpY(0);
					mem.setHelpGuerrier(null);
					broadcast(groupName, "base", "guerrier:baseDetruite", tabo);
					broadcast(groupName, "guerrier", "guerrier:baseDetruite");
              				attaquant = false;
				}
			}	
              	}
              	
              	//récupération de percepts
              	if(percepts.length > 0){
			for(int i=0;i<percepts.length;i++){
				p = percepts[i];
				pType= p.getPerceptType();
				if( pType.equals("RocketLauncher") && (!p.getTeam().equals(getTeam())) ){
				    	goals[3]="AttackEnnemi";
				}
				if( pType.equals("Home") && (!p.getTeam().equals(getTeam())) ){
					double d = Math.sqrt((p.getX()-mem.getXBaseEnnemie())*(p.getX()-mem.getXBaseEnnemie())+(p.getY()-mem.getYBaseEnnemie())*(p.getY()-mem.getYBaseEnnemie()));
					if( d < 5 ){
						goals[3]="AttackEnnemi";
					}
				}
				if( pType.equals("Home") && (p.getTeam().equals(getTeam())) ){
					goals[0]="GoingToBase";
				}
              	 	}
              	}
              	
              	//lecture de ma boîte à lettre	
              	k = 0; 
              	while(k < capaciteCourrier){
              		if(stock[k] != null){
              			act = stock[k].getAct();
              			if(act.equals("ok")){
              				lastMessage = stock[k];
              				mem.setHelpGuerrier(null);
              				mem.setHelpX(0);
              				mem.setHelpY(0);
              				goals[2] = null;
              			}
              			if(act.equals("guerrier:baseDetruite")){
              				mem.setXBaseEnnemie(0);
					mem.setYBaseEnnemie(0);
					mem.setHelpX(0);
              				mem.setHelpY(0);
              				mem.setHelpGuerrier(null);
              				mem.setPerceptBase(false);
              				attaquant = false;
				}
              			if(act.equals("base:maj1")){
              				lastMessage = stock[k];
              	 			mem.setXposbase(lastMessage.getFromX());
              	 			mem.setYposbase(lastMessage.getFromY());
              	 			String[] resultat = lastMessage.getContent();
          				x = Double.valueOf(resultat[0]).doubleValue();
		   			y = Double.valueOf(resultat[1]).doubleValue();
		   			if(attaquant){
			   			if( (x != 0) && (y != 0) ){
			   				mem.setXBaseEnnemie(lastMessage.getFromX()+x);
							mem.setYBaseEnnemie(lastMessage.getFromY()+y);
						}
					}
              	 		}
              			if(act.equals("base:baseEnnemie")){
              				lastMessage = stock[k];
              				attaquant = true;
              				String[] result = lastMessage.getContent();
          				x = Double.valueOf(result[0]).doubleValue();
		   			y = Double.valueOf(result[1]).doubleValue();
	   				mem.setXBaseEnnemie(lastMessage.getFromX()+x);
					mem.setYBaseEnnemie(lastMessage.getFromY()+y);
              				goals[1]="AttackBase";
              			}
              		}
              		k++;
              	}
              	
              	//si je peux aller aider : choix du guerrier le plus proche à aider
              	double distmin = 100000;
              	k = 0; 
              	if(mem.getHelpGuerrier() == null){
              		while(k < capaciteCourrier){
              			if(stock[k] != null){
              				act = stock[k].getAct();
              				if(act.equals("guerrier:hostile") || act.equals("base:hostile")){
              					lastMessage = stock[k];
              					goals[2]="Help";
              					String[] result = lastMessage.getContent();
              					x = Double.valueOf(result[0]).doubleValue();
	   					y = Double.valueOf(result[1]).doubleValue();
	   					xDest = lastMessage.getFromX()+x;
	   					yDest = lastMessage.getFromY()+y;
	   					double dist = Math.sqrt(xDest*xDest + yDest*yDest);
	   					if(dist < distmin){
	   						distmin = dist;
	   						mem.setHelpGuerrier(""+lastMessage.getSender());
	   						mem.setHelpX(xDest);
              						mem.setHelpY(yDest);
	   					}
              				}
              			}
              			k++;
              		}
              	}
              	
              	//maintient du but principal
              	if(attaquant){	
              		if( (mem.getXBaseEnnemie() != 0) && (mem.getYBaseEnnemie() != 0) ){
              			goals[1]="AttackBase";
              		}
              	}

              	//sauvegarde de mes buts
              	mem.saveGoals(goals);

		//Moi = engagement à se debloquer              	
		if( (tab[3] == null) && (!isMoving()) ){			
			//engdebloque = 10;
			setHeading(getHeading()+10);
			move();
			return;
		}
              	
              	//choix du but prioritaire pour le tour
              	for(int i=0; i<4; i++){
              		if(goals[i]!=null){goal=i;}
              	}
              	
              	//application de l'action liée au but en cours
              	switch(goal){	
              			case 3:
              				//attaque d'un ennemi
              				int energy = 100000;
              				if(percepts.length > 0){
						for(int i=0;i<percepts.length;i++){
							p = percepts[i];
							pType= p.getPerceptType();
							if( pType.equals("RocketLauncher") && (!p.getTeam().equals(getTeam())) ){
							    	if(energy > p.getEnergy()){
							    		perceptEnnemi = p;
							    		energy = p.getEnergy();
							    	}
							}
							if( pType.equals("Home") && (!p.getTeam().equals(getTeam())) ){
								perceptEnnemi = p;
							}
								
						}
					}
					if(perceptEnnemi!=null){
						pType = perceptEnnemi.getPerceptType();
						if( pType.equals("RocketLauncher") && (!perceptEnnemi.getTeam().equals(getTeam())) ){
							String[] tabo = new String[3];
							tabo[0] = ""+perceptEnnemi.getX();
							tabo[1] = ""+perceptEnnemi.getY();
        						broadcast(groupName, roleName, "guerrier:hostile", tabo);
        						mem.setHelpGuerrier(null);
        						mem.setXGuerrierEnnemi(perceptEnnemi.getX());
        						mem.setYGuerrierEnnemi(perceptEnnemi.getY());
        						if(waitingForRocket == 0){
								launchRocket(towards(perceptEnnemi.getX(), perceptEnnemi.getY()));
								waitingForRocket=waitingMax;
							}
        						else if(waitingForRocket == 1){
        							setHeading(getHeading()+180);
        							move();
        						}
        						else{
        							setHeading(towards(perceptEnnemi.getX(), perceptEnnemi.getY())+180);
        							move();
        						}
              	 					setUserMessage("à l'attaque 1 !!");
              	 					return;
						}
						if( pType.equals("Home") && (!perceptEnnemi.getTeam().equals(getTeam())) ){
							double d = Math.sqrt((perceptEnnemi.getX()-mem.getXBaseEnnemie())*(perceptEnnemi.getX()-mem.getXBaseEnnemie())+(perceptEnnemi.getY()-mem.getYBaseEnnemie())*(perceptEnnemi.getY()-mem.getYBaseEnnemie()));
							if( d < 5 ){
								mem.setPerceptBase(true);
        							launchRocket(towards(perceptEnnemi.getX(), perceptEnnemi.getY()));
        							waitingForRocket=waitingMax;	
              	 						setUserMessage("à l'attaque!!");
              	 						return;
              	 					}
						}
						
					}
					if(engosci>0){
						String[] tabo = new String[3];
						tabo[0] = ""+mem.getXGuerrierEnnemi();
						tabo[1] = ""+mem.getYGuerrierEnnemi();
						broadcast(groupName, roleName, "guerrier:hostile", tabo);
						if(waitingForRocket == 0){
							launchRocket(towards(mem.getXGuerrierEnnemi(), mem.getYGuerrierEnnemi()));
							waitingForRocket=waitingMax;
							}
        					else if(waitingForRocket == 1){
        						setHeading(getHeading()+180);
        						move();
        					}
        					else{
        						setHeading(towards(mem.getXGuerrierEnnemi(), mem.getYGuerrierEnnemi())+180);
        						move();
        					}
						setUserMessage("à l'attaque 2 !!");
						return;
					}
					return;
          			
          			case 2:
					//aider un ami
					if(percepts.length > 0){
						for(int i=0;i<percepts.length;i++){
							p = percepts[i];
							pType= p.getPerceptType();
							if( pType.equals("RocketLauncher") && (!p.getTeam().equals(getTeam())) ){
								mem.setHelpGuerrier(null);
								mem.setHelpX(0);
								mem.setHelpY(0);
								goals[2] = null;
								goals[3] = "AttackEnnemi";
								mem.saveGoals(goals);
								if(waitingForRocket == 0){
									launchRocket(towards(p.getX(), p.getY()));
									waitingForRocket=waitingMax;
								}
        							else if(waitingForRocket == 1){
        								setHeading(getHeading()+180);
        								move();
        							}
        							else{
        								setHeading(towards(p.getX(), p.getY())+180);
        								move();
        							}
	   							return;
	   						}	   						
	   					}
	   				}
					ajoutRedZones(getHeading(),mem.getHelpX(),mem.getHelpY(),20,percepts);   				
	   				setHeading(evitement(percepts, mem.getHelpX(), mem.getHelpY(), getHeading()));
	   				setUserMessage("going to help");
	   				move();
          				return;
              			
          			case 1:
          				//attaquer la base ennemie
          				k = 0;
          				while(k < capaciteCourrier){
              					if(stock[k] != null){
              						act = stock[k].getAct();
              						if(act.equals("base:baseEnnemie")){
              							lastMessage = stock[k];
						         	//moi
								ajoutRedZones(getHeading(), mem.getXBaseEnnemie(), mem.getYBaseEnnemie(),6,percepts);
		         					//fin moi
	   							setHeading(evitement(percepts, mem.getXBaseEnnemie(), mem.getYBaseEnnemie(), getHeading()));
	   							setUserMessage("going to attack base (d)");
	   							move();
          							return;
          						}
          					}
          					k++;
          				}
          				if( (mem.getXBaseEnnemie() != 0) || (mem.getYBaseEnnemie() != 0) ){
         					//moi
          					ajoutRedZones(getHeading(), mem.getXBaseEnnemie(), mem.getYBaseEnnemie(), 6,percepts);
         					//fin moi
          					setHeading(evitement(percepts, mem.getXBaseEnnemie(), mem.getYBaseEnnemie(), getHeading()));
	   					setUserMessage("going to attack base (mem)");
	   					move();
          					return;
          				}
          			case 0: 
          				//retour à la base
          				if(!isMoving()){
          					boolean bp = false;
              	 				setUserMessage("default : bloqué!!");
              	 				send(addressBase, "guerrier:homeposition");
              	 				if(percepts.length > 0){
							for(int i=0;i<percepts.length;i++){
								p = percepts[i];
								pType= p.getPerceptType();
								if( pType.equals("Home") && (perceptHome == null) && (p.getTeam().equals(getTeam())) ){	
									bp = true;
									randomHeading();
									setHeading(evitement(percepts, mem.getXposbase(), mem.getYposbase(), getHeading()));
									move();
									return;
								}
							}
						}
						if(!bp){
							// changement : si il bouge pas alors --> contournement
         						//moi
          						ajoutRedZones(getHeading(), mem.getXposbase(), mem.getYposbase(), 6, percepts);
         						//fin moi
							setHeading(evitement(percepts, mem.getXposbase(), mem.getYposbase(), getHeading()));						
							move();
							return;
						}
					}
          				if(percepts.length > 0){
						for(int i=0;i<percepts.length;i++){
							p = percepts[i];
							pType= p.getPerceptType();
							if( pType.equals("Home") && (perceptHome == null) && (p.getTeam().equals(getTeam())) ){
								perceptHome = p;
								if(distanceTo(perceptHome) >= distMax){
				              				mem.setXposbase(perceptHome.getX());
              	 							mem.setYposbase(perceptHome.getY());
		   							setUserMessage("I return to base");
		   							setHeading(evitement(percepts, mem.getXposbase(), mem.getYposbase(), getHeading()));
					   				move();
			                    				return;
              	 						}
              	 						else{
              	 							if(distanceTo(perceptHome) < distMin){
			              	 					setUserMessage("I am in my base");
				        					mem.setXposbase(perceptHome.getX());
              	 								mem.setYposbase(perceptHome.getY());
              	 								setHeading(evitement(percepts, -mem.getXposbase(), -mem.getYposbase(), getHeading()));
              	 								move();
              	 								return;
              	 							}
              	 							else{
              	 								mem.setXposbase(perceptHome.getX());
              	 								mem.setYposbase(perceptHome.getY());
              	 								randomHeading();
										move();
										return;
									}
								}
							}
						}
					}
          				if( (mem.getXposbase()!=0) && (mem.getYposbase()!=0) ){
         					//moi
         					ajoutRedZones(getHeading(), mem.getXposbase(), mem.getYposbase(), 6,percepts);
         					//fin moi
              	 				setHeading(evitement(percepts, mem.getXposbase(), mem.getYposbase(), getHeading()));
              	 				setUserMessage("default : home");
              	 				move();
              	 				return;
              	 			}	
              	 	}
	}
	
	//méthodes annexes...
	
	void decrWaitingForRocket(){
		waitingForRocket--;
		if (waitingForRocket < 0)
			waitingForRocket = 3;
	}

	public void ajoutRedZones(double h, double goalx, double goaly,int waitingOscillationsMax, Percept[] percepts) {

		if ((waitingOscillations < waitingOscillationsMax) && (isMoving())) {
			xParcouru = xParcouru + Math.cos(Math.toRadians(h))*2;
			yParcouru = yParcouru + Math.sin(Math.toRadians(h))*2;		
			waitingOscillations++; 
		}
		else {
			double dParcouru = Math.sqrt(xParcouru*xParcouru + yParcouru*yParcouru);
			if (dParcouru < 7) {
				double a = towards(goalx,goaly);
				double xx = Math.cos(Math.toRadians(a));
				double yy = Math.sin(Math.toRadians(a));			
				
				boolean isWall = false;
				for(int i=0;i<percepts.length;i++){
					if (percepts[i].getPerceptType() == "Obstacle") isWall = true;
				}	
				if (isWall) {
					redzones.addElement(new Obstacle(xx*30,yy*30,500,1000,10,"RedZone"));
				}
			}
			

			xParcouru = 0;
			yParcouru = 0;
			waitingOscillations = 0;
		}
	}

	
	public void majRedZones(double h) {
		
		Vector newredzones = new Vector();
		
		for(int i=0; i<redzones.size(); i++) {
			double a = Math.toRadians(h);
			double x = ((Obstacle) (redzones.elementAt(i))).getX();
			double y = ((Obstacle) (redzones.elementAt(i))).getY();
			double p = ((Obstacle) (redzones.elementAt(i))).getPower();
			int ml = ((Obstacle) (redzones.elementAt(i))).getMaxLife();
			int l = ((Obstacle) (redzones.elementAt(i))).getLife();
			
			if (l != 0) newredzones.addElement(new Obstacle(x-Math.cos(a)*2,y-Math.sin(a)*2,l-1,ml,p,"RedZone"));
			
		}
		redzones = newredzones;
		
	}
		

	public double evitement(Percept[] percepts, double goalx, double goaly, double h) {
		Obstacle[] obs = new Obstacle[percepts.length];
		
		for (int i = 0; i < percepts.length ;i++)
			if (percepts[i].getPerceptType() == null)
				obs[i] = new Obstacle(percepts[i].getX(),percepts[i].getY(),"Wall");
			else obs[i] = new Obstacle(percepts[i].getX(),percepts[i].getY(),percepts[i].getPerceptType());
		
		double newh = contournement(goalx,goaly,h);
		double newx = Math.cos(Math.toRadians(newh));
		double newy = Math.sin(Math.toRadians(newh));
		newh = evitement(obs, newx, newy, newh);
				
		return newh;
	}

	public double contournement(double goalx, double goaly, double h) {
		Obstacle[] obs = new Obstacle[redzones.size()];

		for (int i = 0; i < redzones.size();i++)
			obs[i] = (Obstacle) redzones.elementAt(i);

		return evitement(obs, goalx, goaly, h);
	}

	
	public double evitement(Obstacle[] obs, double goalx, double goaly, double h) {		
		boolean info = false;
		
		//Plus un objet se trouve (1) près et (2) devant l'agent (=dans sa direction de déplacement), plus il va influencer l'agent.
		//L'influence sur l'agent se fait à l'aide de deux forces :
		//   - la force de répulsion : l'objet repousse l'agent vers l'arrière.
		//     Direction = la droite que l'agent fait avec l'obstacle.
		//     Sens = (Obstacle --> Agent).
		//   - la force d'évitement : l'objet repousse l'agent sur le coté (perpendiculairement à la force de répulsion)
		//     Direction : perpendiculaire à la force de répulsion.
		//     Sens : le sens (à choisir parmi les deux possibles) qui fait l'angle le plus petit avec la droite 
		//            (But, Agent).
		// On fait la somme vectorielle des forces issues de chaque objet perçu.
		
		if(obs.length > 0){
			double x = goalx;
			double y = goaly;
			double d = Math.sqrt(goalx*goalx + goaly*goaly);
			x = x/d;
			y = y/d;
			double deltaxr = 0;
			double deltayr = 0;
			double deltaxe = 0;
			double deltaye = 0;
		
	  		for(int i=0;i<obs.length;i++){
	  			
				Obstacle p = obs[i];
				double xp = p.getX();
				double yp = p.getY();
				
				double dp = Math.sqrt(xp*xp + yp*yp);
						
				xp = xp/dp;
				yp = yp/dp;
				
				double force = 70/dp; //formule de la force 
				
				double a1 = towards(x,y);
				double a2 = towards(xp,yp);
				double deltaho = Math.abs(h - a2);
				double deltabo = Math.abs(a1 - a2);
				if  (deltaho > 180) deltaho = 360 - deltaho;
				if  (deltabo > 180) deltabo = 360 - deltabo;
				
				double fho = (180 - deltaho)/180;
				double fbo = (180 - deltabo)/180;
				
				double forcee = force*force*force*fho;
				double forcer = force*force*force*fho;
				
				if ((p.getType()).equals("RedZone")) {
					forcee = force*force*fho*p.power;
					forcer = force*force*fho*p.power;
				}

																	
				double a3 = towards(-yp,xp);
				double a4 = towards(yp,-xp);
				double deltaa3 = Math.abs(a1 - a3);
				if  (deltaa3 > 180) deltaa3 = 360 - deltaa3;
				double deltaa4 = Math.abs(a1 - a4);
				if  (deltaa4 > 180) deltaa4 = 360 - deltaa4;

				double xe;
				double ye;
				double xr = -xp*forcer;
				double yr = -yp*forcer;
				double xr2 = 0;
				double yr2 = 0;
								
				if (deltaa3 <= deltaa4) {
						xe = -yp*forcee;
						ye = xp*forcee;
				} else {
						xe = yp*forcee;
						ye = -xp*forcee;
				}					
					
				deltaxe = deltaxe+xe;
				deltaye = deltaye+ye;
				deltaxr = deltaxr+xr;
				deltayr = deltayr+yr;
        		}
        		
        		double newx = x + deltaxr + deltaxe;
        		double newy = y + deltayr + deltaye;  		
       			
			return towards(newx, newy);
        	} 
        	else return towards(goalx, goaly);
        }	
}