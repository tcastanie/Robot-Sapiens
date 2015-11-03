/*
* PointMessage.java - DistributedBees demo program
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
import java.awt.Point;

/** A message that carries a single Point */
public class PointMessage extends Message
{  
    Point content = null;
    
    public PointMessage()
    {
	super();
    }  
 
    public PointMessage(Point v)
    {
	super();
	content=v;
    }
    
    public Point getPoint()
    {
	return content;
    }    

    public void setPoint(Point pt)
    {
	content = pt;
    }

    public String toString()
    {
	if(content != null)
	    return ("PointMessage: content = "+ content.toString());
	else return "PointMessage: content = NULL";
    }
}
