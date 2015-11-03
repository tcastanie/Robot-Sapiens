/*
* Utils.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
//package editor;

package jsynedit;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.KeyboardFocusManager;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

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

    static public void setFrameParentTitle(Container c, String s){
        Container parent = getFrameParent(c);
        if (parent instanceof JInternalFrame)
            ((JInternalFrame) parent).setTitle(s);
        else if (parent instanceof Frame)
            ((Frame) parent).setTitle(s);
    }

	// utility function to get portion of a name. Should be
    // placed in a utility class..
    public static String getLastPortion(String s, char c){
        int k = s.lastIndexOf(c);
        if (k != -1){
            return s.substring(k+1);
        }
        else return s;
   }

   public static String getFileNameWithoutExtensionFromPath(String s){
        s = s.replace(File.separatorChar,'/');
        s = getLastPortion(s,'/');
        int k = s.lastIndexOf('.');
        if (k != -1){
            return s.substring(0,k);
        } else
            return s;
   }

   public static String getFileNameFromPath(String s){
        s = s.replace(File.separatorChar,'/');
        return(getLastPortion(s,'/'));
   }
   
   public static Component getFocusOwner(Component c)
   {
	   return (
			   c.isFocusOwner() ?
			   c :
			   KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()
			   );
   }
}
