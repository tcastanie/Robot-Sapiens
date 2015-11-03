package madkit.TreeTools;

import javax.swing.event.*;
import java.io.*;

public class LocalTree extends GenericTree implements TreeWillExpandListener {

    String rootPath;
    File rootFile;
    GenericTreeNode localRoot;
    String selection;

    protected void buildDirectoryTree(AbstractFileNode parent, File file){
        if (file == null) return;
        else if (file.isDirectory()){
            File[] content = file.listFiles();
            //System.out.println("Class LocalTree, Method buildDirectoryTree : "+file.getName());
            for (int i=0;i<content.length;i++) {
                if (content[i].isDirectory()){
		    DirNode dir;
		    //System.out.print("Class LocalTree, Method buildDirectoryTree : ");
		    if (content[i].list() == null) { 
			//System.out.println(content[i].getName()+" is NOT REACHABLE");
			dir = new DirNode(content[i], false);
			parent.add(dir);
		    }	    
		    else {
			if (content[i].list().length == 0) {
			    //System.out.println(content[i].getName()+" is EMPTY");
			    dir = new DirNode(content[i], true);
			    parent.add(dir);
			}		
			else {    
			    dir = new DirNode(content[i],true);
			    parent.add(dir);
			    buildFileTree(dir,content[i]);
			}
		    }
		} 
		else buildFileTree(parent,content[i]);
            }
        }
    }
    
    public void buildTree(){
        buildDirectoryTree((AbstractFileNode)localRoot, rootFile);
    }

    protected void buildFileTree(AbstractFileNode parent, File file){
	parent.add(new FileNode(file));
    }

    public void addDirectory(File file){
        localRoot = new DirNode(file,true);
        top.add(localRoot);
        buildDirectoryTree((AbstractFileNode) localRoot,file);
        this.treeModel.reload();
    }
 
    
    public LocalTree(File _file){
        super(new GenericTreeNode("top"));
	File file = _file;
	try {
	    file = _file.getCanonicalFile();
	}
	catch(IOException e){
	    System.out.println("Couldn't get Canonical File");
	}
	//System.out.println("Class LocalTree, Constructor : "+file.getAbsolutePath());
	File parent = file.getParentFile();
	if (parent == null) {
	    if (isWindowsPlatform()) rootPath = file.getAbsolutePath()+File.separator;
	    else rootPath = File.separator;
 	}
	else rootPath = parent.getAbsolutePath()+File.separator;

	rootFile = file;
	localRoot = new DirNode(file,true);
	top.add(localRoot);
	buildTree();
	installTree();
    }

    public void installTree(){
        super.installTree();
        tree.setShowsRootHandles(true);
        tree.setRootVisible(false);
	tree.addTreeWillExpandListener(this);
    }

    // Returns File Path
    public String getPath(Object[] nodePath){
	String res="";
	int length = nodePath.length;
	for (int i=1;i<length;i++){
	    res = res+nodePath[i].toString();
	    if (i != length-1) res = res+File.separator; 
	}
	res = rootPath+res;
	//System.out.println("Class LocalTree, Method getPath : "+res);
	return res;
    }

    // Required by TreeWillExpandListener interface.
    public void treeWillExpand(TreeExpansionEvent e){
	//System.out.println("Tree-will-expand event detected");
 	AbstractFileNode node = (AbstractFileNode)((e.getPath()).getLastPathComponent());
	node.removeAllChildren();
 	File file = new File(getPath((e.getPath()).getPath()));
	buildDirectoryTree(node,file);
	//System.out.println("Class LocalTree, Method treeWillExpand : "+getPath((e.getPath()).getPath()));
    }
    
    // Required by TreeWillExpandListener interface.
    public void treeWillCollapse(TreeExpansionEvent e) {
	//System.out.println("Tree-will-collapse event detected");
    }

    public void valueChanged(TreeSelectionEvent e){
	selection = getPath((e.getPath()).getPath()); 	
    }
    
    static boolean isWindowsPlatform() {
        String os = System.getProperty("os.name");
        if ( os != null && os.startsWith(WIN_ID)) return true;
        else return false;
    }
    
    // Used to identify the windows platform.
    private static final String WIN_ID = "Windows";
    // The default system browser under windows.
    private static final String WIN_PATH = "rundll32";
    // The flag to display a url.
    private static final String WIN_FLAG = "url.dll,FileProtocolHandler";
    // The default browser under unix.
    private static final String UNIX_PATH = "netscape";
    // The flag to display a url.
    private static final String UNIX_FLAG = "-remote openURL";
}
