/*
* CommandAction.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;


/**
 * The CommandAction defines a generic implementation of
 * actionPerformed. Here actionPerformed simply calls the execute
 * method on its command object.
 */
public class CommandAction extends AbstractAction 
{
    /* Initially from example code in JavaWorld by Anthony Sintes
       ObjectWave Corporation */

    protected Executable command;
    /**
     *	This constructor creates an action without an icon.
     *	@param command the command for this action to act upon
     *	@param name the action's name
     */

    public CommandAction(Executable command, String name) 
    {
	super(name);
	setCommand(command);
    }
    /**
     *	This constructor creates an action with an icon.
     *	@param command the command for this action to act upon
     *	@param name the action's name
     *	@param icon the action's icon
     */
    public CommandAction(Executable command, String name, Icon icon) 
    {
	super(name, icon);
	setCommand(command);
    }

    /**
     *	This constructor creates an action with an icon.
     *	@param command the command for this action to act upon
     *	@param name the action's name
     *	@param icon the action's icon
     *	@param shortdesc the action's name
     *	@param longdesc the action's name
     */
    public CommandAction(Executable command, String name, Icon icon,
			 String shortdesc, String longdesc) 
    {
	super(name, icon);
	setCommand(command);
	putValue(Action.SHORT_DESCRIPTION, shortdesc);
	putValue(Action.LONG_DESCRIPTION,  longdesc);
    }

    /**
       * Set the value of the short description.
       * @param v  Value to assign to the short escription.
       */
   public void setShortDescription(String v) {putValue(Action.SHORT_DESCRIPTION, v);}

    /**
       * Set the value of the long description.
       * @param v  Value to assign to the long description.
       */
   public void setLongDescription(String v) {	putValue(Action.LONG_DESCRIPTION, v);}
    

    /**
     *  actionPerformed is what executed the command. actionPerformed
     *  is called whenever the action is acted upon.
     *	@param e the action event */
    public void actionPerformed(ActionEvent e) 
    {
	getCommand().execute();
    }

    /**
     * This method retrieves the encapsulated command.
     * @return Executable
     */
    protected final Executable getCommand() 
    {
	return command;
    }
    /**
     * This method sets the action's command object.
     * @param newValue the command for this action to act upon
     */
    protected final void setCommand(Executable newValue) 
    {
	this.command = newValue;
    }
}
