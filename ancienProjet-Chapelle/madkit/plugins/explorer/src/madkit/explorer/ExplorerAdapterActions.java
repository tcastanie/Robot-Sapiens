/*
 * Created on 11 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.undo.CannotUndoException;



/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerAdapterActions extends ExplorerAbstactActions {
	


	/* (non-Javadoc)
	 * @see madkit.explorer.ExplorerAbstactActions#exec()
	 */
	void exec() throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException {

	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor arg0) throws UnsupportedFlavorException, IOException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void updateButtons()
	{
		System.out.println("Nous sommes dans updateButtons de ExploreradapterActions");
// update undo state
		adaptee.bUndo.setEnabled(adaptee.undoManager.canUndo());
		adaptee.jMenuEditUndo.setEnabled(adaptee.undoManager.canUndo());
// update redo state
		adaptee.bRedo.setEnabled(adaptee.undoManager.canRedo());
		adaptee.jMenuEditRedo.setEnabled(adaptee.undoManager.canRedo());	
	
	}
	
	public void undo() throws CannotUndoException
	{
		System.out.println (" adapter action undo ");
		super.undo();
	}

	public void redo() throws CannotUndoException
	{

		System.out.println (" adapter action redo ");
		super.redo();
	}


}


