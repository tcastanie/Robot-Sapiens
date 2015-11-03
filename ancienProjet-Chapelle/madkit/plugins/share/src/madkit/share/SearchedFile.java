package madkit.share;
import madkit.kernel.AgentAddress;


public class SearchedFile
{
    String virtualPath;
    boolean isDir;
    float sizeFile;
    String serverName;
    AgentAddress serverAddress;
    

    public SearchedFile(String vp,boolean id,float sf,String s,AgentAddress aa){
	virtualPath = vp;
	isDir=id;
	sizeFile=sf;
	serverName = s;
	serverAddress=aa;
    }


    public String getVirtualPath() {return virtualPath;}
    public boolean isDirectory() {return isDir;}
    public float getSizeFile() {return sizeFile;}
    public String getServerName() {return serverName;}
    public AgentAddress getServerAddress() {return serverAddress;}
}
