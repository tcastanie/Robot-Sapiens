/*
* GInternalFrame.java - SEdit, a tool to design and animate graphs in MadKit
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

import javax.swing.JButton;
import javax.swing.JPanel;

import SEdit.StructureEditor;

class TestJInternalFrame extends SEditJInternalFrame
{
    JButton b1,b2,b3;


    TestJInternalFrame(String s, GInternalFrame f){
        super(s,f);
        b1 = new JButton("Button1");
        b2 = new JButton("Button2");
        b3 = new JButton("A big button");
        JPanel p1= new JPanel();
        p1.add(b1);
        p1.add(b2);
        p1.add(b3);
        getContentPane().add(p1);
       // this.addMouseListener(this);
       // this.addMouseMotionListener(this);
        cont = f;
    }

}

public class GInternalFrame extends GJavaComponent {

    public void paint(Graphics g) {}

    public GInternalFrame (){
        comp = new SEditJInternalFrame("Internal Frame",this);
    }

    public GInternalFrame (String title){
        comp = new SEditJInternalFrame(title,this);
    }

    void repaint(){
        StructureEditor ed=getEditor();
        if (ed != null)
            ed.repaint();
    }

    public void select(boolean state){
        if (selected == state)
            return;
        else {
            selected = state;
	        if (comp != null)
              try{
                ((SEditJInternalFrame) comp).setSelected(state);
             } catch(java.beans.PropertyVetoException e){
                System.err.println("SEditJInternalFrame activation/deactivation impossible");
             }
        }
    }

    void switchSelection(boolean b){
        StructureEditor ed=getEditor();
        if (b)
            if (isSelected())
                return;
            else {
                //System.out.println("select");
                ed.clearSelection();
                ed.addSelected(this);
            }
        else {
            if (!isSelected())
                return;
            else {
                //System.out.println("unselect");
                ed.removeSelected(this);
            }
        }
    }

    void killMe(){
        if (deleted) return;
        deleted = true;
        if (comp != null)
            getEditor().remove(comp);
	    getEditor().removeGObject(this); //	getSElement().delete();
        getSElement().delete();
        getEditor().repaint();
    }

    public void delete(){
        if (deleted) return;
        deleted = true;
        if (comp != null)
            getEditor().remove(comp);
	    editor.removeGObject(this);
        try {
           if (comp != null)
                ((SEditJInternalFrame) comp).setClosed(true);
        } catch (java.beans.PropertyVetoException e){
            System.err.println("JInternalFrame cannot be closed");
        }
    }

    public void init(){
        super.init();
	    //System.out.println("Init GInternalFrame OK1");
        ((SEditJInternalFrame)comp).show();
	    //System.out.println("Init GInternalFrame OK3");
    }

    public void doDrag(int dx, int dy){
		super.doDrag(dx,dy);
        //System.out.println("dragging a GInternalFrame");
        //comp.setLocation(x,y);
		comp.setSize(getWidth(),getHeight());
        comp.revalidate();
	}
}
