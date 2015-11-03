/*
 * PluginList.java - Created on Feb 1, 2004
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
 * Last Update: $Date: 2004/06/29 13:44:51 $
 */

package madkit.pluginmanager;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.5 $
 */
public class PluginList {

	private File _dir;
	private File _installedFile;
	private ZipFile _pluginZip;
	private Hashtable _installed;
	private Hashtable _installedFiles;
	private Hashtable _available;
	private Hashtable _categories;
	private Hashtable _hosts;

	/**
	 * 
	 */
	public PluginList(File tmpDir, File pluginZip, File installed) throws ZipException, IOException {
		super();
		
		_dir=tmpDir;
		if(_dir.exists())
			_dir.delete();
		_dir.mkdirs();
		_dir.deleteOnExit();
		
		
		
		_pluginZip=new ZipFile(pluginZip);
		_installed=new Hashtable();
		_installedFiles=new Hashtable();
		_available= new Hashtable();
		_categories=new Hashtable();
		_hosts=new Hashtable();
		_installedFile=installed;
	}
	
	public void init() throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
		createInstalledInfo(_installedFile,null,null,null,null);
		createAvailablePluginInfo();
	}
	
	/**
	 * 
	 */
	private void createAvailablePluginInfo() throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError {
		File f=extractFile(_pluginZip,_pluginZip.getEntry("plugins.xml"),_dir);
		
		Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
		NodeList list=doc.getElementsByTagName("plugin");
		for(int i=0;i<list.getLength();i++){
			Element el=(Element) list.item(i);
			String name=el.getAttribute("name");
			String cat=el.getAttribute("category");
			_hosts.put(name,el.getAttribute("server"));
			Vector v=(Vector) _categories.remove(cat);
			if(v==null){
				v=new Vector();
			}
			v.add(name);
			_categories.put(cat,v);
		}
		
	}

	/**
	 * @param installed
	 */
	private void createInstalledInfo(File installed, String toRemove, String newInstall,Version version, Vector files) throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError {
	
	    
		Document doc=DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(installed);
		if(newInstall!=null){//put the new elements before parsing
			Element e=doc.createElement("plugin");
			e.setAttribute("name",newInstall);
			e.setAttribute("version",version.toString());
			for (Iterator it = files.iterator(); it.hasNext();) {
				String element = (String) it.next();
				Element f=doc.createElement("file");
				f.setAttribute("location",element);
				e.appendChild(f);
			}
			doc.getDocumentElement().appendChild(e);
		}

		NodeList list=doc.getElementsByTagName("plugin");
		for(int i=0;i<list.getLength();i++){
		    
			Element el=(Element) list.item(i);
			String key=el.getAttribute("name");
			
			if(toRemove!=null && key.equals(toRemove)){
			    
				doc.getDocumentElement().removeChild(el);
				list=doc.getElementsByTagName("plugin");
				i=0;
				_installedFiles=new Hashtable();
				_installed=new Hashtable();
				continue;
			}
			
			
			Version ver=Version.valueOf(el.getAttribute("version"));
			NodeList fileList=el.getElementsByTagName("file");
			Vector v=new Vector();
			for(int j=0;j<fileList.getLength();j++){
				Element f=(Element) fileList.item(i);
				if(f!=null){
					v.add(f.getAttribute("location"));
				}
				
			}
			_installedFiles.put(key,v);
			_installed.put(key,ver);
		}
		
		
		if(newInstall!=null || toRemove!=null){//save docuement
			XMLWriter.saveDocument(_installedFile,doc);
			
		}
	
	}

	/**
	 * @param string
	 */
	private static void debug(String string) {
		System.out.println("Debug *** "+string);
		
	}

	public PluginInformation getPlugin(String plugin){
		PluginInformation info=(PluginInformation) _available.get(plugin);
		if(info!=null){
			return info;
		}
		
		File file=getPluginFile(plugin);
		File madkitDir=_installedFile.getParentFile().getParentFile();
		info=PluginInformation.getPluginInformation(file,(String) _hosts.get(plugin),madkitDir);
		if(info!=null)
			_available.put(plugin,info);
		return info;
	}
	
	/**Gets the description file of a plugin.
	 * It uses the _pluginZip file.
	 * @param plugin wanted plugin
	 * @return the description file
	 */
	private File getPluginFile(String plugin){
		File plgFile=new File(_dir.getAbsolutePath()+File.separatorChar+plugin+".xml");
		if(plgFile.exists()){
			return plgFile;
		}
		
		ZipEntry entry=_pluginZip.getEntry(plugin+".xml");
		if(entry==null){
			return null;
		}
		return extractFile(_pluginZip,entry,_dir);
	}
	
	/**Extracts the File represented by the ZipEntry e. 
	 * @param zip ZipFile containing the wanted file
	 * @param e	ZipEntry representing the File
	 * @param tmpDir Temp Directory to use in the extration
	 * @return the File or null if the file does not exist
	 */
	public static File extractFile(ZipFile zip, ZipEntry e, File tmpDir){
		try {
			ZipInputStream in=new ZipInputStream(zip.getInputStream(e));
			if(!tmpDir.exists())
				tmpDir.mkdirs();
			
			File tmp=new File(tmpDir.getAbsolutePath()+File.separatorChar+e.getName());
			if(tmp.exists()){
				tmp.delete();
			}
			tmp.getParentFile().mkdirs();
			copyInputStream(zip.getInputStream(e), new BufferedOutputStream(new FileOutputStream(tmp)));
			return tmp;
		} catch (IOException e1) {
			System.out.println("IOException caught "+e1.getMessage());
		}
		return null;
		
	}
	private static final void copyInputStream(InputStream in, OutputStream out) throws IOException{
		copyInputStream(in,out,true);
	}
	private static final void copyInputStream(InputStream in, OutputStream out,boolean closeStream)
		throws IOException
		{
		  byte[] buffer = new byte[1024];
		  int len;

		  while((len = in.read(buffer)) >= 0)
			out.write(buffer, 0, len);
		  if(closeStream){
			in.close();
			out.close();
		  }
		  
		}

	
	public void clean(){
		_dir.delete();
	}

	public Collection getPluginsByCategory(String category){
		return (Collection) _categories.get(category);
	}
	
	public int status(String plugin){
		Version instver=(Version) _installed.get(plugin);
		if(instver==null){
			return PluginInformation.NEW;
		}
		
		PluginInformation plginfo=(PluginInformation)getPlugin(plugin);
		if(plginfo==null)
			return PluginInformation.NEW;//TODO -- should not check . all should be there
		
		Version avi=(Version) (plginfo).getVersion();
		if(avi==null){
			_available.put(plugin,getPlugin(plugin));
		}
		
		if(instver.isHigher(avi)){
			return PluginInformation.NEEDS_UPDATE;
		}else{
			return PluginInformation.UP_TO_DATE;
		}
	}
	
	public Enumeration getPluginNames(){
		return _available.keys();
	}
	
	public Enumeration getCategories(){
		return _categories.keys();
	}

	/**
	 * @param pluginName
	 * @return
	 */
	public boolean isInstalled(String pluginName, Version version) {
		
		return (_installed.keySet().contains(pluginName)&& _installed.get(pluginName).equals(version));
	}
	
	public static File mergePlugins(String server,File newPlugins,File zipToMerge) throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
		
		File finalDir=zipToMerge.getParentFile();
		finalDir.mkdirs();//ensure the path exists
		
		String pluginsFileName="plugins.xml";
		String tmpDir=ManagerAgent.getTempDir().getAbsolutePath();

		File newTmpDir=new File(tmpDir+File.separatorChar+"new");
		newTmpDir.delete();
		newTmpDir.mkdirs();
		File oldTmpDir=new File(tmpDir+File.separatorChar+"old");
		oldTmpDir.delete();
		oldTmpDir.mkdirs();
		
		File oldZipFile=new File(oldTmpDir.getAbsolutePath()+File.separatorChar+"plugin.zip");
		oldZipFile.delete();//erase old copies
		ZipFile oldPluginsZip=null;
		//oldPluginsZip=new ZipFile(zipToMerge);
		
		if(zipToMerge.exists()){
			if(!Utils.copyFile(zipToMerge,oldZipFile,true)){
				System.out.println("error in copy!!!!");
			}
			zipToMerge.delete();
		}
		
		ZipFile newPluginsZip=new ZipFile(newPlugins);
		
		boolean onlyOneZip=true;
		if(oldZipFile.exists()){
			//oldPluginsZip=new ZipFile(oldZipFile);//tmp until multiple source fixed
			//onlyOneZip=false;//tmp until multiple source fixed
		}else{
			onlyOneZip=true;
		}
		
		ZipOutputStream finalZip=new ZipOutputStream(new FileOutputStream(zipToMerge));
		Vector entries=new Vector();
		if(onlyOneZip){
			File newPluginsXml=extractFile(newPluginsZip,newPluginsZip.getEntry("plugins.xml"),newTmpDir);
			Document newdoc =DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(newPluginsXml);
			Document finaldoc=DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			Element oldE=null;
			
			Element docelem=finaldoc.createElement("plugins");
			NodeList list=newdoc.getElementsByTagName("plugin");
			for(int i=0;i<list.getLength();i++){
				Element e=(Element) list.item(i);
				Merged merged=getMergedElement(oldPluginsZip,oldE,newPluginsZip,e,finaldoc,server);
				docelem.appendChild(merged.mergedElement);
				entries.add(merged);
			}
			
			finaldoc.appendChild(docelem);
			
			createFinalZip(finalZip, entries, finaldoc);
		}else{
			File oldPluginsXml=extractFile(oldPluginsZip,oldPluginsZip.getEntry("plugins.xml"),oldTmpDir);
			File newPluginsXml=extractFile(newPluginsZip,newPluginsZip.getEntry("plugins.xml"),newTmpDir);
			
			Document newdoc =DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(newPluginsXml);
			 DocumentBuilder db=DocumentBuilderFactory.newInstance().newDocumentBuilder();
			 db.setEntityResolver(new EntityResolver(){

				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					return new InputSource( ClassLoader.getSystemResourceAsStream("/madkit/pluginmanager/plugins.dtd"));
				}
			 });
			Document olddoc =db.parse(oldPluginsXml);
			Document finaldoc=DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
			
			Element docelem=finaldoc.createElement("plugins");
			NodeList list=newdoc.getElementsByTagName("plugin");
			//NodeList l2=olddoc.getElementsByTagName("plugin");
			
			for(int i=0;i<list.getLength();i++){
				Element e=(Element) list.item(i);
				Element oldE=olddoc.getElementById(e.getAttribute("name"));
				Merged merged=getMergedElement(oldPluginsZip,oldE,newPluginsZip,e,finaldoc,server);
				docelem.appendChild(merged.mergedElement);
				entries.add(merged);
				//olddoc.removeChild(oldE);
			}
			
			list=olddoc.getElementsByTagName("plugin");
			System.out.println("@@ list =" +list.getLength());
			//TODO FINISH Multiple Source merge
			finaldoc.appendChild(docelem);

			createFinalZip(finalZip, entries, finaldoc);

			System.out.println("In the right track .. ");
			oldZipFile.delete();
			System.exit(0);
		}
		
		
		
		newTmpDir.delete();
		oldTmpDir.delete();
		
		
		return zipToMerge;
	}
	
	private static void createFinalZip(ZipOutputStream finalZip, Vector entries, Document finaldoc) throws IOException {
		//add the new plugins.xml
		ZipEntry entry;
		entry=new ZipEntry("plugins.xml");
		finalZip.putNextEntry(entry);

		OutputFormat format=new OutputFormat(finaldoc);
		format.setEncoding("ISO-8859-1");
		format.setIndenting(true);
		XMLSerializer serial=new XMLSerializer(finalZip,format);
		serial.asDOMSerializer();
		serial.serialize(finaldoc);
		
		finalZip.closeEntry();
		for(int i=0;i<entries.size();i++){
			Merged m=(Merged) entries.elementAt(i);
			entry=new ZipEntry(m.entryName);
			finalZip.putNextEntry(entry);
			copyInputStream(m.origin.getInputStream(entry),finalZip,false);
		}
		
		finalZip.flush();
		finalZip.finish();
		finalZip.close();
	}

	private static Merged getMergedElement(ZipFile olddoc,Element old, ZipFile newdoc, Element newE,Document docToInsertInto,String server){
		Version oldversion;
		if(old==null){
			oldversion=new Version(0,0,0);
		}else{
			oldversion=Version.valueOf(old.getAttribute("version"));
		}
		
		Version newversion=Version.valueOf(newE.getAttribute("version"));
		Element e=docToInsertInto.createElement("plugin");
		
		if(oldversion.isHigher(newversion)){//new version IS  higher than oldversion
			NamedNodeMap atts=newE.getAttributes();
			for(int i=0;i<atts.getLength();i++){
				 Node n=atts.item(i);
				 e.setAttribute(n.getNodeName(),n.getNodeValue());
			}
			
			e.setAttribute("server",server);
			
			return new Merged( newdoc,e,e.getAttribute("desc"));
		}else{//newversion is NOT higher than oldversion => keep the old entry 
			NamedNodeMap atts=old.getAttributes();
			for(int i=0;i<atts.getLength();i++){
				 Node n=atts.item(i);
				 e.setAttribute(n.getNodeName(),n.getNodeValue());
			}

			e.setAttribute("server",server);
			return new Merged(olddoc,e,e.getAttribute("desc"));
		}
		
	}

	/**
	 * @param string
	 * @param version
	 * @return
	 */
	public boolean isAvailable(String pluginName, Version version) {
		PluginInformation info=getPlugin(pluginName);
		if(info==null){
			return false;
		}
		return info.getVersion().equals(version)||version.isHigher(info.getVersion());
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean installedConstains(String name) {
		return _installed.containsKey(name);
	}

	/**
	 * @param name
	 */
	public Collection getFilesInstalledPlugin(String name) {
		Collection c=(Collection) _installedFiles.get(name);;
		if(c==null){
			return new Vector();
		}
		return c;
	}

	/**
	 * @param name
	 */
	public void removePlugin(String name) {
		if(_installed.containsKey(name)){
			try {
				createInstalledInfo(_installedFile, name,null,null,null);
			} catch (SAXException e) {
				//logger.debug("SAXException caught ",e);
				debug("SAXException caught "+e.getMessage());
			} catch (IOException e) {
				//logger.debug("IOException caught ",e);
				debug("IOException caught "+e.getMessage());
			} catch (ParserConfigurationException e) {
				//logger.debug("ParserConfigurationException caught ",e);
				debug("ParserConfigurationException caught "+e.getMessage());
			} catch (FactoryConfigurationError e) {
				//logger.debug("FactoryConfigurationError caught ",e);
				debug("FactoryConfigurationError caught "+e.getMessage());
			}
			_installedFiles.remove(name);
		}
		
	}

	/**
	 * @param string
	 * @return
	 */
	public Vector packagesOfFile(String string) {
		Vector v=new Vector();
		Enumeration enu=_installedFiles.keys();
		while (enu.hasMoreElements()) {
			String element = (String) enu.nextElement();
			if(((Vector)_installedFiles.get(element)).contains(string)){
				v.add(element);
			}
		}
		return v;
	}

	/**
	 * @param string
	 * @param instFiles
	 */
	public void installPlugin(String string, Version version,Vector instFiles) {
		try {
			createInstalledInfo(_installedFile,null,string,version,instFiles);
		} catch (SAXException e) {
			//logger.debug("SAXException caught ",e);
			debug("SAXException caught "+e.getMessage());
		} catch (IOException e) {
			//logger.debug("IOException caught ",e);
			debug("IOException caught "+e.getMessage());
		} catch (ParserConfigurationException e) {
			//logger.debug("ParserConfigurationException caught ",e);
			debug("ParserConfigurationException caught "+e.getMessage());
		} catch (FactoryConfigurationError e) {
			//logger.debug("FactoryConfigurationError caught ",e);
			debug("FactoryConfigurationError caught "+e.getMessage());
		}
	}

    
//
//	/**
//	 * @param string
//	 * @return
//	 */
//	public boolean isMetaPackage(String plugin) {
//		return ((Vector)_categories.get("meta")).contains(plugin);
//		
//	} 
}
class Merged{
	ZipFile origin;
	Element mergedElement;
	String entryName;
	
	public Merged(ZipFile orig,Element elem, String entryName){
		origin=orig;
		mergedElement=elem;
		this.entryName=entryName;
	}
}