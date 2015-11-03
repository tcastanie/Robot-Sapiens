/*
* OrganizationRepresentation.java - libs: the general utils library of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Jacques Ferber, Fabien Michel
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

package madkit.utils.common;

import madkit.kernel.AgentAddress;
import madkit.kernel.GroupIdentifier;

public class SimplePasswdFilter implements GroupIdentifier {

  private String password;
  public SimplePasswdFilter() {
      randomPasswd();
  }

  public SimplePasswdFilter(String passwd) {
      password = passwd;
  }

  void randomPasswd(){
        String s="";
	for(int i = 0;i<24;i++)
	if(Math.random() < .5)
		s+='A';
	else
		s += (int) (Math.random()*10);
	password = s;
	//System.err.println(s);
  }

  public boolean allowAgentToTakeRole(AgentAddress requester, String roleName, Object memberCard) {
     //   System.out.println("requesting access: " + requester + " with passwd " + memberCard);
     //   System.out.println("and password is : "+password);
    	if(memberCard != null && memberCard instanceof String)
	{
		return ((String) memberCard).equals(password);
	}
	return false;
  }
  public boolean allowOverlooking(AgentAddress requester, Object accessCard) {
	return allowAgentToTakeRole(requester, null, accessCard);
  }
}

