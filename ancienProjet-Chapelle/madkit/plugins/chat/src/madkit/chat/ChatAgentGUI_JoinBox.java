/*
* ChatAgentGUI_JoinBox.java - ChatAgent, a chat application for MadKit
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
 * Description :  Graphic User Interface, used to join a channel.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class ChatAgentGUI_JoinBox extends JDialog implements ActionListener,java.io.Serializable {
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

  public ChatAgentGUI_JoinBox(Frame parent) {
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
  public ChatAgentGUI_JoinBox(ChatAgent ag_) {
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

    this.setTitle("Join");
    pMain.setLayout(borderLayout1);
    lTop.setText("Please, enter or select the channel\'s name you ");
    pCenter.setLayout(gridLayout1);
    jLabel1.setText("will be happy to join or create.");
    jTextField1.setMaximumSize(new Dimension(4, 17));
    jTextField1.setToolTipText("write the channel\'s name you want to join/create here.");
    jTextField1.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
                      bJoin_actionPerformed(e);
      }
    });
    jComboBox1.setToolTipText("Select the channel\'s name you want to join in the list.");
    bJoinOK.setBorder(BorderFactory.createRaisedBevelBorder());
    bJoinOK.setHorizontalTextPosition(SwingConstants.LEFT);
    bJoinOK.setText("OK Join");
    bJoinOK.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
                      bJoin_actionPerformed(e);
      }
    });
    gridLayout1.setRows(5);
    gridLayout1.setColumns(2);
    this.getContentPane().add(pMain, BorderLayout.CENTER);
    pMain.add(lTop, BorderLayout.NORTH);
    pMain.add(pCenter, BorderLayout.CENTER);
    pCenter.add(jLabel1, null);
    pCenter.add(jComboBox1, null);
    pCenter.add(jTextField1, null);
    pCenter.add(bJoinOK, null);
  }

  public void bJoin_actionPerformed(ActionEvent e) {
          if(!(jTextField1.getText()).equals("")) {
                  String newCh = jTextField1.getText();
                  Object [] tabAg = agentChat.getAgentsWithRole("Chat",newCh, "chatter");
                  //System.out.println("[OBJECT] tabAg.length = "+tabAg.length);
                  if(!agentChat.display.isExistChatPanel(jTextField1.getText())) {
                          if((tabAg.length)==0) {   //ce channel n'existait pas avant
                                  //System.out.println("[OBJECT] cas tabAg==null");
                                  agentChat.display.addNewChatPanel(jTextField1.getText());

                                  //AVT DE FAIRE



                                  agentChat.sendAddChannel(newCh);
                                  agentChat.addChannelForChatter(newCh);
                          }
                          else {  //ce channel existait avt
                                  //System.out.println("[OBJECT] cas tabAg PAS null");
                                  agentChat.display.addNewChatPanel(newCh);
                                  agentChat.sendMAJNbrUsrersOfChannel(newCh);
                                  agentChat.addChannelForChatter(newCh);
                          }
                          dispose();
                  }
                  else
                          jLabel1.setText("Channel already in use on your chat");
          }
          else {
                  String newCh = (String ) jComboBox1.getSelectedItem();
                  if(!agentChat.display.isExistChatPanel(newCh)) {
                          agentChat.display.addNewChatPanel(newCh);
                          agentChat.sendMAJNbrUsrersOfChannel(newCh);
                          agentChat.addChannelForChatter(newCh);
                          dispose();
                  }
                  else
                          jLabel1.setText("Channel already in use on your chat");
          }
  }


} //fin de la class
