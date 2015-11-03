/*
* LoadDialog.java - a tool to open and save files in MadKit
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
package madkit.utils.graphics;

import java.awt.Component;
import java.awt.FileDialog;
import java.awt.Frame;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.UIManager;

// awt class
class AwtFileFilter implements java.io.FilenameFilter {

    final static String xml = "xml";
    String extens="xml";


    AwtFileFilter(String ext){
        extens = ext;
    }

    // Filter files
    public boolean accept(File dir,String name) {
        if (extens == null)
            return true;
        else if (name.endsWith("."+extens))
            return true;
        else
            return false;
    }

    // The description of this filter
    public String getDescription() {
        return extens+" files";
    }
}

// swing class
class SwingFileFilter extends javax.swing.filechooser.FileFilter {

    final static String xml = "xml";
    String extens="xml";

    SwingFileFilter(String ext){
        extens = ext;
    }

    // Accept all directories and extension file
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        if (extens == null)
            return true;
        else if (f.getName().endsWith("."+extens))
            return true;
        else
            return false;
    }

    // The description of this filter
    public String getDescription() {
        return extens+" files";
    }
}


public class LoadDialog {

  public static final boolean LOAD = true;
  public static final boolean SAVE = false;

  boolean swingLF=true;

  String dirName;
  String fileName;

  boolean choosed=false;

  public String getDirName(){return dirName;}
  public void setDirName(String s){dirName = s;}

  public String getFileName(){return fileName;}
  public void setFileName(String s){fileName = s;}

  public LoadDialog(Component comp, boolean direction, String title){
        getFileDialog(comp,direction,title,"xml");
  }

  public LoadDialog(Component comp, boolean direction, String title,String extens){
        getFileDialog(comp,direction,title,extens);
  }

  public boolean isFileChoosed(){
        return(choosed);
  }

  public boolean getFileDialog(Component comp, boolean direction, String title, String extens)
	{
		if ((UIManager.getLookAndFeel().getName().equals("Windows")) && !swingLF) {
                Frame owner = (Frame) GraphicUtils.getRealFrameParent(comp);
                if (owner == null){
                    System.err.println("Owner=null");
                    return(false);
                }
       		   	FileDialog fd = new FileDialog(owner,title,
       		   							((direction)?FileDialog.LOAD:FileDialog.SAVE));
                if (extens != null){
                    System.out.println("set filter: " + extens);
                    fd.setFilenameFilter(new AwtFileFilter(extens));
                }
       			fd.show();
       		    dirName = fd.getDirectory();
                fileName = fd.getFile();
                if (fileName != null)
                    choosed=true;
                return(choosed);
       } else {
       		JFileChooser fd = new JFileChooser(System.getProperty("user.dir",null));
            if (extens != null){
                    System.out.println("set filter: " + extens);
                fd.setFileFilter(new SwingFileFilter(extens));
            }
       		fd.setDialogTitle(title);
            int retval=0;
            if (direction == LOAD){
                //fd.setDialogType(JFileChooser.OPEN_DIALOG);
       		    retval = fd.showOpenDialog(comp);
            } else {
                //fd.setDialogType(JFileChooser.SAVE_DIALOG);
       		    retval = fd.showSaveDialog(comp);
            }
       		if (retval == JFileChooser.APPROVE_OPTION)	{
                if (fd.getSelectedFile()!=null)
                {
                    dirName = fd.getSelectedFile().getParent()+File.separator;
                    fileName = fd.getSelectedFile().getName();
                    choosed=true;
                    return(true);
                }
                else
                    return false;
		    } else
                    return(false);
	    }
	 }

}
