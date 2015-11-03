/*
* Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package warbot.GGG;

import warbot.kernel.*;
import madkit.kernel.*;

/* GG_Warbot  fonctions génériques de tous les types de warbot RocketLauncher, Explorer et Home */
public abstract class GGG_WB_Mobile extends GGG_WB {
	
	private static final double FACTEUR_TACT_ATTEINDRE = 1.0;

	// Mémorisation du nombre de tours précédents sans déplacement
	int aPasBouge = 0;

	// L'objectif de l'agent en rapport avec sa tactique
	protected GGG_Target tactTgt = new GGG_Target();

	// L'objectif de l'agent en rapport avec son but
	protected GGG_Target butTgt = new GGG_Target();

	// L'objectif de l'agent en rapport avec la réalisation de son but
	protected GGG_Target actionTgt = new GGG_Target();

	// Id du groupe de l'agent
	protected	int groupe = -1;

	// ID du sergent si on pas sergent
	GGG_Target sergent = new GGG_Target();


	// ----------------------------------------------
  //  Constructeur
  // ----------------------------------------------
	public GGG_WB_Mobile() {
    super ();
  }
	
	// ----------------------------------------------
  //  Initialisationde l'agent
  // ----------------------------------------------
	public void activate() {
    super.activate ();
    randomHeading();
		tactTgt = new GGG_Target(getHeading(), 1.0);
		//tgt.somme(tmp); // Gil:???
	}
	
  // ----------------------------------------------
  //  Boucle principale d'acion de l'agent
  // ----------------------------------------------
	public void doIt() {
		super.doIt();
	}

  // ----------------------------------------------
	// Gestion générique des états internes pour les bots mobiles
	// ----------------------------------------------  
	protected void introspecter() {
		super.introspecter();

		// Sin on est bloqué
		if (!isMoving()) {
			aPasBouge++;
		}

		// Rétablir les coordonnées des cibles par rapport au déplacement effectué
		tactTgt = retablirCible(tactTgt, actionTgt);
		butTgt = retablirCible(butTgt, actionTgt);
  }

	// ----------------------------------------------
	//	Recalcul du rôle de l'agent
  //		Gestion des changement de rôle
  //		et des actions à faire en cas de changement de rôle
	// ----------------------------------------------
	protected void actualiserRole () {
    super.actualiserRole();	}

  // ----------------------------------------------
  //  Réalisation du rôle
  //		Traitement propre au rôle
  // ----------------------------------------------
	protected void effectuerRole () {
		super.effectuerRole();
	}

	// ----------------------------------------------
	// Recalcul de la tactique de l'agent
  //		Gestion des changement de tactique
  //		et des actions à faire en cas de changement de tactique
	// ----------------------------------------------
	protected void actualiserTactique () {
		super.actualiserTactique();
	}

  // ----------------------------------------------
  //  Réalisation de la tactique
  //		Traitement propre à la tactique
	// ----------------------------------------------
	protected void effectuerTactique () {
		super.effectuerTactique();
	}

	// ----------------------------------------------
	// Recalcul du but de l'agent
  //		Gestion des changement de but
  //		et des actions à faire en cas de changement de but
	// ----------------------------------------------
	protected void actualiserBut () {
		super.actualiserBut();
	}

  // ----------------------------------------------
  //  Réalisation du but
  //		Traitement propre au but
  // ----------------------------------------------
	protected void effectuerBut () {
		super.effectuerBut();
	}
	
	
	//******************************************************
	// Procédure qui recalcule la distance et l'angle pour atteindre une cible
	// après un déplacement
	//********************************************************	
	protected GGG_Target retablirCible(GGG_Target but, GGG_Target dep) {
		double angleBut=towards(but.x,but.y);
		double distBut=distanceTo(but.x,but.y);
		double angleDep=towards(-dep.x,-dep.y);
		double distDep=getCoveredDistance();
		
		double x;
		double y;
		
		GGG_Target nouveauBut;
		
		// Si le but est un point fixe
		if (but.type > 0)	{
		x = (Math.cos(toRadian(angleBut))*distBut) + (Math.cos(toRadian(angleDep))*distDep);
		y = (Math.sin(toRadian(angleBut))*distBut) + (Math.sin(toRadian(angleDep))*distDep);
		
		distBut = Math.sqrt((x*x) + (y*y));
		angleBut = towards(x,y);
		
		nouveauBut = new GGG_Target (angleBut, distBut);
		nouveauBut.type = but.type;
		nouveauBut.id = but.id;
		}
		else {
			nouveauBut = but;
		}
		return nouveauBut;
	}

