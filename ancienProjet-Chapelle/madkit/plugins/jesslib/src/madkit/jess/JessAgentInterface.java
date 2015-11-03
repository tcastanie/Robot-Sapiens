/*
* JessAgentInterface.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
* Copyright (C) 1998-2002  Jacques Ferber
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.jess;

/**
 * Titre :        JessAgent
 * Description :  Version 0.8 de JessAgent
 * Copyright :    Copyright (c) 2001
 * Société :      LIRMM
 * @author J. Ferber
 * @version 1.0
 */

import java.util.Vector;

import madkit.kernel.AbstractAgent;
import madkit.kernel.AgentAddress;
import madkit.kernel.GroupIdentifier;
import madkit.kernel.Message;

public interface JessAgentInterface {
  // public void doJoinGroup(String group);
  public void doRequestRole(String group, String role);
  public void doLeaveRole(String group, String role);
  public void doLeaveGroup(String group);
  public int createGroup(boolean d, String name, String desc, GroupIdentifier id);

  public AgentAddress[] doGetAgentsWithRole(String group, String role);

  public AgentAddress doGetAgentWithRole(String group, String role);
  public String doGetName();
  public String[] doGetRoles(String group);

  public Vector doGetGroups();
  public boolean doIsGroup(String group);
 // public void doFoundGroup(String group);

  public void doLaunchAgent(AbstractAgent agent, java.lang.String name, boolean gui);

  public void readMessages();

  // public  void doSendStringMessage(AgentAddress a, String m);
  public  void doSendMessage(AgentAddress a, Message m);
  // public void doSendControlMessage(String act);
 // public void doSendControlMessage(String act, String cont);

  public  void doSendMessage(java.lang.String g, java.lang.String r, Message m);
  public void doBroadcastMessage(java.lang.String g, java.lang.String r, Message m);
}
