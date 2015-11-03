/*
* BroadcastMessage.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
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
import madkit.kernel.Message;

public class BroadcastMessage extends MadkitFunction implements Userfunction {

    public BroadcastMessage(JessController abstractjessagent1)
    {
        ag = abstractjessagent1;
    }

    public String getName()
    {
        return "broadcastMessage";
    }

    public Value call(ValueVector valuevector1, Context context1)
        throws JessException
    {
        int nargs = valuevector1.size()-1;
        if((nargs > 4) || (nargs < 3))
            throw new JessException( "broadcastMessage", "wrong number of arguments: " + (valuevector1.size() - 1) + " should be 3 or 4", "" );
        else
        {
            Value value1 = valuevector1.get( 1 ).resolveValue( context1 );
            Value value2 = valuevector1.get( 2 ).resolveValue( context1 );
            Value value3 = valuevector1.get( 3 ).resolveValue( context1 );

            if (nargs == 3){
                if( value1.type() != RU.STRING && value2.type() != RU.STRING && value3.type() != RU.EXTERNAL_ADDRESS)
                        throw new JessException( "broadcastMessage", " not a valid call " +
                            (valuevector1.get(1).toString()) + " " + (valuevector1.get(2).toString())+ " " +
                                (valuevector1.get(3).toString()),"" );
                Object m = value3.externalAddressValue(context1);
                if (!(m instanceof Message))
                     throw new JessException( "broadcastMessage", " invalid message : " + m, "" );
                ag.doBroadcastMessage(value1.stringValue(context1), value2.stringValue(context1), (Message) m);
            } else {
              Value value4 = valuevector1.get( 4 ).resolveValue( context1 );
              if( value1.type() != RU.STRING && value2.type() != RU.STRING && value3.type() != RU.STRING && value4.type() != RU.EXTERNAL_ADDRESS)
                        throw new JessException( "broadcastMessage", " not a valid call " +
                            (valuevector1.get(1).toString()) + " " + (valuevector1.get(2).toString())+ " " +
                                (valuevector1.get(3).toString())+ " " + (valuevector1.get(4).toString()),"" );
                Object m = value4.externalAddressValue(context1);
                if (!(m instanceof Message))
                     throw new JessException( "broadcastMessage", " invalid message : " + m, "" );
                ag.doBroadcastMessage(value1.stringValue(context1), value2.stringValue(context1),
                                        value3.stringValue(context1), (Message) m);
            }
            return Funcall.TRUE;
        }
    }
}
