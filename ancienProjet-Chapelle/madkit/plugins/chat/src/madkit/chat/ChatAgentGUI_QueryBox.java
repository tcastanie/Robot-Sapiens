/*
* ChatAgentGUI_QueryBox.java - ChatAgent, a chat application for MadKit
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import madkit.kernel.AgentAddress;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  GUI, used to ask somebody to chat in private.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class ChatAgentGUI_QueryBox extends JDialog implements ActionListener {
    JPanel pCenter = new JPanel();
    JPanel pTop = new JPanel();
    BorderLayout borderLayout1 = new BorderLayout();
    JLabel lTop = new JLabel();
    JTextField tNick = new JTextField();
    JButton bQuery = new JButton();
    ChatAgent ag;
    JComboBox jComboBox1 = new JComboBox();
    String sender;

    public ChatAgentGUI_QueryBox(ChatAgent ag_) {
    try {
        ag=ag_;
        jbInit();	
    }
    catch(Exception e) {
	e.printStackTrace();
    }
    pack();
    }
    
    public ChatAgentGUI_QueryBox(Frame parent) {
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
	this.setTitle("Query");
	pCenter.setLayout(borderLayout1);
	lTop.setText("Enter the nickname you want to query");
	bQuery.setBorder(BorderFactory.createRaisedBevelBorder());
	bQuery.setText("Query");
        
        jComboBox1.setToolTipText("Select one nick in the list");
        String [] tabChNames = ag.getNamesList();
        if(tabChNames==null)
                  jComboBox1 = new JComboBox();
        else
                  jComboBox1 = new JComboBox(tabChNames);
        
	this.getContentPane().add(pCenter, BorderLayout.CENTER);
	this.getContentPane().add(pTop, BorderLayout.NORTH);
	pTop.add(lTop, null);
	pCenter.add(tNick, BorderLayout.NORTH);
	pCenter.add(bQuery, BorderLayout.SOUTH);
	pCenter.add(jComboBox1, null);

	bQuery.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bQuery_actionPerformed();
	      }
	  });     
    }

    public void bQuery_actionPerformed()
    { 
            String n=""; 
            if((tNick.getText()).equals("")) {
                    n = (String) jComboBox1.getSelectedItem();                    
            }
            else
                    n = tNick.getText();
            
            
	
        AgentAddress ad=ag.getChatterAddress(n);
	//String m=tNick.getText();
	
	sender=ag.getChatter();
	
	ag.launchAgent(new PrivateAgent1(ad,n,sender),n,true);
	ag.sendQuerry(ad,n);
	cancel();
    }
}//fin de class
