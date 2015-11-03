/*
* ChatAgentGUI_RetireBox.java - ChatAgent, a chat application for MadKit
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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
//import com.borland.jbcl.layout.*;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  Graphic User Interface, used to remove a channel from the chat.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class ChatAgentGUI_RetireBox extends JDialog implements ActionListener {
  JPanel pMain = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel lTop = new JLabel();
  JPanel pCenter = new JPanel();
  JLabel jLabel1 = new JLabel();
  JTextField jTextField1 = new JTextField();
  JButton bJoinOK = new JButton();
  GridLayout gridLayout1 = new GridLayout();
  ChatAgent agentChat;
  String [] tabChNames;
  JComboBox jComboBox1;

  public ChatAgentGUI_RetireBox(Frame parent) {
//    super(parent);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    pack();
  }
  public ChatAgentGUI_RetireBox(ChatAgent ag_) {
    agentChat = ag_;
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
    if (e.getSource() == bJoinOK) {
      cancel();
    }
  }


  private void jbInit() throws Exception {
          //agentChat.printnameChannel();
          tabChNames = agentChat.getChannelsNamesList();
          if(tabChNames==null)
                  jComboBox1 = new JComboBox();
          else
                  jComboBox1 = new JComboBox(tabChNames);

    this.setTitle("Remove a tabbedPanel");
    pMain.setLayout(borderLayout1);
    lTop.setText("Please, select the panel you want to remove from your Chat.");
    pCenter.setLayout(gridLayout1);
    jLabel1.setText("                                                ");
    jTextField1.setMaximumSize(new Dimension(4, 17));
    jTextField1.setToolTipText("write the chanel\'s name here");
    jComboBox1.setToolTipText("Select one chanel in the list");
    bJoinOK.setBorder(BorderFactory.createRaisedBevelBorder());
    bJoinOK.setHorizontalTextPosition(SwingConstants.LEFT);
    bJoinOK.setText("OK Remove");
    bJoinOK.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
                      bRetire_actionPerformed(e);
      }
    });
    gridLayout1.setRows(5);
    gridLayout1.setColumns(2);
    this.getContentPane().add(pMain, BorderLayout.CENTER);
    pMain.add(lTop, BorderLayout.NORTH);
    pMain.add(pCenter, BorderLayout.CENTER);
    pCenter.add(jLabel1, null);
    pCenter.add(jComboBox1, null);
//    pCenter.add(jTextField1, null);
    pCenter.add(bJoinOK, null);
  }

  public void bRetire_actionPerformed(ActionEvent e) {
          String newCh = (String ) jComboBox1.getSelectedItem();
          if(agentChat.display.isExistChatPanel(newCh)) {
          if(!newCh.equals("defaultChannel")) {
                  Object [] tab = agentChat.getAgentsWithRole("Chat", newCh, "chatter");
                  System.out.println("[RETIREBOX] tab.length = "+tab.length);
                  if(tab.length == 1) {
                          agentChat.display.removeChatPanel(newCh);
                          agentChat.leaveRole("Chat", newCh, "chatter");
                         // System.out.println("[RETIREBOX] J'ai fait un removeChatPanel");
                  }
                  else {
                          agentChat.display.removeChatPanelViewOnly(newCh);
                          agentChat.leaveRole("Chat", newCh, "chatter");
                         // System.out.println("[RETIREBOX] J'ai fait un removeChatPanelViewOnly");
                  }
                  agentChat.sendMAJNbrUsrersOfChannel(newCh);
                  dispose();
          }
          else
                  jLabel1.setText("NOT ALLOWED");
          }
          else
                  jLabel1.setText("Can't remove panel, does not exist on your Chat");
  }



} //fin de la class
