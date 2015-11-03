/*
 * Codec.java - Created on Oct 19, 2003
 * 
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Last Update: $Date: 2003/12/17 16:33:14 $
 */

package madkit.netcomm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**Utility class.
 * Provides a simple way to encode and decode Objects.
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
public class Codec {
	
	/**Decodes an object
	 * @param raw raw byte format of the object
	 * @return the Object
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static final Object decode(byte[] raw) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bis=new ByteArrayInputStream(raw);		
		ObjectInputStream objin = new ObjectInputStream(bis);
		return objin.readObject();
		
	}

	/**Encodes an object.
	 * @param o Obejct to encode 
	 * @return the encoded object
	 * @throws IOException
	 */
	public static final byte[] encode(Object o) throws IOException{
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		ObjectOutputStream objout = new ObjectOutputStream(byteout);
		objout.writeObject(o); //write the object
		return byteout.toByteArray();
	}
	
}
