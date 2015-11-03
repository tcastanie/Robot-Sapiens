package warbot.XO;

import java.lang.*;
import java.util.*;
import java.io.*;
import warbot.kernel.*;
import madkit.kernel.*;

public class OlivXavDetector extends Brain{

	String groupName="warbot-";
	String roleName="eclaireur";
	Memoire mem;
	int signe;
	Engagement engagement = new Engagement();
	double goalx,goaly,goalr,oldHeading;
	double basex = 0;
        double basey = 0;

	int RZWallTTL = 1000;
	int RZSearchTTL = 500;
	double RZWallPower = 100;
	double RZSearchPower = 10;
	
	//moi
	int freq = 5;
	double[] oldbasex = new double[freq];
        double[] oldbasey = new double[freq];
	int tours = 0;
	int waitingOscillations = 0;
	boolean wasMoving;
	Vector redzones = new Vector();
	//fin moi


	public OlivXavDetector(){}

	public void activate(){
		groupName=groupName+getTeam();
		randomHeading();
    		createGroup(false,groupName,null,null);
		requestRole(groupName,roleName,null);
   		showUserMessage(true);
   		mem = new Memoire(0, 0, 0, 0);
   		signe = 1;
  		//moi
   		wasMoving = false;
   		move();	
   		//fin moi
		move();

	}

	public void doIt(){
		WarbotMessage m=null;
		WarbotMessage lastMessage=null;
		String act = null;
		double deltax = 0;
		double deltay = 0;
		double coef = 0;
		int searchRadius = 0;
		double searchAngle = 0;
		double centerx = 0;
		double centery = 0;
		
		tours++;
		
		AgentAddress[] effectifDetecteurs = getAgentsWithRole(groupName, "eclaireur");
		int nbDetect = effectifDetecteurs.length;
		Percept[] percepts = getPercepts();
		Percept perceptEnemi=null;
		Percept p;
		String pType;

		// moi
		// Tout fonctionne comme sur des roulettes, sauf qu'il faudrait fixer une coordonnée de convention 
		// qui représenterais le but à atteindre ! Attention : le code ci dessous est conçu pour le retour à la base
		// A la place des coordonnées de la base mettre celles du but à atteindre !

		if (isMoving())	majRedZones(getHeading());
		//utilise l'ancienne direction, la liste des redzones, waitingOscillations, waitingOscillationsMax, xParcouru
		//yParcouru et les coordonnées du goal (qui sont ici toujours les coordonnées de la base retourner à la base)
		
		//fin moi

		
		//System.out.println("Engagement "+engagement.nature+" pendant "+engagement.val+" tours");

		if (isMoving()) {
			if ((goalx != 0) || (goaly != 0)){
				goalx = goalx - 2*Math.cos(Math.toRadians(getHeading()));
				goaly = goaly - 2*Math.sin(Math.toRadians(getHeading()));
			}
		
			if ((basex != 0) || (basey != 0)){
				basex = basex - 2*Math.cos(Math.toRadians(getHeading()));
				basey = basey - 2*Math.sin(Math.toRadians(getHeading()));
			}
		}
				
		engagement.decr();
		
		//envoie de la position d'une base ennemie perçue	
		if(percepts.length > 0){
			for(int i=0;i<percepts.length;i++){
				p = percepts[i];
				pType= p.getPerceptType();
				if(pType.equals("Home") && (perceptEnemi == null) && (!p.getTeam().equals(getTeam()))){perceptEnemi = p;}
				if(perceptEnemi != null){
					String[] tabo = new String[2];
					tabo[0] = ""+perceptEnemi.getX();
					tabo[1] = ""+perceptEnemi.getY();
        				broadcast(groupName, "base", "eclaireur:baseEnnemie", tabo);
					mem.setXBaseEnnemie(perceptEnemi.getX());
					mem.setYBaseEnnemie(perceptEnemi.getY());
              	 		}
              	 	}
              	}
              	
              	//réception des Red Zone des autres détecteurs          	
              	while((m = readMessage())!= null){
			 lastMessage=m;		
		
			if (lastMessage != null){
	   			act = lastMessage.getAct();
	   			if (act.equals("base:stand far !")) {	   				
					basex = lastMessage.getFromX();
					basey = lastMessage.getFromY();
				}

	   			if (act.equals("eclaireur:redzoneSearch")) {
	   				double redzonex = Double.parseDouble(lastMessage.getArg1());
	   				double redzoney = Double.parseDouble(lastMessage.getArg2());
		  			double sourcex = lastMessage.getFromX();
		  			double sourcey = lastMessage.getFromY();		  			
		  		 	if ((sourcex != 0) || (sourcey != 0)) redzones.addElement(new Obstacle(sourcex+redzonex,sourcey+redzoney,RZSearchTTL,RZSearchPower,"RedZone"));
		  		 	
 		 			

				}

	   			if (act.equals("eclaireur:redzoneWall")) {
	   				double redzonex = Double.parseDouble(lastMessage.getArg1());
	   				double redzoney = Double.parseDouble(lastMessage.getArg2());
		  			double sourcex = lastMessage.getFromX();
		  			double sourcey = lastMessage.getFromY();		  			
		  		 	if ((sourcex != 0) || (sourcey != 0)) redzones.addElement(new Obstacle(sourcex+redzonex,sourcey+redzoney,RZWallTTL,RZWallPower,"RedZone"));
				}

			}
		}
		
		//gestion des Red Zones
		if ((goalx == 0) && (goaly == 0)) {
			randomHeading();			
			goalx = Math.cos(Math.toRadians(getHeading()))*100;
			goaly = Math.sin(Math.toRadians(getHeading()))*100;
		}
							
		if ((!isMoving(basex,basey,oldbasex[tours % freq],oldbasey[tours % freq])) && (!engagement.nature.equals("debloque"))) {
			double xmur = 0;
			double ymur = 0;
			double dmur = 10000000;
			
			filtreRedZones(1000,200);

			if ((xmur != 0) || (ymur != 0)) broadcastRedZones(xmur,ymur,RZWallTTL,RZWallPower,"redzoneWall");
			else broadcastRedZones(goalx,goaly,RZWallTTL,RZWallPower*redzones.size(),"redzoneWall");

			goalx = Math.cos(Math.toRadians(getHeading()+180))*200;
			goaly = Math.sin(Math.toRadians(getHeading()+180))*200;
 			engagement.init(100,"debloque");
 		}


		if ((engagement.nature).equals("")) {
			if ((basex != 0) || (basey != 0)) broadcastRedZones(basex,basey,RZSearchTTL,RZSearchPower,"redzoneSearch");
			else broadcastRedZones(goalx,goaly,RZSearchTTL,RZSearchPower,"redzoneSearch");
			engagement.init(100,"recherche");
		}

		double newh = getHeading();
		if (!engagement.nature.equals("debloque")) {
			newh = contournement(goalx,goaly,getHeading());
			goalx = Math.cos(Math.toRadians(newh))*200;
			goaly = Math.sin(Math.toRadians(newh))*200;
		}
			
		setHeading(evitement(percepts,goalx,goaly,newh));

		oldbasex[tours % freq] = basex;
		oldbasey[tours % freq] = basey;

		move();	
	}
	
