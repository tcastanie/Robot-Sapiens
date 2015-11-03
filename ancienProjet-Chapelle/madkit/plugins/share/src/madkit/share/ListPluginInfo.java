package madkit.share;
import java.util.Vector;


public class ListPluginInfo implements java.io.Serializable{
   
    Vector listPluginInfo;

    public ListPluginInfo(){listPluginInfo = new Vector();}
 
    public Vector getVector() {return listPluginInfo;}

    public void add(PluginInfo pi){
	listPluginInfo.add(pi);
    }

    public PluginInfo elementAt(int i){
	return (PluginInfo)listPluginInfo.elementAt(i);
    }

    public int size(){
	return listPluginInfo.size();
    }

    public void display(){
	System.out.println("ListPluginInfo");
	for(int i=0;i<listPluginInfo.size();i++)
	    System.out.println("\t"+((PluginInfo)(listPluginInfo.elementAt(i))).pluginName +" , "+ ((PluginInfo)(listPluginInfo.elementAt(i))).pluginVersion);
    }
	public String displayToString(){
		String res="";
		for(int i=0;i<listPluginInfo.size();i++)
			res = res + ((PluginInfo)listPluginInfo.elementAt(i)).pluginName +", "+ 
			((PluginInfo)listPluginInfo.elementAt(i)).pluginVersion+"\n";
		return res;
	}
			   	       

}
