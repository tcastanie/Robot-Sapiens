package madkit.explorer;

import madkit.TreeTools.*;
import javax.swing.*;
import javax.swing.tree.*;
import java.io.*;
import madkit.kernel.AbstractAgent;

public class MadkitExplorer extends Explorer {

	ExplorerTree tree;
	AbstractFileNode node;

	public MadkitExplorer(AbstractAgent ag,ExplorerTree tree, AbstractFileNode node, String path, int iconSize, JTextField display) {
		super(ag, path, iconSize, display);
		this.tree = tree;
		this.node = node;
	}

	public boolean readDown() {
//		System.out.println("OLD ABSOLUTE PATH : "+absolutePath);
		TreePath path = new TreePath(node.getPath());
//		System.out.println("OLD TREE PATH NODE : "+path);
		tree.getTree().expandPath(path);
		super.readDown();
		//System.out.println("ABSOLUTE PATH : "+absolutePath);
		String s =
			absolutePath.substring(
				absolutePath.lastIndexOf(File.separatorChar),
				absolutePath.length());
		s = s.substring((s.lastIndexOf(File.separatorChar)) + 1, s.length());
		//System.out.println("ABSOLUTE PATH RETOUCHE : "+s);
		DefaultMutableTreeNode treeNode;
		int childCount = node.getChildCount();
		for (int i = 0; i < childCount; i++) {
			treeNode = (DefaultMutableTreeNode) node.getChildAt(i);
			String userObject = treeNode.getUserObject().toString();
			if (userObject.equals(s)) {
				node = (AbstractFileNode) treeNode;
				path = new TreePath(treeNode.getPath());
				tree.getTree().setSelectionPath(path);
				return true;
			}
		}
		return false;
	}

	public boolean readUp() {
		//System.out.println("ABSOLUTE PATH : "+getAbsolutePath());
		DefaultMutableTreeNode parent =
			(DefaultMutableTreeNode) node.getParent();
		if (parent.getUserObject().toString().equals("top") || !super.readUp())
			return false;
		absolutePath = getParent(absolutePath);
		//System.out.println("NODE : "+node);
		//System.out.println("PARENT : "+parent);
		TreePath path = new TreePath(parent.getPath());
		//System.out.println(path);
		tree.getTree().setSelectionPath(path);
		tree.getTree().collapsePath(path);
		return true;
	}

	public void setNode(AbstractFileNode node) {
		this.node = node;
	}
}
