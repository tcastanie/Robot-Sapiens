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
public abstract class GGG_WB extends Brain {
	// Bot type constants
  static final int BOT_TYPE_Home  = 0;
  static final int BOT_TYPE_Ex    = 1;
  static final int BOT_TYPE_RL    = 2;
  
  public static final String TEAM			  = "GGG";

	// Le groupe partagé par tous les agents
	public static final int ALL_GROUPS			= -1;

	// Le groupe partagé par tous les agents
	public static final int ALL_ROLES 			= -1;
	
	// Les roles possibles des agents
	static final int ROLE_INCONNU			  = 0;
	static final int ROLE_SERGENT			  = 1;
	static final int ROLE_SOLDAT				= 2;
	static final int ROLE_EXPLORATEUR	  = 3;
	static final int ROLE_SENTINELLE		= 4;
	static final int ROLE_BASE					= 5;
	static final int ROLE_BASE_CHEF		  = 6;
	static final int ROLE_ECLAIREUR		  = 7;
	static final int NBR_ROLE					  = 8;
	// Les chaines associées pour les messages
	static String[] ROLE_STR = new String[NBR_ROLE];
	static {
		ROLE_STR[ROLE_INCONNU] =			"R_Inc";
		ROLE_STR[ROLE_SERGENT] =			"R_Ser";
		ROLE_STR[ROLE_SOLDAT] =				"R_Sol";
		ROLE_STR[ROLE_EXPLORATEUR] =	"R_Exp";
		ROLE_STR[ROLE_ECLAIREUR] =		"R_Ecl";
		ROLE_STR[ROLE_SENTINELLE] =		"R_Sen";
		ROLE_STR[ROLE_BASE] =					"R_Bas";
		ROLE_STR[ROLE_BASE_CHEF] =		"R_BaC";
	}


	// Les tactiques possibles des agents
	static final int TACT_INCONNU					  = 0;
	static final int TACT_FUIR							= 1;
	static final int TACT_APPATER					  = 2;
	static final int TACT_PATROUILLER			  = 3;
	static final int TACT_RETRAITE					= 4;
	static final int TACT_OBEIR						  = 5;
	static final int TACT_ATTAQUER_BASE		  = 6;
	static final int TACT_ATTAQUER_MOBILES	= 7;
	static final int TACT_ORGANISER					= 8;
	static final int TACT_SURVEILLER				= 9;
	static final int TACT_REJOINDRE_POINT		= 10;
	static final int TACT_INITIALISER				= 11;
	static final int TACT_ECLAIRER					= 12;
	static final int NBR_TACT							  = 13;
	// Les chaines associées
	static String[] TACT_STR = new String[NBR_TACT];
	static {
		TACT_STR[TACT_INCONNU] =					"T_Inc";
		TACT_STR[TACT_FUIR] =							"T_Fui";
		TACT_STR[TACT_APPATER] =					"T_App";
		TACT_STR[TACT_PATROUILLER] =			"T_Pat";
		TACT_STR[TACT_RETRAITE] =					"T_Ret";
		TACT_STR[TACT_OBEIR] =						"T_Obe";
		TACT_STR[TACT_ATTAQUER_BASE] =		"T_ABa";
		TACT_STR[TACT_ATTAQUER_MOBILES] = "T_AMo";
		TACT_STR[TACT_ORGANISER] =				"T_Org";
		TACT_STR[TACT_REJOINDRE_POINT] =	"T_Rej";
		TACT_STR[TACT_SURVEILLER] =				"T_Sur";
		TACT_STR[TACT_ECLAIRER] =					"T_Ecl";
		TACT_STR[TACT_INITIALISER] =			"T_Ini";
	}

	// Les buts possibles des agents
	static final int BUT_INCONNU					= 0;
	static final int BUT_DEPLACEMENT			= 1;
	static final int BUT_RASSEMBLER				= 2;
	static final int BUT_RASSEMBLEMENT		= 3;
	static final int BUT_SUIVRE_AMI				= 4;
	static final int BUT_ATTAQUER_CIBLE		= 5;
	static final int BUT_OBSERVER					= 6;
	static final int BUT_ORGANISER				= 7;
	static final int BUT_APPELER_ESCOUADE	= 8;
	static final int BUT_COMPTER_ESCOUADE	= 9;
	static final int BUT_TOURNER_AUTOUR		= 10;
	static final int NBR_BUT							= 11;

