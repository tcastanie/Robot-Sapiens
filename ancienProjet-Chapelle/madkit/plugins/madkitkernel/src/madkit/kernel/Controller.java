/*
* Controller.java - Kernel: the kernel of MadKit
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

/**
 * A controller, as it name implies, controls the behavior of an agent.
 * A controller is an abstraction of the internal architecture of an agent.
 * It is possible to describe the general capabilities (skills of an agent)
 * and try different controllers for this agent.
 * <p> It is also handy for integrating new scripting languages into madkit and
 * makes those scripting language work for "synchronous" as well as "asynchronous" agents.
 * See the integration of the Jess (rule based system) and kawa (scheme) agents.</p>
 * <p> Warning: you may "plug" a behavior into an agent at creation time as follows:</p>
 * <pre>
 * class myAgent extends .... {
 *      myAgent(){
 *          setController(new MyController());
 *      }
 * }
 * </pre>
 * Madkit project (C) 1998-2002 Madkit Team
 * Copyright :    Copyright (c) 2001
 * @author O. Gutknecht, F. Michel, J. Ferber
 */

public interface Controller {
    public AbstractAgent thisAgent();

    /**
     * The activate method which takes precedence over the activate method defined
     * in the agent.
     * You may call the latter by invoking it directly from this method.
     */
    public void activate();

    /**
     * The end method which takes precedence over the end method defined
     * in the agent.
     * You may call the latter by invoking it directly from this method.
     */
    public void end();

    /**
     * The invocation method sent by the scheduler.
     * Warning: you have to take care that the scheduler invokes this "doIt"
     * method!!
     */
    public void doIt();

    /**
     * The live method which describes the overall behavior of a ("asynchronous")
     * agent.
     * <p> Warning: This method takes precedence over the agent's live method.
     * The agent's live method does not work when a controller is "plugged" into
     * an agent.. </p>
     */
    public void live();
}
