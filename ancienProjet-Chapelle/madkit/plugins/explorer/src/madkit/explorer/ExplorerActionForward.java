/*
 * Created on 8 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import javax.swing.tree.TreePath;

import madkit.TreeTools.AbstractFileNode;
import madkit.TreeTools.GenericTreeNode;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActionForward extends ExplorerAdapterActions{

	public ExplorerActionForward()
	{
	}
	/**
	 * @param adaptee
	 */
	public ExplorerActionForward(ExplorerGUI _adaptee, String _source) {

		this.adaptee = _adaptee;
		this.source = _source;
		

	}

	public void exec()
	{
		adaptee.backward.setEnabled(true);
		if (ExplorerBackForWard.vectorGuiPos < (ExplorerBackForWard.vectorSelection.size() -1))
		{
			ExplorerBackForWard.vectorGuiPos = ExplorerBackForWard.vectorGuiPos + 1;
			int pos = ExplorerBackForWard.vectorGuiPos;
			String selection = (String)ExplorerBackForWard.vectorSelection.elementAt(pos).toString();
			GenericTreeNode node = (GenericTreeNode)ExplorerBackForWard.vectorNode.elementAt(pos);
			if (node == null) return;
			adaptee.explorer.setNode((AbstractFileNode)node);

			adaptee.explorer.setAbsolutePath(selection);
			TreePath path = new TreePath(node.getPath());

			adaptee.tree.getTree().expandPath(path);
			adaptee.tree.getTree().setSelectionPath(path);

		}


		if (ExplorerBackForWard.vectorGuiPos >= (ExplorerBackForWard.vectorSelection.size() -1))
			adaptee.bForward.setEnabled(false);

	}
}
