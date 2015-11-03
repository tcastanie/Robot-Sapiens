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
//import madkit.lib.simulation.*;

// Warbot internal percept structure
public class GGG_Msg {

	//	Constantes pour broadcast message
	public static final madkit.kernel.AgentAddress	MULTIPLE =					null;
	public static final String											STR_ALL_GROUPS =		GGG_WB.TEAM;	
	public static final String											STR_ALL_ROLES	=			"member";
	public static final String											GROUPE_ESCOUADE_x	= "Escouade_";

	// Les 3 types de messages //
	public static final int TYPE_MSG_ANNONCE			= 0;
	public static final int TYPE_MSG_REQUETE			= 1;
	public static final int TYPE_MSG_ORDRE				= 2;
	public static final int NBR_TYPE_MSG          = 3;
	// Les chaines associées
	public static String[] TYPE_MSG_STR = new String[NBR_TYPE_MSG];
	static {
		TYPE_MSG_STR[TYPE_MSG_ANNONCE] =			"Annonce : ";
		TYPE_MSG_STR[TYPE_MSG_REQUETE] =			"Requete : ";
		TYPE_MSG_STR[TYPE_MSG_ORDRE] =				"Ordre : ";
	}

	// les messages type
	public static final int MSG_PRES_HOME					= 0;
	public static final int MSG_PRES_RL						= 1;
	public static final int MSG_PRES_EX						= 2;
	public static final int REP_PRES_HOME					= 3;
	public static final int REP_PRES_RL						= 4;
	public static final int REP_PRES_EX						= 5;
	public static final int MSG_ENNEMI_RL					= 6;
	public static final int MSG_AFFECTATION				= 7;
	public static final int MSG_RASSEMBLEMENT			= 8;
	public static final int MSG_RASSEMBLE					= 9;
	public static final int MSG_PERDU							= 10;
	public static final int MSG_AU_RAPPORT				= 11;
	public static final int MSG_PRES_SERGENT			= 12;
	public static final int MSG_MORT							= 13;
	public static final int MSG_DETRUIT						= 14;
	public static final int MSG_POSITION					= 15;
	public static final int MSG_ATTAQUER_MOBILE		= 16;
	public static final int MSG_SUIVRE						= 17;
	public static final int MSG_PRES_SOLDAT				= 18;
	public static final int MSG_REJOINDRE_POINT		= 19;
	public static final int MSG_ENNEMI_HOME				= 20;
	public static final int MSG_ENNEMI_EX					= 21;
	public static final int MSG_AFFECTE						= 22;
	public static final int NBR_MSG								= 23;
	// Les chaines associées
	public static String[] MSG_STR = new String[NBR_MSG];
	static {
		// Présentation
		MSG_STR[MSG_PRES_HOME] =				"Salut, je suis une base";
		MSG_STR[MSG_PRES_RL] =					"Salut, je suis un rocket launcher";
		MSG_STR[MSG_PRES_EX] =					"Salut, je suis un explorateur";
		MSG_STR[MSG_PRES_SERGENT] =			"Salut, je suis votre nouveau sergent";
		MSG_STR[MSG_PRES_SOLDAT] =			"Salut, je suis dans ton groupe";

		// Réponse présentation
		MSG_STR[REP_PRES_HOME] =				"Salut à toi, je suis une base ";
		MSG_STR[REP_PRES_RL] =					"Salut à toi, je suis un rocket launcher";
		MSG_STR[REP_PRES_EX] =					"Salut à toi, je suis un explorateur";

		// Annonces diverses
		MSG_STR[MSG_RASSEMBLE] =				"Je suis Rassemble";
		MSG_STR[MSG_PERDU] =						"Je suis perdu";
		MSG_STR[MSG_AU_RAPPORT] =				"Soldat, au rapport";
		MSG_STR[MSG_POSITION] =					"Je suis là";
		MSG_STR[MSG_MORT] =							"Arrrgggghhhh....";
		MSG_STR[MSG_DETRUIT] =					"Je l'ai eu";
		MSG_STR[MSG_AFFECTE] =					"Je suis affecté à ce groupe";

		// Alerte
		MSG_STR[MSG_ENNEMI_RL] =				"Attention ! RocketLauncher ennemi détecté en :";
		MSG_STR[MSG_ENNEMI_EX] =				"Attention ! Explrateur ennemie détectée en :";
		MSG_STR[MSG_ENNEMI_HOME] =			"Attention ! Base ennemie détectée en :";

		// Ordre de la base
		MSG_STR[MSG_AFFECTATION] =			"Tu appartiens maintenant à ce groupe !";
		MSG_STR[MSG_REJOINDRE_POINT] =	"Vas là-bas !";
		
		// Ordre du sergent
		MSG_STR[MSG_RASSEMBLEMENT] =		"Rassemblement !";
		MSG_STR[MSG_ATTAQUER_MOBILE] =	"Attaque ce robot !";
		MSG_STR[MSG_SUIVRE] =						"Suivez-moi les gars !";
	}

