/*
* MinimalChatPanel.java -a minimal Chat application with MadKit
* Copyright (C) 2000-2002 Jacques Ferber
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
package madkit.system;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import madkit.utils.agents.AbstractEditorPanel;

/**
 * Titre :        Madkit 3.0 dev projet
 * Description :  Madkit project
 * (C) 1998-2001 Madkit Team
 * Copyright :    Copyright (c) 2001
 * Société :
 * @author O. Gutknecht, M. Fabien, J. Ferber
 * @version 1.0
 */

public class MinimalChatPanel extends AbstractEditorPanel {

    MinimalChatAgent getMyAgent(){return (MinimalChatAgent) ag;}

    public MinimalChatPanel(MinimalChatAgent _ag){
      		super(_ag);

      		JMenuBar myMenubar = getMenubar();
	        // menu "Control"
	        JMenu menuControl=new JMenu("Actions");
	        myMenubar.add(menuControl);
	        addMenuItem(this, menuControl, "Send selection", "sendsel", KeyEvent.VK_E, KeyEvent.VK_E);
	        addMenuItem(this, menuControl, "Send buffer", "sendbuff", KeyEvent.VK_B, KeyEvent.VK_B);


	        JToolBar mytoolBar = getToolbar();
	        mytoolBar.addSeparator();
		//			/addTool(toolBar, "print", "demo/agents/system/print.gif");
	        addTool(mytoolBar, "sendbuff", "Send buffer", "/images/agents/system/sendbuf.gif");
	        addTool(mytoolBar, "sendsel", "Send selection", "/images/agents/system/sendsel.gif");

            JTextField fieldName = new JTextField(20);
            mytoolBar.add(fieldName);
            fieldName.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    String s = ((JTextField) e.getSource()).getText();
     	            getMyAgent().setChatter(s);
     	            stdout().println("my name is " + s);
                }
            });


            JTextField line = new JTextField();
			getContentPane().add("South",line);
            line.addActionListener(new ActionListener(){
                MinimalChatAgent a = getMyAgent();
                public void actionPerformed(ActionEvent e){
                    String s = ((JTextField) e.getSource()).getText();
     	            ((MinimalChatAgent) a).sendChatMessage(s);
                    ((JTextField) e.getSource()).setText("");
     	            stdout().println("me> " + s);
                }
            });
    }


 	public void command(String c){
 		if (c.equals("sendbuff")) evalBuffer();
 		else if (c.equals("sendsel")) evalSelection();
 		else super.command(c);
 	}


    void evalBuffer() {
        String s = inputArea.getText();
     	((MinimalChatAgent) ag).sendChatMessage(s);
     	stdout().println("me> " + s);
   }

   void evalSelection() {
     	String s = inputArea.getSelectedText();
     	((MinimalChatAgent) ag).sendChatMessage(s);
     	stdout().println("me> " + s);
   }
}
