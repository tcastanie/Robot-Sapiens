/*
* Graph.java - LineCharts for MadKit
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
import java.util.Enumeration;

class Graph extends java.util.Vector {

	String Nom;
        Color couleur;
       	boolean activate;
       	boolean drPoint;
        PointP P;
	int x_init;
	int y_init;
	int x_fin;
	int y_fin;
	boolean firstTime = true;
	
	
	// Affichages des points du graphe O/N
	
	 void setDrawPoint(boolean i) {
		drPoint = i;}
	
	/** Retourne T si on affiche les points du graph , F sinon.*/
	
	 boolean getDrawPoint() {
		
		return drPoint;
		}
       
       
       /** Attribution d'un nom à un graph. Peut servir à changer le nom du graph */
       
        void setName(String Nom)
	{
		this.Nom = Nom;	
		
	}	
	
	/** Retourne le nom du graph */
	
	 String getName()
	{
		
		return Nom;	
		
	}

    	/** returne T si le graph est actif (actif = le graph s'affichera) F sinon */
    	
     boolean isActivated(){
	return activate;
    }

	/** Activer un graph (le graph s'affichera)*/
	
     void  Activate(){
	activate = true;
    }
    
    /** Désactiver le graph (le graph ne s'affichera pas) */
    
     void Desactivate() {
	activate = false;

    }

 	/** Constructeurs */
 
    public  Graph () 
        {
	
	P = new PointP();
	firstTime = true;
	activate = true;
	drPoint = false;
	couleur = Color.black;
	}


	public Graph(String nom) 
	{	
	
	P = new PointP();
	firstTime = true;
	activate = true;
	drPoint = false;
	Nom = nom;
	couleur = Color.black;	
	}
	
	
	public Graph(String nom, Color couleur)
	{
	P = new PointP();
	firstTime = true;
	activate = true;
	drPoint = false;
	
	Nom = nom;
	this.couleur = couleur;	
	couleur = Color.black;
	}
	
	public Graph(String nom, Color couleur,boolean state)
	{
	P = new PointP();
	firstTime = true;
	activate = true;
	drPoint = false;
	Nom = nom;
	this.couleur = couleur;	
	activate = state;	
	couleur = Color.black;
	}
	
	


	/**Attribution d'une couleur au graph */
	
         void setColor(Color couleur)
        { 
	this.couleur = couleur;
        }

	/** retourne la couleur du graph */
	
         Color getColor()
        {
	    return couleur;
	}

	
	/**Ajoute une droite au graph (ceci est fait par GraphComponent */
	
	 void ajouteDroite(Droite droite)
	{
		addElement(droite);
	}
	
	
	/** Efface toutes les droite de ce graph*/
	
	 void efface() {
		removeAllElements();
	}
	
	/** Affichage du graph*/
	
	 void dessineToi(Graphics g)
	{
		Droite droite ;
		
		for( Enumeration e = elements(); e.hasMoreElements();)
		{
		droite = (Droite) e.nextElement();
		droite.setColor(this.getColor());
		droite.dessineToi(g);
		}

	}
 }
 
		