/*
 * Created on 23 juin 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.awt.dnd.*;
import java.awt.event.MouseEvent;
import java.io.*;

import javax.swing.JComponent;
import javax.swing.JTree;

import madkit.TreeTools.GenericTreeNode;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerActionDrag extends JComponent

{  
	protected String path;
	protected ExplorerGUI adaptee;
	protected File file;
	GenericTreeNode node;
	MouseEvent e;
	DragSource dragSource;
	ExplorerActionDrop explorerActionDrop;
	
	Explorer explorer;
	FileIcon icon;

	
	public ExplorerActionDrag() 
	{
		explorerActionDrop = new ExplorerActionDrop();
	}
	
	public void execIcon(Explorer _explorer, FileIcon _icon)
	{
		ExplorerActions.actionToolBar = "Drag";
		this.icon = _icon;
		this.explorer = _explorer;
		file = new File (icon.name);
		transDrag();
		transIcon();


	}
	
	public void execTree(ExplorerGUI _adaptee)
	{
	 	ExplorerActions.actionToolBar ="Drag";
		this.adaptee = _adaptee;
		path  = adaptee.textField.getText();
		transDrag();
		transTree();
	}
	
	protected void transDrag()
	{
		dragSource = DragSource.getDefaultDragSource();
	}
	
	protected void transIcon()
	{
		dragSource.createDefaultDragGestureRecognizer
		   (icon,
			DnDConstants.ACTION_COPY_OR_MOVE, 
			new DragGestureListener()
			{
				public void dragGestureRecognized(DragGestureEvent event)
				{
					File[] files = {file};
					ExplorerTransferableFile selection = new ExplorerTransferableFile(files);

					if(selection != null)
					{
						event.startDrag(null,selection, new ExplorerDragSourceListener()); 
						explorerActionDrop.execIcon(explorer,icon);
					}else{
						System.out.println("selection is null");
					}

		
				}
			}
		   );
		 
		
	}
	
	protected void transTree()
	{
		JTree tree = adaptee.tree.getTree();	
		try
		{
			dragSource.createDefaultDragGestureRecognizer
			   (tree,
				DnDConstants.ACTION_COPY_OR_MOVE, 
				new DragGestureListener()
				{
					public void dragGestureRecognized(DragGestureEvent event)
					{
						file = new File (path);
						ExplorerActions.actionToolBar = "Drag";
						File[] files = {file};
						ExplorerTransferableFile selection = new ExplorerTransferableFile(files);
		
						if(selection != null)
						{
							event.startDrag(null,selection, new ExplorerDragSourceListener()); 
							explorerActionDrop.execTree(adaptee);
						}else{
							System.out.println("selection is null");
						}
		
				
					}
				}
			   );
		}catch (InvalidDnDOperationException ioe)
		{
		}catch(Exception e)
		{
		}

	}
	
	
	protected class ExplorerDragSourceListener implements DragSourceListener 
	{
	
		public void dragDropEnd(DragSourceDropEvent event)
		{
			if (event.getDropSuccess())
			{
				System.out.println("DragDropEnd  DRAG " +event.getDropAction());
			}
			else
			{
				System.out.println("DragDropEnd  No suceess " +	event.getDropAction());
			}
			
		}	
	
		public void dragEnter(DragSourceDragEvent dsde)
		{
//			System.out.println("Drag enter  DRAG");
		}
	
		public void dragExit(DragSourceEvent dse)
		{
//			System.out.println("Drag exit  DRAG");
		}
	
		public void dragOver(DragSourceDragEvent dsde)
		{	
//			System.out.println("Drag Over DRAG");
		}
	
		public void dropActionChanged(DragSourceDragEvent dsde)
		{
//			System.out.println("DropActionChanged DRAG");
		}
	}


}






