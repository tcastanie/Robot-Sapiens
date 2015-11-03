/*
* GBasicBody.java -Warbot: robots battles in MadKit
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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;


public class GBasicBody extends GEntity {

    public void paint(Graphics g){
        super.paint(g);
        BasicBody e = (BasicBody)getSElement();
        Point p = getCenter();
        if (e.getShowDetect()){
            int r = e.getDetectingRange();
            g.setColor(Color.red);
            g.drawOval(p.x-r,p.y-r,2*r,2*r);
        }
        if (e.getShowEnergyLevel()){
            if (e.initialEnergy == 0)
            e.initialEnergy = e.getEnergy();
            drawEnergyLevel(g, e.getEnergy(), e.initialEnergy);
    	}
        Brain brain=e.getBrain();
        if (brain != null){
             int n=0;
             if ((n = brain.getMessageBoxSize())>0)
                    g.drawString(""+n,p.x+e.getRadius()+2,p.y);
        }
        g.setColor(Color.blue);
        g.drawLine(p.x,p.y,(int)(p.x+20*e.angleCos),(int)(p.y+20*e.angleSin));

        if (e.isShowUserMessage()){
            drawUserMessage(g,e.getUserMessage());
        }
    }

     void drawEnergyLevel(Graphics g, int n, int max){
            double prop = (double)width/(double)max;
            int v = (int)Math.round(prop*(double)n);
            int r = width-v;
            g.setColor(Color.green);
            g.fillRect(x,y-5,v,3);
            g.setColor(Color.red);
            g.fillRect(x+v,y-5,r,3);
     }

     void showLine(int x, int y, int fromX, int fromY, Color c){
        BasicBody e = (BasicBody)getSElement();
        if (e.isShowMessages()){
           Graphics g = getEditor().getGraphics();
           g.setColor(c);
           g.drawLine(x,y,fromX,fromY);
        }
     }

     static int userRectWidth=100;
     static int userRectHeight=15;

     static Font fmess = new Font("Serif", Font.PLAIN,10);


     void drawUserMessage(Graphics g, String s){
        if (s != null){
            FontMetrics fm = g.getFontMetrics(fmess);
            int a = fm.getAscent();
            int d = fm.getDescent();
            int h = a+d+4;
            if (s.length() > 24)
	    			s = s.substring(0,24)+"..";
            int sw = fm.stringWidth(s);
            int w = (sw < userRectWidth ? sw : userRectWidth);
            g.setColor(Color.black);
            g.drawRect(x-20,y-10-h,w+5,h);
            g.drawLine(x,y,x-18,y-10);
            g.setFont(fmess);
            g.drawString(s,x-18,y-10-h+a+2);
        }
	 }



}
