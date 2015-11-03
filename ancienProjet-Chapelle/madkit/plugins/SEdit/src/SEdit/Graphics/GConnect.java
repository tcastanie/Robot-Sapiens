/*
* GConnect.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit.Graphics;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import SEdit.SConnect;

/**************************************************************************

				CLASSE GConnect

**************************************************************************/

/** classe abstraite correspondant à la représentation graphique
 des connecteurs des composants (et des modules)
 @see SConnect
 @see GInConnect
 @see GOutConnect
*/

abstract public class GConnect extends GObject {

    protected static int r=10;   	//Rayon ...
    int dir = LEFT;
    protected Color fillColor = Color.white;


    public static int getRadius() {return(r);}

    public GConnect() {
	  setDimension(r,r);
    }

    public void setDir(int d)
    { dir = d;}

    public boolean contains(Point p){
	  return ((p.x <= (x+width))  && (p.x > x) &&
		(p.y <= (y+height)) && (p.y > y));
    }

    public Point intersection(Point p){
	  return new Point(x+width/2,
			 y+height/2);
    }

    public void snapToGrid(int s){}

    public void paint(Graphics g){
	  if ((getSElement() != null) && (getSElement() instanceof SConnect)){
		// FIXME should have one file for constants
		dir = ((SConnect)getSElement()).getConnectorPlacement().getSide();
	    }
    }

  //Calcule le point d'intersection de la 1/2 droite
  //[centre du rectangle, g) avec le rectangle
  //Utilise pour positionner les Bornes sur le composant
  public void computeDir(GObject g)
  {
      /*  		float k,k1,k2;
        k1= Math.abs( (float) g.rx / (float)(x-g.x) );
        k2= Math.abs( (float) g.ry / (float)(y-g.y) );
        k=Math.min(k1,k2);

        x=Math.round(g.x+k*(x-g.x));
        y=Math.round(g.y+k*(y-g.y));

        //Direction de la Borne (Haut,Bas,Gauche,Droite)
        if( k==k1 )
        //Gauche ou Droite
        {   if( x < g.x )
                setDir('G');  //Gauche
            else
                setDir('D');  //Droite
        }
        else
        //Haut ou Bas
        {   if( y < g.y )
                setDir('H');  //Haut
            else
                setDir('B');  //Bas
		}*/
  }

  public void displayName(Graphics g) {
	String name;
	name = getSElement().getLabel();
	if (name==null)
	    name = getSElement().getID();

	FontMetrics f = g.getFontMetrics(g.getFont());
	int w = f.stringWidth(name);
	int a = f.getAscent();
	int d = f.getDescent();

        switch( dir )
        {
	case TOP :
	    g.drawString(name,x,y-d);
	    break;
	case RIGHT :
	    g.drawString(name,x+width+2,y+height-d);
	    break;
	case BOTTOM :
	    g.drawString(name,x,y+height+a);
	    break;
	case LEFT :
	    g.drawString(name,x-w-2,y+height-d);
	    break;
	}
  }
}











