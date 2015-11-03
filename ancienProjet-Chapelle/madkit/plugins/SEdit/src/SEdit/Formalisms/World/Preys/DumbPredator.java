/*
* DumbPredator.java - A simple reactive agent library
* Copyright (C) 1998-2002 Jacques Ferber
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
package SEdit.Formalisms.World.Preys;

import SEdit.Formalisms.World.*;
import java.io.*;
import madkit.kernel.AbstractAgent;
import java.util.*;
import SEdit.*;

/**
 * Titre :        Preys
 * Description :
 * Copyright :    Copyright (c) 2000
 * Société :
 * @author J. Ferber
 * @version 1.0
 */

 /**
  * A very simple predator which is absolutely dumb!!
  */

public class DumbPredator extends AbstractPredator {

  /** brain activator*/
  public void doIt(){
  	  WorldEntity e;
  	  Vector ents = body.detect();
  	  if (ents != null){
		 PreyBody p = getPrey(ents);
		 if (p != null){
			body.moveTo(p);
			return;
		 } else
			avoidPredators(ents);
  	  } else
		    body.randomMove();
  }

}
