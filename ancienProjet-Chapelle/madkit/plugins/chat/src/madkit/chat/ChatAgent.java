/*
* ChatAgent.java - ChatAgent, a chat application for MadKit
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


import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;
import madkit.messages.ActMessage;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  Messages struct, used to send the chatter list.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

class ChatterListMsg extends Message {
        Vector list = new Vector();
        String [] tab = null;
        String chatter = null;

        String getChatter() {return chatter;}
        String [] getList() {return tab;}
        ChatterListMsg(String from, String [] tmp) {
                chatter = from;
                tab = tmp;
        }
}//fin ChatterListMsg

/**
 * Titre :        Chat Agent for MadKit
 * Description :  Message struct, used to build chat's messages..
 * Copyright :    Copyright (c) 2002
 * Société :      UMII
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

class ChatMessage extends Message {
    /**Chat's message content*/
    String content = null;
    /**Chatter name*/
    String chatter = null;
    /**Channel name*/
    String channel = null;

    /**Get the chat's message content*/
    String getString()
    {
        return content;
    }

    /**Build the String of the chat's message*/
    public String toString()
    {
        if (chatter == null)
            return content;
        else
            return chatter + "> " + content;
    }

    /**Set the chat's message content to s*/
    ChatMessage(String s){
        content=s;
    }
    /**Set the chat's message content to s and the chatter's name to from*/
    ChatMessage(String from, String s){
        content=s;
        chatter = from;
    }
    /**Set the chat's message content to s, chatter's name to from and channel's name to channel*/
    ChatMessage(String from, String name, String s){
        content=s;
        chatter = from;
        channel = name;
    }
    /**Return the chatter's name*/
    String getChatter(){return chatter;}
     /**Return the chatter's name*/
    String getChannel(){return channel;}

}//fin ChatMessage


/**
 * Titre :        Chat Agent for MadKit
 * Description :  The Agent itself, used to control every pieces of the project.
 * Copyright :    Copyright (c) 2002
 * Société :      UMII
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */


public class  ChatAgent extends Agent {
    Hashtable nickChatter = new Hashtable();
    Hashtable nameChannel = new Hashtable();
    ChatAgentGUI display;
    String chatter;
    AgentAddress[] adminList;
    String myCurrentRole="";
    String myCurrentChannel="";
    boolean kicked = false;
    String [] tabChatter = null;

  public void setChatter(String s) {chatter=s;}
  public String getChatter() {return chatter;}
  public String [] getTabChatter() {return tabChatter;}

  public void initGUI()
  {
    display = new ChatAgentGUI(this);
	setGUIObject(display);
  }


  public void activate() {
          /* Community == "Chat" (nothing else than "Chat")
             Groupe == "Channel"  (Anything you want)
             Role == "Role" (Admin/Chatter) */
    int r = createGroup(true,"Chat","defaultChannel",null,null);
    if(r==-1) System.out.println("Groupe already exist.");
    int r2;
    r2 = requestRole("Chat","defaultChannel","chatter",null);
    myCurrentRole="chatter"; myCurrentChannel="defaultChannel";
    switch(r2) {
            case -1:System.out.println("[Error] Accesss denied.");
            case -2:System.out.println("[Error] Role already handle by this agent.");
            case -3:System.out.println("[Error] The group does not exist.");
            case -4:System.out.println("[Error] The community does not exist.");
    }
  }

  public void live(){
        firstThingsToDo();
	while(true) {
		Message m = waitNextMessage();
                handleMessage(m);
	}
  }

  public void firstThingsToDo() {
          //Ask for a Nickname and E-mail
          display.bNick_actionPerformed();
          //Look for an administrator
          if(doesAdminExist()) {
                  //It could exist one or more admin, almost once by chanel.
                  for(int i=0; i< adminList.length;i++){
  			if (!adminList[i].equals(getAddress()))
  				sendMessage(adminList[i], new ChatMessage(chatter, "#NICKNAME "+chatter+" "+ getChatterEmailInString(chatter)+" "+getChatterRole(chatter)));
                                sendMAJNbrUsrersOfChannel("defaultChannel");
                  }
          }
          else {
                  requestRole("Chat","defaultChannel","Admin",null);
                  setChatterRole("Admin");
                  upDateNickname();
                  allowFunctions();
          }
  }


