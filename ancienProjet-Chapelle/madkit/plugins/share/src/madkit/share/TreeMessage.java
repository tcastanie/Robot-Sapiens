package madkit.share;
import madkit.TreeTools.DirEntry;
import madkit.kernel.Message;


public class TreeMessage extends Message {     	
    DirEntry d = null;
    
    public DirEntry getDir() {return d;}
	
    public TreeMessage(DirEntry _d) {
	d=_d;
    }
}
