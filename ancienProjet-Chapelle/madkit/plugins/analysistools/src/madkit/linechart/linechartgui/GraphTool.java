/*
* GraphTool.java - LineCharts for MadKit
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
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Enumeration;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

 class GraphTool extends JFrame{


	 	GraphComponent MonRepere;
		JLabel monlabel;
		Color couleur;
		int i=0;
		JCheckBox check;
		JCheckBox state;
		JComboBox combo;
		Container content;


	 GraphTool(GraphComponent repere) {

		MonRepere = repere;
 		content = getContentPane();

 		setTitle("Graph tool");

		for(Enumeration e=MonRepere.TabGraph.elements(); e.hasMoreElements();)
			{

		Graph G = (Graph) e.nextElement();
		i = i++;
		}
		content.setLayout(new GridLayout(i,3));



	for(Enumeration e=MonRepere.TabGraph.elements(); e.hasMoreElements();)
			{

		Graph G = (Graph) e.nextElement();

		monlabel = new JLabel(G.getName());





		if(G.isActivated())
		 check= new JCheckBox(monlabel.getText(),true);
		else  check = new JCheckBox(monlabel.getText(),false);
		check.addActionListener(new Evenement(MonRepere));
		content.add(check);



		if(G.drPoint == true) state = new JCheckBox("Points ", true);
		else state = new JCheckBox("Points ", false);
		state.addActionListener(new Evenement3(MonRepere,G));
		content.add(state);



		combo = new JComboBox();

		combo.addItem("red");
		if(G.getColor().equals(Color.red)) combo.setSelectedItem("red");

		combo.addItem("green");
		if(G.getColor().equals(Color.green)) combo.setSelectedItem("green");


		combo.addItem("blue");
		if(G.getColor().equals(Color.blue)) combo.setSelectedItem("blue");

		combo.addItem("black");
		if(G.getColor().equals(Color.black)) combo.setSelectedItem("black");

		combo.addItem("yellow");
		if(G.getColor().equals(Color.yellow)) combo.setSelectedItem("yellow");

		combo.addItem("cyan");
		if(G.getColor().equals(Color.cyan)) combo.setSelectedItem("cyan");


		combo.addItem("darkGray");
		if(G.getColor().equals(Color.darkGray)) combo.setSelectedItem("darkGray");

		content.add(combo);
		combo.addItemListener(new Evenement2(MonRepere,G));




				}


	      // pack();

	}

}


class Evenement implements ActionListener {

	GraphComponent MonRepere;
	 Graph G;
	 Evenement(GraphComponent MonRepere)
	{
		this.MonRepere = MonRepere;
	}

	public 	 void actionPerformed(ActionEvent e)
	{
		String ev = e.getActionCommand();
		for(Enumeration en=MonRepere.TabGraph.elements(); en.hasMoreElements();)
			{
		  G = (Graph) en.nextElement();


		if(ev.equals(G.getName()))
			{
			if (G.isActivated())
				{
					G.Desactivate();
					//G.setDrawPoint(false);
				}
			else G.Activate();
			}

		}
		MonRepere.paintImmediately(0,0,MonRepere.sizeX,MonRepere.sizeY);
	}
}

class Evenement2 implements ItemListener {

	 GraphComponent MonRepere;
	 Graph G;
	 int index;
	public Evenement2(GraphComponent MonRepere, Graph G)
	{
		this.MonRepere = MonRepere;
		this.G = G;
	}

	public void itemStateChanged(ItemEvent e)
	{

		String ev = e.getItem().toString();

		if(ev.equals("red")) {
					G.setColor(Color.red);

					}
		if(ev.equals("green")) {

			G.setColor(Color.green);


			}

		if(ev.equals("blue")) {
				G.setColor(Color.blue);

			}

		if(ev.equals("black")) {
				G.setColor(Color.black);

				}

		if(ev.equals("yellow")) {
				G.setColor(Color.yellow);

			}


		if(ev.equals("cyan")){

			 G.setColor(Color.cyan);


			 }

		if(ev.equals("darkGray")) {

			 G.setColor(Color.darkGray);

			 }


		MonRepere.paintImmediately(0,0,MonRepere.sizeX,MonRepere.sizeY);
	}

}

class Evenement3 implements ActionListener {

	 GraphComponent MonRepere;
	 Graph G;
	 int index;

	public Evenement3(GraphComponent MonRepere, Graph G)
	{
		this.MonRepere = MonRepere;
		this.G = G;
	}

	public void actionPerformed(ActionEvent e)
	{

	if(G.getDrawPoint())
		G.setDrawPoint(false);
	else G.setDrawPoint(true);
	MonRepere.paintImmediately(0,0,MonRepere.sizeX,MonRepere.sizeY);
	}

}