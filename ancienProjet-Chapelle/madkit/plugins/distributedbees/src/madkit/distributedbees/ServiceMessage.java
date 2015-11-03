/*
* ServiceMessage.java - DistributedBees demo program
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

import madkit.kernel.*;
import madkit.communicator.*;
import java.awt.Point;

public class ServiceMessage extends Message implements UDPMessage
{  

    Point[] table;
    
    public ServiceMessage()
    {
        super();
    }   

    public ServiceMessage(Point[] tableau)
    {
        super();
        table = tableau;
    }   
    
    public Point[] getTable()
    {
        return table;
    }

    public void setTable(Point[] t)
    {
        table = t;
    }
    
    public String toString()
    {
        return ("ServiceMessage"+table.toString());
    }
}



