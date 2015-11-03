/*
* MapViewer.java -Warbot: robots battles in MadKit
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

final public class MapViewer extends AbstractViewer
{
   int width=500;
   int height=500;
   int ratio=1;

 MapViewer (int w,int h,int ratio,String group)
{
	super(group);
	width = w+5;
	height = h+25; // to let some room for the number of entities
	this.ratio = ratio;
}
 MapViewer (String theGroup){super(theGroup);}


    public void initGUI()
    {
	setGUIObject(onScreen = new AwtGridCanvas(width,height,this));
    }

     void paintEntities(Graphics g)
    {
        g.setColor(Color.black);
        g.drawLine(0,0,width,0);
        g.drawLine(width-1,0,width-1,height-1);
        g.drawLine(width-1,height-1,0,height-1);
        g.drawLine(0,height-1,0,0);
        entities=world.getAllEntities();
    	if (entities!=null)
    	{
			nEntities=entities.length;
			nBasicBodies=0;
			g.setColor(Color.red);
			//Image robot = Toolkit.getDefaultToolkit().getImage("src/madkit/models/warbot/images/robot1.gif");
			for(int i=0;i<entities.length;i++)
			{  Entity e=entities[i];
				if (e instanceof BasicBody) {
					if(e.getTeam().equals("green"))
						g.setColor(Color.green);
					else
						g.setColor(Color.red);
					nBasicBodies++;
				} else {
					g.setColor(Color.black);
				}
				int r=e.getRadius()/ratio;
				if(e instanceof Home || e instanceof Wall)
					g.fillOval( (int) ((e.xcor()-e.getRadius())/ratio)+5,
								(int) ((e.ycor()-e.getRadius())/ratio)+5,r,r);
				else
					g.drawOval( (int) ((e.xcor()-e.getRadius())/ratio)+5,
								(int) ((e.ycor()-e.getRadius())/ratio)+5,r,r);
			}
       	}
    }

    public void observe()
    {
    	checkMail();
	if (show )
		if (swing)
		   onScreen.repaint();
		else
		   ((AwtGridCanvas)onScreen).display();
    }


}
