/*
 * PluginInformation.java - Created on Jan 31, 2004
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
 * Last Update: $Date: 2004/04/14 11:55:47 $
 */

package madkit.pluginmanager;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.3 $
 */
class PluginInformation{
	public final static int UP_TO_DATE=0;
	public final static int NEEDS_UPDATE=1;
	public final static int NEW=2;
	
	
	
	private String _name;
	private Version _version;
	private int _size;
	private String _fileName;
	private String _description;
	
	private Hashtable _dependencies=new Hashtable();
	private Collection _actions=new Vector();
	private String _serverUrl;
	
	//author data
	private String _authoName;
	private String _authorEmail;
	private String _authorWeb;
    private String _md5;
	
	
	
	
	/**
	 * 
	 */
	private PluginInformation(String name, Version version) {
		super();
		_name=name.toString();
		_version=version;
	}

	/**
	 * @return Returns the _authoName.
	 */
	public final String getAuthorName() {
		return _authoName;
	}

	/**
	 * @return Returns the _authorEmail.
	 */
	public final String getAuthorEmail() {
		return _authorEmail;
	}

	/**
	 * @return Returns the _authorWeb.
	 */
	public final String getAuthorWeb() {
		return _authorWeb;
	}

	/**
	 * @return Returns the _description.
	 */
	public final String getDescription() {
		return _description;
	}

	/**
	 * @return Returns the _fileName.
	 */
	public final String getFileName() {
		return _fileName;
	}
	
	public final String getFileMD5Sum(){
	    return _md5;
	}
	/**
	 * @return Returns the _name.
	 */
	public final String getName() {
		return _name;
	}

	/**
	 * @return Returns the _serverUrl.
	 */
	public final String getServerURL() {
		return _serverUrl;
	}

	/**
	 * @return Returns the _size.
	 */
	public final int getSize() {
		return _size;
	}

	/**
	 * @return Returns the _version.
	 */
	public final Version getVersion() {
		return _version;
	}
	
	public final Collection getDependencies(){
		return _dependencies.values();
	}
	
	
	
	public static PluginInformation getPluginInformation(File file, String serverURL,File madkitDirectory){
	    if(file==null || !file.exists()){
	    	debug("Can not Construct PluginInformation: file does not exist");
	        return null;
	    }
	    if(serverURL==null){
	    	debug("Can not Construct a PluginInformation with out a server url\n " +
	    			"Under Windows systems this error might by linked to a mistyping in the plugin name");
	    	return null;
	    }
		try {
			Document doc =DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file);
			Element docelem=doc.getDocumentElement();
			PluginInformation info=new PluginInformation(docelem.getAttribute("name"),Version.valueOf(docelem.getAttribute("version")));
			info._serverUrl=serverURL;
			Element elem;
			NodeList list;
			Text text;

			//set author
			list=doc.getElementsByTagName("author");
			elem=(Element) list.item(0);
			list=elem.getChildNodes();
			for(int i = 0; i < list.getLength();i++){
				String tag=list.item(i).getNodeName();
				text=(Text) list.item(i).getFirstChild();
				if(tag.equals("name")){
					info._authoName=text.getData();
				}else if(tag.equals("email")){
					info._authorEmail=text.getData();
				}else if(tag.equals("web")){
					info._authorWeb=text.getData();
				}
			}
			
			//file props
			list=doc.getElementsByTagName("file");
			elem=(Element) list.item(0);
			if(elem!=null){
				info._fileName=elem.getAttribute("dir")+"/"+elem.getAttribute("name");
				info._size=Integer.valueOf(elem.getAttribute("size")).intValue();
				info._md5=elem.getAttribute("checksum");
			}
			//dependencies
			list=doc.getElementsByTagName("depends");
			elem=(Element) list.item(0);
			list=elem.getElementsByTagName("plugin");
			for(int i=0;i<list.getLength();i++){
				elem=(Element) list.item(i);
				Dependency d=new Dependency(elem.getAttribute("name"),Version.valueOf(elem.getAttribute("version")));
				//System.out.println("dep "+d.toString());
				info._dependencies.put(d.name,d);
			}
			
			//set desciptions
			list=doc.getElementsByTagName("description");
			elem=(Element) list.item(0);
			text=(Text) elem.getFirstChild();
			info._description=text.getData();
			
			//actions
			list=doc.getElementsByTagName("action");
			info._actions=new Vector();
			for(int i=0; i<list.getLength();i++){
			    Element ac=(Element) list.item(i);
			    Action action=Actions.getActionByName(ac.getAttribute("id"),madkitDirectory,info.getName(),info.getVersion());
			    //setParamenters
			    Collection acs=action.getMandatoryParameters();
			    acs.addAll(action.getOptionalParameters());
			    Properties props=new Properties();
			    for (Iterator iter = acs.iterator(); iter.hasNext();) {
                    String element = (String) iter.next();
                    if(ac.getAttribute(element)!=null){
                        props.setProperty(element,ac.getAttribute(element));
                    }                   
                }		    
			    if(action!=null){
			        action.setParamenters(props);
			        info._actions.add(action);
			    }
			}
			
			//return the plug info
			return info;
			
		} catch (SAXException e) {
			debug("SAXException caught "+e.getMessage());
		} catch (IOException e) {
			debug("IOException caught "+e.getMessage());
			
		} catch (ParserConfigurationException e) {
			debug("ParserConfigurationException caught "+e.getMessage());
		} catch (FactoryConfigurationError e) {
			debug("FactoryConfigurationError caught "+e.getMessage());
		}
		
		return null;
		
	}

	public final String getDownloadURL(){
		return _serverUrl+"/download.php?file="+_fileName;
	}
	/**
	 * @param string
	 */
	private static void debug(String string) {
		System.err.println(string);
		
	}

    /**
     * @return
     */
    public Collection getActions() {

        return _actions;
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */

}
