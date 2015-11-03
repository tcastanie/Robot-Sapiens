/*
* PetriPlace.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.util.*;
import java.io.*;

import SEdit.*;
import SEdit.Graphics.*;

// pour avoir des jetons colorés...
import gnu.lists.*;
import kawa.standard.*;
import kawa.lang.*;
import kawa.*;
import SEdit.Scheme.*;
import javax.swing.*;



public class PetriPlace extends SimpleNode implements PetriPlaceBehavior {
	static  String askForString(String header) {
		return askForString(header, "");
	}

	static  String askForString(String header, String message) {
		String s;
        s = JOptionPane.showInputDialog(null, header, message , JOptionPane.PLAIN_MESSAGE);
       if ((s == null) || (s.equals("")))
       	return(null);
       else
       	return(s);
	}

	int jetons=0;	// nbre de jetons
	LList coloredTokens; // tokens colorés... (s'il y en a..)

	public PetriPlace() { }


	public int getNumberTokens() {
		return(jetons);
	}

	public LList getColoredTokens() { return(coloredTokens); }

  	public void addColoredToken() {
   		String s = askForString("Entrez un nouveau token:");
   		try {
       		if (s != null)
           		addColoredToken(STools.readFromString(s));
        } catch (Exception e) {
   			System.out.println(":: Erreur de saisie de token");
		}
    }

    public void addToken(){produce(1);}

  	void addColoredToken(Object token) {
  	   	if (token == null) return;
  		if (coloredTokens == null)
  			coloredTokens = new Pair(token,LList.Empty);
  		else
  			coloredTokens = new Pair(token,coloredTokens);
		//update();
       	if (outArrows != null) {
       	// System.out.println(toString()+" : On active les liens de sortie");
  			for(int i = 0; i < outArrows.size(); i++)
  		 		((PetriInLink)outArrows.elementAt(i)).doActivate();
       }
  	}

  	public void produce(int j) {
   	//	display();
		jetons += j;
       	if (outArrows != null) {
       	// System.out.println(toString()+" : On active les liens de sortie");
  			for(int i = 0; i < outArrows.size(); i++)
  		 		((PetriInLink)outArrows.elementAt(i)).doActivate();
       }
   	//	display();
  	}

  	public void produce(Object e) {
  		addColoredToken(e);
  	}

  	void removeAllTokens() {
  		coloredTokens = null;
  		jetons = 0;
  		if (outArrows != null) {
   			for(int i = 0; i < outArrows.size(); i++)
  					((PetriInLink)outArrows.elementAt(i)).inhibit();
  		}
  	}

  	public void consume(int j) {
		if (jetons >= j) {
   	//		display();
   			jetons -= j;
  		if (outArrows != null) {
   			for(int i = 0; i < outArrows.size(); i++)
  					((PetriInLink)outArrows.elementAt(i)).inhibit();
        }
    //   	display();
		}
  	}

  	public void consume(Object expr) {
  		if (coloredTokens != null) {
  			coloredTokens = (LList) STools.delete(expr,coloredTokens);
  			if (coloredTokens.size() == 0) coloredTokens = null;
  			if (outArrows != null) {
   				for(int i = 0; i < outArrows.size(); i++)
  					((PetriInLink)outArrows.elementAt(i)).inhibit();
        	}
  	//		update();
  		}
  	}

  	public boolean isEmpty() {
  		return(jetons==0);
	}

	public void modifyColoredTokens(){
      	String tokens=null;

      	if ((coloredTokens instanceof LList) && (coloredTokens != LList.Empty)){
      		tokens="(";
      		LList ltoks=coloredTokens;
      		int n = coloredTokens.size();
      		for (int i = 0; i < n; i++, ltoks = (LList)((Pair)ltoks).cdr) {
  				tokens = tokens + STools.writeToString(((Pair)ltoks).car)+"\n";
  			}
  			tokens=tokens+")";

      	} else tokens = "()";

   	//	String s = SEditTools.editText(getAgent().gui, "Editing tokens of " + getLabel(), tokens);

		String s = PetriLink.askForNewString(tokens);

  		try {
       		if (s != null) {
       			Object o = STools.readFromString(s);
           		if (o instanceof LList)
           			coloredTokens = (LList) o;
         //  		update();
           	}
		}
		catch (Exception e) {

		    System.out.println(":: Erreur de saisie d'action");
		}
	}

/*	public void doAction(String command) {
   		if (command.equals("addToken"))    {
       	//jetons++;
           produce(1);
       } else
       if (command.equals("addColoredToken"))
       		addColoredToken();
       else if (command.equals("removeToken")) {
       	consume(1);}
       else if (command.equals("removeAllTokens")) {
       	removeAllTokens();
       }
       else if (command.equals("inspect")){
       	modifyColoredTokens();
       }
       else super.doAction(command);
	} */

/*   protected void inspect()  {
   	super.inspect();

   	System.out.println("     [jetons] " + jetons);
   } */
}

