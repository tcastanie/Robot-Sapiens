/*
* ActMessage.java - Message library of MadKit
* Copyright (C) 1998-2002  Olivier Gutknecht, Jacques Ferber
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
package madkit.messages;

import java.util.Enumeration;
import java.util.Hashtable;

/** This class describes an generic speech act message.

  @author Ol. Gutknecht
  @date 10/03/98 original, revision 1.1 04/2002 J.Ferber
  @version 1.1
  */

public class ActMessage extends madkit.kernel.Message
{
    protected String action;
    protected Hashtable fields;
    String content;

    /** Constructor for GenericMessage class */
    public ActMessage(String actiontype)
    {
	    action = actiontype;
	    fields = new Hashtable();
    }

    public ActMessage(String actiontype, String cont)
    {
	    this(actiontype);
            content = cont;
    }

    public ActMessage(String actiontype, Object obj)
    {
	    this(actiontype);
            setObject(obj);
    }

    public ActMessage(String actiontype, String cont, Object obj)
    {
        this(actiontype);
        content = cont;
        setObject(obj);
    }

    public String getAction()
    {
	return action;
    }

    public String getContent()
    {
	    return content;
    }

    public void setContent (String s)
    {
	    content = s;
    }

    public Object getObject()
    {
	return fields.get("object");
    }

    public void setObject (Object s)
    {
	fields.put("object",s);
    }

    public Enumeration getKeys()
    {
	return fields.keys();
    }

    public void setField(String key, Object value)
    {
	fields.put(key,value);
    }
    public Object getFieldValue(String key)
    {
	return fields.get(key);
    }

    public String getInReplyTo(){
        return (String)getFieldValue(":in-reply-to");
    }

    public void setInReplyTo (String s){
        setField(":in-reply-to",s);
    }

}