/*
* DebugViewer.java -Warbot: robots battles in MadKit
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

import java.awt.Color;
import java.awt.Graphics;

final public class DebugViewer extends AbstractViewer
{
   int width=500;
   int height=500;

DebugViewer(String theGroup){super(theGroup);}

    public void initGUI()
    {
    	/*if (swing)
		setGUIObject(onScreen = new GridCanvas(width,height,this));
	else*/ 
		setGUIObject(onScreen = new AwtGridCanvas(width,height,this));
    }
    
     void paintEntities(Graphics g)
    {
	g.setColor(Color.black);
	g.drawLine(0,0,width,0);
	g.drawLine(width,0,width,height);
	g.drawLine(width,height,0,height);
	g.drawLine(0,height,0,0);
   	entities=world.getAllEntities();
    	if (entities!=null)
    	{
    	g.setColor(Color.red);
    	//Image robot = Toolkit.getDefaultToolkit().getImage("src/madkit/models/warbot/images/robot1.gif");
    	for(int i=0;i<entities.length;i++)
    	{
    		if (entities[i] instanceof BasicBody)
    		{
    			g.setColor(Color.red);
    			g.drawOval(entities[i].xcor()-entities[i].getRadius(),entities[i].ycor()-entities[i].getRadius(),entities[i].getRadius()*2,entities[i].getRadius()*2);
    			g.drawLine( entities[i].xcor(), entities[i].ycor() , entities[i].xcor() , entities[i].ycor() );
    		}
     		else if (entities[i] instanceof Rocket)
    		{
    			g.setColor(Color.black);
    			g.drawOval(entities[i].xcor()-entities[i].getRadius(),entities[i].ycor()-entities[i].getRadius(),entities[i].getRadius()*2,entities[i].getRadius()*2);
    		}
     		/*else if (entities[i] instanceof LaserShot)
    		{
    			g.setColor(Color.green);
    			g.drawLine( ((LaserShot)entities[i]).xcor(),((LaserShot)entities[i]).ycor(),((LaserShot)entities[i]).getXfinish(),((LaserShot)entities[i]).getYfinish() );
    		}*/
    		else 
    		{
    			g.setColor(Color.blue);
    			g.drawOval(entities[i].xcor()-entities[i].getRadius(),entities[i].ycor()-entities[i].getRadius(),entities[i].getRadius()*2,entities[i].getRadius()*2);
    		}
    	}
    		
       	}
    }

    public void observe()
    {
	checkMail();
	if (show)
		if (swing)
		   onScreen.repaint();
		else
		   ((AwtGridCanvas)onScreen).display();
    }
}

