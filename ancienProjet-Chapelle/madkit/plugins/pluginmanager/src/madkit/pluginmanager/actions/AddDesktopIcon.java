/*
 * AddDesktopIcon.java - Created on Feb 24, 2004
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

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import madkit.pluginmanager.Version;

class AddDesktopIcon extends AbstractAction{
    private Properties _params=new Properties();
	/**
	 * @param name
	 */
	protected AddDesktopIcon(File madkitDirectory, String plugin, Version pluginVersion) {
		super("adddesktopicon",madkitDirectory, plugin, pluginVersion);
	}
	
	private boolean addIcon(File desktopIni, String name, String code, String description, String icon, String type,Point position){
		try {
			Document document=openXMLDocument(desktopIni);
		} catch (SAXException e) {
			//logger.debug("SAXException caught ",e);
		//	debug("SAXException caught "+e.getMessage());
		} catch (IOException e) {
			//logger.debug("IOException caught ",e);
		//	debug("IOException caught "+e.getMessage());
		} catch (ParserConfigurationException e) {
			//logger.debug("ParserConfigurationException caught ",e);
		//	debug("ParserConfigurationException caught "+e.getMessage());
		} catch (FactoryConfigurationError e) {
			//logger.debug("FactoryConfigurationError caught ",e);
		//	debug("FactoryConfigurationError caught "+e.getMessage());
		}
		
		return false;
	}

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#getMandatoryParameters()
     */
    public Collection getMandatoryParameters() {
        Vector v=new Vector();
        v.add("icon");
        v.add("code");
        v.add("name");
        v.add("type");
        return v;
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#getOptionalParameters()
     */
    public Collection getOptionalParameters() {
        Vector v=new Vector();
        v.add("posX");
        v.add("posY");
        v.add("description");
        return v;
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#setParamenters(java.util.Properties)
     */
    public void setParamenters(Properties params) {
        _params=params;
        if(_params==null){
            _params=new Properties();
        }
        
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#execute()
     */
    public boolean execute() {
        // TODO Auto-generated method stub
        return false;
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#getFailureReason()
     */
    public String getFailureReason() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * @see madkit.pluginmanager.Action#requiredPlugins()
     */
    public Collection requiredPlugins() {
        Vector v=new Vector();
        v.add("desktop2");
        return v;
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#shouldAskUser()
     */
    public boolean shouldAskUser() {
        return false;
    }

    /* (non-Javadoc)
     * @see madkit.pluginmanager.Action#getQuestionToUser()
     */
    public String getQuestionToUser() {
        return "Do you want to add an icon to your desktop?";
    }
}
