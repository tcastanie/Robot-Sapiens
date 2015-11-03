/*
 * Created on 22 mars 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.explorer;

import madkit.TreeTools.*;
import java.io.*;


/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class MadkitExplorerTree extends ExplorerTree {
	public MadkitExplorerTree(File file) {
			super(file);
		}
	
	protected void buildExplorerTree(AbstractFileNode parent, File file) {
			if (file == null) return;
			if (file.isDirectory())
				super.buildExplorerTree(parent,file);
			else {
				buildFileTree(parent, file);
			}
	}
	protected void buildFileTree(AbstractFileNode parent, File file){
		parent.add(new FileNode(file));
	 }
}