  public void end() {
          //I am an Admin
          if(getDebug())  {//true if madkit's debug mode is on.
                  System.out.println("\t["+chatter+"] isAdmin = "+isAdmin("defaultChannel", chatter));
                  System.out.println("\t["+chatter+"]");
                  //printnameChannel();
          }
          if(isAdmin("defaultChannel", chatter)) {
                  int i=0;
                  //Send to a chatter the order to become an Admin
                  AgentAddress[] agList = getAgentsWithRole("Chat","defaultChannel","chatter");
                  if(agList.length >1) {
                          while(agList[i]==getAddress()) {
                                  i++;
                          }
                          //System.out.println("["+chatter+"] envoi #NEWADMIN à : "+agList[i]+" = "+(getChatterThanksAgentAddress(agList[1])).getNick());
                          sendMessage(agList[i], new ChatMessage(chatter,"#NEWADMIN"));
                         //DEBUT DE LA MODIF CI DESSOUS
//                         leaveRole("Chat", "defaultChannel", "Admin");
//                          String [] tab = getMyGroups("Chat");
//                          for(int j=0; j<tab.length; j++) {
//                                  leaveRole("Chat", tab[j], "chatter");
//                                  System.out.println("Je suis admin et ds channel: "+tab[i]);
//                                  sendMAJNbrUsrersOfChannel(tab[j]);
//                          }
                          //IMPORTANT faire sendMAJNbrUsrersOfChannel(String chName)
                          //pour tt les channels auxquel apparttient le chatter
                          //MAIS AVT FAIRE UN LEAVEROLE("Admin")
                          endChannel(agList[i]);
                  }
          }
          //I am not an Admin
          else {
                  AgentAddress agAdmin = getAgentWithRole("Chat","defaultChannel","Admin");
                  endChannel(agAdmin);
                  sendMessage(agAdmin, new ChatMessage(chatter, "#REMOVENICK "+chatter));
          }
  }

  public void endChannel(AgentAddress newAdmin) {
          Chatter tmpCh;
          if(nickChatter.containsKey(chatter)) {
                  tmpCh = (Chatter) nickChatter.get(chatter);
                  Vector tmpVect = tmpCh.getChannel();
                  for(int i=1; i<tmpVect.size(); i++) {
                          String chName = (String)tmpVect.elementAt(i);
                          int nbUsers = getChannelNbrUsers(chName);
                          if(nbUsers==1)
                                  nameChannel.remove(chName);
                          else
                                  setChannelNbrUsers(chName, nbUsers-1);
                  }
                  int nbUsersDef = getChannelNbrUsers("defaultChannel");
                  setChannelNbrUsers("defaultChannel", nbUsersDef-1);
                  sendChannelHashtableObject(newAdmin);
                  //System.out.println("nameChannel de ["+chatter+"] :");
                  //printnameChannel();
                  Chatter toto = getChatterThanksAgentAddress(newAdmin);
                  //System.out.println("nameChannel envoyé à : "+toto.getNick());
          }
  }

  /**Say if chatter is the channel's admin*/
  public boolean isAdmin(String chan_, String chat_) {
          boolean b=false;
          try {
          Channel ch = (Channel) nameChannel.get((Object) chan_);
          String adm = ch.getAdminName();
          if(adm.equals(chat_)) {
                  b=true;
          }
          }
          catch(Exception e) {System.out.println("[Erreur] You attempt to create a chatter with no name !!");}
          return b;
  }

  public void println(String s){
  	display.stdout().println(s);
    }

 public void handleMessage(Message m){
  	String s=null;
  	if (m instanceof ChatMessage) {
  		s=((ChatMessage)m).getString();
                String c = ((ChatMessage)m).getChatter();
                String channel = ((ChatMessage)m).getChannel();
                if(!filterChatMessage(s,m)) {
                        if(channel != null) {
                                if(display.isExistChatPanel(channel))
                                        ((ChatAgentGUI)display).printlnOnChannel(channel, c+"> "+ s);
                        }
                        if (channel==null && c == null){
                                ((ChatAgentGUI)display).println(m.getSender()+"> " + s);
                        }
                        if (channel==null && c != null) {
                                ((ChatAgentGUI)display).println(c+"> " + s);
                        }
                }
        }
        else  if (m instanceof StringMessage){
  		s=((StringMessage)m).getString();
  		((ChatAgentGUI)display).println("<< message from "+m.getSender()+">> \n" + s);
  	}
        if (m instanceof ActMessage) {
                //System.out.println("\t["+chatter+"] reçoit un ACTMessage");
                if(!(getChatterRole(chatter)).equals("Admin")) {
                        ActMessage actM = (ActMessage) m;
                        if(actM.getObject() instanceof Hashtable) {
                                if((actM.getAction()).equals("nick")) {
                                        Hashtable tmp = (Hashtable) actM.getObject();
                                        nickChatter = tmp;
                                        upDateNickname();
                                        //System.out.println("["+chatter+"] fin du traitement ACTMessage Hashtable");
                                }
                                if((actM.getAction()).equals("channel")) {
                                        //System.out.println("["+chatter+"] a reçut nameChannel hashtable");
                                        Hashtable tmp = (Hashtable) actM.getObject();
                                        nameChannel = tmp;
                                }
                        }
                }
        }
        if (m instanceof ChatterListMsg) {
                        //System.out.println("\t\t["+chatter+"] Début du traitement ChatterListMsg");
                        String [] tab = ((ChatterListMsg) m).getList();
                        for(int i=0 ; i<tab.length ; i++)
                              //System.out.println("\t[CLMsg] : tab["+i+"]= "+tab[i]);
                        tabChatter = tab;
                        //System.out.println("["+chatter+"] fin du traitement ChatterListMsg");
                        }
 }


