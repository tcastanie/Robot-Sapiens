/*
* GOutPort.java - SEdit, a tool to design and animate graphs in MadKit
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


public class GOutPort extends GPort {


	public void paint(Graphics g)
	{
		   
		Polygon p=new Polygon();

		p.addPoint(x,y);
		p.addPoint(x+width/2,y);
		p.addPoint(x+width,y+height/2);
		p.addPoint(x+width/2,y+height);
        p.addPoint(x,y+height);

        g.drawPolygon(p);
	   
	   	super.paint(g);
	   	
	   	String s = ((PetriOutPort) getSElement()).getMessageType();
	    if (s != null) { // on affiche l'action si elle existe
	    	Point pc = getCenter();
	    	FontMetrics f = g.getFontMetrics(g.getFont());
        	int w = f.stringWidth(s);
        	int h = f.getHeight();
        	g.drawString(s,pc.x-w/2-4,pc.y);
	   }
	}
}
