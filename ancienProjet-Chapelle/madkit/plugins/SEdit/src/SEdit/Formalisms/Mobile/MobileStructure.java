/*
* MobileStructure.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit.Formalisms.Mobile;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;
import java.lang.Math;
import java.io.*;

import SEdit.*;
import SEdit.Graphics.*;
import SEdit.Formalisms.*;
import SEdit.Formalisms.StateTransition.*;

/***********************************************************
				CLASSE MobileStructure
************************************************************/

public class MobileStructure extends Structure implements Runnable {

	protected boolean running;
  	boolean stress=false;
  	boolean random=true;
  	Thread relaxing=null;
  	int cpt=0;

    int relaxingTime=120;
    public int getRelaxingTime(){return relaxingTime;}
    public void setRelaxingTime(int t){relaxingTime=t;}

	public void start(){
	  if (!running){
	  	running = true;
      	relaxing = new Thread(this);
      	relaxing.setPriority(Thread.MAX_PRIORITY);
      	relaxing.start();
      }
    }

	public void stop(){
		running = false;
	}

	Thread relaxer;


	public MobileStructure(){;}



   public void run()
    {
      while (running) {
		relax();
		int nnodes = getNodes().size();

	  	try {relaxing.sleep(relaxingTime);}
		catch (InterruptedException e) 	{
			running = false; break;
		}
	  }
    }
    synchronized void relax()
    {
		Vector nodes = getNodes();
		int nnodes = nodes.size();

		for (Enumeration en = getArrows().elements() ; en.hasMoreElements() ;) {
		  MobileLink e = (MobileLink)en.nextElement();
		  MobileNode to = (MobileNode) e.getTarget();
		  MobileNode from = (MobileNode) e.getOrigin();


		  double vx = to.getGObject().getLocation().x - from.getGObject().getLocation().x;
		  double vy = to.getGObject().getLocation().y - from.getGObject().getLocation().y;
		  double len = Math.sqrt(vx * vx + vy * vy);
		  double f = (e.len() - len) / (len * 3) ;
		  double dx = f * vx;
		  double dy = f * vy;

		  to.addDx(dx);
		  to.addDy(dy);
		  from.addDx(-dx);
		  from.addDy(-dy);
		}

		for (int i = 0 ; i < nnodes ; i++) {

		  MobileNode n1 = (MobileNode) nodes.elementAt(i);
		  double dx = 0;
		  double dy = 0;

		  for (int j = 0 ; j < nnodes ; j++)
		    {
		      if (i == j) continue;

		      MobileNode n2 = (MobileNode) nodes.elementAt(j);

		      double vx = n1.getGObject().getLocation().x - n2.getGObject().getLocation().x;
		      double vy = n1.getGObject().getLocation().y - n2.getGObject().getLocation().y;

		      //###
		      //vx=vx*Math.abs(vx);
		      //vy=vy*Math.abs(vy);

		      double len = vx * vx + vy * vy;

		      if (len == 0)
			{
			  dx += Math.random();
			  dy += Math.random();
			}
		      else if (len < 300*300)
			{
			  dx += vx / len;
			  dy += vy / len;
			}
		    }

		  double dlen = dx * dx + dy * dy;
		  if (dlen > 0)
		    {
		      dlen = Math.sqrt(dlen) / 2;
		      n1.addDx(dx / dlen);
		      n1.addDy(dy / dlen);
		    }
		}

		 Dimension d = getEditor().getSize();
		 int width = d.width - 80;
		 int height = d.height - 60;

	      for (int i = 0 ; i < nnodes ; i++)
		{
		  MobileNode n = (MobileNode) nodes.elementAt(i);
		  if (!n.isFixed())
		    {
			n.getGObject().setLocation(n.getGObject().getLocation().x + (int) Math.max(-5, Math.min(5, n.getDx())),
				      n.getGObject().getLocation().y + (int) Math.max(-5, Math.min(5, n.getDy())));
		      //System.out.println("v= " + n.dx + "," + n.dy);
		      // on verifie que ça ne touche pas les bords.
		      // en fait on devrait prendre la taille des SNodes pour vérifier qu'ils
		      // ne vont pas trop loin...
		      normalize(n);
		    }
		  n.dx /= 2;
		  n.dy /= 2;
		}
	      getEditor().repaint();
	}



    public void scramble() {
	   	Dimension d = getEditor().getSize();
		int width = (d.width/2) - 80;
		int height = (d.height/2) - 60;

	    for (int i = 0 ; i < getNodes().size() ; i++) {
			MobileNode n = (MobileNode) getNodes().elementAt(i);
			if (!n.isFixed()) {
			    n.getGObject().setLocation(10 + (int)((width)*Math.random()),
					  10 + (int)((width)*Math.random()));

			    normalize(n);
			}
	    }
	    getEditor().repaint();
	}

	public void shake() {
		Dimension d = getEditor().getSize();
		int width = (d.width/2) - 80;
		int height = (d.height/2) - 60;

	    for (int i = 0 ; i < getNodes().size() ; i++) {
			MobileNode n = (MobileNode) getNodes().elementAt(i);
			if (!n.isFixed()) {
			    n.getGObject().setLocation(n.getGObject().getLocation().x + (int)(80*Math.random() - 40),
					  n.getGObject().getLocation().y + (int)(80*Math.random() - 40));

			    normalize(n);
			}
	    }
	    getEditor().repaint();
	}

    public void normalize(SElement s)
    {
	Point p = s.getGObject().getLocation();

	if (p.x < 80)
	    p.x=80;
	else if (p.x > getEditor().getWidth())
	    p.x=getEditor().getWidth();
	if (p.y < 60)
	    p.y=60;
	else if (p.y > getEditor().getHeight())
	    p.y=getEditor().getHeight();

	s.getGObject().setLocation(p);
    }

}
