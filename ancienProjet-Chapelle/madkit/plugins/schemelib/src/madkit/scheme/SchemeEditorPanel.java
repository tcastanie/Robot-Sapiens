/*
* SchemeEditorPanel.java - SchemeEditor, a simple editor to evaluate Scheme expressions
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
package madkit.scheme;

import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;

import madkit.utils.graphics.GraphicUtils;


class SchemeEditorPanel extends SchemeAbstractEditorPanel {


      SchemeEditorPanel (EditSchemeAgent _ag) {
      		super(_ag);

      		JMenuBar myMenubar = getMenubar();
	        // menu "Control"
	        JMenu menuControl=new JMenu("Actions");
	        myMenubar.add(menuControl);

	        GraphicUtils.addMenuItem(this, menuControl, "Eval selection", "evalSelection", KeyEvent.VK_Y, KeyEvent.VK_Y);
	        GraphicUtils.addMenuItem(this, menuControl, "Eval buffer", "evalBuffer", KeyEvent.VK_B, KeyEvent.VK_B);


	        JToolBar mytoolBar = getToolbar();
	        mytoolBar.addSeparator();

	        GraphicUtils.addTool(this, mytoolBar, "evalBuffer", "Send buffer", "/images/toolbars/sendbuf.gif");
	        GraphicUtils.addTool(this, mytoolBar, "evalSelection", "Send selection", "/images/toolbars/sendsel.gif");

    }


 	public void command(String c){
 		if (c.equals("evalBuffer")) evalBuffer();
 		else if (c.equals("evalSelection")) evalSelection();
 		else super.command(c);
 	}


     void evalBuffer() {
        String s = inputArea.getText();
     	((EditSchemeAgent) ag).doSendControlMessage("eval",s);
   }

   void evalSelection() {
     	String s = inputArea.getSelectedText();
     	((EditSchemeAgent) ag).doSendControlMessage("eval",s);
   }

   public void println(String s){
   		stdout().println(s);
   }


}
