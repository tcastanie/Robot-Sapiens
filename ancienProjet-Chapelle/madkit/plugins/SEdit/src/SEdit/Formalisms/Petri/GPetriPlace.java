/*
* GPetriPlace.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.*;
import java.awt.image.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

import SEdit.*; 
import SEdit.Graphics.*;
import SEdit.Scheme.*;



public class GPetriPlace extends GOval {

	public void paint(Graphics g)
	{
	   super.paint(g);
	   String s=null;
       int n = ((PetriPlace) getSElement()).getNumberTokens();
       Object o = ((PetriPlace) getSElement()).getColoredTokens();
       if (o == null) {
	       if (n == 0)
	       	return;
	       else
	         if (n <= 4) {
	       		int rr=Math.round( 3 * (float)width / 16);
	       		Point p = getCenter();
				switch( n ){
	       			case 4 : g.fillOval(p.x-rr,   p.y,      rr,rr);
	           		case 3 : g.fillOval(p.x,      p.y,      rr,rr);
	           		case 2 : g.fillOval(p.x,      p.y-rr,   rr,rr);
	           		case 1 : g.fillOval(p.x-rr,   p.y-rr,   rr,rr);
	   			}
	         } 
	         else {
	         	s = ""+n; // JF FIXME: pas beau, mais simple a faire... 
	         	FontMetrics f = g.getFontMetrics(g.getFont());
        		int w = f.stringWidth(s);
        		Point p = getCenter();
        		g.drawString(s,p.x-w/2,p.y);
	         }
	    //   		Afficheur.Afficher_nombre(g,width/4,x,y,n);
	    } else {
	    	s = STools.prinToString(o);
	    	if (s.length() > 30)
	    		s = s.substring(0,30)+"..";
	    	FontMetrics f = g.getFontMetrics(g.getFont());
        	int w = f.stringWidth(s);
        	g.drawString(s,x-w/2,y);
	    // on affiche le token coloré..
	    // on calcule les coordonnées et on affiche quelques caractères..
	   }
	    
	}

}



