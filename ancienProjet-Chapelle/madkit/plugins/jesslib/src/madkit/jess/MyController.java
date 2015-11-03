/*
* MyController.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
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
package madkit.jess;

import jess.Context;
import jess.JessException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

public class MyController extends MadkitFunction implements Userfunction {

    public MyController(JessController c)
    {
        ag = c;
    }

    public String getName()
    {
        return "myController";
    }
    public Value call(ValueVector valuevector1, Context context1)
        throws JessException
    {
        return new Value( ag );
    }
}
