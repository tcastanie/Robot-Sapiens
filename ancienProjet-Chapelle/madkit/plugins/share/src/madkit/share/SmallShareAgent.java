package madkit.share;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;

import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/*===============================================================================================*/
/*==================================== Class SmallShareAgent ====================================*/
/*===============================================================================================*/

public class SmallShareAgent extends Agent
{
    AbstractShareAgent shareAgent;
    AgentAddress serverAddress;
    AgentAddress smallServerAddress;

    String clientPath;
    int totalDownloadSize=0;
    int currentDownloadSize=0;

    Vector stockPaquetOnQueue = new Vector();
    Vector stockUpdatedFile = new Vector();
    int maxSize;
    boolean live;
    boolean killBorn=false;
    boolean updatePlugin;
    String pluginName;

    String currentFile="";
    String writeMode="";

    String incomingPluginsPath;
    	
    ProgressBar progressBar=null;               
    int currentBarSize = 0;
	
    Calendar limitDate;                       
    Calendar currentDate;
    int timeout;
    
    String groupName="share"; // The group of the related share agent
	
	
    /*=======================================================================*/
    /*============================= Accesseurs =============================*/
    public int getTimeout() {return timeout;}
    public void setTimeout(int t) {timeout = t;}
    public boolean getUpdatePlugin() {return updatePlugin;}
	
	
    /*=======================================================================*/
    /*============================= Constructor =============================*/
    public SmallShareAgent() {}

