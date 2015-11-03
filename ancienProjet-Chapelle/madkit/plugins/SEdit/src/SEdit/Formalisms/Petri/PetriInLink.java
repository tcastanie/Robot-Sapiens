/*
* PetriInLink.java - SEdit, a tool to design and animate graphs in MadKit
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

import gnu.lists.*;
import kawa.lang.*;

/*****************************************************************

            CLASSE PetriInLink

/*****************************************************************/

public abstract class PetriInLink extends PetriLink
{

    public abstract void doActivate();
    public abstract void inhibit();

    protected abstract boolean isEmpty();
   // protected abstract void consume(int n);
    protected LList getTokens(){
    	return(((PetriPlaceBehavior) origin).getColoredTokens());
    }

    protected abstract void consume(Hashtable env);

        /** Check if the arrow itself is o.k. for connection */
    public boolean isConnectable(SNode from, SNode to)
    {
    	return ((from instanceof PetriPlaceBehavior) && (to instanceof PetriTransitionBehavior));
    }
}

