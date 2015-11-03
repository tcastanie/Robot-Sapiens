/*
* DefaultControlPanel.java - Graphics utilities for MadKit agents
* Copyright (C) 1998-2002  Olivier Gutknecht
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
package madkit.utils.graphics;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import madkit.kernel.Agent;
import madkit.kernel.Utils;
import madkit.kernel.AgentAddress;
import madkit.messages.ControlMessage;
import madkit.utils.agents.LanguageController;

/**
 * An abstract class which is used by script agents, i.e. agents
 * whose behavior is described using a scripting language.
 * Note: the jedit() method assume that controllers are LanguageController
 * Redefine jedit() if this not the case.. A bit ugly isn't it???
 */
public class DefaultControlPanel extends JPanel implements ActionListener{

    protected Agent ag;
    public DefaultControlPanel(Agent _ag) {
        ag = _ag;
    }

    protected void addButton(JToolBar toolBar, String name, String descr, String imageName) {
        JButton b;
        if (imageName.equals("")) {
            b = (JButton) toolBar.add(new JButton(name));
            b.setActionCommand(name);
        }
        else {
          java.net.URL u = this.getClass().getResource(imageName);
          if (u!=null)
            b = (JButton) toolBar.add(new JButton(new ImageIcon(u)));
          else
              b = (JButton) toolBar.add(new JButton(name));
              b.setActionCommand(name);
        }
        b.setToolTipText(descr);
        b.setMargin(new Insets(0,0,0,0));
        b.addActionListener(this);
    }

    public void command(String c){
    }

    public void actionPerformed(ActionEvent e){
        command(e.getActionCommand());
    }

    protected void jedit(){
        LanguageController co = (LanguageController) ag.getController();
        String path = co.getBehaviorFile();
        if (path == null)
            co.println("sorry no behavior file to edit");
        else {
            ag.println("editing : " + path);
            AgentAddress agJedit = ag.getAgentWithRole("system","jedit");
            if (agJedit == null){
            	try {
	            	// $$ JF : to be fixed
	            	Class cag = Utils.loadClass("JEditAgent");
	            	Agent a = (Agent) cag.newInstance();
	                // JEditAgent a = new JEditAgent(new File(path));
	                ag.launchAgent(a,"jEdit",false);
	                agJedit=ag.getAddress();
	                System.out.println("jEdit address: " + agJedit);
	            } catch(Exception e){
	            	System.err.println("Unable to launch JEditAgent: package jEdit may have not been correctly installed");
	            }
            }
            else {
                ag.sendMessage(agJedit,new ControlMessage("edit",path));
            }
            System.out.println("jEdit launcher : " + agJedit);
            //ag.sendMessage(agJedit,new ControlMessage(JEditAgent.EDIT,path));
        }
    }

}
