/*
* Pause.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
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
import jess.Funcall;
import jess.JessException;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;

public class Pause extends MadkitFunction implements Userfunction {

    public Pause(JessController abstractjessagent1)
    {
        ag = abstractjessagent1;
    }

    public String getName()
    {
        return "pause";
    }

    public Value call(ValueVector valuevector1, Context context1)
        throws JessException
    {
        Value value1 = valuevector1.get( 1 ).resolveValue( context1 );

        if( valuevector1.size() != 2 && value1.type() != 4 )
            throw new JessException( "pause", " not a valid call " + (valuevector1.get( 1 ).toString()), "" );
        else
        {
            ag.doPause(value1.intValue(context1));
            return Funcall.TRUE;
        }
    }
}
