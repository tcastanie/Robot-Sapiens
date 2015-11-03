/*
* XMLMessage.java - Message library of MadKit
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

import java.io.StringReader;
import java.io.StringWriter;

import madkit.kernel.Message;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;


/** A message that carries a XML document.

    You can set the document either through a string or a pre-parsed
    DOM tree. This class is optimized in the sense that it will change
    representations only if needed (i.e. constructed with a String and
    used through the getDocument() accessor). You need the xerces XML
    parser to use this class.  */
public class XMLMessage extends Message
{
    /** The xml content as a Document (null if the content is defined as a string) */
    protected Document doccontent = null;
    /** The xml content as a String (null if the content is defined as a Document) */
    protected String   strcontent = null;

   /** Setup a XMLMessage with the xml document setup as a string. The string is not validated at construction
    * @param s A valid (i.e. parseable) text XML document
    */
  public XMLMessage(String s)
  {
      strcontent=s;
      doccontent=null;
  }

    /** Setup a XMLMessage with the xml document setup as a Document
 * @param d A well-formed DOM object
 */
  public XMLMessage(Document d)
  {
      strcontent=null;
      doccontent=d;
  }

    /** Returns the XMLMessage content as a string. If the String
 * constructor was called, this accessor just returns the initial
 * string. If the Document constructor was used, it calls Xerces
 * to get a textual representation
 * @return A stringified version of the message content
 */
    public String getString()
    {
	if (strcontent!=null)
	    return strcontent;
	else
	    {
		try
		    {
			OutputFormat of = new OutputFormat(doccontent);
			StringWriter writer = new StringWriter();
			of.setIndenting(true);
			XMLSerializer serial = new XMLSerializer(writer, of);
			serial.asDOMSerializer();
			serial.serialize(doccontent);
			strcontent = writer.toString();
			return strcontent;
		    }
		catch (Exception e)
		    {
			System.err.println("XMLMessage Exception "+e);
			return null;
		    }
	    }

    }
    /** Returns the XMLMessage content as a document. If the Document
 * constructor was called, this accessor just returns the initial
 * document. If the String constructor was used, it calls Xerces
 * to parse it into a DOM tree.
 * @return A DOM object for the message content
 */
    public Document getDocument()
    {
	if (doccontent!=null)
	    return doccontent;
	else
	    {
		try
		    {
                      // Xerces lines
                      DOMParser parser = new DOMParser();
                      parser.parse(new InputSource(new StringReader(strcontent)));
                      doccontent = parser.getDocument();
                      return doccontent;
                      // Sun xml.jar lines
                      /* DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			parser.parse(new InputSource(new StringReader(strcontent)));
			doccontent = parser.newDocument();
			return doccontent; */
		    }
		catch (Exception e)
		    {
			System.err.println("XMLMessage Exception "+e);
			return null;
		    }
	    }
    }

/** This method returns a string for the XML document set in this message.
 * Warning, if the document is directly stored as a Document and not a string,
 * this will just call the toString() method on the Document object.
 * @return A stringified version of the document
 */
    public String toString()
    {
	if (strcontent!=null)
	    return strcontent;
	else
	    return doccontent.toString();
    }
}
