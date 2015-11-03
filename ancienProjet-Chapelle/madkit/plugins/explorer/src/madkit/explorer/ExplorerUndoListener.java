/*
 * Created on 19 mai 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package madkit.explorer;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

/**
 * @author B_ROUIFED
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ExplorerUndoListener extends ExplorerAdapterActions implements UndoableEditListener{

	/* (non-Javadoc)
	 * @see javax.swing.event.UndoableEditListener#undoableEditHappened(javax.swing.event.UndoableEditEvent)
	 */
	public void undoableEditHappened(UndoableEditEvent e) {
		adaptee.undoManager.addEdit(e.getEdit());
		updateButtons();
	}

}