	//********************************************************
	// Fonction qui retourne le vecteur à prendre pour aller 
	// vers le point x, y
	//********************************************************
	private double rectifierAngle(double x, double y, double[] arcs) {
		double angle=towards(x, y);
		//System.out.println("Angle a prendre="+angle);
		for ( int i=0; i<arcs.length; i+=2 ) {
			if ( arcs[i] < 0 ) {
				if ( (angle>arcs[i] && angle<arcs[i+1]) || (angle-360>arcs[i] && angle-360<arcs[i+1]) ) {
					if ( angle-arcs[i] > arcs[i+1]-angle ) {
						angle=arcs[i+1];
					}
					else {
						angle=arcs[i];
					}
				}
			}
			else {
				if ( (angle>arcs[i] && angle<arcs[i+1]) ) {
					if ( angle-arcs[i] > arcs[i+1]-angle ) {
						angle=arcs[i+1];
					}
					else {
						angle=arcs[i];
					}
				}
			}		
		}
		//System.out.println("Angle pris="+angle);
		return angle;
	}

	//******************************************************
	// Procédure qui recalcule la distance et l'angle pour atteindre une cible
	// après un déplacement
	//********************************************************	
	protected GGG_Target orienterAvecCorrection (GGG_Target but, int pri) {
		// Si c'est un objet fixe
		GGG_Target newTgt;

		newTgt = new GGG_Target(rectifierAngle(but.x, but.y, arcsInterdits(pri)), distanceTo (but.x, but.y));
		setHeading(towards(newTgt.x, newTgt.y));
		newTgt.type = but.type;
		newTgt.id = but.id;

		return newTgt;
	}

	//******************************************************
	// Procédure qui recalcule la distance et l'angle pour atteindre une cible
	// après un déplacement
	//********************************************************	
	protected GGG_Target orienterOpposeAvecCorrection (GGG_Target but, int pri) {
		// Si c'est un objet fixe
		GGG_Target newTgt;

		newTgt = new GGG_Target(rectifierAngle(-but.x, -but.y, arcsInterdits(pri)), distanceTo (-but.x, -but.y));
		setHeading(towards(newTgt.x, newTgt.y));
		newTgt.type = but.type;
		newTgt.id = but.id;

		return newTgt;
	}

	//********************************************************
	// Fonction qui retourne l'indice du percept que l'on 
	// touche dans le tableau prcptAmiRL
	//********************************************************
	protected int toucheUnAmi() {
		Percept e;

		for( int i=0; i < prcpt.nbrAmiRL; i++ ) {
			e = prcpt.prcptAmiRL[i];
			if( e.getDistance() < e.getRadius()+2.0 ) {
				return i;
			}
		}
		return -1;
	}
	//********************************************************
	// Fonction qui retourne vrai si la cible est en vue
	//********************************************************
	protected boolean enVue(GGG_Target t)	{
		Percept e;

		for( int i=0; i < prcpt.nbrAmiRL; i++ ) {
			e = prcpt.prcptAmiRL[i];
			if( e.getAgent()==t.id && e.getDistance() < 45 ) {
				return true;
			}
		}
		return false;
	}

