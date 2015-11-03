/*
* GObject.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

import SEdit.SElement;
import SEdit.StructureEditor;
/**************************************************************************

				CLASSE GObject

**************************************************************************/

/** classe abstraite correspondant à la représentation graphique des éléments
de la structure.

@see SElement
*/

public abstract class GObject //extends Component
{
    public static final int RIGHT  = 1;
    public static final int LEFT   = 2;
    public static final int TOP    = 3;
    public static final int BOTTOM = 4;
    public static final int CENTER = 5;
    public static final int TOP_RIGHT = 6;

    StructureEditor editor;
    Color foreground = Color.black;

    /**
       * Get the value of editor.
       * @return Value of editor.
       */
    public StructureEditor getEditor() {return editor;}
    public void setEditor(StructureEditor  v) {this.editor = v;}

    public boolean   selected=false;
    protected boolean   displayLabel=true;
    protected int labelLocation = CENTER; // valeur par defaut
    protected int       x,y;    //Coordonnees PHYSIQUES du centre
    protected int    width, height;

	protected Rectangle bounds;
	public Rectangle getRectangle(){
		if (bounds == null){
			bounds = new Rectangle(x,y,width,height);
			return(bounds);
		}
		bounds.setBounds(x,y,width,height);
		return(bounds);
	}


        /**
       * Get the value of labelLocation.
       * @return Value of labelLocation.
       */
    public int getLabelLocation() {return labelLocation;}

    /**
       * Set the value of labelLocation.
       * @param v  Value to assign to labelLocation.
       */
    public void setLabelLocation(int  v) {this.labelLocation = v;}

    /**

     * Get the value of displayLabel.
       * @return Value of displayLabel.
       */
    public boolean getDisplayLabel() {return displayLabel;}

    /**
       * Set the value of displayLabel.
       * @param v  Value to assign to displayLabel.
       */
    public void setDisplayLabel(boolean  v) {
	  //System.err.println("setDL"+this+"/"+getSElement()+"/"+v);
	  this.displayLabel = v;}


    public Point getCenter() { return new Point(x+width/2,y+height/2);};
    public void  setCenter(Point p) { setCenter(p.x, p.y); }
    public void setCenter(int x, int y) {
	  setLocation(x-width/2, y-height/2);
    }

    public Point getLocation(){ return new Point(x,y);};
    public void setLocation(Point p)
    {
	setLocation(p.x, p.y);
    }
    public void setLocation(int _x, int _y)
    {
	x=_x;
	y=_y;
    }
	/**
	 * Initialize the graphic component associated to a node
	 */
	public void init(){}

    public void drag(int xold, int xnew, int yold, int ynew) {
	translate(xnew-xold,ynew-yold);
    }

    public void alignHorizontalTo(GObject o) {
	    setCenter(getCenter().x, o.getCenter().y);
    }

    public void alignVerticalTo(GObject o) {
	    setCenter(o.getCenter().x, getCenter().y);
    }


    public void select(boolean state){
	  selected=state;
    }

    public boolean isSelected() {
   	return(selected);
    }
    public boolean isSelectable() {
   	return(true);
    }


    public void paint(Graphics g)
    {
	//	if (displayLabel)
	//  displayName(g);
    }


    public   boolean contains(Point p)
    {
     return (new Rectangle(x,y,width,height)).contains(p);
    }


    public Point intersection(Point p)
    {
	float k,k1,k2;
	k1= Math.abs( (float) (width/2)  / (float)(p.x-(x+width/2)) );
	k2= Math.abs( (float) (height/2) / (float)(p.y-(y+height/2)) );
	k=Math.min(k1,k2);

	Point i=new Point( Math.round((x+width/2)+k*(p.x-(x+width/2))) ,
			   Math.round((y+height/2)+k*(p.y-(y+height/2))) );
	return i;
    }

    public  int intersection(int side, int position)
    {
	switch (side)
	{
	case RIGHT:
	    return width;
	case BOTTOM:
	    return height;
	case LEFT:
	case TOP:
	default:
	    return 0;
	}

    }

    public void translate(int dx, int dy)
    {
	Point p = getLocation();
	p.translate(dx,dy);
	setLocation(p);
    }

