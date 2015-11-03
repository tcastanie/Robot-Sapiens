/*
* PositionMessage.java - A simple reactive agent library
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

import java.awt.*;
import madkit.kernel.Message;
import SEdit.Formalisms.World.*;

/**
 * Titre :        Preys
 * Description :
 * Copyright :    Copyright (c) 2000
 * Société :
 * @author J. Ferber
 * @version 1.0
 */

public class PositionMessage extends Message {
    int x=0;
	int y=0;

	public int getX(){return x;}
	public int getY(){return y;}

	public Point getPosition(){
		   return(new Point(x,y));
	}

	WorldEntity senderBody=null;

    public PositionMessage(Point _p,WorldEntity w) {
		   x = _p.x;
		   y = _p.y;
		   senderBody=w;
    }

	public WorldEntity getSenderBody(){return senderBody;}

}
