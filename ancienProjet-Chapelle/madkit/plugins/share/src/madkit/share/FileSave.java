package madkit.share;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.util.Vector;

import javax.swing.JOptionPane;

import madkit.kernel.AgentAddress;
import madkit.kernel.InvalidAddressException;


public class FileSave {

    String savePath;
    AbstractShareAgent shareAgent;
    AgentAddress serverAddress;
    int offsetStart;
    int offsetEnd;
    
	public FileSave(AbstractShareAgent sa,String sp)
	{
	    shareAgent=sa;
	    savePath = sp;
	    File f = new File(savePath);
	    try{f.createNewFile();}
	    catch(IOException ioe){System.out.println("(Client) Can't create save file");}			
	}

	public void update(Vector stockPaquetOnQueue)
	{
		try
		{			
			File fic = new File(savePath);
			FileOutputStream save = new FileOutputStream (fic);
			PaquetOnQueue paq;
			OutputStreamWriter w = new OutputStreamWriter(save);
			for(int i=0; i<stockPaquetOnQueue.size(); i++)
			{
				paq = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(i));
				String p = paq.getPath();
				offsetStart = paq.getOffsetStart();
				offsetEnd = paq.getOffsetEnd();
				serverAddress = paq.getAddress();
				String clientPath = paq.getClientPath();
				
				w.write(p+" ** "+offsetStart+" ** "+offsetEnd+" ** "+serverAddress.toString()+" ** "+clientPath+" **\n");
			}
			w.close();
			save.close();

		}
		catch(IOException ioe){System.out.println("(Client) Can't write Save file or Save file doesn't exist");}
	}
	
	public Vector load()
	{
		String element;
		String path;
		String clientPath;
		int cut;
		AgentAddress a;

		Vector stockPaquetOnQueue = new Vector();
		PaquetOnQueue paq;

		File f = new File(savePath);
		try
		{
			RandomAccessFile raf = new RandomAccessFile(f,"r");
			while ( (element = raf.readLine()) != null )
			{
				cut = element.indexOf(" ** ");
				path = element.substring(0,cut);

				element = element.substring(cut+4);
				cut = element.indexOf(" ** ");
				offsetStart = Integer.parseInt(element.substring(0,cut));

				element = element.substring(cut+4);
				cut = element.indexOf(" ** ");
				offsetEnd = Integer.parseInt(element.substring(0,cut));

				try{
				    element = element.substring(cut+4);
				    cut = element.indexOf(" ** ");
				    a = new AgentAddress(element.substring(0,cut));
				    
				    element = element.substring(cut+4);
				    cut = element.indexOf(" **");	
				    clientPath = element.substring(0,cut);	
				    
				    //Ajout du paquetOnQueue
				    paq = new PaquetOnQueue(path,a,offsetStart,offsetEnd,clientPath);
				    stockPaquetOnQueue.add(paq);
				}
				catch(InvalidAddressException iae){
				    System.out.println("Error : Invalid server Address");
				    f.delete();
				}
			}
			raf.close();
			f.delete();
			
		}
		catch(IOException ioe){
		    System.out.println("(Client) Can't read Save file or Save file doesn't exist");
		    f.delete();
		}

		if( !(stockPaquetOnQueue.isEmpty()) )
		{
			int res = JOptionPane.showConfirmDialog(null, "Would you like to continue your download fallen through ?", "Resume files", JOptionPane.YES_NO_OPTION);
			if(res == 0){
				System.out.println("(client) Share a trouvé une liste de paquet(s) non telecharge(s)");
				System.out.println("(client) Verification de l'existence des serveurs necessaires");
				checkServer(stockPaquetOnQueue);
				
			}
			else{stockPaquetOnQueue.clear();}
		}
		return stockPaquetOnQueue;
	}

	public void checkServer(Vector stockPaquetOnQueue)
	{
		boolean ok;
		int findSource=0;
		boolean displayDialog = false;

		AgentAddress[] agList = shareAgent.getAgentsWithRole(((AbstractShareAgent)shareAgent).getGroupName(),"sender");

		int i=0;
		while(i<stockPaquetOnQueue.size()){
		    ok=false;
		    AgentAddress a = ((PaquetOnQueue)stockPaquetOnQueue.elementAt(i)).getAddress();
			
		    for(int j=0;j<agList.length;j++){
			if( (agList[j].getKernel().getHost()).equals(a.getKernel().getHost()) ){
			    ok=true;
			    ((PaquetOnQueue)stockPaquetOnQueue.elementAt(i)).setAddress(agList[j]);
			    continue;
			}
		    }
		    if( !ok ){
			if( !displayDialog ){
			    JOptionPane.showMessageDialog(null,
							  "A server could not be found, download will be aborded.",
							  "Server not found",
							  JOptionPane.ERROR_MESSAGE);
			    displayDialog=true;
			    }
			stockPaquetOnQueue.remove(i);
			i--;
		    }
		    i++;
		}
	}
}
