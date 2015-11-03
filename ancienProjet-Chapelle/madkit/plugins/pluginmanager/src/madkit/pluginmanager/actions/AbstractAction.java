/*
 * AbstractAction.java - Created on Feb 24, 2004
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
 * Last Update: $Date: 2004/03/12 17:25:43 $
 */

package madkit.pluginmanager.actions;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import madkit.pluginmanager.Action;
import madkit.pluginmanager.Version;


abstract class AbstractAction implements Action {
	private String _name;
	private String _plugin;
	private Version _pluginVersion;
	
	protected File _madkitDir;
	
	protected AbstractAction(String name,File installDir,String plugin, Version pluginVersion){
		_name=name;
		_madkitDir=installDir.getAbsoluteFile();
		_plugin=plugin;
		_pluginVersion=pluginVersion;
	}
	public String getOwnerPlugin(){
	    return _plugin;
	}
	
	public Version getOwnerPluginVersion(){
	    return _pluginVersion;
	}
	/* (non-Javadoc)
	 * @see madkit.pluginmanager.Action#getName()
	 */
	public String getName() {
		return _name;
	}
	
	protected Document openXMLDocument(File file) throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
		return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
	}
	
	protected void debug(String str){
	    System.err.println("**Debug** Action "+_name+" : "+str);
	}
}

