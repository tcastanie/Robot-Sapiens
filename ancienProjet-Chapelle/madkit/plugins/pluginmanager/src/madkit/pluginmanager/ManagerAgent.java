/*
 * ManagerAgent.java - Created on Jan 31, 2004
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
 * Last Update: $Date: 2004/06/29 13:50:07 $
 */

package madkit.pluginmanager;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.CellRendererPane;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JToolTip;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolTipUI;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;


import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.StringMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**The ManagerAgent checks for new versions of madkit plugins.
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.8 $
 */
public final class ManagerAgent extends Agent implements UpdateRoles, ObservableProcess{
	private ManagerGUI _gui;
	boolean _alive=true;
	PluginList _list=null;
	
	//config variables
	/**Max number of downloas, 0 mens no limit*/
	private int maxDownloads=0;
	private boolean autoUpdateList=true;
	private boolean informNewVersion=true;
	
	
	//file
	private File configFile;
	private File installedFile;
	private File sourcesFile;
	private File pluginsFile;
	private File _madkitDir;
	private static File _tempDir;
	
	//configuration
	Properties _configuration;
	
	//Agent states
	private boolean _refreshingPlugins=false;
	private int _sourcesCount=0;
	
	private Hashtable _toDownload=new Hashtable();
	private Hashtable _toInstall=null;

	//observable process
	private int _maxGlobal;
	private int _maxCurrent;
	private String _noteCurr=null;
	private int _statusCurr;
	private int _statusGlob;
	private boolean _processFinished=false;
	
	//used by the installer
	boolean _fullInstallRequested=false;
	
	/**Creates a new Plugin Manager Agent.The agent will try to use madkit.dir as home directory or will request
	 * the user for a directory.
	 */
	public ManagerAgent(){
	    this(null,null);
	}
	
	/**Creates a new Plugin Manager Agent.
	 * @param madkitDir Madkit install directory
	 */
	public ManagerAgent(File madkitDir,Properties config) {
		super();
		
		if(madkitDir==null){
			askMadkitHomeDirectory();
		}else{
		    _madkitDir=madkitDir;
		}
		_configuration=config;
		initFiles();
	}

	/**Request the user for the Madkit home Directory
	 * 
	 */
	private void askMadkitHomeDirectory() {
	    //try to guess the directory
	    String sour=System.getProperty("madkit.dir")+File.separatorChar+"cache"+File.separatorChar+"pluginmanager.config";
	    File f=new File(sour);
	    if(f.exists()){
	        
	        File dir=new File(System.getProperty("madkit.dir"));
	        System.out.println("Setting installation dir to "+dir.getAbsolutePath());
	        _madkitDir=dir.getAbsoluteFile();
	        return;
	    }
	    
		JFileChooser fc=new JFileChooser(new File(System.getProperty("user.dir")));
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res=fc.showOpenDialog(new JFrame());
		if(res==JFileChooser.APPROVE_OPTION){
			_madkitDir= fc.getSelectedFile();
			
		}

		
	}

