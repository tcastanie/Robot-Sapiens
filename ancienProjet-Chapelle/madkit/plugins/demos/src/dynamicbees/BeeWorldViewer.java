/*
* BeeWorldViewer.java - DynamicBees, a demo for the probe and watcher mechanisms
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel
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
package dynamicbees;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/**
  @version 2.0
  @author Fabien MICHEL 01/02/2001*/
public class BeeWorldViewer extends madkit.kernel.Watcher implements madkit.kernel.ReferenceableAgent
{
    Component onScreen;

    BeeColorProbe p1;
    BeePointProbe p2;
    BeePointProbe p3;
    BeeColorProbe qp1;
    BeePointProbe qp2;
    BeePointProbe qp3;
  
    int width=300;
    int height=300;
    
    boolean show = true;
    boolean swing = true;
    public void setShow (boolean add){show = add;}
    public boolean getShow(){return show;}
 
    public BeeWorldViewer()
    {
    }
    public BeeWorldViewer(boolean b,int w,int h)
    {
    	this();
    	swing = b;
    	width=w;
    	height=h;
    }

    public void initGUI()
    {
    	if (swing)
		setGUIObject(onScreen = new GridCanvas(width,height,this));
	else 
		setGUIObject(onScreen = new AwtGridCanvas(width,height,this));
    }
    
    public void paintBees(Graphics g)
    {
	BeeColor[] colors =  p1.getColors();
	Point[] points =  p2.getPoints();
	Point[] oldPoints =  p3.getPoints();
	
	if (colors!=null)	
	for(int i=0;i<colors.length;i++)
	    {
		g.setColor(colors[i].currentColor);
		g.drawLine((oldPoints[i]).x,(oldPoints[i]).y,
			   (points[i]).x,(points[i]).y);
		/*g.setColor(Color.white);
		
		g.drawLine((points[i]).x,(points[i]).y,
			   (points[i]).x,(points[i]).y);*/
	    }
    }
    
    public void paintQueenBees(Graphics g)
    {
	BeeColor[] colors =  qp1.getColors();
	Point[] points =  qp2.getPoints();
	Point[] oldPoints =  qp3.getPoints();
	
	if (colors!=null)	
	for(int i=0;i<colors.length;i++)
	    {
		g.setColor(colors[i].currentColor);
		g.drawLine( (oldPoints[i]).x,(oldPoints[i]).y,
			   (points[i]).x,(points[i]).y);
		g.fillOval( (points[i]).x,(points[i]).y,6,6);
	    }
    }

    synchronized public void observe()
    {
	if (show)
		if (swing)
		   onScreen.repaint();
		else
		   ((AwtGridCanvas)onScreen).display();
    }

    public void activate()
    {
    	if (! swing) ((AwtGridCanvas)onScreen).initialisation();
	p1=new BeeColorProbe("bees","bee","beeColor");
	p2 = new BeePointProbe("bees","bee","p");
	p3 = new BeePointProbe("bees","bee","oldp");
	addProbe(p1);
	addProbe(p2);
	addProbe(p3);
	qp1=new BeeColorProbe("bees","queen bee","beeColor");
	qp2 = new BeePointProbe("bees","queen bee","p");
	qp3 = new BeePointProbe("bees","queen bee","oldp");
	addProbe(qp1);
	addProbe(qp2);
	addProbe(qp3);
	foundGroup("bees");
	requestRole("bees","bee observer",null);
	System.err.println("Activated");
    }
    
    public void end()
    {
   	disposeMyGUI();
   	super.end();
   }
    
}

class AwtGridCanvas extends Canvas
{
  BeeWorldViewer simuViewer;
  Image buffer;
  Graphics bufferGraphics;

  public AwtGridCanvas(int width,int height,BeeWorldViewer l)
    {
    setBackground(Color.black);
    setForeground(Color.blue);
	setSize(width,height);
	simuViewer=l;
    }
  
 void initialisation()
  {
	Dimension d = getSize();
	buffer = createImage(d.width,d.height);
   	 bufferGraphics = buffer.getGraphics();
   	 bufferGraphics.setColor(Color.black);
	bufferGraphics.fillRect(0,0,d.width,d.height);
  }

  public Dimension getPreferredSize() {return getSize();}

  void display()
  {
	Dimension d = getSize();
   	bufferGraphics.setColor(Color.black);
	bufferGraphics.fillRect(0,0,d.width,d.height);
	simuViewer.paintBees(bufferGraphics);
	simuViewer.paintQueenBees(bufferGraphics);
	getGraphics().drawImage(buffer,0,0,this);
  }
}
