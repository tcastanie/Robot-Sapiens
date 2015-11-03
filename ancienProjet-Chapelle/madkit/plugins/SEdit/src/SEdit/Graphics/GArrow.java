/*
* GArrow.java - SEdit, a tool to design and animate graphs in MadKit
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
import java.awt.Shape;

import SEdit.SArrow;

/*****************************************************************

            CLASSE GArrow

/*****************************************************************/
public class GArrow extends GObject
{
    public final static int DIRECT_LINE = 1;
    public final static int BROKEN_LINE = 2;
    Shape startingShape = null;
    Shape endingShape = null;
    Shape midShape = null;

    public int   ra=2, ras=5; //Rayon visible/sensible du point d'articulation
    protected Point q,p;    //Queue et pointe du lien

    int lineStyle = BROKEN_LINE;
    /** la forme du début de la flêche. Par defaut, aucune forme particuliere. */
    int startingForm = NOTHING;
    /** la forme de la pointe de la fleche. Par defaut, une pointe simple */
    int endingForm = SHARPEND;;

    protected static final int defaultRp=10,defaultRb=5;  //Rayon de pointe, de base
    protected static final int bigRp=14,bigRb=7;  //Rayon de pointe plus important
    protected static final int rSquare=5;
    protected static final int rRound=5;
    protected static final int rDiamond = 10;

    /**
       * Get the value of lineStyle.
       * @return Value of lineStyle.
       */
    public int getLineStyle() {return lineStyle;}

    /**
       * Set the value of lineStyle.
       * @param v  Value to assign to lineStyle.
       */
    public void setLineStyle(int  v) {this.lineStyle = v;}

    // Ol: Pourquoi y a-t'il double definition de ces constantes dans GArrow et ArrowDesc ?
	public static final int NOTHING = 0;
	public static final int SHARPEND = 1;
	public static final int SQUAREEND = 2;
	public static final int ROUNDEND = 3;
	public static final int DIAMONDEND = 4;
	public static final int WHITESHARPEND = 5;

	public int getEndingForm(){ return endingForm;}
	public int getStartingForm(){ return startingForm;}
	public void setEndingForm(int e){endingForm = e;}
	public void setStartingForm(int e){startingForm = e;}

	public GArrow(){
      	super();
      	setEndingForm(GArrow.SHARPEND);
    }

    public GArrow(int _x, int _y){
	  this();
	  setLocation(_x,_y);
    }

    public Point getCenter() { return new Point(x,y); }
    public void setCenter(int x, int y){
	    setLocation(x,y);
    }


    private Polygon  defineSharpEdge(Point q, Point p, int rp, int rb){
      //Calcule les 3 coins du triangle a partir de
      // -La queue de la fleche en q (articulation)
      // -La pointe de la fleche en p (extremite du cote du noeud)
	  Polygon arrow = new Polygon();
	  arrow.addPoint(p.x,p.y);
      //Pointe

      //Calcule des deux bases
      //Angle de la droite
      double theta=Math.atan( (float)(q.y-p.y) / (float)(p.x-q.x) );
      //Demi angle au sommet du triangle
      double alpha=Math.atan( (float)rb / (float)rp );
      //Rayon de la fleche
      double r=Math.sqrt( rp*rp + rb*rb );
      //Sens de la fleche
      if( (p.x-(x+height/2)) < 0 )
		  r=-r;

	  arrow.addPoint((int) Math.round( p.x-r*Math.cos(theta + alpha) ),
		  (int) Math.round( p.y+r*Math.sin(theta + alpha) ));

      //Base2
	  arrow.addPoint((int) Math.round( p.x-r*Math.cos(theta - alpha) ),
		  (int) Math.round( p.y+r*Math.sin(theta - alpha) ));

	  arrow.addPoint(p.x,p.y);
	  return arrow;
    }

    private Polygon  defineDiamondEdge(Point q, Point p){
		//Calcule les 4 coins du losange
    	// c'est pas tres bon car il faudrait tenir compte de l'angle
    	// du losange...

        //Pointe
	    Polygon diamond = new Polygon();
	    diamond.addPoint(p.x, p.y);
	    //Calcule des deux bases
        //Angle de la droite
        double theta=Math.atan( (float)(q.y-p.y) / (float)(p.x-q.x) );
        //Demi angle au sommet du triangle
        double alpha=Math.atan((float) 1);
        //Rayon de la fleche
        double r=Math.sqrt( rDiamond*rDiamond + rDiamond*rDiamond );
        double rd = 2*rDiamond;
        //Sens de la fleche
        if( (p.x-x) < 0 )  {
        	r=-r;
        	rd = - rd;
        }
        //Base1
        diamond.addPoint((int) Math.round( p.x-r*Math.cos(theta + alpha) ),
		    (int) Math.round( p.y+r*Math.sin(theta + alpha)));

        diamond.addPoint((int) Math.round(p.x-rd*Math.cos(theta)),
		   (int) Math.round(p.y+rd*Math.sin(theta)));

        //Base2
        diamond.addPoint((int) Math.round( p.x-r*Math.cos(theta - alpha)),
		    (int) Math.round( p.y+r*Math.sin(theta - alpha)));
		return diamond;
    }


    // le dessin de la pointe de la fleche
	protected void drawSharpEnd(Graphics g, Point from, Point p){
		endingShape = defineSharpEdge(new Point(from.x+width/2,from.y+height/2),p,defaultRp, defaultRb);
		g.fillPolygon((Polygon)endingShape);
	}

