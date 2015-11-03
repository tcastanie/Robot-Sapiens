/*
* LineChartGUI.java - LineCharts for MadKit
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JPanel;
import javax.swing.JScrollBar;

public class LineChartGUI extends JPanel  {


	public GraphComponent monGraphComponent;

	int sizeX=400;
	int sizeY=300;
	int largeur;
	int hauteur;

	Dimension tailleEcran;

	ToolBar toolbar;

	JScrollBar hScroll;

	public LineChartGUI  (int width,int height)
	{
                sizeX=width;
                sizeY=height;
		setPreferredSize(new Dimension(400,300));
	}

	/** Constructeur par défaut. Nécessaire pour l'intégration à madkit */

	public LineChartGUI()
	{
		sizeX = 400;
		sizeY = 300;
		setPreferredSize(new Dimension(400,300));
	}

            public Dimension getPreferredSize()  {    return getSize();  }

            public void init(){

		setLayout(new BorderLayout(2,1));

		setBackground(Color.white);


		hScroll = new JScrollBar(JScrollBar.HORIZONTAL,0,5,0,100000);

		monGraphComponent = new GraphComponent(sizeX,sizeY,hScroll);
		toolbar = new ToolBar(monGraphComponent);


		hScroll.addAdjustmentListener( new Adjustment(monGraphComponent, hScroll));


		addComponentListener(new WindowResized(this.monGraphComponent ,this));

		 add(monGraphComponent,"Center");
		 add (hScroll,"South");
		 add(toolbar,"North");

		setSize(sizeX,sizeY);
                //monGraphComponent.validate();
                validate();
		//show();
}

	/** Initialise les dimensions de la fenetre suivant la taille de l'écran */

	public void Dimensionner()
	{
	//====== La taille d'affichage de la fenêtre ====>>//

		tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		sizeX = tailleEcran.width-tailleEcran.width*25/100;
		sizeY = tailleEcran.height-tailleEcran.height*25/100;

		 largeur = getSize().width;
		 hauteur = getSize().height;

		int xPos = (tailleEcran.width - largeur)/8;
		int yPos = (tailleEcran.height - hauteur)/8;
	}

	/**Initialise l'échelle de l'axe des x (sert aussi à la modifier)*/

	public void setScaleX(int X) {
		monGraphComponent.setEchelleX(X);
		monGraphComponent.paintImmediately(0,0,monGraphComponent.sizeX,monGraphComponent.sizeY);

		}

	/**Initialise l'échelle de l'axe des y (sert aussi à la modifier)*/

	public void setScaleY(int Y) {
		monGraphComponent.setEchelleY(Y);
		monGraphComponent.paintImmediately(0,0,monGraphComponent.sizeX,monGraphComponent.sizeY);

	}

	/**Retourne l'échelle de l'axe des x */

	public int getEchelleX() {
		return monGraphComponent.getEchelleX();
		}
	/**Retourne l'echelle de l'axe des y */

	public int getEchelleY() {
		return monGraphComponent.getEchelleY();
	}

	/**Initialise l'espace entre deux points de l'axe des x */

	public void setSpaceX(int X) {
		monGraphComponent.setEspaceX(X);
		monGraphComponent.paintImmediately(0,0,monGraphComponent.sizeX,monGraphComponent.sizeY);
		}

	/**Initialise l'espace entre deux points de l'axe des y */

	public void setSpaceY(int Y) {
		monGraphComponent.setEspaceY(Y);
		monGraphComponent.paintImmediately(0,0,monGraphComponent.sizeX,monGraphComponent.sizeY);

		}

	/**Retourne l'espace entre deux points de l'axe des x */

	public int getEspaceX() {
		return monGraphComponent.getEspaceX();
	}

	/**Retourne l'espace entre deux points de l'axe des y */

	public int getEspaceY() {
		return monGraphComponent.getEspaceY();
	}

	/** Crée un nouveau graph, simplement en lui attribuant un nom */

	public void addNewGraph(String nom) {
		Graph graph = new Graph();
		graph.setName(nom);
		graph.Activate();
		monGraphComponent.addGraph(graph);
	}

	/**Attribut une couleur au graph de nom name */

	public void setGraphColor(String name, Color couleur) {
		Graph G;
		for(int i=0;i<monGraphComponent.TabGraph.size();i++)
			{
				G  = (Graph) monGraphComponent.TabGraph.elementAt(i);
				if(G.getName().equals(name))
					{
						G.setColor(couleur);
						monGraphComponent.update();
						i = monGraphComponent.TabGraph.size()+1;
					}
			}

		}


	/**Ajoute puis affiche le point de cordonnée (x,y) au graph nom */

	public synchronized void drawPoint(String nom, int x,int y)
	{
		monGraphComponent.drawPoint(nom,x,y);
	}

	/**Active le graph de nom 'nom'. Ce qui implique son affichage */

	public void activate(String nom)
	{
	Graph G;
		for(int i=0;i<monGraphComponent.TabGraph.size();i++)
			{

				G  = (Graph) monGraphComponent.TabGraph.elementAt(i);

				if(G.getName().equals(nom))
					{
					G.Activate();

					i = monGraphComponent.TabGraph.size()+1;
					}
			}


	}
	/**Desactive le graphe de nom 'nom' . Ce qui le masque */

	public void desactivate(String nom) {


		Graph G;

		for(int i=0;i<monGraphComponent.TabGraph.size();i++)
			{

				G  = (Graph) monGraphComponent.TabGraph.elementAt(i);

				if(G.getName().equals(nom))
					{
					G.Desactivate();

					i = monGraphComponent.TabGraph.size()+1;
					}
			}

		}

	/** Retourne Vrai si le graphe est activé, faux sinon */

	public boolean isActivated(String nom)
	{
		Graph G;
		boolean b = false;
		for(int i=0;i<monGraphComponent.TabGraph.size();i++)
			{

				G  = (Graph) monGraphComponent.TabGraph.elementAt(i);

				if(G.getName().equals(nom))
					{
				b = G.isActivated();

					i = monGraphComponent.TabGraph.size()+1;
					}
			}
		return b;
	}

	/** Retourne la couleur du graph de nom 'nom'*/

	public Color getColor(String nom)
	{
		Graph G;
		Color couleur = Color.black;
		for(int i=0;i<monGraphComponent.TabGraph.size();i++)
			{

				G  = (Graph) monGraphComponent.TabGraph.elementAt(i);

				if(G.getName().equals(nom))
					{
					couleur = G.getColor();

					i = monGraphComponent.TabGraph.size()+1;
					}
			}
		return couleur;




	}

	/**Attribut un titre à l'axe des x */

	public void setTitleAxeX(String titreAxeX)
	{
	monGraphComponent.drTitleX = true;
	monGraphComponent.setTitleAxeX(titreAxeX);
	monGraphComponent.paintImmediately(0,0,monGraphComponent.sizeX,monGraphComponent.sizeY);
	}

	/**Attribut un titre à l'axe des y */

	public void setTitleAxeY(String titreAxeY)
	{
	monGraphComponent.drTitleY = true;
	monGraphComponent.setTitleAxeY(titreAxeY);
	monGraphComponent.paintImmediately(0,0,monGraphComponent.sizeX,monGraphComponent.sizeY);
	}

	/**Attribut un titre au graphe */

	public void setTitleGraph(String titre)
	{
	monGraphComponent.drTitleGraphe = true;
	monGraphComponent.setTitleGraph(titre);
	monGraphComponent.paintImmediately(0,0,monGraphComponent.sizeX,monGraphComponent.sizeY);
	}

	/**Affichage des indices de l'axe des x  */

	public void setDrawScaleX(boolean b)
	{
		monGraphComponent.drIndiceX = b;
	}

	/** retourne vrai si on affiche les indices de l'axe des x*/

	public boolean getDrawScaleX()
	{
		return monGraphComponent.drIndiceX;
	}

	/**Affichage des indices de l'axe des y  */

	public void setDrawScaleY(boolean b)
	{
		monGraphComponent.drIndiceY = b;
	}

	/** retourne vrai si on affiche les indices de l'axe des x*/
	public boolean getDrawScaleY()
	{
		return monGraphComponent.drIndiceY;
	}









}




