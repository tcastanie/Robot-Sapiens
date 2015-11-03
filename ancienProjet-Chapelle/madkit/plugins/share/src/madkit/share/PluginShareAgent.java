/*
 * Created on 23 juil. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
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

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PluginShareAgent extends AbstractShareAgent {
	/*===================================================================================*/
	
		public PluginShareAgent(){
			super();
			setGroupName("madkit_update");
		}
		/*======================= Initialisation de l'interface graphique ===================*/
		public void initGUI() {
			display = new PluginShareAgentPanel(this);
			setGUIObject(display);
		}

		/*=======================================================================*/
		/*======================= Activation de l'agent==========================*/
		public void activate() {
			super.activate();
//			createGroup(true, groupName, null, null);
//			requestRole(groupName, "receiver", null);
//			live = true;
//
//			agList = getAgentsWithRole(groupName, serverRoleName);
//			initDir();
//			loadSettings();
//
//			//previous download aborded
//			saveFic = new FileSave(this, madkitDirectory + File.separator + groupName + File.separator + "save.ini");
//			stockPaquetOnQueue = saveFic.load();
//
//			if (!(stockPaquetOnQueue.isEmpty())) {
//				createSmallShareAgentForResumeFile();
//			}
//
//			//Verification des mises a jour a effectuer
//			majFic =
//				new FileMaj(madkitDirectory + File.separator + groupName + File.separator + "maj.ini");
//			stockUpdatedFile = majFic.load();
		}

		/*========================================================================*/
		/*======================= Boucle principale de l'agent ===================*/
//		public void live() {
//			while (live) {
//				exitImmediatlyOnKill();
//				Message m = waitNextMessage();
//				handleMessage(m);
//			}
//		}

