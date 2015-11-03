/*
 * Utils.java - Created on Feb 2, 2004
 * 
 * Copyright (C) 2003-2004 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
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
 * Last Update: $Date: 2004/03/12 17:25:14 $
 */

package madkit.pluginmanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.2 $
 */
public class Utils {
	public static boolean copyFile(File orig, File dest , boolean overwrite){
		if(!orig.exists() &&!orig.canRead())
			return false;
		if(dest.exists()&&!overwrite)
			return false;
		
		try {
//			FileReader in = new FileReader(orig);
//			FileWriter out = new FileWriter(dest);
//			int c;
//			
//			int count=0;
//			while ((c = in.read()) != -1){
//				out.write(c);
//				count++;
//			}
//
			InputStream in=new FileInputStream(orig);
			FileOutputStream out=new FileOutputStream(dest);
			return copyStream(in, out);
		} catch (FileNotFoundException e) {
			//log.debug("FileNotFoundException caught",e);
			e.printStackTrace();
		} catch (IOException e) {
			//log.debug("IOException caught",e);
			e.printStackTrace();
		}
		return false;
			
	}

	public static boolean copyStream(InputStream in, FileOutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int len ;
		while ((len = in.read(buffer)) > 0) {
		   out.write(buffer, 0, len);
		}
		in.close();
		out.close();
		return true;
	}
	
	public static boolean writeToFile(String string, FileOutputStream out){
		PrintWriter pw=new PrintWriter(out);
		pw.println(string);
		pw.flush();
		pw.close();
		return true;
	}
	
	
	public static String md5sum(File file) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
	    return createMD5(new FileInputStream(file));
	}
	
	public static boolean compareMD5(File file, String md5) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
	    return md5.equals(md5sum(file));
	}
	
	private static String createMD5(final InputStream data) throws NoSuchAlgorithmException, IOException{
        MessageDigest md5 = MessageDigest.getInstance( "MD5" );

        
        DigestInputStream dis = new DigestInputStream(data,md5);
        while (dis.read() != -1) {
            ;
        }
        dis.close();
        data.close();
        
        byte[] fileDigest = md5.digest();
        StringBuffer checksumSb = new StringBuffer();
        for (int i = 0; i < fileDigest.length; i++) {
            String hexStr = Integer.toHexString(0x00ff & fileDigest[i]);
            if (hexStr.length() < 2) {
                checksumSb.append("0");
            }
            checksumSb.append(hexStr);
        }
        return checksumSb.toString();
	}	
}
