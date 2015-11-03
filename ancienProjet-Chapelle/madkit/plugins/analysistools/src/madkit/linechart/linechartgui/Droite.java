/*
* Droite.java - LineCharts for MadKit
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
import java.awt.Color;
import java.awt.Graphics;


 class Droite  {
	
	
	int xInit ;
	int yInit ;
	int  xFin, yFin;

	
	Color couleur;
	GraphComponent MonRepere;
	
	public Droite(int xInit,int yInit,int xFin,int yFin,GraphComponent leRepere) {
	MonRepere = leRepere;	
	this.xInit = xInit;
	this.yInit = yInit;
	this.xFin =  xFin;
	this.yFin =  yFin;
	
	
}

	/** Attribution d'une couleur à la droite */
	
	public void setColor(Color couleur)
	{
		this.couleur = couleur;
		
		}
	
	/**Affichage de la droite */
	
	public void dessineToi(Graphics g)
	{
		
	g.setColor(couleur);
	
	
	if(MonRepere.zoom_mode)
	{
		
	if(((MonRepere.coordY-yFin*MonRepere.espaceY/MonRepere.echelleY)<20)&& (MonRepere.coordY>0))
		{
				
		    MonRepere.setEchelleY(MonRepere.getEchelleY()+10);
		    
		}	
	}
	
	g.drawLine((xInit*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX),(MonRepere.coordY-yInit*MonRepere.espaceY/MonRepere.echelleY), (xFin*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX) ,(MonRepere.coordY-yFin*MonRepere.espaceY/MonRepere.echelleY));
	
	}
	
	/**Affichage des points(les croix) de la droite */
	public void dessineToiBis(Graphics g)
	{
	g.setColor(Color.black);
	
	g.drawLine((xInit*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX),(MonRepere.coordY-yInit*MonRepere.espaceY/MonRepere.echelleY),(xInit*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX)+2,(MonRepere.coordY-yInit*MonRepere.espaceY/MonRepere.echelleY)+2);
	g.drawLine((xInit*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX),(MonRepere.coordY-yInit*MonRepere.espaceY/MonRepere.echelleY),(xInit*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX)-2,(MonRepere.coordY-yInit*MonRepere.espaceY/MonRepere.echelleY)-2);
 	g.drawLine((xInit*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX),(MonRepere.coordY-yInit*MonRepere.espaceY/MonRepere.echelleY),(xInit*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX)+2,(MonRepere.coordY-yInit*MonRepere.espaceY/MonRepere.echelleY)-2);
 	g.drawLine((xInit*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX),(MonRepere.coordY-yInit*MonRepere.espaceY/MonRepere.echelleY),(xInit*MonRepere.espaceX/MonRepere.echelleX+MonRepere.coordX)-2,(MonRepere.coordY-yInit*MonRepere.espaceY/MonRepere.echelleY)+2);
 	
 		
	}

}

	
	
	
	