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

// Boîte à lettre de l'agent
public class GGG_BAL {

	static final int MAX_ANNONCE =	200;
	static final int MAX_REQUETE =	20;
	static final int MAX_ORDRE =		40;
	static final int MAX_ENVOI = 		100;
	
	// Boite des messages reçu
	int nbrMessageRecu = 0;
	int nbrAnnonce = 0;
	GGG_Msg[] annonce = new GGG_Msg[MAX_ANNONCE];
	int nbrRequete = 0;
	GGG_Msg[] requete = new GGG_Msg[MAX_REQUETE];
	int nbrOrdre = 0;
	GGG_Msg[] ordre = new GGG_Msg[MAX_ORDRE];

	// Boite des message à envoyer
	int nbrEnvoi = 0;
	GGG_Msg[] envoi = new GGG_Msg[MAX_ENVOI];
	
	// Constructeur
	public GGG_BAL() {
	}

	// ----------------------------------------------
	//  Remise à zéro des compteurs de la boite à lettre
	// ----------------------------------------------
	private void reset() {
		nbrMessageRecu = 0;
		nbrAnnonce = 0;
		nbrRequete = 0;
		nbrOrdre = 0;
		nbrEnvoi = 0;
	}

	// ----------------------------------------------
	// Ajout des messages reçus dans la boîte à lettre
	// ----------------------------------------------
	public void recevoirMessage(Brain wb) {
		GGG_Msg tmpMsg;
		WarbotMessage wm;
		reset();

		while ( !wb.isMessageBoxEmpty() ) {
			wm = wb.readMessage();
			tmpMsg = new GGG_Msg(wm);

			nbrMessageRecu++;

			// Enregistrement des annonces
			if ((tmpMsg.type == GGG_Msg.TYPE_MSG_ANNONCE) && (nbrAnnonce < MAX_ANNONCE)) {
			  annonce[nbrAnnonce] = tmpMsg;
				nbrAnnonce++;
			}
			// Enregistrement des requetes
			else if ((tmpMsg.type == GGG_Msg.TYPE_MSG_REQUETE) && (nbrRequete < MAX_REQUETE)) {
				requete[nbrRequete] = tmpMsg;
				nbrRequete++;
			}
			// Enregistrement des ordres
			else if ((tmpMsg.type == GGG_Msg.TYPE_MSG_ORDRE) && (nbrOrdre < MAX_ORDRE)) {
				System.out.println(this + " : Ordre reçu : " + GGG_Msg.MSG_STR[tmpMsg.msgId] + " de " + tmpMsg.sender);
			  ordre[nbrOrdre] = tmpMsg;
				nbrOrdre++;
			}
			else {
				System.out.println("Message de type inconnu !!!");
				nbrMessageRecu--;
			}
		}
	}


	// ----------------------------------------------
  //  Envoi d'une annonce à un agent
  // ----------------------------------------------
	public void annoncer (madkit.kernel.AgentAddress destinataire, int msgId, double arg1, double arg2) {
		int groupe = GGG_WB.ALL_GROUPS;
		int role = GGG_WB.ALL_ROLES;

		if (nbrEnvoi < MAX_ENVOI)	{
			envoi[nbrEnvoi] = new GGG_Msg(destinataire, role, groupe, msgId, arg1, arg2, GGG_Msg.TYPE_MSG_ANNONCE);

			nbrEnvoi++;
		}
	}

	// ----------------------------------------------
  //  Envoi d'une annonce à des destinataire multiples
  // ----------------------------------------------
	public void annoncer (int role, int groupe, int msgId, double arg1, double arg2) {
		
		if (nbrEnvoi < MAX_ENVOI)	{
			envoi[nbrEnvoi] = new GGG_Msg(null, role, groupe, msgId, arg1, arg2, GGG_Msg.TYPE_MSG_ANNONCE);

			nbrEnvoi++;
		}
	}

	// ----------------------------------------------
  //  Envoi d'une requete à un agent
  // ----------------------------------------------
	public void requerir (madkit.kernel.AgentAddress destinataire, int msgId, double arg1, double arg2) {
		int groupe = GGG_WB.ALL_GROUPS;
		int role = GGG_WB.ALL_ROLES;

		if (nbrEnvoi < MAX_ENVOI)	{
			envoi[nbrEnvoi] = new GGG_Msg(destinataire, role, groupe, msgId, arg1, arg2, GGG_Msg.TYPE_MSG_REQUETE);

			nbrEnvoi++;
		}
	}

	// ----------------------------------------------
  //  Envoi d'une requete à des destinataire multiples
  // ----------------------------------------------
	public void requerir (int role, int groupe, int msgId, double arg1, double arg2) {
		madkit.kernel.AgentAddress destinataire = null;

		if (nbrEnvoi < MAX_ENVOI)	{
			envoi[nbrEnvoi] = new GGG_Msg(destinataire, role, groupe, msgId, arg1, arg2, GGG_Msg.TYPE_MSG_REQUETE);

			nbrEnvoi++;
		}
	}

	// ----------------------------------------------
  //  Envoi d'un ordre à un agent
  // ----------------------------------------------
	public void ordonner (madkit.kernel.AgentAddress destinataire, int msgId, double arg1, double arg2) {
		int groupe = GGG_WB.ALL_GROUPS;
		int role = GGG_WB.ALL_ROLES;

		if (nbrEnvoi < MAX_ENVOI)	{
			envoi[nbrEnvoi] = new GGG_Msg(destinataire, role, groupe, msgId, arg1, arg2, GGG_Msg.TYPE_MSG_ORDRE);

			nbrEnvoi++;
		}
	}

	// ----------------------------------------------
  //  Envoi d'un ordre à des destinataire multiples
  // ----------------------------------------------
	public void ordonner (int role, int groupe, int msgId, double arg1, double arg2) {
		madkit.kernel.AgentAddress destinataire = null;

		if (nbrEnvoi < MAX_ENVOI)	{
			envoi[nbrEnvoi] = new GGG_Msg(destinataire, role, groupe, msgId, arg1, arg2, GGG_Msg.TYPE_MSG_ORDRE);

			nbrEnvoi++;
		}
	}

	// ----------------------------------------------
  //  Localisation d'un sender, s'il a envoyer un
  // message ce tour
  // ----------------------------------------------
	public boolean localiserSender (GGG_Target t) {
		boolean localise = false;

		for (int i = 0; i < nbrAnnonce; i++) {
			if (annonce[i].sender == t.id) {
				t.x = annonce[i].senderX;
				t.y = annonce[i].senderY;
				localise = true;
			}
		}
		
		if (!localise) {
			for (int i = 0; i < nbrRequete; i++) {
				if (requete[i].sender == t.id) {
					t.x = requete[i].senderX;
					t.y = requete[i].senderY;
					localise = true;
				}
			}
		}

		if (!localise) {
			for (int i = 0; i < nbrOrdre; i++) {
				if (ordre[i].sender == t.id) {
					t.x = ordre[i].senderX;
					t.y = ordre[i].senderY;
					localise = true;
				}
			}
		}

		return localise;
	}

	// ----------------------------------------------
	// Envoi effectif des messages stocker
  // ----------------------------------------------
	public void sendAll(Brain wb) {

	for (int i = 0; i < nbrEnvoi; i++) {
			envoi[i].send(wb);
		}
	}

}