	//********************************************************
	// Fonction qui retourne un vecteur contenant les arcs 
	// interdits pour le percept p
	//********************************************************
	protected double[] arcInterdit(Percept p) {
		double[] arcs=new double[2];
		double d, angle, angle2;
		d=p.getDistance();
		angle=toDegre(Math.asin((p.getRadius()+12)/d));

		if ( !(angle < 360 && angle > 0 ) ) { //Si anglevaut NaN
			angle=90;
		}
		arcs[0]=modulo(towards(p.getX(),p.getY())-angle,360);
		arcs[1]=modulo(towards(p.getX(),p.getY())+angle,360);
		//System.out.println("Détecté " + p.getPerceptType() + " angle" + towards(p.getX(),p.getY()) + " Radius" + p.getRadius()
    //  + " Angle interdit="+arcs[0]+" "+arcs[1]);
		return arcs;
	}
	//********************************************************
	// Fonction qui retourne un vecteur contenant les arcs 
	// interdits (bloqués par un obstacle, RL Ex ou Home ami)
	//********************************************************
	protected double[] arcsInterdits(int priorite) {
		double[] arcs=new double[4*prcpt.nbrAmiRL+4*prcpt.nbrAmiEx+4*prcpt.nbrAmiHome+4*prcpt.nbrObstacle];
		double[] arc=new double[2];
		double normX, normY, arcMin, arcMax;
		int i,j=0;
		
		if (priorite < 2) { 
			for ( i=0; i<prcpt.nbrAmiRL; i++ ) {
				arc=arcInterdit(prcpt.prcptAmiRL[i]);
				if ( arc[0] > arc[1] ) {
					arcs[j]=arc[0]; 
					j++;
					arcs[j]=360.0; 
					j++;
					arcs[j]=0.0;
					j++;
					arcs[j]=arc[1];
					j++;
				}
				else {
					arcs[j]=arc[0];
					j++;
					arcs[j]=arc[1];
					j++;
				}
			}
		}
		if (priorite < 1) {
			for ( i=0; i<prcpt.nbrAmiEx; i++ ) {
				arc=arcInterdit(prcpt.prcptAmiEx[i]);
				if ( arc[0] > arc[1] ) {
					arcs[j]=arc[0]; 
					j++;
					arcs[j]=360.0; 
					j++;
					arcs[j]=0.0;
					j++;
					arcs[j]=arc[1];
					j++;
				}
				else {
					arcs[j]=arc[0];
					j++;
					arcs[j]=arc[1];
					j++;
				}
			}
		}
		for ( i=0; i<prcpt.nbrAmiHome; i++ ) {
			arc=arcInterdit(prcpt.prcptAmiHome[i]);
			if ( arc[0] > arc[1] ) {
					arcs[j]=arc[0]; 
					j++;
					arcs[j]=360.0; 
					j++;
					arcs[j]=0.0;
					j++;
					arcs[j]=arc[1];
					j++;
				}
				else {
					arcs[j]=arc[0];
					j++;
					arcs[j]=arc[1];
					j++;
				}
		}	
		for ( i=0; i<prcpt.nbrObstacle; i++ ) {
			arc=arcInterdit(prcpt.prcptObstacle[i]);
			if ( arc[0] > arc[1] ) {
					arcs[j]=arc[0]; 
					j++;
					arcs[j]=360.0; 
					j++;
					arcs[j]=0.0;
					j++;
					arcs[j]=arc[1];
					j++;
				}
				else {
					arcs[j]=arc[0];
					j++;
					arcs[j]=arc[1];
					j++;
				}
		}	
		// Elimination des arcs qui se croisent
		double min, max;
		for ( i=0; i<arcs.length; i=i+2 ) {
			for ( j=i+2; j<arcs.length; j=j+2 ) {
				//System.out.println("Union de " + (int)arcs[i] + "-" + (int)arcs[i+1] + " et " + (int)arcs[j] + "-" + (int)arcs[j+1]);
				min = max =-361;
				/*if ( arcs[i] > arcs[i+1] ) {
					arcs[i]-=360;
				}
				if ( arcs[j] > arcs[j+1] ) {
					arcs[j]-=360;
				}*/
				if ( arcs[i] > arcs[j] && arcs[i] < arcs[j+1] ) {
					min=arcs[j];
				}
				if ( arcs[i+1] > arcs[j] && arcs[i+1] < arcs[j+1] ) {
					max=arcs[j+1];
				}
				if ( arcs[j] > arcs[i] && arcs[j] < arcs[i+1] ) {
					min=arcs[i];
				}
				if ( arcs[j+1] > arcs[i] && arcs[j+1] < arcs[i+1] ) {
					max=arcs[i+1];
				}
				if ( min < 0 ) {
					min+=360;
				}
				if ( min > 0 && max > 0 ) { // les arcs se coupent
					arcs[i]=arcs[j]=min;
					arcs[i+1]=arcs[j+1]=max;
					//System.out.println("= " + (int)min + "-" + (int)max);
				}
			}			
		}
		return arcs;
	}