//		public void end() {
//			for (int i = 0; i < smallShareAgentList.size(); i++) {
//				killAgent((SmallShareAgent) smallShareAgentList.elementAt(i));
//			}
//			majFic.lastUpdate(stockUpdatedFile);
//			saveFic.update(stockPaquetOnQueue);
//			leaveRole(groupName, "receiver");
//			leaveGroup(groupName);
//		}

		/*=======================================================================*/
		/*======================= Traitement des messages =======================*/
		void handleMessage(Message m) {

//			if (m instanceof TreeMessage) {
//				AgentAddress serverAddress = ((TreeMessage) m).getSender();
//				System.out.println("(client) TreeMessage received");
//				String serverName =
//					((TreeMessage) m).getSender().getKernel().getHost().toString();
//				s = ((TreeMessage) m).getDir();
//				Vector v = s.getVect();
//				((ShareAgentPanel) display).addServerPanel(
//					s,
//					((TreeMessage) m).getSender());
//				//checkPlugins(((TreeMessage)m).getPluginsInfo(), serverName,serverAddress);
//				checkUpdatedFile(s, ((TreeMessage) m).getSender());
//			}

	/*		if (m instanceof IndexMessage) {
				AgentAddress serverAddress = ((IndexMessage) m).getSender();
				AgentAddress smallServerAddress =
					((IndexMessage) m).getSmallServerAddress();
				ListFic listFic = ((IndexMessage) m).getListFic();
				if (!listFic.isEmpty()) {
					SmallShareAgent ssa =
						new SmallShareAgent(
							listFic,
							serverAddress,
							this,
							((IndexMessage) m).getUpdatePlugin());
					launchAgent(ssa, "smallShareAgent" + nbrSmallShareAgent, false);
					nbrSmallShareAgent++;
					smallShareAgentList.add(ssa);
				}
			}

			if (m instanceof SearchFileMessage) {
				String path = ((SearchFileMessage) m).getFile();
				String serverName =
					((SearchFileMessage) m)
						.getSender()
						.getKernel()
						.getHost()
						.toString();
				System.out.println("(client) SearchMessage received");
				System.out.println(
					"---> File "
						+ ((SearchFileMessage) m).getFile()
						+ " found on "
						+ serverName);
			}

			if (m instanceof KillMessage) {
				//System.out.println("(client) KillMessage receive");
				live = false;
			} */
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
//		public void initDir() {
//
//			/* File currentDirectory = new File(System.getProperty("user.dir"));
//			if( ((currentDirectory.getParentFile()).getName()).equals("plugins") )
//				madkitDirectory=(currentDirectory.getParentFile()).getParent();
//			else madkitDirectory=currentDirectory.getParent(); */
//			madkitDirectory = System.getProperty("madkit.dir");
//			File f = new File(madkitDirectory + File.separator + groupName);
//			f.mkdir();
//			f = new File(madkitDirectory + File.separator + groupName + File.separator + "Incoming");
//			f.mkdir();
//		}
		/*========================================================================================*/
		public void checkPlugins(AgentAddress[] agList) {
			SmallUpdatePluginAgent supa = new SmallUpdatePluginAgent(agList, this);
			launchAgent(supa, "SmallUpdatePluginAgent", false);
		}
		
		/*========================================================================================*/
//		public void addPaquetOnQueue(Vector v) {
//			for (int i = 0; i < v.size(); i++) {
//				stockPaquetOnQueue.add(v.elementAt(i));
//			}
//			saveFic.update(stockPaquetOnQueue);
//		}
//		/*========================================================================================*/
//		public void addUpdatedFile(Vector v) {
//			for (int i = 0; i < v.size(); i++) {
//				stockUpdatedFile.add(v.elementAt(i));
//			}
//		}
//		/*==============================================================================================*/
//		public boolean removePaquetOnQueue(
//			String p,
//			AgentAddress a,
//			int s,
//			int e) {
//			for (int i = 0; i < stockPaquetOnQueue.size(); i++) {
//				//System.out.println("Ask UpdateSaveFic");
//				if (p.equals(((PaquetOnQueue) stockPaquetOnQueue.elementAt(i)).getPath()))
//					if (s == ((PaquetOnQueue) stockPaquetOnQueue.elementAt(i)).getOffsetStart())
//						if (e == ((PaquetOnQueue) stockPaquetOnQueue.elementAt(i)).getOffsetEnd()) {
//							stockPaquetOnQueue.removeElementAt(i);
//							saveFic.update(stockPaquetOnQueue);
//							return true;
//						}
//			}
//			return false;
//		}
//		/*==============================================================================================*/
//		public boolean removeSmallShareAgent(SmallShareAgent ssa) {
//			for (int i = 0; i < smallShareAgentList.size(); i++) {
//				if (ssa.equals((SmallShareAgent) smallShareAgentList.elementAt(i))) {
//					smallShareAgentList.removeElementAt(i);
//					if (!ssa.getUpdatePlugin())
//						showUpdateTable();
//					return true;
//				}
//			}
//			return false;
//		}
//		/*==============================================================================================*/
//		public void AskUserUpdatedFile(
//			String virtualPath,
//			String serverName,
//			long lastModified) {
//			boolean alreadyUpdated = false;
//			for (int j = 0; j < stockUpdatedFile.size(); j++) {
//				if ((((UpdatedFile) stockUpdatedFile.elementAt(j)).getPath()).equals(virtualPath)
//					&& (((UpdatedFile) stockUpdatedFile.elementAt(j)).getServerName()).equals(serverName)) {
//					alreadyUpdated = true;
//					stockUpdatedFile.remove(j);
//					stockUpdatedFile.addElement(new UpdatedFile(virtualPath, serverName, lastModified));
//					//majFic.update(stockUpdatedFile);
//					continue;
//				}
//			}
//
//			if (!alreadyUpdated) {
//				UpdatedFile uf = new UpdatedFile(virtualPath, serverName, lastModified);
//				stockUpdatedFile.addElement(uf);
//				//majFic.update(stockUpdatedFile);
//			}
//		}
//
//		/*==============================================================================================*/
//		public void checkUpdatedFile(DirEntry dir, AgentAddress serverAddress) {
//			long res;
//			Date currentDate;
//			for (int i = 0; i < stockUpdatedFile.size(); i++) {
//				//if( (serverAddress.getKernel().getHost().toString()).equals(((UpdatedFile)stockUpdatedFile.elementAt(i)).getServerName())){
//				res = dir.isThereFile(((UpdatedFile) stockUpdatedFile.elementAt(i)).getPath(),"");
//				//System.out.println("apres l'appel à isThereFile, res = "+res);
//				if (res != 0) {
//					//System.out.println("res : "+res+" lastModified : "+((UpdatedFile)stockUpdatedFile.elementAt(i)).getLastModified());
//					if (res > ((UpdatedFile) stockUpdatedFile.elementAt(i)).getLastModified()) {
//						int ret = JOptionPane.showConfirmDialog(null, "Would you like to update file \""
//									+ ((UpdatedFile) stockUpdatedFile.elementAt(i)).getPath()
//									+ "\" ?", "Updated File", JOptionPane.YES_NO_OPTION);
//						if (ret == 0) {
//							sendMessage(serverAddress, new FicMessage(((UpdatedFile) stockUpdatedFile.elementAt(i)).getPath()));
//						}
//					}
//				}
//				//}
//			}
//		}
//		/*==============================================================================================*/
//		public void createSmallShareAgentForResumeFile() {
//			Vector stockTempPaquetOnQueue = new Vector();
//
//			AgentAddress tempServerAddress =
//				((PaquetOnQueue) stockPaquetOnQueue.elementAt(0)).getAddress();
//
//			for (int i = 0; i < stockPaquetOnQueue.size(); i++) {
//				if (tempServerAddress
//					.equals(
//						((PaquetOnQueue) stockPaquetOnQueue.elementAt(i))
//							.getAddress())) {
//					stockTempPaquetOnQueue.add(
//						(PaquetOnQueue) stockPaquetOnQueue.elementAt(i));
//				} else {
//					SmallShareAgent ssa =
//						new SmallShareAgent(stockTempPaquetOnQueue, this);
//					launchAgent(ssa, "smallShareAgent" + nbrSmallShareAgent, false);
//					nbrSmallShareAgent++;
//					smallShareAgentList.add(ssa);
//					stockTempPaquetOnQueue.clear();
//					tempServerAddress =
//						((PaquetOnQueue) stockPaquetOnQueue.elementAt(i))
//							.getAddress();
//				}
//			}
//			SmallShareAgent ssa = new SmallShareAgent(stockTempPaquetOnQueue, this);
//			launchAgent(ssa, "smallShareAgent" + nbrSmallShareAgent, false);
//			nbrSmallShareAgent++;
//			smallShareAgentList.add(ssa);
//			stockTempPaquetOnQueue.clear();
//		}
//		/*==============================================================================================*/
//		public void showUpdateTable() {
//			UpdateTableFrame updateTable =
//				new UpdateTableFrame(stockUpdatedFile, this);
//			updateTable.pack();
//			updateTable.setVisible(true);
//		}
		/*==============================================================================================*/
		public void showUpdatedPlugin(AgentAddress addr,Vector stockUpdatedPlugin) {
			((PluginShareAgentPanel)display).updatePlugin(addr, stockUpdatedPlugin);
//			int res =
//				JOptionPane.showConfirmDialog(
//					null,
//					"Updated plugins were found on a server, would you want to update some of them ?",
//					"Plugins Informations",
//					JOptionPane.YES_NO_OPTION);
//
//			if (res == JOptionPane.YES_OPTION) {
//				UpdatePluginTableFrame updatePluginTable =
//					new UpdatePluginTableFrame(stockUpdatedPlugin, this);
//				updatePluginTable.pack();
//				updatePluginTable.setVisible(true);
//			}
		}
		
		protected void executeServer(AgentAddress addr){
			//sendMessage(addr, new RequestTreeMessage("request-tree"));
			((PluginShareAgentPanel)display).addServerPanel(addr);
		}
		/*==============================================================================================*/
//		public void loadSettings() {
//			try {
//				String settingsSharePath =
//					madkitDirectory
//						+ File.separator
//						+ groupName
//						+ File.separator
//						+ "share.ini";
//				File f = new File(settingsSharePath);
//				f.createNewFile();
//				RandomAccessFile raf = new RandomAccessFile(f, "r");
//				String element;
//				int cut;
//				while ((element = raf.readLine()) != null) {
//					cut = element.indexOf(" ** ");
//					timeout = Integer.parseInt(element.substring(10, cut));
//
//					element = element.substring(cut + 4);
//					cut = element.indexOf(" ** ");
//					maxSize = Integer.parseInt(element.substring(10, cut));
//				}
//				raf.close();
//			} catch (IOException ioe) {
//				System.out.println("(Client) Can't read share.ini");
//			}
//		}

		/*==============================================================================================*/

}
