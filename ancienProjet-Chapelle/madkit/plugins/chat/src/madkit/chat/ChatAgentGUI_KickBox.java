/*
* ChatAgentGUI_KickBox.java - ChatAgent, a chat application for MadKit
* Copyright (C) 2002 Jean-Gabriel Bertrand, Omar Mohsine
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
package madkit.chat;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  Graphic User Interface, used to kick a chatter.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class ChatAgentGUI_KickBox extends JDialog implements ActionListener {
  JPanel pCenter = new JPanel();
  JPanel pTop = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel lTop = new JLabel();
  JTextField tNick = new JTextField();
  JButton bQuery = new JButton();
  ChatAgent agentChat;

  public ChatAgentGUI_KickBox(ChatAgent ag_) {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    pack();
    agentChat = ag_;
  }

  public ChatAgentGUI_KickBox(Frame parent) {
    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    pack();
  }


  /**Remplacé, ainsi nous pouvons sortir quand la fenêtre est fermée*/
  protected void processWindowEvent(WindowEvent e) {
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      cancel();
    }
    super.processWindowEvent(e);
  }
  /**Fermer le dialogue*/
  void cancel() {
    dispose();
  }
  /**Fermer le dialogue sur un événement bouton*/
  public void actionPerformed(ActionEvent e) {
    if (e.getSource() == bQuery) {
      cancel();
    }
  }
  private void jbInit() throws Exception {
    this.setTitle("Kick");
    pCenter.setLayout(borderLayout1);
    lTop.setText("Enter the nickname you want to kick");
    bQuery.setBorder(BorderFactory.createRaisedBevelBorder());
    bQuery.setText("Kick");
    bQuery.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    if(!(tNick.getText()).equals("")) 
                            bQuery_actionPerformed(e);
      }
    });
    this.getContentPane().add(pCenter, BorderLayout.CENTER);
    this.getContentPane().add(pTop, BorderLayout.NORTH);
    pTop.add(lTop, null);
    pCenter.add(tNick, BorderLayout.NORTH);
    pCenter.add(bQuery, BorderLayout.SOUTH);
  }
  
  public void bQuery_actionPerformed(ActionEvent e)  {
          if(!agentChat.isAdmin("defaultChannel",tNick.getText())) {
                  agentChat.sendKick(tNick.getText());
                  dispose();
          }
  }

}//fin de class