    public void translateWrap(int dx, int dy, Rectangle r){
		Point p = getLocation();
		p.x += dx;
		if (p.x+width >= r.x+r.width) p.x = p.x+width - r.width;
		else if (p.x < r.x) p.x = p.x + r.width-width;
		p.y += dy;
		if (p.y+height >= r.y+r.height) p.y = p.y+height - r.height;
		else if (p.y < r.y) p.y = p.y + r.height-height;
		setLocation(p);
    }

    public void translateBound(int dx, int dy, Rectangle r){

		Point p = getLocation();
		p.x += dx;
		if (p.x >= r.x+r.width-width) p.x = r.x + r.width - width -1;
		if (p.x < r.x) p.x = r.x;
		p.y += dy;
		if (p.y >= r.y+r.height-height) p.y = r.y + r.height - height -1;
		if (p.y < r.y) p.y = r.y;
		setLocation(p);
    }

    public void snapToGrid(int s){
		x = s*(int)(Math.round((double)x/(double)s));
		y = s*(int)(Math.round((double)y/(double)s));
    }




    public GObject() {}

    // l'element "logique" auquel est lie le graphique
    protected SElement element;
    public SElement getSElement()  { return element;}
    public void setSElement(SElement o) { element = o; }

    /*    public void prepareDisplay(Graphics g)
	  {
	  //	Graphics g = editor.getGraphics();
	  if(selected)
	  g.setColor(Color.red );
	  else
	  g.setColor(Color.black );
	  }*/

     public boolean isContainedIn(Rectangle r)
    {   return r.contains(getLocation());  }

    public boolean isContainedIn(Polygon p)
    {   return p.contains(getLocation());  }

    public void displayName(Graphics g) {
	  String name;
	  name = getSElement().getLabel();
	  if (name==null)
		  name = getSElement().getID();
	  //	g = editor.getGraphics();
	  // System.err.println("GOBJ"+g+" "+g.getClipBounds()+"|"+getLocation()+" "+getDimension());
	  if (displayLabel) {
		  //	    Utils.debug("INDISPLAYNAME-------"+name);
		  //      	g.setColor(Color.black);
		  switch(labelLocation) {
			case CENTER:	{
			  FontMetrics f = g.getFontMetrics(g.getFont());
			  int w = f.stringWidth(name);
			  int h = f.getAscent();
			  g.drawString(name,x+width/2-w/2,y+height/2+h/2);
			  break;
			}
			case TOP_RIGHT: {
			  g.drawString(name,x+width,y);
			  break;
			}
			case BOTTOM: {
			  FontMetrics f = g.getFontMetrics(g.getFont());
			  int w = f.stringWidth(name);
			  int h = f.getAscent();
			  g.drawString(name,x+width/2-w/2,y+height+h+2);
			  break;
			}
			case TOP: {
			  FontMetrics f = g.getFontMetrics(g.getFont());
			  int w = f.stringWidth(name);
			  g.drawString(name,x+width/2-w/2,y-2);
			  break;
			}
		  }
		}
    }


    /**
       * Get the foreground color of this
       * @return Value of foreground.
       */
    public Color getForeground() {return foreground;}

    /**
       * Set the value of foreground.
       * @param v  Value to assign to foreground.
       */
    public void setForeground(Color  v) {this.foreground = v;}


    /**
     * Get the value of width.
     * @return Value of width.
     */
    public int getWidth() {return width;}
    /**
       * Set the value of width.
       * @param v  Value to assign to width.
       */
    public void setWidth(int  v) {this.width = v;}
    /**
       * Get the value of height.
       * @return Value of height.
       */
    public int getHeight() {return height;}

    /**
       * Set the value of height.
       * @param v  Value to assign to height.
       */
    public void setHeight(int  v) {this.height = v;}

    public void setSize(int w, int h){
        setDimension(w,h);
    }


    public void setDimension(int w, int h){
	    width = w;
	    height = h;
    }

    public Dimension getDimension(){
	    return new Dimension(width, height);
    }
    public Rectangle getBounds(){
	    return new Rectangle(getLocation(), getDimension());
    }

    public void delete(){
	    editor.removeGObject(this);//	getSElement().delete();
    }

	public void reInstall(StructureEditor ed){
	    this.setEditor(ed);
	    ed.addGObject(this);
    }

}







