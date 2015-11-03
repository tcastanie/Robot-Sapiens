package madkit.share;
import madkit.kernel.AgentAddress;

public class PluginInfo implements java.io.Serializable{

    public String pluginName;
    public PluginVersionNumber pluginVersion;
    public String virtualPath;
    public String serverName;	
    public AgentAddress serverAddress;

    public PluginInfo(String pn, PluginVersionNumber pvn,String vp){
	pluginName=pn;
	pluginVersion=pvn;
	virtualPath=vp;
    }

    public PluginInfo(String pn, PluginVersionNumber pv,String vp, String sn, AgentAddress sa){
	pluginName=pn;
	pluginVersion=pv;
	virtualPath=vp;
	serverName=sn;	
	serverAddress=sa;
    }

    public void setServerName(String sn){serverName=sn;}
    public void setServerAddress(AgentAddress sa){serverAddress=sa;}
}
