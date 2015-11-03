package madkit.TreeTools;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.tree.*;

class GenericTreeCellRenderer extends DefaultTreeCellRenderer {

	public GenericTreeCellRenderer() {
		super();
		;
	}

	public Component getTreeCellRendererComponent(
		JTree tree,
		Object value,
		boolean sel,
		boolean expanded,
		boolean leaf,
		int row,
		boolean hasFocus) {

		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		if (value != null) {
			//System.out.println("Valeur : "+value);
			if (leaf) {
				ImageIcon icon = ((GenericTreeNode) value).getLeafIcon();
				if (icon != null)
					setIcon(icon);
			} else {
				ImageIcon icon =
					((GenericTreeNode) value).getBranchIcon(0, expanded);
				if (icon != null)
					setIcon(icon);
			}
		}
		return this;
	}
	

}

public class GenericTree extends JPanel implements MouseListener {

	protected JTree tree;
	protected GenericTreeNode top;
	protected DefaultTreeModel treeModel;
	protected GenericTreeCellRenderer myRenderer;
	JScrollPane scrollPane;

	public GenericTree() {
		super();
	}

	public GenericTree(GenericTreeNode root) {
		top = root;
	}

	protected void installTree() {
		treeModel = new DefaultTreeModel(top);
		myRenderer = new GenericTreeCellRenderer();
		tree = new JTree(treeModel);
		tree.setCellRenderer(myRenderer);
		tree.addMouseListener(this);
		setLayout(new BorderLayout());
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(tree);
		add(scrollPane, BorderLayout.CENTER);
	}

	public JTree getTree() {
		return tree;
	}

	public DefaultTreeModel getTreeModel() {
		return (DefaultTreeModel) treeModel;
	}

	public GenericTreeNode getRootNode() {
		return top;
	}

	public void addNode(DefaultMutableTreeNode parent, DefaultMutableTreeNode child) {
		((DefaultTreeModel) treeModel).insertNodeInto(
			child, parent, ((DefaultTreeModel) treeModel).getChildCount(parent));
	}

	public void mousePressed(MouseEvent e) {
		int selRow = tree.getRowForLocation(e.getX(), e.getY());
		if (selRow != -1) {
			if (e.getClickCount() == 2)
				LeftDoubleClick(e);
			else if (
				(e.getClickCount() == 1)
					&& ((e.getModifiers() & InputEvent.BUTTON3_MASK)
						== InputEvent.BUTTON3_MASK))
				RightClick(e);
		}
	}

	public void mouseEntered(MouseEvent e) {
	}
	public void mouseExited(MouseEvent e) {
	}
	public void mouseClicked(MouseEvent e) {
	}
	public void mouseReleased(MouseEvent e) {
	}

	public void LeftDoubleClick(MouseEvent e) {
		TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
		GenericTreeNode n = (GenericTreeNode) selPath.getLastPathComponent();
		n.execute();
	}

	public void RightClick(MouseEvent e) {
		TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
		GenericTreeNode node = (GenericTreeNode) selPath.getLastPathComponent();
		GenericIconDescriptor desc = node.getDescriptor();
		if (desc != null) {
			JPopupMenu popup = desc.getPopup();
			if (popup != null) {
				desc.setArg(node);
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}
}