	/**
	 * @see madkit.kernel.Agent#live()
	 */
	public void live(){
		while(_alive){
			try{
				Message m=waitNextMessage(1000);
				exitImmediatlyOnKill();
				if(m!=null){
					if(m instanceof SelectedPluginMessage){
						markForInstall(((SelectedPluginMessage)m).getPluginName());
					}else if(m instanceof DownloadFinished){
						handleFinishedDownload(( DownloadFinished)m);
					}
					pause(100);	
				}
				
					
			}catch(Exception exp){
				String msg="Exception Caught:\n" +
						"Class: "+exp.getClass().getName()+"\n" +
						"Cause: "+exp.getCause()+"\n" +
						"Message: "+exp.getMessage()+"\n" +
						"\nPlease Report this bug.";
				println(msg);
				exp.printStackTrace();
				
				if(_gui!=null){
					JOptionPane.showMessageDialog(_gui,msg,"Exception",JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	/**Checks if a plugin is going to be installed
	 * @param pluginname Plugin Name
	 * @return true if the plugin is scheduled to download, false otherwise. 
	 */
	public boolean isGoingToInstall(String pluginname){
		return _toDownload.containsKey(pluginname);
	}
	
	/**
	 * @param finished
	 */
	private void handleFinishedDownload(DownloadFinished msg) {
		String name=msg.getName();
		System.out.println("handling Finished download "+msg.getName());
		if(name.equals("plugins.zip")){
			if(!checkDownloadStatus(msg)) return; //if the download failed return 
			
			println("Download from "+msg.getServer()+" Finished ");
			_sourcesCount--;
			if(_sourcesCount==0){
				println("All Sources Downloaded");
				println("Merging Sources");
				try {
					PluginList.mergePlugins(msg.getServer(),msg.getFile(),pluginsFile);
					updatePlugins();
					checkForNewPluginManager();
					if(_fullInstallRequested){
						markForInstall("madkit-full");
						startInstall();
					}
				} catch (SAXException e) {
					//logger.debug("SAXException caught ",e);
					debug("SAXException caught "+e.getMessage());
				} catch (IOException e) {
					//logger.debug("IOException caught ",e);
					debug("IOException caught "+e.getMessage());
					e.printStackTrace();
				} catch (ParserConfigurationException e) {
					//logger.debug("ParserConfigurationException caught ",e);
					debug("ParserConfigurationException caught "+e.getMessage());
				} catch (FactoryConfigurationError e) {
					//logger.debug("FactoryConfigurationError caught ",e);
					debug("FactoryConfigurationError caught "+e.getMessage());
				}
			}
			
		}else{
			if(_toInstall==null){_toInstall = new Hashtable();}
			InstallPlugin instplg=(InstallPlugin) _toDownload.remove(name);
			if(_toInstall.containsKey(msg.getName())) return;
			instplg.file=msg.getFile();
			_toInstall.put(name,instplg);
			
			if(!_toDownload.isEmpty()){
				int downloadersCount=getAgentsWithRole(community,group,DOWNLOADER).length;
				if(maxDownloads ==0 || downloadersCount<maxDownloads){
					InstallPlugin ip=(InstallPlugin)_toDownload.elements().nextElement();
					if(!ip.downloading){
						PluginInformation pi=ip.pluginInformation;
						ip.downloading=true;
						startDownlad(pi);
					}
					downloadersCount++;
				}
				incrementCurrentProcessStatus();
				
			}else{//download is finished.. time to install
				boolean successful=doPluginsInstall();
				//all done
				setProcessStatusFinished(true);
				_toDownload=new Hashtable();
				_toInstall=new Hashtable();
				if(hasGUI()){
					_gui.refresh();
					informInstallFinished(successful);
					_gui.setEnabled(true);
				}
				
			}
		}
		
	}
	
	private Collection doPluginsCheckMD5Sum(){
		Vector fail=new Vector();
		// It takes a long time to check the plugins using the MD5 checksum, and 
		// I am not sure that this is necessary... Jacques
//	    setCurrentProcessNote("Checking Downloaded Files MD5...");
//		resetCurrentProgressStatus();
//		
//		for (Iterator it = _toInstall.values().iterator(); it.hasNext();) {
//			InstallPlugin element = (InstallPlugin) it.next();
//			incrementCurrentProcessStatus();
//			if(!checksum(element)){
//				fail.add(element);
//			}
//		} 
		return fail;
	}
	
	
	/**
	 * Installs all downloaded plugins
	 */
	private boolean doPluginsInstall() {
		Collection fail=doPluginsCheckMD5Sum();
		boolean success=true;
		int selected=2;
		if(!fail.isEmpty()){
			String errMsg="<html>MD5 check Failed<br> Some Plugins failed the MD5 check.<br>" +
					"<strong>Options:</strong>" +
					
					"Try to install: The plugin mananger will try to install the plugins any way. This option could crash the manager.<br>" +
					"Remove corrupt plugins: All corrupt files will not be installed. It may cause dependency problems.<br>" +
					"Abort: Stops the installation. </html>";
			Object []options={"Try to install","Remove corrupt plugins","Abort"};
			selected=JOptionPane.showOptionDialog(_gui,errMsg,"MD5 check Failed",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE, null, options, options[2]);
			success=false;
			if(selected==1){
				for (Iterator iter = fail.iterator(); iter.hasNext();) {
					InstallPlugin element = (InstallPlugin) iter.next();
					_toInstall.remove(element.pluginInformation.getName());
				}
				selected=0;
			}
		}else{
			selected=0;
		}
		
		if(selected==0){
			setCurrentProcessNote("Removing old packages...");
			resetCurrentProgressStatus();
			for (Iterator it = _toInstall.values().iterator(); it.hasNext();) {
				InstallPlugin element = (InstallPlugin) it.next();
				doRemove(element.pluginInformation.getName(),element.pluginInformation.getVersion());
				incrementCurrentProcessStatus();
			}

			setCurrentProcessNote("Installing new packages...");
			resetCurrentProgressStatus();
			
			for (Iterator it = _toInstall.values().iterator(); it.hasNext();) {
				InstallPlugin element = (InstallPlugin) it.next();
				try {
					doInstall(element);
					incrementCurrentProcessStatus();
				} catch (ZipException e) {
					//logger.debug("ZipException caught ",e);
					debug("ZipException caught "+e.getMessage());
				} catch (IOException e) {
					//logger.debug("IOException caught ",e);
					debug("IOException caught "+e.getMessage());
				}	
			
			}
			doActions();
			
		}
		return success;
	}

	/**Performs all actions required by the installed plugins
     * 
     */
    private void doActions() {
    	setCurrentProcessNote("Configuring Plugins...");
		resetCurrentProgressStatus();
        for (Iterator it = _toInstall.values().iterator(); it.hasNext();) {
            InstallPlugin element = (InstallPlugin) it.next();
            Collection actions=element.pluginInformation.getActions();
            if(actions!=null && !actions.isEmpty()){
                for (Iterator iter = actions.iterator(); iter.hasNext();) {
                    Action ac = (Action) iter.next();
                    executeAction(ac);
                }
            }
            incrementCurrentProcessStatus();
        }
        
        Actions.finished(_madkitDir);
    }

    /**
     * @param ac
     */
    private void executeAction(Action ac) {
        println("Executing "+ac.getName());
        if(ac.shouldAskUser()){//requires user confirmation??
            int op=JOptionPane.showConfirmDialog(_gui,ac.getQuestionToUser(),"Action Confirmation",JOptionPane.YES_NO_OPTION);
            if(op==JOptionPane.NO_OPTION){
                println("Aborting "+ac.getName()+" by user request");
                return;
            }
        }
        //confirmation given now check if the required plugins are installed
        
        if(!ac.requiredPlugins().isEmpty()){
            for (Iterator it = ac.requiredPlugins().iterator(); it.hasNext();) {
                String reqplg = (String) it.next();
                if(!_list.isInstalled(reqplg,Version.valueOf("0.0.0"))){
                    println("Action "+ac.getName()+" for "+ac.getOwnerPlugin()+" requires "+reqplg+" ... Aborting...");
                    return;
                }
                    
            }
        }
        
        if(!ac.execute()){
            JOptionPane.showMessageDialog(_gui,ac.getFailureReason(),"Action Failed",JOptionPane.ERROR_MESSAGE);
        }
    }

    /**Checks if a download was successfull, if not it will inform the user.
	 * @param msg message containing the information of the concerned download.
	 * @return true if and only if the status of the download is SUCCESS
	 */
	private boolean checkDownloadStatus(DownloadFinished msg) {
		if(msg.getStatus()==DownloadFinished.FAILED){
			String info="<html>Downloading : " +msg.getName()+" Failed.";
			if(msg.getName().equals("plugins.zip")){
			    info+="<br>The Server URL might be incorrect.";
			    info+="<br>You can select a your server in Settings -> Options -> Change Download Source ";
			    info+="<br>Remember you must provide a valid email.<br> You Can change it in Settings -> Options -> Settings ";
			}
			info+="</html>";
			JOptionPane.showMessageDialog(_gui,info);
			return false;
		}
		return true;
	}

	/**Informs the user that the installation of the selected plugins is finished.
	 * 
	 */
	private void informInstallFinished(boolean successful) {
		if(successful){
			JOptionPane.showMessageDialog(_gui,"Plugin Installation finished Successfully","Install",JOptionPane.INFORMATION_MESSAGE);
		}else{
			JOptionPane.showMessageDialog(_gui,"Plugin Installation finished with errors","Install",JOptionPane.WARNING_MESSAGE);
		}
		
	}

	/**
	 * @param element
	 * @return
	 */
	private boolean checksum(InstallPlugin element) {
	    String md5=element.pluginInformation.getFileMD5Sum();
	    if(md5==null || element.file==null){
	        return true;//for bundle packages
	    }
	    println(element.pluginInformation.getName()+": Checking MD5Sum ... ");
	    try {
	        boolean ok=Utils.compareMD5(element.file,md5);
	        if(ok){
	            println("OK");
	        }else{
	            println("Failed");
	        }
            return ok;
        } catch (NoSuchAlgorithmException e) {
            debug("NoSuchAlgorithmException caught "+e.getMessage());
        } catch (FileNotFoundException e) {
            debug("FileNotFoundException caught "+e.getMessage());
        } catch (IOException e) {
            debug("IOException caught "+e.getMessage());
        }
        
        println("CheckSum for plugin "+element.pluginInformation.getName()+"did not match");
		return false;
	}

	/**
	 * @param name
	 * @param file
	 */
	private void doInstall(InstallPlugin installPlugin) throws ZipException, IOException {
		println("Installing "+ installPlugin.pluginInformation.getName());
		Vector instFiles=new Vector();
		if(installPlugin.file!=null && installPlugin.file.exists()){
			File inPosition=new File(_madkitDir.getAbsolutePath()+installPlugin.pluginInformation.getFileName());
			Utils.copyFile(installPlugin.file,inPosition,true);
			ZipFile zip=new ZipFile(inPosition);
			
			Enumeration entries=zip.entries();
			while (entries.hasMoreElements()) {
				ZipEntry element = (ZipEntry) entries.nextElement();
				Vector owners=_list.packagesOfFile(element.getName());
				if(owners.isEmpty()){//is trying to overwrite files??
					if(!element.isDirectory() && !element.getName().equalsIgnoreCase("info/plugin.xml")){
						File f=new File(_madkitDir.getAbsolutePath()+File.separatorChar+element.getName());
						f.getParentFile().mkdirs();//ensure the path exists
						Utils.copyStream(zip.getInputStream(element),new FileOutputStream(f));
						instFiles.add(element.getName());
					}
				}else{
					println(installPlugin.pluginInformation.getName()+" trying to overwrite "+element.getName()+ " which belongs to "+owners.toString());
				}
			}
			zip.close();
			
			inPosition.delete();
		}
		//put the files into the installed files
		_list.installPlugin(installPlugin.pluginInformation.getName(),installPlugin.pluginInformation.getVersion(),instFiles);
		
	}

	/**
	 * @param name
	 */
	private void doRemove(String name, Version version) {
		println("Removing "+name+" ... ");
		if(_list.installedConstains(name)){	
			Collection files=_list.getFilesInstalledPlugin(name);
			for (Iterator it = files.iterator(); it.hasNext();) {
				File element = new File(_madkitDir.getAbsolutePath()+File.separatorChar+(String) it.next());
				element.delete();
			}
			
		}
		Actions.removePlugin(_madkitDir,name,version);
		_list.removePlugin(name);
	}
	
	/**
	 * @param bigDist Full Distribution Zip File
	 */
	void installFromBigLocalDistribution(File bigDist) throws SAXException, IOException, ParserConfigurationException, FactoryConfigurationError{
	    ZipFile bigZip=new ZipFile(bigDist);
	    ZipEntry entry=bigZip.getEntry("plugins.zip");
	    PluginList.mergePlugins("localsystem",PluginList.extractFile(bigZip,entry,getTempDir()),pluginsFile);
	    File tmp=new File(_madkitDir.getAbsolutePath()+File.separatorChar+"cache"+File.separatorChar+"localsystem");
	    tmp.mkdirs();
	    Enumeration enu=bigZip.entries();
	    while (enu.hasMoreElements()) {
            ZipEntry element = (ZipEntry) enu.nextElement();
            if(!element.getName().equals("plugins.zip")){
                tmp=PluginList.extractFile(bigZip,element,getTempDir());
                Utils.copyFile(tmp,new File(_madkitDir.getAbsolutePath()+File.separatorChar+"cache"+File.separatorChar+"localsystem"+File.separatorChar+element.getName()),true);
                tmp.delete();    
            }
            
        }
	    updatePlugins();
	}
	
	/**Installs a madkit plugin from a local file. It does not check
	 * if the file exists, if the file is a madkit plugin zip , etc.
	 * It doesn't check for dependencies either!.
	 * @param zip Local File
	 * @throws ZipException
	 * @throws IOException
	 */
	void installLocalFilePlugin(File zip) throws ZipException, IOException{
	    ZipFile zipFile=new ZipFile(zip);
	    ZipEntry entry=zipFile.getEntry("info/plugin.xml");
	    if(entry==null){
	        JOptionPane.showMessageDialog(_gui,"Zip File is not a Madkit Plugin","Install",JOptionPane.ERROR_MESSAGE);
	        return;
	    }
	    PluginInformation info=PluginInformation.getPluginInformation(PluginList.extractFile(zipFile,entry,getTempDir()),null,_madkitDir);
	    InstallPlugin iplug=new InstallPlugin(info);
	    doInstall(iplug);
	}
	/**
	 * @param information
	 */
	private void startDownlad(PluginInformation information) {
	    if(information.getServerURL().equals("localsystem")){
	        //it is installing from a big local Bundle
	        File f=new File(_madkitDir.getAbsolutePath()+File.separatorChar+"cache"+File.separatorChar+"localsystem"+File.separatorChar+information.getFileName());
	        sendMessage(getAddress(),new DownloadFinished(information.getName(),f,"localsystem"));
	    }else{
	        launchAgent(new DownloadAgent(information,getTempDir()),"downloading-"+information.getName(),false);
	    }
	}
	
	public void startInstall(){
		_gui.setEnabled(false);
		if(getAgentWithRole(community,group,PROGRESS)==null)
			launchAgent(new ProgressAgent(),"progressAgent",true);
		int i;
		if(maxDownloads==0){
			i=Integer.MAX_VALUE;
		}else{
			i=maxDownloads;
		}
		
		///GlobalProcess Dialog
		setGlobalProcessMax(_toDownload.size() * 5);//5 steps : download, checkmd5, remove old, install new, actions
		setCurrentlProcessMax(_toDownload.size());
		
		setCurrentProcessNote("Downloading Plugins...");
		setProcessStatusFinished(false);
		ProcessMonitor gpd=new ProcessMonitor(_gui,this);
		gpd.showProgressDialog();
		
		
		Enumeration enu=_toDownload.keys();
		while (enu.hasMoreElements() && i > 0 ) {
			String element = (String) enu.nextElement();
			InstallPlugin ip=(InstallPlugin) _toDownload.get(element);
			ip.downloading=true;
			PluginInformation pi=(ip).pluginInformation;
			startDownlad(pi);
			i--;
		}
	}

	public void markForInstall(String pluginname){
		InstallPlugin p=new InstallPlugin(_list.getPlugin(pluginname));
		p.dependsOn=InstallPlugin.DIRECT_REQUEST;
		_toDownload.put(pluginname,p);
		addDependencies(p);
		_gui.refresh();
	}
	

	/**
	 * @param p
	 */
	private boolean addDependencies(InstallPlugin p) {
		Vector addedPlugins=new Vector();
		
		for (Iterator it=p.pluginInformation.getDependencies().iterator(); it.hasNext();) {
			Dependency element = (Dependency) it.next();
			if(_list.isInstalled(element.name,element.version)){//if installed no need to re-install
				continue;
			}
			if(_list.isAvailable(element.name,element.version)){ //check if the plugin exists
				addedPlugins.add(element.name);
				if(!_toDownload.containsKey(element.name)){
					markForInstall(element.name);
				}
				
				//return false;
			}else{
				if(hasGUI()){
					_gui.informUnavailablePlugin(element.name,element.version,p.pluginInformation.getName());
				}
			}
		}
		return true;
	}

	private void init(){
		
		try {
			loadConfig();
			updatePlugins();
		} catch (ZipException e) {
			//logger.debug("ZipException caught ",e);
			debug("ZipException caught "+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			//logger.debug("IOException caught ",e);
			debug("IOException caught "+e.getMessage());
			e.printStackTrace();
		}

	}


	/**
	 * 
	 */
	private void initFiles() {
		//erase previous temps
		if(getTempDir().exists())
			getTempDir().delete();
		getTempDir().mkdirs();
		getTempDir().deleteOnExit();
		if(!_madkitDir.exists()){
			if(askDirectoryCreation(_madkitDir)){
				_madkitDir.mkdirs();
			}
		}
		
		File cacheDir=new File(_madkitDir.getAbsolutePath()+File.separatorChar+"cache");
		cacheDir.mkdirs();
		
		configFile=new File(cacheDir.getAbsolutePath()+File.separatorChar+"pluginmanager.config");
		installedFile=new File(cacheDir.getAbsolutePath()+File.separatorChar+"installed.xml");
		pluginsFile= new File(cacheDir.getAbsolutePath()+File.separatorChar+"plugins.zip");
		sourcesFile=new File(cacheDir.getAbsolutePath()+File.separatorChar+"sources.xml");
	}

	/**
	 * 
	 */
	private void updatePlugins() throws ZipException, IOException {
		if(hasGUI()){ _gui.setRefresingState(true);}
		if(!pluginsFile.exists()){//it may be the first time the module is launched.. wait for the user's request.
			debug("no plugins File found");
			return;
		}
		
		if(!installedFile.exists()){
			Utils.writeToFile("<?xml version=\"1.0\"?><plugins></plugins>",new FileOutputStream(installedFile));
		}
		debug("reading plugin list");
		_list=new PluginList(getTempDir(),pluginsFile,installedFile);
		debug("plugin list created");
		try {
			_list.init();
			if(hasGUI()){
				_gui.setRefresingState(false);
			}
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

	public static final File getTempDir(){
		if(_tempDir!=null) return _tempDir;
		_tempDir=new File(System.getProperty("java.io.tmpdir")+File.separatorChar+"madkitTmp"+new Date().getTime());
		if(!_tempDir.exists()){
			_tempDir.mkdirs();
		}
		return _tempDir;
	}
	/**
	 * Downloads the plugins.zip file. Althought previewed for multiple sources, for the moment handles a single
	 * source.
	 */
	void refreshPluginsFromSources() {
		_refreshingPlugins=true;	
		_gui.setRefresingState(true);
		
		Vector sources=getDownloadSources();
		_sourcesCount=sources.size();
		for(int i=0;i<sources.size();i++){
			System.out.println("source : "+sources.elementAt(i));
			DownloadAgent down=new DownloadAgent((String)sources.elementAt(i),"plugins.zip",getTempDir());
			launchAgent(down,"downloading-plugin-list",false);
		}
		
		
		
	}

	/**
	 * @return
	 */
	private Vector getDownloadSources() {
		//check if sourceFile exists
		Vector v=new Vector();
		if(!sourcesFile.exists()){
			askDownloadSource();

		}
		
		try {
			Document doc =DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(sourcesFile);
			NodeList list=doc.getElementsByTagName("source");
			for(int i=0;i<list.getLength();i++){
				Element e=(Element) list.item(i);
				v.add(e.getAttribute("server"));
			}
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
		return v;
	}

	
	/**Ask the user for the download source. It will then write the source to the sources.xml file.
     * 
     */
    void askDownloadSource() {
        String server="http://www.madkit.net/plugins/";
        
        String downServer=JOptionPane.showInputDialog(new JFrame(),"Enter the Download Site.\r (e.g. \"http://www.madkit.net/plugins/\")",server);
        if(downServer==null){
        	return;
        }
        setDownloadSource(downServer);
    }

    /**Sets the Download Souce when installing from internet
	 * @param downServer deonload server url
	 */
	void setDownloadSource(String downServer) {
		try {
        	Utils.writeToFile("<?xml version=\"1.0\"?><sources><source server=\""+downServer+"\"/></sources>",new FileOutputStream(sourcesFile));
        } catch (FileNotFoundException e1) {
        	debug("FileNotFoundException caught "+e1.getMessage());
        }
	}
	
	private void loadConfig(File config) throws FileNotFoundException, IOException{
		boolean reqSettings=false;
		if(_configuration==null){
			_configuration=new Properties();
			reqSettings=true;
		}
		if(config.exists()){
			_configuration.load(new FileInputStream(config));
		}else{
			
		    _configuration.setProperty("http.proxyHost",_configuration.getProperty("http.proxyHost",""));
			_configuration.setProperty("http.proxyPort",_configuration.getProperty("http.proxyPort","8080"));
			_configuration.setProperty("madkit.user.name",_configuration.getProperty("madkit.user.name",""));
			_configuration.setProperty("madkit.user.email",_configuration.getProperty("madkit.user.email",""));
			saveConfig(config);
		    if(reqSettings)_gui.changeSettings();
		}
		System.setProperty("http.proxyHost",_configuration.getProperty("http.proxyHost"));
		System.setProperty("http.proxyPort",_configuration.getProperty("http.proxyPort"));
		System.setProperty("madkit.user.name",_configuration.getProperty("madkit.user.name"));
		System.setProperty("madkit.user.email",_configuration.getProperty("madkit.user.email"));
	}
	
    
	/**Saves the current configuration to a file 
	 * @param config Configuration file
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void saveConfig(File config) throws FileNotFoundException, IOException{
		_configuration.store(new FileOutputStream(config),"Plugin Manager Config File, please do not edit");
	}
	
	void saveConfig(){
	    try {
            saveConfig(configFile);
        } catch (FileNotFoundException e) {
            //logger.debug("FileNotFoundException caught ",e);
            debug("FileNotFoundException caught "+e.getMessage());
        } catch (IOException e) {
            //logger.debug("IOException caught ",e);
            debug("IOException caught "+e.getMessage());
        }
	}
	
	void loadConfig(){
	    try {
            loadConfig(configFile);
        } catch (FileNotFoundException e) {
            //logger.debug("FileNotFoundException caught ",e);
            debug("FileNotFoundException caught "+e.getMessage());
        } catch (IOException e) {
            //logger.debug("IOException caught ",e);
            debug("IOException caught "+e.getMessage());
        }
	}
	
	/**Asks to create the installation directory
	 * @param madkitDir
	 * @return
	 */
	private boolean askDirectoryCreation(File madkitDir) {
		int op=JOptionPane.showConfirmDialog(new JFrame(), "The directory does not exist. Do you want to create it?","Create directory",JOptionPane.OK_CANCEL_OPTION);
		return op==JOptionPane.OK_OPTION;
	}
	
	/**Checks if a login information is available, if not.. ask. ;-)
	 * The Information is saved as System properties.
	 * madkit.user.name for the user name
	 * madkit.user.email for the user's email 
	 */
	private void checkForLoginInfo(){
	    if(System.getProperty("madkit.user.name")==null){
	        changeLoginInformation();
	    }else{
	        if(System.getProperty("madkit.user.email")==null){
	            changeLoginInformation();
	        }
	    }
	    
	}
	
	private void checkForNewPluginManager(){
	    if(_list.status("pluginmanager")==PluginInformation.NEEDS_UPDATE){
	        String msg="New Plugin Manager Version Available\n";
	        msg+="It is recommended to update to the new version before installing or upgrading other plugins\n";
	        msg+="Do you want to update the Plugin Manager?\n";
	        int op=JOptionPane.showConfirmDialog(_gui,msg,"New Plugin Manager Version",JOptionPane.YES_NO_OPTION);
	        if(op==JOptionPane.YES_OPTION){
	            markForInstall("pluginmanager");
	            startInstall();
	        }
	    }else{
	        println("no new version");
	    }
	    
	    
	}
	
	/**
     * Changes the user's login information
     */
    private void changeLoginInformation() {
        if(hasGUI())
        	_gui.changeSettings();
        
    }

    /* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() {
		createGroup(false,community,group,null,null);
		requestRole(community,group,PLUGIN_MANAGER,null);
		init();
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() {
	    try {
	    	sendMessage(getAgentWithRole(community,group,PROGRESS),new StringMessage(String.valueOf(true)));
            saveConfig(configFile);
        } catch (FileNotFoundException e) {
            //logger.debug("FileNotFoundException caught ",e);
            debug("FileNotFoundException caught "+e.getMessage());
        } catch (IOException e) {
            //logger.debug("IOException caught ",e);
            debug("IOException caught "+e.getMessage());
        }
		println("Stopping plugin manager agent");
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#initGUI()
	 */
	public void initGUI() {
		_gui=new ManagerGUI(this);
		setGUIObject(_gui);
	}

	/**
	 * @param plugname
	 */
	void showDetails(String plugname) {
		DetailsAgent detAgent=new DetailsAgent(_list.getPlugin(plugname));
		launchAgent(detAgent,"Details-"+plugname,true);
		
	}

	/**
	 * @param string
	 */
	public void removeFromInstall(String string) {
		_toDownload.remove(string);
	}

	/**
	 * 
	 */
	public void upgrade() {
		Enumeration enu=_list.getPluginNames();
		while (enu.hasMoreElements()) {
			String plg = (String) enu.nextElement();
			
			if(_list.status(plg)==PluginInformation.NEEDS_UPDATE){
				markForInstall(plg);
			}
			
		}
		if(hasGUI()){
			_gui.refresh();
		}
	}
	
	///////////////////////////////////
	///Observable Process Interface
	//////////////////////////////////
	
	/* (non-Javadoc)
	 * @see madkit.pluginmanager.ObservableProcess#getGlobalProcessNote()
	 */
	public String getGlobalProcessNote() {
		return "Global Install Status";
	}

	/* (non-Javadoc)
	 * @see madkit.pluginmanager.ObservableProcess#getCurrentProcessNote()
	 */
	public String getCurrentProcessNote() {
		if(_noteCurr==null){
			_noteCurr="";
		}
		return _noteCurr;
	}

	/* (non-Javadoc)
	 * @see madkit.pluginmanager.ObservableProcess#getGlobalProcessStatus()
	 */
	public int getGlobalProcessStatus() {
		return _statusGlob;
	}

	/* (non-Javadoc)
	 * @see madkit.pluginmanager.ObservableProcess#getCurrentProcessStatus()
	 */
	public int getCurrentProcessStatus() {
		return _statusCurr;
	}

	/* (non-Javadoc)
	 * @see madkit.pluginmanager.ObservableProcess#getGlobalProcessMax()
	 */
	public int getGlobalProcessMax() {
		return _maxGlobal;
	}

	/* (non-Javadoc)
	 * @see madkit.pluginmanager.ObservableProcess#getCurrentProcessMax()
	 */
	public int getCurrentProcessMax() {
		return _maxCurrent;
	}
	
	public boolean isGlobalProcessFinished(){
		return _processFinished;
	}

	private void setCurrentProcessNote(String note){
		_noteCurr=note;
	}
	
	private void setGlobalProcessMax(int max){
		_maxGlobal=max;
	}
	
	private void setCurrentlProcessMax(int max){
		_maxCurrent=max;
	}
	
	private void incrementCurrentProcessStatus(){
		_statusCurr++;
		_statusGlob++;
	}
	
//	private void incrementGlobalProcessStatus(){
//		_statusGlob++;
//	}

	private void resetCurrentProgressStatus(){
		_statusCurr=0;
	}
	
	private void setProcessStatusFinished(boolean f){
		_processFinished=f;
	}

	/**
	 * 
	 */
	public void setFullInstallRequest() {
		_fullInstallRequested=true;
		
	}
}

class ManagerGUI extends JPanel{
	
	public final static String NEEDS_UPDATE="Needs Update";
	public final static String UP_TO_DATE="Up to date";
	public final static String NEW="New";
	
	private ManagerAgent _agent;
	private JTabbedPane _tabpane;
	private Hashtable _categoriesTables=new Hashtable();
	private Collection _buttons=new Vector();
	
	public ManagerGUI(ManagerAgent agent){
		_agent=agent;
		setLayout(new BorderLayout());
		
		//toolbar
		JToolBar bar=new JToolBar();
		JMenuBar menubar=new JMenuBar();
		JMenu sett=new JMenu("Settings");
		
		JMenuItem reload=new JMenuItem("Update Plugin List");
		reload.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				_agent.refreshPluginsFromSources();
				
			}
		});
		JMenu options=new JMenu("Options");
		
		JMenuItem source=new JMenuItem("Change Download Source");
		
		source.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                _agent.askDownloadSource();
                
            }
		});
		options.add(source);
		
		
		JMenuItem settitem=new JMenuItem("Settings");
		settitem.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                changeSettings();
            }

            
		});
		
		options.add(settitem);
		
		sett.add(reload);
		sett.add(options);
		menubar.add(sett);
		
		JMenu tools=new JMenu("Tools");
		final JCheckBox showProg=new JCheckBox("Show Download Progress",true);
		showProg.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				boolean hide=!showProg.isSelected();
				AgentAddress pa=_agent.getAgentWithRole(UpdateRoles.community,UpdateRoles.group,UpdateRoles.PROGRESS);
				if(pa!=null){
					_agent.sendMessage(_agent.getAgentWithRole(UpdateRoles.community,UpdateRoles.group,UpdateRoles.PROGRESS),new StringMessage(Boolean.toString(hide)));
				}else{
					if(!hide)
						_agent.launchAgent(new ProgressAgent(),"progressAgent",true);
				}
			}
		});
		
		JMenu menu=new JMenu("Install From Local System");
		
		JMenuItem item=new JMenuItem("Single Plugin");
		item.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                loadLocalPluginFile(false);
                
            }
		});
		
		menu.add(item);
		
		item=new JMenuItem("Full Madkit Bundle");
		item.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent e) {
                loadLocalPluginFile(true);
                
            }
		});
		
		menu.add(item);
		
		tools.add(showProg);
		tools.add(menu);
		menubar.add(tools);
		
		bar.add(menubar);
		
		add(bar,BorderLayout.NORTH);
		
		//Tabs
		_tabpane=new JTabbedPane();
		
		
		add(_tabpane,BorderLayout.CENTER);
		//buttons
		
		JPanel btnPanel = new JPanel();
		
		JButton install=new JButton("Install");
		_buttons.add(install);
		install.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				_agent.startInstall();
				
			}
		});
		
		
		JButton details=new JButton("Details");
		_buttons.add(details);
		details.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				String cat=_tabpane.getTitleAt(_tabpane.getSelectedIndex());//get the selected category
				JTable table=(JTable) _categoriesTables.get(cat);//get the table model
				int r=table.getSelectedRow();
				if(r<0) return;
				PluginsTableModel model=(PluginsTableModel) table.getModel();
				String plugname=(String) model.getValueAt(r,0);
				_agent.showDetails(plugname);
				
			}
		});
		
		
		JButton upgrade=new JButton("Upgrade");
		_buttons.add(upgrade);
		upgrade.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				_agent.upgrade();
				
			}
		});
		
		JButton close=new JButton("Quit");
		close.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				_agent._alive=false;
				
			}
		});
		
		
		btnPanel.add(install);
		btnPanel.add(details);
		btnPanel.add(upgrade);
		btnPanel.add(close);
		
		add(btnPanel,BorderLayout.SOUTH);
		
		
		//size
		setSize(400,500);
	}
	
	void loadLocalPluginFile(boolean bigDist){
	    JFileChooser fc=new JFileChooser();
	    int res=fc.showOpenDialog(this);
	    if(res==JFileChooser.APPROVE_OPTION){
	        try {
	            if(bigDist){
	                _agent.installFromBigLocalDistribution(fc.getSelectedFile());
	            }else{
	                _agent.installLocalFilePlugin(fc.getSelectedFile());
	            }
            } catch (ZipException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (FactoryConfigurationError e) {
                e.printStackTrace();
            }
	        
	    }
	}
	/**
     * 
     */
    void changeSettings() {
        SettingsFrame sf=new SettingsFrame();
        int res=sf.showSettings(_agent._configuration);
        if(res==SettingsFrame.OK_OPTION){
            _agent._configuration= sf.getSelectedConfig();
            _agent.saveConfig();/// save the new config
            _agent.loadConfig();/// reload
        }
    }
    
	public void setEnabled(boolean e){
		_tabpane.setEnabled(e);

		Collection tables=_categoriesTables.values();
		for (Iterator it = tables.iterator(); it.hasNext();) {
			JTable element = (JTable) it.next();
			element.setEnabled(e);
		}

		for (Iterator iter = _buttons.iterator(); iter.hasNext();) {
			JButton element = (JButton) iter.next();
			element.setEnabled(e);
		}
	}
	
	public void markUpgrade(){
		Enumeration enu=_categoriesTables.keys();
		while (enu.hasMoreElements()) {
			String element = (String) enu.nextElement();

			JTable table=(JTable) _categoriesTables.get(element);//get the table model
			((PluginsTableModel)table.getModel()).upgrade();		
	
		}
		
	}
	/**
	 * Refreshes all the tables. 
	 */
	public void refresh() {
		Enumeration enu=_categoriesTables.keys();
		while (enu.hasMoreElements()) {
			String element = (String) enu.nextElement();

			JTable table=(JTable) _categoriesTables.get(element);//get the table model
			((PluginsTableModel)table.getModel()).fireTableDataChanged();		
	
		}		
	}

	/**
	 * @param string
	 * @param version
	 * @param string2
	 */
	public void informUnavailablePlugin(String unavailablePluginName, Version neededVersion, String requestingPlugin) {
		JOptionPane.showMessageDialog(this,"Plugin "+unavailablePluginName+" version: "+neededVersion+"" +
															"can not be found. Need by "+requestingPlugin,"Broken Dependency",JOptionPane.ERROR_MESSAGE);
		
	}

	public void setRefresingState(boolean b){
		if(b){
			setEnabled(!b);
			
		}else{
			_tabpane.removeAll();
			loadPlugins();
			setEnabled(!b);
		}
		
	}
	
	public void loadPlugins(){
		if(_agent._list==null) {System.out.println("null plugin list");return;}
		Enumeration cats=_agent._list.getCategories();
		while (cats.hasMoreElements()) {			
			String category = (String) cats.nextElement();
			PluginsTableModel model=new PluginsTableModel(_agent,category);
			JTable table=setupTable(model);
			JScrollPane scp=new JScrollPane();
			scp.setViewportView(table);
			_categoriesTables.put(category,table);
			if(category.equals("bundle")){//if its the bundle category ... goes first
			    _tabpane.add(scp,category,0);
			}else{
				if(!(category.equals("docs")|| category.equals("sources") ))
					_tabpane.add(category,scp);
			}
			
			
		}
	}
	
	private JTable setupTable(PluginsTableModel model){
		JTable table=new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(model);
		CustomTableCellRenderer render= new CustomTableCellRenderer();
		for(int i=1;i<model.getColumnCount();i++){
			TableColumn column=table.getColumnModel().getColumn(i);
			column.setCellRenderer(render);
			
			//table.setDefaultEditor(model.getColumnClass(i),new DefaultCellEditor());
	   }
		return table;
	}
	
}


