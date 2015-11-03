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
//import madkit.lib.simulation.*;

// Warbot internal percept structure
public class GGG_Percept {

	static final int MAX_PERCEPT_HOME 		= 20;
	static final int MAX_PERCEPT_RL 			= 40;
	static final int MAX_PERCEPT_EX 			= 20;
	static final int MAX_PERCEPT_OBSTACLE	= 40;
	static final int MAX_PERCEPT_ROCKET 	= 100;

	int nbrEntite = 0;
	
	Percept nearest;

	int nbrObstacle = 0;
	Percept[] prcptObstacle = new Percept[MAX_PERCEPT_OBSTACLE];
	
	int nbrRocket = 0;
	Percept[] prcptRocket = new Percept[MAX_PERCEPT_ROCKET];

	int nbrEnnemi = 0;
	int nbrEnnemiRL = 0;
	Percept[] prcptEnnemiRL = new Percept[MAX_PERCEPT_RL];
	int nbrEnnemiEx = 0;
	Percept[] prcptEnnemiEx = new Percept[MAX_PERCEPT_EX];
	int nbrEnnemiHome = 0;
	Percept[] prcptEnnemiHome = new Percept[MAX_PERCEPT_HOME];

	int nbrAmi = 0;
	int nbrAmiRL = 0;
	Percept[] prcptAmiRL = new Percept[MAX_PERCEPT_RL];
	int nbrAmiEx = 0;
	Percept[] prcptAmiEx = new Percept[MAX_PERCEPT_EX];
	int nbrAmiHome = 0;
	Percept[] prcptAmiHome = new Percept[MAX_PERCEPT_HOME];

	// Constructeur
	public GGG_Percept() {}

	public void reset() {
		nbrEntite = 0;
		nearest = null;
		nbrObstacle = 0;
		nbrRocket = 0;
		nbrEnnemi = 0;
		nbrEnnemiRL = 0;
		nbrEnnemiEx = 0;
		nbrEnnemiHome = 0;
		nbrAmi = 0;
		nbrAmiRL = 0;
		nbrAmiEx = 0;
		nbrAmiHome = 0;
	}

	// ----------------------------------------------
  // Voir et enregistrer toutes les entités prôches
	// ----------------------------------------------
  public void voir (GGG_WB wb) {
		Percept[] detectedEntities = wb.getPercepts();
			
		int nbrPercept, nearest = 0;
		double dist;
		Percept[] tmpPercept;

		nbrPercept = detectedEntities.length;
		tmpPercept = new Percept[nbrPercept];

		reset();

		// Je sais c'est pas optimisé, pas le temps !

		for (int j = 0; j < nbrPercept; j++) {
			for (int i = 0; i < nbrPercept; i++) {
				dist = 10000;

				if (detectedEntities[i] != null) {
					if (dist > wb.distanceTo(detectedEntities[i])) {
						dist = wb.distanceTo(detectedEntities[i]);
						nearest = i;
					}
				}
			}

			// Store percept in agent percept structure
			addEntity (detectedEntities[nearest]);
			detectedEntities[nearest] = null;
		}
  }

	// ----------------------------------------------
  //  Store the percept p in the appropriate vector
  // ----------------------------------------------
	private void addEntity(Percept p) {

		nbrEntite++;

		if ( nearest == null || p.getDistance() < nearest.getDistance() ) {
				nearest = p;
		}

		//System.out.println("Percept : " + p.getPerceptType());


		// L'un des notres ?
		if (p.getTeam().equals(GGG_WB.TEAM)) {
			// De quel type ?
			nbrAmi++;

			if ((p.getPerceptType().equals("Home")) && (nbrAmiHome < MAX_PERCEPT_HOME)) {
				prcptAmiHome[nbrAmiHome] = p;
				nbrAmiHome++;
			}
			else if ((p.getPerceptType().equals("RocketLauncher"))	&& (nbrAmiRL < MAX_PERCEPT_RL)) {
				prcptAmiRL[nbrAmiRL] = p;
				nbrAmiRL++;
			}
			else if ((p.getPerceptType().equals("Explorer"))	&& (nbrAmiEx < MAX_PERCEPT_EX)) {
				prcptAmiEx[nbrAmiEx] = p;
				nbrAmiEx++;
			}
			else {
				System.out.println("Ami de type inconnu !!!");
			}
		}
		else {

			if ((p.getPerceptType().equals("Obstacle")) && (nbrObstacle < MAX_PERCEPT_OBSTACLE)) {
				prcptObstacle[nbrObstacle] = p;
				nbrObstacle++;
			}
			else if ((p.getPerceptType().equals("Rocket"))	&& (nbrRocket < MAX_PERCEPT_ROCKET)) {
				prcptRocket[nbrRocket] = p;
				nbrRocket++;
			}

			else {
				nbrEnnemi++;
				if ((p.getPerceptType().equals("Home")) && (nbrEnnemiHome < MAX_PERCEPT_HOME)) {
					prcptEnnemiHome[nbrEnnemiHome] = p;
					nbrEnnemiHome++;
				}
				else if ((p.getPerceptType().equals("RocketLauncher"))	&& (nbrEnnemiRL < MAX_PERCEPT_RL)) {
					prcptEnnemiRL[nbrEnnemiRL] = p;
					nbrEnnemiRL++;
				}
				else if ((p.getPerceptType().equals("Explorer"))	&& (nbrEnnemiEx < MAX_PERCEPT_EX)) {
					prcptEnnemiEx[nbrEnnemiEx] = p;
					nbrEnnemiEx++;
				}
				else {
					System.out.println("Ennemi de type inconnu !!! " + p.getPerceptType());
					nbrEntite--;
				}
			}
		}
	}
}
