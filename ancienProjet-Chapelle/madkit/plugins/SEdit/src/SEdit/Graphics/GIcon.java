/*
* GIcon.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit.Graphics;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.ImageObserver;
import java.util.Hashtable;


/**************************************************************

		Graphic element which draws an iconic representation
		of a node

***************************************************************/

public class GIcon extends GNode implements ImageObserver
{
    Image icon;
    String imageAddress;

    public GIcon()  { }

    public boolean imageUpdate(Image img, int infoflags,
			       int x, int y, int width,int height) {
	  if ((infoflags & (FRAMEBITS|ALLBITS)) != 0) {
		  setDimension(width,height);
		  getEditor().repaint();
		  return true;
	  } else
		  return true;
    }

    public String getImageAddress() {
		return imageAddress;
    }

    public void setImageAddress(String s) {
	  try{
		MediaTracker m = new MediaTracker(editor);
		imageAddress=s;
		// System.err.println(getSElement());

		icon = Toolkit.getDefaultToolkit().getImage(getSElement().getDescriptor().getFormalism().getBase()+imageAddress);
		m.addImage(icon, 0);

		int h = icon.getHeight(this);
		int w = icon.getWidth(this);

		m.waitForID(0);

		Hashtable p = this.getSElement().getDescriptor().getGraphicProperties();
		if (p != null){
		  if (p.containsKey("width"))
			w = Integer.parseInt((String) p.get("width"));
		  if (p.containsKey("height"))
			h = Integer.parseInt((String) p.get("height"));
		}
		if ((h!=-1) && (w!=-1))
			setDimension(w,h);
	  }
	  catch(Exception e){
	    e.printStackTrace();}
    }

    public void paint(Graphics g)
    {
	// System.err.println(icon.toString());
	//	g.drawRect(x,y,width,height);
	//	System.err.println("In"+getDimension());
	g.drawImage(icon,x,y,width,height,this);
	//	System.err.println("Io"+getDimension());
	if (selected)
	    g.drawRect(x,y,width,height);
    }
}




