	protected void drawWhiteSharpEnd(Graphics g, Point from, Point p){
		Color c = g.getColor();
		endingShape = defineSharpEdge(new Point(from.x+width/2,from.y+height/2),p,bigRp, bigRb);
		g.setColor(Color.white);
		g.fillPolygon((Polygon)endingShape);
		g.setColor(c);
		g.drawPolygon((Polygon)endingShape);
	}

	protected void drawSquareEnd(Graphics g, Point p){
		endingShape = new Rectangle(p.x-rSquare,p.y-rSquare,2*rSquare,2*rSquare);
		g.fillRect(p.x-rSquare,p.y-rSquare,2*rSquare,2*rSquare);
	}

	protected void drawRoundEnd(Graphics g, Point p){
		g.fillOval(p.x-rRound,p.y-rRound,2*rRound,2*rRound);
	}

    protected void drawDiamondEnd(Graphics g, Point from, Point p) {
		endingShape = defineDiamondEdge(new Point(from.x+width/2,from.y+height/2),p);
		g.setColor(Color.white);
		g.fillPolygon((Polygon)endingShape);
		g.setColor(Color.black);
		g.drawPolygon((Polygon)endingShape);
    }

   protected void fixBounds(Point _q, Point _p){
	    q=_q;
        p=_p;
    }

	protected void prepareColor(Graphics g)  {
		  if(selected)
			  g.setColor(Color.red );
	}

	protected void drawEnding(Graphics g, int formType, Point from, Point p){
		switch(formType) {
			case NOTHING: break;	// NOTHING
			case SHARPEND: drawSharpEnd(g,from,p); break; // SHARPEDGE
			case SQUAREEND: drawSquareEnd(g,p);break; // SQUAREEND
			case ROUNDEND: drawRoundEnd(g,p); break; // ROUNDEND
			case DIAMONDEND: drawDiamondEnd(g,from,p); break; //DIAMONDEND
			case WHITESHARPEND: drawWhiteSharpEnd(g,from,p); break; // WHITESQUAREEND
		}
	}

	public void paint(Graphics g) {
		prepareColor(g);

		GObject origin = ((SArrow)getSElement()).getOrigin().getGObject();
		GObject target = ((SArrow)getSElement()).getTarget().getGObject();

		Point p_orig=origin.getLocation(), q_target=target.getLocation();
		Dimension d_orig = origin.getDimension(), d_target = target.getDimension();

		if (lineStyle == DIRECT_LINE) {
			x = (p_orig.x+d_orig.width/2+q_target.x+d_target.width/2)/2;
			y = (p_orig.y+d_orig.height/2+q_target.y+d_target.height/2)/2;
		}

		//Calcule des points d'intersection du lien avec les noeuds ...
		q=origin.intersection(getLocation());
		p=target.intersection(getLocation());

		if (lineStyle == DIRECT_LINE) {
			g.drawLine(q.x,q.y,p.x,p.y);
		} else {
			g.drawLine(q.x,q.y,x,y);
			//	    midShape = new Ellpse(x-ra,y-ra,2*ra,2*ra);
			g.fillOval(x-ra,y-ra,2*ra,2*ra);
			g.drawLine(x,y,p.x,p.y);
		}
		// Modifs Jak Version 0.6
		if ((startingForm == -1) || (endingForm == -1)) {
			startingForm = startingForm;
			endingForm = endingForm;
		}
		drawEnding(g,startingForm,getLocation(),q); //starting form
		drawEnding(g,endingForm,getLocation(),p);   //ending form

    }


    public void displayName(Graphics g){
		String name;
		name = getSElement().getLabel();
		if (name==null)
			name = getSElement().getID();

		if (displayLabel) {
		switch(labelLocation) {
			case CENTER:	{
				FontMetrics f = g.getFontMetrics(g.getFont());
				int w = f.stringWidth(name);
				g.drawString(name,x-w/2,y-2);
			    break;
			}
			case TOP_RIGHT: {
				g.drawString(name,x+4,y-4);
				break;
			}
			case BOTTOM: {
				FontMetrics f = g.getFontMetrics(g.getFont());
				int w = f.stringWidth(name);
				g.drawString(name,x-w/2,y+10);
				break;
			}
			case TOP: {
				FontMetrics f = g.getFontMetrics(g.getFont());
				int w = f.stringWidth(name);
				g.drawString(name,x-w/2,y-4);
				break;
			}
		  }
		}
    }


    public void snapToGrid(int s){}


    public boolean contains(Point p)
    {   int DCarre,RCarre;

        DCarre=(x-p.x)*(x-p.x) + (y-p.y)*(y-p.y);
        RCarre=ras*ras;

        return( DCarre <= RCarre );
    }

    public boolean isContainedIn(Rectangle r)
    {   return r.contains(x+width/2,y+height/2);  }

    public boolean isContainedIn(Polygon p)
    {   return p.contains(x+width/2,y+height/2);  }


    public Point intersection(Point p)
    {   return new Point(x+width/2,y+height/2);
    }

    public Dimension getDimension(){
		return getBounds().getSize();
    }

    public Rectangle getBounds(){
		Rectangle r = new Rectangle(p);
		r.add(q);
		if (lineStyle != DIRECT_LINE){
			Rectangle rLoc=new Rectangle(x-ra,y-ra,2*ra,2*ra);
			// r.add(getLocation());
			r.add(rLoc);
		}
		if (endingShape!=null)
			r.add(endingShape.getBounds());
		if (midShape!=null)
			r.add(midShape.getBounds());
		if (startingShape!=null)
			r.add(startingShape.getBounds());
		return r;
    }
}







