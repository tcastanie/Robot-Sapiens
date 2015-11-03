/*
* GPetriTransition.java - SEdit, a tool to design and animate graphs in MadKit
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



public class GPetriTransition extends GRectangle {

	public void paint(Graphics g)
	{
       super.paint(g);
       Point p = getCenter();

       String s = ((PetriTransition) getSElement()).getPredicateString();
       if (s != null) { // on affiche le predicat s'il existe
	    	if (s.length() > 30)
	    		s = s.substring(0,30)+"..";
	    	FontMetrics f = g.getFontMetrics(g.getFont());
        	int w = f.stringWidth(s);
        	g.drawString(s,p.x-w/2,p.y-(height/2)-4);
	   }
	   
       s = ((PetriTransition) getSElement()).getActionString();
	   if (s != null) { // on affiche l'action si elle existe
	    	if (s.length() > 30)
	    		s = s.substring(0,30)+"..";
	    	FontMetrics f = g.getFontMetrics(g.getFont());
        	int w = f.stringWidth(s);
        	int h = f.getHeight();
        	g.drawString(s,p.x-w/2,p.y+(height/2)+h+2);
	   }
	    
	}
}



