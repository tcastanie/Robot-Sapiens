/*
* AGRTrio.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
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
package madkit.kernel;



/** A kernel internal structure. This class might be used by "system"

    agent developers as it is the structure of choice about

    organization in MadKit. It is thus present in many parameters to

    the kernel calls (and exhibited in the hooks) */

public class AGRTrio implements java.io.Serializable

{

	private AgentAddress agent;

	private String community;

	private String group;

	private String role;





/** Defines an Agent/Group/Role structure. */

public AGRTrio (AgentAddress theAddress,String communityName, String theGroup, String theRole)

{

	agent = theAddress;

	role = theRole;

	group = theGroup;

	community = communityName;

}



public AGRTrio (AgentAddress theAddress, String theGroup, String theRole)

{

	agent = theAddress;

	role = theRole;

	group = theGroup;

	community="public";

}



/** Defines an Agent/Group/Role structure with role defined as null */

public AGRTrio (AgentAddress theAddress, String theGroup)

{

	this(theAddress, theGroup, (String)null);

}

  

public AgentAddress getAgent(){	return agent;}



/** Return the group information part */

public String getCommunity(){	return community;}



/** Return the group information part */

public String getGroup(){	return group;}



/** Return the role information part */

public String getRole(){	return role;}

}



  