  public void printTabString( String [] tabStr) {
          System.out.println("printTabString ACTIVE est tabStr.length = "+tabStr.length);
          for (int i=0; i<tabStr.length ; i++) {
          System.out.println("["+i+"] :"+tabStr[i]);}
  }

  /**Allow or not to use chat functions if the chatter is normal or Admin*/
  public void allowFunctions() {
          if(!(getChatterEmailInString(getChatter())).equals("-1")) {
              if((getChatterRole(getChatter())).equals("Admin"))
                      display.allowEverything();
              else display.allowChatterRight();
      }
  }

  /**Answer true if an admin is detected, else false*/
  public boolean doesAdminExist() {
          AgentAddress[] agList = getAgentsWithRole("Chat","defaultChannel","Admin");
          if(agList.length==0)
                  return false;
          else {
                  adminList = agList;
                  return true;
          }
  }

  public void sendChatMessage(String s){
          if(!kicked) {
  		AgentAddress[] agList = getAgentsWithRole("Chat","defaultChannel","chatter");
  		for(int i=0; i< agList.length;i++){
  			if (!agList[i].equals(getAddress()))
  				sendMessage(agList[i], new ChatMessage(chatter,s));
  		}
          }
  }
  public void sendChatMessage(String s, String channel){
          if(!kicked) {
  		AgentAddress[] agList = getAgentsWithRole("Chat","defaultChannel","chatter");
  		for(int i=0; i< agList.length;i++){
  			if (!agList[i].equals(getAddress()))
  				sendMessage(agList[i], new ChatMessage(chatter,channel,s));
  		}
          }
  }


