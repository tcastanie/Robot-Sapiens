/*
* ChatAgentGUI.java - ChatAgent, a chat application for MadKit
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

//Ferber
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  The main piece of the graphic user interface.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class ChatAgentGUI extends JRootPane
{
        boolean flag=false;
        Vector chattersNameList;
        protected PrintWriter out;
        protected PrintWriter nick;
        protected JTextArea inputArea;
        Hashtable channelOut = new Hashtable();
        Hashtable channelPanel = new Hashtable();
        Hashtable channelNickArea = new Hashtable();
    JPanel contentPane;
    JMenuBar jMenuBar1 = new JMenuBar();
    JMenu jMenuFile = new JMenu();
    JMenuItem jMenuFileExit = new JMenuItem();
    JMenu jMenuHelp = new JMenu();
    JMenuItem jMenuHelpAbout = new JMenuItem();
    JToolBar jToolBar = new JToolBar();
    JButton bOpen = new JButton();
    JButton bSave = new JButton();
    JButton bHelp = new JButton();
    JLabel lBlank = new JLabel();
    JButton bNickname = new JButton();
    ImageIcon image1;
    ImageIcon image2;
    ImageIcon image3;
    ImageIcon chatterImage;
    ImageIcon adminImage;
    JLabel statusBar = new JLabel();
    BorderLayout borderLayout1 = new BorderLayout();
    JPanel jLeftP = new JPanel();
    JPanel jCenterP = new JPanel();
    GridLayout gridLayout2 = new GridLayout();
    TitledBorder titledBorder1;
    BorderLayout borderLayout2 = new BorderLayout();
    JLabel lBottom = new JLabel();
    JPanel pCenter = new JPanel();
    JButton bBan = new JButton();
    JButton bTopic = new JButton();
    JButton bList = new JButton();
    JButton bQuery = new JButton();
    JButton bNames = new JButton();
    JButton bKick = new JButton();
    JToolBar jToolBar1 = new JToolBar();
    JButton bNick = new JButton();
    JButton bJoin = new JButton();
    JLabel lChanelTopic = new JLabel();
    JTabbedPane jTabbedPane2 = new JTabbedPane();
    JSplitPane jSplitPane1 = new JSplitPane();
    JLabel lChanelName = new JLabel();
    GridLayout gridLayout4 = new GridLayout();
    JPanel pMChatUp = new JPanel();
    BorderLayout borderLayout3 = new BorderLayout();
    JPanel pMiniChat = new JPanel();
    JTextField tWriteArea = new JTextField();
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JScrollPane pChatArea = new JScrollPane();
    JTextArea tChatArea = new JTextArea();
    JScrollPane pNames = new JScrollPane();
    JTextArea tNamesArea = new JTextArea();
    ChatAgent agentChat;
    //Test TabbedPane
    JPanel panelTMP = new JPanel();

    public PrintWriter stdout() { return out;}
    public PrintWriter stdnick() {return nick;}

    public void println(String s){
     	stdout().println(s);
    }
    public void printlnOnChannel(String chName,String msg) {
            givePrintWriterLinkedWithTabbedPane(chName).println(msg);
    }
    public void printNickln(String s){
        stdnick().println(s);
    }

    private ChatAgent getMyAgent() {return (ChatAgent) agentChat;}


    /**Construire le cadre*/
    public ChatAgentGUI(ChatAgent _ag) {
        agentChat = _ag;
	try {
	    jbInit();
	}
	catch(Exception e) {
	    System.out.println("Erreur lors de l'appel de jbInit() ds la classe ChatAgentGUI");
            e.printStackTrace();
	}
    }

  /**Initialiser le composant*/
  private void jbInit() throws Exception {
      chattersNameList = new Vector();
      out  = new PrintWriter(new JTextAreaWriter(tChatArea), true);
      nick = new PrintWriter(new JTextAreaWriter(tNamesArea), true);
      image1 = new ImageIcon(ChatAgentGUI.class.getResource("/images/toolbars/openFile.gif"));
      image2 = new ImageIcon(ChatAgentGUI.class.getResource("/images/toolbars/closeFile.gif"));
      image3 = new ImageIcon(ChatAgentGUI.class.getResource("/images/toolbars/help.gif"));
      chatterImage = new ImageIcon(ChatAgentGUI.class.getResource("/images/agents/individual_green24.jpg"));
      adminImage = new ImageIcon(ChatAgentGUI.class.getResource("/images/agents/individual_red24.jpg"));
      contentPane = (JPanel) this.getContentPane();
      titledBorder1 = new TitledBorder("");
      contentPane.setLayout(borderLayout1);
      this.setSize(new Dimension(400, 300));
      //  this.setTitle("Chat Agent for MadKit ; GUI code BERTRAND Jean-Gabriel; GUI Design MOHSINE Omar");
      //  setTitle("Chat Agent for MadKit ; GUI code BERTRAND Jean-Gabriel; GUI Design MOHSINE Omar");/*========PB du Titre*/
      statusBar.setText(" ");
      jMenuFile.setText("File");
      jMenuFileExit.setText("Quit");
      jMenuFileExit.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  jMenuFileExit_actionPerformed(e);
	      }
	  });
      jMenuHelp.setText("Help");
      jMenuHelpAbout.setText("About");
      jMenuHelpAbout.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  jMenuHelpAbout_actionPerformed(e);
	      }
	  });
      bOpen.setIcon(image1);
      bOpen.setText("Open");
      bOpen.setToolTipText("Open a file");
      bSave.setIcon(image2);
      bSave.setText("Save");
      bSave.setToolTipText("Close a file");
      bHelp.setIcon(image3);
      bHelp.setText("Help");
      bHelp.setToolTipText("Help");
      lBlank.setText("                      ");
      jCenterP.setLayout(borderLayout2);
      jLeftP.setLayout(gridLayout2);
      //lBottom.setText("BERTRAND J-Gabriel, MOHSINE Omar welcome you to the MadKit\'s chat");
      pCenter.setLayout(gridBagLayout1);

      specifButton(bBan, "   Ban  ", 'B', "Ban one user from the chat", 1,true);
      bBan.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bBan_actionPerformed(e);
	      }
	  });
      specifButton(bTopic, " Topic  ", 'T', "Change a chanel\'s topic", 1, false);
      bTopic.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bTopic_actionPerformed(e);
	      }
	  });
      specifButton(bList, "  List ", 'L', "List the chat\'s chanels available", 1, true);
      bList.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bList_actionPerformed(e);
	      }
	  });
      specifButton(bQuery, " Query  ", 'Q', "Search one chat\'s user", 1, true);
      bQuery.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bQuery_actionPerformed(e);
	      }
	  });
      specifButton(bNames, "  Names ", 'N', "Display chat\'s user\'s names", 1,true);
      bNames.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bNames_actionPerformed(e);
	      }
	  });
      specifButton(bKick, "  Kick  ", 'K', "Kick one user out of the chat", 1, true);
      bKick.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bKick_actionPerformed(e);
	      }
	  });
      specifButton(bNick, "Remove", 'R', "Your chat nickname", 1, true);
      bNick.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  //bNick_actionPerformed();
                  bRetire_actionPerformed();
	      }
	  });
      specifButton(bJoin, "  Join  ", 'J', "Join a chat channel", 1, true);
      bJoin.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bJoin_actionPerformed(e);
	      }
	  });
      lChanelTopic.setText("Default channel's subject");
      lChanelTopic.setHorizontalAlignment(SwingConstants.LEFT);
      lChanelTopic.setBorder(BorderFactory.createRaisedBevelBorder());
      lChanelTopic.setBackground(Color.gray);
      jSplitPane1.setDividerSize(8);
      jSplitPane1.setOneTouchExpandable(true);
      lChanelName.setBackground(Color.gray);
      lChanelName.setBorder(BorderFactory.createRaisedBevelBorder());
      lChanelName.setHorizontalAlignment(SwingConstants.LEFT);
      lChanelName.setHorizontalTextPosition(SwingConstants.LEFT);
      lChanelName.setText("Default channel's name");
      gridLayout4.setColumns(3);
      pMChatUp.setLayout(gridLayout4);
      pMiniChat.setLayout(borderLayout3);
      tWriteArea.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  tWriteArea_actionPerformed(e);
	      }
	  });
      //jToolBar.add(bOpen);
      //jToolBar.add(bSave);
      //jToolBar.add(bHelp);
      //jToolBar.add(lBlank);
      jMenuFile.add(jMenuFileExit);
      jMenuHelp.add(jMenuHelpAbout);
      jMenuBar1.add(jMenuFile);
      jMenuBar1.add(jMenuHelp);
      this.setJMenuBar(jMenuBar1);
      //contentPane.add(jToolBar,  BorderLayout.NORTH);
      contentPane.add(statusBar, BorderLayout.EAST);
      contentPane.add(pCenter,  BorderLayout.CENTER);
      jToolBar1.add(bNick);
      jToolBar1.add(bJoin);
      jToolBar1.add(bList);
      jToolBar1.add(bNames, null);
      jToolBar1.add(bQuery, null);
      jToolBar1.add(bKick, null);
      jToolBar1.add(bBan, null);
      jToolBar1.add(bTopic, null);
      pCenter.add(jToolBar1,      new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
							 ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 75, 8));
      pCenter.add(jTabbedPane2,  new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
							,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 158, 56));
      jTabbedPane2.addTab("defaultChannel", pMiniChat);
      jSplitPane1.add(pChatArea, JSplitPane.TOP);
      jSplitPane1.add(pNames, JSplitPane.BOTTOM);
      pNames.getViewport().add(tNamesArea, null);
      pChatArea.getViewport().add(tChatArea, null);
      pMiniChat.add(tWriteArea,  BorderLayout.SOUTH);
      pMiniChat.add(pMChatUp, BorderLayout.NORTH);
      pMiniChat.add(jSplitPane1, BorderLayout.CENTER);
      pMChatUp.add(lChanelName, null);
      pMChatUp.add(lChanelTopic, null);
      contentPane.add(lBottom, BorderLayout.SOUTH);
      jSplitPane1.setDividerLocation(314);
      //A Ajouter pour que defaultChannel soit visible
      channelOut.put("defaultChannel", out);
      channelPanel.put("defaultChannel", pMiniChat);
      //Essai sur le TabbedPane
      //addNewChatPanel("Gabs");
      //removeChatPanel("Gabs");

      //1ere chose à faire au lancement
      allowOnlyNickname();
      //println("First of all, click on \"Nickname\" button");
      bNickname.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
		  bNickname_actionPerformed();
	      }
	  });

  }

  public void addNewChatPanel(String panelName) {
          final String pN = panelName;
          JPanel pMiniChatBuild = new JPanel();
          BorderLayout borderLayoutBuild = new BorderLayout();
          JTextField tWriteAreaBuild = new JTextField();
          JPanel pMChatUpBuild = new JPanel();
          JLabel lChanelNameBuild = new JLabel();
          JLabel lChanelTopicBuild = new JLabel();
          JSplitPane jSplitPaneBuild = new JSplitPane();
          JScrollPane pChatAreaBuild = new JScrollPane();
          JScrollPane pNamesBuild = new JScrollPane();
          final JTextArea tNamesAreaBuild = new JTextArea();
          JTextArea tChatAreaBuild = new JTextArea();
          lChanelNameBuild.setText(panelName);
          lChanelNameBuild.setHorizontalAlignment(SwingConstants.LEFT);
          lChanelNameBuild.setBorder(BorderFactory.createRaisedBevelBorder());
          lChanelNameBuild.setBackground(Color.gray);
          lChanelTopicBuild.setText("unknown topic");
          lChanelTopicBuild.setHorizontalAlignment(SwingConstants.LEFT);
          lChanelTopicBuild.setBorder(BorderFactory.createRaisedBevelBorder());
          lChanelTopicBuild.setBackground(Color.gray);
          final PrintWriter outBuild;
          final PrintWriter nickBuild;
          jSplitPaneBuild.setDividerSize(8);
          jSplitPaneBuild.setOneTouchExpandable(true);

          jSplitPaneBuild.add(pChatAreaBuild, JSplitPane.TOP);
          jSplitPaneBuild.add(pNamesBuild, JSplitPane.BOTTOM);
          pNamesBuild.getViewport().add(tNamesAreaBuild, null);
          pChatAreaBuild.getViewport().add(tChatAreaBuild, null);
          outBuild  = new PrintWriter(new JTextAreaWriter(tChatAreaBuild), true);
          nickBuild = new PrintWriter(new JTextAreaWriter(tNamesAreaBuild), true);

          tWriteAreaBuild.addActionListener(new ActionListener()  {
	      public void actionPerformed(ActionEvent e) {
          tWriteArea_actionPerformed(e, outBuild, pN, nickBuild, tNamesAreaBuild);
	      }
	  });
          jSplitPaneBuild.setDividerLocation(314);
          pMChatUpBuild.setLayout(gridLayout4);
          pMChatUpBuild.add(lChanelNameBuild, null);
          pMChatUpBuild.add(lChanelTopicBuild, null);
          pMiniChatBuild.setLayout(borderLayoutBuild);
          pMiniChatBuild.add(tWriteAreaBuild,  BorderLayout.SOUTH);
          pMiniChatBuild.add(pMChatUpBuild, BorderLayout.NORTH);
          pMiniChatBuild.add(jSplitPaneBuild, BorderLayout.CENTER);
          jTabbedPane2.addTab(panelName, pMiniChatBuild);

          channelOut.put(panelName, outBuild);
          channelPanel.put(panelName, pMiniChatBuild); //Obligatoire pour pv enlever le tabbedpan après.
          channelNickArea.put(panelName, nickBuild);
          agentChat.createGroup(true,"Chat",panelName,null,null);
          agentChat.requestRole("Chat",panelName,"chatter",null);
          //agentChat.addCouple(panelName, new Channel(panelName, "No topic yet.", nbrUsers, agentChat.getChatter()), nbrUsers);
  }

  public void removeChatPanel(String n) {
          JPanel tmp;
          if(channelPanel.containsKey((Object) n)) {
                  tmp = (JPanel) channelPanel.get(n);
                  jTabbedPane2.remove(tmp);
                  channelOut.remove(n);
                  channelPanel.remove(n);
                  agentChat.sendRemoveChannel(n);
          }
  }
  public void removeChatPanelViewOnly(String n) {
          JPanel tmp;
          if(channelPanel.containsKey((Object) n)) {
                  tmp = (JPanel) channelPanel.get(n);
                  jTabbedPane2.remove(tmp);
                  channelOut.remove(n);
                  channelPanel.remove(n);
          }
  }

  public boolean isExistChatPanel(String n) {
          boolean b = channelOut.containsKey(n);
          return b;
  }

  public PrintWriter givePrintWriterLinkedWithTabbedPane(String paneName) {
          PrintWriter tmp=null;
          if(!channelOut.isEmpty())
                  tmp = (PrintWriter) channelOut.get(paneName);
          return tmp;
  }


  public void addNicknameButton() {
          if((agentChat.getChatterRole(agentChat.getChatter())).equals("Admin"))
                  bNickname.setIcon(adminImage);
          else
                  bNickname.setIcon(chatterImage);
          bNickname.setText(agentChat.getChatter());
          bNickname.setToolTipText("Your nickname");
          jToolBar1.add(bNickname);
  }


  /**specify the buttons settings*/
  public void specifButton(JButton buttonName, String txt, char mnemo, String toolTip, int borderType, boolean enable) {
      buttonName.setText(txt);
      buttonName.setMnemonic(mnemo);
      buttonName.setToolTipText(toolTip);
      buttonName.setEnabled(enable);
      if(borderType==1) {
      buttonName.setBorder(BorderFactory.createRaisedBevelBorder());}
  }
  /**Allow the user, only to set is nickname*/
  public void allowOnlyNickname() {
       bNick.setEnabled(true);
       bKick.setEnabled(false);
       bList.setEnabled(false);
       bTopic.setEnabled(false);
       bJoin.setEnabled(false);
       bNames.setEnabled(false);
       bQuery.setEnabled(false);
       bBan.setEnabled(false);
  }
  /**Set the button for an normal chatter*/
  public void allowChatterRight() {
       bNick.setEnabled(true);
       bKick.setEnabled(false);
       bList.setEnabled(true);
       bTopic.setEnabled(true);
       bJoin.setEnabled(true);
       bNames.setEnabled(true);
       bQuery.setEnabled(true);
       bBan.setEnabled(false);
  }
  /**Allow the user to use everything, chatter is an Admin*/
  public void allowEverything() {
        bNick.setEnabled(true);
        bKick.setEnabled(true);
        bList.setEnabled(true);
        bTopic.setEnabled(true);
        bJoin.setEnabled(true);
        bNames.setEnabled(true);
        bQuery.setEnabled(true);
        bBan.setEnabled(true);
  }

  /**Opération Fichier | Quitter effectuée*/
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    //System.exit(0); //PB: CA TUE MADKIT AUSSI !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  }
  /**Opération Aide | A propos effectuée*/
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    ChatAgentGUI_AboutBox dlg = new ChatAgentGUI_AboutBox(/*this*/);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    //dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(false);
    dlg.show();
  }

  public void bJoin_actionPerformed(ActionEvent e) {
    ChatAgentGUI_JoinBox jap = new ChatAgentGUI_JoinBox(agentChat);
    Dimension japSize = jap.getPreferredSize();
    Dimension frameSize = getSize();
    Point loc = getLocation();
    //jap.setLocation((frameSize.width - japSize.width) / 2 + loc.x, (frameSize.height - japSize.height) / 2 + loc.y);
    jap.setModal(false);
    jap.show();
  }

   public void bQuery_actionPerformed(ActionEvent e) {
    ChatAgentGUI_QueryBox jap = new ChatAgentGUI_QueryBox(agentChat);
    Dimension japSize = jap.getPreferredSize();
    Dimension frameSize = getSize();
    Point loc = getLocation();
    //jap.setLocation((frameSize.width - japSize.width) / 2 + loc.x, (frameSize.height - japSize.height) / 2 + loc.y);
    jap.setModal(false);
    jap.show();
  }

   public void bKick_actionPerformed(ActionEvent e) {
    ChatAgentGUI_KickBox jap = new ChatAgentGUI_KickBox(agentChat);
    Dimension japSize = jap.getPreferredSize();
    Dimension frameSize = getSize();
    Point loc = getLocation();
    //jap.setLocation((frameSize.width - japSize.width) / 2 + loc.x, (frameSize.height - japSize.height) / 2 + loc.y);
    jap.setModal(false);
    jap.show();
  }

   public void bBan_actionPerformed(ActionEvent e) {
    ChatAgentGUI_BanBox jap = new ChatAgentGUI_BanBox(agentChat);
    Dimension japSize = jap.getPreferredSize();
    Dimension frameSize = getSize();
    Point loc = getLocation();
    //jap.setLocation((frameSize.width - japSize.width) / 2 + loc.x, (frameSize.height - japSize.height) / 2 + loc.y);
    jap.setModal(false);
    jap.show();
  }

   public void bTopic_actionPerformed(ActionEvent e) {
    ChatAgentGUI_TopicBox jap = new ChatAgentGUI_TopicBox(agentChat);
    Dimension japSize = jap.getPreferredSize();
    Dimension frameSize = getSize();
    Point loc = getLocation();
    //jap.setLocation((frameSize.width - japSize.width) / 2 + loc.x, (frameSize.height - japSize.height) / 2 + loc.y);
    jap.setModal(false);
    jap.show();
  }

   public void bList_actionPerformed(ActionEvent e) {
    ChatAgentGUI_ListBox jap = new ChatAgentGUI_ListBox(agentChat);
    Dimension japSize = jap.getPreferredSize();
    Dimension frameSize = getSize();
    Point loc = getLocation();
    //jap.setLocation((frameSize.width - japSize.width) / 2 + loc.x, (frameSize.height - japSize.height) / 2 + loc.y);
    jap.setModal(false);
    jap.show();
  }

   public void bNames_actionPerformed(ActionEvent e) {
    ChatAgentGUI_NamesBox jap = new ChatAgentGUI_NamesBox(agentChat);
    Dimension japSize = jap.getPreferredSize();
    Dimension frameSize = getSize();
    Point loc = getLocation();
    //jap.setLocation((frameSize.width - japSize.width) / 2 + loc.x, (frameSize.height - japSize.height) / 2 + loc.y);
    jap.setModal(false);
    jap.show();
  }

   public void bNick_actionPerformed() {
    allowEverything();
    ChatAgentGUI_NickBox jap = new ChatAgentGUI_NickBox(agentChat, nick);
    Dimension japSize = jap.getPreferredSize();
    Dimension frameSize = getSize();
    Point loc = getLocation();
    //jap.setLocation((frameSize.width - japSize.width) / 2 + loc.x, (frameSize.height - japSize.height) / 2 + loc.y);
    jap.setModal(true);
    jap.show();
  }
   public void bRetire_actionPerformed() {
    ChatAgentGUI_RetireBox jap = new ChatAgentGUI_RetireBox(agentChat);
    Dimension japSize = jap.getPreferredSize();
    Dimension frameSize = getSize();
    Point loc = getLocation();
    //jap.setLocation((frameSize.width - japSize.width) / 2 + loc.x, (frameSize.height - japSize.height) / 2 + loc.y);
    jap.setModal(true);
    jap.show();
  }
  public void bNickname_actionPerformed() {
          if(agentChat.getDebug()) {
                  System.out.println("START DISPLAY");
                  agentChat.printnickChatter();
                  //agentChat.printnameChannel();
                  System.out.println("END DISPLAY");
          }
  }


   public void tWriteArea_actionPerformed(ActionEvent e) {
           ChatAgent a = getMyAgent();
           String s = ((JTextField) e.getSource()).getText();
           if(!s.equals("#CHATLIST")) {
                   ((ChatAgent) a).sendChatMessage(s);
                   ((JTextField) e.getSource()).setText("");
                   stdout().println("me> " + s);
           }
           else {sendChattersNameList();}
   }

   public void tWriteArea_actionPerformed(ActionEvent e, PrintWriter pw, String chName, PrintWriter nw, JTextArea ta) {
           ChatAgent a = getMyAgent();
           String s = ((JTextField) e.getSource()).getText();
           if(!s.equals("#CHATLIST")) {
                   ((ChatAgent) a).sendChatMessage(s,chName);
                   ((JTextField) e.getSource()).setText("");
                   pw.println("me> " + s);
           }
           else {sendChattersNameList();}
           //MAJ champs des Nickname du panel actif
           printNickList(nw, chName, ta);
   }

   public void printNickList(PrintWriter nw, String ch, JTextArea ta) {
           //Mettre a blanc la zone de texte
           ta.setText("");
           String [] tabNick = agentChat.getNamesList();
           Vector tabNickOK = new Vector();
           for(int i=0; i<tabNick.length;i++) {
                   if(agentChat.isChatterInChannel(tabNick[i], ch))
                           tabNickOK.add(tabNick[i]);
           }
           for (int i=0; i<tabNickOK.size(); i++) {
                   nw.println(tabNickOK.elementAt(i));
           }
   }

  /**Remplacé, ainsi nous pouvons sortir quand la fenêtre est fermée*/
  protected void processWindowEvent(WindowEvent e) {
//    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }



  public void addChatterName(String s) {
          printNickln(s);
          chattersNameList.addElement(s);
  }

  public void resetNamesArea() {
          tNamesArea.setText("");
  }

  public void sendChattersNameList() {
          String ch = "";
          for(int i=0; i<=chattersNameList.size()-1 ;i++) {
                  ch=ch+((String) chattersNameList.elementAt(i))+"["+getMyAgent().getChatter()+"]"+" ";
          }
          //System.out.println("Liste des chatters: "+ch);
          getMyAgent().sendChatMessage("#CHATLIST "+ch);
  }

   void sendSmiley() {
           //envoyer un msg type object ou alors txt disant de chager un gif prédéfinit
   }
}