	//********************************************************
	// Décalage en spirale
	//********************************************************
	protected void spirale( GGG_Target t, double borneMinSpiral, double borneMaxSpiral, double augAngleSpirale, double augDistanceSpirale) {
		double angle=towards(t.x,t.y);
		double dist=distanceTo(t.x,t.y);
		
		if (dist < borneMaxSpiral) {
			dist += augDistanceSpirale;
		}
		else if (dist < borneMinSpiral) {
			dist = borneMinSpiral;
		}
		angle = modulo(angle + augAngleSpirale,360);
		t.x = Math.cos(toRadian(angle))*dist;
		t.y = Math.sin(toRadian(angle))*dist;
	}

	// ----------------------------------------------
  //  Calcul du déplacement pour tourner autour d'un point
  // ----------------------------------------------
	protected GGG_Target tournerAutour (GGG_Target tgt, int distMin, int distMax) {
		GGG_Target dep = new GGG_Target();

		// Si on est trop prêt on s'éloigne
		if (distanceTo(tgt.x, tgt.y) < distMin) {
			dep.x = -tgt.x;
			dep.y = -tgt.y;
		}
		else {
			// Si on est trop loin on se rapproche
			if (distanceTo(tgt.x, tgt.y) > distMax) {
				dep.x = tgt.x;
				dep.y = tgt.y;
			}
			// Sinon, on tourne autour
			else {
				dep.x = -tgt.y;
				dep.y = tgt.x;
			}
		}
		dep.type = tgt.type;
		dep.id = tgt.id;

		return dep;
	}

	// ----------------------------------------------
	// Suivre quelqu'un à distance
	// ----------------------------------------------
	protected GGG_Target suivre(GGG_Target t, double dist) {
		GGG_Target tgt = new GGG_Target();
		
		if (distanceTo (t.x, t.y) < dist) {
			tgt.x = - t.x;
			tgt.y = - t.y;
		}
		else {
			tgt = t;
		}

		tgt.type = t.type;

		return tgt;
	}
		
	// ----------------------------------------------
  //  Calcul du vecteur d'evitement
  // ----------------------------------------------
	protected void calculerEvitement (GGG_Target v) {
		int i;
		double d;

		// Obstacles
		for (i = 0; i < prcpt.nbrObstacle; i++) {
			d = prcpt.prcptObstacle[i].getDistance();
			if (d != 0) {
				v.x += prcpt.prcptObstacle[i].getX()/d*(150-d)/150;
				v.y += prcpt.prcptObstacle[i].getY()/d*(150-d)/150;
			}
		}

		for (i = 0; i < prcpt.nbrEnnemiRL; i++) {
			d=prcpt.prcptEnnemiRL[i].getDistance();
			if (d != 0) {
				v.x += prcpt.prcptEnnemiRL[i].getX()/d*(150-d)/75;
				v.y += prcpt.prcptEnnemiRL[i].getY()/d*(150-d)/75;
			}
		}

		for (i=0; i < prcpt.nbrAmiRL; i++) {
			d = prcpt.prcptAmiRL[i].getDistance();
			if (d != 0) {
				v.x += prcpt.prcptAmiRL[i].getX()/d*(150-d)/100;
				v.y += prcpt.prcptAmiRL[i].getY()/d*(150-d)/100;
			}
		}

		for (i=0; i < prcpt.nbrAmiEx; i++) {
			d=prcpt.prcptAmiEx[i].getDistance();
			if (d != 0) {
				v.x += prcpt.prcptAmiEx[i].getX()/d*(150-d)/300;
				v.y += prcpt.prcptAmiEx[i].getY()/d*(150-d)/300;
			}
		}

		for (i=0; i < prcpt.nbrAmiHome; i++) {
			d = prcpt.prcptAmiHome[i].getDistance();
			if (d != 0) {
				v.x += prcpt.prcptAmiHome[i].getX()/d*(150-d)/300;
				v.y += prcpt.prcptAmiHome[i].getY()/d*(150-d)/300;
			}
		}

		for (i = 0; i < prcpt.nbrRocket; i++) {
			d = prcpt.prcptRocket[i].getDistance();
			if (d != 0) {
				if (d > 100) {
					v.x -= prcpt.prcptRocket[i].getY()/d*(150-d)/150;
					v.y += prcpt.prcptRocket[i].getX()/d*(150-d)/150;
				}
				else {
					v.x += prcpt.prcptRocket[i].getX()/d*(150-d)/150;
					v.y += prcpt.prcptRocket[i].getY()/d*(150-d)/150;
				}
			}
		}
		v.x = v.x * 100;
		v.y = v.y * 100;
	}
}
