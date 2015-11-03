/*
* AutomatonState.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.util.*;
import java.io.*;
import SEdit.*;
import SEdit.Graphics.*;
import SEdit.Formalisms.StateTransition.*;
import java.awt.Color;


/**********************************************************

				CLASSE AutomatonState

***********************************************************/



public class AutomatonState extends SimpleState 
{

    public AutomatonState()
    {
	setLabel("");
    }
    
    String word = null;// = "$";
    
    /**
       * Get the value of word.
       * @return Value of word.
       */
    public String getWord() {return word;}
    
    /**
       * Set the value of word.
       * @param v  Value to assign to word.
       */
    public void setWord(String  v) { produce(v);}
    

    public void activate()
    {
	if (word != null)
	    {
		if (getOutArrows() != null)
		    for (Enumeration e=getOutArrows().elements(); 
			 e.hasMoreElements();)
			((AutomatonLink) e.nextElement()).activate(word);
	    }
	if (getGObject()!=null)
	    {
		if ((word != null) && (!word.equals("")))	
		    getGObject().setForeground(Color.blue);
		else
		    getGObject().setForeground(Color.black);
		getGObject().getEditor().repaint();
	    }
    }
    
    
    public void produce(String w) 
    {
	word = w;
   	if (word != null) 
	    activate();
	setLabel(word);
    }
}










