package madkit.TreeTools;

import java.io.*;


public class AbstractFileNode extends GenericTreeNode {

    protected File file;
    protected Entry entry;

    public static String getNameFromPath(String s){
        int k = s.lastIndexOf('/');
        if (k != -1){
            return s.substring(k+1);
        }
        else return s;
    }

    public AbstractFileNode(File _file){
	super(_file.getName());
	file = _file;
    }

    public AbstractFileNode(Entry _entry){
	super(_entry.getName());
	entry = _entry;
    }

}
