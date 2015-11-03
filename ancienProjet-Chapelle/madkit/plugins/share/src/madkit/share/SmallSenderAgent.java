package madkit.share;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;



/*===============================================================================================*/
/*==================================== Class SmallSenderAgent ===================================*/
/*===============================================================================================*/

public class SmallSenderAgent extends Agent
{
	AbstractServerAgent senderAgent;
	AgentAddress clientAddress;
	String virtualPath;
	int offsetStart;
	int offsetEnd;
	FicDataBase dataBase;
	
	boolean live = true;
	

	/*==================================================================================*/
	/*============================ Constructor =========================================*/
	public SmallSenderAgent(){}

	public SmallSenderAgent(AgentAddress aa,String vp,int os,int oe, FicDataBase fdb,AbstractServerAgent s)
	{
		clientAddress = aa;
		virtualPath = vp;
		offsetStart = os;
		offsetEnd = oe;
		dataBase = fdb;
		
		senderAgent = s;
	}


	/*=======================================================================*/
	/*======================= Activation de l'agent==========================*/
	public void activate()
    	{
	    String groupName = senderAgent.getGroupName();
	    createGroup(true,groupName,null,null);
	    requestRole(groupName,"SmallSender",null);
	    
	    sendReturnMessage();    	
    	}

	/*========================================================================*/
	/*======================= Boucle principale de l'agent ===================*/
    	public void live()
	{
		while(live)
        	{
			exitImmediatlyOnKill();
            		Message m = waitNextMessage();
			try
            		{handleMessage(m);}
			catch(IOException exc){System.out.println("Error : AbstractServerAgent --> HandleMessage");}
        	}
        }
        
        /*========================================================================*/
	/*============================ Mort de l'agent ===========================*/
        public void end()
	{
		//System.out.println("(server) SmallSenderAgent killed");	
	}
        
        
	/*=======================================================================*/
	/*======================= Traitement des messages =======================*/	      
        void handleMessage(Message m) throws IOException
    	{

    		if(m instanceof OffsetMessage)
    		{
    			//System.out.println("(SmallServer) OffsetMessage received");
			virtualPath = ((OffsetMessage)m).getName();
			offsetStart = ((OffsetMessage)m).getOffsetStart();
			offsetEnd = ((OffsetMessage)m).getOffsetEnd();
			
			sendReturnMessage();
			
    		}
    		
    		if(m instanceof EndDownloadMessage)
    		{
    			//System.out.println("(SmallServer) EndDownloadMessage received");
    			senderAgent.removeSmallSenderAgent(this);
    			live = false;
    		}
	}
	
	
	/*==========================================================================*/
	/*============================= Methodes annexes ===========================*/
	public void sendReturnMessage()
	{
		String realPath = dataBase.searchRealPath(virtualPath);
		if(realPath.equals("error")){
		    System.out.println("(SmallSenderAgent) Fichier non autorise :"+virtualPath);
		    return;
		}
		try{
			File f = new File(realPath);
			long lastModified = f.lastModified();
			RandomAccessFile raf = new RandomAccessFile(f,"r");
			raf.seek(offsetStart);
			byte[] b = new byte[offsetEnd-offsetStart];
			raf.read(b,0,offsetEnd-offsetStart);
			raf.close();

			sendMessage(clientAddress,new ReturnMessage(b,virtualPath,offsetStart,offsetEnd,lastModified));
		}
		catch(IOException ioe){System.out.println("Error : SmallSenderAgent --> can't access file \""+realPath+"\".");}
	}
}
