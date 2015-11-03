/*
* DefaultController.java - Controllers : behavior plugging mechanism into MadKit agents
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
package madkit.utils.agents;

import madkit.kernel.AbstractAgent;
import madkit.kernel.Controller;

/**
 * Titre :        Madkit 3.0 dev projet
 * Description :  Madkit project
 * (C) 1998-2001 Madkit Team
 * Copyright :    Copyright (c) 2001-2002
 * @author O. Gutknecht, M. Fabien, J. Ferber
 * @version 1.0
 */

public class DefaultController implements Controller{

    protected AbstractAgent thisAgent;

    public AbstractAgent thisAgent(){return thisAgent;}

    public void activate(){
        thisAgent.println(".. controller is active");
        thisAgent.activate();
    }
    public void live(){}
    public void doIt(){}
    public void end(){
        thisAgent.println(".. controller is terminating");
        thisAgent.end();
    }

    public DefaultController(AbstractAgent _ag) {
        thisAgent = _ag;
    }
}
