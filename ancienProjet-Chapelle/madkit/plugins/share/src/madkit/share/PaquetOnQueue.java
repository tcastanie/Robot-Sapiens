package madkit.share;
import madkit.kernel.AgentAddress;


public class PaquetOnQueue
{
    String path;
    AgentAddress serverAddress;
    int offsetStart;
    int offsetEnd;
    String clientPath;

    public PaquetOnQueue(String p, AgentAddress a, int s,int e,String c)
    {
	path = p;
	serverAddress=a;
	offsetStart=s;
	offsetEnd=e;
	clientPath=c;
    }


    public String getPath() {return path;}
    public AgentAddress getAddress() {return serverAddress;}
    public int getOffsetStart() {return offsetStart;}
    public int getOffsetEnd() {return offsetEnd;}
    public String getClientPath() {return clientPath;}
    public int getPaquetSize() {return (offsetEnd-offsetStart);};
	
    public void setPath(String p) {path = p;}
    public void setAddress(AgentAddress a) {serverAddress = a;}
    public void setOffsetStart(int o) {offsetStart = o;}
    public void setOffsetEnd(int o) {offsetEnd = o;}

}
