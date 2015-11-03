/* Taken from jEdit..
 * OperatingSystem.java - OS detection
 * Copyright (C) 2002 Slava Pestov
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
 */

package madkit.utils.common;

import javax.swing.UIManager;
import java.io.*;

/**
 * Contains operating system detection routines.
 * @author Slava Pestov
 * @version $Id: OperatingSystem.java,v 1.1 2003/12/08 13:04:58 jferber Exp $
 * 
 */
public class OperatingSystem
{
	
	/**
	 * Returns if we're running Windows 95/98/ME/NT/2000/XP.
	 */
	public static final boolean isWindows()
	{
		return os == WINDOWS_9x || os == WINDOWS_NT;
	}

	/**
	 * Returns if we're running Windows 95/98/ME.
	 */
	public static final boolean isWindows9x()
	{
		return os == WINDOWS_9x;
	} 


	/**
	 * Returns if we're running Windows NT/2000/XP.
	 */
	public static final boolean isWindowsNT()
	{
		return os == WINDOWS_NT;
	} 



	//{{{ isUnix() method
	/**
	 * Returns if we're running Unix (this includes MacOS X).
	 */
	public static final boolean isUnix()
	{
		return os == UNIX || os == MAC_OS_X;
	} //}}}

	//{{{ isMacOS() method
	/**
	 * Returns if we're running MacOS X.
	 */
	public static final boolean isMacOS()
	{
		return os == MAC_OS_X;
	} 

        /**
         * Returns if we're running MacOS X and using the native L&F.
         */
        public static final boolean isMacOSLF()
        {
                return (isMacOS() && UIManager.getLookAndFeel().isNativeLookAndFeel());
        } 

	/**
	 * Returns if Java 2 version 1.4 is in use.
	 */
	public static final boolean hasJava14()
	{
		return java14;
	}

	
	private static final int UNIX = 0x31337;
	private static final int WINDOWS_9x = 0x640;
	private static final int WINDOWS_NT = 0x666;
	private static final int OS2 = 0xDEAD;
	private static final int MAC_OS_X = 0xABC;
	private static final int UNKNOWN = 0xBAD;

	private static int os;
	private static boolean java14;


	static
	{
		if(System.getProperty("mrj.version") != null)
		{
			os = MAC_OS_X;
		}
		else
		{
			String osName = System.getProperty("os.name");
			if(osName.indexOf("Windows 9") != -1
				|| osName.indexOf("Windows ME") != -1)
			{
				os = WINDOWS_9x;
			}
			else if(osName.indexOf("Windows") != -1)
			{
				os = WINDOWS_NT;
			}
			else if(osName.indexOf("OS/2") != -1)
			{
				os = OS2;
			}
			else if(File.separatorChar == '/')
			{
				os = UNIX;
			}
			else
			{
				os = UNKNOWN;
				System.err.println("Warning: "+
					"Unknown operating system: " + osName);
			}
		}

		if(System.getProperty("java.version").compareTo("1.4") >= 0)
			java14 = true;
	}


}
