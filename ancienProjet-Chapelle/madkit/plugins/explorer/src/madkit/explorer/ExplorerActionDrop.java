/*
 * Created on 10 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.TreePath;


import madkit.TreeTools.AbstractFileNode;
import madkit.TreeTools.GenericTreeNode;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActionDrop implements 	 DropTargetListener 
{	
	protected JTree tree;
	protected String action;
	protected FileIcon icon;
	ExplorerGUI adaptee;
	private Explorer explorer;
	private File oldFile;
	private File newFile;
	public DropTarget dropTarget;
	private Point point;
	
	

	public ExplorerActionDrop()
	{
		
	}
	
	protected void execIcon(Explorer _explorer, FileIcon _icon)
	{
		ExplorerActions.actionToolBar = "drop";
		action = "icon";
		this.explorer = _explorer;
		this.icon = _icon;
//		File file = new File (icon.name);
//		System.out.println ("DROP file = " +  file.getAbsolutePath());


		dropTarget = new DropTarget(explorer,
									DnDConstants.ACTION_COPY_OR_MOVE,
									this,
									explorer.isEnabled(),
									null);
	}

	protected void execTree(ExplorerGUI _adaptee) {

		ExplorerActions.actionToolBar = "Drop";
		ExplorerActions.actionBefore = "Drag";
		action = "tree";
		this.adaptee = _adaptee;
		this.explorer = adaptee.explorer;
		tree = adaptee.tree.getTree();

		dropTarget = new DropTarget(tree,
									DnDConstants.ACTION_COPY_OR_MOVE,
									this,
									tree.isEnabled(),
									null);

	}
		public void dragEnter(DropTargetDragEvent event) {

			if (event.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				event.acceptDrag(DnDConstants.ACTION_COPY_OR_MOVE);
			}else
				System.out.println ("Drage enter UNsupported");	
		}

		public void dragOver(DropTargetDragEvent event) {
			
		}

		public void dropActionChanged(DropTargetDragEvent event) {
		
		}

		public void drop(DropTargetDropEvent event) {
			point = event.getLocation();

			Transferable transferable =event.getTransferable();
			if (transferable.isDataFlavorSupported(DataFlavor.javaFileListFlavor))
			{
				event.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE)	;
				try
				{
					List files = (List)transferable.getTransferData(DataFlavor.javaFileListFlavor);
					File f = (File) files.get(0);
					transfertFile(f);
					event.dropComplete(true);
				}catch (Exception ex )
				{
					event.dropComplete(false);
					
				}
			}else{
				
				event.rejectDrop();
				event.dropComplete(false);
				return;	
			}
			tree.setCursor(Cursor.getDefaultCursor());
		}

		public void dragExit(DropTargetEvent event) {
		
		}
		
		
	public void transfertFile(File _f)
	{
		tree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		oldFile = _f;
		String path = null;
		if (explorer != null)				
				path = explorer.getAbsolutePath();
		else
				path = AbstractFileNode.getNameFromPath((String) oldFile.toString());
		if (action.equals("tree"))
		{
			path = findTreePath(oldFile);
		}

		tree.setToolTipText(path);

		newFile = new File (path+File.separator+oldFile.getName());
		
		ExplorerDirectoryFile explorerDirectoryFile = new ExplorerDirectoryFile(explorer,oldFile, newFile);
	}

	public String findTreePath(File _oldFile)
	{
	
		String stringPath = null;
		TreePath selPath = tree.getPathForLocation(point.x, point.y);
		GenericTreeNode node = (GenericTreeNode) selPath.getLastPathComponent();
		if (node == null) return null;
		adaptee.explorer.setNode((AbstractFileNode)node);

		TreePath path = new TreePath(node.getPath());
		adaptee.tree.getTree().expandPath(path);

		adaptee.tree.getTree().setSelectionPath(path);
		
		stringPath= adaptee.textField.getText();
		return stringPath;
		
	}

	



}