	// Les chaines associées
	static String[] BUT_STR = new String[NBR_BUT];
	static {
		BUT_STR[BUT_INCONNU] =					"B_Inc";
		BUT_STR[BUT_DEPLACEMENT] =			"B_Dep";
		BUT_STR[BUT_RASSEMBLER] =				"B_RAS";
		BUT_STR[BUT_RASSEMBLEMENT] =		"B_Ras";
		BUT_STR[BUT_SUIVRE_AMI] =				"B_Sui";
		BUT_STR[BUT_ATTAQUER_CIBLE] =		"B_Att";
		BUT_STR[BUT_OBSERVER] =					"B_Obs";
		BUT_STR[BUT_ORGANISER] =				"B_Org";
		BUT_STR[BUT_APPELER_ESCOUADE] =	"B_App";
		BUT_STR[BUT_COMPTER_ESCOUADE] =	"B_Cpt";
		BUT_STR[BUT_TOURNER_AUTOUR] =		"B_TAu";
	}

	// Types de cible
	// Cibles mobiles
	static final int TGT_TYPE_RL =			-2;
	static final int TGT_TYPE_Ex =			-1;
	// Cible par défaut
	static final int TGT_TYPE_Inconnu =	0;
	// Cibles fixes
	static final int TGT_TYPE_Home =		1;
	static final int TGT_TYPE_Point =		2;


	// Constantes de gestion du groupe
	static final String GROUPE_INCONNU	= "Je ne connais pas mon groupe";
	static final int TAILLE_ESCOUADE	= 3;

	// Type d'agent 
	int myType;

	// Etat du moral de l'agent
	int moral = 100;

  // Comportement courant de l'agent
  int role = ROLE_INCONNU;
  int tactique = TACT_INCONNU;
	int but = BUT_INCONNU;
	int cptDureeRole = 0;
	int cptDureeTactique = 0;
	int cptDureeBut = 0;

  // Information sur notre équipe (Max et courante)
	int nbrMaxRL = 0;
	int nbrMaxEx = 0;
	int nbrMaxHome = 0;

	int nbrRL = 0;
	int nbrEx = 0;
	int nbrHome = 0;
	int nbrEscouade = 0;
	int nbrSergent = 0;
	int nbrGroupe = 0; // taille actuelle du groupe

  // Information sur l'équipe adverse
	int nbrEnnemyRL = 0;
	int nbrEnnemyEx = 0;
	int nbrEnnemyHome = 0;

	// Représentation interne des perceptions de l'agent
	GGG_Percept prcpt = new GGG_Percept();

	// BAL triée de l'agent
	GGG_BAL bAL = new GGG_BAL();
	
	// ----------------------------------------------
  //  Constructeur
  // ----------------------------------------------
	public GGG_WB() {}
	
	// ----------------------------------------------
  //  Initialisationde l'agent
  // ----------------------------------------------
	public void activate() {
		createGroup(true, TEAM, null, null);

		showUserMessage (true);
	}
	
  // ----------------------------------------------
  //  Boucle principale d'acion de l'agent
  // ----------------------------------------------
	public void doIt() {
		//setUserMessage("R:" + ROLE_STR[role] + " T:" + TACT_STR[tactique] + " B:" + BUT_STR[but]);

		bAL.recevoirMessage(this);

		prcpt.voir(this);

	  introspecter();

		actualiserRole();
		effectuerRole();

		actualiserTactique();
		effectuerTactique();

		actualiserBut();
		effectuerBut();
	}
	
  // ----------------------------------------------
	// Gestion générique des états internes
	// ----------------------------------------------  
	protected void introspecter () {

    // Comptage de l'équipe GGG
		for ( int i=0; i< bAL.nbrAnnonce; i++ ) {
			if ( bAL.annonce[i].msgId == GGG_Msg.MSG_PRES_EX ) {
        nbrEx++;
				nbrMaxEx++;
			}
			if ( bAL.annonce[i].msgId == GGG_Msg.MSG_PRES_RL ) {
        nbrRL++;
				nbrMaxRL++;
			}
			if ( bAL.annonce[i].msgId == GGG_Msg.MSG_PRES_HOME ) {
        nbrHome++;
				nbrMaxHome++;
			}
		}

		// On décrémente les compteurs de durée
		cptDureeRole--;
		cptDureeTactique--;
		cptDureeBut--;
  }

