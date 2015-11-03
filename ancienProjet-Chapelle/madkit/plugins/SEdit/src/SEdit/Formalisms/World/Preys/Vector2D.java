/*
* Vector2D.java - A simple reactive agent library
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

/**
 * Titre :        Preys
 * Description :
 * Copyright :    Copyright (c) 2000
 * Société :
 * @author J. Ferber
 * @version 1.0
 */

public class Vector2D {
       static final double convDegGrad = 0.0174533;
        static final double convGradDeg = 57.29577951;

    double x=0.0;
	double y=0.0;

	public double getX(){return x;}
	public double getY(){return y;}

    public Vector2D(double _x, double _y) {
    }
	public Vector2D add(Vector2D a){
		   double rx = x+a.getX();
		   double ry = y+a.getY();
		   return new Vector2D(rx,ry);
	}

	public Vector2D mult(double c){
		   double rx = Math.round(x*c);
		   double ry = Math.round(y*c);
		   return new Vector2D(rx,ry);
	}

	public Vector2D getOpposite(){
		   return new Vector2D(-x,-y);
	}

	public void opposite(){
		   x = -x;
		   y = -y;
	}

	public int rho(){
		   return((int) Math.round(Math.sqrt(x*x+y*y)));
	}

	// angle in degre
	public int theta(){
		   double dir = Math.atan(((double) y)/(double) x);
  	       int direct = (int)Math.round(dir*convGradDeg);
  	       if ((direct < 0)||(y < 0)) {
			  if (x > 0)
				 direct = 360+direct;
			  else
				direct = 180+direct;
		   }
  	       return direct;
	}

	public void addPolar(int d, int r){
		   x = x+Math.round(d*Math.cos(convDegGrad*r));
		   y = y+Math.round(d*Math.sin(convDegGrad*r));
	}

	public void normalize(){
		   double r = rho();
		   x = x/rho();
		   y = y/rho();
	}

	public String toString(){
		   return("Vector2D("+x+", "+y+")");
	}

}
