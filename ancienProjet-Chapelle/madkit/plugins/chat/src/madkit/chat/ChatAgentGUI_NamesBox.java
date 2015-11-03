/*
* ChatAgentGUI_NamesBox.java - ChatAgent, a chat application for MadKit
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
import java.awt.Color;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  Graphic User Interface, used to display the chatters list.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class ChatAgentGUI_NamesBox extends JDialog implements ActionListener {
    boolean DEBUG = false;
    ChatAgent agentChat;
    JPanel pCenter = new JPanel();
    JPanel pTop = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JLabel lTop = new JLabel();
    JButton bRefresh = new JButton();
    JScrollPane jScrollPane1 = new JScrollPane();

    final String[] columnNames = {"Nicknames","Addresses"};
    Object[][] data = new Object [20][2];
    JTable jTableList = new JTable(data, columnNames);
    DefaultTableModel listModel;
    
    public ChatAgent getMyAgent(){return (ChatAgent) agentChat;}

  public ChatAgentGUI_NamesBox(ChatAgent ag) {
    try {
	agentChat=ag;
	listModel = new DefaultTableModel(columnNames,0);
	jTableList = new JTable(listModel);

      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    pack();
  }
    
    public void addNewChatter(String name,String email)
    {
	String[] m = new String[2];
	m[0]= name;
	m[1]=email;
	listModel.addRow(m);
    }

  public ChatAgentGUI_NamesBox(Frame parent) {
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
    if (e.getSource() == bRefresh) {
      cancel();
    }
  }
  private void jbInit() throws Exception {
    this.setTitle("Names List");
    pCenter.setLayout(borderLayout1);
    lTop.setMaximumSize(new Dimension(171, 13));
    lTop.setMinimumSize(new Dimension(171, 13));
    lTop.setHorizontalAlignment(SwingConstants.LEFT);
    lTop.setHorizontalTextPosition(SwingConstants.LEFT);
    lTop.setText("Names list");
    bRefresh.setBorder(BorderFactory.createRaisedBevelBorder());
    bRefresh.setText("Refresh");
    jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
    jTableList.setBorder(BorderFactory.createLineBorder(Color.black));
    jTableList.setColumnSelectionAllowed(true);
    this.getContentPane().add(pCenter, BorderLayout.CENTER);
    this.getContentPane().add(pTop, BorderLayout.NORTH);
    pTop.add(lTop, null);
    pCenter.add(bRefresh, BorderLayout.SOUTH);
    pCenter.add(jScrollPane1, BorderLayout.CENTER);
    jScrollPane1.getViewport().add(jTableList, null);

    bRefresh.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bRefresh_actionPerformed(e);
	      }
	  });
    refresher();

  }
  public void bRefresh_actionPerformed(ActionEvent e) {
        if(DEBUG==true) {getMyAgent().printnickChatter();}
        refresher();
  }

  public void refresher() {
        listModel=new DefaultTableModel(columnNames,0);
        jTableList.setModel(listModel);
        ChatAgent toto = getMyAgent();
        String [] tabName = toto.getNamesList();
        String [] tabEmail = toto.getEmailList();
        String [] tabRole = toto.getRoleList();
        for (int i=0; i<tabName.length; i++) {
                if((tabRole[i]).equals("Admin")) {
                        addNewChatter("@ "+tabName[i],tabEmail[i]);
                }
                else addNewChatter(tabName[i],tabEmail[i]);
        }
  }


}//fin de class
