package madkit.share;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.tree.TreePath;

import madkit.TreeTools.DirEntry;
import madkit.TreeTools.RemoteTree;
import madkit.kernel.AgentAddress;

public class ShareRemoteTree extends RemoteTree {

    AbstractShareAgent agent;
    AgentAddress address;
    String path;


    public ShareRemoteTree(DirEntry entry, AbstractShareAgent _agent, AgentAddress _address){
	super(entry);
	agent = _agent;
	address = _address;
	path = "";
    }

    public void LeftDoubleClick(MouseEvent e) {
	TreePath selPath = getTree().getPathForLocation(e.getX(), e.getY());
	//System.out.println("Selection : "+selPath.toString());
	getPath(selPath.toString());
	send();
    }

    public String getPath(String s) {
	String s1;
	int l = s.indexOf(',');
	if (l<0) {
	    s1 = s.substring(1,s.length()-1);
	    path = path+s1;
	}
	else {  
	    s1 = s.substring(1,l);
	    path = path + s1 + File.separator;
	    String s2 = s.substring(l+1,s.length());
	    getPath(s2);	
	}
	return path;
    }

    public void send(){
	int cut = path.indexOf(File.separator);
	path = path.substring(cut+1,path.length());
	path = path.replace('\\','/');
	agent.sendMessage(address,new FicMessage(path));
	path="";
    }
}
