/*
* SEditJInternalFrame.java - SEdit, a tool to design and animate graphs in MadKit
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

import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

public class SEditJInternalFrame extends JInternalFrame
                                implements InternalFrameListener
{

    GInternalFrame cont;

    public SEditJInternalFrame(String s, GInternalFrame f) {
        super(s,true,true,true,true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        cont = f;
        addInternalFrameListener(this);
    }

    public void setBounds(int x, int y, int w, int h){
        super.setBounds(x,y,w,h);
        if (cont != null){
            cont.setLocation(x,y);
            cont.setSize(w,h);
            if(isShowing()){
                cont.repaint();
            }
        }
    }

    public void updateGContainer(){
        cont.setLocation(getX(),getY());
        cont.setSize(getWidth(),getHeight());
        if (isShowing()){
            cont.repaint();
        }
    }

  public void internalFrameOpened(InternalFrameEvent e){}

  public void internalFrameClosing(InternalFrameEvent e) {
       // System.out.println("closing...");
        cont.killMe();
  }

  public void internalFrameClosed(InternalFrameEvent e){
       // System.out.println("Argghh... I'm dead");
  }

  public void internalFrameActivated(InternalFrameEvent e){
       // System.out.println("activated");
        if (cont != null)
            ((GInternalFrame) cont).switchSelection(true);
  }

  public void internalFrameDeactivated(InternalFrameEvent e){
      //  System.out.println("deactivated");
        if (cont != null)
            ((GInternalFrame) cont).switchSelection(false);
  }

  public void internalFrameIconified(InternalFrameEvent e){}

  public void internalFrameDeiconified(InternalFrameEvent e){}


}
