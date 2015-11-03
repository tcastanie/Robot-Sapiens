/*
* ActivePetriStructure.java - SEdit, a tool to design and animate graphs in MadKit
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


//import gnu.kawa.util.*;
import gnu.lists.*;
import kawa.standard.*;
import kawa.lang.*;
import kawa.*;
import javax.swing.*;

import madkit.kernel.*;
import madkit.messages.*;

/***********************************************************
				CLASSE ActivePetriStructure
************************************************************/

public class ActivePetriStructure extends PetriStructure implements ActiveStructure {

	public Vector getInPorts(){
		Vector v = new Vector();
		Vector nodes = getNodes();
		Object o;
		for(int i=0;i<nodes.size();i++){
			if ((o = nodes.elementAt(i)) instanceof PetriInPort)
				v.addElement(o);
		}
		return(v);
	}

	public PetriInPort getInPort(String s){
		Vector nodes = getInPorts();
		for(int i=0;i<nodes.size();i++){
			Object o = nodes.elementAt(i);
			if (s.equals(((SNode) o).getLabel()))
				return((PetriInPort) o);
		}
		return(null);
	}

	public void handleMessage(Message m){
		SEditMessage fm=null;
		ActMessage am=null;
		PetriInPort o =null;

		String request = null;
		Object content = null;

		LList mess = null;

		if (m instanceof StringMessage){
			o = getInPort("StringMessage");
			if (o != null){
				System.out.println(":: got a StringMessage : " + m);
				mess = new Pair("StringMessage", new Pair(m.getSender(),
								new Pair(((StringMessage) m).getString(),LList.Empty)));
			}
		} else if (m instanceof ActMessage) {
			am = (ActMessage) m;
			request = am.getAction();
			content = am.getContent();

			System.out.println(":: traitement du message " + am.getClass().getName()+ ":"
									+ request + " : " + content);
			o = getInPort(request);
			if (o != null) {
				// System.out.println(":: got one SInPort : " + o);
				mess = null;
				String head=am.getClass().getName(); // on met en fait la classe du message
									   // et on peut ainsi récupérer n'importe quel type de message
				if (content == null)
					mess = new Pair(head,LList.Empty);
				else {
				  try {
					mess = new Pair(head, new Pair(m.getSender(),
											new Pair(request,
												new Pair(content,LList.Empty))));
					o.addColoredToken(mess);
				  } catch (Exception e) {
					 System.err.println(":: ERROR : invalid token " + content + " in " + this);
				  }
				}
			}
		} else {
			System.err.println("Warning: received an unknown message : " + m);
			mess = new Pair(m, LList.Empty);
		}
		if (mess != null)
			o.addColoredToken(mess);
	}
}
