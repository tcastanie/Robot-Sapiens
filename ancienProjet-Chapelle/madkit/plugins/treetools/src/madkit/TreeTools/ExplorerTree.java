package madkit.TreeTools;

import javax.swing.tree.*;
import javax.swing.event.*;
import java.io.*;

public class ExplorerTree extends GenericTree
	implements TreeWillExpandListener, FileFilter {

	String rootPath;
	File rootFile;
	GenericTreeNode localRoot;
	String selection;
	boolean hidden;

	protected void buildExplorerTree(AbstractFileNode parent, File file) {
		if (file == null || !file.isDirectory())
			return;
		File[] content = file.listFiles(this);
		for (int i = 0; i < content.length; i++) {
			//System.out.println(content[i].getName());
			DirNode dir;
			if (content[i].listFiles(this) == null) {
				// System.out.println(content[i].getName()+" is NOT REACHABLE");
				dir = new DirNode(content[i], false);
				parent.add(dir);
			} else {
				if (content[i].listFiles(this).length == 0) {
					// System.out.println(content[i].getName()+" is EMPTY");
					dir = new DirNode(content[i], true);
					parent.add(dir);
				} else {
					//System.out.println(content[i].getName()+" is NOT EMPTY");
					dir = new DirNode(content[i], true);
					parent.add(dir);
					addTreeNode(dir, content[i]);
				}
			}
		}
	}

	public GenericTreeNode getLocalRoot() {
		return localRoot;
	}

	public boolean accept(File pathname) {
		if (hidden)
			return pathname.isDirectory();
		return (!pathname.isHidden() && pathname.isDirectory());
	}

	public void setHidden(boolean bool) {
		hidden = bool;
	}
	public boolean getHidden() {
		return hidden;
	}

	public void buildTree() {
		buildExplorerTree((AbstractFileNode) localRoot, rootFile);
	}

	protected void addTreeNode(AbstractFileNode parent, File file) {
		parent.add( new FileNode(file));
	}

	public void addDirectory(File file) {
		localRoot = new DirNode(file, true);
		top.add(localRoot);
		buildExplorerTree((AbstractFileNode) localRoot, file);
		this.treeModel.reload();
	}

	private File init(File _file) {
		File file = _file;
		try {
			file = _file.getCanonicalFile();
		} catch (IOException e) {
			System.out.println("Couldn't get Canonical File");
		}
		File parent = file.getParentFile();
		if (parent == null) {
			if (isWindowsPlatform())
				rootPath = file.getAbsolutePath() + File.separator;
			else
				rootPath = File.separator;
		} else
			rootPath = parent.getAbsolutePath() + File.separator;
		return file;
	}

	public ExplorerTree(File file) {
		super(new GenericTreeNode("top"));
		hidden = false;
		rootFile = init(file);
		localRoot = new DirNode(rootFile, true);
		top.add(localRoot);
		buildTree();
		installTree();
	}

	public void installTree() {
		super.installTree();
		tree.setShowsRootHandles(true);
		tree.setRootVisible(false);
		tree.addTreeWillExpandListener(this);
		tree.expandRow(0);
	}
	
	public void clear() {
		top.removeAllChildren();
		treeModel.reload();
	}
	
	public void reInstallRoot(File file){
		clear();
		//treeModel.removeNodeFromParent(localRoot);
		//top.removeAllChildren();
		rootFile = init(file);
		System.out.println("reinstalling root:"+file+", on top:"+top);
		localRoot = new DirNode(rootFile, true);
		
		top.add(localRoot);
		buildTree();
		treeModel.reload();
		tree.expandRow(0);

		tree.scrollPathToVisible(new TreePath(localRoot.getPath()));
    
		tree.repaint();
	}

	// Returns File Path
	public String getPath(Object[] nodePath) {
		String res = "";
		int length = nodePath.length;
		for (int i = 1; i < length; i++) {
			res = res + nodePath[i].toString();
			if (i != length - 1)
				res = res + File.separator;
		}
		res = rootPath + res;
		//System.out.println("Class LocalTree, Method getPath : "+res);
		return res;
	}

	//Expand selected node
	public void expandSelectedNode() {
		System.out.println("Expansion");
		// 	Object object = getLastSelectedPathComponent();
		// 	AbstractFileNode node = (AbstractFileNode)getTree().object;
	}

	// Required by TreeWillExpandListener interface.
	public void treeWillExpand(TreeExpansionEvent e) {
		//System.out.println("Tree-will-expand event detected");
		AbstractFileNode node =
			(AbstractFileNode) ((e.getPath()).getLastPathComponent());
		node.removeAllChildren();
		File file = new File(getPath((e.getPath()).getPath()));
		buildExplorerTree(node, file);
		this.treeModel.reload(node);
		//System.out.println("Class LocalTree, Method treeWillExpand : "+getPath((e.getPath()).getPath()));
	}

	// Required by TreeWillExpandListener interface.
	public void treeWillCollapse(TreeExpansionEvent e) {
		//System.out.println("Tree-will-collapse event detected");
	}

	public void valueChanged(TreeSelectionEvent e) {
		selection = getPath((e.getPath()).getPath());
	}

	static boolean isWindowsPlatform() {
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith(WIN_ID))
			return true;
		else
			return false;
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