	// ----------------------------------------------
	//	Recalcul du rôle de l'agent
  //		Gestion des changement de rôle
  //		et des actions à faire en cas de changement de rôle
	// ----------------------------------------------
	protected void actualiserRole () {
	}

  // ----------------------------------------------
  //  Réalisation du rôle
  //		Traitement propre au rôle
  // ----------------------------------------------
	protected void effectuerRole () {
	// On annonce à tout le monde la présence d'ennemi RL
		for (int i = 0; i < prcpt.nbrEnnemiRL; i++) {
			bAL.annoncer(ALL_ROLES, ALL_GROUPS, GGG_Msg.MSG_ENNEMI_RL,
				prcpt.prcptEnnemiRL[i].getX(), prcpt.prcptEnnemiRL[i].getY());
		}

		for (int i = 0; i < prcpt.nbrEnnemiEx; i++) {
			bAL.annoncer(ALL_ROLES, ALL_GROUPS, GGG_Msg.MSG_ENNEMI_EX,
				prcpt.prcptEnnemiEx[i].getX(), prcpt.prcptEnnemiEx[i].getY());
		}

		for (int i = 0; i < prcpt.nbrEnnemiHome; i++) {
			bAL.annoncer(ALL_ROLES, ALL_GROUPS, GGG_Msg.MSG_ENNEMI_HOME,
				prcpt.prcptEnnemiHome[i].getX(), prcpt.prcptEnnemiHome[i].getY());
		}
	}

	// ----------------------------------------------
	// Recalcul de la tactique de l'agent
  //		Gestion des changement de tactique
  //		et des actions à faire en cas de changement de tactique
	// ----------------------------------------------
	protected void actualiserTactique () {
	}

  // ----------------------------------------------
  //  Réalisation de la tactique
  //		Traitement propre à la tactique
	// ----------------------------------------------
	protected void effectuerTactique () {
	}

	// ----------------------------------------------
	// Recalcul du but de l'agent
  //		Gestion des changement de but
  //		et des actions à faire en cas de changement de but
	// ----------------------------------------------
	protected void actualiserBut () {
	}

  // ----------------------------------------------
  //  Réalisation du but
  //		Traitement propre au but
  // ----------------------------------------------
	protected void effectuerBut () {
	}


	// ----------------------------------------------
	// ----------------------------------------------
	//
	//  Fonctions utilitaires communes à tous les bots
	//
	// ----------------------------------------------
	// ----------------------------------------------

	// ----------------------------------------------
  //  Calcul de distance
  // ----------------------------------------------
	public double distanceTo(double X, double Y) {
		return ( Math.sqrt(X*X+Y*Y) );
	}
	
	//********************************************************
	// Fonction qui prend un angle en degre et retourne son équivalent en Radians
	//********************************************************
	protected double toRadian(double angle)	{
		return ((1.0 * Math.PI * angle)/180.0);
	}	
	
	//********************************************************
	// Fonction qui prend un angle en Radians et retourne son équivalent en degre
	//********************************************************
	protected double toDegre(double angle) {
		return ((180.0 * angle)/Math.PI);
	}
	
	//********************************************************
	// Fonction qui prend un double et un int et qui retourne 
	// le reste de la division du double par l'int
	//********************************************************
	protected double modulo(double valeur, int diviseur) {
		while((valeur - diviseur) >= 0)	{
			valeur = valeur - diviseur;
		}
		return valeur;
	}

	//********************************************************
	// Fonction qui retourne vrai si la cible est perdue de vue
	//********************************************************
	protected boolean localiserAmi(GGG_Target t){
		Percept e;

		for( int i=0; i < prcpt.nbrAmiRL; i++ ) {
			e = prcpt.prcptAmiRL[i];
			if( e.getAgent()==t.id ) {
				t.x = e.getX();
				t.y = e.getY();
				return true;
			}
		}

		for( int i=0; i < prcpt.nbrAmiEx; i++ ) {
			e = prcpt.prcptAmiEx[i];
			if( e.getAgent()==t.id ) {
				t.x = e.getX();
				t.y = e.getY();
				return true;
			}
		}

		for( int i=0; i < prcpt.nbrAmiHome; i++ ) {
			e = prcpt.prcptAmiHome[i];
			if( e.getAgent()==t.id ) {
				t.x = e.getX();
				t.y = e.getY();
				return true;
			}
		}
		return false;
	}
}
