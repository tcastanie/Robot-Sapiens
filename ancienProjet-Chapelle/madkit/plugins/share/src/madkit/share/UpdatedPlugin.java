package madkit.share;
import madkit.kernel.AgentAddress;


public class UpdatedPlugin{
	
    String pluginName;
    PluginVersionNumber serverPluginVersion;
    PluginVersionNumber pluginVersion;
    String virtualPath;
    AgentAddress serverAddress;
    
    public UpdatedPlugin(String p,PluginVersionNumber pv, PluginVersionNumber spv,String vp,AgentAddress aa){
	pluginName=p;
	serverPluginVersion=spv;
	pluginVersion=pv;
	virtualPath=vp;
	serverAddress=aa;
    }

    public String getPluginName(){return pluginName;}
    public PluginVersionNumber getServerPluginVersion() {return serverPluginVersion;}
    public PluginVersionNumber getPluginVersion() {return pluginVersion;}
    public String getVirtualPath() {return virtualPath;}
    public AgentAddress getServerAddress() {return serverAddress;}

}
