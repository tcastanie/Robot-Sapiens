/*
* GJavaComponent.java - SEdit, a tool to design and animate graphs in MadKit
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
import java.awt.Graphics;

import javax.swing.JButton;
import javax.swing.JComponent;

/**************************************************************************

				CLASS GJavaComponent

**************************************************************************/


public class GJavaComponent extends GNode {
    JComponent comp = null;

    public static int DEFAULT_WIDTH = 120;
    public static int DEFAULT_HEIGHT= 80;

  	boolean deleted=false; // a flag to avoid circularity

	public GJavaComponent() {
		comp = new JButton("Swing!!");
        Dimension d=comp.getPreferredSize();
        if (d != null)
            setDimension(d.width,d.height);
    }

	public JComponent getComponent(){
		return comp;
	}

    public void delete(){
       // System.out.println("Actually deleting " + this);
        if (deleted) return;
        deleted = true;
        if (comp != null)
            getEditor().remove(comp);
        super.delete();
    }

    public void translate(int dx, int dy){
        super.translate(dx,dy);
        if (comp != null)
            comp.reshape(x,y,getWidth(),getHeight());
    }

	 protected String javaComponentClass=null;

    /** set the JavaComponentClass name which
	 *  has to be instantiated
	  */
    public void setJavaComponentClass(String s){javaComponentClass = s;}

    /** Get the JavaComponentClass name which
	 *  has to be instantiated
	 *  */
    public String getJavaComponentClass(){return javaComponentClass;}

	/**
	 * Creates the JavaComponent from the JavaComponentClass
     */
public void init(){

	  if (javaComponentClass != null){
		  try {
			  Class c;
              c = madkit.kernel.Utils.loadClass(javaComponentClass);
			  //c = Class.forName(javaComponentClass);
			  comp = (JComponent) c.newInstance();
		  }
		  catch(ClassNotFoundException ev){
			  System.err.println("Class not found :" + javaComponentClass + " "+ev);
			  ev.printStackTrace();
		  }
		  catch(InstantiationException e){
			  System.err.println("Can't instanciate ! " + javaComponentClass +" "+e);
			  e.printStackTrace();
		  }
		  catch(IllegalAccessException e){
			  System.err.println("illegal access! " + javaComponentClass+" "+e);
			  e.printStackTrace();
		  }
	  }
      if (comp != null){
        Dimension d=getDimension();
        Dimension pref=comp.getPreferredSize();
        if ((getWidth()==0) && (getHeight()==0)){
            if (pref != null)
                comp.reshape(x,y,pref.width,pref.height);
            else {
                comp.reshape(x,y,DEFAULT_WIDTH, DEFAULT_HEIGHT);
                setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
            }
        } else
            comp.reshape(x,y,getWidth(),getHeight());
          //System.out.println("dimension1: "+ getWidth()+", " + getHeight());
        getEditor().add(comp);
    }
   // System.out.println("InitGJavaComponent OK");
}

    public void setCenter(int _x, int _y) {
	    super.setCenter(_x,_y);
        if (comp != null){
            comp.setLocation(x,y);
            comp.revalidate();
        }
    }


	public void doDrag(int dx, int dy){
		super.doDrag(dx,dy);
        //comp.setLocation(x,y);
		comp.setSize(getWidth(),getHeight());
        comp.revalidate();
	}

    public void paint(Graphics g){
	    Color c=null;
        if (comp != null){
            comp.setLocation(x,y);
            comp.setSize(getWidth(),getHeight());
        }
      if (selected)
	    g.drawRect(x-1,y+1,width+2,height+2);
    }


}













