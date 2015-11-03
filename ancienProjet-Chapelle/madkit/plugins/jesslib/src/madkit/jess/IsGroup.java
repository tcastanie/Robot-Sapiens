/*
* IsGroup.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
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
import jess.RU;
import jess.Userfunction;
import jess.Value;
import jess.ValueVector;


public class IsGroup extends MadkitFunction implements Userfunction {

    public IsGroup(JessController abstractjessagent1)
    {
        ag = abstractjessagent1;
    }

    public String getName()
    {
        return "isGroup";
    }

    public Value call(ValueVector valuevector1, Context context1)
        throws JessException
    {
        int nargs = valuevector1.size()-1;
        if((nargs > 2) || (nargs < 1))
            throw new JessException( "isGroup", "wrong number of arguments: " + (valuevector1.size() - 1) + " should be 1 or 2", "" );
        Value value1 = valuevector1.get(1).resolveValue( context1 );

        if(value1.type() != RU.STRING )
            throw new JessException( "isGroup", " not a valid call " + (valuevector1.get(1).toString()), "" );
        else {
            if (nargs == 1)
                if (ag.doIsGroup( value1.stringValue( context1 ) ))
                    return Funcall.TRUE;
                else
                    return Funcall.FALSE;
            else {
                Value value2 = valuevector1.get( 2 ).resolveValue( context1 );
                if (value2.type() != RU.STRING)
                        throw new JessException( "isGroup", " not a valid call " +
                            (valuevector1.get(1).toString()) + " " + (valuevector1.get(2).toString()),"" );
                    if (ag.doIsGroup( value1.stringValue( context1 ), value2.stringValue( context1 ) ))
            	        return Funcall.TRUE;
                    else
            	        return Funcall.FALSE;

            }
        }
    }
}
