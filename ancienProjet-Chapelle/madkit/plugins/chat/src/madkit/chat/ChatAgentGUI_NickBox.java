/*
* ChatAgentGUI_NickBox.java - ChatAgent, a chat application for MadKit
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
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
 * Description :  Graphic User Interface, used to get name and e-mail from the chatter at start.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class ChatAgentGUI_NickBox extends JDialog implements ActionListener {

  PrintWriter nick;
  ChatAgent agentChat;
  int i = 0;
  JPanel pCenter = new JPanel();
  JPanel pTop = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel lTop = new JLabel();
  JTextField tNick = new JTextField();
  JButton bEnter = new JButton();
  JPanel pCenter2 = new JPanel();
  BorderLayout borderLayout2 = new BorderLayout();
  JLabel lAllready = new JLabel();
  //JRadioButton bTest = new JRadioButton();
  JLabel bTest = new JLabel();
  JTextField tEmail = new JTextField();

  ChatAgent getMyAgent() {return (ChatAgent) agentChat;}

  public ChatAgentGUI_NickBox(ChatAgent ag, PrintWriter nickname) {
    agentChat=ag;
    nick = nickname;
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    pack();
  }

  public ChatAgentGUI_NickBox(Frame parent) {
    super(parent);
    //enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
    pack();
  }


//  /**Remplacé, ainsi nous pouvons sortir quand la fenêtre est fermée*/
//  protected void processWindowEvent(WindowEvent e) {
//    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//      cancel();
//    }
//    super.processWindowEvent(e);
//  }
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
    this.setTitle("Nickname");
    pCenter.setLayout(borderLayout1);
    lTop.setText("Please, enter your nickname and e-mail");
    bEnter.setBorder(BorderFactory.createRaisedBevelBorder());
    bEnter.setText("Enter");
    bEnter.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
              if(!(tNick.getText()).equals("") && !(tEmail.getText()).equals(""))
                      bEnter_actionPerformed(e);
      }
    });
    // set the host kernel as default name
    //tNick.setText("");
    tNick.setText(agentChat.getAddress().getKernel().getHost());
    tNick.setToolTipText("Write your nickname in this text field.");
    tEmail.setText("None");
    tNick.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
              if(!(tNick.getText()).equals("") && !(tEmail.getText()).equals(""))
                      bEnter_actionPerformed(e);
      }
    });
    // set the host kernel as default email
    //tEmail.setText("");
    tNick.setText(agentChat.getAddress().getKernel().getHost());
    tEmail.setToolTipText("Write you e-mail address in this text field");
    tEmail.addActionListener(new ActionListener()  {
      public void actionPerformed(ActionEvent e) {
              if(!(tNick.getText()).equals("") && !(tEmail.getText()).equals(""))
                      bEnter_actionPerformed(e);
      }
    });
    pCenter2.setLayout(borderLayout2);
    lAllready.setForeground(Color.red);
    lAllready.setMaximumSize(new Dimension(179, 13));
    lAllready.setMinimumSize(new Dimension(179, 13));
    lAllready.setPreferredSize(new Dimension(179, 13));
    lAllready.setHorizontalAlignment(SwingConstants.CENTER);
    bTest.setText("                                                                ");
    bTest.setForeground(Color.red);
    this.getContentPane().add(pCenter, BorderLayout.CENTER);
    this.getContentPane().add(pTop, BorderLayout.NORTH);
    pTop.add(lTop, null);
    pCenter.add(tNick, BorderLayout.NORTH);
    pCenter.add(bEnter, BorderLayout.SOUTH);
    pCenter.add(pCenter2, BorderLayout.CENTER);
    pCenter2.add(lAllready, BorderLayout.NORTH);
    pCenter2.add(bTest, BorderLayout.SOUTH);
    pCenter2.add(tEmail, BorderLayout.NORTH);
  }

  //PB : lors de la 1ere utilisation la table n'est pas encore rempli => répond tjs faux !!
  public boolean isNameExist(String n) {
          //  A METTRE AU POINT
          boolean b=false;
          ChatAgent a = getMyAgent();
          Object [] tabAgAdr = a.getAgentsWithRole("Chat","defaultChannel","chatter");
          if(tabAgAdr.length > 1) {
                  a.sendUnique();
                  //PAUSE ??? CAUSE PROPAGATION???
                  //String [] tab = a.getNamesList();
                  String [] tab = a.getTabChatter();
                  if(tab==null) //PB ICI tab est NULL !!!!
                          System.out.println("\tCan't ckeck if name is already in use");
                  else {
                          for(int i=0; i<tab.length; i++) {
                                  if(tab[i].equals(n)) b=true;
                                  System.out.println("\t[isNameExist] tab["+i+"]:"+tab[i]+" =?= chosed:"+n);
                          }
                  }
          }
          return b;
  }

  public void bEnter_actionPerformed(ActionEvent e) {
    Chatter ch; boolean flag = false;
    ChatAgent a = getMyAgent();
    String s= tNick.getText();
    String s2= tEmail.getText();
    //Check the email spelling(wspace) and correct the nickname(one word)
    if( checkString(s)>1 || checkString(s2)>1) {
            if(checkString(s)>1) {
                    s=repairString(s);
                    flag=true;
            }
            if(checkString(s2)>1) {
                    bTest.setText("Wrong e-mail type, check white space !");
                    flag=false;
             }
     }
     else flag=true;
    //Get the hashtable and check if nickname already exist
    a.sendGetTable();
    if(isNameExist(s))
            bTest.setText("NickName already in use");
    else {
            if(flag) {
                    ((ChatAgent) a).setChatter(s);
                    ((ChatAgent) a).println("your nickname is now "+s);
                    ((ChatAgent) a).sendChatMessage("I 'VE JUST ARRIVED ON THE CHAT !! ");
                    if(s!=null) {
                        ch =new Chatter(a.getAddress(), s, s2, "chatter", "defaultChannel");}
                    else {ch =new Chatter(a.getAddress(), "no nickname", s2, "chatter", "defaultChannel");}

                    a.addCouple(s, ch);
                    String role="";
                    if(a.doesAdminExist()) role="none"; else role=s;
                    Object [] tabNbrUsers = agentChat.getAgentsWithRole("Chat","defaultChannel","chatter");
                    int nbrUsers = tabNbrUsers.length;
                    //System.out.println("[NickBox] Nbre d'agents (Chat,defaultChannel,chatter) = "+nbrUsers);
                    a.addCouple("defaultChannel", new Channel("defaultChannel", "defaultTopic", nbrUsers, role), nbrUsers);
                    a.upDateNickname();
                    //a.printAdrNick();
                    a.allowFunctions();
                    a.display.addNicknameButton();
                    dispose();
            }
        }
  }
  /**Return the number of word in the string*/
  public int checkString(String s) {
          int n = 0;
          StringTokenizer st = new StringTokenizer(s);
          n = st.countTokens();
          return n;
  }
  /**Return the first element of a string*/
  public String returnFirstElem(String s) {
           String tmp ="";
           StringTokenizer st = new StringTokenizer(s);
           if((st.countTokens())>=1) {
                   tmp = (String) st.nextElement();
           }
           else tmp = "-1";
           return tmp;
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
