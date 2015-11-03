/*
* SComponent.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import SEdit.Graphics.GObject;


/**************************************************************************

			CLASSE SComponent

     Decrit un noeud qui comporte un ensemble
     de connecteurs en entrée et en sortie
     (reprend la structure de SModule d'une manière générale...)
     Les différences avec SModule sont indiquées en commentaire..
     La grande différence vient des connecteurs: SInConnect et SOutConnect
     qui fonctionne différemment des "pins".

**************************************************************************/
public class SComponent extends SNode
{
    boolean beingDeleted=false;

    private Hashtable inPins=new Hashtable(); // contient les bornes d'entrees
    private Hashtable outPins=new Hashtable(); // contient les bornes de sorties

    public  Vector getInArrows() {return null;}
    public  Vector getOutArrows() {return null;}

    protected void addOutArrow(SArrow a)
    {
    }

    protected void addInArrow(SArrow a)
    {
    }

    public void addConnector(String s)
    {
	try
	    {
		SConnect c = ((ComponentDesc)getDescriptor()).makeConnector(s);
		if (c instanceof SInConnect)
		    addInConnect(s,(SInConnect)c);
		if (c instanceof SOutConnect)
		    addOutConnect(s,(SOutConnect)c);

		c.setComponent(this);

		getStructure().addNode(c); // mmmm
		//		c.setLabel(s); // mmmh
		c.initGraphics(null);//roperties); // mmmh
		getStructure().getAgent().gui.getEditor().installNode(c.getGObject(),false); // mmmh
		//Utils.debug(nd+" "+id+" "+label+" "+p+" "+properties+" "+graphicProperties);
		computeConnectorsPositions();
	    }
	catch (Exception e)
	    {
		System.err.println("AddConnector Error"+e);
		e.printStackTrace();
	    }
    }



    protected void addInConnect(String n, SInConnect a)
    {
	if (inPins.containsKey(n))
	    {
		int i=1;
		while (inPins.containsKey(n+" "+i))
		    {i++; }
		inPins.put(n+" "+i, a);
		a.setLabel(n+i);
	    }
	else
	    {
		inPins.put(n, a);
		a.setLabel(n);
	    }
    }


    protected void addOutConnect(String n, SOutConnect a)
    {
	if (outPins.containsKey(n))
	    {
		int i=1;
		while (outPins.containsKey(n+" "+i))
		    {i++; }
		outPins.put(n+" "+i, a);
		a.setLabel(n+i);
	    }
	else
	    {
		outPins.put(n, a);
		a.setLabel(n);
	    }
    }

    protected void removeInConnect(SInConnect a) {
    	inPins.remove(a);
    }

    protected void removeOutConnect(SOutConnect a) {
    	outPins.remove(a);
    }
    protected void removeConnector(SConnect a) {
	if (a instanceof SInConnect)
	    removeInConnect((SInConnect)a);
	if (a instanceof SOutConnect)
	    removeOutConnect((SOutConnect)a);

    }

    public SInConnect getInConnect(int index)
    {
	int i=0;
	for (Enumeration e = inPins.elements() ; e.hasMoreElements() ;)
	    {
		SInConnect s = (SInConnect)e.nextElement();
		if (i==index)
		    return s;
		i++;
	    }
	return null;
    }

    public SOutConnect getOutConnect(int index)
    {
	int i=0;
	for (Enumeration e = outPins.elements() ; e.hasMoreElements() ;)
	    {
		SOutConnect s = (SOutConnect)e.nextElement();
		if (i==index)
		    return s;
		i++;
	    }
	return null;
    }

    public SInConnect getInConnect(String s) {
    	SInConnect o;
	return (SInConnect) inPins.get(s);
    }

    public SOutConnect getOutConnect(String s) {
    	SOutConnect o;
	return (SOutConnect) outPins.get(s);
    }

    public Enumeration getInNames()
    {
	return inPins.keys();
    }
   public Enumeration getOutNames()
    {
	return outPins.keys();
    }
   public Enumeration getInConnectors(){
       return inPins.elements();
   }
    public Enumeration getOutConnectors(){
	return outPins.elements();
    }

