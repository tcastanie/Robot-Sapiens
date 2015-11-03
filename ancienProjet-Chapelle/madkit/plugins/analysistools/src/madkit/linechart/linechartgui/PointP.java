/*
* PointP.java - LineCharts for MadKit
* Copyright (C) 2000-2002 Hakim Chorfi 
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

package madkit.linechart.linechartgui;
import java.awt.Graphics;
import java.util.Enumeration;

class PointP extends java.util.Vector {
	
	
	 void ajouteDroite(Droite droite)
	
	{
		addElement(droite);
	}
	
	
	void efface() {
		removeAllElements();
	}
	
	void dessineToi(Graphics g)
	{
		Droite droite ;
		
		for( Enumeration e = elements(); e.hasMoreElements();)
		{
		droite = (Droite) e.nextElement();
		droite.dessineToiBis(g);
		}

	}
 }
 
			