/*
* PetriStructure.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit.Formalisms.Petri;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

import SEdit.*;
import SEdit.Graphics.*;
import SEdit.Scheme.*;


import gnu.kawa.util.*;
import kawa.standard.*;
import kawa.lang.*;
import kawa.*;
import javax.swing.*;

import madkit.kernel.Message;
import madkit.messages.ACLMessage;

/***********************************************************
				CLASSE PetriStructure
************************************************************/

public class PetriStructure extends Structure implements PetriScheduler {
	// le vecteur des activations en cours
	protected Vector activatedList = new Vector();

	public void addActivated(PetriTransition a) {
		activatedList.addElement(a);
	}

	public void removeActivated(PetriTransition a) {
		activatedList.removeElement(a);
	}

	// implemente ici la maniere de recuperer un element.
	// pour l'instant la recherche est aleatoire....
	protected PetriTransition getActivated() {
		PetriTransition a;
		int c = (int) (Math.random()*activatedList.size());
		a = (PetriTransition) activatedList.elementAt(c);
		return(a);
	}

	protected SchemeModule scheme;

	public SchemeModule getSchemeModule()
    {
		return scheme;
    }


	public PetriStructure(){
		scheme = new SchemeModule(this);
	}

	public void initStructure(){
		scheme.init();
	}

 	public void step() {
 		if (!activatedList.isEmpty()){
 			getActivated().validate();
 		}
 	}


	public void handleMessage(Message m){
		SEditMessage fm=null;
		ACLMessage am=null;

		String request = null;
		Object content = null;

		if (m instanceof SEditMessage) {
			fm = (SEditMessage) m;
			request = fm.getRequest();
			content = fm.getParameter();
		} else if (m instanceof ACLMessage) {
			am = (ACLMessage) m;
			request = am.getAct();
			content = am.getContent();
		} else
			System.err.println("ERROR: invalid message type: " + m);

		// System.out.println(":: traitement du message " + request + " : " + content);
		SNode o = getNodeFromLabel(request);
		/* if ((o != null) && (o instanceof SInPort)){
			// System.out.println(":: got one SInPort : " + o);
			LList mess = null;
			String head="message"; // on pourrait en fait mettre la classe du message
								   // et ainsi pouvoir récupérer n'importe quel type de message

			if (content == null)
				mess = new Pair(head,LList.Empty);
			else {
			  try {
				mess = new Pair(head, new Pair(m.getSender(), new Pair(content,LList.Empty)));
				((SInPort)o).addColoredToken(mess);
			  } catch (Exception e) {
				 System.err.println(":: ERROR : invalid token " + content + " in " + this);
			  }
			}
		} */
	}

}
