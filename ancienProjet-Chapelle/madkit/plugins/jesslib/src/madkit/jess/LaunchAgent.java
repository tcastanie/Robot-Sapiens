/*
* LaunchAgent.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
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
import madkit.kernel.AbstractAgent;


public class LaunchAgent extends MadkitFunction implements Userfunction {

    public LaunchAgent(JessController abstractjessagent1)
    {
        ag = abstractjessagent1;
    }


    public String getName()
    {
        return "launchAgent";
    }

    public Value call(ValueVector valuevector1, Context context1)
        throws JessException
    {
        Object obj = null;

        if( valuevector1.size() != 3 )
            throw new JessException( "launchAgent", "wrong number of arguments: " + (valuevector1.size() - 1) + " should be 2", "" );
        else
        {
            Value value1 = valuevector1.get( 1 ).resolveValue( context1 );
            Value value2 = valuevector1.get( 2 ).resolveValue( context1 );

            if( value1.type() != 2048 )
                throw new JessException( "launchAgent", " not an agent " + (valuevector1.get( 1 ).toString()), "" );
            else
            {
                obj = value1.externalAddressValue( context1 );
                if( !( obj instanceof AbstractAgent ) )
                    throw new JessException( "launchAgent", " not a valid agent " + value1.toString(), "" );
                else
                {
                    ag.doLaunchAgent( (AbstractAgent) obj, value2.toString(), true );
                    return Funcall.TRUE;
                }
            }
        }
    }
}
