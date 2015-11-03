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
import java.util.*;

/* Rocket Launcher */
public class GGG_Home extends GGG_WB {

	final static double DIST_PROCHE_BASE = 300;
	final static double DIST_LOIN_BASE = 1000;
	

	Vector affect=new Vector();

	// Constructeur
	public GGG_Home() {
    super ();
    myType = BOT_TYPE_Home;
		nbrMaxHome = 1;
		nbrHome = 1;
  }

	public void activate() {
		super.activate();
		requestRole(TEAM, ROLE_STR[role], null);
	}


	public void doIt() {
    super.doIt();
	}
	
	// ----------------------------------------------
	//	Recalcul du rôle de l'agent
  //		Gestion des changement de rôle
  //		et des actions à faire en cas de changement de rôle
	// ----------------------------------------------
	protected void actualiserRole () {
    super.actualiserRole();

		switch (role) {
		case ROLE_INCONNU:
			bAL.annoncer (ALL_ROLES, ALL_GROUPS, GGG_Msg.MSG_PRES_HOME, 0.0, 0.0);
			if (nbrMaxHome == 1) {
				role = ROLE_BASE_CHEF;
				requestRole( TEAM, ROLE_STR[role], null);
				setUserMessage("Base principale");
			}
			else {
				role = ROLE_BASE;
				requestRole( TEAM, ROLE_STR[role], null);
				setUserMessage("Base secondaire");
			}
			break;
		}
	}

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

		switch (role) {
		case ROLE_BASE:
			tactique = TACT_SURVEILLER;
			break;

		case ROLE_BASE_CHEF:
			tactique = TACT_ORGANISER;
			break;
		}
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

		switch (tactique)	{
		case TACT_SURVEILLER:
			but = BUT_OBSERVER;
			break;
		
		case TACT_ORGANISER:
			but = BUT_ORGANISER;
			break;
		}
	}

  // ----------------------------------------------
  //  Réalisation du but
  //		Traitement propre au but
  // ----------------------------------------------
	protected void effectuerBut () {
		super.effectuerBut();

		switch (but) {
		case BUT_OBSERVER:
			break;

		case BUT_ORGANISER:
			affecterGroupe();
			
			for ( int i=0; i < bAL.nbrAnnonce; i++ ) {
				if ( bAL.annonce[i].msgId == GGG_Msg.MSG_RASSEMBLE ) {
					// Si le groupe rassemble est trop loin on lui demande de revenir
					double distGroupe = distanceTo (bAL.annonce[i].senderX, bAL.annonce[i].senderY);
					if (distGroupe > DIST_LOIN_BASE) {
						bAL.ordonner (bAL.annonce[i].sender, GGG_Msg.MSG_REJOINDRE_POINT,
							bAL.annonce[i].senderX*(DIST_PROCHE_BASE / distGroupe),
							bAL.annonce[i].senderY*(DIST_PROCHE_BASE / distGroupe));
					}
					else if (distGroupe < DIST_PROCHE_BASE) {
						bAL.ordonner (bAL.annonce[i].sender, GGG_Msg.MSG_REJOINDRE_POINT,
							bAL.annonce[i].senderX*(((DIST_PROCHE_BASE + DIST_LOIN_BASE)/2)/ distGroupe),
							bAL.annonce[i].senderY*(((DIST_PROCHE_BASE + DIST_LOIN_BASE)/2)/ distGroupe));
					}
      	}
			}
			break;
		}

 		bAL.sendAll(this);
	}


	// ----------------------------------------------
  //  Affectation des escouade de RL
  // ----------------------------------------------
	private void affecterGroupe() {

		affect.clear();
		for ( int i=0; i < bAL.nbrAnnonce; i++ ) {
			if ( bAL.annonce[i].msgId == GGG_Msg.MSG_PRES_RL ) {
				affect.add(bAL.annonce[i].sender);
			}
		}
		
		if( affect.size() != 0 ) {
			int nbGroupe = affect.size() / TAILLE_ESCOUADE;
			int reste = affect.size() % TAILLE_ESCOUADE;
			//On forme des groupes de TAILLE_ESCOUADE agents
			int numGroupe=1;
			int i;
			while( numGroupe != nbGroupe+1 ) {
				for( i=(numGroupe-1) * TAILLE_ESCOUADE; i < numGroupe * TAILLE_ESCOUADE; i++ ) {							
					if( i == (numGroupe - 1) * TAILLE_ESCOUADE ) {
						System.out.println ("Ordre d'affectation d'un sergent : " + (AgentAddress) affect.elementAt(i));
						bAL.ordonner((AgentAddress) affect.elementAt(i), GGG_Msg.MSG_AFFECTATION , ROLE_SERGENT, numGroupe);				

					}
					else {
						System.out.println ("Ordre d'affectation d'un soldat : " + (AgentAddress) affect.elementAt(i));
						bAL.ordonner((AgentAddress) affect.elementAt(i), GGG_Msg.MSG_AFFECTATION, ROLE_SOLDAT, numGroupe);							
					}
			 	}
			 	numGroupe=numGroupe+1;
			 }
			 if( reste != 0 ) {
			 	numGroupe=1;
			 	for( i = nbGroupe * TAILLE_ESCOUADE; i < affect.size(); i++ ) {
					System.out.println ("Ordre d'affectation d'un soldat : " + (AgentAddress) affect.elementAt(i));
			 		bAL.ordonner((AgentAddress) affect.elementAt(i), GGG_Msg.MSG_AFFECTATION, ROLE_SOLDAT,numGroupe);							
			 		if( numGroupe == nbGroupe || nbGroupe == 0 ) {
			 			numGroupe=1;
			 		}
			 		else {
			 			numGroupe=numGroupe+1;
			 		}
			 	}	
			}
			System.out.println ("Affectation de " + affect.size() + " RL");
		}
	}
}