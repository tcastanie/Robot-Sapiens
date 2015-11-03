/*
 * Actions.java - Created on Feb 3, 2004
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
import java.io.IOException;

import madkit.pluginmanager.actions.AddDocumentationReference;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.2 $
 */
class Actions {
	
	public static int exec( String args[]){
		
		try {
			Process prc=Runtime.getRuntime().exec(args);
			return prc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	public static Action getActionByName(String str, File madkitDirectory, String ownerPlugin, Version ownerVersion){
		if(str.equals("addddocumentationreference") 
		        || str.equals("adddocref") 
		        || str.equals("docref")){
		    
			return new AddDocumentationReference(madkitDirectory,ownerPlugin,ownerVersion);
		}
		return null;
	}
	
	public static void removePlugin(File madkitDirecotry, String plugin, Version version){
	    try {
            AddDocumentationReference.removePlugin(madkitDirecotry,plugin, version);
        } catch (IOException e) { }
	}

    /**This method is call when all action are done to execute wrap up actions.
     * 
     */
    public static void finished(File madkitDirectory) {
        try {
            AddDocumentationReference.generateHtmlDocumentation(madkitDirectory);
        } catch (IOException e) {
            System.out.println("Failed to generation Html Doc. IOException caught "+e.getMessage());
        }
        
    }
}


