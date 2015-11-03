/*
* ChatAgentGUI_NickOKBox.java - ChatAgent, a chat application for MadKit
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
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  Alllow MadKit's users to communicate through a chat.
 * Of course the chat is a MadKit agent
 * Copyright :    BERTRAND Jean-Gabriel Copyright (c) 2002
 * @author
 * @version 1.0
 */

public class ChatAgentGUI_NickOKBox extends JDialog implements ActionListener {
  JPanel pCenter = new JPanel();
  JPanel pTop = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel lTop = new JLabel();
  JButton bOK = new JButton();
  JLabel lNick = new JLabel();
  String nick="nick not defined yet";

  public ChatAgentGUI_NickOKBox(String n) {
    try {
      nick=n;
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    pack();
  }

  public ChatAgentGUI_NickOKBox() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public ChatAgentGUI_NickOKBox(Frame parent) {
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


  public static void main(String[] args) {
    ChatAgentGUI_NickOKBox ChatAgentGUI_NickOKBox1 = new ChatAgentGUI_NickOKBox();
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
    if (e.getSource() == bOK) {
      cancel();
    }
  }
  private void jbInit() throws Exception {
    this.setTitle("Nickname OK");
    pCenter.setLayout(borderLayout1);
    lTop.setText("Your nickname is now");
    bOK.setBorder(BorderFactory.createRaisedBevelBorder());
    bOK.addActionListener(this);
    bOK.setActionCommand("bOK");
    bOK.setText("OK");
    lNick.setText(nick);
    pCenter.setMinimumSize(new Dimension(244, 27));
    pCenter.setPreferredSize(new Dimension(244, 27));
    pTop.setMinimumSize(new Dimension(267, 27));
    pTop.setPreferredSize(new Dimension(267, 27));
    this.getContentPane().add(pCenter, BorderLayout.CENTER);
    this.getContentPane().add(pTop, BorderLayout.NORTH);
    pTop.add(lTop, null);
    pCenter.add(bOK, BorderLayout.SOUTH);
    pCenter.add(lNick, BorderLayout.NORTH);
  }

}//fin de class
