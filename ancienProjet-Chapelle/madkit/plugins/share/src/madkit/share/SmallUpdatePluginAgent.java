package madkit.share;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.Vector;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;

import java.awt.*;
import javax.swing.*;


//class SmallPluginShareAgentPanel extends JPanel {
//	SmallUpdatePluginAgent agent;
//	
//	SmallPluginShareAgentPanel(SmallUpdatePluginAgent ag){
//		agent = ag;
//	}
//}

/*===============================================================================================*/
/*==================================== Class SmallUpdatePluginAgent =============================*/
/*===============================================================================================*/
public class SmallUpdatePluginAgent extends Agent
{
   
    String pluginsPath;
    
	//SmallPluginShareAgentPanel display;

    /*=======================================================================*/
    /*============================== Attributs ==============================*/
    AgentAddress[] agList;  
    PluginShareAgent shareAgent;
    int timeout;
    boolean live;
    Calendar limitDate; 
    Calendar currentDate;
    ListPluginInfo listPluginInfo;
    Vector stockUpdatedPlugin; 
    int nbServer;

    /*=======================================================================*/
    /*============================= Constructor =============================*/
    public SmallUpdatePluginAgent(AgentAddress[] _agList, PluginShareAgent _shareAgent){
	agList=_agList;
	shareAgent=_shareAgent;
	stockUpdatedPlugin=new Vector();
	timeout=30;
	nbServer = agList.length;
	pluginsPath=shareAgent.madkitDirectory+File.separator+"plugins";
    }

//	public void initGUI() {
//				display = new SmallPluginShareAgentPanel(this);
//				setGUIObject(display);
//			}


    /*=======================================================================*/
    /*======================= Activation de l'agent==========================*/
    public void activate(){
	System.out.println("(SmallUpdatePlugin) created");
	String groupName = shareAgent.getGroupName();
	createGroup(true,groupName,null,null);
	requestRole(groupName,"SmallUpdatePlugin",null);
	live=true;

	sendAllRequestUpdatePluginMessage();
	limitDate = Calendar.getInstance();
	limitDate.add(Calendar.SECOND, timeout);
    }


    /*========================================================================*/
    /*======================= Boucle principale de l'agent ===================*/
    public void live(){
	while(live)
	    {
		exitImmediatlyOnKill();
		checkTimeout();
		if (!isMessageBoxEmpty()){
		    Message m = waitNextMessage();
		    --nbServer;
		    try {handleMessage(m);}
		    catch(IOException exc){System.out.println("Error : SmallUpdatePluginAgent --> HandleMessage");}	
		}
		if(nbServer<=0){
		    live=false;
		}
		else{
		    pause(1000);
		}	
	    }
    }
    

    /*========================================================================*/
    /*============================ Mort de l'agent ===========================*/
//    public void end(){ 
//	if(!stockUpdatedPlugin.isEmpty())
//	    shareAgent.showUpdatedPlugin(stockUpdatedPlugin);
//    }

    /*=======================================================================*/
    /*======================= Traitement des messages =======================*/
    void handleMessage(Message m) throws IOException{
	if(m instanceof PluginInfoMessage){
	    AgentAddress serverAddress = ((PluginInfoMessage)m).getSender();
	    String serverName = ((PluginInfoMessage)m).getSender().getKernel().getHost().toString();

	    limitDate = Calendar.getInstance();
	    limitDate.add(Calendar.SECOND, timeout);
	    
	    listPluginInfo = ((PluginInfoMessage)m).getListPluginInfo();
	    checkUpdatePlugin(serverName,serverAddress);

		//if(!stockUpdatedPlugin.isEmpty())
		shareAgent.showUpdatedPlugin(serverAddress, stockUpdatedPlugin);
	    
	    System.out.println("(SmallUpdatePlugin) PluginInfoMessageReceived");
	}
	
    }

    /*=======================================================================*/
    /*======================== Methodes annexes =============================*/

    public void checkUpdatePlugin(String serverName,AgentAddress serverAddress){

	for(int i=0; i<listPluginInfo.size();i++){
	    comparePlugin(listPluginInfo.elementAt(i),serverName,serverAddress);
	}
    }

    /*==============================================================================================*/
    public void comparePlugin(PluginInfo pluginInfo,String serverName,AgentAddress serverAddress){
	String pluginName = pluginInfo.pluginName;
	PluginVersionNumber serverPluginVersion = pluginInfo.pluginVersion;
	String virtualPath = pluginInfo.virtualPath;
	
	File dirPlugins = new File(pluginsPath);	
	File[] listPlugins = dirPlugins.listFiles();
	File plugin;

	boolean findPlugin = false;
	
	if (listPlugins != null){
	    for(int i=0; i<listPlugins.length; i++){
		plugin=listPlugins[i];
		if((plugin.getName()).equals(pluginName)){
		    findPlugin=true;
		    try{
			File pluginProperties = new File(pluginsPath+File.separator+pluginName+File.separator+pluginName+".properties");
			
			if(pluginProperties.exists()){
			    RandomAccessFile raf = new RandomAccessFile(pluginProperties,"r");
			    String line;
			    while ( (line = raf.readLine()) != null ){
				int start;
				if( (start = line.indexOf("madkit.plugin.version=")) != -1){
				    start = line.indexOf("=");
				    PluginVersionNumber pluginVersion = new PluginVersionNumber(line.substring(start+1));
				    if(pluginVersion.isSmaller(serverPluginVersion)){
					UpdatedPlugin upPlugin = new UpdatedPlugin(pluginName,pluginVersion,serverPluginVersion,virtualPath,serverAddress);
					stockUpdatedPlugin.add(upPlugin);
				    }
				}
			    }
			    raf.close();
			}
		    }
		    catch(IOException ioe){System.out.println("(Client) Can't read "+pluginName+".properties");}
		}
	    }
	}
	if(!findPlugin){
	    UpdatedPlugin upPlugin = new UpdatedPlugin(pluginName,new PluginVersionNumber("0"),serverPluginVersion,virtualPath,serverAddress);
	    stockUpdatedPlugin.add(upPlugin);
	}
    }
    /*==============================================================================================*/
    public void checkTimeout(){
	currentDate = Calendar.getInstance();
	if(currentDate.after(limitDate)){
	    live=false;
	    System.out.println("SmallUpadtePLuginAgent --> timeout");
	}
    }
    /*==============================================================================================*/
    public void sendAllRequestUpdatePluginMessage(){
	for(int i=0;i<agList.length;i++)
	    sendMessage(agList[i], new RequestPluginInfoMessage());
    }
}
