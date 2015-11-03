/*
* ActiveStructure.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit;


import madkit.kernel.Message;


/******************************************************************
			Interface ActiveStructure
******************************************************************/


/** An active structure is a structure which is able to receive 
	and process messages.
    
    @author Jacques Ferber and Olivier Gutknecht
    @version 2.0
    @see StructureAgent, Structure */

public interface ActiveStructure
{	
	public void handleMessage(Message m);
    
}











