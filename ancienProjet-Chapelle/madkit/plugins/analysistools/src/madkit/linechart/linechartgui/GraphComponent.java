/*
* GraphComponent.java - LineCharts for MadKit
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
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JScrollBar;


 class GraphComponent extends JComponent { 
	
	
    /** Coordonnées de l'origine du repère */
	
    int coordX ;
    int coordY ;
    int coordX_Bis;
    int lastIndice;
    
    /** mode_negatif =true => il y a des y<0 Sinon mode_negatif = false*/
    
    boolean mode_negatif = false;
    boolean FirstTime;
	 
	
    /** Longuer de l'axe des  X et de  l'axe Y*/
	
    int sizeAxeX;
    int sizeAxeY;
	 
	 
    int old_sizeAxeX;
    int old_sizeAxeY;
	
    int SommeX =0;
    int SommeY =0;
    int Somme =0;
	
    boolean drPoint = false;
    boolean drTitleX =false;
    boolean drTitleY =false;
    boolean drIndiceX = true;
    boolean drIndiceY = true;
    boolean drTitleGraphe = false;
	
    int translation ;
	
    /** Espace entre les segments :*/
	
    int espaceX;
    int espaceY;
	
	
    Droite D;
    Graph G;
    Vector TabGraph;
    PointP P;
	
    int X;
    int Y;
	
    boolean zoom_mode;
	
    JScrollBar Scroll ;
	
    String TitleAxeX;
    String TitleAxeY;
    String TitleGraphe;
	
    Color couleur;
	
    int oldCoordX;
    int sizeX;
    int sizeY;
	
    int old_sizeX;
    int old_sizeY;
	
    int echelleX;
    int echelleY;
    int echelleXX;
    int echelleYY;
    int echelleX_old;
    int echelleY_old;
    int x_init;
    int y_init;
    int x_fin;
    int y_fin;
	
    GraphTool graphtool;

    /** Constructeur, prend en parametre la longueur de la fenetre en X et en Y et le scrollBar*/
	
    public GraphComponent (int sizeX,int sizeY, JScrollBar monScroll) 
    {
	setDimension(sizeX,sizeY);
	setSize(new Dimension(sizeX,sizeY));
	setPreferredSize(new Dimension(sizeX,sizeY));
		
	espaceX = 40;
	espaceY = 40;
	translation = 1;
	Scroll = monScroll;
	       
	zoom_mode = false;

	G = new Graph();
	P = new PointP();
	TabGraph = new Vector();
	Somme = (coordX+sizeAxeX) ;
	this.setBackground(Color.white);
		
	graphtool = new GraphTool(this);
	//Premier tour => FirstTime = true Sinon FirstTime = false; 
	FirstTime = true;
		
	X = 0;
	Y = 0;
	echelleX = 10;
	echelleY = 100;
		
	echelleX_old = echelleX;
	echelleY_old = echelleY;
	echelleXX = 0;
	echelleYY = 0;
	validate();	
	repaint();
    }

  public Dimension getPreferredSize()  {    return getSize();  }

	
    /**Initialisation(ou modification) de la longueur de l'axe des X */
	
    void setSizeAxeX (int newSizeX) {
		
	sizeAxeX = newSizeX;
		
    } 
	
	
    /**Initialisation(ou modification) de la longueur de l'axe des Y */
	
    void setSizeAxeY (int newSizeY) {
		
	sizeAxeY = newSizeY;
    }
	
    /**Affectation de l'espace entre deux points sur l'axe des X */
	
    void setEspaceX(int newEspace){
	espaceX = newEspace;
    }
    /** Affectation (ou modification) de l'echelle de l'axe des X*/
	
    void setEchelleX(int newEchelleX)
    {
	echelleX = newEchelleX;	
    }
    
    /**Affectation de l'echelle de l'axe Y */
    	
    void setEchelleY(int newEchelleY) {
	echelleY = newEchelleY;
    }
    
    /**Retourne l'echelle de l' Axe X */
    	
    int getEchelleX() {
    		
	return echelleX;
    }
    	
    /**Retourne l'echelle de l'axe Y */
 
    int getEchelleY(){
    		
	return echelleY ;
    }
    	
    /**Retourne l'espace entre deux points de l'axe X */ 
 	  	
    int getEspaceX() {
	return espaceX;
    }
	
    /**Affectation  de l'espace entre deux points de l'axe Y*/
	
    void setEspaceY(int newEspace){
	espaceY = newEspace;
    }
	
    /** Retourne l'espace entre deux points de l'axe Y*/
	
    int getEspaceY() {
	return espaceY;
    }
	
    public void paint(Graphics g) {
		
	
	Graph current_g=null;	
 	g.setColor(Color.black);
	SommeX = 0;
	SommeY = 0;
	echelleXX = 0;
	echelleYY = 0;
		
		
		
	drawAxeX(g);
		
	drawAxeY(g);
		
	//for(Enumeration e=TabGraph.elements(); e.hasMoreElements();)
	 
	 if(TabGraph != null)
	 
	 	for(int i=0; i<TabGraph.size();i++)
	    		{
		//	current_g = (Graph) e.nextElement();
	      		current_g = (Graph) TabGraph.elementAt(i);
	      		if(current_g.isActivated())
		    	{  
		  		current_g.dessineToi(g);
		    		if(current_g.getDrawPoint())
			    		current_g.P.dessineToi(g);
		    	}   

	    	}

		
	if(drTitleX ==true)
	    drawTitleAxeX(g);
		
	if(drTitleY == true)
	    drawTitleAxeY(g);
	if(drTitleGraphe == true)
	    drawTitleGraphe(g);
		
    }
	
	
    void drawAxeX(Graphics g)
    {
	
	g.drawLine(coordX, coordY,sizeX-20,coordY);
	
	
	drawTriangleX(g, sizeX-20, coordY );
	int Var1 = coordX;     
	
	while(Var1<(sizeX-10))
	    {
		drawEchelleX(g, Var1);
		Var1 = Var1 + espaceX;
	    } 
	
    }

    /** Les transletions sont composées de deux types: Gauche, Droite. Mais aussi des fonctions du type TranslationG/D et TranslG_Bis/D_Bis, les une utilisées par le gestionnaire d'événement l'autre par GraphComponent.*/
    void translationRightAxeX_Bis()
    {
	sizeAxeX = sizeAxeX + espaceX;
	coordX = coordX - espaceX;
 		
	if(coordX > 20) 
	    { 
		coordX = 20;
		sizeAxeX= old_sizeAxeX;
		paintImmediately(0,0,sizeX,sizeY);
 			
	    }
	paintImmediately(0,0,sizeX,sizeY);
 		
 		
    }
 	
    int getSomme(){
	return Somme;
    }
 	
 	
    void translationRightAxeX()
    {
 	Scroll.setValue(Scroll.getValue()+1); 		
 		
    }
 	
    void translationLeftAxeX()
    {
 	Scroll.setValue(Scroll.getValue()-1);
 	
    }
 	
    void translationLeftAxeX_Bis()
    {
 		
	sizeAxeX = sizeAxeX - espaceX;
	coordX = coordX + espaceX;
 	if(coordX > 20) 
	    { 
		coordX = 20;
		old_sizeAxeX = sizeAxeX;
		paintImmediately(0,0,sizeX,sizeY);
	    }
 		 		
	paintImmediately(0,0,sizeX,sizeY);
    }
 	
 	
    void drawAxeY(Graphics g)
    {
 	g.drawLine(20,sizeY-100, 20, 20);
 	drawTriangleY(g, 20, 20);
 	int Var2;
 	
 	if(mode_negatif == true)
	    {
		Var2 = (sizeY-100)/2;
 		int i = 1;
 		while (Var2 > (20))
		    {
			drawEchelleY(g,Var2);
			Var2 = Var2 - espaceY;
 		
		    }

 		Var2 = (sizeY-100)/2;
 		
 		while (Var2< (sizeY-100))
		    {drawEchelleY_Negatif(g,Var2);
		    Var2 = Var2 + espaceY;
		    }
 		
	    }


 	else 

	    {
 		Var2 = (sizeY - 100);
 		
 		while (Var2 > (20))
		    {
			drawEchelleY(g,Var2);
			Var2 = Var2 - espaceY;
 		
		    }
	    }
 	
 	
    }
 
 
    void drawTriangleX(Graphics g, int x, int y) {
 
  	g.drawLine(x,y-2,x,y+2);
 	g.drawLine(x,y-2,x+4,y);
 	g.drawLine(x,y+2,x+4,y); 
 	this.setBackground(Color.white);
    }

	
    void drawTriangleY(Graphics g, int x, int y) {
 
  	g.drawLine(x-2,y,x+2,y);
 	
 	g.drawLine(x-2,y,x,y-4);
 	g.drawLine(x+2,y,x,y-4); 
 	this.setBackground(Color.white);
    }
 	
 	
    void drawEchelleX (Graphics g,int x) {
 		
	g.drawLine(x,coordY-2,x,coordY+2);
	if(drIndiceX == true)
	    {
 			
		Integer I = new Integer(Integer.toString(echelleXX));
 			
		g.drawString(I.toString(),x-5, coordY+15);
		echelleXX = echelleX+ echelleXX;
	    }
	lastIndice = echelleXX;
    }
 	
    void drawEchelleY (Graphics g,int y) {
 		
 		
	g.drawLine(18,y,22,y);
	if(drIndiceY == true)
	    {	
		Integer I = new Integer(Integer.toString(echelleYY));
		g.drawString(I.toString(),5,y);
		echelleYY = echelleYY + echelleY;
	    }
    }
 	
 	
 	
 	
    void drawEchelleY_Negatif (Graphics g,int y) {
 		
 		
	g.drawLine(18,y,22,y);
	g.drawLine(18,coordY+y,22,coordY+y);
 		
	if(drIndiceY == true)
	    {
		Integer I = new Integer(Integer.toString(echelleY));
 			
 			
 			
 			
		g.drawString("-"+I.toString(),5,y);
		echelleYY = echelleYY + echelleY;
	    }
    }
 		
 		
 		
    /**Dessine une croix à point x,y de couleur 'couleur' */
 	
    void drawPoint (Graphics g,Color couleur,int x, int y)
    {
 	g.setColor(couleur);
	g.drawLine(x,y,x+2,y+2);
	g.drawLine(x,y,x-2,y-2);
	g.drawLine(x,y,x+2,y-2);
	g.drawLine(x,y,x-2,y+2);
	this.setBackground(Color.white);
    }  
 	
 	
 
    /** On dessine le point x,y d'un graphe de nom 'name' */
 
 
    synchronized void drawPoint(String name,int x,int y)
    {
	Graphics gg = getGraphics();
 	Graph current_g=null;	
 		
 		
 		
	if(y<0)
	    {
		mode_negatif = true;
		paintImmediately(0,0,sizeX,sizeY);
	    }
		
		
	for(int i=0;i<TabGraph.size();i++)
	    {
		if( ((Graph)TabGraph.elementAt(i)).getName().equals(name))
		{
		current_g  = (Graph)TabGraph.elementAt(i);
		    break;
		}
	    }
	    
	if (current_g==null) return;	
    
			
	if(FirstTime )
	    {
		FirstTime = false;
	    }
		
	//Si c'est le premier point, on le stocke et on attend le deuxieme pour pouvoir dessiner une droite !
		
	if(current_g.firstTime)
	    {
				
		current_g.x_init = x;
		current_g.y_init = y;
		current_g.firstTime = false;
	    }
		
	else {
		
	    current_g.x_fin = x;
	    current_g.y_fin = y;
		
	    Droite d = new Droite(current_g.x_init,current_g.y_init,current_g.x_fin,current_g.y_fin,this);
		
	    d.couleur = current_g.getColor();
	    oldCoordX = coordX;
	
		current_g.P.ajouteDroite(d);
		current_g.ajouteDroite(d);
		 
	    
	    // =====>> Changement de l'echelle si nécessaire : 
 		
	    if(((coordY-d.yFin*espaceY/echelleY)<20)&& (coordY>0))
		{
				
		    setEchelleY(getEchelleY()+10);
		    
		}
			
   	
	    // On gere le scrollbar si on arrive en bout d'affichage ...
 	       
	    //if((d.xInit*espaceX/echelleX+coordX) >= (Somme))
	    
	    if((d.xInit>=lastIndice)&&(coordX<=20)) 
		{
			    
		if(sizeX >=300) {
		    Scroll.setValue(Scroll.getValue()+4);
		    sizeAxeX = sizeAxeX + 4*espaceX;
		    coordX = coordX - 4*espaceX;
		    
		}
		
		else {
			
		Scroll.setValue(Scroll.getValue()+1);
		sizeAxeX = sizeAxeX + espaceX;
		coordX = coordX - espaceX;	
			
			
		}
		   
		    
		    coordX_Bis = coordX;
 			
		   // paintImmediately(0,0,sizeX,sizeY);
		   
		}
 		/* if(current_g.isActivated())
		{
 		
		    d.dessineToi(gg);
		    current_g.dessineToi(gg);
		    if(current_g.drPoint)
			d.dessineToiBis(gg);
		}*/
		
		current_g.x_init = current_g.x_fin;
		current_g.y_init = current_g.y_fin;
		paintImmediately(0,0,sizeX,sizeY);
		 
		 
		 
		 
	}
	
    } 
 		
 		
    /**Ajoute le graph 'mongraph' dans le vecteur TabGraph */
 		
    void addGraph(Graph mongraph)
    {
 	
 	TabGraph.addElement(mongraph);
 	update();
 	
    }	
 
    void update() {
 		
 		
 	graphtool.setVisible(false);
 	graphtool = new GraphTool(this);
 	graphtool.show();
    }
 	
    /**Affiche le nom de l'axe des X */
 	
    void drawTitleAxeX (Graphics g)
    {
 	g.setColor(Color.black);
 	g.drawString(TitleAxeX,coordX + sizeAxeX-(TitleAxeX.length()*5),coordY+30);	
    }
 	
    /** Affiche le titre du graph*/
 	
    void drawTitleGraphe(Graphics g)
    {
 	g.setColor(Color.black);
 	g.drawString(TitleGraphe,sizeX/3, 15);	
    }
 
	
    /**Affiche le Nom de l'axe des Y */
	
    void drawTitleAxeY (Graphics g)
    {
 	g.setColor(Color.black);
 	g.drawString(TitleAxeY,10,10);	
    } 

	
    /**Initialise toute les dimensions (longeur de l'axe des X des Y... */
	
    void setDimension(int largeur, int hauteur)
    {
	sizeX = largeur;
	sizeY = hauteur;
	old_sizeX = sizeX;
	old_sizeY = sizeY;
	coordX = 20;
	if(mode_negatif==true)
	    coordY = (sizeY - 100)/2;
	else coordY = (sizeY - 100);
	
	
	
	sizeAxeX =sizeX - 40;
	old_sizeAxeX = sizeAxeX;
	old_sizeAxeY = sizeAxeY;
	sizeAxeY = sizeY - 115;
	
	
	paintImmediately(0,0,sizeX,sizeY);	
    } 



    void setDimension_Bis(int largeur, int hauteur)
    {
	
	sizeAxeX = sizeAxeX + (largeur -sizeX) ;
	sizeAxeY = sizeAxeY + (hauteur - sizeY);
	sizeX = largeur;
	sizeY = hauteur;	
	if(mode_negatif ==true)
	    coordY = (sizeY - 100)/2;
	else coordY = sizeY - 100;
	//repaint();
	paintImmediately(0,0,sizeX,sizeY);
	
    } 

    /**Affectation (modification) du nom de l'axe des X */
 
    void setTitleAxeX(String Mystring) {
	TitleAxeX = Mystring;
    }
 	
    /**Affectation (modification) du nom de l'axe des Y */
 	
    void setTitleAxeY(String Mystring) {
	TitleAxeY = Mystring;
    }
 
    /**Affectation (modification) du titre du graph*/
 	
    void setTitleGraph(String titre)
    {
	TitleGraphe = titre;
    }
 	
 	
    /**Efface toute les courbe stockées. */
 	
    void Clear()
    {
 		
       
 	P.efface();

	for(Enumeration e=TabGraph.elements(); e.hasMoreElements();)
	    {
		G = (Graph) e.nextElement();
		G.setDrawPoint(false);
		G.efface();
		G.P.efface();
	    }


	TabGraph.removeAllElements();
 	setDimension(sizeX,sizeY);
 	Scroll.setValue(0);
 	drPoint = false;
 	drTitleX = false;
 	drTitleY = false;
 	drTitleGraphe = false;	
 	setEspaceX(40);
 	setEspaceY(40);
	drIndiceX = true;
	drIndiceY = false;
 	if(graphtool != null) 
	    graphtool.setVisible(false);
 		
 	graphtool = new GraphTool(this);
 	graphtool.setVisible(false);
 	FirstTime = true;
 	
 	repaint();	
 		
    }
 
}
