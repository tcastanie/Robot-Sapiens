/* XMLWriter.java
 *Copyright (C) 2003 Sebastian A. Rodriguez
 * 
 * This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*
 * Created on 25 avr. 03
 * 
 * Revision $Revision: 1.1 $
 * Last Update $Date: 2004/02/16 14:52:23 $
 */
package madkit.pluginmanager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;


/**
 * 
 * @author Sebastian Rodriguez - sebastianrodriguez@gmx.net
 */
public class XMLWriter {
	public static boolean saveDocument(File file, Document doc) throws IOException{
		
		OutputFormat format=new OutputFormat(doc);
		format.setEncoding("ISO-8859-1");
		format.setIndenting(true);
		FileWriter fw=new FileWriter(file);
		XMLSerializer serial=new XMLSerializer(fw,format);
		serial.asDOMSerializer();
		serial.serialize(doc.getDocumentElement());
		fw.close();
		
		return true;
	}
}