class Adjustment implements AdjustmentListener {

	GraphComponent Mine;

	JScrollBar MonScroll;

	int x;

	public Adjustment(GraphComponent MonRepere, JScrollBar Scroll){
		Mine = MonRepere;
		MonScroll = Scroll;
		x = MonScroll.getValue();
	}

	public void adjustmentValueChanged(AdjustmentEvent e)
	{

		if(x< e.getValue())
		 {


		 	Mine.translationRightAxeX_Bis();

		}

		 else if (x > e.getValue())

		{



		Mine.translationLeftAxeX_Bis();

		}
		x = MonScroll.getValue();

		//==> ajuster l'axe des x à son origine

		if(x == 0)
		{
		Mine.coordX = 20;
		//Mine.paintImmediately(0,0,Mine.sizeX,Mine.sizeY);
		Mine.repaint();
		}

		if((x>MonScroll.getMaximum()-10)&& (x>20))
		{
			Graph G ;
			int max = 0;
			for(int i=0;i<Mine.TabGraph.size();i++)
			{

			G =(Graph)Mine.TabGraph.elementAt(i);
			if(G.x_init > max) max = G.x_init;
			}

			if(max>Mine.lastIndice)
			    {
				while(max>(Mine.lastIndice))
					{
						Mine.translationRightAxeX_Bis();

					}

			    }
		 else
		     {
			 if(max<(Mine.lastIndice-Mine.espaceX))
			{
			    while(max<(Mine.lastIndice-Mine.espaceX))
				Mine.translationLeftAxeX_Bis();
			}

		     }
			//	Mine.paintImmediately(0,0,Mine.sizeX,Mine.sizeY);
			Mine.repaint();




		}

	}
}

class WindowResized implements ComponentListener {

		public GraphComponent MonRepere;
		public LineChartGUI MonCreateRepere;

		public WindowResized(GraphComponent MonRepere, LineChartGUI MonCreateRepere)
		{
			this.MonCreateRepere = MonCreateRepere ;
			this.MonRepere = MonRepere;
		}

		public void componentShown(ComponentEvent e) {}

		public void componentResized(ComponentEvent e)
		{
		MonCreateRepere.Dimensionner();

		MonRepere.setDimension_Bis(MonCreateRepere.largeur, MonCreateRepere.hauteur);
		MonRepere.zoom_mode = true;
		//	MonRepere.paintImmediately(0,0,MonRepere.sizeX,MonRepere.sizeY);
		MonRepere.repaint();
	}

		public void componentMoved(ComponentEvent e) {}
		public void componentHidden(ComponentEvent e) {}
	}
