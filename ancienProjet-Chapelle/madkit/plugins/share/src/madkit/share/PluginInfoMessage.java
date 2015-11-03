package madkit.share;
import madkit.kernel.Message;


public class PluginInfoMessage extends Message { 
    
    ListPluginInfo listPluginInfo;
	
    public PluginInfoMessage(ListPluginInfo lpi){
	listPluginInfo=lpi;
    }
    
    public ListPluginInfo getListPluginInfo(){return listPluginInfo;}
}
