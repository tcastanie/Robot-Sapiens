/*
* Chatter.java - ChatAgent, a chat application for MadKit
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


import java.util.Vector;

import madkit.kernel.AgentAddress;

/**
 * Titre :        Chat Agent for MadKit
 * Description :  Data struct, used to register chat's users.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */


public class Chatter implements java.io.Serializable /*IMPORTANT car bien que cette struct
                                                        de données ne se compose que de struct
                                                        simple, elles ne sont pas envoyer via le ACTMsg
                                                        mais référencé par pteur mémoire si on omet
                                                        java.io.Serializable*/
{
    AgentAddress chatter;
    String nickName;
    String email;
    String role;
    Vector channel = new Vector();
    
    public Chatter() {}
    
    public Chatter(AgentAddress ch, String ni,String em, String r, String c)
    {
	chatter=ch;
	nickName=ni;
	email=em;
        role=r;
        channel.addElement(c);
    }   
    public AgentAddress getAgentAddress()  {return(chatter);}
    public String getNick(){return(nickName);}
    public String getEmail(){return(email);}
    public String getRole(){return(role);}
    public String getChannel(int i){return((String)channel.elementAt(i));}
    public Vector getChannel() {return(channel);}

    public void setAgentAddress(AgentAddress ad){chatter=ad;}
    public void setNick(String n){nickName=n;}
    public void setEmail(String e){email=e;}
    public void setRole(String r){role=r;}
    
    public void setChannel(String c){channel.addElement(c);}
    
    public int sizeOfChannelVect() {return channel.size();}
    
    public boolean chatterIsInChannel(String ch) {
            boolean b=false;
            for(int i=0; i<channel.size(); i++) {
                    String s = (String)channel.elementAt(i);
                    if(s.equals(ch))
                            b = true;
            }
            return b;
    }
    
}