/////////////////////////////////////////////////////////////////////////////////////////////
/////////// Table Model
///////////////////////////////////////////////////////////////////////////////////////////

class PluginsTableModel extends AbstractTableModel{
	ManagerAgent _agent;
	String _category;
	
	final static int PLUGIN_NAME=0;
	//final static int STATUS=1;
	final static int INSTALL=1;
	final static int DOC=2;
	final static int SRC=3;
	
	private String[] _columnNames={"Plugin",
									//"Status",
									"Binary",
									"Docs",
									"Sources"
									};
	public PluginsTableModel(ManagerAgent agent, String category ){
		_agent=agent;
		_category=category;
	}

	/**
	 * 
	 */
	public void upgrade() {
		_agent.upgrade();
		
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		if(_agent==null  || _agent._list==null)
			return 0;
		else
		return _agent._list.getPluginsByCategory(_category).size();
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		if(_category.equals("bundle")){
			return 2;
		}
		return _columnNames.length;
	}

	/*------------------------------------------------------------------------------------*/
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Object result=null;
		Vector v=new Vector(_agent._list.getPluginsByCategory(_category));
		String pluginName=(String) v.get(rowIndex);
		
		switch (columnIndex) {
			case PLUGIN_NAME :
					result=pluginName.toString();
				break;
			case INSTALL :
				result=new Boolean(_agent.isGoingToInstall(pluginName));
				break;
			case DOC :
				result=new Boolean(_agent.isGoingToInstall(pluginName+"-doc"));
				break;
			case SRC :
				result=new Boolean(_agent.isGoingToInstall(pluginName+"-src"));
				break;
			default :
				break;
		}
		return result;
	}

	int getStatus(String pluginName){
		return _agent._list.status(pluginName);	
	}
	String getPluginNameForColumn(int row, int column){
		String result=(String) getValueAt(row,0);;
		switch (column) {

			case INSTALL :
				result=(String) getValueAt(row,0);
				break;
			case DOC :
				result=(String) getValueAt(row,0)+"-doc";
				break;
			case SRC :
				result=(String) getValueAt(row,0)+"-src";
				break;
			default :
				break;
		}
		return result;
	}
	public String getColumnName(int col){
		return _columnNames[col];
	}
	
	public boolean isCellEditable(int row, int col){
		if(col!=PLUGIN_NAME){
			PluginInformation info=_agent._list.getPlugin(getPluginNameForColumn(row,col));
			if(info==null){
				return false;
			}
			return true;
		}
			
		else
			return false;
	}
	
	public Class getColumnClass(int c) {
	   return getValueAt(0, c).getClass();
   }
	
	public void setValueAt(Object obj, int row, int col){
		if(col!=PLUGIN_NAME){//just doble checking
			if(obj instanceof Boolean){
				String name=(String) getValueAt(row,PLUGIN_NAME);
				boolean install=((Boolean)obj).booleanValue();
				if(col==DOC) name+="-doc";
				if(col==SRC) name+="-src";
				
				if(install)
					_agent.markForInstall(name);
				else
					_agent.removeFromInstall(name);
			}
		}
	}
}



