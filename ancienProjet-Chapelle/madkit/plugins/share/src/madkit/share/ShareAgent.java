package madkit.share;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JRootPane;

import madkit.TreeTools.DirEntry;
import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;

/*===============================================================================================*/
/*==================================== Class ShareAgent =========================================*/
/*===============================================================================================*/
public class ShareAgent extends AbstractShareAgent {

	/*=======================================================================*/
	/*============================== Attributs ==============================*/
//	JRootPane display;
//	DirEntry s; //Server Tree
//	FileSave saveFic; //File where stockPaquetOnqueue is saved
//	FileMaj majFic; //File where MAJ files are
//	String groupName = "madkit_update";
//	String serverRoleName = "sender";
//	
//	public String madkitDirectory; //see initDir method
//	AgentAddress[] agList; //List of Server Agent
//	int nbrSmallShareAgent = 0;
//	boolean live;
//
//	int timeout = 60 * 3;
//	int maxSize = 1048576 / 2;
//
//	Vector stockPaquetOnQueue = new Vector(); //List of PaquetOnQueue
//	Vector stockUpdatedFile = new Vector(); //List of Update Files
//	Vector smallShareAgentList = new Vector(); //List of SmallShareAgent

	/*===================================================================================*/
	/*================================== Accesseurs =====================================*/
//	public String getGroupName() {
//		return groupName;
//	}
//	public void setGroupName(String n) {
//		groupName = n;
//	}
//	public JRootPane getDisplay() {
//		return display;
//	}
//	public void setLive(boolean b) {
//		live = b;
//	}
//
//	public void setTimeout(int t) {
//		timeout = t;
//	}
//	public void setMaxSize(int s) {
//		maxSize = s;
//	}
//	public int getTimeout() {
//		return timeout;
//	}
//	public int getMaxSize() {
//		return maxSize;
//	}
//	public FileMaj getFileMaj() {
//		return majFic;
//	}

	/*===================================================================================*/
	/*======================= Initialisation de l'interface graphique ===================*/
	public void initGUI() {
		display = new ShareAgentPanel(this);
		setGUIObject(display);
	}

	/*=======================================================================*/
	/*======================= Activation de l'agent==========================*/
//	public void activate() {
//		createGroup(true, groupName, null, null);
//		requestRole(groupName, "receiver", null);
//		live = true;
//
//		agList = getAgentsWithRole(groupName, serverRoleName);
//		initDir();
//		loadSettings();
//
//		//previous download aborded
//		saveFic = new FileSave(this, madkitDirectory + File.separator + groupName + File.separator + "save.ini");
//		stockPaquetOnQueue = saveFic.load();
//
//		if (!(stockPaquetOnQueue.isEmpty())) {
//			createSmallShareAgentForResumeFile();
//		}
//
//		//Verification des mises a jour a effectuer
//		majFic =
//			new FileMaj(madkitDirectory + File.separator + groupName + File.separator + "maj.ini");
//		stockUpdatedFile = majFic.load();
//	}

	/*========================================================================*/
	/*======================= Boucle principale de l'agent ===================*/
//	public void live() {
//		while (live) {
//			exitImmediatlyOnKill();
//			Message m = waitNextMessage();
//			handleMessage(m);
//		}
//	}

//	public void end() {
//		for (int i = 0; i < smallShareAgentList.size(); i++) {
//			killAgent((SmallShareAgent) smallShareAgentList.elementAt(i));
//		}
//		majFic.lastUpdate(stockUpdatedFile);
//		saveFic.update(stockPaquetOnQueue);
//		leaveRole(groupName, "receiver");
//		leaveGroup(groupName);
//	}

	/*=======================================================================*/
	/*======================= Traitement des messages =======================*/
	void handleMessage(Message m) {
		if (m instanceof TreeMessage) {
			AgentAddress serverAddress = ((TreeMessage) m).getSender();
			System.out.println("(client) TreeMessage received");
			String serverName =
				((TreeMessage) m).getSender().getKernel().getHost().toString();
			s = ((TreeMessage) m).getDir();
			Vector v = s.getVect();
			((ShareAgentPanel) display).addServerPanel(
				s,
				((TreeMessage) m).getSender());
			//checkPlugins(((TreeMessage)m).getPluginsInfo(), serverName,serverAddress);
			checkUpdatedFile(s, ((TreeMessage) m).getSender());
		}
		else 
			super.handleMessage(m);

	} //End Handle

