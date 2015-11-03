/*
* PrivateAgent1.java - ChatAgent, a chat application for MadKit
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

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  PrivateAgent, allows the chatter to chat in private on the second way. Control part.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */

public class PrivateAgent1 extends Agent {

    String chatter;
    String autre1;
    String nickname1;
    AgentAddress ab;
    
    
  public void setChatter(String s){chatter=s;}
  public String getChatter(){return chatter;}

    ChatAgentGUI_Private display;
    
    public PrivateAgent1(AgentAddress a,String autr,String nick)
    {
	nickname1=nick;
	autre1=autr;
	ab=a;
	initGUI();
    }
    
    public void initGUI(){
	display = new ChatAgentGUI_Private(this);
	setGUIObject(display);
    }
    
  public void activate() {
      int r = createGroup(true,"Chat","private",null,null);
      int r2 = requestRole("Chat","private",nickname1+"-"+autre1,null);
  }

    public void handleMessage1(Message m)
    {
	String s=null;
	if (m instanceof ChatMessage){
	    s=((ChatMessage)m).getString();
	    String c = ((ChatMessage)m).getChatter();
	    if (c == null){
		((ChatAgentGUI_Private)display).println(m.getSender()+"> " + s);
	    } else {
		((ChatAgentGUI_Private)display).println(c+"> " + s);
	    }
	}
	else  if (m instanceof StringMessage){
	    s=((StringMessage)m).getString();
	    ((ChatAgentGUI_Private)display).println("<< message from "+m.getSender()+">> \n" + s);
	}
    }

  public void live(){
      while(true)
	  {
		Message m = waitNextMessage();
		handleMessage1(m);
	  }
  } 
    public void sendPrivateMessage1(String s)
    {
	AgentAddress[] agList = getAgentsWithRole("Chat","private",nickname1+"-"+autre1);
	for(int i=0; i<agList.length;i++)
	    {
		if (! agList[i].equals(getAddress()))
        	    {
			sendMessage(agList[i], new ChatMessage(nickname1,s));
		    }
	    }
    }
}