  /**Allows ChatAgent to filter its own command, return true if the message is a command*/
  public boolean filterChatMessage(String s,Message m) {
          try {
                  //System.out.println("Txt traité par le filtre: "+s);
                  StringTokenizer st = new StringTokenizer(s);
                  int cpt = st.countTokens(); //System.out.println("countTokens="+cpt);
                  while (st.hasMoreTokens()) {
                          String command = st.nextToken();
                          if(command.equals("#NICK") && cpt<=2) {
                                  //Chatter's name arrive
                                  String newChatter = st.nextToken();
                                  display.addChatterName(newChatter);
                                  //System.out.println("chatter= "+newChatter+"=>ret true");
                                  return true;
                          }
                          if(command.equals("#GETCHATLIST")) {
                                  //Receiving the order to send the ChatList
                                  display.sendChattersNameList();
                                  //System.out.println("Received order to send Chatters names list");
                                  return true;
                          }
                          if(command.equals("#CHATLIST")) {
                                  //Receiving the chattters names list and adding them.
                                   while (st.hasMoreTokens()) {
                                        String nName= st.nextToken();
                                        //System.out.println("Receiving: "+nName+" to add");
                                        display.addChatterName(nName);
                                  }
                                  return true;
                          }
                          if(command.equals("#NICKNAME")) {
                                  //Admin receiving a new chatter ID, adding it to the nickChatter hashtable and sending the complete hashtable
                                  while (st.hasMoreTokens()) {
                                        String nickname= st.nextToken();
                                        String email = st.nextToken();
                                        String role = st.nextToken();
                                        //String channel = st.nextToken();
                                        //System.out.println("Nickname= "+nickname);
                                        //System.out.println("email= "+email);
                                        //System.out.println("role= "+role);
                                        Chatter ch = new Chatter(m.getSender(),nickname ,email, role, "NULL"/*channle*/);
                                        addCouple(nickname, ch);
                                  }
                                  sendToAllNoneAdmin();
                                  upDateNickname();
                                  allowFunctions();
                                  return true;
                          }
                          if(command.equals("#REMOVENICK")) {
                                  String nickname = st.nextToken();
                                  removeChatter(nickname);
                                  sendToAllNoneAdmin();
                                  upDateNickname();
                                  if((getChatterRole(chatter)).equals("Admin")) {
                                          sendChatMessage(nickname+ " is gone :) [Admin]");
                                  }
                                  return true;
                          }
                          if(command.equals("#NEWADMIN")) {
                                  String adminNick = ((ChatMessage)m).getChatter();
                                  //System.out.println("\n"+chatter+" a recu #NEWADMIN");
                                  //System.out.println("Admin envoyant le msg #NEWADMIN = "+adminNick);
                                  printNickChatter();
                                  //leaveRole("Chat","defaultChannel","chatter");
                                  requestRole("Chat","defaultChannel","Admin",null);
                                  setChatterRole("Admin");
                                  setChannelRole("defaultChannel");
                                  removeChatter(adminNick);
                                  sendToAllNoneAdmin();
                                  allowFunctions();
                                  upDateNickname();
                                  return true;
                          }
                          if(command.equals("#GETTABLES")) {
                                  sendToAllNoneAdmin();
                                  upDateNickname();
                                  return true;
                          }
                          if(command.equals("#QUERRY")){
			      while (st.hasMoreTokens()) {
				  String nickname= st.nextToken();
                                  String autre = st.nextToken();
				  //System.out.println("Nickname with Whom you want to chat is "+nickname);
				  AgentAddress ad = getChatterAddress(nickname);
				  this.launchAgent(new PrivateAgent(ad,autre, nickname),autre,true);       //nickname,true);

				}
			      return true;
			  }
                          if(command.equals("#KICK")) {
                                  broadcastMessage("Chat","defaultChannel","chatter", new ChatMessage(chatter,"I have been kicked off by the chat administrator"));
                                  ((ChatAgentGUI)display).println(" You have been KICKED!! U R NOW ALONE !!");
                                  leaveRole("Chat","defaultChannel","chatter");
                                  leaveGroup("Chat","defaultChannel");
                                  kicked = true;
                                  return true;
                          }
                          if(command.equals("#BAN")) {
                                  killAgent(this);
                                  return true;
                          }
                          if(command.equals("#TOPIC")) {
                                  String na = st.nextToken();
                                  String to = st.nextToken();
                                  display.lChanelTopic.setText(to);
                                  display.lChanelName.setText(na);
                                  return true;
                          }
                          if(command.equals("#UNIQUE")) {
                                  //System.out.println("["+chatter+"] a reçut #UNIQUE");
                                  String ni = ((ChatMessage)m).getChatter();
                                  AgentAddress senderAdr = m.getSender();
                                  //sendAdminHashtableObject(agAdr);
                                  sendMessage(senderAdr, new ChatterListMsg(chatter, getNamesList()));
                                  //System.out.println("["+chatter+"] fin de #UNIQUE");
                                  return true;
                          }
                          if(command.equals("#ADDNEWCHANNEL")) {
                                  String ni = ((ChatMessage)m).getChatter();
                                  String chName = ""; int i =0;
                                  while(st.hasMoreTokens()) {
                                          String tmp = st.nextToken();
                                          if(i==0)
                                                  chName = chName + tmp;
                                          else
                                                  chName = chName +" "+ tmp;
                                          i++;
                                  }
                                  Object [] tabNbrUsers = getAgentsWithRole("Chat",chName,"chatter");
                                  int nbrUsers = tabNbrUsers.length;
                                  addCouple(chName, new Channel(chName, "No topic yet.", nbrUsers, ni), nbrUsers);
                                  sendToAllNoneAdmin();
                                  return true;
                          }
                          if(command.equals("#MAJNBRUSRCHANNEL")) {
                                  String chName = "";  int i =0;
                                  while(st.hasMoreTokens()) {
                                          String tmp = st.nextToken();
                                          if(i==0)
                                                  chName = chName + tmp;
                                          else
                                                  chName = chName +" "+ tmp;
                                          i++;
                                  }
                                  Object [] tabNbrUsers = getAgentsWithRole("Chat",chName,"chatter");
                                  int nbrUsers = tabNbrUsers.length;
                                  String adminName = getChannelAdminName(chName);
                                  if(nbrUsers!=0)
                                          addCouple(chName, new Channel(chName, "No topic yet.", nbrUsers, adminName), nbrUsers);
                                  sendToAllNoneAdmin();
                                  return true;
                          }
                          if(command.equals("#REMOVECHANNEL")) {
                                  String chName = "";  int i =0;
                                  while(st.hasMoreTokens()) {
                                          String tmp = st.nextToken();
                                          if(i==0)
                                                  chName = chName + tmp;
                                          else
                                                  chName = chName +" "+ tmp;
                                          i++;
                                  }
                                  removeChannel(chName);
                                //  System.out.println("["+chatter+"] a effectué un removeChannel("+chName+")");
                                  return true;
                          }

                          else {return false;}
                  }
          }
          catch(Exception e) {
                  System.out.println("Erreur lors de l'appel de filterChatMessage() ds la classe ChatAgent :"+e);
                  e.printStackTrace();
	}
        return false;
  }


