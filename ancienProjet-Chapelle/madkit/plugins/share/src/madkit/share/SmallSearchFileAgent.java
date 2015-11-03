package madkit.share;
import java.io.IOException;
import java.util.Vector;

import madkit.kernel.Agent;
import madkit.kernel.Message;

/*===============================================================================================*/
/*==================================== Class SmallSearchFileAgent =============================*/
/*===============================================================================================*/
public class SmallSearchFileAgent extends Agent
{
   
    /*=======================================================================*/
    /*============================== Attributs ==============================*/
    AbstractShareAgent shareAgent;
    ShareSearchFilePanel searchPanel;
    Vector stockResultSearchFile; 
    boolean live;
    String groupName; 
    String fileName;

    /*=======================================================================*/
    /*============================= Constructor =============================*/
    public SmallSearchFileAgent(AbstractShareAgent sa,ShareSearchFilePanel ssfp){
	shareAgent=sa;
	searchPanel=ssfp;
	groupName = sa.getGroupName();
	fileName=searchPanel.getFileName();
	stockResultSearchFile=new Vector();
	live=true;
    }
    
    
    /*=======================================================================*/
    /*======================= Activation de l'agent==========================*/
    public void activate(){
	createGroup(true,groupName,null,null);
	requestRole(groupName,"SmallSearchFileAgent",null);

	sendAllRequestSearchFileMessage();
    }


    /*========================================================================*/
    /*======================= Boucle principale de l'agent ===================*/
    public void live(){
	while(live)
	    {
		exitImmediatlyOnKill();
		if (!isMessageBoxEmpty()){
		    Message m = waitNextMessage();
		    try {handleMessage(m);}
		    catch(IOException exc){System.out.println("Error : SmallSearchFileAgent --> HandleMessage");}	
		}
		else{
		    pause(1000);
		}	
	    }
    }
    

    /*========================================================================*/
    /*============================ Mort de l'agent ===========================*/
    public void end(){ 
	System.out.println("(client) SmallSearchFileAgent killed");
    }

    /*=======================================================================*/
    /*======================= Traitement des messages =======================*/
    void handleMessage(Message m) throws IOException{
	if(m instanceof SearchFileMessage){
	    stockResultSearchFile.add(new SearchedFile(
						       ((SearchFileMessage)m).getVirtualPath(),
						       ((SearchFileMessage)m).isDirectory(),
						       ((SearchFileMessage)m).getSize(),
						       ((SearchFileMessage)m).getSender().getKernel().getHost().toString(),
						       ((SearchFileMessage)m).getSender() ));
	    searchPanel.updateResultTable(stockResultSearchFile);
	   // System.out.println("(SmallSearchFileAgent) SearchFileMessage received");
	}
	
    }

    /*=======================================================================*/
    /*======================== Methodes annexes =============================*/

    public void sendAllRequestSearchFileMessage(){
	//System.out.println("(SmallSearchFileAgent) Send all RequestSearchFileMessage");
	broadcastMessage(groupName,"sender", new RequestSearchFileMessage(fileName));
    }
}

