/*
* CommunicatingPredator.java - A simple reactive agent library
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

import java.awt.Point;
import java.awt.Color;
import SEdit.Formalisms.World.*;
import java.io.*;
import madkit.kernel.AbstractAgent;
import java.util.*;

/**
 * Titre :        Preys
 * Description :
 * Copyright :    Copyright (c) 2000
 * Société :
 * @author J. Ferber
 * @version 1.0
 */

public class CommunicatingPredator extends AbstractPredator {

    public CommunicatingPredator() {
    }
    public void doIt() {
  	  WorldEntity e;
  	  Vector ents = body.detect();
	  PositionMessage msg = (PositionMessage) nextMessage();
  	  if (ents != null){
		 PreyBody p = getPrey(ents);
		 if (p != null){
			body.moveTo(p);
			broadcastMessage("Preys","predator",
			       new PositionMessage(p.getPosition(),getBody()));
			return;
		 } else if (msg != null){
		   body.setDirection(body.getDirectionTo(msg.getPosition()));
		   //println("receive position message");
		   body.showLine(msg.getSenderBody(),Color.blue);
		   body.move();
		 } else
			avoidPredators(ents);
  	  } else {
	        if (msg != null){
			   body.setDirection(body.getDirectionTo(msg.getPosition()));
			   //println("receive position message");
			   body.showLine(msg.getSenderBody(),Color.blue);
		       body.move();
	        } else
		      body.randomMove();
  	  }
    }
}
