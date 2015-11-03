/*
* EditorAgent.java -a NotePad agent, to edit text and send string messages to other agents
* Copyright (C) 1998-2002 Jacques Ferber
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
package madkit.system;

import java.io.*;

import java.util.*;

import madkit.kernel.*;
import madkit.utils.common.*;
import madkit.kernel.Utils;
import madkit.messages.*;

class PluginInformation implements Serializable {
	public final static int UP_TO_DATE=0;
	public final static int NEEDS_UPDATE=1;
	public final static int NEW=2;
	
	
	
	private String name;
	private Version version;
	private int size;
	private File directory;
//	private String fileName;
	private String description;
	private String agentNames;
	
	private Hashtable dependencies=new Hashtable();
	private Collection actions=new Vector();
//	private String serverUrl;
	private String docPath; // DocPath for the manual section
	
	//author data
	private String authorName;
//	private String _authorEmail;
//	private String _authorWeb;
//	private String _md5;
	
	/**
	 * 
	 */
	
	protected PluginInformation(File dir) {
		directory = dir;
	}
	
	public String getDocPath(){return docPath;}

	/**
	 * @return Returns the _authoName.
	 */
	public final String getAuthorName() {
		return authorName;
	}

	/**
	 * @return Returns the _authorEmail.
	 */
//	public final String getAuthorEmail() {
//		return _authorEmail;
//	}

	/**
	 * @return Returns the _authorWeb.
	 */
//	public final String getAuthorWeb() {
//		return _authorWeb;
//	}

	/**
	 * @return Returns the _description.
	 */
	public final String getDescription() {
		return description;
	}
	
	/**
	 * @return Returns the directory where the plugin is located.
	 */
	public final File getDirectory() {
		return directory;
	}

	/**
	 * @return Returns the _fileName.
	 */
//	public final String getFileName() {
//		return fileName;
//	}
	
//	public final String getFileMD5Sum(){
//		return _md5;
//	}
/**
	 * @return Returns the _name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return Returns the _serverUrl.
	 */
//	public final String getServerURL() {
//		return serverUrl;
//	}

	/**
	 * @return Returns the _size.
	 */
	public final int getSize() {
		return size;
	}

	/**
	 * @return Returns the _version.
	 */
	public final Version getVersion() {
		return version;
	}
	
	public final Collection getDependencies(){
		return dependencies.values();
	}
	
	boolean updateInfo(){
		if ((directory != null)&& (directory.isDirectory())){
			name = Utils.getFileNameFromPath(directory.getPath());
			File propFile = new File(directory,name+".properties");
			if (!propFile.isFile()){
				System.out.println("Warning: the plugin "+name+" is not a correct plugin");
				return(false);
			}
			PropertyFile pf = new PropertyFile();
			pf.loadFrom(propFile);
			updateInfo(pf);
			return true;
		}else {
			System.out.println("Warning: the plugin "+getName()+" does not seem to have been correctly installed");
			return false;
		}
	}
		
	void updateInfo(PropertyFile pf){
			name = pf.getProperty("madkit.plugin.name");
			agentNames = pf.getProperty("madkit.plugin.agents");
			authorName = pf.getProperty("madkit.plugin.author");
			version = Version.valueOf(pf.getProperty("madkit.plugin.version"));
			description = pf.getProperty("madkit.plugin.description");
			String depends = pf.getProperty("madkit.plugin.depend");
			String docRef = pf.getProperty("madkit.plugin.docfile");
			if ((docRef != null) && (docRef != "${docfile}")){
				File _docFile = new File(directory,"docs"+File.separator+docRef);
				if (_docFile.isFile()){
					docPath = _docFile.getPath();
				}
			}
			
		}
		
	
	public String toString(){
		return getName()+": "+this.getDescription()+", "+
		this.getAuthorName()+", "+this.getVersion();
	}
}


/**
 * This class represent the different plugins that are in the kernel..
 * 
 * @author J.Ferber
 *
 */
public class PluginAgent extends Agent {
	
	PluginInformation plugin;
	
	public void init(File plugindir){
		if (plugin == null){
			plugin = new PluginInformation(plugindir);
		}
		if (!plugin.updateInfo())
			killAgent(this);
	}
	
	public String getPluginName(){ return plugin.getName();}
	
	public void activate(){
		createGroup(false,"system",null,null);
		requestRole("system","plugin",null);
		
	}
	
	public void live(){
		while (true){
			Message m = waitNextMessage();
			if (m instanceof StringMessage)
				handleStringMessage((StringMessage) m);
			else if (m instanceof ActMessage){
				handleActMessage((ActMessage) m);
			}
		}
	}
	
	void handleStringMessage(StringMessage msg){
		String content = msg.getString();
		StringTokenizer st = new StringTokenizer(content);
		String action = st.nextToken();
		if ("info".equalsIgnoreCase(action))
			sendMessage(msg.getSender(),new StringMessage(plugin.toString()));
		else if ("getdoc".equalsIgnoreCase(action)){
			if (plugin.getDocPath() != null){
				String res = "displayLink£Plugins£"+plugin.getName()+"£"+plugin.getDocPath()+"£"+plugin.getName();
				sendMessage(msg.getSender(),new StringMessage(res));
			}
		}
	}
	
	void handleActMessage(ActMessage msg){
		String action = msg.getAction();
		if ("info".equalsIgnoreCase(action))
			sendMessage(msg.getSender(),new ActMessage("infos",plugin));
	}

}