  /**Send the hashtable thanks to the optimized method to all the none "Admin" chatter*/
  public void sendToAllNoneAdmin() {
          //if(myRole.equals("Admin") marche mieux ?
          if(getChatterRole(chatter).equals("Admin")) {
                                          AgentAddress [] tab = getChatterAddressList();
                                          for(int i=0; i<tab.length; i++) {
                                                  sendAdminHashtableObject(tab[i]);
                                                  sendChannelHashtableObject(tab[i]);
                                          }
                                  }
  }

  /**Not optimized method, send the content of the nickChatter hashtable*/
  public void sendAdminHashtable(AgentAddress a) {
          Enumeration e = nickChatter.elements();
          while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                  if (!a.equals(getAddress())) {
                          String email = ch.getEmail();
                          String role = ch.getRole();
                          String n = ch.getNick();
                          sendMessage(a, new ChatMessage(chatter, "#NICKNAME "+n+" "+ email+" "+role));
                  }
          }
  }
  /**Optimized method, send the object : nickChatter hashtable*/
  public void sendAdminHashtableObject(AgentAddress a) {
          sendMessage(a, new ActMessage("nick", nickChatter));
  }
  /**Optimized method, send the object : nickChatter hashtable*/
  public void sendChannelHashtableObject(AgentAddress a) {
          sendMessage(a, new ActMessage("channel", nameChannel));
  }

  /**Update the chatter tNames textArea thanks to the nickChatter hashtable*/
  public void upDateNickname() {
          display.resetNamesArea();
          Enumeration e = nickChatter.elements();
          while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                  String n = ch.getNick();
                  String role = ch.getRole();
                  if(role.equals("Admin")) {
                          display.addChatterName("@ "+n);
                  }
                  else display.addChatterName(n);
          }
          display.addNicknameButton();
  }
  /**Print on the std output the nickChatter hashtable content*/
  public void printnickChatter() {
          System.out.println("\n\t\tContenu de la hashtable nickChatter de "+chatter);
          Enumeration e = nickChatter.elements();
          while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                  String n = ch.getNick();
                  String role = ch.getRole();
                  String email = ch.getEmail();
                  AgentAddress agAdr= ch.getAgentAddress();
                  String s="";
                  for(int i=0 ; i<ch.sizeOfChannelVect(); i++)
                          s = s +" "+ ch.getChannel(i);
                  System.out.println("\tNick= "+n+"\tRole= "+role+"\tEmail= "+email+"\tAgent Address= "+agAdr+"\tListOfChannel= "+s);
          }
  }

  /**Print on the std output the nameChannel hashtable content*/
  public void printnameChannel() {
          System.out.println("\n\t\tContenu de la hashtable nameChannel de "+chatter);
          Enumeration e = nameChannel.elements();
          while(e.hasMoreElements()) {
                  Channel ch = (Channel) e.nextElement();
                  String n = ch.getName();
                  String top_ = ch.getTopic();
                  int nbr = ch.getNbrUsers();
                  String adminN = ch.getAdminName();
                  System.out.println("\tName= "+n+"\tTopic= "+top_+"\tNbreChatter= "+nbr+"\tNom Admin= "+adminN);
          }
  }

  /**Add a couple (Nickname,Chatter) in the hashtable nickChatter*/
  public boolean addCouple(String nick, Chatter ch ) {
          boolean b = false;
          if(!nickChatter.containsKey(nick)) {
                  nickChatter.put(nick, ch);
                  b = true;
          }
          return b;
  }
  /**Set the role of the chatter to "admin" or "chatter" */
  public void setChatterRole(String r) {
          Chatter ch;
          if(!nickChatter.isEmpty()) {
                  Object o = nickChatter.get(chatter);
                  ch = (Chatter) o;
                  ch.setRole(r);
          }
  }
  /**Set the role of the chatter to "admin" or "chatter" */
  public void addChannelForChatter(String newCh) {
          Chatter ch;
          if(nickChatter.containsKey(chatter)) {
                  Object o = nickChatter.get(chatter);
                  ch = (Chatter) o;
                  ch.setChannel(newCh);
          }
          //printnickChatter();
  }
  /**Get the role of the chatter to "admin" or "chatter" */
  public String getChatterRole(String n) {
          Chatter ch; String tmp="";
          if(!nickChatter.isEmpty()) {
                  Object o = nickChatter.get(n);
                  ch = (Chatter) o;
                  tmp = ch.getRole();
                  return tmp;
          }
          else return (tmp="-1");
  }
  /**Print the nickChatter hashtable on the stdoutput*/
  public void printNickChatter() {
          int i=0; String nick_,email_,role_,channel_;
          System.out.println("Hashtable nickChatter content :");
          Enumeration e = nickChatter.elements();
          while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                  nick_ = ch.getNick();
                  email_ = ch.getEmail();
                  role_ = ch.getRole();
                  channel_ = ch.getChannel(0);
                  System.out.println("\t["+i+"] : "+"\t"+nick_+"\t"+email_+"\t"+role_+"\t"+channel_);
                  i++;
          }
  }
  /**Get the AgentAddress from the chatter's nickname*/
  public AgentAddress getChatterAddress(String nickname) {
        AgentAddress a;
        if (!nickChatter.isEmpty()) {
                Object o = nickChatter.get(nickname);
                Chatter ch = (Chatter) o;
                a = (AgentAddress) ch.getAgentAddress();
        }
        else a = null;
        return a;
  }
  /**Get a AgentAddress list of the chatter*/
  public AgentAddress [] getChatterAddressList() {
        int i=0;
        AgentAddress [] tab = new AgentAddress[nickChatter.size()];
        Enumeration e = nickChatter.elements();
        while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                  AgentAddress n = ch.getAgentAddress();
                  tab[i]=n; i++;
        }
        return tab;
  }
   /**Get the AgentAddress from the chatter's nickname, return a String*/
  public String getChatterAddressInString(String nickname) {
        AgentAddress a; String tmp="";
        if (!nickChatter.isEmpty()) {
                Object o = nickChatter.get((Object) nickname);
                Chatter ch = (Chatter) o;
                a = (AgentAddress) ch.getAgentAddress();}
        else a = null;
        if(a==null) {tmp="-1";} else {tmp=a.toString();}
        return tmp;
  }
  /**Get the E-Mail from the chatter's nickname, return a String*/
  public String getChatterEmailInString(String nickname) {
        String tmp="essai";
       if (!nickChatter.isEmpty()) {
                Object o = nickChatter.get((Object) nickname);
                Chatter ch = (Chatter) o;
                tmp = ch.getEmail();}
        else tmp = null;
        if(tmp==null) {tmp="-1";}
        return tmp;
  }
  /**Get the E-Mail from the chatter's nickname, return a String*/
  public String getChatterNickInString(String nickname) {
        String tmp="";
       if (!nickChatter.isEmpty()) {
                Object o = nickChatter.get((Object) nickname);
                Chatter ch = (Chatter) o;
                tmp = ch.getNick();}
        else tmp = null;
        if(tmp==null) {tmp="-1";}
        return tmp;
  }
  /**Get the chatter instance that correspond to the agent address*/
  public Chatter getChatterThanksAgentAddress(AgentAddress agAd) {
          Chatter retch = new Chatter();
          Enumeration e = nickChatter.elements();
          while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                  AgentAddress tmp = ch.getAgentAddress();
                  if(tmp==agAd)
                          retch = ch;
          }
          return retch;
  }
  /**Is chatter in this channel*/
  public boolean isChatterInChannel(String nick_, String ch_) {
          boolean b = false;
          if(nickChatter.containsKey(nick_)) {
                  Chatter tmpCh = (Chatter) nickChatter.get(nick_);
                  if(tmpCh.chatterIsInChannel(ch_))
                          b = true;
          }
          return b;
  }
  /**Get a string tab of the chatters names*/
  public String[] getNamesList() {
          int s = nickChatter.size(); int i=0;
          String [] tab = new String[s];
          Enumeration e = nickChatter.elements();
          while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                  String n = ch.getNick();
                  tab[i]=n; i++;
          }
          return tab;
  }
  /**Get a string tab of the chatters email*/
  public String[] getEmailList() {
          int s = nickChatter.size(); int i=0;
          String [] tab = new String[s];
          Enumeration e = nickChatter.elements();
          while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                  String n = ch.getEmail();
                  tab[i]=n; i++;
          }
          return tab;
  }
  /**Get a string tab of the chatters Role*/
  public String[] getRoleList() {
          int s = nickChatter.size(); int i=0;
          String [] tab = new String[s];
          Enumeration e = nickChatter.elements();
          while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                  String n = ch.getRole();
                  tab[i]=n; i++;
          }
          return tab;
  }
  /**Get chatters AgentAddress List for a choosen channel, RETURN INSTANCES OF CHATTER !!*/
  public Chatter [] getChatterListWhoseAreInAChannel(String ch_) {
          int s = nickChatter.size(); int i=0;
          Chatter [] tab = new Chatter[s];
          Enumeration e = nickChatter.elements();
          while(e.hasMoreElements()) {
                  Chatter ch = (Chatter) e.nextElement();
                 // String cha = ch.getChannel();
                  if(ch.chatterIsInChannel(ch_)) {
                          tab[i] = ch;
                  }
                  i++;
          }
          return tab;
  }
  /**Remove the couple (Nickname,AgentAddress) in the hashtable, return true if the job is well done*/
  public boolean removeChatter(String nickname) {
        boolean b;
        if (!nickChatter.isEmpty()) {
                Object o = nickChatter.remove(nickname);
                if (!o.equals(null)) {
                        b = true;}
                else b = false;
        }
        else b = false;
        return b;
  }
  /**Print all the couple  (Nickname,AgentAddress) */
  public void printAdrNick() {
          String tmp = nickChatter.toString();
        System.out.println("La table nickChatter: "+tmp);
  }


  /**Add a couple (Channelname,Channel) in the hashtable nameChannel*/
  public boolean addCouple(String name, Channel ch, int nbrUsers ) {
          boolean b = false;
          if(!nameChannel.containsKey(name)) {
                  nameChannel.put(name, ch);
                  b = true;
          }
         if(nameChannel.containsKey(name)) {
                Channel tmp = (Channel) nameChannel.get(name);
                tmp.setNbrUsers(nbrUsers);
          }
          return b;
  }

  /**Get the topic from the channel's name*/
  public String getChannelTopic(String ch) {
        String a;
        if (!nameChannel.isEmpty()) {
                Object o = nameChannel.get((Object)ch);
                Channel cha = (Channel) o;
                a = cha.getTopic();
        }
        else a = "-1";
        return a;
  }

  /**Get the admin 's name from the channel's name*/
  public String getChannelAdminName(String ch) {
        String a;
        if (nameChannel.containsKey(ch)) {
                Object o = nameChannel.get((Object)ch);
                Channel cha = (Channel) o;
                a = cha.getAdminName();
        }
        else a = "-1";
        return a;
  }

  /**Set the name of the admin to chatter for a chosen channel*/
  public void setChannelRole(String n) {
        if (!nameChannel.isEmpty()) {
                Object o = nameChannel.get((Object)n);
                Channel cha = (Channel) o;
                cha.setAdminName(chatter);
        }
  }
  /**Set the number of user for a selected channel*/
  public void setChannelNbrUsers(String chName, int nbr) {
        if (nameChannel.containsKey(chName)) {
                Object o = nameChannel.get((Object)chName);
                Channel cha = (Channel) o;
                cha.setNbrUsers(nbr);
        }
  }
  /**get the number of user for a selected channel*/
  public int getChannelNbrUsers(String chName) {
        int i = -1;
        if (nameChannel.containsKey(chName)) {
                Object o = nameChannel.get((Object)chName);
                Channel cha = (Channel) o;
                i = cha.getNbrUsers();
        }
        return i;
  }
  /**Increase the number of users of the selected channel by one*/
  public void incrChannelNbrUsers(String chName) {
          if (nameChannel.containsKey(chName)) {
                  Channel ch;
                  ch = (Channel) nameChannel.get(chName);
                  int n = ch.getNbrUsers();
                  n++;
                  ch.setNbrUsers(n);
                  nameChannel.put(chName,ch);
          }
  }
  /**Get a string tab of the channels names*/
  public String[] getChannelsNamesList() {
          int s = nameChannel.size(); int i=0;
          String [] tab = new String[s];
          Enumeration e = nameChannel.elements();
          while(e.hasMoreElements()) {
                  Channel ch = (Channel) e.nextElement();
                  String n = ch.getName();
                  tab[i]=n; i++;
          }
          return tab;
  }
  /**Get a string tab of the channels topics*/
  public String[] getChannelsTopicsList() {
          int s = nameChannel.size(); int i=0;
          String [] tab = new String[s];
          Enumeration e = nameChannel.elements();
          while(e.hasMoreElements()) {
                  Channel ch = (Channel) e.nextElement();
                  String n = ch.getTopic();
                  tab[i]=n; i++;
          }
          return tab;
  }
  /**Get a string tab of the channels numbers of users*/
  public String[] getChannelsNbrUsersList() {
          int s = nameChannel.size(); int i=0;
          String [] tab = new String[s];
          Enumeration e = nameChannel.elements();
          while(e.hasMoreElements()) {
                  Channel ch = (Channel) e.nextElement();
                  int nb = (ch.getNbrUsers());
                  String n = String.valueOf(nb);
                  tab[i]=n; i++;
          }
          return tab;
  }
  /**Get a string tab of the channels admins name*/
  public String[] getChannelsAdminNamesList() {
          int s = nameChannel.size(); int i=0;
          String [] tab = new String[s];
          Enumeration e = nameChannel.elements();
          while(e.hasMoreElements()) {
                  Channel ch = (Channel) e.nextElement();
                  String n = (ch.getAdminName());
                  tab[i]=n; i++;
          }
          return tab;
  }

  public void removeChannel(String n) {
          //System.out.println("nameChannel.containsKey= "+nameChannel.containsKey((Object) n));
          if(nameChannel.containsKey((Object) n)) {
                  nameChannel.remove(n);
                  //System.out.println("removeChannel("+n+") effectué");
                  //printnameChannel();
                  //sendToAllNoneAdmin();
          }
  }


  /**Send query msg*/
  public void sendQuerry(AgentAddress ad, String n) {
          sendMessage(ad, new ChatMessage(chatter,"#QUERRY "+n+" "+chatter));
  }
  /**Send  gettable msg*/
  public void sendGetTable() {
          AgentAddress agAdmin = getAgentWithRole("Chat","defaultChannel","Admin");
          sendMessage(agAdmin, new ChatMessage(chatter, "#GETTABLES"));
  }
  /**Send #KICK msg*/
  public void sendKick(String n) {
          AgentAddress ad = getChatterAddress(n);
          sendMessage(ad, new ChatMessage(chatter,"#KICK"));
  }
  /**Send #BAN msg*/
  public void sendBan(String n) {
          AgentAddress ad = getChatterAddress(n);
          sendMessage(ad, new ChatMessage(chatter,"#BAN"));
  }
  /**Send #TOPIC msg*/
  public void sendTopic(String n, String t) {
          broadcastMessage("Chat","defaultChannel","chatter", new ChatMessage(chatter,"#TOPIC "+n+" "+t));
  }
  /**Send #UNIQUE msg*/
  public void sendUnique() {
          //System.out.println("\t Je vais envoyer #UNIQUE");
          AgentAddress agAdmin = getAgentWithRole("Chat","defaultChannel","Admin");
          sendMessage(agAdmin, new ChatMessage(chatter, "#UNIQUE"));
  }
  /** Send #ADDNEWCHANNEL msg*/
  public void sendAddChannel(String chName) {
          AgentAddress agAdmin = getAgentWithRole("Chat","defaultChannel","Admin");
          sendMessage(agAdmin, new ChatMessage(chatter, "#ADDNEWCHANNEL "+chName));
  }
  /**Send #MAJNBRUSRCHANNEL msg*/
  public void sendMAJNbrUsrersOfChannel(String chName) {
          AgentAddress agAdmin = getAgentWithRole("Chat","defaultChannel","Admin");
          sendMessage(agAdmin, new ChatMessage(chatter, "#MAJNBRUSRCHANNEL "+chName));
  }
  /**Send #REMOVECHANNEL msg*/
  public void sendRemoveChannel(String chName) {
          AgentAddress agAdmin = getAgentWithRole("Chat","defaultChannel","Admin");
          sendMessage(agAdmin, new ChatMessage(chatter, "#REMOVECHANNEL "+chName));
  }

} //fin de la classe ChatAgent

