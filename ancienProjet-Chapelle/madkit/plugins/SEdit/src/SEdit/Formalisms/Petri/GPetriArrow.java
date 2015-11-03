/*
* GPetriArrow.java - SEdit, a tool to design and animate graphs in MadKit
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
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

import SEdit.*;
import SEdit.Graphics.*;
import SEdit.Scheme.*;

/*****************************************************************

            CLASS GPetriArrow

/*****************************************************************/
public class GPetriArrow extends GArrow
{

	public void paint(Graphics g){
		super.paint(g);
		System.out.println("...");
	}
	
	public void displayName(Graphics g) {
		int w;
		FontMetrics f;
		String s;
		
		int weight = ((PetriLink)getSElement()).getWeight();
		
		Object filter = ((PetriLink)getSElement()).getFilter();
		
		Point p=getLocation();
		
    		if (weight != 1) {
    			s = String.valueOf(weight);
    			f = g.getFontMetrics(g.getFont());
        		w = f.stringWidth(s);
        		g.drawString(s,p.x-w/2,p.y-4); // TOP
    		}
    		else if (filter != null) {
    			s = STools.writeToString(filter);
	    		if (s.length() > 30)
	    			s = s.substring(0,30)+"..";
	    		f = g.getFontMetrics(g.getFont());
        		w = f.stringWidth(s);
        		g.drawString(s,p.x-w/2,p.y-4); // TOP
    		}
	}

}
