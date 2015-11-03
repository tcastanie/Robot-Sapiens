
/*
* PingPong.java - a PingPong agent in Java
* Copyright (C) 1998-2002 Olivier Gutknecht
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
package madkit.demos;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;

public class PingPong extends Agent
{
	AgentAddress other = null;
	boolean creator=false;

/** This method allows the user to set the "other" agent with the G-Box property sheet */
public void setOther(AgentAddress add)
{
	other = add;
}
/** This method allows the user to get the "other" agent address in the G-Box property sheet */
public AgentAddress getOther()
{
	return other;
}


/** This method sets the agent to create or join a "ping-pong" group, and then registers it with a role of "player"
Could be simpler, but I wanted to show a complete and careful implementation */

public void activate()
{
	println("PingPong agent Activated");
	println("Looking for a ping-pong group...");
	if (isGroup("ping-pong"))
	{
		println ("Yeah ! I join");
		creator=false;
	}
	else
	{
		println ("Nope ! I create one");
		createGroup(true,"ping-pong",null,null);
		creator=true;
	}
	requestRole("ping-pong","player",null);
	createGroup(true,"bench",null,null);
}

public void live()
{
	println("Looking for a sport partner...");
	do
	{
		exitImmediatlyOnKill(); //to be sure the agent thread can be really stopped
		pause(100);
		AgentAddress[] v = getAgentsWithRole("ping-pong","player");
		for (int i=0; i < v.length; i++)
		{
			AgentAddress agent = v[i];
			if (! agent.equals(getAddress()))
				other = agent;
		}
	}
	while (other == null);
	println("Other is :"+other);
	
	// If I'm not the founder agent, I send the first ball
	if (! creator)
		sendMessage(other, new StringMessage("Ball"));
	
	for (int i = 5; i > 0; i--)
	{
		Message m = waitNextMessage();
		if(! m.getSender().equals(other))
		{
			while(true)
			{
				exitImmediatlyOnKill();
				Message m2 = waitNextMessage(1000);
				if(m2==null)
				{
					other = m.getSender();
					i=5;
					println("partner is gone !!");
					println("playing with new partner "+other);
					break;
				}
				else
					if(m2.getSender().equals(other))
						break;
			}
		}		
		StringMessage ans = (StringMessage) m;
		println("GEE ! My turn..."+ m.getCreationDate());
		pause(1000);
		sendMessage(other, new StringMessage(ans.getString()));
	}
}

public void end()
{
	println("Bye Bye !!");
	println ("PingPong agent Ended");
}
}
