/*
 * Created on 22 juil. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.share;

import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;

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
public class AbstractServerAgent extends Agent {
		public static String separator = "/";
		public String madkitDirectory;         //see initDir method
    
		DirEntry dir;
	
    
    

		/*=====================================================*/
		/*======================= Attributs ===================*/
		JRootPane display;
		int nbrSmallSenderAgent = 0;
		boolean live;

		FicDataBase dataBase;
		public FicDataBase getData(){return dataBase;}
	
		String groupName = "share";
		String roleName = "sender";
		
		Vector smallSenderAgentList = new Vector();     //List of SmallSenderAgent
		
	/*===================================================================================*/
		/*================================== Accesseurs =====================================*/
		public void changeGroup(String s) {
			leaveGroup(groupName);
			groupName=s;
			createGroup(true,groupName,null,null);
			requestRole(groupName,roleName,null);
		}
		public void changeRole(String g, String r) {
			leaveGroup(groupName);
			groupName=g;
			createGroup(true,groupName,null,null);
			requestRole(groupName,r,null);
		}

		public String getGroupName() {return groupName;}
		protected void setGroupName(String s){
			groupName = s;
		}
		
		public String getRoleName() {
			return roleName;
		}
	
		public void setRoleName(String s) {
			roleName = s;
		}

	
	/*=======================================================================*/
	/*======================= Traitement des messages =======================*/
	void handleMessage(Message m) throws IOException{
		if(m instanceof KillMessage){
			//System.out.println("(client) KillMessage received");
			live=false;
			
		} 
	else if(m instanceof OffsetMessage) {
				AgentAddress clientAddress = ((OffsetMessage)m).getSender();
				String virtualPath = ((OffsetMessage)m).getName();
				int offsetStart = ((OffsetMessage)m).getOffsetStart();
				int offsetEnd = ((OffsetMessage)m).getOffsetEnd();
	
				//System.out.println("(server) OffsetMessage received");
				nbrSmallSenderAgent++;
				SmallSenderAgent ssa = new SmallSenderAgent(clientAddress,virtualPath,offsetStart,offsetEnd,dataBase,this);
				launchAgent(ssa,"smallSenderAgent"+nbrSmallSenderAgent,false);
				nbrSmallSenderAgent++;
				smallSenderAgentList.add(ssa);
				//System.out.println("(server) SmallSenderAgent created");
			}
//			else if(m instanceof RequestSearchFileMessage) {
//				AgentAddress addressClient=((RequestSearchFileMessage)m).getSender();
//				String path = ((RequestSearchFileMessage)m).getFile();
//				//System.out.println("(server) SearchMessage received"); 
//				Vector res = dataBase.searchFile(((RequestSearchFileMessage)m).getFile());
//				if(!res.isEmpty()){
//					System.out.println("---> File "+((RequestSearchFileMessage)m).getFile()+" found");
//					for(int i=0;i<res.size();i++)
//					sendMessage(addressClient,new SearchFileMessage(path,
//											((AssocPath)res.elementAt(i)).getVirtualPath(),
//											((AssocPath)res.elementAt(i)).getSizeFile(),
//											((AssocPath)res.elementAt(i)).isDirectory()));
//				}
//			}
		
		
		
		else {
			println("Receiving "+m.getClass().getName()+" message type");
		}
	}

	/*===============================================================================================*/
	   /*==================================== Methodes Annexes =========================================*/
	   /*===============================================================================================*/

	
	public boolean removeSmallSenderAgent(SmallSenderAgent ssa)
	{
	for(int i=0;i<smallSenderAgentList.size();i++)
		{
		if(ssa.equals( (SmallSenderAgent)smallSenderAgentList.elementAt(i) ))
			{
			smallSenderAgentList.removeElementAt(i);
			return true;
			}
		}
	return false;
	}


}


