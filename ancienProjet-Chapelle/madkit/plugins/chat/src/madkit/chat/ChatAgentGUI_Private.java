/*
* ChatAgentGUI_Private.java - ChatAgent, a chat application for MadKit
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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  PrivateAgent 's Graphic User Interface.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class ChatAgentGUI_Private extends JRootPane implements ActionListener {

    protected PrintWriter out; 
    ChatAgent agentChat;
    JPanel pCenter = new JPanel();
    JPanel pTop = new JPanel();
    JScrollPane jScrollPane1 = new JScrollPane();
    BorderLayout borderLayout1 = new BorderLayout();
    JTextField tWriteField = new JTextField();
    JTextArea tChatArea = new JTextArea(16,34);
    PrivateAgent agentPrivate;
    PrivateAgent1 agentPrivate1;
    
    public ChatAgentGUI_Private(PrivateAgent ag) {	
	agentPrivate = ag;
	try {	    jbInit();	}
	catch(Exception e) {e.printStackTrace();}//    pack();
    }
    public ChatAgentGUI_Private(PrivateAgent1 ag1){
	agentPrivate1 = ag1;
	try {	    jbInit();	}
	catch(Exception e) {e.printStackTrace();}//    pack(); 
    }	
    
    private PrivateAgent  getMyAgent()  {return(PrivateAgent) agentPrivate;}   
    private PrivateAgent1 getMyAgent1() {return(PrivateAgent1) agentPrivate1;}

    public PrintWriter stdout() { return out;}
    public void println(String s){stdout().println(s);}
    
  /**Remplacé, ainsi nous pouvons sortir quand la fenêtre est fermée*/
    protected void processWindowEvent(WindowEvent e) {
	if (e.getID() == WindowEvent.WINDOW_CLOSING) {cancel();}
;
    }    

    void cancel() {
//	dispose();
    }  
  /**Fermer le dialogue sur un événement bouton*/
  public void actionPerformed(ActionEvent e) {
   // if (e.getSource() == bQuerry) {	
      cancel();
  }
    private void jbInit() throws Exception {
	out  = new PrintWriter(new JTextAreaWriter(tChatArea), true);
	jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
	pCenter.setLayout(borderLayout1);
	this.getContentPane().add(pCenter, BorderLayout.CENTER);
	this.getContentPane().add(pTop, BorderLayout.NORTH);
	
	this.setSize(new Dimension(500, 600));
	this.setSize(500,600);
	
	jScrollPane1.setBorder(BorderFactory.createLoweredBevelBorder());
	pCenter.add(tWriteField,BorderLayout.SOUTH);
	jScrollPane1.getViewport().add(tChatArea,null); // BorderLayout.CENTER);
	pCenter.add(jScrollPane1, BorderLayout.CENTER);
	jScrollPane1.getViewport().add(tChatArea, null);
	
	tWriteField.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  tWriteField_actionPerformed(e);
	      }
	    });
    }
    public void tWriteField_actionPerformed(ActionEvent e) {
	PrivateAgent a=getMyAgent();
	PrivateAgent1 b=getMyAgent1();
	
	String s = ((JTextField) e.getSource()).getText();
	
	
	if(b == null){
	    a.sendPrivateMessage(s);
	}
	
	    if(a==null){
		b.sendPrivateMessage1(s);
	    }
	    
		((JTextField) e.getSource()).setText("");
	stdout().println("me>  " + s);		
    }
	
}//fin de class
