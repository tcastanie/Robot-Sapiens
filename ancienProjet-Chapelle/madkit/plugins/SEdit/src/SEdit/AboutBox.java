/*
* AboutBox.java - SEdit, a tool to design and animate graphs in MadKit
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
package  SEdit;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;



class AboutBox extends JDialog
{
  String image = "images/smallsedit.jpg";

  public AboutBox()
  {
    getContentPane().setBackground(Color.white);
    getContentPane().setLayout(new BoxLayout(getContentPane(),
					     BoxLayout.Y_AXIS));

    ImageIcon seditPicture = new ImageIcon(image);
    JLabel ta = new JLabel(seditPicture);
    ta.setSize(seditPicture.getIconWidth(),seditPicture.getIconHeight());

    ta.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    ta.setAlignmentX(Box.CENTER_ALIGNMENT);

    getContentPane().add(ta);
    MultiLineDisplay mld = new MultiLineDisplay();
    mld.add("SEdit");
    mld.add("- a structure editor -");
    mld.add("");
    mld.add("by J.Ferber, Ol. Gutknecht");
    mld.add("(c) 1998-2001");

    mld.add("");
    mld.add("SEdit version: "+StructureAgent.VERSION);
    mld.add("running on:");
    mld.add("MadKit kernel: "+madkit.kernel.Kernel.VERSION);
    mld.setAlignmentX(Box.CENTER_ALIGNMENT);
    getContentPane().add(mld);
    getContentPane().add(Box.createVerticalStrut(10));
    JButton ok     = new JButton("OK");
    ok.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
	dispose();
      }});
    ok.setAlignmentX(Box.CENTER_ALIGNMENT);
    getContentPane().add(ok);
    getContentPane().add(Box.createVerticalStrut(10));
    getRootPane().setDefaultButton(ok);

    new WindowCloser(this);

    /*int x = frame.getLocation().x + 30;
      int y = frame.getLocation().y + 100;*/
    pack();
    show();
  }

    public void actionPerformed(ActionEvent evt) {
	// our button got pushed.
        dispose();
    }

}

class MultiLineDisplay extends JPanel
{
  MultiLineDisplay()
    {
      setLayout(new GridLayout(0,1));
      setBackground(Color.white);
    }
  void add(String s)
    {
      JLabel l = new JLabel(s);
      l.setHorizontalAlignment(JLabel.CENTER);
      setBackground(Color.white);
      add(l);
    }


} class WindowCloser implements WindowListener {

   /**
    * Create an adaptor to listen for window closing events
    * on the given window and actually perform the close.
    */

    public WindowCloser(Window w) {
	this(w, false);
    }

   /**
    * Create an adaptor to listen for window closing events
    * on the given window and actually perform the close.
    * If "exitOnClose" is true we do a System.exit on close.
    */

    public WindowCloser(Window w, boolean exitOnClose) {
	this.exitOnClose = exitOnClose;
	w.addWindowListener(this);
    }


    public void windowOpened(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
	if (exitOnClose) {
            System.exit(0);
	}
	e.getWindow().dispose();
    }

    public void windowClosed(WindowEvent e) {
	if (exitOnClose) {
            System.exit(0);
	}
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    private boolean exitOnClose;

}














