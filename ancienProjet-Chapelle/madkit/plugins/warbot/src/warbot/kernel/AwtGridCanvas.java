/*
* AwtGridCanvas.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

final class AwtGridCanvas extends Canvas
{
  AbstractViewer simuViewer;
  Image buffer;
  Graphics bufferGraphics;

  public AwtGridCanvas(int width,int height,AbstractViewer l)
    {
    setBackground(Color.white);
    //setForeground(Color.blue);
	setSize(width,height);
	simuViewer=l;
    }

 void initialisation()
  {
	Dimension d = getSize();
	buffer = createImage(d.width,d.height);
   	bufferGraphics = buffer.getGraphics();
   	bufferGraphics.setColor(Color.white);
	bufferGraphics.fillRect(0,0,d.width,d.height);
  }

  public Dimension getPreferredSize() {return getSize();}

  void display()
  {
	Dimension d = getSize();
   	bufferGraphics.setColor(Color.white);
	bufferGraphics.fillRect(0,0,d.width,d.height);
	simuViewer.paintEntities(bufferGraphics);
	getGraphics().drawImage(buffer,0,0,this);
    getGraphics().drawString("entities:" + simuViewer.getNumberEntities() +
	    "    agents:"+simuViewer.getNumberAgents(),
		5,getHeight()-5);
  }

  public void paint(Graphics g){
        display();
  }
}
