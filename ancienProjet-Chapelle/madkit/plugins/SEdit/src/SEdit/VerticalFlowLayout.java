/*
* VerticalFlowLayout.java - SEdit, a tool to design and animate graphs in MadKit
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


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;

// Ol: Class ok.

/**
 * VerticalFlow layout is used to layout buttons in a panel. It will arrange
 * buttons top to bottom until no more buttons fit on the same column.
 * Each column is centered. VerticalFlowLayout behaves as FlowLayout, except that buttons are 
 * arranged in columns instead of lines.
 *
 * @version 	1.23, 24/07/97
 * @author 	Jacques Ferber
 */
class VerticalFlowLayout implements java.awt.LayoutManager, java.io.Serializable {

    /**
     * The left alignment variable. 
     */
    public static final int TOP 	= 0;

    /**
     * The right alignment variable. 
     */
    public static final int CENTER 	= 1;

    /**
     * The right alignment variable.
     */

    int align;
    int hgap;
    int vgap;

    /*
     * JDK 1.1 serialVersionUID 
     */
     private static final long serialVersionUID = -7262534875583282631L;

    /**
     * Constructs a new Flow Layout with a centered alignment and a
     * default 5-unit horizontal and vertical gap.
     */
    public VerticalFlowLayout() {
	this(CENTER, 5, 5);
    }

    /**
     * Constructs a new Flow Layout with the specified alignment and a
     * default 5-unit horizontal and vertical gap.
     * @param align the alignment value
     */
    public VerticalFlowLayout(int align) {
	this(align, 5, 5);
    }

    /**
     * Constructs a new Flow Layout with the specified alignment and gap
     * values.
     * @param align the alignment value
     * @param hgap the horizontal gap variable
     * @param vgap the vertical gap variable
     */
    public VerticalFlowLayout(int align, int hgap, int vgap) {
	this.align = align;
	this.hgap = hgap;
	this.vgap = vgap;
    }

    /**
     * Returns the alignment value for this layout, one of TOP or
     * CENTER.
     */
    public int getAlignment() {
	return align;
    }
    
    /**
     * Sets the alignment value for this layout.
     * @param align the alignment value, one of TOp or CENTER.
     */
    public void setAlignment(int align) {
	this.align = align;
    }

    /**
     * Returns the horizontal gap between components.
     */
    public int getHgap() {
	return hgap;
    }
    
    /**
     * Sets the horizontal gap between components.
     * @param hgap the horizontal gap between components
     */
    public void setHgap(int hgap) {
	this.hgap = hgap;
    }
    
    /**
     * Returns the vertical gap between components.
     */
    public int getVgap() {
	return vgap;
    }
    
    /**
     * Sets the vertical gap between components.
     * @param vgap the vertical gap between components
     */
    public void setVgap(int vgap) {
	this.vgap = vgap;
    }

    /**
     * Adds the specified component to the layout. Not used by this class.
     * @param name the name of the component
     * @param comp the the component to be added
     */
    public void addLayoutComponent(String name, Component comp) {
    }

    /**
     * Removes the specified component from the layout. Not used by
     * this class.  
     * @param comp the component to remove
     */
    public void removeLayoutComponent(Component comp) {
    }

    /**
     * Returns the preferred dimensions for this layout given the components
     * in the specified target container.
     * @param target the component which needs to be laid out
     * @see Container
     * @see #minimumLayoutSize
     */
    public Dimension preferredLayoutSize(Container target) {
	    Dimension dim = new Dimension(0, 0);
	    int nmembers = target.getComponentCount();

	    for (int i = 0 ; i < nmembers ; i++) {
	      Component m = target.getComponent(i);
	      if (m.isVisible()) {
		      Dimension d = m.getPreferredSize();
		      dim.width = Math.max(dim.width, d.width);
		      if (i > 0) {
		        dim.height += vgap;
		      }
		      dim.height += d.height;
	      }
	    }
	    Insets insets = target.getInsets();
	    dim.width += insets.left + insets.right + hgap*2;
	    dim.height += insets.top + insets.bottom + vgap*2;
	    return dim;
    }

    /**
     * Returns the minimum dimensions needed to layout the components
     * contained in the specified target container.
     * @param target the component which needs to be laid out 
     * @see #preferredLayoutSize
     */
    public Dimension minimumLayoutSize(Container target) {
	    Dimension dim = new Dimension(0, 0);
	    int nmembers = target.getComponentCount();

	    for (int i = 0 ; i < nmembers ; i++) {
	      Component m = target.getComponent(i);
	      if (m.isVisible()) {
		    Dimension d = m.getMinimumSize();
		    dim.width = Math.max(dim.width, d.width);
		    if (i > 0) {
		      dim.height += vgap;
		    }
		    dim.height += d.height;
	    }
	}
	Insets insets = target.getInsets();
	dim.width += insets.left + insets.right + hgap*2;
	dim.height += insets.top + insets.bottom + vgap*2;
	return dim;
    }

    /** 
     * Centers the elements in the specified column, if there is any slack.
     * @param target the component which needs to be moved
     * @param x the x coordinate
     * @param y the y coordinate
     * @param width the width dimensions
     * @param height the height dimensions
     * @param rowStart the beginning of the row
     * @param rowEnd the the ending of the row
     */
    private void moveComponents(Container target, int x, int y, int width, int height, int rowStart, int rowEnd) {
	    switch (align) {
	      case TOP:
	        break;
	      case CENTER:
	        x += height / 2;
	        break;
	    }
	    for (int i = rowStart ; i < rowEnd ; i++) {
	      Component m = target.getComponent(i);
	      if (m.isVisible()) {
		      m.setLocation(x + (width - m.getSize().width) / 2, y);
		      y += vgap + m.getSize().height;
	    }
	}
    }

    /**
     * Lays out the container. This method will actually reshape the
     * components in the target in order to satisfy the constraints of
     * the BorderLayout object. 
     * @param target the specified component being laid out.
     * @see Container
     */
    public void layoutContainer(Container target) {
	    Insets insets = target.getInsets();
	    int maxheight = target.getSize().height - (insets.top + insets.bottom + vgap*2);
	    int nmembers = target.getComponentCount();
	    int x = insets.left + hgap, y = 0;
	    int colv = 0, start = 0;

	    for (int i = 0 ; i < nmembers ; i++) {
	        Component m = target.getComponent(i);
	        if (m.isVisible()) {
		        Dimension d = m.getPreferredSize();
		        m.setSize(d.width, d.height);
	
		        if ((y == 0) || ((y + d.height) <= maxheight)) {
		          if (y > 0) {
			          y += vgap;
		          }
		          y += d.height;
		          colv = Math.max(d.width, colv);
		        } else {
		          moveComponents(target, x, insets.top + vgap, colv, maxheight - y, start, i);
		          y = d.height;
		          x += hgap + colv;
		          colv = d.width;
		          start = i;
		        }
	        }
	    }
      moveComponents(target, x, insets.top + vgap, colv, maxheight - y, start, nmembers);
    }
    
    /**
     * Returns the String representation of this FlowLayout's values.
     */
    public String toString() {
	    String str = "";
	    switch (align) {
	        case TOP:    str = ",align=top"; break;
	        case CENTER:  str = ",align=center"; break;
	    }
	    return getClass().getName() + "[hgap=" + hgap + ",vgap=" + vgap + str + "]";
    }
}
