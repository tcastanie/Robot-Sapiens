/*
* MinimalChatAgent.java -a minimal Chat application with MadKit
* Copyright (C) 2000-2002 Jacques Ferber
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
package madkit.system;

import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;
import madkit.utils.agents.AbstractEditorAgent;



class ChatMessage extends Message {
    String content = null;
    String chatter = null;

    String getString()
    {
        return content;
    }

    public String toString()
    {
        if (chatter == null)
            return content;
        else
            return chatter + "> " + content;
    }


    ChatMessage(String s){
        content=s;
    }
    ChatMessage(String from, String s){
        content=s;
        chatter = from;
    }

    String getChatter(){return chatter;}
}

public class MinimalChatAgent extends AbstractEditorAgent {

  String chatter;

  public MinimalChatAgent(){super();}

  public void setChatter(String s){chatter=s;}
  public String getChatter(){return chatter;}

  public void initGUI()
  {
    display = new MinimalChatPanel(this);
	setGUIObject(display);
  }


  public void activate() {
    int r = createGroup(true,"minimalchat",null,null);
    int r2 = requestRole("minimalchat","chatter",null);
  }

  public void handleMessage(Message m){
  	String s=null;
  	if (m instanceof ChatMessage){
  		s=((ChatMessage)m).getString();
        String c = ((ChatMessage)m).getChatter();
        if (c == null){
  		    ((MinimalChatPanel)display).println(m.getSender()+"> " + s);
        } else {
  		    ((MinimalChatPanel)display).println(c+"> " + s);
        }
  	}
    else  if (m instanceof StringMessage){
  		s=((StringMessage)m).getString();
  		((MinimalChatPanel)display).println("<< message from "+m.getSender()+">> \n" + s);
  	}
  }



  public void live(){
	while(true) {
		Message m = waitNextMessage();
	    handleMessage(m);
	}
  }

  public void sendChatMessage(String s){
  		AgentAddress[] agList = getAgentsWithRole("minimalchat","chatter");
  		for(int i=0; i< agList.length;i++){
  			if (!agList[i].equals(getAddress()))
  				sendMessage(agList[i], new ChatMessage(chatter,s));
  		}
  	}
}
