/*
* KQMLMessage.java - Message library of MadKit
* Copyright (C) 1998-2002  Olivier Gutknecht
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

//import madkit.kernel.*;
import java.util.Enumeration;

/** This class describes a KQML message. It provides accessors for all
  reserved fields defined in the KQML Specification. 
  Note that the :receiver and :sender are automatically mapped to the
  MadKit AgentAddress.

  @author Ol. Gutknecht
  @date 10/03/98
  @version 1.0
  */

public class KQMLMessage extends ActMessage
{
  /** Constructor for KQMLMessage class */
  public KQMLMessage(String performative)
  {
      super(performative);
  }

  public String getPerformative()
  {
	return action;
  }    


  public String getForce()
  {
    return (String)getFieldValue("force");
  }
  public void setForce (String s)
  {
    fields.put("force",s);
  }

  public String getReplyWith()
  {
    return (String)getFieldValue("reply-with");
  }
  public void setReplyWith (String s)
  {
    fields.put("reply-with",s);
  }

  public String getInReplyTo()
  {
    return (String)getFieldValue("in-reply-to");
  }
  public void setInReplyTo (String s)
  {
    fields.put("in-reply-to",s);
  }

  public String getLanguage()
  {
    return (String)getFieldValue("language");
  }
  public void setLanguage (String s)
  {
    fields.put("language",s);
  }

  public String getOntology()
  {
    return (String)getFieldValue("ontology");
  }
  public void setOntology (String s)
  {
    fields.put("ontology",s);
  }


  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    
    buffer.append("("+getPerformative());
    for (Enumeration e = fields.keys() ; e.hasMoreElements() ;) 
      {
	Object field = e.nextElement();
	buffer.append(" :"+field+" ");
	buffer.append(getFieldValue((String)field));
      }
    if (getSender() != null)
      buffer.append(" :sender \""+getSender()+"\"");
    if (getReceiver() != null)
      buffer.append(" :receiver \""+getReceiver()+"\"");

    buffer.append(")");
    return new String(buffer);
  }

  static public void main (String[] args)
  {
    KQMLMessage m = new KQMLMessage("tell");
    m.setLanguage("KIF");
    m.setOntology("cook");
    m.setForce("permanent");
    m.setReplyWith("q2");
    m.setInReplyTo("q4");
    m.setInReplyTo("carottes-sont-cuites");
    // setSender(...) isn't public anymore
    // m.setSender(new AgentAddress("foobar",new KernelAddress(true)));
    
    System.out.println(m);
  }
  
}