    public SmallShareAgent(ListFic listFic, AgentAddress sa,AbstractShareAgent s,boolean _updatePlugin)
    {
	Vector index = listFic.getVector();
	serverAddress = sa;
	shareAgent = s;
	updatePlugin=_updatePlugin;

	incomingPluginsPath=shareAgent.madkitDirectory;
	
	timeout=shareAgent.getTimeout();
	maxSize=shareAgent.getMaxSize();
		
	createPaquet(index,serverAddress);
	shareAgent.addPaquetOnQueue(stockPaquetOnQueue);

	//Mode plugin
	if(updatePlugin){
	    pluginName = parsePluginName(((PaquetOnQueue)stockPaquetOnQueue.elementAt(0)).getPath());
	    File f = new File(clientPath+"/plugins/"+pluginName);	   
	    if(f.exists()){
		File fBackup = new File(clientPath+"/plugins/"+pluginName+"Backup");
		if(fBackup.exists()){
		    eraseDirectory(fBackup);
		    System.out.println("(client) "+pluginName+"Backup deleted"); 
		}
		f.renameTo(fBackup);
		System.out.println("(client) "+pluginName+"Backup created"); 
	    }
	}
    }
    
	
    public SmallShareAgent(Vector stockPOQ,AbstractShareAgent s)
    {
	shareAgent = s;
	updatePlugin=false;
	timeout=shareAgent.getTimeout();
	maxSize=shareAgent.getMaxSize();
	for(int i=0;i<stockPOQ.size();i++){
	    stockPaquetOnQueue.add(stockPOQ.elementAt(i));
	    totalDownloadSize+=((PaquetOnQueue)stockPaquetOnQueue.elementAt(i)).getPaquetSize();
	}
	serverAddress = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(0)).getAddress();
    }

	
    /*=======================================================================*/
    /*======================= Activation de l'agent==========================*/
    public void activate()
    {
	groupName = shareAgent.getGroupName();
	
	createGroup(true,groupName,null,null);
	requestRole(groupName,"SmallShare",null);
	if(killBorn == false){
	    live = true;
	    startDownload();
	}
	else{}
    }


    /*========================================================================*/
    /*======================= Boucle principale de l'agent ===================*/
    public void live()		
    {
	while(live)
	    {
		exitImmediatlyOnKill();
		checkTimeout();
		if (!isMessageBoxEmpty()){
		    Message m = waitNextMessage();
		    try {handleMessage(m);}
		    catch(IOException exc){System.out.println("Error : SmallShareAgent --> HandleMessage");}	
		}
		else{
		    pause(1000);
		}	
	    }

    }
	
    /*========================================================================*/
    /*============================ Mort de l'agent ===========================*/
    public void end()
    {
    	if(!killBorn){
	    this.killAgent(progressBar);
	    sendMessage(smallServerAddress,new EndDownloadMessage());
	}
	//System.out.println("(client) SmallShareAgent killed");	
    }

	
    /*=======================================================================*/
    /*======================= Traitement des messages =======================*/
    void handleMessage(Message m) throws IOException
    {

	if(m instanceof ReturnMessage)
	    {
		//System.out.println("(Smallclient) ReturnMessage received");
		String serverName = ((ReturnMessage)m).getSender().getKernel().getHost().toString();
		smallServerAddress = ((ReturnMessage)m).getSender();
	
		String virtualPath = ((ReturnMessage)m).getPath();
		int offsetStart=((ReturnMessage)m).getOffsetStart();
		int offsetEnd=((ReturnMessage)m).getOffsetEnd();
		byte[] b = ((ReturnMessage)m).getByte();
		long lastModified = ((ReturnMessage)m).getLastModified();
		String clientPath = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(0)).getClientPath();

		progressBar.setText("Download "+virtualPath);
		progressBar.setDownloadSize(currentDownloadSize/1024+"/"+totalDownloadSize/1024+"Ko");
		progressBar.setMessage(null);

		//Creating file downloaded.
		boolean createFile = createFile(virtualPath,offsetStart,offsetEnd,b,clientPath);

		currentBarSize++;
		(progressBar.getBar()).setValue(currentBarSize);
		progressBar.validate();

		//Verification si le fichier est entier et mise a jour de l'historique de telechargement
		int l = stockPaquetOnQueue.size();
		boolean endFic = true;
		for(int i=0; i<l ;i++){
		    PaquetOnQueue paq;
		    paq = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(0));
		    String p = paq.getPath();
		    if(p.equals(virtualPath)){
			endFic = false;
			break;
		    }
		}
		if(endFic && !updatePlugin && createFile)
		    {
			updateHistory(virtualPath,serverName);
			shareAgent.AskUserUpdatedFile(virtualPath,serverName,lastModified);

		    }

		//Demande du paquet suivant
		if( !(stockPaquetOnQueue.isEmpty())){
			PaquetOnQueue paq;
			paq = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(0));
			String p = paq.getPath();
			offsetStart = paq.getOffsetStart();
			offsetEnd = paq.getOffsetEnd();
				
			sendMessage(smallServerAddress,new OffsetMessage(p,offsetStart,offsetEnd));
			limitDate = Calendar.getInstance();
			limitDate.add(Calendar.SECOND, timeout);
		    }
		else {
			if(updatePlugin)
			    addUpdatePluginToUpdatingPluginFile();
			live = false;
			shareAgent.removeSmallShareAgent(this);
			currentBarSize = 0;
			System.out.println("(client) All files have been downloaded");
		    }
	    }
	    
	if(m instanceof KillMessage)
	    {
		//System.out.println("(client) SmallShareAgent killed");
		for(int i=0;i<stockPaquetOnQueue.size();i++)
		    shareAgent.removePaquetOnQueue(
						   ((PaquetOnQueue)stockPaquetOnQueue.elementAt(i)).getPath(),
						   smallServerAddress,
						   ((PaquetOnQueue)stockPaquetOnQueue.elementAt(i)).getOffsetStart(),
						   ((PaquetOnQueue)stockPaquetOnQueue.elementAt(i)).getOffsetEnd());
		live=false;
		shareAgent.removeSmallShareAgent(this);
	    }
    }
	

	
	
    /*==========================================================================*/
    /*============================= Methodes annexes ===========================*/
    public String getGroupName(){
    	return groupName;
    }
    
    public void createPaquet(Vector index, AgentAddress serverAddress)
    {		
    	if(updatePlugin){
	    clientPath=incomingPluginsPath;
	}
	else{
	    //create a file dialog
	    JFileChooser chooser = new JFileChooser();
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    chooser.setDialogTitle("Choose Directory");
	    chooser.setApproveButtonText("Accept");
	    chooser.setCurrentDirectory(new File(shareAgent.madkitDirectory+File.separator+groupName+File.separator+"Incoming"));

	    int returnVal = chooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
		clientPath =  (chooser.getSelectedFile()).getAbsolutePath();
	    }
	    else{
		killBorn=true;
		live=false;
		return;		
	    }    
	}
	clientPath = clientPath.replace('\\','/');       

	for(int i=0;i<index.size();i++)
	    {
		//create file on queue
		int sizeFic = ((FileInfo)index.elementAt(i)).getSizeFic();
		String path = ((FileInfo)index.elementAt(i)).getPath();
		
		totalDownloadSize+=sizeFic;
		
		int nbrPaquet;
		int sizeLastPaq = sizeFic%maxSize;
		if (sizeLastPaq == 0)
		    {nbrPaquet = sizeFic/maxSize;}
		else{nbrPaquet = sizeFic/maxSize + 1;}


		//Ajout des n-1 paquets en queue
		int offsetStart = 0;
		int offsetEnd = 0;
		for(int k=1;k<nbrPaquet;k++)
		    {
			offsetEnd += maxSize;
			PaquetOnQueue paq = new PaquetOnQueue(path,serverAddress,offsetStart,offsetEnd,clientPath);
			stockPaquetOnQueue.addElement(paq);
			offsetStart = offsetEnd;
		    }

		offsetStart = offsetEnd ;
		offsetEnd += sizeLastPaq ;
		PaquetOnQueue paq = new PaquetOnQueue(path,serverAddress,offsetStart,offsetEnd,clientPath);

		stockPaquetOnQueue.addElement(paq);
	    }
    }
    /*==============================================================================================*/
	
    public void startDownload(){
		if(updatePlugin){ 
				progressBar = new ProgressBar(0,"Updating plugin(s) from "+serverAddress.getKernel().getHost().toString(),"Connection ...",this);}
		else{ 
				progressBar = new ProgressBar(0,"Downloading from "+serverAddress.getKernel().getHost().toString(),"Connection ...",this);}
		this.launchAgent(progressBar,serverAddress.getKernel().getHost().toString(),true);
		progressBar.setVisible(true);
		progressBar.validate();
	
		progressBar.setMax(stockPaquetOnQueue.size());
		progressBar.setVisible(true);
		(progressBar.getBar()).setValue(0);
		progressBar.validate();
	
		PaquetOnQueue paq;
		paq = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(0));
		String p = paq.getPath();
		int offsetStart = paq.getOffsetStart();
		int offsetEnd = paq.getOffsetEnd();
		AgentAddress address = paq.getAddress();
		limitDate = Calendar.getInstance();
		limitDate.add(Calendar.SECOND, timeout);
			
		sendMessage(address,new OffsetMessage(p,offsetStart,offsetEnd));
    }   

    /*==============================================================================================*/
    public boolean createFile(String virtual_Path,int offsetStart,int offsetEnd,byte[] b,String clientPath)
    {
	boolean createFile = true;

	//Creation du repertoire si besoin
	int o = virtual_Path.lastIndexOf("/");
	String virtual_Path_Dir;
	
	if (o != -1){
	    virtual_Path_Dir = virtual_Path.substring(0,o);
	    File dir = new File(clientPath+"/"+virtual_Path_Dir);
	    dir.mkdirs();
	}

	//Vérification de l'existence du fichier et boite de dialogue si tel est le cas
	if(offsetStart==0)
	    createFile= !(checkIfFileAlreadyExist(virtual_Path,clientPath));

	if(! (currentFile.equals(clientPath+"/"+virtual_Path) && writeMode.equals("ignore")) ){
	    writeFile(virtual_Path,offsetStart,offsetEnd,b,clientPath);
	}

	//Suppression du paquet dans stockPaquetOnQueue
	if( ! (writeMode.equals("null") ||  writeMode.equals("ignore") ||  writeMode.equals("ignore all")) )
	    if(!stockPaquetOnQueue.isEmpty()){
		stockPaquetOnQueue.removeElementAt(0);						
		shareAgent.removePaquetOnQueue(virtual_Path,smallServerAddress,offsetStart,offsetEnd);
	    }
	return createFile;
    }
    /*==============================================================================================*/
    public void updateHistory(String virtual_Path,String serverName)
    {
	try
	    {
		String date1 = new Date().toString();
		File f = new File(shareAgent.madkitDirectory+File.separator+groupName+File.separator+"history.ini");
	    	if(!f.exists())
	    	    {
	    		f.createNewFile();
			Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
			Element racine = doc.createElement("history");
	    	
			Element elt = doc.createElement("file");
			elt.setAttribute("date", date1);
			elt.setAttribute("path", virtual_Path);
			elt.setAttribute("server-name", serverName);
			racine.appendChild(elt);
		
			doc.appendChild(racine);
			OutputFormat of = new OutputFormat(doc);
			FileWriter writer = new FileWriter(f);
			of.setIndenting(true);
			of.setLineSeparator(System.getProperties().get("line.separator").toString());
			XMLSerializer serial = new XMLSerializer(writer, of);
			serial.asDOMSerializer();
			serial.serialize(doc);
	    	    }
	    	else
	    	    {
		    	Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(f);
		    	Element racine;
	
		    	racine=doc.getDocumentElement();
		    
		    	Element elt = doc.createElement("file");
		    	elt.setAttribute("date", date1);
			elt.setAttribute("path", virtual_Path);
			elt.setAttribute("server-name", serverName);
		    	racine.appendChild(elt);
		    
		    	OutputFormat of = new OutputFormat(doc);
		   	FileWriter writer = new FileWriter(f);
		    	of.setIndenting(true);
		    	of.setLineSeparator(System.getProperties().get("line.separator").toString());
		    	XMLSerializer serial = new XMLSerializer(writer, of);
		    	serial.asDOMSerializer();
		    	serial.serialize(doc);
		    }	
	    }
	catch(Exception e){e.printStackTrace(System.err);}
    }

    /**
	 * check the timeout and asks to reconnect to the server
	 */
	public void checkTimeout(){
		currentDate = Calendar.getInstance();
		if(currentDate.after(limitDate)){
		    int resTimeout = JOptionPane.showConfirmDialog(null, "Error connection on server \""+serverAddress.getKernel().getHost().toString()+"\" !"+"\nRetry ?", "Timeout", JOptionPane.YES_OPTION);
		    if(resTimeout == 0){
				limitDate.add(Calendar.SECOND, timeout);
				this.killAgent(progressBar);
				startDownload();		
		    } 
		    else {
				resTimeout = JOptionPane.showConfirmDialog(null, "Would you like to save infos to resume download", "Timeout", JOptionPane.YES_OPTION);
				if(resTimeout != 0) {
					for(int i=0;i<stockPaquetOnQueue.size();i++) {
						String virtualPath = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(i)).getPath();
						AgentAddress smallServerAddress = serverAddress;
						int offsetStart = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(i)).getOffsetStart();
						int offsetEnd = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(i)).getOffsetEnd();								shareAgent.removePaquetOnQueue(virtualPath,smallServerAddress,offsetStart,offsetEnd);
					}
					stockPaquetOnQueue.clear();
					shareAgent.removeSmallShareAgent(this);
				}
				live=false;						
		    }
		}
    }
    /*==============================================================================================*/
    public void writeFile(String virtual_Path,int offsetStart,int offsetEnd,byte[] b,String clientPath){
	currentDownloadSize+=b.length;
	try{
	    File f = new File(clientPath+"/"+virtual_Path);
	    RandomAccessFile raf = new RandomAccessFile(f,"rw");
	    raf.seek(offsetStart);
	    raf.write(b);
	    raf.close();
	}
	catch(IOException ioe){System.out.println("Error : SmallShareAgent --> can't write file \""+clientPath+"/"+virtual_Path+"\".");}
    }
    
    /*==============================================================================================*/
    public String parsePluginName(String path){
	path=((PaquetOnQueue)stockPaquetOnQueue.elementAt(0)).getPath();
	int cut = path.indexOf("/");
	path = path.substring(cut+1);
	cut = path.indexOf("/");
	return path.substring(0,cut);
    } 
    /*==============================================================================================*/
    public void addUpdatePluginToUpdatingPluginFile(){
	try{
	    File f = new File(shareAgent.madkitDirectory+File.separator+groupName+File.separator+"updatingPlugin.ini");
	    if(!f.exists()){
		f.createNewFile();
		Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().newDocument();
		Element racine = doc.createElement("updated-plugins");
	    	
		Element elt = doc.createElement("plugin");
		elt.setAttribute("plugin-name", pluginName);
		racine.appendChild(elt);
		
		doc.appendChild(racine);
		OutputFormat of = new OutputFormat(doc);
		FileWriter writer = new FileWriter(f);
		of.setIndenting(true);
		of.setLineSeparator(System.getProperties().get("line.separator").toString());
		XMLSerializer serial = new XMLSerializer(writer, of);
		serial.asDOMSerializer();
		serial.serialize(doc);
	    }
	    else
	    	{
		    Document doc = DocumentBuilderFactoryImpl.newInstance().newDocumentBuilder().parse(f);
		    Element racine;
	
		    racine=doc.getDocumentElement();
		    
		    Element elt = doc.createElement("plugin");
		    elt.setAttribute("plugin-name", pluginName);
		    racine.appendChild(elt);
		    
		    OutputFormat of = new OutputFormat(doc);
		    FileWriter writer = new FileWriter(f);
		    of.setIndenting(true);
		    of.setLineSeparator(System.getProperties().get("line.separator").toString());
		    XMLSerializer serial = new XMLSerializer(writer, of);
		    serial.asDOMSerializer();
		    serial.serialize(doc);
		}
	}
	catch(Exception e){e.printStackTrace(System.err);}
    }
    /*==============================================================================================*/
    public boolean checkIfFileAlreadyExist(String virtual_Path, String clientPath){
	boolean fileAlreadyExists = false;
	File f = new File(clientPath+"/"+virtual_Path);
	if(f.exists() && !updatePlugin){
	    Object[] possibilities = {"ignore", "ignore all","replace","replace all","replace with backup","replace all with backup"};
	    writeMode = (String)JOptionPane.showInputDialog( null,
							     "An other file with the same name ("+virtual_Path+") was found\n"+
							     "What would you like to do?",
							     "Warning",JOptionPane.PLAIN_MESSAGE,
							     null, possibilities, "ignore");
	    
	    if(writeMode.equals("ignore") || writeMode.equals("null")){
		currentFile=clientPath+"/"+virtual_Path;
		while(!stockPaquetOnQueue.isEmpty() && virtual_Path.equals( ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(0))).getPath() )){
		    shareAgent.removePaquetOnQueue(((PaquetOnQueue)(stockPaquetOnQueue.elementAt(0))).getPath(),
						   ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(0))).getAddress(),
						   ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(0))).getOffsetStart(),
						   ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(0))).getOffsetEnd());
		    stockPaquetOnQueue.remove(0);
		    totalDownloadSize=totalDownloadSize-((PaquetOnQueue)(stockPaquetOnQueue.elementAt(0))).getPaquetSize();
		    currentBarSize++; 
		    fileAlreadyExists=true;
		}
	    }
	    if(writeMode.equals("ignore all")){
		File temp;
		int size=stockPaquetOnQueue.size();
		int i=0;
		while(i<size){
		    temp = new File(clientPath+"/"+ ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(i))).getPath());
		    if(temp.exists()){
			shareAgent.removePaquetOnQueue(((PaquetOnQueue)(stockPaquetOnQueue.elementAt(i))).getPath(),
						       ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(i))).getAddress(),
						       ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(i))).getOffsetStart(),
						       ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(i))).getOffsetEnd());
			totalDownloadSize=totalDownloadSize-((PaquetOnQueue)(stockPaquetOnQueue.elementAt(i))).getPaquetSize();
			stockPaquetOnQueue.remove(i);
			currentBarSize++;
			size=size-1;
		    }
		    else{
			i++;
		    }
		}  
		fileAlreadyExists=true;
	    }
	    if(writeMode.equals("replace")){
		f.delete();
	    }		 
	    if(writeMode.equals("replace all")){
		File temp;
		for(int i=0;i<stockPaquetOnQueue.size();i++){
		    temp = new File(clientPath+"/"+ ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(i))).getPath());
		    if(temp.exists())
			temp.delete();
		}   
	    }
	    if(writeMode.equals("replace with backup")){
		f.renameTo(new File(clientPath+"/"+virtual_Path+".backup"));	
	    }
	    if(writeMode.equals("replace all with backup")){
		File temp;
		for(int i=0;i<stockPaquetOnQueue.size();i++){
		    temp = new File(clientPath+"/"+ ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(i))).getPath());
		    if(temp.exists()){
			temp.renameTo(new File(clientPath+"/"+ ((PaquetOnQueue)(stockPaquetOnQueue.elementAt(i))).getPath()+".backup"));
		    }
		}
	    }
	}
	return fileAlreadyExists;
    }
    /*==============================================================================================*/
    public void eraseDirectory(File directory){
	File[] listFile=directory.listFiles();
	for(int i=0;i<listFile.length;i++){
 	    File temp = listFile[i]; 
	    if(temp.isDirectory())
		eraseDirectory(temp);
	    temp.delete();
	}
	directory.delete();	
    }
}


