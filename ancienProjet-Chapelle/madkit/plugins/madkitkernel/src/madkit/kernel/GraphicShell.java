/*
* GraphicShell.java - Kernel: the kernel of MadKit
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

import java.awt.Point;
import java.awt.Dimension;

/** An useful interface if you want to define your own GUI for MadKit.

    See the Desktop or Console booter source for more information. The
    principle here is being able to plug the MadKit agent-kernel into
    various applications.  These applications could be development or
    test platforms, regular application with an embedded MAS, or
    applets.
*/
public interface GraphicShell
{
    /** This method is called by the MadKit kernel when a new agent is
	launched, it is up to the graphical application to setup the
	appropriate widget in cooperation with the agent (i.e. with
	the getGUIObject() method in the Agent class)
    */
    public void setupGUI(AbstractAgent a);
    
	/** This method is identical to setupGUI(AbstractAgent a) except that
	 * location and dimension of the graphic interface are passed as arguments.
	 * This method should not be called directly and is used only for restoring 
	 * a configuration of agents
	 */
	public void setupGUI(AbstractAgent a, Point p, Dimension d);

    /** This method is called by the MadKit kernel when a local agent is
	killed, so that the host graphical application can clean up the wrapper
	graphical interface
    */
    public void disposeGUI(AbstractAgent a);

    /** This method is called by the MadKit kernel when a local agent request
	the host graphical application to remove its wrapper
	graphical interface
    */
    public void disposeGUIImmediatly(AbstractAgent a);


    /** This method defines a default GUI object that will be
	instanciated when an agent does not define its initGUI()
	method. The agent is passed in reference so that the
	GraphicShell has an opportunity to handle different GUIs
	according to the agent type or redirect the output stream
    */
    public Object getDefaultGUIObject(AbstractAgent a);
}



