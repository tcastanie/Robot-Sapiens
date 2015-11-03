/*
 * Version.java
 * 
 * Copyright (C) 2003 Sebastian A. Rodriguez
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
 * 
 * Created on May 19, 2003
 * Version: $Revision: 1.1 $
 * Last Update: $Date: 2004/06/02 10:15:29 $
 * 
 */
package madkit.utils.common;

import java.util.StringTokenizer;

/**Simple class to keep track of version incompatibilities.
 * 
 * @author  Sebastian Rodriguez -- sebastian.rodriguez@utbm.fr
 * @version $Revision: 1.1 $
 */
public class Version {

	private final int _major;
	private final int _minor;
	private final int _release;
	
	public Version (int major, int minor, int release){
		_major=major;
		_minor=minor;
		_release=release;
	}
	/**Return true if the <code>v</code> is higher than this version
	 * @param v
	 * @return
	 */
	public boolean isHigher(Version v){
		if(_major<v.getMajorVersion()){
			return true;
		}else if( _minor<v.getMinorVersion()){
			return true;
		}else if(_release< v.getReleaseVersion()){
			return true;
		}
		return false;
	}
	
	public int getMajorVersion(){
		return _major;
	}
	
	public int getMinorVersion(){
		return _minor;
	}
	
	public int getReleaseVersion(){
		return _release;
	}
	
	public String toString(){
		return _major+"."+_minor+"."+_release;
	}
	/**creates a version object reprented by the String <code>string</code>
	 * @param string 
	 * @return
	 */
	public static Version valueOf(String string) {
		if(string.equalsIgnoreCase("null")) string="0.0.0";
		StringTokenizer st=new StringTokenizer(string,".");
		int[] v=new int[3];
		for (int i=0;i<3;i++){
			String tk="";
			if(st.hasMoreTokens())
				tk=st.nextToken();				
			if(tk.equals("")){
				tk="0";
			}
			v[i]=Integer.valueOf(tk).intValue();
		}
		return new Version(v[0],v[1],v[2]);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if(obj!=null && obj instanceof Version){
			Version toComp=(Version) obj;
			return (_major==toComp.getMajorVersion() &&
						_minor==toComp.getMinorVersion() &&
						_release==toComp._release);
		}
		return false;
	}

}
