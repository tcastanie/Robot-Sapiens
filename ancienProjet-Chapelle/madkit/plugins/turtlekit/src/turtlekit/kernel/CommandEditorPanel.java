/*
* CommandEditorPanel.java -TurtleKit - A 'star logo' in MadKit
* Copyright (C) 2000-2002 Fabien Michel
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
package turtlekit.kernel;

import java.io.File;

import javax.swing.JFileChooser;

import madkit.kernel.AbstractAgent;
import madkit.python.PythonEditorPanel;

import org.python.util.PythonInterpreter;
/**
    @author Fabien MICHEL
    @version 1.2 4/1/2002 */

public class CommandEditorPanel extends PythonEditorPanel
{
	PythonCommandCenter myAgent;


public CommandEditorPanel (AbstractAgent _ag, PythonInterpreter interpret)
{
	super(_ag,interpret);
	myAgent = (PythonCommandCenter) _ag;
}


public void command(String c)
{
	if (c.equals("evalBuffer")) evalBuffer1();
	else if (c.equals("evalSelection")) evalSelection1();
	else if(c.equals("dir"))
 		myAgent.evaluation("dir",null);
	else super.command(c);
}

    // could be more generic
     void evalBuffer1() {
        String s = inputArea.getText();
     	myAgent.evaluation("eval",s);
    }

    void evalSelection1() {
     	String s = inputArea.getSelectedText();
     	myAgent.evaluation("eval",s);
   }


  protected boolean getFileDialog(boolean direction, String title,String extens){
            JFileChooser fd = new JFileChooser(System.getProperty("madkit.dir",null)+File.separator+"plugins"+File.separator+"turtlekit"+File.separator+"pythonturtles");
            if (extens != null){
                    System.out.println("set filter: " + extens);
                fd.setFileFilter(new TurtleFileFilter(extens));
            }
       		fd.setDialogTitle(title);
            int retval=0;
            if (direction == LOAD){
                //fd.setDialogType(JFileChooser.OPEN_DIALOG);
       		    retval = fd.showOpenDialog(this);
            } else {
                //fd.setDialogType(JFileChooser.SAVE_DIALOG);
       		    retval = fd.showSaveDialog(this);
            }
       		if (retval == JFileChooser.APPROVE_OPTION)	{
                if (fd.getSelectedFile()!=null)
                {
                    setCurrentFile(fd.getSelectedFile().getPath());
                    return(true);
                }
                else
                    return false;
		    } else
                    return(false);
 }

 public void openFile() {
	Object result;
	if (getFileDialog(LOAD,"Open python turtle file","pyt")) {
		if (getCurrentFile() != null){
            readFile(getCurrentFile());
        }
    }
 }

	public void saveAs() {
		if (getFileDialog(SAVE,"Save python turtle file","pyt"))
	       	save();
	}

}




class TurtleFileFilter extends javax.swing.filechooser.FileFilter {

    String extens="py";

    TurtleFileFilter(String ext){
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
