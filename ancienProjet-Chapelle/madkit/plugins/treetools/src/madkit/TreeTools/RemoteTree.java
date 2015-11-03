package madkit.TreeTools;

import java.util.*;
import java.io.*;

public class RemoteTree extends GenericTree {

    Entry rootFile;
    GenericTreeNode localRoot;

    public Entry getRootFile() { return rootFile; }

    protected void buildDirectoryTree(AbstractFileNode parent, Entry entry){
        if (entry == null) return;
        else if (entry.isDir()){
            Vector v = ((DirEntry)entry).getVect();
	for(int i=0;i<v.size();i++) {
		if(((Entry)v.elementAt(i)).isDir()){
			DirNode dir = new DirNode((Entry)v.elementAt(i),true);
			parent.add(dir);
                    	buildDirectoryTree(dir,(Entry)v.elementAt(i));
		}
	 else
                    buildFileTree(parent,(Entry)v.elementAt(i));
            }
        }
    }

    protected void buildFileTree(AbstractFileNode parent, Entry entry){
        parent.add(new FileNode(entry));
    }

    public void buildTree(){
        buildDirectoryTree((AbstractFileNode)top,rootFile);
    }
    
    public RemoteTree(Entry entry){
        super(new DirNode(entry,true));
        rootFile = entry;
	buildTree();
        super.installTree();
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
    }

    public void addNode(Entry entry){
	((DirEntry)rootFile).addDirEntry((DirEntry)entry);
	DirNode node = new DirNode(entry,true);
	super.addNode(top,node);
	buildDirectoryTree((AbstractFileNode)node, entry);
	//tree.setShowsRootHandles(true);
	//scrollPane.repaint();
	treeModel.reload();
    }

    public void remNode(int index){
	((DirEntry)rootFile).remDirEntry(index);
	//System.out.println("Avant: "+top.getChildCount());
	top.remove(index);
	//System.out.println("Apres: "+top.getChildCount());
	treeModel.reload();
    }

    public String getPath(Object[] nodePath){
	String res="";
	int length = nodePath.length;
	for (int i=1;i<length;i++){
	    res = res+nodePath[i].toString();
	    if (i != length-1) res = res+File.separator; 
	}
	//System.out.println("Class RemoteTree, Method getPath : "+res);
	return res;
    }
}