	/*   public void windowClosing(WindowEvent we)
	   {
		saveFic.update(stockPaquetOnQueue);
		System.exit(0);
	   } */

	/*===============================================================================================*/
	/*==================================== Methodes Annexes =========================================*/
	/*===============================================================================================*/
	protected void executeServer(AgentAddress addr){
		sendMessage(addr, new RequestTreeMessage("request-tree"));
	}
	
//	public void initDir() {
//
//		/* File currentDirectory = new File(System.getProperty("user.dir"));
//		if( ((currentDirectory.getParentFile()).getName()).equals("plugins") )
//		    madkitDirectory=(currentDirectory.getParentFile()).getParent();
//		else madkitDirectory=currentDirectory.getParent(); */
//		madkitDirectory = System.getProperty("madkit.dir");
//		File f = new File(madkitDirectory + File.separator + groupName);
//		f.mkdir();
//		f = new File(madkitDirectory + File.separator + groupName + File.separator + "Incoming");
//		f.mkdir();
//	}
	/*========================================================================================*/
//	public void checkPlugins(AgentAddress[] agList) {
//		SmallUpdatePluginAgent supa = new SmallUpdatePluginAgent(agList, this);
//		launchAgent(supa, "SmallUpdatePluginAgent", false);
//	}
	/*========================================================================================*/
//	public void addPaquetOnQueue(Vector v) {
//		for (int i = 0; i < v.size(); i++) {
//			stockPaquetOnQueue.add(v.elementAt(i));
//		}
//		saveFic.update(stockPaquetOnQueue);
//	}
	/*========================================================================================*/
//	public void addUpdatedFile(Vector v) {
//		for (int i = 0; i < v.size(); i++) {
//			stockUpdatedFile.add(v.elementAt(i));
//		}
//	}
	/*==============================================================================================*/
//	public boolean removePaquetOnQueue(
//		String p,
//		AgentAddress a,
//		int s,
//		int e) {
//		for (int i = 0; i < stockPaquetOnQueue.size(); i++) {
//			//System.out.println("Ask UpdateSaveFic");
//			if (p.equals(((PaquetOnQueue) stockPaquetOnQueue.elementAt(i)).getPath()))
//				if (s == ((PaquetOnQueue) stockPaquetOnQueue.elementAt(i)).getOffsetStart())
//					if (e == ((PaquetOnQueue) stockPaquetOnQueue.elementAt(i)).getOffsetEnd()) {
//						stockPaquetOnQueue.removeElementAt(i);
//						saveFic.update(stockPaquetOnQueue);
//						return true;
//					}
//		}
//		return false;
//	}
	/*==============================================================================================*/
//	public boolean removeSmallShareAgent(SmallShareAgent ssa) {
//		for (int i = 0; i < smallShareAgentList.size(); i++) {
//			if (ssa.equals((SmallShareAgent) smallShareAgentList.elementAt(i))) {
//				smallShareAgentList.removeElementAt(i);
//				if (!ssa.getUpdatePlugin())
//					showUpdateTable();
//				return true;
//			}
//		}
//		return false;
//	}
	/*==============================================================================================*/
//	public void AskUserUpdatedFile(
//		String virtualPath,
//		String serverName,
//		long lastModified) {
//		boolean alreadyUpdated = false;
//		for (int j = 0; j < stockUpdatedFile.size(); j++) {
//			if ((((UpdatedFile) stockUpdatedFile.elementAt(j)).getPath()).equals(virtualPath)
//				&& (((UpdatedFile) stockUpdatedFile.elementAt(j)).getServerName()).equals(serverName)) {
//				alreadyUpdated = true;
//				stockUpdatedFile.remove(j);
//				stockUpdatedFile.addElement(new UpdatedFile(virtualPath, serverName, lastModified));
//				//majFic.update(stockUpdatedFile);
//				continue;
//			}
//		}
//
//		if (!alreadyUpdated) {
//			UpdatedFile uf = new UpdatedFile(virtualPath, serverName, lastModified);
//			stockUpdatedFile.addElement(uf);
//			//majFic.update(stockUpdatedFile);
//		}
//	}

