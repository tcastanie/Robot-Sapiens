/*
* GetMyGroups.java - Jess agents: linking MadKit with Jess to describe agents behaviors in Jess
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


public class GetMyGroups extends MadkitFunction implements Userfunction {

    public GetMyGroups(JessController abstractjessagent1)
    {
        ag = abstractjessagent1;
    }

    public String getName()
    {
        return "getMyGroups";
    }

    public Value call(ValueVector valuevector1, Context context1)
        throws JessException
    {

        if( valuevector1.size() < 1 || valuevector1.size() > 2)
            throw new JessException( "getMyGroups", "wrong number of arguments: " +
            						 (valuevector1.size() - 1) + " should be 0 or 1", "" );
        else
        {
            String[] res=null;
            if  (valuevector1.size()== 1)
                res = ag.doGetMyGroups();
            else {
                Value value1 = valuevector1.get( 1 ).resolveValue( context1 );
                if (value1.type() != RU.STRING)
                        throw new JessException( "getMyGroups", " not a valid call " +
                            (valuevector1.get(1).toString()),"" );
                res = ag.doGetMyGroups(value1.stringValue(context1));
            }

            ValueVector valuevector2 = new ValueVector(res.length);
            for(int i = 0; i < res.length; ++i )
                valuevector2.add(new Value(res[i], RU.STRING) );
            return new Value(valuevector2, RU.LIST);
        }
    }
}
