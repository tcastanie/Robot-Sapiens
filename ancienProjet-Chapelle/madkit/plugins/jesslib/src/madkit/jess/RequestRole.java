/*
* RequestRole.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
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
import jess.RU;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;


public class RequestRole extends MadkitFunction implements Userfunction {

    public RequestRole(JessController abstractjessagent1)
    {
        ag = abstractjessagent1;
    }

    public String getName()
    {
        return "requestRole";
    }

    public Value call(ValueVector valuevector1, Context context1)
        throws JessException
    {
        int nargs = valuevector1.size()-1;
        if((nargs > 3) || (nargs < 2))
            throw new JessException( "requestRole", "wrong number of arguments: " + (valuevector1.size() - 1) + " should be 2 or 3", "" );
        else
        {
            Value value1 = valuevector1.get( 1 ).resolveValue( context1 );
            Value value2 = valuevector1.get( 2 ).resolveValue( context1 );

            if( value1.type() != RU.STRING && value2.type() != RU.STRING )
                throw new JessException( "requestRole", " not a valid call " + (valuevector1.get(1).toString()) + " " +
                                            (valuevector1.get( 2 ).toString()), "" );
            else {
              int r=0;
              if (nargs == 2)
                r = ag.doRequestRole( value1.stringValue(context1), value2.stringValue(context1) );
              else {
                Value value3 = valuevector1.get( 3 ).resolveValue( context1 );
                if (value3.type() != RU.STRING)
                        throw new JessException( "requestRole", " not a valid call " +
                            (valuevector1.get(1).toString()) + " " + (valuevector1.get(2).toString())+ " " +
                                (valuevector1.get(3).toString()),"" );
                r = ag.doRequestRole( value1.stringValue(context1), value2.stringValue(context1), value3.stringValue(context1) );
              }
              return new Value(r,RU.INTEGER);
            }
        }
    }
}
