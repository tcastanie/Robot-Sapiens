/*
* TableMessage.java - DistributedBees demo program
* Copyright (C) 1998-2004 P. Bommel, F. Michel
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

package madkit.distributedbees;

import madkit.kernel.Message;
import java.util.*;

/** Cette classe est une extension de la classe Message de MadKit. Elle est utilisée pour
 *  construire les messages échangés entre
 *  {@link BeeLauncher BeeLaunchers} (voir
 *  {@link BeeLauncher#conversion()}).
 */
public class TableMessage extends Message
{
    /** Tableau de vecteurs */
    Vector[] table;
    List theLaunchers;

    /** Constructeur: initialisation du tableau de vecteurs. */
    public TableMessage(Vector[] tableau)
    {
	super();
	table = tableau;
        theLaunchers = new Vector(0);
    }

    public TableMessage(Vector[] tableau, List launchers)
    {
	super();
	table = tableau;
        theLaunchers = launchers;
    }

    /** Accesseur en lecture du tableau de vecteurs.*/
    public Vector[] getTable()
    {
	return table;
    }

    public List getLaunchers()
    {
	return theLaunchers;
    }
    /** Renvoie le string "TableMessage". */
    public String toString()
    {
	//return ("MasterMessage: tableau = " + table.toString());
	return ("TableMessage");
    }
}


