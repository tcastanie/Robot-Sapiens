/*
* ChatAgentGUI_TopicBox.java - ChatAgent, a chat application for MadKit
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
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  Graphic User Interface, used to get the channel's topic.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class ChatAgentGUI_TopicBox extends JDialog implements ActionListener {
  JPanel pCenter = new JPanel();
  JPanel pTop = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel lTop = new JLabel();
  JTextField tChanelName = new JTextField();
  JButton bEnter = new JButton();
  JPanel jPanel1 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JPanel jPanel2 = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel lChanelTopic = new JLabel();
  JPanel jPanel3 = new JPanel();
  BorderLayout borderLayout4 = new BorderLayout();
  JTextField tChanelTopic = new JTextField();
  ChatAgent agentChat;

  public ChatAgentGUI_TopicBox(ChatAgent ag_) {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    pack();
    agentChat = ag_;
  }

  public ChatAgentGUI_TopicBox(Frame parent) {
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
    if (e.getSource() == bEnter) {
      cancel();
    }
  }
  private void jbInit() throws Exception {
    this.setTitle("Chanel's Topic");
    pCenter.setLayout(borderLayout1);
    lTop.setMaximumSize(new Dimension(171, 13));
    lTop.setMinimumSize(new Dimension(171, 13));
    lTop.setHorizontalAlignment(SwingConstants.LEFT);
    lTop.setHorizontalTextPosition(SwingConstants.LEFT);
    lTop.setText("Enter the chanel\'s name");
    bEnter.setBorder(BorderFactory.createRaisedBevelBorder());
    bEnter.setText("Enter");
    bEnter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    if(!(tChanelName.getText()).equals("") && !(tChanelTopic.getText()).equals("")) 
                            bEnter_actionPerformed(e);
      }
    });
    jPanel1.setLayout(borderLayout2);
    jPanel2.setLayout(borderLayout3);
    lChanelTopic.setText("Chanel\'s topic");
    jPanel3.setLayout(borderLayout4);
    tChanelTopic.setToolTipText("Write here the chanel\'s topic");
    tChanelName.setToolTipText("Write here the chanel\'s name");
    this.getContentPane().add(pCenter, BorderLayout.CENTER);
    this.getContentPane().add(pTop, BorderLayout.NORTH);
    pTop.add(lTop, null);
    pCenter.add(tChanelName, BorderLayout.NORTH);
    pCenter.add(bEnter, BorderLayout.SOUTH);
    pCenter.add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(lChanelTopic, BorderLayout.NORTH);
    jPanel2.add(jPanel3, BorderLayout.CENTER);
    jPanel3.add(tChanelTopic, BorderLayout.NORTH);       
  }
  
  public void bEnter_actionPerformed(ActionEvent e)  {
          String na = repairString(tChanelName.getText());
          String to = repairString(tChanelTopic.getText());
          agentChat.sendTopic(na,to);
          dispose();
  }
  
   /**Build a one word string from a x words string*/
  public String repairString(String s) {
          String ret=""; int i=0;
          StringTokenizer st = new StringTokenizer(s);
          while (st.hasMoreTokens()) {
                  String tok= st.nextToken();
                  if(i<1)
                          ret = ret+tok;
                  else
                          ret = ret+"_"+tok;
                  i++;
          }
          return ret;
  }

}//fin de class
