/*
* BeanShellEditorPanel.java - BeanShellEditor agents, an small editor to evaluate beanshell expressions
* Copyright (C) 1998-2000 Jacques Ferber
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
package madkit.bsh;

import java.awt.event.KeyEvent;
import java.io.PrintStream;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import madkit.kernel.AbstractAgent;
import madkit.utils.agents.AbstractEditorPanel;
import madkit.utils.graphics.JTextAreaOutputStream;
import bsh.Interpreter;

public class BeanShellEditorPanel extends AbstractEditorPanel {


    protected JPanel commandPanel;
    protected PrintStream bshout;


    public BeanShellEditorPanel (AbstractAgent _ag, Interpreter interp) {
      		super(_ag);

      		JMenuBar myMenubar = getMenubar();
	        // menu "Control"
	        JMenu menuControl=new JMenu("Actions");
	        myMenubar.add(menuControl);
	        //addMenuItem(this, menuControl, "Dir", "dir", KeyEvent.VK_T, KeyEvent.VK_T);
	        addMenuItem(this, menuControl, "Eval buffer", "evalBuffer", KeyEvent.VK_B, KeyEvent.VK_B);
	        addMenuItem(this, menuControl, "Eval selection", "evalSelection", KeyEvent.VK_D, KeyEvent.VK_D);


	        JToolBar mytoolBar = getToolbar();
	        mytoolBar.addSeparator();
	        addTool(mytoolBar, "evalBuffer", "Eval buffer", "/images/toolbars/sendbuf.gif");
	        addTool(mytoolBar, "evalSelection", "Eval selection", "/images/toolbars/sendsel.gif");

            JTextAreaOutputStream outStream = new JTextAreaOutputStream(this.getOutputArea());
            bshout = new PrintStream(outStream, true);
            interp.setErr(bshout);
            interp.setOut(bshout);
            setExtens("bsh");
    }


 	public void command(String c){
        BeanShellController co = (BeanShellController) ag.getController();
 		if (c.equals("evalBuffer")) evalBuffer();
 		else if (c.equals("evalSelection")) evalSelection();
   		else if(c.equals("dir"))
  	 		co.doSendControlMessage("dir");
 		else super.command(c);
 	}

    // could be more generic
     void evalBuffer() {
        BeanShellController co = (BeanShellController) ag.getController();
        String s = inputArea.getText();
     	co.doSendControlMessage("eval",s);
    }

    void evalSelection() {
        BeanShellController co = (BeanShellController) ag.getController();
     	String s = inputArea.getSelectedText();
     	co.doSendControlMessage("eval",s);
   }




}
