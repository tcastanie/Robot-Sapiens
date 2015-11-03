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
import java.util.*;
//import madkit.lib.simulation.*;
//import madkit.lib.messages.ACLMessage;

/* Rocket Launcher */
public class GGG_RocketLauncher extends GGG_WB_Mobile {

  // Un certain temps
  private static final int DELAI_ROCKET			= 3;
	private static final int DISTANCE_COMBAT_SOLDAT = 180;
	private static final int DISTANCE_COMBAT_SERGENT = 180;

	// angle de dispersion des roquettes
	private static final double ANGLE_TIR_Home = 0.0;
	private static final double ANGLE_TIR_RL = 10.0;
	private static final double ANGLE_TIR_Ex = 60.0;

	// Distance à laquelle suivre le sergent
	final static int DIST_SERGENT = 45;
	
	// Parametres de la spirale de deplacement
	final static double BORNE_MAX_SPIRALE = 4000;
	final static double AUGMENTATION_DISTANCE_SPIRALE= 20;
	final static double BORNE_MIN_SPIRALE = 2000;
	final static double AUGMENTATION_ANGLE_SPIRALE = 1;
	
	// Délai d'attente pour tirer une roquette
	int waitRocket = 0;

	// Equipe de l'agent
	String idGroupe = "Groupe inconnu";
	String idRole = ROLE_STR[role];
		
	// Presence d'un explorateur avec le groupe
	private boolean presenceEx = false;
	private int distanceCombat;

/* C quoi ça ???
	public boolean deteded(AgentAddress a) {
		Percept[] detectedEntities = getPercepts();
		for( int i=0;i<detectedEntities.length;i++)	{
			Percept e=detectedEntities[i];
			if ( e.getAgent() == a ) {
					return true;
				}
		}
		return false;
*/


	// ----------------------------------------------
  //  Constructeur
  // ----------------------------------------------
	public GGG_RocketLauncher() {
    myType = BOT_TYPE_RL;
		nbrMaxRL = 1;
		nbrRL = 1;
		sergent.type = TGT_TYPE_RL;
	}
	
  // ----------------------------------------------
  //  Initialisationde l'agent
  // ----------------------------------------------
	public void activate() {
		super.activate();
	}
	
  // ----------------------------------------------
  //  Boucle principale d'acion de l'agent
  // ----------------------------------------------
	public void doIt() {
    super.doIt();
		setUserMessage(ROLE_STR[role] + ":" + TACT_STR[tactique] + ":" + BUT_STR[but] + "." + nbrGroupe);
	}
	// ----------------------------------------------
  //  Mort de l'agent
  // ----------------------------------------------
	public void end()	{
		if (role == ROLE_SOLDAT) {
			bAL.annoncer(ROLE_SERGENT, groupe, GGG_Msg.MSG_MORT, role,0.0);
			System.out.println ("Sergent: Je meurs, groupe " + groupe);
		}
		else {
			bAL.annoncer(ALL_ROLES, groupe, GGG_Msg.MSG_MORT, role,0.0);
			System.out.println ("Soldats: Je meurs, groupe " + groupe);
		}
		bAL.sendAll(this);
	}


	// ----------------------------------------------
	// Mise à jour des états interne
	// ----------------------------------------------
	protected void introspecter () {
    super.introspecter();
    
    if (waitRocket > 0) {
    	waitRocket--;
    }
	}