class CustomTableCellRenderer extends JCheckBox  implements TableCellRenderer{
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#createToolTip()
	 */
	public JToolTip createToolTip() {
		
		return new JMultiLineToolTip();
	}
	/**
	 * 
	 */
	public CustomTableCellRenderer() {
		super();
	}
	
		
		public Component getTableCellRendererComponent
				(JTable table, Object value, boolean isSelected,boolean hasFocus, int row, int column){
				PluginsTableModel model=(PluginsTableModel) table.getModel();
				PluginInformation info=model._agent._list.getPlugin(model.getPluginNameForColumn(row,column));
				
				if(info==null){
					setBackground(Color.GRAY);
					setToolTipText("Not Available");
					setEnabled(false);
					return this;
				}
				Color c=Color.WHITE;
				if(column!=PluginsTableModel.PLUGIN_NAME){
					
					c=getColor(table.getValueAt(row,PluginsTableModel.PLUGIN_NAME),column,model);
				}
				setBackground(c);
				setOpaque(true);
				setToolTipText("<html>Colors:<br>" +
						"<table><tbody>" +
						"<tr><td bgcolor=\"white\" width=\"20\"></td><td>Not Installed</td></tr>" +
						"<tr><td bgcolor=\"green\" width=\"20\"></td><td>Installed / Up to Date</td></tr>" +
						"<tr><td bgcolor=\"yellow\" width=\"20\"></td><td>Installed / Needs Update</td></tr>" +
						"</tbody></table>"+
						"</html>");
				setSelected(((Boolean)value).booleanValue());
				return this;
		 }

		
		/**
		 * @param valueAt
		 * @param column
		 */
		private Color getColor(Object valueAt, int column,PluginsTableModel model) {
			Color c=Color.WHITE;
			if(valueAt instanceof String){
				String name=null;
				switch (column) {
					case PluginsTableModel.DOC :
						name=valueAt+"-doc";
						break;
					case PluginsTableModel.SRC :
						name=valueAt+"-src";
						break;

					default :
						name=valueAt.toString();
						break;
				}
				
				switch (model.getStatus(name)) {
					case PluginInformation.NEEDS_UPDATE :
						c=Color.YELLOW;
					
						break;
					case PluginInformation.NEW :
						c=Color.WHITE;
						break;
					case PluginInformation.UP_TO_DATE :
						c=Color.green;
						break;

					default :
						break;
				}
			}
			
			return c;
			
		}

}