	/*==============================================================================================*/
//	public void checkUpdatedFile(DirEntry dir, AgentAddress serverAddress) {
//		long res;
//		Date currentDate;
//		for (int i = 0; i < stockUpdatedFile.size(); i++) {
//			//if( (serverAddress.getKernel().getHost().toString()).equals(((UpdatedFile)stockUpdatedFile.elementAt(i)).getServerName())){
//			res = dir.isThereFile(((UpdatedFile) stockUpdatedFile.elementAt(i)).getPath(),"");
//			//System.out.println("apres l'appel à isThereFile, res = "+res);
//			if (res != 0) {
//				//System.out.println("res : "+res+" lastModified : "+((UpdatedFile)stockUpdatedFile.elementAt(i)).getLastModified());
//				if (res > ((UpdatedFile) stockUpdatedFile.elementAt(i)).getLastModified()) {
//					int ret = JOptionPane.showConfirmDialog(null, "Would you like to update file \""
//								+ ((UpdatedFile) stockUpdatedFile.elementAt(i)).getPath()
//								+ "\" ?", "Updated File", JOptionPane.YES_NO_OPTION);
//					if (ret == 0) {
//						sendMessage(serverAddress, new FicMessage(((UpdatedFile) stockUpdatedFile.elementAt(i)).getPath()));
//					}
//				}
//			}
//			//}
//		}
//	}
	/*==============================================================================================*/
//	public void createSmallShareAgentForResumeFile() {
//		Vector stockTempPaquetOnQueue = new Vector();
//
//		AgentAddress tempServerAddress =
//			((PaquetOnQueue) stockPaquetOnQueue.elementAt(0)).getAddress();
//
//		for (int i = 0; i < stockPaquetOnQueue.size(); i++) {
//			if (tempServerAddress
//				.equals(
//					((PaquetOnQueue) stockPaquetOnQueue.elementAt(i))
//						.getAddress())) {
//				stockTempPaquetOnQueue.add(
//					(PaquetOnQueue) stockPaquetOnQueue.elementAt(i));
//			} else {
//				SmallShareAgent ssa =
//					new SmallShareAgent(stockTempPaquetOnQueue, this);
//				launchAgent(ssa, "smallShareAgent" + nbrSmallShareAgent, false);
//				nbrSmallShareAgent++;
//				smallShareAgentList.add(ssa);
//				stockTempPaquetOnQueue.clear();
//				tempServerAddress =
//					((PaquetOnQueue) stockPaquetOnQueue.elementAt(i))
//						.getAddress();
//			}
//		}
//		SmallShareAgent ssa = new SmallShareAgent(stockTempPaquetOnQueue, this);
//		launchAgent(ssa, "smallShareAgent" + nbrSmallShareAgent, false);
//		nbrSmallShareAgent++;
//		smallShareAgentList.add(ssa);
//		stockTempPaquetOnQueue.clear();
//	}
	/*==============================================================================================*/
//	public void showUpdateTable() {
//		UpdateTableFrame updateTable =
//			new UpdateTableFrame(stockUpdatedFile, this);
//		updateTable.pack();
//		updateTable.setVisible(true);
//	}
//	/*==============================================================================================*/
//	public void showUpdatedPlugin(Vector stockUpdatedPlugin) {
//		int res =
//			JOptionPane.showConfirmDialog(
//				null,
//				"Updated plugins were found on a server, would you want to update some of them ?",
//				"Plugins Informations",
//				JOptionPane.YES_NO_OPTION);
//
//		if (res == JOptionPane.YES_OPTION) {
//			UpdatePluginTableFrame updatePluginTable =
//				new UpdatePluginTableFrame(stockUpdatedPlugin, this);
//			updatePluginTable.pack();
//			updatePluginTable.setVisible(true);
//		}
//	}
	/*==============================================================================================*/
//	public void loadSettings() {
//		try {
//			String settingsSharePath =
//				madkitDirectory
//					+ File.separator
//					+ groupName
//					+ File.separator
//					+ "share.ini";
//			File f = new File(settingsSharePath);
//			f.createNewFile();
//			RandomAccessFile raf = new RandomAccessFile(f, "r");
//			String element;
//			int cut;
//			while ((element = raf.readLine()) != null) {
//				cut = element.indexOf(" ** ");
//				timeout = Integer.parseInt(element.substring(10, cut));
//
//				element = element.substring(cut + 4);
//				cut = element.indexOf(" ** ");
//				maxSize = Integer.parseInt(element.substring(10, cut));
//			}
//			raf.close();
//		} catch (IOException ioe) {
//			System.out.println("(Client) Can't read share.ini");
//		}
//	}

	/*==============================================================================================*/

} // fin classe