	// ----------------------------------------------
	//	Recalcul du rôle de l'agent
  //		Gestion des changement de rôle
  //		et des actions à faire en cas de changement de rôle
	// ----------------------------------------------
	protected void actualiserRole () {

    super.actualiserRole();

		// Affectation d'un role à agent par la base
		for (int i=0; i < bAL.nbrOrdre; i++) {
			if ( bAL.ordre[i].msgId == GGG_Msg.MSG_AFFECTATION ) {
				role = (int)bAL.ordre[i].arg1;
				groupe = (int) bAL.ordre[i].arg2;
				createGroup(true, GGG_Msg.GROUPE_ESCOUADE_x + groupe, null, null);
				requestRole(GGG_Msg.GROUPE_ESCOUADE_x + groupe, ROLE_STR[role], null);
				if (role == ROLE_SERGENT) {
					distanceCombat = DISTANCE_COMBAT_SERGENT;
					// Le sergent demande le rassemblement
					tactique = TACT_INITIALISER;
					cptDureeTactique = 3;
					bAL.requerir(ROLE_ECLAIREUR, groupe, GGG_Msg.MSG_PRES_SERGENT, 0.0, 0.0);
				}
				else {
					distanceCombat = DISTANCE_COMBAT_SOLDAT;
				}
			}
		}

		// Mort d'un agent, si c'est le chef et qu'il n'y en a pas de nouveau on prends sa place
		for ( int i=0; i < bAL.nbrAnnonce; i++ ) {
			if (bAL.annonce[i].msgId == GGG_Msg.MSG_MORT) {
				//nbrGroupe--;
				if (bAL.annonce[i].sender == sergent.id) {
					int nouveauSergent = -1;
					// Est ce que l'on reçoit un message d'un nouveau sergent
					for (int j=0; j < bAL.nbrOrdre; j++) {
						if (bAL.ordre[j].msgId == GGG_Msg.MSG_PRES_SERGENT) {
							nouveauSergent = j;
						}
					}

					if (nouveauSergent == -1) {
						// Je suis le chef, je l'annonce
						System.out.println ("Pas d'autre sergent, je quitte le rôle: " + ROLE_STR[role] + " du groupe: " + GGG_Msg.GROUPE_ESCOUADE_x + groupe);
						leaveRole(GGG_Msg.GROUPE_ESCOUADE_x + groupe, ROLE_STR[role], null);
						role = ROLE_SERGENT;
						distanceCombat = DISTANCE_COMBAT_SERGENT;
						// Le sergent demande le rassemblement
						tactique = TACT_INITIALISER;
						cptDureeTactique = 3;
						but = BUT_INCONNU;
						requestRole(GGG_Msg.GROUPE_ESCOUADE_x + groupe, ROLE_STR[role], null);
						System.out.println ("Je suis votre nouveau sergent !!!");
						bAL.ordonner(ROLE_SOLDAT, groupe, GGG_Msg.MSG_PRES_SERGENT, 0.0, 0.0);
						bAL.requerir(ROLE_ECLAIREUR, groupe, GGG_Msg.MSG_PRES_SERGENT, 0.0, 0.0);
					}
					else {
						System.out.println ("Il y a un nouveau sergent, je reste soldat");
						role = ROLE_SOLDAT;
						distanceCombat = DISTANCE_COMBAT_SOLDAT;
						requestRole(GGG_Msg.GROUPE_ESCOUADE_x + groupe, ROLE_STR[role], null);
						// Enregistrement des paramètres du nouveau sergent
						sergent.x = bAL.ordre[nouveauSergent].senderX;
						sergent.y = bAL.ordre[nouveauSergent].senderY;
						sergent.id = bAL.ordre[nouveauSergent].sender;
					}
				}
			}
		}
	}

  // ----------------------------------------------
  //  Réalisation du rôle
  //		Traitement propre au rôle
  // ----------------------------------------------
	protected void effectuerRole () {
		super.effectuerRole();
		
		switch ( role ) {
		case ROLE_INCONNU:
			bAL.annoncer (ALL_ROLES, ALL_GROUPS, GGG_Msg.MSG_PRES_RL, 0.0, 0.0);
			break;		
		
		case ROLE_SERGENT:
			for (int i = 0; i < bAL.nbrAnnonce; i++) {
				// Si un soldate du groupe s'est perdu.
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_PERDU)	{
					// Je suis la !
					bAL.annoncer(bAL.annonce[i].sender, GGG_Msg.MSG_POSITION, 0.0, 0.0);
				}
				// le sergent compte les morts
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_MORT) {
					System.out.println(this + ":Mort d'un membre du groupe !");
					nbrGroupe--;
				}
			}
			break;
			
