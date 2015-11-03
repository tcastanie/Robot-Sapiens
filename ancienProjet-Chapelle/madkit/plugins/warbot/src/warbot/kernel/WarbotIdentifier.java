/*
* WarbotIdentifier.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

import madkit.kernel.AgentAddress;

final class WarbotIdentifier implements madkit.kernel.GroupIdentifier
{
	static String password;

public boolean allowAgentToTakeRole(AgentAddress requester, String roleName, Object memberCard)
{
	if(memberCard != null && memberCard instanceof String)
	{
		return ((String) memberCard).equals(password);
	}
	return false;
}

public boolean allowOverlooking(AgentAddress requester, Object accessCard)
{
	return allowAgentToTakeRole(requester, null, accessCard);
}

WarbotIdentifier()
{
	String s="";
	for(int i = 0;i<24;i++)
	if(Math.random() < .5)
		s+='A';
	else
		s += (int) (Math.random()*10);
	password = s;
	//System.err.println(s);
}


}


