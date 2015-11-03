/*
* ConnectorPlacement.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit;


public class ConnectorPlacement
{ 

    public static final int UNSPECIFIED = 0;
    public static final int RIGHT  = 1;
    public static final int LEFT   = 2;
    public static final int TOP    = 3;
    public static final int BOTTOM = 4;
    
    int side;
    double ratio;
    
    /**
       * Get the value of yratio.
       * @return Value of yratio.
       */
    public double getRatio() {return ratio;}
    
    /**
       * Set the value of yratio.
       * @param v  Value to assign to yratio.
       */
    public void setRatio(double  v) {this.ratio = v;}
    
    public int getSide() {return side;}
    
    /**
       * Set the value of side.
       * @param v  Value to assign to side.
       */
    public void setSide(int  v) {this.side = v;}

    public ConnectorPlacement()
    {
	side=UNSPECIFIED;
	ratio=-1;
    }
    public ConnectorPlacement(int s)
    {
	side=s;
	ratio=-1;
    }
    public ConnectorPlacement(int s, double r)
    {
	side=s;
	ratio=r;
    }
    
    
}