		case ROLE_SOLDAT:
			// Le sergent se présente, on lui réponds
			for ( int i=0; i< bAL.nbrAnnonce; i++ ) {
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_PRES_SERGENT) {
					bAL.annoncer (bAL.annonce[i].sender, GGG_Msg.MSG_PRES_SOLDAT, 0.0, 0.0);
					sergent.id = bAL.annonce[i].sender;
				}
			}
		}
	}

	// ----------------------------------------------
	// Recalcul de la tactique de l'agent
  //		Gestion des changement de tactique
  //		et des actions à faire en cas de changement de tactique
	// ----------------------------------------------
	protected void actualiserTactique () {
		super.actualiserTactique();

    switch (role) {
		case ROLE_SOLDAT :
			// Une seule tactique pour les soldats
			tactique = TACT_OBEIR;
			break;

    case ROLE_SERGENT :
			// L'initialisation ne peut pas être interrompue avant son terme
			if  (tactique == TACT_INITIALISER) {
				if (cptDureeTactique == 0) {
					tactique = TACT_INCONNU;
					but = BUT_INCONNU;
				}
			}
			else {
				// Si des ennemis sont repéré, on attaque quelquesoit la tactique, après l'initialisation

				if (determinerCibleGroupe (tactTgt)) {
					tactique = TACT_ATTAQUER_MOBILES;
					but = BUT_INCONNU;
				}
				else {
					// Il n'y a plus personne on continue vers le dernier ennemi vu
					if (tactique == TACT_ATTAQUER_MOBILES) {
						tactique = TACT_REJOINDRE_POINT;
						tactTgt = butTgt;
						tactTgt.type = TGT_TYPE_Point;
						cptDureeTactique=(int)distanceTo(tactTgt.x, tactTgt.y);
						bAL.ordonner(ROLE_SOLDAT, groupe, GGG_Msg.MSG_SUIVRE, 0.0, 0.0);
						but = BUT_INCONNU;
					}
			
					// Réception des ordres de la base
					for (int i=0; i < bAL.nbrOrdre; i++) {
						if (bAL.ordre[i].msgId == GGG_Msg.MSG_REJOINDRE_POINT) {
							tactique = TACT_REJOINDRE_POINT;
							tactTgt.type = TGT_TYPE_Point;
							tactTgt.x = bAL.ordre[i].senderX + bAL.ordre[i].arg1;
							tactTgt.y = bAL.ordre[i].senderY + bAL.ordre[i].arg2;
							cptDureeTactique=(int)distanceTo(tactTgt.x, tactTgt.y);
							but = BUT_INCONNU;
						}
					}
			
					// Si on a rejoint le point
					if (tactique == TACT_REJOINDRE_POINT) {
						if (distanceTo(tactTgt.x, tactTgt.y) < 20 || cptDureeTactique==0 )	{
							tactique = TACT_INCONNU;
							but = BUT_INCONNU;
						}
					}
				}
			}

			// Tactique patrouiller par défaut
			if (tactique == TACT_INCONNU)	{
				tactique = TACT_PATROUILLER;
				but = BUT_INCONNU;
			}
      break;
	  }
	}

  // ----------------------------------------------
  //  Réalisation de la tactique
  //		Traitement propre à la tactique
	// ----------------------------------------------
	protected void effectuerTactique () {
		super.effectuerTactique();

    switch (tactique) {
    case TACT_PATROUILLER :
		case TACT_REJOINDRE_POINT :
			break;
			
		case TACT_ATTAQUER_MOBILES :
			bAL.ordonner(ROLE_SOLDAT, groupe, GGG_Msg.MSG_ATTAQUER_MOBILE, tactTgt.x, tactTgt.y);
			break;

		case TACT_OBEIR:
			break;
    }
	}

	// ----------------------------------------------
	// Recalcul du but de l'agent
  //		Gestion des changement de but
  //		et des actions à faire en cas de changement de but
	// ----------------------------------------------
	protected void actualiserBut () {
		super.actualiserBut();

		switch (tactique)	{

		case TACT_INITIALISER :
			switch (but) {
			case BUT_APPELER_ESCOUADE:
				// Si l'action est terminée;
				if (cptDureeBut == 0) {
					System.out.println("Appel à l'escouade terminé, on passe aucomptage");
					but = BUT_COMPTER_ESCOUADE;
				}
				break;

			// But par défaut
			case BUT_INCONNU :
				System.out.println("But inconnu, on passe à l'appel de l'escouade");
				but = BUT_APPELER_ESCOUADE;
				// Action de durée 2 tours fixe;
				cptDureeBut = 2;
				break;
			}
			break;

    case TACT_PATROUILLER :
			if ((but == BUT_RASSEMBLER) && (groupeRassemble()))	{
				but = BUT_DEPLACEMENT;
				bAL.ordonner(ROLE_SOLDAT, groupe, GGG_Msg.MSG_SUIVRE, 0.0, 0.0);
				bAL.annoncer(ROLE_BASE_CHEF, ALL_GROUPS, GGG_Msg.MSG_RASSEMBLE, 0.0, 0.0);
			}
			
			if (but == BUT_DEPLACEMENT) {
				spirale(butTgt, BORNE_MIN_SPIRALE, BORNE_MAX_SPIRALE, AUGMENTATION_ANGLE_SPIRALE, AUGMENTATION_DISTANCE_SPIRALE);
			}

			// But par défaut
			if (but == BUT_INCONNU)	{
				but = BUT_RASSEMBLER;
			}			
			break;

		case TACT_REJOINDRE_POINT:
			but = BUT_DEPLACEMENT;
			butTgt = tactTgt;
			break;

		case TACT_ATTAQUER_MOBILES :
			but = BUT_ATTAQUER_CIBLE;
			butTgt.type = tactTgt.type;
			butTgt.x = tactTgt.x;
			butTgt.y = tactTgt.y;
			break;

		case TACT_OBEIR :
			// Gestion des requetes
			for (int i=0; i < bAL.nbrRequete; i++) {
				// Indiquer sa position sur demande
				if (bAL.requete[i].msgId == GGG_Msg.MSG_POSITION) {
					bAL.annoncer(bAL.requete[i].sender, GGG_Msg.MSG_POSITION, 0.0, 0.0);
				}
			}	
			/*
			// Si on voit un ennemi, hors combat, on le signale au sergent
			if ((prcpt.nbrEnnemiRL > 0) && (but != BUT_ATTAQUER_CIBLE)) {
				for (int i = 0; i < prcpt.nbrEnnemiRL; i++) {
					bAL.annoncer(ROLE_SERGENT, groupe, GGG_Msg.MSG_ENNEMI_RL,
						prcpt.prcptEnnemiRL[i].getX(), prcpt.prcptEnnemiRL[i].getY());
				}
			}*/

			// Gestion des nouveaux ordres
		  for (int i=0; i < bAL.nbrOrdre; i++) {
				// Suivre le sergent
			  if (bAL.ordre[i].msgId == GGG_Msg.MSG_SUIVRE) {
					but = BUT_SUIVRE_AMI;
					butTgt.x = bAL.ordre[i].senderX + bAL.ordre[i].arg1;
				  butTgt.y = bAL.ordre[i].senderY + bAL.ordre[i].arg2;
					butTgt.id = sergent.id;
					butTgt.type = TGT_TYPE_RL;
				}

				// Se rassembler au point indique
			  if (bAL.ordre[i].msgId == GGG_Msg.MSG_RASSEMBLEMENT) {
					but = BUT_RASSEMBLEMENT;
				  butTgt.x = bAL.ordre[i].senderX + bAL.ordre[i].arg1;
				  butTgt.y = bAL.ordre[i].senderY + bAL.ordre[i].arg2;
					butTgt.type = TGT_TYPE_Point;
				}

				// Attaquer le groupe désigné
			  if (bAL.ordre[i].msgId == GGG_Msg.MSG_ATTAQUER_MOBILE) {
					but = BUT_ATTAQUER_CIBLE;
				  butTgt.x = bAL.ordre[i].senderX + bAL.ordre[i].arg1;
				  butTgt.y = bAL.ordre[i].senderY + bAL.ordre[i].arg2;
					butTgt.type = TGT_TYPE_RL;
				}
			}
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
		case BUT_APPELER_ESCOUADE :
			// Délai d'attente pour que les soldats rejoignent le groupe
			if (cptDureeBut == 1)	{
				// Appel de tout les soldats;
				System.out.println("Appel des soldats");
				bAL.annoncer(ROLE_SOLDAT, groupe, GGG_Msg.MSG_PRES_SERGENT, 0.0, 0.0);
				//bAL.requerir(ROLE_EXPLORATEUR, ALL_GROUPS, GGG_Msg.MSG_AFFECTATION, ROLE_ECLAIREUR, groupe);
			}
			else {
				System.out.println("Attente avant appel des soldats");
			}
			break;
			
		case BUT_COMPTER_ESCOUADE :
			// On se compte
			System.out.println("Comptage de l'escouade");
			nbrGroupe = 1;
			// On compte les réponse des soldats
			for ( int i=0; i<bAL.nbrAnnonce; i++ ) {
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_PRES_SOLDAT) {
					System.out.println("1 Soldat se signale");
					nbrGroupe++;
				}
			}
			System.out.println("Escouade de " + nbrGroupe + " RL");
			break;

		case BUT_RASSEMBLER:
			bAL.requerir(ROLE_SOLDAT, groupe, GGG_Msg.MSG_POSITION, 0.0, 0.0);			
			butTgt = determinerPointDeRencontre();
			bAL.ordonner(ROLE_SOLDAT, groupe, GGG_Msg.MSG_RASSEMBLEMENT, butTgt.x, butTgt.y);
			
			actionTgt = orienterAvecCorrection(butTgt, 2);
			move();
			break;
						
		case BUT_SUIVRE_AMI:
			// Si un soldat perds de vue son sergent il l'appelle
			if (!localiserAmi(butTgt)) {
				bAL.annoncer (butTgt.id, GGG_Msg.MSG_PERDU, 0.0, 0.0);
				bAL.localiserSender(butTgt);
			}
			actionTgt = suivre (butTgt, DIST_SERGENT);
			actionTgt = orienterAvecCorrection(actionTgt, 1);
			move();
			break;
			
		case BUT_DEPLACEMENT:
			actionTgt = orienterAvecCorrection(butTgt, 2);
			move();
			break;
			
		case BUT_RASSEMBLEMENT:
			if ( enVue(sergent) ) {
				bAL.annoncer(sergent.id, GGG_Msg.MSG_RASSEMBLE, 0.0, 0.0);
				aPasBouge = 0;
			}
			else {
				actionTgt = orienterAvecCorrection(butTgt, 1);
				move();
			}
			break;

		case BUT_ATTAQUER_CIBLE:
			if (determinerCibleTir(actionTgt)) {
				if (waitRocket == 0) {
					tirer(actionTgt);
					waitRocket = DELAI_ROCKET;
				}
				else {
					// Rechargement des roquettes si on en a plus
					/*if ( getRocketNumber()==0 && getEnergyLevel()>(getMaximumEnergy()/4) ) {
						buildRocket();
						//System.out.println("Je cree une roquette");
					}*/
					// Sinon on se positionne par rapport à la cible individuelle
					if (distanceTo (actionTgt.x, actionTgt.y) < distanceCombat) {
						actionTgt = orienterOpposeAvecCorrection(actionTgt, 1);
						move();
					}
					else {
						actionTgt = orienterAvecCorrection(actionTgt, 1);
						move();
					}
				}
			}
			// Sinon on se positionne par rapport à la cible du groupe
			else {
				if (distanceTo (butTgt.x, butTgt.y) < distanceCombat -10) {
					actionTgt = orienterOpposeAvecCorrection(butTgt, 1);
					move();
				}
				else {
					actionTgt = orienterAvecCorrection(butTgt, 1);
					move();
				}
			}
			break;
		}
		
		// Court-circuit, si bloqué et pas en combat
		if (prcpt.nbrEnnemiRL == 0) {	
			if (aPasBouge > 0) {
				if (!isMoving()) {
					randomHeading();
				}
				else {
					aPasBouge--;
				}
				move();
			}
		}
		bAL.sendAll(this);
	}


	// ----------------------------------------------
	// ----------------------------------------------
	//
	//  Fonctions utilitaires pour les rocketlaunchers
	//
	// ----------------------------------------------
	// ----------------------------------------------

	// ----------------------------------------------
	// Détermine la cible du groupe parmi les ennemis perçues
	// ----------------------------------------------
	private boolean groupeRassemble() {
		boolean rassemble = false;
	  int n = 1;

	  for (int i = 0; i < bAL.nbrAnnonce; i++ ) {
		  if (bAL.annonce[i].msgId == GGG_Msg.MSG_RASSEMBLE) {
				 n++;
		  }
		}
		if (n == nbrGroupe) {
			rassemble = true;
		}

		return rassemble;
	}

	// ----------------------------------------------
	// Détermine le point de rassemblement
	// A refaire
	// ----------------------------------------------
	private GGG_Target determinerPointDeRencontre() {
		GGG_Target tgt = new GGG_Target();
		int nbrMsgPos =1;
		
		for ( int i=0; i<bAL.nbrAnnonce; i++ ) {
			if ( bAL.annonce[i].msgId == GGG_Msg.MSG_POSITION ) {
				tgt.x+=bAL.annonce[i].senderX;
				tgt.y+=bAL.annonce[i].senderY;
				nbrMsgPos++;
			}
		}
		tgt.x=tgt.x/nbrMsgPos;
		tgt.y=tgt.y/nbrMsgPos;
		tgt.type = TGT_TYPE_Point;
		return tgt;
	}

	// ----------------------------------------------
	// Détermine la cible du groupe parmi les ennemis perçues
	// A refaire
	// ----------------------------------------------
	private boolean determinerCibleGroupe(GGG_Target tgt) {
		double tmpX, tmpY, tmpDist;
		double cibleDist = 2000;
		boolean cibleGroupe = false;

  	// RLs visibles
  	if (prcpt.nbrEnnemiRL > 0) {
	  	tgt.x = prcpt.prcptEnnemiRL[0].getX();
	  	tgt.y = prcpt.prcptEnnemiRL[0].getY();
			tgt.type = TGT_TYPE_RL;
			cibleGroupe = true;
			//System.out.println("Ennemi RL en vue !");
	  }

		if (!cibleGroupe) {
			// RLs repérés par un allié
			for (int i=0; i < bAL.nbrAnnonce; i++) {
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_ENNEMI_RL) {
					tmpDist = distanceTo (bAL.annonce[i].senderX + bAL.annonce[i].arg1, bAL.annonce[i].senderY + bAL.annonce[i].arg2);
					tmpX = bAL.annonce[i].senderX + bAL.annonce[i].arg1;
					tmpY = bAL.annonce[i].senderY + bAL.annonce[i].arg2;
					if (tmpDist < cibleDist) {
						tgt.x = tmpX;
						tgt.y = tmpY;
						cibleDist = tmpDist;
						tgt.type = TGT_TYPE_RL;
						cibleGroupe = true;
						//System.out.println("Ennemi RL repéré !");
					}
				}
			}
		}
		
		if (!cibleGroupe) {
  		// Exs visibles
  		if (prcpt.nbrEnnemiEx > 0) {
	  		tgt.x = prcpt.prcptEnnemiEx[0].getX();
	  		tgt.y = prcpt.prcptEnnemiEx[0].getY();
				tgt.type = TGT_TYPE_Ex;
				cibleGroupe = true;
				//System.out.println("Ennemi Home en vue !");
			}
		}

		if (!cibleGroupe) {
			// Exs repérés par un allié
			for (int i=0; i < bAL.nbrAnnonce; i++) {
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_ENNEMI_EX) {
					tmpDist = distanceTo (bAL.annonce[i].senderX + bAL.annonce[i].arg1, bAL.annonce[i].senderY + bAL.annonce[i].arg2);
					tmpX = bAL.annonce[i].senderX + bAL.annonce[i].arg1;
					tmpY = bAL.annonce[i].senderY + bAL.annonce[i].arg2;
					if (tmpDist < cibleDist){
						tgt.x = tmpX;
						tgt.y = tmpY;
						tgt.type = TGT_TYPE_Ex;
						cibleDist = tmpDist;
						cibleGroupe = true;
						//System.out.println("Ennemi Explorateur repéré !");
					}
				}
			}
		}

		if (!cibleGroupe) {
  		// RLs visibles
  		if (prcpt.nbrEnnemiHome > 0) {
	  		tgt.x = prcpt.prcptEnnemiHome[0].getX();
	  		tgt.y = prcpt.prcptEnnemiHome[0].getY();
				tgt.type = TGT_TYPE_Home;
				cibleGroupe = true;
				//System.out.println("Ennemi Home en vue !");
			}
		}

		if (!cibleGroupe) {
			// RLs repérés par un allié
			for (int i=0; i < bAL.nbrAnnonce; i++) {
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_ENNEMI_HOME) {
					tmpDist = distanceTo (bAL.annonce[i].senderX + bAL.annonce[i].arg1, bAL.annonce[i].senderY + bAL.annonce[i].arg2);
					tmpX = bAL.annonce[i].senderX + bAL.annonce[i].arg1;
					tmpY = bAL.annonce[i].senderY + bAL.annonce[i].arg2;
					if (tmpDist < cibleDist){
						tgt.x = tmpX;
						tgt.y = tmpY;
						tgt.type = TGT_TYPE_Home;
						cibleDist = tmpDist;
						cibleGroupe = true;
						//System.out.println("Ennemi Home repéré !");
					}
				}
			}
		}

		return cibleGroupe;
	}


	// ----------------------------------------------
	// Détermine la cible du groupe parmi les ennemis perçues
	// A refaire
	// ----------------------------------------------
	private boolean determinerCibleTir(GGG_Target tgt) {
		double tmpX, tmpY, tmpDist;
		double cibleDist = 500;
		boolean cibleTir = false;

  	// RLs visibles
  	for (int i = 0; i < prcpt.nbrEnnemiRL; i++)	{
	  	tmpX = prcpt.prcptEnnemiRL[i].getX();
	  	tmpY = prcpt.prcptEnnemiRL[i].getY();
			if (tirPossible(tmpX, tmpY, arcsInterdits(0))) {
	  		tgt.x = tmpX;
	  		tgt.y = tmpY;
				tgt.type = TGT_TYPE_RL;
				cibleTir = true;
				//System.out.println("Ennemi RL en vue !");
			}
	  }

		if (!cibleTir) {
			// RLs repérés par un allié
			for (int i=0; i < bAL.nbrAnnonce; i++) {
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_ENNEMI_RL) {
					tmpX = bAL.annonce[i].senderX + bAL.annonce[i].arg1;
					tmpY = bAL.annonce[i].senderY + bAL.annonce[i].arg2;
					tmpDist = distanceTo (tmpX, tmpY);
					if ((tmpDist < cibleDist) && (tirPossible(tmpX, tmpY, arcsInterdits(0)))) {
						tgt.x = tmpX;
						tgt.y = tmpY;
						cibleDist = tmpDist;
						tgt.type = TGT_TYPE_RL;
						cibleTir = true;
						//System.out.println("Ennemi RL repéré !");
					}
				}
			}
		}

		if (!cibleTir) {
			// Bases ennemies visibles
			for (int i = 0; i < prcpt.nbrEnnemiEx; i++)	{
	  		tmpX = prcpt.prcptEnnemiEx[i].getX();
	  		tmpY = prcpt.prcptEnnemiEx[i].getY();
				if (tirPossible(tmpX, tmpY, arcsInterdits(0))) {
	  			tgt.x = tmpX;
	  			tgt.y = tmpY;
					tgt.type = TGT_TYPE_Ex;
					cibleTir = true;
					System.out.println("Ennemi Ex en vue !");				
				}
			}
	  }

		if (!cibleTir) {
			// RLs repérés par un allié
			for (int i=0; i < bAL.nbrAnnonce; i++) {
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_ENNEMI_EX) {
					tmpX = bAL.annonce[i].senderX + bAL.annonce[i].arg1;
					tmpY = bAL.annonce[i].senderY + bAL.annonce[i].arg2;
					tmpDist = distanceTo (tmpX, tmpY);
					if ((tmpDist < cibleDist) && (tirPossible(tmpX, tmpY, arcsInterdits(0)))){
						tgt.x = tmpX;
						tgt.y = tmpY;
						tgt.type = TGT_TYPE_Ex;
						cibleDist = tmpDist;
						cibleTir = true;
						System.out.println("Ennemi Ex repéré !");
					}
				}
			}
		}

		if (!cibleTir) {
			// Bases ennemies visibles
			for (int i = 0; i < prcpt.nbrEnnemiHome; i++)	{
	  		tmpX = prcpt.prcptEnnemiHome[i].getX();
	  		tmpY = prcpt.prcptEnnemiHome[i].getY();
				if (tirPossible(tmpX, tmpY, arcsInterdits(0))) {
	  			tgt.x = tmpX;
	  			tgt.y = tmpY;
					tgt.type = TGT_TYPE_Home;
					cibleTir = true;
					System.out.println("Ennemi Home en vue !");				
				}
			}
	  }

		if (!cibleTir) {
			// RLs repérés par un allié
			for (int i=0; i < bAL.nbrAnnonce; i++) {
				if (bAL.annonce[i].msgId == GGG_Msg.MSG_ENNEMI_HOME) {
					tmpX = bAL.annonce[i].senderX + bAL.annonce[i].arg1;
					tmpY = bAL.annonce[i].senderY + bAL.annonce[i].arg2;
					tmpDist = distanceTo (tmpX, tmpY);
					if ((tmpDist < cibleDist) && (tirPossible(tmpX, tmpY, arcsInterdits(0)))){
						tgt.x = tmpX;
						tgt.y = tmpY;
						tgt.type = TGT_TYPE_Home;
						cibleDist = tmpDist;
						cibleTir = true;
						System.out.println("Ennemi Home repéré !");
					}
				}
			}
		}

		return cibleTir;
	}


	//********************************************************
	// Fonction qui retourne vrai si il existe un angle de 
	// tir vers la cible t
	//********************************************************
	private boolean tirPossible(double x, double y, double[] arcs) {
		boolean possible = true;
		double angle = towards(x,y);
		
		// Si à portée
		if (distanceTo(x, y) > 200) { 
			possible = false;
		}
		else {
			// Vérification qu'aucun obstacle ne gène le tire
			for (int i = 0; i < arcs.length; i += 2) {
				if (arcs[i] < 0 ) {
					if ((angle > arcs[i] && angle < arcs[i+1]) || (angle - 360 > arcs[i] && angle-360 < arcs[i+1])) {
						return false;
					}
				}
				else if ((angle > arcs[i] && angle < arcs[i+1])) {
					return false;
				}
			}
		}

		return possible;
	}
	
	//********************************************************
	// Fonction qui retourne vrai si il existe un angle de 
	// tir dans la direction de angle
	//********************************************************
	private boolean tirPossible(double angle, double dist, double[] arcs, int rien) {
		boolean possible = true;
		
		// Si à portée
		if (dist > 200) { 
			possible = false;
		}
		else {
			// Vérification qu'aucun obstacle ne gène le tire
			for (int i = 0; i < arcs.length; i += 2) {
				if (arcs[i] < 0 ) {
					if ((angle > arcs[i] && angle < arcs[i+1]) || (angle - 360 > arcs[i] && angle-360 < arcs[i+1])) {
						return false;
					}
				}
				else if ((angle > arcs[i] && angle < arcs[i+1])) {
					return false;
				}
			}
		}

		return possible;
	}

	//********************************************************
	// Fonction qui retourne vrai si la cible est morte 
	//********************************************************
	private boolean isDead(GGG_Target t) {
		for (int i = 0; i < prcpt.nbrEnnemiRL; i++) {
			if (t.x == prcpt.prcptEnnemiRL[i].getX() && t.y == prcpt.prcptEnnemiRL[i].getY() && prcpt.prcptEnnemiRL[i].getEnergy() < 30) {
				return true;
			}
		}
			return false;
	}
	//********************************************************
	// Fonction qui retourne vrai si la cible est a portee  
	// de rocket
	//********************************************************
	/*private boolean aPorte(GGG_Target t) {
		if ( distanceTo(t.x, t.y) < 200 ) { 
			return true;
		}
		return false;
	}*/
	

	//******************************************************
	// Tir sur une cible 
	// (modifie l'angle de tir en fonction de la cible)
	//********************************************************	
	private void tirer(GGG_Target t) {
		double varAngle=0.0, dir=0.0;
		
		if ( t.type == TGT_TYPE_RL ) {
			varAngle=ANGLE_TIR_RL;
		}
		if ( t.type == TGT_TYPE_Ex ) {
			/*
			if ( distanceTo(t.x,t.y) != 0 ) {
				varAngle=toDegre(2*Math.atan(4/distanceTo(t.x,t.y)));
			}*/
			varAngle=ANGLE_TIR_Ex;
		}
		if ( t.type == TGT_TYPE_Home ) {
			varAngle=ANGLE_TIR_Home;
		}
		dir=towards(t.x, t.y) + (Math.random()-0.5)*varAngle;
		while ( !tirPossible(dir,distanceTo(t.x,t.y),arcsInterdits(0),1) ) {
			dir=towards(t.x, t.y) + (Math.random()-0.5)*varAngle;
		}
		launchRocket(dir);
	}
	
}