/**Provides a simple multiple line Tooltip.<br>You can use html to format your text.
 * @author  Sebastian Rodriguez -- sebastian.rodriguez@utbm.fr
 * @version $Revision: 1.8 $
 */
class JMultiLineToolTip extends JToolTip {

			private static final String uiClassID = "ToolTipUI";
        
			String tipText;
			JComponent component;
        
			public JMultiLineToolTip() {
				updateUI();
				ToolTipManager.sharedInstance().setDismissDelay(1000000);
			}
        
			public void updateUI() {
				setUI(JMultiLineToolTipUI.createUI(this));
			}
        
			public void setColumns(int columns)
			{
					this.columns = columns;
					this.fixedwidth = 0;
			}
        
			public int getColumns()
			{
					return columns;
			}
        
			public void setFixedWidth(int width)
			{
					this.fixedwidth = width;
					this.columns = 0;
			}
        
			public int getFixedWidth()
			{
					return fixedwidth;
			}
        
			protected int columns = 0;
			protected int fixedwidth = 50;
	}



	class JMultiLineToolTipUI extends BasicToolTipUI {
			static JMultiLineToolTipUI sharedInstance = new JMultiLineToolTipUI();
			Font smallFont;                              
			static JToolTip tip;
			protected CellRendererPane rendererPane;
        
			//private static JTextArea textArea ;
			private static JLabel textArea ;
        
			public static ComponentUI createUI(JComponent c) {
				return sharedInstance;
			}
        
			public JMultiLineToolTipUI() {
				super();
			}
        
			public void installUI(JComponent c) {
				super.installUI(c);
				tip = (JToolTip)c;
				rendererPane = new CellRendererPane();
				c.add(rendererPane);
			}
        
			public void uninstallUI(JComponent c) {
					super.uninstallUI(c);
                
				c.remove(rendererPane);
				rendererPane = null;
			}
        
			public void paint(Graphics g, JComponent c) {
				Dimension size = c.getSize();
				textArea.setBackground(c.getBackground());
				rendererPane.paintComponent(g, textArea, c, 1, 1,
																	size.width - 1, size.height - 1, true);
			}
        
			public Dimension getPreferredSize(JComponent c) {
					String tipText = ((JToolTip)c).getTipText();
					if (tipText == null)
							return new Dimension(0,0);
					//textArea = new JTextArea(tipText );
					textArea=new JLabel();
					textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
					//textArea.setFont(smallFont);
					textArea.setText(tipText);
					rendererPane.removeAll();
					rendererPane.add(textArea );
					//textArea.setWrapStyleWord(true);
					int width = ((JMultiLineToolTip)c).getFixedWidth();
					int columns = ((JMultiLineToolTip)c).getColumns();
                
					if( columns > 0 )
					{
							//textArea.setColumns(columns);
							textArea.setSize(0,0);
							//textArea.setLineWrap(true);
							textArea.setSize( textArea.getPreferredSize() );
					}
					else if( width > 0 )
					{
							//textArea.setLineWrap(true);
							Dimension d = textArea.getPreferredSize();
							d.width = width;
							d.height++;
							textArea.setSize(d);
					}
					else{
						//textArea.setLineWrap(false);
					}
							


					Dimension dim = textArea.getPreferredSize();
                
					dim.height += 1;
					dim.width += 1;
					return dim;
			}
        
			public Dimension getMinimumSize(JComponent c) {
				return getPreferredSize(c);
			}
        
			public Dimension getMaximumSize(JComponent c) {
				return getPreferredSize(c);
			}
	}


