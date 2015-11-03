/*
* ToolBar.java - LineCharts for MadKit
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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JToolBar;


 class ToolBar extends JToolBar {
	
	 GraphComponent MonRepere;
	JButton GraphTool;
	
	public ToolBar(GraphComponent repere) {
	MonRepere = repere;
	
	JButton ZoomPlusX = new JButton("Zoom + X");
	JButton ZoomMoinsX = new JButton("Zoom - X");
	
	JButton ZoomPlusY = new JButton("Zoom + Y");
	JButton ZoomMoinsY = new JButton("Zoom - Y");
	
	JButton InitZoom = new JButton("Init Zooms");
	
	GraphTool = new JButton("Graph tool");
	GraphTool.addActionListener(new GraphToolEvent(MonRepere));
	
	
	JButton Reset = new JButton("Reset");
	Reset.addActionListener(new Clear(this));
	
	ZoomPlusX.addActionListener(new ZoomPlusXEvent(this));
	ZoomPlusY.addActionListener(new ZoomPlusYEvent(this));
	ZoomMoinsX.addActionListener(new ZoomMoinsXEvent(this));
	ZoomMoinsY.addActionListener(new ZoomMoinsYEvent(this));
	InitZoom.addActionListener(new InitZoomEvent(this));
	
	add(ZoomPlusX);
	add(ZoomMoinsX);
	add(ZoomPlusY);
	add(ZoomMoinsY);
	add(InitZoom);
	add(GraphTool);
	//add(Reset);

	}
}

class ZoomPlusXEvent implements ActionListener {
	
	public ToolBar toolbar;
	public int echelleX;
	
	public ZoomPlusXEvent(ToolBar toolbar)
	{
	this.toolbar = toolbar; 	
	}
	
	public void  actionPerformed(ActionEvent e) {
		
		
		echelleX = toolbar.MonRepere.getEchelleX();
		
		if(toolbar.MonRepere.getEchelleX() <=1) 
			toolbar.MonRepere.setEchelleX(echelleX);
		
		else { 
			
			if(toolbar.MonRepere.getEchelleX() <= 10)
				toolbar.MonRepere.setEchelleX(toolbar.MonRepere.getEchelleX()-1);
			
			else toolbar.MonRepere.setEchelleX(toolbar.MonRepere.getEchelleX() - 10);
				
		}
		
		
			
		//toolbar.MonRepere.sizeAxeX = toolbar.MonRepere.sizeAxeX-;
		
		
		//toolbar.MonRepere.Scroll.setBlockIncrement(toolbar.MonRepere.Scroll.getBlockIncrement()+toolbar.MonRepere.Scroll.getBlockIncrement()/4) ;
		toolbar.MonRepere.paintImmediately(0,0,toolbar.MonRepere.sizeX,toolbar.MonRepere.sizeY);
		
		
	}


}		


class ZoomPlusYEvent implements ActionListener {
	
	public ToolBar toolbar;
	int echelleY;
	
	public ZoomPlusYEvent(ToolBar toolbar)
	{
	this.toolbar = toolbar; 
		
	}
	
	public void  actionPerformed(ActionEvent e) {
		
		
		echelleY = toolbar.MonRepere.getEchelleY();
		
			
		if(toolbar.MonRepere.getEchelleY() <= 1) 
			{
				toolbar.MonRepere.setEchelleY(echelleY);
			
			}
		else {
			if(toolbar.MonRepere.getEchelleY()<=10)
			toolbar.MonRepere.setEchelleY(toolbar.MonRepere.getEchelleY()-1);
			
			else toolbar.MonRepere.setEchelleY(toolbar.MonRepere.getEchelleY() -10);
			}
		
		
		toolbar.MonRepere.zoom_mode = true;
		
		toolbar.MonRepere.sizeAxeY = toolbar.MonRepere.sizeAxeY+toolbar.MonRepere.sizeAxeY/4;
		toolbar.MonRepere.Scroll.setBlockIncrement(toolbar.MonRepere.Scroll.getBlockIncrement()+toolbar.MonRepere.Scroll.getBlockIncrement()/4) ;
		toolbar.MonRepere.paintImmediately(0,0,toolbar.MonRepere.sizeX,toolbar.MonRepere.sizeY);
		toolbar.MonRepere.zoom_mode = false;
	}


}		




class ZoomMoinsYEvent implements ActionListener {
	
	public ToolBar toolbar;
	
	public ZoomMoinsYEvent(ToolBar toolbar)
	{
	this.toolbar = toolbar; 	
	}
	
	public void  actionPerformed(ActionEvent e) {
		
		
		
		toolbar.MonRepere.zoom_mode = true;
		
		if(toolbar.MonRepere.getEchelleY() < 10) 
			
			toolbar.MonRepere.setEchelleY(toolbar.MonRepere.echelleY + 1);
		
		else toolbar.MonRepere.setEchelleY(toolbar.MonRepere.echelleY + 10);	
		
		
		toolbar.MonRepere.sizeAxeY = toolbar.MonRepere.sizeAxeY-toolbar.MonRepere.sizeAxeY/4;
		
		toolbar.MonRepere.Scroll.setBlockIncrement(toolbar.MonRepere.Scroll.getBlockIncrement()-toolbar.MonRepere.Scroll.getBlockIncrement()/4) ;
		toolbar.MonRepere.paintImmediately(0,0,toolbar.MonRepere.sizeX,toolbar.MonRepere.sizeY);
		toolbar.MonRepere.zoom_mode = false;
		
	}


}		


class ZoomMoinsXEvent implements ActionListener {
	
	public ToolBar toolbar;
	
	public ZoomMoinsXEvent(ToolBar toolbar)
	{
	this.toolbar = toolbar; 	
	}
	
	public void  actionPerformed(ActionEvent e) {
		
		
		if(toolbar.MonRepere.getEchelleX()< 10)
			toolbar.MonRepere.setEchelleX(toolbar.MonRepere.echelleX + 1);
		else toolbar.MonRepere.setEchelleX(toolbar.MonRepere.echelleX + 10);
			
		
		//toolbar.MonRepere.sizeAxeX = toolbar.MonRepere.sizeAxeX-toolbar.MonRepere.sizeAxeX/4;
		
		
		//toolbar.MonRepere.Scroll.setBlockIncrement(toolbar.MonRepere.Scroll.getBlockIncrement()-25*toolbar.MonRepere.Scroll.getBlockIncrement()/100) ;
		toolbar.MonRepere.paintImmediately(0,0,toolbar.MonRepere.sizeX,toolbar.MonRepere.sizeY);
	}


}
		

class  Clear implements ActionListener {
	
	public 	ToolBar toolbar;
	
	public Clear(ToolBar toolbar)
	{
		this.toolbar = toolbar;
	}
	
	public void  actionPerformed(ActionEvent e)
	{
		
		toolbar.MonRepere.Clear();
	}
}


class GraphToolEvent implements ActionListener{
	
	GraphComponent Repere;
	
	public GraphToolEvent(GraphComponent repere)
	{
		
		Repere = repere;
	}
	
	public void actionPerformed(ActionEvent e)
	{
	Repere.graphtool.setVisible(true);	
			
	}
}


class InitZoomEvent implements ActionListener {
	
	public ToolBar toolbar; 
	
	public InitZoomEvent(ToolBar toolbar){
	this.toolbar = toolbar;
	}

	public void actionPerformed(ActionEvent e)
	{
	toolbar.MonRepere.setEchelleX(toolbar.MonRepere.echelleX_old);
	toolbar.MonRepere.setEchelleY(toolbar.MonRepere.echelleY_old);	
	toolbar.MonRepere.paintImmediately(0,0,toolbar.MonRepere.sizeX,toolbar.MonRepere.sizeY);	
	}

} 