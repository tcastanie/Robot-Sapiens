/*
* AutomatonTransition.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit.Formalisms.Automaton;

import java.awt.*;
import java.util.*;

import SEdit.*;
import SEdit.Graphics.*;
import SEdit.Formalisms.*;
import SEdit.Formalisms.StateTransition.*;

/***********************************************************
				CLASSE AutomatonTransition
************************************************************/

public class AutomatonTransition extends SimpleTransition 
{
    public boolean activated=false;
    public String currentWord;

    public char c='#' ; // le caractère que peut analyser l'automate
    /**
       * Get the value of c.
       * @return Value of c.
       */
    public char getTransitionValue() {return c;}
    
    /**
       * Set the value of c.
       * @param v  Value to assign to c.
       */
    public void setTransitionValue(char  v) {this.c = v; setLabel(""+c);}
    
    
    
   public void wakeUp(String w) 
    {
	getGObject().getEditor().repaint();
	
	if  (w.equals(""))
	    activated = false;
	else if (c == w.charAt(0)) {
	    activated = true;
	    // et on se place dans le scheduler de la structure
	    ((AutomatonStructure)structure).setActivated(this);
	    currentWord = w;
	}
	else
	    activated = false;
  				// on dit a tous les arcs entrants et
  				// sortants qu'ils doivent se placer dans l'Žtat 'activated'
	if (inArrows != null)
	    for(int i = 0; i < inArrows.size(); i++)
		((AutomatonLink)inArrows.elementAt(i)).active(activated);
	if (outArrows != null)
	    for(int i = 0; i < outArrows.size(); i++)
		((AutomatonLink)outArrows.elementAt(i)).active(activated);

	if(activated)
            getGObject().setForeground(Color.blue);
	else
            getGObject().setForeground(Color.black);
	
	getGObject().getEditor().repaint();
    }
    
    public void validate()
    {
	if (activated){
	    if (inArrows != null)
		for(int i = 0; i < inArrows.size(); i++)
		    ((AutomatonLink)inArrows.elementAt(i)).consume();
	    if (outArrows != null)
		for(int i = 0; i < outArrows.size(); i++)
		    ((AutomatonLink)outArrows.elementAt(i)).produce(currentWord.substring(1));
	} else
	    System.out.println("erreur, transition validee non active!!");
	
    }
        

    
}


























