    public void deleteConnector(String remove)
    {
	if (inPins.containsKey(remove))
	    inPins.remove(remove);

	if (outPins.containsKey(remove))
	    outPins.remove(remove);
    }

    public void delete()
    {
	super.delete();
	beingDeleted=true;
	for (Enumeration e = inPins.elements() ; e.hasMoreElements() ;)
	    ((SInConnect)e.nextElement()).delete();
	for (Enumeration e = outPins.elements() ; e.hasMoreElements() ;)
	    ((SOutConnect)e.nextElement()).delete();
    }

    public void computeConnectorsPositions()
    {
	  GObject comp = getGObject();
	  int width = comp.getDimension().width;
	  int height = comp.getDimension().height;
	  int x = comp.getLocation().x;
	  int y = comp.getLocation().y;
	  int step = 4;
	  int i=0;

	  switch (((ComponentDesc)getDescriptor()).getLayout())
	    {
	    case ComponentDesc.MODULE_PLACEMENT_AUTO:
		i = y+step;

		for (Enumeration e = inPins.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			s.getGObject().setLocation(x-s.getGObject().getDimension().width/2,
						   i);
			i=i+s.getGObject().getDimension().height+step;
			s.getConnectorPlacement().setSide(ConnectorPlacement.LEFT);
		    }
		i = y+step;
		for (Enumeration e = outPins.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			s.getGObject().setLocation(x+width-s.getGObject().getDimension().width/2,
						   i);
			i=i+s.getGObject().getDimension().height+step;
			s.getConnectorPlacement().setSide(ConnectorPlacement.RIGHT);
		    }
		break;
	    case ComponentDesc.MODULE_PLACEMENT_JUSTIFIED:
		Vector left   = new Vector();
		Vector right  = new Vector();
		Vector top    = new Vector();
		Vector bottom = new Vector();
		int leftlength   = 0;
		int rightlength  = 0;
		int toplength    = 0;
		int bottomlength = 0;

		for (Enumeration e = inPins.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			switch (s.getConnectorPlacement().getSide())
			  {
			  case ConnectorPlacement.LEFT:
			      left.addElement(s);
			      leftlength+=s.getGObject().getDimension().height;
			  break;
			  case ConnectorPlacement.RIGHT:
			      right.addElement(s);
			      rightlength+=s.getGObject().getDimension().height;
			  break;
			  case ConnectorPlacement.TOP:
			      top.addElement(s);
			      toplength+=s.getGObject().getDimension().width;
			  break;
			  case ConnectorPlacement.BOTTOM:
			      bottom.addElement(s);
			      bottomlength+=s.getGObject().getDimension().width;
			  break;
			  }
		    }
		for (Enumeration e = outPins.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			switch (s.getConnectorPlacement().getSide())
			  {
			  case ConnectorPlacement.LEFT:
			      left.addElement(s);
			      leftlength+=s.getGObject().getDimension().height;
			  break;
			  case ConnectorPlacement.RIGHT:
			      right.addElement(s);
			      rightlength+=s.getGObject().getDimension().height;
			  break;
			  case ConnectorPlacement.TOP:
			      top.addElement(s);
			      toplength+=s.getGObject().getDimension().width;
			  break;
			  case ConnectorPlacement.BOTTOM:
			      bottom.addElement(s);
			      bottomlength+=s.getGObject().getDimension().width;
			  break;
			  }
		    }
		step = (height-leftlength) / (left.size()+1);
		i=y+step;
		for (Enumeration e = left.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			s.getGObject().setLocation(x-s.getGObject().getDimension().width/2,
						   i);
			i=i+s.getGObject().getDimension().height+step;
		    }
		step = (height-rightlength) / (right.size()+1);
		i=y+step;
		for (Enumeration e = right.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			s.getGObject().setLocation(x+width-s.getGObject().getDimension().width/2,
						   i);
			i=i+s.getGObject().getDimension().height+step;
		    }
		step = (width-toplength) / (top.size()+1);
		i=x+step;
		for (Enumeration e = top.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			s.getGObject().setLocation(i,
						   y-s.getGObject().getDimension().height/2);
			i=i+s.getGObject().getDimension().width+step;
		    }
		step = (width-bottomlength) / (bottom.size()+1);
		i=x+step;
		for (Enumeration e = bottom.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			s.getGObject().setLocation(i,
						   y+height-s.getGObject().getDimension().height/2);
			i=i+s.getGObject().getDimension().width+step;
		    }
		break;

	    case ComponentDesc.MODULE_PLACEMENT_MANUAL:

		for (Enumeration e = inPins.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			ConnectorPlacement p = s.getConnectorPlacement();
			double objx = 0; double objy = 0;;
			int pos;
			int halfwidth  = (int)Math.round(s.getGObject().getDimension().width/2);
			int halfheight = (int)Math.round(s.getGObject().getDimension().height/2);

			switch (p.getSide())
			    {
			    case ConnectorPlacement.LEFT:
				pos = (int)Math.round(height*p.getRatio());
				objx= x-halfwidth+comp.intersection(GObject.LEFT,pos);
				objy= y+pos-halfheight;
				break;
			    case ConnectorPlacement.RIGHT:
				pos = (int)Math.round(height*p.getRatio());
				objx= x-halfwidth+comp.intersection(GObject.RIGHT,pos);
				objy= y+pos-halfheight;
				break;
			    case ConnectorPlacement.TOP:
				pos = (int)Math.round(width*p.getRatio());
				objx= x+pos-halfwidth;
				objy= y-halfheight+comp.intersection(GObject.TOP,pos);
				break;
			    case ConnectorPlacement.BOTTOM:
				pos = (int)Math.round(width*p.getRatio());
				objx= x+pos-halfwidth;
				objy= y-halfheight+comp.intersection(GObject.BOTTOM,pos);
				break;
			    }
			s.getGObject().setLocation((int)Math.round(objx), (int)Math.round(objy));
		    }

		for (Enumeration e = outPins.elements() ; e.hasMoreElements() ;)
		    {
			SConnect s = (SConnect)e.nextElement();
			ConnectorPlacement p = s.getConnectorPlacement();
			double objx = 0; double objy = 0;;
			int pos;
			int halfwidth  = (int)Math.round(s.getGObject().getDimension().width/2);
			int halfheight = (int)Math.round(s.getGObject().getDimension().height/2);

			switch (p.getSide())
			    {
			    case ConnectorPlacement.LEFT:
				pos = (int)Math.round(height*p.getRatio());
				objx= x-halfwidth+comp.intersection(GObject.LEFT,pos);
				objy= y+pos-halfheight;
				break;
			    case ConnectorPlacement.RIGHT:
				pos = (int)Math.round(height*p.getRatio());
				objx= x-halfwidth+comp.intersection(GObject.RIGHT,pos);
				objy= y+pos-halfheight;
				break;
			    case ConnectorPlacement.TOP:
				pos = (int)Math.round(width*p.getRatio());
				objx= x+pos-halfwidth;
				objy= y-halfheight+comp.intersection(GObject.TOP,pos);
				break;
			    case ConnectorPlacement.BOTTOM:
				pos = (int)Math.round(width*p.getRatio());
				objx= x+pos-halfwidth;
				objy= y-halfheight+comp.intersection(GObject.BOTTOM,pos);
				break;
			    }
			s.getGObject().setLocation((int)Math.round(objx), (int)Math.round(objy));
		    }

	    }
    }


    protected boolean isConnectable(SArrow s, boolean isTarget)
    {
    	return(false);
    }

    public String toString()
    {
	String str = "SComponent "+super.toString();
	str=str+" in[";
	for (Enumeration e = inPins.elements() ; e.hasMoreElements() ;)
	    {
		SInConnect s = (SInConnect)e.nextElement();
		str=str+"("+s.getGObject()+")";
	    }
	str=str+"] out[";
	for (Enumeration e = outPins.elements() ; e.hasMoreElements() ;)
	    {
		SOutConnect s = (SOutConnect)e.nextElement();
		str=str+"("+s.getGObject()+")";
	    }
	return str+"]";
    }


}