	// Méthodes annexes...
	public void broadcastRedZones(double goalx, double goaly, int ttl,double power,String type) {
		double a = towards(goalx,goaly);
														
		double xx = Math.cos(Math.toRadians(a));
		double yy = Math.sin(Math.toRadians(a));			
									
		redzones.addElement(new Obstacle(xx*30,yy*30,ttl,ttl,power,"RedZone"));

		broadcast(groupName,roleName,"eclaireur:"+type,""+(xx*30),""+(yy*30));
	}
		

	public void ajoutRedZones(double goalx, double goaly, int ttl,double power) {
		double a = towards(goalx,goaly);
														
		double xx = Math.cos(Math.toRadians(a));
		double yy = Math.sin(Math.toRadians(a));			
									
		redzones.addElement(new Obstacle(xx*30,yy*30,ttl,ttl,power,"RedZone"));
	}

	public void filtreRedZones(double t,double dist) {
		Vector newredzones = new Vector();
		for(int i=0; i<redzones.size(); i++) {
			double x = ((Obstacle) (redzones.elementAt(i))).getX();
			double y = ((Obstacle) (redzones.elementAt(i))).getY();
			double d = Math.sqrt(x*x+y*y);
			int l = ((Obstacle) (redzones.elementAt(i))).getLife();
			int ml = ((Obstacle) (redzones.elementAt(i))).getMaxLife();
			double p = ((Obstacle) (redzones.elementAt(i))).getPower();
			
			if ((t < (ml - l)) && (dist > d)) newredzones.addElement(new Obstacle(x,y,l,ml,p,"RedZone"));			
		}
		
		redzones = newredzones;
	}
		

	public void majRedZones(double h) {
		
		Vector newredzones = new Vector();
		
		for(int i=0; i<redzones.size(); i++) {
			double a = Math.toRadians(h);
			double x = ((Obstacle) (redzones.elementAt(i))).getX();
			double y = ((Obstacle) (redzones.elementAt(i))).getY();
			int l = ((Obstacle) (redzones.elementAt(i))).getLife();
			int ml = ((Obstacle) (redzones.elementAt(i))).getMaxLife();
			double p = ((Obstacle) (redzones.elementAt(i))).getPower();
			if (ml > 0) p = p*l/ml;
			
			if (l != 0) newredzones.addElement(new Obstacle(x-Math.cos(a)*2,y-Math.sin(a)*2,l-1,p,"RedZone"));
			
		}
		
		redzones = newredzones;		
	}
		

	public double evitement(Percept[] percepts, double goalx, double goaly, double h) {
		Obstacle[] obs = new Obstacle[percepts.length];
		
		for (int i = 0; i < percepts.length ;i++)
			if (percepts[i].getPerceptType() == null)
				obs[i] = new Obstacle(percepts[i].getX(),percepts[i].getY(),"Wall");
			else obs[i] = new Obstacle(percepts[i].getX(),percepts[i].getY(),percepts[i].getPerceptType());
						
		return evitement(obs, goalx, goaly, h);
	}

	public double contournement(double goalx, double goaly, double h) {
		Obstacle[] obs = new Obstacle[redzones.size()];

		for (int i = 0; i < redzones.size();i++)
			obs[i] = (Obstacle) redzones.elementAt(i);

		return evitement(obs, goalx, goaly, h);
	}

	public boolean isMoving(double x1, double y1,double x2, double y2) {
		return (isMoving() && (Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2)) > 5)); 
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
	  			if (info) System.out.println("Obstacle "+i+" : ");
	  			
				Obstacle p = obs[i];
				double xp = p.getX();
				double yp = p.getY();
				
				double dp = Math.sqrt(xp*xp + yp*yp);
						
				xp = xp/dp;
				yp = yp/dp;
				double force = 40/dp; //formule de la force 
				
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

class Engagement {
	String nature;
	int val;
	
	Engagement() {
		nature = "";
		val = 0;
	}
	
	public void decr() {
		if (val > 0) val--;
		else nature="";
	}
	public void init(int a, String s) {
		val = a;
		nature = s;
	}
}
	