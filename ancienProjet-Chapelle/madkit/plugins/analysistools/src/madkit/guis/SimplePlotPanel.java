/*
* SimplePlotPanel.java - Simulation: the general classes for handling simulation in MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.guis;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

/** a basic f(x) info displayer for watchers. For testing purposes.

  @author Fabien MICHEL
  @version 1.2 31/01/2000 */

public class SimplePlotPanel extends Canvas

{

	Image buffer;

	Graphics bufferGraphics;

	public int width = 250;

	public int height=300;

	int x;

	int y;

	int scale=1;

	static int step = 1;

	String unitName;

    

  public SimplePlotPanel(String unitName,int a,int b)

  {

	  this.unitName=unitName;

	  if (a<500) width=a;

	  if (b>500) scale = ((int) b/500)+1; 

	  height=20+(int) (b/scale);

	  setBackground(Color.black);

	  setForeground(Color.blue);

	  setSize(width,height);

  }

  

  public void initialisation()

  {

	buffer = createImage(width,height);

    bufferGraphics = buffer.getGraphics();

    bufferGraphics.setColor(Color.white);

	bufferGraphics.fillRect(0,0,width,height);

    bufferGraphics.setColor(Color.red);



    for (int i=50;i<height;i+=50)

	{

		bufferGraphics.drawString(""+(i*scale),2,height-i+10);

		bufferGraphics.drawLine(0,height-i,width,height-i);

	}

	bufferGraphics.drawString("iterations",width-65,height-10);

	bufferGraphics.drawString(""+width,width-20,height-1);

	bufferGraphics.drawString(unitName,4,10);

    bufferGraphics.setColor(Color.black);

	x=0;

  }



  public void addPoint(int data)

  {

    bufferGraphics.drawLine(x,height-y,x+step,height-(data/scale));

    x=(x+step)%width;

    y=data/scale;

	getGraphics().drawImage(buffer,0,0,this);

  }

  

  public Dimension getPreferredSize()  {    return getSize();  }



}



