	// Structure du message
	public madkit.kernel.AgentAddress sender;
	public double senderX;
	public double senderY;
	public int type = -1;
	public int msgId;
	public double arg1;
	public double arg2;

	// Attribut privées, ils sont utilisés pour l'envoi seulement
	private madkit.kernel.AgentAddress destinataire;
	private int role;
	private int groupe;


  // ----------------------------------------------
	// Constructeur vide
  // ----------------------------------------------
	public GGG_Msg() {
	}

  // ----------------------------------------------
	// Constructeur à partir d'un message warbot
  // ----------------------------------------------
	public GGG_Msg(WarbotMessage wm) {
		int i = 0;
		Double tmp = new Double(0.0);

		sender = wm.getSender();
		senderX = wm.getFromX();
		senderY = wm.getFromY();

		while (i < NBR_TYPE_MSG) {
			if (wm.getAct().equals(TYPE_MSG_STR[i])) {
				type = i;
			}
			i++;
		}

		if (type == -1)	{
			// Juste pour vérifier
			System.out.println("Message de type inconnu !!!");
		}
		
		msgId = (int)tmp.valueOf(wm.getArgN(1)).doubleValue();
		arg1 = tmp.valueOf(wm.getArgN(2)).doubleValue();
		arg2 = tmp.valueOf(wm.getArgN(3)).doubleValue();
	}

  // ----------------------------------------------
	// Constructeur d'un message à envoyer
  // ----------------------------------------------
	public GGG_Msg (madkit.kernel.AgentAddress dest, int msgRole, int msgGroupe, int id, double msgArg1, double msgArg2, int msgType) {
		destinataire = dest;
		role = msgRole;
		groupe = msgGroupe;
		msgId = id;
		arg1 = msgArg1;
		arg2 = msgArg2;
		type = msgType;
	}
	
	// ----------------------------------------------
	// Envoi d'un message
  // ----------------------------------------------
	public void send(Brain wb) {
		String cont[] = new String[3];

		// Conversion des propriétés en chaine
		cont[0] = "" + msgId;
		cont[1] = "" + arg1;
		cont[2] = "" + arg2;
		String strType = TYPE_MSG_STR[type];
		String strGroupe;
		String strRole;

		if (destinataire == MULTIPLE) {

			if (groupe == GGG_WB.ALL_GROUPS)	{
				strGroupe = STR_ALL_GROUPS;
			}
			else {
				strGroupe = GROUPE_ESCOUADE_x + groupe;
			}

			if (role == GGG_WB.ALL_ROLES) {
				strRole = STR_ALL_ROLES;
			}
			else {
				strRole = GGG_WB.ROLE_STR[role];
			}

			wb.broadcast(strGroupe, strRole, strType, cont);
		}
		else {
			wb.send (destinataire, strType, cont);
		}
		System.out.println(strType + MSG_STR[msgId] + cont[1] +cont[2]);
	}
}
