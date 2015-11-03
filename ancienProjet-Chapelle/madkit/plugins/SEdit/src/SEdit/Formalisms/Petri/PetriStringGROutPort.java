/*
* PetriStringGROutPort.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit.Formalisms.Petri;

import java.util.*;
import java.io.*;

import SEdit.*;
import SEdit.Graphics.*;
import SEdit.Scheme.*;

import madkit.kernel.*;
import madkit.messages.*;



public class PetriStringGROutPort extends PetriOutPort {


    String group;

    /**
       * Get the value of group.
       * @return Value of group.
       */
    public String getGroup() {return group;}

    /**
       * Set the value of group.
       * @param v  Value to assign to group.
       */
    public void setGroup(String  v) {this.group = v;}


    String role;

    /**
       * Get the value of role.
       * @return Value of role.
       */
    public String getRole() {return role;}

    /**
       * Set the value of role.
       * @param v  Value to assign to role.
       */
    public void setRole(String  v) {this.role = v;}


	public PetriStringGROutPort(){
		setMessageType("StringGR");
	}

	public void produce(Object e) {
  		String s = STools.prinToString(e);
  		StringMessage m = new StringMessage(s);
  		System.out.println(">> send StringMessage : " + s);
		//  		getStructure().getAgent().sendToRecipient(m);
		//  		getStructure().getAgent().doSendMessage(group,role,
		//				m);

  	}

}



