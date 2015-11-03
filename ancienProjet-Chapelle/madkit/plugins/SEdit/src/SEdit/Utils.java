/*
* Utils.java - SEdit, a tool to design and animate graphs in MadKit
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
import java.awt.Event;
import java.awt.Frame;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

public class Utils
{

    public static void log(String s)
    {
	//	System.err.println(s);
    }

    public static void debug(Object s)
    {
	//	System.err.println("DEBUG:"+s);
    }
    public static void debug(Object o, Object s)
    {
	//	System.err.println("Debug("+o+"):"+s);
    }

    public static CommandAction addCommand(Command c, JMenu m, JToolBar t, String name, String shortdesc, String icon, int key)
    {
	try
	    {
		ImageIcon i = null;
		if ((icon != null) && (!icon.equals("")))
		    {
			java.net.URL u = c.getClass().getResource(icon);
			i = new ImageIcon (u);
		    }
	CommandAction action = new CommandAction(c, name, i);

	if (t!=null)
	    {
		JButton b = t.add(action);
		b.setToolTipText(shortdesc);
		b.setMargin(new Insets(0,0,0,0));
		if ((i!=null) && (i.getImage() != null))
		    b.setText("");
	    }

	JMenuItem menuItem = m.add(action);
	setKeystroke(menuItem, key);

	if ((i!=null) && (i.getImage() != null))
	    menuItem.setIcon(null);
	return action;
	    }
	catch (Exception e)
	    {
		System.err.println("Utils.addCommand exception "+e);
		return null;

	    }
    }
    /*
     * Get the extension of a file.
     */
    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 &&  i < s.length() - 1) {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

   /** get the Frame that surrounds a component. May be used for buildings
       dialogs Ol: JF import */
   static public Frame getRealFrameParent(Component c){
		while (!((c instanceof Frame) || (c instanceof JFrame))){
			if (c == null)
				return(null);
			else
				c = c.getParent();
        }
		return((Frame) c);
	}

    static public Container getFrameParent(Container _c){
		Container c = _c;
		while (!((c instanceof JFrame) ||
				 (c instanceof Frame)
			  || (c instanceof JInternalFrame))){
			if (c == null)
				return(null);
			else
				c = c.getParent();
		}
		return((Container) c);
	}

  // utilise par StructureFrame
  public static void addMenuItem(ActionListener al, JMenu m, String label, String command, int key) {
  		addMenuItem(al, m, label, command, key, -1);
   }

  public  static  void addMenuItem(ActionListener al, JMenu m, String label, String command, int key, int ckey) {
        JMenuItem menuItem;
	menuItem = new JMenuItem(label);
        m.add(menuItem);

        menuItem.setActionCommand(command);
        menuItem.addActionListener(al);
	setKeystroke(menuItem, ckey);
  }

    static void setKeystroke(JMenuItem m, int key)
    {
	if (key > 0)
	    {
		if (key != KeyEvent.VK_DELETE)
		    m.setAccelerator(KeyStroke.getKeyStroke(key, Event.CTRL_MASK, false));
		else
		    m.setAccelerator(KeyStroke.getKeyStroke(key, 0, false));
	    }
   }

 public static void addRadioButtonMenuItem(ActionListener al, JMenu m, String label, String command, boolean b, ButtonGroup g)
  {
       JRadioButtonMenuItem menuItem;
       menuItem = new JRadioButtonMenuItem(label);
        m.add(menuItem);
        menuItem.setActionCommand(command);
        menuItem.addActionListener(al);
	menuItem.setSelected(b);
	g.add(menuItem);
  }

    /** Add the content of a hashtable to an existing popup-menu */
    static  public void addPopupItems(JPopupMenu p, Hashtable elems, ActionListener al)
    {
	for (Enumeration e = elems.keys() ; e.hasMoreElements() ;)
	    {
		String text = (String)e.nextElement();
		ActionDesc call = (ActionDesc)(elems.get(text));

		JMenuItem mi = new JMenuItem(text);
		mi.setActionCommand(call.getDescription());
		mi.addActionListener(al);
		p.add(mi);
	    }
    }
}












