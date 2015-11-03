/*
* SendMessage.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
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
import madkit.kernel.AgentAddress;
import madkit.kernel.InvalidAddressException;
import madkit.kernel.Message;


public class SendMessage extends MadkitFunction implements Userfunction {

    public SendMessage(JessController abstractjessagent1)
    {
        ag = abstractjessagent1;
    }

    public String getName()
    {
        return "sendMessage";
    }

    public Value call(ValueVector valuevector1, Context context1)
        throws JessException
    {
        Object obj = null;
        Object obj1 = null;
        Message message1 = null;

        if( valuevector1.size() != 3 )
            throw new JessException( "sendMessage", "wrong number of arguments: " + (valuevector1.size() - 1) + " should be 2", "" );
        else
        {
            Value value1 = valuevector1.get( 1 ).resolveValue( context1 );
            Value value2 = valuevector1.get( 2 ).resolveValue( context1 );

            if( value2.type() != RU.EXTERNAL_ADDRESS)
                throw new JessException( "sendMessage", " not a valid message " + (valuevector1.get( 2 ).toString()), "" );
            else
            {
                obj1 = value2.externalAddressValue( context1 );
                if( !( obj1 instanceof Message ) )
                    throw new JessException( "sendMessage", " not a message " + value2.toString(), "" );
                else
                {
                    message1 = (Message) obj1;
                    if( value1.type() == RU.EXTERNAL_ADDRESS)
                    {
                        obj = valuevector1.get( 1 ).externalAddressValue( context1 );
                        if( !( obj instanceof AgentAddress ) )
                            throw new JessException( "sendMessage", " invalid agent : " + value1.toString(), "" );
                    }
                    else
                    {
                        if( value1.type() != RU.STRING )
                            throw new JessException( "sendMessage", " not a valid agent address : " + value1.toString(), "" );
                        try
                        {
                            obj = new AgentAddress( value1.stringValue( context1 ) );
                        }
                        catch( InvalidAddressException unused8 )
                        {
                            throw new JessException( "sendMessage", " not an agent : " + value1.toString(), "" );
                        }
                    }
                    ag.doSendMessage( (AgentAddress) obj, message1 );
                    return Funcall.TRUE;
                }
            }
        }
    }
}
