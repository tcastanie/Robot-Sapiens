/*
* PetriTransition.java - SEdit, a tool to design and animate graphs in MadKit
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
import java.awt.*;

import SEdit.*;
import SEdit.Scheme.*;

import gnu.lists.*;

public class PetriTransition extends SimpleNode implements PetriTransitionBehavior{
		boolean activated=false;
		Hashtable currentEnv; // l'environnement courant lors d'un filtrage

	/*	public PetriTransition() {
      		super();
    		outArrows=new Vector();
    		inArrows=new Vector();
		} */

	// le prédicat a evaluer eventuellement
	Object predicate;
	public Object getPredicate(){return(predicate);}
	void setPredicate(Object o){predicate = o;}

	// l'action a déclencher lorsque la transition est activée
	Object action;
	public Object getAction(){return(action);}
	void setAction(Object o){action = o;}

	String actionString=null;
	public void setActionString(String s){
		try {
       		if (s != null) {
           		action = STools.readFromString(s);
	        	actionString = s;
           	}
		}
		catch (Exception e) {
		    System.out.println(":: Erreur de saisie d'action");
		}
	}
	public String getActionString(){ return actionString;}

	String predicateString=null;
	public void setPredicateString(String s){
	  	try {
       		if (s != null) {
           		predicate = STools.readFromString(s);
           		predicateString = s;
           	}
		}
		catch (Exception e) {
		    System.out.println(":: Erreur de saisie de prédicat");
		}
	}

	public String getPredicateString(){ return predicateString;}


  	public void wakeUp() {
			if (!activated) {
				currentEnv = null;
				if (inArrows.size() > 0) {
					currentEnv = saturer(0,new Hashtable());
  					if (currentEnv != null){
  		/*				if (predicate != null) {
  							Object r = STools.evalit(predicate,currentEnv);
  							if (r == Interpreter.trueObject)
  								activated = true;
  						}
  						else */
  							activated = true;
  					}
  				}
  			}

  			// si véritablement actif, alors on met en évidence
    		if (activated) {
  				// si on devient actif on dit a tous les arcs entrants et
  				// sortants qu'ils doivent devenir actifs.
  				// DEBUG System.out.println(">> Env: " + currentEnv);

  				for(int i = 0; i < inArrows.size(); i++)
  					((PetriInLink)inArrows.elementAt(i)).active(true);
  				for(int i = 0; i < outArrows.size(); i++)
  					((PetriOutLink)outArrows.elementAt(i)).active(true);
  				// et on s'ajoute dans le scheduler
  				((PetriScheduler)structure).addActivated(this);
  				if(activated)
            		getGObject().setForeground(Color.magenta);
				else
            		getGObject().setForeground(Color.black);

				getGObject().getEditor().repaint();
  			}

  	}

  	Hashtable saturer(int i, Hashtable env) {
  		Hashtable cloneEnv;
  		Hashtable nenv;
  		Object f;
  		Object pat;
  		LList facts;

  		// DEBUG System.out.println(">> filtrer :" + i + ", env: " + env);
  		if (i >= inArrows.size()) {
			if (predicate != null) {

				// $$$ Brrr.... ATTENTION: selectionne la structure comme environnement courant!!
    			((PetriStructure)structure).getSchemeModule().setAsCurrentEnv();
				Object r = STools.evalit(predicate,env);
				if (r == Boolean.TRUE)
					return(env); // solution trouvée (on s'arrete!!)
				else
					return(null);
			} else
  				return(env);
  		}
  		f = ((PetriInLink)inArrows.elementAt(i)).getFilter();
  		if (f == null) {
  			// si il n'y a pas de filtre, on regarde si la liaison est vide...
  			if (((PetriInLink)inArrows.elementAt(i)).isEmpty())
  				return(null); // echec si elle est vide
  			else
  				return(saturer(i+1,env)); // succes, il y a quelque chose
  		} else {
  			// si c'est une liaison à filtre...
  			/* if (!(f instanceof LList)) {
  				System.out.println(":: Erreur: les filtres doivent etre des listes: " + f);
  				return(null);
  			} */

  			pat = f;  // inutilie reminiscence des anciens temps
  			facts = ((PetriInLink)inArrows.elementAt(i)).getTokens(); // on recupere les "faits"

  			if ((facts == null) || (facts.size() == 0))
  				return(null); // il n'y a aucun "fait" dans la place d'entrée..

  			// on parcours la liste des "faits"
  			for (int j = facts.size(); j > 0; j--, facts = (LList)((Pair)facts).cdr) {
  				// DEBUG : System.out.println("boucle : "+j);
  				cloneEnv = (Hashtable) env.clone();
  				nenv = STools.match(pat, ((Pair)facts).car, cloneEnv);

  				if (nenv != null) {
  					if (i > inArrows.size()) {
  						if (predicate != null) {
							// $$$ Brrr.... ATTENTION: selectionne la structure comme environnement courant!!
    						((PetriStructure)structure).getSchemeModule().setAsCurrentEnv();
  							Object r = STools.evalit(predicate,nenv);
  							if (r == Boolean.TRUE)
  								return(nenv); // solution trouvée (on s'arrete!!)
  						}
  					} else {
  						nenv = saturer(i+1,nenv);
  						if (nenv != null) return(nenv);
  					}
  				}
  				// DEBUG System.out.println(".. rien trouve, on continue");
  			}
  			return(null);
  		}
  	}


  	public void goToSleep()
  	{
  			// DEBUG System.out.println(">> " + getName() + " goToSleep:" + ", oldenv: " + currentEnv);
  			if (activated) {
  				Hashtable nenv;
  				activated = false;
				if (inArrows.size() > 0) {
					nenv = saturer(0,new Hashtable());
  					currentEnv = nenv;
  					if (nenv != null) {
  						activated = true;
  					}
  				// DEBUG System.out.println("<< " + getName() + " goToSleep, nenv: " + nenv);
  			}

  			if (!activated) {
  			// si on devient inactif on dit a tous les arcs entrants et
  			// sortants qu'ils doivent devenir non actifs.
  				for(int i = 0; i < inArrows.size(); i++)
  					((PetriInLink)inArrows.elementAt(i)).active(false);
  				for(int i = 0; i < outArrows.size(); i++)
  					((PetriOutLink)outArrows.elementAt(i)).active(false);
  			// et on se supprime du scheduler
  					((PetriScheduler)structure).removeActivated(this);
  			}
  			if(activated)
            		getGObject().setForeground(Color.magenta);
			else
            		getGObject().setForeground(Color.black);

			getGObject().getEditor().repaint();
  		}
  	}

  	public void validate()
  	{
  		if (activated){
  			Hashtable e = currentEnv;

  			for(int i = 0; i < inArrows.size(); i++)
  				((PetriInLink)inArrows.elementAt(i)).consume(e);

  		 	if (action != null) {
  				String s=STools.writeToString(action);
  				System.out.println(":: on evalue: "+ s + " dans " + e);
				// $$$ Brrr.... ATTENTION: selectionne la structure comme environnement courant!!
    			((PetriStructure)structure).getSchemeModule().setAsCurrentEnv();
  				Object r = STools.evalit(action,e);
	  			System.out.println(">> " + r + " : " + STools.prinToString(r));
  			}

  			for(int i = 0; i < outArrows.size(); i++)
  				((PetriOutLink)outArrows.elementAt(i)).produce(e);

  	  } else
  	    	System.out.println("erreur, transition validee non active!!");

  	}

  	public void modifyPredicate() {
      	String pred;
      	if (predicate != null) {
	    	pred = STools.writeToString(predicate);
      	} else pred = "";
   		String s = SEditTools.editText(getStructure().getAgent().gui, "Editing predicate of " + getLabel(), pred);
		// String s = PetriLink.askForNewString(pred);
   		setPredicateString(s);
	}

  	public void modifyAction() {
      	String pred;
      	if (action != null) {
	    	pred = STools.writeToString(action);
      	} else pred = "";
   		String s = SEditTools.editText(getStructure().getAgent().gui, "Editing action of " + getLabel(), pred);
		// String s = PetriLink.askForNewString(pred);
  		setActionString(s);

	}

/*
 	public String bodyString()
    {   String strPred=null;
    	String strAct=null;

    	if (action != null){
			if (predicate != null) {
				strPred = "'" + STools.writeToString(getPredicate());
			}
			if (strPred == null)
				strPred = "()";
			strAct = "'" + STools.writeToString(getAction());
			return(super.bodyString()+" "+strPred+" "+strAct);
    	} else if (predicate != null) {
    		strPred = "'" + STools.writeToString(getPredicate());
    		return(super.bodyString()+" "+strPred);
    	} else return(super.bodyString());
    }

   public void setOptions(Vector v) {
  		if ((v != null) && (v.size() >0)) {
  			Object val = v.firstElement();
  			if (val != LList.Empty)
   				setPredicate(val);
   			if (v.size() > 1){
   				setAction(v.elementAt(1));
   			}
   		}
   } */

/*   public void doAction(String command)
   {
   		if (command.equals("validate"))    {
       		validate();
       	}
    	else if (command.equals("modifyPredicate"))    {
       		modifyPredicate();
       	}
    	else if (command.equals("modifyAction"))    {
       		modifyAction();
       	}
    else super.doAction(command);
   } */

}

