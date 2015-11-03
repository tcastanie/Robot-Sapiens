/*
* Channel.java - ChatAgent, a chat application for MadKit
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



/**
 * Titre :        Chat Agent for MadKit
 * Description :  Data structure, used to register channels.
 * Copyright :    Copyright (c) 2002
 * @author:       BERTRAND Jean-Gabriel, MOHSINE Omar
 * @version:      0.8
 * @email:        bertrandj34@free.fr ; omario@caramail.com
 */


public class Channel implements java.io.Serializable /*IMPORTANT car bien que cette struct
                                                        de données ne se compose que de struct
                                                        simple, elles ne sont pas envoyer via le ACTMsg
                                                        mais référencé par pteur mémoire si on omet
                                                        java.io.Serializable*/
{
        
        String name;
        String topic;
        int nbrUsers;
        String adminName;
   
  public Channel(String n, String t, int nbr, String a) {
          name=n;
          topic=t;
          nbrUsers=nbr;
          adminName=a;
  }
  public String getName() {return name;}
  public void setName(String name) {this.name = name;}   
  public String getTopic() {return topic;}
  public void setTopic(String topic) {this.topic = topic;}
  public int getNbrUsers() {return nbrUsers;}
  public void setNbrUsers(int nbrUsers) {this.nbrUsers = nbrUsers;} 
  public String getAdminName() {return adminName;}
  public void setAdminName(String a) {this.adminName = a;} 
}
